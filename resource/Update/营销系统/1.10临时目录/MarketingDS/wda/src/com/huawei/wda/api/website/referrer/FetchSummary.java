package com.huawei.wda.api.website.referrer;

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
     *             SQL异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String fromDate = null;
        String toDate = null;
        // 申明环比开始日期
        String huanBiFromDate = null;
        // 申明环比结束日期
        String huanBiToDate = null;
        // 判断periodTime是否包含#键
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
            // 当对比日期不为空的时候
            if (StringUtils.isNotBlank(huanBiDate))
            {
                // 对对比日期进行分割
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
        // 把请求里的参数赋值到脚本的参数里面
        statement.setString(5, fromDate);
        statement.setString(6, toDate);
        //设置7、8两个参数：环比起止时间
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
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        // 直接返回ok，执行脚本之后，不做任何操作！
        return OK;
    }
}