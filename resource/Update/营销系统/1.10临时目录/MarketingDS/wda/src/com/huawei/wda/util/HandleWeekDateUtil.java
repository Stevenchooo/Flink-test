package com.huawei.wda.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 处理按周、按月的结果集
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月26日]
 * @see [相关类/方法]
 */
public abstract class HandleWeekDateUtil
{
    /**
     * 按周的情况下，处理脚本里返回来的结果集 <功能详细描述>
     * 
     * @param a
     *            a集合
     * @param fromDate
     *            开始日期
     * @param toDate
     *            结束日期
     * @return List<Map>
     * @see [类、类#方法、类#成员]
     */

    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    public static List<Map> obtainTotalWeekDate(List a, String fromDate,
            String toDate)
    {
        List<Map> mapList = new ArrayList<Map>();
        if (null != a && !a.isEmpty())
        {
            // 重置第一个list元素的开始时间
            String xData = (String) ((Map) a.get(0)).get("x");
            if (null != xData)
            {
                String[] tempDate = xData.split("-");
                String fromDateFirst = tempDate[0];
                Date fromDateCondition = DateUtil.strToDate(DateUtil
                        .changeDateFormat(fromDate));
                Date fromDateFirstCondition = DateUtil.strToDate(fromDateFirst);
                // 选择的fromDate大于a集合中的第一个元素x的开始时间
                if (fromDateCondition.getTime() > fromDateFirstCondition
                        .getTime())
                {
                    ((Map) a.get(0)).remove("x");
                    ((Map) a.get(0)).put("x",
                            DateUtil.changeDateFormat(fromDate) + "-"
                                    + tempDate[1]);
                }
            }
            String xDataSecond = (String) ((Map) a.get(a.size() - 1)).get("x");
            if (null != xDataSecond)
            {
                String[] tempDateSecond = xDataSecond.split("-");
                String endDateFirst = tempDateSecond[1];
                Date toDateCondition = DateUtil.strToDate(DateUtil
                        .changeDateFormat(toDate));
                Date endDateFirstCondtion = DateUtil.strToDate(endDateFirst);
                // 结束时间小于a集合中最后一个元素的x的结束时间
                if (toDateCondition.getTime() < endDateFirstCondtion.getTime())
                {
                    ((Map) a.get(a.size() - 1)).remove("x");
                    ((Map) a.get(a.size() - 1)).put("x", tempDateSecond[0]
                            + "-" + DateUtil.changeDateFormat(toDate));
                }

            }
            // 获取选择的时间段自然周分割的List
            List<String> oldList = DateUtil.obtainWeekDate(fromDate, toDate);
            for (int i = 0; i < oldList.size(); i++)
            {
                String x = oldList.get(i);
                Map map = new HashMap();
                map.put("x", oldList.get(i));
                map.put("y", 0);
                for (int j = 0; j < a.size(); j++)
                {
                    if (x.equals(((Map) a.get(j)).get("x")))
                    {
                        map.remove("y");
                        map.put("y", ((Map) a.get(j)).get("y"));
                    }
                }
                mapList.add(map);
            }
        }

        return mapList;
    }

    /**
     * 按月or按日的情况下，对返回的结果集进行处理 <功能详细描述>
     * 
     * @param a
     *            a集合
     * @param fromDate
     *            开始日期
     * @param toDate
     *            结束日期
     * @param isAccordingMonth
     *            是否是按月
     * @return List<Map>
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    public static List<Map> obtainTotalMonthOrTotalDay(List a, String fromDate,
            String toDate, boolean isAccordingMonth)
    {
        List<Map> mapList = new ArrayList<Map>();
        List<String> list = DateUtil.obtainMonthListOrDayList(fromDate, toDate,
                isAccordingMonth);
        if (null != a && !a.isEmpty())
        {
            for (int i = 0; i < list.size(); i++)
            {
                String x = list.get(i);
                Map map = new HashMap();
                map.put("x", list.get(i));
                map.put("y", 0);
                for (int j = 0; j < a.size(); j++)
                {
                    if (x.equals(((Map) a.get(j)).get("x")))
                    {

                        map.remove("y");
                        map.put("y", ((Map) a.get(j)).get("y"));
                    }
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

}
