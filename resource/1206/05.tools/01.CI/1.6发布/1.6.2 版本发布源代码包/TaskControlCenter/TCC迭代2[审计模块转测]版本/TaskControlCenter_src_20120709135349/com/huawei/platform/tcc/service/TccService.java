/*
 * 文 件 名:  TccService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.tcc.service;

import java.util.Date;
import java.util.List;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.domain.CycleDependRelation;
import com.huawei.platform.tcc.entity.CycleTaskDetailEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;

/**
 * TCC业务逻辑操作接口
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-11-10]
 * @see  [相关类/方法]
 */
public interface TccService
{
    /**
     * 调度任务
     */
    public abstract void scheduleTask();
    
    /**
     * 获取任务Id从开始时间后的所有周期Id
     * @param taskId 任务Id
     * @return 获取任务Id从开始时间后的所有周期Id
     * @throws Exception 异常
     */
    public abstract List<String> getAllCycleIdList(Long taskId)
        throws Exception;
    
    /**
     * 获取任务运行状态信息
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 任务运行状态信息
     * @throws CException 抛出异常
     */
    public abstract TaskRunningStateEntity getRunningStateEntity(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取任务运行状态信息
     * @param task 任务
     * @param cycleId 周期Id
     * @return 任务运行状态信息
     * @throws CException 抛出异常
     */
    public abstract TaskRunningStateEntity getRunningStateEntity(TaskEntity task, String cycleId)
        throws CException;
    
    /**
     * 获取指定任务周期所直接依赖的任务周期的运行状态列表
     *
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 依赖的任务周期的运行状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskRunningStateEntity> getDependingRunningState(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取指定任务周期所直接依赖的任务周期的运行状态列表
     *
     * @param task 任务
     * @param cycleId 周期Id
     * @return 依赖的任务周期的运行状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskRunningStateEntity> getDependingRunningState(TaskEntity task, String cycleId)
        throws CException;
    
    /**
     * 获取直接依赖于指定任务周期的任务运行状态列表
     *
     * @param taskRS 任务运行状态,同时用于返回状态
     * @return 依赖的任务周期的运行状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskRunningStateEntity> getDependedRSList(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 获取直接或间接依赖于指定任务周期的所有任务周期的运行状态集合
     *
     * @param taskId 根节点的任务Id
     * @param cycleId  根节点的周期Id
     * @return 依赖树的所有任务周期的运行状态集合
     * @throws CException 统一封装的异常
     */
    public List<TaskRunningStateEntity> getAllDependedRSList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取开始时间到结束时间范围内任务状态列表
     * @param taskId 任务Id
     * @param startTime 开始时间
     * @param endTime 结束时间(包括)
     * @return 开始时间到结束时间范围内任务状态列表
     * @throws CException 抛出异常
     */
    public abstract List<TaskRunningStateEntity> getTaskRunningStateList(Long taskId, Date startTime, Date endTime)
        throws CException;
    
    /**
     * 重做任务树根节点列表中的所有任务树
     * 停掉以指定周期任务为根节点的依赖树上的周期任务，重做该依赖树上的所有周期任务
     * @param taskRsList 任务树根节点列表
     * @throws CException 统一封装的异常
     */
    public abstract void redoCycleTaskTree(List<TaskRunningStateEntity> taskRsList)
        throws CException;
    
    /**
     * 重做指定的周期任务
     * 停掉以指定周期任务为根节点的依赖树上的周期任务，重做该依赖树上的所有周期任务
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param ignoreNoExistError 是否忽略任务树根节点的任务运行状态不存在错误
     * @param keepStopedSchedule 是否保持停止调度任务树，由外部统一启动调度
     * @throws CException 统一封装的异常
     */
    public abstract void redoCycleTaskTree(Long taskId, String cycleId, boolean ignoreNoExistError,
        boolean keepStopedSchedule)
        throws CException;
    
    /**
     * 重做指定的周期任务
     * 停掉指定周期任务，重做该周期任务
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param ignoreNoExistError 是否忽略任务树根节点的任务运行状态不存在错误
     * @throws CException 统一封装的异常
     */
    public abstract void redoCycleTask(Long taskId, String cycleId, boolean ignoreNoExistError)
        throws CException;
    
    /**
     * 重做任务周期列表
     * 停掉列表中的所有任务周期，并重做任务周期列表
     * @param taskRsList 要重做的任务周期列表
     * @throws Exception 统一封装的异常
     */
    public abstract void redoCycleTask(List<TaskRunningStateEntity> taskRsList)
        throws Exception;
    
    /**
     * 重做任务周期列表
     * 停掉列表中的所有任务周期，并重做任务周期列表
     * @param taskRsList 要重做的任务周期列表
     * @param keepStopedSchedule 是否保持停止调度任务树，由外部统一启动调度
     * @throws Exception 统一封装的异常
     */
    public abstract void redoTaskCycles(List<TaskRunningStateEntity> taskRsList, boolean keepStopedSchedule)
        throws Exception;
    
    /**
     * 继续重新调度taskRsList中的任务周期
     * 
     * @param taskRsList 任务周期列表
     */
    public abstract void continueSchedule(List<TaskRunningStateEntity> taskRsList);
    
    /**
     * 获取运行的周期任务数
     * @return 获取运行的周期任务数
     */
    public abstract int getRunningTaskNum();
    
    /**
     * 获取等待运行的周期任务数
     * @return 获取等待运行的周期任务数
     */
    public abstract int getWaittingTaskNum();
    
    /**
     * 获取运行的周期任务
     * @return 获取运行的周期任务
     */
    public abstract String getRunningTasks();
    
    /**
     * 获取等待运行的周期任务
     * @return 获取等待运行的周期任务
     */
    public abstract String getWaittingTasks();
    
    /**
     * 获取运行队列中的任务周期详细信息列表
     * @return 运行队列中的任务周期详细信息列表
     */
    public abstract List<CycleTaskDetailEntity> getCycleDetailsRunning();
    
    /**
     * 获取等待队列中的任务周期详细信息列表
     * @return 等队列中的任务周期详细信息列表
     */
    public abstract List<CycleTaskDetailEntity> getCycleDetailsWaitting();
    
    /**
     * 计算直接依赖当前任务周期的所有任务周期集合,仅支持同周期依赖
     * @param task 任务
     * @param tasks 直接依赖于task的任务集合
     * @param cycleId 周期Id
     * @return 依赖当前任务周期的所有任务周期集合
     * @exception Exception 异常
     */
    public abstract List<CycleDependRelation> getAllCycleDeppingRs(TaskEntity task, List<TaskEntity> tasks,
        String cycleId)
        throws Exception;
    
    /**
     * 获取直接或间接依赖于指定周期任务的所有任务周期的运行状态集合（实时计算）
     *
     * @param taskId 根节点的任务Id
     * @param cycleId  根节点的周期Id
     * @param maxCount 任务周期数
     * @return 依赖树的所有任务周期的运行状态集合
     * @throws Exception 统一封装的异常
     */
    public abstract List<TaskRunningStateEntity> getAllDependedRSListRealTime(Long taskId, String cycleId, int maxCount)
        throws Exception;
    
    /**
     * 停止调度tasks中的任务
     * @param tasks 任务列表
     */
    public abstract void stopScheduleTasks(List<TaskEntity> tasks);
    
    /**
     * 继续重新调度tasks中的任务
     * 
     * @param tasks 任务列表
     */
    public abstract void continueScheduleTasks(List<TaskEntity> tasks);
}
