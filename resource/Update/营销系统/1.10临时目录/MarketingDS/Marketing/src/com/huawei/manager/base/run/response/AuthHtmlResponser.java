package com.huawei.manager.base.run.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.response.HtmlResponser;
import com.huawei.waf.facade.run.IMethodAide;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class AuthHtmlResponser extends HtmlResponser
{
    /**
     * meta
     */
    private static final String META = "meta";
    
    /**
     * 输出
     * @param context      上下文
     * @param out          输出流
     * @param model        模型
     * @throws ServletException      servlet异常
     * @throws IOException           IO异常
     */
    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model)
        throws ServletException, IOException
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
