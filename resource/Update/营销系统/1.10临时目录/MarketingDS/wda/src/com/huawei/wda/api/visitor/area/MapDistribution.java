package com.huawei.wda.api.visitor.area;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 地域分布.
 */
public class MapDistribution extends AuthRDBProcessor
{

    private static final Logger LOG = LogUtil.getInstance();

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

        Map<String, Object> reqParameters = context.getParameters();

        // 参数校验
        String fromDate = null;
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String toDate = null;

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            fromDate = tempDate[0];
            toDate = tempDate[1];
        }
        else
        {
            // 参数错误
            LOG.debug("wrong parameter");
            return WRONG_PARAMETER;
        }

        statement.setString(5, fromDate);
        statement.setString(6, toDate);
        // 设置请求参数
        reqParameters.put("fromDate", fromDate);
        reqParameters.put("toDate", toDate);

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
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        List a = (ArrayList) context.getResults().get("data");

        List b = new ArrayList();

        for (int i = 0; i < a.size() && i <= 9; i++)
        {
            Map m = new HashMap();

            m.put("key", ((Map<String, Object>) a.get(i)).get("name"));
            m.put("y", ((Map<String, Object>) a.get(i)).get("event"));

            b.add(m);
        }

        context.setResult("area_distribution_pie_data", b);

        return OK;
    }
}