package com.huawei.wda.api.visitor;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 趋势分析.
 */
public class TrendSummary extends AuthRDBProcessor
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

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        String provinceName = JsonUtil.getAsStr(reqParameters, "province_name");

        String fromDate = null;
        String toDate = null;
        String huanBiFromDate = null;
        String huanBiToDate = null;

        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
            if (StringUtils.isNotBlank(huanBiDate))
            {
                String[] tempDate1 = huanBiDate.split("#");
                huanBiFromDate = tempDate1[0];
                huanBiToDate = tempDate1[1];
            }
        }
        else
        {
            // 趋势分析错误参数
            LOG.debug("wrong parameter");
            return WRONG_PARAMETER;
        }

        statement.setString(5, fromDate);
        statement.setString(6, toDate);
        statement.setString(7, huanBiFromDate);
        statement.setString(8, huanBiToDate);

        try
        {
            statement.setString(9, URLDecoder.decode(provinceName, "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.debug("wda api visitor TrendSummary : UnsupportedEncodingException");
        }

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
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        return OK;
    }
}