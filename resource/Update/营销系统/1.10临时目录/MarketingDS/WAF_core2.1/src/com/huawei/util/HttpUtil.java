package com.huawei.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import com.huawei.waf.protocol.Const;

//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import com.huawei.tool.PerformanceTest.TestTask;
//import com.huawei.tool.PerformanceTest;

public class HttpUtil {
    public static final int HTTP_OK = 200;

    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 以post方式请求url，参数以键值对方式放入params中
     * @param url
     * @param params
     * @return
     */
    public static final HttpResponse post(String url, Map<String, Object> params, HttpClient httpClient, HttpClientContext context, RequestConfig requestConfig, String contentType) {  
        HttpPost httpPost = new HttpPost(url);  
        try {
            if(requestConfig != null) {
                httpPost.setConfig(requestConfig);
            }
            
            if(!Utils.isStrEmpty(contentType)) {
                httpPost.setHeader("Content-type", contentType);
            }
            httpPost.setHeader("Connection", "keep-alive");
            
            if(params != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
                for(String key : params.keySet()) {  
                    nvps.add(new BasicNameValuePair(key, JsonUtil.getAsStr(params, key, "")));  
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }
            
            return httpClient.execute(httpPost, context); 
        } catch (Exception e) {
            LogUtil.logUtilException(LOG, e, "Fail to post {}", url);
        } finally {
            if(httpPost != null) {
                httpPost.completed();
            }
        }
        
        return null;
    }

    public static final HttpResponse post(String url, Map<String, Object> params, HttpClient httpClient, RequestConfig requestConfig, String contentType) {
        return post(url, params, httpClient, null, requestConfig, contentType);
    }
    
    /**
     * 以post方式请求url，参数以键值对方式放入params中
     * @param url
     * @param params "a=1&b=2..." 或 {"a":1,"b":2}
     * @param contentType
     * @param timeout
     * @return
     */
    public static final HttpResponse post(String url, String params, HttpClient httpClient, HttpClientContext context, RequestConfig requestConfig, String contentType) {  
        HttpPost httpPost = new HttpPost(url);  
        
        try {
            if(requestConfig != null) {
                httpPost.setConfig(requestConfig);
            }
            httpPost.setEntity(new StringEntity(params, Const.DEFAULT_CHARSET));
            if(!Utils.isStrEmpty(contentType)) {
                httpPost.setHeader("Content-type", contentType);
            }
            httpPost.setHeader("Connection", "keep-alive");
            
            return httpClient.execute(httpPost, context);  
        } catch (Exception e) {
            LogUtil.logUtilException(LOG, e, "Fail to post {}", url);
        } finally {
            if(httpPost != null) {
                httpPost.completed();
            }
        }
        
        return null;
    }
    
    public static final HttpResponse post(String url, String params, HttpClient httpClient, RequestConfig requestConfig, String contentType) {
        return post(url, params, httpClient, null, requestConfig, contentType);
    }
    
    public static final HttpResponse post(String url, Map<String, Object> params, String contentType, int timeout) {
        HttpClientBuilder hb = HttpClientBuilder.create();
        HttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        HttpClient httpClient = hb.setConnectionManager(cm).setMaxConnPerRoute(4).build();
//        try {
            RequestConfig requestConfig = null;
            if(timeout > 0) {
                requestConfig = RequestConfig.custom()
                        .setSocketTimeout(timeout)
                        .setConnectTimeout(timeout) //设置请求和传输超时时间
                        .setConnectionRequestTimeout(timeout)
                        .build();
            }
            
            return post(url, params, httpClient, null, requestConfig, contentType);
//        } finally {
//            if(cm != null) {
//                cm.shutdown();
//            }
//        }
    }
            
    public static final HttpResponse post(String url, String params, String contentType, int timeout) {
        HttpClientBuilder hb = HttpClientBuilder.create();
        HttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        HttpClient httpClient = hb.setConnectionManager(cm).setMaxConnPerRoute(4).build();
        
//        try {
            RequestConfig requestConfig = null;
            if(timeout > 0) {
                requestConfig = RequestConfig.custom()
                        .setSocketTimeout(timeout)
                        .setConnectTimeout(timeout) //设置请求和传输超时时间
                        .setConnectionRequestTimeout(timeout)
                        .build();
            }
            return post(url, params, httpClient, null, requestConfig, contentType);
//        } finally {
//            if(cm != null) {
//                cm.shutdown();
//            }
//        }
    }
    
    public static final HttpResponse post(String url, String params, String contentType) {
        return post(url, params, contentType, 0);
    }
    
    public static final HttpResponse postJson(String url, String params) {
        return post(url, params, Const.JSON_CONTENT_TYPE, 0);
    }

    /**
     * 以get方法请求url，获得字符串返回
     * @param url
     * @return
     */
    public static final HttpResponse get(String url, int timeout) {
        return get(url, null, timeout);
    }
    
    public static final HttpResponse get(String url, Map<String, Object> params, int timeout) {
        HttpClientBuilder hb = HttpClientBuilder.create();
        HttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        HttpClient httpClient = hb.setConnectionManager(cm).setMaxConnPerRoute(4).build();
        
//        try {
            RequestConfig requestConfig = null;
            if(timeout > 0) {
                requestConfig = RequestConfig.custom()
                        .setSocketTimeout(timeout)
                        .setConnectTimeout(timeout) //设置请求和传输超时时间
                        .setConnectionRequestTimeout(timeout)
                        .build();
            }
            return get(url, params, httpClient, null, requestConfig);
//        } finally {
//            if(cm != null) {
//                cm.shutdown(); //不可以shutdown，否则返回的response无效
//            }
//        }
    }
    
    public static final HttpResponse get(String url, Map<String, Object> params, HttpClient httpClient, RequestConfig requestConfig) {
        return get(url, params, httpClient, null, requestConfig);
    }
    /**
     * get方法请求
     * @param ctx 会话，可以为空
     * @param url url
     * @param params 参数
     * @param timeout 超时时间
     * @return
     */
    public static final HttpResponse get(String url, Map<String, Object> params, HttpClient httpClient, HttpClientContext context, RequestConfig requestConfig) {
        HttpGet httpGet = new HttpGet(url); //必须放在参数处理之后，否则使用的url可能不是期望的url
        
        try {
            if(params != null) {
                StringBuilder sb = new StringBuilder(url);
                boolean first = true;
                if(url.indexOf('?') < 0) {
                    sb.append('?');
                } else {
                    first = url.indexOf('=') < 0;
                }
                
                for(String key : params.keySet()) {
                    if(!first) {
                        sb.append('&');
                    } else {
                        first = false;
                    }
                    sb.append(key)
                      .append('=')
                      .append(URLEncoder.encode(JsonUtil.getAsStr(params, key), Const.DEFAULT_ENCODING));
                }
                url = sb.toString();
            }
            
            if(requestConfig != null) {
                httpGet.setConfig(requestConfig);
            }
            
            return httpClient.execute(httpGet, context);  
        } catch (Exception e) {
            LogUtil.logUtilException(LOG, e, "Fail to get {}", url);
        } finally {
            if(httpGet != null) {
                httpGet.completed();
            }
        }
        
        return null;
    }
    
    public static final String getString(HttpResponse resp) {
        if(resp == null || resp.getStatusLine().getStatusCode() != HTTP_OK) {
            return null;
        }
        
        try {
            HttpEntity entity = resp.getEntity();
            //如果不设置charset，则使用响应contenttype中设置的字符集
            return EntityUtils.toString(entity, Const.DEFAULT_CHARSET);
        } catch (Exception e) {
            LogUtil.logUtilException(LOG, e, "Fail to get string from response");
        }
        return null;
    }
    
    public static final byte[] getBinary(HttpResponse resp) {
        if(resp == null || resp.getStatusLine().getStatusCode() != HTTP_OK) {
            return null;
        }
        
        try {
            HttpEntity entity = resp.getEntity();
            return EntityUtils.toByteArray(entity);
        } catch(Exception e) {
            LogUtil.logUtilException(LOG, e, "Fail to get binary from response");
        }
        
        return null;
    }
    
    /**
     * 将文件内容base64之后，同步给其他服务器，只适合处理小文件，
     * 与对端的SyncFile处理类配合使用
     * @param fileName
     * @param url
     * @return
     */
    public static final String syncFile(String fileName, String url) {
    	File f = new File(fileName);
    	if(!f.exists()) {
    		LOG.error("File {} not exists", fileName);
    		return null;
    	}
    	
    	FileInputStream fis = null;
    	byte[] buf = null;
    	int len = 0;
    	
		try {
			fis = new FileInputStream(f);
	    	len = fis.available();
	    	buf = new byte[len];
	    	len = fis.read(buf);
	    	if(len != buf.length) {
	    		LOG.error("Fail to read all content from file {}", fileName);
	    		return null;
	    	}
		} catch (Exception e) {
            LogUtil.logUtilException(LOG, e, "Fail to read content from {}", fileName);
			return null;
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
		            LogUtil.logUtilException(LOG, e, "Fail to close {}", fileName);
				}
			}
		}
		
    	String content = Utils.bin2base64(buf);
    	StringBuilder sb = new StringBuilder('{');
    	sb.append("\"name\"=\"").append(fileName).append("\",")
    	  .append("\"size\"=").append(len).append(',')
    	  .append("\"file\"=\"").append(content).append("\"}");
    	HttpResponse resp = postJson(url, sb.toString());
    	
    	return getString(resp);
    }
    
    public static final String addUrlParameter(String url, String name, Object val) {
    	if(val == null) {
    		val = "";
    	}
    	
		try {
	    	if(url.indexOf('?') >= 0) {
				return url + '&' + name + '=' + URLEncoder.encode(val.toString(), Const.DEFAULT_ENCODING);
	    	}
	    	return url + '?' + name + '=' + URLEncoder.encode(val.toString(), Const.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Fail to encode {}", val, e);
		}
		return url;
    }
//    public static void main(String[] args) throws Exception {
//        HttpClientBuilder hb = HttpClientBuilder.create();
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(100, TimeUnit.SECONDS);
//        cm.setMaxTotal(15); //总连接数
//        cm.setDefaultMaxPerRoute(3); //每个连接服务端的最大连接数
//        
//        HttpClient httpClient = hb.setConnectionManager(cm).build();
//        RequestConfig rc = RequestConfig.custom()
//                .setConnectTimeout(2000)
//                .setSocketTimeout(4000)
//                .setConnectionRequestTimeout(2000)
//                .build();
//        
//        for(int i = 0; i < 10000; i++) {
//            long start = System.currentTimeMillis();
//            HttpResponse resp = get("http://192.168.22.13:8580/WAF_core/api/testtimeout", null, httpClient, null, rc);
////                  HttpResponse resp = get("http://www.google.com", null, httpClient, null, rc);
//            if(resp != null) {
//                System.out.println(getString(resp));
//            } else {
//                cm.shutdown();
//                cm = new PoolingHttpClientConnectionManager(100, TimeUnit.SECONDS);
//                httpClient = hb.setConnectionManager(cm).build();
//            }
//            System.out.println(Integer.toString(i) + ":interval=" + (System.currentTimeMillis() - start));
//            Thread.sleep(1000);
//        }
////        HttpResponse resp = get("http://192.168.7.135", null, httpClient, null, rc);
//        System.exit(0);
//    }
    
//    public static void main(String[] args) throws Exception {
//        int timeout = 10 * 1000;
//        
//        final Map<String, Object> data = new java.util.HashMap<String, Object>();
//        data.put("slat", 39.91667);
//        data.put("slon", 116.41667);
//        
//        org.apache.http.impl.conn.PoolingHttpClientConnectionManager connectionManager = new
//                org.apache.http.impl.conn.PoolingHttpClientConnectionManager(100,
//                java.util.concurrent.TimeUnit.SECONDS);
//        connectionManager.setMaxTotal(100);
//        connectionManager.setDefaultMaxPerRoute(50);
//        
//        final HttpClient httpClient = HttpClientBuilder.create()
//                .setMaxConnPerRoute(4)
//                .setConnectionManager(connectionManager)
//                .build();  
//        
//        final RequestConfig requestConfig = RequestConfig.custom()
//                .setSocketTimeout(timeout + timeout)
//                .setConnectTimeout(timeout) //设置请求和传输超时时间
//                .build();
//        
//        TestTask task = new TestTask("httptest", 50, 2, null) {
//            @Override
//            public void test(Object runtimeData) {
//                org.apache.http.impl.conn.PoolingHttpClientConnectionManager connectionManager = new
//                        org.apache.http.impl.conn.PoolingHttpClientConnectionManager(100,
//                        java.util.concurrent.TimeUnit.SECONDS);
//                connectionManager.setMaxTotal(100);
//                connectionManager.setDefaultMaxPerRoute(50);
//                
////                ArrayList<Header> headers = new ArrayList<Header>();
////                headers.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0"));
////                headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8"));
////                headers.add(new BasicHeader("Cookie", "JSESSIONID="+Utils.genUUID_64()+"; CNZZDATA5957928=cnzz_eid%3D1937302742-1409205440-%26ntime%3D1409205440"));
//                
////                final HttpClient httpClient = HttpClientBuilder.create()
////                        .setMaxConnPerRoute(4)
////                        .setDefaultHeaders(headers)
////                        .setConnectionManager(connectionManager)
////                        .build();  
//                
////                HttpResponse resp = post("http://www.youngartist.cn/ext/artist/vote", "id=00000001900147d2f7ca3", httpClient, requestConfig, null);
////              HttpResponse resp = get("http://www.baidu.com", null, httpClient, requestConfig);
//              HttpResponse resp = get("http://www.baidu.com", null, 10 * 1000);
////                HttpResponse resp = get("http://localhost:8080/WebServer/api/testtimeout", null, 10 * 1000);
////                HttpResponse resp = get("http://192.168.22.13:8580/WAF_core/api/testtimeout", null, 10 * 1000);
//                String str = getString(resp);
//                System.out.println("resp:" + str);
//            }
//        };
//        
//        PerformanceTest.test(task);
//        System.exit(0);
//    }
}
