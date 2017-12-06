/*
 * 文 件 名:  TccTaskAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.Exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.Exception.PrivilegeNotEnoughParam;
import com.huawei.platform.tcc.PrivilegeControl.Operator;
import com.huawei.platform.tcc.PrivilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.constants.type.SubPrivilegeType;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.domain.DependRelation;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.ServiceTaskGroup;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * TCC任务表处理逻辑
 * 
 * @author  w00190929
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class TccTaskAction extends BaseAction
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccTaskAction.class);
    
    private static final Integer TASK_STATE_ALL = 2;
    
    /**
     * 默认有衔接
     */
    private static final Integer DEFAULT_PRIORITY = 5;
    
    /**
     * 任务信息
     */
    private transient TaskEntity task;
    
    /**
     * 任务信息列表
     */
    private List<TaskEntity> taskList;
    
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
     * 获取用户可操作的业务Id业务名键值对列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getOptServiceIdNameList()
        throws Exception
    {
        
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        Integer serviceId = null;
        String taskGroup = null;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
            String obj = request.getParameter("serviceId");
            if (null != obj)
            {
                serviceId = Integer.parseInt(obj);
            }
            
            String tg = request.getParameter("taskGroup");
            if (!StringUtils.isEmpty(tg))
            {
                taskGroup = new String(tg.getBytes("ISO8859-1"), "UTF-8");
                taskGroup = URLDecoder.decode(taskGroup, "UTF-8");
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOptServiceIdNameList failed!", e);
        }
        
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        Operator operator = getOperator();
        
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        if (null != operator)
        {
            List<ServiceDefinationEntity> services = getOperatorMgnt().getVisibleServices(operator.getOperatorName());
            
            KeyValuePair keyValuePair;
            
            if (containAllCol)
            {
                keyValuePairList.add(new KeyValuePair("-1", "全部"));
            }
            
            //修改
            if (null != serviceId && null != taskGroup)
            {
                //源业务任务组具有删除权限
                if (operator.canDelete(serviceId, taskGroup))
                {
                    for (ServiceDefinationEntity service : services)
                    {
                        //目标业务任务组具有删除权限
                        if (!serviceId.equals(service.getServiceId()) && operator.canAdd(service.getServiceId()))
                        {
                            keyValuePair =
                                new KeyValuePair(Integer.toString(service.getServiceId()), service.getServiceName());
                            keyValuePairList.add(keyValuePair);
                        }
                    }
                }
                
                //获取业务名
                String serviceName = Integer.toString(serviceId);
                for (ServiceDefinationEntity service : services)
                {
                    //目标业务任务组具有删除权限
                    if (serviceId.equals(service.getServiceId()))
                    {
                        serviceName = service.getServiceName();
                    }
                }
                
                keyValuePair = new KeyValuePair(Integer.toString(serviceId), serviceName);
                keyValuePairList.add(keyValuePair);
            }
            else
            {
                for (ServiceDefinationEntity service : services)
                {
                    //仅展示具有新增权限
                    if (operator.canAdd(service.getServiceId()))
                    {
                        keyValuePair =
                            new KeyValuePair(Integer.toString(service.getServiceId()), service.getServiceName());
                        keyValuePairList.add(keyValuePair);
                    }
                }
            }
        }
        
        out.print(JSONObject.toJSONString(keyValuePairList));
        return null;
    }
    
    /**
     * 业务Id业务名键值对列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getVServiceIdNameList()
        throws Exception
    {
        
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
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
        
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String operatorName = OperatorMgnt.getOperatorName(request.getSession());
        List<ServiceDefinationEntity> services = getOperatorMgnt().getVisibleServices(operatorName);
        
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
        
        out.print(JSONObject.toJSONString(keyValuePairList));
        return null;
    }
    
    /**
     * 可操作的业务Id对应的任务组列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getOptServiceTGNameList()
        throws Exception
    {
        
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        boolean returnEmpty = false;
        Integer serviceId = null;
        Integer choosedServiceId = null;
        String taskGroup = null;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
            choosedServiceId = Integer.parseInt(request.getParameter("choosedServiceId"));
            String sid = request.getParameter("serviceId");
            if (null != sid)
            {
                serviceId = Integer.parseInt(sid);
            }
            String tg = request.getParameter("taskGroup");
            if (!StringUtils.isEmpty(tg))
            {
                taskGroup = new String(tg.getBytes("ISO8859-1"), "UTF-8");
                taskGroup = URLDecoder.decode(taskGroup, "UTF-8");
            }
        }
        catch (Exception e)
        {
            returnEmpty = true;
            LOGGER.error("getOptServiceTGNameList error!", e);
        }
        
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        
        Operator operator = getOperator();
        
        if (null == operator)
        {
            returnEmpty = true;
        }
        
        if (!returnEmpty)
        {
            
            List<String> taskGroupNames =
                getOperatorMgnt().getVisibleTaskGroupNames(operator.getOperatorName(), choosedServiceId);
            
            KeyValuePair keyValuePair;
            
            if (containAllCol)
            {
                keyValuePairList.add(new KeyValuePair("-1", "全部"));
            }
            
            //修改
            if (null != choosedServiceId && null != serviceId && null != taskGroup)
            {
                //源业务任务组具有删除权限
                for (String groupName : taskGroupNames)
                {
                    //目标业务任务组具有删除权限
                    if (choosedServiceId.equals(serviceId) && taskGroup.equals(groupName))
                    {
                        //当前业务的当前任务组不需要权限判断
                        keyValuePair = new KeyValuePair(taskGroup, taskGroup);
                        keyValuePairList.add(keyValuePair);
                    }
                    else if (null != operator && operator.canDelete(serviceId, taskGroup)
                        && operator.canAdd(choosedServiceId, groupName))
                    {
                        keyValuePair = new KeyValuePair(groupName, groupName);
                        keyValuePairList.add(keyValuePair);
                    }
                }
            }
            else if (null != choosedServiceId)
            {
                if (null != operator)
                {
                    for (String groupName : taskGroupNames)
                    {
                        //目标业务任务组具有新增权限
                        if (operator.canAdd(choosedServiceId, groupName))
                        {
                            keyValuePair = new KeyValuePair(groupName, groupName);
                            keyValuePairList.add(keyValuePair);
                        }
                    }
                }
            }
            
        }
        
        out.print(JSONObject.toJSONString(keyValuePairList));
        return null;
    }
    
    /**
     * 可见的业务Id对应的任务组列表
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getVServiceTGNameList()
        throws Exception
    {
        
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        boolean returnEmpty = false;
        Integer choosedServiceId = null;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
            choosedServiceId = Integer.parseInt(request.getParameter("choosedServiceId"));
        }
        catch (Exception e)
        {
            returnEmpty = true;
            LOGGER.error("getVServiceTGNameList error!", e);
        }
        
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        
        if (!returnEmpty)
        {
            String operatorName = OperatorMgnt.getOperatorName(request.getSession());
            List<String> taskGroupNames = getOperatorMgnt().getVisibleTaskGroupNames(operatorName, choosedServiceId);
            
            KeyValuePair keyValuePair;
            
            if (containAllCol)
            {
                keyValuePairList.add(new KeyValuePair("-1", "全部"));
            }
            
            for (String groupName : taskGroupNames)
            {
                keyValuePair = new KeyValuePair(groupName, groupName);
                keyValuePairList.add(keyValuePair);
            }
        }
        
        out.print(JSONObject.toJSONString(keyValuePairList));
        return null;
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
     * @see [类、类#方法、类#成员]
     */
    public String saveTccTask()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        try
        {
            if (taskReqAdd)
            {
                //生成任务ID，业务ID＋2位任务类型＋4位编号
                Long taskIDTemp = getTccPortalService().generateTaskID(task.getServiceid(), task.getTaskType());
                //修改线程的名字
                Thread.currentThread().setName(String.format("AddTccTask_%d", taskIDTemp));
                MDC.put(TccUtil.TASK_ID, taskIDTemp);
                
                task.setTaskId(taskIDTemp);
                task.setTaskEnableFlag(false);
                task.setTaskState(TaskState.STOP);
                
                Operator operator = getOperator();
                //检查添加任务权限
                checkAddTaskPrivilege(operator,
                    task.getTaskId(),
                    task.getServiceid(),
                    task.getServiceTaskGroup(),
                    task.getDependTaskIdList());
                
                //非系统管理员不允许修改优先级
                if (!operator.isSystemAdmin())
                {
                    task.setPriority(DEFAULT_PRIORITY);
                }
                
                LOGGER.info("add Task[{}]", new Object[] {task});
                //新增
                getTccPortalService().addTask(task);
                rv.setSuccess(true);
                rv.addReturnValue(String.valueOf(task.getTaskId()));
            }
            else
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("UpdateTccTask_%d", task.getTaskId()));
                MDC.put(TccUtil.TASK_ID, task.getTaskId());
                
                HttpServletRequest request =
                    (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                
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
                checkModifyTaskPrivilege(operator,
                    task.getTaskId(),
                    task.getServiceid(),
                    task.getServiceTaskGroup(),
                    task.getDependTaskIdList());
                
                //非系统管理员不允许修改优先级
                if (!operator.isSystemAdmin())
                {
                    task.setPriority(null);
                }
                
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
                rv.setSuccess(true);
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
    private void checkAddTaskPrivilege(Operator operator, Long taskId, Integer serviceId, String groupName,
        String depends)
        throws Exception
    {
        Operator mOperator = operator;
        List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
        
        if (null == mOperator)
        {
            mOperator = getOperator();
        }
        
        if (null == mOperator || !mOperator.canAdd(serviceId, groupName))
        {
            priParams.add(new PrivilegeNotEnoughParam(getServiceName(serviceId), groupName, SubPrivilegeType.ADD));
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
    private void checkModifyTaskPrivilege(Operator operator, Long taskId, Integer serviceId, String groupName,
        String depends)
        throws Exception
    {
        Operator mOperator = operator;
        //权限处理,包含三部分权限
        List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
        
        if (null == mOperator)
        {
            mOperator = getOperator();
        }
        Integer dstServiceId = serviceId;
        String dstGroupName = groupName;
        //获取原任务的业务和任务组
        TaskEntity srcTask = getTccPortalService().getTaskInfo(taskId);
        Integer srcServiceId = srcTask.getServiceid();
        String srcGroupName = srcTask.getServiceTaskGroup();
        
        //修改了业务或者任务组，需要具有src的删除权限以及目标的新增权限
        if (!srcServiceId.equals(dstServiceId) || !srcGroupName.equals(dstGroupName))
        {
            checkModifyServiceTGPri(mOperator,
                    priParams,
                    dstServiceId,
                    dstGroupName,
                    srcServiceId,
                    srcGroupName);
        }
        else
        {
            if (null == mOperator || !mOperator.canModify(dstServiceId, dstGroupName))
            {
                priParams.add(new PrivilegeNotEnoughParam(getServiceName(dstServiceId), dstGroupName,
                    SubPrivilegeType.MODIFY));
            }
        }
        
        //检查修改依赖关系的权限
        checkModifyDepends(priParams, mOperator, taskId, depends);
        
        if (!priParams.isEmpty())
        {
            throw new PrivilegeNotEnoughException(priParams);
        }
    }

    /**
     * 检查是否具有修改任务组权限
     * @see [类、类#方法、类#成员]
     */
    private void checkModifyServiceTGPri(Operator operator,
            List<PrivilegeNotEnoughParam> priParams, Integer dstServiceId,
            String dstGroupName, Integer srcServiceId, String srcGroupName)
    {
        String srcSN = getServiceName(srcServiceId);
        if (null == operator || !operator.canDelete(srcServiceId, srcGroupName))
        {
            priParams.add(new PrivilegeNotEnoughParam(srcSN, srcGroupName, SubPrivilegeType.DELETE));
        }
        
        String dstSN;
        if (srcServiceId.equals(dstServiceId))
        {
            //已经查询过
            dstSN = srcSN;
        }
        else
        {
            dstSN = getServiceName(dstServiceId);
        }
        
        if (null == operator || !operator.canAdd(dstServiceId, dstGroupName))
        {
            PrivilegeNotEnoughParam param = new PrivilegeNotEnoughParam(dstSN, dstGroupName, SubPrivilegeType.ADD);
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
        //依赖关系列表
        HashMap<Long, DependRelation> dependRelationLst = new HashMap<Long, DependRelation>();
        List<Long> taskIds = new ArrayList<Long>();
        //解析任务依赖依赖关系
        TccUtil.parseDependIdList(taskId, depends, dependRelationLst, taskIds);
        List<TaskEntity> tasks = getTccPortalService().getTaskList(taskIds);
        
        for (TaskEntity taskE : tasks)
        {
            if (null == operator || !operator.canQuery(taskE.getServiceid(), taskE.getServiceTaskGroup()))
            {
                PrivilegeNotEnoughParam param =
                    new PrivilegeNotEnoughParam(getServiceName(task.getServiceid()), taskE.getServiceTaskGroup(),
                        SubPrivilegeType.QUERY);
                
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
        List<TaskEntity> tasks = new ArrayList<TaskEntity>();
        List<Long> taskIds = new ArrayList<Long>();
        try
        {
            Thread.currentThread().setName(String.format("batchRedoTasks_..."));
            LOGGER.info("call batchRedoTasks(taskInfos),taskInfos is {}", new Object[] {taskInfos});
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
                //获取原任务的业务和任务组
                List<TaskEntity> srcTasks = getTccPortalService().getTaskList(taskIds);
                List<PrivilegeNotEnoughParam> params = new ArrayList<PrivilegeNotEnoughParam>();
                Integer srcServiceId;
                String srcGroupName;
                PrivilegeNotEnoughParam param;
                for (TaskEntity srcTask : srcTasks)
                {
                    srcServiceId = srcTask.getServiceid();
                    srcGroupName = srcTask.getServiceTaskGroup();
                    //需要具有重做权限
                    if (null == operator || !operator.canBatchRedo(srcServiceId, srcGroupName))
                    {
                        param =
                            new PrivilegeNotEnoughParam(getServiceName(srcServiceId), srcGroupName,
                                SubPrivilegeType.BATCH_REDO);
                        //避免重复提示
                        if (!params.contains(param))
                        {
                            params.add(param);
                        }
                    }
                }
                
                if (!params.isEmpty())
                {
                    throw new PrivilegeNotEnoughException(params);
                }
                
                getTccService().stopScheduleTasks(tasks);
                //更新任务的重做配置
                for (TaskEntity taskEOne : tasks)
                {
                    getTccPortalService().updateTask(taskEOne);
                }
                
                //重新初始化任务的周期
                for (TaskEntity taskETwo : tasks)
                {
                    getTccPortalService().reInitTaskRS(taskETwo);
                }
                
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
            LOGGER.error("failed to call batchRedoTasks(taskInfos),taskInfos is {}.", new Object[] {taskInfos, e});
        }
        finally
        {
            getTccService().continueScheduleTasks(tasks);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
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
                LOGGER.error("parse2Date failed! dateStr is [{}].", dateStr, e);
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
        
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
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
        
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        //数据过滤
        Operator operator = getOperator();
        List<String> vSTgs = null;
        if (null != operator)
        {
            List<ServiceTaskGroup> visibleSTGs = operator.getVisibleServiceTaskGroup();
            vSTgs = new ArrayList<String>();
            if (null != visibleSTGs)
            {
                for (ServiceTaskGroup sTG : visibleSTGs)
                {
                    vSTgs.add(String.format("%d,%s", sTG.getServiceId(), sTG.getTaskGroup()));
                }
            }
            else
            {
                vSTgs = null;
            }
        }
        else
        {
            vSTgs = new ArrayList<String>(0);
        }
        
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        if (null == vSTgs || !vSTgs.isEmpty())
        {
            List<TaskEntity> tasks = getTccPortalService().getAllTaskIdNameList(vSTgs);
            
            KeyValuePair keyValuePair;
            
            if (containAllCol)
            {
                keyValuePairList.add(new KeyValuePair(Long.toString(0), "全部"));
            }
            
            for (TaskEntity taskE : tasks)
            {
                keyValuePair = new KeyValuePair(Long.toString(taskE.getTaskId()), taskE.getTaskName());
                keyValuePairList.add(keyValuePair);
            }
        }
        
        out.print(JSONObject.toJSONString(keyValuePairList));
        return null;
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
            List<ServiceTaskGroup> visibleSTGs = operator.getVisibleServiceTaskGroup();
            List<String> vSTgs = new ArrayList<String>();
            if (null != visibleSTGs)
            {
                for (ServiceTaskGroup sTG : visibleSTGs)
                {
                    vSTgs.add(String.format("%d,%s", sTG.getServiceId(), sTG.getTaskGroup()));
                }
            }
            else
            {
                vSTgs = null;
            }
            
            List<TaskEntity> taskIdNameList;
            if (null == visibleSTGs || !visibleSTGs.isEmpty())
            {
                taskIdNameList = getTccPortalService().getAllTaskIdNameList(vSTgs);
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
     * @throws CException 统一封装的异常
     */
    public String reqChoosedTaskList()
        throws CException
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
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
            
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", tasks.size());
            jsonObject.put("rows", tasks);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("reqChoosedTaskList fail", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 由查询条件获取任务
     * @return 查询成功标志位
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String reqDataGridJsonObject()
        throws CException
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            //查询的任务ID列表，用”;“号分隔
            String searchTaskId = request.getParameter("searchTaskId");
            //查询状态
            Integer searchTaskState = Integer.parseInt(request.getParameter("searchTaskState"));
            //查询周期类型
            String searchCycleType = request.getParameter("searchCycleType");
            //查询任务类型
            Integer searchTaskType = Integer.parseInt(request.getParameter("searchTaskType"));
            //查询业务Id
            Integer searchServiceId = null;
            if (!StringUtils.isEmpty(request.getParameter("searchServiceId")))
            {
                searchServiceId = Integer.parseInt(request.getParameter("searchServiceId"));
            }
            
            //查询业务组
            String searchTaskGroup = request.getParameter("searchTaskGroup");
            TaskSearchEntity entity = new TaskSearchEntity();
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
            
            //2 表示状态全部选择
            if (null != searchTaskId && !TASK_STATE_ALL.equals(searchTaskState))
            {
                entity.setTaskState(searchTaskState);
            }
            //0 表示周期类型全部选择
            if (StringUtils.isNotBlank(searchCycleType) && (!"0".equals(searchCycleType)))
            {
                entity.setCycleType(searchCycleType);
            }
            //0 表示任务类型全部选择
            if (null != searchTaskType && searchTaskType != 0)
            {
                entity.setTaskType(searchTaskType);
            }
            
            if (null != searchServiceId && searchServiceId != -1)
            {
                entity.setServiceId(searchServiceId);
            }
            
            if (!StringUtils.isEmpty(searchTaskGroup))
            {
                searchTaskGroup = new String(searchTaskGroup.getBytes("ISO8859-1"), "UTF-8");
                searchTaskGroup = URLDecoder.decode(searchTaskGroup, "UTF-8");
                entity.setTaskGroup(searchTaskGroup);
            }
            
            //数据过滤
            Operator operator = getOperator();
            List<ServiceTaskGroup> visibleSTGs;
            
            if (null != operator)
            {
                visibleSTGs = operator.getVisibleServiceTaskGroup();
                List<String> vSTgs = new ArrayList<String>();
                if (null != visibleSTGs)
                {
                    for (ServiceTaskGroup sTG : visibleSTGs)
                    {
                        vSTgs.add(String.format("%d,%s", sTG.getServiceId(), sTG.getTaskGroup()));
                    }
                    entity.setVisibleSTgs(vSTgs);
                }
                else
                {
                    entity.setVisibleSTgs(null);
                }
            }
            else
            {
                entity.setVisibleSTgs(new ArrayList<String>(0));
            }
            
            entity.setPageIndex((page - 1) * rows);
            entity.setPageSize(rows);
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            //返回空数据
            if (null != entity.getVisibleSTgs() && entity.getVisibleSTgs().isEmpty())
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
            out.print(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("search Task fail", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 改变任务状态
     * @return  操作成功标志位
     * @see [类、类#方法、类#成员]
     */
    public String changeTaskState()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("changeTaskState_%d", task.getTaskId()));
            MDC.put(TccUtil.TASK_ID, task.getTaskId());
            
            //权限处理
            Operator operator = getOperator();
            //获取原任务的业务和任务组
            TaskEntity srcTask = getTccPortalService().getTaskInfo(task.getTaskId());
            
            Integer srcServiceId = srcTask.getServiceid();
            String srcGroupName = srcTask.getServiceTaskGroup();
            
            //需要具有src的启动权限
            if (task.getTaskState() == TaskState.NORMAL)
            {
                if (null == operator || !operator.canStart(srcServiceId, srcGroupName))
                {
                    throw new PrivilegeNotEnoughException(getServiceName(srcServiceId), srcGroupName,
                        SubPrivilegeType.START);
                }
            }
            else
            {
                if (null == operator || !operator.canStop(srcServiceId, srcGroupName))
                {
                    throw new PrivilegeNotEnoughException(getServiceName(srcServiceId), srcGroupName,
                        SubPrivilegeType.STOP);
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
                if (null != operator && !StringUtils.isEmpty(operator.getOperatorName())
                    && !StringUtils.isEmpty(operator.getOsUserName()))
                {
                    taskE.setStartOperator(operator.getOperatorName());
                    taskE.setOsUserName(operator.getOsUserName());
                    
                    if (getTccPortalService().startTask(taskE))
                    {
                        LOGGER.info("start Task[taskId={}].", task.getTaskId());
                        rv.setSuccess(true);
                        rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                        rv.addReturnValue(operator.getOperatorName());
                    }
                    else
                    {
                        LOGGER.info("because of having started. start Task[taskId={}] fail.", task.getTaskId());
                        rv.setSuccess(true);
                        rv.setReturnValue2PageType(ReturnValue2PageType.HAVE_STARTED);
                        rv.addReturnValue(operator.getOperatorName());
                    }
                }
                else
                {
                    rv.setSuccess(false);
                    rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
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
                
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
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
     * @see [类、类#方法、类#成员]
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
            LOGGER.info("grabTaskName error! taskId is {}.", request.getParameter("taskId"), e);
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
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return "success";
    }
    
    /**
     * 删除任务
     * @return 操作成功的标志位
     * @see [类、类#方法、类#成员]
     */
    public String deleteTask()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("deleteTask_%d", task.getTaskId()));
            MDC.put(TccUtil.TASK_ID, task.getTaskId());
            
            //权限处理
            Operator operator = getOperator();
            //获取原任务的业务和任务组
            TaskEntity srcTask = getTccPortalService().getTaskInfo(task.getTaskId());
            Integer srcServiceId = srcTask.getServiceid();
            String srcGroupName = srcTask.getServiceTaskGroup();
            //需要具有src的删除权限
            if (null == operator || !operator.canModify(srcServiceId, srcGroupName))
            {
                throw new PrivilegeNotEnoughException(getServiceName(srcServiceId), srcGroupName,
                    SubPrivilegeType.DELETE);
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
            LOGGER.info("delete all related infos of task[taskId={}] sucess.", task.getTaskId());
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
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
     * @see [类、类#方法、类#成员]
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
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
}
