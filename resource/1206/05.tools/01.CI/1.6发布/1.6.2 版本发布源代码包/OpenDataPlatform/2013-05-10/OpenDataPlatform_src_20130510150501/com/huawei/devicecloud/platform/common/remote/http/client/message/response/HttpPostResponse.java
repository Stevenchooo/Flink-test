/*
 * 文 件 名:  HttpPostResponse.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-23
 */
package com.huawei.devicecloud.platform.common.remote.http.client.message.response;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * HTTP Post响应信息封装类
 * 
 * @author  q00107831
 * @version [Internet Business Service Platform SP V100R100, 2011-8-23]
 * @see  [相关类/方法]
 */
public class HttpPostResponse
{
    private static final int SMALL_SIZE = 10;
    
    /**Http响应返回码*/
    private int statuCode;
    
    /**处理结果，默认为成功*/
    private boolean operResult = true;
    
    /**
     * 操作结果描述信息
     */
    private String operResultDesc = "success";
    
    /**Http响应消息头参数*/
    private Map<String, String> respHeaderParams = new HashMap<String, String>(SMALL_SIZE);

    /**
     * 响应消息体参数
     */
    private Map<String, String> respBodyParams =  new HashMap<String, String>(SMALL_SIZE);

    /**
     * 响应消息体
     */
    private String respBody;
    
    /**
     * 添加响应消息体参数
     * @param key 参数名
     * @param value 参数值
     * @see [类、类#方法、类#成员]
     */
    public void addRespBodyParam(final String key, final String value)
    {
        respBodyParams.put(key, value);
    }
    
    /** 
     * 获取响应消息体参数
     * @param key 参数名
     * @return 参数值
     * @see [类、类#方法、类#成员]
     */
    public String getRespBodyParam(final String key)
    {
        return respBodyParams.get(key);
    }
    
    /**
     * 添加响应头参数
     * @param key 参数名
     * @param value 参数值
     * @see [类、类#方法、类#成员]
     */
    public void addRespHeaderParam(final String key, final String value)
    {
        this.respHeaderParams.put(key, value);
    }
    
    /**
     * 获取响应头参数
     * @param key 参数名
     * @return 参数值
     * @see [类、类#方法、类#成员]
     */
    public String getRespHeaderParam(final String key)
    {
        return this.respHeaderParams.get(key);
    }

    /** statuCode getter 方法
     * @return 返回状态码
     * @see [类、类#方法、类#成员]
     */
    public int getStatuCode()
    {
        return statuCode;
    }

    /** statuCode setter 方法
     * @param statuCode 返回状态码
     * @see [类、类#方法、类#成员]
     */
    public void setStatuCode(final int statuCode)
    {
        this.statuCode = statuCode;
    }

    /** operResult getter 方法
     * @return operating result
     * @see [类、类#方法、类#成员]
     */
    public boolean isOperationSuccess()
    {
        return operResult;
    }

    /** operResult setter 方法
     * @param operResult 操作结果
     * @see [类、类#方法、类#成员]
     */
    public void setOperResult(final boolean operResult)
    {
        this.operResult = operResult;
    }

    /** respHeaderParams getter 方法
     * @return 响应头参数
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getRespHeaderParams()
    {
        return respHeaderParams;
    }

    /** respHeaderParams setter 方法
     * @param respHeaderParams 响应头参数
     * @see [类、类#方法、类#成员]
     */
    public void setRespHeaderParams(Map<String, String> respHeaderParams)
    {
        this.respHeaderParams = respHeaderParams;
    }

    /** respBodyparams getter 方法
     * @return 响应消息体的参数
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getRespBodyParams()
    {
        return respBodyParams;
    }

    /** respBodyParams setter 方法
     * @param respBodyParams 响应消息体中的参数
     */
    public void setRespBodyParams(Map<String, String> respBodyParams)
    {
        this.respBodyParams = respBodyParams;
    }

    public String getRespBody()
    {
        return respBody;
    }

    public void setRespBody(String respBody)
    {
        this.respBody = respBody;
    }

    public String getOperResultDesc()
    {
        return operResultDesc;
    }

    public void setOperResultDesc(String operResultDesc)
    {
        this.operResultDesc = operResultDesc;
    }

    @Override
    public String toString()
    {
        return "HttpPostResponse [operResult=" + operResult + ", respBody=" + respBody + ", respBodyParams="
            + respBodyParams + ", respHeaderParams=" + respHeaderParams + ", statuCode=" + statuCode + ']';
    }
}
