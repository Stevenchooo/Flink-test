package com.huawei.manager.base.api;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;

import com.huawei.manager.common.client.User;
import com.huawei.manager.common.client.meta.LoginResponse;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.api.VerifyCode;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.RDBProcessor;
import com.huawei.waf.facade.run.IMethodAide;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class UserLogin extends RDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final int PRINCIPAL_ERROR = 7000;
    
    private static final int USER_ERROR = 7001;
    
    private static final int ACCOUNT_ERROR = 4001;
    
    private static final String ACCOUNT = "account";
    
    private static final String VERIFYCODE = "verify";
    
    private static final String PWD = "password";
    
    /**
     * 处理前
     * @param context    上线文
     * @param conn       数据库链接
     * @param statement  执行语句
     * @return           是否成功
     * @throws SQLException  sql异常
     */
    public int beforeProcess(MethodContext context, DBConnection conn, CallableStatement statement)
        throws SQLException
    {
        if (isCasEnable())
        {
            AttributePrincipal principal =
                (AttributePrincipal)(context.getRequest().getSession().getAttribute("principal"));
            
            String userAccount = (null != principal) ? principal.getName() : null;
            
            context.setParameter(ACCOUNT, userAccount);
        }
        else
        {
            
            Map<String, Object> reqParameters = context.getParameters();
            
            //校验验证码
            String verifyCode = JsonUtil.getAsStr(reqParameters, VERIFYCODE);
            String verifyCodeCookie = VerifyCode.getVerifyCodeCookie(context);
            
            //自动化测要求，暂时屏蔽校验码
            int state = StringUtils.checkVerifyCode(verifyCode, verifyCodeCookie);
            if (RetCode.OK != state)
            {
                return state;
            }
            
            //校验用户名密码
            String account = JsonUtil.getAsStr(reqParameters, ACCOUNT);
            String pwd = JsonUtil.getAsStr(reqParameters, PWD);
            
            String umServerUrl = StringUtils.getConfigInfo("userMangerServerUrl");
            User user = new User(umServerUrl, account, pwd);
            LoginResponse lr = user.login();
            
            if (null == lr || lr.getResultCode().intValue() != 0)
            {
                LOG.error("user {} login failed", account);
                return ACCOUNT_ERROR;
            }
            
            return RetCode.OK;
        }
        
        return RetCode.OK;
    }
    
    /**
     * 处理后
     * @param context   上下文
     * @param conn      数据库链接
     * @return          是否成功
     * @throws SQLException      sql异常
     */
    @Override
    public int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        LOG.info("enter user login process");
        
        String account = null;
        //cas集成
        if (isCasEnable())
        {
            AttributePrincipal principal =
                (AttributePrincipal)(context.getRequest().getSession().getAttribute("principal"));
            
            //没有用户信息重新登录
            if (null == principal)
            {
                return PRINCIPAL_ERROR;
            }
            
            account = principal.getName();
        }
        
        //直接登录
        else
        {
            account = context.getParameter(ACCOUNT).toString();
            
        }
        
        //用户是否存在
        Boolean userFlag = JsonUtil.getAsBool(context.getResults(), "exists");
        
        if (!userFlag)
        {
            return USER_ERROR;
        }
        
        String userKey = Utils.genUUID_64();
        LOG.info("user name is {}", account);
        
        context.getMethodConfig().getAide().saveAuthInfo(context, account, userKey, IMethodAide.CHECK_TYPE_KEY);
        
        VerifyCode.rmvVerifyCodeCookie(context);
        //响应时告诉userKey，浏览器中记录
        context.setResult(IMethodAide.USERKEY, userKey);
        
        return RetCode.OK;
    }
    
    /**
     * <是否cas集成>
     * @return   是否cas集成
     * @see [类、类#方法、类#成员]
     */
    private boolean isCasEnable()
    {
        String value = StringUtils.getConfigInfo("casEnable");
        
        return Boolean.valueOf(value);
    }
    
}
