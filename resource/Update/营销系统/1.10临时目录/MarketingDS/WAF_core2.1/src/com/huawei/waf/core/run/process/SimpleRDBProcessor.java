package com.huawei.waf.core.run.process;

import java.sql.*;
import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.process.SimpleRDBProcessConfig;
import com.huawei.waf.core.config.method.response.RDBResponseConfig;
import com.huawei.waf.core.config.method.response.ResultSetConfig;
import com.huawei.waf.core.run.MethodContext;

import static com.huawei.waf.protocol.RetCode.*;

/**
 * @author flyinmind
 * 不能执行存储过程的RDB操作，比如sqlite等数据库，
 * mysql等，如果不用存储过程，也可以用这个类型
 */
public class SimpleRDBProcessor extends RDBProcessor {
    private static final Logger LOG = LogUtil.getInstance();
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
    protected int getResults(MethodContext context, PreparedStatement statement,
            Map<String, Object> respData) throws SQLException {
        MethodConfig mc = context.getMethodConfig();
        RDBResponseConfig responseConfig = (RDBResponseConfig)mc.getResponseConfig();
        int idx = 1;

        ResultSetConfig[] resultSetConfigs = responseConfig.getResultSetConfigs();
        if (resultSetConfigs.length <= 0) {
            LOG.debug("No resultSet config of {}", mc.getName());
            return OK;
        }

        ResultSet resultSet = null;
        ResultSetMetaData meta;

        int columnNum;
        int resultSetNum = 0;

        for (ResultSetConfig resultCfg : resultSetConfigs) {
            resultSet = statement.getResultSet(); //结果集一定可以getResultSet，不必从cursor中获取
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
    protected int prepareStatement(PreparedStatement statement, MethodContext context) throws SQLException {
        MethodConfig mc = context.getMethodConfig();
        
        int index = 0;

        for (ParameterInfo parameterInfo : mc.getRequestConfig().getParameters()) {
            //设为0的情况，则忽略这个参数；大于0表示修改index；默认小于0，按顺序处理
            if ((index = parameterInfo.getIndex()) <= 0) { //default is -1, if not set
                continue;
            }

            parameterInfo.setToStatement(statement, index, context);
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
    protected int beforeProcess(MethodContext context, DBConnection conn, PreparedStatement statement) throws SQLException {
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
        PreparedStatement statement = null;
        MethodConfig mc = context.getMethodConfig();
        SimpleRDBProcessConfig config = (SimpleRDBProcessConfig) mc.getProcessConfig();
        String sql = config.getSQL();
        Connection conn = dbConn.getConnection();
        int retCode = OK;
        
        try {
            process: {
                if ((statement = conn.prepareStatement(sql)) == null) {
                    retCode = INTERNAL_ERROR;
                    LOG.error("Fail to prepare sql statement {}", sql);
                    break process;
                }
    
                if ((retCode = beforeProcess(context, dbConn, statement)) != OK) {
                    LOG.error("Fail to call beforeProcess in method {}", mc.getName());
                    break process;
                }
                
                retCode = prepareStatement(statement, context);
                if (retCode != OK) {
                    LOG.error("Fail to prepare statement in method {}", mc.getName());
                    break process;
                }
                
                if(mc.isCanLog() && securityAide.isCanPrintSql()) {
                    DBUtil.printStatement(statement, LOG);
                }
                
                statement.execute();
                if ((retCode = getResults(context, statement, context.getResults())) != OK) {
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
