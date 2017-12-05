package com.huawei.manager.mkt.dao;

import java.util.List;
import java.util.Map;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;

public final class TransPicReportDao {
	/**
	 * <默认构造函数>
	 */
	private TransPicReportDao(){
		
	}
	/**
     * 转化漏斗
     * @param conn   数据库执行对象
     * @param account 用户账号
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
	public static List<Map<String, Object>> getTotalResult(MethodContext context,DBConnection conn){
		Map<String, Object> reqParams = context.getParameters();
    	String activityIds =JsonUtil.getAsStr(reqParams, "pActivtiy_id");   //营销活动id
    	String adTypes=JsonUtil.getAsStr(reqParams,"pAd_type");    //广告类型
    	String materialTypes = JsonUtil.getAsStr(reqParams, "pMaterial_type"); //媒体类型
    	String mediaTypes = JsonUtil.getAsStr(reqParams,"pMedia_type");//端口
    	String landingPlats = JsonUtil.getAsStr(reqParams, "pLandingPlat"); //着陆平台
    	String beginTime = JsonUtil.getAsStr(reqParams, "pBegin_time");//投放周期开始时间
    	String endTime = JsonUtil.getAsStr(reqParams, "pEnd_time");//投放周期结束时间
    	String activityId[]=activityIds.split(",");
    	String adType[]= adTypes.split(",");
    	String mediaType[]=mediaTypes.split(",");
    	String materialType[]=materialTypes.split(",");
    	String landingPlat[]=landingPlats.split(",");
    	String tmpSql="where activtiy_id IN(";
    	if(!activityIds.isEmpty()){
    		for(int index = 0; index < activityId.length; index++){
    			if (index < activityId.length - 1){
    				tmpSql+="\'"+activityId[index]+"\',";
    			}else{
    				tmpSql+="\'"+activityId[index]+"\') ";
    			}
    		}
    	}
    	if(!adTypes.isEmpty()){
    		tmpSql+=" AND material_type in(";
    		for(int index = 0; index < adType.length; index++){
    			if (index < adType.length - 1){
    				tmpSql+="\'"+adType[index]+"\',";
    			}else{
    				tmpSql+="\'"+adType[index]+"\') ";
    			}
    		}
    	}
    	if(!materialTypes.isEmpty()){
    		tmpSql+=" AND media_name in(";
    		for(int index = 0; index < materialType.length; index++){
    			if (index < materialType.length - 1){
    				tmpSql+="\'"+materialType[index]+"\',";
    			}else{
    				tmpSql+="\'"+materialType[index]+"\')";
    			}
    		}
    	}
    	if(!landingPlats.isEmpty()){
    		tmpSql+=" AND landing_plate in(";
    		for(int index = 0; index < landingPlat.length; index++){
    			if (index < landingPlat.length - 1){
    				tmpSql+="\'"+landingPlat[index]+"\',";
    			}else{
    				tmpSql+="\'"+landingPlat[index]+"\') ";
    			}
    		}
    	}
    	if(!mediaTypes.isEmpty()){
    		tmpSql+=" AND media_type in(";
    		for(int index = 0; index < mediaType.length; index++){
    			if (index < mediaType.length - 1){
    				tmpSql+="\'"+mediaType[index]+"\',";
    			}else{
    				tmpSql+="\'"+mediaType[index]+"\') ";
    			}
    		}
    	}
    	String timeSql1="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\')";
		String sql="select "+
				"t1.media_id,"+
				"SUM(t2.bg_uv) AS bg_uv,"+
				"SUM(t2.dj_uv) AS dj_uv,"+
				"SUM(t2.landing_uv) AS landing_uv,"+
				"SUM(t2.bespeak_nums) AS bespeak_nums,"+
				"SUM(t3.order_count) AS order_count,"+
				"SUM(t3.real_order_count) AS real_order_count "+
				" from "+
				" (SELECT activtiy_id,sid,cid,media_id FROM dim_mkt_base_info_ds ";
			sql+=tmpSql;
		    sql+=" ) t1"+
				" LEFT OUTER JOIN "+
				" (SELECT sid,SUM(bg_uv) AS bg_uv,SUM(dj_uv) AS dj_uv,SUM(landing_uv) AS landing_uv,SUM(bespeak_nums) AS bespeak_nums,pt_d FROM "+
				" dw_honor_marketing_result_hm ";
			sql+=timeSql1;
			sql+=" group by sid,pt_d ) t2 ON t1.sid=t2.sid LEFT OUTER JOIN "+
				" ( "+
				" SELECT "+ 
				" cid, "+
				" sum(order_count) as order_count, "+
				" sum(order_pay_count - order_pay_cancel_cnt - order_return_cnt) AS real_order_count,pt_d"+
				" FROM "+
				" dw_vmallrt_mkt_sale_dm ";
			sql+=timeSql1;
			sql+="  GROUP BY cid,pt_d )t3 "+
			     " ON t1.cid=t3.cid and t2.pt_d=t3.pt_d GROUP BY t1.media_id;";
			List<Map<String, Object>> list = DBUtil.query(conn, sql, false, new Object[] {});
		return list;
	}
	
	/**
     * 整体转化
     * @param conn   数据库执行对象
     * @param account 用户账号
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
	public static List<Map<String, Object>> getResult(MethodContext context,DBConnection conn){
		Map<String, Object> reqParams = context.getParameters();
    	String activityIds =JsonUtil.getAsStr(reqParams, "pActivtiy_id");   //营销活动id
    	String adTypes=JsonUtil.getAsStr(reqParams,"pAd_type");    //广告类型
    	String materialTypes = JsonUtil.getAsStr(reqParams, "pMaterial_type"); //媒体类型
    	String mediaTypes = JsonUtil.getAsStr(reqParams,"pMedia_type");//端口
    	String landingPlats = JsonUtil.getAsStr(reqParams, "pLandingPlat"); //着陆平台
    	String beginTime = JsonUtil.getAsStr(reqParams, "pBegin_time");//投放周期开始时间
    	String endTime = JsonUtil.getAsStr(reqParams, "pEnd_time");//投放周期结束时间
    	String activityId[]=activityIds.split(",");
    	String adType[]= adTypes.split(",");
    	String mediaType[]=mediaTypes.split(",");
    	String materialType[]=materialTypes.split(",");
    	String landingPlat[]=landingPlats.split(",");
    	String tmpSql="where activtiy_id IN(";
    	if(!activityIds.isEmpty()){
    		for(int index = 0; index < activityId.length; index++){
    			if (index < activityId.length - 1){
    				tmpSql+="\'"+activityId[index]+"\',";
    			}else{
    				tmpSql+="\'"+activityId[index]+"\') ";
    			}
    		}
    	}
    	if(!adTypes.isEmpty()){
    		tmpSql+=" AND material_type in(";
    		for(int index = 0; index < adType.length; index++){
    			if (index < adType.length - 1){
    				tmpSql+="\'"+adType[index]+"\',";
    			}else{
    				tmpSql+="\'"+adType[index]+"\') ";
    			}
    		}
    	}
    	if(!materialTypes.isEmpty()){
    		tmpSql+=" AND media_name in(";
    		for(int index = 0; index < materialType.length; index++){
    			if (index < materialType.length - 1){
    				tmpSql+="\'"+materialType[index]+"\',";
    			}else{
    				tmpSql+="\'"+materialType[index]+"\')";
    			}
    		}
    	}
    	if(!landingPlats.isEmpty()){
    		tmpSql+=" AND landing_plate in(";
    		for(int index = 0; index < landingPlat.length; index++){
    			if (index < landingPlat.length - 1){
    				tmpSql+="\'"+landingPlat[index]+"\',";
    			}else{
    				tmpSql+="\'"+landingPlat[index]+"\') ";
    			}
    		}
    	}
    	if(!mediaTypes.isEmpty()){
    		tmpSql+=" AND media_type in(";
    		for(int index = 0; index < mediaType.length; index++){
    			if (index < mediaType.length - 1){
    				tmpSql+="\'"+mediaType[index]+"\',";
    			}else{
    				tmpSql+="\'"+mediaType[index]+"\') ";
    			}
    		}
    	}
    	String timeSql1="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\')";
    	String sql="SELECT "+
    			"SUM(t2.dj_uv) AS dj_uv,"+
    			"SUM(t3.real_pay_count) AS real_pay_count,"+
    			" t2.pt_d as pt_d "+
    			" FROM "+
    			"(SELECT activtiy_id,sid,cid,media_name FROM dim_mkt_base_info_ds  ";
    	sql+=tmpSql;
    	sql+=" ) t1 "+
    			"LEFT OUTER JOIN "+
    			" (SELECT sid,SUM(bg_uv) AS bg_uv,SUM(dj_uv) AS dj_uv,SUM(landing_uv) AS landing_uv,SUM(bespeak_nums) AS bespeak_nums,pt_d FROM dw_honor_marketing_result_hm ";
    	sql+=timeSql1;
    	sql+=" group by sid,pt_d ) t2 "+
    			" ON t1.sid=t2.sid  "+
    			" LEFT OUTER JOIN "+
    			" (SELECT "+
    			" cid, sum(order_pay_count - order_pay_cancel_cnt - order_return_cnt) AS real_pay_count,pt_d "+
    			" FROM dw_vmallrt_mkt_sale_dm ";
    	sql+=timeSql1;
    	sql+=" group by cid,pt_d )t3 "+
    			" ON t1.cid=t3.cid and t2.pt_d=t3.pt_d GROUP BY t2.pt_d ;";
    	List<Map<String, Object>> list = DBUtil.query(conn, sql, false, new Object[] {});
		return list;
	}
	
	
	/**
     * 分类转化
     * @param conn   数据库执行对象
     * @param account 用户账号
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
	public static List<Map<String, Object>> getDayResult(MethodContext context,DBConnection conn){
		Map<String, Object> reqParams = context.getParameters();
		String activityIds =JsonUtil.getAsStr(reqParams, "pActivtiy_id");   //营销活动id
    	String adTypes=JsonUtil.getAsStr(reqParams,"pAd_type");    //广告类型
    	String materialTypes = JsonUtil.getAsStr(reqParams, "pMaterial_type"); //媒体类型
    	String mediaTypes = JsonUtil.getAsStr(reqParams,"pMedia_type");//端口
    	String landingPlats = JsonUtil.getAsStr(reqParams, "pLandingPlat"); //着陆平台
    	String beginTime = JsonUtil.getAsStr(reqParams, "pBegin_time");//投放周期开始时间
    	String endTime = JsonUtil.getAsStr(reqParams, "pEnd_time");//投放周期结束时间
    	String activityId[]=activityIds.split(",");
    	String adType[]= adTypes.split(",");
    	String mediaType[]=mediaTypes.split(",");
    	String materialType[]=materialTypes.split(",");
    	String landingPlat[]=landingPlats.split(",");
    	String tmpSql="where activtiy_id IN(";
    	if(!activityIds.isEmpty()){
    		for(int index = 0; index < activityId.length; index++){
    			if (index < activityId.length - 1){
    				tmpSql+="\'"+activityId[index]+"\',";
    			}else{
    				tmpSql+="\'"+activityId[index]+"\') ";
    			}
    		}
    	}
    	if(!adTypes.isEmpty()){
    		tmpSql+=" AND material_type in(";
    		for(int index = 0; index < adType.length; index++){
    			if (index < adType.length - 1){
    				tmpSql+="\'"+adType[index]+"\',";
    			}else{
    				tmpSql+="\'"+adType[index]+"\') ";
    			}
    		}
    	}
    	if(!materialTypes.isEmpty()){
    		tmpSql+=" AND media_name in(";
    		for(int index = 0; index < materialType.length; index++){
    			if (index < materialType.length - 1){
    				tmpSql+="\'"+materialType[index]+"\',";
    			}else{
    				tmpSql+="\'"+materialType[index]+"\')";
    			}
    		}
    	}
    	if(!landingPlats.isEmpty()){
    		tmpSql+=" AND landing_plate in(";
    		for(int index = 0; index < landingPlat.length; index++){
    			if (index < landingPlat.length - 1){
    				tmpSql+="\'"+landingPlat[index]+"\',";
    			}else{
    				tmpSql+="\'"+landingPlat[index]+"\') ";
    			}
    		}
    	}
    	if(!mediaTypes.isEmpty()){
    		tmpSql+=" AND media_type in(";
    		for(int index = 0; index < mediaType.length; index++){
    			if (index < mediaType.length - 1){
    				tmpSql+="\'"+mediaType[index]+"\',";
    			}else{
    				tmpSql+="\'"+mediaType[index]+"\') ";
    			}
    		}
    	}
    	String timeSql1="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\')";
    	String sql="SELECT "+
    			"t1.media_name as media_name,"+
    			"SUM(t2.bg_uv) AS bg_uv,"+
    			"SUM(t2.dj_uv) AS dj_uv,"+
    			"SUM(t2.landing_uv) AS landing_uv,"+
    			"SUM(t2.bespeak_nums) AS bespeak_nums,"+
    			"SUM(t3.order_count) AS order_count,"+
    			"SUM(t3.real_pay_count) AS real_pay_count,"+
    			"t2.pt_d "+
    			"FROM  "+
    			"(SELECT activtiy_id,sid,cid,media_name FROM dim_mkt_base_info_ds ";
    	sql+=tmpSql;
    	sql+=" ) t1 "+
    		" LEFT OUTER JOIN "+
    		"(SELECT sid,SUM(bg_uv) AS bg_uv,SUM(dj_uv) AS dj_uv,SUM(landing_uv) AS landing_uv,SUM(bespeak_nums) AS bespeak_nums,pt_d FROM "+
    		" dw_honor_marketing_result_hm ";
    	sql+=timeSql1;
    	sql+=" group by sid,pt_d ) t2 "+
    		 " ON t1.sid=t2.sid "+
    		 " LEFT OUTER JOIN "+
    		 "( SELECT "+
    		 " cid,"+
    		 "sum(order_count) as order_count,"+
    		 "sum(order_pay_count - order_pay_cancel_cnt - order_return_cnt) AS real_pay_count,pt_d "+
    		 "FROM dw_vmallrt_mkt_sale_dm ";
    	sql+=timeSql1;
    	sql+=" group by cid,pt_d )t3 ON t1.cid=t3.cid and t2.pt_d=t3.pt_d GROUP BY t2.pt_d,t1.media_name;";
		List<Map<String, Object>> list = DBUtil.query(conn, sql, false, new Object[] {});
		return list;
	}
}
