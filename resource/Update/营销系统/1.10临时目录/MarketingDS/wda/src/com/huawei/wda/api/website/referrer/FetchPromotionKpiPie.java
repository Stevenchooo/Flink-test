package com.huawei.wda.api.website.referrer;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 查询数据FetchPromotionKpiPie <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchPromotionKpiPie extends AuthRDBProcessor
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
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();
        String fromDate = null;
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String toDate = null;

        if (periodTime.contains("#"))
        {
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

        statement.setString(6, fromDate);
        statement.setString(7, toDate);

        return OK;
    }
}