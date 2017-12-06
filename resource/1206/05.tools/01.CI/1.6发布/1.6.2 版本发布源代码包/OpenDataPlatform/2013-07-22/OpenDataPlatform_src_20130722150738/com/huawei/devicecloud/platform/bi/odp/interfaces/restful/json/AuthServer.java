/*
 * 文 件 名:  AuthServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  认证服务入口
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.interfaces.restful.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.huawei.devicecloud.platform.bi.common.utils.filter.StatFilter;
import com.huawei.devicecloud.platform.bi.odp.message.rsp.AuthRsp;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 认证服务入口
 * 
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
@Controller
@RequestMapping("/")
public class AuthServer
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServer.class);
    
    /**
     * 认证用户名和密码是否匹配,认证通过后返回Token
     * @param body 请求消息体AuthReqReq
     * @return  响应消息体
     */
    @RequestMapping(method = RequestMethod.POST, value = "/authReq")
    public ModelAndView authReq(@RequestBody String body)
    {
        LOGGER.debug("Enter authReq,request body is {}", new Object[] {body});
        AuthRsp rsp = new AuthRsp();
        try
        {
            //提取请求对象
            //AuthReq req = OdpCommonUtils.parseObject(body, AuthReq.class);
            
            //验证用户名和密码并返回
            //Token token = userManagement.authentication(req.getUserName(), req.getPwd());
            
            //构造返回消息
            rsp.setResult_code(0);
            //rsp.setToken(token.getToken());
        }
        catch (Exception e)
        {
            LOGGER.error("Exit authReq exception,request body is {}", new Object[] {body}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.debug("Exit authReq successfully,request body is {}, return body is {}", new Object[] {body, rsp});
        StatFilter.timeStat("authReq");
        return new ModelAndView("authReqView", OdpCommonUtils.covObj2Map(rsp));
    }
}
