package com.huawei.waf.facade.run;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.protocol.RetCode;

public abstract class AbstractRequester extends AbstractHandler {
    private static final Logger LOG = LogUtil.getInstance();
    
    abstract protected int buildParas(MethodContext context, Map<String, Object> reqParameters);
    
    public int buildParameters(MethodContext context, Map<String, Object> reqParameters) {
        int retCode = buildParas(context, reqParameters);
        if(retCode != RetCode.OK) {
            LOG.error("Fail to get parameters from request");
            return retCode;
        }
        
        HttpServletRequest req = context.getRequest();
        
        /**
         * 从cookie中获取参数，比如通行证等，如果请求参数与cookie都有，以cookie优先
         * 放在buildParas前面，防止通行证在cookie中，
         * JsonRequester中，如果消息体加密了，会用到通行证
         */
        Cookie[] cookies = req.getCookies();
        if(cookies != null) { //cookie is treated as normal parameters
            String cookieHead = WAFConfig.getCookieHead();
            String name;
            int start = Utils.isStrEmpty(cookieHead) ? 0 : cookieHead.length();
            
            for(Cookie c : cookies) {
                name = c.getName();
                //通过头部过滤掉非本域的cookie
                if(start > 0 && name.indexOf(cookieHead) == 0) {
                    reqParameters.put(name.substring(start), c.getValue());
                }
            }
        }
        
        return RetCode.OK;
    }
    
    public int checkRequest(MethodContext context) {
        MethodConfig mc = context.getMethodConfig();
        AbstractRequestConfig rc = mc.getRequestConfig();
        int retCode = RetCode.OK;
        Object v;
        
        int sn = 1;
        for(ParameterInfo pi : rc.getParameters()) {
            v = pi.getValue(context); //必须先调用getValue，否则系统参数类型可能不在请求参数中
            if(!pi.check(context, v)) {
            	retCode = context.getResultCode();
            	if(retCode == RetCode.OK) {
	                retCode = pi.getWrongCode(sn);
	                //约定返回码-RetCode.WRONG_PARAMETER为第N个参数错误，也可以通过配置制定返回码
	                context.setResult(retCode, "parameter(" + pi.getName() + ") error:" + pi.getClaim());
            	}
            	break;
            }
            
            sn++;
        }
        
        return retCode;
    }
}