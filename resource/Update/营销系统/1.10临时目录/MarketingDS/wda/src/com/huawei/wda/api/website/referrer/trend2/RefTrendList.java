package com.huawei.wda.api.website.referrer.trend2;

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
import com.huawei.util.LogUtil;
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
     * 日志类
     */
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

        // 开始时间 -- 结束时间
        String startDate = null;
        String endDate = null;

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分
            startDate = tempDate[0];
            endDate = tempDate[1];
        }

        String decodeRefName = null;

        try
        {
            decodeRefName = URLDecoder.decode(
                    JsonUtil.getAsStr(reqParameters, "ref_name"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("RefTrendList: character is not supported!");
        }

        statement.setString(5, decodeRefName);
        statement.setString(6, startDate);
        statement.setString(7, endDate);

        reqParameters.put("encodeRefName", decodeRefName);
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
        List hbdata = null;
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");
        int timeDim = JsonUtil.getAsInt(reqParameters, "time_dim");
        // 编码名称
        String decodeRefName = JsonUtil
                .getAsStr(reqParameters, "encodeRefName");
        // 获取过来的对比日期参数
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
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
                    "call sp_website_referrer_trend2_list(?,?,?,?,?,?,?)",
                    true, conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, timeDim, decodeRefName, hbStartDate,
                            hbEndDate});
            LOG.info("siteId = " + siteId + ";periodTime = " + periodTime
                    + ";timeDim = " + timeDim + ";decodeRefName = "
                    + decodeRefName + ";hbStartDate = " + hbStartDate
                    + ";hbEndDate = " + hbEndDate);

            // 获取环比的数据
            hbdata = res.getResult();
            // 判断开始日期是否等于结束日期
            if (startDate.equals(endDate))
            {
                // 格式
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate));
            }
            else
            {
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate) + " - "
                                + DateUtil.changeDateFormat(hbEndDate));
            }
            // 按日的情况
            if (timeDim == 2 || timeDim == 3 || timeDim == 4)
            {
                // 开始日期等于结束日期的情况
                if (startDate.equals(endDate))
                {
                    context.setResult("hbDateByDay",
                            DateUtil.changeDateFormat(startDate) + "与"
                                    + DateUtil.changeDateFormat(hbStartDate));
                }
                else
                {
                    context.setResult(
                            "hbDateByDay",
                            DateUtil.changeDateFormat(startDate) + "-"
                                    + DateUtil.changeDateFormat(endDate) + "与"
                                    + DateUtil.changeDateFormat(hbStartDate)
                                    + "-"
                                    + DateUtil.changeDateFormat(hbEndDate));
                }

            }

        }
        // 对比数据不为空的情况
        if (hbdata != null)
        {
            // 直接赋值对比数据
            context.setResult("hbdata", hbdata.get(0));
        }

        if (startDate.equals(endDate))
        {
            // selectDate，相同时间选开始时间即可
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            // selectDate，起止时间都需要
            context.setResult(
                    "selectDate",
                    DateUtil.changeDateFormat(startDate) + " - "
                            + DateUtil.changeDateFormat(endDate));
        }
        // 处理完成之后，返回ok
        return OK;

    }
}