package com.huawei.wda.api.visitor.userinsight;

import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 访客画像柱状图 <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月8日]
 * @see [相关类/方法]
 */
public class UserinsightBar extends AuthRDBProcessor
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
        String huanBiDate = JsonUtil.getAsStr(parameters, "huanBiDate");
        String fromDate = null;
        String toDate = null;
        String huanBiFromDate = null;
        String huanBiToDate = null;
        if (periodTime.contains("#"))
        {
            // tempDate放着两个日期
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
            // 判空
            if (StringUtils.isNotBlank(huanBiDate))
            {
                String[] tempDate1 = huanBiDate.split("#");
                huanBiFromDate = tempDate1[0];
                huanBiToDate = tempDate1[1];
            }
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }
        statement.setString(5, fromDate);
        statement.setString(6, toDate);
        // 这里的几个值，都赋值到statement
        statement.setString(7, huanBiFromDate);
        statement.setString(8, huanBiToDate);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);
        parameters.put("huanBiFromDate", huanBiFromDate);
        parameters.put("huanBiToDate", huanBiToDate);
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
