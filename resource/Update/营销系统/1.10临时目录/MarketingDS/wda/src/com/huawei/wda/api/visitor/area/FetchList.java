package com.huawei.wda.api.visitor.area;

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
 * 地域分布.
 */
public class FetchList extends AuthRDBProcessor
{

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

        // 开始时间-- 结束时间
        String startDate = null;
        String endDate = null;

        // 地域分布
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            //拆分
            startDate = tempDate[0];
            endDate = tempDate[1];
        }

        statement.setString(4, startDate);
        statement.setString(5, endDate);

        // 地域分布
        reqParameters.put("startDate", startDate);
        reqParameters.put("endDate", endDate);

        return OK;
    }

    /**
     * 调用此函数时， sql脚本已经执行 ，    context的result中已有数据库返回的结果集.
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
        // 地域分布
        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        List hbdata = null;

        if (startDate.equals(endDate))
        {
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            context.setResult("selectDate", DateUtil.changeDateFormat(startDate)
                    + " - " + DateUtil.changeDateFormat(endDate));
        }

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
                    "call sp_visitor_area_distribution_list(?,?,?,?,?)", true,
                    conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, hbStartDate, hbEndDate});
            // 地域分布数据查询结果
            hbdata = res.getResult();

            if (startDate.equals(endDate))
            {
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate));
            }
            else
            {
                context.setResult(
                        "selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate) + " - "
                                + DateUtil.changeDateFormat(hbEndDate));
            }
        }

        if (hbdata != null)
        {
            context.setResult("hbdata", hbdata.get(0));
        }

        return OK;
    }
}