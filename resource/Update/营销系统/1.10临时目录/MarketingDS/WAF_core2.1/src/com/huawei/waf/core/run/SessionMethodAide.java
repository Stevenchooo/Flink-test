package com.huawei.waf.core.run;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.protocol.RetCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.IMethodAide;

public class SessionMethodAide implements IMethodAide {
    @Override
    public String getPassportName() {
        return AUTH;
    }

    @Override
    public int parseAuthInfo(MethodContext context) {
        //当为none时，不检查通行证，也不会刷新通行证的时间
        if(context.getMethodConfig().getAuthType() == MethodConfig.AUTH_NONE) {
            return RetCode.OK;
        }
        HttpServletRequest req = context.getRequest();
        HttpSession session = req.getSession(true);

        String account = (String)session.getAttribute(USERACCOUNT);
        if(Utils.isStrEmpty(account)) {
            return context.setResult(RetCode.INVALID_SESSION, "No account or userKey is null");
        }
        
        String userKey = (String)session.getAttribute(USERKEY);
        context.setAccount(account);
        context.setResult(USERACCOUNT, account);
        context.setUserKey(userKey);
        
        int checkType = context.getMethodConfig().getProcessor().getCheckType();
        if((checkType & CHECK_TYPE_KEY) != 0) {
            String clientKey = JsonUtil.getAsStr(context.getParameters(), USERKEY, "");
            if(!clientKey.equals(userKey)) {
                return context.setResult(RetCode.INVALID_USERKEY, "invalid userKey");
            }
        }
        
        return RetCode.OK;
    }

    @Override
    public void saveAuthInfo(MethodContext context, String account, String userKey, int header) {
        HttpServletRequest req = context.getRequest();
        HttpSession session = req.getSession(true);
        
        session.setAttribute(USERACCOUNT, account);
        session.setAttribute(USERKEY, userKey);
        
        context.setAccount(account);
        context.setUserKey(userKey);
    }

    @Override
    public void removeAuthInfo(MethodContext context) {
        HttpServletRequest req = context.getRequest();
        HttpSession session = req.getSession(false);
        if(session != null) {
            session.invalidate();
        }
    }

    @Override
    public String getLanguage(MethodContext context) {
        String lang = JsonUtil.getAsStr(context.getParameters(), LANGUAGE, null);
        if(!Utils.isStrEmpty(lang)) {
            return lang;
        }
        return context.getRequest().getLocale().toString();
    }
}
