package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetWebNameByAdId extends AuthRDBProcessor {
	/**
     * 根据查询参数， 查找结果，分配结果集
     * @param context 上下文
     * @param dbConn 数据库连接
     * @return 结果集
     */
    public int process(MethodContext context, DBConnection dbConn){
    	Map<String, Object> reqParams = context.getParameters();
    	String activityIds =JsonUtil.getAsStr(reqParams, "adName");
    	String activityid[] = activityIds.split(",");
    	String exeSqlAct = "select  distinct media_id as id, media_name as name from dim_mkt_base_info_ds  where activtiy_id in ( ";
    	for(int i=0;i<activityid.length;i++){
    		 if (i < activityid.length - 1)
             {
    			 exeSqlAct += "\'" + activityid[i] + "\',";
             }
             else
             {
            	 exeSqlAct += "\'" + activityid[i] + "\')";
             }
    	}
    	exeSqlAct+= " group by media_id,media_name order by convert(media_name USING gbk) COLLATE gbk_chinese_ci  ;";
    	List<Map<String, Object>> list = DBUtil.query(dbConn, exeSqlAct, false, new Object[] {});
    	context.setResult("results", list);
    	return RetCode.OK;
    }
}
