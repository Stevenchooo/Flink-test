/*
 * 文 件 名:  TaskManage.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务管理
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.tcc.task;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.DependRelation;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.event.Event;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.listener.Listener;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 任务管理
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-10]
 */
public class TaskManage implements Listener
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManage.class);
    
    //消息通知类型数
    private static final Integer NOTIFY_TYPE_NUM = 4;
    
    //任务集合
    private static final Map<Long, Task> TASKS = new HashMap<Long, Task>();
    
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
     * 获取所有已经正常启用的任务
     * @return 所有已经正常启用的任务
     */
    public List<TaskEntity> getEnabledTaskList()
    {
        List<TaskEntity> taskLst = new ArrayList<TaskEntity>();
        TaskEntity taskE;
        Task task;
        synchronized (TASKS)
        {
            for (Entry<Long, Task> entry : TASKS.entrySet())
            {
                task = entry.getValue();
                if (null != task)
                {
                    taskE = task.getTaskEntity();
                    //仅获取已经启用任务
                    if (null != taskE && null != taskE.getTaskEnableFlag() && taskE.getTaskEnableFlag())
                    {
                        //任务实体只是替换，不会修改
                        taskLst.add(taskE);
                    }
                }
            }
        }
        
        return taskLst;
    }
    
    /**
     * 获取所有已经启用非停止的任务
     * @return 所有已经启用非停止的任务
     */
    public List<TaskEntity> getNormalTaskList()
    {
        List<TaskEntity> taskLst = new ArrayList<TaskEntity>();
        TaskEntity taskE;
        Task task;
        synchronized (TASKS)
        {
            for (Entry<Long, Task> entry : TASKS.entrySet())
            {
                task = entry.getValue();
                if (null != task)
                {
                    taskE = task.getTaskEntity();
                    //仅获取已经启用任务
                    if (null != taskE && null != taskE.getTaskEnableFlag() && taskE.getTaskEnableFlag()
                        || null != taskE.getTaskState() && TaskState.NORMAL == taskE.getTaskState())
                    {
                        //任务实体只是替换，不会修改
                        taskLst.add(taskE);
                    }
                }
            }
        }
        
        return taskLst;
    }
    
    /**
     * 获取所有的任务
     * @return 所有的任务
     */
    public List<TaskEntity> getAllTaskList()
    {
        List<TaskEntity> taskLst = new ArrayList<TaskEntity>();
        TaskEntity taskE;
        Task task;
        synchronized (TASKS)
        {
            for (Entry<Long, Task> entry : TASKS.entrySet())
            {
                task = entry.getValue();
                if (null != task)
                {
                    taskE = task.getTaskEntity();
                    if (null != taskE)
                    {
                        //任务实体只是替换，不会修改
                        taskLst.add(taskE);
                    }
                }
            }
        }
        
        return taskLst;
    }
    
    /**
     * 获取所有的任务
     * @return 所有的任务
     */
    public List<TaskEntity> getAllTasksClone()
    {
        List<TaskEntity> taskLst = new ArrayList<TaskEntity>();
        TaskEntity taskE;
        Task task;
        synchronized (TASKS)
        {
            for (Entry<Long, Task> entry : TASKS.entrySet())
            {
                task = entry.getValue();
                if (null != task)
                {
                    taskE = task.getTaskEntity();
                    if (null != taskE)
                    {
                        //任务实体只是替换，不会修改
                        taskLst.add(taskE.clone());
                    }
                }
            }
        }
        
        return taskLst;
    }
    
    /**
     * 获取所有的任务
     * @param taskIds 任务Id集合
     * @return 所有的任务
     */
    public List<TaskEntity> getTasksClone(List<Long> taskIds)
    {
        List<TaskEntity> taskLst = new ArrayList<TaskEntity>();
        if (null == taskIds || taskIds.isEmpty())
        {
            return taskLst;
        }
        
        TaskEntity taskE;
        Task task;
        synchronized (TASKS)
        {
            for (Long taskId : taskIds)
            {
                task = TASKS.get(taskId);
                if (null != task)
                {
                    taskE = task.getTaskEntity();
                    if (null != taskE)
                    {
                        //任务实体只是替换，不会修改
                        taskLst.add(taskE.clone());
                    }
                }
            }
        }
        
        return taskLst;
    }
    
    /**
     * 获取所有已经正常启用的任务
     * @param taskIds 任务Id集合
     * @return 所有已经正常启用的任务
     */
    public List<TaskEntity> getEnabledTaskList(List<Long> taskIds)
    {
        List<TaskEntity> taskLst = new ArrayList<TaskEntity>();
        TaskEntity taskE;
        Task task;
        for (Long taskId : taskIds)
        {
            synchronized (TASKS)
            {
                task = TASKS.get(taskId);
            }
            if (null == task)
            {
                continue;
            }
            
            taskE = task.getTaskEntity();
            if (null == taskE)
            {
                continue;
            }
            
            //仅获取已经启用任务
            if (null != taskE.getTaskEnableFlag() && taskE.getTaskEnableFlag())
            {
                //任务实体只是替换，不会修改
                taskLst.add(taskE);
            }
        }
        
        return taskLst;
    }
    
    /**
     * 获取任务实体
     * @param taskId 任务Id
     * @return 任务实体
     */
    public TaskEntity getTaskEntity(Long taskId)
    {
        if (null != taskId)
        {
            Task task = null;
            synchronized (TASKS)
            {
                task = TASKS.get(taskId);
            }
            
            if (null != task)
            {
                //任务实体只是替换，不会修改
                return task.getTaskEntity();
            }
        }
        
        return null;
    }
    
    /**
     * 获取任务名
     * @param taskId 任务Id
     * @return 任务名
     */
    public String getTaskName(Long taskId)
    {
        if (null != taskId)
        {
            Task task = null;
            synchronized (TASKS)
            {
                task = TASKS.get(taskId);
                
                if (null != task)
                {
                    //任务实体只是替换，不会修改
                    return task.getTaskEntity().getTaskName();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 是否正常启动
     * @param taskId 任务Id
     * @return 正常启动
     */
    public boolean isNormalStart(Long taskId)
    {
        synchronized (TASKS)
        {
            Task task = TASKS.get(taskId);
            if (null != task)
            {
                TaskEntity taskE = task.getTaskEntity();
                //任务正常启动
                return null != taskE && null != taskE.getTaskEnableFlag() && taskE.getTaskEnableFlag()
                    && null != taskE.getTaskState() && TaskState.NORMAL == taskE.getTaskState();
            }
        }
        
        return false;
    }
    
    /**
     * 任务是否正常启动且正常启动
     * @param taskId 任务Id
     * @return 正常启动
     */
    public boolean isNormalStartAndNoDependsError(Long taskId)
    {
        synchronized (TASKS)
        {
            Task task = TASKS.get(taskId);
            if (null != task)
            {
                TaskEntity taskE = task.getTaskEntity();
                //任务正常启动且无依赖配置错误
                return !task.isDependError() && null != taskE && null != taskE.getTaskEnableFlag()
                    && taskE.getTaskEnableFlag() && null != taskE.getTaskState()
                    && TaskState.NORMAL == taskE.getTaskState();
            }
        }
        
        return false;
    }
    
    /**
     * 是否任务依赖错误
     * @param taskId 任务Id
     * @return 任务依赖错误
     */
    public boolean isDependsError(Long taskId)
    {
        synchronized (TASKS)
        {
            Task task = TASKS.get(taskId);
            if (null != task)
            {
                //依赖关系错误
                return task.isDependError();
            }
        }
        
        return false;
    }
    
    /**
     * 获取所有反向依赖任务集合
     * @param taskId 任务Id
     * @return 反向依赖任务集合
     */
    public List<TaskEntity> getDeppingTasks(Long taskId)
    {
        List<TaskEntity> tasks = new ArrayList<TaskEntity>();
        synchronized (TASKS)
        {
            Task task = TASKS.get(taskId);
            if (null != task)
            {
                List<TaskRelation> preTaskRs = task.getPreTaskRs();
                if (null != preTaskRs)
                {
                    //依赖关系
                    for (TaskRelation taskR : preTaskRs)
                    {
                        tasks.add(taskR.getSrcTask().getTaskEntity());
                    }
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * 获取任务(以及任务的直接依赖关系)
     * @param taskId 任务Id
     * @return 任务
     * @throws CException 依赖关系错误异常
     */
    public Task getTaskDepends(Long taskId)
        throws CException
    {
        Task task = null;
        synchronized (TASKS)
        {
            task = TASKS.get(taskId);
            if (null != task)
            {
                //依赖关系错误
                if (task.isDependError())
                {
                    throw new CException(ResultCode.TASK_DEPEND_REALATION_ERROR, "", taskId);
                }
                
                Task taskCp = new Task();
                taskCp.setTaskId(task.getTaskId());
                if (null != task.getTaskEntity())
                {
                    //任务实体只是替换，不会修改
                    taskCp.setTaskEntity(task.getTaskEntity());
                }
                
                List<TaskRelation> subTaskRs = task.getSubTaskRs();
                if (null != subTaskRs)
                {
                    taskCp.setSubTaskRs(new ArrayList<TaskRelation>());
                    Task subTask;
                    Task subTaskCp;
                    //依赖关系
                    for (TaskRelation taskR : subTaskRs)
                    {
                        subTask = taskR.getDstTask();
                        //目标节点的前驱和后继关系不再复制
                        subTaskCp = new Task();
                        subTaskCp.setTaskId(subTask.getTaskId());
                        //任务实体只是替换，不会修改
                        subTaskCp.setTaskEntity(subTask.getTaskEntity());
                        
                        taskCp.getSubTaskRs().add(new TaskRelation(taskCp, subTaskCp, taskR.isFullCycleDepend(),
                            taskR.isIgnoreError()));
                    }
                }
                
                return taskCp;
            }
        }
        
        return  null;
    }
    
    /**
     * 初始化
     */
    public void init()
    {
        //获取任务列表
        try
        {
            synchronized (TASKS)
            {
                TASKS.clear();
            }
            
            List<TaskEntity> taskLst = tccDao.getTaskList();
            
            //任务列表不为空
            if (null != taskLst)
            {
                //初始化任务
                Task task;
                
                //构造依赖关系(前驱后继)
                //后继任务
                synchronized (TASKS)
                {
                    for (TaskEntity taskE : taskLst)
                    {
                        task = new Task();
                        task.setTaskId(taskE.getTaskId());
                        task.setTaskEntity(taskE);
                        //设置默认的依赖关系为出错
                        task.setDependError(true);
                        TASKS.put(task.getTaskId(), task);
                    }
                    
                    for (Entry<Long, Task> curEntry : TASKS.entrySet())
                    {
                        task = curEntry.getValue();
                        //清除原来的数据，共享
                        conTaskRelation(task);
                    }
                }
            }
            
            Eventor.register(EventType.START_TASK, this);
            Eventor.register(EventType.STOP_TASK, this);
            Eventor.register(EventType.STOPPED_TASK, this);
            Eventor.register(EventType.ADD_TASK, this);
            Eventor.register(EventType.DELETE_TASK, this);
            Eventor.register(EventType.UPDATE_TASK, this);
        }
        catch (CException e)
        {
            LOGGER.error("TaskManage init failed!", e);
        }
    }
    
    /**
     * 删除前驱
     */
    private void removePreRelations(Long taskId)
    {
        Task task = TASKS.get(taskId);
        
        if (null != task)
        {
            //删除前驱
            List<TaskRelation> preTaskRs = task.getPreTaskRs();
            if (null != preTaskRs)
            {
                for (TaskRelation taskR : preTaskRs)
                {
                    //移除源节点的后继
                    taskR.getSrcTask().getSubTaskRs().remove(taskR);
                }
                
                //清除前驱
                preTaskRs.clear();
            }
        }
    }
    
    /**
     * 移除任务的依赖关系
     * @param taskId 任务Id
     */
    private void removeTaskRelation(Long taskId)
    {
        Task task = TASKS.get(taskId);
        
        if (null != task)
        {
            //删除后继
            List<TaskRelation> subTaskRs = task.getSubTaskRs();
            if (null != subTaskRs)
            {
                for (TaskRelation taskR : subTaskRs)
                {
                    //移除目标节点的前驱
                    taskR.getDstTask().getPreTaskRs().remove(taskR);
                }
                
                //清除后继
                subTaskRs.clear();
            }
        }
    }
    
    /**
     * 构建任务依赖关系
     */
    private void conTaskRelation(Task task)
    {
        //构造依赖关系(前驱后继)
        String depends;
        Long taskId;
        List<Long> taskIds = new ArrayList<Long>();
        HashMap<Long, DependRelation> dependRelationLst = new HashMap<Long, DependRelation>();
        
        //后继任务
        Task subTask;
        TaskRelation taskR;
        List<TaskRelation> subTaskRs;
        List<TaskRelation> preTaskRs;
        //清除原来的数据，共享
        taskIds.clear();
        dependRelationLst.clear();
        taskId = task.getTaskId();
        depends = task.getTaskEntity().getDependTaskIdList();
        
        if (StringUtils.isBlank(depends))
        {
            task.setDependError(false);
            return;
        }
        
        try
        {
            task.setDependError(false);
            //解析任务依赖依赖关系
            TccUtil.parseDependIdList(taskId, depends, dependRelationLst, taskIds);
        }
        catch (Exception e)
        {
            //任务依赖关系配置出错
            task.setDependError(true);
            //不构建相应的依赖关系，禁止调度时运行
            LOGGER.error("dependRelation of task[taskId={}] is error! please correct it!", taskId, e);
        }
        
        subTaskRs = task.getSubTaskRs();
        //需要创建后继队列
        if (null == subTaskRs)
        {
            subTaskRs = new ArrayList<TaskRelation>();
            task.setSubTaskRs(subTaskRs);
        }
        
        for (Long curTaskId : taskIds)
        {
            subTask = TASKS.get(curTaskId);
            //存在，不存在则忽略
            if (null != subTask)
            {
                //需要创建前驱队列
                preTaskRs = subTask.getPreTaskRs();
                if (null == preTaskRs)
                {
                    preTaskRs = new ArrayList<TaskRelation>();
                    subTask.setPreTaskRs(preTaskRs);
                }
                
                //是否全周期依赖
                boolean fullCycleDepend = dependRelationLst.get(curTaskId).isFullCycleDepend();
                //是否忽略错误
                boolean ignoreError = dependRelationLst.get(curTaskId).isIgnoreError();
                taskR = new TaskRelation(task, subTask, fullCycleDepend, ignoreError);
                
                subTaskRs.add(taskR);
                preTaskRs.add(taskR);
            }
            else
            {
                //依赖的任务不存在
                task.setDependError(true);
            }
        }
    }
    
    /**
     * 删除任务
     * @param taskId 任务Id
     */
    private void deleteTask(Long taskId)
    {
        if (null == taskId)
        {
            return;
        }
        
        synchronized (TASKS)
        {
            removePreRelations(taskId);
            
            removeTaskRelation(taskId);
            TASKS.remove(taskId);
        }
        
        //删除任务
        Eventor.fireEvent(this, EventType.TASK_DELETE_FINISHED, taskId);
    }
    
    /**
     * 修改任务的状态
     */
    private void changeTaskState(Long taskId, int state)
    {
        if (null == taskId)
        {
            return;
        }
        
        boolean dependChanged = false;
        synchronized (TASKS)
        {
            Task task = TASKS.get(taskId);
            if (null != task)
            {
                TaskEntity taskEOld = task.getTaskEntity();
                taskEOld.setTaskState(state);
                //启动任务的话需要启用任务
                if (state == TaskState.NORMAL)
                {
                    taskEOld.setTaskEnableFlag(true);
                    
                    //原来的依赖关系错误，需要重新构造依赖
                    if (task.isDependError())
                    {
                        //移除原来的依赖关系
                        removeTaskRelation(taskId);
                        //重新构建新的依赖关系
                        conTaskRelation(task);
                    }
                    
                    //依赖正确了
                    if (!task.isDependError())
                    {
                        dependChanged = true;
                    }
                }
            }
        }
        
        //通知
        if (state == TaskState.NORMAL)
        {
            Eventor.fireEvent(this, EventType.TASK_START_FINISHED, taskId);
        }
        else
        {
            Eventor.fireEvent(this, EventType.TASK_STOP_FINISHED, taskId);
        }
        
        //通知依赖关系改变
        if (dependChanged)
        {
            Eventor.fireEvent(this, EventType.TASK_DEPEND_RELATION_CHANGED, taskId);
        }
        
    }
    
    /**
     * 加载任务
     * @param tasks 任务列表
     * @throws CException 异常
     */
    public void loadTasks(List<TaskEntity> tasks)
        throws CException
    {
        if (null == tasks || tasks.isEmpty())
        {
            return;
        }
        
        List<Long> taskIds = new ArrayList<Long>();
        Long taskId;
        Task task;
        //初始化
        Map<Long, BitSet> notifyBits = new HashMap<Long, BitSet>();
        for (TaskEntity taskE : tasks)
        {
            taskIds.add(taskE.getTaskId());
            notifyBits.put(taskE.getTaskId(), new BitSet(NOTIFY_TYPE_NUM));
        }
        
        List<TaskEntity> dependingTasks = tccDao.getDependedTasks(taskIds);
        //排除掉taskIds集合中的任务
        Iterator<TaskEntity> dependingTasksIter = dependingTasks.iterator();
        TaskEntity dependingTaskE;
        while (dependingTasksIter.hasNext())
        {
            dependingTaskE = dependingTasksIter.next();
            taskId = dependingTaskE.getTaskId();
            if (null == taskId || taskIds.contains(taskId))
            {
                dependingTasksIter.remove();
            }
        }
        
        for (TaskEntity taskDependingE : dependingTasks)
        {
            notifyBits.put(taskDependingE.getTaskId(), new BitSet(NOTIFY_TYPE_NUM));
        }
        
        int addedBit = 0;
        int dependsChangedBit = addedBit + 1;
        int startTimeChangedBit = dependsChangedBit + 1;
        int cycleTypeLenChangedBit = startTimeChangedBit + 1;
        synchronized (TASKS)
        {
            for (TaskEntity taskE : tasks)
            {
                taskId = taskE.getTaskId();
                //不存在的新增
                task = TASKS.get(taskId);
                if (null == task)
                {
                    task = new Task();
                    task.setTaskId(taskId);
                    task.setTaskEntity(taskE);
                    //设置默认的依赖关系为出错
                    task.setDependError(true);
                    
                    //新增
                    TASKS.put(taskId, task);
                    
                    //设置新增位
                    notifyBits.get(taskId).set(addedBit);
                }
            }
            
            for (TaskEntity taskDepending : dependingTasks)
            {
                taskId = taskDepending.getTaskId();
                //不存在的新增
                task = TASKS.get(taskId);
                if (null == task)
                {
                    task = new Task();
                    task.setTaskId(taskId);
                    task.setTaskEntity(taskDepending);
                    //设置默认的依赖关系为出错
                    task.setDependError(true);
                    
                    //新增
                    TASKS.put(taskId, task);
                    
                    //设置新增位
                    notifyBits.get(taskId).set(addedBit);
                }
            }
            
            TaskEntity taskEOld;
            boolean dependsChanged;
            boolean startTimeChanged;
            boolean cycleTypeLenChanged;
            for (TaskEntity taskENew : tasks)
            {
                taskId = taskENew.getTaskId();
                if (null == taskId)
                {
                    continue;
                }
                
                dependsChanged = false;
                startTimeChanged = false;
                cycleTypeLenChanged = false;
                
                task = TASKS.get(taskId);
                taskEOld = task.getTaskEntity();
                
                dependsChanged = !TccUtil.equals(taskENew.getDependTaskIdList(), taskEOld.getDependTaskIdList());
                startTimeChanged = !TccUtil.equals(taskENew.getStartTime(), taskEOld.getStartTime());
                cycleTypeLenChanged = !TccUtil.equals(taskENew.getCycleLength(), taskEOld.getCycleLength());
                cycleTypeLenChanged =
                    cycleTypeLenChanged || !TccUtil.equals(taskENew.getCycleType(), taskEOld.getCycleType());
                //依赖关系不变,仅更新配置
                task.setTaskEntity(taskENew);
                
                //依赖改变或者原来的依赖错误（依赖的任务现在存在了）
                if (dependsChanged || task.isDependError())
                {
                    //移除原来的依赖关系
                    removeTaskRelation(taskId);
                    //重新构建新的依赖关系
                    conTaskRelation(task);
                }
                
                //依赖改变
                if (dependsChanged)
                {
                    //设置新增位
                    notifyBits.get(taskId).set(dependsChangedBit);
                }
                
                //其实时间改变
                if (startTimeChanged)
                {
                    //设置新增位
                    notifyBits.get(taskId).set(startTimeChangedBit);
                }
                
                //周期长度或者类型
                if (cycleTypeLenChanged)
                {
                    //设置新增位
                    notifyBits.get(taskId).set(cycleTypeLenChangedBit);
                }
            }
            
            //检查是否因为新增的任务，使得原来不正确的依赖关系现在可以变得正确
            for (TaskEntity taskDepending : dependingTasks)
            {
                taskId = taskDepending.getTaskId();
                
                task = TASKS.get(taskId);
                //原来的依赖错误
                if (task.isDependError())
                {
                    //移除原来的依赖关系
                    removeTaskRelation(taskId);
                    //重新构建新的依赖关系
                    conTaskRelation(task);
                }
                
                //依赖变正确了
                if (!task.isDependError())
                {
                    //设置新增位
                    notifyBits.get(taskId).set(dependsChangedBit);
                }
            }
        }
        
        //通知,置于synchronized块外，防止锁竞争严重
        BitSet bits;
        for (Entry<Long, BitSet> keyValueSet : notifyBits.entrySet())
        {
            taskId = keyValueSet.getKey();
            bits = keyValueSet.getValue();
            //新增
            if (bits.get(addedBit))
            {
                //新增任务
                Eventor.fireEvent(this, EventType.TASK_ADD_FINISHED, taskId);
            }
            
            //依赖改变
            if (bits.get(dependsChangedBit))
            {
                Eventor.fireEvent(this, EventType.TASK_DEPEND_RELATION_CHANGED, taskId);
            }
            
            //起始时间改变
            if (bits.get(startTimeChangedBit))
            {
                Eventor.fireEvent(this, EventType.TASK_START_TIME_CHANGED, taskId);
            }
            
            //周期长度或者类型
            if (bits.get(cycleTypeLenChangedBit))
            {
                Eventor.fireEvent(this, EventType.TASK_CYCLE_TYPE_LENGTH_CHANGED, taskId);
            }
        }
    }
    
    /**
     * 新增或者更新任务
     * @param taskId 任务Id
     * @param taskENew 新任务
     */
    private void addOrUpdateTask(Long taskId, TaskEntity taskENew)
    {
        if (null == taskId)
        {
            return;
        }
        
        try
        {
            boolean added = false;
            boolean dependsChanged = false;
            boolean startTimeChanged = false;
            boolean cycleTypeLenChanged = false;
            
            if (null == taskENew)
            {
                taskENew = tccDao.getTask(taskId);
            }
            synchronized (TASKS)
            {
                Task task = TASKS.get(taskId);
                if (null != task)
                {
                    TaskEntity taskEOld = task.getTaskEntity();
                    
                    dependsChanged = !TccUtil.equals(taskENew.getDependTaskIdList(), taskEOld.getDependTaskIdList());
                    startTimeChanged = !TccUtil.equals(taskENew.getStartTime(), taskEOld.getStartTime());
                    cycleTypeLenChanged = !TccUtil.equals(taskENew.getCycleLength(), taskEOld.getCycleLength());
                    cycleTypeLenChanged =
                        cycleTypeLenChanged || !TccUtil.equals(taskENew.getCycleType(), taskEOld.getCycleType());
                    //依赖关系不变,仅更新配置
                    task.setTaskEntity(taskENew);
                    
                    //依赖改变或者原来的依赖错误（依赖的任务现在存在了）
                    if (dependsChanged || task.isDependError())
                    {
                        //移除原来的依赖关系
                        removeTaskRelation(taskId);
                        //重新构建新的依赖关系
                        conTaskRelation(task);
                    }
                }
                else
                {
                    task = new Task();
                    task.setTaskId(taskId);
                    task.setTaskEntity(taskENew);
                    task.setDependError(true);
                    //新增
                    TASKS.put(taskId, task);
                    //构造依赖关系
                    conTaskRelation(task);
                    
                    added = true;
                }
            }
            
            if (added)
            {
                //新增任务
                Eventor.fireEvent(this, EventType.TASK_ADD_FINISHED, taskId);
            }
            
            //依赖改变
            if (dependsChanged)
            {
                Eventor.fireEvent(this, EventType.TASK_DEPEND_RELATION_CHANGED, taskId);
            }
            
            //其实时间改变
            if (startTimeChanged)
            {
                Eventor.fireEvent(this, EventType.TASK_START_TIME_CHANGED, taskId);
            }
            
            //周期长度或者类型
            if (cycleTypeLenChanged)
            {
                Eventor.fireEvent(this, EventType.TASK_CYCLE_TYPE_LENGTH_CHANGED, taskId);
            }
        }
        catch (CException e)
        {
            LOGGER.error("addOrUpdateTask failed!", e);
        }
    }
    
    @Override
    public void process(Event event)
    {
        if (null != event)
        {
            int eventType = event.getType();
            if (EventType.ADD_TASK == eventType)
            {
                addOrUpdateTask((Long)(event.getData()), null);
            }
            else if (EventType.UPDATE_TASK == eventType || EventType.STOPPED_TASK == eventType)
            {
                addOrUpdateTask((Long)(event.getData()), null);
            }
            else if (EventType.DELETE_TASK == eventType)
            {
                deleteTask((Long)(event.getData()));
            }
            else if (EventType.START_TASK == eventType)
            {
                changeTaskState((Long)(event.getData()), TaskState.NORMAL);
            }
            else if (EventType.STOP_TASK == eventType)
            {
                changeTaskState((Long)(event.getData()), TaskState.STOP);
            }
            
        }
    }
}
