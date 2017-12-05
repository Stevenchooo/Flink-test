package com.huawei.wda.api.page.trend;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 处理请求前后参数和返回值
 * 
 * @author y84030100
 * 
 */
public class FetchUpDown extends AuthRDBProcessor
{
    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        // 时间周期类型
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        // 开始时间 和 结束 时间
        String startDate = null;
        String endDate = null;

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            startDate = tempDate[0];
            endDate = tempDate[1];
        }

        statement.setString(6, startDate);
        statement.setString(7, endDate);

        reqParameters.put("startDate", startDate);
        reqParameters.put("endDate", endDate);

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
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        return OK;
    }
}