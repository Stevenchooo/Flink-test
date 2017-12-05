package com.huawei.waf.servlet;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.response.ResultSetConfig;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.Methods;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.response.RDBResponseConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.MethodRunner;
import com.huawei.waf.facade.config.AbstractResponseConfig;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.facade.run.AbstractRequester;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.facade.run.IMethodAide;
import com.huawei.waf.protocol.Const;
import com.huawei.waf.protocol.RetCode;
import com.huawei.waf.facade.MethodRunner.MethodRunnable;

//@WebServlet(name = "index", loadOnStartup = 1, urlPatterns = {"/*"}, asyncSupported = true)
public class Dispatcher extends HttpServlet {
    protected static final long serialVersionUID = 100001L;
    private static final Logger LOG = LogUtil.getInstance();
    private static final Logger ACCESS_LOG = LogUtil.getInstance("ACCESS");
    
    private static String PARA_VERSION = "api_ver";
    
    private static final AtomicLong serialNo = new AtomicLong(0); 
    
    /**
     * 主要的处理函数，所有关于method的分发都在这个函数中完成
     * 启动异步servlet会话，在线程池中负责完成异步会话后面的业务逻辑。
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void process(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException {
        final String method = getMethod(req, resp);
        if(Utils.isStrEmpty(method)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        final MethodConfig methodConfig = Methods.get(method);
        if(methodConfig == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            LOG.warn("Fail to get method {}", method);
            return;
        }
        
        if(methodConfig.getRequestConfig().isHttpsOnly()) {
            if(!req.getScheme().equals("https")) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                LOG.warn("{} is not a https request", method);
                return;
            }
        }
        
        //容许异步会话，如果不写这句，在使用了filter设置cookie时会异常
        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        final long startTime = System.currentTimeMillis(); 
        if(LOG.isDebugEnabled()) {
            LOG.debug("REQUEST: {}", method);
        }
        
        /**
         * 在servlet线程中启动异步会话，而不是在线程池中启动异步会话，
         * 防止非常繁忙时，线程池排队，导致会话被阻塞在线程池中，
         * 没有办法启动异步会话 
         */
        
        try {
			req.setCharacterEncoding(Const.DEFAULT_ENCODING);
            AsyncContext asyncContext = req.startAsync();
            asyncContext.setTimeout(methodConfig.getTimeout()); //默认超时时间为10秒
            //构建请求的会话
            MethodContext context = new MethodContext(methodConfig, asyncContext);
            
            context.setEncrypt(Utils.parseInt(req.getParameter(Const.ENCRYPT), 0));
            
            MethodRunner.exec(new MethodRunnable(context) {
                public void run() {
                    //设置线程名称，每个接口不同，这样容易定位问题
                    Thread worker = Thread.currentThread();
                    worker.setName(method + '_' + serialNo.addAndGet(1));
                    context.setWorker(worker);
                    if(!context.isValid()) { //可能任务队列还没有排到这个任务，会话就已经超时了
                        LOG.warn("Context is already timeout, discard it directly");
                        return;
                    }
                    context.judgeBusy(); //判断在队列中等待的时长是否超过了最大预估的执行时长，如果超过了，则业务应该做降级处理
                	
                    int retCode = RetCode.OK;
                    AbstractProcessor processor = methodConfig.getProcessor();
                    AbstractResponser responser = methodConfig.getResponser();
                    try {
                        AbstractRequester requester = methodConfig.getRequester();
                        Map<String, Object> parameters = new HashMap<String, Object>(); 
                        context.setParameters(parameters); //先设置到context中，避免中途用到
                        if((retCode = requester.buildParameters(context, parameters)) == RetCode.OK) {
                            context.initLanguage(); //本质是调用aide获取当前的语言
                            
                            context.setCheckType(processor.getCheckType());
                            retCode = methodConfig.getAide().parseAuthInfo(context); 
                        	if(retCode == RetCode.OK && (retCode = requester.checkRequest(context)) == RetCode.OK) {
                                retCode = processor.handle(context);
                        	}
                        }
                        context.setResult(retCode);
                        
                        if(context.isValid()) {
                            responser.response(context);
                        } else {
                            LOG.info("context is already invalid when response");
                        }
                        
                        if(WAFConfig.isAccessLog()) {
                            Logger ACCESSLOG = getAccessLogger();
                            if(ACCESSLOG.isInfoEnabled()) {
                                //删除不可以打印的字段，打印敏感信息会引起法律纠纷，比如密码
                                Map<String, Object> paras = new HashMap<String, Object>(parameters);
                                paras.remove(IMethodAide.USERKEY);
                                filter(paras, methodConfig.getRequestConfig().getParameters());
                                paras.put(IMethodAide.USERACCOUNT, context.getAccount());
                                //打印响应信息
                                Map<String, Object> results = new HashMap<String, Object>(context.getResults());
                                filter(results, methodConfig.getResponseConfig());
                                
                                if(methodConfig.isCanLog()) {
                                    ACCESSLOG.info("{}-REQUEST: {}\nRESPONSE: {}",
                                            System.currentTimeMillis() - startTime,
                                            paras.toString(),
                                            results.toString());
                                } else {
                                    ACCESSLOG.info("{}-REQUEST: {}",
                                            System.currentTimeMillis() - startTime,
                                            paras.toString());
                                }
                            }
                        }
                    } catch(Exception e) {
                        context.setResult(RetCode.INTERNAL_ERROR);
                        try {
                            responser.response(context);
                        } catch(Exception ex) {
                            LOG.error("Fail to call response", ex);
                        }
                        LOG.error("Fail to execute {}", methodConfig.getName(), e);
                    } finally {
                        try {
                            processor.afterAll(context);
                        } catch(Exception e) {
                            LOG.error("Fail to call afterAll of {}", methodConfig.getName(), e);
                        }
                        context.complete();
                    }
                }
            });
        } catch(SecurityException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            LOG.warn("Fail to get method {}", method, e);
        } catch(Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            LOG.error("Fail to execute {}", method, e);
        }
    }
    
    /**
     * 通过重载此函数，改变method
     * @param req
     * @param resp
     * @return
     */
    protected String getMethod(HttpServletRequest req, HttpServletResponse resp) {
        String uri = req.getRequestURI();
        String webRoot = AbstractInitializer.getWebRoot();
        int pos = uri.indexOf(webRoot);
        if(pos < 0) {
            LOG.warn("URI {} not started with {}", uri, webRoot);
            return null;
        }
        
        uri = uri.substring(pos + webRoot.length());
        if(Utils.isStrEmpty(uri)) { //支持不带具体路径的访问
        	uri = "/";
        }
        
        String ver = req.getParameter(PARA_VERSION); //版本必须作为参数传入
        if(ver == null || ver.equals("")) {
            ver = "1";
        }
        
        return ver + uri;	
    }
    
    protected void setVersionName(String verName) {
    	PARA_VERSION = verName;
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        String root = AbstractInitializer.getWebRoot();
        LOG.info("Run dispatcher in [{}]", Utils.isStrEmpty(root) ? "/" : root);
    }
    
    @Override
    public void destroy() {
        String root = AbstractInitializer.getWebRoot();
        LOG.info("Destory dispatcher in [{}]", Utils.isStrEmpty(root) ? "/" : root);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        process(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        process(req, resp);
    }
    
    protected static void filter(Map<String, Object> p, ParameterInfo[] paras) {
		for(ParameterInfo pi : paras) {
			if(!pi.isCanLog()) {
				p.put(pi.getName(), "***");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected static void filter(Map<String, Object> results, AbstractResponseConfig responseConfig) {
        //如果是数据库操作的响应，则需要过滤掉不可以打印的内容
        if(!(responseConfig instanceof RDBResponseConfig)) {
        	return;
        }
    	RDBResponseConfig config = (RDBResponseConfig)responseConfig;
    	
        for(ResultSetConfig rsc : config.getResultSetConfigs()) {
        	if(!rsc.isCanLog()) {
        		results.remove(rsc.getName());
        		continue;
        	}
        	
        	if(rsc.isMerge()) {
        		filter(results, rsc.getSegmentList());
        	} else if(rsc.isMulti()) {
    			Map<String, Object> line;
    			List<Object> list = JsonUtil.getAsList(results, rsc.getName());
    			if(list != null) {
	    			int num = list.size();
	    			
	    			for(int i = 0; i < num; i++) {
	    				line = new HashMap<String, Object>((Map<String, Object>)list.get(i));
	    				list.set(i, line);
	            		filter(line, rsc.getSegmentList());
	    			}
    			}
        	} else {
        		Map<String, Object> map = new HashMap<String, Object>(JsonUtil.getAsObject(results, rsc.getName()));
        		filter(map, rsc.getSegmentList());
        		results.put(rsc.getName(), map);
        	}
        }
	}
	
	/**
	 * 接口详情的接口，此log会打印详细的请求、响应内容（过滤掉不可打印的），
	 * 此日志的日志级别至少是INFO级别
	 * 默认使用默认的日志文件，可以通过重载此函数改变日志文件
	 * @return
	 */
	protected Logger getAccessLogger() {
	    return ACCESS_LOG != null ? ACCESS_LOG : LOG;
	}
}
