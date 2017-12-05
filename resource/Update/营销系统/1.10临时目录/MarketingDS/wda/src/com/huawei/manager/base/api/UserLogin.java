package com.huawei.manager.base.api;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.protocol.RetCodeExt;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.EncryptUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.api.VerifyCode;
import com.huawei.waf.core.config.sys.SecurityConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.IMethodAide;
import com.huawei.waf.protocol.RetCode;
import com.huawei.wda.util.StringUtils;

/**
 * 登录
 * 
 * @author w00296102
 */
public class UserLogin extends AuthRDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();

    private static final String ACCOUNT = "account";

    private static final String VERIFYCODE = "verify";

    /**
     * 运行前
     * 
     * @param context
     *            方法的配置信息
     * @param conn
     *            数据库对象
     * @param statement
     *            statement
     * @throws SQLException
     *             SQLException
     * 
     * @return 返回码
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        if (isCasEnable())
        {
            String userAccount = (String) (context.getRequest().getSession()
                    .getAttribute("principal_name"));
            if (null != userAccount)
            {
                userAccount = EncryptUtil.configDecode(userAccount);
            }
            context.setParameter(ACCOUNT, userAccount);
        }
        else
        {

            Map<String, Object> reqParameters = context.getParameters();
            String verifyCode = JsonUtil.getAsStr(reqParameters, VERIFYCODE);
            String verifyCodeCookie = VerifyCode.getVerifyCodeCookie(context);
            if (Utils.isStrEmpty(verifyCodeCookie))
            {
                LOG.info("verifyCode expired");
                return RetCode.VERIFYCODE_EXPIRED;
            }

            if (Utils.isStrEmpty(verifyCode))
            {
                LOG.info("verifyCode is null");
                return RetCode.WRONG_VERIFYCODE;
            }

            if (!verifyCodeCookie.equalsIgnoreCase(verifyCode))
            {
                LOG.info(
                        "verifyCode {} != verifyCode in Cookie {}, authCode={}",
                        verifyCode, verifyCodeCookie, WAFConfig.getAuthCode());
                return RetCodeExt.WRONG_VERIFYCODE;
            }
            statement.setString(5, SecurityConfig.getLoginLockType());
            statement.setInt(6, SecurityConfig.getUserLockInterval());
            statement.setInt(7, SecurityConfig.getMaxReloginTimes());
        }
        return RetCode.OK;
    }

    /**
     * 运行前
     * 
     * @param context
     *            方法的配置信息
     * @param conn
     *            数据库对象
     * @throws SQLException
     *             SQLException
     * 
     * @return 返回码
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {

        String account = null;

        boolean isHonorOrNot = false;
        String or = (String) (context.getRequest().getSession()
                .getAttribute("href_name"));
        context.getRequest().getSession().setAttribute("href_name", null);

        if ("yes".equals(or))
        {
            isHonorOrNot = true;
            context.getRequest().getSession().setAttribute("href_name", "");
        }

        // cas集成
        if (isCasEnable())
        {
            account = (String) (context.getRequest().getSession()
                    .getAttribute("principal_name"));

            // 没有用户信息重新登录
            if (null == account)
            {
                return 7000;
            }
            account = EncryptUtil.configDecode(account);
        }
        else
        {

            int expiredDays = JsonUtil.getAsInt(context.getResults(),
                    "expiredDays", 0);
            String userKey = Utils.genUUID_64();
            account = context.getParameter(ACCOUNT).toString();
            if (expiredDays <= 0)
            { // 密码未超期
                context.getMethodConfig().getAide().saveAuthInfo(context,
                        account, userKey, IMethodAide.CHECK_TYPE_KEY);
                VerifyCode.rmvVerifyCodeCookie(context);
            }
            else
            {
                context.getMethodConfig().getAide().saveAuthInfo(context,
                        account, userKey, IMethodAide.CHECK_TYPE_KEY);
            }
            // 响应时告诉userKey，浏览器中记录
            context.setResult(IMethodAide.USERKEY, userKey);
        }

        // 用户是否存在
        Boolean userFlag = JsonUtil.getAsBool(context.getResults(), "exists");

        if (!userFlag)
        {
            return 7001;
        }

        String userKey = Utils.genUUID_64();
        LOG.info("user name is {}", account);

        context.getMethodConfig().getAide().saveAuthInfo(context, account,
                userKey, IMethodAide.CHECK_TYPE_KEY);

        VerifyCode.rmvVerifyCodeCookie(context);
        // 响应时告诉userKey，浏览器中记录
        context.setResult(IMethodAide.USERKEY, userKey);

        context.setResult("__userHref", isHonorOrNot);

        return RetCode.OK;

    }

    /**
     * <是否cas集成>
     * 
     * @return 是否cas集成
     * @see [类、类#方法、类#成员]
     */
    private boolean isCasEnable()
    {
        String value = StringUtils.getConfigInfo("casEnable");
        return Boolean.valueOf(value);
    }
}
