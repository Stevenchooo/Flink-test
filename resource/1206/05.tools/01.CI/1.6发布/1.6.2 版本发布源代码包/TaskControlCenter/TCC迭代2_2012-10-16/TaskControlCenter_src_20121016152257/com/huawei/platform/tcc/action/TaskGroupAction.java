/*
 * 文 件 名:  TaskGroupAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.type.ForeignKeyConstraintType;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.RolePrivilegeInfoEntity;
import com.huawei.platform.tcc.entity.ServiceTaskGroupInfoEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 业务任务组管理
 * 
 * @author  w00190929
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class TaskGroupAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = -4278173599543862664L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskGroupAction.class);
    
    /**
     * 业务信息
     */
    private transient ServiceTaskGroupInfoEntity serviceTG;
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 保存任务组信息
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public String saveServiceTG()
    {
        String result = "false";
        serviceTG = new ServiceTaskGroupInfoEntity();
        Boolean serviceTGReqAdd = false;
        Boolean isContinue = true;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            serviceTG.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            serviceTG.setTaskGroup(request.getParameter("taskGroup"));
            serviceTG.setDesc(request.getParameter("desc"));
            serviceTGReqAdd = Boolean.parseBoolean(request.getParameter("serviceTGReqAdd"));
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveServiceTG error", e);
        }
        
        try
        {
            if (isContinue)
            {
                //新增
                if (serviceTGReqAdd)
                {
                    if (null != serviceTG)
                    {
                        //判断是否存在任务Id
                        if (null != getTccPortalService().getService(serviceTG.getServiceId()))
                        {
                            //新增记录
                            getTccPortalService().addTaskGroup(serviceTG);
                            //记录操作日志
                            LOGGER.info("add taskGroup[{}] successfully!", new Object[] {serviceTG});
                            result = "true," + Integer.toString(serviceTG.getServiceId());
                            
                            //记录审计信息
                            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                            operateAuditInfo.setOpType(OperType.TASKGROUP_ADD);
                            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                            operateAuditInfo.setServiceIdSingle(String.valueOf(serviceTG.getServiceId()));
                            operateAuditInfo.setOperParameters(serviceTG.toString());
                            getOperationRecord().writeOperLog(operateAuditInfo);
                        }
                        else
                        {
                            result = "false,1," + Integer.toString(serviceTG.getServiceId());
                        }
                    }
                }
                else
                {
                    //更新
                    if (null != serviceTG)
                    {
                        //更新记录
                        getTccPortalService().updateTaskGroup(serviceTG);
                        //记录日志
                        LOGGER.info("update taskGroup[{}] successfully!", new Object[] {serviceTG});
                        result = "true";
                        
                        //记录审计信息
                        OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                        operateAuditInfo.setOpType(OperType.TASKGROUP_MODIFY);
                        operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                        operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                        operateAuditInfo.setServiceIdSingle(String.valueOf(serviceTG.getServiceId()));
                        operateAuditInfo.setOperParameters(serviceTG.toString());
                        getOperationRecord().writeOperLog(operateAuditInfo);
                    }
                }
            }
        }
        catch (DuplicateKeyException e)
        {
            result = "fasle,2,duplicateKey";
        }
        catch (Exception e)
        {
            String opt = (null != serviceTGReqAdd && serviceTGReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the service[{}].", new Object[] {serviceTG, e});
        }
        finally
        {
            serviceTG = null;
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
     * 由查询条件获取任务组信息
     * @return 查询成功标志位
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String reqServiceTGs()
        throws CException
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            Integer serviceId = Integer.parseInt(request.getParameter("serviceId"));
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            //查询业务的所有任务组查询
            List<ServiceTaskGroupInfoEntity> serviceTGs = getTccPortalService().getTaskGroups(serviceId);
            jsonObject.put("total", serviceTGs.size());
            jsonObject.put("rows", serviceTGs);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("reqServiceTGs fail", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务组信息
     * @return 操作成功的标志位
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String deleteServiceTG()
        throws CException
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        ServiceTaskGroupInfoEntity taskGroup = new ServiceTaskGroupInfoEntity();
        try
        {
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            String serviceId = request.getParameter("serviceId");
            String taskGroupName = request.getParameter("taskGroup");
            if (null != serviceId && null != taskGroupName)
            {
                taskGroup.setServiceId(Integer.parseInt(serviceId));
                taskGroup.setTaskGroup(taskGroupName);
                // 查询是否有绑定此任务组的任务
                TaskSearchEntity taskSearch = new TaskSearchEntity();
//                taskSearch.setTaskNames(null);
//                taskSearch.setVisibleSTgs(null);
                taskSearch.setServiceId(Integer.parseInt(serviceId));
                taskSearch.setTaskGroup(taskGroupName);
                List<TaskEntity> taskList = getTccPortalService().getTaskListByServiceTG(taskSearch);
                if (null != taskList && 0 != taskList.size())
                {
                    LOGGER.info("deleteServiceTG[{}] fail.", taskGroup);
                    rv.setReturnValue2PageType(ReturnValue2PageType.FOREIGNKEY_CONSTRAINT);
                    rv.addReturnValue(ForeignKeyConstraintType.TASK);
                }
                else
                {
                    // 删除角色权限
                    RolePrivilegeInfoEntity rolePrivilegeInfo = new RolePrivilegeInfoEntity();
                    rolePrivilegeInfo.setServiceId(Integer.parseInt(serviceId));
                    rolePrivilegeInfo.setTaskGroup(taskGroupName);
                    getOperatorMgnt().deleteRolePrivilege(rolePrivilegeInfo);
                    //删除任务组
                    getTccPortalService().deleteServiceTG(taskGroup);
                    //记录日志
                    LOGGER.info("delete serviceTG[{}] sucess.", taskGroup);
                    rv.setSuccess(true);
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.TASKGROUP_DELETE);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setServiceIdSingle(String.valueOf(taskGroup.getServiceId()));
                    operateAuditInfo.setOperParameters(taskGroup.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
            }
            
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete serviceTG[{}].", taskGroup, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return SUCCESS;
    }
    
    public ServiceTaskGroupInfoEntity getServiceDef()
    {
        return serviceTG;
    }
    
    public void setServiceTG(ServiceTaskGroupInfoEntity serviceTG)
    {
        this.serviceTG = serviceTG;
    }
    
    public int getRows()
    {
        return rows;
    }
    
    public void setRows(int rows)
    {
        this.rows = rows;
    }
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
    }
}
