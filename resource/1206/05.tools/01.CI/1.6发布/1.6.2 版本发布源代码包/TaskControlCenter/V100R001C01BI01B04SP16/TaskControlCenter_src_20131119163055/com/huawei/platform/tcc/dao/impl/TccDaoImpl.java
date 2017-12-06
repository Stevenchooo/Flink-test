/*
 * 
 * 文 件 名:  TccDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190165
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.tcc.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.AlarmFactSearch;
import com.huawei.platform.tcc.domain.DependCyclesExistNum;
import com.huawei.platform.tcc.domain.InstanceRelationSearch;
import com.huawei.platform.tcc.domain.Log2DBQueryParam;
import com.huawei.platform.tcc.domain.LongTimeShellParam;
import com.huawei.platform.tcc.domain.MultiNameSearch;
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
import com.huawei.platform.tcc.entity.TaskIDSerialEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.entity.TaskStepExchangeEntity;
import com.huawei.platform.tcc.entity.TaskStepIdSerialEntity;
import com.huawei.platform.tcc.entity.UserGroupMappingEntity;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * TCC的数据库操作类实现
 * 
 * @author z00190165
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-11-10]
 */
public class TccDaoImpl extends SqlSessionDaoSupport implements TccDao
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 2560796253460468758L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccDaoImpl.class);
    
    /************************* Tcc 任务表[begin] ***********************************************/
    /**
     * 保存任务信息到任务表
     * 
     * @param entity
     *            任务信息
     * @throws Exception
     *             统一封装的异常
     */
    @Override
    public void addTask(TaskEntity entity)
        throws Exception
    {
        LOGGER.debug("Enter addTask...");
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.task.addTask", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get Task[entity is {}] failed!", entity, e);
            throw e;
        }
    }
    
    /**
     * 更新任务信息
     * 
     * @param entity
     *            任务信息
     * @throws Exception
     *             统一封装的异常
     */
    @Override
    public void updateTask(TaskEntity entity)
        throws Exception
    {
        LOGGER.debug("Enter updateTask...");
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.task.updateTask", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("update Task[entity is {}] failed!", entity, e);
            throw e;
        }
    }
    
    /**
     * 更新OS组关联的所有业务Id
     * 
     * @param entity  OS组
     * @throws Exception   统一封装的异常
     */
    @Override
    public void updateTaskServiceId(OSGroupInfoEntity entity)
        throws Exception
    {
        LOGGER.debug("Enter updateTaskServiceId. entity is {}", entity);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.task.updateServiceIdByGroup", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("updateTaskServiceId failed!entity is {}", entity, e);
            throw e;
        }
    }
    
    /**
     * 更新OS用户关联的os组已经业务Id
     * 
     * @param entity  OS用户、组、业务对象
     * @throws Exception   统一封装的异常
     */
    @Override
    public void updateTaskGroupServiceId(OSUserGroupServiceEntity entity)
        throws Exception
    {
        LOGGER.debug("Enter updateTaskGroupServiceId. entity is {}", entity);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.task.updateTaskGroupServiceId", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("updateTaskGroupServiceId failed!entity is {}", entity, e);
            throw e;
        }
    }
    
    /**
     * 启动任务，要求任务必须是停止的
     * @param entity 任务信息
     * @throws Exception 统一封装的异常
     * @return 是否启动成功
     */
    @Override
    public boolean startTask(TaskEntity entity)
        throws Exception
    {
        LOGGER.debug("Enter updateTask...");
        try
        {
            if (null == entity || null == entity.getTaskState() || entity.getTaskState() != TaskState.NORMAL)
            {
                return false;
            }
            
            int objRtn = getSqlSession().update("com.huawei.platform.tcc.dao.task.startTask", entity);
            if (objRtn == 1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("start Task[entity is {}] failed!", entity, e);
            throw e;
        }
    }
    
    /**
     * 通过taskId获取任务实体
     * 
     * @param taskId
     *            任务Id
     * @return 任务实体
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public TaskEntity getTask(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter getTask...");
        try
        {
            return (TaskEntity)getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "task.getTask", taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("get Task[taskId={}] failed!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过taskName获取任务实体
     * 
     * @param taskName
     *            任务名
     * @return 任务实体
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public TaskEntity getTask(String taskName)
        throws CException
    {
        LOGGER.debug("Enter getTask. taskName is {}", taskName);
        try
        {
            return (TaskEntity)getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "task.getTaskByName",
                taskName);
        }
        catch (Exception e)
        {
            LOGGER.error("get Task[taskName={}] failed!", taskName, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取启用任务或者禁用任务列表
     * 
     * @param taskEnableFlag
     *            任务是否启动标志
     * @return 启用任务或者禁用任务列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getTaskList(Integer taskEnableFlag)
        throws CException
    {
        List<TaskEntity> tasks;
        LOGGER.debug("Enter getTaskList...");
        try
        {
            tasks =
                (List<TaskEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getTaskList",
                    taskEnableFlag);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == tasks)
        {
            tasks = new ArrayList<TaskEntity>(0);
        }
        
        LOGGER.debug("get taskList success...");
        
        return tasks;
    }
    
    /**
     * 获取启用任务或者禁用任务列表[仅获取如下字段]
     * Task_ID,Task_State,Cycle_Depend_Flag,Cycle_Type,
     * Depend_Task_ID_List,Cycle_Length,Priority,weight,
     * start_Time,Task_Name
     * 
     * @param taskEnableFlag
     *            任务是否启动标志
     * @return 启用任务或者禁用任务列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getSchTaskList(Integer taskEnableFlag)
        throws CException
    {
        List<TaskEntity> tasks;
        LOGGER.debug("Enter getTaskList...");
        try
        {
            tasks =
                (List<TaskEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getSchTaskList",
                    taskEnableFlag);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == tasks)
        {
            tasks = new ArrayList<TaskEntity>(0);
        }
        
        LOGGER.debug("get taskList success...");
        
        return tasks;
    }
    
    /**
     * 获取全部任务列表
     * 
     * @return 全部任务列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getTaskList()
        throws CException
    {
        List<TaskEntity> tasks;
        LOGGER.debug("Enter getTaskList...");
        try
        {
            tasks =
                (List<TaskEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getAllTaskList");
        }
        catch (Exception e)
        {
            LOGGER.error("get taskList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == tasks)
        {
            tasks = new ArrayList<TaskEntity>(0);
        }
        
        LOGGER.debug("get taskList success...");
        
        return tasks;
    }
    
    /**
     * 获取任务ID列表
     * 
     * @param taskIdList
     *            任务ID列表
     * @return 任务ID列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getTaskList(List<Long> taskIdList)
        throws CException
    {
        List<TaskEntity> tasks = null;
        LOGGER.debug("Enter getTaskList...");
        try
        {
            if (null != taskIdList && !taskIdList.isEmpty())
            {
                tasks =
                    (List<TaskEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                        + "task.getTaskListByIds",
                        taskIdList);
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("get taskList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        LOGGER.debug("get taskList success...");
        
        if (null == tasks)
        {
            tasks = new ArrayList<TaskEntity>(0);
        }
        return tasks;
    }
    
    /**
     * 获取任务隶属相关信息
     * 
     * @param taskIdList  任务ID列表
     * @return 任务隶属相关信息
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskOwnerInfo> getTaskOwnersByIds(List<Long> taskIdList)
    {
        List<TaskOwnerInfo> taskOwners = null;
        LOGGER.debug("Enter getTaskOwnersByIds...");
        try
        {
            if (null != taskIdList && !taskIdList.isEmpty())
            {
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao.task.getTaskOwnersByIds", taskIdList);
                if (null != objRtn)
                {
                    taskOwners = (List<TaskOwnerInfo>)objRtn;
                }
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskOwnersByIds failed!", e);
        }
        
        LOGGER.debug("getTaskOwnersByIds success...");
        
        if (null == taskOwners)
        {
            taskOwners = new ArrayList<TaskOwnerInfo>(0);
        }
        return taskOwners;
    }
    
    /**
     * 获取全部任务ID列表
     * 
     * @param entity
     *            任务查询条件类
     * @return 任务ID列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getTaskAllList(TaskSearchEntity entity)
        throws CException
    {
        List<TaskEntity> taskList = null;
        LOGGER.debug("Enter getTaskAllList...");
        try
        {
            taskList =
                (List<TaskEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getTaskAllList",
                    entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return taskList;
    }
    
    /**
     * 通过名字模糊查询任务信息列表(任务名集合为多个则精确查询)
     * 
     * @param entity
     *            任务查询条件类
     * @return 任务信息列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getTaskListByNames(TaskSearchEntity entity)
        throws CException
    {
        try
        {
            if (null != entity && !entity.getTaskNames().isEmpty())
            {
                Object obj =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getTaskListByNames", entity);
                
                if (null != obj)
                {
                    return (List<TaskEntity>)obj;
                }
            }
            
            return new ArrayList<TaskEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskListByNames failed.", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过名字模糊查询任务信息的条数(任务名集合为多个则精确查询)
     * 
     * @param entity
     *            查询条件
     * @return 记录条数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public Integer getTaskListSizeByNames(TaskSearchEntity entity)
        throws CException
    {
        try
        {
            if (null == entity || (null != entity.getTaskNames() && entity.getTaskNames().isEmpty()))
            {
                return 0;
            }
            
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "task.getTaskListSizeByNames", entity);
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.error("get TaskListSizeByNames failed.", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过名字模糊查询任务ID集合(任务名集合为多个则精确查询)
     * @param entity  查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getTaskIdsByNames(TaskSearchEntity entity)
        throws CException
    {
        try
        {
            if (null != entity && !entity.getTaskNames().isEmpty())
            {
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getTaskIdsByNames", entity);
                if (null != objRtn)
                {
                    return (List<Long>)objRtn;
                }
            }
            
            return new ArrayList<Long>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskIdsByNames failed. entity is {}", entity, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务
     * 
     * @param taskId
     *            传入的任务ID
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteTask(Long taskId)
        throws CException
    {
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao." + "task.deleteTask", taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTask failed, taskId is [{}]", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public Integer getTaskListSize(TaskSearchEntity entity)
        throws CException
    {
        LOGGER.debug("Enter getTaskListSize...");
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "task.getTaskListSize", entity);
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get taskListSize failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskStepEntity> getTaskStepList(List<Long> taskIds)
        throws CException
    {
        List<TaskStepEntity> taskSteps = null;
        LOGGER.debug("Enter getTaskStepList...");
        try
        {
            if (!taskIds.isEmpty())
            {
                taskSteps =
                    (List<TaskStepEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                        + "taskstep.getTaskStepList",
                        taskIds);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get taskStepList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == taskSteps)
        {
            taskSteps = new ArrayList<TaskStepEntity>(0);
        }
        
        LOGGER.debug("get taskStepList success...");
        
        return taskSteps;
    }
    
    /**
     * 获取步骤的部署节点
     * 
     * @param taskId
     *            任务Id
     * @param stepId
     *            步骤Id
     * @return 获取步骤的执行命令的主机地址
     */
    @Override
    public String getDeployedNodes(Long taskId, Integer stepId)
    {
        LOGGER.debug("Enter getStepHost. taskId is [{}], stepId is [{}]", taskId, stepId);
        try
        {
            TaskStepEntity taskSE = new TaskStepEntity();
            taskSE.setTaskId(taskId);
            taskSE.setStepId(stepId);
            
            Object nodes = getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskstep.getDeployedNodes", taskSE);
            
            if (null != nodes)
            {
                return nodes.toString();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get StepHost[taskId={},stepId={}] failed!", new Object[] {taskId, stepId, e});
        }
        
        return "";
    }
    
    /************************* Tcc 任务步骤表[begin] ***********************************************/
    
    /************************* Tcc 任务运行状态表[begin] ***********************************************/
    /**
     * 指定任务的最大的周期Id
     * 
     * @param taskId
     *            任务ID
     * @return 最大的周期Id
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public String getMinCycleID(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter getMinCycleID..., taskId is [{}]", taskId);
        try
        {
            Object minCycleId =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "taskrunningstate.getMinCycleID", taskId);
            if (null != minCycleId)
            {
                return minCycleId.toString();
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get MinCycleID failed, taskId is [{}]!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 指定任务的最大的周期Id
     * 
     * @param taskId
     *            任务ID
     * @return 最大的周期Id
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public String getMaxCycleID(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter getMaxCycleID..., taskId is [{}]", taskId);
        try
        {
            Object maxCycleId =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "taskrunningstate.getMaxCycleID", taskId);
            if (null != maxCycleId)
            {
                return maxCycleId.toString();
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get MaxCycleID failed, taskId is [{}]!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public boolean isExistsTaskRS(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter isExistsTaskRS. taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(taskId);
            taskRS.setCycleId(cycleId);
            Object count =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "taskrunningstate.isExistsTaskRS", taskRS);
            if (null != count)
            {
                return (Integer)count > 0;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("isExistsTaskRS execute failed taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public boolean existsAllTaskIDCycleID(Integer equalCount, String taskIdCycleIds)
        throws CException
    {
        LOGGER.debug("Enter existsAllTaskIDCycleID..., equalCount is [{}], taskIdCycleIds is [{}]",
            equalCount,
            taskIdCycleIds);
        try
        {
            Object count =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "taskrunningstate.getCountTaskIDCycleID",
                    taskIdCycleIds);
            if (null != count)
            {
                if (count.equals(equalCount))
                {
                    return true;
                }
            }
            else
            {
                if (null == equalCount || equalCount.equals(0))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("existsAllTaskIDCycleID excute failed, equalCount is [{}], taskIdCycleIds is [{}]!",
                new Object[] {equalCount, taskIdCycleIds, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 新增任务运行状态信息
     * 
     * @param taskRS  任务运行状态实体
     * @return 添加是否成功
     * @throws CException 统一封装的异常
     */
    @Override
    public boolean addTaskRunningState(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("Enter addTaskRunningState..., taskRS is [{}]", taskRS);
        try
        {
            int rows =
                getSqlSession().insert("com.huawei.platform.tcc.dao.taskrunningstate.addTaskRunningState", taskRS);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add TaskRunningState error, taskRS is [{}]!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 新增任务运行状态信息
     * 
     * @param taskRSs 任务运行状态实体列表
     * @return 添加是否成功
     * @throws CException  统一封装的异常
     */
    @Override
    public boolean addTaskRunningState(List<TaskRunningStateEntity> taskRSs)
        throws CException
    {
        LOGGER.debug("Enter addTaskRunningState..., taskRSs is [{}]", taskRSs);
        try
        {
            int rows =
                getSqlSession().insert("com.huawei.platform.tcc.dao.taskrunningstate.addTaskRunningStates", taskRSs);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add TaskRunningState error, taskRSs is [{}]!", taskRSs, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 获取任务运行状态信息
     * 
     * @param taskRS
     *            任务运行状态实体
     * @return 任务运行状态信息
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public TaskRunningStateEntity getTaskRunningState(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("Enter getTaskRunningState..., taskRS is [{}]", taskRS);
        try
        {
            return (TaskRunningStateEntity)getSqlSession().selectOne("com.huawei.platform.tcc.dao."
                + "taskrunningstate.getTaskRunningState",
                taskRS);
        }
        catch (Exception e)
        {
            LOGGER.error("get TaskRunningState error, taskRS is [{}]!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有需要执行的周期任务（运行状态信息列表）
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
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getNeedRunCycleTasks(Long taskId, Date startTime, String maxCycleId)
        throws CException
    {
        List<TaskRunningStateEntity> taskRSLst;
        LOGGER.debug("Enter getNeedRunCycleTasks...");
        try
        {
            TaskCycleParam taskCycleParam = new TaskCycleParam();
            taskCycleParam.setTaskId(taskId);
            String minCycleId;
            if (null != startTime && startTime.after(TccConfig.getBenchDate()))
            {
                Calendar cTime = Calendar.getInstance();
                cTime.setTime(startTime);
                if (0 != cTime.get(Calendar.SECOND))
                {
                    cTime.add(Calendar.MINUTE, 1);
                }
                minCycleId = TccUtil.covDate2CycleID(startTime);
            }
            else
            {
                minCycleId = TccConfig.getMinCycleId();
            }
            taskCycleParam.setMinCycleId(minCycleId);
            taskCycleParam.setMaxCycleId(maxCycleId);
            taskRSLst =
                (List<TaskRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getNeedRunCycleTasks",
                    taskCycleParam);
        }
        catch (Exception e)
        {
            LOGGER.error("get getNeedRunCycleTasks failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == taskRSLst)
        {
            taskRSLst = new ArrayList<TaskRunningStateEntity>(0);
        }
        
        LOGGER.debug("get needRunCycleTasks success...");
        
        return taskRSLst;
    }
    
    /**
     * 获取所有需要执行的周期任务（运行状态信息列表）
     * 
     * @param taskId 任务Id
     * @param startTime 开始时间
     * @return 所有需要执行的周期任务
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getNeedRunCycleTasks(Long taskId, Date startTime)
        throws CException
    {
        List<TaskRunningStateEntity> taskRSLst;
        LOGGER.debug("Enter getNeedRunCycleTasks...");
        try
        {
            TaskCycleParam taskCycleParam = new TaskCycleParam();
            taskCycleParam.setTaskId(taskId);
            taskCycleParam.setMinCycleId(TccUtil.getMinCycleId(startTime));
            taskRSLst =
                (List<TaskRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getAllNeedRunCycleTasks",
                    taskCycleParam);
        }
        catch (Exception e)
        {
            LOGGER.error("get getNeedRunCycleTasks failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == taskRSLst)
        {
            taskRSLst = new ArrayList<TaskRunningStateEntity>(0);
        }
        
        LOGGER.debug("get needRunCycleTasks success...");
        
        return taskRSLst;
    }
    
    /**
     * 获取所有可能需要告警的的任务周期
     * 
     * @param taskId 任务Id
     * @param maxCycleId 最大的周期Id
     * @param startTime 开始时间
     * @return 所有可能需要告警的的任务周期
     * @throws Exception
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getNeedAlarmTaskRSs(Long taskId, Date startTime, String maxCycleId)
        throws Exception
    {
        List<TaskRunningStateEntity> taskRSLst;
        LOGGER.debug("Enter getNeedAlarmTaskRunningStates...");
        try
        {
            TaskCycleParam taskCycleParam = new TaskCycleParam();
            taskCycleParam.setTaskId(taskId);
            String minCycleId;
            if (null != startTime && startTime.after(TccConfig.getBenchDate()))
            {
                Calendar cTime = Calendar.getInstance();
                cTime.setTime(startTime);
                if (0 != cTime.get(Calendar.SECOND))
                {
                    cTime.add(Calendar.MINUTE, 1);
                }
                minCycleId = TccUtil.covDate2CycleID(startTime);
            }
            else
            {
                minCycleId = TccConfig.getMinCycleId();
            }
            taskCycleParam.setMinCycleId(minCycleId);
            taskCycleParam.setMaxCycleId(maxCycleId);
            taskRSLst =
                (List<TaskRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getNeedAlarmTaskRSsByTask",
                    taskCycleParam);
        }
        catch (Exception e)
        {
            LOGGER.error("getNeedAlarmTaskRunningStates failed!", e);
            throw e;
        }
        
        if (null == taskRSLst)
        {
            taskRSLst = new ArrayList<TaskRunningStateEntity>(0);
        }
        
        LOGGER.debug("getNeedAlarmTaskRunningStates success...");
        
        return taskRSLst;
    }
    
    /**
     * 获取所有需要执行的最老的周期任务（运行状态信息列表）
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
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getTopOldNeedRunCycleTasks(Long taskId, Date startTime, String maxCycleId)
        throws CException
    {
        List<TaskRunningStateEntity> taskRSLst;
        LOGGER.debug("Enter getTopOldNeedRunCycleTasks...");
        try
        {
            TaskCycleParam taskCycleParam = new TaskCycleParam();
            taskCycleParam.setTaskId(taskId);
            String minCycleId;
            if (null != startTime && startTime.after(TccConfig.getBenchDate()))
            {
                Calendar cTime = Calendar.getInstance();
                cTime.setTime(startTime);
                if (0 != cTime.get(Calendar.SECOND))
                {
                    cTime.add(Calendar.MINUTE, 1);
                }
                minCycleId = TccUtil.covDate2CycleID(cTime.getTime());
            }
            else
            {
                minCycleId = TccConfig.getMinCycleId();
            }
            taskCycleParam.setMinCycleId(minCycleId);
            taskCycleParam.setMaxCycleId(maxCycleId);
            taskRSLst =
                (List<TaskRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getTopOldNeedRunCycleTasks",
                    taskCycleParam);
        }
        catch (Exception e)
        {
            LOGGER.error("get getTopOldNeedRunCycleTasks failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == taskRSLst)
        {
            taskRSLst = new ArrayList<TaskRunningStateEntity>(0);
        }
        
        LOGGER.debug("get getTopOldNeedRunCycleTasks success...");
        
        return taskRSLst;
    }
    
    /**
     * 获取所有时间上可以执行的最老的任务周期列表（运行状态信息列表）
     * 
     * @param excludeTaskIds  忽略的任务Id集合
     * @param bench  基准时间
     * @param curr 当前时间
     * @return 所有需要执行的周期任务
     * @throws CException  统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getTopOldNeedRunCycles(List<Long> excludeTaskIds, Date bench, Date curr)
        throws CException
    {
        List<TaskRunningStateEntity> taskRSLst;
        LOGGER.debug("Enter getTopOldNeedRunCycles...");
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("excludeTaskIds", StringUtils.join(excludeTaskIds, ','));
            param.put("bench", bench);
            param.put("curr", curr);
            taskRSLst =
                (List<TaskRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getTopOldNeedRunCycles",
                    param);
        }
        catch (Exception e)
        {
            LOGGER.error("get getTopOldNeedRunCycles failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == taskRSLst)
        {
            taskRSLst = new ArrayList<TaskRunningStateEntity>(0);
        }
        
        LOGGER.debug("get getTopOldNeedRunCycleTasks success...");
        
        return taskRSLst;
    }
    
    /**
     * 获取所有时间上可以执行的最老的任务周期列表（运行状态信息列表）
     * 
     * @param depends  周期以及周期Id构成的字符串序列"(1000,'20121011-00',1000,'20121010-00',3),..."
     * @return 所有需要执行的周期任务
     * @throws CException  统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DependCyclesExistNum> getDependsOkCycleNums(String depends)
        throws CException
    {
        List<DependCyclesExistNum> dependsExistNums;
        LOGGER.debug("Enter getDependsOkCycleNums...");
        try
        {
            dependsExistNums =
                (List<DependCyclesExistNum>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getDependsOkCycleNums",
                    depends);
        }
        catch (Exception e)
        {
            LOGGER.error("get getDependsOkCycleNums failed!length of depends is {}!", depends.length(), e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == dependsExistNums)
        {
            dependsExistNums = new ArrayList<DependCyclesExistNum>(0);
        }
        
        LOGGER.debug("get getDependsOkCycleNums success...");
        
        return dependsExistNums;
    }
    
    /**
     * 存在的周期任务的总数
     * 
     * @param taskIdCycleIDStateLst
     *            任务id与周期id以及状态构成的集合,eg:((100,2011101-12,2),(100,2011102-12,2))
     * @return 存在的周期任务的总数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int getTaskRunningStateCount(List<TaskRunningStateEntity> taskIdCycleIDStateLst)
        throws CException
    {
        LOGGER.debug("Enter getTaskRunningStateCount. taskIdCycleIDStateLst is [{}]", taskIdCycleIDStateLst);
        try
        {
            if (!taskIdCycleIDStateLst.isEmpty())
            {
                Map<String, String> maps = new HashMap<String, String>();
                //任务周期状态
                StringBuilder tcsSB = new StringBuilder();
                //任务周期
                StringBuilder tcSB = new StringBuilder();
                Set<String> dis = new HashSet<String>();
                StringBuilder temp = new StringBuilder();
                tcSB.append('(');
                tcsSB.append('(');
                for (TaskRunningStateEntity tasRSE : taskIdCycleIDStateLst)
                {
                    temp.delete(0, temp.length());
                    temp.append('(');
                    temp.append(tasRSE.getTaskId());
                    temp.append(",'");
                    temp.append(tasRSE.getCycleId());
                    temp.append("')");
                    if (!dis.contains(temp.toString()))
                    {
                        tcSB.append(temp);
                        tcSB.append(',');
                        dis.add(temp.toString());
                    }
                    
                    tcsSB.append('(');
                    tcsSB.append(tasRSE.getTaskId());
                    tcsSB.append(",'");
                    tcsSB.append(tasRSE.getCycleId());
                    tcsSB.append("',");
                    tcsSB.append(tasRSE.getState());
                    tcsSB.append("),");
                }
                
                //删除逗号，添加）
                tcsSB.setCharAt(tcsSB.length() - 1, ')');
                tcSB.setCharAt(tcSB.length() - 1, ')');
                maps.put("tcsSB", tcsSB.toString());
                maps.put("tcSB", tcSB.toString());
                Object count =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao."
                        + "taskrunningstate.getTaskRunningStateCount",
                        maps);
                
                if (null != count)
                {
                    return (Integer)count;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRunningStateCount excute failed, taskIdCycleIDStateLst is [{}]!",
                taskIdCycleIDStateLst,
                e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return 0;
    }
    
    /**
     * 存在的周期任务的总数（不考虑运行状态）
     * 
     * @param taskIdCycleIDStateLst
     *            任务id与周期id以及状态构成的集合,eg:((100,2011101-12,2),(100,2011102-12,2))
     * @return 存在的周期任务的总数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int getTaskRunningStateExceptStateCount(List<TaskRunningStateEntity> taskIdCycleIDStateLst)
        throws CException
    {
        LOGGER.debug("Enter getTaskRunningStateExceptStateCount. taskIdCycleIDStateLst is [{}]", taskIdCycleIDStateLst);
        try
        {
            if (!taskIdCycleIDStateLst.isEmpty())
            {
                Object count =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao."
                        + "taskrunningstate.getTaskRunningStateExceptStateCount",
                        taskIdCycleIDStateLst);
                
                if (null != count)
                {
                    return (Integer)count;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRunningStateExceptStateCount excute failed, " + "taskIdCycleIDStateLst is [{}]!",
                taskIdCycleIDStateLst,
                e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return 0;
    }
    
    /**
     * 更新任务运行状态
     * 
     * @param taskRS
     *            任务运行状态
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int updateTaskRunningState(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("Enter updateTaskRunningState..., taskRS is [{}]", taskRS);
        try
        {
            return getSqlSession().update("com.huawei.platform.tcc.dao.taskrunningstate" + ".updateTaskRunningState",
                taskRS);
        }
        catch (Exception e)
        {
            LOGGER.error("update TaskRunningState error, taskRS is [{}]!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 更新任务运行状态,同时将运行次数自动加1
     * @param taskRS 任务运行状态
     * @return 更新的行数
     * @throws CException 统一封装的异常
     */
    @Override
    public int updateTaskRsIncReturnTimes(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("Enter updateTaskRsIncReturnTimes..., taskRS is [{}]", taskRS);
        try
        {
            return getSqlSession().update("com.huawei.platform.tcc.dao.taskrunningstate"
                + ".updateTaskRsIncReturnTimes",
                taskRS);
        }
        catch (Exception e)
        {
            LOGGER.error("updateTaskRsIncReturnTimes error, taskRS is {}!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 更新任务运行状态
     * 
     * @param taskRS
     *            任务运行状态
     * @return 更新的行数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int updateTaskRunningStateWithNullTime(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("Enter updateTaskRunningStateWithNullTime..., taskRS is [{}]", taskRS);
        try
        {
            return getSqlSession().update("com.huawei.platform.tcc.dao.taskrunningstate"
                + ".updateTaskRunningStateWithNullTime",
                taskRS);
        }
        catch (Exception e)
        {
            LOGGER.error("update TaskRunningStateWithNullTime error, taskRS is [{}]!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public int updateAppendBeginDependTaskList(Long taskId, String cycleId, String appendTaskCycleId)
        throws CException
    {
        LOGGER.debug("Enter updateAppendBeginDependTaskList..., param is [taskId=[{}],cycleId=[{}]"
            + ",appendTaskCycleId=[{}]]", new Object[] {taskId, cycleId, appendTaskCycleId});
        try
        {
            TaskRunningStateEntity taskRSDep = new TaskRunningStateEntity();
            taskRSDep.setTaskId(taskId);
            taskRSDep.setCycleId(cycleId);
            taskRSDep.setBeginDependTaskList(appendTaskCycleId);
            return getSqlSession().update("com.huawei.platform.tcc.dao.taskrunningstate"
                + ".updateAppendDependTaskList",
                taskRSDep);
        }
        catch (Exception e)
        {
            LOGGER.debug("update AppendBeginDependTaskList failed, param is [taskId=[{}],cycleId=[{}],"
                + "appendTaskCycleId=[{}]], error is [{}]", new Object[] {taskId, cycleId, appendTaskCycleId}, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public int updateAppendFinishDependTaskList(Long taskId, String cycleId, String appendTaskCycleId)
        throws CException
    {
        LOGGER.debug("Enter updateAppendFinishDependTaskList..., param is [taskId=[{}],cycleId=[{}]"
            + ",appendTaskCycleId=[{}]]", new Object[] {taskId, cycleId, appendTaskCycleId});
        try
        {
            TaskRunningStateEntity taskRSDep = new TaskRunningStateEntity();
            taskRSDep.setTaskId(taskId);
            taskRSDep.setCycleId(cycleId);
            taskRSDep.setFinishDependTaskList(appendTaskCycleId);
            return getSqlSession().update("com.huawei.platform.tcc.dao.taskrunningstate"
                + ".updateAppendDependTaskList",
                taskRSDep);
        }
        catch (Exception e)
        {
            LOGGER.debug("update AppendFinishDependTaskList failed, param is [taskId=[{}],cycleId=[{}],"
                + "appendTaskCycleId=[{}]], error is [{}]", new Object[] {taskId, cycleId, appendTaskCycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public boolean existsTimeOutBatchRS(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter existsTimeOutBatchRS..., taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            batchRS.setState(RunningState.TIMEOUT);
            
            Object count =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "batchrunningstate.getCountTaskIDCycleID",
                    batchRS);
            if (null != count)
            {
                if ((Integer)count > 0)
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("existsTimeOutBatchRS excute failed, taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return false;
    }
    
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
    @Override
    public boolean existsErrorBatchRS(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter existsTimeOutBatchRS..., taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            batchRS.setState(RunningState.ERROR);
            
            Object count =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "batchrunningstate.getCountTaskIDCycleID",
                    batchRS);
            if (null != count)
            {
                if ((Integer)count > 0)
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("existsTimeOutBatchRS excute failed, taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return false;
    }
    
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
    @Override
    public boolean existsBatchRS(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter existsTimeOutBatchRS..., taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            
            Object count =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.batchrunningstate.getBatchRSCount", batchRS);
            if (null != count)
            {
                if ((Integer)count > 0)
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("existsTimeOutBatchRS excute failed, taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return false;
    }
    
    /**
     * 新增批次运行状态记录
     * 
     * @param batchRS
     *            批次运行状态记录
     * @return 新增是否成功
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public boolean addBatchRunningState(BatchRunningStateEntity batchRS)
        throws CException
    {
        LOGGER.debug("Enter addTaskRunningState..., taskRS is [{}]", batchRS);
        try
        {
            // null转换为空字符串
            if (null == batchRS.getJobInput())
            {
                batchRS.setJobInput("");
            }
            
            int rows =
                getSqlSession().insert("com.huawei.platform.tcc.dao.batchrunningstate.addBatchRunningState", batchRS);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add BatchRunningState error, batchRS is [{}]!", batchRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 获取批次运行状态记录
     * 
     * @param batchRS
     *            批次运行状态记录
     * @return 批次运行状态记录
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public BatchRunningStateEntity getBatchRunningState(BatchRunningStateEntity batchRS)
        throws CException
    {
        LOGGER.debug("Enter getBatchRunningState..., taskRS is [{}]", batchRS);
        try
        {
            // null转换为空字符串
            if (null == batchRS.getJobInput())
            {
                batchRS.setJobInput("");
            }
            
            return (BatchRunningStateEntity)getSqlSession().selectOne("com.huawei.platform.tcc.dao"
                + ".batchrunningstate.getBatchRunningState",
                batchRS);
        }
        catch (Exception e)
        {
            LOGGER.error("get BatchRunningState error, batchRS is [{}]!", batchRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public int getMaxBatchId(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter getMaxBatchId..., taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            
            Object maxBatchId =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "batchrunningstate.getMaxBatchId", batchRS);
            if (null != maxBatchId)
            {
                return Integer.parseInt(maxBatchId.toString());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getMaxBatchId excute failed, taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return 0;
    }
    
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
    @Override
    public int deleteBatchRunningStates(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter deleteBatchRunningStates..., taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            
            return getSqlSession().delete("com.huawei.platform.tcc.dao.batchrunningstate.deleteBatchRunningStates",
                batchRS);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteBatchRunningStates excute failed, taskId is [{}], cycleId is [{}]!", new Object[] {
                taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 更新批任务运行状态信息
     * 
     * @param batchRS
     *            批任务运行状态信息
     * @return 影响的行数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int updateBatchRunningState(BatchRunningStateEntity batchRS)
        throws CException
    {
        LOGGER.debug("Enter updateBatchRunningState..., batchRS is [{}]", batchRS);
        try
        {
            return getSqlSession().update("com.huawei.platform.tcc.dao.batchrunningstate.updateBatchRunningState",
                batchRS);
        }
        catch (Exception e)
        {
            LOGGER.error("updateBatchRunningState excute failed, batchRS is [{}]!", batchRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @SuppressWarnings("unchecked")
    @Override
    public List<BatchRunningStateEntity> getUnCompletedBatch(Long taskId, String cycleId)
        throws CException
    {
        List<BatchRunningStateEntity> batchRSLst;
        LOGGER.debug("Enter getUnCompletedBatch. taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            
            batchRSLst =
                (List<BatchRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "batchrunningstate.getUnCompletedBatch",
                    batchRS);
        }
        catch (Exception e)
        {
            LOGGER.error("get UnCompletedBatch failed, taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == batchRSLst)
        {
            batchRSLst = new ArrayList<BatchRunningStateEntity>();
        }
        
        LOGGER.debug("get getUnCompletedBatch success...");
        return batchRSLst;
    }
    
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
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getJobInputFiles(Long taskId, String cycleId)
        throws CException
    {
        List<String> batchFileLst;
        LOGGER.debug("Enter getJobInputFiles. taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(taskId);
            batchRS.setCycleId(cycleId);
            
            batchFileLst =
                (List<String>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "batchrunningstate.getJobInputFiles",
                    batchRS);
        }
        catch (Exception e)
        {
            LOGGER.error("get getJobInputFiles failed, taskId is [{}], cycleId is [{}]!", new Object[] {taskId,
                cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        if (null == batchFileLst)
        {
            batchFileLst = new ArrayList<String>();
        }
        
        LOGGER.debug("get getJobInputFiles success. taskId is [{}], cycleId is [{}]", taskId, cycleId);
        return batchFileLst;
    }
    
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
    @Override
    public int deleteStepRunningStates(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("Enter deleteStepRunningStates. taskId is [{}], cycleId is [{}]", taskId, cycleId);
        try
        {
            StepRunningStateEntity stepRS = new StepRunningStateEntity();
            stepRS.setTaskId(taskId);
            stepRS.setCycleId(cycleId);
            
            return getSqlSession().delete("com.huawei.platform.tcc.dao.steprunningstate"
                + ".deleteStepRunningStatesInCycleTask",
                stepRS);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteStepRunningStates excute failed, taskId is [{}], cycleId is [{}]!", new Object[] {
                taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取指定周期任务的所有步骤运行状态为start而且jobid存在的步骤
     * 
     * @param taskId
     *            任务Id
     * @param cycleId
     *            周期Id
     * @return 已经启动过(状态为start)的步骤运行状态信息
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<StepRunningStateEntity> getStartedStepRunningStates(Long taskId, String cycleId)
    {
        List<StepRunningStateEntity> stepRSLst = null;
        LOGGER.debug("Enter getStartedStepRunningStates.");
        try
        {
            StepRunningStateEntity stepRS = new StepRunningStateEntity();
            stepRS.setTaskId(taskId);
            stepRS.setCycleId(cycleId);
            
            stepRSLst =
                (List<StepRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "steprunningstate.getStartedStepRunningStates",
                    stepRS);
        }
        catch (Exception e)
        {
            LOGGER.error("get getStartedStepRunningStates failed!", e);
        }
        
        if (null == stepRSLst)
        {
            stepRSLst = new ArrayList<StepRunningStateEntity>();
        }
        
        LOGGER.debug("get getJobInputFiles success.");
        return stepRSLst;
    }
    
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
    @Override
    public int getMaxStepId(Long taskId, String cycleId, int batchId)
        throws CException
    {
        LOGGER.debug("Enter getMaxStepId. param is [taskId=[{}], cycleId=[{}],batchId=[{}]]", new Object[] {taskId,
            cycleId, batchId});
        try
        {
            StepRunningStateEntity stepRS = new StepRunningStateEntity();
            stepRS.setTaskId(taskId);
            stepRS.setCycleId(cycleId);
            stepRS.setBatchId(batchId);
            
            Object maxStepId =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao." + "steprunningstate.getMaxStepId", stepRS);
            if (null != maxStepId)
            {
                return (Integer)maxStepId;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getMaxStepId excute failed. param is [taskId=[{}], cycleId=[{}],batchId=[{}]]!",
                new Object[] {taskId, cycleId, batchId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return 0;
    }
    
    /**
     * 新增步骤运行状态信息
     * 
     * @param stepRS
     *            步骤运行状态
     * @return 是否添加成功
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public boolean addStepRunningState(StepRunningStateEntity stepRS)
        throws CException
    {
        LOGGER.debug("Enter addStepRunningState. stepRS is [{}]", stepRS);
        try
        {
            int rows =
                getSqlSession().insert("com.huawei.platform.tcc.dao.steprunningstate.addStepRunningState", stepRS);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add addStepRunningState error, stepRS is [{}]!", stepRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 更新步骤运行状态信息
     * 
     * @param stepRS
     *            步骤运行状态
     * @return 受影响的记录数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int updateStepRunningState(StepRunningStateEntity stepRS)
        throws CException
    {
        LOGGER.debug("Enter updateStepRunningState..., stepRS is [{}]", stepRS);
        try
        {
            return getSqlSession().update("com.huawei.platform.tcc.dao." + "steprunningstate.updateStepRunningState",
                stepRS);
        }
        catch (Exception e)
        {
            LOGGER.error("updateStepRunningState excute failed, stepRS is [{}]!", stepRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取步骤运行状态信息
     * 
     * @param stepRS
     *            步骤运行状态（包含主键）
     * @return 步骤运行状态信息
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public StepRunningStateEntity getStepRunningState(StepRunningStateEntity stepRS)
        throws CException
    {
        LOGGER.debug("Enter getStepRunningState..., stepRS is [{}]", stepRS);
        try
        {
            Object stepRSO =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.steprunningstate" + ".getStepRunningState",
                    stepRS);
            if (null != stepRSO)
            {
                return (StepRunningStateEntity)stepRSO;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getStepRunningState excute failed, stepRS is [{}]!", stepRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return null;
    }
    
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
    @Override
    public int deleteStepRunningStates(Long taskId, String cycleId, Integer batchId)
        throws CException
    {
        LOGGER.debug("Enter deleteStepRunningStates. param is [taskId=[{}], cycleId=[{}],batchId=[{}]]", new Object[] {
            taskId, cycleId, batchId});
        try
        {
            StepRunningStateEntity stepRS = new StepRunningStateEntity();
            stepRS.setTaskId(taskId);
            stepRS.setCycleId(cycleId);
            stepRS.setBatchId(batchId);
            
            return getSqlSession().delete("com.huawei.platform.tcc.dao."
                + "steprunningstate.deleteStepRunningStatesInBatchTask",
                stepRS);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteStepRunningStates excute failed, param is [taskId=[{}], cycleId=[{}],batchId=[{}]]!",
                new Object[] {taskId, cycleId, batchId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @SuppressWarnings("unchecked")
    @Override
    public List<StepRunningStateEntity> getNoSucessedStepRunningStateLst(Long taskId, String cycleId, Integer batchId)
        throws CException
    {
        List<StepRunningStateEntity> stepRSLst = null;
        LOGGER.debug("Enter getNoSucessedStepRunningStateLst. param is [taskId=[{}], cycleId=[{}],batchId=[{}]]",
            new Object[] {taskId, cycleId, batchId});
        try
        {
            StepRunningStateEntity stepRS = new StepRunningStateEntity();
            stepRS.setTaskId(taskId);
            stepRS.setCycleId(cycleId);
            stepRS.setBatchId(batchId);
            
            stepRSLst =
                (List<StepRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "steprunningstate.getNoSucessedStepRunningStateLst",
                    stepRS);
        }
        catch (Exception e)
        {
            LOGGER.error("getNoSucessedStepRunningStateLst excute failed, param is [taskId=[{}],"
                + " cycleId=[{}],batchId=[{}]]!", new Object[] {taskId, cycleId, batchId}, e);
        }
        
        if (null == stepRSLst)
        {
            stepRSLst = new ArrayList<StepRunningStateEntity>();
        }
        
        LOGGER.debug("get getNoSucessedStepRunningStateLst success.");
        return stepRSLst;
    }
    
    /************************* Tcc 任务步骤运行状态表[begin] ***********************************************/
    
    /************************* portal [begin] ***********************************************/
    /**
     * <获得任务ID编号>
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
    @Override
    public Integer generateTaskIDSerialNo(Integer serviceID, Integer taskType)
        throws CException
    {
        TaskIDSerialEntity entity = new TaskIDSerialEntity();
        entity.setServiceId(serviceID);
        entity.setTaskType(taskType);
        try
        {
            getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskidserial.genTaskSerialID", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("generateTaskID failed", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return entity.getSerialno();
    }
    
    /**
     * 获得步骤ID序号
     * 
     * @param taskId
     *            任务Id
     * @return 步骤ID的编号
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public int generateTaskStepID(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter generateTaskStepID. param is [taskId={}]", taskId);
        TaskStepIdSerialEntity entity = new TaskStepIdSerialEntity();
        entity.setTaskId(taskId);
        try
        {
            getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskstep.genTaskStepSerialID", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("generate taskStepID[taskId={}] failed!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit generateTaskStepID. param is [taskId={}]", taskId);
        return entity.getStepSerialno();
    }
    
    /**
     * 新增任务步骤信息
     * 
     * @param entity
     *            新建的任务步骤信息
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void addTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter addTaskStep. param is [entity={}]", entity);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskstep.addTaskStep", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("add taskStep[{}] failed!", entity, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit addTaskStep. param is [entity={}]", entity);
    }
    
    /**
     * 更新任务步骤信息
     * 
     * @param entity
     *            任务步骤信息
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter updateTaskStep. param is [entity={}]", entity);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskstep.updateTaskStep", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("update taskStep[{}] failed!", entity, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit updateTaskStep. param is [entity={}]", entity);
    }
    
    /**
     * 删除任务步骤信息
     * 
     * @param entity
     *            任务步骤信息
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void deleteTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter deleteTaskStep. param is [entity={}]", entity);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskstep.deleteTaskStep", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("delete taskStep[{}] failed!", entity, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit deleteTaskStep. param is [entity={}]", entity);
    }
    
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
    @Override
    public TaskStepEntity getTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter getTaskStep. param is [entity={}]", entity);
        try
        {
            Object obj = getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskstep.getTaskStep", entity);
            
            if (null == entity)
            {
                return null;
            }
            else
            {
                return (TaskStepEntity)obj;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get taskStep[{}] failed!", entity, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @SuppressWarnings("unchecked")
    public List<TaskStepEntity> getTaskStepList(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter getTaskStep. param is [taskId={}]", taskId);
        try
        {
            if (null == taskId)
            {
                return new ArrayList<TaskStepEntity>(0);
            }
            
            Object obj = getSqlSession().selectList("com.huawei.platform.tcc.dao.taskstep.getTaskSteps", taskId);
            
            if (null == obj)
            {
                return new ArrayList<TaskStepEntity>(0);
            }
            else
            {
                return (List<TaskStepEntity>)obj;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get taskStepList[taskId={}] failed!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务步骤信息列表
     * 
     * @param taskId  任务Id
     * @return 任务步骤信息列表
     * @throws Exception  统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskStepEntity> getEnableTaskStepList(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter getEnableTaskStepList. param is [taskId={}]", taskId);
        try
        {
            if (null == taskId)
            {
                return new ArrayList<TaskStepEntity>(0);
            }
            
            Object obj = getSqlSession().selectList("com.huawei.platform.tcc.dao.taskstep.getEnableTaskSteps", taskId);
            
            if (null == obj)
            {
                return new ArrayList<TaskStepEntity>(0);
            }
            else
            {
                return (List<TaskStepEntity>)obj;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get getEnableTaskStepList[taskId={}] failed!", taskId, e);
            throw e;
        }
    }
    
    /**
     * 更新任务步骤Id
     * 
     * @param entity
     *            要更新的任务步骤交换信息,stepIdOne为源Id，stepIdTwo为目标Id
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void updateTaskStepId(TaskStepExchangeEntity entity)
        throws CException
    {
        LOGGER.debug("Enter updateTaskStepId. param is [entity={}]", entity);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskstep.updateTaskStepId", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("update taskStepId[{}] failed!", entity, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit updateTaskStepId. param is [entity={}]", entity);
    }
    
    /**
     * 获取所有的任务Id列表
     * @param vGroups 可见的用户组
     * @return 获取所有的任务Id列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getAllTaskIdList(List<String> vGroups)
        throws CException
    {
        LOGGER.debug("Enter getAllTaskIdList.");
        try
        {
            Map<String, List<String>> nameMap = new HashMap<String, List<String>>();
            nameMap.put("visibleGroups", vGroups);
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.task.getAllTaskIdList", nameMap);
            if (null != objRtn)
            {
                return (List<Long>)objRtn;
            }
            else
            {
                return new ArrayList<Long>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get getAllTaskIdList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有的任务Id、名称列表
     * @param vGroups 可见的OS用户组
     * @return 获取所有的任务Id、名称列表
     * @throws CException 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getAllTaskIdNameList(List<String> vGroups)
        throws CException
    {
        LOGGER.debug("Enter getAllTaskIdNameList. vGroups is {}", vGroups);
        try
        {
            Map<String, List<String>> nameMap = new HashMap<String, List<String>>();
            nameMap.put("visibleGroups", vGroups);
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.task.getAllTaskIdNameList", nameMap);
            if (null != objRtn)
            {
                return (List<TaskEntity>)objRtn;
            }
            else
            {
                return new ArrayList<TaskEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get getAllTaskIdNameList failed!  vSTgs is {}", vGroups, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @Override
    public int getVisualSuccessedTaskCycleNum(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getVisualSuccessedTaskCycleNum, param is [taskId={}]", taskId);
        try
        {
            if (null == taskId)
            {
                return 0;
            }
            
            TaskRunningStateEntity taskRs = new TaskRunningStateEntity();
            taskRs.setTaskId(taskId);
            taskRs.setCycleId(cycleId);
            taskRs.setState(RunningState.VSUCCESS);
            
            return (Integer)getSqlSession().selectOne("com.huawei.platform.tcc.dao"
                + ".taskrunningstate.getTaskCycleNumByIdState",
                taskRs);
        }
        catch (Exception e)
        {
            LOGGER.error("get visual Successed taskCycleNum failed!,taskId=[{}]", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取虚拟执行成功的最小周期Id
     * 
     * @param taskId
     *            任务Id
     * @return 虚拟执行成功的最小周期Id
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public String getMinVisualSuccessedCycleId(Long taskId)
        throws CException
    {
        LOGGER.debug("enter getMinVisualSuccessedCycleId, param is [taskId={}]", taskId);
        try
        {
            if (null == taskId)
            {
                return null;
            }
            
            TaskRunningStateEntity taskRs = new TaskRunningStateEntity();
            taskRs.setTaskId(taskId);
            taskRs.setState(RunningState.VSUCCESS);
            
            Object obj =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".taskrunningstate.getMinCycleIDByIDState",
                    taskRs);
            if (null == obj)
            {
                return null;
            }
            else
            {
                return obj.toString();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getMinVisualSuccessedCycleId failed!,taskId=[{}]", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将最小周期Id到最大周期Id之间的所有任务周期运行状态更新为state状态
     * 
     * @param taskCycleParam
     *            任务周期参数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void updateTaskRunningStateIntegration(TaskCycleParam taskCycleParam)
        throws CException
    {
        LOGGER.debug("enter updateTaskRunningStateIntegration, param is [taskCycleParam={}]", taskCycleParam);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao"
                + ".taskrunningstate.updateTaskRunningStateIntegration",
                taskCycleParam);
        }
        catch (Exception e)
        {
            LOGGER.error("updateTaskRunningStateIntegration failed!,taskCycleParam=[{}]!", taskCycleParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @SuppressWarnings("unchecked")
    @Override
    public List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getBatchRSList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null != taskId && null != cycleId)
            {
                BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
                batchRS.setTaskId(taskId);
                batchRS.setCycleId(cycleId);
                
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".batchrunningstate.getBatchRSList",
                        batchRS);
                
                if (null != objRtn)
                {
                    return (List<BatchRunningStateEntity>)objRtn;
                }
            }
            
            return new ArrayList<BatchRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getBatchRSList failed!,param=[taskId={},cycleId={}]!", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有任务周期的批次状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param startIndex 开始索引
     * @param rows 行数
     * @return 所有所有任务周期的批次状态总数
     * @throws CException 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    public List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId, Long startIndex, Integer rows)
        throws CException
    {
        LOGGER.debug("enter getBatchRSList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null != taskId && null != cycleId)
            {
                TaskRSQueryParam taskRSQuery = new TaskRSQueryParam();
                taskRSQuery.setTaskId(taskId);
                taskRSQuery.setStartCycleId(cycleId);
                taskRSQuery.setEndCycleID(cycleId);
                taskRSQuery.setStartIndex(startIndex);
                taskRSQuery.setRows(rows);
                
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".batchrunningstate.getBatchRSListPage",
                        taskRSQuery);
                
                if (null != objRtn)
                {
                    return (List<BatchRunningStateEntity>)objRtn;
                }
            }
            
            return new ArrayList<BatchRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getBatchRSList failed!,param=[taskId={},cycleId={}]!", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有任务周期的批次状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有所有任务周期的批次状态总数
     * @throws CException 统一封装的异常
     */
    @Override
    public Integer getBatchRSCount(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getBatchRSCount, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null != taskId && null != cycleId)
            {
                BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
                batchRS.setTaskId(taskId);
                batchRS.setCycleId(cycleId);
                
                Object objRtn =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".batchrunningstate.getBatchRSCount",
                        batchRS);
                
                if (null != objRtn)
                {
                    return (Integer)objRtn;
                }
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.error("getBatchRSCount failed!,param=[taskId={},cycleId={}]!", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取最早开始的批次
     * 
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 获取最早开始的批次
     * @throws CException 统一封装的异常
     */
    @Override
    public BatchRunningStateEntity getOldestBatchRS(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getOldestBatchRS, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null != taskId && null != cycleId)
            {
                BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
                batchRS.setTaskId(taskId);
                batchRS.setCycleId(cycleId);
                
                Object objRtn =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".batchrunningstate.getOldestBatchRS",
                        batchRS);
                
                if (null != objRtn)
                {
                    return (BatchRunningStateEntity)objRtn;
                }
            }
            
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("getOldestBatchRS failed!,param=[taskId={},cycleId={}]!", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    @SuppressWarnings("unchecked")
    @Override
    public List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getStepRSList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null != taskId && null != cycleId)
            {
                StepRunningStateEntity stepRS = new StepRunningStateEntity();
                stepRS.setTaskId(taskId);
                stepRS.setCycleId(cycleId);
                
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".steprunningstate.getStepRSList",
                        stepRS);
                
                if (null != objRtn)
                {
                    return (List<StepRunningStateEntity>)objRtn;
                }
            }
            
            return new ArrayList<StepRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getStepRSList failed!,param=[taskId={},cycleId={}]", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有任务周期的步骤状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param startIndex 开始索引
     * @param rows 行数
     * @return 所有所有任务周期的步骤状态总数
     * @throws CException 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    public List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId, Long startIndex, Integer rows)
        throws CException
    {
        LOGGER.debug("enter getStepRSList, param is [taskId={},cycleId={},startIndex={},rows={}]", new Object[] {
            taskId, cycleId, startIndex, rows});
        try
        {
            if (null != taskId && null != cycleId)
            {
                TaskRSQueryParam taskRSQuery = new TaskRSQueryParam();
                taskRSQuery.setTaskId(taskId);
                taskRSQuery.setStartCycleId(cycleId);
                taskRSQuery.setEndCycleID(cycleId);
                taskRSQuery.setStartIndex(startIndex);
                taskRSQuery.setRows(rows);
                
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".steprunningstate.getStepRSListPage",
                        taskRSQuery);
                
                if (null != objRtn)
                {
                    return (List<StepRunningStateEntity>)objRtn;
                }
            }
            
            return new ArrayList<StepRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getStepRSList failed!,param=[taskId={},cycleId={},startIndex={},rows={}]!", new Object[] {
                taskId, cycleId, startIndex, rows, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务周期的所有步骤状态总数
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 任务周期的所有步骤状态总数
     * @throws CException 统一封装的异常
     */
    @Override
    public Integer getStepRSCount(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getStepRSCount, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null != taskId && null != cycleId)
            {
                StepRunningStateEntity stepRS = new StepRunningStateEntity();
                stepRS.setTaskId(taskId);
                stepRS.setCycleId(cycleId);
                
                Object objRtn =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".steprunningstate.getStepRSCount",
                        stepRS);
                
                if (null != objRtn)
                {
                    return (Integer)objRtn;
                }
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.error("getStepRSCount failed!,param=[taskId={},cycleId={}]!", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询任务运行状态列表(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam
     *            查询条件
     * @return 任务运行状态列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getTaskRSList(TaskRSQueryParam taskRSQueryParam)
        throws CException
    {
        LOGGER.debug("enter getTaskRSList, param is [taskRSQueryParam={}]", taskRSQueryParam);
        try
        {
            if (null != taskRSQueryParam)
            {
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao"
                        + ".taskrunningstate.getTaskRSListByQueryParam",
                        taskRSQueryParam);
                
                if (null != objRtn)
                {
                    return (List<TaskRunningStateEntity>)objRtn;
                }
            }
            
            return new ArrayList<TaskRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRSList failed!, taskRSQueryParam={}", taskRSQueryParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询所有未完成的任务运行状态列表[左闭右开周期区间]
     * @param taskId 周期Id
     * @param minCycleId 最小周期Id
     * @param maxCycleId 最大周期Id
     * @return 任务运行状态列表
     * @throws CException 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getNSTaskRSs(Long taskId, String minCycleId, String maxCycleId)
        throws CException
    {
        LOGGER.debug("enter getNonSuccessTaskRSsByTaskId, param is [taskId={},minCycleId={},maxCycleId={}]",
            new Object[] {taskId, minCycleId, maxCycleId});
        try
        {
            TaskCycleParam taskCycleParam = new TaskCycleParam();
            taskCycleParam.setTaskId(taskId);
            taskCycleParam.setMinCycleId(minCycleId);
            taskCycleParam.setMaxCycleId(maxCycleId);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            //当天凌晨时间
            taskCycleParam.setStartDate(cal.getTime());
            
            cal.add(Calendar.DAY_OF_MONTH, 1);
            //加一天
            taskCycleParam.setEndDate(cal.getTime());
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao"
                    + ".taskrunningstate.getNonSuccessTaskRSsByTaskId",
                    taskCycleParam);
            
            if (null != objRtn)
            {
                return (List<TaskRunningStateEntity>)objRtn;
            }
            
            return new ArrayList<TaskRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getNonSuccessTaskRSsByTaskId failed!, taskId={},minCycleId={},maxCycleId={}.", new Object[] {
                taskId, minCycleId, maxCycleId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询任务的最小最大周期Id
     * 
     * @param taskId 任务Id
     * @return 任务的最小最大周期Id
     * @throws CException 统一封装的异常
     */
    public TaskMinMaxCycle getTaskMinMaxCycleId(Long taskId)
        throws CException
    {
        LOGGER.debug("enter getTaskMinMaxCycleId. taskId is {}.", taskId);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskrunningstate.getTaskMinMaxCycleId");
            
            if (null != objRtn)
            {
                return (TaskMinMaxCycle)objRtn;
            }
            
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskMinMaxCycleId failed! taskId is {}", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询所有任务的最小最大周期Id
     * 
     * @return 所有任务的最小最大周期Id
     * @throws CException 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskMinMaxCycle> getTaskMinMaxCycleIds()
        throws CException
    {
        LOGGER.debug("enter getTaskMinMaxCycleIds.");
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.taskrunningstate.getTaskMinMaxCycleIds");
            
            if (null != objRtn)
            {
                return (List<TaskMinMaxCycle>)objRtn;
            }
            
            return new ArrayList<TaskMinMaxCycle>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskMinMaxCycleIds failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取查询任务运行状态的总数(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam
     *            查询条件
     * @return 查询任务运行状态的总数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public Integer getTaskRSCount(TaskRSQueryParam taskRSQueryParam)
        throws CException
    {
        LOGGER.debug("enter getTaskRSCount, param is [taskRSQueryParam={}]", taskRSQueryParam);
        try
        {
            if (null != taskRSQueryParam)
            {
                Object objRtn =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao"
                        + ".taskrunningstate.getTaskRSCountByQueryParam",
                        taskRSQueryParam);
                
                if (null != objRtn)
                {
                    return (Integer)objRtn;
                }
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRSCount failed!, taskRSQueryParam={}", taskRSQueryParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务运行状态的总数
     * 
     * @param taskId
     *            任务Id
     * @return 任务运行状态的总数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public Integer getTaskRSCount(Long taskId)
        throws CException
    {
        LOGGER.debug("enter getTaskRSCount, param is [taskId={}]", taskId);
        try
        {
            if (null != taskId)
            {
                Object objRtn =
                    getSqlSession().selectOne("com.huawei.platform.tcc.dao"
                        + ".taskrunningstate.getTaskRSCountByTaskId",
                        taskId);
                
                if (null != objRtn)
                {
                    return (Integer)objRtn;
                }
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRSCount failed!, taskId={}", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
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
    public int getTaskStepEnableSize(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter getTaskStepEnableSize...");
        try
        {
            return (Integer)getSqlSession().selectOne("com.huawei.platform.tcc.dao."
                + "taskstep.getTaskStepsEnableSize",
                taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("get getTaskStepEnableSize failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有步骤列表
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void deleteTaskSteps(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteTaskSteps, param is [taskId={}]", taskId);
        try
        {
            if (null != taskId)
            {
                getSqlSession().update("com.huawei.platform.tcc.dao" + ".taskstep.deleteTaskSteps", taskId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTaskSteps failed!, taskId={}!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有运行状态
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void deleteTaskRunningStates(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteTaskRunningStates, param is [taskId={}]", taskId);
        try
        {
            if (null != taskId)
            {
                getSqlSession().update("com.huawei.platform.tcc.dao.taskrunningstate.deleteTaskRunningStates", taskId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTaskRunningStates failed!, taskId={}!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有批次运行状态
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void deleteBatchRunningStates(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteBatchRunningStates, param is [taskId={}]", taskId);
        try
        {
            if (null != taskId)
            {
                getSqlSession().update("com.huawei.platform.tcc.dao.batchrunningstate"
                    + ".deleteBatchRunningStatesByTaskId",
                    taskId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteBatchRunningStates failed!, taskId={}!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有步骤运行状态
     * 
     * @param taskId
     *            任务ID
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void deleteStepRunningStates(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteStepRunningStates, param is [taskId={}]", taskId);
        try
        {
            if (null != taskId)
            {
                getSqlSession().update("com.huawei.platform.tcc.dao"
                    + ".steprunningstate.deleteStepRunningStatesByTaskId",
                    taskId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteStepRunningStates failed!, taskId={}!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除所有依赖任务Id列表中包含任务Id的依赖关系
     * 
     * @param taskId
     *            任务Id
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void deleteDependTaskId(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteDependTaskId, param is [taskId={}]", taskId);
        try
        {
            if (null != taskId)
            {
                getSqlSession().update("com.huawei.platform.tcc.dao" + ".task.deleteDependTaskId", taskId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteDependTaskId failed!, taskId={}!", taskId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务运行状态列表
     * 
     * @param taskRSList
     *            需要查询的任务运行状态列表
     * @return 任务运行状态列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getTaskRunningStateList(List<TaskRunningStateEntity> taskRSList)
        throws CException
    {
        LOGGER.debug("enter getTaskRunningStateList, param is [taskRSList={}]", taskRSList);
        try
        {
            if (null != taskRSList && !taskRSList.isEmpty())
            {
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao"
                        + ".taskrunningstate.getTaskRunningStateList",
                        taskRSList);
                
                if (null != objRtn)
                {
                    return (List<TaskRunningStateEntity>)objRtn;
                }
            }
            
            return new ArrayList<TaskRunningStateEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRunningStateList failed!, taskRSList={}!", taskRSList, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将depend_Task_Id_List中包含task.taskName的那部分依赖信息替换成task.taskId
     * 
     * @param task
     *            任务Id和任务名
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public void replaceDependTaskName2Id(TaskEntity task)
        throws CException
    {
        LOGGER.debug("enter replaceDependTaskName2Id, param is [task={}]", task);
        try
        {
            if (null != task && !StringUtils.isEmpty(task.getTaskName()) && null != task.getTaskId())
            {
                getSqlSession().update("com.huawei.platform.tcc.dao" + ".task.replaceDependTaskName2Id", task);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("replaceDependTaskName2Id failed!, task={}!", task, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取依赖于该任务的所有任务
     * 
     * @param taskIds
     *            任务Id集合
     * @return 依赖于该任务的所有任务
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getDependedTasks(List<Long> taskIds)
        throws CException
    {
        LOGGER.debug("enter getDependedTasks, param is [taskIds={}]", taskIds);
        try
        {
            if (null != taskIds && !taskIds.isEmpty())
            {
                StringBuilder taskIdRegs = new StringBuilder();
                for (Long taskId : taskIds)
                {
                    taskIdRegs.append(";");
                    taskIdRegs.append(taskId);
                    taskIdRegs.append(",|");
                }
                
                if (taskIdRegs.length() > 0)
                {
                    taskIdRegs.deleteCharAt(taskIdRegs.length() - 1);
                }
                
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".task.getDependedTasks",
                        taskIdRegs.toString());
                
                if (null != objRtn)
                {
                    return (List<TaskEntity>)objRtn;
                }
            }
            
            return new ArrayList<TaskEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("get dependedTasks failed!, taskRSList={}!", taskIds, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务周期的相关日志信息
     * 
     * @param taskRS
     *            任务运行周期
     * @return 任务周期的相关日志信息
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Log2DBEntity> getTccLogList(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("enter getTccLogList, param is [taskRS={}]", taskRS);
        try
        {
            if (null != taskRS && null != taskRS.getTaskId() && !StringUtils.isEmpty(taskRS.getCycleId()))
            {
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".log2DB.getTccLogList", taskRS);
                
                if (null != objRtn)
                {
                    return (List<Log2DBEntity>)objRtn;
                }
            }
            
            return new ArrayList<Log2DBEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("get tccLogList failed!, taskRS={}!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取remoteshell执行的控制台输出日志
     * 
     * @param taskRS
     *            任务运行周期
     * @return remoteshell执行的控制台输出日志
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Log2DBEntity> getRsLogList(TaskRunningStateEntity taskRS)
        throws CException
    {
        LOGGER.debug("enter getRsLogList, param is [taskRS={}]", taskRS);
        try
        {
            if (null != taskRS && null != taskRS.getTaskId() && !StringUtils.isEmpty(taskRS.getCycleId()))
            {
                Object objRtn =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".log2DB.getRSLogList", taskRS);
                
                if (null != objRtn)
                {
                    return (List<Log2DBEntity>)objRtn;
                }
            }
            
            return new ArrayList<Log2DBEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("get rsLogList failed!, taskRS={}!", taskRS, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 根据查询条件查询tcc日志记录列表
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return 查询tcc日志记录列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Log2DBEntity> getTccLogList(Log2DBQueryParam logQueryParam)
        throws CException
    {
        LOGGER.debug("enter getTccLogList, param is [logQueryParam={}]", logQueryParam);
        try
        {
            if (null == logQueryParam)
            {
                return new ArrayList<Log2DBEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".log2DB.getTccLogList", logQueryParam);
            
            if (null != objRtn)
            {
                return (List<Log2DBEntity>)objRtn;
            }
            
            return new ArrayList<Log2DBEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * tcc日志记录数
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return tcc日志记录数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public Integer getTccLogCount(Log2DBQueryParam logQueryParam)
        throws CException
    {
        LOGGER.debug("enter getTccLogCount, param is [logQueryParam={}]", logQueryParam);
        try
        {
            if (null == logQueryParam)
            {
                return 0;
            }
            
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".log2DB.getTccLogCount", logQueryParam);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogCount failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 根据查询条件查询Rs日志记录列表
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return 查询Rs日志记录列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Log2DBEntity> getRsLogList(Log2DBQueryParam logQueryParam)
        throws CException
    {
        LOGGER.debug("enter getRsLogList, param is [logQueryParam={}]", logQueryParam);
        try
        {
            if (null == logQueryParam)
            {
                return new ArrayList<Log2DBEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".log2DB.getRsLogList", logQueryParam);
            
            if (null != objRtn)
            {
                return (List<Log2DBEntity>)objRtn;
            }
            
            return new ArrayList<Log2DBEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.debug("getRsLogList failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * rs日志记录数
     * 
     * @param logQueryParam
     *            日志查询条件
     * @return rs日志记录数
     * @throws CException
     *             统一封装的异常
     */
    @Override
    public Integer getRsLogCount(Log2DBQueryParam logQueryParam)
        throws CException
    {
        LOGGER.debug("enter getRsLogCount, param is [logQueryParam={}]", logQueryParam);
        try
        {
            if (null == logQueryParam)
            {
                return 0;
            }
            
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".log2DB.getRsLogCount", logQueryParam);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.debug("getRsLogCount failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取已经记录日志的所有用户名列表
     * 
     * @return 已经记录日志的所有用户名列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAllUserName()
        throws CException
    {
        LOGGER.debug("enter getAllUserName.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".log2DB.getAllUserName");
            
            if (null != objRtn)
            {
                return (List<String>)objRtn;
            }
            
            return new ArrayList<String>(0);
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed.", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * 
     * @param param
     *            查询条件
     * @return 周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * @throws Exception
     *             异常
     */
    @Override
    public Integer getLongTimeShellsCount(LongTimeShellParam param)
        throws Exception
    {
        LOGGER.debug("enter getLongTimeShellsCount, param is [logQueryParam={}]", param);
        try
        {
            if (null == param)
            {
                return 0;
            }
            
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".steprunningstate.getLongTimeShellsCount",
                    param);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            
            return 0;
        }
        catch (Exception e)
        {
            LOGGER.debug("getLongTimeShellsCount failed, param is {}", param, e);
            throw e;
        }
    }
    
    /**
     * 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * 
     * @param param
     *            查询条件
     * @return 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * @throws Exception
     *             异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LongTimeShellEntity> getLongTimeShellList(LongTimeShellParam param)
        throws Exception
    {
        LOGGER.debug("enter getLongTimeShellList, param is [param={}]", param);
        try
        {
            if (null == param)
            {
                return new ArrayList<LongTimeShellEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao" + ".steprunningstate.getLongTimeShellList",
                    param);
            
            if (null != objRtn)
            {
                return (List<LongTimeShellEntity>)objRtn;
            }
            
            return new ArrayList<LongTimeShellEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.debug("get LongTimeShellList failed, param is {}", param, e);
            throw e;
        }
    }
    
    /**
     * 重新初始化任务的重做开始以及重做结束范围内的任务周期
     * 
     * @param queryParam
     *            开始周期Id，结束周期Id，任务Id
     * @throws Exception
     *             异常
     */
    @Override
    public void reInitTaskRS(TaskRSQueryParam queryParam)
        throws Exception
    {
        LOGGER.debug("enter reInitTaskRS, param={}", queryParam);
        try
        {
            if (null == queryParam)
            {
                return;
            }
            
            getSqlSession().update("com.huawei.platform.tcc.dao" + ".taskrunningstate.reInitTaskRS", queryParam);
        }
        catch (Exception e)
        {
            LOGGER.debug("reInitTaskRS failed, taskE is {}", queryParam, e);
            throw e;
        }
    }
    
    /**
     * 删除taskIdCycleIds对应的日志记录（保留最新的reserveCount个成功的任务周期的日志）
     * 
     * @param taskIdCycleIds
     *            要删除的任务Id周期Id键值对集合
     * @throws CException
     *             统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteRemoteshellLog(List<TaskIdCycleIdPair> taskIdCycleIds)
        throws CException
    {
        LOGGER.debug("enter deleteRemoteshellLog, taskIdCycleIds is {}", taskIdCycleIds);
        try
        {
            if (null != taskIdCycleIds && !taskIdCycleIds.isEmpty())
            {
                getSqlSession().delete("com.huawei.platform.tcc.dao.log2DB.deleteRemoteshellLog", taskIdCycleIds);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteRemoteshellLog failed, taskIdCycleIds is [{}]", taskIdCycleIds, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取需要删除日志的任务Id周期Id键值对集合（保留最新的reserveCount个成功的任务周期的日志）
     * 
     * @param reserveCycleLog
     *            要保留的任务周期日志参数
     * @throws Exception
     *             统一封装的异常
     * @return 需要删除日志的任务Id周期Id键值对集合
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskIdCycleIdPair> getNeedDeleteLogTaskRSs(ReserveCycleLogParam reserveCycleLog)
        throws Exception
    {
        LOGGER.debug("enter getNeedDeleteLogTaskRSs, param is [param={}]", reserveCycleLog);
        try
        {
            if (null == reserveCycleLog)
            {
                return new ArrayList<TaskIdCycleIdPair>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.log2DB.getNeedDeleteLogTaskRSs",
                    reserveCycleLog);
            
            if (null != objRtn)
            {
                return (List<TaskIdCycleIdPair>)objRtn;
            }
            
            return new ArrayList<TaskIdCycleIdPair>(0);
        }
        catch (Exception e)
        {
            LOGGER.debug("get getNeedDeleteLogTaskRSs failed, param is {}", reserveCycleLog, e);
            throw e;
        }
    }
    
    /**
     * 获取业务Id业务名集合
     * 
     * @return 业务Id业务名集合
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws CException
    {
        LOGGER.debug("Enter getAllServiceIdNameList.");
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.serviceDef" + ".getAllServiceIdNameList");
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllServiceIdNameList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过业务名集合查询指定业务
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception
    {
        LOGGER.debug("Enter getServicesByNames. serviceSearch is {}", serviceSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.serviceDef.getServicesByName", serviceSearch);
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getServicesByNames failed! serviceSearch is {}", serviceSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过业务名集合查询指定业务总数
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    @Override
    public Integer getServicesCountByName(ServiceSearch serviceSearch)
        throws Exception
    {
        LOGGER.debug("Enter getServicesCountByName. serviceSearch is {}", serviceSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.serviceDef.getServicesCountByName",
                    serviceSearch);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getServicesCountByName failed! serviceSearch is {}", serviceSearch, e);
            throw e;
        }
    }
    
    /**
     * 新增业务信息
     * @param serviceDef 业务实体
     * @throws Exception 异常
     */
    @Override
    public void addService(ServiceDefinationEntity serviceDef)
        throws Exception
    {
        LOGGER.debug("Enter addService. serviceDef is {}", serviceDef);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.serviceDef.addService", serviceDef);
        }
        catch (Exception e)
        {
            LOGGER.error("addService failed! serviceDef is {}", serviceDef, e);
            throw e;
        }
    }
    
    /**
     * 更新业务信息
     * @param serviceDef 业务实体
     * @throws Exception 统一封装的异常
     */
    @Override
    public void updateService(ServiceDefinationEntity serviceDef)
        throws Exception
    {
        LOGGER.debug("Enter updateService. serviceDef is {}", serviceDef);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.serviceDef.updateService", serviceDef);
        }
        catch (Exception e)
        {
            LOGGER.error("updateService failed! serviceDef is {}", serviceDef, e);
            throw e;
        }
    }
    
    /**
     * 删除业务信息
     * @param serviceId 业务Id
     * @throws Exception 异常
     */
    @Override
    public void deleteService(Integer serviceId)
        throws Exception
    {
        LOGGER.debug("Enter deleteService. serviceId is {}", serviceId);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.serviceDef.deleteService", serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteService failed! serviceId is {}", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 获取业务实体
     * @param serviceId 业务Id
     * @return 业务实体
     * @throws Exception 异常
     */
    @Override
    public ServiceDefinationEntity getService(Integer serviceId)
        throws Exception
    {
        LOGGER.debug("enter getService, serviceId is {}", serviceId);
        try
        {
            return (ServiceDefinationEntity)getSqlSession().selectOne("com.huawei.platform.tcc.dao."
                + "serviceDef.getService",
                serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("get Service failed, serviceId is [{}].", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 节点信息列表
     * @param search 查询条件
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfoEntity> getNodes(Search search)
        throws Exception
    {
        LOGGER.debug("enter getNodes, search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.nodeInfo.getNodes", search);
            
            if (null != objRtn)
            {
                return (List<NodeInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<NodeInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodes failed!", e);
            throw e;
        }
    }
    
    /**
     * 节点总数
     * @param search 查询条件
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @Override
    public Integer getNodesCount(Search search)
        throws Exception
    {
        LOGGER.debug("enter getNodesCount, search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.nodeInfo.getNodesCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodesCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 节点信息列表
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfoEntity> getNodeInfoList()
        throws Exception
    {
        LOGGER.debug("enter getNodeInfoList");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.nodeInfo.getNodeInfoList");
            
            if (null != objRtn)
            {
                return (List<NodeInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<NodeInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeInfoList failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取节点信息
     * @param nodeId 节点Id
     * @return 节点信息
     * @throws Exception 异常
     */
    @Override
    public NodeInfoEntity getNodeInfo(Integer nodeId)
        throws Exception
    {
        LOGGER.debug("enter getNodeInfo, nodeId is {}", nodeId);
        try
        {
            if (null == nodeId)
            {
                return null;
            }
            
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.nodeInfo.getNodeInfo", nodeId);
            
            if (null != objRtn)
            {
                return (NodeInfoEntity)objRtn;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeInfo failed, nodeId is [{}].", nodeId, e);
            throw e;
        }
    }
    
    /**
     * 新增节点信息
     * @param nodeInfo 节点信息
     * @return 是否新增成功
    * @throws Exception 异常
     */
    @Override
    public boolean addNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception
    {
        LOGGER.debug("Enter addNodeInfo. nodeInfo is {}", nodeInfo);
        try
        {
            int rows = getSqlSession().insert("com.huawei.platform.tcc.dao.nodeInfo.addNodeInfo", nodeInfo);
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("addNodeInfo failed! nodeInfo is {}", nodeInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改节点信息
     * @param nodeInfo 节点信息
     * @return 是否更新成功
     * @throws Exception 异常
     */
    public boolean updateNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateNodeInfo. nodeInfo is {}", nodeInfo);
        try
        {
            int rows = getSqlSession().update("com.huawei.platform.tcc.dao.nodeInfo.updateNodeInfo", nodeInfo);
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("updateNodeInfo failed! nodeInfo is {}", nodeInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除节点信息
     * @param nodeId 节点Id
     * @return 是否删除成功
     */
    public boolean deleteNodeInfo(Integer nodeId)
    
    {
        LOGGER.debug("Enter deleteNodeInfo. nodeId is {}", nodeId);
        try
        {
            int rows = getSqlSession().delete("com.huawei.platform.tcc.dao.nodeInfo.deleteNodeInfo", nodeId);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteNodeInfo failed! nodeId is {}", nodeId, e);
        }
        return false;
    }
    
    /**
     * 通过账号密码查询用户权限
     * 
     * @param usernameAndPasswordParam 账号密码
     * @return 操作员信息
     * @throws Exception 异常
     */
    @Override
    public OperatorInfoEntity getOperatorInfo(UsernameAndPasswordParam usernameAndPasswordParam)
        throws Exception
    
    {
        LOGGER.debug("Enter getOperatorInfo. usernameAndPasswordParam is {}", usernameAndPasswordParam);
        try
        {
            Object objOperatorInfo =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".operatorInfo.getOperatorInfo",
                    usernameAndPasswordParam);
            if (null != objOperatorInfo)
            {
                return (OperatorInfoEntity)objOperatorInfo;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfo failed! usernameAndPasswordParam is {}", usernameAndPasswordParam, e);
            throw e;
        }
    }
    
    /**
     * 通过用户名查询用户权限
     * @param operatorName 用户名
     * @return 操作员信息
     * @throws Exception 异常
     */
    @Override
    public OperatorInfoEntity getOperatorInfoByName(String operatorName)
        throws Exception
    
    {
        LOGGER.debug("Enter getOperatorInfoByName. operatorName is {}", operatorName);
        try
        {
            Object objOperatorInfo =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao" + ".operatorInfo.getOperatorInfoByName",
                    operatorName);
            if (null != objOperatorInfo)
            {
                return (OperatorInfoEntity)objOperatorInfo;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfoByName failed! operatorName is {}", operatorName, e);
            throw e;
        }
    }
    
    /**
     * 通过用户名集合查询指定用户
     * @param userSearch 用户查询
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperatorInfoEntity> getUsers(UserSearch userSearch)
        throws Exception
    {
        LOGGER.debug("Enter getUsers. userSearch is {}", userSearch);
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.operatorInfo.getUsers", userSearch);
            
            if (null != objRtn)
            {
                return (List<OperatorInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<OperatorInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsers failed! userSearch is {}", userSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过用户名集合查询指定用户总数
     * @param search 用户查询
     * @return 用户集合
     * @throws Exception 异常
     */
    @Override
    public Integer getUsersCount(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCount. search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.operatorInfo.getUsersCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过绑定的OS用户查询指定用户
     * @param osUser 操作系统用户
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperatorInfoEntity> getUsersByOsUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getUsersByOsUser. osUser is {}", osUser);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.operatorInfo.getUsersByOsUser", osUser);
            
            if (null != objRtn)
            {
                return (List<OperatorInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<OperatorInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersByOsUser failed! osUser is {}", osUser, e);
            throw e;
        }
    }
    
    /**
     * 通过绑定的OS用户查询指定用户总数
     * @param osUser 操作系统用户
     * @return 用户总数
     * @throws Exception 统一封装的异常
     */
    @Override
    public Integer getUsersCountByOsUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCountByOsUser. osUser is {}", osUser);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.operatorInfo.getUsersCountByOsUser", osUser);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCountByOsUser failed! osUser is {}", osUser, e);
            throw e;
        }
    }
    
    /**
     * 新增用户
     * @param operatorInfo 用户
     * @throws Exception 异常
     */
    @Override
    public void addUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter addUser. operatorInfo is {}", operatorInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.operatorInfo.addUser", operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改用户
     * @param operatorInfo 用户
     * @throws Exception 异常
     */
    @Override
    public void updateUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateUser. operatorInfo is {}", operatorInfo);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.operatorInfo.updateUser", operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除用户
     * @param operatorInfo 用户
     * @throws Exception 统一封装的异常
     */
    @Override
    public void deleteUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteUser. operatorInfo is {}", operatorInfo);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.operatorInfo.deleteUser", operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 通过角色ID集合查询指定角色
     * @param roleSearch 角色查询
     * @return 角色信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RoleDefinationEntity> getRolesByName(RoleSearch roleSearch)
        throws Exception
    {
        LOGGER.debug("Enter getRolesByName. roleSearch is {}", roleSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.roleDef" + ".getRolesByName", roleSearch);
            
            if (null != objRtn)
            {
                return (List<RoleDefinationEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<RoleDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRolesByName failed! roleSearch is {}", roleSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过角色ID集合查询指定角色总数
     * @param search 角色查询
     * @return 角色总数
     * @throws Exception 异常
     */
    @Override
    public Integer getRolesCountByName(RoleSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getRolesCountByName. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.roleDef" + ".getRolesCountByName", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRolesCountByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过角色id查询角色
     * @param roleId 角色id
     * @return 角色信息
     * @throws Exception 异常
     */
    @Override
    public RoleDefinationEntity getRole(Integer roleId)
        throws Exception
    {
        LOGGER.debug("Enter getRole. roleId is {}", roleId);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.roleDef.getRole", roleId);
            
            if (null != objRtn)
            {
                return (RoleDefinationEntity)objRtn;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRole failed! roleId is {}", roleId, e);
            throw e;
        }
    }
    
    /**
     * 获取角色Id集合
     * 
     * @return 角色Id集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RoleDefinationEntity> getAllRoleIdList()
        throws Exception
    {
        LOGGER.debug("Enter getAllRoleIdList.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.roleDef.getAllRoleIdList");
            
            if (null != objRtn)
            {
                return (List<RoleDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<RoleDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllRoleIdList failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取可见的业务Id集合
     * @param groups 用户组列表
     * @return 可见的任务Id集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getVisualServiceIdNames(List<String> groups)
        throws Exception
    {
        LOGGER.debug("Enter getVisualServiceIdNames. groups is {}", groups);
        try
        {
            //直接返回空集合
            if (null != groups && groups.isEmpty())
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.serviceDef.getVisualServiceIdNames", groups);
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getVisualServiceIdNames failed! groups is {}", groups, e);
            throw e;
        }
    }
    
    /**
     * 获取所有可见的业务Id集合
     * @throws Exception 异常
     * @return 可见的业务Id集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getAllVisualServiceIdNames()
        throws Exception
    {
        LOGGER.debug("Enter getAllVisualServiceIdNames. groups is {}");
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.serviceDef.getAllVisualServiceIdNames");
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllVisualServiceIdNames failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取可见的业务任务组名集合
     * @param roleId 角色Id
     * @param serviceId 业务Id
     * @return 获取可见的业务任务组名集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getVisualTaskGroupNames(Integer roleId, Integer serviceId)
        throws Exception
    {
        LOGGER.debug("Enter getVisualTaskGroupNames. roleId is {}, serviceId is {}", roleId, serviceId);
        try
        {
            if (null == roleId || null == serviceId)
            {
                return new ArrayList<String>(0);
            }
            
            RolePrivilegeInfoEntity rolePri = new RolePrivilegeInfoEntity();
            rolePri.setRoleId(roleId.toString());
            rolePri.setServiceId(serviceId);
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.rolePrivilegeInfo" + ".getVisualTaskGroupNames",
                    rolePri);
            
            if (null != objRtn)
            {
                return (List<String>)objRtn;
            }
            else
            {
                return new ArrayList<String>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getVisualTaskGroupNames failed! roleId is {}, serviceId is {}", new Object[] {roleId,
                serviceId, e});
            throw e;
        }
    }
    
    /**
     * 获取所有任务组和对应的权限类型
     * @param search 角色id
     * @return 角色权限信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RolePrivilegeInfo> getPrivileges(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getPrivileges. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.rolePrivilegeInfo.getPrivileges", search);
            
            if (null != objRtn)
            {
                return (List<RolePrivilegeInfo>)objRtn;
            }
            else
            {
                return new ArrayList<RolePrivilegeInfo>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getPrivileges failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 获取已分配的任务组和对应的权限类型
     * @param search 角色id
     * @return 角色权限信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RolePrivilegeInfo> getHavePrivileges(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getHavePrivileges. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.rolePrivilegeInfo.getHavePrivileges", search);
            
            if (null != objRtn)
            {
                return (List<RolePrivilegeInfo>)objRtn;
            }
            else
            {
                return new ArrayList<RolePrivilegeInfo>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getHavePrivileges failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 新增角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    @Override
    public void addRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter addRole. roleDef is {}", roleDef);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.roleDef.addRole", roleDef);
        }
        catch (Exception e)
        {
            LOGGER.error("addRole failed! roleDef is {}", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 修改角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    @Override
    public void updateRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter updateRole. roleDef is {}", roleDef);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.roleDef.updateRole", roleDef);
        }
        catch (Exception e)
        {
            LOGGER.error("updateRole failed! roleDef is {}", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 删除角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    @Override
    public void deleteRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter deleteRole. roleDef is {}", roleDef);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.roleDef.deleteRole", roleDef);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteRole failed! roleDef is {}", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 新增角色权限
     * @param rolePrivilegeInfo 角色权限
     * @throws Exception 异常
     */
    @Override
    public void addRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter addRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.rolePrivilegeInfo.addRolePrivilege", rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改角色权限
     * @param rolePrivilegeInfo 角色权限
     * @throws Exception 异常
     */
    @Override
    public void updateRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.rolePrivilegeInfo.updateRolePrivilege",
                rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除角色权限
     * @param rolePrivilegeInfo 角色权限
     * @throws Exception 异常
     */
    @Override
    public void deleteRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.rolePrivilegeInfo.deleteRolePrivilege",
                rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 通过角色查询指定角色总数
     * @param rolePrivilegeInfo 角色查询
     * @return 角色总数
     * @throws Exception 统一封装的异常
     */
    @Override
    public Integer getRolePrivilegeCount(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter getRolePrivilegeCount. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.rolePrivilegeInfo" + ".getRolePrivilegeCount",
                    rolePrivilegeInfo);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRolePrivilegeCount failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取未绑定OS用户的用户集合
     * @return 取未未绑定OS用户的用户集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperatorInfoEntity> getUsersNoOsUser()
        throws Exception
    {
        LOGGER.debug("Enter getUsersNoOsUser.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.operatorInfo.getUsersNoOsUser");
            
            if (null != objRtn)
            {
                return (List<OperatorInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<OperatorInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersNoOsUser failed!", e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名集合查询指定OS用户
     * @param search OS用户查询
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserInfoEntity> getOSUsersByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersByName. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.OSUserInfo.getOSUsersByName", search);
            
            if (null != objRtn)
            {
                return (List<OSUserInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<OSUserInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsersByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 获取所有的OS用户列表
     * @return OS用户列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserInfoEntity> getOSUsers()
        throws Exception
    {
        LOGGER.debug("Enter getOSUsers");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.OSUserInfo.getOSUsers");
            
            if (null != objRtn)
            {
                return (List<OSUserInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<OSUserInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsers failed!", e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名集合查询指定OS用户总数
     * @param search OS用户查询
     * @return OS用户总数
     * @throws Exception 异常
     */
    @Override
    public Integer getOSUsersCountByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersCountByName. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.OSUserInfo.getOSUsersCountByName", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsersCountByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 新增OS用户
     * @param mOSUserInfo OS用户
     * @throws Exception 异常
     */
    @Override
    public void addOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter addOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.OSUserInfo.addOSUser", mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改OS用户
     * @param mOSUserInfo OS用户
     * @throws Exception 异常
     */
    @Override
    public void updateOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.OSUserInfo.updateOSUser", mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除OS用户信息
     * @param mOSUserInfo OS用户
     * @throws Exception 异常
     */
    @Override
    public void deleteOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.OSUserInfo.deleteOSUser", mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取OS用户信息
     * @param osUserName OS用户名
     * @return OS用户信息
     */
    @Override
    public OSUserInfoEntity getOSUser(String osUserName)
    {
        LOGGER.debug("Enter getOSUser. osUserName is {}", osUserName);
        try
        {
            if (StringUtils.isBlank(osUserName))
            {
                return null;
            }
            
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.OSUserInfo.getOSUser", osUserName);
            
            if (null != objRtn)
            {
                return (OSUserInfoEntity)objRtn;
                
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUser failed! osUserName is {}", osUserName, e);
        }
        
        return null;
    }
    
    /**
     * 获取用户名集合所属的用户组集合
     * @param osUsers OS用户名集合
     * @return 用户组集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserInfoEntity> getGroupByOSUsers(List<String> osUsers)
    {
        LOGGER.debug("Enter getGroupByOSUsers. osUsers is {}", osUsers);
        try
        {
            if (null == osUsers || osUsers.isEmpty())
            {
                return new ArrayList<OSUserInfoEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.OSUserInfo.getGroupByOSUsers", osUsers);
            
            if (null != objRtn)
            {
                return (List<OSUserInfoEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getGroupByOSUsers failed! osUsers is {}", osUsers, e);
        }
        
        return new ArrayList<OSUserInfoEntity>(0);
    }
    
    /**
     * 获取用户组信息
     * @param group OS用户组
     * @return 用户组集合
     */
    @Override
    public OSGroupInfoEntity getOSGroup(String group)
    {
        LOGGER.debug("Enter getOSGroup. group is {}", group);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.OSGroupInfo.getOSGroup", group);
            
            if (null != objRtn)
            {
                return (OSGroupInfoEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSGroup failed! group is {}", group, e);
        }
        
        return null;
    }
    
    /**
     * 新增OS用户组
     * @param groupE OS用户组
     * @throws Exception 异常
     */
    @Override
    public void addOSGroup(OSGroupInfoEntity groupE)
        throws Exception
    {
        LOGGER.debug("Enter addOSGroup. groupE is {}", groupE);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.OSGroupInfo.addOSGroup", groupE);
        }
        catch (Exception e)
        {
            LOGGER.error("addOSGroup failed! groupE is {}", groupE, e);
            throw e;
        }
    }
    
    /**
     * 修改OS用户组
     * @param groupE OS用户组
     * @throws Exception 异常
     */
    @Override
    public void updateOSGroup(OSGroupInfoEntity groupE)
        throws Exception
    {
        LOGGER.debug("Enter updateOSGroup. groupE is {}", groupE);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.OSGroupInfo.updateOSGroup", groupE);
        }
        catch (Exception e)
        {
            LOGGER.error("updateOSGroup failed! groupE is {}", groupE, e);
            throw e;
        }
    }
    
    /**
     * 删除OS用户组信息
     * @param groupE OS用户组
     * @throws Exception 异常
     */
    @Override
    public void deleteOSGroup(OSGroupInfoEntity groupE)
        throws Exception
    {
        LOGGER.debug("Enter deleteOSGroup. mOSUserInfo is {}", groupE);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.OSGroupInfo.deleteOSGroup", groupE);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOSGroup failed! mOSUserInfo is {}", groupE, e);
            throw e;
        }
    }
    
    /**
     * 获取OS用户组列表
     * @return OS用户组列表
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSGroupInfoEntity> getOSGroups()
    {
        LOGGER.debug("Enter getOSGroups.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.OSGroupInfo.getOSGroups");
            
            if (null != objRtn)
            {
                return (List<OSGroupInfoEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSGroups failed!", e);
        }
        
        return null;
    }
    
    /**
     * 增加操作记录
     * @param operateAuditInfo 操作记录
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void addOperLog(OperateAuditInfoEntity operateAuditInfo)
        throws Exception
    {
        LOGGER.debug("Enter addOperLog. operateAuditInfo is {}", operateAuditInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.operationRecord.addOperLog", operateAuditInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addOperLog failed! operateAuditInfo is {}", operateAuditInfo, e);
            throw e;
        }
    }
    
    /**
     * 由查询条件取出审计记录
     * @param search 查询条件
     * @return 审计记录集合
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperateAuditInfoEntity> getOperationRecords(OperationRecordSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getOperationRecords. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.operationRecord" + ".getOperationRecords",
                    search);
            
            if (null != objRtn)
            {
                return (List<OperateAuditInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<OperateAuditInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOperationRecords failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过查询条件查询指定审计记录总数
     * @param search 查询
     * @return 审计记录总数
     * @throws Exception 异常
     */
    @Override
    public Integer getRecordCount(OperationRecordSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getRecordCount. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.operationRecord.getRecordCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRecordCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 获取告警项
     * @param taskId 任务Id
     * @return 告警项
     * @throws Exception 异常
     */
    @Override
    public TaskAlarmItemsEntity getTaskAlarmItems(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter getTaskAlarmItems. taskId is {}", taskId);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskAI.getTaskAlarmItems", taskId);
            
            if (null != objRtn)
            {
                return (TaskAlarmItemsEntity)objRtn;
                
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskAlarmItems failed! taskId is {}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 通过任务id以及等级查询任务告警渠道
     * 
     * @param taskAlarmChannel 任务告警渠道实体
     * @throws Exception 异常
     * @return 任务告警渠道实体
     */
    @Override
    public TaskAlarmChannelInfoEntity getTaskAlarmChannelInfo(TaskAlarmChannelInfoEntity taskAlarmChannel)
        throws Exception
    {
        LOGGER.debug("Enter getTaskAlarmChannelInfo. taskAlarmChannel is {}", taskAlarmChannel);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskAC.getTaskAlarmChannel", taskAlarmChannel);
            
            if (null != objRtn)
            {
                return (TaskAlarmChannelInfoEntity)objRtn;
                
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskAlarmChannelInfo failed! taskAlarmChannel is {}", taskAlarmChannel, e);
            throw e;
        }
    }
    
    /**
     * 插入告警记录
     * @param alarmFactInfo 告警记录实体
     * @return 是否成功
     * @throws Exception 异常
     */
    @Override
    public boolean addAlarmFactInfo(AlarmFactInfoEntity alarmFactInfo)
        throws Exception
    {
        LOGGER.debug("Enter addAlarmFactInfo. alarmFactInfo is {}", alarmFactInfo);
        try
        {
            int objRtn = getSqlSession().insert("com.huawei.platform.tcc.dao.alarmFT.addAlarmfact", alarmFactInfo);
            
            if (objRtn > 0)
            {
                return true;
                
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("addAlarmFactInfo failed! taskAlarmChannel is {}", alarmFactInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取所有的告警阈值记录
     * @throws Exception 异常
     * @return 任务告警阈值集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAlarmThresholdEntity> getAlarmThresholdList()
        throws Exception
    {
        LOGGER.debug("Enter getAlarmThresholdList.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.taskAlarmTS.getAlarmThresholdList");
            
            if (null != objRtn)
            {
                return (List<TaskAlarmThresholdEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<TaskAlarmThresholdEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmThresholdList failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取告警阈值
     * @param taskId 任务Id 
     * @throws Exception 异常
     * @return 任务告警阈值
     */
    @Override
    public TaskAlarmThresholdEntity getAlarmThreshold(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter getAlarmThreshold. taskId is {}", taskId);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskAlarmTS.getAlarmThreshold", taskId);
            
            if (null != objRtn)
            {
                return (TaskAlarmThresholdEntity)objRtn;
                
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmThreshold failed! taskId is {}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 获取任务Id告警渠道列表
     * @param taskId 任务Id
     * @return 任务Id告警渠道列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAlarmChannelInfoEntity> getAlarmChannelList(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter getAlarmChannelList. taskId is {}", taskId);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.taskAC.getTaskAlarmChannels", taskId);
            
            if (null != objRtn)
            {
                return (List<TaskAlarmChannelInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<TaskAlarmChannelInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmChannelList failed! taskId is {}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 新增告警渠道
     * @param alarmChannel 告警渠道
     * @throws Exception 异常
     */
    @Override
    public void addAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception
    {
        LOGGER.debug("Enter addAlarmChannel. mOSUserInfo is {}", alarmChannel);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskAC.addTaskAlarmChannel", alarmChannel);
        }
        catch (Exception e)
        {
            LOGGER.error("addAlarmChannel failed! alarmChannel is {}", alarmChannel, e);
            throw e;
        }
    }
    
    /**
     * 更新告警渠道
     * @param alarmChannel 告警渠道
     * @throws Exception 异常
     */
    @Override
    public void updateAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception
    {
        LOGGER.debug("Enter updateAlarmChannel. alarmChannel is {}", alarmChannel);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskAC.updateAlarmChannel", alarmChannel);
        }
        catch (Exception e)
        {
            LOGGER.error("updateAlarmChannel failed! alarmChannel is {}", alarmChannel, e);
            throw e;
        }
    }
    
    /**
     * 删除任务的告警渠道信息
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    @Override
    public void deleteAlarmChannel(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter deleteAlarmChannel. taskId is {}", taskId);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskAC.deleteAlarmChannel", taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteAlarmChannel failed! taskId is {}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 新增告警阈值
     * @param alarmThreshold 告警阈值
     * @throws Exception 异常
     */
    @Override
    public void addAlarmThreshold(TaskAlarmThresholdEntity alarmThreshold)
        throws Exception
    {
        LOGGER.debug("Enter addAlarmChannel. alarmThreshold is {}", alarmThreshold);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskAlarmTS.addAlarmThreshold", alarmThreshold);
        }
        catch (Exception e)
        {
            LOGGER.error("addAlarmThreshold failed! alarmThreshold is {}", alarmThreshold, e);
            throw e;
        }
    }
    
    /**
     * 更新告警阈值
     * @param alarmThreshold 告警阈值
     * @throws Exception 异常
     */
    @Override
    public void updateAlarmThreshold(TaskAlarmThresholdEntity alarmThreshold)
        throws Exception
    {
        LOGGER.debug("Enter updateAlarmThreshold. alarmThreshold is {}", alarmThreshold);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskAlarmTS.updateAlarmThreshold", alarmThreshold);
        }
        catch (Exception e)
        {
            LOGGER.error("updateAlarmThreshold failed! alarmThreshold is {}", alarmThreshold, e);
            throw e;
        }
    }
    
    /**
     * 删除任务的告警阈值
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    @Override
    public void deleteAlarmThreshold(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter deleteAlarmThreshold. taskId is {}", taskId);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskAlarmTS.deleteAlarmThreshold", taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteAlarmThreshold failed! taskId is {}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 新增告警类型项
     * @param alarmItems 告警类型项
     * @throws Exception 异常
     */
    @Override
    public void addAlarmItems(TaskAlarmItemsEntity alarmItems)
        throws Exception
    {
        LOGGER.debug("Enter addAlarmChannel. alarmItems is {}", alarmItems);
        try
        {
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskAI.insertAlarmItems", alarmItems);
        }
        catch (Exception e)
        {
            LOGGER.error("addAlarmItems failed! alarmItems is {}", alarmItems, e);
            throw e;
        }
    }
    
    /**
     * 更新告警类型项
     * @param alarmItems 告警类型项
     * @throws Exception 异常
     */
    @Override
    public void updateAlarmItems(TaskAlarmItemsEntity alarmItems)
        throws Exception
    {
        LOGGER.debug("Enter updateAlarmItems. alarmItems is {}", alarmItems);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.taskAI.updateAlarmItems", alarmItems);
        }
        catch (Exception e)
        {
            LOGGER.error("updateAlarmItems failed! alarmItems is {}", alarmItems, e);
            throw e;
        }
    }
    
    /**
     * 删除任务的告警类型项
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    @Override
    public void deleteAlarmItems(Long taskId)
        throws Exception
    {
        LOGGER.debug("Enter deleteAlarmItems. taskId is {}", taskId);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskAI.deleteAlarmItems", taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteAlarmItems failed! taskId is {}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 获取任务告警渠道
     * @param alarmChannel 告警渠道
     * @return 任务告警渠道
     * @throws Exception 异常
     */
    @Override
    public TaskAlarmChannelInfoEntity getAlarmChannel(TaskAlarmChannelInfoEntity alarmChannel)
        throws Exception
    {
        LOGGER.debug("Enter getAlarmChannel. alarmChannel is {}", alarmChannel);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.taskAC.getAlarmChannel", alarmChannel);
            if (null != objRtn)
            {
                return (TaskAlarmChannelInfoEntity)objRtn;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmChannel failed! alarmChannel is {}", alarmChannel, e);
            throw e;
        }
    }
    
    /**
     * 更新告警处理信息
     * @param alarmFact 告警处理信息
     * @throws Exception 异常
     */
    @Override
    public void updateAlarmFact(AlarmFactInfoEntity alarmFact)
        throws Exception
    {
        LOGGER.debug("Enter updateAlarmFact. alarmFact is {}", alarmFact);
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.alarmFT.updateAlarmFact", alarmFact);
        }
        catch (Exception e)
        {
            LOGGER.error("updateAlarmFact failed! alarmFact is {}", alarmFact, e);
            throw e;
        }
    }
    
    /**
     * 由查询条件获取告警事实信息列表
     * @param search 查询条件
     * @return 告警事实信息列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<AlarmFactInfoEntity> getAlarmFacts(AlarmFactSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getAlarmFacts. search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.alarmFT.getAlarmFacts", search);
            
            if (null != objRtn)
            {
                return (List<AlarmFactInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<AlarmFactInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmFacts failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 由查询条件获取告警事实信息列表总数
     * @param search 查询条件
     * @return 告警事实信息列表总数
     * @throws Exception 统一封装的异常
     */
    @Override
    public Integer getAlarmFactCount(AlarmFactSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getAlarmFactCount. search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.tcc.dao.alarmFT.getAlarmFactCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmFactCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 删除三个月前的审计记录
     * @param date 三个月前的时间
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteOldOperationRecord(Date date)
        throws Exception
    {
        LOGGER.debug("Enter deleteOldOperationRecord. date is {}", date);
        try
        {
            getSqlSession().delete("com.huawei.platform.tcc.dao.operationRecord.deleteOldOperationRecord", date);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOldOperationRecord failed! date is {}", date, e);
            throw e;
        }
    }
    
    /**
     * 通过业务任务组查询任务信息列表(任务名集合为多个则精确查询)
     * @param entity 任务查询条件类
     * @return 任务信息列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskEntity> getTaskListByServiceTG(TaskSearchEntity entity)
        throws Exception
    {
        try
        {
            if (null != entity)
            {
                Object obj =
                    getSqlSession().selectList("com.huawei.platform.tcc.dao." + "task.getTaskListByServiceTG", entity);
                
                if (null != obj)
                {
                    return (List<TaskEntity>)obj;
                }
            }
            
            return new ArrayList<TaskEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskListByServiceTG failed.", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 更新任务是否加载标识，如果任务已启动，则不允许改为不加载（0）
     * @param entity 任务信息
     * @throws Exception  统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public void changeTaskEnableFlag(TaskEntity entity)
        throws Exception
    {
        LOGGER.debug("Enter changeTaskEnableFlag...");
        try
        {
            getSqlSession().update("com.huawei.platform.tcc.dao.task.changeTaskEnableFlag", entity);
        }
        catch (Exception e)
        {
            LOGGER.error("changeTaskEnableFlag[entity is {}] failed!", entity, e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名查询相关的OS用户、用户组已经业务信息
     * @param osUsers OS用户列表
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<OSUserGroupServiceEntity> getOSUserGroupServicesByName(List<String> osUsers)
        throws Exception
    {
        try
        {
            //空集合直接返回
            if (null != osUsers && osUsers.isEmpty())
            {
                return new ArrayList<OSUserGroupServiceEntity>(0);
            }
            
            MultiNameSearch search = new MultiNameSearch();
            search.setNames(osUsers);
            Object obj = getSqlSession().selectList("com.huawei.platform.tcc.dao.OSUserInfo.getOSUGEByName", search);
            
            if (null != obj)
            {
                return (List<OSUserGroupServiceEntity>)obj;
            }
            
            return new ArrayList<OSUserGroupServiceEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUserGroupServicesByName failed. osUsers is {}.", osUsers, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过用户组查询指定的os用户列表
     * @param groups 用户组列表
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<String> getVisibleOsUsers(List<String> groups)
        throws Exception
    {
        try
        {
            //空集合直接返回
            if (null != groups && groups.isEmpty())
            {
                return new ArrayList<String>(0);
            }
            
            MultiNameSearch search = new MultiNameSearch();
            search.setNames(groups);
            Object obj = getSqlSession().selectList("com.huawei.platform.tcc.dao.OSUserInfo.getVisibleOsUsers", search);
            
            if (null != obj)
            {
                return (List<String>)obj;
            }
            
            return new ArrayList<String>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getVisibleOsUsers failed. groups is {}.", groups, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取OS组总数
     * @param search 检索条件
     * @return OS任务组总数
     */
    public Integer getOSGroupsCountBySearch(OsGroupSearch search)
    {
        try
        {
            Object obj =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.OSGroupInfo.getOSGroupsCountBySearch", search);
            
            if (null != obj)
            {
                return (Integer)obj;
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getOSGroupsBySearch failed. search is {}.", search, e);
        }
        
        return 0;
    }
    
    /**
     * 获取OS组列表
     * @param search 检索条件
     * @return OS组列表
     */
    @SuppressWarnings("unchecked")
    public List<OSGroupInfoEntity> getOSGroupsBySearch(OsGroupSearch search)
    {
        try
        {
            Object obj =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.OSGroupInfo.getOSGroupsBySearch", search);
            
            if (null != obj)
            {
                return (List<OSGroupInfoEntity>)obj;
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("search failed. search is {}.", search, e);
        }
        
        return new ArrayList<OSGroupInfoEntity>(0);
    }
    
    /**
     * 新增实例关系
     * @param instanceRels 实例关系
     */
    @Override
    public void addInstanceRels(List<InstanceRelationEntity> instanceRels)
    {
        LOGGER.debug("Enter addInstanceRels. instanceRels is {}", instanceRels);
        try
        {
            if (null == instanceRels || instanceRels.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.insrel.addInstanceRels", instanceRels);
        }
        catch (Exception e)
        {
            LOGGER.error("addInstanceRels failed! instanceRels is {}", instanceRels, e);
        }
    }
    
    /**
     * 删除实例关系的指定日期分区
     * @param scheduleDate 调度日期
     */
    @Override
    public void deleteInstanceRelPartition(Date scheduleDate)
    {
        LOGGER.debug("Enter deleteInstanceRelPartition. executeDate is {}", scheduleDate);
        try
        {
            Map<String, String> map = new HashMap<String, String>();
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            map.put("scheduleDate", df.format(scheduleDate));
            getSqlSession().delete("com.huawei.platform.tcc.dao.insrel.deleteInstanceRelPartition", map);
        }
        catch (Exception e)
        {
            LOGGER.warn("deleteInstanceRelPartition failed! executeDate is {}", scheduleDate, e);
        }
    }
    
    /**
     * 创建实例关系的指定日期分区
     * @param scheduleDate 调度日期
     */
    @Override
    public void addInstanceRelPartition(Date scheduleDate)
    {
        LOGGER.debug("Enter addInstanceRelPartition. executeDate is {}", scheduleDate);
        try
        {
            Map<String, String> map = new HashMap<String, String>();
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            map.put("scheduleDate", df.format(scheduleDate));
            getSqlSession().delete("com.huawei.platform.tcc.dao.insrel.addInstanceRelPartition", map);
        }
        catch (Exception e)
        {
            LOGGER.error("addInstanceRelPartition failed! executeDate is {}", scheduleDate, e);
        }
    }
    
    /**
     * 获取任务实例关系
     * @param insRelSearch 实例关系查询条件
     * @return 任务实例列表
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<InstanceRelationEntity> getInstanceRels(InstanceRelationSearch insRelSearch)
    {
        LOGGER.debug("Enter instanceRel. insRelSearch is {}.", insRelSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.insrel.getInstanceRels", insRelSearch);
            
            if (null != objRtn)
            {
                return (List<InstanceRelationEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getInstanceRels failed! insRelSearch is {}.", insRelSearch, e);
        }
        
        return new ArrayList<InstanceRelationEntity>(0);
    }
    
    /**
     * 删除实例关系
     * @param instanceRel 实例关系
     */
    @Override
    public void deleteInstanceRel(InstanceRelationEntity instanceRel)
    {
        try
        {
            LOGGER.debug("enter deleteInstanceRel, instanceRel is [{}]", instanceRel);
            getSqlSession().delete("com.huawei.platform.tcc.dao.insrel.deleteInstanceRel", instanceRel);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteInstanceRel failed, instanceRel is [{}]", instanceRel, e);
        }
    }
    
    /**
     * 获取所有目标数据库配置信息
     * @return 所有目标数据库配置信息
     * @throws CException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DBServerConfigEntity> getDBServers()
        throws CException
    {
        LOGGER.debug("Enter getDataDBServers.");
        try
        {
            final Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.dbServers.getDBServers");
            
            if (null == objRtn)
            {
                return new ArrayList<DBServerConfigEntity>(0);
            }
            else
            {
                return (List<DBServerConfigEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getDataDBServers failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取TCC服务数据库服务配置
     * @param tccName tcc名字
     * @return TCC服务数据库服务配置
     * @throws CException 异常
     */
    public DBServerConfigEntity getDBServerConfig(String tccName)
        throws CException
    {
        LOGGER.debug("Enter getDBServerConfig. tccName is {}.", tccName);
        try
        {
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.tcc.dao.dbServers.getDBServer", tccName);
            
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                return (DBServerConfigEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getDBServerConfig failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取OS用户映射关系
     * @param tccName 目标TCC
     * @return OS用户映射关系
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserMappingEntity> getOSUserMappings(String tccName)
        throws Exception
    {
        LOGGER.debug("Enter getOSUserMappings.");
        try
        {
            final Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.osusermapping.getOSUserMappings", tccName);
            
            if (null == objRtn)
            {
                return new ArrayList<OSUserMappingEntity>(0);
            }
            else
            {
                return (List<OSUserMappingEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUserMappings failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取用户组映射关系
     * @param tccName 目标TCC
     * @return 用户组映射关系
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserGroupMappingEntity> getUserGroupMappings(String tccName)
        throws Exception
    {
        LOGGER.debug("Enter getUserGroupMappings.");
        try
        {
            final Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao." + "usergroupmapping.getUserGroupMappings",
                    tccName);
            
            if (null == objRtn)
            {
                return new ArrayList<UserGroupMappingEntity>(0);
            }
            else
            {
                return (List<UserGroupMappingEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUserGroupMappings failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取路径映射关系
     * @param tccName 目标TCC
     * @return 路径映射关系
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PathMappingEntity> getPathMappings(String tccName)
        throws Exception
    {
        LOGGER.debug("Enter getPathMappings.");
        try
        {
            final Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.pathmapping.getPathMappings", tccName);
            
            if (null == objRtn)
            {
                return new ArrayList<PathMappingEntity>(0);
            }
            else
            {
                return (List<PathMappingEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getPathMappings failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取节点映射关系
     * @param tccName 目标TCC
     * @return 节点映射关系
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeMappingEntity> getNodeMappings(String tccName)
        throws Exception
    {
        LOGGER.debug("Enter getNodeMappings.");
        try
        {
            final Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.nodemapping.getNodeMappings", tccName);
            
            if (null == objRtn)
            {
                return new ArrayList<NodeMappingEntity>(0);
            }
            else
            {
                return (List<NodeMappingEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeMappings failed!", e);
            throw e;
        }
    }
    
    /**
     * 保存任务信息集合保存到任务表
     * 
     * @param tasks 任务集合
     * @throws Exception 统一封装的异常
     */
    @Override
    public void replaceTasks(List<TaskEntity> tasks)
        throws Exception
    {
        LOGGER.debug("Enter replaceTasks...");
        try
        {
            if (null == tasks || tasks.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.task.replaceTasks", tasks);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceTasks failed! tasks is {}", tasks, e);
            throw e;
        }
    }
    
    /**
     * 保存所有的告警阈值列表
     * 
     * @param alarmThresholds 告警阈值列表
     * @throws Exception 统一封装的异常
     */
    public void replaceAlarmThresholds(List<TaskAlarmThresholdEntity> alarmThresholds)
        throws Exception
    {
        LOGGER.debug("Enter replaceAlarmThresholds...");
        try
        {
            if (null == alarmThresholds || alarmThresholds.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskAlarmTS.replaceAlarmThresholds", alarmThresholds);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceAlarmThresholds failed! alarmThresholds is {}", alarmThresholds, e);
            throw e;
        }
    }
    
    /**
     * 保存任务信息保存到任务表
     * 
     * @param task 任务
     * @throws Exception 统一封装的异常
     */
    @Override
    public void replaceTask(TaskEntity task)
        throws Exception
    {
        LOGGER.debug("Enter replaceTask. task is {}!", task);
        try
        {
            if (null == task)
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.task.replaceTask", task);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceTask failed! task is {}", task, e);
            throw e;
        }
    }
    
    /**
     * 获取所有的任务步骤
     * 
     * @return 所有的任务步骤
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskStepEntity> getAllTaskSteps()
        throws Exception
    {
        List<TaskStepEntity> taskSteps = null;
        LOGGER.debug("Enter getAllTaskSteps...");
        try
        {
            taskSteps =
                (List<TaskStepEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskstep.getAllTaskSteps");
        }
        catch (Exception e)
        {
            LOGGER.error("getAllTaskSteps failed!", e);
            throw e;
        }
        
        if (null == taskSteps)
        {
            taskSteps = new ArrayList<TaskStepEntity>(0);
        }
        
        LOGGER.debug("getAllTaskSteps success...");
        
        return taskSteps;
    }
    
    /**
     * 替换任务步骤集合
     * 
     * @param steps 步骤集合
     * @throws Exception 异常
     */
    @Override
    public void replaceSteps(List<TaskStepEntity> steps)
        throws Exception
    {
        LOGGER.debug("Enter replaceSteps...");
        try
        {
            if (null == steps || steps.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskstep.replaceSteps", steps);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceSteps failed! steps is {}!", steps, e);
            throw e;
        }
        
        LOGGER.debug("replaceSteps success...");
    }
    
    /**
     * 获取任务的所有运行状态
     * @param taskIds 任务Id集合
     * @return 任务的所有运行状态
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskRunningStateEntity> getAllTaskRSs(List<Long> taskIds)
        throws Exception
    {
        List<TaskRunningStateEntity> taskRSs = null;
        LOGGER.debug("Enter getAllTaskRSs. taskIds is {}", taskIds);
        try
        {
            if (null == taskIds || taskIds.isEmpty())
            {
                return new ArrayList<TaskRunningStateEntity>(0);
            }
            
            taskRSs =
                (List<TaskRunningStateEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskrunningstate.getAllTaskRSs",
                    taskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("getAllTaskRSs failed! taskIds is {}!", taskIds, e);
            throw e;
        }
        
        if (null == taskRSs)
        {
            taskRSs = new ArrayList<TaskRunningStateEntity>(0);
        }
        
        LOGGER.debug("getAllTaskRSs success...");
        
        return taskRSs;
    }
    
    /**
     * 替换任务运行状态
     * @param taskRSs 运行状态集合
     * @throws Exception 异常
     */
    @Override
    public void replaceTaskRSs(List<TaskRunningStateEntity> taskRSs)
        throws Exception
    {
        LOGGER.debug("Enter replaceTaskRSs...");
        try
        {
            if (null == taskRSs || taskRSs.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskrunningstate.replaceTaskRSs", taskRSs);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceTaskRSs failed! taskRSs is {}!", taskRSs, e);
            throw e;
        }
        
        LOGGER.debug("replaceTaskRSs success...");
    }
    
    /**
     * 获取所有的告警项
     * @return 所有的告警项
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAlarmItemsEntity> getAlarmItems()
        throws Exception
    {
        List<TaskAlarmItemsEntity> alarmItems = null;
        LOGGER.debug("Enter getAlarmItems");
        try
        {
            alarmItems =
                (List<TaskAlarmItemsEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskAI.getAllAlarmItems");
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmItems failed!", e);
            throw e;
        }
        
        if (null == alarmItems)
        {
            alarmItems = new ArrayList<TaskAlarmItemsEntity>(0);
        }
        
        LOGGER.debug("getAlarmItems success...");
        
        return alarmItems;
    }
    
    /**
     * 获取指定任务集合的告警阈值
     * @param taskIds 任务Id集合
     * @return 告警阈值集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<TaskAlarmThresholdEntity> getAlarmThresholds(List<Long> taskIds)
        throws Exception
    {
        List<TaskAlarmThresholdEntity> alarmThresholds = null;
        LOGGER.debug("Enter getAlarmThresholds. taskIds is {}!", taskIds);
        try
        {
            if (null == taskIds || taskIds.isEmpty())
            {
                return new ArrayList<TaskAlarmThresholdEntity>(0);
            }
            
            alarmThresholds =
                (List<TaskAlarmThresholdEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskAlarmTS.getAlarmThresholds",
                    taskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmThresholds failed! taskIds is {}!", taskIds, e);
            throw e;
        }
        
        if (null == alarmThresholds)
        {
            alarmThresholds = new ArrayList<TaskAlarmThresholdEntity>(0);
        }
        
        LOGGER.debug("getAlarmThresholds success...");
        
        return alarmThresholds;
    }
    
    /**
     * 获取指定任务集合的告警项
     * @param taskIds 任务Id集合
     * @return 所有的告警项
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAlarmItemsEntity> getAlarmItems(List<Long> taskIds)
        throws Exception
    {
        List<TaskAlarmItemsEntity> alarmItems = null;
        LOGGER.debug("Enter getAlarmItems. taskIds is {}!", taskIds);
        try
        {
            if (null == taskIds || taskIds.isEmpty())
            {
                return new ArrayList<TaskAlarmItemsEntity>(0);
            }
            
            alarmItems =
                (List<TaskAlarmItemsEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskAI.getAlarmItems",
                    taskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmItems failed! taskIds is {}!", taskIds, e);
            throw e;
        }
        
        if (null == alarmItems)
        {
            alarmItems = new ArrayList<TaskAlarmItemsEntity>(0);
        }
        
        LOGGER.debug("getAlarmItems success...");
        
        return alarmItems;
    }
    
    /**
     * 替换告警项
     * @param alarmItems 告警项集合
     * @throws Exception 异常
     */
    @Override
    public void replaceAlarmItems(List<TaskAlarmItemsEntity> alarmItems)
        throws Exception
    {
        LOGGER.debug("Enter replaceAlarmItems...");
        try
        {
            if (null == alarmItems || alarmItems.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskAI.replaceAlarmItems", alarmItems);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceAlarmItems failed! alarmItems is {}!", alarmItems, e);
            throw e;
        }
        
        LOGGER.debug("replaceAlarmItems success...");
    }
    
    /**
     * 获取所有的告警渠道
     * @return 告警渠道集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAlarmChannelInfoEntity> getAlarmChannels()
        throws Exception
    {
        List<TaskAlarmChannelInfoEntity> alarmChannels = null;
        LOGGER.debug("Enter getAlarmChannels");
        try
        {
            alarmChannels =
                (List<TaskAlarmChannelInfoEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskAC.getAllAlarmChannels");
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmChannels failed!", e);
            throw e;
        }
        
        if (null == alarmChannels)
        {
            alarmChannels = new ArrayList<TaskAlarmChannelInfoEntity>(0);
        }
        
        LOGGER.debug("getAlarmChannels success...");
        
        return alarmChannels;
    }
    
    /**
     * 获取任务Id集合的告警渠道
     * @param taskIds 任务Id集合
     * @return 告警渠道集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskAlarmChannelInfoEntity> getAlarmChannels(List<Long> taskIds)
        throws Exception
    {
        List<TaskAlarmChannelInfoEntity> alarmChannels = null;
        LOGGER.debug("Enter getAlarmChannels. taskIds is {}!", taskIds);
        try
        {
            if (null == taskIds || taskIds.isEmpty())
            {
                return new ArrayList<TaskAlarmChannelInfoEntity>(0);
            }
            
            alarmChannels =
                (List<TaskAlarmChannelInfoEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao."
                    + "taskAC.getAlarmChannels",
                    taskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("getAlarmChannels failed! taskIds is {}!", taskIds, e);
            throw e;
        }
        
        if (null == alarmChannels)
        {
            alarmChannels = new ArrayList<TaskAlarmChannelInfoEntity>(0);
        }
        
        LOGGER.debug("getAlarmChannels success...");
        
        return alarmChannels;
    }
    
    /**
     * 替换所有的告警渠道
     * @param alarmChannels 告警渠道集合
     * @throws Exception 异常
     */
    @Override
    public void replaceAlarmChannels(List<TaskAlarmChannelInfoEntity> alarmChannels)
        throws Exception
    {
        LOGGER.debug("Enter replaceAlarmChannels.");
        try
        {
            if (null == alarmChannels || alarmChannels.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.taskAC.replaceAlarmChannels", alarmChannels);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceAlarmChannels failed! alarmChannels is {}!", alarmChannels, e);
            throw e;
        }
        
        LOGGER.debug("replaceAlarmChannels success...");
    }
    
    /**
     * 获取任务Id集合的任务步骤
     * @param taskIds 任务Id集合
     * @return 任务步骤集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TaskStepEntity> getTaskSteps(List<Long> taskIds)
        throws Exception
    {
        List<TaskStepEntity> steps = null;
        LOGGER.debug("Enter getTaskSteps. taskIds is {}!", taskIds);
        try
        {
            if (null == taskIds || taskIds.isEmpty())
            {
                return new ArrayList<TaskStepEntity>(0);
            }
            
            steps =
                (List<TaskStepEntity>)getSqlSession().selectList("com.huawei.platform.tcc.dao.taskstep.getSteps",
                    taskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskSteps failed! taskIds is {}!", taskIds, e);
            throw e;
        }
        
        if (null == steps)
        {
            steps = new ArrayList<TaskStepEntity>(0);
        }
        
        LOGGER.debug("getTaskSteps success...");
        
        return steps;
    }
    
    /**
     * 获取任务名重复的任务Id集合
     * @param tasks 任务集合
     * @return 任务名重复的任务Id集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getDupNameTasks(List<TaskEntity> tasks)
        throws Exception
    {
        List<Long> taskIds = null;
        LOGGER.debug("Enter getDupNameTasks.");
        try
        {
            if (null == tasks || tasks.isEmpty())
            {
                return new ArrayList<Long>(0);
            }
            
            StringBuilder values = new StringBuilder();
            for (TaskEntity taskE : tasks)
            {
                values.append('(');
                values.append(taskE.getTaskId());
                values.append(",'");
                values.append(taskE.getTaskName());
                values.append("'),");
            }
            values.deleteCharAt(values.length() - 1);
            
            taskIds =
                (List<Long>)getSqlSession().selectList("com.huawei.platform.tcc.dao.task.getDupNameTasks",
                    values.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("getDupNameTasks failed! tasks is {}!", tasks, e);
            throw e;
        }
        
        if (null == taskIds)
        {
            taskIds = new ArrayList<Long>(0);
        }
        
        LOGGER.debug("getDupNameTasks success...");
        
        return taskIds;
    }
    
    /**
     * 获取存在的任务Id集合
     * @param taskIds 任务Id集合
     * @return 存在的任务Id集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getExistTaskIds(List<Long> taskIds)
        throws Exception
    {
        List<Long> existTaskIds = null;
        LOGGER.debug("Enter getExistTaskIds. taskIds is {}!", taskIds);
        try
        {
            if (null == taskIds || taskIds.isEmpty())
            {
                return new ArrayList<Long>(0);
            }
            
            existTaskIds =
                (List<Long>)getSqlSession().selectList("com.huawei.platform.tcc.dao.task.getExistTaskIds", taskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("getExistTaskIds failed! taskIds is {}!", taskIds, e);
            throw e;
        }
        
        if (null == existTaskIds)
        {
            existTaskIds = new ArrayList<Long>(0);
        }
        
        LOGGER.debug("getExistTaskIds success...");
        
        return existTaskIds;
    }
    
    /**
     * 删除指定告警阈值
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    @Override
    public void deleteAlarmThresholds(List<Long> atNotExistTaskIds)
        throws Exception
    {
        LOGGER.debug("Enter deleteAlarmThresholds. atNotExistTaskIds is {}", atNotExistTaskIds);
        try
        {
            if (null == atNotExistTaskIds || atNotExistTaskIds.isEmpty())
            {
                return;
            }
            
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskAlarmTS.deleteAlarmThresholds", atNotExistTaskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteAlarmThresholds failed! atNotExistTaskIds is {}", atNotExistTaskIds, e);
            throw e;
        }
    }
    
    /**
     * 删除指定告警项
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    @Override
    public void deleteAlarmItems(List<Long> atNotExistTaskIds)
        throws Exception
    {
        LOGGER.debug("Enter deleteAlarmItems. atNotExistTaskIds is {}", atNotExistTaskIds);
        try
        {
            if (null == atNotExistTaskIds || atNotExistTaskIds.isEmpty())
            {
                return;
            }
            
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskAI.deleteMultiAlarmItems", atNotExistTaskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteAlarmItems failed! atNotExistTaskIds is {}", atNotExistTaskIds, e);
            throw e;
        }
    }
    
    /**
     * 删除指定告警渠道
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    @Override
    public void deleteAlarmChannels(List<Long> atNotExistTaskIds)
        throws Exception
    {
        LOGGER.debug("Enter deleteAlarmChannels. atNotExistTaskIds is {}", atNotExistTaskIds);
        try
        {
            if (null == atNotExistTaskIds || atNotExistTaskIds.isEmpty())
            {
                return;
            }
            
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskAC.deleteAlarmChannels", atNotExistTaskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteAlarmChannels failed! atNotExistTaskIds is {}", atNotExistTaskIds, e);
            throw e;
        }
    }
    
    /**
     * 删除指定任务步骤
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    @Override
    public void deleteSteps(List<Long> atNotExistTaskIds)
        throws Exception
    {
        LOGGER.debug("Enter deleteSteps. atNotExistTaskIds is {}", atNotExistTaskIds);
        try
        {
            if (null == atNotExistTaskIds || atNotExistTaskIds.isEmpty())
            {
                return;
            }
            
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskstep.deleteSteps", atNotExistTaskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteSteps failed! atNotExistTaskIds is {}", atNotExistTaskIds, e);
            throw e;
        }
    }
    
    /**
     * 删除指定任务运行状态
     * @param atNotExistTaskIds 任务ID集合
     * @throws Exception 异常
     */
    @Override
    public void deleteTaskRSs(List<Long> atNotExistTaskIds)
        throws Exception
    {
        LOGGER.debug("Enter deleteTaskRSs. atNotExistTaskIds is {}", atNotExistTaskIds);
        try
        {
            if (null == atNotExistTaskIds || atNotExistTaskIds.isEmpty())
            {
                return;
            }
            
            getSqlSession().delete("com.huawei.platform.tcc.dao.taskrunningstate.deleteTaskRSs", atNotExistTaskIds);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTaskRSs failed! atNotExistTaskIds is {}", atNotExistTaskIds, e);
            throw e;
        }
    }
    
    /**
     * 获取同步时所有需要停止步骤的任务Id集合
     * @throws Exception 异常
     * @return 需要停止步骤的任务Id集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getStepsStoppedTaskIds()
        throws Exception
    {
        List<Long> taskIds;
        LOGGER.debug("Enter getStepsStoppedTaskIds.");
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.tcc.dao.tasktbloperate.getStepsStoppedTaskIds");
            
            if (null != objRtn)
            {
                taskIds = (List<Long>)objRtn;
            }
            else
            {
                taskIds = new ArrayList<Long>(0);
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getStepsStoppedTaskIds failed!", e);
            throw e;
        }
        
        return taskIds;
    }
    
    /**
     * 获取所有的业务信息
     * @return 业务信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<ServiceDefinationEntity> getServices()
        throws Exception
    {
        List<ServiceDefinationEntity> serviceDefs;
        LOGGER.debug("Enter getServices.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.tcc.dao.serviceDef.getServices");
            
            if (null != objRtn)
            {
                serviceDefs = (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                serviceDefs = new ArrayList<ServiceDefinationEntity>(0);
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getServices failed!", e);
            throw e;
        }
        
        return serviceDefs;
    }
    
    /**
     * 替换所有的业务
     * @param services 业务集合
     * @throws Exception 异常
     */
    @Override
    public void replaceServices(List<ServiceDefinationEntity> services)
        throws Exception
    {
        LOGGER.debug("Enter replaceServices.");
        try
        {
            if (null == services || services.isEmpty())
            {
                return;
            }
            
            getSqlSession().insert("com.huawei.platform.tcc.dao.serviceDef.replaceServices", services);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceServices failed! services is {}!", services, e);
            throw e;
        }
        
        LOGGER.debug("replaceServices success...");
    }
    /************************* portal [end] ***********************************************/
    
}
