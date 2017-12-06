/*
 * 文 件 名:  DataQueryServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  数据查询服务入口
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.interfaces.restful.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.huawei.devicecloud.platform.bi.common.utils.filter.StatFilter;
import com.huawei.devicecloud.platform.bi.odp.constants.type.InterfaceType;
import com.huawei.devicecloud.platform.bi.odp.management.UserManagement;
import com.huawei.devicecloud.platform.bi.odp.message.req.GroupRevokeReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.GroupsPrepareReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.QueryDataCountReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.ReserveBatchDataReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.RevokeReserveReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.UserQueryReq;
import com.huawei.devicecloud.platform.bi.odp.message.rsp.QueryDataCountRsp;
import com.huawei.devicecloud.platform.bi.odp.message.rsp.ReserveBatchDataRsp;
import com.huawei.devicecloud.platform.bi.odp.server.DataOpenServer;
import com.huawei.devicecloud.platform.bi.odp.utils.CheckParamUtils;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 数据查询服务入口
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
@Controller
@RequestMapping("/")
public class DataQueryServer
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataQueryServer.class);
    
    /**
     * 数据开发服务
     */
    private DataOpenServer dataOpenServer;
    
    /**
     * userManagement对象
     */
    private UserManagement userManagement;
    
    /**
     * 查询用户总数
     * @param body 请求消息体
     * @param authenInfo 安全认证信息
     * @return  响应消息体
     */
    @RequestMapping(method = RequestMethod.POST, value = "user/analysis/user_query")
    public ModelAndView queryDataCountReq(@RequestBody String body,
        @RequestHeader(value = "Authorization", required = false) String authenInfo)
    {
        LOGGER.info("Enter authReq,request body is {}", new Object[] {body});
        QueryDataCountRsp rsp = new QueryDataCountRsp();
        QueryDataCountReq req = null;
        try
        {
            //提取请求对象
            req = OdpCommonUtils.parseObject(body, UserQueryReq.class).createQueryDataCountReq();
            req.setAuthenInfo(authenInfo);
            
            //参数校验
            CheckParamUtils.checkQueryDataCountReq(req);
            
            //认证消息头
            userManagement.authentication(req.getAuthenInfo(), req.getAppId(), req.getTimestamp());
            
            //检查是否有权限访问该接口
            userManagement.checkPriveleges(req.getAppId(), InterfaceType.QUERY_DATA_COUNT);
            
            //查询数据记录总数
            dataOpenServer.queryDataCount(req);
            
            //构造返回消息
            rsp.setResult_code(0);
        }
        catch (Exception e)
        {
            LOGGER.error("Exit queryDataCountReq exception,request body is {}", new Object[] {body}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.info("Exit queryDataCountReq successfully,request body is {}, return body is {}", new Object[] {body,
            rsp});
        //接口执行时间统计
        StatFilter.timeStat(String.format("[user_query(tid=%s,ts=%s)]",
            null == req ? "" : req.getTransactionId(),
            null == req ? "" : req.getTimestamp()));
        return new ModelAndView("queryDataCountReqView", OdpCommonUtils.covObj2Map(rsp));
    }
    
    /**
     * 预留批次数据
     * @param body 请求消息体
     * @param authenInfo 安全认证信息
     * @return 响应消息体
     */
    @RequestMapping(method = RequestMethod.POST, value = "user/analysis/groups_prepare")
    public ModelAndView reserveBatchDataReq(@RequestBody String body,
        @RequestHeader(value = "Authorization", required = false) String authenInfo)
    {
        LOGGER.info("Enter reserveBatchDataReq,request body is {}", new Object[] {body});
        ReserveBatchDataRsp rsp = new ReserveBatchDataRsp();
        ReserveBatchDataReq req = null;
        try
        {
            //提取请求对象
            req = OdpCommonUtils.parseObject(body, GroupsPrepareReq.class).createReserveBatchDataReq();
            req.setAuthenInfo(authenInfo);
            
            //参数校验
            CheckParamUtils.checkReserveBatchDataReq(req);
            
            //认证消息头
            userManagement.authentication(req.getAuthenInfo(), req.getAppId(), req.getTimestamp());
            
            //检查是否有权限访问该接口
            userManagement.checkPriveleges(req.getAppId(), InterfaceType.RESERVE_BATCH_DATA);
            
            //预留批量数据
            dataOpenServer.reserveBatchData(req);
            
            //构造返回消息
            rsp.setResult_code(0);
        }
        catch (Exception e)
        {
            //记录错误信息，并设置错误返回码
            LOGGER.error("Exit reserveBatchDataReq exception,request body is {}", new Object[] {body}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.info("Exit reserveBatchDataReq successfully,request body is {}, return body is {}", new Object[] {body,
            rsp});
        //记录接口执行时间
        StatFilter.timeStat(String.format("[groups_prepare(tid=%s,ts=%s)]",
            null == req ? "" : req.getTransactionId(),
            null == req ? "" : req.getTimestamp()));
        return new ModelAndView("reserveBatchDataReqView", OdpCommonUtils.covObj2Map(rsp));
    }
    
    /**
     * 取消预留数据
     * @param body 请求消息体
     * @param authenInfo 安全认证信息
     * @return 响应消息体
     */
    @RequestMapping(method = RequestMethod.POST, value = "user/analysis/group_revoke")
    public ModelAndView revokeReserveReq(@RequestBody String body,
        @RequestHeader(value = "Authorization", required = false) String authenInfo)
    {
        LOGGER.info("Enter reserveBatchDataReq,request body is {}", new Object[] {body});
        ReserveBatchDataRsp rsp = new ReserveBatchDataRsp();
        RevokeReserveReq req = null;
        try
        {
            //提取请求对象
            req = OdpCommonUtils.parseObject(body, GroupRevokeReq.class).createRevokeReserveReq();
            req.setAuthenInfo(authenInfo);
            
            //参数校验
            CheckParamUtils.checkRevokeReserveReq(req);
            
            //认证消息头
            userManagement.authentication(req.getAuthenInfo(), req.getAppId(), req.getTimestamp());
            
            //检查是否有权限访问该接口
            userManagement.checkPriveleges(req.getAppId(), InterfaceType.REVOKE_RESERVE);
            
            //取消预留数据
            dataOpenServer.revokeReserve(req);
            
            //构造返回消息
            rsp.setResult_code(0);
        }
        catch (Exception e)
        {
            //记录错误信息，并设置错误返回码
            LOGGER.error("Exit revokeReserveReq exception,request body is {}", new Object[] {body}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.info("Exit revokeReserveReq successfully,request body is {}, return body is {}",
            new Object[] {body, rsp});
        //记录接口执行时间
        StatFilter.timeStat(String.format("[group_revoke(ts=%s)]", null == req ? "" : req.getTimestamp()));
        return new ModelAndView("revokeReserveReqView", OdpCommonUtils.covObj2Map(rsp));
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
     * 获取userManagement
     * @return userManagement
     */
    public UserManagement getUserManagement()
    {
        return userManagement;
    }
    
    /**
     * 设置dataOpenServer
     * @param dataOpenServer 数据开发服务
     */
    @Autowired
    @Qualifier("dataOpenServer")
    public void setDataOpenServer(DataOpenServer dataOpenServer)
    {
        this.dataOpenServer = dataOpenServer;
    }
    
    /**
     * 设置userManagement
     * @param userManagement 用户管理
     */
    @Autowired
    @Qualifier("userManagement")
    public void setUserManagement(UserManagement userManagement)
    {
        this.userManagement = userManagement;
    }
}
