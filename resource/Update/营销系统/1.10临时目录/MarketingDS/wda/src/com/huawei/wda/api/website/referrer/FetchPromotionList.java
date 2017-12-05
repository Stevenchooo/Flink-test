package com.huawei.wda.api.website.referrer;

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
 * 查询数据FetchPromotionList <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchPromotionList extends AuthRDBProcessor
{

    /**
     * FetchPromotionList在数据脚本执行之前调用，提供一个机会来处理其他事情
     * 比如给statement添加不在parameters中的其他参数
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

        statement.setString(5, startDate);
        statement.setString(6, endDate);

        reqParameters.put("startDate", startDate);
        reqParameters.put("endDate", endDate);

        return OK;
    }

    /**
     * FetchPromotionList调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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
    @SuppressWarnings(
    {"rawtypes"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();
        List hbdata = null;
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 起止时间段
        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");
        // 环比
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        // 查询环比数据
        if (StringUtils.isNotBlank(huanBiDate))
        {
            String siteId = JsonUtil.getAsStr(reqParameters, "site_id");

            int referrerType = JsonUtil
                    .getAsInt(reqParameters, "referrer_type");

            // 环比开始时间--结束时间
            String hbStartDate = "";
            String hbEndDate = "";

            // 拆分从前台传过来的环比日期
            String[] tempDate = huanBiDate.split("#");
            hbStartDate = tempDate[0];
            hbEndDate = tempDate[1];

            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "call sp_website_referrer_promotion_list(?,?,?,?,?,?)",
                    true, conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, referrerType, hbStartDate, hbEndDate});

            hbdata = res.getResult();

            if (startDate.equals(endDate))
            {
                // 如果时间相同，只需要展示一个开始时间或者结束时间
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate));
            }
            else
            {
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate) + " - "
                                + DateUtil.changeDateFormat(hbEndDate));
            }
        }

        // List data = (ArrayList) context.getResults().get("data");
        // 数据不为空
        if (hbdata != null)
        {
            context.setResult("hbdata", hbdata.get(0));
        }

        // 根据选择日期区间天数判断展示格式
        if (startDate.equals(endDate))
        {
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            context.setResult(
                    "selectDate",
                    DateUtil.changeDateFormat(startDate) + " - "
                            + DateUtil.changeDateFormat(endDate));
        }

        return OK;
    }
}