/*
 * 文 件 名:  TccServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.tcc.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.constants.type.TaskEnableFlag;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.CycleDependRelation;
import com.huawei.platform.tcc.domain.DependRelation;
import com.huawei.platform.tcc.entity.CycleTaskDetailEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.service.TccService;
import com.huawei.platform.tcc.task.CycleTask;
import com.huawei.platform.tcc.utils.TccUtil;
import com.huawei.platform.tcc.utils.ThreadPoolUtil;

/**
 * TCC业务逻辑操作实现
 * 
 * @author z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-11-10]
 * @see [相关类/方法]
 */
public class TccServiceImpl implements TccService, Serializable
{
    /**
     * 序号
     */
    private static final long serialVersionUID = 8362958969001099923L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccServiceImpl.class);
    
    private long checkTimes = 0;
    
    /**
     * 等待执行周期任务hashmap
     */
    private Map<Long, HashMap<String, CycleTask>> waittingTasks = new HashMap<Long, HashMap<String, CycleTask>>();
    
    /**
     * 临时停止调度的周期任务hashmap
     */
    private Map<Long, HashMap<String, TaskRunningStateEntity>> tmpStopedTasks = new HashMap<Long, HashMap<String, TaskRunningStateEntity>>();
    
    /**
     * 临时停止调度的任务hashmap
     */
    private Map<Long, TaskEntity> tmpStopedTaskEs = new HashMap<Long, TaskEntity>();
    
    /**
     * 等待执行周期任务队列
     */
    private List<CycleTask> waittingQueue = new ArrayList<CycleTask>();
    
    /**
     * 正运行的周期任务hashmap
     */
    private Map<Long, HashMap<String, CycleTask>> runningTasks = new HashMap<Long, HashMap<String, CycleTask>>();
    
    private TccDao tccDao;
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 获取任务Id从开始时间后的所有周期Id
     * @param taskId 任务Id
     * @return 获取任务Id从开始时间后的所有周期Id
     * @throws Exception 异常
     */
    @Override
    public List<String> getAllCycleIdList(Long taskId) throws Exception
    {
        return generateAllCycleIDS(taskId);
    }
    
    /**
     * 获取开始时间到结束时间范围内任务状态列表
     * @param taskId 任务Id
     * @param startTime 开始时间
     * @param endTime 结束时间(包括)
     * @return 开始时间到结束时间范围内任务状态列表
     * @throws CException 抛出异常
     */
    @Override
    public List<TaskRunningStateEntity> getTaskRunningStateList(Long taskId,
            Date startTime, Date endTime) throws CException
    {
        Date ownStartTime = startTime;
        List<TaskRunningStateEntity> taskRsList = new ArrayList<TaskRunningStateEntity>();
        TaskEntity task = tccDao.getTask(taskId);
        if (null != task)
        {
            if (null == task.getStartTime()
                    || ownStartTime.before(task.getStartTime()))
            {
                //任务启动时间startTime
                ownStartTime = task.getStartTime();
                
                if (ownStartTime.before(TccConfig.getBenchDate()))
                {
                    ownStartTime = TccConfig.getBenchDate();
                }
            }
            List<String> cycleIds = this.generateCycleIDs(ownStartTime,
                    endTime,
                    CycleType.toCycleType(task.getCycleType()),
                    task.getCycleLength());
            
            TaskRunningStateEntity taskRs;
            for (String cycleId : cycleIds)
            {
                taskRs = new TaskRunningStateEntity();
                taskRs.setTaskId(taskId);
                taskRs.setCycleId(cycleId);
                taskRsList.add(taskRs);
            }
            
        }
        return taskRsList;
    }
    
    /**
     * 获取任务运行状态信息
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 任务运行状态信息
     * @throws CException 抛出异常
     */
    @Override
    public TaskRunningStateEntity getRunningStateEntity(Long taskId,
            String cycleId) throws CException
    {
        TaskRunningStateEntity taskRs = new TaskRunningStateEntity();
        taskRs.setTaskId(taskId);
        taskRs.setCycleId(cycleId);
        if (null == taskId || null == cycleId)
        {
            return taskRs;
        }
        
        try
        {
            TaskRunningStateEntity taskRsRtn = tccDao.getTaskRunningState(taskRs);
            
            //为空表示未初始化
            if (null == taskRsRtn)
            {
                taskRs.setState(RunningState.NOTINIT);
                return taskRs;
            }
            else
            {
                //获取更详细的状态信息
                if (RunningState.START == taskRsRtn.getState()
                        || RunningState.INIT == taskRsRtn.getState())
                {
                    HashMap<String, CycleTask> cycleIdWaittingMap = waittingTasks.get(taskId);
                    if (null != cycleIdWaittingMap
                            && cycleIdWaittingMap.containsKey(cycleId))
                    {
                        taskRsRtn.setState(RunningState.WAITTINGRUN);
                    }
                    else
                    {
                        HashMap<String, CycleTask> cycleIdRunningMap = runningTasks.get(taskId);
                        if (null != cycleIdRunningMap)
                        {
                            CycleTask cycleTask = cycleIdRunningMap.get(cycleId);
                            if (null != cycleTask)
                            {
                                //在运行队列中的开始表示正在运行中
                                if (RunningState.START == cycleTask.getState())
                                {
                                    taskRsRtn.setState(RunningState.RUNNING);
                                }
                                else
                                {
                                    taskRsRtn.setState(cycleTask.getState());
                                }
                            }
                            else
                            {
                                //查询一下看是否已停用
                                TaskEntity task = tccDao.getTask(taskId);
                                if (null == task
                                        || !task.getTaskEnableFlag()
                                        || task.getTaskState() == TaskState.STOP)
                                {
                                    taskRsRtn.setState(RunningState.STOP);
                                }
                            }
                        }
                        else
                        {
                            //查询一下看是否已停用
                            TaskEntity task = tccDao.getTask(taskId);
                            if (null == task || !task.getTaskEnableFlag()
                                    || task.getTaskState() == TaskState.STOP)
                            {
                                taskRsRtn.setState(RunningState.STOP);
                            }
                        }
                    }
                }
                return taskRsRtn;
            }
        }
        catch (CException e)
        {
            LOGGER.error("get runningStateEntity failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取任务运行状态信息
     * @param task 任务
     * @param cycleId 周期Id
     * @return 任务运行状态信息
     * @throws CException 抛出异常
     */
    @Override
    public TaskRunningStateEntity getRunningStateEntity(TaskEntity task,
            String cycleId) throws CException
    {
        if (null == task)
        {
            return null;
        }
        
        Long taskId = task.getTaskId();
        TaskRunningStateEntity taskRs = new TaskRunningStateEntity();
        taskRs.setTaskId(taskId);
        taskRs.setCycleId(cycleId);
        if (null == taskId || null == cycleId)
        {
            return taskRs;
        }
        
        try
        {
            TaskRunningStateEntity taskRsRtn = tccDao.getTaskRunningState(taskRs);
            
            //为空表示未初始化
            if (null == taskRsRtn)
            {
                taskRs.setState(RunningState.NOTINIT);
                return taskRs;
            }
            else
            {
                //获取更详细的状态信息
                if (RunningState.START == taskRsRtn.getState()
                        || RunningState.INIT == taskRsRtn.getState())
                {
                    HashMap<String, CycleTask> cycleIdWaittingMap = waittingTasks.get(taskId);
                    if (null != cycleIdWaittingMap
                            && cycleIdWaittingMap.containsKey(cycleId))
                    {
                        taskRsRtn.setState(RunningState.WAITTINGRUN);
                    }
                    else
                    {
                        HashMap<String, CycleTask> cycleIdRunningMap = runningTasks.get(taskId);
                        if (null != cycleIdRunningMap)
                        {
                            CycleTask cycleTask = cycleIdRunningMap.get(cycleId);
                            if (null != cycleTask)
                            {
                                //在运行队列中的开始表示正在运行中
                                if (RunningState.START == cycleTask.getState())
                                {
                                    taskRsRtn.setState(RunningState.RUNNING);
                                }
                                else
                                {
                                    taskRsRtn.setState(cycleTask.getState());
                                }
                            }
                            else
                            {
                                //查询一下看是否已停用
                                if (null == task
                                        || !task.getTaskEnableFlag()
                                        || task.getTaskState() == TaskState.STOP)
                                {
                                    taskRsRtn.setState(RunningState.STOP);
                                }
                            }
                        }
                        else
                        {
                            //查询一下看是否已停用
                            if (null == task || !task.getTaskEnableFlag()
                                    || task.getTaskState() == TaskState.STOP)
                            {
                                taskRsRtn.setState(RunningState.STOP);
                            }
                        }
                    }
                }
                return taskRsRtn;
            }
        }
        catch (CException e)
        {
            LOGGER.error("get runningStateEntity failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取指定任务周期所直接依赖的任务周期的运行状态列表
     *
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 依赖的任务周期的运行状态列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskRunningStateEntity> getDependingRunningState(Long taskId,
            String cycleId) throws CException
    {
        TaskEntity task = null;
        
        try
        {
            task = tccDao.getTask(taskId);
        }
        catch (CException e)
        {
            LOGGER.error("get dependingRunningState failed!", e);
            throw e;
        }
        
        return getDependingRunningState(task, cycleId);
    }
    
    /**
     * 获取指定任务周期所直接依赖的任务周期的运行状态列表
     *
     * @param task 任务
     * @param cycleId 周期Id
     * @return 依赖的任务周期的运行状态列表
     * @throws CException 统一封装的异常
     */
    public List<TaskRunningStateEntity> getDependingRunningState(
            TaskEntity task, String cycleId) throws CException
    {
        if (null == task)
        {
            return new ArrayList<TaskRunningStateEntity>(0);
        }
        //获取周期任务的所有周期依赖关系
        List<CycleDependRelation> cycleDepRLst = getAllCycleDepRs(task.getTaskId(),
                cycleId,
                task);
        
        //获取依赖关系
        List<TaskRunningStateEntity> dependingTaskRsList = new ArrayList<TaskRunningStateEntity>();
        TaskRunningStateEntity dependingTaskRs;
        for (CycleDependRelation cycleDependRelation : cycleDepRLst)
        {
            dependingTaskRs = getRunningStateEntity(cycleDependRelation.getDependTaskId(),
                    cycleDependRelation.getDependCycleId());
            if (null != dependingTaskRs)
            {
                dependingTaskRsList.add(dependingTaskRs);
            }
        }
        return dependingTaskRsList;
    }
    
    /**
     * 获取直接或间接依赖于指定周期任务的所有任务周期的运行状态集合（实时计算）
     *
     * @param taskId 根节点的任务Id
     * @param cycleId  根节点的周期Id
     * @param maxCount 任务周期数
     * @return 依赖树的所有任务周期的运行状态集合
     * @throws Exception 统一封装的异常
     */
    @Override
    public List<TaskRunningStateEntity> getAllDependedRSListRealTime(
            Long taskId, String cycleId, int maxCount) throws Exception
    {
        //修改线程的名字
        Thread.currentThread()
                .setName(String.format("getAllDependedRSListRealTime_%d_%s",
                        taskId,
                        cycleId));
        
        //已经处理过依赖关系的任务周期,防止重复读取依赖关系
        List<TaskRunningStateEntity> taskRSProcessed = new ArrayList<TaskRunningStateEntity>();
        
        try
        {
            TaskRunningStateEntity curNode = null;
            //待处理任务状态队列
            Deque<TaskRunningStateEntity> processingQueue = new ArrayDeque<TaskRunningStateEntity>();
            Map<Long, TaskEntity> taskMaps = new HashMap<Long, TaskEntity>();
            Map<Long, List<TaskEntity>> taskDepMaps = new HashMap<Long, List<TaskEntity>>();
            TaskEntity currTask;
            List<TaskEntity> tasks;
            List<CycleDependRelation> cycleDeppingRs;
            List<Long> taskIds;
            if (null != taskId && (StringUtils.isNotBlank(cycleId)))
            {
                TaskRunningStateEntity rootNode = new TaskRunningStateEntity();
                rootNode.setTaskId(taskId);
                rootNode.setCycleId(cycleId);
                
                //初始化依赖任务树的根
                processingQueue.push(rootNode);
                
                //处理依赖周期任务树
                while (!processingQueue.isEmpty()
                        && taskRSProcessed.size() < maxCount)
                {
                    curNode = processingQueue.remove();
                    
                    //获取反向依赖的任务周期
                    if (!taskMaps.containsKey(curNode.getTaskId()))
                    {
                        currTask = tccDao.getTask(curNode.getTaskId());
                        if (null == currTask)
                        {
                            LOGGER.warn("task[taskId={}] does't exist! ignore it!",
                                    curNode.getTaskId());
                            continue;
                        }
                        taskMaps.put(currTask.getTaskId(), currTask);
                    }
                    else
                    {
                        currTask = taskMaps.get(curNode.getTaskId());
                    }
                    
                    //缓存任务依赖关系
                    if (!taskDepMaps.containsKey(currTask.getTaskId()))
                    {
                        taskIds = new ArrayList<Long>();
                        taskIds.add(currTask.getTaskId());
                        //查询直接反向依赖任务
                        tasks = tccDao.getDependedTasks(taskIds);
                        taskDepMaps.put(currTask.getTaskId(), tasks);
                    }
                    else
                    {
                        tasks = taskDepMaps.get(currTask.getTaskId());
                    }
                    
                    cycleDeppingRs = getAllCycleDeppingRs(currTask,
                            tasks,
                            curNode.getCycleId());
                    //查询任务周期的状态
                    curNode = getRunningStateEntity(curNode.getTaskId(),
                            curNode.getCycleId());
                    
                    //不存在该周期
                    if (null == curNode
                            || curNode.getState() == RunningState.NOTINIT)
                    {
                        continue;
                    }
                    
                    taskRSProcessed.add(curNode);
                    
                    for (CycleDependRelation cycleDependRelation : cycleDeppingRs)
                    {
                        curNode = new TaskRunningStateEntity();
                        curNode.setTaskId(cycleDependRelation.getDependTaskId());
                        curNode.setCycleId(cycleDependRelation.getDependCycleId());
                        //防止重复处理
                        if (!processingQueue.contains(curNode)
                                && !taskRSProcessed.contains(curNode))
                        {
                            processingQueue.add(curNode);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllDependedRSListRealTime fail", e);
        }
        
        return taskRSProcessed;
    }
    
    /**
     * 获取直接或间接依赖于指定周期任务的所有任务周期的运行状态集合（根据历史记录构建）
     *
     * @param taskId 根节点的任务Id
     * @param cycleId  根节点的周期Id
     * @return 依赖树的所有任务周期的运行状态集合
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskRunningStateEntity> getAllDependedRSList(Long taskId,
            String cycleId) throws CException
    {
        //修改线程的名字
        Thread.currentThread()
                .setName(String.format("getAllDependedRSList_%d_%s",
                        taskId,
                        cycleId));
        
        //检查任务运行状态是否存在
        TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
        taskRS.setTaskId(taskId);
        taskRS.setCycleId(cycleId);
        taskRS = tccDao.getTaskRunningState(taskRS);
        
        //如果不存在，则返回空对象
        if (null == taskRS)
        {
            return new ArrayList<TaskRunningStateEntity>(0);
        }
        
        //待处理任务状态队列
        Deque<TaskRunningStateEntity> processingQueue = new ArrayDeque<TaskRunningStateEntity>();
        //初始化重做任务树的根
        processingQueue.push(taskRS);
        
        //依赖任务的运行状态
        TaskRunningStateEntity taskRSE;
        //当前的任务运行状态
        TaskRunningStateEntity curTaskRS;
        
        //需要更新的任务运行状态
        TaskRunningStateEntity taskRSInit = new TaskRunningStateEntity();
        
        //已经处理过依赖关系的任务周期,防止重复读取依赖关系
        List<TaskRunningStateEntity> tasRSProcessed = new ArrayList<TaskRunningStateEntity>();
        
        //依赖树的所有任务周期的运行状态集合
        List<TaskRunningStateEntity> tasRSListRtn = new ArrayList<TaskRunningStateEntity>();
        
        //处理依赖周期任务树
        while (!processingQueue.isEmpty())
        {
            curTaskRS = processingQueue.remove();
            
            //已经处理过依赖关系的任务周期
            tasRSProcessed.add(curTaskRS);
            
            //任务运行状态
            taskRSInit.setTaskId(curTaskRS.getTaskId());
            taskRSInit.setCycleId(curTaskRS.getCycleId());
            //获取依赖任务，对于每一个依赖任务，添加到待处理队列中
            curTaskRS = tccDao.getTaskRunningState(taskRSInit);
            
            //如果不存在，则记录
            if (null == curTaskRS)
            {
                LOGGER.error("dependent taskRunningState[taskId={},cycleId={}] do not exist, ignore it.",
                        new Object[] { taskId, cycleId });
                continue;
            }
            
            tasRSListRtn.add(curTaskRS);
            
            //依赖任务列表
            String depends = curTaskRS.getBeginDependTaskList();
            if (!StringUtils.isEmpty(depends))
            {
                String[] taskIdCycleIds = depends.split(";");
                Long eTaskId;
                String eCycleId;
                String[] taskIdCycleIdArr;
                //循环每一个依赖任务，添加到处理队列中
                for (String taskIdCycleId : taskIdCycleIds)
                {
                    if (!StringUtils.isEmpty(taskIdCycleId))
                    {
                        taskIdCycleIdArr = taskIdCycleId.split(",");
                        eTaskId = Long.parseLong(taskIdCycleIdArr[0]);
                        eCycleId = taskIdCycleIdArr[1];
                        
                        //初始化任务运行状态对象
                        taskRSE = new TaskRunningStateEntity();
                        taskRSE.setTaskId(eTaskId);
                        taskRSE.setCycleId(eCycleId);
                        
                        //保证processedQueue中的任务运行状态对象(主键)的唯一性
                        if (!processingQueue.contains(taskRSE)
                                && !tasRSProcessed.contains(taskRSE))
                        {
                            processingQueue.add(taskRSE);
                        }
                        
                    }
                }
            }
        }
        
        return tasRSListRtn;
    }
    
    /**
     * 获取直接依赖于指定任务周期的任务运行状态列表
     *
     * @param taskRS 任务运行状态,同时用于返回状态
     * @return 依赖的任务周期的运行状态列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskRunningStateEntity> getDependedRSList(
            TaskRunningStateEntity taskRS) throws CException
    {
        //修改线程的名字
        Thread.currentThread()
                .setName(String.format("getAllDependedRSList_%d_%s",
                        taskRS.getTaskId(),
                        taskRS.getCycleId()));
        
        //检查任务运行状态是否存在
        TaskRunningStateEntity taskRSE = new TaskRunningStateEntity();
        taskRSE.setTaskId(taskRS.getTaskId());
        taskRSE.setCycleId(taskRS.getCycleId());
        TaskRunningStateEntity taskRSERtn = tccDao.getTaskRunningState(taskRSE);
        
        //如果不存在，则返回空对象
        if (null == taskRSERtn)
        {
            taskRS.setState(RunningState.NOTINIT);
            return new ArrayList<TaskRunningStateEntity>(0);
        }
        
        //修改返回状态
        taskRS.setState(taskRSERtn.getState());
        List<TaskRunningStateEntity> taskRSList = new ArrayList<TaskRunningStateEntity>();
        
        //依赖任务列表
        String depends = taskRSERtn.getBeginDependTaskList();
        if (!StringUtils.isEmpty(depends))
        {
            String[] taskIdCycleIds = depends.split(";");
            Long eTaskId;
            String eCycleId;
            String[] taskIdCycleIdArr;
            //循环每一个依赖任务，添加到处理队列中
            for (String taskIdCycleId : taskIdCycleIds)
            {
                if (!StringUtils.isEmpty(taskIdCycleId))
                {
                    taskIdCycleIdArr = taskIdCycleId.split(",");
                    eTaskId = Long.parseLong(taskIdCycleIdArr[0]);
                    eCycleId = taskIdCycleIdArr[1];
                    
                    //初始化任务运行状态对象
                    taskRSERtn = new TaskRunningStateEntity();
                    taskRSERtn.setTaskId(eTaskId);
                    taskRSERtn.setCycleId(eCycleId);
                    
                    //保证taskRSList中的任务运行状态对象唯一性
                    if (!taskRSList.contains(taskRSERtn))
                    {
                        taskRSList.add(taskRSERtn);
                    }
                    
                }
            }
        }
        return taskRSList;
    }
    
    /**
     * 重做任务树根节点列表中的所有任务树
     * 停掉以指定周期任务为根节点的依赖树上的周期任务，重做该依赖树上的所有周期任务
     * @param taskRsList 任务树根节点列表
     * @throws CException 统一封装的异常
     */
    @Override
    public void redoCycleTaskTree(List<TaskRunningStateEntity> taskRsList)
            throws CException
    {
        //修改线程的名字
        Thread.currentThread().setName("redoCycleTaskList");
        
        //检查任务运行状态是否存在
        
        if (null == taskRsList)
        {
            return;
        }
        
        for (TaskRunningStateEntity taskRs : taskRsList)
        {
            if (null != taskRs && null != taskRs.getTaskId()
                    && null != taskRs.getCycleId())
            {
                redoCycleTaskTree(taskRs.getTaskId(),
                        taskRs.getCycleId(),
                        true,
                        true);
            }
        }
        
        for (TaskRunningStateEntity taskRs : taskRsList)
        {
            if (null != taskRs && null != taskRs.getTaskId()
                    && null != taskRs.getCycleId())
            {
                redoCycleTaskTree(taskRs.getTaskId(),
                        taskRs.getCycleId(),
                        true,
                        true);
            }
        }
        
        //重新允许周期任务开始调度
        synchronized (tmpStopedTasks)
        {
            tmpStopedTasks.clear();
        }
    }
    
    /**
     * 重做指定的周期任务
     * 停掉以指定周期任务为根节点的依赖树上的周期任务，重做该依赖树上的所有周期任务
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param ignoreNoExistError 是否忽略任务树根节点的任务运行状态不存在错误
     * @param keepStopedSchedule 是否保持停止调度任务树，由外部统一启动调度
     * @throws CException 统一封装的异常
     */
    @Override
    public void redoCycleTaskTree(Long taskId, String cycleId,
            boolean ignoreNoExistError, boolean keepStopedSchedule)
            throws CException
    {
        //修改线程的名字
        Thread.currentThread().setName(String.format("redoCycleTask_%d_%s",
                taskId,
                cycleId));
        
        //检查任务运行状态是否存在
        TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
        taskRS.setTaskId(taskId);
        taskRS.setCycleId(cycleId);
        taskRS = tccDao.getTaskRunningState(taskRS);
        
        //如果不存在，则抛出异常
        if (null == taskRS)
        {
            if (ignoreNoExistError)
            {
                return;
            }
            
            LOGGER.error("dependent taskRunningState[taskId={},cycleId={}] do not exist, ignore it.",
                    new Object[] { taskId, cycleId });
            throw new CException(
                    ResultCodeConstants.TASK_CYCLE_NOT_EXIST_ERROR,
                    new Object[] { taskId, cycleId });
        }
        
        //待处理任务状态队列
        Deque<TaskRunningStateEntity> processedQueue = new ArrayDeque<TaskRunningStateEntity>();
        //初始化重做任务树的根
        processedQueue.push(taskRS);
        
        //依赖任务的运行状态
        TaskRunningStateEntity taskRSE;
        //当前的任务运行状态
        TaskRunningStateEntity curTaskRS;
        
        //需要更新的任务运行状态
        TaskRunningStateEntity taskRSInit = new TaskRunningStateEntity();
        
        HashMap<String, TaskRunningStateEntity> cycleIdKeyhashMap;
        //处理依赖周期任务树
        while (!processedQueue.isEmpty())
        {
            curTaskRS = processedQueue.remove();
            
            //添加到临时不调度周期任务队列
            
            synchronized (tmpStopedTasks)
            {
                
                if (tmpStopedTasks.containsKey(curTaskRS.getTaskId()))
                {
                    tmpStopedTasks.get(curTaskRS.getTaskId())
                            .put(curTaskRS.getCycleId(), curTaskRS);
                }
                else
                {
                    cycleIdKeyhashMap = new HashMap<String, TaskRunningStateEntity>();
                    cycleIdKeyhashMap.put(curTaskRS.getCycleId(), curTaskRS);
                    tmpStopedTasks.put(curTaskRS.getTaskId(), cycleIdKeyhashMap);
                }
            }
            
            //调用stop方法，要保证stop之后，周期任务不会更新任务运行状态
            stopCycleTask(curTaskRS.getTaskId(), curTaskRS.getCycleId());
            //更新任务运行状态
            taskRSInit.setTaskId(curTaskRS.getTaskId());
            taskRSInit.setCycleId(curTaskRS.getCycleId());
            taskRSInit.setState(RunningState.INIT);
            tccDao.updateTaskRunningState(taskRSInit);
            //获取依赖任务，对于每一个依赖任务，添加到待处理队列中
            curTaskRS = tccDao.getTaskRunningState(taskRSInit);
            
            //如果不存在，则记录
            if (null == curTaskRS)
            {
                LOGGER.error("dependent taskRunningState[taskId={},cycleId={}] do not exist, ignore it.",
                        new Object[] { taskId, cycleId });
                continue;
            }
            
            //依赖任务列表
            String depends = curTaskRS.getBeginDependTaskList();
            if (!StringUtils.isEmpty(depends))
            {
                curTaskRS.setBeginDependTaskList("");
                curTaskRS.setFinishDependTaskList("");
                tccDao.updateTaskRunningState(curTaskRS);
                
                String[] taskIdCycleIds = depends.split(";");
                Long eTaskId;
                String eCycleId;
                String[] taskIdCycleIdArr;
                //循环每一个依赖任务，添加到处理队列中
                for (String taskIdCycleId : taskIdCycleIds)
                {
                    if (!StringUtils.isEmpty(taskIdCycleId))
                    {
                        taskIdCycleIdArr = taskIdCycleId.split(",");
                        eTaskId = Long.parseLong(taskIdCycleIdArr[0]);
                        eCycleId = taskIdCycleIdArr[1];
                        
                        //初始化任务运行状态对象
                        taskRSE = new TaskRunningStateEntity();
                        taskRSE.setTaskId(eTaskId);
                        taskRSE.setCycleId(eCycleId);
                        
                        //保证processedQueue中的任务运行状态对象(主键)的唯一性
                        if (!processedQueue.contains(taskRSE))
                        {
                            processedQueue.add(taskRSE);
                        }
                        
                    }
                }
            }
        }
        
        //重新允许周期任务开始调度
        if (!keepStopedSchedule)
        {
            synchronized (tmpStopedTasks)
            {
                tmpStopedTasks.clear();
            }
        }
    }
    
    /**
     * 向waittingQueue队列中按值大小顺序插入一个元素
     */
    private void insertSorted(CycleTask element)
    {
        boolean inserted = false;
        for (int i = 0; i < waittingQueue.size(); i++)
        {
            if (waittingQueue.get(i).compareTo(element) < 0)
            {
                waittingQueue.add(i, element);
                inserted = true;
                break;
            }
        }
        
        if (!inserted)
        {
            waittingQueue.add(element);
        }
    }
    
    /**
     * 重做指定的周期任务
     * 停掉指定周期任务，重做该周期任务
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param ignoreNoExistError 是否忽略任务树根节点的任务运行状态不存在错误
     * @throws CException 统一封装的异常
     */
    @Override
    public void redoCycleTask(Long taskId, String cycleId,
            boolean ignoreNoExistError) throws CException
    {
        //修改线程的名字
        Thread.currentThread().setName(String.format("redoCycleTask_%d_%s",
                taskId,
                cycleId));
        
        //检查任务运行状态是否存在
        TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
        taskRS.setTaskId(taskId);
        taskRS.setCycleId(cycleId);
        taskRS = tccDao.getTaskRunningState(taskRS);
        
        //如果不存在，则抛出异常
        if (null == taskRS)
        {
            if (ignoreNoExistError)
            {
                return;
            }
            
            LOGGER.error("dependent taskRunningState[taskId={},cycleId={}] do not exist, ignore it.",
                    new Object[] { taskId, cycleId });
            throw new CException(
                    ResultCodeConstants.TASK_CYCLE_NOT_EXIST_ERROR,
                    new Object[] { taskId, cycleId });
        }
        //调用stop方法，要保证stop之后，周期任务不会更新任务运行状态
        stopCycleTask(taskId, cycleId);
        
        //更新任务运行状态
        taskRS = new TaskRunningStateEntity();
        taskRS.setTaskId(taskId);
        taskRS.setCycleId(cycleId);
        taskRS.setState(RunningState.INIT);
        tccDao.updateTaskRunningStateWithNullTime(taskRS);
    }
    
    /**
     * 重做任务周期列表
     * 停掉列表中的所有任务周期，并重做任务周期列表
     * @param taskRsList 要重做的任务周期列表
     * @throws Exception 统一封装的异常
     */
    @Override
    public void redoCycleTask(List<TaskRunningStateEntity> taskRsList)
            throws Exception
    {
        redoTaskCycles(taskRsList, false);
    }
    
    /**
     * 重做任务周期列表
     * 停掉列表中的所有任务周期，并重做任务周期列表
     * @param taskRsList 要重做的任务周期列表
     * @param keepStopedSchedule 是否保持停止调度任务树，由外部统一启动调度
     * @throws Exception 统一封装的异常
     */
    @Override
    public void redoTaskCycles(List<TaskRunningStateEntity> taskRsList,
            boolean keepStopedSchedule) throws Exception
    {
        //修改线程的名字
        Thread.currentThread().setName("redoTaskCycles");
        
        if (null == taskRsList || taskRsList.isEmpty())
        {
            return;
        }
        
        try
        {
            //临时停止调度taskRsList中的任务周期
            stopSchedule(taskRsList);
            
            for (TaskRunningStateEntity taskRs : taskRsList)
            {
                if (null != taskRs && null != taskRs.getTaskId()
                        && null != taskRs.getCycleId())
                {
                    redoCycleTask(taskRs.getTaskId(), taskRs.getCycleId(), true);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("redo TaskCycles failed, taskRsList is {}",
                    taskRsList,
                    e);
            throw e;
        }
        finally
        {
            if (!keepStopedSchedule)
            {
                continueSchedule(taskRsList);
            }
        }
    }
    
    /**
     * 停止调度taskRsList中的任务周期
     * 
     * @param taskRsList 任务周期列表
     */
    public void stopSchedule(List<TaskRunningStateEntity> taskRsList)
    {
        if (null == taskRsList || taskRsList.isEmpty())
        {
            return;
        }
        
        //添加到临时不调度周期任务队列
        HashMap<String, TaskRunningStateEntity> cycleIdKeyhashMap;
        for (TaskRunningStateEntity taskRs : taskRsList)
        {
            if (null != taskRs && null != taskRs.getTaskId()
                    && null != taskRs.getCycleId())
            {
                synchronized (tmpStopedTasks)
                {
                    if (tmpStopedTasks.containsKey(taskRs.getTaskId()))
                    {
                        tmpStopedTasks.get(taskRs.getTaskId())
                                .put(taskRs.getCycleId(), taskRs);
                    }
                    else
                    {
                        cycleIdKeyhashMap = new HashMap<String, TaskRunningStateEntity>();
                        cycleIdKeyhashMap.put(taskRs.getCycleId(), taskRs);
                        tmpStopedTasks.put(taskRs.getTaskId(),
                                cycleIdKeyhashMap);
                    }
                }
            }
        }
    }
    
    /**
     * 继续重新调度taskRsList中的任务周期
     * 
     * @param taskRsList 任务周期列表
     */
    @Override
    public void continueSchedule(List<TaskRunningStateEntity> taskRsList)
    {
        if (null == taskRsList || taskRsList.isEmpty())
        {
            return;
        }
        
        Map<String, TaskRunningStateEntity> cycleTaskRSMap;
        //重新允许周期任务开始调度
        synchronized (tmpStopedTasks)
        {
            //执行完毕后，通过迭代器移除周期任务
            for (TaskRunningStateEntity taskRs : taskRsList)
            {
                if (null != taskRs && null != taskRs.getTaskId()
                        && null != taskRs.getCycleId())
                {
                    cycleTaskRSMap = tmpStopedTasks.get(taskRs.getTaskId());
                    if (null != cycleTaskRSMap
                            && cycleTaskRSMap.containsKey(taskRs.getCycleId()))
                    {
                        cycleTaskRSMap.remove(taskRs.getCycleId());
                        
                        //如果cycleTaskRSMap为空，则移除掉
                        if (cycleTaskRSMap.isEmpty())
                        {
                            tmpStopedTasks.remove(taskRs.getTaskId());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 停止调度tasks中的任务
     * @param tasks 任务列表
     */
    @Override
    public void stopScheduleTasks(List<TaskEntity> tasks)
    {
        if (null == tasks || tasks.isEmpty())
        {
            return;
        }
        
        //重新允许周期任务开始调度
        synchronized (tmpStopedTaskEs)
        {
            //执行完毕后，通过迭代器移除周期任务
            for (TaskEntity task : tasks)
            {
                if (null != task && null != task.getTaskId())
                {
                    if (!tmpStopedTaskEs.containsKey(task.getTaskId()))
                    {
                        tmpStopedTaskEs.put(task.getTaskId(), task);
                    }
                }
            }
        }
    }
    
    /**
     * 继续重新调度tasks中的任务
     * 
     * @param tasks 任务列表
     */
    @Override
    public void continueScheduleTasks(List<TaskEntity> tasks)
    {
        if (null == tasks || tasks.isEmpty())
        {
            return;
        }
        
        //重新允许周期任务开始调度
        synchronized (tmpStopedTaskEs)
        {
            //执行完毕后，通过迭代器移除周期任务
            for (TaskEntity task : tasks)
            {
                if (null != task && null != task.getTaskId())
                {
                    if (tmpStopedTaskEs.containsKey(task.getTaskId()))
                    {
                        tmpStopedTaskEs.remove(task.getTaskId());
                    }
                }
            }
        }
    }
    
    /**
     * 调度任务
     */
    public void scheduleTask()
    {
        checkTimes++;
        LOGGER.debug("run ... , {} times", checkTimes);
        try
        {
            //获取任务列表
            List<TaskEntity> taskLst = tccDao.getTaskList(TaskEnableFlag.ENABLE);
            
            //停止周期任务或者填充任务周期
            stopfillCycleTask(taskLst);
            
            //指定任务Id对应的所以周期任务集合
            List<TaskRunningStateEntity> taskRunStatelst;
            
            //周期ID集合
            List<Long> disTaskIds = new ArrayList<Long>();
            
            //时间上可以运行的周期任务集合
            List<TaskRunningStateEntity> taskRSlst = new ArrayList<TaskRunningStateEntity>();
            
            for (TaskEntity task : taskLst)
            {
                if (TaskState.STOP == task.getTaskState()
                        || tmpStopedTaskEs.containsKey(task.getTaskId()))
                {
                    continue;
                }
                
                String maxCycleId = maxCycleIdTimeisOK(task.getCycleOffset());
                Long taskId = task.getTaskId();
                disTaskIds.add(taskId);
                
                //获取指定任务Id对应的所有时间上可以运行周期任务集合（运行状态信息列表）
                if (!task.getCycleDependFlag())
                {
                    taskRunStatelst = tccDao.getNeedRunCycleTasks(taskId,
                            task.getStartTime(),
                            maxCycleId);
                }
                else
                {
                    //如果是顺序依赖，只需要取一条
                    taskRunStatelst = tccDao.getTopOldNeedRunCycleTasks(taskId,
                            task.getStartTime(),
                            maxCycleId);
                }
                
                if (null != taskRunStatelst)
                {
                    taskRSlst.addAll(taskRunStatelst);
                }
                
            }
            
            //估计任务步骤数不会太多,需要的任务步骤一次全部加载
            List<TaskStepEntity> taskStepLst = tccDao.getTaskStepList(disTaskIds);
            
            //为方便后续快速查找使用，先存放到map中
            HashMap<Long, TaskEntity> taskMap = new HashMap<Long, TaskEntity>();
            HashMap<Long, HashMap<Integer, TaskStepEntity>> taskStepMap = new HashMap<Long, HashMap<Integer, TaskStepEntity>>();
            for (TaskEntity task : taskLst)
            {
                taskMap.put(task.getTaskId(), task);
            }
            
            for (TaskStepEntity taskStep : taskStepLst)
            {
                if (taskStepMap.containsKey(taskStep.getTaskId()))
                {
                    taskStepMap.get(taskStep.getTaskId())
                            .put(taskStep.getStepId(), taskStep);
                }
                else
                {
                    HashMap<Integer, TaskStepEntity> stepKeyTaskStepMap = new HashMap<Integer, TaskStepEntity>();
                    stepKeyTaskStepMap.put(taskStep.getStepId(), taskStep);
                    taskStepMap.put(taskStep.getTaskId(), stepKeyTaskStepMap);
                }
            }
            
            //循环处理每一个周期任务
            Long taskId = 0L;
            String cycleId = "";
            for (TaskRunningStateEntity taskRS : taskRSlst)
            {
                try
                {
                    taskId = taskRS.getTaskId();
                    cycleId = taskRS.getCycleId();
                    
                    synchronized (tmpStopedTasks)
                    {
                        if (tmpStopedTasks.containsKey(taskId)
                                && tmpStopedTasks.get(taskId)
                                        .containsKey(cycleId))
                        {
                            LOGGER.debug("because of redo, temporary stop to schedule taskRS[taskId=[{}],cycleId=[{}]]",
                                    taskId,
                                    cycleId);
                            //临时停止调度当前周期任务
                            continue;
                        }
                    }
                    
                    HashMap<String, CycleTask> waitings = null;
                    HashMap<String, CycleTask> runnings = null;
                    
                    synchronized (runningTasks)
                    {
                        if (runningTasks.containsKey(taskId)
                                && runningTasks.get(taskId)
                                        .containsKey(cycleId)
                                || waittingTasks.containsKey(taskId)
                                && waittingTasks.get(taskId)
                                        .containsKey(cycleId))
                        {
                            LOGGER.debug("because of running, stop to schedule taskRS[taskId=[{}],cycleId=[{}]]",
                                    taskId,
                                    cycleId);
                            //停止调度当前正在运行的周期任务
                            continue;
                        }
                        runnings = runningTasks.get(taskId);
                        waitings = waittingTasks.get(taskId);
                    }
                    
                    //如果等待队列中已经包含了此周期任务，则不做任何处理
                    if (null != runnings && runnings.containsKey(cycleId)
                            || null != waitings
                            && waitings.containsKey(cycleId))
                    {
                        continue;
                    }
                    
                    //检查任务是否存在
                    TaskEntity task = taskMap.get(taskId);
                    if (null == task)
                    {
                        LOGGER.error("CycleTask[{},{}] prepare to run,but Task does not exist.",
                                new Object[] { taskId, cycleId });
                        LOGGER.warn("state of CycleTask[{},{}] is set to reqdelete.",
                                taskId,
                                cycleId);
                        TaskRunningStateEntity taskRSU = new TaskRunningStateEntity();
                        taskRSU.setTaskId(taskId);
                        taskRSU.setCycleId(cycleId);
                        taskRSU.setState(RunningState.REQDELETE);
                        tccDao.updateTaskRunningState(taskRSU);
                        
                        continue;
                    }
                    
                    //获取周期任务的所有周期依赖关系
                    List<CycleDependRelation> cycleDepRLst = getAllCycleDepRs(taskId,
                            cycleId,
                            task);
                    
                    //如果依赖任务都满足要求,则将周期任务添加到等待队列中
                    if (isDependsOk(cycleDepRLst, taskId))
                    {
                        //初始化周期任务
                        CycleTask cycleTask = new CycleTask(task,
                                taskStepMap.get(taskId), taskRS, cycleDepRLst);
                        
                        //传递dao对象
                        cycleTask.setTccDao(tccDao);
                        add2WaittingTasks(taskId, cycleId, cycleTask);
                    }
                    
                }
                catch (Exception e)
                {
                    //调度单个任务周期出错，忽略继续执行下一个任务周期
                    LOGGER.error("schedule taskRS[taskId={},cycleId={}]",
                            new Object[] { taskId, cycleId, e });
                    continue;
                }
            }
            //移除已经结束的周期任务
            removeEndedActivityCycleTask();
            
            //从等待任务队列中选出优先级最高的N-runningTask.size()个周期任务，
            //并启用工作线程执行
            maxPriorityCycleTask();
        }
        catch (Throwable e)
        {
            LOGGER.error("scheduleTask execute failed!", e);
        }
    }
    
    //移除已经结束的周期任务
    private void removeEndedActivityCycleTask()
    {
        try
        {
            synchronized (runningTasks)
            {
                //执行完毕后，通过迭代器移除周期任务
                Iterator<Entry<Long, HashMap<String, CycleTask>>> taskIdEntryIter = runningTasks.entrySet()
                        .iterator();
                Entry<Long, HashMap<String, CycleTask>> taskIdCycleEntry;
                Long taskId;
                String cycleId;
                Iterator<Entry<String, CycleTask>> cycleIdEntryIter;
                Entry<String, CycleTask> cycleIdEntry;
                HashMap<String, CycleTask> cycleTasks;
                CycleTask cycleTask;
                while (taskIdEntryIter.hasNext())
                {
                    taskIdCycleEntry = taskIdEntryIter.next();
                    taskId = taskIdCycleEntry.getKey();
                    cycleTasks = taskIdCycleEntry.getValue();
                    
                    cycleIdEntryIter = cycleTasks.entrySet().iterator();
                    while (cycleIdEntryIter.hasNext())
                    {
                        cycleIdEntry = cycleIdEntryIter.next();
                        cycleId = cycleIdEntry.getKey();
                        cycleTask = cycleIdEntry.getValue();
                        if (RunningState.INIT != cycleTask.getState()
                                && RunningState.START != cycleTask.getState())
                        {
                            cycleIdEntryIter.remove();
                            LOGGER.info("removed cycleTask[taskId={},cycleId={}] from runningTasks finished",
                                    new Object[] { taskId, cycleId });
                        }
                    }
                    
                    if (cycleTasks.isEmpty())
                    {
                        taskIdEntryIter.remove();
                    }
                }
            }
        }
        catch (Throwable e)
        {
            LOGGER.error("removed from runningQueue failed!", e);
        }
    }
    
    private void maxPriorityCycleTask()
    {
        //总的资源数
        int countResN = TccConfig.getMaxRunningCycleTaskNum();
        //已经使用的资源数
        int runningResN = getRunningTaskWeights();
        
        int newRunningResN = countResN - runningResN;
        
        int count = 0;
        CycleTask cycleTask;
        Long taskId;
        String cycleId;
        
        synchronized (runningTasks)
        {
            //waittingQueue中的周期任务按照优先级从高到底排序，权重从高到地排序
            if (!waittingQueue.isEmpty())
            {
                //第一个元素的优先级
                int priority = waittingQueue.get(0).getPriority();
                
                Iterator<CycleTask> cycleTaskIter = waittingQueue.iterator();
                
                while (cycleTaskIter.hasNext())
                {
                    //先从等待优先级队列中获取周期任务
                    cycleTask = cycleTaskIter.next();
                    
                    //记录添加到运行队列的元素
                    taskId = cycleTask.getTaskId();
                    cycleId = cycleTask.getCycleId();
                    
                    //相同优先级才需要扫描合适权重的周期任务，否则直接退出
                    if (priority != cycleTask.getPriority())
                    {
                        break;
                    }
                    
                    //可用资源数足够运行当前的周期任务的话，就启动当前周期任务
                    if (newRunningResN >= cycleTask.getTaskWeight())
                    {
                        //从等待队列中移除
                        cycleTaskIter.remove();
                        
                        //减去占用的资源数
                        newRunningResN -= cycleTask.getTaskWeight();
                        
                        //添加到运行任务hashMap中
                        if (runningTasks.containsKey(taskId))
                        {
                            runningTasks.get(taskId).put(cycleId, cycleTask);
                        }
                        else
                        {
                            HashMap<String, CycleTask> cycleTaskMap = new HashMap<String, CycleTask>();
                            cycleTaskMap.put(cycleId, cycleTask);
                            runningTasks.put(taskId, cycleTaskMap);
                        }
                        
                        LOGGER.info("put cycleTask[taskId=[{}],cycleId=[{}]] into runningTasks.",
                                new Object[] { taskId, cycleId });
                        
                        //从等待任务haspmap移除cycleTask周期任务,周期ID与周期任务hashmap集合为空时仍然保留
                        waittingTasks.get(taskId).remove(cycleId);
                        
                        //启动周期任务
                        ThreadPoolUtil.submmitTask(cycleTask);
                        count++;
                    }
                }
            }
        }
        
        //记录可用的剩余资源数
        LOGGER.debug("number of available resouce  is [{}]", newRunningResN);
        
        //记录添加到等待队列的元素
        int runningN = getRunningTaskNum();
        LOGGER.debug("schedule {} cycleTask to run. size of runningTasks is [{}]",
                count,
                runningN);
        
        //记录运行队列中的周期任务
        logRunningTasks();
    }
    
    /**
     * 记录运行队列中的周期任务
     */
    private void logRunningTasks()
    {
        //仅仅记录degug级别日志
        if (!LOGGER.isDebugEnabled())
        {
            return;
        }
        StringBuilder sbRunningTasks = new StringBuilder();
        sbRunningTasks.append("runningTasks is [\n");
        synchronized (runningTasks)
        {
            for (HashMap<String, CycleTask> cycleTasks : runningTasks.values())
            {
                for (CycleTask cycleTask : cycleTasks.values())
                {
                    sbRunningTasks.append("taskId=");
                    sbRunningTasks.append(cycleTask.getTaskId());
                    sbRunningTasks.append(",cycleId=");
                    sbRunningTasks.append(cycleTask.getCycleId());
                    sbRunningTasks.append("\n");
                }
            }
        }
        sbRunningTasks.append("]");
        LOGGER.debug(sbRunningTasks.toString());
    }
    
    /**
     * 获取运行的周期任务数
     * @return 获取运行的周期任务数
     */
    @Override
    public int getRunningTaskNum()
    {
        int runningN = 0;
        synchronized (runningTasks)
        {
            for (HashMap<String, CycleTask> cycleTasks : runningTasks.values())
            {
                for (CycleTask cycleTask : cycleTasks.values())
                {
                    if (RunningState.INIT == cycleTask.getState()
                            || RunningState.START == cycleTask.getState())
                    {
                        runningN++;
                    }
                }
            }
        }
        return runningN;
    }
    
    /**
     * 获取运行的周期任务的权重和
     * @return 获取运行的周期任务的权重和
     */
    public int getRunningTaskWeights()
    {
        int weights = 0;
        synchronized (runningTasks)
        {
            for (HashMap<String, CycleTask> cycleTasks : runningTasks.values())
            {
                for (CycleTask cycleTask : cycleTasks.values())
                {
                    if (RunningState.INIT == cycleTask.getState()
                            || RunningState.START == cycleTask.getState())
                    {
                        weights += cycleTask.getTaskWeight();
                    }
                }
            }
        }
        return weights;
    }
    
    /**
     * 获取运行队列中的任务周期详细信息列表
     * @return 运行队列中的任务周期详细信息列表
     */
    public List<CycleTaskDetailEntity> getCycleDetailsRunning()
    {
        List<CycleTaskDetailEntity> cycleDetails = new ArrayList<CycleTaskDetailEntity>();
        CycleTaskDetailEntity cycleDetail;
        Date currDate = new Date();
        synchronized (runningTasks)
        {
            for (HashMap<String, CycleTask> cycleTasks : runningTasks.values())
            {
                for (CycleTask cycleTask : cycleTasks.values())
                {
                    if (RunningState.INIT == cycleTask.getState()
                            || RunningState.START == cycleTask.getState())
                    {
                        cycleDetail = new CycleTaskDetailEntity();
                        cycleDetail.setTaskId(cycleTask.getTaskId());
                        cycleDetail.setCycleId(cycleTask.getCycleId());
                        cycleDetail.setState(cycleTask.getState());
                        cycleDetail.setStartTime(cycleTask.getStartTime());
                        cycleDetail.setCurrentTime(currDate);
                        cycleDetail.setPriority(cycleTask.getPriority());
                        cycleDetail.setStartCycleId(cycleTask.getStartCycleId());
                        cycleDetail.setWeight(cycleTask.getTaskWeight());
                        cycleDetail.setDependIdList(cycleTask.getDependTaskIdList());
                        cycleDetail.setCycleDependFlag(cycleTask.getCycleDependFlag());
                        cycleDetails.add(cycleDetail);
                    }
                }
            }
        }
        return cycleDetails;
    }
    
    /**
     * 获取等待队列中的任务周期详细信息列表
     * @return 等队列中的任务周期详细信息列表
     */
    public List<CycleTaskDetailEntity> getCycleDetailsWaitting()
    {
        List<CycleTaskDetailEntity> cycleDetails = new ArrayList<CycleTaskDetailEntity>();
        CycleTaskDetailEntity cycleDetail;
        Date currDate = new Date();
        synchronized (runningTasks)
        {
            for (CycleTask cycleTask : waittingQueue)
            {
                cycleDetail = new CycleTaskDetailEntity();
                cycleDetail.setTaskId(cycleTask.getTaskId());
                cycleDetail.setCycleId(cycleTask.getCycleId());
                cycleDetail.setState(cycleTask.getState());
                cycleDetail.setStartTime(cycleTask.getStartTime());
                cycleDetail.setCurrentTime(currDate);
                cycleDetail.setPriority(cycleTask.getPriority());
                cycleDetail.setStartCycleId(cycleTask.getStartCycleId());
                cycleDetail.setWeight(cycleTask.getTaskWeight());
                cycleDetail.setDependIdList(cycleTask.getDependTaskIdList());
                cycleDetail.setCycleDependFlag(cycleTask.getCycleDependFlag());
                cycleDetails.add(cycleDetail);
            }
        }
        return cycleDetails;
    }
    
    /**
     * 获取运行的周期任务
     * @return 获取运行的周期任务
     */
    @Override
    public String getRunningTasks()
    {
        StringBuilder runningSb = new StringBuilder();
        synchronized (runningTasks)
        {
            for (HashMap<String, CycleTask> cycleTasks : runningTasks.values())
            {
                for (CycleTask cycleTask : cycleTasks.values())
                {
                    if (RunningState.INIT == cycleTask.getState()
                            || RunningState.START == cycleTask.getState())
                    {
                        runningSb.append(cycleTask.getTaskId());
                        runningSb.append(',');
                        runningSb.append(cycleTask.getCycleId());
                        runningSb.append(';');
                    }
                }
            }
        }
        return runningSb.toString();
    }
    
    /**
     * 获取等待运行的周期任务
     * @return 获取等待运行的周期任务
     */
    @Override
    public String getWaittingTasks()
    {
        StringBuilder waittingSb = new StringBuilder();
        synchronized (runningTasks)
        {
            for (HashMap<String, CycleTask> cycleTasks : waittingTasks.values())
            {
                for (CycleTask cycleTask : cycleTasks.values())
                {
                    waittingSb.append(cycleTask.getTaskId());
                    waittingSb.append(',');
                    waittingSb.append(cycleTask.getCycleId());
                    waittingSb.append(';');
                }
            }
        }
        return waittingSb.toString();
    }
    
    /**
     * 获取等待运行的周期任务数
     * @return 获取等待运行的周期任务数
     */
    @Override
    public int getWaittingTaskNum()
    {
        int waittingN = 0;
        synchronized (runningTasks)
        {
            waittingN = waittingQueue.size();
        }
        return waittingN;
    }
    
    private void add2WaittingTasks(Long taskId, String cycleId,
            CycleTask cycleTask)
    {
        synchronized (runningTasks)
        {
            //添加到等待任务hashMap中
            if (waittingTasks.containsKey(taskId))
            {
                waittingTasks.get(taskId).put(cycleId, cycleTask);
            }
            else
            {
                HashMap<String, CycleTask> cycleTaskMap = new HashMap<String, CycleTask>();
                cycleTaskMap.put(cycleId, cycleTask);
                waittingTasks.put(taskId, cycleTaskMap);
            }
            
            //添加到等待优先级队列中
            insertSorted(cycleTask);
        }
        //记录添加到等待队列的元素
        LOGGER.debug("put cycleTask[taskId=[{}],cycleId=[{}]] into waittingQueue[size={}]. ",
                new Object[] { taskId, cycleId, waittingQueue.size() });
    }
    
    /**
     * 计算直接依赖当前任务周期的所有任务周期集合,仅支持同周期依赖
     * @param task 任务
     * @param tasks 直接依赖于task的任务集合
     * @param cycleId 周期Id
     * @return 依赖当前任务周期的所有任务周期集合
     * @exception Exception 异常
     */
    @Override
    public List<CycleDependRelation> getAllCycleDeppingRs(TaskEntity task,
            List<TaskEntity> tasks, String cycleId) throws Exception
    {
        if (null == task || null == task.getTaskId() || null == cycleId)
        {
            return new ArrayList<CycleDependRelation>(0);
        }
        
        //最大开始时间
        Date curMaxStartTime = task.getStartTime();
        if (null == curMaxStartTime
                || curMaxStartTime.before(TccConfig.getBenchDate()))
        {
            curMaxStartTime = TccConfig.getBenchDate();
        }
        
        //当前周期的起始时间
        Date currentDate = TccUtil.covCycleID2Date(cycleId);
        
        //处在起始时间之前的任务周期不会被任何周期依赖
        if (currentDate.before(curMaxStartTime))
        {
            return new ArrayList<CycleDependRelation>(0);
        }
        
        List<CycleDependRelation> cycleDeppedRLst = new ArrayList<CycleDependRelation>();
        
        Long taskId = task.getTaskId();
        int cycleType = CycleType.toCycleType(task.getCycleType());
        boolean seqDepend = task.getCycleDependFlag();
        int cycleLength = task.getCycleLength();
        
        //任务周期不能超越当前时间
        Date maxTime = new Date();
        
        //如果是顺序依赖，则后一个任务周期依赖于当前任务周期。
        if (seqDepend)
        {
            //后一个周期
            Date nextDate = TccUtil.roll2CycleStart(currentDate,
                    cycleType,
                    cycleLength);
            String nextDateCycleId = TccUtil.covDate2CycleID(nextDate);
            
            //基准时间或者开始时间之前的周期可不用考虑依赖关系
            if (!nextDate.before(currentDate) && nextDate.before(maxTime))
            {
                //周期依赖关系
                CycleDependRelation cycleDepRT = new CycleDependRelation();
                cycleDepRT.setDependTaskId(taskId);
                cycleDepRT.setDependCycleId(nextDateCycleId);
                cycleDepRT.setIgnoreError(false);
                if (!cycleDeppedRLst.contains(cycleDepRT))
                {
                    cycleDeppedRLst.add(cycleDepRT);
                }
            }
        }
        
        //周期依赖关系
        CycleDependRelation cycleDepR;
        //周期大小比较
        int compareValue;
        //依赖关系
        DependRelation dependRelation;
        Long deppingTaskId;
        int deppingCycleType;
        int deppingCycleLength;
        for (TaskEntity depingTask : tasks)
        {
            dependRelation = TccUtil.filterDepRs(taskId,
                    depingTask.getDependTaskIdList());
            
            if (null != dependRelation)
            {
                deppingTaskId = depingTask.getTaskId();
                deppingCycleType = CycleType.toCycleType(depingTask.getCycleType());
                deppingCycleLength = depingTask.getCycleLength();
                
                //周期大小比较
                compareValue = deppingCycleType * depingTask.getCycleLength()
                        - cycleType * cycleLength;
                
                //如果大周期类型依赖于小周期类型并且是全周期依赖
                if (compareValue > 0)
                {
                    //小周期的当前周期的起始时间
                    Date smallcurrDate = TccUtil.covCycleID2Date(cycleId);
                    //计算smallcurrDate所在的大周期的周期时间
                    Date depingPreDate = TccUtil.roll2CycleStartOnBenchDate(smallcurrDate,
                            deppingCycleType,
                            depingTask.getCycleLength(),
                            0);
                    Date depingNextDate = TccUtil.roll2CycleStart(depingPreDate,
                            deppingCycleType,
                            depingTask.getCycleLength());
                    
                    Date nextSmallDate = TccUtil.roll2CycleStart(smallcurrDate,
                            cycleType,
                            cycleLength);
                    
                    //如果是全周期依赖或者是大周期内的最后一个小周期
                    if (dependRelation.isFullCycleDepend()
                            || !nextSmallDate.before(depingNextDate))
                    {
                        //允许最早开始的时间
                        Date earliestStartTime = depingTask.getStartTime();
                        if (null == earliestStartTime
                                || earliestStartTime.before(TccConfig.getBenchDate()))
                        {
                            earliestStartTime = TccConfig.getBenchDate();
                        }
                        
                        //基准时间之前的周期可不用考虑依赖关系
                        if (!depingPreDate.before(earliestStartTime)
                                && depingPreDate.before(maxTime))
                        {
                            cycleDepR = new CycleDependRelation();
                            cycleDepR.setDependTaskId(deppingTaskId);
                            cycleDepR.setDependCycleId(TccUtil.covDate2CycleID(depingPreDate));
                            cycleDepR.setIgnoreError(dependRelation.isIgnoreError());
                            if (!cycleDeppedRLst.contains(cycleDepR))
                            {
                                cycleDeppedRLst.add(cycleDepR);
                            }
                        }
                    }
                }
                else
                {
                    //小周期依赖与大周期(已知)
                    //大周期的当前周期的起始时间
                    Date largeCurrentDate = TccUtil.covCycleID2Date(cycleId);
                    //大周期的后一个周期的起始时间
                    Date largenextDate = TccUtil.roll2CycleStart(largeCurrentDate,
                            cycleType,
                            cycleLength);
                    
                    //做一些微量的时间偏移，以便排除掉最后一个小周期
                    largenextDate = tuneTiny(largenextDate, false);
                    
                    //获取上一个大周期内的全部小周期ID
                    List<String> dependCycleIds = generateCycleIDs(largeCurrentDate,
                            largenextDate,
                            deppingCycleType,
                            deppingCycleLength);
                    
                    //允许最早开始的时间
                    Date earliestStartTime = depingTask.getStartTime();
                    if (null == earliestStartTime
                            || earliestStartTime.before(TccConfig.getBenchDate()))
                    {
                        earliestStartTime = TccConfig.getBenchDate();
                    }
                    
                    Date deppingCycleTime;
                    
                    for (String depCycleId : dependCycleIds)
                    {
                        //基准时间或者开始时间之前的周期可不用考虑依赖关系
                        deppingCycleTime = TccUtil.covCycleID2Date(depCycleId);
                        if (!deppingCycleTime.before(earliestStartTime)
                                && deppingCycleTime.before(maxTime))
                        {
                            cycleDepR = new CycleDependRelation();
                            cycleDepR.setDependTaskId(deppingTaskId);
                            cycleDepR.setDependCycleId(depCycleId);
                            cycleDepR.setIgnoreError(dependRelation.isIgnoreError());
                            if (!cycleDeppedRLst.contains(cycleDepR))
                            {
                                cycleDeppedRLst.add(cycleDepR);
                            }
                        }
                    }
                }
            }
        }
        
        return cycleDeppedRLst;
    }
    
    /**
     * 获取任务周期的全部周期依赖关系
     * @throws CException 统一封装的异常
     */
    private List<CycleDependRelation> getAllCycleDepRs(Long taskId,
            String cycleId, TaskEntity task) throws CException
    {
        List<CycleDependRelation> cycleDepRLst = new ArrayList<CycleDependRelation>();
        int cycleType = CycleType.toCycleType(task.getCycleType());
        boolean seqDepend = task.getCycleDependFlag();
        String depends = task.getDependTaskIdList();
        int cycleLength = task.getCycleLength();
        //如果是顺序依赖，需要依赖于前一个周期。
        if (seqDepend)
        {
            //当前周期的起始时间
            Date currentDate = TccUtil.covCycleID2Date(cycleId);
            
            //依赖任务的前一个周期
            Date priviousDate = TccUtil.roll2CycleStart(currentDate,
                    cycleType,
                    -cycleLength);
            String priviousCycleId = TccUtil.covDate2CycleID(priviousDate);
            
            //最大开始时间
            Date maxStartTime = task.getStartTime();
            if (null == maxStartTime
                    || maxStartTime.before(TccConfig.getBenchDate()))
            {
                maxStartTime = TccConfig.getBenchDate();
            }
            
            //基准时间或者开始时间之前的周期可不用考虑依赖关系
            if (!priviousDate.before(maxStartTime))
            {
                //周期依赖关系
                CycleDependRelation cycleDepR = new CycleDependRelation();
                cycleDepR.setDependTaskId(taskId);
                cycleDepR.setDependCycleId(priviousCycleId);
                cycleDepR.setIgnoreError(false);
                if (!cycleDepRLst.contains(cycleDepR))
                {
                    cycleDepRLst.add(cycleDepR);
                }
            }
        }
        
        if (!StringUtils.isEmpty(depends))
        {
            //依赖关系列表
            HashMap<Long, DependRelation> dependRelationLst = new HashMap<Long, DependRelation>();
            
            List<Long> taskIds = new ArrayList<Long>();
            
            //解析任务依赖依赖关系
            TccUtil.parseDependIdList(taskId,
                    depends,
                    dependRelationLst,
                    taskIds);
            
            //一次获取全部的依赖任务
            List<TaskEntity> taskLst = tccDao.getTaskList(taskIds);
            
            //周期依赖关系
            CycleDependRelation cycleDepR;
            //依赖任务id
            Long depTaskId;
            //忽略依赖错误
            boolean bIgnoreDependError;
            //依赖周期类型
            int depCycleType;
            //周期大小比较
            int compareValue;
            
            for (TaskEntity taskEntity : taskLst)
            {
                depTaskId = taskEntity.getTaskId();
                depCycleType = CycleType.toCycleType(taskEntity.getCycleType());
                
                //周期大小比较
                compareValue = cycleType * cycleLength - depCycleType
                        * taskEntity.getCycleLength();
                
                //如果大周期类型依赖于小周期类型并且是全周期依赖
                if (compareValue > 0)
                {
                    //大周期的当前周期的起始时间
                    Date currentDate = TccUtil.covCycleID2Date(cycleId);
                    //大周期的前一个周期的起始时间
                    Date priviousDate;
                    
                    //如果是同周期依赖
                    if (dependRelationLst.get(depTaskId).isSameCycleDepend())
                    {
                        priviousDate = currentDate;
                        //大周期的后一个周期的起始时间
                        currentDate = TccUtil.roll2CycleStart(currentDate,
                                cycleType,
                                cycleLength);
                    }
                    else
                    {
                        priviousDate = TccUtil.roll2CycleStart(currentDate,
                                cycleType,
                                -cycleLength);
                    }
                    
                    //做一些微量的时间偏移，以便排除掉最后一个小周期
                    currentDate = tuneTiny(currentDate, false);
                    
                    //获取上一个大周期内的全部小周期ID
                    List<String> dependCycleIds = generateCycleIDs(priviousDate,
                            currentDate,
                            CycleType.toCycleType(taskEntity.getCycleType()),
                            taskEntity.getCycleLength());
                    
                    //允许最早开始的时间
                    Date earliestStartTime = taskEntity.getStartTime();
                    if (null == earliestStartTime
                            || earliestStartTime.before(TccConfig.getBenchDate()))
                    {
                        earliestStartTime = TccConfig.getBenchDate();
                    }
                    
                    //如果是全周期依赖
                    if (dependRelationLst.get(depTaskId).isFullCycleDepend())
                    {
                        for (String depCycleId : dependCycleIds)
                        {
                            //基准时间或者开始时间之前的周期可不用考虑依赖关系
                            priviousDate = TccUtil.covCycleID2Date(depCycleId);
                            if (!priviousDate.before(earliestStartTime))
                            {
                                cycleDepR = new CycleDependRelation();
                                cycleDepR.setDependTaskId(depTaskId);
                                cycleDepR.setDependCycleId(depCycleId);
                                bIgnoreDependError = dependRelationLst.get(depTaskId)
                                        .isIgnoreError();
                                cycleDepR.setIgnoreError(bIgnoreDependError);
                                if (!cycleDepRLst.contains(cycleDepR))
                                {
                                    cycleDepRLst.add(cycleDepR);
                                }
                            }
                        }
                    }
                    else
                    {
                        //非全周期依赖，选择最后一个周期
                        if (!dependCycleIds.isEmpty())
                        {
                            //基准时间或者开始时间之前的周期可不用考虑依赖关系
                            String depCycleId = Collections.max(dependCycleIds);
                            priviousDate = TccUtil.covCycleID2Date(depCycleId);
                            if (!priviousDate.before(earliestStartTime))
                            {
                                cycleDepR = new CycleDependRelation();
                                cycleDepR.setDependTaskId(depTaskId);
                                cycleDepR.setDependCycleId(depCycleId);
                                bIgnoreDependError = dependRelationLst.get(depTaskId)
                                        .isIgnoreError();
                                cycleDepR.setIgnoreError(bIgnoreDependError);
                                if (!cycleDepRLst.contains(cycleDepR))
                                {
                                    cycleDepRLst.add(cycleDepR);
                                }
                            }
                        }
                    }
                }
                else
                {
                    //当前周期的起始时间
                    Date currentDate = TccUtil.covCycleID2Date(cycleId);
                    
                    //依赖任务的前一个周期
                    String priviousCycleId = getPrevCycleId(currentDate,
                            cycleType,
                            cycleLength,
                            depCycleType,
                            taskEntity.getCycleLength());
                    
                    //如果是同周期依赖
                    if (dependRelationLst.get(depTaskId).isSameCycleDepend())
                    {
                        //如果是同周期依赖，就在前周期依赖的基础上往后推一个周期
                        priviousCycleId = TccUtil.covDate2CycleID(TccUtil.roll2CycleStart(TccUtil.covCycleID2Date(priviousCycleId),
                                depCycleType,
                                taskEntity.getCycleLength()));
                    }
                    
                    //允许最早开始的时间
                    Date earliestStartTime = taskEntity.getStartTime();
                    if (null == earliestStartTime
                            || earliestStartTime.before(TccConfig.getBenchDate()))
                    {
                        earliestStartTime = TccConfig.getBenchDate();
                    }
                    Date priviousDate = TccUtil.covCycleID2Date(priviousCycleId);
                    //基准时间之前的周期可不用考虑依赖关系
                    if (!priviousDate.before(earliestStartTime))
                    {
                        cycleDepR = new CycleDependRelation();
                        cycleDepR.setDependTaskId(depTaskId);
                        cycleDepR.setDependCycleId(priviousCycleId);
                        bIgnoreDependError = dependRelationLst.get(depTaskId)
                                .isIgnoreError();
                        cycleDepR.setIgnoreError(bIgnoreDependError);
                        if (!cycleDepRLst.contains(cycleDepR))
                        {
                            cycleDepRLst.add(cycleDepR);
                        }
                    }
                }
            }
        }
        
        return cycleDepRLst;
    }
    
    /**
     * 依赖关系是否全部ok
     */
    private boolean isDependsOk(List<CycleDependRelation> cycleDepRLst,
            Long taskId) throws CException
    {
        if (null == cycleDepRLst) 
        {
            return true;
        }
        List<TaskRunningStateEntity> taskIdCycleIDStateLst = new ArrayList<TaskRunningStateEntity>();
        TaskRunningStateEntity taskRS;
        for (CycleDependRelation cycleDepR : cycleDepRLst)
        {
            taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(cycleDepR.getDependTaskId());
            taskRS.setCycleId(cycleDepR.getDependCycleId());
            taskRS.setState(RunningState.SUCCESS);
            taskIdCycleIDStateLst.add(taskRS);
            
            //如果依赖于本任务,虚拟成功标识也认为是成功
            if (taskId.equals(cycleDepR.getDependTaskId()))
            {
                taskRS = new TaskRunningStateEntity();
                taskRS.setTaskId(cycleDepR.getDependTaskId());
                taskRS.setCycleId(cycleDepR.getDependCycleId());
                taskRS.setState(RunningState.VSUCCESS);
                taskIdCycleIDStateLst.add(taskRS);
            }
            
            //如果忽略依赖任务执行出错的状态
            if (cycleDepR.isIgnoreError())
            {
                taskRS = new TaskRunningStateEntity();
                taskRS.setTaskId(cycleDepR.getDependTaskId());
                taskRS.setCycleId(cycleDepR.getDependCycleId());
                taskRS.setState(RunningState.ERROR);
                taskIdCycleIDStateLst.add(taskRS);
            }
        }
        //当前的周期任务所依赖任务的周期必然已经创建了至少一个
        
        //存在的周期任务数
        
        //int existNum = tccDao.getTaskRunningStateExceptStateCount(taskIdCycleIDStateLst);
        int existNum = cycleDepRLst.size();
        
        //存在且状态完成的周期任务数
        int existFinishedNum = tccDao.getTaskRunningStateCount(taskIdCycleIDStateLst);
        if (existNum == existFinishedNum)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 获取时间上满足运行条件的最大的周期Id
     * @param cycleOffSet 周期偏移
     */
    private String maxCycleIdTimeisOK(String cycleOffSet) throws CException
    {
        Date currentDate = CommonUtils.getCrruentTime();
        //cycleOffSet 形如：xMxD xhxm
        
        String[] cycleOffSetArr = new String[1];
        cycleOffSetArr[0] = cycleOffSet;
        //获取偏移月数
        int mouths = TccUtil.extractNum(cycleOffSetArr, "M");
        //获取偏移天数
        int days = TccUtil.extractNum(cycleOffSetArr, "D");
        //获取偏移小时数
        int hours = TccUtil.extractNum(cycleOffSetArr, "h");
        //获取偏移分钟数
        
        int minutes = TccUtil.extractNum(cycleOffSetArr, "m");
        
        Calendar cDate = Calendar.getInstance();
        
        //将周期加上周期偏移
        cDate.setTime(currentDate);
        cDate.add(Calendar.MONTH, -mouths);
        cDate.add(Calendar.DAY_OF_MONTH, -days);
        cDate.add(Calendar.HOUR_OF_DAY, -hours);
        cDate.add(Calendar.MINUTE, -minutes);
        
        return TccUtil.covDate2CycleID(cDate.getTime());
    }
    
    /**
     * 将date提前或延迟数秒
     */
    private Date tuneTiny(Date date, boolean bDelay)
    {
        Calendar cDate = Calendar.getInstance();
        //校准start为本周期起始点
        cDate.setTime(date);
        if (bDelay)
        {
            cDate.add(Calendar.SECOND, TccConfig.TUNE_SECONDS);
        }
        else
        {
            cDate.add(Calendar.SECOND, -TccConfig.TUNE_SECONDS);
        }
        return cDate.getTime();
    }
    
    /**
     * 仅仅停止运行中的任务，暂时不移除
     */
    private void stopCycleTask(Long taskId, String cycleId)
    {
        //如果是停止，停止所有已经运行的任务
        CycleTask cycleTask = null;
        synchronized (runningTasks)
        {
            //先从等待队列中移除
            HashMap<String, CycleTask> cycleTaskWaits = waittingTasks.get(taskId);
            if (null != cycleTaskWaits)
            {
                cycleTask = cycleTaskWaits.remove(cycleId);
                if (null != cycleTask)
                {
                    LOGGER.debug("start to stop cycleTask[taskId=[{}],cycleId=[{}]]",
                            taskId,
                            cycleId);
                    //从排序优先级队列中移除
                    waittingQueue.remove(cycleTask);
                    LOGGER.debug("finish to stop cycleTask[taskId=[{}],cycleId=[{}]]",
                            taskId,
                            cycleId);
                }
                else
                {
                    LOGGER.debug("no cycleTask[taskId=[{}],cycleId=[{}]] is found.",
                            taskId,
                            cycleId);
                }
            }
            
            if (runningTasks.containsKey(taskId))
            {
                HashMap<String, CycleTask> cycleTasks = runningTasks.get(taskId);
                if (cycleTasks.containsKey(cycleId))
                {
                    //运行队列中的周期任务
                    cycleTask = cycleTasks.get(cycleId);
                    if (null != cycleTask)
                    {
                        LOGGER.debug("start to stop cycleTask[taskId=[{}],cycleId=[{}]]",
                                taskId,
                                cycleId);
                        //停止执行周期任务
                        cycleTask.stopCycleTask();
                        LOGGER.debug("finish to stop cycleTask[taskId=[{}],cycleId=[{}]]",
                                taskId,
                                cycleId);
                    }
                    else
                    {
                        LOGGER.debug("no cycleTask[taskId=[{}],cycleId=[{}]] is found.",
                                taskId,
                                cycleId);
                    }
                }
            }
        }
    }
    
    /**
     * 停止指定任务的所有周期的运行，并从队列中移除
     */
    private void stopRunningCycleTasks(Long taskId)
    {
        //如果是停止，停止所有已经运行的任务
        
        HashMap<String, CycleTask> cycleTasks = null;
        HashMap<String, CycleTask> waittingCycleTasks = null;
        
        synchronized (runningTasks)
        {
            if (runningTasks.containsKey(taskId))
            {
                //同时移除等待队列与运行队列中的周期任务
                cycleTasks = runningTasks.remove(taskId);
                waittingCycleTasks = waittingTasks.remove(taskId);
                
                //从优先级队列中移除
                for (CycleTask cycleTask : waittingCycleTasks.values())
                {
                    waittingQueue.remove(cycleTask);
                }
                
            }
        }
        
        if (null != cycleTasks)
        {
            
            for (CycleTask cycleTask : cycleTasks.values())
            {
                LOGGER.debug("start to stop cycleTask[taskId=[{}],cycleId=[{}]]",
                        cycleTask.getTaskId(),
                        cycleTask.getCycleId());
                //停止执行周期任务
                cycleTask.stopCycleTask();
                LOGGER.debug("finish to stop cycleTask[taskId=[{}],cycleId=[{}]]",
                        cycleTask.getTaskId(),
                        cycleTask.getCycleId());
            }
            LOGGER.debug("all cycleTasks of Task[{}] have stopped.", taskId);
        }
        else
        {
            LOGGER.debug("no any cycleTask of Task[{}] needs stop.", taskId);
        }
    }
    
    /**
     * 停止或者补齐周期任务
     * @param tasks 已经启用的任务
     */
    private void stopfillCycleTask(List<TaskEntity> tasks) throws Exception
    {
        try
        {
            for (TaskEntity task : tasks)
            {
                if (TaskState.STOP == task.getTaskState())
                {
                    //如果是停止，停止所有已经运行的任务
                    stopRunningCycleTasks(task.getTaskId());
                    
                    //不启用任务，防止任务后续做无用的调度
                    TaskEntity taskE = new TaskEntity();
                    taskE.setTaskId(task.getTaskId());
                    taskE.setTaskEnableFlag(false);
                    tccDao.updateTask(taskE);
                }
                else
                {
                    //补齐task任务的任务运行状态到当前周期
                    fillCycle2Current(task.getTaskId(),
                            CycleType.toCycleType(task.getCycleType()),
                            task.getCycleLength(),
                            task.getDependTaskIdList());
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("fill cycleId to Current error!", e);
            throw e;
        }
    }
    
    /**
     *
     * 产生有效时间之后的的所有周期ID集合
     * 
     * @param taskId 任务Id
     * @return 有效时间之后的的所有周期ID集合
     * @throws Exception 异常
     */
    private List<String> generateAllCycleIDS(Long taskId) throws Exception
    {
        TaskEntity task = null;
        
        try
        {
            task = tccDao.getTask(taskId);
        }
        catch (CException e)
        {
            LOGGER.error("generate AllCycleIDS failed!", e);
            throw e;
        }
        
        if (null == task)
        {
            return new ArrayList<String>(0);
        }
        
        Date maxCycleIdDate = task.getStartTime();
        if (null == maxCycleIdDate
                || maxCycleIdDate.before(TccConfig.getBenchDate()))
        {
            maxCycleIdDate = TccConfig.getBenchDate();
        }
        
        Date currentTime = CommonUtils.getCrruentTime();
        List<String> cycleIDs = generateCycleIDs(maxCycleIdDate,
                currentTime,
                CycleType.toCycleType(task.getCycleType()),
                task.getCycleLength());
        return cycleIDs;
    }
    
    /**
     * 补齐任务的任务运行状态到当前周期,始终填充到benchTime
     */
    private void fillCycle2Current(Long taskId, int cycleType, int cycleLength,
            String depends) throws CException
    {
        //获取最大的cycleID,补齐右边
        String maxCycleID = tccDao.getMaxCycleID(taskId);
        Date maxCycleIdDate = TccUtil.covCycleID2Date(maxCycleID);
        if (null == maxCycleIdDate
                || maxCycleIdDate.before(TccConfig.getBenchDate()))
        {
            //任务启动时间startTime
            maxCycleIdDate = TccConfig.getBenchDate();
        }
        Date currentTime = CommonUtils.getCrruentTime();
        List<String> cycleIDRights = generateCycleIDs(maxCycleIdDate,
                currentTime,
                cycleType,
                cycleLength);
        //如果已经创建了最大周期，则去掉maxCycleID
        if (null != maxCycleID)
        {
            cycleIDRights.remove(maxCycleID);
        }
        
        //补齐左边
        String minCycleID = tccDao.getMinCycleID(taskId);
        Date minCycleIdDate = TccUtil.covCycleID2Date(minCycleID);
        List<String> cycleIDLefts = null;
        if (null != minCycleIdDate
                && minCycleIdDate.after(TccConfig.getBenchDate()))
        {
            //任务启动时间startTime
            cycleIDLefts = generateCycleIDs(TccConfig.getBenchDate(),
                    minCycleIdDate,
                    cycleType,
                    cycleLength);
            cycleIDLefts.remove(minCycleID);
        }
        
        List<String> validCycleIDs = cycleIDRights;
        if (null != cycleIDLefts)
        {
            validCycleIDs.addAll(cycleIDLefts);
        }
        //List<String> validCycleIDs = filterValidCycleIDs(taskId, cycleType, cycleLength, cycleIDs, depends);
        
        //初始化任务运行状态信息
        TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
        taskRS.setTaskId(taskId);
        taskRS.setState(RunningState.INIT);
        //必需使用空串，因为这个字段要使用到concat函数
        taskRS.setBeginDependTaskList("");
        taskRS.setFinishDependTaskList("");
        for (String cycleId : validCycleIDs)
        {
            taskRS.setCycleId(cycleId);
            tccDao.addTaskRunningState(taskRS);
        }
        
    }
    
    //有效的周期ID是指：依赖任务的上一个周期ID的运行状态信息全部存在
    private List<String> filterValidCycleIDs(long taskId, int cycleType,
            int cycleLength, List<String> cycleIDs, String dependTasks)
            throws CException
    {
        List<String> validCycleIDs = new ArrayList<String>();
        if (!cycleIDs.isEmpty() && !StringUtils.isEmpty(dependTasks))
        {
            //依赖关系列表
            HashMap<Long, DependRelation> dependRelationLst = new HashMap<Long, DependRelation>();
            
            List<Long> taskIds = new ArrayList<Long>();
            
            //解析任务依赖依赖关系
            TccUtil.parseDependIdList(taskId,
                    dependTasks,
                    dependRelationLst,
                    taskIds);
            
            List<TaskEntity> taskLst = tccDao.getTaskList(taskIds);
            
            List<TaskRunningStateEntity> taskIdCycleIdLst = new ArrayList<TaskRunningStateEntity>();
            TaskRunningStateEntity taskIdCycleId;
            Date curTime;
            String priviousCycleId;
            int depCycleType;
            int compareValue;
            for (String cycleId : cycleIDs)
            {
                taskIdCycleIdLst.clear();
                curTime = TccUtil.covCycleID2Date(cycleId);
                for (TaskEntity task : taskLst)
                {
                    //未处理大周期全周期（同周期）依赖于小周期的情况
                    priviousCycleId = getPrevCycleId(curTime,
                            cycleType,
                            cycleLength,
                            CycleType.toCycleType(task.getCycleType()),
                            task.getCycleLength());
                    //如果是同周期依赖
                    if (dependRelationLst.get(task.getTaskId())
                            .isSameCycleDepend())
                    {
                        depCycleType = CycleType.toCycleType(task.getCycleType());
                        
                        //周期大小比较
                        compareValue = cycleType * cycleLength - depCycleType
                                * task.getCycleLength();
                        
                        //如果大周期类型依赖于小周期类型并且是全周期依赖
                        if (compareValue > 0)
                        {
                            //大周期的当前周期的起始时间
                            Date currentDate = TccUtil.covCycleID2Date(cycleId);
                            //大周期的前一个周期的起始时间
                            Date priviousDate = currentDate;
                            //大周期的后一个周期的起始时间
                            currentDate = TccUtil.roll2CycleStart(currentDate,
                                    cycleType,
                                    cycleLength);
                            
                            //做一些微量的时间偏移，以便排除掉最后一个小周期
                            currentDate = tuneTiny(currentDate, false);
                            
                            //获取上一个大周期内的全部小周期ID
                            List<String> dependCycleIds = generateCycleIDs(priviousDate,
                                    currentDate,
                                    CycleType.toCycleType(task.getCycleType()),
                                    task.getCycleLength());
                            String lastSmallCycleId;
                            if (null != dependCycleIds
                                    && !dependCycleIds.isEmpty())
                            {
                                lastSmallCycleId = Collections.max(dependCycleIds);
                                //允许最早开始的时间
                                Date earliestStartTime = task.getStartTime();
                                if (null == earliestStartTime
                                        || earliestStartTime.before(TccConfig.getBenchDate()))
                                {
                                    earliestStartTime = TccConfig.getBenchDate();
                                }
                                
                                //基准时间或者开始时间之前的周期可不用考虑依赖关系
                                priviousDate = TccUtil.covCycleID2Date(lastSmallCycleId);
                                if (!priviousDate.before(earliestStartTime))
                                {
                                    priviousCycleId = lastSmallCycleId;
                                    
                                    taskIdCycleId = new TaskRunningStateEntity();
                                    taskIdCycleId.setTaskId(task.getTaskId());
                                    taskIdCycleId.setCycleId(priviousCycleId);
                                    taskIdCycleIdLst.add(taskIdCycleId);
                                }
                            }
                        }
                        else
                        {
                            //如果是同周期依赖，就在前周期依赖的基础上往后推一个周期
                            priviousCycleId = TccUtil.covDate2CycleID(TccUtil.roll2CycleStart(TccUtil.covCycleID2Date(priviousCycleId),
                                    CycleType.toCycleType(task.getCycleType()),
                                    task.getCycleLength()));
                            
                            taskIdCycleId = new TaskRunningStateEntity();
                            taskIdCycleId.setTaskId(task.getTaskId());
                            taskIdCycleId.setCycleId(priviousCycleId);
                            taskIdCycleIdLst.add(taskIdCycleId);
                        }
                    }
                    else
                    {
                        taskIdCycleId = new TaskRunningStateEntity();
                        taskIdCycleId.setTaskId(task.getTaskId());
                        taskIdCycleId.setCycleId(priviousCycleId);
                        taskIdCycleIdLst.add(taskIdCycleId);
                    }
                }
                
                //依赖任务的上一个周期ID的运行状态信息存在的个数
                int existNum = tccDao.getTaskRunningStateExceptStateCount(taskIdCycleIdLst);
                
                //依赖任务的上一个周期ID的运行状态信息全部存在
                if (taskIdCycleIdLst.size() == existNum)
                {
                    validCycleIDs.add(cycleId);
                }
            }
        }
        else
        {
            //不存在依赖任务的任务不需要过滤周期ID
            validCycleIDs.addAll(cycleIDs);
        }
        return validCycleIDs;
    }
    
    /** 
     * 获取上一 依赖周期Id
     */
    private String getPrevCycleId(Date curTime, int cycleType, int cycleLength,
            int depCycleType, int depCycleLength)
    {
        Date priviousDate;
        int compareValue;
        //周期大小比较
        compareValue = cycleType * cycleLength - depCycleType * depCycleLength;
        
        if (compareValue > 0)
        {
            //如果大周期类型依赖与小周期类型
            
            //计算出每一个依赖任务的上一个周期ID
            //大周期依赖的小周期的上一个周期，是指上一个大周期内的启动的最后一个小周期
            priviousDate = TccUtil.roll2CycleStartOnBenchDate(curTime,
                    depCycleType,
                    depCycleLength,
                    0);
            
            if (curTime.equals(priviousDate))
            {
                priviousDate = TccUtil.roll2CycleStart(curTime,
                        depCycleType,
                        -depCycleLength);
            }
        }
        else
        {
            //如果小周期类型依赖于大周期类型，相同周期依赖
            //计算出每一个依赖任务的上一个周期ID
            //小周期依赖的大周期的上一个周期，是指小周期所在大周期的上一个大周期
            priviousDate = TccUtil.roll2CycleStartOnBenchDate(curTime,
                    depCycleType,
                    depCycleLength,
                    -1);
        }
        
        return TccUtil.covDate2CycleID(priviousDate);
    }
    
    /**
     * 产生从start到end时间段包含的完整周期IDs.(start,end]
     * start不能小于基准时间，否则取基准时间为start
     * @param start 起始日期
     * @param end 结束日期
     * @param cycleType 周期类型
     * @param cycleLength 周期长度
     * @return 周期标识集合
     * @see [类、类#方法、类#成员]
     */
    private List<String> generateCycleIDs(Date start, Date end, int cycleType,
            int cycleLength)
    {
        Date startDefault = start;
        List<String> cycldIDLst = new ArrayList<String>();
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        
        //start不能小于基准时间
        if (startDefault.before(TccConfig.getBenchDate()))
        {
            startDefault = TccConfig.getBenchDate();
        }
        
        //如果开始时间晚于结束时间，直接退出
        if (startDefault.after(end))
        {
            return cycldIDLst;
        }
        
        //校准start为本周期起始点
        Date cycleStart = TccUtil.roll2CycleStartOnBenchDate(startDefault,
                cycleType,
                cycleLength,
                0);
        
        cStart.setTime(cycleStart);
        cEnd.setTime(end);
        
        //24小时制
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HH");
        
        //如果start是周期起点，就先添加该周期ID
        if (cycleStart.equals(startDefault))
        {
            cycldIDLst.add(df.format(startDefault));
        }
        
        while (true)
        {
            cStart.add(CycleType.toCalendarType(cycleType), cycleLength);
            if (cStart.after(cEnd))
            {
                break;
            }
            //YYYYMMDD-hh 
            cycldIDLst.add(df.format(cStart.getTime()));
        }
        return cycldIDLst;
    }
}
