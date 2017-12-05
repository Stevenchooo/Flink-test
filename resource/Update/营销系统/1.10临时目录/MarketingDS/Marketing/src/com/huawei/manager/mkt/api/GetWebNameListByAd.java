package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-19]
 * @see  [相关类/方法]
 *  
 */
public class GetWebNameListByAd extends AuthRDBProcessor
{
    
    /**
     * 根据查询参数， 查找结果，分配结果集
     * @param context 上下文
     * @param dbConn 数据库连接
     * @return 结果集
     */
    public int process(MethodContext context, DBConnection dbConn)
    {
        Map<String, Object> reqParams = context.getParameters();
        String activityName = JsonUtil.getAsStr(reqParams, "adName");
        String activityNameA[] = activityName.split(",");
        String exeSql = "select  distinct media_id as id, media_name as name from dim_mkt_base_info_ds";
        String exeSqlAct = "select  distinct activtiy_id from dim_mkt_base_info_ds";
        if (!"全部".equals(activityNameA[0]))
        {
            String tmpSql = " where activity_name in (";
            for (int index = 0; index < activityNameA.length; index++)
            {
                if (index < activityNameA.length - 1)
                {
                    tmpSql += "\'" + activityNameA[index] + "\',";
                }
                else
                {
                    tmpSql += "\'" + activityNameA[index] + "\')";
                }
                
            }
            exeSql += tmpSql;
            exeSqlAct += tmpSql;
        }
        exeSql += " group by media_id,media_name order by convert(media_name USING gbk) COLLATE gbk_chinese_ci  ;";
        
        List<Map<String, Object>> list = DBUtil.query(dbConn, exeSql, false, new Object[] {});
        List<Map<String, Object>> listAct = DBUtil.query(dbConn, exeSqlAct, false, new Object[] {});
                
        if ( null!= list && null!=listAct)
        {
            List<Integer> aidList = new ArrayList<Integer>();
            for (Map<String, Object> item : listAct)
            {
                int aid = Integer.parseInt((String)item.get("activtiy_id"));
                if (!aidList.contains(aid))
                {
                    aidList.add(aid);
                }
            }
            context.setResult("results", list);
            context.setResult("total", list.size());
            context.setResult("aidList", aidList);
        }
        
        
        return RetCode.OK;
    }
}
