package com.huawei.manager.base.run.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.response.HtmlResponser;
import com.huawei.waf.facade.run.IMethodAide;

/**
 * AuthHtmlResponser
 * 
 * @author w00296102
 */
public class AuthHtmlResponser extends HtmlResponser
{
    private static final String META = "meta";

    /**
     * 输出
     * 
     * @param context
     *            context
     * @param out
     *            out
     * @param model
     *            model
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    @Override
    public void output(MethodContext context, OutputStream out,
            Map<String, Object> model) throws ServletException, IOException
    {
        model.put(IMethodAide.USERKEY, context.getUserKey());
        model.put(IMethodAide.USERACCOUNT, context.getAccount());
        Object meta = context.getParameter(META);
        if (meta == null)
        {
            meta = context.getMethodConfig().getDataType();
        }
        model.put(META, meta);
        super.output(context, out, model);
    }
}
