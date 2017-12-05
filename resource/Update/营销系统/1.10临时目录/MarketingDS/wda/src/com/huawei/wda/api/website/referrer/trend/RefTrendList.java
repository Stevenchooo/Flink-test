package com.huawei.wda.api.website.referrer.trend;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.DateUtil;

/**
 * 查询数据RefTrendList <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class RefTrendList extends AuthRDBProcessor
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
        // 时间周期类型
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 开始时间--结束时间
        String startDate = null;
        String endDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            startDate = tempDate[0];
            endDate = tempDate[1];
        }
        reqParameters.put("startDate", startDate);
        reqParameters.put("endDate", endDate);
        // 设置请求参数
        statement.setString(6, startDate);
        statement.setString(7, endDate);

        return OK;

    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
    @SuppressWarnings(
    {"rawtypes"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        int timeDim = JsonUtil.getAsInt(reqParameters, "time_dim");
        // 根据referrer类型来判断
        int referrerType = JsonUtil.getAsInt(reqParameters, "referrer_type");
        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        List hbdata = null;

        // 查询环比数据
        if (StringUtils.isNotBlank(huanBiDate))
        {
            // 获取到网址
            String siteId = JsonUtil.getAsStr(reqParameters, "site_id");
            // 开始时间hbStartDate
            String hbStartDate = "";
            // 结束时间hbEndDate
            String hbEndDate = "";
            String[] tempDate = huanBiDate.split("#");
            hbStartDate = tempDate[0];
            hbEndDate = tempDate[1];

            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "call sp_website_referrer_trend_list(?,?,?,?,?,?,?)", true,
                    conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, timeDim, referrerType, hbStartDate,
                            hbEndDate});
            // 获取响应参数
            hbdata = res.getResult();
            // startDate等于endDate
            if (startDate.equals(endDate))
            {
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate));
            }
            else
            {
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate) + " - "
                                + DateUtil.changeDateFormat(hbEndDate));
            }
            // 2代表是按日
            if (timeDim == 2 || timeDim == 3 || timeDim == 4)
            {
                if (startDate.equals(endDate))
                {
                    context.setResult("hbDateByDay",
                            DateUtil.changeDateFormat(startDate) + " 与  "
                                    + DateUtil.changeDateFormat(hbStartDate));
                }
                else
                {
                    // hbDateByDay
                    context.setResult(
                            "hbDateByDay",
                            DateUtil.changeDateFormat(startDate) + "-"
                                    + DateUtil.changeDateFormat(endDate)
                                    + " 与 "
                                    + DateUtil.changeDateFormat(hbStartDate)
                                    + "-"
                                    + DateUtil.changeDateFormat(hbEndDate));
                }

            }

        }
        // 环比数据判断不为空的情况下，进行直接键值赋值
        if (hbdata != null)
        {
            context.setResult("hbdata", hbdata.get(0));
        }

        // 设置selectDate，根据选择天数判断展示样式
        if (startDate.equals(endDate))
        {
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            // selectDate
            context.setResult(
                    "selectDate",
                    DateUtil.changeDateFormat(startDate) + " - "
                            + DateUtil.changeDateFormat(endDate));
        }

        return OK;

    }
}