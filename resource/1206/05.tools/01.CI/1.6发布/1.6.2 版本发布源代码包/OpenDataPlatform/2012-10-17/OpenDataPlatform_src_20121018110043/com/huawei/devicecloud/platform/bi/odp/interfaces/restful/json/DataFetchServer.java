/*
 * 文 件 名:  DataFetchServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  数据获取服务入口类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.interfaces.restful.json;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.huawei.devicecloud.platform.bi.common.utils.filter.StatFilter;
import com.huawei.devicecloud.platform.bi.odp.constants.type.InterfaceType;
import com.huawei.devicecloud.platform.bi.odp.management.UserManagement;
import com.huawei.devicecloud.platform.bi.odp.message.req.GroupFetchReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.WGetFileReq;
import com.huawei.devicecloud.platform.bi.odp.message.rsp.WGetFileRsp;
import com.huawei.devicecloud.platform.bi.odp.server.DataOpenServer;
import com.huawei.devicecloud.platform.bi.odp.utils.CheckParamUtils;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 数据获取服务入口类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
@Controller
@RequestMapping("/")
public class DataFetchServer
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFetchServer.class);
    
    //HTTP异常码
    private static final int ERROR_STATUS = 299;
    
    /**
     * 数据开发服务
     */
    private DataOpenServer dataOpenServer;
    
    /**
     * userManagement对象
     */
    private UserManagement userManagement;
    
    /**
     * 获取文件流接口
     * @param body 请求消息体
     * @param response 响应对象
     * @param authenInfo 安全认证信息
     * @return 响应消息体
     */
    @RequestMapping(method = RequestMethod.POST, value = "user/analysis/group_fetch")
    public ModelAndView wgetFileReq(HttpServletResponse response, @RequestBody String body,
        @RequestHeader(value = "Authorization", required = false) String authenInfo)
    {
        LOGGER.info("Enter wgetFileReq,request body is {}", new Object[] {body});
        WGetFileRsp rsp = new WGetFileRsp();
        WGetFileReq req = null;
        try
        {
            //提取请求对象
            req = OdpCommonUtils.parseObject(body, GroupFetchReq.class).createWGetFileReq();
            req.setAuthenInfo(authenInfo);
            
            //参数校验
            CheckParamUtils.checkWgetFileReq(req);
            
            //认证消息头
            userManagement.authentication(req.getAuthenInfo(), req.getAppId(), req.getTimestamp());
            
            //检查是否有权限访问该接口
            userManagement.checkPriveleges(req.getAppId(), InterfaceType.WGET_FILE);
            
            //获取文件流接口
            File file = dataOpenServer.wgetFile(req);
            
            //构造返回消息
            rsp.setResult_code(0);
            
            //设置返回消息头
            setZipHeader(response, file);
            //将文件流转到HTTP输出流
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
            LOGGER.info("Exit wgetFileReq successfully,request body is {}, return body is {}",
                new Object[] {body, rsp});
            return null;
        }
        catch (Exception e)
        {
            //构造异常返回码
            LOGGER.error("Exit wgetFileReq exception,request body is {}", new Object[] {body}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        
        //执行时间统计
        StatFilter.timeStat(String.format("[group_fetch(ts=%s)]",
            null == req ? "" : req.getTimestamp()));
        //设置错误状态码
        response.setStatus(ERROR_STATUS);
        return new ModelAndView("wgetFileReqView", OdpCommonUtils.covObj2Map(rsp));
    }
    
    /**
     * 设置zip文件头
     */
    private void setZipHeader(HttpServletResponse response, File file)
    {
        if (null != response)
        {
            //设置文件相关头
            if (null != file)
            {
                response.setContentType("application/oct-stream");
                response.addHeader("Content-Disposition", String.format("attachment; filename=%s.zip", file.getName()));
                response.addHeader("Content-Length", Long.toString(file.length()));
            }
        }
    }
    
    /**
     * 获取dataOpenServer
     * @return dataOpenServer
     */
    public DataOpenServer getDataOpenServer()
    {
        return dataOpenServer;
    }
    
    /**
     * 设置dataOpenServer
     * @param dataOpenServer 数据开发服务器
     */
    @Autowired
    @Qualifier("dataOpenServer")
    public void setDataOpenServer(DataOpenServer dataOpenServer)
    {
        this.dataOpenServer = dataOpenServer;
    }
    
    /**
     * 获取userManagement
     * @return userManagement
     */
    public UserManagement getUserManagement()
    {
        return userManagement;
    }
    
    /**
     * 设置userManagement
     * @param userManagement 用户管理对象
     */
    @Autowired
    @Qualifier("userManagement")
    public void setUserManagement(UserManagement userManagement)
    {
        this.userManagement = userManagement;
    }
}
