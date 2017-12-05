package com.huawei.waf.core.run.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.Const;

public class EmptyResponser extends AbstractResponser {
    private static final Logger LOG = LogUtil.getInstance();

    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException {
        HttpServletResponse response = context.getResponse();
        if(response != null) {
            //response.setContentType("application/octet-stream");
            //不做任何处理，响应内容由processor提供
        } else {
            LOG.error("Fail to get response from context when call {}", context.getMethodConfig().getName());
        }
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
        return Const.BIN_CONTENT_TYPE;
    }
}
