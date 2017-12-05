package com.huawei.waf.core.run.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class JsonRequester extends RestRequester {
    private static final String METHOD_POST = "POST";
    private static final Logger LOG = LogUtil.getInstance();
    
    @Override
    public int buildParas(MethodContext context, Map<String, Object> reqParameters) {
        int retCode = super.buildParas(context, reqParameters); 
        if(retCode != RetCode.OK) {
            return retCode;
        }
        
        HttpServletRequest req = context.getRequest();
        if(!METHOD_POST.equals(req.getMethod())) { //兼容配置错误，非post方式，无消息体
            return RetCode.OK;
        }
        
        Map<String, Object> para = null;
        try {
            para = JsonUtil.jsonStreamToMap(req.getInputStream());
            if(para != null) {
                reqParameters.putAll(para);
            } else {
                LOG.warn("Fail to read json data from input stream in {}", context.getMethodConfig().getName());
            }
            
            /**
             * 只有设置了sign的情况，才会将原始请求参数记录下来，
             * 这里的操作在父类中做过一次，但是这里又有了更新
             */
            if(context.getMethodConfig().getRequestConfig().isSign()) {
                List<String> orgParameters = new ArrayList<String>(reqParameters.keySet());
                Collections.sort(orgParameters);
                context.setOriginalParameters(orgParameters);
            }            
            return RetCode.OK;
        } catch (IOException e) {
            LOG.warn("Fail to read json data from input stream in {}", context.getMethodConfig().getName());
            return RetCode.WRONG_PARAMETER;
        }
    }
}
