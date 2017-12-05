package com.huawei.wda.api.visitor;

import static com.huawei.waf.protocol.RetCode.OK;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.DateUtil;

/**
 * 访问趋势列表.
 */
public class VisitorTrendList extends AuthRDBProcessor
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

        // 时间周期类型
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        // 开始时间--结束时间
        String startDate = null;
        String endDate = null;

        // 访问趋势列表
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            startDate = tempDate[0];
            endDate = tempDate[1];
        }

        String encodeProvinceName = null;

        try
        {
            encodeProvinceName = URLDecoder.decode(
                    JsonUtil.getAsStr(reqParameters, "province_name"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.debug("VisitorTrendList beforeProcess: UnsupportedEncodingException");
        }

        statement.setString(5, encodeProvinceName);
        statement.setString(6, startDate);
        statement.setString(7, endDate);

        reqParameters.put("encodeProvinceName", encodeProvinceName);
        // 访问趋势列表起止时间
        reqParameters.put("startDate", startDate);
        reqParameters.put("endDate", endDate);

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
    {"rawtypes"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 按日或按天
        int timeDim = JsonUtil.getAsInt(reqParameters, "time_dim");

        String encodeProvinceName = JsonUtil.getAsStr(reqParameters,
                "encodeProvinceName");
        // 访问趋势列表
        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        List hbdata = null;

        // 查询环比数据
        if (StringUtils.isNotBlank(huanBiDate))
        {

            String siteId = JsonUtil.getAsStr(reqParameters, "site_id");

            // 开始时间
            String hbStartDate = "";

            // 结束时间
            String hbEndDate = "";

            String[] tempDate = huanBiDate.split("#");

            hbStartDate = tempDate[0];
            hbEndDate = tempDate[1];

            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "call sp_visitor_trend_list(?,?,?,?,?,?,?)", true,
                    conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, timeDim, encodeProvinceName,
                            hbStartDate, hbEndDate});
            // 访问趋势列表数据查询结果
            hbdata = res.getResult();

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

            // 如果选择的是按日展示
            if (timeDim == 2 || timeDim == 3 || timeDim == 4)
            {
                // 起止时间相同，即选择范围只有一天
                if (startDate.equals(endDate))
                {
                    context.setResult("hbDateByDay",
                            DateUtil.changeDateFormat(startDate) + " 与  "
                                    + DateUtil.changeDateFormat(hbStartDate));
                }
                else
                {
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

        // 数据不为空
        if (hbdata != null)
        {
            context.setResult("hbdata", hbdata.get(0));
        }

        // 访问趋势列表
        if (startDate.equals(endDate))
        {
            // 设置时间值，只需要开始时间
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            // 时间不同，需要起止时间
            context.setResult(
                    "selectDate",
                    DateUtil.changeDateFormat(startDate) + " - "
                            + DateUtil.changeDateFormat(endDate));
        }
        return OK;
    }
}