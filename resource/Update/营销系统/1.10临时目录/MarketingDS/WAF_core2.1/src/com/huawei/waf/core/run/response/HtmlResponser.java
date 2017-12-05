package com.huawei.waf.core.run.response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.Languages;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.response.HtmlResponseConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.protocol.Const;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.core.Configurable;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class HtmlResponser extends JsonResponser {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String WEB_ROOT = "webRoot";
    private static final String IMG_ROOT = "imgRoot";
    private static final String JS_ROOT  = "jsRoot";
    
    private static final String COOKIE_HEAD = "cookieHead";
    
    private static final String FREEMARK_SETTINGS = "settings";
    private static final String FREEMARK_VARIABLES = "variables";
    
    private static final String INITPARAM_TEMPLATE_PATH = "template_path";
    private static final String INITPARAM_CONTENT_TYPE = "content_type";
    private static final String DEPR_INITPARAM_WRAPPER_SIMPLE = "simple";
    private static final String DEPR_INITPARAM_WRAPPER_BEANS = "beans";
    private static final String DEPR_INITPARAM_WRAPPER_JYTHON = "jython";
    private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_RETHROW = "rethrow";
    private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_DEBUG = "debug";
    private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG = "htmlDebug";
    private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_IGNORE = "ignore";
    
    private static String contentType = Const.HTML_CONTENT_TYPE;

    private static final String RESULT_LANGUAGE = "lang";
    private static final String RESULT_VARIABLES = "vars";
    private static final String RESULT_CUR_LANGUAGE = "curLang";
    
    private static Map<String, Object> variables;
    
    
    //-------------------------------------------------------------------------
    //freemarker
    protected static final Configuration config = new Configuration();
    private static ObjectWrapper wrapper = null;
    private static String htmlPath;
    //-------------------------------------------------------------------------
    
    /**
     * 给客户端响应 网页
     * @param context 异步servlet会话
     * @param model 数据模型
     * @param ftlFile 模板文件名
     */
    @Override
    public void output(MethodContext context, OutputStream out, Map<String, Object> model) throws ServletException, IOException {
        MethodConfig mc = context.getMethodConfig();
        HtmlResponseConfig rc = (HtmlResponseConfig)mc.getResponseConfig();
        String ftlFile = rc.getTemplate(context.getResultCode());
        
        //当无模板时，直接返回json，相当于JsonResponse
        if(ftlFile == null) {
            super.output(context, out, model);
            return;
        }
        
        HttpServletResponse response = context.getResponse();
        if(response == null) {
            LOG.error("Fail to get response from context when call {}", mc.getName());
            return;
        }

        /**
         * 处理页面中的参数，比如webroot、语言标签等
         */
        //---------------------------------------------------------------------
        Map<String, Object> pageParameters = new HashMap<String, Object>(model);
        String lang = context.getLanguage();
        //有模板的情况，设置语言标签
        //模板中可以获取当前语言的名称，以及对应的语言标签
        Map<String, String> tags = Languages.get(lang);
        if(tags != null) {
            pageParameters.put(RESULT_LANGUAGE, tags);
        }
        pageParameters.put(RESULT_CUR_LANGUAGE, lang);

        //用于在页面中处理URI的绝对路径
        pageParameters.put(RESULT_VARIABLES, variables);
        //---------------------------------------------------------------------
        
        Template template = null;
        try {
            template = config.getTemplate(ftlFile, Const.DEFAULT_ENCODING);
        } catch (FileNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            LOG.error("{} not found", ftlFile, e);
            return;
        }
        
        //设置http头
        Object attrContentType = template.getCustomAttribute(INITPARAM_CONTENT_TYPE);
        if(attrContentType != null) {
            response.setContentType(attrContentType.toString());
        } else {
            response.setContentType(contentType);
        }
        
        try {
            // Process the template
			//必须指定编码，否则，在部分机器上运行会乱码
            template.process(pageParameters, new OutputStreamWriter(out, Const.DEFAULT_CHARSET));
        } catch (TemplateException te) {
            LOG.error("Error executing freemarker template {}", ftlFile, te);
        }
    }
    
    //-------------------------------------------------------------------------
    //freemarker init
    public static final synchronized boolean initFreemarker(ServletContext context, Map<String, Object> cfgs) {
        if(wrapper != null) { //已经初始化过
            return true;
        }

        variables = JsonUtil.getAsObject(cfgs, FREEMARK_VARIABLES);
        if(variables == null) {
            variables = new HashMap<String, Object>();
        }
        variables.put(COOKIE_HEAD, WAFConfig.getCookieHead());
        
        String webRoot = AbstractInitializer.getWebRoot();
        
        /**
         * 设置默认的js与img的根目录，在freemarker模板中可以使用
         **/
        String s = JsonUtil.getAsStr(variables, WEB_ROOT);
        if(Utils.isStrEmpty(s)) {
            variables.put(WEB_ROOT, webRoot);
        }
        
        s = JsonUtil.getAsStr(variables, IMG_ROOT);
        if(Utils.isStrEmpty(s)) {
            variables.put(IMG_ROOT, webRoot + "/imgs");
        }
        
        s = JsonUtil.getAsStr(variables, JS_ROOT);
        if(Utils.isStrEmpty(s)) {
            variables.put(JS_ROOT, webRoot + "/js");
        }
        
        Map<String, Object> cfg = JsonUtil.getAsObject(cfgs, FREEMARK_SETTINGS);
        if(cfg == null) {
            cfg = new HashMap<String, Object>();
        }
        
        String templatePath = "/WEB-INF/views"; //路径写死
        cfg.put(INITPARAM_TEMPLATE_PATH, templatePath);
        LOG.info("{} is {}", INITPARAM_TEMPLATE_PATH, templatePath);
        htmlPath = FileUtil.addPath(context.getRealPath("/"), templatePath.substring(1));
        
        //+++exception handler+++
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        
        //+++wrapper+++
        // Process object_wrapper init-param out of order: 
        String value = JsonUtil.getAsStr(cfg, Configuration.OBJECT_WRAPPER_KEY, null);
        
        //不能写在上面{}里面，因为如果没有设置，value为空，返回默认wrapper
        wrapper = createObjectWrapper(value);
        config.setObjectWrapper(wrapper);
        LOG.info("Using object wrapper of class {}", wrapper.getClass().getName());
        
        for (String key : cfg.keySet()) {
            value = JsonUtil.getAsStr(cfg, key, "");
            try {
                if(key.equals(Configuration.DEFAULT_ENCODING_KEY)) {
                    config.setDefaultEncoding(value);
                } else if(key.equals(INITPARAM_TEMPLATE_PATH)) {
                    LOG.info("{} is {}", INITPARAM_TEMPLATE_PATH, value);
                    config.setTemplateLoader(createTemplateLoader(context, value));
                } else if(key.equals(Configuration.TEMPLATE_UPDATE_DELAY_KEY)) { //单位：秒
                    config.setTemplateUpdateDelay(Integer.valueOf(value));
                } else if(key.equals(Configuration.TEMPLATE_EXCEPTION_HANDLER_KEY)) {
                    if(DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_RETHROW.equals(value)) {
                        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
                    } else if(DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_DEBUG.equals(value)) {
                        config.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
                    } else if(DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG.equals(value)) {
                        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
                    } else if(DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_IGNORE.equals(value)) {
                        config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
                    } else {
                        LOG.error("Invalid value for freemarker exception handler {}:{}", Configuration.TEMPLATE_EXCEPTION_HANDLER_KEY, value);
                    }
                } else if(key.equals(INITPARAM_CONTENT_TYPE)) {
                    contentType = value;
                } else {
                    LOG.info("config key={}, value={}", key, value);
                    config.setSetting(key, value);
                }
            } catch (Exception e) {
                LOG.error("Fail to init freemarker", e);
            }
        }
        
        return true;
    }
    
    /**
     * This method is called from {@link #init()} to create the
     * FreeMarker object wrapper object that this servlet will use
     * for adapting request, session, and servlet context attributes into 
     * template models.. This is a hook that allows you
     * to custom-configure the wrapper object in a subclass.
     * The default implementation returns a wrapper that depends on the value
     * of <code>ObjectWrapper</code> init parameter. If <code>simple</code> is
     * specified, {@link ObjectWrapper#SIMPLE_WRAPPER} is used; if <code>jython</code>
     * is specified, {@link freemarker.ext.jython.JythonWrapper} is used. In
     * every other case {@link ObjectWrapper#DEFAULT_WRAPPER} is used.
     */
    protected static final ObjectWrapper createObjectWrapper(String wrapper) {
        if(wrapper == null || wrapper.isEmpty()) {
            return ObjectWrapper.DEFAULT_WRAPPER;
        }
        
        if(DEPR_INITPARAM_WRAPPER_BEANS.equals(wrapper)) {
            return ObjectWrapper.BEANS_WRAPPER;
        }
        
        if(DEPR_INITPARAM_WRAPPER_SIMPLE.equals(wrapper)) {
            return ObjectWrapper.SIMPLE_WRAPPER;
        }
        
        if(DEPR_INITPARAM_WRAPPER_JYTHON.equals(wrapper)) {
            // Avoiding compile-time dependency on Jython package
            try {
                return (ObjectWrapper)Class.forName("freemarker.ext.jython.JythonWrapper").newInstance();
            } catch (InstantiationException e) {
                throw new InstantiationError(e.getMessage());
            } catch (IllegalAccessException e) {
                throw new IllegalAccessError(e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        
        try {
            config.setSetting(Configurable.OBJECT_WRAPPER_KEY, wrapper);
            return config.getObjectWrapper();
        } catch (TemplateException e) {
            LOG.error("Fail to set wrapper:{}", wrapper, e);
            return ObjectWrapper.DEFAULT_WRAPPER;
        }
    }
    
    /**
     * Create the template loader. The default implementation will create a
     * {@link ClassTemplateLoader} if the template path starts with "class://",
     * a {@link FileTemplateLoader} if the template path starts with "file://",
     * and a {@link WebappTemplateLoader} otherwise.
     * @param templatePath the template path to create a loader for
     * @return a newly created template loader
     * @throws IOException
     */
    protected static final TemplateLoader createTemplateLoader(ServletContext context, String templatePath) {
        try {
            if(templatePath.startsWith("class://")) {
                // substring(7) is intentional as we "reuse" the last slash
                return new ClassTemplateLoader(HtmlResponser.class, templatePath.substring(7));
            } else {
                if(templatePath.startsWith("file://")) {
                    templatePath = templatePath.substring(7);
                    return new FileTemplateLoader(new File(templatePath));
                } else {
                    return new WebappTemplateLoader(context, templatePath);
                }
            }
        } catch (IOException e) {
            LOG.error("Fail to init templateloader", e);
            return null;
        }
    }
    
    /**
     * Maps the request URL to a template path that is passed to 
     * {@link Configuration#getTemplate(String, Locale)}. You can override it
     * (i.e. to provide advanced rewriting capabilities), but you are strongly
     * encouraged to call the overridden method first, then only modify its
     * return value. 
     * @param request the currently processed request
     * @return a String representing the template path
     */
    protected static final String requestUrlToTemplatePath(HttpServletRequest request) {
        // First, see if it is an included request
        String includeServletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
        if(includeServletPath != null) {
            // Try path info; only if that's null (servlet is mapped to an
            // URL extension instead of to prefix) use servlet path.
            String includePathInfo = (String)request.getAttribute("javax.servlet.include.path_info");
            return includePathInfo == null ? includeServletPath : includePathInfo;
        }
        // Seems that the servlet was not called as the result of a 
        // RequestDispatcher.include(...). Try pathInfo then servletPath again,
        // only now directly on the request object:
        String path = request.getPathInfo();
        if(path != null) {
            return path;
        }
        
        path = request.getServletPath();
        if(path != null) {
            return path;
        }
        // Seems that it is a servlet mapped with prefix, and there was no extra path info.
        return "";
    }

    @Override
    public String getContentType(MethodContext context) {
        return Const.HTML_CONTENT_TYPE;
    }
    
    /**
     * 获取freemaker的配置信息
     * @return
     */
    public static Configuration getFreemarkerConfig() {
        return config;
    }
    
    /**
     * 模板存放的根目录
     * @return
     */
    public static final String getHtmlPath() {
    	return htmlPath;
    }
}
