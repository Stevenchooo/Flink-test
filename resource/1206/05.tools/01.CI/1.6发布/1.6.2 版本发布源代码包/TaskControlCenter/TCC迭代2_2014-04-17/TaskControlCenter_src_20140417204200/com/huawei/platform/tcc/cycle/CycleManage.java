/*
 * 文 件 名:  CycleManage.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  周期管理类
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-21
 */
package com.huawei.platform.tcc.cycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.CycleDependRelation;
import com.huawei.platform.tcc.domain.DGLink;
import com.huawei.platform.tcc.domain.DGNode;
import com.huawei.platform.tcc.domain.Digraph;
import com.huawei.platform.tcc.domain.InstanceRelationSearch;
import com.huawei.platform.tcc.domain.TaskCycleParam;
import com.huawei.platform.tcc.domain.TaskMinMaxCycle;
import com.huawei.platform.tcc.entity.InstanceRelationEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.event.Event;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.listener.Listener;
import com.huawei.platform.tcc.task.Task;
import com.huawei.platform.tcc.task.TaskManage;
import com.huawei.platform.tcc.task.TaskRelation;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 周期管理类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-21]
 */
public class CycleManage implements Listener
{
    private static final int MOD_NUM = 1000;
    
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CycleManage.class);
    
    //任务周期集合，按照任务Id、周期Id分类
    //应该采用压缩存储方式
    private static final Map<Long, Map<String, Cycle>> CYCLES = new HashMap<Long, Map<String, Cycle>>();
    
    /**
     * 周期统计信息
     */
    private static final Map<Long, TaskMinMaxCycle> CYCLE_RANGES = new HashMap<Long, TaskMinMaxCycle>();
    
    /**
     * 依赖关系OK的所有任务周期
     */
    private static final Set<Cycle> DEP_RELA_OK_CYCLES = new HashSet<Cycle>();
    
    private Date startDate;
    
    private TaskManage taskManage;
    
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
     * 获取taskRS的所有依赖周期集合
     * @param taskRS 任务周期
     * @return 所有的依赖周期集合
     */
    public synchronized List<TaskRunningStateEntity> getDependsTaskRSs(TaskRunningStateEntity taskRS)
    {
        List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
        
        Map<String, Cycle> cycleMaps = CYCLES.get(taskRS.getTaskId());
        if (null == cycleMaps)
        {
            return taskRSs;
        }
        
        Cycle cycle = cycleMaps.get(taskRS.getCycleId());
        if (null == cycle)
        {
            return taskRSs;
        }
        
        List<CycleRelation> relations = cycle.getSubCycleRs();
        if (null != relations)
        {
            for (CycleRelation cycleRelation : relations)
            {
                taskRSs.add(cycleRelation.getDstCycle().getTaskRS());
            }
        }
        
        return taskRSs;
    }
    
    /**
     * 是否可以运行
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 是否可以运行
     */
    public synchronized boolean canRun(Long taskId, String cycleId)
    {
        Cycle cycle = getCycle(taskId, cycleId);
        if (null == cycle)
        {
            return false;
        }
        
        //可参与调度调度、依赖全部ok、到达可运行时间
        if (cycle.canSchedule() && timeOK(taskId, cycleId)
            && taskManage.isNormalStartAndNoDependsError(cycle.getTaskRS().getTaskId()))
        {
            if (cycle.isMoreSubRs())
            {
                LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because moreSubRs is true.",
                    taskId,
                    cycle.getTaskRS().getCycleId());
                //说明依赖关系不完整,对不完整的依赖关系进行重建
                cycle.setConNExistSubCycle(true);
                reConCycleSubRels(cycle);
            }
            
            return cycle.isDependsOk();
        }
        
        return false;
    }
    
    /**
     * 是否是忽略错误运行
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @return 是否可以运行
     */
    public synchronized boolean ignoreErrorRun(Long taskId, String cycleId)
    {
        Cycle cycle = getCycle(taskId, cycleId);
        if (null == cycle)
        {
            return false;
        }
        
        if (cycle.isMoreSubRs())
        {
            LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because moreSubRs is true.",
                taskId,
                cycle.getTaskRS().getCycleId());
            //说明依赖关系不完整,对不完整的依赖关系进行重建
            cycle.setConNExistSubCycle(true);
            reConCycleSubRels(cycle);
        }
        
        //是否忽略错误运行
        return cycle.ignoreErrorRun();
    }
    
    private Date getExpectedExecuteDate(Long taskId, String cycleId, int state)
    {
        TaskEntity taskE = null;
        Date currentDate = null;
        Date canRunningTime = null;
        try
        {
            taskE = taskManage.getTaskEntity(taskId);
            canRunningTime = TccUtil.canRunningTime(cycleId, taskE.getCycleOffset());
            
            //今天已经可以运行但是还没有开始执行，那么预期执行时间即为今天
            if (canRunningTime.before(startDate))
            {
                return startDate;
            }
            else
            {
                return TccUtil.getDate(canRunningTime);
            }
        }
        catch (CException e)
        {
            LOGGER.error("canRunningTime is {},currentDate is {}, taskE is {}.", new Object[] {canRunningTime,
                currentDate, taskE, e});
        }
        
        return null;
    }
    
    private boolean timeOK(Long taskId, String cycleId)
    {
        TaskEntity taskE = null;
        Date currentDate = null;
        Date canRunningTime = null;
        try
        {
            taskE = taskManage.getTaskEntity(taskId);
            currentDate = new Date();
            canRunningTime = TccUtil.canRunningTime(cycleId, taskE.getCycleOffset());
            
            //任务周期时间上可以运行
            if (currentDate.after(canRunningTime))
            {
                return true;
            }
        }
        catch (CException e)
        {
            LOGGER.error("canRunningTime is {},currentDate is {}, taskE is {}.", new Object[] {canRunningTime,
                currentDate, taskE, e});
        }
        
        return false;
    }
    
    private Cycle getCycle(Long taskId, String cycleId)
    {
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            return null;
        }
        
        return cycleMaps.get(cycleId);
    }
    
    /**
     * 如果不存在则创建周期以及正反向依赖关系，否则直接返回周期
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param otherDaySchedule 是否非当天调度日期
     * @return 周期
     */
    private Cycle createCycle(Long taskId, String cycleId, boolean otherDaySchedule)
    {
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            cycleMaps = new HashMap<String, Cycle>();
            CYCLES.put(taskId, cycleMaps);
        }
        
        TaskRunningStateEntity taskRS;
        TaskMinMaxCycle taskMinMaxCycle;
        Cycle cycle = cycleMaps.get(cycleId);
        if (null == cycle)
        {
            taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(taskId);
            taskRS.setCycleId(cycleId);
            
            cycle = new Cycle(taskRS);
            
            taskMinMaxCycle = CYCLE_RANGES.get(taskId);
            if (null != taskMinMaxCycle && null != taskMinMaxCycle.getMaxCycleId()
                && cycleId.compareTo(taskMinMaxCycle.getMinCycleId()) >= 0
                && cycleId.compareTo(taskMinMaxCycle.getMaxCycleId()) <= 0)
            {
                //在最大最小值区间的周期状态为sucess
                taskRS.setState(RunningState.SUCCESS);
                
                cycle.setOtherDaySchedule(otherDaySchedule);
            }
            else
            {
                //其它情况为不存在
                taskRS.setState(RunningState.NOTINIT);
                
                //noinit状态需要保存
            }
            
            //设置预期执行时间
            cycle.setExpectExecuteDate(getExpectedExecuteDate(taskRS.getTaskId(),
                taskRS.getCycleId(),
                taskRS.getState()));
            cycleMaps.put(cycleId, cycle);
            
            //构建依赖关系
            reConCyclePreRels(cycle);
            reConCycleSubRels(cycle);
        }
        
        return cycle;
    }
    
    /**
     * 获取所有可以运行的任务周期列表
     * @return 所有可以运行的任务周期列表
     */
    public synchronized List<TaskRunningStateEntity> getCanRunningTaskRSs()
    {
        Long taskId = null;
        String cycleId = null;
        List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
        for (Cycle cycle : DEP_RELA_OK_CYCLES)
        {
            taskId = cycle.getTaskRS().getTaskId();
            cycleId = cycle.getTaskRS().getCycleId();
            //选择时间上可运行的周期
            if (timeOK(taskId, cycleId))
            {
                //仅获取拷贝
                taskRSs.add(cycle.getTaskRS().clone());
            }
        }
        
        return taskRSs;
    }
    
    /**
     * 获取所有可以运行的任务周期列表
     * @param tmpStopedTasks 临时停止的任务
     * @return 所有可以运行的任务周期列表
     */
    public synchronized List<TaskRunningStateEntity> getCanRunningTaskRSs(Set<Long> tmpStopedTasks)
    {
        Long taskId;
        String cycleId;
        List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
        for (Cycle cycle : DEP_RELA_OK_CYCLES)
        {
            taskId = cycle.getTaskRS().getTaskId();
            if (tmpStopedTasks.contains(taskId))
            {
                continue;
            }
            
            cycleId = cycle.getTaskRS().getCycleId();
            //选择时间上可运行的周期
            if (timeOK(taskId, cycleId))
            {
                taskRSs.add(cycle.getTaskRS());
            }
        }
        
        return taskRSs;
    }
    
    /**
     * 初始化
     * @param taskM 任务管理
     */
    public synchronized void init(TaskManage taskM)
    {
        //TODO 直接返回
        int a = 1;
        if (a == 1)
        {
            return;
        }
        
        try
        {
            CYCLES.clear();
            CYCLE_RANGES.clear();
            DEP_RELA_OK_CYCLES.clear();
            
            this.taskManage = taskM;
            
            startDate = TccUtil.getCurrentDate();
            
            List<TaskRunningStateEntity> taskAllRSs = new ArrayList<TaskRunningStateEntity>();
            List<TaskRunningStateEntity> taskRSs;
            Map<String, Cycle> cycleMaps;
            Long taskId;
            TaskEntity taskE;
            String minCycleId;
            
            //构造任务周期的统计信息
            List<TaskMinMaxCycle> taskMinMaxCycles = tccDao.getTaskMinMaxCycleIds();
            
            if (null != taskMinMaxCycles && !taskMinMaxCycles.isEmpty())
            {
                for (TaskMinMaxCycle minMaxCycle : taskMinMaxCycles)
                {
                    taskE = taskManage.getTaskEntity(minMaxCycle.getTaskId());
                    if (null == taskE)
                    {
                        LOGGER.warn("task[taskId={}] doesn't exist!", minMaxCycle.getTaskId());
                        continue;
                    }
                    
                    minCycleId = TccUtil.getMinCycleId(taskE.getStartTime());
                    if (null == minMaxCycle.getMinCycleId()
                        || minCycleId.compareToIgnoreCase(minMaxCycle.getMinCycleId()) > 0)
                    {
                        //修正最小周期边界
                        minMaxCycle.setMinCycleId(minCycleId);
                    }
                    
                    //需要对最小周期Id进行修正
                    CYCLE_RANGES.put(minMaxCycle.getTaskId(), minMaxCycle);
                }
            }
            
            List<TaskEntity> tasks = taskManage.getAllTaskList();
            TaskMinMaxCycle minMaxCycle;
            //获取所有状态为非sucess状态的任务周期
            for (TaskEntity task : tasks)
            {
                taskId = task.getTaskId();
                
                minMaxCycle = CYCLE_RANGES.get(taskId);
                if (null == minMaxCycle)
                {
                    minMaxCycle = new TaskMinMaxCycle();
                    minMaxCycle.setTaskId(taskId);
                    minMaxCycle.setMinCycleId(TccUtil.getMinCycleId(task.getStartTime()));
                    CYCLE_RANGES.put(taskId, minMaxCycle);
                }
                
                cycleMaps = CYCLES.get(taskId);
                if (null == cycleMaps)
                {
                    cycleMaps = new HashMap<String, Cycle>();
                    CYCLES.put(taskId, cycleMaps);
                }
                
                //仅加载开始时间和起始时间之后的周期
                taskRSs = tccDao.getNSTaskRSs(taskId, minMaxCycle.getMinCycleId(), null);
                if (null != taskRSs && !taskRSs.isEmpty())
                {
                    taskAllRSs.addAll(taskRSs);
                }
            }
            
            Cycle cycle;
            for (TaskRunningStateEntity taskRS : taskAllRSs)
            {
                cycleMaps = CYCLES.get(taskRS.getTaskId());
                if (null == cycleMaps)
                {
                    cycleMaps = new HashMap<String, Cycle>();
                    CYCLES.put(taskRS.getTaskId(), cycleMaps);
                }
                
                //构建周期依赖时，需要构建不存在的周期
                cycle = new Cycle(taskRS, true);
                //设置预期执行时间
                cycle.setExpectExecuteDate(getExpectedExecuteDate(taskRS.getTaskId(),
                    taskRS.getCycleId(),
                    taskRS.getState()));
                cycleMaps.put(taskRS.getCycleId(), cycle);
            }
            
            LOGGER.info("load {} taskRSs.", taskAllRSs.size());
            //遍历所有的周期，根据任务依赖构建周期依赖关系
            int i = 0;
            for (TaskRunningStateEntity taskRS : taskAllRSs)
            {
                i++;
                if (i % MOD_NUM == 0)
                {
                    LOGGER.info("construct cycle relations[{}]", i);
                }
                
                reConCycleSubRels(CYCLES.get(taskRS.getTaskId()).get(taskRS.getCycleId()));
            }
            
            //遍历所有节点，找出反向依赖不全的周期
            markMorePreSubRels();
            
            LOGGER.info("construct cycle relations[{}] finished!", taskAllRSs.size());
            
            //保存今天的实例关系
            saveCycleRelations();
            
            grabDependsOkCycles();
            
            //注册监听事件
            Eventor.register(EventType.CHANGE_TASKRS_STATE, this);
            Eventor.register(EventType.CHANGE_MULTI_TASKRS_STATE, this);
            Eventor.register(EventType.RANGE_REINIT_TASKRS_STATE, this);
            Eventor.register(EventType.INTEGRATION_CHANGE_TASKRS_STATE, this);
            Eventor.register(EventType.ADD_TASKRS, this);
            Eventor.register(EventType.ADD_MULTI_TASKRS, this);
            Eventor.register(EventType.DELETE_TASKRS, this);
            Eventor.register(EventType.TASK_START_TIME_CHANGED, this);
            Eventor.register(EventType.TASK_DEPEND_RELATION_CHANGED, this);
            Eventor.register(EventType.TASK_CYCLE_TYPE_LENGTH_CHANGED, this);
            Eventor.register(EventType.TASK_ADD_FINISHED, this);
            Eventor.register(EventType.TASK_DELETE_FINISHED, this);
            Eventor.register(EventType.TASK_START_FINISHED, this);
            Eventor.register(EventType.TASK_STOP_FINISHED, this);
        }
        catch (Exception e)
        {
            LOGGER.error("init failed!", e);
        }
    }
    
    private void markMorePreSubRels()
    {
        Long taskId = null;
        //遍历周期集合
        for (Entry<Long, Map<String, Cycle>> cyclesIter : CYCLES.entrySet())
        {
            taskId = cyclesIter.getKey();
            //任务的依赖关系配置出错，记录日志
            if (taskManage.isDependsError(taskId))
            {
                LOGGER.error("the dependding relations of task[taskId={}] error!", taskId);
                continue;
            }
            
            String cycleId;
            Cycle cycle;
            TaskEntity taskE;
            List<TaskEntity> tasks;
            List<CycleDependRelation> cDeppingRLst;
            List<CycleRelation> preCycleRels;
            //必需从启用的非停止任务中选择
            for (Entry<String, Cycle> cycleIter : cyclesIter.getValue().entrySet())
            {
                cycle = cycleIter.getValue();
                cycleId = cycle.getTaskRS().getCycleId();
                taskE = taskManage.getTaskEntity(taskId);
                tasks = taskManage.getDeppingTasks(taskId);
                //因为周期已经存在，反向依赖的周期可能需要重新依赖它【不能忽略】
                try
                {
                    cDeppingRLst = TccUtil.getDeppingCycleRs(taskE, tasks, cycleId);
                    preCycleRels = cycle.getPreCycleRs();
                    if (null == cDeppingRLst && null == preCycleRels)
                    {
                        cycle.setMorePreRs(false);
                    }
                    else if (null != cDeppingRLst && null != preCycleRels && preCycleRels.size() == cDeppingRLst.size())
                    {
                        //反向依赖的数目相同
                        cycle.setMorePreRs(false);
                    }
                    else
                    {
                        cycle.setMorePreRs(true);
                    }
                    
                }
                catch (Exception e)
                {
                    LOGGER.error("markMorePreSubRels failed! cycle is {}.", cycle, e);
                }
            }
        }
    }
    
    /**
     * 从周期集合中选择依赖关系全部满足又可以参与调度的周期
     */
    private void grabDependsOkCycles()
    {
        Long taskId = null;
        //遍历周期集合
        for (Entry<Long, Map<String, Cycle>> cyclesIter : CYCLES.entrySet())
        {
            taskId = cyclesIter.getKey();
            //任务的依赖关系配置出错，记录日志
            if (taskManage.isDependsError(taskId))
            {
                LOGGER.error("the dependding relations of task[taskId={}] error!", taskId);
                continue;
            }
            
            //必需从启用的非停止任务中选择
            if (!taskManage.isNormalStart(taskId))
            {
                continue;
            }
            
            for (Entry<String, Cycle> cycleIter : cyclesIter.getValue().entrySet())
            {
                Cycle cycle = cycleIter.getValue();
                
                if (cycle.canSchedule())
                {
                    if (cycle.isMoreSubRs())
                    {
                        LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because "
                            + "moreSubRs is true.", cycle.getTaskRS().getTaskId(), cycle.getTaskRS().getCycleId());
                        //说明依赖关系不完整,对不完整的依赖关系进行重建
                        cycle.setConNExistSubCycle(true);
                        reConCycleSubRels(cycle);
                    }
                    
                    if (cycle.isDependsOk())
                    {
                        DEP_RELA_OK_CYCLES.add(cycle);
                        LOGGER.info("cycle[{}]", cycle.getTaskRS());
                    }
                }
            }
        }
    }
    
    /**
     * 从disCycles中重新选择依赖关系全部满足又可以参与调度的周期
     */
    private void reChoosedDependsOkCycles(Collection<Cycle> disCycles)
    {
        if (null == disCycles)
        {
            return;
        }
        
        //遍历周期集合
        for (Cycle cycle : disCycles)
        {
            if (null != cycle)
            {
                //先移除
                DEP_RELA_OK_CYCLES.remove(cycle);
                
                if (cycle.canSchedule() && taskManage.isNormalStartAndNoDependsError(cycle.getTaskRS().getTaskId()))
                {
                    if (cycle.isMoreSubRs())
                    {
                        LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because"
                            + " moreSubRs is true.", cycle.getTaskRS().getTaskId(), cycle.getTaskRS().getCycleId());
                        //说明依赖关系不完整,对不完整的依赖关系进行重建
                        cycle.setConNExistSubCycle(true);
                        reConCycleSubRels(cycle);
                    }
                    
                    if (cycle.isDependsOk())
                    {
                        DEP_RELA_OK_CYCLES.add(cycle);
                    }
                }
            }
        }
    }
    
    /**
     * 从disCycles中重新选择依赖关系全部满足又可以参与调度的周期
     * @param taskId 任务Id
     */
    private void reChoosedDependsOkCycles(Long taskId)
    {
        if (null == taskId)
        {
            return;
        }
        
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            return;
        }
        
        Cycle cycle;
        //遍历周期集合
        for (Entry<String, Cycle> cycleIter : cycleMaps.entrySet())
        {
            cycle = cycleIter.getValue();
            if (null == cycle)
            {
                LOGGER.error("cycle[taskId={},cycleId={}] not exist!", taskId, cycleIter.getKey());
                continue;
            }
            
            //先移除
            DEP_RELA_OK_CYCLES.remove(cycle);
            
            if (cycle.canSchedule() && taskManage.isNormalStartAndNoDependsError(cycle.getTaskRS().getTaskId()))
            {
                if (cycle.isMoreSubRs())
                {
                    LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because moreSubRs is true.",
                        cycle.getTaskRS().getTaskId(),
                        cycle.getTaskRS().getCycleId());
                    //说明依赖关系不完整,对不完整的依赖关系进行重建
                    cycle.setConNExistSubCycle(true);
                    reConCycleSubRels(cycle);
                }
                
                if (cycle.isDependsOk())
                {
                    DEP_RELA_OK_CYCLES.add(cycle);
                }
            }
        }
    }
    
    private void reConCyclePreRels(Cycle cycle)
    {
        if (null == cycle)
        {
            return;
        }
        
        //移除原有的反向依赖
        removeDeppingsRelation(cycle);
        
        //构造反向依赖(前驱前驱)
        conDeppingCycles(cycle);
    }
    
    /**
     * 重新构建任务依赖关系
     */
    private void reConCycleSubRels(Cycle cycle)
    {
        if (null == cycle)
        {
            return;
        }
        
        try
        {
            //删除原有依赖关系
            removeDependsRelation(cycle);
            
            //重置状态
            cycle.setMoreSubRs(false);
            
            //构造依赖关系(前驱后继)
            Long taskId = cycle.getTaskRS().getTaskId();
            String cycleId = cycle.getTaskRS().getCycleId();
            
            List<CycleRelation> subCycleRs = cycle.getSubCycleRs();
            //需要创建后继队列
            if (null == subCycleRs)
            {
                subCycleRs = new ArrayList<CycleRelation>();
                cycle.setSubCycleRs(subCycleRs);
            }
            
            //获取周期任务的所有周期依赖关系
            List<CycleDependRelation> cycleDepRLst = getAllCycleDepRs(taskId, cycleId, false);
            Map<String, Cycle> cycleMaps;
            Long dTaskId;
            String dCycleId;
            Cycle subCycle;
            List<CycleRelation> preCycleRs;
            CycleRelation cycleR;
            
            for (CycleDependRelation cDepRela : cycleDepRLst)
            {
                dTaskId = cDepRela.getDependTaskId();
                dCycleId = cDepRela.getDependCycleId();
                cycleMaps = CYCLES.get(dTaskId);
                if (null == cycleMaps)
                {
                    //任务的周期可能不存在, 也可能已完成
                    cycleMaps = new HashMap<String, Cycle>();
                    CYCLES.put(dTaskId, cycleMaps);
                }
                
                subCycle = cycleMaps.get(dCycleId);
                
                if (null == subCycle && !cycle.isConNExistSubCycle())
                {
                    //只要有一个依赖关系没建立
                    cycle.setMoreSubRs(true);
                    continue;
                }
                else if (null == subCycle && cycle.isConNExistSubCycle())
                {
                    createCycle(dTaskId, dCycleId, true);
                    //依赖关系已经创建
                    continue;
                }
                
                //存在，不存在则忽略
                //需要创建前驱队列
                preCycleRs = subCycle.getPreCycleRs();
                if (null == preCycleRs)
                {
                    preCycleRs = new ArrayList<CycleRelation>();
                    subCycle.setPreCycleRs(preCycleRs);
                }
                
                cycleR = new CycleRelation(cycle, subCycle, cDepRela.isIgnoreError());
                
                subCycleRs.add(cycleR);
                preCycleRs.add(cycleR);
            }
            
            replaceInstanceRel(cycle);
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
        }
    }
    
    /**
     * 获取任务周期的全部周期依赖关系
     * @throws CException 统一封装的异常
     */
    private List<CycleDependRelation> getAllCycleDepRs(Long taskId, String cycleId, boolean getOne)
        throws CException
    {
        List<CycleDependRelation> cycleDepRLst = new ArrayList<CycleDependRelation>();
        //一次获取全部的依赖任务
        Task t = taskManage.getTaskDepends(taskId);
        
        TaskEntity task = t.getTaskEntity();
        int cycleType = CycleType.toCycleType(task.getCycleType());
        boolean seqDepend = task.getCycleDependFlag();
        int cycleLength = task.getCycleLength();
        //如果是顺序依赖，需要依赖于前一个周期。
        if (seqDepend)
        {
            //当前周期的起始时间
            Date currentDate = TccUtil.covCycleID2Date(cycleId);
            
            //依赖任务的前一个周期
            Date priviousDate = TccUtil.roll2CycleStart(currentDate, cycleType, -cycleLength);
            String priviousCycleId = TccUtil.covDate2CycleID(priviousDate);
            
            //最大开始时间
            Date maxStartTime = task.getStartTime();
            if (null == maxStartTime || maxStartTime.before(TccConfig.getBenchDate()))
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
                    
                    //仅获取一个依赖
                    if (getOne && cycleDepRLst.size() > 0)
                    {
                        return cycleDepRLst;
                    }
                }
            }
        }
        
        if (null != t && null != t.getSubTaskRs())
        {
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
            
            TaskEntity taskEntity;
            for (TaskRelation curTR : t.getSubTaskRs())
            {
                //当前的任务实例
                taskEntity = curTR.getDstTask().getTaskEntity();
                depTaskId = taskEntity.getTaskId();
                depCycleType = CycleType.toCycleType(taskEntity.getCycleType());
                
                //周期大小比较
                compareValue = cycleType * cycleLength - depCycleType * taskEntity.getCycleLength();
                
                //如果大周期类型依赖于小周期类型并且是全周期依赖
                if (compareValue > 0)
                {
                    //大周期的当前周期的起始时间
                    Date currentDate = TccUtil.covCycleID2Date(cycleId);
                    //大周期的前一个周期的起始时间
                    Date priviousDate;
                    
                    priviousDate = currentDate;
                    //大周期的后一个周期的起始时间
                    currentDate = TccUtil.roll2CycleStart(currentDate, cycleType, cycleLength);
                    
                    //做一些微量的时间偏移，以便排除掉最后一个小周期
                    currentDate = TccUtil.tuneTiny(currentDate, false);
                    
                    //获取上一个大周期内的全部小周期ID
                    List<String> dependCycleIds =
                        TccUtil.generateCycleIDs(priviousDate,
                            currentDate,
                            CycleType.toCycleType(taskEntity.getCycleType()),
                            taskEntity.getCycleLength());
                    
                    //允许最早开始的时间
                    Date earliestStartTime = taskEntity.getStartTime();
                    if (null == earliestStartTime || earliestStartTime.before(TccConfig.getBenchDate()))
                    {
                        earliestStartTime = TccConfig.getBenchDate();
                    }
                    
                    //如果是全周期依赖
                    if (curTR.isFullCycleDepend())
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
                                bIgnoreDependError = curTR.isIgnoreError();
                                cycleDepR.setIgnoreError(bIgnoreDependError);
                                if (!cycleDepRLst.contains(cycleDepR))
                                {
                                    cycleDepRLst.add(cycleDepR);
                                    
                                    //仅获取一个依赖
                                    if (getOne && cycleDepRLst.size() > 0)
                                    {
                                        return cycleDepRLst;
                                    }
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
                                bIgnoreDependError = curTR.isIgnoreError();
                                cycleDepR.setIgnoreError(bIgnoreDependError);
                                if (!cycleDepRLst.contains(cycleDepR))
                                {
                                    cycleDepRLst.add(cycleDepR);
                                    
                                    //仅获取一个依赖
                                    if (getOne && cycleDepRLst.size() > 0)
                                    {
                                        return cycleDepRLst;
                                    }
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
                    String priviousCycleId =
                        TccUtil.getPrevCycleId(currentDate,
                            cycleType,
                            cycleLength,
                            depCycleType,
                            taskEntity.getCycleLength());
                    
                    //在前周期依赖的基础上往后推一个周期
                    priviousCycleId =
                        TccUtil.covDate2CycleID(TccUtil.roll2CycleStart(TccUtil.covCycleID2Date(priviousCycleId),
                            depCycleType,
                            taskEntity.getCycleLength()));
                    
                    //允许最早开始的时间
                    Date earliestStartTime = taskEntity.getStartTime();
                    if (null == earliestStartTime || earliestStartTime.before(TccConfig.getBenchDate()))
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
                        bIgnoreDependError = curTR.isIgnoreError();
                        cycleDepR.setIgnoreError(bIgnoreDependError);
                        if (!cycleDepRLst.contains(cycleDepR))
                        {
                            cycleDepRLst.add(cycleDepR);
                            
                            //仅获取一个依赖
                            if (getOne && cycleDepRLst.size() > 0)
                            {
                                return cycleDepRLst;
                            }
                        }
                    }
                }
            }
        }
        
        return cycleDepRLst;
    }
    
    //从依赖taskRS的所有周期中找到依赖关系满足的周期列表
    private void findNewDependsOk(Cycle cycle)
    {
        if (null == cycle)
        {
            return;
        }
        
        List<CycleRelation> preCycleRs = cycle.getPreCycleRs();
        
        if (null == preCycleRs || preCycleRs.isEmpty())
        {
            return;
        }
        
        Cycle srcCycle;
        Long taskId;
        
        //防止内部修改集合
        List<CycleRelation> preCycleRsCpy = new ArrayList<CycleRelation>();
        preCycleRsCpy.addAll(preCycleRs);
        for (CycleRelation cycleReation : preCycleRsCpy)
        {
            srcCycle = cycleReation.getSrcCycle();
            taskId = srcCycle.getTaskRS().getTaskId();
            
            if (srcCycle.isMoreSubRs())
            {
                LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because moreSubRs is true.",
                    srcCycle.getTaskRS().getTaskId(),
                    srcCycle.getTaskRS().getCycleId());
                //说明依赖关系不完整,对不完整的依赖关系进行重建
                srcCycle.setConNExistSubCycle(true);
                reConCycleSubRels(srcCycle);
            }
            
            //可参与调度调度
            if (srcCycle.canSchedule() && taskManage.isNormalStartAndNoDependsError(srcCycle.getTaskRS().getTaskId()))
            {
                if (srcCycle.isMoreSubRs())
                {
                    LOGGER.info("need reConstruct depends of cycle[taskId={},cycleId={}], because moreSubRs is true.",
                        taskId,
                        cycle.getTaskRS().getCycleId());
                    //说明依赖关系不完整,对不完整的依赖关系进行重建
                    srcCycle.setConNExistSubCycle(true);
                    reConCycleSubRels(srcCycle);
                }
                
                //依赖全部ok
                if (srcCycle.isDependsOk())
                {
                    DEP_RELA_OK_CYCLES.add(srcCycle);
                }
            }
        }
    }
    
    /**
     * 移除周期正向依赖关系
     */
    private List<CycleRelation> removeDependsRelation(Cycle cycle)
    {
        if (null == cycle)
        {
            return null;
        }
        
        List<CycleRelation> subCycleRs = cycle.getSubCycleRs();
        if (null == subCycleRs || subCycleRs.isEmpty())
        {
            return null;
        }
        
        List<CycleRelation> preCycleRs;
        for (CycleRelation cycleRelation : subCycleRs)
        {
            preCycleRs = cycleRelation.getDstCycle().getPreCycleRs();
            if (null != preCycleRs)
            {
                preCycleRs.remove(cycleRelation);
            }
        }
        
        //删除直接依赖
        cycle.setSubCycleRs(null);
        return subCycleRs;
    }
    
    /**
     * 移除周期反向依赖关系
     */
    private List<CycleRelation> removeDeppingsRelation(Cycle cycle)
    {
        if (null == cycle)
        {
            return null;
        }
        
        List<CycleRelation> preCycleRs = cycle.getPreCycleRs();
        if (null == preCycleRs)
        {
            return null;
        }
        
        List<CycleRelation> subCycleRs;
        for (CycleRelation cycleRelation : preCycleRs)
        {
            subCycleRs = cycleRelation.getSrcCycle().getSubCycleRs();
            if (null != subCycleRs)
            {
                subCycleRs.remove(cycleRelation);
            }
        }
        
        //删除直接依赖
        cycle.setPreCycleRs(null);
        
        return preCycleRs;
    }
    
    /**
     * 删除周期
     */
    private void deleteCycle(TaskRunningStateEntity taskRS)
    {
        Map<String, Cycle> cycleMaps = CYCLES.get(taskRS.getTaskId());
        if (null == cycleMaps)
        {
            return;
        }
        
        //周期存在
        Cycle cycle = cycleMaps.get(taskRS.getCycleId());
        if (null == cycle)
        {
            return;
        }
        
        //移除依赖
        removeDependsRelation(cycle);
        
        List<CycleRelation> preCycleRs = cycle.getPreCycleRs();
        if (null == preCycleRs || preCycleRs.isEmpty())
        {
            //可以彻底删除
            cycleMaps.remove(taskRS.getCycleId());
        }
        else
        {
            //被其它周期依赖，则不删除仅修改状态
            cycle.getTaskRS().setState(RunningState.NOTINIT);
        }
        
        //删除实例关系
        deleteInstanceRel(cycle);
        
        DEP_RELA_OK_CYCLES.remove(cycle);
    }
    
    private void addCycle(List<TaskRunningStateEntity> taskRSs)
    {
        Set<Cycle> cycles = new HashSet<Cycle>();
        Cycle cycle;
        for (TaskRunningStateEntity taskRS : taskRSs)
        {
            cycle = addCycle(taskRS);
            if (null != cycle)
            {
                cycles.add(cycle);
            }
        }
        
        //等依赖关系和状态修改完成后，再寻找可以调度的任务周期
        reChoosedDependsOkCycles(cycles);
    }
    
    private boolean afterLeftRange(Long taskId, String cycleId)
    {
        TaskMinMaxCycle minMaxCycle = CYCLE_RANGES.get(taskId);
        if (null == minMaxCycle)
        {
            TaskEntity taskE = taskManage.getTaskEntity(taskId);
            if (null == taskE)
            {
                LOGGER.error("task[taskId={}] not exist! cycleId is {}.", taskId, cycleId);
                return false;
            }
            
            minMaxCycle = new TaskMinMaxCycle();
            String minCycleId = TccUtil.getMinCycleId(taskE.getStartTime());
            
            minMaxCycle.setTaskId(taskId);
            minMaxCycle.setMinCycleId(minCycleId);
            
            CYCLE_RANGES.put(taskId, minMaxCycle);
        }
        
        return cycleId.compareTo(minMaxCycle.getMinCycleId()) >= 0;
    }
    
    /**
     * 新增或者更新周期
     */
    private Cycle addCycle(TaskRunningStateEntity taskRS)
    {
        Long taskId = taskRS.getTaskId();
        String cycleId = taskRS.getCycleId();
        
        if (!afterLeftRange(taskId, cycleId))
        {
            LOGGER.warn("ignore taskRS[{}], because it is less than left range!.", TccUtil.truncatEncode(taskRS));
            return null;
        }
        
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            //任务的周期可能不存在, 也可能已完成
            cycleMaps = new HashMap<String, Cycle>();
            CYCLES.put(taskId, cycleMaps);
        }
        
        Cycle cycle = cycleMaps.get(cycleId);
        
        //周期如果存在、则删除依赖关系
        if (null == cycle)
        {
            TaskRunningStateEntity taskRSNew = new TaskRunningStateEntity();
            taskRSNew.setTaskId(taskId);
            taskRSNew.setCycleId(cycleId);
            cycle = new Cycle(taskRSNew);
            cycle.setOtherDaySchedule(false);
            cycleMaps.put(cycleId, cycle);
        }
        
        int state = taskRS.getState();
        //重新设置状态
        cycle.getTaskRS().setState(state);
        //设置预期执行时间
        cycle.setExpectExecuteDate(getExpectedExecuteDate(taskId, cycleId, state));
        
        //构建依赖关系
        reConCyclePreRels(cycle);
        reConCycleSubRels(cycle);
        
        return cycle;
    }
    
    /**
     * 当周期任务结束时触发
     * @param taskRS 任务周期
     * @throws CException 
     */
    private void stateChanged(List<TaskRunningStateEntity> taskRSs)
    {
        if (null == taskRSs || taskRSs.isEmpty())
        {
            return;
        }
        
        Set<Cycle> reCheckCycles = new HashSet<Cycle>();
        Set<Cycle> findDepends = new HashSet<Cycle>();
        Cycle cycle;
        for (TaskRunningStateEntity taskRS : taskRSs)
        {
            //修改周期状态
            int dstState = taskRS.getState();
            
            //可能是区间内的成功状态
            Long taskId = taskRS.getTaskId();
            String cycleId = taskRS.getCycleId();
            cycle = createCycle(taskId, cycleId, false);
            
            //周期不存在直接返回
            int srcState = cycle.getTaskRS().getState();
            if (RunningState.NOTINIT == srcState)
            {
                return;
            }
            
            //状态变迁
            //修改状态
            if (RunningState.NOBATCH == dstState || RunningState.ERROR == dstState || RunningState.TIMEOUT == dstState
                || RunningState.SUCCESS == dstState || RunningState.VSUCCESS == dstState)
            {
                cycle.getTaskRS().setState(dstState);
                
                //不可调度状态，移除相应周期
                DEP_RELA_OK_CYCLES.remove(cycle);
                
                //找到可能可以运行的周期
                findDepends.add(cycle);
            }
            else if (RunningState.REQDELETE == dstState)
            {
                cycle.getTaskRS().setState(dstState);
                
                //不可调度状态，移除相应周期
                DEP_RELA_OK_CYCLES.remove(cycle);
                deleteCycle(taskRS);
                
                //删除依赖关系
                deleteInstanceRel(cycle);
            }
            else if (RunningState.STOP == dstState)
            {
                //不可调度状态，移除相应周期
                DEP_RELA_OK_CYCLES.remove(cycle);
            }
            else if (RunningState.INIT == dstState || RunningState.WAIT_NEXT_NODE_EXE == dstState)
            {
                cycle.getTaskRS().setState(dstState);
                
                //不可调度状态，移除相应周期
                DEP_RELA_OK_CYCLES.remove(cycle);
                
                reCheckCycles.add(cycle);
                
                //重新修改预期执行时间
                cycle.setExpectExecuteDate(getExpectedExecuteDate(taskId, cycleId, dstState));
                replaceInstanceRel(cycle);
            }
            else if (RunningState.WAITTINGRUN == dstState || RunningState.RUNNING == dstState
                || RunningState.START == dstState)
            {
                cycle.getTaskRS().setState(dstState);
            }
            
        }
        
        //对当前周期进行一次判断是否可以重新调度
        reChoosedDependsOkCycles(reCheckCycles);
        //等依赖关系和状态修改完成后，再寻找可以调度的任务周期
        for (Cycle currCycle : findDepends)
        {
            findNewDependsOk(currCycle);
        }
    }
    
    private void replaceInstanceRel(Cycle cycle)
    {
        if (null == cycle)
        {
            return;
        }
        
        //实例运行完毕，依赖关系就不要修改了
        int state = cycle.getTaskRS().getState();
        if (RunningState.INIT == state || RunningState.START == state || RunningState.WAIT_NEXT_NODE_EXE == state
            || RunningState.WAITTINGRUN == state || RunningState.RUNNING == state || RunningState.NOTINIT == state)
        {
            InstanceRelationEntity instanceRel = cycle.cov2InstanceRelation(startDate);
            List<InstanceRelationEntity> instanceRels = new ArrayList<InstanceRelationEntity>();
            instanceRels.add(instanceRel);
            tccDao.addInstanceRels(instanceRels);
        }
    }
    
    private void replaceAll(List<InstanceRelationEntity> instanceRels)
    {
        if (null != instanceRels && !instanceRels.isEmpty())
        {
            tccDao.addInstanceRels(instanceRels);
        }
    }
    
    private void deleteInstanceRel(Cycle cycle)
    {
        if (null != cycle)
        {
            InstanceRelationEntity instanceRel = cycle.cov2InstanceRelation(startDate);
            List<InstanceRelationEntity> instanceRels = new ArrayList<InstanceRelationEntity>();
            instanceRels.add(instanceRel);
            tccDao.deleteInstanceRel(instanceRel);
        }
    }
    
    /**
     * 重做任务时导致的状态改变
     * @param taskRS 任务周期
     * @throws CException 
     */
    private void redoTasksStateChanged(List<TaskEntity> tasks)
    {
        if (null != tasks)
        {
            List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
            Map<String, Cycle> cycleMaps;
            Long taskId;
            Date redoS;
            Date redoE;
            Date cycleDate;
            Cycle cycle;
            for (TaskEntity task : tasks)
            {
                redoS = task.getRedoStartTime();
                redoE = task.getRedoEndTime();
                taskId = task.getTaskId();
                if (null == redoS || null == redoE)
                {
                    continue;
                }
                
                cycleMaps = CYCLES.get(taskId);
                if (null == cycleMaps)
                {
                    continue;
                }
                
                for (Entry<String, Cycle> cycleIter : cycleMaps.entrySet())
                {
                    cycle = cycleIter.getValue();
                    try
                    {
                        cycleDate = TccUtil.covCycleID2Date(cycleIter.getKey());
                        if (!cycleDate.before(redoS) && !cycleDate.after(redoE))
                        {
                            taskRSs.add(cycle.getTaskRS());
                        }
                    }
                    catch (CException e)
                    {
                        LOGGER.error("", e);
                    }
                }
            }
            
            stateChanged(taskRSs);
        }
    }
    
    /**
     * 集成重做关联修改状态
     * @param taskRS 任务周期
     * @throws CException 
     */
    private void integratedTasksStateChanged(TaskCycleParam taskCycleParam)
    {
        if (null == taskCycleParam || null == taskCycleParam.getMinCycleId() || null == taskCycleParam.getMaxCycleId()
            || null == taskCycleParam.getTaskId())
        {
            return;
        }
        
        List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
        Long taskId = taskCycleParam.getTaskId();
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            return;
        }
        
        Cycle cycle;
        String cycleId;
        for (Entry<String, Cycle> cycleIter : cycleMaps.entrySet())
        {
            cycle = cycleIter.getValue();
            cycleId = cycleIter.getKey();
            
            //处于最大最小周期之间
            if (cycleId.compareTo(taskCycleParam.getMinCycleId()) >= 0
                && cycleId.compareTo(taskCycleParam.getMaxCycleId()) <= 0)
            {
                taskRSs.add(cycle.getTaskRS());
            }
        }
        
        stateChanged(taskRSs);
    }
    
    private void startTask(Long taskId)
    {
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            return;
        }
        
        //等依赖关系和状态修改完成后，再寻找可以调度的任务周期
        reChoosedDependsOkCycles(cycleMaps.values());
    }
    
    private void stopTask(Long taskId)
    {
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            return;
        }
        
        for (Entry<String, Cycle> cycleIter : cycleMaps.entrySet())
        {
            //从依赖满足周期集合中移除已停止的任务周期
            DEP_RELA_OK_CYCLES.remove(cycleIter.getValue());
        }
    }
    
    private void deleteTask(Long taskId)
    {
        //从周期统计中移除任务
        CYCLE_RANGES.remove(taskId);
        
        //从周期中移除任务
        Map<String, Cycle> cycleMaps = CYCLES.remove(taskId);
        if (null == cycleMaps)
        {
            return;
        }
        
        for (Entry<String, Cycle> cycleIter : cycleMaps.entrySet())
        {
            //移除正向依赖关系
            removeDependsRelation(cycleIter.getValue());
            
            //移除反向依赖关系
            removeDeppingsRelation(cycleIter.getValue());
            
            DEP_RELA_OK_CYCLES.remove(cycleIter.getValue());
            
            //删除实例关系
            deleteInstanceRel(cycleIter.getValue());
        }
    }
    
    private void reloadCycles(Long taskId)
    {
        try
        {
            //删除任务的所有周期
            deleteTask(taskId);
            
            //加载任务
            loadCycles(taskId);
            
            //重构所有反向依赖任务的正向周期依赖关系
            List<TaskEntity> tasks = reConDependRelaOfDeppingTask(taskId);
            
            //重新选择
            tasks.add(new TaskEntity(taskId));
            for (TaskEntity taskE : tasks)
            {
                reChoosedDependsOkCycles(taskE.getTaskId());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("taskId is {}.", taskId, e);
        }
        
    }
    
    /**
     * 重构taskId的所有反向依赖任务的正向周期依赖关系
     * @return 反向依赖任务列表
     * @param taskId 任务Id
     */
    private List<TaskEntity> reConDependRelaOfDeppingTask(Long taskId)
    {
        //删除反向依赖任务的依赖关系
        List<TaskEntity> tasks = taskManage.getDeppingTasks(taskId);
        Map<String, Cycle> cycleMaps;
        for (TaskEntity task : tasks)
        {
            cycleMaps = CYCLES.get(task.getTaskId());
            if (null == cycleMaps)
            {
                continue;
            }
            
            List<Cycle> cycles = new ArrayList<Cycle>();
            cycles.addAll(cycleMaps.values());
            
            for (Cycle cycle : cycles)
            {
                try
                {
                    reConCycleSubRels(cycle);
                }
                catch (Exception e)
                {
                    LOGGER.error("conCycleRelation failed! ignore it!", e);
                }
            }
        }
        
        return tasks;
    }
    
    private void loadCycles(Long taskId)
        throws CException
    {
        TaskEntity taskE = taskManage.getTaskEntity(taskId);
        if (null == taskE)
        {
            LOGGER.warn("task[taskId={}] doesn't exist!", taskId);
            return;
        }
        
        String minCycleId = TccUtil.getMinCycleId(taskE.getStartTime());
        //构造任务周期的统计信息
        TaskMinMaxCycle minMaxCycle = tccDao.getTaskMinMaxCycleId(taskId);
        if (null == minMaxCycle)
        {
            minMaxCycle = new TaskMinMaxCycle();
            minMaxCycle.setTaskId(taskId);
        }
        
        if (null == minMaxCycle.getMinCycleId() || minCycleId.compareToIgnoreCase(minMaxCycle.getMinCycleId()) > 0)
        {
            //修正最小周期边界
            minMaxCycle.setMinCycleId(minCycleId);
        }
        CYCLE_RANGES.put(taskId, minMaxCycle);
        
        //获取所有状态为非sucess状态的任务周期
        taskId = taskE.getTaskId();
        
        //仅加载开始时间和起始时间之后的周期
        List<TaskRunningStateEntity> taskRSs = tccDao.getNSTaskRSs(taskId, minMaxCycle.getMinCycleId(), null);
        
        Map<String, Cycle> cycleMaps;
        cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            cycleMaps = new HashMap<String, Cycle>();
            CYCLES.put(taskId, cycleMaps);
        }
        
        Cycle cycle;
        for (TaskRunningStateEntity taskRS : taskRSs)
        {
            cycle = new Cycle(taskRS, true);
            cycle.setExpectExecuteDate(getExpectedExecuteDate(taskRS.getTaskId(),
                taskRS.getCycleId(),
                taskRS.getState()));
            cycleMaps.put(taskRS.getCycleId(), cycle);
        }
        LOGGER.info("load {} taskRS of Task[taskId={}].", taskRSs.size(), taskId);
        
        //遍历所有的周期，根据任务依赖构建周期依赖关系
        for (TaskRunningStateEntity taskRS : taskRSs)
        {
            try
            {
                reConCycleSubRels(CYCLES.get(taskRS.getTaskId()).get(taskRS.getCycleId()));
            }
            catch (Exception e)
            {
                LOGGER.error("conCycleRelation failed! ignore it!", e);
            }
        }
        
        List<InstanceRelationEntity> instanceRels = new ArrayList<InstanceRelationEntity>();
        //修改实例关系
        for (Cycle cycleRep : cycleMaps.values())
        {
            instanceRels.add(cycleRep.cov2InstanceRelation(startDate));
        }
        replaceAll(instanceRels);
    }
    
    private void addTask(Long taskId)
    {
        reloadCycles(taskId);
    }
    
    private void taskCycleTypeLengthChanged(Long taskId)
    {
        reloadCycles(taskId);
    }
    
    /**
     * 任务依赖关系改变
     * @param taskE 任务
     */
    private void taskDependRelaChanged(Long taskId)
    {
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            LOGGER.error("no any cycle of Task[taskId={}].", taskId);
            return;
        }
        
        //reConCycleRelation会修改cycleMaps集合
        List<Cycle> cycles = new ArrayList<Cycle>();
        cycles.addAll(cycleMaps.values());
        //全部周期的依赖关系重建
        for (Cycle cycle : cycles)
        {
            try
            {
                reConCycleSubRels(cycle);
            }
            catch (Exception e)
            {
                LOGGER.error("reCconCycleRelation failed! ignore it!", e);
            }
        }
        
        //修改实例关系
        for (Cycle cycle : cycles)
        {
            replaceInstanceRel(cycle);
        }
        
        reChoosedDependsOkCycles(cycleMaps.values());
    }
    
    private void taskStartTimeChanged(Long taskId)
    {
        try
        {
            TaskEntity taskE = taskManage.getTaskEntity(taskId);
            String newMinCycleId = TccUtil.getMinCycleId(taskE.getStartTime());
            
            //从周期统计中移除任务
            TaskMinMaxCycle minMaxCycle = CYCLE_RANGES.get(taskId);
            
            String oldMinCycleId = minMaxCycle.getMinCycleId();
            int compareValue = newMinCycleId.compareToIgnoreCase(oldMinCycleId);
            if (compareValue > 0)
            {
                //修正最小周期边界
                minMaxCycle.setMinCycleId(newMinCycleId);
                
                //移除左边的依赖关系，并删除周期
                Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
                if (null == cycleMaps)
                {
                    return;
                }
                
                Iterator<Entry<String, Cycle>> cycleIter = cycleMaps.entrySet().iterator();
                Set<Cycle> disCycles = new HashSet<Cycle>();
                List<Cycle> deletedCycles = new ArrayList<Cycle>();
                List<CycleRelation> preRels;
                Entry<String, Cycle> cycleEntry;
                String cycleId;
                Cycle cycle;
                while (cycleIter.hasNext())
                {
                    cycleEntry = cycleIter.next();
                    cycle = cycleEntry.getValue();
                    cycleId = cycleEntry.getKey();
                    if (cycleId.compareToIgnoreCase(newMinCycleId) < 0)
                    {
                        //移除依赖关系
                        removeDependsRelation(cycle);
                        
                        deletedCycles.add(cycle);
                        
                        //移除周期
                        cycleIter.remove();
                        
                        //移除
                        DEP_RELA_OK_CYCLES.remove(cycle);
                        
                        //因为周期已经移除，反向依赖的周期不需要依赖它【不能忽略】
                        preRels = removeDeppingsRelation(cycle);
                        
                        //将受影响的周期记录到disCycles中
                        if (null != preRels)
                        {
                            for (CycleRelation cycleRel : preRels)
                            {
                                disCycles.add(cycleRel.getSrcCycle());
                            }
                        }
                    }
                }
                
                //移除已经删除的周期
                for (Cycle cycleDel : deletedCycles)
                {
                    disCycles.remove(cycleDel);
                }
                
                //删除实例关系
                for (Cycle cycleDel : deletedCycles)
                {
                    deleteInstanceRel(cycleDel);
                }
                
                //修改实例关系
                for (Cycle cycleRep : disCycles)
                {
                    replaceInstanceRel(cycleRep);
                }
                
                reChoosedDependsOkCycles(disCycles);
            }
            else if (compareValue < 0)
            {
                //修正最小周期边界
                //minMaxCycle.setMinCycleId(newMinCycleId);
                //构造任务周期的统计信息
                minMaxCycle = tccDao.getTaskMinMaxCycleId(taskId);
                if (null == minMaxCycle)
                {
                    minMaxCycle = new TaskMinMaxCycle();
                    minMaxCycle.setTaskId(taskId);
                }
                
                if (null == minMaxCycle.getMinCycleId()
                    || newMinCycleId.compareToIgnoreCase(minMaxCycle.getMinCycleId()) > 0)
                {
                    //修正最小周期边界
                    minMaxCycle.setMinCycleId(newMinCycleId);
                }
                CYCLE_RANGES.put(taskId, minMaxCycle);
                //需要补充新的周期进来、对新周期以及反向依赖周期进行重新构建依赖关系
                
                List<TaskRunningStateEntity> taskAllRSs = tccDao.getNSTaskRSs(taskId, newMinCycleId, oldMinCycleId);
                Set<Cycle> disCycles = new HashSet<Cycle>();
                Map<String, Cycle> cycleMaps;
                Cycle cycle;
                for (TaskRunningStateEntity taskRS : taskAllRSs)
                {
                    cycleMaps = CYCLES.get(taskRS.getTaskId());
                    if (null == cycleMaps)
                    {
                        cycleMaps = new HashMap<String, Cycle>();
                        CYCLES.put(taskRS.getTaskId(), cycleMaps);
                    }
                    
                    cycle = new Cycle(taskRS);
                    cycleMaps.put(taskRS.getCycleId(), cycle);
                    
                    disCycles.add(cycle);
                    
                    conDeppingCycles(cycle);
                }
                
                //遍历所有的周期，根据任务依赖构建周期依赖关系
                for (Cycle disCycle : disCycles)
                {
                    try
                    {
                        reConCycleSubRels(disCycle);
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("reCconCycleRelation failed! ignore it!", e);
                    }
                }
                
                //修改实例关系
                for (Cycle cycleRep : disCycles)
                {
                    replaceInstanceRel(cycleRep);
                }
                
                reChoosedDependsOkCycles(disCycles);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("taskId is {}.", taskId, e);
        }
    }
    
    /**
     * 构造反向依赖关系
     * @param currCycle 周期
     */
    private void conDeppingCycles(Cycle currCycle)
    {
        if (null == currCycle)
        {
            return;
        }
        
        try
        {
            Long taskId = currCycle.getTaskRS().getTaskId();
            String cycleId = currCycle.getTaskRS().getCycleId();
            TaskEntity taskE = taskManage.getTaskEntity(taskId);
            List<TaskEntity> tasks = taskManage.getDeppingTasks(taskId);
            
            //重置状态
            currCycle.setMorePreRs(false);
            
            //因为周期已经存在，反向依赖的周期可能需要重新依赖它【不能忽略】
            List<CycleDependRelation> cDeppingRLst = TccUtil.getDeppingCycleRs(taskE, tasks, cycleId);
            
            if (null == cDeppingRLst)
            {
                return;
            }
            
            List<CycleRelation> preCycleRs = currCycle.getPreCycleRs();
            if (null == preCycleRs)
            {
                preCycleRs = new ArrayList<CycleRelation>();
                currCycle.setPreCycleRs(preCycleRs);
            }
            
            CycleRelation cycleRel;
            List<CycleRelation> subCycleRs;
            Long dTaskId;
            String dCycleId;
            for (CycleDependRelation cDeppingRela : cDeppingRLst)
            {
                dTaskId = cDeppingRela.getDependTaskId();
                dCycleId = cDeppingRela.getDependCycleId();
                Cycle cycle = getCycle(dTaskId, dCycleId);
                //周期还未构建
                if (null == cycle)
                {
                    currCycle.setMorePreRs(true);
                    continue;
                }
                
                subCycleRs = cycle.getSubCycleRs();
                if (null == subCycleRs)
                {
                    subCycleRs = new ArrayList<CycleRelation>();
                    cycle.setSubCycleRs(subCycleRs);
                }
                cycleRel = new CycleRelation(cycle, currCycle, cDeppingRela.isIgnoreError());
                preCycleRs.add(cycleRel);
                subCycleRs.add(cycleRel);
                
                //修改了正向依赖，更新
                markMoreSubRs(cycle);
                replaceInstanceRel(cycle);
            }
        }
        catch (Exception e)
        {
            //获取依赖关系错误
            LOGGER.error("needReConDepRelaCycles failed!", e);
        }
    }
    
    /**
     * 标记周期是否还有更多正向的依赖
     * @param cycle 周期
     */
    private void markMoreSubRs(Cycle cycle)
    {
        
        try
        {
            Long taskId = cycle.getTaskRS().getTaskId();
            String cycleId = cycle.getTaskRS().getCycleId();
            List<CycleDependRelation> cycleDepRLst = getAllCycleDepRs(taskId, cycleId, false);
            List<CycleRelation> subCycleRels = cycle.getSubCycleRs();
            if (null == cycleDepRLst && null == subCycleRels)
            {
                cycle.setMoreSubRs(false);
            }
            else if (null != cycleDepRLst && null != subCycleRels && cycleDepRLst.size() == subCycleRels.size())
            {
                //正向依赖的数目相同
                cycle.setMoreSubRs(false);
            }
            else
            {
                cycle.setMoreSubRs(true);
            }
        }
        catch (CException e)
        {
            LOGGER.error("markMoreSubRs failed!", e);
        }
        
    }
    
    /**
     * 处理事件【可以使用事件队列】
     * @param event 事件
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void process(Event event)
    {
        //TODO 直接返回
        int a = 1;
        if (a == 1)
        {
            return;
        }
        
        if (null != event)
        {
            //事件类型
            int eventType = event.getType();
            if (EventType.CHANGE_TASKRS_STATE == eventType)
            {
                if (event.getData() instanceof TaskRunningStateEntity)
                {
                    List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
                    taskRSs.add((TaskRunningStateEntity)event.getData());
                    stateChanged(taskRSs);
                }
            }
            else if (EventType.CHANGE_MULTI_TASKRS_STATE == eventType)
            {
                if (event.getData() instanceof List)
                {
                    stateChanged((List<TaskRunningStateEntity>)(event.getData()));
                }
            }
            else if (EventType.RANGE_REINIT_TASKRS_STATE == eventType)
            {
                if (event.getData() instanceof List)
                {
                    redoTasksStateChanged((List<TaskEntity>)(event.getData()));
                }
            }
            else if (EventType.INTEGRATION_CHANGE_TASKRS_STATE == eventType)
            {
                if (event.getData() instanceof TaskCycleParam)
                {
                    integratedTasksStateChanged((TaskCycleParam)(event.getData()));
                }
            }
            else if (EventType.ADD_TASKRS == eventType)
            {
                if (event.getData() instanceof TaskRunningStateEntity)
                {
                    List<TaskRunningStateEntity> taskRSs = new ArrayList<TaskRunningStateEntity>();
                    taskRSs.add((TaskRunningStateEntity)event.getData());
                    addCycle(taskRSs);
                }
            }
            else if (EventType.ADD_MULTI_TASKRS == eventType)
            {
                if (event.getData() instanceof List)
                {
                    addCycle((List<TaskRunningStateEntity>)event.getData());
                }
            }
            else if (EventType.DELETE_TASKRS == eventType)
            {
                if (event.getData() instanceof TaskRunningStateEntity)
                {
                    deleteCycle((TaskRunningStateEntity)event.getData());
                }
            }
            else if (EventType.TASK_START_TIME_CHANGED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    taskStartTimeChanged((Long)event.getData());
                }
            }
            else if (EventType.TASK_DEPEND_RELATION_CHANGED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    taskDependRelaChanged((Long)event.getData());
                }
            }
            else if (EventType.TASK_CYCLE_TYPE_LENGTH_CHANGED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    taskCycleTypeLengthChanged((Long)event.getData());
                }
            }
            else if (EventType.TASK_ADD_FINISHED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    addTask((Long)event.getData());
                }
            }
            else if (EventType.TASK_DELETE_FINISHED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    deleteTask((Long)event.getData());
                }
            }
            else if (EventType.TASK_START_FINISHED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    startTask((Long)event.getData());
                }
            }
            else if (EventType.TASK_STOP_FINISHED == eventType)
            {
                if (event.getData() instanceof Long)
                {
                    stopTask((Long)event.getData());
                }
            }
        }
    }
    
    /**
     * 保存周期依赖关系
     */
    public synchronized void saveCycleRelations()
    {
        //重建当天分区
        //tccDao.deleteInstanceRelPartition(startDate);
        tccDao.addInstanceRelPartition(startDate);
        
        //将周期全部保存到实例关系表中
        List<InstanceRelationEntity> instanceRels = new ArrayList<InstanceRelationEntity>(0);
        //遍历周期集合
        for (Entry<Long, Map<String, Cycle>> cyclesIter : CYCLES.entrySet())
        {
            for (Entry<String, Cycle> cycleIter : cyclesIter.getValue().entrySet())
            {
                Cycle cycle = cycleIter.getValue();
                //仅存储当天调度的周期
                if (!cycle.isOtherDaySchedule())
                {
                    instanceRels.add(cycle.cov2InstanceRelation(startDate));
                }
            }
        }
        
        tccDao.addInstanceRels(instanceRels);
    }
    
    /**
     * 获取调度图
     * @param insRelSearch 实例关系检索条件
     * @return 调度图
     */
    public Digraph getDigraph(InstanceRelationSearch insRelSearch)
    {
        //        Date scheduleDate = insRelSearch.getSchedleDate();
        //        if (scheduleDate.equals(startDate))
        //        {
        Set<Long> disTaskIds = new HashSet<Long>();
        List<Long> taskIds = insRelSearch.getTaskIds();
        if (null != taskIds)
        {
            for (Long taskId : taskIds)
            {
                disTaskIds.add(taskId);
            }
        }
        return cov2Higraph(disTaskIds);
        //        }
        //        else
        //        {
        //return loadDigraph(insRelSearch);
        //        }
    }
    
    private Digraph loadDigraph(InstanceRelationSearch insRelSearch)
    {
        Digraph digraph = new Digraph();
        digraph.setLinks(new ArrayList<DGLink>());
        //设置分层节点
        digraph.setHiNodes(new ArrayList<List<DGNode>>());
        
        Map<String, DGNode> idNodes = new HashMap<String, DGNode>();
        Map<String, DGNode> nodeLefts = new HashMap<String, DGNode>();
        DGNode dgNode;
        
        List<InstanceRelationEntity> instanceRels = tccDao.getInstanceRels(insRelSearch);
        Long taskId;
        String cycleId;
        for (InstanceRelationEntity instanceRelE : instanceRels)
        {
            taskId = instanceRelE.getTaskId();
            cycleId = instanceRelE.getCycleId();
            dgNode = new DGNode(getName(taskId, cycleId));
            dgNode.setTaskId(taskId);
            dgNode.setCycleId(cycleId);
            //dgNode.setMoreTLs(cycle.isMoreSubRs());
            //dgNode.setMoreFLs(cycle.isMorePreRs());
            //设置状态
            dgNode.setState(getState(taskId, cycleId));
            
            //过滤实例状态
            if (null != insRelSearch.getState() && dgNode.getState() != insRelSearch.getState())
            {
                continue;
            }
            
            //构造任务Id名字键值对
            idNodes.put(dgNode.getName(), dgNode);
            nodeLefts.put(dgNode.getName(), dgNode);
        }
        
        List<DGLink> links;
        List<DGNode> curNodes = new ArrayList<DGNode>();
        digraph.getHiNodes().add(curNodes);
        
        for (InstanceRelationEntity instanceRelE : instanceRels)
        {
            taskId = instanceRelE.getTaskId();
            cycleId = instanceRelE.getCycleId();
            dgNode = idNodes.get(getName(taskId, cycleId));
            if (null == dgNode)
            {
                continue;
            }
            
            //必需从启用的非停止任务中选择
            links = getDGLinks(dgNode, instanceRelE.getSubDependentList(), idNodes);
            if (null == links || links.isEmpty())
            {
                //根节点
                curNodes.add(dgNode);
                //移除
                nodeLefts.remove(dgNode.getName());
            }
            else
            {
                //构造有向线段
                digraph.getLinks().addAll(links);
            }
        }
        
        List<DGNode> nNodes;
        DGNode nodeTemp;
        int n = 0;
        //不为空
        while (!nodeLefts.isEmpty())
        {
            n++;
            if (curNodes.isEmpty())
            {
                LOGGER.error("cov2Higraph failed, cicle may be exist!,current layer is {}, left {} nodes",
                    n,
                    nodeLefts.size());
                break;
            }
            
            nNodes = new ArrayList<DGNode>();
            for (DGNode node : curNodes)
            {
                //寻找直接所有子女节点
                for (DGLink link : node.getFromLinks())
                {
                    nodeTemp = link.getTargetNode();
                    if (!nNodes.contains(nodeTemp))
                    {
                        nNodes.add(nodeTemp);
                    }
                }
            }
            
            //如果子女节点中有作为子女的节点,则移除
            DGNode[] nodesCp = new DGNode[nNodes.size()];
            nNodes.toArray(nodesCp);
            for (DGNode node : nodesCp)
            {
                for (DGLink link : node.getFromLinks())
                {
                    nodeTemp = link.getTargetNode();
                    if (nNodes.contains(nodeTemp))
                    {
                        //移除子女兄弟
                        nNodes.remove(nodeTemp);
                    }
                }
            }
            
            //如果有父节点在剩余集合中的，则移除
            nodesCp = new DGNode[nNodes.size()];
            nNodes.toArray(nodesCp);
            for (DGNode node : nodesCp)
            {
                for (DGLink link : node.getToLinks())
                {
                    nodeTemp = link.getSourceNode();
                    if (nodeLefts.containsKey(nodeTemp.getName()))
                    {
                        //移除当前节点
                        nNodes.remove(node);
                    }
                }
            }
            
            digraph.getHiNodes().add(nNodes);
            //从剩余节点集合中删除
            for (DGNode node : nNodes)
            {
                nodeLefts.remove(node.getName());
            }
            
            curNodes = nNodes;
        }
        
        List<DGLink> fromLinks;
        boolean moreFLs;
        //遍历所有节点，判断节点的fromlinks是否与反向依赖相同
        for (InstanceRelationEntity instanceRelE : instanceRels)
        {
            taskId = instanceRelE.getTaskId();
            cycleId = instanceRelE.getCycleId();
            dgNode = idNodes.get(getName(taskId, cycleId));
            if (null == dgNode)
            {
                continue;
            }
            
            fromLinks = dgNode.getFromLinks();
            moreFLs = false;
            try
            {
                TaskEntity taskE = taskManage.getTaskEntity(taskId);
                List<TaskEntity> tasks = taskManage.getDeppingTasks(taskId);
                
                //因为周期已经存在，反向依赖的周期可能需要重新依赖它【不能忽略】
                List<CycleDependRelation> cDeppingRLst = TccUtil.getDeppingCycleRs(taskE, tasks, cycleId);
                moreFLs = !containAll(fromLinks, cDeppingRLst);
                
            }
            catch (Exception e)
            {
                //获取依赖关系错误
                LOGGER.error("needReConDepRelaCycles failed!", e);
            }
            
            dgNode.setMoreFLs(moreFLs);
        }
        
        return digraph;
    }
    
    /**
     * fromLinks中是否全部包含cDeppingRLst中的任务周期ID
     * @param fromLinks 
     * @param cDeppingRLst
     * @return fromLinks中是否全部包含cDeppingRLst中的任务周期ID
     */
    private boolean containAll(List<DGLink> fromLinks, List<CycleDependRelation> cDeppingRLst)
    {
        if (TccUtil.noAnyElement(fromLinks) && TccUtil.noAnyElement(cDeppingRLst) || !TccUtil.noAnyElement(fromLinks)
            && TccUtil.noAnyElement(cDeppingRLst))
        {
            return true;
        }
        else if (TccUtil.noAnyElement(fromLinks) && !TccUtil.noAnyElement(cDeppingRLst))
        {
            return false;
        }
        else
        {
            boolean contain;
            DGNode dgNode;
            for (DGLink dgLink : fromLinks)
            {
                contain = false;
                dgNode = dgLink.getTargetNode();
                for (CycleDependRelation cycleDependRel : cDeppingRLst)
                {
                    if (dgNode.getTaskId().equals(cycleDependRel.getDependTaskId())
                        && dgNode.getCycleId().equals(cycleDependRel.getDependCycleId()))
                    {
                        contain = true;
                    }
                }
                
                if (false == contain)
                {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    /**
     * 转化为调度图
     * @param taskIds 任务Id集合
     * @return 调度图
     */
    private synchronized Digraph cov2Higraph(Set<Long> taskIds)
    {
        //taskManage.getVisibleTasks(vGroups);
        Digraph digraph = new Digraph();
        digraph.setLinks(new ArrayList<DGLink>());
        //设置分层节点
        digraph.setHiNodes(new ArrayList<List<DGNode>>());
        
        Map<String, DGNode> idNodes = new HashMap<String, DGNode>();
        Map<String, DGNode> nodeLefts = new HashMap<String, DGNode>();
        DGNode dgNode;
        
        Map<String, Cycle> cycleMaps;
        //遍历可见周期集合
        for (Long taskId : taskIds)
        {
            cycleMaps = CYCLES.get(taskId);
            if (null == cycleMaps)
            {
                continue;
            }
            
            //必需从启用的非停止任务中选择
            for (Entry<String, Cycle> cycleIter : cycleMaps.entrySet())
            {
                Cycle cycle = cycleIter.getValue();
                if (cycle.isOtherDaySchedule())
                {
                    continue;
                }
                
                dgNode = new DGNode(getName(cycleIter.getValue()));
                dgNode.setTaskId(taskId);
                dgNode.setCycleId(cycleIter.getKey());
                dgNode.setMoreTLs(cycle.isMoreSubRs());
                dgNode.setMoreFLs(cycle.isMorePreRs());
                //设置状态
                dgNode.setState(cycle.getTaskRS().getState());
                //构造任务Id名字键值对
                idNodes.put(dgNode.getName(), dgNode);
                nodeLefts.put(dgNode.getName(), dgNode);
                
                //                if (null != cycle.getSubCycleRs())
                //                {
                //                    //将直接后继也加入到节点中
                //                    for (CycleRelation subRel : cycle.getSubCycleRs())
                //                    {
                //                        dgNode = new DGNode(getName(subRel.getDstCycle()));
                //                        dgNode.setTaskId(subRel.getDstCycle().getTaskRS().getTaskId());
                //                        dgNode.setCycleId(subRel.getDstCycle().getTaskRS().getCycleId());
                //                        dgNode.setMoreTLs(subRel.getDstCycle().isMoreSubRs());
                //                        dgNode.setMoreFLs(subRel.getDstCycle().isMorePreRs());
                //                        //设置状态
                //                        dgNode.setState(subRel.getDstCycle().getTaskRS().getState());
                //                        //构造任务Id名字键值对
                //                        idNodes.put(dgNode.getName(), dgNode);
                //                        nodeLefts.put(dgNode.getName(), dgNode);
                //                    }
                //                }
            }
        }
        
        List<DGLink> links;
        List<DGNode> curNodes = new ArrayList<DGNode>();
        digraph.getHiNodes().add(curNodes);
        
        Cycle cycle;
        //遍历可见周期集合
        for (Entry<String, DGNode> nodeEntry : idNodes.entrySet())
        {
            dgNode = nodeEntry.getValue();
            cycle = getCycle(dgNode.getTaskId(), dgNode.getCycleId());
            if (null == cycle)
            {
                continue;
            }
            
            //必需从启用的非停止任务中选择
            links = getDGLinks(cycle, idNodes);
            if (null == links || links.isEmpty())
            {
                //根节点
                curNodes.add(dgNode);
                //移除
                nodeLefts.remove(dgNode.getName());
            }
            else
            {
                //构造有向线段
                digraph.getLinks().addAll(links);
            }
        }
        
        List<DGNode> nNodes;
        DGNode nodeTemp;
        int n = 0;
        //不为空
        while (!nodeLefts.isEmpty())
        {
            n++;
            if (curNodes.isEmpty())
            {
                LOGGER.error("cov2Higraph failed, cicle may be exist!,current layer is {}, left {} nodes",
                    n,
                    nodeLefts.size());
                break;
            }
            
            nNodes = new ArrayList<DGNode>();
            for (DGNode node : curNodes)
            {
                //寻找直接所有子女节点
                for (DGLink link : node.getFromLinks())
                {
                    nodeTemp = link.getTargetNode();
                    if (!nNodes.contains(nodeTemp))
                    {
                        nNodes.add(nodeTemp);
                    }
                }
            }
            
            //如果子女节点中有作为子女的节点,则移除
            DGNode[] nodesCp = new DGNode[nNodes.size()];
            nNodes.toArray(nodesCp);
            for (DGNode node : nodesCp)
            {
                for (DGLink link : node.getFromLinks())
                {
                    nodeTemp = link.getTargetNode();
                    if (nNodes.contains(nodeTemp))
                    {
                        //移除子女兄弟
                        nNodes.remove(nodeTemp);
                    }
                }
            }
            
            //如果有父节点在剩余集合中的，则移除
            nodesCp = new DGNode[nNodes.size()];
            nNodes.toArray(nodesCp);
            for (DGNode node : nodesCp)
            {
                for (DGLink link : node.getToLinks())
                {
                    nodeTemp = link.getSourceNode();
                    if (nodeLefts.containsKey(nodeTemp.getName()))
                    {
                        //移除当前节点
                        nNodes.remove(node);
                    }
                }
            }
            
            digraph.getHiNodes().add(nNodes);
            //从剩余节点集合中删除
            for (DGNode node : nNodes)
            {
                nodeLefts.remove(node.getName());
            }
            
            curNodes = nNodes;
        }
        
        List<CycleRelation> preCycleRs;
        List<DGLink> fromLinks;
        boolean moreFLs;
        //遍历所有节点，判断节点的fromlinks是否与反向依赖相同
        for (DGNode node : idNodes.values())
        {
            cycle = getCycle(node.getTaskId(), node.getCycleId());
            if (null == cycle)
            {
                continue;
            }
            
            moreFLs = cycle.isMorePreRs();
            if (!moreFLs)
            {
                preCycleRs = cycle.getPreCycleRs();
                fromLinks = node.getFromLinks();
                if (null == preCycleRs && null == fromLinks)
                {
                    moreFLs = false;
                }
                else if (null != preCycleRs && null != fromLinks && preCycleRs.size() == fromLinks.size())
                {
                    moreFLs = false;
                }
                else
                {
                    moreFLs = true;
                }
            }
            
            node.setMoreFLs(moreFLs);
        }
        
        return digraph;
    }
    
    //获取依赖任务到当前任务的所有有向线段
    private List<DGLink> getDGLinks(DGNode dgNode, String subDependentList, Map<String, DGNode> idNodes)
    {
        List<DGLink> dgLinks = new ArrayList<DGLink>();
        if (StringUtils.isBlank(subDependentList))
        {
            return dgLinks;
        }
        
        DGNode sourceNode;
        DGLink dgLink;
        String sNodeName;
        String[] subDependents = subDependentList.split(";");
        Long taskId;
        String cycleId;
        boolean isIgnoreError;
        String[] instanceInfos;
        int index;
        boolean moreDeps = false;
        for (String subDependent : subDependents)
        {
            //有更多依赖
            if (subDependent.equals("..."))
            {
                moreDeps = true;
                continue;
            }
            
            instanceInfos = subDependent.split(",");
            index = 0;
            taskId = Long.parseLong(instanceInfos[index++]);
            cycleId = instanceInfos[index++];
            isIgnoreError = "1".equals(instanceInfos[index++]);
            
            dgLink = new DGLink();
            sNodeName = getName(taskId, cycleId);
            sourceNode = idNodes.get(sNodeName);
            //源不存在的依赖不再构建
            if (null == sourceNode)
            {
                moreDeps = true;
                continue;
            }
            
            dgLink.setSource(sourceNode.getName());
            dgLink.setTarget(dgNode.getName());
            //设置源节点和目标节点
            dgLink.setSourceNode(sourceNode);
            dgLink.setTargetNode(dgNode);
            
            //是否弱依赖
            dgLink.setType(isIgnoreError);
            
            //设置源和目标节点的Link
            sourceNode.getFromLinks().add(dgLink);
            dgNode.getToLinks().add(dgLink);
            
            dgLinks.add(dgLink);
        }
        
        dgNode.setMoreTLs(moreDeps);
        
        return dgLinks;
    }
    
    //获取依赖任务到当前任务的所有有向线段
    private List<DGLink> getDGLinks(Cycle cycle, Map<String, DGNode> idNodes)
    {
        List<DGLink> dgLinks = new ArrayList<DGLink>();
        String name = getName(cycle);
        if (null == name)
        {
            return dgLinks;
        }
        
        //依赖关系为空直接返回
        List<CycleRelation> cycleRelations = cycle.getSubCycleRs();
        if (null == cycleRelations || cycleRelations.isEmpty())
        {
            return dgLinks;
        }
        
        DGLink dgLink;
        String sNodeName;
        DGNode targetNode = idNodes.get(name);
        if (null == targetNode)
        {
            //节点不存在
            return dgLinks;
        }
        
        DGNode sourceNode;
        boolean moreDeps = cycle.isMoreSubRs();
        for (CycleRelation cycleRela : cycleRelations)
        {
            dgLink = new DGLink();
            sNodeName = getName(cycleRela.getDstCycle());
            sourceNode = idNodes.get(sNodeName);
            //源不存在的依赖不再构建
            if (null == sourceNode)
            {
                moreDeps = true;
                continue;
            }
            
            dgLink.setSource(sourceNode.getName());
            dgLink.setTarget(targetNode.getName());
            //设置源节点和目标节点
            dgLink.setSourceNode(sourceNode);
            dgLink.setTargetNode(targetNode);
            
            //是否弱依赖
            dgLink.setType(cycleRela.isIgnoreError());
            
            //设置源和目标节点的Link
            sourceNode.getFromLinks().add(dgLink);
            targetNode.getToLinks().add(dgLink);
            
            dgLinks.add(dgLink);
        }
        
        targetNode.setMoreTLs(moreDeps);
        
        return dgLinks;
    }
    
    private synchronized int getState(Long taskId, String cycleId)
    {
        int state;
        if (null == taskId || null == cycleId)
        {
            return RunningState.UNKNOWN;
        }
        
        Map<String, Cycle> cycleMaps = CYCLES.get(taskId);
        if (null == cycleMaps)
        {
            cycleMaps = new HashMap<String, Cycle>();
            CYCLES.put(taskId, cycleMaps);
        }
        
        TaskRunningStateEntity taskRS;
        TaskMinMaxCycle taskMinMaxCycle;
        Cycle cycle = cycleMaps.get(cycleId);
        if (null == cycle)
        {
            taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(taskId);
            taskRS.setCycleId(cycleId);
            taskMinMaxCycle = CYCLE_RANGES.get(taskId);
            if (null != taskMinMaxCycle && null != taskMinMaxCycle.getMaxCycleId()
                && cycleId.compareTo(taskMinMaxCycle.getMinCycleId()) >= 0
                && cycleId.compareTo(taskMinMaxCycle.getMaxCycleId()) <= 0)
            {
                //在最大最小值区间的周期状态为sucess
                state = RunningState.SUCCESS;
            }
            else
            {
                //其它情况为不存在
                state = RunningState.NOTINIT;
            }
            
        }
        else
        {
            state = cycle.getTaskRS().getState();
        }
        
        return state;
    }
    
    private synchronized String getName(Cycle cycle)
    {
        if (null == cycle || null == cycle.getTaskRS())
        {
            return null;
        }
        
        Long taskId = cycle.getTaskRS().getTaskId();
        String taskName = taskManage.getTaskName(taskId);
        return String.format("%s,%s,%d", taskName, cycle.getTaskRS().getCycleId(), taskId);
    }
    
    private synchronized String getName(Long taskId, String cycleId)
    {
        if (null == taskId || null == cycleId)
        {
            return null;
        }
        
        String taskName = taskManage.getTaskName(taskId);
        return String.format("%s,%s,%d", taskName, cycleId, taskId);
    }
}
