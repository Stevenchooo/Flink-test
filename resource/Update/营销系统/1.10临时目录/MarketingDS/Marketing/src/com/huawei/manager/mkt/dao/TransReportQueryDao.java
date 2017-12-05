package com.huawei.manager.mkt.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2016-3-1]
 * @see  [相关类/方法]
 */
public final class TransReportQueryDao
{
    
    /**
     * <默认构造函数>
     */
    private TransReportQueryDao()
    {
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param context 数据信息 
     * @param dbConn  数据库链接
     * @param to      开始
     * @param from    结束
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int setContext(MethodContext context, DBConnection dbConn, int from, int to)
    {
        Map<String, Object> reqParams = context.getParameters();
        String activityId = JsonUtil.getAsStr(reqParams, "mktId");
        String activityIdA[] = activityId.split(",");
        String adId = JsonUtil.getAsStr(reqParams, "adInfoWebName");
        String adIdA[] = adId.split(",");
        
        if (activityIdA.length <= 0 || "".equals(activityIdA[0]))
        {
            return 0;
        }
        
        if (adIdA.length <= 0 || "".equals(adIdA[0]))
        {
            return 0;
        }
        
        int adInfoPort = Integer.parseInt(JsonUtil.getAsStr(reqParams, "adInfoPort"));
        String adInfoPlatform = JsonUtil.getAsStr(reqParams, "adInfoPlatform");
        String inputUser = JsonUtil.getAsStr(reqParams, "inputUser");
        String cid = JsonUtil.getAsStr(reqParams, "cid");
        String account = context.getAccount();
        String adqueryDateBeginDay = JsonUtil.getAsStr(reqParams, "adqueryDateBeginDay");
        String adqueryDateEndDay = JsonUtil.getAsStr(reqParams, "adqueryDateEndDay");
        String adquerySid = JsonUtil.getAsStr(reqParams, "adquerySid");
        int adType = Integer.parseInt(JsonUtil.getAsStr(reqParams, "adType"));
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        
        if(activityIdA.length == 1 && "-1".equals(activityIdA[0]) && "".equals(cid) && "".equals(adquerySid)) 
        {
             return 50001;
        }
        for (int i = 0; i < activityIdA.length; i++)
        {
            int tactId = Integer.parseInt(activityIdA[i]);
            for (int j = 0; j < adIdA.length; j++)
            {
                int tadId = Integer.parseInt(adIdA[j]);
                List<Map<String, Object>> ccResultList = DBUtil.query(dbConn,
                    "call sp_mktChinaTransformQuery(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    true,
                    new Object[] {account, tactId, tadId, adInfoPort, adInfoPlatform, inputUser, cid,
                        adqueryDateBeginDay, adqueryDateEndDay, adquerySid, adType, from, to});
                if (null != ccResultList && !ccResultList.isEmpty())
                {
                    result.addAll(ccResultList);
                }
            }
        }
        context.setResult("result", result);
        context.setResult("total", result.size());
        return 0;
    }
}
