package com.huawei.wda.api.website;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.RDBProcessor;
import com.huawei.wda.util.CollectionUtil;
import com.huawei.wda.util.DateUtil;

/**
 * 查询数据summary <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class DynamicSummary extends RDBProcessor
{

    // private static final String QUERY_CURRENT_MAX_EXISTING_HOUR =
    // "select max(pt_h) as curMaxHour from dw_wda_websites_summary_hm where DATE_FORMAT(pt_d, '%Y-%m-%d') = ?";

    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return 参数是否正确(int)
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {
        Map<String, Object> reqParameters = context.getParameters();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String curDate = dateFormat.format(new Date());
        int period = JsonUtil.getAsInt(reqParameters, "period");

        String fromDate = null;
        String toDate = null;

        if (period == 0)
        {
            fromDate = curDate;
            toDate = curDate;
        }
        else if (period == -1)
        {
            fromDate = DateUtil.getSpecifiedDayBefore(curDate, 1);
            toDate = fromDate;
        }
        else if (period == -7)
        {
            fromDate = DateUtil.getSpecifiedDayBefore(curDate, 7);
            toDate = curDate;
        }
        else if (period == -30)
        {
            fromDate = DateUtil.getSpecifiedDayBefore(curDate, 30);
            toDate = curDate;
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }

        statement.setString(5, fromDate);
        statement.setString(6, toDate);

        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return 参数是否正确(int)
     * @throws SQLException
     *             SQLException
     */
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        List a = (ArrayList) context.getResults().get("data");

        context.setResult("data", CollectionUtil.formatData(a));

        return OK;
    }
}