package com.huawei.wda.util;

import java.util.HashMap;
import java.util.Map;

/**
 * KpiMap.
 */
public class KpiMap
{

    /**
     * 设置对应中文名
     */
    @SuppressWarnings(
    {"unchecked", "rawtypes", "serial"})
    public static final Map MAP = new HashMap()
    {
        {
            put("pv", "浏览量(PV)");
            put("visits", "访问次数");
            put("uv", "访客数(UV)");
            put("nuv", "新访客数");
            put("nuv_rate", "新访客比率");
            put("ip", "IP数");
            put("avg_visit_time", "平均访问时长");
            put("avg_visit_pages", "平均访问页面");

            put("landing_pages", "入口页次数");
            put("up_down_pages", "贡献下游浏览量");
            put("exit_pages", "退出页次数");

            put("orderQuantity", "订单数");
            put("orderAmount", "订单金额");
            put("orderConversation", "订单转化率");

        }
    };

    /**
     * 获取kpi名称.
     * 
     * @param key
     *            key
     * @return String
     */
    public static String getKpiName(String key)
    {

        if (key == null || key.length() == 0)
        {
            return "";
        }

        return (String) MAP.get(key);

    }

}
