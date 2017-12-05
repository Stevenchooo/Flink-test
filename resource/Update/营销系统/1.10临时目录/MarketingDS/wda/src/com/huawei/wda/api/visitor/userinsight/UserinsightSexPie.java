package com.huawei.wda.api.visitor.userinsight;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 访客画像性别饼图 <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月7日]
 * @see [相关类/方法]
 */
public class UserinsightSexPie extends AuthRDBProcessor
{
    /**
     * 执行脚本之前
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return int
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {
        Map<String, Object> parameters = context.getParameters();
        String periodTime = JsonUtil.getAsStr(parameters, "periodTime");
        String startDate = null;
        String endDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            startDate = tempDate[0];
            endDate = tempDate[1];
        }
        statement.setString(4, startDate);
        statement.setString(5, endDate);

        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        return RetCode.OK;
    }

    /**
     * 执行脚本之后
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        return RetCode.OK;
    }

}
