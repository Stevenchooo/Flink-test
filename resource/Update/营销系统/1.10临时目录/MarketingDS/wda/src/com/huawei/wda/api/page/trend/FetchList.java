package com.huawei.wda.api.page.trend;

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
 * 处理请求前后参数和返回值
 * 
 * @author y84030100
 * 
 */
public class FetchList extends AuthRDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();

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

        String decodePageUrl = null;
        try
        {
            decodePageUrl = URLDecoder.decode(
                    JsonUtil.getAsStr(reqParameters, "page_url"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("FetchList : The Character Encoding is not supported");
        }

        statement.setString(5, decodePageUrl);
        statement.setString(6, startDate);
        statement.setString(7, endDate);

        reqParameters.put("decodePageUrl", decodePageUrl);
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
    @SuppressWarnings("rawtypes")
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        int timeDim = JsonUtil.getAsInt(reqParameters, "time_dim");

        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        List hbdata = null;

        // 查询环比数据
        if (StringUtils.isNotBlank(huanBiDate))
        {
            String siteId = JsonUtil.getAsStr(reqParameters, "site_id");
            String decodePageUrl = JsonUtil.getAsStr(reqParameters,
                    "decodePageUrl");

            // 开始时间
            String hbStartDate = "";

            // 结束时间
            String hbEndDate = "";

            String[] tempDate = huanBiDate.split("#");
            hbStartDate = tempDate[0];
            hbEndDate = tempDate[1];

            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "call sp_pages_trend_list(?,?,?,?,?,?,?)", true,
                    conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, timeDim, decodePageUrl, hbStartDate,
                            hbEndDate});

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

        // List data = (ArrayList) context.getResults().get("data");
        // 获取的数据不为空
        if (hbdata != null)
        {
            // 设置数据
            context.setResult("hbdata", hbdata.get(0));
        }

        // 如果起止时间相同
        if (startDate.equals(endDate))
        {
            // 只需要一个时间展示
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            // 时间不同，要展示两个时间段
            context.setResult(
                    "selectDate",
                    DateUtil.changeDateFormat(startDate) + " - "
                            + DateUtil.changeDateFormat(endDate));
        }

        return OK;
    }
}