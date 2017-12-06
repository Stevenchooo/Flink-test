/*
 * 文 件 名:  DataQueryServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  数据查询服务入口
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.interfaces.restful.json;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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

import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;
import com.huawei.devicecloud.platform.bi.common.utils.Encoder;
import com.huawei.devicecloud.platform.bi.common.utils.filter.StatFilter;
import com.huawei.devicecloud.platform.bi.odp.constants.type.InterfaceType;
import com.huawei.devicecloud.platform.bi.odp.management.UserManagement;
import com.huawei.devicecloud.platform.bi.odp.message.req.FileUploadReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.GroupRevokeReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.GroupsPrepareReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.QueryDataCountReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.ReserveBatchDataReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.RevokeReserveReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.UserQueryReq;
import com.huawei.devicecloud.platform.bi.odp.message.rsp.FileUploadRsp;
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
    public ModelAndView queryDataCountReq(@RequestBody
        String body, @RequestHeader(value = "Authorization", required = false)
        String authenInfo)
    {
        LOGGER.info("Enter authReq,request body is {}", new Object[] {Encoder.getInstance().truncatEncode(body)});
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
            LOGGER.error("Exit queryDataCountReq exception,request body is {}", new Object[] {Encoder.getInstance()
                .truncatEncode(body)}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.info("Exit queryDataCountReq successfully,request body is {}, return body is {}", new Object[] {
            Encoder.getInstance().truncatEncode(body), rsp});
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
    public ModelAndView reserveBatchDataReq(@RequestBody
        String body, @RequestHeader(value = "Authorization", required = false)
        String authenInfo)
    {
        LOGGER.info("Enter reserveBatchDataReq,request body is {}",
            new Object[] {Encoder.getInstance().truncatEncode(body)});
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
            
            //文件ID校验
            dataOpenServer.checkFileId(req.getImeiFileId());
            
            //预留批量数据
            dataOpenServer.reserveBatchData(req);
            
            //构造返回消息
            rsp.setResult_code(0);
        }
        catch (Exception e)
        {
            //记录错误信息，并设置错误返回码
            LOGGER.error("Exit reserveBatchDataReq exception,request body is {}", new Object[] {Encoder.getInstance()
                .truncatEncode(body)}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.info("Exit reserveBatchDataReq successfully,request body is {}, return body is {}", new Object[] {
            Encoder.getInstance().truncatEncode(body), rsp});
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
    public ModelAndView revokeReserveReq(@RequestBody
        String body, @RequestHeader(value = "Authorization", required = false)
        String authenInfo)
    {
        LOGGER.info("Enter reserveBatchDataReq,request body is {}",
            new Object[] {Encoder.getInstance().truncatEncode(body)});
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
            LOGGER.error("Exit revokeReserveReq exception,request body is {}", new Object[] {Encoder.getInstance()
                .truncatEncode(body)}, e);
            OdpCommonUtils.setResultByException(rsp, e);
        }
        LOGGER.info("Exit revokeReserveReq successfully,request body is {}, return body is {}", new Object[] {
            Encoder.getInstance().truncatEncode(body), rsp});
        //记录接口执行时间
        StatFilter.timeStat(String.format("[group_revoke(ts=%s)]", null == req ? "" : req.getTimestamp()));
        return new ModelAndView("revokeReserveReqView", OdpCommonUtils.covObj2Map(rsp));
    }
    
    /**
     * <文件上传接口>
     * @param authenInfo    鉴权信息
     * @param req           文件上传请求
     * @param rsp           文件上传返回
     * @return              响应消息体
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST, value = "user/analysis/upload_file")
    public ModelAndView fileUpLoadReq(@RequestHeader(value = "Authorization", required = false)
        String authenInfo, HttpServletRequest req, HttpServletResponse rsp)
    {
        LOGGER.info("Enter fileUpLoadReq");
        FileUploadRsp uploadRsp = new FileUploadRsp();
        FileUploadReq uploadReq = new FileUploadReq();
        try
        {
            //设置文件上传参数
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(-1);
            ServletFileUpload load = new ServletFileUpload(factory);
            load.setFileSizeMax(-1);
            long sizeMax = -1;
            try
            {
                sizeMax = Long.valueOf(CommonUtils.getSysConfigValueByKey("file.upload.maxsize"));
            }
            catch (Exception e)
            {
                LOGGER.error("get file.upload.maxsize config error!");
                
                //文件大小默认最大为100M
                sizeMax = 104857600;
            }
            
            load.setSizeMax(sizeMax);
            load.setHeaderEncoding("UTF-8");
            List<FileItem> fileItemList = load.parseRequest(req);
            uploadReq = makeFileUploadReq(fileItemList);
            uploadReq.setAuthenInfo(authenInfo);
            
            //参数校验
            CheckParamUtils.checkFileUploadReq(uploadReq);
            
            //认证消息头
            userManagement.authentication(uploadReq.getAuthenInfo(), uploadReq.getAppId(), uploadReq.getTimestamp());
            
            //检查是否有权限访问该接口
            userManagement.checkPriveleges(uploadReq.getAppId(), InterfaceType.FILE_UPLOAD);
            
            //处理文件上传入库并生成相应的ID
            dataOpenServer.dealFileUpload(uploadRsp, fileItemList);
            
            //构造返回消息
            uploadRsp.setResult_code(0);
        }
        catch (Exception e)
        {
            //记录错误信息，并设置错误返回码
            LOGGER.error("Exit fileUpLoadReq exception is {}", new Object[] {e});
            OdpCommonUtils.setResultByException(uploadRsp, e);
        }
        LOGGER.info("Exit upload_file successfully,request body is {}, return body is {}", new Object[] {uploadReq,
            uploadRsp});
        
        return new ModelAndView("fileUpLoadReqView", OdpCommonUtils.covObj2Map(uploadRsp));
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param fileItemList
     * @return
     * @throws UnsupportedEncodingException
     * @throws CException
     * @see [类、类#方法、类#成员]
     */
    private FileUploadReq makeFileUploadReq(List<FileItem> fileItemList)
        throws UnsupportedEncodingException
    {
        FileUploadReq req = new FileUploadReq();
        for (FileItem fileItem : fileItemList)
        {
            if (fileItem.isFormField())
            {
                if (fileItem.getFieldName().equals("app_id"))
                {
                    req.setAppId(fileItem.getString("UTF-8"));
                }
                else if (fileItem.getFieldName().equals("timestamp"))
                {
                    req.setTimestamp(fileItem.getString("UTF-8"));
                }
                else if (fileItem.getFieldName().equals("file_type"))
                {
                    req.setFileType(fileItem.getString("UTF-8"));
                }
            }
            
        }
        return req;
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
