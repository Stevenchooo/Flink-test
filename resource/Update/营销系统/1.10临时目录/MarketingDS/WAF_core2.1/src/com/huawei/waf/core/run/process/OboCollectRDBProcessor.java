package com.huawei.waf.core.run.process;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.OboCollectRDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 遍历所有分库，查找符合条件的数据，每次只会在一个数据库中查询
 * 
 * @author  l00152046
 * @version  [VMALL OMS V100R001C01, 2014-5-27]
 * @since  [VMALL OMS]
 */
public class OboCollectRDBProcessor extends RDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();
    
    private static String[] dbServers = null;
    
    @Override
    public synchronized boolean init() {
    	if(dbServers != null) {
    		return true;
    	}
    	
        Collection<String> servers = DBUtil.getBalancer().getNodes();
        dbServers = servers.toArray(new String[servers.size()]);
		if(dbServers == null || dbServers.length <= 0) {
			return false;
		}
		
        return true;
    }
    
    //下面关于nextposition的函数可以重载
    //startId,dbNo
    protected String formatDbPosition(DbPosition dp) {
        return Integer.toString(dp.startId) + ',' +  Integer.toString(dp.dbNo);
    }
    
    //startId,dbNo
    protected DbPosition parseDbPosition(String dpStr) {
        int pos = dpStr.indexOf(',');
        if(pos <= 0) {
            return null;
        }
        
        DbPosition dp = new DbPosition();
        try {
            dp.startId = Integer.parseInt(dpStr.substring(0, pos));
            dp.dbNo = Integer.parseInt(dpStr.substring(pos + 1));
        } catch(Exception e) { //防止非法的更改nextPosition参数，比如改成非数字
            LOG.error("Fail to parase int parameter", e);
            return null;
        }
        
        return dp;
    }
    
    /**
     * 
     * @param dp
     * @param startId 开始的id，一般为表的自增id
     * @param pageSize 当前库返回符合条件的结果集的行数
     * @param maxCount 希望返回的总行数
     * @return
     */
    protected boolean updateDbPosition(DbPosition dp, int startId, int pageSize, int maxCount) {
        if(pageSize < maxCount) { //当前库已取完
            if(dp.dbNo + 1 >= dbServers.length) {
                return false;
            }
            dp.startId = 0;
            dp.dbNo++;
        } else {
            dp.startId = startId;
        }
        return true;
    }
    
    protected DBConnection getConnection(DbPosition dp, boolean transaction) {
        if(dp.dbNo >= dbServers.length || dp.dbNo < 0) {
            return null;
        }
        return DBUtil.getConnection(dbServers[dp.dbNo], transaction);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int process(MethodContext context) {
        MethodConfig methodConfig = context.getMethodConfig();
        OboCollectRDBProcessConfig procCfg = (OboCollectRDBProcessConfig)methodConfig.getProcessConfig();
        Map<String, Object> reqParameters = context.getParameters();
        DbPosition dbPosition = null;
        
        String paraStartPosition = procCfg.getStartPosition();
        String startPosition = JsonUtil.getAsStr(reqParameters, paraStartPosition, "");
        // 兼容调用方第一次调用：可以不传，或传""、"0"，这时会初始化数据
        if(Utils.isStrEmpty(startPosition) || startPosition.equals("0")) {
            dbPosition = new DbPosition();
        } else {
            if((dbPosition = parseDbPosition(startPosition)) == null) {
                LOG.error("Fail to get {} from request, value is {}", paraStartPosition, startPosition);
                return RetCode.WRONG_PARAMETER;
            }
        }
        
        //改变了参数的内容，startPosition变为下一次查询的开始id
        //当改变参数的情况，就不可以执行同步，因为如果同步，参数已不一致了
        reqParameters.put(paraStartPosition, dbPosition.startId);
        int pageSize = JsonUtil.getAsInt(reqParameters, procCfg.getPageSize());
        int listSize = 0;
        int startId = 0;
        int retCode = RetCode.OK;
        
        Map<String, Object> resp = context.getResults();
        DBConnection dbConn = getConnection(dbPosition, procCfg.useTransaction());
        
        traverse_rdb_process : {
            if (dbConn == null) {
                retCode = RetCode.WRONG_PARAMETER;
                LOG.error("Fail to get rdb connection by {},value={}, retCode={}", paraStartPosition, startPosition, retCode);
                break traverse_rdb_process;
            }
            
            try {
                retCode = process(context, dbConn);
                if(retCode != RetCode.OK) {
                    break traverse_rdb_process;
                }
                
                List<Object> list = JsonUtil.getAsList(resp, procCfg.getListName());
                
                if(list == null) {
                    retCode = RetCode.INTERNAL_ERROR;
                    LOG.error("There must be a {} response, retCode={}", procCfg.getListName(), retCode);
                    break traverse_rdb_process;
                }
                
                startId = JsonUtil.getAsInt(resp, procCfg.getStartIdName(), 0);
                listSize = list.size();
            } catch (Exception e) {
                retCode = RetCode.INTERNAL_ERROR;
                LOG.error("Fail to execute, retCode={}", retCode, e);
                break traverse_rdb_process;
            }
        }
        
        DBUtil.freeConnection(dbConn, retCode == RetCode.OK);
        
        /**
         * 当前库失败的情况，listSize为0，移到下一个库，
         * 成功时，根据startId与listSize判断是否移到下一个库 
         */
        if(updateDbPosition(dbPosition, startId, listSize, pageSize)){
            resp.put(procCfg.getNextPosition(), formatDbPosition(dbPosition));
        } else {
            resp.put(procCfg.getNextPosition(), "");
        }
        
        return RetCode.OK; //无论如何，返回OK
    }
    
    protected class DbPosition {
        int startId = 0; //开始的id，一般为表的自增id
        int dbNo = 0;
    }
}
