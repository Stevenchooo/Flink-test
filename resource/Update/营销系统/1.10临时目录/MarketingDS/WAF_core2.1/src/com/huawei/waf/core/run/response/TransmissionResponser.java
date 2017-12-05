package com.huawei.waf.core.run.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.response.TransmissionResponseConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.Const;

public class TransmissionResponser extends AbstractResponser {
    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException {
    	String content = JsonUtil.getAsStr(model, "content");
    	if(Utils.isStrEmpty(content)) {
    		output(context, out, new byte[]{});
    	} else {
    		output(context, out, content.getBytes(Const.DEFAULT_CHARSET));
    	}
    }

    @Override
    protected void setContentType(MethodContext context, HttpServletResponse response) {
    	TransmissionResponseConfig trc = (TransmissionResponseConfig)context.getMethodConfig().getResponseConfig();
        response.setContentType(trc.getContentType()); 
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
        return Const.XML_CONTENT_TYPE;
    }
}
