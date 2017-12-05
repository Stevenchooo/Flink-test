package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetPersonType extends AuthRDBProcessor {
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
        String timeSql1=" and  pt_d<=DATE_FORMAT(\'"+endTime+"\',\'%Y%m%d\') AND pt_d>=DATE_FORMAT(\'"+beginTime+"\',\'%Y%m%d\')";
        
      //性别
        String sql="select dim_value as name,sum(cookies) as value from dw_cooperation_mkt_result_dm ";
        sql+=tmpSql+timeSql1+" and pt_dim=\'gender\' group by dim_value;";
        
        //年龄
        String ageSql="select dim_value as name,sum(cookies) as value from dw_cooperation_mkt_result_dm ";
        ageSql+=tmpSql+timeSql1+" and pt_dim=\'age\' group by dim_value;";
        
        //媒体轨迹
        String mediaSql="select dim_value as name,sum(cookies) as value from dw_cooperation_mkt_result_dm ";
        mediaSql+=tmpSql+timeSql1+" and pt_dim=\'media\' group by dim_value;";
        
        //行业
        String industrySql="select dim_value as name,sum(cookies) as value from dw_cooperation_mkt_result_dm ";
        industrySql+=tmpSql+timeSql1+" and pt_dim=\'industry\' group by dim_value;";
        //性别比例
        List<Map<String, Object>> gender=DBUtil.query(dbConn, sql, false, new Object[] {});
        
        
        String name=null;
        double value;
        //年龄比例
        List<Map<String, Object>> ageList =DBUtil.query(dbConn, ageSql, false, new Object[] {});
        double ageTotal=0.0;
        double ageRate;
        List<Map<String, Object>> age=new ArrayList<Map<String,Object>>();
        for(Map<String, Object> rs:ageList){
            value=Double.parseDouble((String)rs.get("value")==null?"0.0":(String)rs.get("value"));
            ageTotal+=value;
        }
        for(Map<String, Object> rs:ageList){
            Map<String, Object> t=new HashMap<String, Object>();
            name=(String)rs.get("name");
            value=Double.parseDouble((String)rs.get("value")==null?"0.0":(String)rs.get("value"));
            ageRate=value/ageTotal;
            t.put("name", name);
            t.put("value", (double)Math.round(ageRate * 10000) / 100.0);
            age.add(t);
        }
        
        //媒体轨迹比例
        List<Map<String, Object>> mediaList=DBUtil.query(dbConn, mediaSql, false, new Object[] {});
        double mediaTotal=0.0;
        double mediaRate;
        List<Map<String, Object>> media=new ArrayList<Map<String,Object>>();
        for(Map<String, Object> rs:mediaList){
            value=Double.parseDouble((String)rs.get("value")==null?"0.0":(String)rs.get("value"));
            mediaTotal+=value;
        }
        for(Map<String, Object> rs:mediaList){
            Map<String, Object> tt=new HashMap<String, Object>();
            name=(String)rs.get("name");
            value=Double.parseDouble((String)rs.get("value")==null?"0.0":(String)rs.get("value"));
            mediaRate=value/mediaTotal;
            tt.put("name", name);
            tt.put("value", (double)Math.round(mediaRate * 10000) / 100.0);
            media.add(tt);
        }
        
        //行业比例
        List<Map<String, Object>> industryList=DBUtil.query(dbConn, industrySql, false, new Object[] {});
        double industryTotal=0.0;
        double industryRate;
        List<Map<String, Object>> industry=new ArrayList<Map<String,Object>>();
        for(Map<String, Object> rs:industryList){
            value=Double.parseDouble((String)rs.get("value")==null?"0.0":(String)rs.get("value"));
            industryTotal+=value;
        }
        for(Map<String, Object> rs:industryList){
            Map<String, Object> h=new HashMap<String, Object>();
            name=(String)rs.get("name");
            value=Double.parseDouble((String)rs.get("value")==null?"0.0":(String)rs.get("value"));
            industryRate=value/industryTotal;
            h.put("name", name);
            h.put("value", (double)Math.round(industryRate * 10000) / 100.0);
            industry.add(h);
        }

        context.setResult("gender", gender);
        context.setResult("age", age);
        context.setResult("media", media);  
        context.setResult("industry", industry);
        return  RetCode.OK;
    }
}
