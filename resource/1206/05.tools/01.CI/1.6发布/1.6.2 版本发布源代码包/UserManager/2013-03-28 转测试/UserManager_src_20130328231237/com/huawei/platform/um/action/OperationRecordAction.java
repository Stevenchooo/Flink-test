/*
 * 文 件 名:  OperationRecordAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-07-04
 */
package com.huawei.platform.um.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.um.constants.type.OperType;
import com.huawei.platform.um.constants.type.ReturnValue2PageType;
import com.huawei.platform.um.domain.OperationRecordSearch;
import com.huawei.platform.um.domain.ReturnValue2Page;
import com.huawei.platform.um.entity.OSUserInfoEntity;
import com.huawei.platform.um.entity.OperateAuditInfoEntity;
import com.huawei.platform.um.privilegeControl.OperatorMgnt;
import com.opensymphony.xwork2.ActionContext;

/**
 * 审计
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-07-04]
 */
public class OperationRecordAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRecordAction.class);
    
    /**
     * 三个月前
     */
    private static final int THREEMONTHSAGO = -3;
    
    /**
     * 由查询条件获取审计记录
     * @return 查询成功标志位
     * @throws Exception 异常
     */
    public String reqOperationRecords()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            request.setCharacterEncoding("UTF-8");
            String serviceId = request.getParameter("serviceId");
            String taskId = request.getParameter("taskId");
            String operType = request.getParameter("operType");
            String operator = request.getParameter("operator");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //以名字模糊查询
            OperationRecordSearch search = new OperationRecordSearch();
            search.setOperatorName(StringUtils.isEmpty(operator) ? null : operator);
            search.setOperType(StringUtils.isEmpty(operType) ? null : Integer.valueOf(operType));
            search.setServiceId(StringUtils.isEmpty(serviceId) ? null : serviceId);
            search.setTaskId(StringUtils.isEmpty(taskId) ? null : taskId);
            search.setStartTime(StringUtils.isEmpty(startTime) ? null : df.parse(startTime));
            search.setEndTime(StringUtils.isEmpty(endTime) ? null : df.parse(endTime));
            
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            List<OperateAuditInfoEntity> operationRecords = getOperationRecord().readRecord(search);
            Integer count = getOperationRecord().getRecordCount(search);
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", operationRecords);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (IOException e)
        {
            LOGGER.error("reqRoles fail", e);
            throw e;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("reqRoles fail", e);
            throw e;
        }
    }
    
    /**
     * 删除三个月前的审计记录
     * @return 操作成功的标志位
     * @throws Exception 异常
     */
    public String deleteOldOperationRecord()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        OSUserInfoEntity mOSUserInfo = new OSUserInfoEntity();
        try
        {
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            HttpServletResponse response =
                (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, THREEMONTHSAGO);
            getOperationRecord().deleteOldOperationRecord(calendar.getTime());
            //记录日志
            LOGGER.info("deleteOldOperationRecord success.");
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
            
            //记录审计信息
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.OPERATIONRECORD_DELOLD);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            operateAuditInfo.setOperParameters(mOSUserInfo.toString());
            getOperationRecord().writeOperLog(operateAuditInfo);
            
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to deleteOldOperationRecord.", e);
            throw e;
        }
        return SUCCESS;
    }
}
