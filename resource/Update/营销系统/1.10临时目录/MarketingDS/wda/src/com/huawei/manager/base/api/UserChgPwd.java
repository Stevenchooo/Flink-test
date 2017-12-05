package com.huawei.manager.base.api;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.protocol.RetCodeExt;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.api.VerifyCode;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see [相关类/方法]
 */
public class UserChgPwd extends AuthRDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();

    private static final String VERIFYCODE = "verify";

    /**
     * 处理前
     * 
     * @param context
     *            系统上下文
     * @param conn
     *            数据库连接
     * @param statement
     *            执行语句
     * @return 是否成功
     * @throws SQLException
     *             sql异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        LOG.info("now it is time to verify code!!");

        Map<String, Object> reqParameters = context.getParameters();
        String verifyCode = JsonUtil.getAsStr(reqParameters, VERIFYCODE);

        String verifyCodeCookie = VerifyCode.getVerifyCodeCookie(context);

        LOG.debug("Verify code: {}", verifyCode);
        LOG.debug("verifyCodeCookie : {}", verifyCodeCookie);

        if (Utils.isStrEmpty(verifyCodeCookie))
        {
            LOG.info("verifyCode expired");
            return RetCode.VERIFYCODE_EXPIRED;
        }

        if (Utils.isStrEmpty(verifyCode))
        {
            LOG.info("verifyCode is null!");
            return RetCode.WRONG_VERIFYCODE;
        }

        if (!verifyCodeCookie.equalsIgnoreCase(verifyCode))
        {
            LOG.info("verifyCode {} != verifyCode in Cookie {}, authCode={}",
                    verifyCode, verifyCodeCookie, WAFConfig.getAuthCode());
            return RetCodeExt.WRONG_VERIFYCODE;
        }

        return RetCode.OK;
    }

    /**
     * 系统处理后
     * 
     * @param context
     *            系统上下文
     * @param conn
     *            数据库连接
     * @return 是否成功
     * @throws SQLException
     *             sql异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        return RetCode.OK;
    }
}
