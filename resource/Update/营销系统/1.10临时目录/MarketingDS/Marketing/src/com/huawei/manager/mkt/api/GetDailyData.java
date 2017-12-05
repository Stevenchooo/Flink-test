package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetDailyData extends AuthRDBProcessor{
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
        String timeSql1="WHERE pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\') AND HOUR!=\'NA\' AND city=\'NA\'";
        //按日曲线
        String sql="select "+
                "tt.media_name AS media_name, "+
                "IF(tt.all_bg_uv IS NULL,0,tt.all_bg_uv) AS all_bg_uv, "+
                "IF(tt.all_bg_pv IS NULL,0,tt.all_bg_pv) AS all_bg_pv, "+
                "IF(tt.all_dj_pv IS NULL,0,tt.all_dj_pv) AS all_dj_pv, "+
                "IF(tt.all_dj_uv IS NULL,0,tt.all_dj_uv) AS all_dj_uv,"+
                "tt.pt_d AS pt_d "+
                "FROM (  "+
                " SELECT "+
                " t.media_name  AS media_name, "+
                " SUM(t1.bg_uv) AS all_bg_uv, "+
                " SUM(t1.bg_pv) AS all_bg_pv, "+
                " SUM(t1.dj_pv) AS all_dj_pv, "+
                " SUM(t1.dj_uv) AS all_dj_uv, "+
                " t1.pt_d       AS pt_d "+
                " FROM "+
                " (SELECT sid,cid,media_id,media_name FROM dim_mkt_base_info_ds ";
        sql+=tmpSql;
        sql+=" ) t "+
                " LEFT OUTER JOIN "+
                " (SELECT sid,bg_pv,bg_uv,dj_pv,dj_uv,pt_d FROM dw_mkt_result_dm ";
        sql+=timeSql;
        sql+=") t1 "+
                " ON t.sid=t1.sid "+
                " GROUP BY t.media_name,t1.pt_d "+
                " UNION ALL "+
                " SELECT "+
                " \'total\'    AS media_name, "+
                " SUM(t1.bg_uv) AS all_bg_uv,"+
                " SUM(t1.bg_pv) AS all_bg_pv,"+
                " SUM(t1.dj_pv) AS all_dj_pv,"+
                " SUM(t1.dj_uv) AS all_dj_uv,"+
                " t1.pt_d       AS pt_d "+
                " FROM "+
                " (SELECT sid FROM dim_mkt_base_info_ds ";
        sql+=tmpSql;
        sql+=" ) t "+
                " LEFT OUTER JOIN "+
                " (SELECT sid,bg_pv,bg_uv,dj_pv,dj_uv,pt_d FROM dw_mkt_result_dm ";
        sql+=timeSql;
        sql+=" ) t1 "+
                " ON t.sid=t1.sid "+
                " GROUP BY media_name,t1.pt_d "+
                " ) tt; ";
        
        
        //分时曲线
        String hourSql="select "+
                "tt.media_name AS media_name, "+
                "IF(tt.all_bg_uv IS NULL,0,tt.all_bg_uv) AS all_bg_uv, "+
                "IF(tt.all_bg_pv IS NULL,0,tt.all_bg_pv) AS all_bg_pv, "+
                "IF(tt.all_dj_pv IS NULL,0,tt.all_dj_pv) AS all_dj_pv, "+
                "IF(tt.all_dj_uv IS NULL,0,tt.all_dj_uv) AS all_dj_uv,"+
                "tt.hour AS hour "+
                "FROM (  "+
                " SELECT "+
                " t.media_name  AS media_name, "+
                " SUM(t1.bg_uv) AS all_bg_uv, "+
                " SUM(t1.bg_pv) AS all_bg_pv, "+
                " SUM(t1.dj_pv) AS all_dj_pv, "+
                " SUM(t1.dj_uv) AS all_dj_uv, "+
                " t1.hour       AS hour "+
                " FROM "+
                " (SELECT sid,cid,media_id,media_name FROM dim_mkt_base_info_ds ";
        hourSql+=tmpSql;
        hourSql+=" ) t "+
                " LEFT OUTER JOIN "+
                " (SELECT sid,bg_pv,bg_uv,dj_pv,dj_uv,hour FROM dw_mkt_result_dm ";
        hourSql+=timeSql1;
        hourSql+=") t1 "+
                " ON t.sid=t1.sid "+
                " GROUP BY t.media_name,t1.hour "+
                " UNION ALL "+
                " SELECT "+
                " \'total\'    AS media_name, "+
                " SUM(t1.bg_uv) AS all_bg_uv,"+
                " SUM(t1.bg_pv) AS all_bg_pv,"+
                " SUM(t1.dj_pv) AS all_dj_pv,"+
                " SUM(t1.dj_uv) AS all_dj_uv,"+
                " t1.hour       AS hour "+
                " FROM "+
                " (SELECT sid FROM dim_mkt_base_info_ds ";
        hourSql+=tmpSql;
        hourSql+=" ) t "+
                " LEFT OUTER JOIN "+
                " (SELECT sid,bg_pv,bg_uv,dj_pv,dj_uv,hour FROM dw_mkt_result_dm ";
        hourSql+=timeSql1;
        hourSql+=" ) t1 "+
                " ON t.sid=t1.sid "+
                " GROUP BY media_name,t1.hour "+
                " ) tt; ";
        
        
        //按日曲线
        List<Map<String, Object>> list = DBUtil.query(dbConn, sql, false, new Object[] {});
        List<Map<String, Object>> hourList = DBUtil.query(dbConn, hourSql, false, new Object[] {});
        
        //按日曲线
        context.setResult("dailyList", list);
        context.setResult("hourList", hourList);
        return RetCode.OK;
    }
}
