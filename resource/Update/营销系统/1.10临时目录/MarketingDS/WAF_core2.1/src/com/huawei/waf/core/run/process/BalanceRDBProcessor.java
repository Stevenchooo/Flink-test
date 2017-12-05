package com.huawei.waf.core.run.process;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.BalanceRDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <需要分库的数据库处理器>
 * 
 * @author  l00152046
 * 默认根据用户账号分库，如果配置了balanceKey，则根据balanceKey指定名称的参数来分库
 * 均衡策略可以在BalanceRDBProcessConfig中设置
 */
public class BalanceRDBProcessor extends RDBProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 字段值重复，入库失败错误码
     */
    public static final int DUPLICATE_SQL_CODE = 1062;
    
    //---------------------------------------------------------------------
    protected DBConnection getConnection(MethodContext context) {
    	MethodConfig methodConfig = context.getMethodConfig();
        BalanceRDBProcessConfig processConfig = (BalanceRDBProcessConfig)methodConfig.getProcessConfig();
        DBConnection dbConn = null;
        
        String poolName = processConfig.getDataSource(context);
        if(Utils.isStrEmpty(poolName)) {
            LOG.error("Fail to get data-source from request, balance key is {}", processConfig.getBalanceKey());
            return null;
        }
        
        dbConn = DBUtil.getConnection(poolName, processConfig.useTransaction());
        if (dbConn == null) {//取不到连接，失败，也不能随便弄个链接，因为数据可能会乱掉
        	LOG.error("Fail to get rdb connection, poolName:{}", poolName);
        }
        
        return dbConn;
    }
    
    @Override
    public int process(MethodContext context) {
        DBConnection dbConn = getConnection(context);
        if (dbConn == null) {
            LOG.error("Fail to get rdb connection in method {}", context.getMethodConfig().getName());
            return context.setResult(RetCode.TOO_BUSY);
        }
        
        int retCode = RetCode.OK;
        try {
            retCode = process(context, dbConn);
        } catch (Exception e) {
            LOG.error("Fail to execute {}", context.getMethodConfig().getName(), e);
            retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.freeConnection(dbConn, retCode == RetCode.OK);
        }
        
        return retCode;
    }
}
