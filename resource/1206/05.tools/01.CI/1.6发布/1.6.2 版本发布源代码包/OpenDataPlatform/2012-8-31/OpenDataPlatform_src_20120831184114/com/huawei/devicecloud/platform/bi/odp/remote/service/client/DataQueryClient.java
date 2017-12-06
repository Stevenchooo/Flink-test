/*
 * 文 件 名:  DataQueryClient.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  数据查询客户端类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.remote.service.client;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.remote.message.req.GroupsPrepareResultReq;
import com.huawei.devicecloud.platform.bi.odp.remote.message.req.UserQueryResultReq;
import com.huawei.devicecloud.platform.bi.odp.remote.message.rsp.GroupsPrepareResultRsp;
import com.huawei.devicecloud.platform.bi.odp.remote.message.rsp.UserQueryResultRsp;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;
import com.huawei.devicecloud.platform.common.remote.http.client.HttpRequester;
import com.huawei.devicecloud.platform.common.remote.http.client.HttpResponse;
import com.huawei.devicecloud.platform.common.remote.http.client.RemoteHttpServiceImpl;

/**
 * 数据查询客户端类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class DataQueryClient
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataQueryClient.class);
    
    /**
     * 执行远程HTTP服务访问的公共服务接口
     */
    private RemoteHttpServiceImpl remoteHttpService;
    
    /**
     * 查询记录总数相应接口
     * @param callBackUrl 回调接口地址
     * @param req 请求体
     * @return 响应
     * @throws CException 异常
     */
    public UserQueryResultRsp queryDataCountResp(final String callBackUrl, final UserQueryResultReq req)
        throws CException
    {
        //转化为JSON字符串
        final String body = JSON.toJSONString(req);
        
        //远程调用接口
        if (StringUtils.isBlank(callBackUrl))
        {
            LOGGER.error("callBackUrl is blank! req is {}", req);
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        
        final HttpRequester httpreq = new HttpRequester();
        
        //HttpPostRequest httpreq = buildHttpPostReq(body, callBackUrl);
        LOGGER.info("call queryDataCountResp start, request is {}", body);
        //HttpPostResponse httprsp = remoteHttpService.sendHttpPostMessage(httpreq);
        HttpResponse httprsp = null;
        try
        {
            //通过post方法调用回调url地址
            httprsp = httpreq.post(callBackUrl, null, body);
            
            LOGGER.info("call queryDataCountResp end, response is {}", httprsp);
            if (null != httprsp && HttpRequester.HTTP_OK == httprsp.getCode())
            {
                //获取HTTP响应消息
                final String rspBody = httprsp.getContent();
                final UserQueryResultRsp rsp = OdpCommonUtils.parseObject(rspBody, UserQueryResultRsp.class);
                //返回码为0表示正常响应
                if (null != rsp && 0 == rsp.getResult_code())
                {
                    return rsp;
                }
                else
                {
                    LOGGER.error("call queryDataCountResp failed! httprsp is {}", httprsp);
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
            else
            {
                //HTTP响应异常
                LOGGER.error("not correct responce for called queryDataCountResp! request is {},response is {}",
                    new Object[] {req, httprsp});
                throw new CException(ResultCode.SYSTEM_ERROR);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("call httpreq.post failed! request is {},response is {}", new Object[] {req, httprsp, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询记录总数相应接口
     * @param callBackUrl 回调地址
     * @param req 请求体
     * @return 响应
     * @throws CException 异常
     */
    public GroupsPrepareResultRsp reserveBatchDataResp(final String callBackUrl, final GroupsPrepareResultReq req)
        throws CException
    {
        //转化为JSON字符串
        final String body = JSON.toJSONString(req);
        
        //远程调用接口
        if (StringUtils.isBlank(callBackUrl))
        {
            LOGGER.error("callBackUrl is blank! req is {}", req);
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        
        final HttpRequester httpreq = new HttpRequester();
        
        //HttpPostRequest httpreq = buildHttpPostReq(body, callBackUrl);
        LOGGER.info("call reserveBatchDataResp start, request is {}", body);
        //HttpPostResponse httprsp = remoteHttpService.sendHttpPostMessage(httpreq);
        HttpResponse httprsp = null;
        try
        {
            //通过post方法调用回调url地址
            httprsp = httpreq.post(callBackUrl, null, body);
            
            LOGGER.info("call reserveBatchDataResp end, response is {}", httprsp);
            if (null != httprsp && HttpRequester.HTTP_OK == httprsp.getCode())
            {
                //获取http响应消息
                final String rspBody = httprsp.getContent();
                final GroupsPrepareResultRsp rsp = OdpCommonUtils.parseObject(rspBody, GroupsPrepareResultRsp.class);
                //返回码为0表示正常响应
                if (null != rsp && 0 == rsp.getResult_code())
                {
                    return rsp;
                }
                else
                {
                    //记录错误抛出异常
                    LOGGER.error("call reserveBatchDataResp failed! httprsp is {}", httprsp);
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
            else
            {
                //HTTP响应异常
                LOGGER.error("not correct responce for called reserveBatchDataResp! request is {},response is {}",
                    new Object[] {req, httprsp});
                throw new CException(ResultCode.SYSTEM_ERROR);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("call httpreq.post failed! request is {},response is {}", new Object[] {req, httprsp, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    //    private HttpPostRequest buildHttpPostReq(String reqBody, String url)
    //    {
    //        HttpPostRequest httpreq = new HttpPostRequest();
    //        httpreq.setContentType(RemoteHttpConstant.CONTENT_TYPE_JSON);
    //        httpreq.setContentCharset(RemoteHttpConstant.CONTENT_CHARSET);
    //        httpreq.setRequestBody(reqBody);
    //        httpreq.setTargetURL(url);
    //        return httpreq;
    //    }
    
    public RemoteHttpServiceImpl getRemoteHttpService()
    {
        return remoteHttpService;
    }
    
    public void setRemoteHttpService(final RemoteHttpServiceImpl remoteHttpService)
    {
        this.remoteHttpService = remoteHttpService;
    }
}
