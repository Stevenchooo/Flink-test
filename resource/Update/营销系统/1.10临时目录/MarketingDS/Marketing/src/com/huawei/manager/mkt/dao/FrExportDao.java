package com.huawei.manager.mkt.dao;

import java.util.List;
import java.util.Map;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2016年3月8日]
 * @see  [相关类/方法]
 */
public final class FrExportDao
{
    /**
     * <默认构造函数>
     */
    private FrExportDao()
    {
    
    }
    
    /**
     * <判断广告位着陆链接信息是否存在>
     * @param context    广告位ID
     * @param dbConn     数据库链接
     * @return         广告位着陆链接信息是否存在
     * @see [类、类#方法、类#成员]
     */
    public static int setContext(MethodContext context, DBConnection dbConn)
    {
        Map<String, Object> reqParams = context.getParameters();
        String account = context.getAccount();
        int mktinfoName = Integer.parseInt(JsonUtil.getAsStr(reqParams, "mktinfoName"));
        int reportPlatform = Integer.parseInt(JsonUtil.getAsStr(reqParams, "reportPlatform"));
        int reportPersonGroup = Integer.parseInt(JsonUtil.getAsStr(reqParams, "reportPersonGroup"));
        String pPtd = JsonUtil.getAsStr(reqParams, "pPt_d");
        int pAreatype = Integer.parseInt(JsonUtil.getAsStr(reqParams, "pArea_type"));
        int reportProvince = Integer.parseInt(JsonUtil.getAsStr(reqParams, "reportProvince"));
        int reportSelectType = Integer.parseInt(JsonUtil.getAsStr(reqParams, "reportSelectType"));
        int reportSelectMax = Integer.parseInt(JsonUtil.getAsStr(reqParams, "reportSelectMax"));
        
        String exeSql = "call sp_getFrequencyStat_Exp(?,?,?,?,?,?,?,?,?,?)";
        List<Map<String, Object>> ccResultList = DBUtil.query(dbConn,
            exeSql,
            true,
            new Object[] {account, mktinfoName, reportPlatform, reportPersonGroup, pPtd, pAreatype, reportProvince,
                reportSelectType, reportSelectMax});
        if (ccResultList != null)
        {
            context.setResult("result", ccResultList);
            context.setResult("total", ccResultList.size());
        }
        
        return RetCode.OK;
    }
}
