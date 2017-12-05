package com.huawei.wda.http;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;

/**
 * http调用请求
 * 
 * @author yWX289656
 * 
 */
public class HttpInvokerReq
{
    /**
     * 默认超时时间 60秒
     */
    public static final int DEFAULT_TIMEOUT = 60000;

    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 请求内容
     */
    private String content;

    /**
     * 名称-值列表
     */
    private List<NameValuePair> nameValues;

    /**
     * 文件类型
     */
    private ContentType contentType;

    /**
     * http方法
     */
    private HttpMethodType type = HttpMethodType.POST;

    /**
     * 超时时间
     */
    private int timeOut = DEFAULT_TIMEOUT;

    /**
     * 请求体类型
     */
    private RequestBodyType bodyType = null;

    /**
     * 是否需要信任所有
     */
    private boolean trustAny = false;

    /**
     * 需要上传的文件
     */
    private List<CustomFilePart> fileParts;

    /**
     * 构造函数
     */
    public HttpInvokerReq()
    {

    }

    /**
     * 构造函数
     * 
     * @param uri
     *            uri
     * @param contentType
     *            contentType
     * @param bodyType
     *            请求体类型
     * @param content
     *            content请求体内容
     */
    public HttpInvokerReq(String uri, ContentType contentType,
            RequestBodyType bodyType, String content)
    {
        this.uri = uri;
        this.contentType = contentType;
        this.bodyType = bodyType;
        this.content = content;
    }

    /**
     * 创建客户端
     * 
     * @return CloseableHttpClient 客户端
     */
    public CloseableHttpClient createClient()
    {

        CloseableHttpClient cilent = null;
        if (trustAny)
        {
            try
            {
                SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, new TrustAnyStrategy())
                        .build();

                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslContext, NoopHostnameVerifier.INSTANCE);
                cilent = HttpClients.custom().setSSLSocketFactory(sslsf)
                        .build();
            }
            catch (KeyManagementException e)
            {
                LOGGER.error("createClient has KeyManagementException");
            }
            catch (NoSuchAlgorithmException e)
            {
                LOGGER.error("createClient has NoSuchAlgorithmException");
            }
            catch (KeyStoreException e)
            {
                LOGGER.error("createClient has KeyStoreException");
            }
        }

        if (null == cilent)
        {
            cilent = HttpClientBuilder.create().build();
        }

        return cilent;

    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public ContentType getContentType()
    {
        return contentType;
    }

    public void setContentType(ContentType contentType)
    {
        this.contentType = contentType;
    }

    public HttpMethodType getType()
    {
        return type;
    }

    public void setType(HttpMethodType type)
    {
        this.type = type;
    }

    public int getTimeOut()
    {
        return timeOut;
    }

    public void setTimeOut(int timeOut)
    {
        this.timeOut = timeOut;
    }

    public RequestBodyType getBodyType()
    {
        return bodyType;
    }

    public void setBodyType(RequestBodyType bodyType)
    {
        this.bodyType = bodyType;
    }

    public String getMimeType()
    {
        return null == contentType ? null : contentType.getMimeType();
    }

    public List<NameValuePair> getNameValues()
    {
        return nameValues;
    }

    public void setNameValues(List<NameValuePair> nameValues)
    {
        this.nameValues = nameValues;
    }

    public boolean isTrustAny()
    {
        return trustAny;
    }

    public void setTrustAny(boolean trustAny)
    {
        this.trustAny = trustAny;
    }

    /**
     * 增加name=value键值对
     * 
     * @param name
     *            名称
     * @param value
     *            值
     */
    public void addNameValue(String name, Object value)
    {
        if (null == nameValues)
        {
            nameValues = new ArrayList<NameValuePair>();
        }
        String valueStr = (null == value) ? "" : value.toString();
        NameValuePair pair = new BasicNameValuePair(name, valueStr);
        nameValues.add(pair);
    }

    /**
     * 增加文件
     * 
     * @param name
     *            字段名
     * @param file
     *            文件
     * @param fileName
     *            文件名
     */
    public void addFilePart(String name, File file, String fileName)
    {
        if (null == fileParts)
        {
            fileParts = new ArrayList<CustomFilePart>();
        }
        fileParts.add(new CustomFilePart(name, file, fileName));
    }

    public List<CustomFilePart> getFileParts()
    {
        return fileParts;
    }

    public void setFileParts(List<CustomFilePart> fileParts)
    {
        this.fileParts = fileParts;
    }

    /**
     * 获取该实例的表示
     * 
     * @return String 该实例的表示
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpInvokerReq [uri=");
        builder.append(uri);
        builder.append(", content=");
        builder.append(content);
        builder.append(", contentType=");
        builder.append(contentType);
        builder.append(", type=");
        builder.append(type);
        builder.append(", timeOut=");
        builder.append(timeOut);
        builder.append(", bodyType=");
        builder.append(bodyType);
        builder.append(']');
        return builder.toString();
    }

    /**
     * 创建请求
     * 
     * @return 请求消息
     * @throws IOException
     *             IOException
     */
    public HttpRequestBase createHttpRequest() throws IOException
    {
        HttpRequestBase httpRequest = null;
        try
        {
            httpRequest = type.getHttpRequest(uri);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.warn("createHttpRequest falied, IllegalArgumentException");
        }

        if (null == httpRequest)
        {
            throw new IOException("httpRequest is null");
        }
        if (null != contentType)
        {
            httpRequest.addHeader("Content-Type",
                    contentType.getContentTypeHeader());
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
        httpRequest.setConfig(requestConfig);

        if (null != bodyType)
        {
            bodyType.combine(httpRequest, this);
        }
        return httpRequest;
    }

    /**
     * 信任所有
     * 
     * @author yWX289656
     * 
     */
    public static class TrustAnyStrategy implements TrustStrategy
    {

        /**
         * 是否信任
         * 
         * @param arg0
         *            arg0
         * @param arg1
         *            arg1
         * @throws CertificateException
         *             CertificateException
         * @return boolean 是否信任
         */
        @Override
        public boolean isTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException
        {
            return true;
        }

    }
}
