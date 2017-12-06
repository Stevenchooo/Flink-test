/*
 * 文 件 名:  HttpRequester.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  默认信任证书的Http请求客户端
 * 修 改 人:  z00190465
 * 修改时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.common.remote.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 默认信任证书的Http请求客户端
 * 
 * @author z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class HttpRequester
{
    /**
     * HTTP正常响应状态
     */
    public static final int HTTP_OK = 200;
    
    /**
     * HTTP超时响应状态
     */
    public static final int HTTP_TIMEOUT = 408;
    
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequester.class);
    
    /**
     * POST方法
     */
    private static final String METHOD_POST = "POST";
    
    /**
     * GET方法
     */
    private static final String METHOD_GET = "GET";
    
    private static SSLSocketFactory ssf;
    
    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier()
    {
        public boolean verify(String s, SSLSession sslsession)
        {
            LOGGER.warn("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };
    
    /**
     * 忽视证书 Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager()
    {
        private X509Certificate[] certificates;
        
        @Override
        public void checkClientTrusted(X509Certificate[] certificates, String authType)
            throws CertificateException
        {
            if (this.certificates == null)
            {
                this.certificates = certificates;
            }
            
        }
        
        @Override
        public void checkServerTrusted(X509Certificate[] ax509certificate, String s)
            throws CertificateException
        {
            if (this.certificates == null)
            {
                this.certificates = ax509certificate;
            }
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            //替换 return null;
            return new X509Certificate[0];
        }
    };
    
    private String defaultContentEncoding = "UTF-8";
    
    static
    {
        /*
         * use ignore host name verifier
         */
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        
        // Prepare SSL Context
        try
        {
            TrustManager[] tm = {ignoreCertificationTrustManger};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            LOGGER.error("Fail to init ssl context", e);
        }
    }
    
    /**
     * 发送GET请求
     * 
     * @param urlString 目标地址url
     * @param properties head属性
     * @param parameters get携带的参数值
     * @throws IOException 异常
     * @return HttpResponse 得到的response响应对象
     */
    public HttpResponse get(String urlString, Map<String, String> properties, String parameters)
        throws IOException
    {
        StringBuilder sbUrl = new StringBuilder(urlString);
        if (parameters != null)
        {
            if (urlString.indexOf('?') > 0)
            {
                sbUrl.append('&').append(parameters);
            }
            else
            {
                sbUrl.append('?').append(parameters);
            }
        }
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("properties:" + (properties == null ? "" : properties));
            LOGGER.debug("url:" + sbUrl.toString() + ",method:GET");
            LOGGER.debug("parameters:" + (parameters == null ? "" : parameters));
        }
        
        URL url = new URL(sbUrl.toString());
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        
        if (sbUrl.indexOf("https://") >= 0)
        {
            ((HttpsURLConnection)urlConnection).setSSLSocketFactory(ssf);
        }
        
        urlConnection.setRequestMethod(METHOD_GET);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        
        if (properties != null)
        {
            //使用键值对迭代器可以加快效率
            for (Entry<String, String> keyValue : properties.entrySet())
            {
                urlConnection.addRequestProperty(keyValue.getKey(), keyValue.getValue());
            }
        }
        
        return this.makeContent(urlConnection);
    }
    
    /**
     * 发送POST请求
     * 
     * @param urlString 消息发送地址
     * @param properties 消息头键值对集合
     * @param parameters body体
     * @throws IOException 异常
     * @return HttpResponse HTTP返回消息
     */
    public HttpResponse post(String urlString, Map<String, String> properties, String parameters)
        throws IOException
    {
        if (LOGGER.isDebugEnabled())
        {
            //记录属性信息
            LOGGER.debug("properties:" + (properties == null ? "" : properties));
            LOGGER.debug("url:" + urlString + ",method:POST");
            LOGGER.debug("parameters:" + (parameters == null ? "" : parameters));
        }
        
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        
        //https请求需要设置ssl工厂
        if (urlString.indexOf("https://") >= 0)
        {
            ((HttpsURLConnection)urlConnection).setSSLSocketFactory(ssf);
        }
        
        urlConnection.setRequestMethod(METHOD_POST);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        
        if (properties != null)
        {
            //使用键值对迭代器可以加快效率
            for (Entry<String, String> keyValue : properties.entrySet())
            {
                urlConnection.addRequestProperty(keyValue.getKey(), keyValue.getValue());
            }
        }
        
        OutputStream out = null;
        try
        {
            //获取输出流
            out = urlConnection.getOutputStream();
            if (parameters != null)
            {
                out.write(parameters.getBytes(defaultContentEncoding));
                out.flush();
            }
        }
        catch (IOException e)
        {
            //记录错误信息
            LOGGER.error("write outStream error!", e);
            throw e;
        }
        finally
        {
            //关闭输出流
            OdpCommonUtils.close(out);
        }
        
        return this.makeContent(urlConnection);
    }
    
    /**
     * 得到响应对象
     */
    private HttpResponse makeContent(HttpURLConnection urlConnection)
        throws IOException
    {
        HttpResponse httpResponser = new HttpResponse();
        InputStream in;
        BufferedReader bufferedReader = null;
        StringBuilder temp = new StringBuilder();
        try
        {
            //获取输入流
            in = urlConnection.getInputStream();
            //实例化缓存reader
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();
            while (line != null)
            {
                temp.append(line).append('\n');
                line = bufferedReader.readLine();
            }
        }
        catch (IOException e)
        {
            //记录错误信息同时抛出异常
            LOGGER.error("read content failed! content is {}.", temp.toString(), e);
            throw e;
        }
        finally
        {
            //关闭缓存reader
            OdpCommonUtils.close(bufferedReader);
        }
        
        //获取内容编码
        String encoding = urlConnection.getContentEncoding();
        if (encoding == null)
        {
            encoding = this.defaultContentEncoding;
        }
        
        URL url = urlConnection.getURL();
        
        //实例化Http响应对象
        httpResponser.setDefaultPort(url.getDefaultPort());
        httpResponser.setFile(url.getFile());
        httpResponser.setHost(url.getHost());
        httpResponser.setPath(url.getPath());
        httpResponser.setPort(url.getPort());
        httpResponser.setProtocol(url.getProtocol());
        httpResponser.setQuery(url.getQuery());
        httpResponser.setRef(url.getRef());
        httpResponser.setUserInfo(url.getUserInfo());
        httpResponser.setContent(new String(temp.toString().getBytes(), encoding));
        httpResponser.setContentEncoding(encoding);
        httpResponser.setCode(urlConnection.getResponseCode());
        httpResponser.setMessage(urlConnection.getResponseMessage());
        httpResponser.setContentType(urlConnection.getContentType());
        httpResponser.setMethod(urlConnection.getRequestMethod());
        httpResponser.setConnectTimeout(urlConnection.getConnectTimeout());
        httpResponser.setReadTimeout(urlConnection.getReadTimeout());
        return httpResponser;
    }
    
    /**
     * 默认的响应字符集
     * @return 默认编码
     */
    public String getDefaultContentEncoding()
    {
        return this.defaultContentEncoding;
    }
    
    /**
     * 设置默认的响应字符集
     * @param defaultContentEncoding 默认编码值
     */
    public void setDefaultContentEncoding(String defaultContentEncoding)
    {
        this.defaultContentEncoding = defaultContentEncoding;
    }
    
}
