/*
 * 文 件 名:  TaskStepAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.constants.type.SubPrivilegeType;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.entity.TaskStepExchangeEntity;
import com.huawei.platform.tcc.exception.NotExistException;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughParam;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务步骤相关的操作处理
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-17]
 */
public class TaskStepAction extends BaseAction
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = -5124478778818161589L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStepAction.class);
    
    private transient TaskStepEntity taskStep;
    
    private transient List<TaskStepEntity> taskSteps;
    
    private boolean stepReqAdd;
    
    private transient TaskStepExchangeEntity taskStepExchange;
    
    public TaskStepEntity getTaskStep()
    {
        return taskStep;
    }
    
    public void setTaskStep(TaskStepEntity taskStep)
    {
        this.taskStep = taskStep;
    }
    
    public List<TaskStepEntity> getTaskSteps()
    {
        return taskSteps;
    }
    
    public void setTaskSteps(List<TaskStepEntity> taskSteps)
    {
        this.taskSteps = taskSteps;
    }
    
    /**
     * 保存TCC任务步骤信息
     * @return 操作成功标志符
     */
    public String saveTccStep()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (stepReqAdd)
        {
            //添加步骤
            int stepId = 0;
            //修改线程的名字
            Thread.currentThread().setName(String.format("AddTccStep_%d", taskStep.getTaskId()));
            try
            {
                MDC.put(TccUtil.TASK_ID, taskStep.getTaskId());
                
                //越权判断
                List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
                Operator operator = getOperator();
                TaskEntity task = getTccPortalService().getTaskInfo(taskStep.getTaskId());
                if (null == task)
                {
                    throw new NotExistException(String.format("%d does not exist!", taskStep.getTaskId()));
                }
                
                if (null == operator || !operator.canAdd(task.getOsUser()))
                {
                    priParams.add(new PrivilegeNotEnoughParam(task.getOsUser(), SubPrivilegeType.ADD));
                }
                
                if (!priParams.isEmpty())
                {
                    throw new PrivilegeNotEnoughException(priParams);
                }
                
                TaskSearchEntity taskSearchEntity = new TaskSearchEntity();
                List<Long> taskIds = new ArrayList<Long>();
                
                taskIds.add(taskStep.getTaskId());
                taskSearchEntity.setTaskIds(taskIds);
                
                //判断是否存在任务Id
                if (getTccPortalService().getTaskListSize(taskSearchEntity) > 0)
                {
                    stepId = getTccPortalService().generateTaskStepID(taskStep.getTaskId());
                    taskStep.setStepId(stepId);
                    if (null == taskStep.getStepEnableFlag())
                    {
                        taskStep.setStepEnableFlag(false);
                    }
                    
                    LOGGER.info("add taskStep[{}].", taskStep);
                    getTccPortalService().addTaskStep(taskStep);
                    rv.setSuccess(true);
                    rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                    rv.addReturnValue(Integer.toString(stepId));
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.TASKSTEP_ADD);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setServiceIdSingle(String.valueOf(task.getServiceId()));
                    operateAuditInfo.setTaskIdSingle(String.valueOf(taskStep.getTaskId()));
                    operateAuditInfo.setOperParameters(taskStep.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
                else
                {
                    rv.setSuccess(false);
                    rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
                    rv.addReturnValue(Long.toString(taskStep.getTaskId()));
                }
            }
            catch (PrivilegeNotEnoughException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
                rv.addReturnValue(e.toString());
            }
            catch (NotExistException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
                rv.addReturnValue(e.toString());
            }
            catch (Exception e)
            {
                LOGGER.error("failed to add taskStep[{}].", taskStep, e);
            }
            finally
            {
                MDC.remove(TccUtil.TASK_ID);
            }
            
        }
        else
        {
            //更新步骤
            //修改线程的名字
            Thread.currentThread().setName(String.format("UpdateTccStep_%d", taskStep.getTaskId()));
            try
            {
                MDC.put(TccUtil.TASK_ID, taskStep.getTaskId());
                
                //越权判断
                List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
                Operator operator = getOperator();
                TaskEntity task = getTccPortalService().getTaskInfo(taskStep.getTaskId());
                if (null == task)
                {
                    throw new NotExistException(String.format("%d does not exist!", taskStep.getTaskId()));
                }
                
                if (null == operator || !operator.canModify(task.getOsUser()))
                {
                    priParams.add(new PrivilegeNotEnoughParam(task.getOsUser(), SubPrivilegeType.MODIFY));
                }
                
                if (!priParams.isEmpty())
                {
                    throw new PrivilegeNotEnoughException(priParams);
                }
                
                LOGGER.info("update taskStep[{}].", taskStep);
                //空默认为false
                if (null == taskStep.getStepEnableFlag())
                {
                    taskStep.setStepEnableFlag(false);
                }
                getTccPortalService().updateTaskStep(taskStep);
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASKSTEP_MODIFY);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(task.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(taskStep.getTaskId()));
                operateAuditInfo.setOperParameters(taskStep.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            catch (PrivilegeNotEnoughException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
                rv.addReturnValue(e.toString());
            }
            catch (NotExistException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
                rv.addReturnValue(e.toString());
            }
            catch (Exception e)
            {
                LOGGER.error("failed to update taskStep[{}].", taskStep, e);
            }
            finally
            {
                MDC.remove(TccUtil.TASK_ID);
            }
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("save the taskInfo fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 获取业务名
     * @param serviceId 业务Id
     * @return 业务名
     */
    public String getServiceName(Integer serviceId)
    {
        String servieName = Integer.toString(serviceId);
        try
        {
            ServiceDefinationEntity service = getTccPortalService().getService(serviceId);
            if (null != service)
            {
                servieName = service.getServiceName();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getServiceName error. serviceId is {}", serviceId, e);
        }
        
        return servieName;
    }
    
    /**
     * 删除任务步骤
     * 
     * @return 操作成功标志符
     */
    public String deleteTaskStep()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (null != taskStep && null != taskStep.getTaskId() && null != taskStep.getStepId())
        {
            try
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("deleteTaskStep_%d", taskStep.getTaskId()));
                MDC.put(TccUtil.TASK_ID, taskStep.getTaskId());
                
                //越权判断
                List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
                Operator operator = getOperator();
                TaskEntity task = getTccPortalService().getTaskInfo(taskStep.getTaskId());
                if (null != task)
                {
                    if (null == operator || !operator.canDelete(task.getOsUser()))
                    {
                        priParams.add(new PrivilegeNotEnoughParam(task.getOsUser(), SubPrivilegeType.DELETE));
                    }
                    
                    if (!priParams.isEmpty())
                    {
                        throw new PrivilegeNotEnoughException(priParams);
                    }
                }
                else
                {
                    throw new NotExistException(String.format("%d does not exist!", taskStep.getTaskId()));
                }
                
                LOGGER.info("delete taskStep[taskId={},stepId={}].",
                    new Object[] {taskStep.getTaskId(), taskStep.getStepId()});
                getTccPortalService().deleteTaskStep(taskStep);
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASKSTEP_DELETE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(null == task.getServiceId() ? null
                    : String.valueOf(task.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(taskStep.getTaskId()));
                operateAuditInfo.setOperParameters(taskStep.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            catch (PrivilegeNotEnoughException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
                rv.addReturnValue(e.toString());
            }
            catch (NotExistException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
                rv.addReturnValue(e.toString());
            }
            catch (Exception e)
            {
                LOGGER.error("failed to delete taskStep[taskId={},stepId={}].", new Object[] {taskStep.getTaskId(),
                    taskStep.getStepId(), e});
            }
            finally
            {
                MDC.remove(TccUtil.TASK_ID);
            }
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("deleteTaskStep failed!", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 交换任务步骤
     * 
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String exchangeTaskStep()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (null != taskStepExchange && null != taskStepExchange.getTaskId() && null != taskStepExchange.getStepIdOne()
            && null != taskStepExchange.getStepIdTwo())
        {
            try
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("exchangeTaskStep_%d", taskStepExchange.getTaskId()));
                MDC.put(TccUtil.TASK_ID, taskStepExchange.getTaskId());
                
                //权限处理
                Operator operator = getOperator();
                //获取原任务的业务和任务组
                TaskEntity srcTask = getTccPortalService().getTaskInfo(taskStepExchange.getTaskId());
                //需要具有修改权限
                if (null == operator || !operator.canModify(srcTask.getOsUser()))
                {
                    throw new PrivilegeNotEnoughException(srcTask.getOsUser(), SubPrivilegeType.MODIFY);
                }
                
                LOGGER.info("exchange taskStep, taskStepExchange is {}.", taskStepExchange);
                getTccPortalService().exchangeTaskStep(taskStepExchange);
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASKSTEP_EXCHANGE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(srcTask.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(taskStepExchange.getTaskId()));
                operateAuditInfo.setOperParameters(taskStepExchange.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            catch (PrivilegeNotEnoughException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
                rv.addReturnValue(e.toString());
            }
            catch (Exception e)
            {
                LOGGER.error("failed to exchange taskStep, taskStepExchange is {}.", taskStepExchange, e);
                throw e;
            }
            finally
            {
                MDC.remove(TccUtil.TASK_ID);
            }
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("exchangeTaskStep failed!", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 更新步骤的状态
     * @return 成功与否标识字符串
     */
    public String changeStepState()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (null != taskStep && null != taskStep.getTaskId() && null != taskStep.getStepId()
            && null != taskStep.getStepEnableFlag())
        {
            TaskStepEntity taskStepNew = new TaskStepEntity();
            taskStepNew.setTaskId(taskStep.getTaskId());
            taskStepNew.setStepId(taskStep.getStepId());
            taskStepNew.setStepEnableFlag(taskStep.getStepEnableFlag());
            
            String oper = "stop";
            if (taskStep.getStepEnableFlag())
            {
                oper = "start";
            }
            try
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("changeStepState_%d", taskStep.getTaskId()));
                MDC.put(TccUtil.TASK_ID, taskStep.getTaskId());
                
                //权限处理
                Operator operator = getOperator();
                //获取原任务的业务和任务组
                TaskEntity srcTask = getTccPortalService().getTaskInfo(taskStep.getTaskId());
                //需要具有src的删除权限
                if (taskStep.getStepEnableFlag())
                {
                    if (null == operator || !operator.canStart(srcTask.getOsUser()))
                    {
                        throw new PrivilegeNotEnoughException(srcTask.getOsUser(), SubPrivilegeType.START);
                    }
                }
                else
                {
                    if (null == operator || !operator.canStop(srcTask.getOsUser()))
                    {
                        throw new PrivilegeNotEnoughException(srcTask.getOsUser(), SubPrivilegeType.STOP);
                    }
                }
                
                LOGGER.info(oper + " taskStep[taskId={},stepId={}].", new Object[] {taskStepNew.getTaskId(),
                    taskStepNew.getStepId()});
                getTccPortalService().updateTaskStep(taskStepNew);
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                if (taskStep.getStepEnableFlag())
                {
                    operateAuditInfo.setOpType(OperType.TASKSTEP_START);
                }
                else
                {
                    operateAuditInfo.setOpType(OperType.TASKSTEP_STOP);
                }
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(srcTask.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(taskStep.getTaskId()));
                operateAuditInfo.setOperParameters(taskStep.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            catch (PrivilegeNotEnoughException e)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
                rv.addReturnValue(e.toString());
            }
            catch (Exception e)
            {
                LOGGER.error("failed to " + oper + " taskStep[taskId={},stepId={}].",
                    new Object[] {taskStepNew.getTaskId(), taskStepNew.getStepId(), e});
            }
            finally
            {
                MDC.remove(TccUtil.TASK_ID);
            }
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("changeStepState failed!", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 步骤信息列表数据json格式
     * 
     * @return 步骤列表信息数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqDataGridJsonObject()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Long taskId = null;
        Integer stepId = null;
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            if (!StringUtils.isEmpty(request.getParameter("stepId")))
            {
                stepId = Integer.parseInt(request.getParameter("stepId"));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("taskId or stepId must be an Integer!", e);
            returnEmpty = true;
        }
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        
        List<TaskStepEntity> taskStepList = new ArrayList<TaskStepEntity>();
        
        if (!returnEmpty)
        {
            //权限处理
            Operator operator = getOperator();
            returnEmpty = true;
            if (null != operator)
            {
                //获取原任务的业务和任务组
                TaskEntity srcTask = getTccPortalService().getTaskInfo(taskId);
                if (null != srcTask)
                {
                    //需要具有重做权限
                    if (operator.canQuery(srcTask.getUserGroup()))
                    {
                        returnEmpty = false;
                    }
                }
            }
            
            if (!returnEmpty)
            {
                if (null == stepId)
                {
                    taskStepList = getTccPortalService().getTaskStepList(taskId);
                }
                else
                {
                    TaskStepEntity taskStepE = new TaskStepEntity();
                    taskStepE.setTaskId(taskId);
                    taskStepE.setStepId(stepId);
                    TaskStepEntity returnStepE = getTccPortalService().getTaskStep(taskStepE);
                    if (null != returnStepE)
                    {
                        taskStepList = new ArrayList<TaskStepEntity>();
                        taskStepList.add(returnStepE);
                    }
                }
            }
        }
        
        jsonObject.put("total", taskStepList.size());
        jsonObject.put("rows", taskStepList);
        setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    public boolean getStepReqAdd()
    {
        return stepReqAdd;
    }
    
    public void setStepReqAdd(boolean stepReqAdd)
    {
        this.stepReqAdd = stepReqAdd;
    }
    
    public TaskStepExchangeEntity getTaskStepExchange()
    {
        return taskStepExchange;
    }
    
    public void setTaskStepExchange(TaskStepExchangeEntity taskStepExchange)
    {
        this.taskStepExchange = taskStepExchange;
    }
}
