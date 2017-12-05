package com.huawei.tool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;

import com.huawei.util.HttpUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.protocol.Const;
import com.huawei.waf.protocol.RetCode;

public class FeatureTest {
    private static final Logger LOG = LogUtil.getInstance();
    
	private final static String PARAMETERS = "parameters";
	
	private final static String HOST = "host";
    private final static String TIMEOUT = "timeout";
	
	private final static String UC_LIST = "usecases";
	
    private final static String UC_NAME = "name";
	private final static String UC_MESSAGE = "message";
	private final static String UC_URL = "url";
    private final static String UC_METHOD = "method";
    private final static String UC_CONTENT_TYPE = "contentType";
    private final static String UC_SUCCESS = "success";
    private final static String UC_REPEAT = "repeat";
	
    private final static String METHOD_GET = "GET";
    private final static String METHOD_POST = "POST";
    
    private final static String CONTENT_TYPE_JSON = "JSON";
    private final static String CONTENT_TYPE_REST = "REST";
	
    private int timeout = 10 * 1000;
	private String host = "http://localhost:8080/WebServer";
	
	private Map<String, Object> runtimeData = null;
	private HttpClientContext httpContext = null;
	private HttpClient httpClient;
	private RequestConfig requestConfig = null;
	
    public static void main(String[] args) {
        FeatureTest tester = new FeatureTest();
        tester.startTest(args);
        System.exit(0);
    }
    
	public void startTest(String[] args) {
		String usecaseFile;
		if(args.length > 0) {
			usecaseFile = args[0];
		} else {
			String sp = System.getProperty("file.separator");
			usecaseFile = System.getProperty("user.dir") + sp + "usecase" + sp + "test.cfg";
		}
		
		File ucf = new File(usecaseFile);
		if(!ucf.exists()) {
		    LOG.error("File not exists:{}", usecaseFile);
			System.exit(0);
		}
		
		LOG.info("Start execute use cases in:{}", usecaseFile);
		Map<String, Object> cfg = JsonUtil.jsonFileToMap(ucf);
		if(cfg == null) {
		    LOG.error("Fail to parse config file:{}", usecaseFile);
            System.exit(0);
		}
		
		timeout = JsonUtil.getAsInt(cfg, TIMEOUT, 10 * 1000);
		host = JsonUtil.getAsStr(cfg, HOST, null);
		if(Utils.isStrEmpty(host)) {
            LOG.error("Invalid {} config item", HOST);
            System.exit(0);
		}
		
		runtimeData = JsonUtil.getAsObject(cfg, PARAMETERS);
		if(runtimeData == null) {
		    runtimeData = new HashMap<String, Object>();
		}
		
        PoolingHttpClientConnectionManager connectionManager
         = new  PoolingHttpClientConnectionManager(65, TimeUnit.SECONDS);
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(15); 
          
        httpClient = HttpClientBuilder.create()
            .setMaxConnPerRoute(4)
            .setConnectionManager(connectionManager)
            .build();  
        if(timeout > 0) {
            requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout + timeout)
                .setConnectTimeout(timeout) //设置请求和传输超时时间
                .build();
        }
		
		httpContext = HttpClientContext.create();
		httpContext.setCookieStore(new BasicCookieStore());
		List<Object> ucList = JsonUtil.getAsList(cfg, UC_LIST);
		if(ucList != null) {
			execute(ucList);
		}
		System.exit(0);
	}
	
    private void useRuntimeParameters(Map<String, Object> msg) {
        String val;
        Object o;
        int len;
        
        for(Map.Entry<String, Object> one : msg.entrySet()) {
            o = one.getValue();
            if(o instanceof String) {
                val = (String)o;
                len = val.length();
                if(val.indexOf('{') == 0 && val.lastIndexOf('}') == len - 1) {
                    o = runtimeData.get(val.substring(1, len - 1));
                    msg.put(one.getKey(), o);
                }
            }
        }
    }
	
    private String useRuntimeParameters(String url) {
        int start = 0, end = 0;
        String v;
        
        while((start = url.indexOf('{', start)) > 0) {
            start++;
            if((end = url.indexOf('}', start)) < 0) {
                return url;
            }
            v = url.substring(start, end);
            end++;
            if(runtimeData.containsKey(v)) {
                url = url.substring(0, start - 1) + JsonUtil.getAsStr(runtimeData, v, "") + url.substring(end);
            }
            start = end;
        }
        return url;
    }
    
	private void execute(List<Object> arr) {
		int i, size = arr.size();
		
		for(i = 0; i < size; i++) {
			executeOne(JsonUtil.getAsObject(arr, i));
		}
	}
	
	/**
	 * 用于生成批量处理的序号
	 * @param v
	 * @param no
	 * @return
	 */
	private Object[] formatNo(String v, int no) {
	    int num = 0;
	    
	    //计算%的个数，所以原始值中不可以放不参与格式化的%
	    for(int i = v.length() - 1; i >= 0; i--) {
	        if(v.charAt(i) == '%') {
	            num++;
	        }
	    }
	    Object[] oo = new Object[num];
	    for(int i = 0; i < num; i++) {
	        oo[i] = no;
	    }
	    return oo;
	}

	/**
	 * 获取运行时参数，此参数包括了前面接口运行的返回信息
	 * @return
	 */
	protected Map<String, Object> getRuntimeData() {
	    return runtimeData;
	}
	
	/**
	 * 执行用例前改变参数，用于不同的系统重载
	 * @param ucConfig  用例配置信息
	 * @param parameters 用例参数，运行参数已设置进入参数
	 * @return 改变后的参数
	 */
	protected Map<String, Object> changeParameters(Map<String, Object> ucConfig, Map<String, Object> parameters) {
	    return parameters;
	}
	
	private void executeOne(Map<String, Object> uc) {
	    LOG.info("================================================");
        String name = JsonUtil.getAsStr(uc, UC_NAME, "Unknown");
	    String url = JsonUtil.getAsStr(uc, UC_URL);
		if(Utils.isStrEmpty(url)) {
	  	    LOG.warn("No url to exeucte in {}", name);
	  	    return;
		}
		url = useRuntimeParameters(url);
        Map<String, Object> msg = JsonUtil.getAsObject(uc, UC_MESSAGE);
        if(msg == null) {
            msg = new HashMap<String, Object>();
        }
        Map<String, Object> success = JsonUtil.getAsObject(uc, UC_SUCCESS);
        int repeat = JsonUtil.getAsInt(uc, UC_REPEAT, 1);
		String contentType = JsonUtil.getAsStr(uc, UC_CONTENT_TYPE, CONTENT_TYPE_REST).toUpperCase();
        String method = JsonUtil.getAsStr(uc, UC_METHOD, METHOD_POST).toUpperCase();
		LOG.info("===REQUEST {}.{}.{}.{}:{}", name, method, contentType, repeat, url);
  	    useRuntimeParameters(msg);
  	    msg = changeParameters(uc, msg);
        HttpResponse resp = null;
  	    
        String str;
        Object[] noList;
        long t, t1 = System.currentTimeMillis();
  	    for(int i = 0; i < repeat; i++) {
      	    if(contentType.equals(CONTENT_TYPE_JSON)) {
                if(method.equals(METHOD_GET)) {
                    LOG.warn("Invalid get method when use json data");
                } else /*if(method.equals(METHOD_POST))*/ {
                    str = JsonUtil.mapToJson(msg);
                    noList = formatNo(str, i);
                    if(noList.length > 0) {
                        str = String.format(str, noList);
                    }
                    LOG.info("===PARAMETERS:{}", str);
                    resp = HttpUtil.post(host + url, str, httpClient, httpContext, requestConfig, Const.JSON_CONTENT_TYPE);
                }
      	    } else /*if(contentType.equals(CONTENT_TYPE_REST)) */ {
                Map<String, Object> params = new HashMap<String, Object>();
                Object o;
                
                for(Map.Entry<String, Object> one : msg.entrySet()) {
                    o = one.getValue();
                    if(o == null) {
                        continue;
                    }
                    str = o.toString();
                    noList = formatNo(str, i);
                    if(noList.length > 0) {
                        str = String.format(str, noList);
                    }
                    params.put(one.getKey(), str);
                }
                LOG.info("===PARAMETERS:{}", params.toString());
                
                if(method.equals(METHOD_GET)) {
                    resp = HttpUtil.get(host + url, params, httpClient, httpContext, requestConfig);
                } else /*if(method.equals(METHOD_POST))*/ {
                    resp = HttpUtil.post(host + url, params, httpClient, httpContext, requestConfig, Const.REST_CONTENT_TYPE);
                }
      	    }
      	    int status = resp == null ? -1 : resp.getStatusLine().getStatusCode();
      	    if(status != HttpStatus.SC_OK) {
                LOG.error("Fail to get response, status {}", status);
                return;
      	    }
      	    
      	    if(i + 1 == repeat) { //只记录最后一个
                String jsonStr = HttpUtil.getString(resp);
                Map<String, Object> respJson = JsonUtil.jsonToMap(jsonStr);
                if(respJson == null) {
                    LOG.error("Fail to parse json from response, jsonStr:{}", jsonStr);
                    return;
                }
                
                int retCode = JsonUtil.getAsInt(respJson, Const.RESULT);
                if(retCode == RetCode.OK) {
                    LOG.info("===RESPONSE:{}", jsonStr);
                } else {
                    LOG.error("===RESPONSE:{}", jsonStr);
                }
                
          	    if(success != null) {
          	        Object s, v;
          	        String key;
          	        
          	        for(Map.Entry<String, Object> o : success.entrySet()) {
          	            key = o.getKey();
          	            s = o.getValue();
          	            v = respJson.get(key);
          	            if(v == null || !v.equals(s)) {
          	                LOG.error("ERROR:{} => {}, which expect {}", key, v, s);
          	            }
          	        }
          	    }
          	    
                for(Cookie c : httpContext.getCookieStore().getCookies()) {
                    runtimeData.put(c.getName(), c.getValue());
                }
                runtimeData.putAll(respJson);
      	    }
  	    }
  	    t = System.currentTimeMillis() - t1;
  	    if(t < 1) {
  	        t = 1;
  	    }
  	    LOG.info("PERFORMANCE: {} milliseconds, speed {}", t, (((long)repeat) * 1000 / t));
	}
}
