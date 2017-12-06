/*
 * 文 件 名:  ServiceManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.um.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.constants.type.OperType;
import com.huawei.platform.um.constants.type.ReturnValue2PageType;
import com.huawei.platform.um.domain.KeyValuePair;
import com.huawei.platform.um.domain.ReturnValue2Page;
import com.huawei.platform.um.domain.ServiceSearch;
import com.huawei.platform.um.entity.OperateAuditInfoEntity;
import com.huawei.platform.um.entity.ServiceDefinationEntity;
import com.huawei.platform.um.privilegeControl.OperatorMgnt;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.opensymphony.xwork2.ActionContext;

/**
 * 业务管理
 * 
 * @author  w00190929
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-2-17]
 */
public class ServiceManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManageAction.class);
    
    /**
     * 业务信息
     */
    private transient ServiceDefinationEntity serviceDef;
    
    /**
     * 保存业务信息
     * @return 操作成功标志符
     */
    public String saveService()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String result = "false";
        Boolean serviceReqAdd = false;
        try
        {
            serviceReqAdd =
                StringUtils.isEmpty(request.getParameter("serviceReqAdd")) ? false
                    : Boolean.parseBoolean(request.getParameter("serviceReqAdd"));
            //新增
            if (serviceReqAdd)
            {
                if (null != serviceDef)
                {
                    serviceDef.setCreateTime(new Date());
                    //新增记录
                    getTccPortalService().addService(serviceDef);
                    
                    //记录操作日志
                    LOGGER.info("add service[{}] successfully!", new Object[] {serviceDef});
                    result = "true";
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.SERVICE_ADD);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setServiceIdSingle(String.valueOf(serviceDef.getServiceId()));
                    operateAuditInfo.setOperParameters(serviceDef.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
            }
            else
            {
                //更新
                if (null != serviceDef)
                {
                    //                    serviceDef.setCreateTime(new Date());
                    //更新记录
                    getTccPortalService().updateService(serviceDef);
                    //记录日志
                    LOGGER.info("update service[{}] successfully!", new Object[] {serviceDef});
                    result = "true";
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.SERVICE_MODIFY);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setServiceIdSingle(String.valueOf(serviceDef.getServiceId()));
                    operateAuditInfo.setOperParameters(serviceDef.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
            }
        }
        catch (DuplicateKeyException e)
        {
            result = "fasle,duplicateKey";
        }
        catch (Exception e)
        {
            String opt = (null != serviceReqAdd && serviceReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the service[{}].", new Object[] {serviceDef, e});
        }
        finally
        {
            serviceDef = null;
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("save the service fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 获取业务名
     * @return  操作成功标志位
     * @throws Exception 统一封装的异常
     */
    public String grabServiceName()
        throws Exception
    {
        String result = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Integer serviceId = null;
        try
        {
            if (null != request.getParameter("serviceId"))
            {
                serviceId = Integer.parseInt(request.getParameter("serviceId"));
            }
        }
        catch (Exception e)
        {
            LOGGER.info("grabServiceName error! serviceId is {}.", request.getParameter("serviceId"), e);
        }
        
        try
        {
            
            ServiceDefinationEntity service = getTccPortalService().getService(serviceId);
            result += "|" + service.getServiceName();
        }
        catch (Exception e)
        {
            result = "false";
            LOGGER.error("grab serviceName fail! serviceId is {}", serviceId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return "success";
    }
    
    /**
     * 查询选择的业务列表
     * @return 查询成功标志位
     * @throws CException 统一封装的异常
     */
    /**
     * 业务Id业务名键值对列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqServiceNames()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
        }
        catch (Exception e)
        {
            LOGGER.warn("containAllCol must be true or false!", e);
        }
        
        List<ServiceDefinationEntity> services = getTccPortalService().getAllServiceIdNameList();
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        
        KeyValuePair keyValuePair;
        
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("-1", "全部"));
        }
        
        for (ServiceDefinationEntity service : services)
        {
            keyValuePair = new KeyValuePair(Integer.toString(service.getServiceId()), service.getServiceName());
            keyValuePairList.add(keyValuePair);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 由查询条件获取任务
     * @return 查询成功标志位
     */
    public String reqServices()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            request.setCharacterEncoding("UTF-8");
            String serviceNames = request.getParameter("serviceNames");
            ServiceSearch serviceSearch = new ServiceSearch();
            if (!StringUtils.isEmpty(serviceNames))
            {
                String[] serviceNameArr = serviceNames.split(";");
                //如果searchTaskId为以分号结尾的单任务名,则保留分号，进行精确查询
                if (serviceNameArr.length == 1 && serviceNames.endsWith(";"))
                {
                    serviceSearch.getServiceNames().add(URLDecoder.decode(serviceNameArr[0], "utf-8") + ";");
                }
                else
                {
                    for (String serviceNameStr : serviceNameArr)
                    {
                        if (!StringUtils.isEmpty(serviceNameStr))
                        {
                            serviceSearch.getServiceNames().add(URLDecoder.decode(serviceNameStr, "utf-8"));
                        }
                        else
                        {
                            serviceSearch.getServiceNames().add("");
                        }
                    }
                }
            }
            else
            {
                serviceSearch.getServiceNames().add("");
            }
            
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            serviceSearch.setPageIndex((page - 1) * rows);
            serviceSearch.setPageSize(rows);
            
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            //以名字模糊查询
            List<ServiceDefinationEntity> services = getTccPortalService().getServicesByName(serviceSearch);
            Integer count = getTccPortalService().getServicesCountByName(serviceSearch);
            jsonObject.put("total", count);
            jsonObject.put("rows", services);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            
        }
        catch (NumberFormatException e1)
        {
            LOGGER.error("reqServices fail", e1);
        }
        catch (NullPointerException e2)
        {
            LOGGER.error("reqServices fail", e2);
        }
        catch (UnsupportedEncodingException e3)
        {
            LOGGER.error("reqServices fail", e3);
        }
        catch (Exception e4)
        {
            LOGGER.error("reqServices fail", e4);
        }
        
        return SUCCESS;
    }
    
    /**
     * 删除业务
     * @return 操作成功的标志位
     */
    public String deleteService()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        Integer serviceId = null;
        
        //删除任务
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        try
        {
            if (null != request.getParameter("serviceId"))
            {
                serviceId = Integer.parseInt(request.getParameter("serviceId"));
                
                //删除业务
                getTccPortalService().deleteService(serviceId);
                //记录日志
                LOGGER.info("delete service[serviceId={}] sucess.", serviceId);
                rv.setSuccess(true);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.SERVICE_DELETE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(serviceId));
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (Exception e)
        {
            if (e.getCause() instanceof MySQLIntegrityConstraintViolationException)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.FOREIGNKEY_CONSTRAINT);
            }
            LOGGER.info("deleteService error! serviceId is {}.", request.getParameter("serviceId"), e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete service[serviceId={}].", serviceId, e);
        }
        
        return SUCCESS;
    }
    
    public ServiceDefinationEntity getServiceDef()
    {
        return serviceDef;
    }
    
    public void setServiceDef(ServiceDefinationEntity serviceDef)
    {
        this.serviceDef = serviceDef;
    }
}
