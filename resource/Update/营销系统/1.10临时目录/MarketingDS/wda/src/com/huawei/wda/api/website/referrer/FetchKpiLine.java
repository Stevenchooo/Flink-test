package com.huawei.wda.api.website.referrer;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.HandleWeekDateUtil;

/**
 * 查询数据FetchKpiLine <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchKpiLine extends AuthRDBProcessor
{

    /**
     * FetchKpiLine在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
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
        // 请求参数map
        Map<String, Object> reqParameters = context.getParameters();

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            fromDate = tempDate[0];
            toDate = tempDate[1];
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }

        statement.setString(5, fromDate);
        statement.setString(6, toDate);

        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);

        return OK;
    }

    /**
     * FetchKpiLine调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
        SerializerFeature f = SerializerFeature.DisableCircularReferenceDetect;
        Map<String, Object> reqParameters = context.getParameters();
        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");
        List result = new ArrayList();

        List a = (ArrayList) context.getResults().get("data1");
        if (null != a && !a.isEmpty())
        {
            if (!fromDate.equals(toDate))
            {
                a = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(a, fromDate,
                        toDate, false);
            }
            Map m = new HashMap();
            m.put("values", a);
            m.put("key", "广告导流");
            result.add(m);
        }

        List b = (ArrayList) context.getResults().get("data2");
        if (null != b && !b.isEmpty())
        {
            if (!fromDate.equals(toDate))
            {
                b = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(b, fromDate,
                        toDate, false);
            }
            Map m2 = new HashMap();
            m2.put("values", b);
            m2.put("key", "链接导流");
            result.add(m2);
        }

        List c = (ArrayList) context.getResults().get("data3");
        if (null != c && !c.isEmpty())
        {
            if (!fromDate.equals(toDate))
            {
                c = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(c, fromDate,
                        toDate, false);
            }
            Map m3 = new HashMap();
            m3.put("values", c);
            m3.put("key", "直接访问");
            result.add(m3);
        }
        String result1 = JSON.toJSONString(result, f);
        context.setResult("data", result1);

        return OK;
    }
}