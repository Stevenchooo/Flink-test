package com.huawei.waf.core.run;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.util.TimeWinCounter;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.protocol.Const;
import com.huawei.waf.protocol.RetCode;
import com.huawei.waf.protocol.RetCode.CodePair;
import com.huawei.waf.servlet.AsyncContextListener;

public class MethodContext {
    private static final Logger LOG = LogUtil.getInstance();
    private static final int TIME_WIN = 64;
    private static final TimeWinCounter twc = new TimeWinCounter(TIME_WIN, 1000);
    private static final AtomicInteger runningTaskNum = new AtomicInteger(0);
    private AsyncContext asyncContext = null;
    private HttpServletRequest request = null;
    private HttpServletResponse respone = null;
    private Thread worker = null;
    private boolean valid = true;
    private MethodConfig config = null;
    private Map<String, Object> parameters = null;
    private Map<String, Object> results = new HashMap<String, Object>();
    private int resultCode = -1; //如果设置为OK，默认的info success设置不进去
    private String language = Const.DEFAULT_LANGUAGE;
    private String account = null;
    private String clientIp = null;
    private String userKey = null;
    
    private int checkType = 0;
    private boolean busy = false;
    private long createTime = System.currentTimeMillis();
    
    /**
     * 请求内容是否加密，如果请求内容加密，则相应内容也加密，
     * 加密的key就是userKey，在登录或其他渠道协商获得，
     * 默认的userKey是在登录时，服务端分配，告知给客户端，
     * 同时记录在通行证中，默认不加密
     * bit 0：表示请求是否加密
     * bit 1：表示响应是否要求加密
     */
    private int encrypt = 0x00;  
    
    /**
     * jsonp调用时传递的会掉函数名字的参数名，比如:callback=jQueryxxxx
     * jsonp是指这里的callback
     */
    private String jsonp = null;
    
    /**
     * 运行时数据，可以是任何类型的数据
     */
    private Object runtimeData = null;
    
    private List<String> originalParameters = null;
    
    public MethodContext(MethodConfig config, AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
        this.request = (HttpServletRequest)asyncContext.getRequest();
        this.respone = (HttpServletResponse)asyncContext.getResponse();
        this.config = config;
        this.asyncContext.addListener(new AsyncContextListener(this));
        this.setResult(RetCode.OK, Const.SUCCESS); //默认ok
    }

    /**
     * 用于复制会话，在接口需要并发多线程执行，每个接口参数不同时，使用复制
     * @param parent
     * @param parameters
     * @param result
     */
    public MethodContext(MethodContext parent, Map<String, Object> parameters, Map<String, Object> results) {
    	this.asyncContext = parent.asyncContext;
    	this.request = parent.request;
        this.respone = parent.respone;
        this.valid = parent.valid; //不能保证被复制的会话中valid状态一致
        this.config = parent.config;
        this.parameters = parameters; //请求参数
        this.results = results; //响应参数
        this.resultCode = parent.resultCode;
        this.language = parent.language;
        this.account = parent.account;
        this.clientIp = parent.clientIp;
        this.userKey = parent.userKey;
        this.checkType = parent.checkType;
        this.jsonp = parent.jsonp;
        this.encrypt = parent.encrypt;
    }
    
    public MethodContext(MethodContext parent, Map<String, Object> results) {
    	this(parent, parent.parameters, results);
    }
    
    public void invalidate() {
        this.valid = false;
        if(this.config.isInterruptable() && this.worker != null) { //打断线程执行，所有后面的执行都会出错
            try {
                this.worker.interrupt();
            } catch(Exception e) {
                LOG.error("Fail to interrupt the work thread, {}", this.config.getName());
            }
        }
    }
    
    public boolean isValid() {
        return this.valid;
    }
    
    public void setWorker(Thread worker) {
        this.worker = worker;
    }
    
    public boolean responseBusy() throws IOException {
        if(!valid) {
            return false;
        }
        respone.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        asyncContext.complete();
        return true;
    }
    
    public AsyncContext getContext() {
        if(isValid()) { //当会话超时或发生错误时，会被置为无效
            return asyncContext;
        }
        return null;
    }
    
    public void complete() {
        if(isValid()) { //当会话超时或发生错误时，会被置为无效
            asyncContext.complete();
        }
        runningTaskNum.decrementAndGet();
    }
    
    public HttpServletRequest getRequest() {
        return request;
    }
    
    public HttpServletResponse getResponse() {
        if(isValid()) { //当会话超时或发生错误时，会被置为无效
        	return respone;
        }
        return null;
    }
    
    public MethodConfig getMethodConfig() {
    	return config;
    }
    
    public void addParameters(Map<String, Object> parameters) {
    	if(parameters == null) {
    		return;
    	}
        this.parameters.putAll(parameters);
    }  
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public void setParameter(String key, Object val) {
        parameters.put(key, val);
    }

    public Map<String, Object> getParameters() {
    	return parameters;
    }
    
    /**
     * 通过参数名称获取参数的值，
     * @param name
     * @return
     */
    public Object getParameter(String name) {
        return parameters.get(name);
    }
    
    /**
     * 通过参数名称获取参数的值，
     * @param name
     * @return
     */
    public Object getContextParameter(String name) {
        ParameterInfo pi = config.getRequestConfig().getParameter(name);
        return pi != null ? pi.getValue(this) : null;
    }
        
    public Object getResult(String name) {
        return results.get(name);
    }
    
    public void setResult(String k, Object v) {
    	results.put(k, v);
    }
    
    /**
     * 设置返回码与返回信息，可能会被设置多次
     * @param resultCode
     * @param reason
     * @param coverIt 覆盖原有的reason
     */
    public int setResult(int retCode, String reason, boolean coverIt) {
        //设置了转换错误码，则一定使用转换后的错误码及原因
        CodePair cp = RetCode.getMapCode(retCode);
        if(cp != null) {
            this.resultCode = cp.retCode;
            this.results.put(config.getResultCodeName(), resultCode);
            this.results.put(config.getReasonName(), cp.description);
            return retCode;
        }
        
        if(coverIt || this.resultCode != retCode) {
            if(Utils.isStrEmpty(reason)) {
                reason = this.config.getResponseConfig().getReason(retCode);
                if(Utils.isStrEmpty(reason)) {
                    reason = RetCode.getDescription(retCode);
                }
                this.results.put(config.getReasonName(), reason == null ? "Unknown" : reason);
            } else {
                this.results.put(config.getReasonName(), reason);
            }
        }
        
        this.resultCode = retCode;
        this.results.put(config.getResultCodeName(), resultCode);
        return retCode;
    }
    
    public int setResult(int retCode, String reason) {
        return setResult(retCode, reason, false);
    }
    
    /**
     * 设置返回码，错误原因从默认的描述中寻找
     * @param resultCode
     */
    public int setResult(int retCode) {
        return setResult(retCode, null, false);
    }
    
    public void addResults(Map<String, Object> results) {
    	if(results == null) {
    		return;
    	}
        this.results.putAll(results);
    }

    public int getResultCode() {
        return resultCode;
    }
    
    public Map<String, Object> getResults() {
    	return results;
    }
    
    public String getRemoteAddr() {
    	return getRemoteAddress(this.request);
    }
    
	private static final String[] ipHeaders = new String[] {
		"HTTP_CLIENT_IP", 
		"HTTP_X_FORWARDED_FOR", 
		"x-forwarded-for"
	};
	
    public static final String getRemoteAddress(HttpServletRequest req) {
    	String ip;
    	char ch;
    	
    	for(String header : ipHeaders) {
    		ip = req.getHeader(header);
            if(ip != null && ip.length() > 0) {
            	ch = ip.charAt(0);
            	if(ch >= '0' && ch <= '9') { //only judge the first character
            		return ip;
            	}
            }
    	}
    	
        return req.getRemoteAddr();   
    }
    
    /**
     * 通过aide获取语言
     * aide默认从请求参数中获取语言，包括cookie
     */
    public void initLanguage() {
        language = config.getAide().getLanguage(this);
    }
    
    public String getLanguage() {
        return language;
    }
    
    public String getJsonp() {
        return jsonp;
    }

    /**
     * jsonp请求时，携带jsonp函数名的参数名称
     * @param jsonp
     */
    public void setJsonp(String jsonp) {
        this.jsonp = jsonp;
    }

    /**
     * 用户临时key，不同于用户密码
     * 在使用HttpClient访问时可以用于报文加密，
     * 在浏览器中使用是，可以用于防止跨站js攻击
     * @return
     */
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getClientIp() {
        if(Utils.isStrEmpty(clientIp)) {
            this.clientIp = getRemoteAddress(this.request);
        }
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        if(Utils.isStrEmpty(clientIp)) {
            this.clientIp = getRemoteAddress(this.request);
        } else {
            this.clientIp = clientIp;
        }
    }

    /**
     * 请求或响应是否需要加密
     * 在不同的请求中，对加密标志处理不同， 如果是二进制协议，需要其他方式传递此参数，
     * 加密消息体是不能用于传递此参数的，只能在url中传递，
     * 
     * 加密的情况，DefaultProjectAide中密码记录在通行证中，
     * 登录时，已通过响应消息中"__key"告知了通行证的中密码，
     * 客户端在请求时，可以通过增加_enc参数，告诉服务端请求、响应是否加密。
     * 因为密码在通行证中，通行证可以被截取，但是不能被解开，
     * 所以即使被截取，也不能构造出不同的参数，
     * 
     * 但是可以重放攻击，所以在系统中需要增加类似计数之类的东西，防止重放，
     * 一般放在AbstractProjectAide的子类中实现，默认的DefaultProjectAide未实现此功能，
     * 因为每种系统中记录信息的方法不一致。
     * 
     * @param encrypt 加密标志
     *      bit 0:请求是否加密
     *      bit 1:响应是否加密
     */
    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * 获得检查类型，比如：ip方式认证等
     * @return
     */
    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

	public Object getRuntimeData() {
		return runtimeData;
	}

	public void setRuntimeData(Object runtimeData) {
		this.runtimeData = runtimeData;
	}
	
    /**
     * 只有请求中设置了sign为true的情况，才会产生原始请求参数名称列表，
     * 名称是经过排序的
     * @return
     */
    public List<String> getOriginalParameters() {
        return originalParameters;
    }

    public void setOriginalParameters(List<String> originalParameters) {
        this.originalParameters = originalParameters;
    }
    
    public boolean isHttps() {
        return this.request.getScheme().equals("https");
    }
    
    public String getUrl() {
        return this.request.getRequestURL().toString();
    }
    
    public void judgeBusy() {
        int num = runningTaskNum.incrementAndGet();
        long interval = System.currentTimeMillis() - createTime;
        
        if(interval >= config.getMaxInterval()) {
            LOG.info("Busy now, task({}) wait {}ms", config.getName(), interval);
            twc.inc(1);
            this.busy = true;
            return;
        }
        
        twc.inc(0);
        //预留一部分线程，防止整个线程池都被堵塞，设置busy，但是不计数
        int freeThread = WAFConfig.getThreadPoolSize() - num;
        if(freeThread <= WAFConfig.getReservedThreadNum()) {
            LOG.info("Too busy, use reserved thread to run");
            this.busy = true;
            return;
        }
        
        if(twc.getTotal() >= WAFConfig.getMaxBusyTimeoutNum()) {
            LOG.warn("Busy now, there are {} tasks wait more than {}ms, in {}s",
                    twc.getTotal(), config.getMaxInterval(), TIME_WIN);
            this.busy = true;
        }
    }
    
    /**
     * 业务如果能在繁忙情况下对业务进行降级处理，则需要判断isBusy
     * SECONDS秒内，如果busy的数量超过上限，则直接认为当前处于忙
     * @return
     */
    public boolean isBusy() {
        return busy;
    }
}
