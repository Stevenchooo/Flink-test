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
 * 查询数据FetchTopPages <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchTopPages extends AuthRDBProcessor
{
    // 按时
    private static final String QUERY_TOTAL_PV_BY_HM = "select sum(pv) as totalPv from dw_wda_websites_summary_hm where site_id = ? and DATE_FORMAT(pt_d, '%Y-%m-%d') >= ? and DATE_FORMAT(pt_d, '%Y-%m-%d') <= ?";

    // 按日
    private static final String QUERY_TOTAL_PV_BY_DM = "select sum(pv) as totalPv from dw_wda_websites_summary_dm where site_id = ? and DATE_FORMAT(pt_d, '%Y-%m-%d') > ? and DATE_FORMAT(pt_d, '%Y-%m-%d') <= ?";

    /**
     * FetchTopPages在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
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
        String fromDate = null;
        //时间声明
        String toDate = null;
        // 做个判断，是否含有#
        if (periodTime.contains("#"))
        {
            // 分割成一个数组
            String[] tempDate = periodTime.split("#");
            //拆分
            fromDate = tempDate[0];
            toDate = tempDate[1];
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }
        // 往里面塞值
        statement.setString(4, fromDate);
        statement.setString(5, toDate);

        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);

        return OK;
    }

    /**
     * FetchTopPages调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            内容
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
        // 获取数据data
        List a = (ArrayList) context.getResults().get("data");

        Map<String, Object> reqParameters = context.getParameters();
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 网址
        String siteId = JsonUtil.getAsStr(reqParameters, "site_id");
        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");

        // 查询总pv
        List<Map<String, Object>> list = null;
        String tempFromDate = null;
        String tempEndDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            tempFromDate = tempDate[0];
            tempEndDate = tempDate[1];
        }
        if (null != tempFromDate)
        {
            if (!tempFromDate.equals(tempEndDate))
            {
                // 按日查询集合
                list = DBUtil.query(conn, QUERY_TOTAL_PV_BY_DM, false,
                        new Object[]
                        {siteId, fromDate, toDate});
            }
            else
            {
                // 按时查询数据
                list = DBUtil.query(conn, QUERY_TOTAL_PV_BY_HM, false,
                        new Object[]
                        {siteId, fromDate, toDate});
            }
        }

        long totalPv = 0;
        // 集合不为空的情况
        if (list != null && !list.isEmpty())
        {
            totalPv = JsonUtil.getAsLong(list.get(0), "totalPv");
            context.setResult("totalPv", totalPv);
        }

        List b = new ArrayList();

        for (int i = 0; i < a.size(); i++)
        {
            Map<String, Object> m2 = new HashMap();
            m2.put("key", ((Map<String, Object>) a.get(i)).get("page_url"));
            m2.put("y",
                    (float) ((Long) (((Map<String, Object>) a.get(i)).get("pv")) * 1.0 / totalPv));
            b.add(m2);
        }

        /*
         * List result = new ArrayList(); m.put("values", b); m.put("key",
         * "aa"); m.put("color", "#ff7f0e"); result.add(m);
         */

        context.setResult("top_pages_pie_data", b);

        return OK;
    }
}