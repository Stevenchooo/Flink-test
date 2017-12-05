package com.huawei.wda.api.page.trend;

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
 * 处理请求前后参数和返回值
 * 
 * @author y84030100
 *
 */
public class FetchSummary extends AuthRDBProcessor
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

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");

        String huanBiFromDate = null;
        String fromDate = null;
        String toDate = null;
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
            LOG.error("periodTime is error");
            return WRONG_PARAMETER;
        }

        String decodePageUrl = null;

        try
        {
            decodePageUrl = URLDecoder.decode(
                    JsonUtil.getAsStr(reqParameters, "page_url"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("The Character Encoding is not supported");
        }

        statement.setString(5, decodePageUrl);

        statement.setString(6, fromDate);
        statement.setString(7, toDate);
        statement.setString(8, huanBiFromDate);
        statement.setString(9, huanBiToDate);

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
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        return OK;
    }
}