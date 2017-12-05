package com.huawei.waf.core.run.process;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.process.SelfRDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * @author l00152046
 * 只是帮助获取数据库连接，不自动执行数据库操作
 */
public class SelfRDBProcessor extends AbstractProcessor {
    private static final Logger LOG = LogUtil.getInstance();

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }

    /**
     * 可以重载此函数，实现不同的数据库处理
     * @param context
     * @param dbConn
     * @return
     */
    public int process(MethodContext context, DBUtil.DBConnection dbConn) {
        return RetCode.OK;
    }
    
	@Override
    public int process(MethodContext context) {
        MethodConfig mc = context.getMethodConfig();
        SelfRDBProcessConfig config = (SelfRDBProcessConfig) mc.getProcessConfig();
        
        DBUtil.DBConnection dbConn = DBUtil.getConnection(config.getDataSource(context), config.useTransaction());
        if (dbConn == null) {
            LOG.error("Fail to get rdb connection");
            return context.setResult(RetCode.TOO_BUSY);
        }
        
        int retCode = RetCode.OK;
        try {
            retCode = process(context, dbConn);
        } catch (Exception e) {
            LOG.error("Fail to execute {}", mc.getName(), e);
            retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.freeConnection(dbConn, retCode == RetCode.OK);
        }
        
        return retCode;
    }

    /* (non-Javadoc)
     * @see com.huawei.waf.core.facade.AbstractProcessor#afterAll(com.huawei.waf.core.run.MethodContext)
     */
    @Override
    public int afterAll(MethodContext context) {
        return RetCode.OK;
    }
    
    /**
     * <批量执行SQL语句>
     * 
     * @param conn {@link Connection}：数据库连接对象
     * @param dataList List：对象列表
     * @param parametersCfg List：参数列表
     * @param sql String：SQL语句
     * @param batchNum 每个批量提交多少数据
     * @return int：0表示成功，非0表示失败
     */
    public static final int batchExecSQL(MethodContext context, Connection conn, List<Object> dataList,
        ParameterInfo[] parametersCfg, String sql, int batchNum) {
        CallableStatement statement = null;
        int index;
        
        try {
            int num = 0;
            int total = dataList.size();
            statement = conn.prepareCall(sql);
            for (int i = 0; i < total; i++) {
                Object o = dataList.get(i);
                if (!(o instanceof Map<?, ?>)) {
                    LOG.error("Not a map in list");
                    continue;
                }
                
                for (ParameterInfo parameterInfo : parametersCfg) {
                    //如果多个参数，其中一个参数定义了index，那么可能会乱掉
                    index = parameterInfo.getIndex();
                    if (index <= 0) { //配置为<=0的情况，参数忽略
                        continue;
                    }
                    
                    parameterInfo.setToStatement(statement, index, context);
                }
                statement.addBatch();
                num++;
                
                if(num >= batchNum) {
                    statement.executeBatch();
                    statement.clearBatch();
                    num = 0;
                }
            }
            
            if(num > 0) {
                statement.executeBatch();
            }
            
            return RetCode.OK;
        } catch (Exception e) {
            LOG.error("Fail to execute:{} ", sql, e);
            return RetCode.DB_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
        }
    }
}
