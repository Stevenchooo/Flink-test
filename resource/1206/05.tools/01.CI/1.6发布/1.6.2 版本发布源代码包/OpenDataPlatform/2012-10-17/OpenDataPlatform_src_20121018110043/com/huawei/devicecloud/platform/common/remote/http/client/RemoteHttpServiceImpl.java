/*
 * 文 件 名:  RemoteHttpServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <执行远程HTTP服务访问的公共服务接口>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-23
 */
package com.huawei.devicecloud.platform.common.remote.http.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import com.huawei.devicecloud.platform.common.remote.http.client.message.request.HttpPostRequest;
import com.huawei.devicecloud.platform.common.remote.http.client.message.response.HttpPostResponse;

/**
 * 
 * 执行远程HTTP服务访问的公共服务接口
 * 
 * @author  q00107831
 * @version [Internet Business Service Platform SP V100R100, 2011-8-23]
 * @see  [相关类/方法]
 */
public class RemoteHttpServiceImpl
{
    /**
     * 每秒的毫秒数
     */
    private static final int MILLS_PER_SECOND = 1000;
    
    //公用HTTP客户端
    private HttpClient client;
    
    //响应等待超时
    private int soTimeOut;
    
    //连接超时
    private int connTimeOut;
    
    //单主机最大连接数
    private int maxConnPerHost;
    
    //全局最大连接数
    private int maxTotalConnCount;
    
    /**
     * 初始化远程调用http服务
     */
    public void initRemoteHttpService()
    {
        //连接管理器
        HttpConnectionManager httpConManager = new MultiThreadedHttpConnectionManager();
        
        HttpConnectionManagerParams params = httpConManager.getParams();
        
        //如下两个参数配置单位：秒
        params.setSoTimeout(soTimeOut * MILLS_PER_SECOND);
        
        params.setConnectionTimeout(connTimeOut * MILLS_PER_SECOND);
        
        params.setDefaultMaxConnectionsPerHost(maxConnPerHost);
        
        params.setMaxTotalConnections(maxTotalConnCount);
        
        client = new HttpClient(httpConManager);
        
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        
        //需要手动设置本地IP时才使用
        //        client.getHostConfiguration().setLocalAddress(null);
    }
    
    /**
     * 
     * 根据制定的请求对象，发送Http Post消息
     * @param postReq 请求
     * @return HttpPostResponse http返回信息
     */
    public HttpPostResponse sendHttpPostMessage(HttpPostRequest postReq)
    {
        HttpPostResponse postResp = new HttpPostResponse();
        
        PostMethod postMethod = null;
        
        BufferedReader reader = null;
        
        try
        {
            //添加URL参数
            buildRequestURL(postReq);
            
            String postTargetURL = postReq.getTargetURL();
            
            postMethod = new PostMethod(postTargetURL);
            
            postMethod.setRequestHeader("Content-Type", postReq.getContentType());
            
            //存储所有请求消息头参数
            storeRequestHeaderParams(postMethod, postReq);
            
            //如果单独指定的请求体为空，则不设置到请求消息
            if (StringUtils.isNotBlank(postReq.getRequestBody()))
            {
                //设置请求消息体
                StringRequestEntity reqEntity =
                    new StringRequestEntity(postReq.getRequestBody(), postReq.getContentType(),
                        postReq.getContentCharset());
                
                postMethod.setRequestEntity(reqEntity);
            }
            
            //添加请求参数
            addRequestParameter(postMethod, postReq);
            
            //发送请求
            int statusCode = client.executeMethod(postMethod);
            
            postResp.setStatuCode(statusCode);
            
            //服务器返回失败
            if (HttpServletResponse.SC_OK != statusCode)
            {
                postResp.setOperResult(false);
            }
            
            Header[] respHeaders = postMethod.getResponseHeaders();
            
            if ((null != respHeaders) && (respHeaders.length > 0))
            {
                //添加响应返回参数信息
                for (Header header : respHeaders)
                {
                    postResp.addRespHeaderParam(header.getName(), header.getValue());
                }
            }
            
            //读取响应消息体信息
            StringBuffer respStrBuffer = new StringBuffer();
            
            reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), "UTF-8"));
            
            String temStr = null;
            
            while (true)
            {
                temStr = reader.readLine();
                //流读取完毕则退出循环
                if (null == temStr)
                {
                    break;
                }
                respStrBuffer.append(temStr);
            }
            
            //添加响应参数
            addRespBodyParam(postResp, respStrBuffer.toString());
        }
        catch (Exception e)
        {
            postResp.setOperResult(false);
            postResp.setOperResultDesc("Exception occured with " + e.toString());
        }
        finally
        {
            //释放连接
            if (null != postMethod)
            {
                postMethod.releaseConnection();
            }
            
            IOUtils.closeQuietly(reader);
        }
        
        return postResp;
        
    }
    
    /**
     * 添加响应消息体参数
     * @param response 响应
     * @param paramStr 返回参数字符串
     * @see [类、类#方法、类#成员]
     */
    private void addRespBodyParam(HttpPostResponse response, String paramStr)
    {
        if (StringUtils.isBlank(paramStr))
        {
            return;
        }
        
        response.setRespBody(paramStr);
        
        StrTokenizer paramToken = new StrTokenizer(paramStr, "&");
        
        StrTokenizer singleToken = null;
        
        String[] params = paramToken.getTokenArray();
        
        String[] keyValue = null;
        
        if (null != params)
        {
            for (String str : params)
            {
                if (StringUtils.isNotBlank(paramStr))
                {
                    singleToken = new StrTokenizer(str, "=");
                    keyValue = singleToken.getTokenArray();
                    if ((null != keyValue) && keyValue.length > 1)
                    {
                        response.addRespBodyParam(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
    }
    
    /**
     * 添加请求头参数
     * @param method 执行的方法对象
     * @param request 请求消息
     */
    private void storeRequestHeaderParams(PostMethod method, HttpPostRequest request)
    {
        if (null == request)
        {
            return;
        }
        
        if ((null != request.getHeaderParams()) && (!request.getHeaderParams().isEmpty()))
        {
            for (Entry<String, String> keyvalue : request.getHeaderParams().entrySet())
            {
                method.addRequestHeader(keyvalue.getKey(), keyvalue.getValue());
            }
        }
    }
    
    /**
     * 根据请求URL及参数列表构造带参数的请求路径
     * @param request 请求
     */
    private void buildRequestURL(HttpPostRequest request)
    {
        if (null == request)
        {
            return;
        }
        
        if ((null != request.getUrlParams()) && (!request.getUrlParams().isEmpty()))
        {
            StringBuffer sb = new StringBuffer();
            sb.append(request.getTargetURL()).append('?');
            
            for (Entry<String, String> keyValue : request.getUrlParams().entrySet())
            {
                sb.append(keyValue.getKey()).append('=').append(keyValue.getValue());
                sb.append('&');
            }
            
            //删除?或者&号
            sb.deleteCharAt(sb.length() - 1);
            
            request.setTargetURL(sb.toString());
        }
    }
    
    /**
     * 添加请求参数
     * @param method 请求方法
     * @param request 请求信息
     */
    private void addRequestParameter(PostMethod method, HttpPostRequest request)
    {
        if ((null == request.getBodyParams()) || (request.getBodyParams().isEmpty()))
        {
            return;
        }
        
        Set<Entry<String, String>> paramSet = request.getBodyParams().entrySet();
        
        for (Entry<String, String> keyValue : paramSet)
        {
            //键和值均不能为null
            if (null != keyValue.getKey() && null != keyValue.getValue())
            {
                method.setParameter(keyValue.getKey(), keyValue.getValue());
            }
        }
    }
    
    public int getSoTimeOut()
    {
        return soTimeOut;
    }
    
    public void setSoTimeOut(int soTimeOut)
    {
        this.soTimeOut = soTimeOut;
    }
    
    public int getConnTimeOut()
    {
        return connTimeOut;
    }
    
    public void setConnTimeOut(int connTimeOut)
    {
        this.connTimeOut = connTimeOut;
    }
    
    public int getMaxConnPerHost()
    {
        return maxConnPerHost;
    }
    
    public void setMaxConnPerHost(int maxConnPerHost)
    {
        this.maxConnPerHost = maxConnPerHost;
    }
    
    public int getMaxTotalConnCount()
    {
        return maxTotalConnCount;
    }
    
    public void setMaxTotalConnCount(int maxTotalConnCount)
    {
        this.maxTotalConnCount = maxTotalConnCount;
    }
    
}
