package com.huawei.wda.honor;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.CommonUtils;
import com.huawei.wda.util.DateUtil;
import com.huawei.wda.util.HandleWeekDateUtil;

/**
 * PositionManagement阵地经营 <一句话功能简述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月14日]
 * @see [相关类/方法]
 */
public class PositionManagement extends AuthRDBProcessor
{
    /**
     * 荣耀官网--PC
     */
    private static final String VMALL_PC = "www.honor.cn";

    /**
     * 荣耀官网 -- 手机
     */
    private static final String VMALL_WAP = "m.honor.cn";

    /**
     * 花粉论坛
     */
    private static final String VMALL_CLUB = "cn.club.vmall.com";

    /**
     * pSiteIdWAP 请求柱状图的类型（referrer_type：1、2、3对应广告导流、链接导流、直接访问）和对应的值
     */
    private static final String SELECT_BAR = "select referrer_type as x, SUM(uv) as y from dw_wda_websites_summary_by_referrer_dm where pt_d >= DATE_FORMAT(DATE_SUB(CURRENT_DATE(),INTERVAL 3 MONTH ), '%Y%m%d') and pt_d <= CURRENT_DATE() and site_id =? group by x order by x desc";

    /**
     * pSiteIdWAP 请求折线图，需要日期和对应的总量
     */
    private static final String SELECT_LINE = "select DATE_FORMAT(pt_d, '%Y/%m/%d') as x, SUM(uv) as y from dw_wda_websites_summary_by_referrer_dm where pt_d >= DATE_FORMAT(DATE_SUB(CURRENT_DATE(),INTERVAL 3 MONTH ), '%Y%m%d') and pt_d <= CURRENT_DATE() and site_id =? group by x";

    /**
     * 执行脚本之前
     * 
     * @param context
     *            上下文
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return int
     * @throws SQLException
     *             sql异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        statement.setString(2, VMALL_PC);
        statement.setString(3, VMALL_WAP);
        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        String fromDate = DateUtil.obtainxMonthsAgoDate(-3);
        String toDate = DateUtil.obtainCurrentStrDate();

        // 请求荣耀官网WAP端的值
        List<Map<String, Object>> wapBarList = DBUtil.query(conn, SELECT_BAR,
                false, new Object[]
        {VMALL_WAP});

        List<Map<String, Object>> wapLineList = DBUtil.query(conn, SELECT_LINE,
                false, new Object[]
        {VMALL_WAP});

        context.setResult("wapBarList", wapBarList);

        // 请求花粉论坛的值
        List<Map<String, Object>> clubBarList = DBUtil.query(conn, SELECT_BAR,
                false, new Object[]
        {VMALL_CLUB});

        List<Map<String, Object>> clubLineList = DBUtil.query(conn, SELECT_LINE,
                false, new Object[]
        {VMALL_CLUB});

        context.setResult("clubBarList", clubBarList);

        // 荣耀官网 -- PC的line值重新塞入
        List dataLinePCList = (ArrayList) context.getResults()
                .get("dataLinePC");

        if (CommonUtils.isNotEmptyList(dataLinePCList))
        {
            dataLinePCList = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(
                    dataLinePCList, fromDate, toDate, false);
        }
        context.setResult("dataLinePC", dataLinePCList);

        // 花粉论坛的line值重新塞入，做下处理
        List mapList = new ArrayList();
        if (null != clubLineList)
        {
            for (int i = 0; i < clubLineList.size(); i++)
            {
                mapList.add(clubLineList.get(i));
            }
        }

        // 荣耀官网WAP端的值重新塞入
        List mapWapList = new ArrayList();
        if (null != wapLineList)
        {
            for (int i = 0; i < wapLineList.size(); i++)
            {
                mapWapList.add(wapLineList.get(i));
            }
        }

        // 花粉论坛
        if (CommonUtils.isNotEmptyList(mapList))
        {
            mapList = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(mapList,
                    fromDate, toDate, false);
        }
        context.setResult("clubLineList", mapList);
        // wap端
        if (CommonUtils.isNotEmptyList(mapWapList))
        {
            mapWapList = HandleWeekDateUtil.obtainTotalMonthOrTotalDay(
                    mapWapList, fromDate, toDate, false);
        }
        context.setResult("wapLineList", mapWapList);

        return OK;
    }
}
