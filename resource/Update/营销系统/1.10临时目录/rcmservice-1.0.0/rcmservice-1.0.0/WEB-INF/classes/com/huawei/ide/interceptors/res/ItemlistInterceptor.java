package com.huawei.ide.interceptors.res;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.io.CharStreams;
import com.huawei.ide.interceptors.res.rcm.CException;
import com.huawei.ide.interceptors.res.rcm.CheckParamUtils;
import com.huawei.ide.interceptors.res.rcm.CommonUtils;
import com.huawei.ide.interceptors.res.rcm.RCMResultCodeConstants;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListReq;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListRsp;
import com.huawei.ide.services.rcm.IRecommendService;

public class ItemlistInterceptor implements HandlerInterceptor
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemlistInterceptor.class);
    
    /**
     * 时间戳正则
     */
    private static final String REGEX = "[0-9]{13,13}";
    
    @Autowired
    private IRecommendService recommendService;
    
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Object obj, Exception exception)
        throws Exception
    {
        LOGGER.debug("ItemlistInterceptor afterCompletion.");
        
    }
    
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj,
        ModelAndView modelAndView)
        throws Exception
    {
        LOGGER.debug("ItemlistInterceptor postHandle.");
        
    }
    
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj)
        throws Exception
    {
        LOGGER.info("rcmForItemlist start..");
        String body = null;
        if ("POST".equalsIgnoreCase(httpServletRequest.getMethod()))
        {
            body = CharStreams.toString(httpServletRequest.getReader());
        }
        
        RcmForItemListReq req = new RcmForItemListReq();
        RcmForItemListRsp rsp = new RcmForItemListRsp();
        
        try
        {
            // 消息鉴权
            CommonUtils.authentication(httpServletRequest.getHeader("authorization"), body);
            
            req = CommonUtils.jsonToItemListReq(body);
            // 校验请求id
            if (judgeParameters(req.getReqId(), true))
            {
                LOGGER.error("authentication failed: the ReqId is invalid!");
                rsp.setResultCode(RCMResultCodeConstants.INVALID_REQ_ID_ERROR);
                extractResponseCode(req, rsp, httpServletResponse);
                return false;
            }
            // 校验场景id
            if (judgeParameters(req.getRcmScenario(), false))
            {
                LOGGER.error("authentication failed: the RcmScenarioId is invalid!");
                rsp.setResultCode(RCMResultCodeConstants.INVALID_RCM_SCENARIO);
                extractResponseCode(req, rsp, httpServletResponse);
                return false;
            }
            // 校验appKey
            boolean boolAppKey = validateAppKey(req.getAppKey());
            // 如果appkey不符合要求，返回参数不合法的resultCode
            if (!boolAppKey)
            {
                LOGGER.error("authentication failed: the AppKey is invalid!");
                rsp.setResultCode(RCMResultCodeConstants.INVALID_APPKEY_ERROR);
                extractResponseCode(req, rsp, httpServletResponse);
                return false;
            }
            // 如果设备id是空或者长度大于等于16，则返回参数错误码
            if (validateStrisNullOrEmpty(req.getDeviceId()) || req.getDeviceId().length() >= 16)
            {
                LOGGER.error("authentication failed: the DeviceId is invalid!");
                rsp.setResultCode(RCMResultCodeConstants.INVALID_DEVICE_ID_ERROR);
                extractResponseCode(req, rsp, httpServletResponse);
                return false;
            }
            // 验证时间戳，如果是空，返回参数错误码
            if (validateStrisNullOrEmpty(req.getTs()) || !req.getTs().matches(REGEX))
            {
                LOGGER.error("authentication failed: the timeStamp is invalid!");
                rsp.setResultCode(RCMResultCodeConstants.INVALID_TIMESTAMP);
                extractResponseCode(req, rsp, httpServletResponse);
                return false;
            }
            
        }
        catch (JSONException e)
        {
            rsp.setReqId(req.getReqId());
            rsp.setRcmScenario(req.getRcmScenario());
            rsp.setResultCode(RCMResultCodeConstants.JSON_FORMAT_ERROR);
            LOGGER.error("authentication failed: the body can't format to json!");
            extractPublicCode(rsp, httpServletResponse);
            return false;
        }
        catch (CException e)
        {
            rsp.setReqId(req.getReqId());
            rsp.setRcmScenario(req.getRcmScenario());
            rsp.setResultCode(RCMResultCodeConstants.INVALID_APPSECRET);
            LOGGER.error("authentication failed! the body is invalid!");
            extractPublicCode(rsp, httpServletResponse);
            return false;
        }
        // 响应体
        rsp = recommendService.executeRecommendService(req);
        // rsp 为空的情况，给其返回一个错误码
        if (rsp == null)
        {
            rsp = new RcmForItemListRsp();
            rsp.setResultCode(RCMResultCodeConstants.SYS_ERROR);
            rsp.setReqId(req.getReqId());
            rsp.setRcmScenario(req.getRcmScenario());
            LOGGER.error("please check the rule enigeer!");
        }
        // 推荐的Item列表为空
        else if (null != rsp && rsp.getRecommendList() == null)
        {
            rsp.setResultCode(RCMResultCodeConstants.SYS_ERROR);
            rsp.setReqId(req.getReqId());
            rsp.setRcmScenario(req.getRcmScenario());
            LOGGER.error("please check the rule enigeer!");
        }
        extractPublicCode(rsp, httpServletResponse);
        LOGGER.info("rcmForItemlist end..");
        return false;
    }
    
    /**
     * 提取的公共代码 <功能详细描述>
     * 
     * @param rsp
     *            rsp
     * @param httpservletresponse
     *            httpservletresponse
     * @throws IOException
     *             IO异常
     * @see [类、类#方法、类#成员]
     */
    private void extractPublicCode(RcmForItemListRsp rsp, HttpServletResponse httpservletresponse)
        throws IOException
    {
        String jsonStr = JSON.toJSONString(rsp);
        PrintWriter out = httpservletresponse.getWriter();
        out.write(jsonStr);
        out.flush();
    }
    
    /**
     * 用于RequestId 和 RcmScenario的条件判断
     * 
     * @param str
     *            字符串
     * @param isRequestId
     *            是否是RequestId
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    private boolean judgeParameters(String str, boolean isRequestId)
    {
        return null == str || "".equals(str) || (isRequestId ? str.length() > 40 : str.length() > 3);
    }
    
    /**
     * 验证appKey
     * 
     * @param appKey
     *            appKey
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    private boolean validateAppKey(String appKey)
    {
        try
        {
            CheckParamUtils.checkAppKey(appKey);
        }
        catch (CException e)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 判断字符串是否是空，如果为空返回true,否则返回false
     * 
     * @param str
     *            str
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    private boolean validateStrisNullOrEmpty(String str)
    {
        return null == str || str.isEmpty() || "".equals(str);
    }
    
    /**
     * 提取响应体中的公共代码
     * 
     * @param req
     *            RcmForItemListReq
     * @param rsp
     *            RcmForItemListRsp
     * @param httpServletResponse
     *            httpServletResponse
     * @throws IOException
     *             IO异常
     * @see [类、类#方法、类#成员]
     */
    private void extractResponseCode(RcmForItemListReq req, RcmForItemListRsp rsp,
        HttpServletResponse httpServletResponse)
        throws IOException
    {
        rsp.setReqId(req.getReqId());
        rsp.setRcmScenario(req.getRcmScenario());
        extractPublicCode(rsp, httpServletResponse);
    }
    
}
