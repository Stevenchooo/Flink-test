package com.huawei.manager.mkt.api;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;


/**
 * 
 * @author w84057406
 *
 */
public class GetGeneralSituatInfo extends AuthRDBProcessor {
	/**
     * 根据查询参数， 查找结果，分配结果集
     * @param context 上下文
     * @param dbConn 数据库连接
     * @return 结果集
     */
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
    	String activity="(";
    	for(int index = 0; index < activityId.length; index++){
			if (index < activityId.length - 1){
				activity+="\'"+activityId[index]+"\',";
			}else{
				activity+="\'"+activityId[index]+"\') ";
			}
		}
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
    	String timeSql1="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\')";
    	//总体概况
    	String generalsql="SELECT  "+ 
    			" SUM(fk.bg_pv) AS bg_pv,"+
    			" SUM(fk.dj_pv) AS dj_pv,"+
    			" SUM(fk.bg_uv) AS bg_uv,"+
    			" SUM(fk.dj_uv) AS dj_uv,"+
    			" SUM(buy_nums) AS buy_nums,"+
    			" fk.media_id AS media_id "+
    			" FROM "+
    			" ( SELECT  "+
    			         "t.activtiy_id     AS activtiy_id,"+
    			         "t.activity_name   AS activity_name,"+
    			         "t0.bg_pv          AS bg_pv,"+
    			         "t0.dj_pv          AS dj_pv,"+
    			         "t0.bg_uv          AS bg_uv,"+
    					 "t0.dj_uv          AS dj_uv,"+
    					 "t0.buy_nums       As buy_nums, "+
    					 "t.media_id        AS media_id "+
    			    " FROM (SELECT  "+
    			           "sid,"+
    			           "activtiy_id,"+
    			           "media_id,"+
    			           "activity_name "+
    			    " FROM dim_mkt_base_info_ds  ";
    	generalsql+=tmpSql;
    	generalsql+= ") t"+
    			" LEFT OUTER JOIN"+
    			"("+
    			"SELECT "+
    				" bg_pv,"+
    				"dj_pv,"+
    				"bg_uv,"+
    				"dj_uv,"+
    				"sid,"+
    				"buy_nums"+
    				" FROM dw_honor_marketing_result_hm ";
    	generalsql+=timeSql1;
    	generalsql+=") t0 ON t.sid = t0.sid )  fk GROUP BY fk.media_id;";
    	
    	//媒体排名BG
    	String mediaSql="SELECT  "+
    			" fk.media_id     AS mt_media_id, "+ 
    			" fk.media_name   AS mt_media_name,"+
    			"IF(fk.bg_pv_sum IS NULL,0,fk.bg_pv_sum)    AS mt_bg_pv_sum, "+
    			"IF(fk.bg_uv_sum IS NULL,0,fk.bg_uv_sum)    AS mt_bg_uv_sum "+
				" FROM "+
				"("+
					"SELECT c.media_id   AS media_id,"+
						"c.media_name AS media_name,"+
						"c.bg_pv_sum  AS bg_pv_sum,"+
						"c.bg_uv_sum  AS bg_uv_sum  "+
						"  FROM  "+
						"("+
							"SELECT "+
							"f.media_id   AS media_id,"+
							"f.media_name AS media_name,"+
							"f.bg_pv_sum  AS bg_pv_sum,"+
							"f.bg_uv_sum  AS bg_uv_sum "+
							" FROM "+
							"( SELECT  "+
								" t.media_id         AS media_id,"+
								"MIN(t.media_name)  AS media_name,"+
								"SUM(t0.bg_pv)      AS bg_pv_sum,"+
								"SUM(t0.bg_uv)      AS bg_uv_sum "+
								" FROM  ("+
									"SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
    	mediaSql+=tmpSql;
    	mediaSql+=" ) t  LEFT OUTER JOIN ( SELECT  bg_pv,  bg_uv, sid  FROM dw_honor_marketing_result_hm ";
    	mediaSql+=timeSql1;
    	mediaSql+=" ) t0 ON t.sid = t0.sid GROUP BY t.media_id ) f  ORDER BY f.bg_uv_sum DESC LIMIT 15 ) c "+
    			" UNION ALL "+
	    		" SELECT "+
		           " kk.media_id   AS media_id,"+
		           "kk.media_name AS media_name,"+
		           "kk.bg_pv_sum  AS bg_pv_sum,"+
		           "kk.bg_uv_sum  AS bg_uv_sum "+
		           		" FROM   ( SELECT "+
		           " IF(SUM(k.bg_pv_sum) IS NULL,NULL,-1)                 AS media_id,"+
		           "IF(SUM(k.bg_pv_sum) IS NULL,NULL,'其他')             AS media_name,"+
		           "SUM(k.bg_pv_sum)   AS bg_pv_sum,"+
		           "SUM(k.bg_uv_sum)   AS bg_uv_sum "+
		           	" FROM ( "+
			           " SELECT media_id,media_name,bg_pv_sum,bg_uv_sum FROM "+
			           "( "+
			              " SELECT "+
			              " t.media_id         AS media_id,"+
			              "MIN(t.media_name)  AS media_name,"+
			              "SUM(t0.bg_pv)      AS bg_pv_sum,"+
			              "SUM(t0.bg_uv)      AS bg_uv_sum "+
			              " FROM  ("+
		               " SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
    	mediaSql+=tmpSql;
    	mediaSql+=" ) t LEFT OUTER JOIN  ( SELECT  bg_pv, bg_uv, sid FROM dw_honor_marketing_result_hm ";
    	mediaSql+=timeSql1;
    	mediaSql+=" ) t0 ON t.sid = t0.sid  GROUP BY t.media_id ) f ORDER BY f.bg_uv_sum DESC LIMIT 15,2000  ) k  LIMIT 1  ) kk"+
    			") fk WHERE fk.media_id IS NOT NULL AND (fk.bg_pv_sum !=0 OR fk.bg_uv_sum !=0 );";	
    	
    	//媒体排名DJ
    	String mediaDjSql="SELECT "+ 
    				" fk.media_id     AS mt_media_id, "+  
    				"fk.media_name   AS mt_media_name,"+ 
    				"IF(fk.dj_pv_sum IS NULL,0,fk.dj_pv_sum)    AS mt_dj_pv_sum, "+ 
    				"IF(fk.dj_uv_sum IS NULL,0,fk.dj_uv_sum)    AS mt_dj_uv_sum "+ 
    				"FROM ("+ 
    					" SELECT c.media_id   AS media_id,"+ 
    						"c.media_name AS media_name,"+ 
    						"c.dj_pv_sum  AS dj_pv_sum,"+ 
    						"c.dj_uv_sum  AS dj_uv_sum "+ 
    					" FROM  ("+ 
    						"SELECT "+ 
    						" f.media_id   AS media_id,"+ 
    						"f.media_name AS media_name,"+ 
    						"f.dj_pv_sum  AS dj_pv_sum,"+ 
    						"f.dj_uv_sum  AS dj_uv_sum "+ 
    						" FROM  ("+ 
    							"SELECT "+ 
    							" t.media_id         AS media_id,"+ 
    							"MIN(t.media_name)  AS media_name,"+ 
    							"SUM(t0.dj_pv)      AS dj_pv_sum,"+ 
    							"SUM(t0.dj_uv)      AS dj_uv_sum "+ 
    							" FROM   ("+ 
    								"SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
    	mediaDjSql+=tmpSql;
    	mediaDjSql+=") t LEFT OUTER JOIN (  SELECT  dj_pv,dj_uv,sid FROM dw_honor_marketing_result_hm ";
    	mediaDjSql+=timeSql1;
    	mediaDjSql+=") t0 ON t.sid = t0.sid GROUP BY t.media_id ) f ORDER BY f.dj_uv_sum DESC LIMIT 15 ) c "+
    				" UNION ALL "+
    				" SELECT "+
    				" kk.media_id   AS media_id,"+
    				"kk.media_name AS media_name,"+
    				"kk.dj_pv_sum  AS dj_pv_sum,"+
    				"kk.dj_uv_sum  AS dj_uv_sum "+
    				" FROM ("+
    					"SELECT "+
    					" IF(SUM(k.dj_pv_sum) IS NULL,NULL,-1)                 AS media_id,"+
    					"IF(SUM(k.dj_pv_sum) IS NULL,NULL,'其他')             AS media_name,"+
    					"SUM(k.dj_pv_sum)   AS dj_pv_sum,"+
    					"SUM(k.dj_uv_sum)   AS dj_uv_sum "+
    					" FROM("+
    						" SELECT media_id,media_name,dj_pv_sum,dj_uv_sum FROM "+
    						"("+
    							"SELECT "+
    								" t.media_id         AS media_id,"+
    								"MIN(t.media_name)  AS media_name,"+
    								"SUM(t0.dj_pv)      AS dj_pv_sum,"+
    								"SUM(t0.dj_uv)      AS dj_uv_sum "+
    								" FROM ("+
    									" SELECT sid,media_name,media_id FROM dim_mkt_base_info_ds ";
    	mediaDjSql+=tmpSql;
    	mediaDjSql+= ") t LEFT OUTER JOIN (SELECT dj_pv,dj_uv,sid FROM dw_honor_marketing_result_hm ";
    	mediaDjSql+=timeSql1;
    	mediaDjSql+=") t0 ON t.sid = t0.sid GROUP BY t.media_id ) f ORDER BY f.dj_uv_sum DESC LIMIT 15,2000 ) k  LIMIT 1 ) kk ) fk "+
    			" WHERE fk.media_id IS NOT NULL AND (fk.dj_pv_sum !=0 OR fk.dj_uv_sum !=0 );";	
    	
    	//24小时趋势
    	String hourSql="SELECT t0.hour AS hour,SUM(t0.bg_pv)  AS hour_bg_pv_sum,SUM(t0.dj_pv)  AS hour_dj_pv_sum "
    				+" FROM ( SELECT sid  FROM dim_mkt_base_info_ds ";
    	hourSql+=tmpSql;
    	hourSql+=") t JOIN( SELECT  bg_pv,dj_pv, HOUR,  sid FROM dw_mkt_result_dm ";
    	hourSql+="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\') AND city=\'NA\'";
    	hourSql+=") t0 ON t.sid = t0.sid GROUP BY t0.hour ORDER BY t0.hour desc;";
    	
    	//点击趋势
    	String djSql="SELECT "+
    			"t.media_id        AS dj_media_id,"+
    			"t.media_name      AS dj_media_name,"+
    			"IF(t0.click_frequency_users_sum IS NULL ,0,t0.click_frequency_users_sum) AS click_frequency_users_sum,"+
    			"IF(t0.frequency IS NULL,0,t0.frequency)         AS frequency "+
    			" FROM  ("+
    				"SELECT "+
    				"media_id,"+
    				"media_name         "+
    				"FROM dim_mkt_base_info_ds ";
    	djSql+=tmpSql;
    	djSql+=" GROUP BY  media_id,media_name ) t JOIN ("+
    			"SELECT "+
    			" SUM(click_frequency_users) AS click_frequency_users_sum,"+
    			"media_id                   AS media_id,"+
    			"frequency                  AS frequency "+
    			"FROM dw_exposure_frequency_dm ";
    	djSql+="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\')";
    	djSql+=" AND activtiy_id IN "+activity+" AND frequency<=20 GROUP BY media_id,frequency ) t0 ON t.media_id = t0.media_id;";
    	
    	//地域
    	String areaSql="SELECT "+ 
    			" SUM(bg_pv)      AS area_bg_pv,"+ 
    			"SUM(dj_pv)      AS area_dj_pv,"+ 
    			"SUM(bg_uv)      AS area_bg_uv,"+ 
    			"SUM(dj_uv)      AS area_dj_uvm, "+ 
    			"SUBSTRING_INDEX(SUBSTRING_INDEX(province,'省',1),'市',1)        AS province   "+ 
    			" FROM  ("+ 
    				" SELECT "+ 
    				" t0.bg_pv          AS bg_pv,"+ 
    				"t0.dj_pv          AS dj_pv,"+ 
    				"t0.bg_uv          AS bg_uv,"+ 
    				"t0.dj_uv          AS dj_uv,"+ 
    				"t0.province       AS province "+ 
    				" FROM ("+ 
    					"SELECT "+ 
    					"sid,"+ 
    					"activtiy_id "+ 
    					" FROM dim_mkt_base_info_ds ";
    	areaSql+=tmpSql;
    	areaSql+=") t JOIN ( SELECT bg_pv,dj_pv, bg_uv,dj_uv,sid, province FROM dw_mkt_result_dm ";
    	areaSql+=timeSql;
    	areaSql+=" ) t0 ON t.sid = t0.sid )  fk GROUP BY fk.province;";
    	
    	//截止日期
    	String fianlTimeSql="select max(pt_d) as finalTime from dw_mkt_result_dm;";
    	
    	//总体概况
		NumberFormat num = NumberFormat.getPercentInstance(); 
		num.setMaximumIntegerDigits(3); 
		num.setMaximumFractionDigits(2);
    	List<Map<String, Object>> list = DBUtil.query(dbConn, generalsql, false, new Object[] {});
    	int bgPvAll=0;
    	int djPvAll=0;
    	int bgUvAll=0;
    	int djUvAll=0;
    	int buyNumsAll=0;
    	double buyRate;
    	double ctr=0.00;
    	Map<String, Object> th = new HashMap<String, Object>();
    	List<Map<String, Object>> li=new ArrayList<Map<String,Object>>();
    	if(list!=null){
    	    for(Map<String,Object> rs:list){
                int bgPv=Integer.parseInt((String)rs.get("bg_pv")==null?"0":(String)rs.get("bg_pv"));
                int djPv=Integer.parseInt((String)rs.get("dj_pv")==null?"0":(String)rs.get("dj_pv"));
                int bgUv=Integer.parseInt((String)rs.get("bg_uv")==null?"0":(String)rs.get("bg_uv"));
                int djUv=Integer.parseInt((String)rs.get("dj_uv")==null?"0":(String)rs.get("dj_uv"));
                int buyNums=Integer.parseInt((String)rs.get("buy_nums")==null?"0":(String)rs.get("buy_nums"));
                if(bgPv>0){
                    bgPvAll+=bgPv;
                }
                if(djPv>0){
                    djPvAll+=djPv;
                }
                if(bgUv>0){
                    bgUvAll+=bgUv;
                }
                if(djUv>0){
                    djUvAll+=djUv;
                }
                if(buyNums>0){
                    buyNumsAll+=buyNums;
                }
            }
    	}
    	
    	th.put("all_bg_pv", bgPvAll);
    	th.put("all_dj_pv", djPvAll);
    	th.put("all_bg_uv", bgUvAll);
    	th.put("all_dj_uv", djUvAll);
    	
    	if(djUvAll>0){
    		buyRate=(double)buyNumsAll/(double)djUvAll;
    		th.put("buy_rate", num.format(buyRate));
    	}else{
    		th.put("buy_rate","1");
    	}
    	int count=0;
        if(bgUvAll<=0){
        	th.put("ctr", "1");
        }else if(bgUvAll>0){
        	for(Map<String,Object> rs:list){
        		int bgUv=Integer.parseInt((String)rs.get("bg_uv")==null?"0":(String)rs.get("bg_uv"));
        		int djUv=Integer.parseInt((String)rs.get("dj_uv")==null?"0":(String)rs.get("dj_uv"));
        		if(bgUv>0){
        			double ctr1=(double)djUv/(double)bgUv;
        			ctr+=ctr1;
        			count++;
        		}
        	}
        	if(ctr/count>1){
        		th.put("ctr","100%");
        	}else{
        		double ctr2=(double)ctr/(double)count;
        		th.put("ctr",num.format(ctr2));
        	}
        }
        li.add(th);
    	
    	List<Map<String, Object>> mediaList=DBUtil.query(dbConn, mediaSql, false, new Object[] {});
    	List<Map<String, Object>> mediaDjList=DBUtil.query(dbConn, mediaDjSql, false, new Object[] {});
    	List<Map<String, Object>> hourList=DBUtil.query(dbConn, hourSql, false, new Object[] {});
    	List<Map<String, Object>> djList=DBUtil.query(dbConn, djSql, false, new Object[] {});
    	List<Map<String, Object>> areaList=DBUtil.query(dbConn, areaSql, false, new Object[] {});
    	List<Map<String, Object>> fianlTime=DBUtil.query(dbConn, fianlTimeSql, false, new Object[] {});

    	context.setResult("general", li);
    	context.setResult("mediaList",mediaList);
    	context.setResult("mediaDjList",mediaDjList);
    	context.setResult("hourList",hourList);
    	context.setResult("djList",djList);
    	context.setResult("areaList",areaList);
    	context.setResult("fianlTime",fianlTime);
    	
    	
		return RetCode.OK;
    	
    }
}
