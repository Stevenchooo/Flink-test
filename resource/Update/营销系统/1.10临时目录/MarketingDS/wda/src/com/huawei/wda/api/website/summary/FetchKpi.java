package com.huawei.wda.api.website.summary;

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
import com.huawei.wda.util.DateUtil;
import com.huawei.wda.util.HandleWeekDateUtil;

/**
 * 查询数据FetchKpi <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchKpi extends AuthRDBProcessor
{

    /**
     * FetchKpi在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
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

        // 参数校验
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        int huanBi = JsonUtil.getAsInt(reqParameters, "huanbi");

        String fromDate = null;
        String toDate = null;
        String huanBiFromDate = null;
        String huanBiToDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            String startDate = tempDate[0];
            String endDate = tempDate[1];
            if (startDate.equals(endDate))
            {
                fromDate = startDate;
                toDate = endDate;
                if (huanBi == 0)
                { // 前一天
                    huanBiFromDate = DateUtil
                            .getSpecifiedDayBefore(fromDate, 1);
                    huanBiToDate = DateUtil.getSpecifiedDayBefore(toDate, 1);
                }
                else if (huanBi == 1)
                { // 上周同期
                    huanBiFromDate = DateUtil
                            .getSpecifiedDayBefore(fromDate, 7);
                    huanBiToDate = DateUtil.getSpecifiedDayBefore(toDate, 7);
                }
            }
            else
            {
                fromDate = startDate;
                toDate = endDate;
            }
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }

        statement.setString(6, fromDate);
        statement.setString(7, toDate);
        statement.setString(8, huanBiFromDate);
        statement.setString(9, huanBiToDate);

        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);
        reqParameters.put("huanBiFromDate", huanBiFromDate);
        reqParameters.put("huanBiToDate", huanBiToDate);

        return OK;
    }

    /**
     * FetchKpi调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
        // int huanBi = JsonUtil.getAsInt(reqParameters, "huanbi", -1);
        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");
        String huanBiFromDate = JsonUtil.getAsStr(reqParameters,
                "huanBiFromDate");
        List a = (ArrayList) context.getResults().get("data");
        List<Map> mapList = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(a,
                fromDate, toDate, false);
        Map m = new HashMap();
        if (fromDate.equals(toDate))
        {
            m.put("values", a);
        }
        else
        {
            m.put("values", mapList);
        }

        m.put("key", fromDate.equals(toDate) ? fromDate
                : (fromDate + " - " + toDate));

        context.setResult("data", m);

        List b = (ArrayList) context.getResults().get("hb_data");
        if (b != null && !b.isEmpty())
        {

            Map m2 = new HashMap();

            m2.put("values", b);
            m2.put("key", huanBiFromDate);
            context.setResult("hb_data", m2);
        }

        return OK;
    }
}