package com.huawei.ide.interceptors.res;

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
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserReq;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserRsp;
import com.huawei.ide.services.rcm.IRecommendService;

/**
 * 
 * <RcmInterceptor
 * <功能详细描述>
 * 
 * @author  z00280396
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class RcmInterceptor implements HandlerInterceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RcmInterceptor.class);
    
    @Autowired
    private IRecommendService recommendService;
    
    private final boolean validator(RcmForThemeUserReq req)
    {
        try
        {
            CheckParamUtils.checkAppKey(req.getAppKey());
            
        }
        catch (CException e)
        {
            return false;
        }
        return true;
    }
    
    /**
     * preHandle
     * @param httpservletrequest
     *         请求消息体
     * @param httpservletresponse
     *         响应消息体
     * @param obj
     *         obj
     * @return    boolean
     * @throws Exception
     *        Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj)
        throws Exception
    {
        
        String body = null;
        if ("POST".equalsIgnoreCase(httpservletrequest.getMethod()))
        {
            body = CharStreams.toString(httpservletrequest.getReader());
        }
        
        RcmForThemeUserReq req = new RcmForThemeUserReq();
        RcmForThemeUserRsp rsp = new RcmForThemeUserRsp();
        //封装请求
        try
        {
            req = CommonUtils.jsonToReq(body);
            if (null == req.getDeviceID() || "".equals(req.getDeviceID()) || req.getDeviceID().length() >= 16)
            {
                rsp.setReqId(req.getReqId());
                rsp.setCode(RCMResultCodeConstants.INVALID_DEVICE_ID_ERROR);
                LOGGER.error("authentication failed: the DeviceID is invalid!");
                String jsonStr = JSON.toJSONString(rsp);
                PrintWriter out = httpservletresponse.getWriter();
                out.write(jsonStr);
                out.flush();
                return false;
            }
            if (null == req.getReqId() || "".equals(req.getReqId()))
            {
                rsp.setCode(RCMResultCodeConstants.INVALID_REQ_ID_ERROR);
                LOGGER.error("authentication failed: the ReqId is invalid!");
                String jsonStr = JSON.toJSONString(rsp);
                PrintWriter out = httpservletresponse.getWriter();
                out.write(jsonStr);
                out.flush();
                return false;
            }
        }
        catch (JSONException e)
        {
            rsp.setCode(RCMResultCodeConstants.JSON_FORMAT_ERROR);
            LOGGER.error("authentication failed: the body can't format to json!");
            String jsonStr = JSON.toJSONString(rsp);
            PrintWriter out = httpservletresponse.getWriter();
            out.write(jsonStr);
            out.flush();
            return false;
        }
        
        String appSecret = req.getAppSecret();
        //消息鉴权
        try
        {
            String result = CommonUtils.checkSecret(appSecret, req.getTs());
            if (!"".equals(result))
            {
                rsp.setReqId(req.getReqId());
                rsp.setCode(result);
                LOGGER.error("authentication failed! the appSecret is invalid!");
                String jsonStr = JSON.toJSONString(rsp);
                PrintWriter out = httpservletresponse.getWriter();
                out.write(jsonStr);
                out.flush();
                return false;
            }
        }
        catch (CException e)
        {
            
            rsp.setReqId(req.getReqId());
            rsp.setCode(RCMResultCodeConstants.INVALID_APPSECRET);
            LOGGER.error("authentication failed! the appSecret is invalid!");
            String jsonStr = JSON.toJSONString(rsp);
            PrintWriter out = httpservletresponse.getWriter();
            out.write(jsonStr);
            out.flush();
            
            return false;
            
        }
        
        //参数校验
        boolean validator = validator(req);
        if (!validator)
        {
            rsp.setReqId(req.getReqId());
            rsp.setCode(RCMResultCodeConstants.INVALID_APPKEY_ERROR);
            LOGGER.error("validator error: the appkey is invalid!");
            String jsonStr = JSON.toJSONString(rsp);
            PrintWriter out = httpservletresponse.getWriter();
            out.write(jsonStr);
            out.flush();
            return false;
        }
        
        rsp = recommendService.executeRecommendService(req);
        
        if (rsp == null)
        {
            rsp = new RcmForThemeUserRsp();
            rsp.setCode(RCMResultCodeConstants.SYS_ERROR);
            rsp.setReqId(req.getReqId());
            LOGGER.error("please check the rule enigeer!");
        }
        else if (rsp != null && rsp.getData().getCards() == null)
        {
            rsp.setCode(RCMResultCodeConstants.SYS_ERROR);
            rsp.setReqId(req.getReqId());
            LOGGER.error("please check the rule enigeer!");
        }
        
        String jsonStr = JSON.toJSONString(rsp);
        PrintWriter out = httpservletresponse.getWriter();
        out.write(jsonStr);
        out.flush();
        
        return false;
    }
    
    /**
     * postHandle
     * @param httpservletrequest
     *          请求消息体
     * @param httpservletresponse
     *         响应消息体
     * @param obj
     *         obj
     * @param modelandview
     *         modelandview
     * @throws Exception
     *           Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj,
        ModelAndView modelandview)
        throws Exception
    {
        LOGGER.debug("RcmInterceptor postHandle.");
    }
    
    /**
     * afterCompletion
     * @param httpservletrequest
     *         请求消息体
     * @param httpservletresponse
     *         响应消息体
     * @param obj
     *         obj
     * @param exception
     *         exception
     * @throws Exception
     *          Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse,
        Object obj, Exception exception)
        throws Exception
    {
        LOGGER.debug("RcmInterceptor afterCompletion.");
    }
}
