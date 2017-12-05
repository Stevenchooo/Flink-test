package com.huawei.wda.api.visitor;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.HandleWeekDateUtil;
import com.huawei.wda.util.KpiMap;

/**
 * 访问趋势KPI.
 */
public class VisitorTrendKpi extends AuthRDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();

    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数.
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @throws SQLException
     *             SQLException
     * @return 0
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String curKpi2 = JsonUtil.getAsStr(reqParameters, "curKpi2");
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        String fromDate = null;
        String toDate = null;
        // 环比时间
        String huanBiFromDate = null;
        String huanBiToDate = null;

        // 如果含有#号
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 访问趋势KPI
            fromDate = tempDate[0];
            toDate = tempDate[1];
            if (huanBiDate.contains("#"))
            {
                String[] tempDate1 = huanBiDate.split("#");
                // 从前端传回的格式固定，分隔直接赋值
                huanBiFromDate = tempDate1[0];
                huanBiToDate = tempDate1[1];
            }
        }
        else
        {
            // 访问趋势KPI参数错误
            LOG.debug("wrong parameter");
            return WRONG_PARAMETER;
        }

        statement.setString(7, curKpi2);
        statement.setString(8, fromDate);
        statement.setString(9, toDate);
        statement.setString(10, huanBiFromDate);
        statement.setString(11, huanBiToDate);

        try
        {
            statement
                    .setString(12, URLDecoder.decode(
                            JsonUtil.getAsStr(reqParameters, "province_name"),
                            "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.debug("VisitorTrendKpi beforeProcess: UnsupportedEncodingException");
        }

        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);
        reqParameters.put("huanBiFromDate", huanBiFromDate);
        reqParameters.put("huanBiToDate", huanBiToDate);

        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集.
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @throws SQLException
     *             SQLException
     * @return 0
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
        String huanBiFromDate = JsonUtil.getAsStr(reqParameters,
                "huanBiFromDate");
        String huanBiToDate = JsonUtil.getAsStr(reqParameters, "huanBiToDate");

        String curKpi1 = JsonUtil.getAsStr(reqParameters, "curKpi1");
        String curKpi2 = JsonUtil.getAsStr(reqParameters, "curKpi2");
        // kpi的名字
        String curKpiName1 = KpiMap.getKpiName(curKpi1);
        String curKpiName2 = KpiMap.getKpiName(curKpi2);
        // 环比日期
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
        int timeDim = JsonUtil.getAsInt(reqParameters, "time_dim");

        String key1 = null;
        String key2 = null;

        if (StringUtils.isNotBlank(huanBiDate))
        {
            if (fromDate.equals(toDate))
            {
                key1 = fromDate;
                key2 = huanBiFromDate;
            }
            else
            {
                // 选择多天，则拼接展示
                key1 = fromDate + " - " + toDate;
                key2 = huanBiFromDate + " - " + huanBiToDate;
            }
        }
        else
        {
            key1 = curKpiName1;
            key2 = curKpiName2;
        }

        List a = (ArrayList) context.getResults().get("data");
        // 按周情况，特殊处理
        List<Map> mapList = handleResults(timeDim, fromDate, toDate, a);

        if (a != null)
        {
            if (a.size() != 1
                    || !"".equals((String) (((Map) a.get(0)).get("x"))))
            {
                // m
                Map m = new HashMap();
                judgeTimeDim(mapList, timeDim, m, a);
                m.put("key", key1);
                context.setResult("data", m);
            }
        }

        List b = (ArrayList) context.getResults().get("hb_data");
        // 按周环比的情况，特殊处理
        Map<String, String> map = obtainParametersMap(huanBiDate,
                huanBiFromDate, huanBiToDate, fromDate, toDate);

        List<Map> mapListHuanBi = handleHuanBiResults(timeDim, map, b, curKpi2);
        if (b != null)
        {
            if (b.size() != 1
                    || !"".equals((String) (((Map) b.get(0)).get("x"))))
            {
                Map m2 = new HashMap();
                judgeTimeDim(mapListHuanBi, timeDim, m2, b);
                m2.put("key", key2);
                context.setResult("hb_data", m2);
            }
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