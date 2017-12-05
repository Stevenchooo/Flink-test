package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetFrequencyStat extends AuthRDBProcessor {
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
    	String timeSql1=" WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\') ";
    	String frSql="select ";
    	frSql+="q.media_id AS media_id,"+
    			"q.media_name AS media_name,"+
    			"q.frequency AS frequency,"+
    			"q.exp_frequency_users AS exp_frequency_users,"+
    			"q.click_frequency_users AS click_frequency_users,"+
    			"q.landing_frequency_users AS landing_frequency_users "+
    			"FROM "+
    			"(SELECT "+
    			"z.media_id AS media_id,"+
    			"MIN(z.media_name) AS media_name,"+
    			"z.frequency AS frequency,"+
    			"SUM(z.exp_frequency_users) AS exp_frequency_users,"+
    			"SUM(z.click_frequency_users) AS click_frequency_users,"+
    			"SUM(z.landing_frequency_users) AS landing_frequency_users "+
    			"FROM( "+
    			"SELECT "+
    			" abc2.media_id AS media_id,"+
    			" abc2.media_name AS media_name,"+
    			" abc2.frequency AS frequency,"+
    			" abc2.exp_frequency_users AS exp_frequency_users,"+
    			" abc2.click_frequency_users AS click_frequency_users,"+
    			" abc2.landing_frequency_users AS landing_frequency_users "+
    			" FROM "+
    			"(SELECT "+
    			" t.media_id AS media_id,"+
    			" t1.frequency AS frequency,"+
    			" MIN(t.media_name) AS media_name,"+
    			" SUM(t1.exp_frequency_users) AS exp_frequency_users,"+
    			" SUM(t1.click_frequency_users) AS click_frequency_users,"+
    			" SUM(t1.landing_frequency_users) AS landing_frequency_users "+
    			" FROM "+
    			" (SELECT "+
    			" activtiy_id,"+
    			" media_name,"+
    			" media_id "+
    			" FROM "+
    			" dim_mkt_base_info_ds ";
    		frSql+=tmpSql;
    		frSql+=" GROUP BY activtiy_id,media_name,media_id) t "+
    				" LEFT OUTER JOIN "+
    				" (SELECT "+
    				" activtiy_id,"+
    				"  media_id,"+
    				" frequency,"+
    				"exp_frequency_users,"+
    				"click_frequency_users,"+
    				"landing_frequency_users "+
    				"FROM dw_exposure_frequency_dm ";
    		frSql+=timeSql1;
    		frSql+=" ) t1 "+
    				" ON t.activtiy_id = t1.activtiy_id "+
    				" AND t.media_id = t1.media_id "+
    				" GROUP BY t.media_id,t1.frequency "+
    				" union all "+
    				" SELECT "+
    				" \'0\' AS media_id,"+
    				"  t01.frequency AS frequency, "+
    				" \'total\' AS media_name,"+
    				"SUM(t01.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(t01.click_frequency_users) AS click_frequency_users,"+
    				" SUM(t01.landing_frequency_users) AS landing_frequency_users "+
    				" FROM "+
    				" (SELECT "+
    				" activtiy_id,"+
    				" media_name, "+
    				" media_id "+
    				" FROM "+
    				"dim_mkt_base_info_ds ";
    		frSql+=tmpSql;
    		frSql+=" GROUP BY activtiy_id,media_name,media_id) t0 "+
    				" LEFT OUTER JOIN "+
    				"(SELECT "+
    				" activtiy_id,"+
    				" media_id, "+
    				" frequency,"+
    				" exp_frequency_users,"+
    				" click_frequency_users,"+
    				" landing_frequency_users "+
    				" FROM "+
    				" dw_exposure_frequency_dm ";
    		frSql+=timeSql1;
    		frSql+=") t01 "+
    				"ON t0.activtiy_id = t01.activtiy_id "+
    				"AND t0.media_id = t01.media_id "+
    				"  GROUP BY t01.frequency) abc2) z "+
    				" GROUP BY z.media_id,z.frequency "+
    				" UNION ALL "+
    				//3+
    				" SELECT "+
    				"  z1.media_id AS media_id,"+
    				" MIN(z1.media_name) AS media_name,"+
    				" \'3+\' AS frequency,"+
    				"  SUM(z1.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(z1.click_frequency_users) AS click_frequency_users,"+
    				" SUM(z1.landing_frequency_users) AS landing_frequency_users "+
    				" FROM ("+
    				" SELECT "+
    				" abc2.media_id AS media_id,"+
    				" abc2.media_name AS media_name,"+
    				" abc2.frequency AS frequency,"+
    				" abc2.exp_frequency_users AS exp_frequency_users,"+
    				" abc2.click_frequency_users AS click_frequency_users,"+
    				" abc2.landing_frequency_users AS landing_frequency_users "+
    				" FROM ("+
    				" SELECT "+
    				" t.media_id AS media_id,"+
    				" t1.frequency AS frequency,"+
    				" MIN(t.media_name) AS media_name,"+
    				" SUM(t1.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(t1.click_frequency_users) AS click_frequency_users,"+
    				" SUM(t1.landing_frequency_users) AS landing_frequency_users "+
    				" FROM "+
    				" (SELECT"+
    				"  activtiy_id,"+
    				" media_name,"+
    				"  media_id "+
    				" FROM "+
    				" dim_mkt_base_info_ds ";
    		frSql+=tmpSql;
    		frSql+=" GROUP BY activtiy_id,media_name, media_id) t "+
    				" LEFT OUTER JOIN "+
    				" (SELECT "+
    				" activtiy_id,"+
    				" media_id,"+
    				" frequency, "+
    				" exp_frequency_users,"+
    				" click_frequency_users,"+
    				" landing_frequency_users "+
    				" FROM dw_exposure_frequency_dm ";
    		frSql+=timeSql1;
    		frSql+=") t1 "+
    				" ON t.activtiy_id = t1.activtiy_id AND t.media_id = t1.media_id "+
    				"GROUP BY t.media_id,t1.frequency "+
    				" UNION ALL "+
    				" SELECT "+
    				" \'0\' AS media_id,"+
    				" t01.frequency AS frequency,"+
    				" \'total\' AS media_name,"+
    				" SUM(t01.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(t01.click_frequency_users) AS click_frequency_users,"+
    				" SUM(t01.landing_frequency_users) AS landing_frequency_users "+
    				" FROM "+
    				" (SELECT "+
    				" activtiy_id,"+
    				" media_name,"+
    				" media_id "+
    				" FROM dim_mkt_base_info_ds ";
    		frSql+=tmpSql;
    		frSql+="GROUP BY activtiy_id,media_name,media_id) t0 "+
    				" LEFT OUTER JOIN "+
    				" (SELECT "+
    				" activtiy_id,"+
    				" media_id,"+
    				" frequency,"+
    				" exp_frequency_users,"+
    				" click_frequency_users,"+
    				" landing_frequency_users "+
    				" FROM dw_exposure_frequency_dm ";
    		frSql+=timeSql1;
    		frSql+=" ) t01"+
    				" ON t0.activtiy_id = t01.activtiy_id  AND t0.media_id = t01.media_id "+
    				" GROUP BY t01.frequency) abc2) z1 "+
    				" WHERE z1.frequency >= 3 GROUP BY z1.media_id "+
    				//15+
    				" UNION ALL "+
    				" SELECT "+
    				" z2.media_id AS media_id,"+
    				" MIN(z2.media_name) AS media_name,"+
    				" \'15+\' AS frequency,"+
    				"  SUM(z2.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(z2.click_frequency_users) AS click_frequency_users,"+
    				" SUM(z2.landing_frequency_users) AS landing_frequency_users "+
    				" FROM ("+
    				" SELECT "+
    				" abc2.media_id AS media_id,"+
    				" abc2.media_name AS media_name,"+
    				" abc2.frequency AS frequency,"+
    				" abc2.exp_frequency_users AS exp_frequency_users,"+
    				" abc2.click_frequency_users AS click_frequency_users,"+
    				" abc2.landing_frequency_users AS landing_frequency_users "+
    				" FROM "+
    				" (SELECT "+
    				" t.media_id AS media_id,"+
    				" t1.frequency AS frequency,"+
    				" MIN(t.media_name) AS media_name,"+
    				"  SUM(t1.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(t1.click_frequency_users) AS click_frequency_users,"+
    				" SUM(t1.landing_frequency_users) AS landing_frequency_users "+
    				" FROM "+
    				" (SELECT "+
    				" activtiy_id,"+
    				" media_name,"+
    				" media_id "+
    				" FROM dim_mkt_base_info_ds ";
    		frSql+=tmpSql;
    		frSql+=" GROUP BY activtiy_id,media_name,media_id) t "+
    				" LEFT OUTER JOIN "+
    				" (SELECT "+
    				" activtiy_id,"+
    				"  media_id,"+
    				"  frequency,"+
    				" exp_frequency_users,"+
    				" click_frequency_users,"+
    				" landing_frequency_users "+
    				"  FROM dw_exposure_frequency_dm ";
    		frSql+=timeSql1;
    		frSql+=") t1 "+
    				" ON t.activtiy_id = t1.activtiy_id "+
    				" AND t.media_id = t1.media_id GROUP BY t.media_id, t1.frequency "+
    				" UNION ALL "+
    				"  SELECT "+
    				"  \'0\' AS media_id,"+
    				"  t01.frequency AS frequency,"+
    				" \'total\' AS media_name,"+
    				" SUM(t01.exp_frequency_users) AS exp_frequency_users,"+
    				" SUM(t01.click_frequency_users) AS click_frequency_users,"+
    				" SUM(t01.landing_frequency_users) AS landing_frequency_users "+
    				" FROM "+
    				" (SELECT "+
    				" activtiy_id,"+
    				" media_name,"+
    				" media_id "+
    				" FROM dim_mkt_base_info_ds ";
    	   frSql+=tmpSql;
    	   frSql+=" GROUP BY activtiy_id, media_name, media_id) t0 "+
    			   "  LEFT OUTER JOIN "+
    			   " (SELECT "+
    			   " activtiy_id,"+
    			   " media_id,"+
    			   " frequency,"+
    			   " exp_frequency_users,"+
    			   " click_frequency_users,"+
    			   " landing_frequency_users "+
    			   " FROM dw_exposure_frequency_dm ";
    	   frSql+=timeSql1;
    	   frSql+="   ) t01 "+
    			   " ON t0.activtiy_id = t01.activtiy_id AND t0.media_id = t01.media_id "+
    			   " GROUP BY t01.frequency) abc2) z2 "+
    			   " WHERE z2.frequency >= 15 "+
    			   " GROUP BY z2.media_id "+
    			   " ) q "+
    			   " WHERE  q.frequency IS NOT NULL "+
    			   " GROUP BY media_id,media_name,frequency "+
    			   " ORDER BY q.media_id ASC ;";
    		
    			
    	
    	List<Map<String, Object>> list = DBUtil.query(dbConn, frSql, false, new Object[] {});
    	context.setResult("frResults", list);
    	
    	return RetCode.OK;
	}
}
