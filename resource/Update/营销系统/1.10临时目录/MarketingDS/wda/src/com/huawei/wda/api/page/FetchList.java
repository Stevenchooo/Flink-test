package com.huawei.wda.api.page;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

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
 * 请求参数处理
 * 
 * @author y84030100
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

        // 开始时间
        String startDate = null;
        // 结束时间
        String endDate = null;

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            // 拆分时间段，得到开始时间和结束时间，固定格式
            startDate = tempDate[0];
            endDate = tempDate[1];
        }
        else
        {
            LOG.error("periodTime is error");
            return WRONG_PARAMETER;
        }

        statement.setString(4, startDate);
        statement.setString(5, endDate);

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
        List hbdata = null;
        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");
        // 请求时间段，起止时间
        String startDate = JsonUtil.getAsStr(reqParameters, "startDate");
        String endDate = JsonUtil.getAsStr(reqParameters, "endDate");
        // 环比日期
        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        // 查询环比数据
        if (StringUtils.isNotBlank(huanBiDate))
        {
            String siteId = JsonUtil.getAsStr(reqParameters, "site_id");
            // 环比：开始时间--结束时间
            String hbStartDate = "";
            String hbEndDate = "";
            
            // 拆分环比时间，格式固定
            String[] tempDate = huanBiDate.split("#");
            hbStartDate = tempDate[0];
            hbEndDate = tempDate[1];

            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "call sp_pages_list(?,?,?,?,?)", true,
                    conn.getResultSetRef(), new Object[]
                    {siteId, periodTime, hbStartDate, hbEndDate});

            hbdata = res.getResult();

            if (startDate.equals(endDate))
            {
                //设置选择的环比日期
                context.setResult("selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate));
            }
            else
            {
                //起止时间不同，环比日期也不一样
                context.setResult(
                        "selectHbDate",
                        DateUtil.changeDateFormat(hbStartDate) + " - "
                                + DateUtil.changeDateFormat(hbEndDate));
            }
        }

        // List data = (ArrayList) context.getResults().get("data");
        if (hbdata != null)
        {
            // 设置环比数据
            context.setResult("hbdata", hbdata.get(0));
        }

        if (startDate.equals(endDate))
        {
            // 设置选择的时间
            context.setResult("selectDate",
                    DateUtil.changeDateFormat(startDate));
        }
        else
        {
            // 如果起止时间不相等，就显示日期时间段
            context.setResult("selectDate", DateUtil.changeDateFormat(startDate)
                    + " - " + DateUtil.changeDateFormat(endDate));
        }

        return OK;

    }
}