package com.huawei.waf.facade.run;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;
import com.huawei.waf.protocol.Const;

public abstract class AbstractResponser extends AbstractHandler {
    private static final Logger LOG = LogUtil.getInstance();
    private static final String HEADER_CORS = "Access-Control-Allow-Origin";
    /**
     * 输出model内容到out中
     * @param context method会话
     * @param out
     * @param model
     * @throws ServletException
     * @throws IOException
     */
    abstract protected void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException;
    abstract protected String getContentType(MethodContext context);
    
    protected void setContentType(MethodContext context, HttpServletResponse response) {
        response.setContentType(getContentType(context)); 
    }
    /**
     * 给客户端响应 网页
     * @param context 异步servlet会话
     * @param model 数据模型
     * @param ftlFile 模板文件名
     */
    public void response(MethodContext context)
        throws ServletException, IOException {
        AsyncContext httpContext = context.getContext();
        if(httpContext == null) {
            throw new ServletException("AsyncContext is timeout or error, no response");
        }
        
        MethodConfig mc = context.getMethodConfig();
        AbstractResponseConfig respConfig = mc.getResponseConfig();
        if(!respConfig.isNeedResponse()) { //不需要框架处理响应
            return;
        }
        
        HttpServletResponse response = context.getResponse();
        if(response == null) {
            LOG.error("Fail to get response from context, when execute {}", mc.getName());
            return;
        }
        setContentType(context, response);
        
        if(respConfig.isNoCache()) { //接口的返回都应该nocache
            setBrowserNoCache(response);
        }
        
        String cors = respConfig.getCors();
        if(cors != null) { //允许跨域访问
            response.setHeader(HEADER_CORS, cors);
        }
        
        Map<String, Object> model = context.getResults();
        AbstractRequestConfig reqCfg = mc.getRequestConfig();
        /**
         * 将需要设置到响应中的内容设置到result中，
         * 在响应json或页面中可以直接使用request中的参数
         * 在参数中增加"response":true配置
         */
        ParameterInfo[] paras = reqCfg.getResponseParameters();
        if(paras != null) {
            for(ParameterInfo pi: paras) {
                model.put(pi.getName(), pi.getValue(context));
            }
        }
        
        OutputStream out = response.getOutputStream();
        output(context, out, model);
        out.close(); //必须关闭，否则在gzip时，无法返回
    }
    
    public static final void output(MethodContext context, OutputStream out, byte[] content)
        throws ServletException, IOException {
        try {
            String jsonp = context.getJsonp();
            if(jsonp == null) {
                out.write(content);
            } else {
                /**
                 * jsonp本质是返回一个js脚本，jsonp的情况下，一定不加密，
                 * 因为它一定是用浏览器访问的，而浏览器的情况，使用https来解决保密问题，
                 * 脚本中执行有jsonp指定的回调函数，
                 * 所以只有json类型的返回数据才可以执行jsonp
                 */
                out.write(jsonp.getBytes(Const.DEFAULT_CHARSET));
                out.write('(');
                out.write(content);
                out.write(')');
            }
            out.flush();
        } catch (Exception e) {
            LOG.error("Fail to output message", e);
        }
    }
    
    //超时时间倒回1年
    protected static final String EXPIRATION_DATE =
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US).format(new Date().getTime() - 86400 * 365 * 1000);
    
    /**
     * If the parameter "nocache" was set to true, generate a set of headers
     * that will advise the HTTP client not to cache the returned page.
     */
    public static final void setBrowserNoCache(HttpServletResponse response) {
        // HTTP/1.1 + IE extensions + FF
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        // HTTP/1.0
        response.setHeader("Pragma", "no-cache");
        // Last resort for those that ignore all of the above
        response.setHeader("Expires", EXPIRATION_DATE);
    }
}