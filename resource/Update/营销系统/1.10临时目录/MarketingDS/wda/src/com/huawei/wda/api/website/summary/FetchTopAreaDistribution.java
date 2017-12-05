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
 * 查询数据FetchTopAreaDistribution <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchTopAreaDistribution extends AuthRDBProcessor
{

    private static final String QUERY_TOTAL_PV_BY_HM = "select sum(pv) as totalPv from dw_wda_visitors_area_distribution_hm t1,t_wda_province_code t2 where t1.province_name = t2.province_name and site_id = ? and DATE_FORMAT(pt_d, '%Y-%m-%d') >= ? and DATE_FORMAT(pt_d, '%Y-%m-%d') <= ?";

    private static final String QUERY_TOTAL_PV_BY_DM = "select sum(pv) as totalPv from dw_wda_visitors_area_distribution_dm t1,t_wda_province_code t2 where t1.province_name = t2.province_name and site_id = ? and DATE_FORMAT(pt_d, '%Y-%m-%d') >= ? and DATE_FORMAT(pt_d, '%Y-%m-%d') <= ?";

    /**
     * FetchTopAreaDistribution在数据脚本执行之前调用，提供一个机会来处理其他事情
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
        // 拿到整体的请求参数reqParameters
        Map<String, Object> reqParameters = context.getParameters();
        // 参数校验
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 开始日期的申明
        String fromDate = null;
        String toDate = null;
        if (periodTime.contains("#"))
        {
            // 对日期用#进行分割，得到一个数组（长度为2）
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }

        statement.setString(4, fromDate);
        statement.setString(5, toDate);
        // 重新把算出来的日期塞到请求参数里面
        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);

        return OK;
    }

    /**
     * FetchTopAreaDistribution调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
        // 拿到数据list集合 a
        List a = (ArrayList) context.getResults().get("data");
        Map<String, Object> reqParameters = context.getParameters();

        String siteId = JsonUtil.getAsStr(reqParameters, "site_id");
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        String fromDate = JsonUtil.getAsStr(reqParameters, "fromDate");
        String toDate = JsonUtil.getAsStr(reqParameters, "toDate");

        // 查询总pv
        String tempFromDate = null;
        String tempEndDate = null;
        List<Map<String, Object>> list = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            tempFromDate = tempDate[0];
            tempEndDate = tempDate[1];
        }

        if (null != tempFromDate)
        {
            // 查询天的集合
            if (!tempFromDate.equals(tempEndDate))
            {
                list = DBUtil.query(conn, QUERY_TOTAL_PV_BY_DM, false,
                        new Object[]
                        {siteId, fromDate, toDate});
            }
            else
            {
                list = DBUtil.query(conn, QUERY_TOTAL_PV_BY_HM, false,
                        new Object[]
                        {siteId, fromDate, toDate});
            }
        }

        long totalPv = 0;
        // list不为空，并且有数据
        if (list != null && !list.isEmpty())
        {
            totalPv = JsonUtil.getAsLong(list.get(0), "totalPv");
            context.setResult("totalPv", totalPv);
        }

        List b = new ArrayList();

        Map m = new HashMap();
        m.put("key", "浏览量TOP");

        for (int i = 0; i < a.size() && i <= 9; i++)
        {
            Map<String, Object> m2 = new HashMap();
            m2.put("label", ((Map<String, Object>) a.get(i)).get("name"));
            m2.put("value", (float) ((Long) (((Map<String, Object>) a.get(i))
                    .get("event")) * 1.0000 / totalPv));
            b.add(m2);
        }

        m.put("values", b);

        List result = new ArrayList();
        result.add(m);

        context.setResult("top_area_v_bar_data", result);

        return OK;
    }
}