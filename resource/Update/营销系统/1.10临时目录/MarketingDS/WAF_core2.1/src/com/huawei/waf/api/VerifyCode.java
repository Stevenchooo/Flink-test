package com.huawei.waf.api;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.huawei.util.AzDGCrypt;
import com.huawei.util.CookieUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.SecureUtil;
import com.huawei.waf.core.config.sys.SecurityConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.RetCode;

public class VerifyCode extends DefaultJavaProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    public static final String VERIFY_COOKIE = "verifysession";
    
    @Override
    public int process(MethodContext context) {
        HttpServletResponse response = context.getResponse();
        AbstractResponser.setBrowserNoCache(response);
        response.setContentType("image/jpeg");
        
        String verifyCode = SecureUtil.generateVerifyCode(SecurityConfig.getVerifyCodeLen());
        //记入cookie，认证时携带，阅完即焚
        CookieUtil.setCookie(
            response,
            WAFConfig.getCookieHead() + VERIFY_COOKIE,
            AzDGCrypt.encrypt(verifyCode, new String(WAFConfig.getAuthCode())),
            WAFConfig.getSessionTimeout(),
            AbstractInitializer.getWebRoot(),
            context.isHttps()
        );
        
        // 将图像输出到Servlet输出流中  
        OutputStream out;
        try {
            out = response.getOutputStream();
            SecureUtil.printVerifyCode(
                out,
                verifyCode,
                SecurityConfig.getVerifyCodeWidth(),
                SecurityConfig.getVerifyCodeHeight(),
                2,
                2
            );
            out.close();
        } catch (Exception e) {
            LOG.error("Fail to output verifyCode", e);
        }
        
        return RetCode.OK;
    }

    /**
     * 删除验证码的cookie
     * 与getVerifyCodeCookie放在VerifyCode中的目的是为了避免cookie名称的泛滥
     * @param context
     */
    public static final void rmvVerifyCodeCookie(MethodContext context) {
        CookieUtil.removeCookie(
            context.getResponse(),
            WAFConfig.getCookieHead() + VERIFY_COOKIE,
            AbstractInitializer.getWebRoot()
        );
    }
    
    /**
     * 获得验证码的cookie
     * @param context
     * @return
     */
    public static final String getVerifyCodeCookie(MethodContext context) {
        Map<String, Object> parameters = context.getParameters();
        String verifyCookie = JsonUtil.getAsStr(parameters, VerifyCode.VERIFY_COOKIE, "");
        return AzDGCrypt.decrypt(verifyCookie, new String(WAFConfig.getAuthCode()));
    }
}
