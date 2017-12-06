/*
 * 文 件 名:  TccTaskAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务行为
 * 创 建 人:  w00190929
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.AlarmType;
import com.huawei.platform.tcc.constants.type.EndBatchFlag;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.constants.type.SubPrivilegeType;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.domain.DependRelation;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.TaskOwnerInfo;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.entity.DBServerConfigEntity;
import com.huawei.platform.tcc.entity.OSUserGroupServiceEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskAlarmChannelInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmItemsEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughParam;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.sync.Sync2Produce;
import com.huawei.platform.tcc.sync.Sync2Test;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * TCC任务表处理逻辑
 * 
 * @author  w00190929
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-17]
 */
public class TccTaskAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccTaskAction.class);
    
    /**
     * 默认的优先级
     */
    private static final Integer DEFAULT_PRIORITY = 5;
    
    /**
     * 默认权重
     */
    private static final Integer DEFAULT_WEIGHT = 1;
    
    /**
     * 任务信息
     */
    private transient TaskEntity task;
    
    /**
     * 任务信息列表
     */
    private transient List<TaskEntity> taskList;
    
    /**
     * 新增或更新标志
     */
    private Boolean taskReqAdd;
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 同步到生产
     */
    private transient Sync2Produce sync2Produce;
    
    /**
     * 同步到测试
     */
    private transient Sync2Test sync2Test;
    
    public Sync2Produce getSync2Produce()
    {
        return sync2Produce;
    }
    
    public void setSync2Produce(Sync2Produce sync2Produce)
    {
        this.sync2Produce = sync2Produce;
    }
    
    public Sync2Test getSync2Test()
    {
        return sync2Test;
    }
    
    public void setSync2Test(Sync2Test sync2Test)
    {
        this.sync2Test = sync2Test;
    }
    
    public TaskEntity getTask()
    {
        return task;
    }
    
    public void setTask(TaskEntity task)
    {
        this.task = task;
    }
    
    public List<TaskEntity> getTaskList()
    {
        return taskList;
    }
    
    public void setTaskList(List<TaskEntity> taskList)
    {
        this.taskList = taskList;
    }
    
    public Boolean getTaskReqAdd()
    {
        return taskReqAdd;
    }
    
    public void setTaskReqAdd(Boolean taskReqAdd)
    {
        this.taskReqAdd = taskReqAdd;
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
    
    /**
     * 所有用户可见的业务Id业务名键值对列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getVServiceIdNameList()
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
        
        List<ServiceDefinationEntity> services = getOperatorMgnt().getVisibleServices(getOperator());
        
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
     * 所有业务Id业务名键值对列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getAllServiceIdNames()
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
        
        List<ServiceDefinationEntity> services = getOperatorMgnt().getAllServiceIdNames(getOperator());
        
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
     * 异步获取最大权重值，是否有相应的历史运行状态记录以及任务的状态（正常或停止）
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String grabMaxWeightHisRSCountState()
        throws Exception
    {
        String returnValue = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = null;
        int maxWeight = TccConfig.getMaxRunningCycleTaskNum();
        try
        {
            if (null != request.getParameter("taskId"))
            {
                taskId = Long.parseLong(request.getParameter("taskId"));
            }
        }
        catch (Exception e)
        {
            LOGGER.info("grabMaxWeightHisRSFlagState error!", e);
        }
        
        try
        {
            if (null != taskId)
            {
                int taskRSCount = 0;
                TaskRSQueryParam taskRSQueryParam = new TaskRSQueryParam();
                taskRSQueryParam.setTaskId(taskId);
                int taskState = -1;
                TaskEntity taskE = getTccPortalService().getTaskInfo(taskId);
                if (null != taskE && null != taskE.getTaskState())
                {
                    taskState = taskE.getTaskState();
                }
                taskRSCount = getTccPortalService().getTaskRSCount(taskId);
                returnValue += String.format("|%d,%d,%d", maxWeight, taskRSCount, taskState);
            }
            else
            {
                returnValue += String.format("|%d", maxWeight);
            }
            
        }
        catch (Exception e)
        {
            returnValue = "false";
            LOGGER.error("grabMaxWeightHisRSFlagState error!", e);
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 保存TCC任务信息
     * @return 操作成功标志符
     */
    public String saveTccTask()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            if (null != task.getDeployedNodeList())
            {
                //移除空格
                task.setDeployedNodeList(task.getDeployedNodeList().replace(" ", ""));
            }
            else
            {
                task.setDeployedNodeList("");
            }
            
            if (taskReqAdd)
            {
                //生成任务ID，业务ID＋2位任务类型＋4位编号
                Long taskIDTemp = getTccPortalService().generateTaskID(task.getServiceId(), task.getTaskType());
                //修改线程的名字
                Thread.currentThread().setName(String.format("AddTccTask_%d", taskIDTemp));
                MDC.put(TccUtil.TASK_ID, taskIDTemp);
                
                task.setTaskId(taskIDTemp);
                task.setTaskEnableFlag(false);
                task.setTaskState(TaskState.STOP);
                if (null == task.getEndBatchFlag())
                {
                    task.setEndBatchFlag(EndBatchFlag.NORMAL);
                }
                
                Operator operator = getOperator();
                //检查添加任务权限
                checkAddTaskPrivilege(operator, task.getTaskId(), task.getOsUser(), task.getDependTaskIdList());
                
                task.setCreator(operator.getOperatorName());
                
                //获取用户组和任务Id防止数据不一致
                List<String> osUsers = new ArrayList<String>();
                osUsers.add(task.getOsUser());
                List<OSUserGroupServiceEntity> osUGSs = getOperatorMgnt().getOSUserGroupServicesByName(osUsers);
                task.setUserGroup(osUGSs.get(0).getUserGroup());
                
                //非系统管理员不允许修改优先级和权重
                if (!operator.isSystemAdmin())
                {
                    task.setPriority(DEFAULT_PRIORITY);
                    task.setWeight(DEFAULT_WEIGHT);
                }
                
                task.setCreator(operator.getOperatorName());
                
                LOGGER.info("add Task[{}]", new Object[] {task});
                //新增
                getTccPortalService().addTask(task);
                
                //通知新增任务
                Eventor.fireEvent(this, EventType.ADD_TASK, task.getTaskId());
                
                //新增告警项
                TaskAlarmItemsEntity taskAlarmItems = new TaskAlarmItemsEntity();
                taskAlarmItems.setTaskId(task.getTaskId());
                taskAlarmItems.setIsAlarmPermitted(true);
                taskAlarmItems.setAlarmType(AlarmType.getSevereAlarmType());
                getTccPortalService().addAlarmItems(taskAlarmItems);
                LOGGER.info("add AlarmItems. taskAlarmItems is {}", taskAlarmItems);
                
                rv.setSuccess(true);
                rv.addReturnValue(String.valueOf(task.getTaskId()));
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASK_ADD);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(task.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(task.getTaskId()));
                operateAuditInfo.setOperParameters(task.toString() + "\n" + taskAlarmItems.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            else
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("UpdateTccTask_%d", task.getTaskId()));
                MDC.put(TccUtil.TASK_ID, task.getTaskId());
                
                boolean resetTaskRS = false;
                try
                {
                    resetTaskRS = Boolean.parseBoolean(request.getParameter("resetTaskRS"));
                }
                catch (Exception e)
                {
                    LOGGER.error("parse resetTaskRS fail!", e);
                }
                
                Operator operator = getOperator();
                //检查修改任务权限
                checkModifyTaskPrivilege(operator, task.getTaskId(), task.getOsUser(), task.getDependTaskIdList());
                
                //获取用户组和任务Id防止数据不一致
                List<String> osUsers = new ArrayList<String>();
                osUsers.add(task.getOsUser());
                List<OSUserGroupServiceEntity> osUGSs = getOperatorMgnt().getOSUserGroupServicesByName(osUsers);
                if (osUGSs.size() > 0)
                {
                    task.setUserGroup(osUGSs.get(0).getUserGroup());
                }
                
                //非系统管理员不允许修改优先级和权重
                if (!operator.isSystemAdmin())
                {
                    task.setPriority(null);
                    task.setWeight(null);
                }
                
                task.setCreator(null);
                
                if (resetTaskRS)
                {
                    //删除任务运行状态
                    getTccPortalService().deleteTaskRunningStates(task.getTaskId());
                    //删除批次运行状态
                    getTccPortalService().deleteBatchRunningStates(task.getTaskId());
                    //删除步骤运行状态
                    getTccPortalService().deleteStepRunningStates(task.getTaskId());
                    LOGGER.info("reset taskRS[taskId={}] sucess.", task.getTaskId());
                    
                }
                
                //更新
                task.setTaskEnableFlag(null);
                task.setTaskState(null);
                LOGGER.info("update Task[{}].", new Object[] {task});
                getTccPortalService().updateTask(task);
                
                //通知更新任务
                Eventor.fireEvent(this, EventType.UPDATE_TASK, task.getTaskId());
                
                rv.setSuccess(true);
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASK_MODIFY);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(task.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(task.getTaskId()));
                operateAuditInfo.setOperParameters(task.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            
            //将depend_Task_Id_List中包含task.taskName的那部分依赖信息替换成task.taskId
            if (!StringUtils.isEmpty(task.getTaskName()) && null != task.getTaskId())
            {
                getTccPortalService().replaceDependTaskName2Id(task);
                rv.setSuccess(true);
                rv.addReturnValue(String.valueOf(task.getTaskId()));
            }
        }
        catch (PrivilegeNotEnoughException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (DuplicateKeyException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.DUPLICATE_KEY);
        }
        catch (Exception e)
        {
            String opt = (null != taskReqAdd && taskReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the taskInfo[{}].", new Object[] {task, e});
        }
        finally
        {
            MDC.remove(TccUtil.TASK_ID);
            task = null;
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
     * 检查新增任务权限
     * @throws Exception 异常
     */
    private void checkAddTaskPrivilege(Operator operator, Long taskId, String osUser, String depends)
        throws Exception
    {
        Operator mOperator = operator;
        List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
        
        if (null == mOperator)
        {
            mOperator = getOperator();
        }
        
        if (null == mOperator || !mOperator.canAdd(osUser))
        {
            priParams.add(new PrivilegeNotEnoughParam(osUser, SubPrivilegeType.ADD));
        }
        //检查依赖关系权限
        checkModifyDepends(priParams, mOperator, taskId, depends);
        
        if (!priParams.isEmpty())
        {
            throw new PrivilegeNotEnoughException(priParams);
        }
    }
    
    /** 
     * 检查修改任务权限
     * @throws Exception 异常
     */
    private void checkModifyTaskPrivilege(Operator operator, Long taskId, String osUser, String depends)
        throws Exception
    {
        Operator mOperator = operator;
        //权限处理,包含三部分权限
        List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
        
        if (null == mOperator)
        {
            mOperator = getOperator();
        }
        //获取原任务的OS用户、依赖关系
        TaskEntity srcTask = getTccPortalService().getTaskInfo(taskId);
        String srcDependRelations = srcTask.getDependTaskIdList();
        
        if (null == mOperator || !mOperator.canModify(srcTask.getOsUser()))
        {
            priParams.add(new PrivilegeNotEnoughParam(srcTask.getOsUser(), SubPrivilegeType.MODIFY));
        }
        
        //修改了OS用户，需要具有src的删除权限以及目标的新增权限
        if (osUser != null && !srcTask.getOsUser().equals(osUser))
        {
            checkModifyServiceTGPri(mOperator, priParams, osUser, srcTask.getOsUser());
        }
        
        //检查修改依赖关系的权限
        List<String> distinct = new ArrayList<String>();
        StringBuilder newAddDepends = new StringBuilder();
        
        if (!StringUtils.isBlank(srcDependRelations))
        {
            String[] ids = srcDependRelations.split(";");
            for (String id : ids)
            {
                if (!StringUtils.isBlank(id))
                {
                    if (!distinct.contains(id))
                    {
                        distinct.add(id);
                    }
                }
            }
        }
        
        if (!StringUtils.isBlank(depends))
        {
            String[] ids = depends.split(";");
            for (String id : ids)
            {
                if (!StringUtils.isBlank(id))
                {
                    if (!distinct.contains(id))
                    {
                        distinct.add(id);
                        newAddDepends.append(id);
                        newAddDepends.append(';');
                    }
                }
            }
        }
        
        //仅对新增的依赖关系检查权限
        if (newAddDepends.length() > 0)
        {
            checkModifyDepends(priParams, mOperator, taskId, newAddDepends.toString());
        }
        
        if (!priParams.isEmpty())
        {
            throw new PrivilegeNotEnoughException(priParams);
        }
    }
    
    /**
     * 检查是否具有修改任务组权限
     */
    private void checkModifyServiceTGPri(Operator operator, List<PrivilegeNotEnoughParam> priParams, String dstOSUser,
        String srcOsUser)
    {
        if (null == operator || !operator.canDelete(srcOsUser))
        {
            priParams.add(new PrivilegeNotEnoughParam(srcOsUser, SubPrivilegeType.DELETE));
        }
        
        if (null == operator || !operator.canAdd(dstOSUser))
        {
            PrivilegeNotEnoughParam param = new PrivilegeNotEnoughParam(dstOSUser, SubPrivilegeType.ADD);
            //防止重复提示
            if (!priParams.contains(param))
            {
                priParams.add(param);
            }
        }
    }
    
    /**
     * 检查是否有修改依赖关系的权限
     */
    private void checkModifyDepends(List<PrivilegeNotEnoughParam> priParams, Operator operator, Long taskId,
        String depends)
        throws Exception
    {
        if (StringUtils.isEmpty(depends))
        {
            return;
        }
        
        //依赖关系列表
        HashMap<Long, DependRelation> dependRelationLst = new HashMap<Long, DependRelation>();
        List<Long> taskIds = new ArrayList<Long>();
        //解析任务依赖依赖关系
        TccUtil.parseDependIdList(taskId, depends, dependRelationLst, taskIds);
        List<TaskEntity> tasks = getTccPortalService().getTaskList(taskIds);
        
        for (TaskEntity taskE : tasks)
        {
            if (null == operator || !operator.canQuery(taskE.getUserGroup()))
            {
                PrivilegeNotEnoughParam param =
                    new PrivilegeNotEnoughParam(taskE.getUserGroup(), SubPrivilegeType.QUERY);
                
                //防止重复提示
                if (!priParams.contains(param))
                {
                    priParams.add(param);
                }
            }
        }
    }
    
    /**
     * 批量重做任务
     * @return 查询成功标志位
     * @throws Exception 统一封装的异常
     */
    public String batchRedoTasks()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //修改线程的名字
        Thread.currentThread().setName(String.format("batchRedoTasks_..."));
        
        String taskInfos = "";
        taskInfos = request.getParameter("taskInfos");
        //是否忽略没有权限的任务
        Boolean filterNoPriTasks = "true".equals(request.getParameter("filterNoPriTasks"));
        
        List<TaskEntity> tasks = new ArrayList<TaskEntity>();
        List<Long> taskIds = new ArrayList<Long>();
        try
        {
            Thread.currentThread().setName(String.format("batchRedoTasks_..."));
            LOGGER.info("call batchRedoTasks(taskInfos),taskInfos is {}",
                new Object[] {TccUtil.truncatEncode(taskInfos)});
            
            if (!StringUtils.isEmpty(taskInfos))
            {
                //修改任务信息
                String[] taskInfoArr = taskInfos.split(";");
                String[] taskFiledArr;
                TaskEntity taskE;
                int index = 0;
                for (String taskInfo : taskInfoArr)
                {
                    if (!StringUtils.isEmpty(taskInfo))
                    {
                        taskFiledArr = taskInfo.split(",");
                        taskE = new TaskEntity();
                        index = 0;
                        taskE.setTaskId(Long.parseLong(taskFiledArr[index]));
                        index++;
                        taskE.setCycleDependFlag(true);
                        index++;
                        taskE.setRedoStartTime(parse2Date(taskFiledArr[index]));
                        index++;
                        taskE.setRedoEndTime(parse2Date(taskFiledArr[index]));
                        index++;
                        taskE.setRedoType(Integer.parseInt(taskFiledArr[index]));
                        index++;
                        taskE.setRedoDayLength(Integer.parseInt(taskFiledArr[index]));
                        tasks.add(taskE);
                        
                        //获取不重复的任务Id集合
                        if (!taskIds.contains(taskE.getTaskId()))
                        {
                            taskIds.add(taskE.getTaskId());
                        }
                    }
                }
                
                //权限处理
                Operator operator = getOperator();
                //获取原任务的任务归属信息
                List<TaskOwnerInfo> srcTaskOwners = getTccPortalService().getTaskOwnersByIds(taskIds);
                List<PrivilegeNotEnoughParam> params = new ArrayList<PrivilegeNotEnoughParam>();
                String osUser;
                PrivilegeNotEnoughParam param;
                
                //没有权限的任务
                List<TaskOwnerInfo> noPriTasks = new ArrayList<TaskOwnerInfo>();
                for (TaskOwnerInfo srcTaskOwner : srcTaskOwners)
                {
                    osUser = srcTaskOwner.getOsUser();
                    //需要具有重做权限
                    if (null == operator || !operator.canBatchRedo(osUser))
                    {
                        noPriTasks.add(srcTaskOwner);
                        param = new PrivilegeNotEnoughParam(osUser, SubPrivilegeType.BATCH_REDO);
                        
                        //避免重复提示
                        if (!params.contains(param))
                        {
                            params.add(param);
                        }
                    }
                }
                
                //过滤掉没有权限的任务周期
                Map<Long, TaskOwnerInfo> noPriTaskMaps = new HashMap<Long, TaskOwnerInfo>();
                if (filterNoPriTasks)
                {
                    for (TaskOwnerInfo noPriTask : noPriTasks)
                    {
                        noPriTaskMaps.put(noPriTask.getTaskId(), noPriTask);
                    }
                }
                else
                {
                    if (!params.isEmpty())
                    {
                        rv.setExtValue(noPriTasks);
                        throw new PrivilegeNotEnoughException(params);
                    }
                }
                
                getTccService().stopScheduleTasks(tasks);
                //更新任务的重做配置
                for (TaskEntity taskEOne : tasks)
                {
                    //忽略相应的任务
                    if (filterNoPriTasks && noPriTaskMaps.containsKey(taskEOne.getTaskId()))
                    {
                        continue;
                    }
                    
                    getTccPortalService().updateTask(taskEOne);
                    
                    //通知更新
                    Eventor.fireEvent(this, EventType.UPDATE_TASK, taskEOne.getTaskId());
                }
                
                //重新初始化任务的周期
                for (TaskEntity taskETwo : tasks)
                {
                    //忽略相应的任务
                    if (filterNoPriTasks && noPriTaskMaps.containsKey(taskETwo.getTaskId()))
                    {
                        continue;
                    }
                    
                    getTccPortalService().reInitTaskRS(taskETwo);
                }
                
                //通知修改范围内的周期
                Eventor.fireEvent(this, EventType.RANGE_REINIT_TASKRS_STATE, tasks);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASK_BATCH_REDO);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setTaskIds(taskIds);
                List<String> serviceIdList = new ArrayList<String>();
                for (TaskOwnerInfo srcTaskOwner : srcTaskOwners)
                {
                    //忽略相应的任务
                    if (filterNoPriTasks && noPriTaskMaps.containsKey(srcTaskOwner.getTaskId()))
                    {
                        continue;
                    }
                    
                    serviceIdList.add(String.valueOf(srcTaskOwner.getServiceId()));
                }
                operateAuditInfo.setServiceIds(serviceIdList);
                operateAuditInfo.setOperParameters(tasks.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (PrivilegeNotEnoughException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
            LOGGER.error("failed to call batchRedoTasks(taskInfos),taskInfos is {}.",
                new Object[] {TccUtil.truncatEncode(taskInfos), e});
        }
        finally
        {
            getTccService().continueScheduleTasks(tasks);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("batchRedoTasks fail!", e1);
        }
        return SUCCESS;
    }
    
    private Date parse2Date(String dateStr)
    {
        if (!StringUtils.isEmpty(dateStr))
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setLenient(false);
            
            try
            {
                return df.parse(dateStr);
            }
            catch (ParseException e)
            {
                LOGGER.error("parse2Date failed! dateStr is [{}].", TccUtil.truncatEncode(dateStr), e);
            }
        }
        
        return new Date();
    }
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqTaskIdJsonObject()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        boolean getAll = false;
        try
        {
            if (!StringUtils.isEmpty(request.getParameter("containAllCol")))
            {
                containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
            }
            
            if (!StringUtils.isEmpty(request.getParameter("getAll")))
            {
                getAll = Boolean.parseBoolean(request.getParameter("getAll"));
            }
        }
        catch (Exception e)
        {
            LOGGER.warn("containAllCol or getAll must be true or false!", e);
        }
        
        //数据过滤
        Operator operator = getOperator();
        List<String> visibleGroups = null;
        if (null != operator)
        {
            if (!getAll)
            {
                visibleGroups = operator.getVisibleGroups();
            }
        }
        else
        {
            visibleGroups = new ArrayList<String>(0);
        }
        
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair(Long.toString(0), "全部"));
        }
        if (null == visibleGroups || !visibleGroups.isEmpty())
        {
            List<TaskEntity> tasks = getTccPortalService().getAllTaskIdNameList(visibleGroups);
            
            KeyValuePair keyValuePair;
            
            for (TaskEntity taskE : tasks)
            {
                keyValuePair = new KeyValuePair(Long.toString(taskE.getTaskId()), taskE.getTaskName());
                keyValuePairList.add(keyValuePair);
            }
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取任务Id与任务名键值对集合
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqTaskIdNames()
        throws Exception
    {
        String result = "true";
        try
        {
            //数据过滤
            Operator operator = getOperator();
            List<String> visibleGroups = null;
            if (null != operator)
            {
                visibleGroups = operator.getVisibleGroups();
            }
            else
            {
                visibleGroups = new ArrayList<String>(0);
            }
            
            List<TaskEntity> taskIdNameList;
            if (null == visibleGroups || !visibleGroups.isEmpty())
            {
                taskIdNameList = getTccPortalService().getAllTaskIdNameList(visibleGroups);
            }
            else
            {
                taskIdNameList = new ArrayList<TaskEntity>(0);
            }
            
            StringBuilder taskIdNames = new StringBuilder();
            for (TaskEntity taskE : taskIdNameList)
            {
                taskIdNames.append(Long.toString(taskE.getTaskId()));
                taskIdNames.append(',');
                taskIdNames.append(taskE.getTaskName());
                taskIdNames.append(';');
            }
            
            result = "true|" + taskIdNames.toString();
        }
        catch (Exception e)
        {
            result = "false";
            LOGGER.error("reqTaskIdNames failed!", e);
        }
        
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 查询选择的任务列表
     * (没有加越权查看检查功能，因为可能存在恶意破坏，导致看到无权查看的任务周期，但是为了效率考虑，我们保证它无法操作即可)
     * @return 查询成功标志位
     */
    public String reqChoosedTaskList()
    {
        try
        {
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            
            String choosedTaskids = null;
            String choosedNeedDependTaskids = null;
            String removeTaskids = null;
            try
            {
                removeTaskids = request.getParameter("removeTaskids");
                choosedTaskids = request.getParameter("choosedTaskids");
                choosedNeedDependTaskids = request.getParameter("choosedNeedDependTaskids");
            }
            catch (Exception e)
            {
                LOGGER.error("parse reqChoosedTaskList fail!", e);
            }
            
            //查询的任务集合
            List<TaskEntity> tasks = new ArrayList<TaskEntity>();
            
            //查询依赖任务id
            if (!StringUtils.isEmpty(choosedNeedDependTaskids))
            {
                String[] ids = choosedNeedDependTaskids.split(";");
                List<Long> dependIds = new ArrayList<Long>();
                for (String taskId : ids)
                {
                    dependIds.add(Long.parseLong(taskId));
                }
                
                tasks.addAll(getTccPortalService().getTaskDeppedTrees(dependIds));
            }
            
            if (!StringUtils.isEmpty(choosedTaskids))
            {
                String[] ids = choosedTaskids.split(";");
                List<Long> taskIds = new ArrayList<Long>();
                for (String taskId : ids)
                {
                    taskIds.add(Long.parseLong(taskId));
                }
                
                List<TaskEntity> tasksTemp = getTccPortalService().getTaskList(taskIds);
                if (null != tasksTemp && !tasksTemp.isEmpty())
                {
                    for (TaskEntity taskE : tasksTemp)
                    {
                        if (!tasks.contains(taskE))
                        {
                            tasks.add(taskE);
                        }
                    }
                }
            }
            
            //从tasks中移除筛选的任务Id集合
            if (!StringUtils.isEmpty(removeTaskids))
            {
                String[] ids = removeTaskids.split(";");
                List<TaskEntity> removeTasks = new ArrayList<TaskEntity>();
                TaskEntity taskTemp;
                for (String taskId : ids)
                {
                    taskTemp = new TaskEntity();
                    taskTemp.setTaskId(Long.parseLong(taskId));
                    removeTasks.add(taskTemp);
                }
                
                for (TaskEntity taskE : removeTasks)
                {
                    if (tasks.contains(taskE))
                    {
                        tasks.remove(taskE);
                    }
                }
            }
            
            //获取不同的任务Id集合
            List<Long> taskIds = new ArrayList<Long>();
            for (TaskEntity taskE : tasks)
            {
                if (!taskIds.contains(taskE.getTaskId()))
                {
                    taskIds.add(taskE.getTaskId());
                }
            }
            
            //权限处理
            Operator operator = getOperator();
            //获取原任务的任务归属信息
            List<TaskOwnerInfo> srcTaskOwners = getTccPortalService().getTaskOwnersByIds(taskIds);
            String userGroup;
            //没有权限的任务
            List<TaskOwnerInfo> noPriTasks = new ArrayList<TaskOwnerInfo>();
            for (TaskOwnerInfo srcTaskOwner : srcTaskOwners)
            {
                userGroup = srcTaskOwner.getUserGroup();
                //需要具有重做权限
                if (!operator.canQuery(userGroup))
                {
                    noPriTasks.add(srcTaskOwner);
                }
            }
            
            Map<Long, TaskOwnerInfo> noPriTaskMaps = new HashMap<Long, TaskOwnerInfo>();
            for (TaskOwnerInfo noPriTask : noPriTasks)
            {
                noPriTaskMaps.put(noPriTask.getTaskId(), noPriTask);
            }
            
            List<TaskEntity> filteredTasks = new ArrayList<TaskEntity>();
            for (TaskEntity taskE : tasks)
            {
                if (noPriTaskMaps.containsKey(taskE.getTaskId()))
                {
                    continue;
                }
                
                filteredTasks.add(taskE);
            }
            
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("noPriTasks", noPriTasks);
            jsonObject.put("total", filteredTasks.size());
            jsonObject.put("rows", filteredTasks);
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        }
        catch (NumberFormatException e1)
        {
            LOGGER.error("reqChoosedTaskList fail", e1);
        }
        catch (NullPointerException e2)
        {
            LOGGER.error("reqChoosedTaskList fail", e2);
        }
        catch (UnsupportedEncodingException e3)
        {
            LOGGER.error("reqChoosedTaskList fail", e3);
        }
        catch (CException e4)
        {
            LOGGER.error("reqChoosedTaskList fail", e4);
        }
        
        return SUCCESS;
    }
    
    /**
     * 由查询条件获取任务
     * @return 查询成功标志位
     * @throws Exception 统一封装的异常
     */
    public String reqDataGridJsonObject()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            TaskSearchEntity entity = new TaskSearchEntity();
            //查询的任务ID列表，用”;“号分隔
            String searchTaskId = request.getParameter("searchTaskId");
            
            //查询周期类型
            String searchCycleType = request.getParameter("searchCycleType");
            
            //查询状态
            if (StringUtils.isNotEmpty(request.getParameter("searchTaskState")))
            {
                entity.setTaskState(Integer.parseInt(request.getParameter("searchTaskState")));
            }
            
            //查询周期类型
            if (StringUtils.isNotEmpty(searchCycleType))
            {
                entity.setCycleType(searchCycleType);
            }
            
            //查询任务类型
            if (StringUtils.isNotEmpty(request.getParameter("searchTaskType")))
            {
                entity.setTaskType(Integer.parseInt(request.getParameter("searchTaskType")));
            }
            
            //查询业务Id
            if (!StringUtils.isEmpty(request.getParameter("searchServiceId")))
            {
                entity.setServiceId(Integer.parseInt(request.getParameter("searchServiceId")));
            }
            
            //查询OS用户
            String searchOsUser = request.getParameter("searchOsUser");
            
            //0 表示全部选择任务ID
            if (!StringUtils.isEmpty(searchTaskId))
            {
                String[] taskIdArr = searchTaskId.split(";");
                //如果searchTaskId为以分号结尾的单任务名,则保留分号，进行精确查询
                if (taskIdArr.length == 1 && searchTaskId.endsWith(";"))
                {
                    entity.getTaskNames().add(URLDecoder.decode(taskIdArr[0], "utf-8") + ";");
                }
                else
                {
                    for (String taskIdStr : taskIdArr)
                    {
                        if (!StringUtils.isEmpty(taskIdStr))
                        {
                            entity.getTaskNames().add(URLDecoder.decode(taskIdStr, "utf-8"));
                        }
                        else
                        {
                            entity.getTaskNames().add("");
                        }
                    }
                }
            }
            else
            {
                entity.getTaskNames().add("");
            }
            
            if (!StringUtils.isEmpty(searchOsUser))
            {
                searchOsUser = new String(searchOsUser.getBytes("ISO8859-1"), "UTF-8");
                searchOsUser = URLDecoder.decode(searchOsUser, "UTF-8");
                entity.setOsUser(searchOsUser);
            }
            
            //数据过滤
            Operator operator = getOperator();
            
            if (null != operator)
            {
                entity.setVisibleGroups(operator.getVisibleGroups());
            }
            else
            {
                entity.setVisibleGroups(new ArrayList<String>(0));
            }
            
            entity.setPageIndex((page - 1) * rows);
            entity.setPageSize(rows);
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            //返回空数据
            if (null != entity.getVisibleGroups() && entity.getVisibleGroups().isEmpty())
            {
                jsonObject.put("total", 0);
                jsonObject.put("rows", new ArrayList<TaskEntity>(0));
            }
            else
            {
                Integer count = getTccPortalService().getTaskListSizeByNames(entity);
                //否则以名字模糊查询
                taskList = getTccPortalService().getTaskListByNames(entity);
                
                jsonObject.put("total", count);
                jsonObject.put("rows", taskList);
            }
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("search Task fail", e);
            throw e;
        }
        catch (IOException e)
        {
            LOGGER.error("search Task fail", e);
            throw e;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("search Task fail", e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * 改变任务状态
     * @return  操作成功标志位
     */
    public String changeTaskState()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("changeTaskState_%d", task.getTaskId()));
            MDC.put(TccUtil.TASK_ID, task.getTaskId());
            
            //权限处理
            Operator operator = getOperator();
            //获取原任务的OS用户
            TaskEntity srcTask = getTccPortalService().getTaskInfo(task.getTaskId());
            
            //需要具有src的启动权限
            if (task.getTaskState() == TaskState.NORMAL)
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
            
            //任务状态  0：正常  1：w未启用
            //更新任务状态为正常，并更新启用标志启用 1：启用
            TaskEntity taskE = new TaskEntity();
            if (task.getTaskState() == TaskState.NORMAL)
            {
                taskE.setTaskId(task.getTaskId());
                taskE.setTaskEnableFlag(true);
                taskE.setTaskState(task.getTaskState());
                //获取操作员
                if (getTccPortalService().startTask(taskE))
                {
                    LOGGER.info("start Task[taskId={}].", task.getTaskId());
                    
                    //通知事件
                    Eventor.fireEvent(this, EventType.START_TASK, task.getTaskId());
                    
                    rv.setSuccess(true);
                    rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                    rv.addReturnValue(operator.getOperatorName());
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.TASK_START);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setServiceIdSingle(String.valueOf(srcTask.getServiceId()));
                    operateAuditInfo.setTaskIdSingle(String.valueOf(task.getTaskId()));
                    operateAuditInfo.setOperParameters(task.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
                else
                {
                    LOGGER.info("because of having started. start Task[taskId={}] fail.", task.getTaskId());
                    rv.setSuccess(true);
                    rv.setReturnValue2PageType(ReturnValue2PageType.HAVE_STARTED);
                    rv.addReturnValue(operator.getOperatorName());
                }
            }
            //更新任务状态为1：stop,并5s后更新启用标志为0：未启用
            else
            {
                //先更新任务状态为1：stop
                taskE.setTaskId(task.getTaskId());
                taskE.setTaskEnableFlag(true);
                taskE.setTaskState(task.getTaskState());
                LOGGER.info("stop Task[taskId={}].", task.getTaskId());
                getTccPortalService().updateTask(taskE);
                
                //通知事件
                Eventor.fireEvent(this, EventType.STOP_TASK, task.getTaskId());
                
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASK_STOP);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(srcTask.getServiceId()));
                operateAuditInfo.setTaskIdSingle(String.valueOf(task.getTaskId()));
                operateAuditInfo.setOperParameters(task.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            
        }
        catch (PrivilegeNotEnoughException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
            String oper = "start";
            if (task.getTaskState() != TaskState.NORMAL)
            {
                oper = "stop";
            }
            LOGGER.error("failed to " + oper + " the task[taskId={}]!", task.getTaskId(), e);
        }
        finally
        {
            MDC.remove(TccUtil.TASK_ID);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("changeTaskState fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 获取任务名
     * @return  操作成功标志位
     * @throws Exception 统一封装的异常
     */
    public String grabTaskName()
        throws Exception
    {
        String result = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = null;
        try
        {
            if (null != request.getParameter("taskId"))
            {
                taskId = Long.parseLong(request.getParameter("taskId"));
            }
        }
        catch (Exception e)
        {
            LOGGER.info("grabTaskName error! taskId is {}.", TccUtil.truncatEncode(request.getParameter("taskId")), e);
        }
        
        try
        {
            
            TaskEntity taskE = getTccPortalService().getTaskInfo(taskId);
            result += "|" + taskE.getTaskName();
        }
        catch (Exception e)
        {
            result = "false";
            LOGGER.error("grab taskName fail! taskId is {}", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return "success";
    }
    
    /**
     * 获取任务名
     * @return  操作成功标志位
     * @throws Exception 统一封装的异常
     */
    public String grabTaskIdNames()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        List<Long> taskIds = new ArrayList<Long>();
        
        if (null != request.getParameter("taskIds"))
        {
            String[] taskIdStrArr = request.getParameter("taskIds").split(";");
            for (String taskIdStr : taskIdStrArr)
            {
                if (!StringUtils.isEmpty(taskIdStr))
                {
                    try
                    {
                        taskIds.add(Long.parseLong(taskIdStr));
                    }
                    catch (Exception e)
                    {
                        LOGGER.warn("grabTaskName error! taskIdStr is {}.", TccUtil.truncatEncode(taskIdStr), e);
                    }
                }
            }
        }
        
        try
        {
            List<TaskEntity> taskEList = getTccPortalService().getTaskList(taskIds);
            
            List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
            KeyValuePair keyValuePair;
            
            for (TaskEntity taskE : taskEList)
            {
                keyValuePair = new KeyValuePair(Long.toString(taskE.getTaskId()), taskE.getTaskName());
                keyValuePairList.add(keyValuePair);
            }
            
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue(JSON.toJSONString(keyValuePairList));
        }
        catch (Exception e)
        {
            LOGGER.error("grab grabTaskIdNames fail! taskIds is {}",
                TccUtil.truncatEncode(request.getParameter("taskIds")),
                e);
        }
        
        setInputStream(new ByteArrayInputStream(JSON.toJSONString(rv).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取所有的目标TCC服务器名字
     * @return 所有的目标TCC服务器名字json格式
     * @throws UnsupportedEncodingException 异常 
     */
    public String getAllTccServers()
        throws UnsupportedEncodingException
    {
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        try
        {
            List<DBServerConfigEntity> dbServers = getTccPortalService().getDBServers();
            KeyValuePair keyValuePair;
            for (DBServerConfigEntity dbServer : dbServers)
            {
                keyValuePair = new KeyValuePair(dbServer.getTccName(), dbServer.getTccName());
                keyValuePairList.add(keyValuePair);
            }
        }
        catch (CException e)
        {
            LOGGER.error("getAllTccServers failed!", e);
        }
        
        setInputStream(new ByteArrayInputStream(JSON.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 执行TCC同步
     * @return 成功状态
     * @throws UnsupportedEncodingException 异常 
     */
    public String syncTcc()
        throws UnsupportedEncodingException
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String dstServer = request.getParameter("dstServer");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String taskIds = request.getParameter("taskIds");
        try
        {
            //ID中心
            if (TccConfig.isTaskIdCenter())
            {
                //同步到测试
                sync2Test.syncTcc(dstServer, userName, password);
            }
            else
            {
                //同步到生产
                List<Long> noSyncTaskIds =
                    sync2Produce.syncTcc(dstServer, userName, password, TccUtil.parseTaskIds(taskIds));
                
                rv.setExtValue(noSyncTaskIds);
            }
        }
        catch (Exception e)
        {
            rv.setSuccess(false);
            if (e instanceof CException)
            {
                rv.setExtValue(((CException)e).getErrorCode());
            }
            else
            {
                rv.setExtValue(ResultCode.SYSTEM_ERROR);
            }
            
            LOGGER.error("getAllTccServers failed!", e);
        }
        finally
        {
            //记录审计信息
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(TccConfig.isTaskIdCenter() ? OperType.SYNC_TEST : OperType.SYNC_PRODUCE);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            operateAuditInfo.setOperParameters(String.format("dstServer=%s,userName=%s,taskIds=%s",
                dstServer,
                userName,
                taskIds));
            getOperationRecord().writeOperLog(operateAuditInfo);
        }
        
        setInputStream(new ByteArrayInputStream(JSON.toJSONString(rv).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 删除任务
     * @return 操作成功的标志位
     */
    public String deleteTask()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("deleteTask_%d", task.getTaskId()));
            MDC.put(TccUtil.TASK_ID, task.getTaskId());
            
            //权限处理
            Operator operator = getOperator();
            //获取原任务的业务和任务组
            TaskEntity srcTask = getTccPortalService().getTaskInfo(task.getTaskId());
            //需要具有src的删除权限
            if (null == operator || !operator.canDelete(srcTask.getOsUser()))
            {
                throw new PrivilegeNotEnoughException(srcTask.getOsUser(), SubPrivilegeType.DELETE);
            }
            
            //删除所有依赖任务Id列表中包含任务Id的依赖关系
            getTccPortalService().deleteDependTaskId(task.getTaskId());
            //删除任务
            getTccPortalService().deleteTask(task.getTaskId());
            //删除任务步骤
            getTccPortalService().deleteTaskSteps(task.getTaskId());
            //删除任务运行状态
            getTccPortalService().deleteTaskRunningStates(task.getTaskId());
            //删除批次运行状态
            getTccPortalService().deleteBatchRunningStates(task.getTaskId());
            //删除步骤运行状态
            getTccPortalService().deleteStepRunningStates(task.getTaskId());
            //删除告警项
            getTccPortalService().deleteAlarmItems(task.getTaskId());
            //删除告警渠道
            getTccPortalService().deleteAlarmChannel(task.getTaskId());
            //删除告警阈值
            getTccPortalService().deleteAlarmThreshold(task.getTaskId());
            
            //如果有，停止正运行的任务周期
            List<Long> taskIds = new ArrayList<Long>();
            taskIds.add(task.getTaskId());
            getTccService().stopCycleTasks(taskIds);
            LOGGER.info("delete all related infos of task[taskId={}] sucess.", task.getTaskId());
            
            //通知删除任务
            Eventor.fireEvent(this, EventType.DELETE_TASK, task.getTaskId());
            //通知删除任务告警阈值
            Eventor.fireEvent(this, EventType.DELETE_ALARM_THRESHOLD, task.getTaskId());
            
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
            //记录审计信息
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.TASK_DELETE);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            operateAuditInfo.setTaskIdSingle(String.valueOf(task.getTaskId()));
            operateAuditInfo.setServiceIdSingle(String.valueOf(srcTask.getServiceId()));
            operateAuditInfo.setOperParameters(task.toString());
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
            LOGGER.error("failed to delete Task[taskId={}].", task.getTaskId(), e);
        }
        finally
        {
            MDC.remove(TccUtil.TASK_ID);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("deleteTask fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 获取启动任务步骤的条数
     * @return 操作成功的标志位
     * @throws CException 统一封装的异常
     */
    public String getStepEnableCount()
        throws CException
    {
        try
        {
            int count = getTccPortalService().getTaskStepEnableSize(task.getTaskId());
            String result = "true" + "," + count;
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
            return "success";
        }
        catch (Exception e)
        {
            LOGGER.error("getStepEnableCount fail", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 是否是系统管理员
     * @return 是否是系统管理员
     */
    public String isSystemAdmin()
    {
        try
        {
            boolean systemAdmin = false;
            Operator operator = getOperator();
            if (null != operator && operator.isSystemAdmin())
            {
                systemAdmin = true;
            }
            setInputStream(new ByteArrayInputStream(Boolean.toString(systemAdmin).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("isSystemAdmin fail!", e);
        }
        
        return SUCCESS;
    }
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     */
    public String getAlarmChannels()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = null;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            
            List<TaskAlarmChannelInfoEntity> alarmChannelList = getTccPortalService().getAlarmChannelList(taskId);
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            if (null == alarmChannelList || alarmChannelList.isEmpty())
            {
                jsonObject.put("total", 0);
                jsonObject.put("rows", new ArrayList<TaskEntity>(0));
            }
            else
            {
                jsonObject.put("total", alarmChannelList.size());
                jsonObject.put("rows", alarmChannelList);
            }
            
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmChannels fail!", e);
        }
        
        return SUCCESS;
    }
}
