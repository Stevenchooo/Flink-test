/*
 * 
 * 文 件 名:  TccDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  DAO接口
 * 创 建 人:  z00190165
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.tcc.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.domain.AlarmFactSearch;
import com.huawei.platform.tcc.domain.DependCyclesExistNum;
import com.huawei.platform.tcc.domain.InstanceRelationSearch;
import com.huawei.platform.tcc.domain.Log2DBQueryParam;
import com.huawei.platform.tcc.domain.LongTimeShellParam;
import com.huawei.platform.tcc.domain.OperationRecordSearch;
import com.huawei.platform.tcc.domain.OsGroupSearch;
import com.huawei.platform.tcc.domain.ReserveCycleLogParam;
import com.huawei.platform.tcc.domain.RolePrivilegeInfo;
import com.huawei.platform.tcc.domain.RoleSearch;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.domain.ServiceSearch;
import com.huawei.platform.tcc.domain.TaskCycleParam;
import com.huawei.platform.tcc.domain.TaskIdCycleIdPair;
import com.huawei.platform.tcc.domain.TaskMinMaxCycle;
import com.huawei.platform.tcc.domain.TaskOwnerInfo;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.domain.UserSearch;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.AlarmFactInfoEntity;
import com.huawei.platform.tcc.entity.BatchRunningStateEntity;
import com.huawei.platform.tcc.entity.DBServerConfigEntity;
import com.huawei.platform.tcc.entity.InstanceRelationEntity;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.entity.LongTimeShellEntity;
import com.huawei.platform.tcc.entity.NodeInfoEntity;
import com.huawei.platform.tcc.entity.NodeMappingEntity;
import com.huawei.platform.tcc.entity.OSGroupInfoEntity;
import com.huawei.platform.tcc.entity.OSUserGroupServiceEntity;
import com.huawei.platform.tcc.entity.OSUserInfoEntity;
import com.huawei.platform.tcc.entity.OSUserMappingEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.PathMappingEntity;
import com.huawei.platform.tcc.entity.RoleDefinationEntity;
import com.huawei.platform.tcc.entity.RolePrivilegeInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.StepRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskAlarmChannelInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmItemsEntity;
import com.huawei.platform.tcc.entity.TaskAlarmThresholdEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.entity.TaskStepExchangeEntity;
import com.huawei.platform.tcc.entity.UserGroupMappingEntity;

/**
 * 
 * TCC的数据库操作接口
 * 
 * @author z00190165
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100,
 *          2011-11-21]
 */
public interface TccDao extends Serializable
{
    /************************* Tcc 任务表[begin] ***********************************************/
    /**
     * 保存任务信息到任务表
     * 
     * @param entity
     *            任务信息
     * @throws Exception
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract void addTask(TaskEntity entity)
        throws Exception;
    
    /**
     * 通过taskId获取任务实体
     * 
     * @param taskId
     *            任务Id
     * @return 任务实体
     * @throws CException
     *             统一封装的异常
     */
    abstract TaskEntity getTask(Long taskId)
        throws CException;
    
    /**
     * 获取启用任务或者禁用任务列表
     * 
     * @param taskEnableFlag
     *            任务是否启动标志
     * @return 启用任务或者禁用任务列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getTaskList(Integer taskEnableFlag)
        throws CException;
    
    /**
     * 获取启用任务或者禁用任务列表[仅获取如下字段] Task_ID,Task_State,Cycle_Depend_Flag,Cycle_Type,
     * Depend_Task_ID_List,Cycle_Length,Priority,weight, start_Time,Task_Name
     * 
     * @param taskEnableFlag
     *            任务是否启动标志
     * @return 启用任务或者禁用任务列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getSchTaskList(Integer taskEnableFlag)
        throws CException;
    
    /**
     * 获取全部任务列表
     * 
     * @return 全部任务列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getTaskList()
        throws CException;
    
    /**
     * 获取任务ID列表
     * 
     * @param taskIds
     *            任务ID列表
     * @return 任务ID列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getTaskList(List<Long> taskIds)
        throws CException;
    
    /**
     * 获取全部任务ID列表
     * 
     * @param entity
     *            任务查询条件类
     * @return 任务ID列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getTaskAllList(TaskSearchEntity entity)
        throws CException;
    
    /************************* Tcc 任务表[end] *************************************************/
    
    /************************* Tcc 任务步骤表[begin] ***********************************************/
    /**
     * 获取指定任务集合的所有已启动的任务步骤
     * 
     * @param taskIds
     *            任务id列表
     * @return 获取指定任务集合的所有已启动的任务步骤
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskStepEntity> getTaskStepList(List<Long> taskIds)
        throws CException;
    
    /**
     * 获取任务步骤信息列表
     * 
     * @param taskId
     *            任务Id
     * @return 任务步骤信息列表
     * @throws Exception
     *             统一封装的异常
     */
    abstract List<TaskStepEntity> getEnableTaskStepList(Long taskId)
        throws Exception;
    
    /**
     * 获取步骤的部署节点
     * 
     * @param taskId
     *            任务Id
     * @param stepId
     *            步骤Id
     * @return 获取步骤的执行命令的主机地址
     */
    abstract String getDeployedNodes(Long taskId, Integer stepId);
    
    /************************* Tcc 任务步骤表[begin] ***********************************************/
    
    /************************* Tcc 任务运行状态表[begin] ***********************************************/
    /**
     * 指定任务的最小的周期Id
     * 
     * @param taskId
     *            任务ID
     * @return 最小的周期Id
     * @throws CException
     *             统一封装的异常
     */
    abstract String getMinCycleID(Long taskId)
        throws CException;
    
    /**
     * 指定任务的最大的周期Id
     * 
     * @param taskId
     *            任务ID
     * @return 最大的周期Id
     * @throws CException
     *             统一封装的异常
     */
    abstract String getMaxCycleID(Long taskId)
        throws CException;
    
    /**
     * 指定周期任务是否存在
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 周期任务是否存在
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean isExistsTaskRS(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 周期任务的是否全部存在
     * 
     * @param equalCount
     *            任务周期的个数
     * @param taskIdCycleIds
     *            任务id与周期id构成的集合,eg:((100,2011101-12),(100,2011102-12))
     * @return 周期任务的是否全部存在
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean existsAllTaskIDCycleID(Integer equalCount, String taskIdCycleIds)
        throws CException;
    
    /**
     * 新增任务运行状态信息
     * 
     * @param taskRS
     *            任务运行状态实体
     * @return 添加是否成功
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean addTaskRunningState(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 新增任务运行状态信息
     * 
     * @param taskRSs 任务运行状态实体列表
     * @return 添加是否成功
     * @throws CException  统一封装的异常
     */
    abstract boolean addTaskRunningState(List<TaskRunningStateEntity> taskRSs)
        throws CException;
    
    /**
     * 获取任务运行状态信息
     * 
     * @param taskRS
     *            任务运行状态实体
     * @return 任务运行状态信息
     * @throws CException
     *             统一封装的异常
     */
    abstract TaskRunningStateEntity getTaskRunningState(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 获取所有需要执行的周期任务（运行状态信息列表）
     * 
     * @param taskId
     *            任务Id
     * @param startTime
     *            开始时间
     * @param maxCycleId
     *            最大的周期Id
     * @return 所有需要执行的周期任务
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getNeedRunCycleTasks(Long taskId, Date startTime, String maxCycleId)
        throws CException;
    
    /**
     * 获取所有需要执行的周期任务（运行状态信息列表）
     * 
     * @param taskId 任务Id
     * @param startTime 开始时间
     * @return 所有需要执行的周期任务
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getNeedRunCycleTasks(Long taskId, Date startTime)
        throws CException;
    
    /**
     * 获取所有可能需要告警的的任务周期
     * 
     * @param taskId
     *            任务Id
     * @param maxCycleId
     *            最大的周期Id
     * @param startTime
     *            开始时间
     * @return 所有可能需要告警的的任务周期
     * @throws Exception
     *             统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getNeedAlarmTaskRSs(Long taskId, Date startTime, String maxCycleId)
        throws Exception;
    
    /**
     * 获取所有需要执行的最老的周期任务（运行状态信息列表）
     * 
     * @param taskId
     *            任务Id
     * @param maxCycleId
     *            最大的周期Id
     * @param startTime
     *            开始时间
     * @return 所有需要执行的周期任务
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getTopOldNeedRunCycleTasks(Long taskId, Date startTime, String maxCycleId)
        throws CException;
    
    /**
     * 获取所有时间上可以执行的最老的任务周期列表（运行状态信息列表）
     * 
     * @param excludeTaskIds  忽略的任务Id集合
     * @param bench  基准时间
     * @param curr 当前时间
     * @return 所有需要执行的周期任务
     * @throws CException  统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getTopOldNeedRunCycles(List<Long> excludeTaskIds, Date bench, Date curr)
        throws CException;
    
    /**
     * 获取所有时间上可以执行的最老的任务周期列表（运行状态信息列表）
     * 
     * @param depends  周期以及周期Id构成的字符串序列"(1000,'20121011-00',1000,'20121010-00',3),..."
     * @return 所有需要执行的周期任务
     * @throws CException  统一封装的异常
     */
    abstract List<DependCyclesExistNum> getDependsOkCycleNums(String depends)
        throws CException;
    
    /**
     * 存在的周期任务的总数
     * 
     * @param taskIdCycleIDStateLst
     *            任务id与周期id以及状态构成的集合,eg:((100,2011101-12,2),(100,2011102-12,2))
     * @return 存在的周期任务的总数
     * @throws CException
     *             统一封装的异常
     */
    abstract int getTaskRunningStateCount(List<TaskRunningStateEntity> taskIdCycleIDStateLst)
        throws CException;
    
    /**
     * 存在的周期任务的总数（不考虑运行状态）
     * 
     * @param taskIdCycleIDStateLst
     *            任务id与周期id以及状态构成的集合,eg:((100,2011101-12,2),(100,2011102-12,2))
     * @return 存在的周期任务的总数
     * @throws CException
     *             统一封装的异常
     */
    abstract int getTaskRunningStateExceptStateCount(List<TaskRunningStateEntity> taskIdCycleIDStateLst)
        throws CException;
    
    /**
     * 更新任务运行状态
     * 
     * @param taskRS
     *            任务运行状态
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateTaskRunningState(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 更新任务运行状态,同时将运行次数自动加1
     * 
     * @param taskRS
     *            任务运行状态
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateTaskRsIncReturnTimes(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 以追加的方式更新开始已启动依赖任务ID列表
     * 
     * @param taskId
     *            依赖任务Id
     * @param cycleId
     *            依赖任务周期标识
     * @param appendTaskCycleId
     *            追加的依赖周期任务
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateAppendBeginDependTaskList(Long taskId, String cycleId, String appendTaskCycleId)
        throws CException;
    
    /**
     * 以追加的方式更新开始已完成依赖任务ID列表
     * 
     * @param taskId
     *            依赖任务Id
     * @param cycleId
     *            依赖任务周期标识
     * @param appendTaskCycleId
     *            追加的依赖周期任务
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateAppendFinishDependTaskList(Long taskId, String cycleId, String appendTaskCycleId)
        throws CException;
    
    /************************* Tcc 任务运行状态表[end] *************************************************/
    
    /************************* Tcc 批任务运行状态表[begin] ***********************************************/
    /**
     * 是否存在执行超时的批任务运行状态
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 是否存在执行超时的批任务运行状态
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean existsTimeOutBatchRS(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 是否存在执行出错的批任务运行状态
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 是否存在执行出错的批任务运行状态
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean existsErrorBatchRS(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 是否存在批任务运行状态
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 是否存在批任务运行状态
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean existsBatchRS(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 新增批次运行状态记录
     * 
     * @param batchRS
     *            批次运行状态记录
     * @return 新增是否成功
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean addBatchRunningState(BatchRunningStateEntity batchRS)
        throws CException;
    
    /**
     * 获取批次运行状态记录
     * 
     * @param batchRS
     *            批次运行状态记录
     * @return 批次运行状态记录
     * @throws CException
     *             统一封装的异常
     */
    abstract BatchRunningStateEntity getBatchRunningState(BatchRunningStateEntity batchRS)
        throws CException;
    
    /**
     * 获得当前最大的批次Id
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 当前最大的批次Id
     * @throws CException
     *             统一抛出的异常
     */
    abstract int getMaxBatchId(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 删除指定的周期任务的所有的批次运行状态
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 删除的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int deleteBatchRunningStates(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 更新批任务运行状态信息
     * 
     * @param batchRS
     *            批任务运行状态信息
     * @return 影响的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateBatchRunningState(BatchRunningStateEntity batchRS)
        throws CException;
    
    /**
     * 获取尚未完成的任务批次运行状态信息（state=init|start）
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 尚未完成的任务批次运行状态信息
     * @throws CException
     *             统一封装的异常
     */
    abstract List<BatchRunningStateEntity> getUnCompletedBatch(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取指定周期任务已经创建的批次的文件列表，去除空文件。
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 指定周期任务已经创建的批次的文件列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<String> getJobInputFiles(Long taskId, String cycleId)
        throws CException;
    
    /************************* Tcc 批任务运行状态表[end] *************************************************/
    
    /************************* Tcc 任务步骤运行状态表[begin] ***********************************************/
    /**
     * 删除指定周期任务所有的步骤运行状态信息
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 受影响的行数
     * @throws CException
     *             统一抛出的异常
     */
    abstract int deleteStepRunningStates(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取指定周期任务的所有步骤运行状态为start而且jobid存在的步骤
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 已经启动过(状态为start)的步骤运行状态信息
     */
    abstract List<StepRunningStateEntity> getStartedStepRunningStates(Long taskId, String cycleId);
    
    /**
     * 获取当前批次的最大步骤Id
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @param batchId
     *            批次Id
     * @return 当前批次的最大步骤Id
     * @throws CException
     *             统一封装的异常
     */
    abstract int getMaxStepId(Long taskId, String cycleId, int batchId)
        throws CException;
    
    /**
     * 新增步骤运行状态信息
     * 
     * @param stepRS
     *            步骤运行状态
     * @return 是否添加成功
     * @throws CException
     *             统一封装的异常
     */
    abstract boolean addStepRunningState(StepRunningStateEntity stepRS)
        throws CException;
    
    /**
     * 更新步骤运行状态信息
     * 
     * @param stepRS
     *            步骤运行状态
     * @return 受影响的记录数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateStepRunningState(StepRunningStateEntity stepRS)
        throws CException;
    
    /**
     * 获取步骤运行状态信息
     * 
     * @param stepRS
     *            步骤运行状态（包含主键）
     * @return 步骤运行状态信息
     * @throws CException
     *             统一封装的异常
     */
    abstract StepRunningStateEntity getStepRunningState(StepRunningStateEntity stepRS)
        throws CException;
    
    /**
     * 删除指定批次的所有步骤的运行状态信息
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @param batchId
     *            批次Id
     * @return 删除的记录数
     * @throws CException
     *             统一封装的异常
     */
    abstract int deleteStepRunningStates(Long taskId, String cycleId, Integer batchId)
        throws CException;
    
    /**
     * 获取指定批次未执行成功(状态为非finish)的所有步骤运行状态信息
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @param batchId
     *            批次Id
     * @return 指定批次未执行成功(状态为非finish)的所有步骤运行状态信息
     * @throws CException
     *             统一封装的异常
     */
    abstract List<StepRunningStateEntity> getNoSucessedStepRunningStateLst(Long taskId, String cycleId, Integer batchId)
        throws CException;
    
    /************************* Tcc 任务步骤运行状态表[begin] ***********************************************/
    
    /****************************************** portal begin **************************************************/
    /**
     * 获得任务ID编号
     * 
     * @param serviceID
     *            业务ID
     * @param taskType
     *            任务类型
     * @return 任务ID的编号
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract Integer generateTaskIDSerialNo(Integer serviceID, Integer taskType)
        throws CException;
    
    /**
     * 更新任务信息
     * 
     * @param entity
     *            任务信息
     * @throws Exception
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract void updateTask(TaskEntity entity)
        throws Exception;
    
    /**
     * 更新OS组关联的所有业务Id
     * 
     * @param entity
     *            OS组
     * @throws Exception
     *             统一封装的异常
     */
    abstract void updateTaskServiceId(OSGroupInfoEntity entity)
        throws Exception;
    
    /**
     * 更新OS用户关联的os组已经业务Id
     * 
     * @param entity
     *            OS用户、组、业务对象
     * @throws Exception
     *             统一封装的异常
     */
    abstract void updateTaskGroupServiceId(OSUserGroupServiceEntity entity)
        throws Exception;
    
    /**
     * 启动任务，要求任务必须是停止的
     * 
     * @param entity
     *            任务信息
     * @throws Exception
     *             统一封装的异常
     * @return 是否启动成功
     */
    abstract boolean startTask(TaskEntity entity)
        throws Exception;
    
    /**
     * 获得步骤ID序号
     * 
     * @param taskId
     *            任务Id
     * @return 步骤ID的编号
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract int generateTaskStepID(Long taskId)
        throws CException;
    
    /**
     * 新增任务步骤信息
     * 
     * @param entity
     *            新建的任务步骤信息
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract void addTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 更新任务步骤信息
     * 
     * @param entity
     *            任务步骤信息
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract void updateTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 删除任务步骤信息
     * 
     * @param entity
     *            任务步骤信息
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 获取任务步骤信息
     * 
     * @param entity
     *            任务步骤信息
     * @return 任务步骤信息
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract TaskStepEntity getTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 获取任务步骤信息列表
     * 
     * @param taskId
     *            任务Id
     * @return 任务步骤信息列表
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract List<TaskStepEntity> getTaskStepList(Long taskId)
        throws CException;
    
    /**
     * 更新任务步骤Id
     * 
     * @param entity
     *            要更新的任务步骤交换信息,stepIdOne为源Id，stepIdTwo为目标Id
     * @throws CException
     *             统一封装的异常
     */
    abstract void updateTaskStepId(TaskStepExchangeEntity entity)
        throws CException;
    
    /**
     * 获取所有的任务Id列表
     * 
     * @param vGroups
     *            可见的用户组
     * @return 获取所有的任务Id列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<Long> getAllTaskIdList(List<String> vGroups)
        throws CException;
    
    /**
     * 获取所有的任务Id、名称列表
     * 
     * @param vGroups
     *            可见的OS用户组
     * @return 获取所有的任务Id、名称列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getAllTaskIdNameList(List<String> vGroups)
        throws CException;
    
    /******************************************* portal end *********************************************************/
    
    /**
     * 获取虚拟执行成功的任务周期数
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 虚拟执行成功的任务周期数
     * @throws CException
     *             统一封装的异常
     */
    abstract int getVisualSuccessedTaskCycleNum(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取虚拟执行成功的最小周期Id
     * 
     * @param taskId
     *            任务Id
     * @return 虚拟执行成功的最小周期Id
     * @throws CException
     *             统一封装的异常
     */
    abstract String getMinVisualSuccessedCycleId(Long taskId)
        throws CException;
    
    /**
     * 将最小周期Id到最大周期Id之间的所有任务周期运行状态更新为state状态
     * 
     * @param taskCycleParam
     *            任务周期参数
     * @throws CException
     *             统一封装的异常
     */
    abstract void updateTaskRunningStateIntegration(TaskCycleParam taskCycleParam)
        throws CException;
    
    /**
     * 更新任务运行状态
     * 
     * @param taskRS
     *            任务运行状态
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    abstract int updateTaskRunningStateWithNullTime(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 获取任务记录数
     * 
     * @param entity
     *            查询条件
     * @return 任务记录数
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract Integer getTaskListSize(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 通过名字模糊查询任务信息列表
     * 
     * @param entity
     *            任务查询条件类
     * @return 任务信息列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getTaskListByNames(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 通过名字模糊查询任务信息的条数
     * 
     * @param entity
     *            查询条件
     * @return 记录条数
     * @throws CException
     *             统一封装的异常
     */
    abstract Integer getTaskListSizeByNames(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 通过名字模糊查询任务ID集合(任务名集合为多个则精确查询)
     * @param entity  查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     */
    abstract List<Long> getTaskIdsByNames(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 获取所有任务周期的批次状态列表
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 所有任务周期的批次状态列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取所有任务周期的批次状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param startIndex 开始索引
     * @param rows 行数
     * @return 所有所有任务周期的批次状态总数
     * @throws CException 统一封装的异常
     */
    abstract List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId, Long startIndex, Integer rows)
        throws CException;
    
    /**
     * 获取所有任务周期的批次状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有所有任务周期的批次状态总数
     * @throws CException 统一封装的异常
     */
    abstract Integer getBatchRSCount(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取最早开始的批次
     * 
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 获取最早开始的批次
     * @throws CException 统一封装的异常
     */
    abstract BatchRunningStateEntity getOldestBatchRS(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取所有任务周期的步骤运行状态列表
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 所有任务周期的步骤运行状态列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取所有任务周期的步骤状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param startIndex 开始索引
     * @param rows 行数
     * @return 所有所有任务周期的步骤状态总数
     * @throws CException 统一封装的异常
     */
    abstract List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId, Long startIndex, Integer rows)
        throws CException;
    
    /**
     * 获取任务周期的所有步骤状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 任务周期的所有步骤状态总数
     * @throws CException 统一封装的异常
     */
    abstract Integer getStepRSCount(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 删除任务
     * 
     * @param taskId
     *            传入的任务ID
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteTask(Long taskId)
        throws CException;
    
    /**
     * 查询任务运行状态列表(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam
     *            查询条件
     * @return 任务运行状态列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getTaskRSList(TaskRSQueryParam taskRSQueryParam)
        throws CException;
    
    /**
     * 查询所有未完成的任务运行状态列表[左闭右开周期区间]
     * @param taskId 周期Id
     * @param minCycleId 最小周期Id
     * @param maxCycleId 最大周期Id
     * @return 任务运行状态列表
     * @throws CException 统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getNSTaskRSs(Long taskId, String minCycleId, String maxCycleId)
        throws CException;
    
    /**
     * 查询所有任务的最小最大周期Id
     * 
     * @return 所有任务的最小最大周期Id
     * @throws CException 统一封装的异常
     */
    abstract List<TaskMinMaxCycle> getTaskMinMaxCycleIds()
        throws CException;
    
    /**
     * 查询任务的最小最大周期Id
     * 
     * @param taskId 任务Id
     * @return 任务的最小最大周期Id
     * @throws CException 统一封装的异常
     */
    abstract TaskMinMaxCycle getTaskMinMaxCycleId(Long taskId)
        throws CException;
    
    /**
     * 获取查询任务运行状态的总数(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam
     *            查询条件
     * @return 查询任务运行状态的总数
     * @throws CException
     *             统一封装的异常
     */
    abstract Integer getTaskRSCount(TaskRSQueryParam taskRSQueryParam)
        throws CException;
    
    /**
     * 获取任务运行状态的总数
     * 
     * @param taskId
     *            任务Id
     * @return 任务运行状态的总数
     * @throws CException
     *             统一封装的异常
     */
    abstract Integer getTaskRSCount(Long taskId)
        throws CException;
    
    /**
     * 获取启动任务步骤条数
     * 
     * @param taskId
     *            任务Id
     * @return 任务步骤条数
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract int getTaskStepEnableSize(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有步骤列表
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteTaskSteps(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有运行状态
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteTaskRunningStates(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有批次运行状态
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteBatchRunningStates(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有步骤运行状态
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteStepRunningStates(Long taskId)
        throws CException;
    
    /**
     * 删除所有依赖任务Id列表中包含任务Id的依赖关系
     * 
     * @param taskId
     *            任务Id
     * @throws CException
     *             统一封装的异常
     */
    abstract void deleteDependTaskId(Long taskId)
        throws CException;
    
    /**
     * 获取任务运行状态列表
     * 
     * @param taskRSList
     *            需要查询的任务运行状态列表
     * @return 任务运行状态列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskRunningStateEntity> getTaskRunningStateList(List<TaskRunningStateEntity> taskRSList)
        throws CException;
    
    /**
     * 将depend_Task_Id_List中包含task.taskName的那部分依赖信息替换成task.taskId
     * 
     * @param task
     *            任务Id和任务名
     * @throws CException
     *             统一封装的异常
     */
    abstract void replaceDependTaskName2Id(TaskEntity task)
        throws CException;
    
    /**
     * 通过taskName获取任务实体
     * 
     * @param taskName
     *            任务名
     * @return 任务实体
     * @throws CException
     *             统一封装的异常
     */
    abstract TaskEntity getTask(String taskName)
        throws CException;
    
    /**
     * 获取依赖于该任务的所有任务
     * 
     * @param taskIds
     *            任务Id集合
     * @return 依赖于该任务的所有任务
     * @throws CException
     *             统一封装的异常
     */
    abstract List<TaskEntity> getDependedTasks(List<Long> taskIds)
        throws CException;
    
    /**
     * 获取任务周期的相关日志信息
     * 
     * @param taskRS
     *            任务运行周期
     * @return 任务周期的相关日志信息
     * @throws CException
     *             统一封装的异常
     */
    abstract List<Log2DBEntity> getTccLogList(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 获取remoteshell执行的控制台输出日志
     * 
     * @param taskRS
     *            任务运行周期
     * @return remoteshell执行的控制台输出日志
     * @throws CException
     *             统一封装的异常
     */
    abstract List<Log2DBEntity> getRsLogList(TaskRunningStateEntity taskRS)
        throws CException;
    
    /**
     * 根据查询条件查询tcc日志记录列表
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return 查询tcc日志记录列表
     * @throws CException
     *             统一封装的异常
     */
    abstract List<Log2DBEntity> getTccLogList(Log2DBQueryParam logQueryParam)
        throws CException;
    
    /**
     * tcc日志记录数
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return tcc日志记录数
     * @throws CException
     *             统一封装的异常
     */
    abstract Integer getTccLogCount(Log2DBQueryParam logQueryParam)
        throws CException;
    
    /**
     * 根据查询条件查询rs日志记录列表
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return 查询rs日志记录列表
     * @throws Exception
     *             异常
     */
    abstract List<Log2DBEntity> getRsLogList(Log2DBQueryParam logQueryParam)
        throws Exception;
    
    /**
     * rs日志记录数
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return rs日志记录数
     * @throws Exception
     *             异常
     */
    abstract Integer getRsLogCount(Log2DBQueryParam logQueryParam)
        throws Exception;
    
    /**
     * 获取已经记录日志的所有用户名列表
     * 
     * @return 已经记录日志的所有用户名列表
     * @throws Exception
     *             异常
     */
    abstract List<String> getAllUserName()
        throws Exception;
    
    /**
     * 查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * 
     * @param param
     *            查询条件
     * @return 周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * @throws Exception
     *             异常
     */
    abstract Integer getLongTimeShellsCount(LongTimeShellParam param)
        throws Exception;
    
    /**
     * 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * 
     * @param param
     *            查询条件
     * @return 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * @throws Exception
     *             异常
     */
    abstract List<LongTimeShellEntity> getLongTimeShellList(LongTimeShellParam param)
        throws Exception;
    
    /**
     * 重新初始化任务的重做开始以及重做结束范围内的任务周期
     * 
     * @param queryParam
     *            开始周期Id，结束周期Id，任务Id
     * @throws Exception
     *             异常
     */
    abstract void reInitTaskRS(TaskRSQueryParam queryParam)
        throws Exception;
    
    /**
     * 删除taskIdCycleIds对应的日志记录（保留最新的reserveCount个成功的任务周期的日志）
     * 
     * @param taskIdCycleIds
     *            要删除的任务Id周期Id键值对集合
     * @throws Exception
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract void deleteRemoteshellLog(List<TaskIdCycleIdPair> taskIdCycleIds)
        throws Exception;
    
    /**
     * 获取需要删除日志的任务Id周期Id键值对集合（保留最新的reserveCount个成功的任务周期的日志）
     * 
     * @param reserveCycleLog
     *            要保留的任务周期日志参数
     * @throws Exception
     *             统一封装的异常
     * @return 需要删除日志的任务Id周期Id键值对集合
     */
    abstract List<TaskIdCycleIdPair> getNeedDeleteLogTaskRSs(ReserveCycleLogParam reserveCycleLog)
        throws Exception;
    
    /**
     * 获取业务Id业务名集合
     * 
     * @return 业务Id业务名集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws Exception;
    
    /**
     * 通过业务名集合查询指定业务
     * 
     * @param serviceSearch
     *            业务查询
     * @return 业务Id业务名集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 通过业务名集合查询指定业务总数
     * 
     * @param serviceSearch
     *            业务查询
     * @return 业务Id业务名集合
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getServicesCountByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 新增业务信息
     * 
     * @param serviceDef
     *            业务实体
     * @throws Exception
     *             异常
     */
    abstract void addService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 更新业务信息
     * 
     * @param serviceDef
     *            业务实体
     * @throws Exception
     *             异常
     */
    abstract void updateService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 删除业务信息
     * 
     * @param serviceId
     *            业务Id
     * @throws Exception
     *             异常
     */
    abstract void deleteService(Integer serviceId)
        throws Exception;
    
    /**
     * 获取业务实体
     * 
     * @param serviceId
     *            业务Id
     * @return 业务实体
     * @throws Exception
     *             异常
     */
    abstract ServiceDefinationEntity getService(Integer serviceId)
        throws Exception;
    
    /**
     * 节点信息列表
     * 
     * @return 节点信息列表
     * @throws Exception
     *             异常
     */
    abstract List<NodeInfoEntity> getNodeInfoList()
        throws Exception;
    
    /**
     * 节点信息列表
     * 
     * @param search
     *            查询条件
     * @return 节点信息列表
     * @throws Exception
     *             异常
     */
    abstract List<NodeInfoEntity> getNodes(Search search)
        throws Exception;
    
    /**
     * 节点总数
     * 
     * @param search
     *            查询条件
     * @return 节点信息列表
     * @throws Exception
     *             异常
     */
    abstract Integer getNodesCount(Search search)
        throws Exception;
    
    /**
     * 获取节点信息
     * 
     * @param nodeId
     *            节点Id
     * @return 节点信息
     * @throws Exception
     *             异常
     */
    abstract NodeInfoEntity getNodeInfo(Integer nodeId)
        throws Exception;
    
    /**
     * 新增节点信息
     * 
     * @param nodeInfo
     *            节点信息
     * @return 是否新增成功
     * @throws Exception
     *             异常
     */
    abstract boolean addNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception;
    
    /**
     * 修改节点信息
     * 
     * @param nodeInfo
     *            节点信息
     * @return 是否更新成功
     * @throws Exception
     *             异常
     */
    abstract boolean updateNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception;
    
    /**
     * 删除节点信息
     * 
     * @param nodeId
     *            节点Id
     * @return 是否删除成功
     */
    abstract boolean deleteNodeInfo(Integer nodeId);
    
    /**
     * 通过账号密码查询用户
     * 
     * @param usernameAndPasswordParam
     *            账号密码
     * @return 操作员信息
     * @throws Exception
     *             异常
     */
    abstract OperatorInfoEntity getOperatorInfo(UsernameAndPasswordParam usernameAndPasswordParam)
        throws Exception;
    
    /**
     * 通过用户名查询用户
     * 
     * @param operatorName
     *            用户名
     * @return 操作员信息
     * @throws Exception
     *             异常
     */
    abstract OperatorInfoEntity getOperatorInfoByName(String operatorName)
        throws Exception;
    
    /**
     * 通过用户名集合查询指定用户
     * 
     * @param userSearch
     *            用户查询
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OperatorInfoEntity> getUsers(UserSearch userSearch)
        throws Exception;
    
    /**
     * 通过用户名集合查询指定用户总数
     * 
     * @param search
     *            用户查询
     * @return 用户集合
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getUsersCount(Search search)
        throws Exception;
    
    /**
     * 通过绑定的OS用户查询指定用户
     * 
     * @param osUser
     *            操作系统用户
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OperatorInfoEntity> getUsersByOsUser(String osUser)
        throws Exception;
    
    /**
     * 通过绑定的OS用户查询指定用户总数
     * 
     * @param osUser
     *            操作系统用户
     * @return 用户总数
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getUsersCountByOsUser(String osUser)
        throws Exception;
    
    /**
     * 新增用户
     * 
     * @param operatorInfo
     *            用户
     * @throws Exception
     *             异常
     */
    abstract void addUser(OperatorInfoEntity operatorInfo)
        throws Exception;
    
    /**
     * 修改用户
     * 
     * @param operatorInfo
     *            用户
     * @throws Exception
     *             异常
     */
    abstract void updateUser(OperatorInfoEntity operatorInfo)
        throws Exception;
    
    /**
     * 删除用户
     * 
     * @param operatorInfo
     *            用户
     * @throws Exception
     *             统一封装的异常
     */
    abstract void deleteUser(OperatorInfoEntity operatorInfo)
        throws Exception;
    
    /**
     * 通过角色ID集合查询指定角色
     * 
     * @param roleSearch
     *            角色查询
     * @return 角色信息集合
     * @throws Exception
     *             异常
     */
    abstract List<RoleDefinationEntity> getRolesByName(RoleSearch roleSearch)
        throws Exception;
    
    /**
     * 通过角色ID集合查询指定角色总数
     * 
     * @param search
     *            角色查询
     * @return 角色总数
     * @throws Exception
     *             异常
     */
    abstract Integer getRolesCountByName(RoleSearch search)
        throws Exception;
    
    /**
     * 通过角色id查询角色
     * 
     * @param roleId
     *            角色id
     * @return 角色信息
     * @throws Exception
     *             异常
     */
    abstract RoleDefinationEntity getRole(Integer roleId)
        throws Exception;
    
    /**
     * 获取可见的任务Id集合
     * 
     * @param groups
     *            用户组列表
     * @return 可见的任务Id集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getVisualServiceIdNames(List<String> groups)
        throws Exception;
    
    /**
     * 获取所有可见的业务Id集合
     * 
     * @return 可见的业务Id集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getAllVisualServiceIdNames()
        throws Exception;
    
    /**
     * 获取角色Id集合
     * 
     * @return 角色Id集合
     * @throws Exception
     *             异常
     */
    abstract List<RoleDefinationEntity> getAllRoleIdList()
        throws Exception;
    
    /**
     * 获取可见的业务任务组名集合
     * 
     * @param roleId
     *            角色Id
     * @param serviceId
     *            业务Id
     * @return 获取可见的业务任务组名集合
     * @throws Exception
     *             异常
     */
    abstract List<String> getVisualTaskGroupNames(Integer roleId, Integer serviceId)
        throws Exception;
    
    /**
     * 获取所有任务组和对应的权限类型
     * 
     * @param search
     *            角色id
     * @return 角色权限信息集合
     * @throws Exception
     *             异常
     */
    abstract List<RolePrivilegeInfo> getPrivileges(Search search)
        throws Exception;
    
    /**
     * 获取已分配的任务组和对应的权限类型
     * 
     * @param search
     *            角色id
     * @return 角色权限信息集合
     * @throws Exception
     *             异常
     */
    abstract List<RolePrivilegeInfo> getHavePrivileges(Search search)
        throws Exception;
    
    /**
     * 新增角色
     * 
     * @param roleDef
     *            角色
     * @throws Exception
     *             异常
     */
    abstract void addRole(RoleDefinationEntity roleDef)
        throws Exception;
    
    /**
     * 修改角色
     * 
     * @param roleDef
     *            角色
     * @throws Exception
     *             异常
     */
    abstract void updateRole(RoleDefinationEntity roleDef)
        throws Exception;
    
    /**
     * 删除角色
     * 
     * @param roleDef
     *            角色
     * @throws Exception
     *             异常
     */
    abstract void deleteRole(RoleDefinationEntity roleDef)
        throws Exception;
    
    /**
     * 新增角色权限
     * 
     * @param rolePrivilegeInfo
     *            角色权限
     * @throws Exception
     *             异常
     */
    abstract void addRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 修改角色权限
     * 
     * @param rolePrivilegeInfo
     *            角色权限
     * @throws Exception
     *             异常
     */
    abstract void updateRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 删除角色权限
     * 
     * @param rolePrivilegeInfo
     *            角色权限
     * @throws Exception
     *             异常
     */
    abstract void deleteRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 通过角色查询指定角色总数
     * 
     * @param rolePrivilegeInfo
     *            角色查询
     * @return 角色总数
     * @throws Exception
     *             异常
     */
    abstract Integer getRolePrivilegeCount(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 获取未绑定OS用户的用户集合
     * 
     * @return 取未未绑定OS用户的用户集合
     * @throws Exception
     *             异常
     */
    abstract List<OperatorInfoEntity> getUsersNoOsUser()
        throws Exception;
    
    /**
     * 通过OS用户名集合查询指定OS用户
     * 
     * @param search
     *            OS用户查询
     * @return OS用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OSUserInfoEntity> getOSUsersByName(Search search)
        throws Exception;
    
    /**
     * 获取所有的OS用户列表
     * 
     * @return OS用户列表
     * @throws Exception
     *             异常
     */
    abstract List<OSUserInfoEntity> getOSUsers()
        throws Exception;
    
    /**
     * 通过OS用户名集合查询指定OS用户总数
     * 
     * @param search
     *            OS用户查询
     * @return OS用户总数
     * @throws Exception
     *             异常
     */
    abstract Integer getOSUsersCountByName(Search search)
        throws Exception;
    
    /**
     * 新增OS用户
     * 
     * @param mOSUserInfoEntity
     *            OS用户
     * @throws Exception
     *             异常
     */
    abstract void addOSUser(OSUserInfoEntity mOSUserInfoEntity)
        throws Exception;
    
    /**
     * 修改OS用户
     * 
     * @param mOSUserInfoEntity
     *            OS用户
     * @throws Exception
     *             异常
     */
    abstract void updateOSUser(OSUserInfoEntity mOSUserInfoEntity)
        throws Exception;
    
    /**
     * 删除OS用户
     * 
     * @param mOSUserInfoEntity
     *            OS用户
     * @throws Exception
     *             异常
     */
    abstract void deleteOSUser(OSUserInfoEntity mOSUserInfoEntity)
        throws Exception;
    
    /**
     * 获取OS用户信息
     * 
     * @param osUserName
     *            OS用户名
     * @return OS用户信息
     */
    abstract OSUserInfoEntity getOSUser(String osUserName);
    
    /**
     * 获取用户名集合所属的用户组集合
     * 
     * @param osUsers
     *            OS用户名集合
     * @return 用户组集合
     */
    abstract List<OSUserInfoEntity> getGroupByOSUsers(List<String> osUsers);
    
    /**
     * 获取用户组信息
     * 
     * @param group
     *            OS用户组
     * @return 用户组集合
     */
    abstract OSGroupInfoEntity getOSGroup(String group);
    
    /**
     * 新增OS用户组
     * 
     * @param groupE
     *            OS用户组
     * @throws Exception
     *             异常
     */
    abstract void addOSGroup(OSGroupInfoEntity groupE)
        throws Exception;
    
    /**
     * 修改OS用户组
     * 
     * @param groupE
     *            OS用户组
     * @throws Exception
     *             异常
     */
    abstract void updateOSGroup(OSGroupInfoEntity groupE)
        throws Exception;
    
    /**
     * 删除OS用户组信息
     * 
     * @param groupE
     *            OS用户组
     * @throws Exception
     *             异常
     */
    abstract void deleteOSGroup(OSGroupInfoEntity groupE)
        throws Exception;
    
    /**
     * 获取OS用户组列表
     * 
     * @return OS用户组列表
     */
    abstract List<OSGroupInfoEntity> getOSGroups();
    
    /**
     * 增加操作记录
     * 
     * @param operateAuditInfo
     *            操作记录
     * @throws Exception
     *             异常
     */
    abstract void addOperLog(OperateAuditInfoEntity operateAuditInfo)
        throws Exception;
    
    /**
     * 由查询条件取出审计记录
     * 
     * @param search
     *            查询条件
     * @return 审计记录集合
     * @throws Exception
     *             异常
     */
    abstract List<OperateAuditInfoEntity> getOperationRecords(OperationRecordSearch search)
        throws Exception;
    
    /**
     * 通过查询条件查询指定审计记录总数
     * 
     * @param search
     *            查询
     * @return 审计记录总数
     * @throws Exception
     *             异常
     */
    abstract Integer getRecordCount(OperationRecordSearch search)
        throws Exception;
    
    /**
     * 获取告警项
     * 
     * @param taskId
     *            任务Id
     * @return 告警项
     * @throws Exception
     *             异常
     */
    abstract TaskAlarmItemsEntity getTaskAlarmItems(Long taskId)
        throws Exception;
    
    /**
     * 通过任务id以及等级查询任务告警渠道
     * 
     * @param taskAlarmChannel
     *            任务告警渠道实体
     * @return 任务告警渠道实体
     * @throws Exception
     *             异常
     */
    abstract TaskAlarmChannelInfoEntity getTaskAlarmChannelInfo(TaskAlarmChannelInfoEntity taskAlarmChannel)
        throws Exception;
    
    /**
     * 插入告警记录
     * 
     * @param alarmFactInfo
     *            告警记录实体
     * @return 是否成功
     * @throws Exception
     *             异常
     */
    abstract boolean addAlarmFactInfo(AlarmFactInfoEntity alarmFactInfo)
        throws Exception;
    
    /**
     * 获取所有的告警阈值记录
     * 
     * @throws Exception
     *             异常
     * @return 任务告警阈值集合
     */
    abstract List<TaskAlarmThresholdEntity> getAlarmThresholdList()
        throws Exception;
    
    /**
     * 获取告警阈值
     * 
     * @param taskId
     *            任务Id
     * @throws Exception
     *             异常
     * @return 任务告警阈值
     */
    abstract TaskAlarmThresholdEntity getAlarmThreshold(Long taskId)
        throws Exception;
    
    /**
     * 获取任务Id告警渠道列表
     * 
     * @param taskId
     *            任务Id
     * @return 任务Id告警渠道列表
     * @throws Exception
     *             异常
     */
    abstract List<TaskAlarmChannelInfoEntity> getAlarmChannelList(Long taskId)
        throws Exception;
    
    /**
     * 新增告警渠道
     * 
     * @param alarmChannel
     *            告警渠道
     * @throws Exception
     *             异常
     */
    abstract void addAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception;
    
    /**
     * 更新告警渠道
     * 
     * @param alarmChannel
     *            告警渠道
     * @throws Exception
     *             异常
     */
    abstract void updateAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception;
    
    /**
     * 删除任务的告警渠道信息
     * 
     * @param taskId
     *            任务Id
     * @throws Exception
     *             异常
     */
    abstract void deleteAlarmChannel(Long taskId)
        throws Exception;
    
    /**
     * 新增告警阈值
     * 
     * @param alarmThreshold
     *            告警阈值
     * @throws Exception
     *             异常
     */
    abstract void addAlarmThreshold(TaskAlarmThresholdEntity alarmThreshold)
        throws Exception;
    
    /**
     * 更新告警阈值
     * 
     * @param alarmThreshold
     *            告警阈值
     * @throws Exception
     *             异常
     */
    abstract void updateAlarmThreshold(TaskAlarmThresholdEntity alarmThreshold)
        throws Exception;
    
    /**
     * 删除任务的告警阈值
     * 
     * @param taskId
     *            任务Id
     * @throws Exception
     *             异常
     */
    abstract void deleteAlarmThreshold(Long taskId)
        throws Exception;
    
    /**
     * 新增告警类型项
     * 
     * @param alarmItems
     *            告警类型项
     * @throws Exception
     *             异常
     */
    abstract void addAlarmItems(TaskAlarmItemsEntity alarmItems)
        throws Exception;
    
    /**
     * 更新告警类型项
     * 
     * @param alarmItems
     *            告警类型项
     * @throws Exception
     *             异常
     */
    abstract void updateAlarmItems(TaskAlarmItemsEntity alarmItems)
        throws Exception;
    
    /**
     * 删除任务的告警类型项
     * 
     * @param taskId
     *            任务Id
     * @throws Exception
     *             异常
     */
    abstract void deleteAlarmItems(Long taskId)
        throws Exception;
    
    /**
     * 更新告警处理信息
     * 
     * @param alarmFact
     *            告警处理信息
     * @throws Exception
     *             异常
     */
    abstract void updateAlarmFact(AlarmFactInfoEntity alarmFact)
        throws Exception;
    
    /**
     * 获取任务告警渠道
     * 
     * @param alarmChannel
     *            告警渠道
     * @return 任务告警渠道
     * @throws Exception
     *             异常
     */
    abstract TaskAlarmChannelInfoEntity getAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception;
    
    /**
     * 由查询条件获取告警事实信息列表
     * 
     * @param search
     *            查询条件
     * @return 告警事实信息列表
     * @throws Exception
     *             异常
     */
    abstract List<AlarmFactInfoEntity> getAlarmFacts(AlarmFactSearch search)
        throws Exception;
    
    /**
     * 由查询条件获取告警事实信息列表总数
     * 
     * @param search
     *            查询条件
     * @return 告警事实信息列表总数
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getAlarmFactCount(AlarmFactSearch search)
        throws Exception;
    
    /**
     * 删除三个月前的审计记录
     * 
     * @param date
     *            三个月前的时间
     * @throws Exception
     *             异常
     * @see [类、类#方法、类#成员]
     */
    abstract void deleteOldOperationRecord(Date date)
        throws Exception;
    
    /**
     * 通过业务任务组查询任务信息列表(任务名集合为多个则精确查询)
     * 
     * @param entity
     *            任务查询条件类
     * @return 任务信息列表
     * @throws Exception
     *             异常
     */
    abstract List<TaskEntity> getTaskListByServiceTG(TaskSearchEntity entity)
        throws Exception;
    
    /**
     * 更新任务是否加载标识，如果任务已启动，则不允许改为不加载（0）
     * 
     * @param entity
     *            任务信息
     * @throws Exception
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    abstract void changeTaskEnableFlag(TaskEntity entity)
        throws Exception;
    
    /**
     * 通过OS用户名查询相关的OS用户、用户组已经业务信息
     * 
     * @param osUsers
     *            OS用户列表
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OSUserGroupServiceEntity> getOSUserGroupServicesByName(List<String> osUsers)
        throws Exception;
    
    /**
     * 通过用户组查询指定的os用户列表
     * 
     * @param groups
     *            用户组列表
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<String> getVisibleOsUsers(List<String> groups)
        throws Exception;
    
    /**
     * 获取任务隶属相关信息
     * 
     * @param taskIdList
     *            任务ID列表
     * @return 任务隶属相关信息
     */
    abstract List<TaskOwnerInfo> getTaskOwnersByIds(List<Long> taskIdList);
    
    /**
     * 获取OS组总数
     * 
     * @param search
     *            检索条件
     * @return OS任务组总数
     */
    abstract Integer getOSGroupsCountBySearch(OsGroupSearch search);
    
    /**
     * 获取OS组列表
     * 
     * @param search
     *            检索条件
     * @return OS组列表
     */
    abstract List<OSGroupInfoEntity> getOSGroupsBySearch(OsGroupSearch search);
    
    /**
     * 新增实例关系
     * @param instanceRels 实例关系
     */
    abstract void addInstanceRels(List<InstanceRelationEntity> instanceRels);
    
    /**
     * 删除实例关系的指定日期分区
     * @param scheduleDate 调度日期
     */
    abstract void deleteInstanceRelPartition(Date scheduleDate);
    
    /**
     * 创建实例关系的指定日期分区
     * @param scheduleDate 调度日期
     */
    abstract void addInstanceRelPartition(Date scheduleDate);
    
    /**
     * 获取任务实例关系
     * @param insRelSearch 实例关系查询条件
     * @return 任务实例列表
     */
    abstract List<InstanceRelationEntity> getInstanceRels(InstanceRelationSearch insRelSearch);
    
    /**
     * 删除实例关系
     * @param instanceRel 实例关系
     */
    abstract void deleteInstanceRel(InstanceRelationEntity instanceRel);
    
    /**
     * 获取所有目标数据库配置信息
     * @return 所有目标数据库配置信息
     * @throws CException 异常
     */
    abstract List<DBServerConfigEntity> getDBServers()
        throws CException;
    
    /**
     * 获取TCC服务数据库服务配置
     * @param tccName tcc名字
     * @return TCC服务数据库服务配置
     * @throws CException 异常
     */
    abstract DBServerConfigEntity getDBServerConfig(String tccName)
        throws CException;
    
    /**
     * 获取OS用户映射关系
     * @param tccName 目标TCC
     * @return OS用户映射关系
     * @throws Exception 异常
     */
    abstract List<OSUserMappingEntity> getOSUserMappings(String tccName)
        throws Exception;
    
    /**
     * 获取用户组映射关系
     * @param tccName 目标TCC
     * @return 用户组映射关系
     * @throws Exception 异常
     */
    abstract List<UserGroupMappingEntity> getUserGroupMappings(String tccName)
        throws Exception;
    
    /**
     * 获取路径映射关系
     * @param tccName 目标TCC
     * @return 路径映射关系
     * @throws Exception 异常
     */
    abstract List<PathMappingEntity> getPathMappings(String tccName)
        throws Exception;
    
    /**
     * 获取节点映射关系
     * @param tccName 目标TCC
     * @return 节点映射关系
     * @throws Exception 异常
     */
    abstract List<NodeMappingEntity> getNodeMappings(String tccName)
        throws Exception;
    
    /**
     * 保存任务信息集合保存到任务表
     * 
     * @param tasks 任务集合
     * @throws Exception 统一封装的异常
     */
    abstract void replaceTasks(List<TaskEntity> tasks)
        throws Exception;
    
    /**
     * 保存所有的告警阈值列表
     * 
     * @param alarmThresholds 告警阈值列表
     * @throws Exception 统一封装的异常
     */
    abstract void replaceAlarmThresholds(List<TaskAlarmThresholdEntity> alarmThresholds)
        throws Exception;
    
    /**
     * 保存任务信息保存到任务表
     * 
     * @param task 任务
     * @throws Exception 统一封装的异常
     */
    abstract void replaceTask(TaskEntity task)
        throws Exception;
    
    /**
     * 获取所有的任务步骤
     * 
     * @return 所有的任务步骤
     * @throws Exception 异常
     */
    abstract List<TaskStepEntity> getAllTaskSteps()
        throws Exception;
    
    /**
     * 替换任务步骤集合
     * 
     * @param steps 步骤集合
     * @throws Exception 异常
     */
    abstract void replaceSteps(List<TaskStepEntity> steps)
        throws Exception;
    
    /**
     * 获取任务的所有运行状态
     * @param taskIds 任务Id集合
     * @return 任务的所有运行状态
     * @throws Exception 异常
     */
    abstract List<TaskRunningStateEntity> getAllTaskRSs(List<Long> taskIds)
        throws Exception;
    
    /**
     * 替换任务运行状态
     * @param taskRSs 运行状态集合
     * @throws Exception 异常
     */
    abstract void replaceTaskRSs(List<TaskRunningStateEntity> taskRSs)
        throws Exception;
    
    /**
     * 获取指定任务集合的告警阈值
     * @param taskIds 任务Id集合
     * @return 告警阈值集合
     * @throws Exception 异常
     */
    abstract List<TaskAlarmThresholdEntity> getAlarmThresholds(List<Long> taskIds)
        throws Exception;
    
    /**
     * 获取所有的告警项
     * @return 所有的告警项
     * @throws Exception 异常
     */
    abstract List<TaskAlarmItemsEntity> getAlarmItems()
        throws Exception;
    
    /**
     * 获取指定任务集合的告警项
     * @param taskIds 任务Id集合
     * @return 所有的告警项
     * @throws Exception 异常
     */
    abstract List<TaskAlarmItemsEntity> getAlarmItems(List<Long> taskIds)
        throws Exception;
    
    /**
     * 替换告警项
     * @param alarmItems 告警项集合
     * @throws Exception 异常
     */
    abstract void replaceAlarmItems(List<TaskAlarmItemsEntity> alarmItems)
        throws Exception;
    
    /**
     * 获取所有的告警渠道
     * @return 告警渠道集合
     * @throws Exception 异常
     */
    abstract List<TaskAlarmChannelInfoEntity> getAlarmChannels()
        throws Exception;
    
    /**
     * 获取任务Id集合的告警渠道
     * @param taskIds 任务Id集合
     * @return 告警渠道集合
     * @throws Exception 异常
     */
    abstract List<TaskAlarmChannelInfoEntity> getAlarmChannels(List<Long> taskIds)
        throws Exception;
    
    /**
     * 替换所有的告警渠道
     * @param alarmChannels 告警渠道集合
     * @throws Exception 异常
     */
    abstract void replaceAlarmChannels(List<TaskAlarmChannelInfoEntity> alarmChannels)
        throws Exception;
    
    /**
     * 获取任务Id集合的任务步骤
     * @param taskIds 任务Id集合
     * @return 任务步骤集合
     * @throws Exception 异常
     */
    abstract List<TaskStepEntity> getTaskSteps(List<Long> taskIds)
        throws Exception;
    
    /**
     * 获取任务名重复的任务Id集合
     * @param tasks 任务集合
     * @return 任务名重复的任务Id集合
     * @throws Exception 异常
     */
    abstract List<Long> getDupNameTasks(List<TaskEntity> tasks)
        throws Exception;
    
    /**
     * 删除指定告警阈值
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    abstract void deleteAlarmThresholds(List<Long> atNotExistTaskIds)
        throws Exception;
    
    /**
     * 删除指定告警项
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    abstract void deleteAlarmItems(List<Long> atNotExistTaskIds)
        throws Exception;
    
    /**
     * 删除指定告警渠道
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    abstract void deleteAlarmChannels(List<Long> atNotExistTaskIds)
        throws Exception;
    
    /**
     * 删除指定任务步骤
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    abstract void deleteSteps(List<Long> atNotExistTaskIds)
        throws Exception;
    
    /**
     * 删除指定任务运行状态
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    abstract void deleteTaskRSs(List<Long> atNotExistTaskIds)
        throws Exception;
    
    /**
     * 获取存在的任务Id集合
     * @param taskIds 任务Id集合
     * @return 存在的任务Id集合
     * @throws Exception 异常
     */
    abstract List<Long> getExistTaskIds(List<Long> taskIds)
        throws Exception;
    
    /**
     * 获取同步时所有需要停止步骤的任务Id集合
     * @throws Exception 异常
     * @return 需要停止步骤的任务Id集合
     */
    abstract List<Long> getStepsStoppedTaskIds()
        throws Exception;
    
    /**
     * 获取所有的业务信息
     * @return 业务信息集合
     * @throws Exception 异常
     */
    abstract List<ServiceDefinationEntity> getServices()
        throws Exception;
    
    /**
     * 替换所有的业务
     * @param services 业务集合
     * @throws Exception 异常
     */
    abstract void replaceServices(List<ServiceDefinationEntity> services)
        throws Exception;
}
