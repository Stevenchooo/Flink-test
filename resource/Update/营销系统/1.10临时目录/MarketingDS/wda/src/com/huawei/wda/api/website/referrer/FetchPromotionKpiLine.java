package com.huawei.wda.api.website.referrer;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.HandleWeekDateUtil;
import com.huawei.wda.util.KpiMap;

/**
 * 查询数据FetchPromotionKpiLine <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchPromotionKpiLine extends AuthRDBProcessor
{

    /**
     * FetchPromotionKpiLine在数据脚本执行之前调用，提供一个机会来处理其他事情
     * 比如给statement添加不在parameters中的其他参数
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

        String fromDate = null;
        String toDate = null;
        Map<String, Object> reqParameters = context.getParameters();
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
        }
        else
        {
            return WRONG_PARAMETER;
        }

        statement.setString(6, fromDate);
        statement.setString(7, toDate);

        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);

        return OK;
    }

    /**
     * FetchPromotionKpiLine调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
        String curKpi = JsonUtil.getAsStr(reqParameters, "curKpi");
        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");
        String curKpiName = KpiMap.getKpiName(curKpi);

        Map result = new HashMap();
        List a = (ArrayList) context.getResults().get("data");
        if (null != a && !a.isEmpty())
        {
            if (!fromDate.equals(toDate))
            {
                a = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(a, fromDate,
                        toDate, false);
            }
            result.put("values", a);
            result.put("key", curKpiName);
        }

        List dataDet = (ArrayList) context.getResults().get("dataDet");
        if (null != dataDet && !dataDet.isEmpty())
        {
            List referName = new ArrayList();
            for (int i = 0; i < dataDet.size(); i++)
            {
                String name = (String) ((Map) dataDet.get(i))
                        .get("referrer_name");
                if (!referName.contains(name))
                {
                    referName.add(name);
                }
            }
            Map<String, List> referMap = new HashMap<String, List>();

            for (int j = 0; j < referName.size(); j++)
            {
                List value = new ArrayList();
                for (int i = 0; i < dataDet.size(); i++)
                {
                    String name = (String) ((Map) dataDet.get(i))
                            .get("referrer_name");
                    if (name.equals(referName.get(j)))
                    {
                        Map formatMap = new HashMap();
                        formatMap.put("x", ((Map) dataDet.get(i)).get("x"));
                        formatMap.put("y", ((Map) dataDet.get(i)).get("y"));
                        value.add(formatMap);
                    }
                }
                if (!fromDate.equals(toDate))
                {
                    value = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(value,
                            fromDate, toDate, false);
                }
                referMap.put((String) referName.get(j), value);
            }
            context.setResult("dataDet", referMap);
        }
        context.setResult("data", result);

        return OK;
    }
}