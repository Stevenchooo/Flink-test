/*
 * 文 件 名:  TccServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.tcc.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.TaskType;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.Log2DBQueryParam;
import com.huawei.platform.tcc.domain.LongTimeShellParam;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.domain.ServiceSearch;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.entity.BatchRunningStateEntity;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.entity.LongTimeShellEntity;
import com.huawei.platform.tcc.entity.NodeInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.ServiceDeployInfoEntity;
import com.huawei.platform.tcc.entity.ServiceTaskGroupInfoEntity;
import com.huawei.platform.tcc.entity.StepRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.entity.TaskStepExchangeEntity;
import com.huawei.platform.tcc.service.TccPortalService;
import com.huawei.platform.tcc.utils.JDBCExtAppender;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * TCC业务逻辑操作实现
 * 
 * @author z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-11-10]
 * @see [相关类/方法]
 */
public class TccPortalServiceImpl implements TccPortalService, Serializable
{
    /**
     * 序号
     */
    private static final long serialVersionUID = -1088385948803845580L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccPortalServiceImpl.class);
    
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
     * 获得任务ID编号
     * @return 任务ID的编号
     * @param serviceID 业务ID
     * @param taskType 任务类型
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Long generateTaskID(Integer serviceID, Integer taskType)
        throws CException
    {
        Long taskID = null;
        try
        {
            Integer taskIDSerailNo = tccDao.generateTaskIDSerialNo(serviceID, taskType);
            //处理任务ID编号，不足4位前面补零
            String serailNo = processStringLen(String.valueOf(taskIDSerailNo), TccConfig.TASKID_SERAILNO_LENGTH);
            //处理任务类型，不足2位前面补零
            String taskTypeTemp = processStringLen(String.valueOf(taskType), TaskType.LENGTH);
            //生成任务ID，业务ID＋2位任务类型＋4位编号
            StringBuffer sb = new StringBuffer();
            sb.append(String.valueOf(serviceID)).append(taskTypeTemp).append(serailNo);
            taskID = Long.valueOf(sb.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("generateTaskID failed, serviceID is [{}],taskType is [{}].", new Object[] {serviceID,
                taskType, e});
            throw new CException(ResultCodeConstants.GENERATE_TASKID_ERROR, e);
        }
        return taskID;
    }
    
    /**
     * 保存任务信息
     * @param entity 新建的任务信息
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void addTask(TaskEntity entity)
        throws Exception
    {
        try
        {
            tccDao.addTask(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("add taskInfo failed, entity is [{}].", entity, e);
            throw e;
        }
    }
    
    /**
     * 更新任务信息
     * @param entity 任务信息
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateTask(TaskEntity entity)
        throws Exception
    {
        try
        {
            tccDao.updateTask(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("update taskInfo failed, entity is [{}].", entity, e);
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
        try
        {
            return tccDao.startTask(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("startTask taskInfo failed, entity is [{}].", entity, e);
            throw e;
        }
    }
    
    /**
     * 获取全部任务信息列表
     * @param entity 任务查询条件类
     * @return 全部任务信息列表
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<TaskEntity> getTaskAllList(TaskSearchEntity entity)
        throws CException
    {
        List<TaskEntity> taskList = null;
        try
        {
            taskList = tccDao.getTaskAllList(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskAllList failed.", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return taskList;
    }
    
    /**
     * 有查询条件获取数据库任务信息的条数(任务名集合为多个则精确查询)
     * @param entity 查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Integer getTaskListSize(TaskSearchEntity entity)
        throws CException
    {
        Integer total = null;
        try
        {
            total = tccDao.getTaskListSize(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskListSize failed.", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return total;
    }
    
    /**
     * 通过名字模糊查询任务信息列表(任务名集合为多个则精确查询)
     * @param entity 任务查询条件类
     * @return 任务信息列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskEntity> getTaskListByNames(TaskSearchEntity entity)
        throws CException
    {
        List<TaskEntity> taskList = null;
        try
        {
            taskList = tccDao.getTaskListByNames(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get getTaskListByNames failed.", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return taskList;
    }
    
    /**
     * 通过名字模糊查询任务信息的条数(任务名集合为多个则精确查询)
     * @param entity 查询条件
     * @return 记录条数
     * @throws CException 统一封装的异常
     */
    @Override
    public Integer getTaskListSizeByNames(TaskSearchEntity entity)
        throws CException
    {
        Integer total = null;
        try
        {
            total = tccDao.getTaskListSizeByNames(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get TaskListSizeByNames failed.", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return total;
    }
    
    /**
     * 获取任务信息
     * @param taskID 任务ID
     * @return TaskEntity 任务信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public TaskEntity getTaskInfo(Long taskID)
        throws CException
    {
        TaskEntity entity = null;
        try
        {
            entity = tccDao.getTask(taskID);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskInfo failed, taskID is [{}].", taskID, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return entity;
    }
    
    /**
     * 删除任务
     * @param taskId 传入的任务ID
     * @see [类、类#方法、类#成员]
     * @throws CException 统一封装的异常
     */
    @Override
    public void deleteTask(Long taskId)
        throws CException
    {
        try
        {
            tccDao.deleteTask(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTask failed, taskID is [{}].", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 补齐一个String类型的值到指定位数
     * 如果输入参数为空返回空
     * 大于指定长度就截取后面的
     * 不足前补充0
     * @param str1 输入的参数值
     * @param length 指定的长度
     * @return 步道指定长度的值
     * @see [类、类#方法、类#成员]
     */
    private static String processStringLen(String str1, int length)
    {
        String str = str1;
        String defaultcode = "0000";
        if (StringUtils.isNotBlank(str))
        {
            int len = str.trim().length();
            if (len > length)
            {
                //大于指定长度就截取后面的
                str = str.substring(len - length);
            }
            else if (len < length)
            {
                //不足前补充0
                str = defaultcode.substring(0, length - len) + str;
            }
        }
        return str;
    }
    
    /**
     * 获得步骤ID序号
     * @param taskId  任务Id
     * @return 步骤ID的编号
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int generateTaskStepID(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter generateTaskStepID. param is [taskId={}]", taskId);
        try
        {
            return tccDao.generateTaskStepID(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("generate taskStepID[taskId={}] failed!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 新增任务步骤信息
     * @param entity 新建的任务步骤信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void addTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter addTaskStep. param is [entity={}]", entity);
        try
        {
            tccDao.addTaskStep(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("add taskStep[{}] failed!", entity, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit addTaskStep. param is [entity={}]", entity);
    }
    
    /**
     * 更新任务步骤信息
     * @param entity 任务步骤信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter updateTaskStep. param is [entity={}]", entity);
        try
        {
            tccDao.updateTaskStep(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("update taskStep[{}] failed!", entity, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit updateTaskStep. param is [entity={}]", entity);
    }
    
    /**
    * 删除任务步骤信息
    * @param entity 任务步骤信息
    * @throws CException 统一封装的异常
    */
    @Override
    public void deleteTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter deleteTaskStep. param is [entity={}]", entity);
        try
        {
            tccDao.deleteTaskStep(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("delete taskStep[{}] failed!", entity, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        LOGGER.debug("Exit deleteTaskStep. param is [entity={}]", entity);
    }
    
    /**
     * 获取任务步骤信息
     * @param entity 任务步骤信息
     * @return 任务步骤信息
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public TaskStepEntity getTaskStep(TaskStepEntity entity)
        throws CException
    {
        LOGGER.debug("Enter getTaskStep. param is [entity={}]", entity);
        try
        {
            return tccDao.getTaskStep(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskStep[{}] failed!", entity, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务步骤信息列表
     * @param taskId 任务Id
     * @return 任务步骤信息列表
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<TaskStepEntity> getTaskStepList(Long taskId)
        throws CException
    {
        LOGGER.debug("Enter getTaskStep. param is [taskId={}]", taskId);
        try
        {
            return tccDao.getTaskStepList(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskStepList[taskId={}] failed!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 交换任务步骤Id
     * @param entity 任务步骤交换信息
     * @throws CException 统一封装的异常
     */
    @Override
    public void exchangeTaskStep(TaskStepExchangeEntity entity)
        throws CException
    {
        LOGGER.debug("exchange taskStep. param is [{}]", entity);
        try
        {
            if (null != entity && null != entity.getTaskId() && null != entity.getStepIdOne()
                && null != entity.getStepIdTwo())
            {
                TaskStepExchangeEntity stepTemp = new TaskStepExchangeEntity();
                stepTemp.setTaskId(entity.getTaskId());
                stepTemp.setStepIdOne(entity.getStepIdOne());
                stepTemp.setStepIdTwo(-entity.getStepIdOne());
                //修改stepIdOne为临时Id
                tccDao.updateTaskStepId(stepTemp);
                stepTemp.setStepIdOne(entity.getStepIdTwo());
                stepTemp.setStepIdTwo(entity.getStepIdOne());
                //修改stepIdTwo为stepIdOne
                tccDao.updateTaskStepId(stepTemp);
                stepTemp.setStepIdOne(-entity.getStepIdOne());
                stepTemp.setStepIdTwo(entity.getStepIdTwo());
                //修改stepIdOne的临时Id为stepIdTwo
                tccDao.updateTaskStepId(stepTemp);
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("exchange taskStep[taskStepExchange={}] failed!", entity, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有的任务Id列表
     * @return 获取所有的任务Id列表
     * @param vSTgs 可见的业务任务组
     * @throws CException 统一封装的异常
     */
    @Override
    public List<Long> getAllTaskIdList(List<String> vSTgs)
        throws CException
    {
        LOGGER.debug("Enter getAllTaskIdList.");
        try
        {
            return tccDao.getAllTaskIdList(vSTgs);
        }
        catch (Exception e)
        {
            LOGGER.error("get getAllTaskIdList failed!", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有的任务Id、名称列表
     * @param vSTgs 可见的业务任务组
     * @return 获取所有的任务Id、名称列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskEntity> getAllTaskIdNameList(List<String> vSTgs)
        throws CException
    {
        LOGGER.debug("Enter getAllTaskIdNameList.vSTgs is {}", vSTgs);
        try
        {
            return tccDao.getAllTaskIdNameList(vSTgs);
        }
        catch (Exception e)
        {
            LOGGER.error("get getAllTaskIdNameList failed! vSTgs is {}", vSTgs, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有任务周期的批次状态列表
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有任务周期的批次状态列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<BatchRunningStateEntity> getBatchRSList(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getBatchRSList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            return tccDao.getBatchRSList(taskId, cycleId);
        }
        catch (Exception e)
        {
            LOGGER.error("getBatchRSList failed!,param=[taskId={},cycleId={}]!", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有任务周期的步骤运行状态列表
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 所有任务周期的步骤运行状态列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<StepRunningStateEntity> getStepRSList(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getStepRSList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            return tccDao.getStepRSList(taskId, cycleId);
        }
        catch (Exception e)
        {
            LOGGER.error("getStepRSList failed!,param=[taskId={},cycleId={}]", new Object[] {taskId, cycleId, e});
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询任务运行状态列表(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam 查询条件
     * @return 任务运行状态列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskRunningStateEntity> getTaskRSList(TaskRSQueryParam taskRSQueryParam)
        throws CException
    {
        LOGGER.debug("enter getTaskRSList, param is [taskRSQueryParam={}]", taskRSQueryParam);
        try
        {
            return tccDao.getTaskRSList(taskRSQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRSList failed!, taskRSQueryParam={}!", taskRSQueryParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        
    }
    
    /**
     * 获取查询任务运行状态的总数(如果任务名以分号结尾，则根据任务名精确查询，否则根据任务名为模糊查询)
     * 
     * @param taskRSQueryParam 查询条件
     * @return 查询任务运行状态的总数
     * @throws CException 统一封装的异常
     */
    @Override
    public Integer getTaskRSCount(TaskRSQueryParam taskRSQueryParam)
        throws CException
    {
        LOGGER.debug("enter getTaskRSCount, param is [taskRSQueryParam={}]", taskRSQueryParam);
        try
        {
            return tccDao.getTaskRSCount(taskRSQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRSCount failed!, taskRSQueryParam={}", taskRSQueryParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务运行状态的总数
     * 
     * @param taskId 任务Id
     * @return 任务运行状态的总数
     * @throws CException 统一封装的异常
     */
    @Override
    public Integer getTaskRSCount(Long taskId)
        throws CException
    {
        LOGGER.debug("enter getTaskRSCount, param is [taskId={}]", taskId);
        try
        {
            return tccDao.getTaskRSCount(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskRSCount failed!, taskId={}", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务ID列表
     * @param taskIdList 任务ID列表
     * @return 任务ID列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskEntity> getTaskList(List<Long> taskIdList)
        throws CException
    {
        LOGGER.debug("Enter getTaskList...");
        try
        {
            return tccDao.getTaskList(taskIdList);
            
        }
        catch (Exception e)
        {
            LOGGER.error("get taskList failed!", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取启动任务步骤条数
     * @param taskId 任务Id
     * @return 启动任务步骤条数
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public int getTaskStepEnableSize(Long taskId)
        throws CException
    {
        try
        {
            return tccDao.getTaskStepEnableSize(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("get taskStepEnableSize failed!", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有步骤列表
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    @Override
    public void deleteTaskSteps(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteTaskSteps, param is [taskId={}]", taskId);
        try
        {
            tccDao.deleteTaskSteps(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTaskSteps failed!, taskId={}!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有运行状态
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    @Override
    public void deleteTaskRunningStates(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteTaskRunningStates, param is [taskId={}]", taskId);
        try
        {
            tccDao.deleteTaskRunningStates(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTaskRunningStates failed!, taskId={}!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有批次运行状态
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    @Override
    public void deleteBatchRunningStates(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteBatchRunningStates, param is [taskId={}]", taskId);
        try
        {
            tccDao.deleteBatchRunningStates(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteBatchRunningStates failed!, taskId={}!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务ID的所有步骤运行状态
     * @param taskId 任务ID
     * @throws CException 统一封装的异常
     */
    @Override
    public void deleteStepRunningStates(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteStepRunningStates, param is [taskId={}]", taskId);
        try
        {
            tccDao.deleteStepRunningStates(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteStepRunningStates failed!, taskId={}!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除所有依赖任务Id列表中包含任务Id的依赖关系
     * @param taskId 任务Id
     * @throws CException 统一封装的异常
     */
    @Override
    public void deleteDependTaskId(Long taskId)
        throws CException
    {
        LOGGER.debug("enter deleteDependTaskId, param is [taskId={}]", taskId);
        try
        {
            tccDao.deleteDependTaskId(taskId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteDependTaskId failed!, taskId={}!", taskId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务运行状态列表
     * 
     * @param taskRSList 需要查询的任务运行状态列表
     * @return 任务运行状态列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskRunningStateEntity> getTaskRunningStateList(List<TaskRunningStateEntity> taskRSList)
        throws CException
    {
        LOGGER.debug("enter getTaskRunningStateList, param is [taskRSList={}]", taskRSList);
        try
        {
            return tccDao.getTaskRunningStateList(taskRSList);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteDependTaskId failed!, taskRSList={}!", taskRSList, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将depend_Task_Id_List中包含task.taskName的那部分依赖信息替换成task.taskId
     * @param task 任务Id和任务名
     * @throws CException 统一封装的异常
     */
    @Override
    public void replaceDependTaskName2Id(TaskEntity task)
        throws CException
    {
        LOGGER.debug("enter replaceDependTaskName2Id, param is [task={}]", task);
        try
        {
            tccDao.replaceDependTaskName2Id(task);
        }
        catch (Exception e)
        {
            LOGGER.error("replaceDependTaskName2Id failed!, task={}!", task, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过taskName获取任务实体
     * @param taskName 任务名
     * @return 任务实体
     * @throws CException 统一封装的异常
     */
    @Override
    public TaskEntity getTask(String taskName)
        throws CException
    {
        LOGGER.debug("Enter getTask. taskName is {}", taskName);
        try
        {
            return tccDao.getTask(taskName);
        }
        catch (Exception e)
        {
            LOGGER.error("get Task[taskName={}] failed!", taskName, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务依赖树的所有任务集合
     * @param taskIds 任务Id集合
     * @return 任务依赖树的所有任务集合
     * @throws CException 统一封装的异常
     */
    public List<TaskEntity> getTaskDeppedTrees(List<Long> taskIds)
        throws CException
    {
        List<TaskEntity> taskDealed = new ArrayList<TaskEntity>();
        taskDealed.addAll(tccDao.getTaskList(taskIds));
        
        List<Long> taskIdTmps = new ArrayList<Long>();
        taskIdTmps.addAll(taskIds);
        //获取依赖于该任务的所有任务
        List<TaskEntity> tasks;
        while (!taskIdTmps.isEmpty())
        {
            tasks = tccDao.getDependedTasks(taskIdTmps);
            taskIdTmps.clear();
            for (TaskEntity taskE : tasks)
            {
                if (!taskDealed.contains(taskE))
                {
                    taskIdTmps.add(taskE.getTaskId());
                    taskDealed.add(taskE);
                }
            }
        }
        
        return taskDealed;
    }
    
    /**
     * 获取直接依赖于taskIds任务的任务集合
     * @param taskIds 任务Id集合
     * @return 直接依赖于taskIds任务的任务集合
     * @throws CException 统一封装的异常
     */
    @Override
    public List<TaskEntity> getDeppingTasks(List<Long> taskIds)
        throws CException
    {
        LOGGER.debug("enter getDeppingTasks, param is [taskIds={}]", taskIds);
        try
        {
            if (null != taskIds && !taskIds.isEmpty())
            {
                return tccDao.getDependedTasks(taskIds);
            }
            
            return new ArrayList<TaskEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getDeppingTasks failed!, taskIds={}!", taskIds, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取任务周期的相关日志信息
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 任务周期的相关日志信息
     * @throws CException 统一封装的异常
     */
    @Override
    public List<Log2DBEntity> getTccLogList(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getTccLogList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null == taskId || StringUtils.isEmpty(cycleId))
            {
                return new ArrayList<Log2DBEntity>(0);
            }
            TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(taskId);
            taskRS.setCycleId(cycleId);
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getTccLogList(taskRS);
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed, param is [taskId={},cycleId={}]", new Object[] {taskId, cycleId}, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取remoteshell执行的控制台输出日志
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return remoteshell执行的控制台输出日志
     * @throws CException 统一封装的异常
     */
    @Override
    public List<Log2DBEntity> getRsLogList(Long taskId, String cycleId)
        throws CException
    {
        LOGGER.debug("enter getRsLogList, param is [taskId={},cycleId={}]", taskId, cycleId);
        try
        {
            if (null == taskId || StringUtils.isEmpty(cycleId))
            {
                return new ArrayList<Log2DBEntity>(0);
            }
            TaskRunningStateEntity taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(taskId);
            taskRS.setCycleId(cycleId);
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getRsLogList(taskRS);
        }
        catch (Exception e)
        {
            LOGGER.debug("get RsLogList failed, param is [taskId={},cycleId={}]", new Object[] {taskId, cycleId}, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 根据查询条件查询tcc日志记录列表
     * @param logQueryParam 日志查询条件
     * @return 查询tcc日志记录列表
     * @throws CException 统一封装的异常
     */
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
            
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getTccLogList(logQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * tcc日志记录数
     * @param logQueryParam 日志查询条件
     * @return tcc日志记录数
     * @throws CException 统一封装的异常
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
            
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getTccLogCount(logQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogCount failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 根据查询条件查询tcc日志记录列表
     * @param logQueryParam 日志查询条件
     * @return 查询tcc日志记录列表
     * @throws CException 统一封装的异常
     */
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
            
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getRsLogList(logQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.debug("get getRsLogList failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * rs日志记录数
     * @param logQueryParam 日志查询条件
     * @return rs日志记录数
     * @throws CException 统一封装的异常
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
            
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getRsLogCount(logQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.debug("getTccLogCount failed, param is [logQueryParam={}]", logQueryParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取已经记录日志的所有用户名列表
     * @return 已经记录日志的所有用户名列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<String> getAllUserName()
        throws CException
    {
        LOGGER.debug("enter getAllUserName.");
        try
        {
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return tccDao.getAllUserName();
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed.", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * @param param 查询条件
     * @return 周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表总数
     * @throws Exception 异常
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
            
            return tccDao.getLongTimeShellsCount(param);
        }
        catch (Exception e)
        {
            LOGGER.debug("getLongTimeShellsCount failed, param is {}", param, e);
            throw e;
        }
    }
    
    /**
     * 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * @param param 查询条件
     * @return 分页查询周期落在指定范围内，并且执行时间超过阈值的长时间脚本列表
     * @throws Exception 异常
     */
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
            
            return tccDao.getLongTimeShellList(param);
        }
        catch (Exception e)
        {
            LOGGER.debug("get LongTimeShellList failed, param is {}", param, e);
            throw e;
        }
    }
    
    /**
     * 重新初始化任务的重做开始以及重做结束范围内的任务周期
     * @param taskE 任务实体
     * @throws Exception 异常
     */
    @Override
    public void reInitTaskRS(TaskEntity taskE)
        throws Exception
    {
        LOGGER.debug("enter reInitTaskRS, taskE={}", taskE);
        try
        {
            if (null == taskE || null == taskE.getTaskId() || null == taskE.getRedoStartTime()
                || null == taskE.getRedoEndTime())
            {
                return;
            }
            
            TaskRSQueryParam taskRSQueryParam = new TaskRSQueryParam();
            taskRSQueryParam.setTaskId(taskE.getTaskId());
            taskRSQueryParam.setStartCycleID(getStartCycleId(taskE.getRedoStartTime()));
            taskRSQueryParam.setEndCycleID(TccUtil.covDate2CycleID(taskE.getRedoEndTime()));
            
            tccDao.reInitTaskRS(taskRSQueryParam);
        }
        catch (Exception e)
        {
            LOGGER.debug("reInitTaskRS failed, taskE is {}", taskE, e);
            throw e;
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
    
    /**
     * 获取业务Id业务名集合
     * @return 业务Id业务名集合
     * @throws CException 统一封装的异常
     */
    @Override
    public List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws CException
    {
        LOGGER.debug("Enter getAllServiceIdNameList.");
        try
        {
            return tccDao.getAllServiceIdNameList();
        }
        catch (Exception e)
        {
            LOGGER.error("getAllServiceIdNameList failed!", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
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
            if (null != serviceSearch)
            {
                return tccDao.getServicesCountByName(serviceSearch);
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
     * 通过业务名集合查询指定业务
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    @Override
    public List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception
    {
        LOGGER.debug("Enter getServicesByNames. serviceSearch is {}", serviceSearch);
        try
        {
            return tccDao.getServicesByName(serviceSearch);
        }
        catch (Exception e)
        {
            LOGGER.error("getServicesByNames failed! serviceSearch is {}", serviceSearch, e);
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
            tccDao.addService(serviceDef);
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
     * @throws Exception 异常
     */
    @Override
    public void updateService(ServiceDefinationEntity serviceDef)
        throws Exception
    {
        LOGGER.debug("Enter updateService. serviceDef is {}", serviceDef);
        try
        {
            tccDao.updateService(serviceDef);
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
            tccDao.deleteService(serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteService failed! serviceId is {}", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 新增业务任务组
     * @param serviceTG 业务组
     * @throws Exception 异常
     */
    @Override
    public void addTaskGroup(ServiceTaskGroupInfoEntity serviceTG)
        throws Exception
    {
        LOGGER.debug("Enter addTaskGroup. serviceTG is {}", serviceTG);
        try
        {
            tccDao.addTaskGroup(serviceTG);
        }
        catch (Exception e)
        {
            LOGGER.error("addTaskGroup failed! serviceTG is {}", serviceTG, e);
            throw e;
        }
    }
    
    /**
     * 修改业务任务组
     * @param serviceTG 业务组
     * @throws Exception 异常
     */
    @Override
    public void updateTaskGroup(ServiceTaskGroupInfoEntity serviceTG)
        throws Exception
    {
        LOGGER.debug("Enter updateTaskGroup. serviceTG is {}", serviceTG);
        try
        {
            tccDao.updateTaskGroup(serviceTG);
        }
        catch (Exception e)
        {
            LOGGER.error("updateTaskGroup failed! serviceTG is {}", serviceTG, e);
            throw e;
        }
    }
    
    /**
     * 获取任务组信息列表
     * @param serviceId 业务Id
     * @return 任务组信息列表
     * @throws Exception 异常
     */
    @Override
    public List<ServiceTaskGroupInfoEntity> getTaskGroups(Integer serviceId)
        throws Exception
    {
        LOGGER.debug("Enter getTaskGroups. serviceId is {}", serviceId);
        try
        {
            return tccDao.getTaskGroups(serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskGroups failed! serviceId is {}", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 获取任务组总数
     * @param search 业务id
     * @return 任务组总数
     * @throws Exception 统一封装的异常
     */
    public Integer getTaskGroupsCount(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getTaskGroupsCount. search is {}", search);
        try
        {
            if (null != search)
            {
                return tccDao.getTaskGroupsCount(search);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskGroupsCount failed! serviceId is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 删除业务任务组
     * @param taskGroup 业务组
     * @throws Exception 异常
     */
    @Override
    public void deleteServiceTG(ServiceTaskGroupInfoEntity taskGroup)
        throws Exception
    {
        LOGGER.debug("Enter deleteServiceTG. taskGroup is {}", taskGroup);
        try
        {
            tccDao.deleteServiceTG(taskGroup);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteServiceTG failed! serviceId is {}", taskGroup, e);
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
            if (null == serviceId)
            {
                return null;
            }
            
            return tccDao.getService(serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("get Service failed, serviceId is [{}].", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 节点信息列表
     * @param node 查询条件
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @Override
    public List<NodeInfoEntity> getNodeInfoList(NodeInfoEntity node)
        throws Exception
    {
        LOGGER.debug("enter getNodeInfoList, node is {}", node);
        try
        {
            if (null == node)
            {
                return new ArrayList<NodeInfoEntity>(0);
            }
            
            return tccDao.getNodeInfoList(node);
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeInfoList failed, node is [{}].", node, e);
            throw e;
        }
    }
    
    /**
     * 获取业务部署信息列表
     * @param serviceDeploy 查询条件
     * @return 业务部署信息列表
     * @throws Exception 异常
     */
    @Override
    public List<ServiceDeployInfoEntity> getServiceDeployInfo(ServiceDeployInfoEntity serviceDeploy)
        throws Exception
    {
        LOGGER.debug("enter getServiceDeployInfo, serviceDeploy is {}", serviceDeploy);
        try
        {
            if (null == serviceDeploy)
            {
                return new ArrayList<ServiceDeployInfoEntity>(0);
            }
            
            return tccDao.getServiceDeployInfo(serviceDeploy);
        }
        catch (Exception e)
        {
            LOGGER.error("getServiceDeployInfo failed, serviceDeploy is [{}].", serviceDeploy, e);
            throw e;
        }
    }
    
    /**
     * 新增业务部署信息
     * @param serviceDeploy 业务部署信息
     * @return 是否新增成功
     */
    @Override
    public boolean addServiceDeploy(ServiceDeployInfoEntity serviceDeploy)
    {
        LOGGER.debug("Enter addServiceDeploy. serviceDeploy is {}", serviceDeploy);
        try
        {
            return tccDao.addServiceDeploy(serviceDeploy);
        }
        catch (Exception e)
        {
            LOGGER.error("addServiceDeploy failed! serviceDeploy is {}", serviceDeploy, e);
        }
        return false;
    }
    
    /**
     * 修改业务部署信息
     * @param serviceDeploy 业务部署信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateServiceDeploy(ServiceDeployInfoEntity serviceDeploy)
    {
        LOGGER.debug("Enter updateServiceDeploy. serviceDeploy is {}", serviceDeploy);
        try
        {
            return tccDao.updateServiceDeploy(serviceDeploy);
        }
        catch (Exception e)
        {
            LOGGER.error("updateServiceDeploy failed! serviceDeploy is {}", serviceDeploy, e);
        }
        return false;
    }
    
    /**
     * 删除业务部署信息
     * @param serviceDeploy 业务部署信息
     * @return 是否删除成功
     */
    @Override
    public boolean deleteServiceDeploy(ServiceDeployInfoEntity serviceDeploy)
    {
        LOGGER.debug("Enter deleteServiceDeploy. serviceDeploy is {}", serviceDeploy);
        try
        {
            return tccDao.deleteServiceDeploy(serviceDeploy);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteServiceDeploy failed! serviceDeploy is {}", serviceDeploy, e);
        }
        return false;
    }
    
}
