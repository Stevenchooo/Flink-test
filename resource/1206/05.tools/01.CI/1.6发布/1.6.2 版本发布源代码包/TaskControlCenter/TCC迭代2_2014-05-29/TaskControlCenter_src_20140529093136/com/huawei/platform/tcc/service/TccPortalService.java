/*
 * 文 件 名:  TccService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.tcc.service;

import java.util.List;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.domain.AlarmFactSearch;
import com.huawei.platform.tcc.domain.Log2DBQueryParam;
import com.huawei.platform.tcc.domain.LongTimeShellParam;
import com.huawei.platform.tcc.domain.ServiceSearch;
import com.huawei.platform.tcc.domain.TaskOwnerInfo;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.entity.AlarmFactInfoEntity;
import com.huawei.platform.tcc.entity.BatchRunningStateEntity;
import com.huawei.platform.tcc.entity.DBServerConfigEntity;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.entity.LongTimeShellEntity;
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

/**
 * TCC业务逻辑操作接口
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-11-10]
 */
public interface TccPortalService
{
    /**
     * 获得任务ID编号
     * @param serviceID  业务ID
     * @param taskType  任务类型
     * @return 任务ID的编号
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract Long generateTaskID(Integer serviceID, Integer taskType)
        throws CException;
    
    /**
     * 保存任务信息
     * @param entity 新建的任务信息
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract void addTask(TaskEntity entity)
        throws Exception;
    
    /**
     * 更新任务信息
     * @param entity 任务信息
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract void updateTask(TaskEntity entity)
        throws Exception;
    
    /**
     * 启动任务，要求任务必须事先已经停止的，启动用户和os用户必填
     * @param entity 任务信息
     * @throws Exception 统一封装的异常
     * @return 是否启动成功
     */
    public abstract boolean startTask(TaskEntity entity)
        throws Exception;
    
    /**
     * 获取全部任务信息列表
     * @param entity 任务查询条件类
     * @return 全部任务信息列表
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract List<TaskEntity> getTaskAllList(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 通过名字模糊查询任务信息列表(任务名集合为多个则精确查询)
     * @param entity 任务查询条件类
     * @return 任务信息列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskEntity> getTaskListByNames(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 通过名字模糊查询任务ID集合(任务名集合为多个则精确查询)
     * @param entity  查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     */
    public abstract List<Long> getTaskIdsByNames(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 通过业务任务组查询任务信息列表(任务名集合为多个则精确查询)
     * @param entity 任务查询条件类
     * @return 任务信息列表
     * @throws Exception 异常
     */
    public abstract List<TaskEntity> getTaskListByServiceTG(TaskSearchEntity entity)
        throws Exception;
    
    /**
     * 通过名字模糊查询任务信息的条数(任务名集合为多个则精确查询)
     * @param entity 查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getTaskListSizeByNames(TaskSearchEntity entity)
        throws CException;
    
    /**
     * 获取任务信息
     * @param taskID 任务ID
     * @return TaskEntity 任务信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract TaskEntity getTaskInfo(Long taskID)
        throws CException;
    
    /**
     * 获得步骤ID序号
     * @param taskId  任务Id
     * @return 步骤ID的编号
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract int generateTaskStepID(Long taskId)
        throws CException;
    
    /**
     * 新增任务步骤信息
     * @param entity 新建的任务步骤信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract void addTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 更新任务步骤信息
     * @param entity 任务步骤信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract void updateTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 删除任务步骤信息
     * @param entity 任务步骤信息
     * @throws CException 统一封装的异常
     */
    public abstract void deleteTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 获取任务步骤信息
     * @param entity 任务步骤信息
     * @return 任务步骤信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract TaskStepEntity getTaskStep(TaskStepEntity entity)
        throws CException;
    
    /**
     * 获取任务步骤信息列表
     * @param taskId 任务Id
     * @return 任务步骤信息列表
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract List<TaskStepEntity> getTaskStepList(Long taskId)
        throws CException;
    
    /**
     * 交换任务步骤Id
     * @param entity 任务步骤交换信息
     * @throws CException 统一封装的异常
     */
    public abstract void exchangeTaskStep(TaskStepExchangeEntity entity)
        throws CException;
    
    /**
     * 获取所有的任务Id列表
     * @param vSTgs 可见的业务任务组
     * @return 获取所有的任务Id列表
     * @throws CException 统一封装的异常
     */
    public abstract List<Long> getAllTaskIdList(List<String> vSTgs)
        throws CException;
    
    /**
     * 获取所有的任务Id、名称列表
     * @param vGroups 可见的OS用户组
     * @return 获取所有的任务Id、名称列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskEntity> getAllTaskIdNameList(List<String> vGroups)
        throws CException;
    
    /**
     * 有查询条件获取数据库任务信息的条数
     * @param entity 查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract Integer getTaskListSize(TaskSearchEntity entity)
        throws CException;
    
    /**
      * 获取所有任务周期的批次状态列表
      * @param taskId 任务Id
      * @param cycleId 周期Id
      * @return 所有任务周期的批次状态列表
      * @throws CException 统一封装的异常
      */
    public abstract List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取所有任务周期的批次状态列表
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param startIndex 开始索引
     * @param rows 行数
     * @return 所有任务周期的批次状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId, Long startIndex,
        Integer rows)
        throws CException;
    
    /**
     * 获取所有任务周期的批次状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有所有任务周期的批次状态总数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getBatchRSCount(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取所有任务周期的步骤运行状态列表
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有任务周期的步骤运行状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取所有任务周期的步骤状态列表
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param startIndex 开始索引
     * @param rows 行数
     * @return 所有任务周期的步骤状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId, Long startIndex,
        Integer rows)
        throws CException;
    
    /**
     * 获取所有任务周期的步骤状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有所有任务周期的步骤状态总数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getStepRSCount(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 删除任务
     * @param taskId 传入的任务ID
     * @see [类、类#方法、类#成员]
     * @throws CException 统一封装的异常
     */
    public abstract void deleteTask(Long taskId)
        throws CException;
    
    /**
     * 查询任务运行状态列表(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam 查询条件
     * @return 任务运行状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskRunningStateEntity> getTaskRSList(TaskRSQueryParam taskRSQueryParam)
        throws CException;
    
    /**
     * 获取查询任务运行状态的总数(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam 查询条件
     * @return 查询任务运行状态的总数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getTaskRSCount(TaskRSQueryParam taskRSQueryParam)
        throws CException;
    
    /**
     * 获取任务运行状态的总数
     * 
     * @param taskId 任务Id
     * @return 任务运行状态的总数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getTaskRSCount(Long taskId)
        throws CException;
    
    /**
     * 获取任务ID列表
     * @param taskIds 任务ID列表
     * @return 任务ID列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskEntity> getTaskList(List<Long> taskIds)
        throws CException;
    
    /**
     * 获取启动任务步骤条数
     * @param taskId 任务Id
     * @return 启动任务步骤条数
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public abstract int getTaskStepEnableSize(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有步骤列表
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    public abstract void deleteTaskSteps(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有运行状态
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    public abstract void deleteTaskRunningStates(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有批次运行状态
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    public abstract void deleteBatchRunningStates(Long taskId)
        throws CException;
    
    /**
     * 删除任务ID的所有步骤运行状态
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    public abstract void deleteStepRunningStates(Long taskId)
        throws CException;
    
    /**
     * 删除所有依赖任务Id列表中包含任务Id的依赖关系
     * @param taskId 任务Id
     * @throws CException 统一封装的异常
     */
    public abstract void deleteDependTaskId(Long taskId)
        throws CException;
    
    /**
     * 获取任务运行状态列表
     * 
     * @param taskRSList 需要查询的任务运行状态列表
     * @return 任务运行状态列表
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskRunningStateEntity> getTaskRunningStateList(List<TaskRunningStateEntity> taskRSList)
        throws CException;
    
    /**
     * 将depend_Task_Id_List中包含task.taskName的那部分依赖信息替换成task.taskId
     * @param task 任务Id和任务名
     * @throws CException 统一封装的异常
     */
    public abstract void replaceDependTaskName2Id(TaskEntity task)
        throws CException;
    
    /**
     * 通过taskName获取任务实体
     * @param taskName 任务名
     * @return 任务实体
     * @throws CException 统一封装的异常
     */
    public abstract TaskEntity getTask(String taskName)
        throws CException;
    
    /**
     * 获取任务依赖树的所有任务集合
     * @param taskIds 任务Id集合
     * @return 任务依赖树的所有任务集合
     * @throws CException 统一封装的异常
     */
    public List<TaskEntity> getTaskDeppedTrees(List<Long> taskIds)
        throws CException;
    
    /**
     * 获取任务周期的相关日志信息
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 任务周期的相关日志信息
     * @throws CException 统一封装的异常
     */
    public abstract List<Log2DBEntity> getTccLogList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 获取remoteshell执行的控制台输出日志
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return remoteshell执行的控制台输出日志
     * @throws CException 统一封装的异常
     */
    public abstract List<Log2DBEntity> getRsLogList(Long taskId, String cycleId)
        throws CException;
    
    /**
     * 根据查询条件查询tcc日志记录列表
     * @param logQueryParam 日志查询条件
     * @return 查询tcc日志记录列表
     * @throws CException 统一封装的异常
     */
    public abstract List<Log2DBEntity> getTccLogList(Log2DBQueryParam logQueryParam)
        throws CException;
    
    /**
     * tcc日志记录数
     * @param logQueryParam 日志查询条件
     * @return tcc日志记录数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getTccLogCount(Log2DBQueryParam logQueryParam)
        throws CException;
    
    /**
     * 获取已经记录日志的所有用户名列表
     * @return 已经记录日志的所有用户名列表
     * @throws CException 统一封装的异常
     */
    public abstract List<String> getAllUserName()
        throws CException;
    
    /**
     * 根据查询条件查询rs日志记录列表
     * @param logQueryParam 日志查询条件
     * @return 查询rs日志记录列表
     * @throws CException 统一封装的异常
     */
    public abstract List<Log2DBEntity> getRsLogList(Log2DBQueryParam logQueryParam)
        throws CException;
    
    /**
     * rs日志记录数
     * @param logQueryParam 日志查询条件
     * @return rs日志记录数
     * @throws CException 统一封装的异常
     */
    public abstract Integer getRsLogCount(Log2DBQueryParam logQueryParam)
        throws CException;
    
    /**
     * 查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * @param param 查询条件
     * @return 周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * @throws Exception 异常
     */
    public abstract Integer getLongTimeShellsCount(LongTimeShellParam param)
        throws Exception;
    
    /**
     * 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * @param param 查询条件
     * @return 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * @throws Exception 异常
     */
    public abstract List<LongTimeShellEntity> getLongTimeShellList(LongTimeShellParam param)
        throws Exception;
    
    /**
     * 重新初始化任务的重做开始以及重做结束范围内的任务周期
     * @param taskE 任务实体
     * @throws Exception 异常
     */
    public abstract void reInitTaskRS(TaskEntity taskE)
        throws Exception;
    
    /**
     * 获取直接依赖于taskIds任务的任务集合
     * @param taskIds 任务Id集合
     * @return 直接依赖于taskIds任务的任务集合
     * @throws CException 统一封装的异常
     */
    public abstract List<TaskEntity> getDeppingTasks(List<Long> taskIds)
        throws CException;
    
    /**
     * 获取业务Id业务名集合
     * @return 业务Id业务名集合
     * @throws CException 统一封装的异常
     */
    public abstract List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws CException;
    
    /**
     * 通过业务名集合查询指定业务
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    public abstract List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 通过业务名集合查询指定业务总数
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    public abstract Integer getServicesCountByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 新增业务信息
     * @param serviceDef 业务实体
     * @throws Exception 异常
     */
    public abstract void addService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 更新业务信息
     * @param serviceDef 业务实体
     * @throws Exception 异常
     */
    public abstract void updateService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 删除业务信息
     * @param serviceId 业务Id
     * @throws Exception 异常
     */
    public abstract void deleteService(Integer serviceId)
        throws Exception;
    
    /**
     * 获取业务实体
     * @param serviceId 业务Id
     * @return 业务实体
     * @throws Exception 异常
     */
    public abstract ServiceDefinationEntity getService(Integer serviceId)
        throws Exception;
    
    /**
     * 获取任务告警渠道列表
     * @param taskId 任务Id
     * @return 任务Id告警渠道列表
     * @throws Exception 异常
     */
    public abstract List<TaskAlarmChannelInfoEntity> getAlarmChannelList(Long taskId)
        throws Exception;
    
    /**
     * 获取任务告警渠道
     * @param taskId 任务Id
     * @param grade 告警等级
     * @return 任务告警渠道
     * @throws Exception 异常
     */
    public abstract TaskAlarmChannelInfoEntity getAlarmChannel(Long taskId, Integer grade)
        throws Exception;
    
    /**
     * 获取告警项
     * @param taskId 任务Id
     * @return 告警项
     * @throws Exception 异常
     */
    public abstract TaskAlarmItemsEntity getTaskAlarmItems(Long taskId)
        throws Exception;
    
    /**
     * 获取告警阈值
     * @param taskId 任务Id 
     * @throws Exception 异常
     * @return 任务告警阈值
     */
    public abstract TaskAlarmThresholdEntity getAlarmThreshold(Long taskId)
        throws Exception;
    
    /**
     * 新增告警渠道
     * @param alarmChannel 告警渠道
     * @throws Exception 异常
     */
    public abstract void addAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception;
    
    /**
     * 更新告警渠道
     * @param alarmChannel 告警渠道
     * @throws Exception 异常
     */
    public abstract void updateAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception;
    
    /**
     * 删除任务的告警渠道信息
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    public abstract void deleteAlarmChannel(Long taskId)
        throws Exception;
    
    /**
     * 新增告警阈值
     * @param alarmThreshold 告警阈值
     * @throws Exception 异常
     */
    public abstract void addAlarmThreshold(TaskAlarmThresholdEntity alarmThreshold)
        throws Exception;
    
    /**
     * 更新告警阈值
     * @param alarmThreshold 告警阈值
     * @throws Exception 异常
     */
    public abstract void updateAlarmThreshold(TaskAlarmThresholdEntity alarmThreshold)
        throws Exception;
    
    /**
     * 删除任务的告警阈值
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    public abstract void deleteAlarmThreshold(Long taskId)
        throws Exception;
    
    /**
     * 新增告警类型项
     * @param alarmItems 告警类型项
     * @throws Exception 异常
     */
    public abstract void addAlarmItems(TaskAlarmItemsEntity alarmItems)
        throws Exception;
    
    /**
     * 更新告警类型项
     * @param alarmItems 告警类型项
     * @throws Exception 异常
     */
    public abstract void updateAlarmItems(TaskAlarmItemsEntity alarmItems)
        throws Exception;
    
    /**
     * 删除任务的告警类型项
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    public abstract void deleteAlarmItems(Long taskId)
        throws Exception;
    
    /**
     * 更新告警处理信息
     * @param alarmFact 告警处理信息
     * @throws Exception 异常
     */
    public abstract void updateAlarmFact(AlarmFactInfoEntity alarmFact)
        throws Exception;
    
    /**
     * 由查询条件获取告警事实信息列表
     * @param search 查询条件
     * @return 告警事实信息列表
     * @throws Exception 异常
     */
    public abstract List<AlarmFactInfoEntity> getAlarmFacts(AlarmFactSearch search)
        throws Exception;
    
    /**
     * 由查询条件获取告警事实信息列表总数
     * @param search 查询条件
     * @return 告警事实信息列表总数
     * @throws Exception 统一封装的异常
     */
    public abstract Integer getAlarmFactCount(AlarmFactSearch search)
        throws Exception;
    
    /**
     * 获取任务隶属相关信息
     * 
     * @param taskIdList  任务ID列表
     * @return 任务隶属相关信息
     */
    abstract List<TaskOwnerInfo> getTaskOwnersByIds(List<Long> taskIdList);
    
    /**
     * 获取所有目标数据库配置信息
     * @return 所有目标数据库配置信息
     * @throws CException 异常
     */
    abstract List<DBServerConfigEntity> getDBServers()
        throws CException;
    
}