package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetHotData extends AuthRDBProcessor {
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
        
        //页面排行
        String sql="SELECT "+
                " url,"+
                " landing_uv,"+
                " landing_pv,"+
                " ipcount,"+
                " entercount,"+
                " downuv,"+
                " IF(landing_uv='0' OR landing_uv IS NULL , '0' , format(visit_time/landing_uv,2)) AS avg_visit_time,"+
                " IF(landing_uv='0' OR landing_uv IS NULL , '0' , format(downuv/landing_uv,4)) AS bnous_rate,"+
                " pt_d"+
                " FROM "+           
                " (SELECT  "+
                " url AS url, "+
                " SUM(t1.landing_uv) AS landing_uv, "+
                " SUM(t1.landing_pv) AS landing_pv, "+
                " SUM(t1.ipcount) AS ipcount, "+
                " SUM(t1.entercount) AS entercount, "+
                " SUM(t1.downuv) AS downuv, "+
                " SUM(visit_time) AS visit_time, "+
                " MAX(pt_d) AS pt_d "+
                " FROM "+
                " dw_mkt_hot_result_dm t1 ";
        sql+=timeSql1;
        sql+=" and cid IN (SELECT distinct cid FROM  dim_mkt_base_info_ds ";
        sql+= tmpSql ;
        sql+=" )  and activtiy_id in (select distinct activtiy_id from dim_mkt_base_info_ds ";
        sql+=tmpSql ;
        sql+=" ) GROUP BY url ) t ORDER BY landing_uv DESC LIMIT 20 ;";
        
        //产品页面排行
        String productSql="SELECT "+
                " url,"+
                " landing_uv,"+
                " landing_pv,"+
                " ipcount,"+
                " entercount,"+
                " downuv,"+
                " IF(landing_uv='0' OR landing_uv IS NULL , '0' , format(visit_time/landing_uv,2)) AS avg_visit_time,"+
                " IF(landing_uv='0' OR landing_uv IS NULL , '0' , format(downuv/landing_uv,4)) AS bnous_rate,"+
                " pt_d"+
                " FROM "+           
                " (SELECT  "+
                " url AS url, "+
                " SUM(t1.landing_uv) AS landing_uv, "+
                " SUM(t1.landing_pv) AS landing_pv, "+
                " SUM(t1.ipcount) AS ipcount, "+
                " SUM(t1.entercount) AS entercount, "+
                " SUM(t1.downuv) AS downuv, "+
                " SUM(visit_time) AS visit_time, "+
                " MAX(pt_d) AS pt_d "+
                " FROM "+
                " dw_mkt_hot_product_result_dm t1 ";
        productSql+=timeSql1;
        productSql+=" and cid IN (SELECT distinct cid FROM  dim_mkt_base_info_ds ";
        productSql+= tmpSql ;
        productSql+=" )  and activtiy_id in (select distinct activtiy_id from dim_mkt_base_info_ds ";
        productSql+=tmpSql ;
        productSql+=" ) GROUP BY url ) t ORDER BY landing_uv DESC LIMIT 20 ;";
        
        
        List<Map<String, Object>> url=DBUtil.query(dbConn, sql, false, new Object[] {});
        List<Map<String, Object>> product=DBUtil.query(dbConn, productSql, false, new Object[] {});

        context.setResult("url", url);
        context.setResult("product", product);
        return RetCode.OK;
    }
}
