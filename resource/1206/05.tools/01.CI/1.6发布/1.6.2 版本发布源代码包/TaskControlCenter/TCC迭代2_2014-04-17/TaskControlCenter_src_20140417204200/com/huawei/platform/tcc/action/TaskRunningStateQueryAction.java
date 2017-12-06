/*
 * 文 件 名:  TaskRunningDetailAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.RedoType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.constants.type.SubPrivilegeType;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.TaskOwnerInfo;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughParam;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务周期查询
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-17]
 */
public class TaskRunningStateQueryAction extends BaseAction
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = -7733674925999090932L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunningStateQueryAction.class);
    
    private transient TaskRunningStateEntity taskRs;
    
    private transient List<String> cycleIdList;
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 异步获获取 canIntegratedTaskInfos字段
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String grabCanIntegratedTaskInfos()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String taskIds = "";
        try
        {
            taskIds = request.getParameter("taskIds");
        }
        catch (Exception e)
        {
            LOGGER.error("grabCanIntegratedTaskInfos error!", e);
        }
        
        List<Long> needTaskIdList = new ArrayList<Long>();
        
        if (!StringUtils.isEmpty(taskIds))
        {
            String[] taskIdArr = taskIds.split(";");
            Long taskId;
            for (String tasIdStr : taskIdArr)
            {
                if (!StringUtils.isEmpty(tasIdStr))
                {
                    taskId = Long.parseLong(tasIdStr);
                    needTaskIdList.add(taskId);
                }
            }
        }
        
        //返回可以使用集成执行功能的任务信息，形如"0010000,d,1,1;0010001,h,1,1;",解释"任务Id,周期类型,周期长度,是否顺序依赖"
        String returnValue = "true|" + filterCanIntegratedTaskInfos(needTaskIdList);
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 异步获取 非停止的任务Id集合
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String grabNormalTaskIds()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String taskIds = "";
        try
        {
            taskIds = request.getParameter("taskIds");
        }
        catch (Exception e)
        {
            LOGGER.error("grabNormalTaskInfos error!", e);
        }
        
        List<Long> needTaskIdList = new ArrayList<Long>();
        
        if (!StringUtils.isEmpty(taskIds))
        {
            String[] taskIdArr = taskIds.split(";");
            Long taskId;
            for (String tasIdStr : taskIdArr)
            {
                if (!StringUtils.isEmpty(tasIdStr))
                {
                    taskId = Long.parseLong(tasIdStr);
                    needTaskIdList.add(taskId);
                }
            }
        }
        
        //返回可以使用集成执行功能的任务信息，形如"0010000,d,1,true,0;0010001,h,1,false,1;",解释"任务Id,周期类型,周期长度,是否顺序依赖,任务启停状态"
        String returnValue = "true|" + filterNormalTaskIds(needTaskIdList);
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取任务依赖树的任务集合
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String grabTaskDependedIds()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String taskIds = "";
        try
        {
            taskIds = request.getParameter("taskIds");
            List<Long> taskIdLst = new ArrayList<Long>();
            if (!StringUtils.isEmpty(taskIds))
            {
                String[] taskIdArr = taskIds.split(";");
                Long taskId;
                for (String tasIdStr : taskIdArr)
                {
                    if (!StringUtils.isEmpty(tasIdStr))
                    {
                        taskId = Long.parseLong(tasIdStr);
                        
                        if (!taskIdLst.contains(taskId))
                        {
                            taskIdLst.add(taskId);
                        }
                    }
                }
            }
            
            List<TaskEntity> taskList = getTccPortalService().getTaskDeppedTrees(taskIdLst);
            
            //权限处理
            Operator operator = getOperator();
            //获取原任务的业务和任务组
            List<TaskEntity> srcTasks = taskList;
            List<PrivilegeNotEnoughParam> params = new ArrayList<PrivilegeNotEnoughParam>();
            String srcGroupName;
            PrivilegeNotEnoughParam param;
            for (TaskEntity srcTask : srcTasks)
            {
                srcGroupName = srcTask.getUserGroup();
                //需要具有重做权限
                if (null == operator || !operator.canQuery(srcGroupName))
                {
                    param = new PrivilegeNotEnoughParam(srcGroupName, SubPrivilegeType.QUERY);
                    
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
            
            StringBuilder taskIdRtns = new StringBuilder();
            for (TaskEntity taskE : taskList)
            {
                taskIdRtns.append(taskE.getTaskId());
                taskIdRtns.append(";");
            }
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue(taskIdRtns.toString());
        }
        catch (PrivilegeNotEnoughException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("grabTaskDependedIds error!", e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("grabTaskDependedIds fail!", e1);
        }
        
        return SUCCESS;
    }
    
    private String filterNormalTaskIds(List<Long> taskIds)
    {
        StringBuilder sbTaskInfo = new StringBuilder();
        List<TaskEntity> taskList = null;
        try
        {
            taskList = getTccPortalService().getTaskList(taskIds);
            String taskInfo;
            for (TaskEntity task : taskList)
            {
                //集成功能仅仅对日和小时周期类型的非批次任务有效
                if (null != task.getTaskState() && task.getTaskState().equals(TaskState.NORMAL))
                {
                    taskInfo = String.format("%d;", task.getTaskId());
                    sbTaskInfo.append(taskInfo);
                }
            }
        }
        catch (CException e)
        {
            LOGGER.error("getTaskList failed!, taskIds is {}", taskIds, e);
        }
        
        return sbTaskInfo.toString();
    }
    
    private String filterCanIntegratedTaskInfos(List<Long> taskIds)
    {
        StringBuilder sbTaskInfo = new StringBuilder();
        List<TaskEntity> taskList = null;
        try
        {
            taskList = getTccPortalService().getTaskList(taskIds);
            String taskInfo;
            for (TaskEntity task : taskList)
            {
                //集成功能仅仅对日和小时周期类型的非批次任务有效
                int cycleType = CycleType.toCycleType(task.getCycleType());
                if (!task.getMultiBatchFlag()
                    && (cycleType == CycleType.DAY || cycleType == CycleType.HOUR || cycleType == CycleType.MINUTE))
                {
                    taskInfo =
                        String.format("%d,%s,%d,%b;",
                            task.getTaskId(),
                            task.getCycleType(),
                            task.getCycleLength(),
                            task.getCycleDependFlag());
                    sbTaskInfo.append(taskInfo);
                }
            }
        }
        catch (CException e)
        {
            LOGGER.error("getTaskList failed!, taskIds is {}", taskIds, e);
        }
        
        return sbTaskInfo.toString();
    }
    
    /**
     * 单次提交选择的任务周期集合
     * 
     * @return 操作成功标志符
     */
    public String redoAll()
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String choosedRedoTaskRSs = request.getParameter("choosedRedoTaskRSs");
        //是否忽略没有权限的任务
        Boolean filterNoPriTasks = "true".equals(request.getParameter("filterNoPriTasks"));
        //修改线程的名字
        Thread.currentThread().setName(String.format("redoAll_..."));
        StringBuilder sbCycles = new StringBuilder();
        if (!StringUtils.isEmpty(choosedRedoTaskRSs))
        {
            //重做任务周期列表
            try
            {
                //将choosedRedoTaskRSs转换成任务运行状态实体列表
                long taskId;
                String cycleId;
                String[] taskIdCycleIdState;
                List<TaskRunningStateEntity> taskRSList = new ArrayList<TaskRunningStateEntity>();
                List<Long> taskIds = new ArrayList<Long>();
                TaskRunningStateEntity taskRSE;
                if (0 != choosedRedoTaskRSs.length())
                {
                    String[] taskCycleStates = choosedRedoTaskRSs.split(";");
                    for (String taskCycleState : taskCycleStates)
                    {
                        if (!StringUtils.isEmpty(taskCycleState))
                        {
                            taskIdCycleIdState = taskCycleState.split(",");
                            taskId = Long.parseLong(taskIdCycleIdState[0]);
                            cycleId = taskIdCycleIdState[1];
                            taskRSE = new TaskRunningStateEntity();
                            taskRSE.setTaskId(taskId);
                            taskRSE.setCycleId(cycleId);
                            
                            if (!taskRSList.contains(taskRSE))
                            {
                                taskRSList.add(taskRSE);
                                
                                //获取不重复的任务Id集合
                                if (!taskIds.contains(taskRSE.getTaskId()))
                                {
                                    taskIds.add(taskRSE.getTaskId());
                                }
                            }
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
                //过滤后的重做周期集合
                List<TaskRunningStateEntity> filterTaskRSs = new ArrayList<TaskRunningStateEntity>();
                if (filterNoPriTasks)
                {
                    Map<Long, TaskOwnerInfo> noPriTaskMaps = new HashMap<Long, TaskOwnerInfo>();
                    for (TaskOwnerInfo noPriTask : noPriTasks)
                    {
                        noPriTaskMaps.put(noPriTask.getTaskId(), noPriTask);
                    }
                    
                    //过滤掉没有权限的周期
                    for (TaskRunningStateEntity taskRS : taskRSList)
                    {
                        if (!noPriTaskMaps.containsKey(taskRS.getTaskId()))
                        {
                            filterTaskRSs.add(taskRS);
                            
                            //格式化任务周期的输出
                            sbCycles.append(taskRS.getTaskId());
                            sbCycles.append(',');
                            sbCycles.append(taskRS.getCycleId());
                            sbCycles.append(';');
                        }
                    }
                }
                else
                {
                    if (!params.isEmpty())
                    {
                        rv.setExtValue(noPriTasks);
                        throw new PrivilegeNotEnoughException(params);
                    }
                    
                    filterTaskRSs = taskRSList;
                }
                
                LOGGER.info("redo taskCycleList[{}].", TccUtil.truncatEncode(sbCycles.toString()));
                getTccService().redoTaskCycles(filterTaskRSs, false);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASKCYCLE_BATCH_REDO);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                List<String> serviceIdList = new ArrayList<String>();
                for (TaskOwnerInfo srcTaskOwner : srcTaskOwners)
                {
                    serviceIdList.add(String.valueOf(srcTaskOwner.getServiceId()));
                }
                operateAuditInfo.setServiceIds(serviceIdList);
                operateAuditInfo.setTaskIds(taskIds);
                operateAuditInfo.setOperParameters(sbCycles.toString());
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
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                LOGGER.error("failed to redo taskCycleList[{}].", TccUtil.truncatEncode(sbCycles.toString()), e);
            }
            
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
            LOGGER.error("redoAll fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 重做单个任务周期集合
     * 
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String redoTaskCycle()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = 0L;
        String cId = "";
        
        try
        {
            cId = request.getParameter("cycleId");
            taskId = Long.parseLong(request.getParameter("taskId"));
        }
        catch (Exception e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
            LOGGER.error("failed to redo taskCycle[taskId={},cycleId={}].",
                new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")), TccUtil.truncatEncode(cId), e});
        }
        
        //重做任务周期列表
        List<TaskRunningStateEntity> taskRSList = new ArrayList<TaskRunningStateEntity>();
        TaskRunningStateEntity taskRSE = new TaskRunningStateEntity();
        taskRSE.setTaskId(taskId);
        taskRSE.setCycleId(cId);
        taskRSList.add(taskRSE);
        
        //重做任务周期列表
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("redoTaskCycle_%d_%s", taskId, cId));
            MDC.put(TccUtil.TASK_ID, taskId);
            MDC.put(TccUtil.CYCLE_ID, cId);
            
            if (rv.isSuccess())
            {
                //权限处理
                Operator operator = getOperator();
                //获取原任务的业务和任务组
                TaskEntity srcTask = getTccPortalService().getTaskInfo(taskId);
                Integer srcServiceId = srcTask.getServiceId();
                String srcGroupName = srcTask.getOsUser();
                //需要具有重做权限
                if (null == operator || !operator.canRedo(srcGroupName))
                {
                    throw new PrivilegeNotEnoughException(srcGroupName, SubPrivilegeType.REDO);
                }
                
                LOGGER.info("redo taskCycle[{},{}].",
                    new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")), TccUtil.truncatEncode(cId)});
                getTccService().redoTaskCycles(taskRSList, false);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASKCYCLE_REDO);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(String.valueOf(srcServiceId));
                operateAuditInfo.setTaskIdSingle(String.valueOf(taskId));
                operateAuditInfo.setOperParameters(taskRSE.toString());
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
            LOGGER.error("failed to redo taskCycle[{},{}].",
                new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")), TccUtil.truncatEncode(cId), e});
        }
        finally
        {
            MDC.remove(TccUtil.TASK_ID);
            MDC.remove(TccUtil.CYCLE_ID);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("redoTaskCycle fail", e1);
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
     * 任务批次运行状态列表数据json格式
     * (没有加越权查看检查功能，因为可能存在恶意破坏，导致看到无权查看的任务周期，但是为了效率考虑，我们保证它无法操作即可)
     * @return 任务批次运行状态列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getChoosedTaskRSList2Json()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        int cols = 1;
        String choosedTaskRSNeedDependss = "";
        String choosedTaskRSs = "";
        Integer maxCount = 0;
        boolean refresh = false;
        try
        {
            choosedTaskRSs = request.getParameter("ctrss");
            choosedTaskRSNeedDependss = request.getParameter("ctrsnds");
            cols = Integer.parseInt(request.getParameter("cols"));
            String refreshPara = request.getParameter("refresh");
            maxCount = Integer.parseInt(request.getParameter("maxcount"));
            if (null != refreshPara)
            {
                refresh = Boolean.parseBoolean(refreshPara);
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getChoosedTaskRSList2Json error!", e);
        }
        
        //赋值
        JSONObject jsonObject = new JSONObject();
        //请求页面警告，查询的任务不完整
        boolean alarmNotEnough = false;
        long taskId;
        String cycleId;
        String[] taskIdCycleIdState;
        List<TaskRunningStateEntity> taskRSListOnce;
        Set<TaskRunningStateEntity> filterTaskRSs = new HashSet<TaskRunningStateEntity>();
        TaskRunningStateEntity taskRSE = new TaskRunningStateEntity();
        Set<Long> filterTaskIds = new HashSet<Long>();
        Set<String> groups = new HashSet<String>();
        //权限处理
        Operator operator = getOperator();
        List<String> vGroups = operator.getVisibleGroups();
        //将list转换为set，提高效率
        if (null != vGroups)
        {
            for (String group : vGroups)
            {
                groups.add(group);
            }
        }
        
        if (0 != choosedTaskRSNeedDependss.length())
        {
            String[] taskCycleStates = choosedTaskRSNeedDependss.split(";");
            for (String taskCycleState : taskCycleStates)
            {
                if (!StringUtils.isEmpty(taskCycleState))
                {
                    taskIdCycleIdState = taskCycleState.split(",");
                    taskId = Long.parseLong(taskIdCycleIdState[0]);
                    cycleId = taskIdCycleIdState[1];
                    taskRSE.setTaskId(taskId);
                    taskRSE.setCycleId(cycleId);
                    
                    //如果taskRSList包含了当前的任务周期，所有依赖关系已经获取，不用继续查找它的子树
                    if (!filterTaskRSs.contains(taskRSE))
                    {
                        //taskRSListOnce = getTccService().getAllDependedRSList(taskId, cycleId);
                        taskRSListOnce =
                            getTccService().getAllDependedRSListRealTime(taskId,
                                cycleId,
                                maxCount,
                                operator,
                                filterTaskIds);
                        //depTaskRSs可能会有重复的任务周期节点(用set去掉重复)
                        filterTaskRSs.addAll(taskRSListOnce);
                        
                        if (filterTaskRSs.size() >= maxCount)
                        {
                            alarmNotEnough = true;
                            break;
                        }
                    }
                }
            }
        }
        
        List<TaskRunningStateEntity> choosedTaskRSList = covert2TaskRSs(choosedTaskRSs, filterTaskIds);
        //任务Id集合
        List<Long> taskIds = new ArrayList<Long>();
        taskIds.addAll(filterTaskIds);
        //获取原任务的任务归属信息
        List<TaskOwnerInfo> taskOwners = getTccPortalService().getTaskOwnersByIds(taskIds);
        String userGroup;
        //没有权限的任务
        List<TaskOwnerInfo> noPriTasks = new ArrayList<TaskOwnerInfo>();
        //重用filterTaskIds集合
        filterTaskIds.clear();
        for (TaskOwnerInfo taskOwner : taskOwners)
        {
            userGroup = taskOwner.getUserGroup();
            //需要具有查询权限
            if (!operator.canQuery(userGroup))
            {
                noPriTasks.add(taskOwner);
                filterTaskIds.add(taskOwner.getTaskId());
            }
        }
        
        //过滤后的重做周期集合
        List<TaskRunningStateEntity> fChoosedTaskRSs = new ArrayList<TaskRunningStateEntity>();
        
        //过滤掉没有权限的周期
        for (TaskRunningStateEntity taskRS : choosedTaskRSList)
        {
            if (!filterTaskIds.contains(taskRS.getTaskId()))
            {
                fChoosedTaskRSs.add(taskRS);
            }
        }
        
        if (refresh)
        {
            //重新获取任务运行状态
            fChoosedTaskRSs = getTccPortalService().getTaskRunningStateList(fChoosedTaskRSs);
        }
        
        //依赖任务周期集合已经过滤过
        filterTaskRSs.addAll(fChoosedTaskRSs);
        
        //filterTaskRSs已经去掉重复，现在需要排序
        List<TaskRunningStateEntity> sortedTaskRss = new ArrayList<TaskRunningStateEntity>();
        sortedTaskRss.addAll(filterTaskRSs);
        Collections.sort(sortedTaskRss);
        
        List<Map<String, String>> taskRSMaplst = getMapList(sortedTaskRss, cols);
        
        jsonObject.put("noPriTasks", noPriTasks);
        jsonObject.put("alarmNotEnough", alarmNotEnough);
        jsonObject.put("total", sortedTaskRss.size());
        jsonObject.put("rows", taskRSMaplst);
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(jsonObject,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    //重新获取任务运行状态
    private List<TaskRunningStateEntity> covert2TaskRSs(String distinctTaskRSs, Set<Long> taskIds)
        throws CException
    {
        String[] taskCycleStates = distinctTaskRSs.split(";");
        long taskId;
        String cycleId;
        Integer state;
        String[] taskIdCycleIdState;
        TaskRunningStateEntity taskRSE;
        List<TaskRunningStateEntity> taskRSList = new ArrayList<TaskRunningStateEntity>();
        for (String taskCycleState : taskCycleStates)
        {
            if (!StringUtils.isEmpty(taskCycleState))
            {
                taskIdCycleIdState = taskCycleState.split(",");
                taskId = Long.parseLong(taskIdCycleIdState[0]);
                cycleId = taskIdCycleIdState[1];
                state = Integer.parseInt(taskIdCycleIdState[1 + 1]);
                taskRSE = new TaskRunningStateEntity();
                taskRSE.setTaskId(taskId);
                taskRSE.setCycleId(cycleId);
                taskRSE.setState(state);
                taskRSList.add(taskRSE);
                
                taskIds.add(taskId);
            }
        }
        
        return taskRSList;
    }
    
    /**
     * 任务批次运行状态列表数据json格式
     * 
     * @return 任务批次运行状态列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String queryTaskRSList2Json()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        int cols = 1;
        Date startTime = null;
        Date endTime = null;
        String taskName = null;
        String osUser = null;
        boolean returnEmpty = false;
        TaskRSQueryParam taskRSQueryParam = new TaskRSQueryParam();
        try
        {
            if (!StringUtils.isEmpty(request.getParameter("state")))
            {
                taskRSQueryParam.setState(Integer.parseInt(request.getParameter("state")));
            }
            
            if (!StringUtils.isEmpty(request.getParameter("serviceId")))
            {
                taskRSQueryParam.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            }
            
            osUser = request.getParameter("osUser");
            if (!StringUtils.isEmpty(osUser))
            {
                osUser = new String(osUser.getBytes("ISO8859-1"), "UTF-8");
                osUser = URLDecoder.decode(osUser, "UTF-8");
                taskRSQueryParam.setOsUser(osUser);
            }
            
            startTime = toDate(request.getParameter("startCycleID"));
            endTime = toDate(request.getParameter("endCycleID"));
            taskName = request.getParameter("taskName");
            
            if (!StringUtils.isEmpty(taskName))
            {
                String[] taskNameArr = taskName.split(";");
                //如果searchTaskId为以分号结尾的单任务名,则保留分号，进行精确查询
                if (taskNameArr.length == 1 && taskName.endsWith(";"))
                {
                    taskRSQueryParam.getTaskNames().add(URLDecoder.decode(taskNameArr[0], "utf-8") + ";");
                }
                else
                {
                    for (String taskNameStr : taskNameArr)
                    {
                        if (!StringUtils.isEmpty(taskNameStr))
                        {
                            taskRSQueryParam.getTaskNames().add(URLDecoder.decode(taskNameStr, "utf-8"));
                        }
                        else
                        {
                            taskRSQueryParam.getTaskNames().add("");
                        }
                    }
                }
            }
            else
            {
                taskRSQueryParam.setTaskNames(null);
            }
            
            taskRSQueryParam.setStartIndex((long)((page - 1) * rows));
            taskRSQueryParam.setRows(rows);
            cols = Integer.parseInt(request.getParameter("cols"));
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("taskRSQueryParam error!", e);
            returnEmpty = true;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("taskRSQueryParam error!", e);
            returnEmpty = true;
        }
        
        taskRSQueryParam.setStartCycleId(getStartCycleId(startTime));
        taskRSQueryParam.setEndCycleID(TccUtil.covDate2CycleID(endTime));
        
        //数据过滤
        Operator operator = getOperator();
        if (null != operator)
        {
            taskRSQueryParam.setVisibleGroups(operator.getVisibleGroups());
        }
        else
        {
            taskRSQueryParam.setVisibleGroups(new ArrayList<String>(0));
        }
        
        JSONObject jsonObject = new JSONObject();
        if (!returnEmpty
            && (null == taskRSQueryParam.getVisibleGroups() || !taskRSQueryParam.getVisibleGroups().isEmpty()))
        {
            Integer count = getTccPortalService().getTaskRSCount(taskRSQueryParam);
            List<TaskRunningStateEntity> taskRSList = getTccPortalService().getTaskRSList(taskRSQueryParam);
            List<Map<String, String>> taskRSMaplst = getMapList(taskRSList, cols);
            jsonObject.put("total", count);
            jsonObject.put("rows", taskRSMaplst);
        }
        else
        {
            jsonObject.put("total", 0);
            jsonObject.put("rows", new ArrayList<Map<String, String>>(0));
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(jsonObject,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    private List<Map<String, String>> getMapList(List<TaskRunningStateEntity> taskRSLst, int cols)
    {
        if (null == taskRSLst || taskRSLst.isEmpty())
        {
            return new ArrayList<Map<String, String>>(0);
        }
        
        List<Map<String, String>> taskRSMaplst = new ArrayList<Map<String, String>>(0);
        
        Map<String, String> taskRsMap = null;
        
        int index = 0;
        int rowId = 0;
        TaskRunningStateEntity taskRS;
        long preTaskId = 0;
        int i = 0;
        while (i < taskRSLst.size())
        {
            taskRS = taskRSLst.get(i);
            if (0 == index)
            {
                taskRsMap = new HashMap<String, String>();
                taskRsMap.put("rowId", Integer.toString(rowId));
                rowId++;
            }
            
            if (preTaskId != taskRS.getTaskId())
            {
                //添加任务Id行
                if (0 == index)
                {
                    taskRsMap.put("col" + index, String.format("%d", taskRS.getTaskId()));
                    index++;
                    fillNullCol(taskRsMap, index, cols);
                    preTaskId = taskRS.getTaskId();
                }
                else
                {
                    //将尾部补充空列
                    fillNullCol(taskRsMap, index, cols);
                }
                
                index = cols - 1;
            }
            else
            {
                taskRsMap.put("col" + index,
                    String.format("%d,%s,%d", taskRS.getTaskId(), taskRS.getCycleId(), taskRS.getState()));
                preTaskId = taskRS.getTaskId();
                i++;
            }
            
            //行已经处理完毕，添加
            if (index == cols - 1)
            {
                taskRSMaplst.add(taskRsMap);
            }
            
            index = (index + 1) % cols;
        }
        
        if (index != 0)
        {
            //将尾部补充空列
            fillNullCol(taskRsMap, index, cols);
            taskRSMaplst.add(taskRsMap);
        }
        
        return taskRSMaplst;
    }
    
    private void fillNullCol(Map<String, String> taskRsMap, int cur, int cols)
    {
        int mCur = cur;
        while (mCur < cols)
        {
            taskRsMap.put("col" + mCur, "");
            mCur++;
        }
        
    }
    
    private String getStartCycleId(Date startTime)
    {
        Date ownStartTime = startTime;
        if (null == ownStartTime)
        {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(ownStartTime);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        if (0 != minute || 0 != second)
        {
            cal.add(Calendar.HOUR_OF_DAY, 1);
            ownStartTime = cal.getTime();
        }
        
        return TccUtil.covDate2CycleID(ownStartTime);
    }
    
    private Date toDate(String datetime)
        throws ParseException
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setLenient(false);
        return df.parse(datetime);
    }
    
    /**
     * 周期Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqCycleIdJsonObject()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Long taskId = null;
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
        }
        catch (Exception e)
        {
            LOGGER.error("taskId must be an Integer!", e);
            returnEmpty = true;
        }
        cycleIdList = getTccService().getAllCycleIdList(taskId);
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        if (!returnEmpty)
        {
            KeyValuePair keyValuePair;
            for (String cycleId : cycleIdList)
            {
                keyValuePair = new KeyValuePair(cycleId, null);
                keyValuePairList.add(keyValuePair);
            }
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 集成重做
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String integratedRedo()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //修改线程的名字
        Thread.currentThread().setName(String.format("integratedRedo_..."));
        
        String taskInfos = "";
        String taskCycles = "";
        
        taskInfos = request.getParameter("taskInfos");
        taskCycles = request.getParameter("taskCycles");
        //是否忽略没有权限的任务
        Boolean filterNoPriTasks = "true".equals(request.getParameter("filterNoPriTasks"));
        
        List<TaskRunningStateEntity> taskRSList = new ArrayList<TaskRunningStateEntity>();
        List<Long> taskIds = new ArrayList<Long>();
        StringBuilder sbCycles = new StringBuilder();
        try
        {
            LOGGER.info("call integratedredo(taskInfos, taskCycles),taskInfos is {},taskCycles is {}.", new Object[] {
                TccUtil.truncatEncode(taskInfos), TccUtil.truncatEncode(taskCycles)});
            if (!StringUtils.isEmpty(taskCycles))
            {
                String[] taskIdCycleIdStates = taskCycles.split(";");
                String[] taskIdCycleIdStateArr;
                TaskRunningStateEntity taskRS;
                
                for (String taskIdCycleIdState : taskIdCycleIdStates)
                {
                    if (!StringUtils.isEmpty(taskIdCycleIdState))
                    {
                        taskIdCycleIdStateArr = taskIdCycleIdState.split(",");
                        taskRS = new TaskRunningStateEntity();
                        taskRS.setTaskId(Long.parseLong(taskIdCycleIdStateArr[0]));
                        taskRS.setCycleId(taskIdCycleIdStateArr[1]);
                        
                        if (!taskRSList.contains(taskRS))
                        {
                            taskRSList.add(taskRS);
                            
                            //获取不重复的任务Id集合
                            if (!taskIds.contains(taskRS.getTaskId()))
                            {
                                taskIds.add(taskRS.getTaskId());
                            }
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
                //过滤后的重做周期集合
                List<TaskRunningStateEntity> filterTaskRSs = new ArrayList<TaskRunningStateEntity>();
                Map<Long, TaskOwnerInfo> noPriTaskMaps = new HashMap<Long, TaskOwnerInfo>();
                if (filterNoPriTasks)
                {
                    for (TaskOwnerInfo noPriTask : noPriTasks)
                    {
                        noPriTaskMaps.put(noPriTask.getTaskId(), noPriTask);
                    }
                    
                    //过滤掉没有权限的周期
                    for (TaskRunningStateEntity taskRSE : taskRSList)
                    {
                        if (!noPriTaskMaps.containsKey(taskRSE.getTaskId()))
                        {
                            filterTaskRSs.add(taskRSE);
                        }
                    }
                }
                else
                {
                    if (!params.isEmpty())
                    {
                        rv.setExtValue(noPriTasks);
                        throw new PrivilegeNotEnoughException(params);
                    }
                    
                    filterTaskRSs = taskRSList;
                }
                
                for (TaskRunningStateEntity taskRSE : filterTaskRSs)
                {
                    //格式化任务周期的输出
                    sbCycles.append(taskRSE.getTaskId());
                    sbCycles.append(',');
                    sbCycles.append(taskRSE.getCycleId());
                    sbCycles.append(';');
                }
                
                getTccService().redoTaskCycles(filterTaskRSs, true);
                
                //修改任务信息
                if (!StringUtils.isEmpty(taskInfos))
                {
                    String[] taskInfoArr = taskInfos.split(";");
                    String[] taskFiledArr;
                    TaskEntity task;
                    int index = 0;
                    for (String taskInfo : taskInfoArr)
                    {
                        if (!StringUtils.isEmpty(taskInfo))
                        {
                            taskFiledArr = taskInfo.split(",");
                            task = new TaskEntity();
                            index = 0;
                            task.setTaskId(Long.parseLong(taskFiledArr[index]));
                            task.setRedoType(RedoType.INTEGRATED);
                            index++;
                            task.setRedoStartTime(parse2Date(taskFiledArr[index]));
                            index++;
                            task.setRedoEndTime(parse2Date(taskFiledArr[index]));
                            task.setCycleDependFlag(true);
                            index++;
                            task.setRedoDayLength(Integer.parseInt(taskFiledArr[index]));
                            
                            //忽略相应的任务
                            if (filterNoPriTasks && noPriTaskMaps.containsKey(task.getTaskId()))
                            {
                                continue;
                            }
                            
                            getTccPortalService().updateTask(task);
                            
                            //通知修改
                            Eventor.fireEvent(this, EventType.UPDATE_TASK, task.getTaskId());
                        }
                    }
                }
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TASKCYCLE_INTEGRATED_REDO);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                List<String> serviceIdList = new ArrayList<String>();
                
                for (TaskOwnerInfo srcTaskOwner : srcTaskOwners)
                {
                    //过滤掉
                    if (filterNoPriTasks && noPriTaskMaps.containsKey(srcTaskOwner.getTaskId()))
                    {
                        continue;
                    }
                    serviceIdList.add(String.valueOf(srcTaskOwner.getServiceId()));
                }
                operateAuditInfo.setServiceIds(serviceIdList);
                operateAuditInfo.setTaskIds(taskIds);
                operateAuditInfo.setOperParameters(taskInfos + "\n" + taskCycles);
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
            LOGGER.error("failed to call integratedredo(taskInfos, taskCycles),taskInfos is {},taskCycles is {}.",
                new Object[] {TccUtil.truncatEncode(taskInfos), TccUtil.truncatEncode(taskCycles), e});
        }
        finally
        {
            getTccService().continueSchedule(taskRSList);
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
            LOGGER.error("integratedRedo fail!", e1);
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
    
    public TaskRunningStateEntity getTaskRs()
    {
        return taskRs;
    }
    
    public void setTaskRs(TaskRunningStateEntity taskRs)
    {
        this.taskRs = taskRs;
    }
    
    public List<String> getCycleIdList()
    {
        return cycleIdList;
    }
    
    public void setCycleIdList(List<String> cycleIdList)
    {
        this.cycleIdList = cycleIdList;
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
