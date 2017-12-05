package com.huawei.waf.core.run.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;

import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.Const;

public class JsonResponser extends AbstractResponser {
    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException {
        byte[] bytes = JsonUtil.mapToBytes(model);
        //只有json格式的才需要加密，html无需加密
        output(context, out, bytes);
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
