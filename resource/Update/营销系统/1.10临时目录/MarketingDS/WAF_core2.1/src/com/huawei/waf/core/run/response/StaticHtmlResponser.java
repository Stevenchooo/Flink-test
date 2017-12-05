package com.huawei.waf.core.run.response;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletException;
import com.huawei.util.FileUtil;
import com.huawei.util.LocalCacheUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.response.StaticHtmlResponseConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.Const;

public class StaticHtmlResponser extends AbstractResponser {
    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException {
        MethodConfig methodConfig = context.getMethodConfig();
        String methodName = methodConfig.getName();
        byte[] content = (byte[])LocalCacheUtil.get(methodName);
        if(content == null) {
            StaticHtmlResponseConfig shrc = (StaticHtmlResponseConfig)methodConfig.getResponseConfig();
            File htmlFile = shrc.getHtml();
            content = FileUtil.readFile(htmlFile);
            LocalCacheUtil.put(methodName, content, shrc.getCacheTime());
        }
        output(context, out, content);
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
        return Const.HTML_CONTENT_TYPE;
    }
}
