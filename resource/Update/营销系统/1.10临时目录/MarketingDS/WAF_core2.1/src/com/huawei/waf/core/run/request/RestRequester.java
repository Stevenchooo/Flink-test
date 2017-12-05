package com.huawei.waf.core.run.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractRequester;
import com.huawei.waf.protocol.RetCode;

public class RestRequester extends AbstractRequester {
    @Override
    public int buildParas(MethodContext context, Map<String, Object> reqParameters) {
        HttpServletRequest req = context.getRequest();
        Map<String, String[]> p = req.getParameterMap();
        String[] v;
        int i, num;
        
        for(Map.Entry<String, String[]> o : p.entrySet()) {
            v = o.getValue();
            if(v == null || v.length == 0) {
                continue;
            }
            
            if(v.length == 1) { //not a list
                reqParameters.put(o.getKey(), v[0]);
                continue;
            }

            List<String> vv = new ArrayList<String>(); //转成list，与json保持一致
            num = v.length;
            for(i = 0; i < num; i++) {
                vv.add(v[i]);
            }
            reqParameters.put(o.getKey(), vv);
        }
        
        /**
         * 只有设置了sign的情况，才会将原始请求参数记录下来
         */
        MethodConfig mc = context.getMethodConfig();
        if(mc.getRequestConfig().isSign()) {
            List<String> orgParameters = new ArrayList<String>(reqParameters.keySet());
            Collections.sort(orgParameters);
            context.setOriginalParameters(orgParameters);
        }
        context.setJsonp(JsonUtil.getAsStr(reqParameters, mc.getJsonP(), null));
        
        return RetCode.OK;
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }
}
