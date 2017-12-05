package com.huawei.waf.core.config.method.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig;
import com.huawei.waf.protocol.Const;

/**
 * @author l00152046
 * HTTP请求处理配置
 */
public class HttpProcessConfig extends AbstractProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();

    protected static final String CONFIG_URL = "url";
	private static final String CONFIG_RETCODE_NAME = "retCodeName";
	private static final String CONFIG_METHOD = "method";
	private static final String CONFIG_RESPONSE = "response";
	private static final String CONFIG_TIMEOUT = "timeout";
	private static final String CONFIG_MAXTOTAL = "maxTotal";
	private static final String CONFIG_MAXPERROUTE = "maxPerRoute";
	private static final String CONFIG_TIMETOLIVE = "timeToLive";
	
	private static final String CONFIG_CONTENTTYPE = "contentType"; //转给第三方时的contentType
	private static final String CONFIG_REQPARAMS = "reqParams"; //需要透传的请求参数
	private static final String CONFIG_RESPPARAMS = "respParams"; //需要透传的响应参数
	private static final String CONFIG_EXTRA_PARAMETERS = "extraParams"; //附加参数，通常是静态的参数
    private static int DEFAULT_TIMEOUT = 5 * 1000;
	private String url;
	
	public enum METHOD {POST, GET};
	
	public enum RESPONSE {TEXT, JSON};
	
	private METHOD method = METHOD.GET;
	
	private RESPONSE response = RESPONSE.TEXT;
	
	private String retCodeName = "retCode"; //返回码名称，默认为retCode
	
	private String contentType = Const.JSON_CONTENT_TYPE;
	
	private Map<String, String> reqParams = null;
	private Map<String, String> respParams = null;	
	private Map<String, Object> extraParams = null;	
	
	private HttpClient httpClient;
	private RequestConfig requestConfig;
	private PoolingHttpClientConnectionManager connectionManager;
	
	private int timeToLive;
    private int maxTotal;
    private int maxPerRoute;
    private int timeout;
	
	
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
    	this.url = JsonUtil.getAsStr(json, CONFIG_URL, null);
    	if(Utils.isStrEmpty(this.url)) {
    		LOG.error("There no {} config", CONFIG_URL);
    		return false;
    	}
    	this.retCodeName = JsonUtil.getAsStr(json, CONFIG_RETCODE_NAME, this.retCodeName);
    	
    	String s = JsonUtil.getAsStr(json, CONFIG_METHOD, "GET").toUpperCase();
    	try {
    		this.method = METHOD.valueOf(s);
    	} catch(IllegalArgumentException iae) {
    		LOG.error("Invalid {}, value={}, use GET as default", CONFIG_METHOD, s, iae);
    	}
    	
    	s = JsonUtil.getAsStr(json, CONFIG_RESPONSE, "TEXT").toUpperCase();
    	try {
    		this.response = RESPONSE.valueOf(s);
    	} catch(IllegalArgumentException iae) {
    		LOG.error("Invalid {}, value={}, use TEXT as default", CONFIG_RESPONSE, s, iae);
    		this.response = RESPONSE.TEXT;
    	}
    	
    	this.contentType = JsonUtil.getAsStr(json, CONFIG_CONTENTTYPE, this.contentType);
    	
		List<Object> list = JsonUtil.getAsList(json, CONFIG_REQPARAMS);
		if(list != null && list.size() > 0) {
			this.reqParams = parseSegmentMap(list);
		}
		
		list = JsonUtil.getAsList(json, CONFIG_RESPPARAMS);
		if(list != null && list.size() > 0) {
			this.respParams = parseSegmentMap(list);
		}
		
		this.extraParams = JsonUtil.getAsObject(json, CONFIG_EXTRA_PARAMETERS);
		
		this.timeToLive = JsonUtil.getAsInt(json, CONFIG_TIMETOLIVE, 100);
    	this.connectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);

    	int threadNum = WAFConfig.getThreadPoolSize();
    	this.maxTotal = JsonUtil.getAsInt(json, CONFIG_MAXTOTAL, threadNum + (threadNum >> 1));
		/**
		 * 设置每个服务器能打开的最大连接数，
		 * 如果并发线程大，适当增大连接数，可以很大的提升性能 
		 */
		this.maxPerRoute = JsonUtil.getAsInt(json, CONFIG_MAXPERROUTE, threadNum);
		if(maxPerRoute >= maxTotal) {
            LOG.error("{}({}) should be smaller than {}({}), value={}, use TEXT as default",
                    CONFIG_MAXPERROUTE, maxPerRoute, CONFIG_MAXTOTAL, maxTotal);
            return false;
		}
		connectionManager.setMaxTotal(maxTotal); //总连接数
		connectionManager.setDefaultMaxPerRoute(maxPerRoute); //每个连接服务端的最大连接数
    			
    	this.httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();  
    	
        this.timeout = JsonUtil.getAsInt(json, CONFIG_TIMEOUT, DEFAULT_TIMEOUT);
        return buildClient();
    }
    
    /**
     * api名称，在网关上注册的接口名称
     * @return
     */
    public String getUrl() {
    	return url;
    }
    
    /**
     * 返回码名称，默认为retCode
     * @return
     */
    public String getRetCodeName() {
    	return retCodeName;
    }
    
    public METHOD getMethod() {
    	return method;
    }
    
    /**
     * 响应类型
     * @return
     */
    public RESPONSE getResponse() {
    	return response;
    }

	/**
	 * 向第三方发起请求时的contentType
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * 每种接口一个client
	 * @return
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * 请求的配置，通常是超时时间
	 * @return
	 */
	public RequestConfig getRequestConfig() {
		return requestConfig;
	}
	
	private static Map<String, String> parseSegmentMap(List<Object> maps) {
		Map<String, String> segMaps = new HashMap<String, String>();
		String val;
		String k, v;
		int pos;
		
		for(Object o : maps) {
			val = Utils.parseString(o, "");
			pos = val.indexOf("->");
			if(pos > 0) {
				k = val.substring(0, pos).trim();
				v = val.substring(pos + 2).trim();
			} else {
				k = val;
				v = val;
			}
			segMaps.put(k, v);
		}
		
		return segMaps;
	}

	/**
	 * 附加的静态参数
	 * @return
	 */
	public Map<String, Object> getExtraParams() {
		return extraParams;
	} 
	
	/**
	 * 需要透传的请求参数
	 * @return
	 */
	public Map<String, String> getReqParams() {
		return reqParams;
	}

    /**
     * 需要透传的响应信息
     * @return
     */
	public Map<String, String> getRespParams() {
		return respParams;
	} 
	
    @Override
	protected boolean isCanBeNull() {
		return false;
	}
    
    
    public boolean buildClient() {
        try {
            if(connectionManager != null) {
                try {
                    connectionManager.shutdown();
                } catch(Exception e) {
                    LOG.error("Fail to shutdown connection manager", e);
                }
            }
            
            connectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);
            connectionManager.setMaxTotal(maxTotal); //总连接数
            connectionManager.setDefaultMaxPerRoute(maxPerRoute); //每个连接服务端的最大连接数
            this.httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connectionManager)
                    .build();  
            if(timeout > 0) {
                this.requestConfig = RequestConfig.custom()
                        .setSocketTimeout(timeout)
                        .setConnectTimeout(timeout) //设置连接与传输的超时时间
                        .setConnectionRequestTimeout(timeout)
                        .build();
            }
            return true;
        } catch(Exception e) {
            LOG.error("Fail to build client", e);
        }
        return false;
    } 
    
    @Override
	public void destroy() {
    	connectionManager.shutdown();
	}
    
	public static Map<String, Object> mapParameters(Map<String, String> maps, Map<String, Object> params) {
		if(maps == null || maps.size() <= 0) { //没有设置映射
			return params;
		}
		Map<String, Object> retParams = new HashMap<String, Object>();
		
		for(Map.Entry<String, String> o : maps.entrySet()) {
			retParams.put(o.getValue(), params.get(o.getKey()));
		}
		
		return retParams;
	}
	
	/**
	 * 设置默认的http请求超时时间
	 * @param timeout
	 */
	public static void setDefaultHttpTimeout(int timeout) {
	    DEFAULT_TIMEOUT = timeout;
	}
}
