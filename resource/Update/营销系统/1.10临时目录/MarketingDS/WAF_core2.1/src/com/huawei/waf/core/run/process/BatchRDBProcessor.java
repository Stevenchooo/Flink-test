package com.huawei.waf.core.run.process;

import java.sql.*;
import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.request.BatchRequester;
import com.huawei.waf.protocol.RetCode;

public class BatchRDBProcessor extends RDBProcessor {
    private static final String RESULT_LIST = "resultList";
    private static final Logger LOG = LogUtil.getInstance();

    @Override
    public int process(MethodContext context) {
        MethodConfig mc = context.getMethodConfig();
        RDBProcessConfig config = (RDBProcessConfig)mc.getProcessConfig();
        DBUtil.DBConnection dbConn = null;

        dbConn = DBUtil.getConnection(config.getDataSource(context), config.useTransaction());
        if (dbConn == null) {
            LOG.error("Fail to get rdb connection");
            return context.setResult(RetCode.TOO_BUSY, "Fail to get db connection");
        }
        
        int retCode = RetCode.OK;
        CallableStatement statement = null;
        String sql = config.getSQL();
        
        try {
            Connection conn = dbConn.getConnection();
            if ((statement = conn.prepareCall(sql)) == null) {
                return RetCode.INTERNAL_ERROR;
            }
            
            if(config.isNeedResult()) {
                retCode = processOneByOne(context, mc, dbConn, statement);
            } else {
                retCode = processBatch(context, mc, dbConn, statement);
            }
        } catch (Exception e) {
            LOG.error("Fail to execute {}", sql, e);
            retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
            DBUtil.freeConnection(dbConn, retCode == RetCode.OK);
        }
        return context.setResult(retCode);
    }
    
    @SuppressWarnings("unchecked")
    protected int processOneByOne(MethodContext context, MethodConfig methodCfg, DBUtil.DBConnection dbConn, CallableStatement statement) throws SQLException {
        int retCode = RetCode.OK;
        /*
         * lines不可能为空，因为在BatchDBRequester中已检查过，
         * 如果无list参数，则返回参数错误
         */
        List<Object> lines = JsonUtil.getAsList(context.getParameters(), BatchRequester.BATCH_ARRAY_NAME);
        Map<String, Object> respData = null;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
        int resultSetRef = dbConn.getResultSetRef();
        
        int lineNo = 0;
        int errorNum = 0;
        RDBProcessConfig config = (RDBProcessConfig)methodCfg.getProcessConfig();
        
        for(Object line : lines) {
            /**
             * 强行改变了context中记录的参数，因为需要用到context中的getParameter
             * 如果不改变，将无法逐行获取参数
             */
            context.setParameters((Map<String, Object>)line);
            respData = new HashMap<String, Object>();
            try {
                if((retCode = beforeProcess(context, dbConn, statement)) != RetCode.OK) {
                    LOG.error("Fail to call beforeProcess at line {}", lineNo);
                    continue;
                }
                
                if ((retCode = prepareStatement(statement, context, resultSetRef)) != RetCode.OK) {
                    LOG.error("Fail to call prepareStatement at line {}", lineNo);
                    continue;
                }
                
                statement.execute();
                if ((retCode = getResults(context, statement, respData, resultSetRef, config.isNeedResult())) != RetCode.OK) {
                    LOG.error("Fail to call getResults at line {}", lineNo);
                    continue;
                }
                
                if((retCode = afterProcess(context, dbConn)) != RetCode.OK) {
                    LOG.error("Fail to call afterProcess at line {}", lineNo);
                    continue;
                }
                lineNo++;
            } catch(Exception e) {
                LOG.error("Fail to execute batch request at line {}", lineNo, e);
                retCode = RetCode.INTERNAL_ERROR;
            } finally {
                if(respData.size() <= 0) {
                    respData.put(methodCfg.getResultCodeName(), retCode);
                }
                resultList.add(respData);
            }
            
            if(retCode != RetCode.OK) {
                errorNum++;
            }
        }
        
        //总是在resultList中返回每一行执行的结果
        context.setResult(RESULT_LIST, resultList);
        if(errorNum > 0) {
            return context.setResult(RetCode.API_ERROR, "Fail to handle line " + lineNo);
        }
        
        return RetCode.OK;
    }
    
    /** 
     * 批量执行，不能用于查询
     * @param context
     * @param methodCfg
     * @param dbConn
     * @param statement
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    protected int processBatch(MethodContext context, MethodConfig methodCfg, DBUtil.DBConnection dbConn, CallableStatement statement) throws SQLException {
        int retCode = RetCode.OK;
        int resultSetRef = dbConn.getResultSetRef();
        //lines不可能为空，因为如果为空，参数校验时就已返回失败
        List<Object> lines = JsonUtil.getAsList(context.getParameters(), BatchRequester.BATCH_ARRAY_NAME);
        
        int lineNo = 0;
        try {
            for(Object line : lines) {
                context.setParameters((Map<String, Object>)line);
                
                if((retCode = beforeProcess(context, dbConn, statement)) != RetCode.OK) {
                    LOG.error("Fail to call beforeProcess at line {}", lineNo);
                    break;
                }
                
                if ((retCode = prepareStatement(statement, context, resultSetRef)) != RetCode.OK) {
                    LOG.error("Fail to prepareStatement line {}", lineNo);
                    break;
                }
                /* 不可以调用afterProcess，因为尚未执行
                if((retCode = afterProcess(context, conn, resultSetRef)) != RetCode.OK) {
                    LOG.error("Fail to call afterProcess at line {}", lineNo);
                    break;
                } */
                
                statement.addBatch(); //批量处理
                lineNo++;
            }
            int[] resultList = statement.executeBatch();
            context.setResult(RESULT_LIST, resultList);
        } catch(Exception e) {
            LOG.error("Fail to execute request {} at line {}", lineNo, e);
            retCode = RetCode.INTERNAL_ERROR;
        }

        if(retCode != RetCode.OK) {
            context.setResult(retCode, "Fail to handle line " + lineNo);
        }
        
        return retCode;
    }
}
