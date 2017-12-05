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
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 查询数据FetchTopReferrer <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchTopReferrer extends AuthRDBProcessor
{
    // 按时查询的sql语句
    private static final String QUERY_TOTAL_PV_BY_HM = "select sum(pv) as totalPv from dw_wda_websites_summary_hm where site_id = ? and DATE_FORMAT(pt_d, '%Y-%m-%d') >= ? and DATE_FORMAT(pt_d, '%Y-%m-%d') <= ?";

    // 按日查询的语句
    private static final String QUERY_TOTAL_PV_BY_DM = "select sum(pv) as totalPv from dw_wda_websites_summary_dm where site_id = ? and DATE_FORMAT(pt_d, '%Y-%m-%d') > ? and DATE_FORMAT(pt_d, '%Y-%m-%d') <= ?";

    /**
     * FetchTopReferrer在数据脚本执行之前调用，提供一个机会来处理其他事情
     * 比如给statement添加不在parameters中的其他参数
     * 
     * @param context
     *            context
     * @param conn
     *            DBConnection
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

        // 拿到数据periodTime
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        String fromDate = null;
        String toDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分： 起
            fromDate = tempDate[0];
            // 止
            toDate = tempDate[1];
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }
        // 往statement塞值
        statement.setString(4, fromDate);
        statement.setString(5, toDate);
        // 把开始日期和结束日期都塞到请求参数里面
        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);

        return OK;
    }

    /**
     * FetchTopReferrer调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
    {"unchecked", "rawtypes"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();
        // fromDate开始日期的获取
        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String siteId = JsonUtil.getAsStr(reqParameters, "site_id");
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");
        // 查询总pv
        List<Map<String, Object>> list = null;
        String tempFromDate = null;
        String tempEndDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 分割一下
            tempFromDate = tempDate[0];
            // 止
            tempEndDate = tempDate[1];
        }

        if (null != tempFromDate)
        {
            // 临时开始日期不等于临时结束日期
            if (!tempFromDate.equals(tempEndDate))
            {
                list = DBUtil.query(conn, QUERY_TOTAL_PV_BY_DM, false,
                        new Object[]
                        {siteId, fromDate, toDate});
            }
            else
            {
                // 按小时
                list = DBUtil.query(conn, QUERY_TOTAL_PV_BY_HM, false,
                        new Object[]
                        {siteId, fromDate, toDate});
            }
        }

        long totalPv = 0;

        if (list != null && !list.isEmpty())
        {
            totalPv = JsonUtil.getAsLong(list.get(0), "totalPv");
            context.setResult("totalPv", totalPv);
        }

        Map m = new HashMap();

        List a = (ArrayList) context.getResults().get("data");

        List b = new ArrayList();

        for (int i = 0; i < a.size(); i++)
        {
            Map<String, Object> m2 = new HashMap();
            m2.put("key", ((Map<String, Object>) a.get(i)).get("referrer_name"));
            // TODO 改为百分比显示，保留2位小数点
            m2.put("y",
                    (float) ((Long) (((Map<String, Object>) a.get(i)).get("pv")) * 1.000 / totalPv));
            b.add(m2);
        }

        List result = new ArrayList();
        m.put("values", b);
        m.put("key", "aa");
        m.put("color", "#ff7f0e");
        result.add(m);

        context.setResult("top_referrer_pie_data", b);

        return OK;
    }
}