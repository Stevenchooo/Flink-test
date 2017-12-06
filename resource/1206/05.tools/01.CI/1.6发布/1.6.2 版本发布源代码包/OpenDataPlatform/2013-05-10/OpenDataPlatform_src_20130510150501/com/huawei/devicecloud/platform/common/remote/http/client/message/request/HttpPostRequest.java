/*
 * 文 件 名:  HttpPostRequest.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-23
 */
package com.huawei.devicecloud.platform.common.remote.http.client.message.request;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * HTTP Post请求消息封装类
 * 
 * @author  q00107831
 * @version [Internet Business Service Platform SP V100R100, 2011-8-23]
 */
public class HttpPostRequest
{
    private static final int SMALL_SIZE = 10;
    
    /**
     * 内容类型
     */
    private String contentType;
    
    /**
     * 字符编码
     */
    private String contentCharset = "UTF-8";
    
    /**请求地址*/
    private String targetURL;
    
    /**请求参数*/
    private Map<String, String> urlParams = new HashMap<String, String>(SMALL_SIZE);
    
    /**请求头参数*/
    private Map<String, String> headerParams = new HashMap<String, String>(SMALL_SIZE);

    /**
     * 消息体参数
     */
    private Map<String, String> bodyParams = new HashMap<String,String>(SMALL_SIZE);
    
    /**请求消息体*/
    private String requestBody;
    
    /**
     * 添加请求体参数
     * @param key 参数名
     * @param value 参数值
     * @see [类、类#方法、类#成员]
     */
    public void addRequestBodyParam(final String key, final String value)
    {
        bodyParams.put(key, value);
    }
    
    /**
     * 添加新的URL附加参数
     * @param key 参数名称
     * @param value 参数取值
     * @see [类、类#方法、类#成员]
     */
    public void addURLParam(final String key, final String value)
    {
        this.urlParams.put(key, value);
    }
    
    /**
     * 添加新的消息头参数
     * @param key 参数名
     * @param value 参数值
     * @see [类、类#方法、类#成员]
     */
    public void addHeaderParam(final String key, final String value)
    {
        this.headerParams.put(key, value);
    }
    
    /** targetURL getter 方法
     * @return target URL
     * @see [类、类#方法、类#成员]
     */
    public String getTargetURL()
    {
        return targetURL;
    }

    /**
     * targetURL setter 方法
     * @param targetURL targetURL
     * @see [类、类#方法、类#成员]
     */
    public void setTargetURL(final String targetURL)
    {
        this.targetURL = targetURL;
    }

    /** requestBody getter 方法
     * @return 请求消息体
     * @see [类、类#方法、类#成员]
     */
    public String getRequestBody()
    {
        return requestBody;
    }

    /**
     * request body setter 方法
     * @param requestBody 请求消息体
     * @see [类、类#方法、类#成员]
     */
    public void setRequestBody(final String requestBody)
    {
        this.requestBody = requestBody;
    }
    
    /**urlParams getter 方法
     * @return url params
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getUrlParams()
    {
        return urlParams;
    }

    /** urlParams setter 方法
     * @param urlParams url中的参数
     * @see [类、类#方法、类#成员]
     */
    public void setUrlParams(Map<String, String> urlParams)
    {
        this.urlParams = urlParams;
    }

    /** headerParams getter 方法
     * @return header parameters
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getHeaderParams()
    {
        return headerParams;
    }

    /** headerParams setter 方法
     * @param headerParams 在请求头中的参数
     * @see [类、类#方法、类#成员]
     */
    public void setHeaderParams(Map<String, String> headerParams)
    {
        this.headerParams = headerParams;
    }

    /** bodyParams getter 方法
     * @return 在返回体重的参数
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getBodyParams()
    {
        return bodyParams;
    }

    /** body params setter 方法
     * @param bodyParams 消息体中的参数
     * @see [类、类#方法、类#成员]
     */
    public void setBodyParams(Map<String, String> bodyParams)
    {
        this.bodyParams = bodyParams;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getContentCharset()
    {
        return contentCharset;
    }

    public void setContentCharset(String contentCharset)
    {
        this.contentCharset = contentCharset;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpPostRequest [bodyParams=");
        builder.append(bodyParams);
        builder.append(", headerParams=");
        builder.append(headerParams);
        builder.append(", requestBody=");
        builder.append(requestBody);
        builder.append(", targetURL=");
        builder.append(targetURL);
        builder.append(", urlParams=");
        builder.append(urlParams);
        builder.append("]");
        return builder.toString();
    }
    
}
