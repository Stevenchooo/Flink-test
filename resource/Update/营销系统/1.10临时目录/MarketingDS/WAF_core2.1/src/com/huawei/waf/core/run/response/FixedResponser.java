package com.huawei.waf.core.run.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;

import com.huawei.waf.core.config.method.response.FixedResponseConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.Const;

public class FixedResponser extends AbstractResponser {
    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException {
    	FixedResponseConfig frc = (FixedResponseConfig)context.getMethodConfig().getResponseConfig(); 
        output(context, out, frc.getContent());
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }

    @Override
    public String getContentType(MethodContext context) {
        return Const.JSON_CONTENT_TYPE;
    }
}
