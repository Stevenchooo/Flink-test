package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetBaseData extends AuthRDBProcessor {
	
	public int process(MethodContext context, DBConnection dbConn){
	    	
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
	    	String timeSql="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\') AND HOUR=\'NA\' AND city=\'NA\'";
	    	//基础数据曝光&点击PV
	    	String pvSql="SELECT  "+
			    " fk.media_id     AS mt_media_id,  "+
			    " fk.media_name   AS mt_media_name,"+
			    " fk.bg_pv_sum    AS mt_bg_pv_sum, "+
			    " fk.dj_pv_sum    AS mt_dj_pv_sum, "+
			    " fk.orderId "+  
			  " FROM "+
			  " (  "+
			  " SELECT c.media_id   AS media_id,  "+
			  " c.media_name AS media_name,  "+
			  " IF(c.bg_pv_sum IS NULL,0,c.bg_pv_sum)  AS bg_pv_sum,  "+
			  " IF(c.dj_pv_sum IS NULL,0,c.dj_pv_sum)  AS dj_pv_sum,  "+
			  " c.orderId                              AS orderId  "+
			  " FROM   "+
			  " (  "+
			  "  SELECT   "+
			  "  f.media_id   AS media_id,  "+
			  "  f.media_name AS media_name,  "+
			  "  f.bg_pv_sum  AS bg_pv_sum,  "+
			  "  f.dj_pv_sum  AS dj_pv_sum,  "+
			  "  1            AS orderId   "+
			  "  FROM   "+
			  "   (  "+  
			  "   SELECT  "+
			  "   t.media_id         AS media_id,  "+
			  "   MIN(t.media_name)  AS media_name,  "+
			  "  SUM(t0.bg_pv)      AS bg_pv_sum,  "+
			  "  SUM(t0.dj_pv)      AS dj_pv_sum  "+
			  "  FROM   "+
			  "  (  "+
			  "  SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds  ";
	    	pvSql+=tmpSql;
	    	pvSql+=" ) t "+
	    			" JOIN "+
	    			"  ( "+
	    			"  SELECT  "+
	    			"  bg_pv, "+
	    			"  dj_pv, "+
	    			"  sid "+
	    			"  FROM dw_mkt_result_dm ";
	    	pvSql+=timeSql;
	    	pvSql+=" ) t0 "+
	    			" ON t.sid = t0.sid "+
	    			" GROUP BY t.media_id "+
	    			"  ) f "+
	    			"  ORDER BY f.dj_pv_sum DESC "+
	    			"  LIMIT 15 "+
	    			"  ) c "+
					"   UNION ALL "+
					"   SELECT "+ 
					"    kk.media_id   AS media_id, "+ 
					"    kk.media_name AS media_name, "+ 
					"    kk.bg_pv_sum  AS bg_pv_sum, "+ 
					"    kk.dj_pv_sum  AS dj_pv_sum, "+ 
					"    kk.orderId    AS orderId "+ 
					"    FROM  "+ 
					"    ( "+ 
					"    SELECT "+ 
					"    IF(SUM(k.bg_pv_sum) IS NULL,NULL,-1)                  AS media_id, "+ 
					"    IF(SUM(k.bg_pv_sum) IS NULL,NULL,'Total')             AS media_name, "+ 
					"    SUM(k.bg_pv_sum)   AS bg_pv_sum, "+ 
					"    SUM(k.dj_pv_sum)   AS dj_pv_sum, "+ 
					"    IF(SUM(k.bg_pv_sum) IS NULL,NULL,0)                    AS orderId "+ 
					"     FROM "+ 
					"    ( "+ 
					"      SELECT media_id,media_name,bg_pv_sum,dj_pv_sum FROM  "+ 
					"      ( "+ 
					"   SELECT "+ 
					"    t.media_id         AS media_id, "+ 
					"    MIN(t.media_name)  AS media_name, "+ 
					"   SUM(t0.bg_pv)      AS bg_pv_sum, "+ 
					"    SUM(t0.dj_pv)      AS dj_pv_sum "+ 
					"    FROM  "+ 
					"    ( "+  
					"    SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds  ";
	    	pvSql+=tmpSql;
	    	pvSql+=" ) t "+
	    			" JOIN "+
	    			" ( "+
	    			"  SELECT  "+
	    			"  bg_pv, "+
	    			"  dj_pv, "+
	    			"  sid "+
	    			"  FROM dw_mkt_result_dm ";
	    	pvSql+=	timeSql;
	    	pvSql+=" ) t0 "+
	    			"   ON t.sid = t0.sid "+
	    			"  GROUP BY t.media_id "+
	    			" ) f "+
	    			" ) k "+
	    			" LIMIT 1 "+
	    			" ) kk "+
	    			" UNION ALL "+
	    			" SELECT  "+
	    			" kk.media_id   AS media_id, "+
	    			"  kk.media_name AS media_name, "+
	    			" kk.bg_pv_sum  AS bg_pv_sum, "+
	    			" kk.dj_pv_sum  AS dj_pv_sum, "+ 
	    			"  kk.orderId    AS orderId "+
	    			"  FROM  "+
	    			" ( "+
	    			"  SELECT "+
	    			"  IF(SUM(k.bg_pv_sum) IS NULL,NULL,-2)                 AS media_id, "+
	    			"  IF(SUM(k.bg_pv_sum) IS NULL,NULL,'其他')             AS media_name, "+
	    			"  SUM(k.bg_pv_sum)   AS bg_pv_sum, "+
	    			"   SUM(k.dj_pv_sum)   AS dj_pv_sum, "+
	    			"  IF(SUM(k.bg_pv_sum) IS NULL,NULL,2)                   AS orderId "+
	    			"   FROM "+
	    			"  ( "+
	    			"   SELECT media_id,media_name,bg_pv_sum,dj_pv_sum FROM  "+
	    			"  ( "+
	    			"  SELECT "+
	    			"   t.media_id         AS media_id, "+
	    			"   MIN(t.media_name)  AS media_name, "+
	    			"  SUM(t0.bg_pv)      AS bg_pv_sum, "+
	    			"  SUM(t0.dj_pv)      AS dj_pv_sum "+
	    			"  FROM  "+
	    			"  ( "+
	    			"  SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
	    	pvSql+=tmpSql;	
	    	pvSql+=" ) t "+
	    			" JOIN "+
	    			"  ( "+
	    			"  SELECT  "+
	    			"      bg_pv, "+
	    			"      dj_pv, "+
	    			"      sid "+
	    			"   FROM dw_mkt_result_dm "	;
	    	pvSql+=timeSql;
	    	pvSql+=" ) t0 "+
              "  ON t.sid = t0.sid "+
              "  GROUP BY t.media_id "+
              " ) f "+
              " ORDER BY f.dj_pv_sum DESC "+
              "  LIMIT 15,2000 "+
              " ) k "+
              "  LIMIT 1 "+
              " ) kk "+
              " ) fk "+
              " WHERE "+
              " fk.media_id IS NOT NULL "+
              " ORDER BY fk.orderId ,fk.bg_pv_sum DESC,fk.dj_pv_sum DESC;";
	    	
	    	//基础数据曝光&点击UV
	    	String uvSql=" SELECT  "+
	    			"  fk.media_id     AS mt_media_id,    "+
	    			"  fk.media_name   AS mt_media_name,  "+
	    			"  fk.bg_uv_sum    AS mt_bg_uv_sum,   "+
	    			"  fk.dj_uv_sum    AS mt_dj_uv_sum,  "+
	    			"  fk.orderId    "+
	    			" FROM  "+
	    			" (  "+
	    			" SELECT c.media_id   AS media_id,  "+
	    			"   c.media_name AS media_name,  "+
	    			"   IF(c.bg_pv_sum IS NULL,0,c.bg_pv_sum)  AS bg_pv_sum,  "+
	    			"   IF(c.bg_uv_sum IS NULL,0,c.bg_uv_sum)  AS bg_uv_sum,  "+
	    			" 	IF(c.dj_pv_sum IS NULL,0,c.dj_pv_sum)  AS dj_pv_sum,  "+
	    			"   IF(c.dj_uv_sum IS NULL,0,c.dj_uv_sum)  AS dj_uv_sum,  "+
	    			"   c.orderId                              AS orderId  "+
	    			"   FROM   "+
	    			"   (  "+
	    			"    SELECT   "+
	    			"    f.media_id   AS media_id,  "+
	    			"   f.media_name AS media_name,  "+
	    			"   f.bg_pv_sum  AS bg_pv_sum,  "+
	    			"   f.bg_uv_sum  AS bg_uv_sum,  "+
	    			"   f.dj_pv_sum  AS dj_pv_sum,  "+
	    			"   f.dj_uv_sum  AS dj_uv_sum,  "+
	    			"    1            AS orderId   "+
	    			"    FROM   "+
	    			"    (  "+
	    			"     SELECT  "+
	    			"    t.media_id         AS media_id,  "+
	    			"    MIN(t.media_name)  AS media_name,  "+
	    			"    SUM(t0.bg_pv)      AS bg_pv_sum,  "+
	    			"   SUM(t0.bg_uv)      AS bg_uv_sum,  "+
	    			"   SUM(t0.dj_pv)      AS dj_pv_sum,  "+
	    			"   SUM(t0.dj_uv)      AS dj_uv_sum  "+
	    			"    FROM   "+
	    			"    (  "+
	    			"    SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
	    	uvSql+=tmpSql;
	    	uvSql+=" ) t "+
	    			" JOIN "+
	    			"  ( "+
	    			"  SELECT  "+
	    			"   bg_pv, "+
	    			"   bg_uv, "+
	    			"  dj_pv, "+
	    			"  dj_uv, "+
	    			"  sid "+
	    			"  FROM dw_mkt_result_dm ";
	    	uvSql+=timeSql;
	    	uvSql+="  ) t0 "+
	    			"  ON t.sid = t0.sid "+
	    			"  GROUP BY t.media_id "+
	    			"   ) f "+
	    			"  ORDER BY f.dj_pv_sum DESC,f.dj_uv_sum DESC "+
	    			"  LIMIT 15 "+
	    			"  ) c "+
	    			"   UNION ALL "+
	    			"   SELECT  "+
	    			"   kk.media_id   AS media_id, "+
	    			"   kk.media_name AS media_name, "+
	    			"   kk.bg_pv_sum  AS bg_pv_sum, "+
	    			"   kk.bg_uv_sum  AS bg_uv_sum, "+
	    			"   kk.dj_pv_sum  AS dj_pv_sum, "+
	    			"   kk.dj_uv_sum  AS dj_uv_sum, "+
	    			"   kk.orderId    AS orderId "+
	    			"   FROM  "+
	    			"   ( "+
	    			"   SELECT "+
	    			"   IF(SUM(k.bg_pv_sum) IS NULL,NULL,-1)                  AS media_id, "+
	    			"   IF(SUM(k.bg_pv_sum) IS NULL,NULL,'Total')             AS media_name, "+
	    			"   SUM(k.bg_pv_sum)   AS bg_pv_sum, "+
	    			"   SUM(k.bg_uv_sum)   AS bg_uv_sum, "+
	    			"   SUM(k.dj_pv_sum)   AS dj_pv_sum, "+
	    			"   SUM(k.dj_uv_sum)   AS dj_uv_sum, "+
	    			"   IF(SUM(k.bg_pv_sum) IS NULL,NULL,0)                    AS orderId "+
	    			"    FROM "+
	    			"   ( "+
	    			"    SELECT media_id,media_name,bg_pv_sum,bg_uv_sum,dj_pv_sum,dj_uv_sum FROM  "+
	    			"    ( "+
	    			"    SELECT "+
	    			"   t.media_id         AS media_id, "+
	    			"   MIN(t.media_name)  AS media_name, "+
	    			"   SUM(t0.bg_pv)      AS bg_pv_sum, "+
	    			"  SUM(t0.bg_uv)      AS bg_uv_sum, "+
	    			"   SUM(t0.dj_pv)      AS dj_pv_sum, "+
	    			"   SUM(t0.dj_uv)      AS dj_uv_sum "+
	    			"   FROM  "+
	    			"    ( "+
            		   "   SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds  ";
	    	uvSql+=tmpSql;
	    	uvSql+="  ) t "+
	    			"   JOIN "+
	    			"  ( "+
	    			"     SELECT  "+
	    			"      bg_pv, "+
	    			"      bg_uv, "+
	    			"      dj_pv, "+
	    			"      dj_uv, "+
	    			"  	   sid "+
	    			"      FROM dw_mkt_result_dm ";
	    	uvSql+=timeSql;
	    	uvSql+=" ) t0 "+
	    			"  ON t.sid = t0.sid "+
                    "   GROUP BY t.media_id "+
	    			"   ) f "+
	    			" ) k "+
	    			"  LIMIT 1 "+
	    			"  ) kk "+
	    			"   UNION ALL "+
	    			"   SELECT  "+
	    			"   kk.media_id   AS media_id, "+
	    			"   kk.media_name AS media_name, "+
	    			"   kk.bg_pv_sum  AS bg_pv_sum, "+
	    			"   kk.bg_uv_sum  AS bg_uv_sum, "+
	    			"   kk.dj_pv_sum  AS dj_pv_sum, "+
	    			"   kk.dj_uv_sum  AS dj_uv_sum, "+
	    			"   kk.orderId    AS orderId "+
	    			"   FROM  "+
	    			"  ( "+
	    			"  SELECT "+
	    			"   IF(SUM(k.bg_pv_sum) IS NULL,NULL,-2)                 AS media_id, "+
	    			"   IF(SUM(k.bg_pv_sum) IS NULL,NULL,'其他')             AS media_name, "+
	    			"   SUM(k.bg_pv_sum)   AS bg_pv_sum, "+
	    			"   SUM(k.bg_uv_sum)   AS bg_uv_sum, "+
	    			"   SUM(k.dj_pv_sum)   AS dj_pv_sum, "+
	    			"   SUM(k.dj_uv_sum)   AS dj_uv_sum, "+
	    			"   IF(SUM(k.bg_pv_sum) IS NULL,NULL,2)                   AS orderId "+
	    			"  FROM "+
	    			"  ( "+
	    			"     SELECT media_id,media_name,bg_pv_sum,bg_uv_sum,dj_pv_sum,dj_uv_sum FROM  "+
	    			"   ( "+
	    			"    SELECT "+
	    			"   t.media_id         AS media_id, "+
	    			"    MIN(t.media_name)  AS media_name, "+
	    			"   SUM(t0.bg_pv)      AS bg_pv_sum, "+
	    			"   SUM(t0.bg_uv)      AS bg_uv_sum, "+
	    			"   SUM(t0.dj_pv)      AS dj_pv_sum, "+
	    			"   SUM(t0.dj_uv)      AS dj_uv_sum "+
	    			"   FROM  "+
	    			"   ( "+
	    			"   SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
	    	uvSql+=tmpSql;
	    	uvSql+="  ) t "+
	    			"   JOIN "+
	    			"   ( "+
	    			"    SELECT  "+
	    			"       bg_pv, "+
	    			"       bg_uv, "+
	    			"       dj_pv, "+
	    			"       dj_uv, "+
	    			"  	    sid "+
	    			"        FROM dw_mkt_result_dm ";
	    	uvSql+=timeSql;
	    	uvSql+=" ) t0 "+ 
              "  ON t.sid = t0.sid "+
              "  GROUP BY t.media_id "+
	    	  "	 ) f "+
	    	  "  ORDER BY f.dj_pv_sum DESC,f.dj_uv_sum DESC "+
	    	  "   LIMIT 15,2000 "+
	    	  "   ) k "+
	    	  "     LIMIT 1 "+
	    	  "  ) kk "+
	    	  " ) fk "+
	    	  " WHERE "+
	    	  " fk.media_id IS NOT NULL "+
	    	  " ORDER BY fk.orderId ,fk.bg_uv_sum DESC,fk.dj_uv_sum DESC;";
	    	
	    	List<Map<String, Object>> pvList = DBUtil.query(dbConn, pvSql, false, new Object[] {});
	    	List<Map<String, Object>> uvList=DBUtil.query(dbConn, uvSql, false, new Object[] {});
	    	context.setResult("pvList", pvList);
	    	context.setResult("uvList",uvList);
	    	
	    	return RetCode.OK;
	}
}
