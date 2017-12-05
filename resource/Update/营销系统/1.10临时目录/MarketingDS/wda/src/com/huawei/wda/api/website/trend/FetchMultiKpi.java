package com.huawei.wda.api.website.trend;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.HandleWeekDateUtil;
import com.huawei.wda.util.KpiMap;

/**
 * 查询数据FetchMultiKpi <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchMultiKpi extends AuthRDBProcessor
{

    /**
     * FetchMultiKpi在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String fromDate = null;
        String toDate = null;
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 直接获取curKpi2
        String curKpi2 = JsonUtil.getAsStr(reqParameters, "curKpi2");
        // 环比的起止日期
        String huanBiFromDate = null;
        String huanBiToDate = null;

        if (periodTime.contains("#"))
        {
            // #号键分成一个数组
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
            if (StringUtils.isNotBlank(huanBiDate))
            {
                // 分割成数组
                String[] tempDate1 = huanBiDate.split("#");
                huanBiFromDate = tempDate1[0];
                huanBiToDate = tempDate1[1];
            }
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }
        // 直接赋值curKpi2
        statement.setString(7, curKpi2);
        // statement赋值
        statement.setString(8, fromDate);
        statement.setString(9, toDate);
        statement.setString(10, huanBiFromDate);
        statement.setString(11, huanBiToDate);
        // 往请求参数里面进行赋值，fromDate等四个参数
        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);
        // 往请求参数里面进行赋值，fromDate等四个参数
        reqParameters.put("huanBiFromDate", huanBiFromDate);
        reqParameters.put("huanBiToDate", huanBiToDate);

        return OK;
    }

    /**
     * FetchMultiKpi调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");
        // 环比结束日期
        String huanBiToDate = JsonUtil.getAsStr(reqParameters, "huanBiToDate");
        // 当前kpi
        String curKpi1 = JsonUtil.getAsStr(reqParameters, "curKpi1");
        String curKpi2 = JsonUtil.getAsStr(reqParameters, "curKpi2");
        String curKpiName1 = KpiMap.getKpiName(curKpi1);
        // kpiname名称
        String curKpiName2 = KpiMap.getKpiName(curKpi2);
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
        // 环比开始日期
        String huanBiFromDate = JsonUtil.getAsStr(reqParameters,
                "huanBiFromDate");
        int timeDim = JsonUtil.getAsInt(reqParameters, "time_dim");

        String key1 = null;
        String key2 = null;

        // 环比日期不为空的情况下，给赋值的内容是不一样的
        if (StringUtils.isNotBlank(huanBiDate))
        {
            // 开始日期等于结束的情况
            if (fromDate.equals(toDate))
            {
                key1 = fromDate;
                key2 = huanBiFromDate;
            }
            else
            {
                // 不包括最后一天
                key1 = fromDate + " - " + toDate;
                key2 = huanBiFromDate + " - " + huanBiToDate;
            }
        }
        else
        {
            // 进行赋值，这种是不环比的情况
            key1 = curKpiName1;
            key2 = curKpiName2;
        }
        // 获取非环比日期的数据集合a
        List a = (ArrayList) context.getResults().get("data");

        // 按周的情况，特殊处理
        List<Map> mapList = handleResults(timeDim, fromDate, toDate, a);

        if (a != null)
        {

            if (a.size() != 1
                    || !"".equals((String) (((Map) a.get(0)).get("x"))))
            {
                Map m = new HashMap();
                judgeTimeDim(mapList, timeDim, m, a);
                m.put("key", key1);
                context.setResult("data", m);
            }
        }
        // 获取环比的数据，集合b
        List b = (ArrayList) context.getResults().get("hb_data");
        Map<String, String> map = obtainParametersMap(huanBiDate,
                huanBiFromDate, huanBiToDate, fromDate, toDate);
        List<Map> mapListHuanBi = handleHuanBiResults(timeDim, map, b, curKpi2);
        // 当集合有数据的情况下
        Map m2 = new HashMap();
        if (b != null)
        {

            if (b.size() != 1
                    || !"".equals((String) (((Map) b.get(0)).get("x"))))
            {
                // 挨个对键进行赋值
                judgeTimeDim(mapListHuanBi, timeDim, m2, b);
                m2.put("key", key2);
            }
            context.setResult("hb_data", m2);
        }

        return OK;
    }

    @SuppressWarnings("rawtypes")
    private List<Map> handleResults(int timeDim, String fromDate,
            String toDate, List a)
    {
        List<Map> mapList = null;
        if (timeDim == 2)
        {
            mapList = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(a,
                    fromDate, toDate, false);
        }
        else if (timeDim == 3)
        {
            mapList = HandleWeekDateUtil.obtainTotalWeekDate(a, fromDate,
                    toDate);
        }
        else if (timeDim == 4)
        {
            mapList = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(a,
                    fromDate, toDate, true);
        }
        return mapList;
    }

    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    private void judgeTimeDim(List<Map> mapList, int timeDim, Map m, List a)
    {
        if (timeDim == 1)
        {
            m.put("values", a);
        }
        else
        {
            m.put("values", mapList);
        }
    }

    @SuppressWarnings("rawtypes")
    private List<Map> handleHuanBiResults(int timeDim, Map<String, String> map,
            List b, String curKpi2)
    {
        List<Map> mapListHuanBi = null;
        if (StringUtils.isNotBlank(map.get("huanBiDate"))
                && StringUtils.isBlank(curKpi2))
        {
            if (timeDim == 2)
            {
                mapListHuanBi = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(
                        b, map.get("huanBiFromDate"), map.get("huanBiToDate"),
                        false);
            }
            else if (timeDim == 3)
            {
                mapListHuanBi = HandleWeekDateUtil.obtainTotalWeekDate(b,
                        map.get("fromDate"), map.get("toDate"));
            }
            else if (timeDim == 4)
            {
                mapListHuanBi = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(
                        b, map.get("huanBiFromDate"), map.get("huanBiToDate"),
                        true);
            }
        }
        else if (StringUtils.isBlank(map.get("huanBiDate"))
                && StringUtils.isNotBlank(curKpi2))
        {
            if (timeDim == 2)
            {
                mapListHuanBi = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(
                        b, map.get("fromDate"), map.get("toDate"), false);
            }
            else if (timeDim == 3)
            {
                mapListHuanBi = HandleWeekDateUtil.obtainTotalWeekDate(b,
                        map.get("fromDate"), map.get("toDate"));
            }
            else if (timeDim == 4)
            {
                mapListHuanBi = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(
                        b, map.get("fromDate"), map.get("toDate"), true);
            }
        }
        return mapListHuanBi;
    }

    private Map<String, String> obtainParametersMap(String huanBiDate,
            String huanBiFromDate, String huanBiToDate, String fromDate,
            String toDate)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("huanBiDate", huanBiDate);
        map.put("huanBiFromDate", huanBiFromDate);
        map.put("huanBiToDate", huanBiToDate);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        return map;

    }
}