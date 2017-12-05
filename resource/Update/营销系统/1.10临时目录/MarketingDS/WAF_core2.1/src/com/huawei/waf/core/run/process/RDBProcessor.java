package com.huawei.waf.core.run.process;

import java.sql.*;
import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.config.method.response.RDBResponseConfig;
import com.huawei.waf.core.config.method.response.ResultSetConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.security.AbstractSecurityAide;

import static com.huawei.waf.protocol.RetCode.*;

public class RDBProcessor extends SelfRDBProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    protected static AbstractSecurityAide securityAide;
    
    @Override
    public boolean init() {
    	securityAide = AbstractInitializer.getSecurityAide();
        return super.init();
    }
    
    protected Map<String, Object> getMapRow(ResultSet result, ResultSetMetaData meta, int columnNum,
            Map<String, ParameterInfo> segments) throws SQLException {
        String columnName;
        ParameterInfo parameterInfo;
        Map<String, Object> respData = new HashMap<String, Object>();

        for (int i = 1; i <= columnNum; i++) {
            columnName = meta.getColumnLabel(i).toUpperCase();
            if ((parameterInfo = segments.get(columnName)) == null) {
                LOG.warn("An unknown segment {}", columnName);
                continue; // 不认识的字段直接忽略
            }
            respData.put(parameterInfo.getName(), parameterInfo.getValue(result, i));
        }
        return respData;
    }

    /**
     * 不依赖数据库返回结果中的列名称，而是从1到n的方式获取字段，
     * 这样无论什么数据库的driver都可以支持
     * @param result
     * @param meta
     * @param columnNum
     * @param segments
     * @return
     * @throws SQLException
     */
    protected Object[] getListRow(ResultSet result, ResultSetMetaData meta, int columnNum,
            Map<String, ParameterInfo> segments) throws SQLException {
        String columnName;
        ParameterInfo parameterInfo;
        Object[] respData = new Object[columnNum];

        for (int i = 1, col = 0; i <= columnNum; i++, col++) {
            columnName = meta.getColumnLabel(i).toUpperCase();
            if ((parameterInfo = segments.get(columnName)) == null) {
                LOG.warn("Unknow column {} in result-set", columnName);
                continue; // 不认识的字段直接忽略
            }
            respData[col] = parameterInfo.getValue(result, i);
        }
        
        return respData;
    }

    /**
     * 从执行结果集中获取数据
     * @param context
     * @param statement
     * @param respData
     * @param resultSetRef
     * @param isNeedResult 是否为存储过程，需要返回执行返回码
     * @return
     * @throws SQLException
     */
    protected int getResults(MethodContext context, CallableStatement statement,
            Map<String, Object> respData, int resultSetRef, boolean isNeedResult) throws SQLException {
        MethodConfig mc = context.getMethodConfig();
        RDBResponseConfig responseConfig = (RDBResponseConfig)mc.getResponseConfig();
        RDBProcessConfig cfg = (RDBProcessConfig) mc.getProcessConfig();
        int idx = 1;

        if (isNeedResult) {
            int retCode = statement.getInt(idx++);
            if (retCode != OK) {
                LOG.error("{} return from storedprocedure in method {}", retCode, mc.getName());
                return retCode;
            }
        }

        ResultSetConfig[] resultSetConfigs = responseConfig.getResultSetConfigs();
        if (resultSetConfigs.length <= 0) {
            LOG.debug("No resultSet config of {}", mc.getName());
            return OK;
        }

        ResultSet resultSet = null;
        ResultSetMetaData meta;

        int columnNum;
        int resultSetNum = 0;
        boolean resultSetInCursor = cfg.isStoredProcedure() && DBConnection.isResultSetInCursor(resultSetRef);

        for (ResultSetConfig resultCfg : resultSetConfigs) {
            if(resultSetInCursor) {
                //oracle/postgresql对存储过程结果集的处理不同于mysql/sqlserver/sybase等
                resultSet = (ResultSet)statement.getObject(idx++);
            } else {
                resultSet = statement.getResultSet();
            }
            
            if (resultSet == null) {
                if(resultCfg.isOptional()) { //结果集是可选的
                    continue;
                }
                LOG.error("Can't get resultSet {} in method {}.{}", (idx-1), mc.getName(), resultCfg.getName());
                return INTERNAL_ERROR;
            }

            resultSetNum++;
            meta = resultSet.getMetaData();
            columnNum = meta.getColumnCount();
            if (resultCfg.isMulti()) {
                List<Object> list = new ArrayList<Object>();

                while (resultSet.next()) {
                    if(resultCfg.isArray()) {
                        list.add(getListRow(resultSet, meta, columnNum, resultCfg.getSegments()));
                    } else {
                        list.add(getMapRow(resultSet, meta, columnNum, resultCfg.getSegments()));
                    }
                }
                respData.put(resultCfg.getName(), list);
            } else if (resultSet.next()) {
                Map<String, Object> row = getMapRow(resultSet, meta, columnNum, resultCfg.getSegments());
                if(!resultCfg.isMerge()) {
                    respData.put(resultCfg.getName(), row);
                } else {
                    //将行信息合并到第一层信息中
                    respData.putAll(row);
                }

                if (LOG.isWarnEnabled()) {
                    if(resultSet.next()) {
                        LOG.warn("There are more rows in result, maybe you should set 'multi' irrelevantly in {}", resultCfg.getName());
                    }
                }
            }
            resultSet.close();
            
            if(resultSetInCursor) { //结果集在out cursor中，statement.getObject获得结果集
                continue;
            }
            
            if(!statement.getMoreResults()) {
                if(resultSetNum < resultSetConfigs.length) {
                    resultCfg = resultSetConfigs[resultSetNum];
                    if(!resultCfg.isOptional()) { //结果集不是可选的
                        LOG.error("Fail to get left {} result-set from statement", resultSetConfigs.length - resultSetNum);
                    }
                }
                break; //必须调用，否则结果集始终是第一个
            }
        }
        
        return OK;
    }

    /**
     * 处理sql的参数，将请求参数设置到sql的问号中，顺序以配置参数的顺序为准
     * @param statement
     * @param mc
     * @param context
     * @param resultSetRef
     * @return
     * @throws SQLException
     */
    protected int prepareStatement(CallableStatement statement, MethodContext context, int resultSetRef) throws SQLException {
        MethodConfig mc = context.getMethodConfig();
        RDBProcessConfig processConfig = (RDBProcessConfig) mc.getProcessConfig();
        
        int idx = 0;
        int sqlIndex = 0;

        // 当script为存储过程时，约定第一个参数为out型，返回存储过程的返回码
        if (processConfig.isNeedResult()) {
            statement.registerOutParameter(++idx, Types.INTEGER);
            /**
             * oracle、postgresql等数据库，结果集只能通过cursor的方式返回
             * cursor是放在存储过程的参数中的，要求第一个out是retCode，
             * 从第二个开始，依次为0-n个结果集。
             * 可以在system.cfg的db配置中指定resultSetInParameter为true
             * 
             * 结果集一定是从第二个开始，有几个结果集就有几个out的cursor
             */
            if(DBConnection.isResultSetInCursor(resultSetRef)) {
                RDBResponseConfig respConfig = (RDBResponseConfig)mc.getResponseConfig();
                int size = respConfig.getResultSetConfigs().length;
                for(int i = 0; i < size; i++) {
                    statement.registerOutParameter(++idx, resultSetRef);
                }
            }
        }

        for (ParameterInfo parameterInfo : mc.getRequestConfig().getParameters()) {
            //设为<=0的情况，则忽略这个参数；大于0表示修改index
            if ((sqlIndex = parameterInfo.getIndex()) <= 0) { //default >= 1, if not set
                continue;
            }
            parameterInfo.setToStatement(statement, idx + sqlIndex, context);
        }

        return OK;
    }

    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情
     * 比如给statement添加不在parameters中的其他参数
     * @param context
     * @param conn
     * @param statement
     * @param isResInCur 结果集是否在out参数中
     * @return
     */
    protected int beforeProcess(MethodContext context, DBConnection conn, CallableStatement statement) throws SQLException {
        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * @param context
     * @param conn
     * @return
     */
    protected int afterProcess(MethodContext context, DBConnection conn) throws SQLException {
        return OK;
    }

    /**
     * 可以重载此函数，实现不同的数据库处理
     * @param context
     * @param dbConn
     * @return
     */
    @Override
    public int process(MethodContext context, DBUtil.DBConnection dbConn) {
        CallableStatement statement = null;
        MethodConfig mc = context.getMethodConfig();
        RDBProcessConfig config = (RDBProcessConfig) mc.getProcessConfig();
        String sql = config.getSQL();
        Connection conn = dbConn.getConnection();
        int resultSetRef = dbConn.getResultSetRef();
        int retCode = OK;
        
        try {
            process: {
                if ((statement = conn.prepareCall(sql)) == null) {
                    retCode = INTERNAL_ERROR;
                    LOG.error("Fail to prepare sql statement {}", sql);
                    break process;
                }
    
                if ((retCode = beforeProcess(context, dbConn, statement)) != OK) {
                    LOG.error("Fail to call beforeProcess in method {}", mc.getName());
                    break process;
                }
                
                retCode = prepareStatement(statement, context, resultSetRef);
                if (retCode != OK) {
                    LOG.error("Fail to prepare statement in method {}", mc.getName());
                    break process;
                }
                
                if(mc.isCanLog() && securityAide.isCanPrintSql()) {
                    DBUtil.printStatement(statement, LOG);
                }
                
                statement.execute();
                if ((retCode = getResults(context, statement, context.getResults(), resultSetRef, config.isNeedResult())) != OK) {
                    LOG.info("Fail to get results in method {}, retCode={}", mc.getName(), retCode);
                    break process;
                }
                
                if((retCode = afterProcess(context, dbConn)) != OK) {
                    LOG.warn("Fail to call afterProcess in method {}", mc.getName());
                    break process;
                }
            }
        } catch (SQLException e) {
            LOG.error("Fail to execute {}", sql, e);
            retCode = INTERNAL_ERROR;
		} finally {
            DBUtil.closeStatement(statement);
        }
        
        return retCode;
    }
}
