package com.huawei.wda.api.website.trend;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 查询数据FetchSummary <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchSummary extends AuthRDBProcessor
{

    /**
     * FetchSummary在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
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
        // 获取请求参数
        Map<String, Object> reqParameters = context.getParameters();
        // 获取请求里面对比的日期（带有#键）
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
        // 获取一开始的日期（periodTime）
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String fromDate = null;
        String toDate = null;
        // 环比
        String huanBiFromDate = null;
        String huanBiToDate = null;
        // 判断periodTime是否含有#号键
        if (periodTime.contains("#"))
        {
            // 对periodTime进行分割，变成一个数组
            String[] tempDate = periodTime.split("#");
            // 获取开始日期fromDate
            fromDate = tempDate[0];
            toDate = tempDate[1];
            // 获取到对比日期不为空的情况
            if (StringUtils.isNotBlank(huanBiDate))
            {
                String[] tempDate1 = huanBiDate.split("#");
                // 从数组中拿到huanBiFromDate
                huanBiFromDate = tempDate1[0];
                huanBiToDate = tempDate1[1];
            }
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }
        // 往脚本的参数里面塞值（共四个）
        statement.setString(5, fromDate);
        statement.setString(6, toDate);

        statement.setString(7, huanBiFromDate);
        statement.setString(8, huanBiToDate);

        return OK;
    }

    /**
     * FetchSummary调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
        // 不做任何操作，直接返回ok
        return OK;
    }
}