package com.huawei.wda.api;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.RDBProcessor;

/**
 * 网站总结.
 */
public class WebsiteSummary extends RDBProcessor
{

    private static final String QUERY_CURRENT_MAX_EXISTING_HOUR = "select max(pt_h) as curMaxHour from dw_wda_websites_summary_hm where DATE_FORMAT(pt_d, '%Y-%m-%d') = ?";

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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String strCurDate = dateFormat.format(new Date());

        statement.setString(3, strCurDate);

        List<Map<String, Object>> list = DBUtil.query(conn,
                QUERY_CURRENT_MAX_EXISTING_HOUR, false, new Object[]
                {strCurDate});

        if (null != list && !list.isEmpty())
        {
            int curMaxHour = JsonUtil.getAsInt(list.get(0), "curMaxHour");
            statement.setInt(4, curMaxHour);
        }
        else
        {
            statement.setInt(4, 0);
        }

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
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        return OK;
    }
}