package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.dao.TransPicReportDao;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class TransPicReprot extends AuthRDBProcessor
{
    public int process(MethodContext context, DBConnection dbConn)
    {
        List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
        rs = TransPicReportDao.getTotalResult(context, dbConn);
        Map<String, Object> th = new HashMap<String, Object>();
        List<Map<String, Object>> li = new ArrayList<Map<String, Object>>();
        double ctrc = 0.0;
        double ctr = 0.0;
        int bgUvAll = 0;
        int djUvAll = 0;
        int landingUvAll = 0;
        int bespeakNumsAll = 0;
        int orderCountAll = 0;
        int realPayCountAll = 0;
        if (rs != null)
        {
            for (Map<String, Object> r : rs)
            {
                int bgUv = Integer.parseInt((String)r.get("bg_uv") == null ? "0" : (String)r.get("bg_uv"));
                int djUv = Integer.parseInt((String)r.get("dj_uv") == null ? "0" : (String)r.get("dj_uv"));
                int landingUv =
                    Integer.parseInt((String)r.get("landing_uv") == null ? "0" : (String)r.get("landing_uv"));
                int bespeakNums =
                    Integer.parseInt((String)r.get("bespeak_nums") == null ? "0" : (String)r.get("bespeak_nums"));
                int orderCount =
                    Integer.parseInt((String)r.get("order_count") == null ? "0" : (String)r.get("order_count"));
                int realPayCount =
                    Integer.parseInt((String)r.get("real_order_count") == null ? "0"
                        : (String)r.get("real_order_count"));
                bgUvAll += bgUv;
                djUvAll += djUv;
                landingUvAll += landingUv;
                bespeakNumsAll += bespeakNums;
                orderCountAll += orderCount;
                realPayCountAll += realPayCount;
                if (bgUv > 0)
                {
                    double ctr1 = (double)djUv / (double)bgUv;
                    ctr += ctr1;
                    ctrc++;
                }
            }
            th.put("all_gb_uv", bgUvAll);
            th.put("all_dj_uv", djUvAll);
            th.put("all_landing_uv", landingUvAll);
            th.put("all_bespeak_nums", bespeakNumsAll);
            th.put("all_order_count", orderCountAll);
            th.put("all_pay_count", realPayCountAll);
            //ctr
            if (ctrc > 0.0)
            {
                if (ctr / ctrc >= 1)
                {
                    th.put("ctr", 100);
                }
                else
                {
                    double ctr2 = (double)ctr / (double)ctrc;
                    th.put("ctr", (double)(Math.round(ctr2 * 10000) / 100.0));
                }
            }
            else
            {
                th.put("ctr", 100);
            }
            
            //到达率
            if (djUvAll > 0)
            {
                if (landingUvAll / djUvAll >= 1)
                {
                    th.put("landingRate", 100);
                }
                else
                {
                    double landingRate2 = (double)landingUvAll / (double)djUvAll;
                    th.put("landingRate", (double)(Math.round(landingRate2 * 10000) / 100.0));
                }
            }
            else if (landingUvAll > 0)
            {
                th.put("landingRate", 100);
            }
            else
            {
                th.put("landingRate", 0);
            }
            
            //预约率
            if (landingUvAll > 0)
            {
                if (bespeakNumsAll / landingUvAll >= 1)
                {
                    th.put("bespeakRate", 100);
                }
                else
                {
                    double bespeakRate2 = (double)bespeakNumsAll / (double)landingUvAll;
                    th.put("bespeakRate", (double)(Math.round(bespeakRate2 * 10000) / 100.0));
                }
            }
            else if (bespeakNumsAll > 0)
            {
                th.put("bespeakRate", 100);
            }
            else
            {
                th.put("bespeakRate", 0);
            }
            
            //下单率
            if (landingUvAll > 0)
            {
                if (orderCountAll / landingUvAll >= 1)
                {
                    th.put("orderRate", 100);
                }
                else
                {
                    double orderRate2 = (double)orderCountAll / (double)landingUvAll;
                    th.put("orderRate", (double)(Math.round(orderRate2 * 10000) / 100.0));
                }
            }
            else if (orderCountAll > 0)
            {
                th.put("orderRate", 100);
            }
            else
            {
                th.put("orderRate", 0);
            }
            
            //购买率
            if (orderCountAll > 0)
            {
                if (realPayCountAll / orderCountAll >= 1)
                {
                    th.put("buyRate", 100);
                }
                
                else if (realPayCountAll / orderCountAll < 0)
                {
                    th.put("buyRate", 0);
                }
                
                else
                {
                    double buyRate2 = (double)realPayCountAll / (double)orderCountAll;
                    th.put("buyRate", (double)(Math.round(buyRate2 * 10000) / 100.0));
                }
                
            }
            else if (realPayCountAll > 0)
            {
                th.put("buyRate", 100);
            }
            else
            {
                th.put("buyRate", 0);
            }
            
            if (bgUvAll != 0 || djUvAll != 0 || landingUvAll != 0 || bespeakNumsAll != 0 || orderCountAll != 0
                || realPayCountAll != 0)
            {
                li.add(th);
            }
        }
        
        //整体转化
        List<Map<String, Object>> rs1 = new ArrayList<Map<String, Object>>();
        rs1 = TransPicReportDao.getResult(context, dbConn);
        List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
        if (rs1 != null)
        {
            for (Map<String, Object> r : rs1)
            {
                String pt_d = (String)r.get("pt_d");
                if (pt_d == null)
                {
                    continue;
                }
                Map<String, Object> total = new HashMap<String, Object>();
                int djUv = Integer.parseInt((String)r.get("dj_uv") == null ? "0" : (String)r.get("dj_uv"));
                int realPayCount =
                    Integer.parseInt((String)r.get("real_pay_count") == null ? "0" : (String)r.get("real_pay_count"));
                
                if (djUv > 0)
                {
                    if (realPayCount / djUv >= 1)
                    {
                        total.put("totalTranRate", 100);
                    }
                    
                    else if (realPayCount / djUv < 0)
                    {
                        total.put("totalTranRate", 0);
                    }
                    
                    else
                    {
                        total.put("totalTranRate",
                            (double)(Math.round((double)realPayCount / (double)djUv * 10000) / 100.0));
                    }
                    
                }
                else if (realPayCount > 0)
                {
                    total.put("totalTranRate", 100);
                }
                else
                {
                    total.put("totalTranRate", 0);
                }
                total.put("pt_d", pt_d);
                totalList.add(total);
            }
        }
        
        //每日比例
        List<Map<String, Object>> rs2 = new ArrayList<Map<String, Object>>();
        rs2 = TransPicReportDao.getDayResult(context, dbConn);
        
        List<Map<String, Object>> hourList = new ArrayList<Map<String, Object>>();
        if (rs2 != null)
        {
            for (Map<String, Object> s : rs2)
            {
                String time = (String)s.get("pt_d");
                if (time == null)
                {
                    continue;
                }
                Map<String, Object> hour = new HashMap<String, Object>();
                String media_name = (String)s.get("media_name");
                int bgUv = Integer.parseInt((String)s.get("bg_uv") == null ? "0" : (String)s.get("bg_uv"));
                int djUv = Integer.parseInt((String)s.get("dj_uv") == null ? "0" : (String)s.get("dj_uv"));
                int landingUv =
                    Integer.parseInt((String)s.get("landing_uv") == null ? "0" : (String)s.get("landing_uv"));
                int orderCount =
                    Integer.parseInt((String)s.get("order_count") == null ? "0" : (String)s.get("order_count"));
                int realPayCount =
                    Integer.parseInt((String)s.get("real_pay_count") == null ? "0" : (String)s.get("real_pay_count"));
                hour.put("medaiName", media_name);
                
                if (bgUv > 0)
                {
                    if ((double)djUv / (double)bgUv > 1)
                    {
                        hour.put("ctr", 100);
                    }
                    else
                    {
                        hour.put("ctr", (double)(Math.round((double)djUv / (double)bgUv * 10000) / 100.0));
                    }
                }
                else if (djUv > 0)
                {
                    hour.put("ctr", 100);
                }
                else
                {
                    hour.put("ctr", 0);
                }
                
                if (djUv > 0)
                {
                    if ((double)landingUv / (double)djUv > 1)
                    {
                        hour.put("landingRate", 100);
                    }
                    else
                    {
                        hour.put("landingRate", (double)(Math.round((double)landingUv / (double)djUv * 10000) / 100.0));
                    }
                }
                else if (landingUv > 0)
                {
                    hour.put("landingRate", 100);
                }
                else
                {
                    hour.put("landingRate", 0);
                }
                
                if (landingUv > 0)
                {
                    if ((double)orderCount / (double)landingUv > 1)
                    {
                        hour.put("orderRate", 100);
                    }
                    else
                    {
                        hour.put("orderRate",
                            (double)(Math.round((double)orderCount / (double)landingUv * 10000) / 100.0));
                    }
                }
                else if (orderCount > 0)
                {
                    hour.put("orderRate", 100);
                }
                else
                {
                    hour.put("orderRate", 0);
                }
                
                if (orderCount > 0)
                {
                    if ((double)realPayCount / (double)orderCount > 1)
                    {
                        hour.put("buyRate", 100);
                        
                    }
                    else if ((double)realPayCount / (double)orderCount < 0)
                    {
                        hour.put("buyRate", 0);
                    }
                    else
                    {
                        hour.put("buyRate",
                            (double)(Math.round((double)realPayCount / (double)orderCount * 10000) / 100.0));
                    }
                }
                else if (realPayCount > 0)
                {
                    hour.put("buyRate", 100);
                }
                else
                {
                    hour.put("buyRate", 0);
                }
                hour.put("time", time);
                hourList.add(hour);
            }
        }
        //漏斗
        context.setResult("transTotal", li);
        //整体转化
        context.setResult("totalTran", totalList);
        //每天比例
        context.setResult("dailyresult", hourList);
        return RetCode.OK;
    }
    
}
