/*
 * 文 件 名:  CycleTask.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.tcc.SSH.CommandType;
import com.huawei.platform.tcc.SSH.RHiveCommand;
import com.huawei.platform.tcc.SSH.RLsCommand;
import com.huawei.platform.tcc.SSH.SSHCommand;
import com.huawei.platform.tcc.SSH.SSHConnect;
import com.huawei.platform.tcc.SSH.SSHManager;
import com.huawei.platform.tcc.alarm.AlarmInfo;
import com.huawei.platform.tcc.alarm.ISendAlarming;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.AlarmType;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.constants.type.EndBatchFlag;
import com.huawei.platform.tcc.constants.type.ExecuteType;
import com.huawei.platform.tcc.constants.type.GradeType;
import com.huawei.platform.tcc.constants.type.NotifyType;
import com.huawei.platform.tcc.constants.type.RedoType;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.cycle.CycleManage;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.CycleDependRelation;
import com.huawei.platform.tcc.domain.NotifyData;
import com.huawei.platform.tcc.domain.ReserveCycleLogParam;
import com.huawei.platform.tcc.domain.TaskCycleParam;
import com.huawei.platform.tcc.domain.TaskIdCycleIdPair;
import com.huawei.platform.tcc.domain.TccNotifyData;
import com.huawei.platform.tcc.entity.BatchRunningStateEntity;
import com.huawei.platform.tcc.entity.StepRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskAlarmThresholdEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.exception.ExecuteException;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 周期任务
 * 任务的某个执行周期
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-20]
 */
public class CycleTask implements Runnable, Comparable<CycleTask>
{
    //记录日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CycleTask.class);
    
    //定时器，用来调度步骤超时
    private static Timer timer = new Timer();
    
    //tcc的数据库操作对象
    private TccDao tccDao;
    
    //发送告警接口
    private transient ISendAlarming alarming;
    
    //运行次数
    private transient int runTimes;
    
    //当前需要运行的周期任务
    private transient TaskRunningStateEntity taskRS;
    
    //任务
    private transient TaskEntity task;
    
    //运行开始时间
    private Date startTime;
    
    //运行状态
    private int state;
    
    //周期依赖关系
    private List<CycleDependRelation> cycleDepRLst;
    
    //步骤ID与任务步骤的键值对集合
    private Map<Integer, TaskStepEntity> stepKeyMap;
    
    //最大的批次id
    private int maxBatchId;
    
    //执行方式
    private int execType;
    
    //开始执行周期
    private String startCycleId;
    
    //未运行完成的批次
    private BlockingQueue<BatchRunningStateEntity> batchRSQueue;
    
    //输入文件结束标志
    private boolean bInputBatchEnd = false;
    
    //同一个周期内的任务的输入文件名不能重复
    private List<String> inputedFiles = new ArrayList<String>();
    
    //任务周期超时处理器
    private DelayAlarmProcessor taskRSTimeOutProc;
    
    //任务周期最迟结束处理器
    private DelayAlarmProcessor taskRSLatestEndProc;
    
    //步骤超时处理器
    private StepTimeOutProcessor stepTimeOut;
    
    //当前节点Id
    private Integer currNodeId;
    
    //节点名
    private String nodeName;
    
    //最后一个节点
    private boolean endNode;
    
    //节点序列
    private Integer nodeSequence;
    
    //是否停止当前任务
    private boolean bStop = false;
    
    //步骤运行超时
    private boolean bTimeout = false;
    
    //批次运行错误
    private boolean bBatchError = false;
    
    //ssh连接
    private SSHConnect sshConnect;
    
    //ssh管理器
    private transient SSHManager sshManager;
    
    //hive命令
    private RHiveCommand hiveCommand;
    
    //ls命令
    private RLsCommand lsCommand;
    
    //等待文件输入的线程
    private Thread waittingFilesThread;
    
    //主线程
    private Thread mainThread;
    
    //是否需要ssh通道
    private boolean needSSH;
    
    //周期管理
    private CycleManage cycleManage;
    
    /**
     * 构造函数
     * @param task 任务
     * @param taskRS 任务运行周期
     * @param cycleDepRLst 周期依赖关系
     */
    public CycleTask(TaskEntity task, TaskRunningStateEntity taskRS, List<CycleDependRelation> cycleDepRLst)
    {
        this.state = RunningState.INIT;
        this.task = task;
        this.stepKeyMap = new HashMap<Integer, TaskStepEntity>();
        this.taskRS = taskRS;
        this.cycleDepRLst = cycleDepRLst;
        //初始化文件批次的容量大小
        this.batchRSQueue = new LinkedBlockingQueue<BatchRunningStateEntity>();
        //默认开始与结束周期相同
        this.startCycleId = taskRS.getCycleId();
        this.runTimes = null == taskRS.getReturnTimes() ? 0 : taskRS.getReturnTimes();
        this.startTime = null == taskRS.getRunningStartTime() ? new Date() : taskRS.getRunningStartTime();
    }
    
    public CycleManage getCycleManage()
    {
        return this.cycleManage;
    }
    
    public void setCycleManage(CycleManage cycleManage)
    {
        this.cycleManage = cycleManage;
    }
    
    public boolean getNeedSSH()
    {
        return this.needSSH;
    }
    
    public void setNeedSSH(boolean needSSH)
    {
        this.needSSH = needSSH;
    }
    
    public String getNodeName()
    {
        return nodeName;
    }
    
    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }
    
    public Integer getNodeSequence()
    {
        return nodeSequence;
    }
    
    public void setNodeSequence(Integer nodeSequence)
    {
        this.nodeSequence = nodeSequence;
    }
    
    public Integer getCurrNodeId()
    {
        return currNodeId;
    }
    
    public void setCurrNodeId(Integer currNodeId)
    {
        this.currNodeId = currNodeId;
    }
    
    public boolean isEndNode()
    {
        return endNode;
    }
    
    public void setEndNode(boolean endNode)
    {
        this.endNode = endNode;
    }
    
    public SSHConnect getSshConnect()
    {
        return sshConnect;
    }
    
    public void setSshConnect(SSHConnect sshConnect)
    {
        this.sshConnect = sshConnect;
    }
    
    /**
     * 设置tcc的数据库操作对象
     * @param tccDao tcc的数据库操作对象
     */
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 获取dao对象
     * @return dao对象
     */
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public int getPriority()
    {
        return task.getPriority();
    }
    
    public String getCycleId()
    {
        return taskRS.getCycleId();
    }
    
    public Long getTaskId()
    {
        return taskRS.getTaskId();
    }
    
    public int getState()
    {
        return state;
    }
    
    public String getOsUser()
    {
        return task.getOsUser();
    }
    
    public String getUserGroup()
    {
        return task.getUserGroup();
    }
    
    /**
     * 是否实时任务
     * @return 是否实时任务
     */
    public boolean rtTask()
    {
        return null != task.getRtTaskFlag() && true == task.getRtTaskFlag();
    }
    
    /**
     * 多批和实时任务的权重为0，不暂用并发数
     * @return 任务权重值
     */
    public int getTaskWeight()
    {
        if ((null != task.getMultiBatchFlag() && true == task.getMultiBatchFlag())
            || (null != task.getRtTaskFlag() && true == task.getRtTaskFlag()))
        {
            return 0;
        }
        else
        {
            return task.getWeight();
        }
    }
    
    public String getDependTaskIdList()
    {
        return task.getDependTaskIdList();
    }
    
    public Boolean getCycleDependFlag()
    {
        return task.getCycleDependFlag();
    }
    
    /**
     * 线程的启动方法
     */
    @Override
    public void run()
    {
        mainThread = Thread.currentThread();
        String threadName = Thread.currentThread().getName();
        try
        {
            //修改线程的名字
            Thread.currentThread()
                .setName(String.format("CycleTaskMain_%d_%s", taskRS.getTaskId(), taskRS.getCycleId()));
            
            //为log4j日志记录填上taskId和cycleId
            TccUtil.startTaskCycleLog(taskRS.getTaskId(), taskRS.getCycleId());
            
            LOGGER.info("run cycleTask[taskId=[{}],cycleId=[{}]] started.", this.getTaskId(), this.getCycleId());
            TaskRunningStateEntity taskRSE = new TaskRunningStateEntity();
            taskRSE.setTaskId(taskRS.getTaskId());
            taskRSE.setCycleId(taskRS.getCycleId());
            
            //如果已经不满足运行条件了，则直接退出
            boolean conditionsOk = canRun();
            if (!conditionsOk)
            {
                LOGGER.warn("conditions of cycleTask[taskId=[{}],cycleId=[{}]] don't satisfy!",
                    this.getTaskId(),
                    this.getCycleId());
                
                //中途停止
                this.state = RunningState.STOP;
                
                //将状态修改回来
                taskRSE.setState(RunningState.START);
                //通知状态修改
                Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSE);
                return;
            }
            
            taskRSE.setState(RunningState.RUNNING);
            //通知状态修改
            Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSE);
            
            //如果是忽略错误启动的任务周期，那么产生忽略错误启动告警
            if (ignoreErrorStart(conditionsOk, cycleDepRLst, this.getTaskId()))
            {
                sendAlarm(AlarmType.IGNORE_ERROR_START);
            }
            
            init();
            
            //退出点
            if (bStop)
            {
                return;
            }
            
            execBatchTasks();
            
            updateRunningTaskCycleState();
            
            LOGGER.debug("run cycleTask[taskId=[{}],cycleId=[{}]] finished.", this.getTaskId(), this.getCycleId());
            
            removeDBLog();
        }
        catch (Throwable e)
        {
            LOGGER.error("CycleTask.run() execute failed, taskRS is [{}]!", taskRS, e);
            //更新任务运行状态为error
            updateTaskRSError(taskRS);
        }
        finally
        {
            //取消还没有开始运行的超时定时器
            if (null != taskRSTimeOutProc)
            {
                taskRSTimeOutProc.finished();
                taskRSTimeOutProc.cancel();
            }
            
            //取消还没有开始运行的最晚结束定时器
            if (null != taskRSLatestEndProc)
            {
                taskRSLatestEndProc.finished();
                taskRSLatestEndProc.cancel();
            }
            
            //释放连接
            if (null != sshConnect)
            {
                sshManager.releaseConnect(sshConnect, true);
            }
            
            LOGGER.info("run cycleTask[taskId=[{}],cycleId=[{}]] finished.", this.getTaskId(), this.getCycleId());
            
            TccUtil.stopTaskCycleLog();
            
            notifyFinished();
        }
        
        Thread.currentThread().setName(threadName);
    }
    
    /**
     * 通知消息
     */
    private void notifyFinished()
    {
        List<String> observerUrls = TccConfig.getObserverUrls();
        
        NotifyData data = getNotifyData();
        for (String url : observerUrls)
        {
            TccUtil.notify(url, data);
        }
    }
    
    private NotifyData getNotifyData()
    {
        TccNotifyData data = new TccNotifyData();
        data.setId(task.getTaskName() + "|" + getCycleId());
        data.setType(NotifyType.HIVE_TABLE_FINISHED);
        data.setTaskName(task.getTaskName());
        data.setCycleId(getCycleId());
        data.setHiveTableName(task.getHiveTableName());
        data.setState(getState());
        data.setEndTime(new Date());
        
        Date realStartTime = null;
        try
        {
            BatchRunningStateEntity batchRS = tccDao.getOldestBatchRS(getTaskId(), getCycleId());
            if (null != batchRS)
            {
                realStartTime = batchRS.getRunningEndTime();
            }
            else
            {
                realStartTime = startTime;
            }
        }
        catch (CException e)
        {
            LOGGER.error("getOldestBatchRS failed!", e);
        }
        
        data.setStartTime(realStartTime);
        
        return data;
    }
    
    /**
     * 异常db日志
     */
    private void removeDBLog()
    {
        ReserveCycleLogParam param = new ReserveCycleLogParam();
        try
        {
            //删除远程库表中的日志
            param.setTaskId(Long.toString(this.getTaskId()));
            param.setReserveCount(TccConfig.getReserverNLCCount());
            //将查询和删除分离，减少死锁的发生
            List<TaskIdCycleIdPair> taskIdCycleIds = tccDao.getNeedDeleteLogTaskRSs(param);
            tccDao.deleteRemoteshellLog(taskIdCycleIds);
        }
        catch (Exception e)
        {
            //删除失败，仅记录日志
            LOGGER.error("deleteRemoteshellLog failed, param is [{}]!", param, e);
        }
    }
    
    /**
     * 检查是否运行条件是否满足
     * @return 是否满足运行条件
     * @throws Exception 异常
     */
    private boolean canRun()
        throws Exception
    {
        //再次判断依赖是否成立,因为在等待队列中的时间可能很长
        if (!TccUtil.isDependsOk(cycleDepRLst, this.getTaskId(), tccDao))
        {
            return false;
        }
        
        TaskEntity taskAgain = tccDao.getTask(this.getTaskId());
        if (null != taskAgain && taskAgain.canRun())
        {
            //时间上是否满足运行条件
            String maxCycleId = TccUtil.maxCycleIdTimeisOK(new Date(), taskAgain.getCycleOffset());
            if (this.getCycleId().compareToIgnoreCase(maxCycleId) > 0)
            {
                return false;
            }
            
            task = taskAgain;
        }
        else
        {
            LOGGER.warn("CycleTask[{},{}] prepare to run,but Task does not exist or cant't run.",
                new Object[] {this.getTaskId(), this.getCycleId()});
            //LOGGER.warn("state of CycleTask[{},{}] is set to reqdelete.", this.getTaskId(), this.getCycleId());
            //TaskRunningStateEntity taskRSU = new TaskRunningStateEntity();
            //taskRSU.setTaskId(this.getTaskId());
            //taskRSU.setCycleId(this.getCycleId());
            //taskRSU.setState(RunningState.REQDELETE);
            //tccDao.updateTaskRunningState(taskRSU);
            
            //通知删除
            //Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSU);
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取cycleTime的下月或者下周的起始时间点
     * @param cycleTime 周期时间
     * @param redoType 重做类型，仅支持月或者周
     * 
     */
    private Calendar getNextMonthWeekBeginTime(Date cycleTime, int redoType)
    {
        if (task.getRedoType() == RedoType.LAST_DAY_OF_WEEK)
        {
            Calendar nextWeekCal = Calendar.getInstance();
            nextWeekCal.setTime(cycleTime);
            int weekDay = nextWeekCal.get(Calendar.DAY_OF_WEEK);
            if (weekDay == Calendar.SUNDAY)
            {
                nextWeekCal.add(Calendar.DAY_OF_MONTH, 1);
            }
            else
            {
                nextWeekCal.add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - (weekDay - Calendar.MONDAY));
            }
            nextWeekCal.set(Calendar.HOUR_OF_DAY, 0);
            nextWeekCal.set(Calendar.MINUTE, 0);
            nextWeekCal.set(Calendar.SECOND, 0);
            nextWeekCal.set(Calendar.MILLISECOND, 0);
            return nextWeekCal;
        }
        else if (task.getRedoType() == RedoType.LAST_DAY_OF_MOUTH)
        {
            Calendar nextMonthCal = Calendar.getInstance();
            nextMonthCal.setTime(cycleTime);
            nextMonthCal.add(Calendar.MONTH, 1);
            nextMonthCal.set(Calendar.DAY_OF_MONTH, 1);
            nextMonthCal.set(Calendar.HOUR_OF_DAY, 0);
            nextMonthCal.set(Calendar.MINUTE, 0);
            nextMonthCal.set(Calendar.SECOND, 0);
            nextMonthCal.set(Calendar.MILLISECOND, 0);
            return nextMonthCal;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取包含cycleTime的当前重做范围开始时间和结束时间点，null表示没有包含
     * 
     * @param cycleTime 周期时间
     * @param startTimeT 任务启动时间 
     * @parma redoStartTime 重做开始时间
     * @parma redoEndTime 重做结束时间
     * @param redoDayLength 重做天数
     * @reurn 开始时间和结束时间点
     */
    private Date[] getCurrNextIntDayBeginTime(Date cycleTime, Date startTimeT, Date redoStartTime, Date redoEndTime,
        int redoDayLength)
    {
        if (null == redoStartTime || null == redoEndTime || null == cycleTime)
        {
            return new Date[] {null, null};
        }
        
        Date startDefault = redoStartTime;
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        
        //start不能小于基准时间
        if (startDefault.before(TccConfig.getBenchDate()))
        {
            startDefault = TccConfig.getBenchDate();
        }
        
        //start不能小于基准时间
        if (null != startTimeT && startDefault.before(startTimeT))
        {
            startDefault = startTimeT;
        }
        
        //cycleTime不在起始和结束时间范围内，直接返回null
        if (!(!startDefault.after(cycleTime) && !cycleTime.after(redoEndTime)))
        {
            return new Date[] {null, null};
        }
        
        cStart.setTime(startDefault);
        cEnd.setTime(startDefault);
        
        while (!cStart.getTime().after(cycleTime))
        {
            //cend往后推移重做天数
            cEnd.add(Calendar.DAY_OF_MONTH, redoDayLength);
            
            if (cEnd.getTime().after(redoEndTime))
            {
                cEnd.setTime(redoEndTime);
                //微调1ms
                cEnd.add(Calendar.MILLISECOND, 1);
            }
            
            if (cEnd.getTime().after(cycleTime))
            {
                return new Date[] {cStart.getTime(), cEnd.getTime()};
            }
            
            cStart.add(Calendar.DAY_OF_MONTH, redoDayLength);
        }
        return new Date[] {null, null};
    }
    
    /**
     * 计算任务周期的执行类型和开始周期Id
     * @throws CException 异常
     */
    private void computeExecuteTypeStartCycleId()
        throws CException
    {
        LOGGER.debug("enter computeExecuteType...");
        
        //多批任务不能集成重做
        if (task.getMultiBatchFlag())
        {
            this.execType = ExecuteType.NORMAL;
            return;
        }
        
        //判断执行方式
        Date cycleTime = TccUtil.covCycleID2Date(this.getCycleId());
        
        int cycleType = CycleType.toCycleType(task.getCycleType());
        
        //不在指定时间范围内或者普通方式
        if (cycleTimeNotIn(cycleTime, task.getRedoStartTime(), task.getRedoEndTime())
            || task.getRedoType() == RedoType.INTEGRATED && 0 == task.getRedoDayLength())
        {
            this.execType = ExecuteType.NORMAL;
            return;
        }
        
        //非分钟、小时或者天任务只能使用普通方式
        if (CycleType.HOUR != cycleType && CycleType.DAY != cycleType && CycleType.MINUTE != cycleType)
        {
            this.execType = ExecuteType.NORMAL;
            return;
        }
        
        //task.getRedoType() == RedoType.INTEGRATED && ! == task.getRedoDayLength()
        //必然是集成重做
        if (task.getRedoType() == RedoType.INTEGRATED)
        {
            //月末或者周末的最后一个周期
            Date nextCycleTime = TccUtil.roll2CycleStart(cycleTime, cycleType, task.getCycleLength());
            Date[] curNextDate =
                getCurrNextIntDayBeginTime(cycleTime,
                    task.getStartTime(),
                    task.getRedoStartTime(),
                    task.getRedoEndTime(),
                    task.getRedoDayLength());
            //没有找到包含cycleTime的时间范围 
            if (null == curNextDate || null == curNextDate[0] || null == curNextDate[1])
            {
                this.execType = ExecuteType.NORMAL;
                return;
            }
            
            Date rightEdgeTime = curNextDate[1];
            if (rightEdgeTime.after(nextCycleTime))
            {
                this.execType = ExecuteType.VISUAL;
                return;
            }
            
            this.execType = ExecuteType.INTEGRATION;
            Date leftEdgeTime = curNextDate[0];
            
            this.startCycleId =
                TccUtil.covDate2CycleID(TccUtil.roll2FirstAfterLeftEdge(cycleTime,
                    leftEdgeTime,
                    CycleType.toCycleType(task.getCycleType()),
                    task.getCycleLength()));
        }
        else
        {
            //月末或者周末的最后一个周期
            Date nextCycleTime = TccUtil.roll2CycleStart(cycleTime, cycleType, task.getCycleLength());
            Calendar nextWeekOrMonthCal = getNextMonthWeekBeginTime(cycleTime, task.getRedoType());
            
            Date rightEdgeTime = nextWeekOrMonthCal.getTime();
            if (rightEdgeTime.after(task.getRedoEndTime()))
            {
                rightEdgeTime = task.getRedoEndTime();
                //微调1微秒
                rightEdgeTime = tuneMillSeconds(rightEdgeTime, 1);
            }
            
            if (rightEdgeTime.after(nextCycleTime))
            {
                this.execType = ExecuteType.VISUAL;
                return;
            }
            
            this.execType = ExecuteType.INTEGRATION;
            if (task.getRedoType() == RedoType.LAST_DAY_OF_MOUTH)
            {
                nextWeekOrMonthCal.add(Calendar.MONTH, -1);
            }
            else
            {
                nextWeekOrMonthCal.add(Calendar.DAY_OF_MONTH, -Calendar.SATURDAY);
            }
            
            //获取当月或者当周的第1个周期Id
            Date leftEdgeTime = nextWeekOrMonthCal.getTime();
            if (leftEdgeTime.before(task.getRedoStartTime()))
            {
                leftEdgeTime = task.getRedoStartTime();
            }
            
            this.startCycleId =
                TccUtil.covDate2CycleID(TccUtil.roll2FirstAfterLeftEdge(cycleTime,
                    leftEdgeTime,
                    CycleType.toCycleType(task.getCycleType()),
                    task.getCycleLength()));
        }
        
        String minCycleId = getTaskMinCycleId(task.getStartTime(), cycleType, task.getCycleLength());
        
        //startCycleId不能小于任务的最小周期Id
        if (minCycleId.compareTo(this.startCycleId) > 0)
        {
            this.startCycleId = minCycleId;
        }
    }
    
    private boolean cycleTimeNotIn(Date cycT, Date redoST, Date redoET)
    {
        return (null != redoST && cycT.before(redoST)) || (null != redoET && cycT.after(redoET));
    }
    
    /**
     * 获取任务的最小周期Id
     */
    private String getTaskMinCycleId(Date tStartTime, int cycleType, int cycleLength)
    {
        Date maxTime = TccConfig.getBenchDate();
        if (null != tStartTime && tStartTime.after(maxTime))
        {
            maxTime = tStartTime;
        }
        
        Date rollCycleDate = TccUtil.roll2CycleStartOnBenchDate(maxTime, cycleType, cycleLength, 0);
        if (!rollCycleDate.equals(maxTime))
        {
            rollCycleDate = TccUtil.roll2CycleStartOnBenchDate(maxTime, cycleType, cycleLength, 1);
        }
        
        return TccUtil.covDate2CycleID(rollCycleDate);
    }
    
    /**
     * 
     * 更新任务运行状态为error
     * @param taskRunningState 任务运作状态
     * 
     */
    private void updateTaskRSError(TaskRunningStateEntity taskRunningState)
    {
        try
        {
            TaskRunningStateEntity taskRSU = new TaskRunningStateEntity();
            taskRSU.setTaskId(taskRunningState.getTaskId());
            taskRSU.setCycleId(taskRunningState.getCycleId());
            if (endNode)
            {
                this.state = RunningState.ERROR;
                taskRSU.setState(RunningState.ERROR);
            }
            else
            {
                this.state = RunningState.WAIT_NEXT_NODE_EXE;
                taskRSU.setState(RunningState.WAIT_NEXT_NODE_EXE);
            }
            taskRSU.setNodeName(this.getNodeName());
            taskRSU.setNodeSequence(this.getNodeSequence());
            taskRSU.setReturnTimes(runTimes);
            taskRSU.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateTaskRunningState(taskRSU);
            
            //通知状态修改
            Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSU);
        }
        catch (Throwable e)
        {
            LOGGER.error("updateTaskRSError cause fatal error!", e);
        }
        
        //发送错误告警
        sendAlarm(AlarmType.ERROR);
    }
    
    /**
     * 
     * 更新批次的运行状态为error
     * @param batchRunningState 批次任务运作状态
     * 
     */
    private void updateBatchRSError(BatchRunningStateEntity batchRunningState)
    {
        try
        {
            batchRunningState.setState(RunningState.ERROR);
            batchRunningState.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRunningState);
        }
        catch (Throwable e)
        {
            LOGGER.error("updateBatchRSError cause fatal error!", e);
        }
    }
    
    /**
     * 
     * 更新步骤的运行状态为error
     * @param stepRunningState 步骤任务运作状态
     * 
     */
    private void updateStepRSError(StepRunningStateEntity stepRunningState)
    {
        try
        {
            stepRunningState.setState(RunningState.ERROR);
            stepRunningState.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateStepRunningState(stepRunningState);
        }
        catch (Throwable e)
        {
            LOGGER.error("updateStepRSError cause fatal error!", e);
        }
    }
    
    /**
     * 初始化运行状态 
     * @throws Exception 统一封装的异常
     */
    private void init()
        throws Exception
    {
        LOGGER.debug("enter init(), cycleTask is [{}]", taskRS);
        this.state = RunningState.START;
        
        //计算任务周期的执行类型和开始周期Id
        computeExecuteTypeStartCycleId();
        
        //只调度Init和Start状态的周期任务
        if (RunningState.INIT == taskRS.getState() || RunningState.WAIT_NEXT_NODE_EXE == taskRS.getState())
        {
            
            //清除原有的批次与批次步骤的运行状态信息(可能是重做请求)
            deleteBatchStepStateRelation(this.getTaskId(), this.getCycleId());
            this.maxBatchId = 0;
            
            //更新任务的运行状态为开始,同时将运行次数加1
            TaskRunningStateEntity taskRSU = new TaskRunningStateEntity();
            taskRSU.setTaskId(this.getTaskId());
            taskRSU.setCycleId(this.getCycleId());
            taskRSU.setState(RunningState.START);
            taskRSU.setNodeName(this.getNodeName());
            taskRSU.setNodeSequence(this.getNodeSequence());
            startTime = new Date();
            taskRSU.setRunningStartTime(startTime);
            tccDao.updateTaskRsIncReturnTimes(taskRSU);
            
            //运行次数+1
            this.runTimes++;
            
            //如果运行次数超过1,表示是重做的任务周期，产生告警
            if (this.runTimes > 1)
            {
                sendAlarm(AlarmType.REDO);
            }
            
            //将当前任务周期添加到所依赖的所有的周期任务的 “已启动依赖任务ID列表”
            addcurrCycleTask2DependTaskList(true);
        }
        else
        {
            startTime = (null == taskRS.getRunningStartTime()) ? new Date() : taskRS.getRunningStartTime();
            //继续原来没有完成批次或者步骤(可能程序中途停止过)
            List<BatchRunningStateEntity> batchRSLst = tccDao.getUnCompletedBatch(this.getTaskId(), this.getCycleId());
            
            for (BatchRunningStateEntity batchRS : batchRSLst)
            {
                try
                {
                    batchRSQueue.put(batchRS);
                }
                catch (InterruptedException e)
                {
                    LOGGER.error("batchRSQueue got InterruptedException!"
                        + " please increase the tcc.benchQueueCapacity config!", e);
                }
            }
            
            //是否需要需要分批
            if (task.getMultiBatchFlag())
            {
                inputedFiles = tccDao.getJobInputFiles(this.getTaskId(), this.getCycleId());
                this.maxBatchId = tccDao.getMaxBatchId(this.getTaskId(), this.getCycleId());
            }
        }
        
        //加载任务步骤，估计任务步骤数不会太多,需要的任务步骤一次全部加载
        List<TaskStepEntity> taskStepLst = tccDao.getEnableTaskStepList(this.getTaskId());
        for (TaskStepEntity taskStep : taskStepLst)
        {
            if (null != taskStep.getStepId() && !stepKeyMap.containsKey(taskStep.getStepId()))
            {
                stepKeyMap.put(taskStep.getStepId(), taskStep);
            }
        }
        
        if (needSSH)
        {
            connectWithFailedRetry(TccConfig.getConRetryTimes());
        }
        
        initTimeOutLatestStartProcessor();
        
        LOGGER.debug("exit init(), cycleTask is [{}]", taskRS);
    }
    
    private void connectWithFailedRetry(int retryTimes)
        throws Exception
    {
        int conTimes = 0;
        
        //非最后一个节点
        if (!endNode)
        {
            //建立ssh连接
            sshConnect.connect();
            return;
        }
        
        //最后一个节点加上重试次数
        do
        {
            try
            {
                conTimes++;
                
                //建立ssh连接
                sshConnect.connect();
                
                //连接成功直接跳出
                break;
            }
            catch (Exception e)
            {
                if (conTimes < retryTimes)
                {
                    try
                    {
                        LOGGER.warn("connect failed! conTimes is {}! retry to reconnect it!",
                            new Object[] {conTimes, e});
                        
                        //关闭连接
                        sshConnect.close();
                        
                        //如果停止直接退出
                        if (bStop)
                        {
                            break;
                        }
                        
                        Thread.sleep(TccConfig.MILLS_PER_MINUTE);
                    }
                    catch (InterruptedException e1)
                    {
                        LOGGER.info("connect failed!", e1);
                        
                        //如果唤醒后停止直接退出
                        if (bStop)
                        {
                            break;
                        }
                    }
                    catch (Exception e2)
                    {
                        LOGGER.info("connect close failed!", e2);
                    }
                }
                else
                {
                    LOGGER.error("connect failed! conTimes is {}!", new Object[] {conTimes, e});
                    throw e;
                }
            }
        } while (conTimes < retryTimes);
    }
    
    /**
     * 初始化任务周期超时和最迟结束处理器
     */
    private void initTimeOutLatestStartProcessor()
        throws Exception
    {
        //初始化任务周期超时处理器
        TaskAlarmThresholdEntity alarmThreshold = tccDao.getAlarmThreshold(this.getTaskId());
        if (null != alarmThreshold)
        {
            Date currentTime = new Date();
            if (null != alarmThreshold.getMaxRunTime() && null != startTime)
            {
                long diff = diffMillSeconds(currentTime, startTime);
                long leftTime = alarmThreshold.getMaxRunTime() * TccConfig.MILLS_PER_MINUTE - diff;
                if (leftTime <= 0)
                {
                    sendAlarm(AlarmType.EXEC_TIME_TIMEOUT);
                }
                else
                {
                    taskRSTimeOutProc = new DelayAlarmProcessor(leftTime, AlarmType.EXEC_TIME_TIMEOUT);
                }
            }
            
            //非重做任务
            if (runTimes <= 1 && !StringUtils.isBlank(alarmThreshold.getLatestEndTime()))
            {
                //周期起点时间
                Date cycleStart = TccUtil.covCycleID2Date(this.getCycleId());
                Date endTime = TccUtil.addTimeOffSet(cycleStart, alarmThreshold.getLatestEndTime());
                long leftTime = diffMillSeconds(endTime, currentTime);
                if (leftTime <= 0)
                {
                    sendAlarm(AlarmType.LATEST_END_TIME_NOT_END);
                }
                else
                {
                    taskRSLatestEndProc = new DelayAlarmProcessor(leftTime, AlarmType.LATEST_END_TIME_NOT_END);
                }
            }
        }
    }
    
    private long diffMillSeconds(Date one, Date two)
    {
        return one.getTime() - two.getTime();
    }
    
    //发送告警
    private void sendAlarm(int alarmType)
    {
        if (null != alarming)
        {
            AlarmInfo alarmInfo = new AlarmInfo();
            alarmInfo.setAlarmType(alarmType);
            alarmInfo.setGrade(GradeType.getGrade(alarmType));
            String subject =
                TccUtil.getEmailSubject(taskRS.getTaskId(), task.getTaskName(), taskRS.getCycleId(), alarmType);
            String content =
                TccUtil.getEmailContent(taskRS.getTaskId(), task.getTaskName(), taskRS.getCycleId(), alarmType);
            alarmInfo.setEmailSubject(subject);
            alarmInfo.setEmailMsg(content);
            alarming.sendAlarm(this.getTaskId(), this.getCycleId(), runTimes, alarmInfo);
        }
    }
    
    /**
     * 删除批次与批次步骤的运行状态信息
     * @throws CException 
     */
    private void deleteBatchStepStateRelation(Long taskId, String cycleId)
        throws CException
    {
        tccDao.deleteBatchRunningStates(taskId, cycleId);
        tccDao.deleteStepRunningStates(taskId, cycleId);
    }
    
    /**
     * 执行批任务
     * @throws Exception 统一封装的异常 
     */
    private void execBatchTasks()
        throws Exception
    {
        LOGGER.debug("enter execBatchTasks(), cycleTask is [{}]", taskRS);
        int endBatchFlag = task.getEndBatchFlag();
        
        //单批任务或者多批任务中的正常任务均创建单批次任务
        if (!task.getMultiBatchFlag() || EndBatchFlag.NORMAL == endBatchFlag)
        {
            //创建非多批（单批）批次任务
            createSingleBatchTask();
            
            //创建批任务后需要立即执行
            execBatchTaskList();
        }
        else
        {
            if (EndBatchFlag.FILES == endBatchFlag)
            {
                //创建新的文件批次任务
                createFileBatchTask();
                
                //创建批任务后需要立即执行
                execBatchTaskList();
            }
            else if (EndBatchFlag.FILESINWAITTING == task.getEndBatchFlag()
                || EndBatchFlag.NINWAITTING == task.getEndBatchFlag()
                || EndBatchFlag.NORWAITTING == task.getEndBatchFlag())
            {
                //用单独的工作线程执行：不断的产生新的批任务，直到达到结束要求
                waittingFilesThread = new Thread(new WaittingInputExcutor());
                waittingFilesThread.start();
                
                while (!bStop && !bTimeout && !bBatchError && !bInputBatchEnd)
                {
                    //创建批任务后需要立即执行
                    execBatchTaskList();
                    
                    if (!bInputBatchEnd && batchRSQueue.isEmpty())
                    {
                        //休眠指定的时间，防止空循环
                        try
                        {
                            Thread.sleep(TccConfig.NBT_SLEEP_MILLISECONDS);
                        }
                        catch (InterruptedException e)
                        {
                            LOGGER.warn("sleep interrupted!", e);
                        }
                    }
                }
            }
        }
        
        LOGGER.debug("exit execBatchTasks(), cycleTask is [{}]", taskRS);
    }
    
    /**
     * 根据批次任务的运行结果，更新运行的任务周期状态
     * @throws CException 异常
     */
    private void updateRunningTaskCycleState()
        throws CException
    {
        //退出点
        if (bStop)
        {
            return;
        }
        
        //更新任务状态
        //多批任务既有超时又有错误时，优先更新任务为超时的状态
        TaskRunningStateEntity taskRSU = new TaskRunningStateEntity();
        taskRSU.setTaskId(this.getTaskId());
        taskRSU.setCycleId(this.getCycleId());
        taskRSU.setRunningEndTime(CommonUtils.getCrruentTime());
        taskRSU.setNodeName(this.getNodeName());
        taskRSU.setNodeSequence(this.getNodeSequence());
        taskRSU.setReturnTimes(runTimes);
        
        //如果没有任何批次运行状态
        if (!tccDao.existsBatchRS(taskRS.getTaskId(), taskRS.getCycleId()))
        {
            this.state = RunningState.NOBATCH;
            taskRSU.setState(RunningState.NOBATCH);
        }
        //如果存在执行超时的批任务运行状态
        else if (tccDao.existsTimeOutBatchRS(taskRS.getTaskId(), taskRS.getCycleId()))
        {
            this.state = RunningState.TIMEOUT;
            taskRSU.setState(RunningState.TIMEOUT);
        }
        //如果存在执行错误的批任务运行状态
        else if (tccDao.existsErrorBatchRS(taskRS.getTaskId(), taskRS.getCycleId()))
        {
            if (endNode)
            {
                this.state = RunningState.ERROR;
                taskRSU.setState(RunningState.ERROR);
            }
            else
            {
                this.state = RunningState.WAIT_NEXT_NODE_EXE;
                taskRSU.setState(RunningState.WAIT_NEXT_NODE_EXE);
            }
        }
        else
        {
            this.state = RunningState.SUCCESS;
            taskRSU.setState(RunningState.SUCCESS);
        }
        
        //如果已经请求停止，就不更新任务运行状态
        if (!bStop)
        {
            if (ExecuteType.NORMAL == this.execType)
            {
                tccDao.updateTaskRunningState(taskRSU);
                
                //通知状态修改
                Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSU);
            }
            else if (ExecuteType.VISUAL == this.execType)
            {
                this.state = RunningState.VSUCCESS;
                taskRSU.setState(RunningState.VSUCCESS);
                tccDao.updateTaskRunningState(taskRSU);
                
                //通知状态修改
                Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSU);
            }
            else
            {
                updateTaskRunningStateIntegration(this.getTaskId(),
                    this.startCycleId,
                    this.getCycleId(),
                    taskRSU.getState());
                
                //更新本周期的结束时间以及运行次数
                tccDao.updateTaskRunningState(taskRSU);
                
                //通知状态修改
                Eventor.fireEvent(this, EventType.CHANGE_TASKRS_STATE, taskRSU);
            }
            
            //发送错误、超时、文件未达到告警
            if (ExecuteType.VISUAL != this.execType && null != taskRSU.getState())
            {
                if (RunningState.ERROR == taskRSU.getState())
                {
                    sendAlarm(AlarmType.ERROR);
                }
                else if (RunningState.TIMEOUT == taskRSU.getState())
                {
                    sendAlarm(AlarmType.TIMEOUT);
                }
                else if (RunningState.NOBATCH == taskRSU.getState())
                {
                    sendAlarm(AlarmType.NOBATCH);
                }
            }
            
            //更新所有依赖周期任务为到周期列表中
            addcurrCycleTask2DependTaskList(false);
        }
    }
    
    /**
     * 将起始周期Id到结束周期Id之间的所有任务周期运行状态更新为state状态
     * @param taskId 任务Id
     * @param minCycleId 起始周期Id（包含）
     * @param maxCycleId 结束周期Id（包含）
     * @param rsState 要更新的状态
     * @throws CException 数据库操作异常
     */
    private void updateTaskRunningStateIntegration(Long taskId, String minCycleId, String maxCycleId, int rsState)
        throws CException
    {
        TaskCycleParam taskCycleParam = new TaskCycleParam();
        taskCycleParam.setTaskId(taskId);
        taskCycleParam.setMinCycleId(minCycleId);
        taskCycleParam.setMaxCycleId(maxCycleId);
        taskCycleParam.setState(rsState);
        //不修改运行结束时间
        tccDao.updateTaskRunningStateIntegration(taskCycleParam);
        
        //通知状态修改
        Eventor.fireEvent(this, EventType.INTEGRATION_CHANGE_TASKRS_STATE, taskCycleParam);
    }
    
    /**
     * 不断的产生新的批任务，直到达到结束要求
     * @throws CException  异常
     */
    private void waittingInput()
        throws CException
    {
        Date dateEnd = CommonUtils.getCrruentTime();
        dateEnd = tuneMinutes(dateEnd, task.getWaitInputMinutes());
        //延迟结束时间数秒，防止线程在最后一个休眠时间内有文件到来
        dateEnd = tuneSeconds(dateEnd, TccConfig.WAITTING_DELAY_SECONDS + TccConfig.WAITTING_DELAY_SECONDS);
        //运行次数
        int execTimes = 0;
        LOGGER.debug("waittingInput of cycleTask started, start time is [{}]",
            new Object[] {CommonUtils.getCrruentTime()});
        switch (task.getEndBatchFlag())
        {
            case EndBatchFlag.FILESINWAITTING:
            {
                while (CommonUtils.getCrruentTime().before(dateEnd))
                {
                    //退出点
                    if (bStop || bTimeout)
                    {
                        return;
                    }
                    createFileBatchTaskSleep();
                    execTimes++;
                    LOGGER.debug("waittingInput of cycleTask[taskId=[{}],cycleId=[{}]] started. runTimes is [{}],"
                        + "current time is [{}]",
                        new Object[] {getTaskId(), getCycleId(), execTimes, CommonUtils.getCrruentTime()});
                }
                break;
            }
            case EndBatchFlag.NINWAITTING:
            {
                while (CommonUtils.getCrruentTime().before(dateEnd)
                    || inputedFiles.size() < task.getInputFileMinCount())
                {
                    //退出点
                    if (bStop || bTimeout)
                    {
                        return;
                    }
                    
                    createFileBatchTaskSleep();
                    execTimes++;
                    LOGGER.debug("waittingInput of cycleTask[taskId=[{}],cycleId=[{}]] started. runTimes is [{}],"
                        + "current time is [{}]",
                        new Object[] {getTaskId(), getCycleId(), execTimes, CommonUtils.getCrruentTime()});
                }
                break;
            }
            case EndBatchFlag.NORWAITTING:
            {
                while (CommonUtils.getCrruentTime().before(dateEnd)
                    && inputedFiles.size() < task.getInputFileMinCount())
                {
                    //退出点
                    if (bStop || bTimeout)
                    {
                        return;
                    }
                    
                    createFileBatchTaskSleep();
                    execTimes++;
                    LOGGER.debug("waittingInput of cycleTask[taskId=[{}],cycleId=[{}]] started. runTimes is [{}],"
                        + "current time is [{}]",
                        new Object[] {getTaskId(), getCycleId(), execTimes, CommonUtils.getCrruentTime()});
                }
                break;
            }
            default:
                break;
        }
        LOGGER.debug("waittingInput of cycleTask finished, finish time is [{}]",
            new Object[] {CommonUtils.getCrruentTime()});
        
        this.bInputBatchEnd = true;
    }
    
    /**
     * 创建新的文件批次任务，并延迟数毫秒
     */
    private void createFileBatchTaskSleep()
        throws CException
    {
        //创建新的文件批次任务
        createFileBatchTask();
        try
        {
            Thread.sleep(TccConfig.NEW_INPUT_FILES_MILLISECONDS);
        }
        catch (InterruptedException ie)
        {
            LOGGER.error("sleep failed!", ie);
        }
    }
    
    /**
     *  将时间调整n分
     */
    private Date tuneMinutes(Date date, Integer n)
    {
        if (null == n || null == date)
        {
            return date;
        }
        
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);
        cDate.add(Calendar.MINUTE, n);
        return cDate.getTime();
    }
    
    /**
     *  将时间调整n秒
     */
    private Date tuneSeconds(Date date, Integer n)
    {
        if (null == n || null == date)
        {
            return date;
        }
        
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);
        cDate.add(Calendar.SECOND, n);
        return cDate.getTime();
    }
    
    /**
     *  将时间调整n微秒
     */
    private Date tuneMillSeconds(Date date, Integer n)
    {
        if (null == n || null == date)
        {
            return date;
        }
        
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);
        cDate.add(Calendar.MILLISECOND, n);
        return cDate.getTime();
    }
    
    /** 
     * 创建非多批（单批）批次任务
     * @throws CException 封装的异常
     */
    private void createSingleBatchTask()
        throws CException
    {
        //获取输入文件列表，并初始化周期批次任务信息
        BatchRunningStateEntity batchRS = new BatchRunningStateEntity();
        batchRS.setTaskId(this.getTaskId());
        batchRS.setCycleId(this.getCycleId());
        batchRS.setBatchId(1);
        BatchRunningStateEntity batchRSExist = tccDao.getBatchRunningState(batchRS);
        
        //如果不存在，则创建批次记录
        if (null == batchRSExist)
        {
            batchRS.setState(RunningState.INIT);
            tccDao.addBatchRunningState(batchRS);
            
            try
            {
                batchRSQueue.put(batchRS);
            }
            catch (InterruptedException e)
            {
                LOGGER.error("batchRSQueue got InterruptedException!"
                    + " please increase the tcc.benchQueueCapacity config!", e);
            }
        }
    }
    
    /**
     * 替换命令中的变量
     * @param cmd 命令
     * @return 替换命令中的变量
     */
    private String replaceVar(String cmd)
    {
        if (null == cmd)
        {
            return null;
        }
        
        return cmd.replace("$OS_USER", this.getOsUser()).replace("$OS_GROUP", this.getUserGroup());
    }
    
    /** 
     * 创建新的文件批次任务
     * @throws CException 封装的异常
     */
    private void createFileBatchTask()
        throws CException
    {
        //获取输入文件列表，并初始化周期批次任务信息
        String files = task.getInputFileList();
        //替换文件或目录的通配符
        files = files.replace(';', ' ');
        
        //按照时间排序，并且输出绝对路径，不递归查找
        String command = String.format(TccConfig.getLSCmdTemplate(), files);
        //替换变量
        command = replaceVar(command);
        List<String> fileLst = null;
        try
        {
            SSHCommand sshCommand = sshConnect.createCommand(CommandType.LS_CMD, command);
            if (sshCommand instanceof RLsCommand)
            {
                lsCommand = (RLsCommand)sshCommand;
                
                //退出点
                if (bStop || bTimeout)
                {
                    return;
                }
                
                fileLst = lsCommand.execute(taskRS);
            }
        }
        catch (ExecuteException e)
        {
            LOGGER.warn("execute command[{}] failed! state=[{}]", command, e.getState());
            //如果是停止异常，不报错
            //if (RunningState.STOP != e.getState())
            //{
            //throw new CException(ResultCode.EXEC_REMOTE_COMMAND_ERROR, e, new Object[] {command,
            //    sshConnect.getConInfo()});
            //}
        }
        catch (Exception e)
        {
            LOGGER.warn("execute command[{}] failed!", command, e);
            //throw new CException(ResultCode.EXEC_REMOTE_COMMAND_ERROR, e, new Object[] {command,
            //    sshConnect.getConInfo()});
        }
        
        if (null == fileLst)
        {
            return;
        }
        
        LOGGER.debug("get files[{}] of cycleTask[taskId=[{}],cycleId=[{}]].", fileLst);
        
        String cycleId = taskRS.getCycleId();
        Integer cycleType = CycleType.toCycleType(task.getCycleType());
        Integer cycleLength = null == task.getCycleLength() ? 1 : task.getCycleLength();
        String[] partailFileNames;
        BatchRunningStateEntity batchRS;
        int index;
        for (String file : fileLst)
        {
            if (!TccConfig.getIsFullName())
            {
                index = file.lastIndexOf('/');
                if (-1 != index)
                {
                    file = file.substring(index + 1);
                }
            }
            
            //不允许同一个周期内的输入文件名重名
            if (StringUtils.isEmpty(file) || inputedFiles.contains(file))
            {
                continue;
            }
            
            //过滤本周期内的文件
            partailFileNames = partialFileNameInCurrentCycle(cycleId, cycleType, cycleLength);
            if (!containsAny(file, partailFileNames))
            {
                continue;
            }
            
            batchRS = new BatchRunningStateEntity();
            batchRS.setTaskId(this.getTaskId());
            batchRS.setCycleId(this.getCycleId());
            batchRS.setBatchId(++maxBatchId);
            batchRS.setState(RunningState.INIT);
            batchRS.setJobInput(file);
            tccDao.addBatchRunningState(batchRS);
            try
            {
                batchRSQueue.put(batchRS);
            }
            catch (InterruptedException e)
            {
                LOGGER.error("batchRSQueue got InterruptedException!"
                    + " please increase the tcc.benchQueueCapacity config!", e);
            }
            inputedFiles.add(file);
        }
    }
    
    /**
     * filename是否包含partailFileNames集合中的任何一个
     * @param filename 文件名
     * @param partailFileNames 部分文件名
     * @return filename是否包含partailFileNames集合中的任何一个
     */
    private boolean containsAny(String filename, String[] partailFileNames)
    {
        if (StringUtils.isEmpty(filename) || null == partailFileNames)
        {
            return false;
        }
        
        for (int i = 0; i < partailFileNames.length; i++)
        {
            if (filename.contains(partailFileNames[i]))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 本周期内的部分文件名集合
     * @param cycleId 周期Id
     * @param cycleType 周期类型
     * @param cycleLength 周期长度
     * @return 本周期内的部分文件名集合
     * @throws CException 时间转换异常
     */
    private String[] partialFileNameInCurrentCycle(String cycleId, Integer cycleType, int cycleLength)
        throws CException
    {
        if (cycleLength <= 0)
        {
            return new String[0];
        }
        
        String[] partialFileNames = new String[cycleLength];
        
        //将周期Id转换为起始时间
        Date cycleStart = TccUtil.covCycleID2Date(cycleId);
        
        partialFileNames[0] = "_" + TccUtil.covDate2CycleTypeString(cycleStart, cycleType);
        for (int i = 1; i < cycleLength; i++)
        {
            cycleStart = TccUtil.tuneDate(cycleStart, cycleType, 1);
            partialFileNames[i] = "_" + TccUtil.covDate2CycleTypeString(cycleStart, cycleType);
        }
        LOGGER.debug("exec partialFileNameInCurrentCycle sucessed. params is [cycleId=[{}], cycleType=[{}],cycleLength="
            + "[{}]], partialFileNames is [{}]",
            new Object[] {cycleId, cycleType, cycleLength, partialFileNames});
        return partialFileNames;
    }
    
    /**
     * 执行批任务列表中的批次任务，其中一个批次任务出错或者超时仍然继续运行
     * @throws CException 
     */
    private void execBatchTaskList()
    {
        LOGGER.debug("enter execBatchTaskList, taskRS is [{}].", taskRS);
        //批次任务运行状态
        BatchRunningStateEntity batchRS;
        //并发执行列表中批次任务
        while (true)
        {
            //退出点
            if (bStop || bTimeout || bBatchError)
            {
                return;
            }
            
            batchRS = batchRSQueue.poll();
            
            if (null == batchRS)
            {
                break;
            }
            
            switch (batchRS.getState())
            {
                case RunningState.INIT:
                {
                    //一个批次需要一个工作线程来执行，以便步骤超时可以终止执行
                    //ThreadPoolUtil.submmitTask();
                    try
                    {
                        LOGGER.debug("batchRS[{}] started to run", batchRS);
                        execSingleBatchTask(batchRS);
                        LOGGER.debug("batchRS[{}] finished to run", batchRS);
                    }
                    catch (Throwable e)
                    {
                        //记录日志
                        LOGGER.error("execSingleBatchTask execute failed, batchRS is [{}]!", batchRS, e);
                        updateBatchRSError(batchRS);
                    }
                    
                    break;
                }
                case RunningState.START:
                {
                    //继续原来没有完成批次或者步骤(可能程序中途停止过)
                    //由于错误或者超时先更新批次后更新步骤,所以此时任务步骤不可能出现error或者timeout的运行状态
                    //获取全部的未完成的运行步骤状态列表
                    //sucess sucess start init init int ...
                    //一个批次需要一个工作线程来执行，以便步骤超时可以终止执行
                    try
                    {
                        LOGGER.debug("continous batchRS[{}] started to run", batchRS);
                        continueStartedBatchTask(batchRS);
                        LOGGER.debug("continous batchRS[{}] finished to run", batchRS);
                    }
                    catch (Throwable e)
                    {
                        //记录日志
                        LOGGER.error("continueStartedBatchTask BatchTask failed, batchRS is [{}]!", batchRS, e);
                        updateBatchRSError(batchRS);
                    }
                    break;
                }
                default:
                    break;
            }
        }
        LOGGER.debug("exit execBatchTaskList, taskRS is [{}].", taskRS);
    }
    
    /** 
     * 继续执行一个单一的批次任务（可能中途停止过）
     * @param batchRS 任务批次运行状态，要求批次任务的状态为start
     * @throws Exception 统一封装的异常
     */
    public void continueStartedBatchTask(BatchRunningStateEntity batchRS)
        throws Exception
    {
        String threadName = Thread.currentThread().getName();
        
        if (null == batchRS)
        {
            return;
        }
        
        //修改线程的名字
        Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d",
            batchRS.getTaskId(),
            batchRS.getCycleId(),
            batchRS.getBatchId()));
        
        List<StepRunningStateEntity> stepRSLst =
            tccDao.getNoSucessedStepRunningStateLst(batchRS.getTaskId(), batchRS.getCycleId(), batchRS.getBatchId());
        
        //后续不更新启动时间
        batchRS.setRunningStartTime(null);
        
        Long taskId = batchRS.getTaskId();
        String cycleId = batchRS.getCycleId();
        Integer batchId = batchRS.getBatchId();
        
        if (null == stepKeyMap)
        {
            //更新批次运行状态为成功
            batchRS.setState(RunningState.SUCCESS);
            batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRS);
            return;
        }
        
        //步骤的运行状态就只能是init或者start
        for (StepRunningStateEntity stepRS : stepRSLst)
        {
            //退出点
            if (bStop || bTimeout)
            {
                return;
            }
            
            //修改线程的名字
            Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d_%d",
                batchRS.getTaskId(),
                batchRS.getCycleId(),
                batchRS.getBatchId(),
                stepRS.getStepId()));
            
            //步骤的运行状态为error
            if (RunningState.ERROR == stepRS.getState())
            {
                //更新批次运行状态为出错
                batchRS.setState(RunningState.ERROR);
                batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
                tccDao.updateBatchRunningState(batchRS);
                break;
            }
            //步骤的运行状态为超时
            else if (RunningState.TIMEOUT == stepRS.getState())
            {
                //更新批次运行状态为出错
                batchRS.setState(RunningState.TIMEOUT);
                batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
                tccDao.updateBatchRunningState(batchRS);
                break;
            }
            //批次任务的状态为start或者init
            else
            {
                Integer stepId = stepRS.getStepId();
                
                //更新任务步骤运行状态为开始
                TaskStepEntity stepSE = stepKeyMap.get(stepId);
                
                if (null == stepSE)
                {
                    //任务的步骤已经被删除,记录错误，并记录步骤运行状态为请求删除
                    LOGGER.error("step[{},{},{},{}] prepare to run,but the step in tcc_TaskStep does not exist.",
                        new Object[] {taskId, cycleId, batchId, stepId});
                    LOGGER.warn("state of step[{},{}] is set to reqdelete.", new Object[] {taskId, cycleId, batchId,
                        stepId});
                    StepRunningStateEntity setpRSU = new StepRunningStateEntity();
                    setpRSU.setTaskId(taskId);
                    setpRSU.setCycleId(cycleId);
                    setpRSU.setBatchId(batchId);
                    setpRSU.setStepId(stepId);
                    setpRSU.setState(RunningState.REQDELETE);
                    tccDao.updateStepRunningState(setpRSU);
                    
                    continue;
                }
                
                try
                {
                    //步骤的执行退出状态
                    int exitStatus = execTaskStep(batchRS, stepSE, stepRS);
                    if (exitStatus == RunningState.ERROR)
                    {
                        bBatchError = true;
                        break;
                    }
                    else if (exitStatus == RunningState.STOP)
                    {
                        return;
                    }
                    else if (exitStatus == RunningState.TIMEOUT)
                    {
                        //更新批次的运行状态为超时
                        batchRS.setState(RunningState.TIMEOUT);
                        batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
                        tccDao.updateBatchRunningState(batchRS);
                        return;
                    }
                }
                catch (Throwable e)
                {
                    LOGGER.error("exec step[batchRS={},stepSE={}] error!", new Object[] {batchRS, stepSE, e});
                    bBatchError = true;
                    break;
                }
            }
        }
        
        //修改线程的名字
        Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d",
            batchRS.getTaskId(),
            batchRS.getCycleId(),
            batchRS.getBatchId()));
        
        //批次运行状态为出错
        if (bBatchError)
        {
            //更新批次运行状态为出错
            batchRS.setState(RunningState.ERROR);
            batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRS);
        }
        else
        {
            //更新批次运行状态为成功
            batchRS.setState(RunningState.SUCCESS);
            batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRS);
        }
        
        Thread.currentThread().setName(threadName);
    }
    
    /**
     * 执行任务的步骤,返回运行状态
     */
    private int execTaskStep(BatchRunningStateEntity batchRS, TaskStepEntity stepSE, StepRunningStateEntity stepRS)
        throws CException
    {
        String threadName = Thread.currentThread().getName();
        
        //设置运行步骤的步骤Id
        stepRS.setStepId(stepSE.getStepId());
        
        //创建步骤超时处理器
        stepTimeOut = new StepTimeOutProcessor(stepSE.getTimeoutMinutes(), stepRS);
        //步骤默认为失败
        boolean bStepError = true;
        try
        {
            //stepRS对象在stepTimeOut与当前对象中共享
            stepTimeOut.setBatchRS(batchRS);
            
            //更新步骤为开始状态
            stepRS.setState(RunningState.START);
            stepRS.setRunningStartTime(CommonUtils.getCrruentTime());
            tccDao.updateStepRunningState(stepRS);
            
            //后续不更新起始时间
            stepRS.setRunningStartTime(null);
            
            //拼接可执行命令
            String command =
                String.format(TccConfig.getExecuteCmdTemplate(),
                    stepSE.getCommand(),
                    batchRS.getJobInput(),
                    this.startCycleId,
                    batchRS.getCycleId());
            //替换变量
            command = replaceVar(command);
            int retryCount = null == stepRS.getRetryCount() ? 0 : stepRS.getRetryCount();
            
            //命令返回结果
            Map<String, Object> exitStatusMap = null;
            while (retryCount < stepSE.getAllowRetryCount())
            {
                //退出点
                if (bStop)
                {
                    return RunningState.STOP;
                }
                
                //修改线程的名字
                Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d_%d_%d",
                    batchRS.getTaskId(),
                    batchRS.getCycleId(),
                    batchRS.getBatchId(),
                    stepRS.getStepId(),
                    retryCount));
                
                if (bTimeout)
                {
                    LOGGER.warn("execute stepRS[{}] timeout. have executed [{}] times", stepRS, retryCount);
                    break;
                }
                
                try
                {
                    //执行远程命令
                    LOGGER.debug("start to exec remotecmd[{}] in conInfo[{}]. stepRS is[{}]", new Object[] {command,
                        sshConnect.getConInfo(), stepRS});
                    
                    //如果是虚拟执行，执行空壳，更新状态为虚拟
                    if (ExecuteType.VISUAL == this.execType)
                    {
                        exitStatusMap = new HashMap<String, Object>();
                        exitStatusMap.put("exitstatus", 0);
                    }
                    else
                    {
                        //执行命令
                        SSHCommand sshCommand = sshConnect.createCommand(CommandType.HIVE_CMD, command);
                        if (sshCommand instanceof RHiveCommand)
                        {
                            hiveCommand = (RHiveCommand)sshCommand;
                            
                            //退出点
                            if (bStop)
                            {
                                return RunningState.STOP;
                            }
                            
                            hiveCommand.execute(stepRS);
                        }
                        else
                        {
                            LOGGER.error("sshCommand as RHiveCommand Type error!");
                        }
                    }
                    
                    //退出点
                    if (bStop)
                    {
                        LOGGER.debug("stepRS[{}] stop sucessed.", stepRS, retryCount);
                        return RunningState.STOP;
                    }
                    
                    if (bTimeout)
                    {
                        LOGGER.warn("execute stepRS[{}] timeout. have executed [{}] times", stepRS, retryCount);
                        break;
                    }
                    
                    //执行远程命令完成
                    LOGGER.debug("finish to exec remotecmd[{}] on retryTimes[{}]. stepRS is[{}]", new Object[] {
                        command, retryCount, stepRS});
                    bStepError = false;
                    break;
                }
                catch (ExecuteException ex)
                {
                    if (bTimeout)
                    {
                        LOGGER.warn("execute stepRS[{}] timeout. have executed [{}] times", stepRS, retryCount);
                        break;
                    }
                    else if (RunningState.STOP == ex.getState())
                    {
                        //脚本停止执行，则直接退出
                        LOGGER.debug("stepRS[{}] stop sucessed.", stepRS, retryCount);
                        return RunningState.STOP;
                    }
                    else
                    {
                        LOGGER.error("exec step[batchRS={},stepSE={}] error!", new Object[] {batchRS, stepSE, ex});
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("exec step[batchRS={},stepSE={}] error!", new Object[] {batchRS, stepSE, e});
                }
                
                retryCount++;
                
                //更新重试次数
                stepRS.setRetryCount(retryCount);
                tccDao.updateStepRunningState(stepRS);
                
                LOGGER.debug("stepRS[{}] retry to execute [{}] times", stepRS, retryCount);
            }
        }
        catch (Throwable e)
        {
            bStepError = true;
            LOGGER.error("execTaskStep stepRS=[{}] failed!", stepRS, e);
        }
        finally
        {
            if (!bTimeout)
            {
                //步骤执行完毕，请求不要使用超时处理
                stepTimeOut.finished();
                
                //清除超时步骤处理器
                clearStepTimeOuts();
            }
        }
        
        Thread.currentThread().setName(threadName);
        
        if (bTimeout)
        {
            //更新步骤的运行状态为超时
            stepRS.setState(RunningState.TIMEOUT);
            stepRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateStepRunningState(stepRS);
            
            return RunningState.TIMEOUT;
        }
        
        //重试多次后仍然出错
        if (bStepError)
        {
            //更新步骤运行状态为出错
            stepRS.setState(RunningState.ERROR);
            stepRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateStepRunningState(stepRS);
        }
        else
        {
            //更新步骤运行状态为成功
            stepRS.setState(RunningState.SUCCESS);
            stepRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateStepRunningState(stepRS);
        }
        
        return true == bStepError ? RunningState.ERROR : RunningState.SUCCESS;
    }
    
    /** 
     * 执行一个单一的批次任务
     * @param batchRS 任务批次运行状态
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public void execSingleBatchTask(BatchRunningStateEntity batchRS)
        throws Exception
    {
        if (null == batchRS)
        {
            return;
        }
        
        String threadName = Thread.currentThread().getName();
        //修改线程的名字
        Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d",
            batchRS.getTaskId(),
            batchRS.getCycleId(),
            batchRS.getBatchId()));
        
        //清除原有的批次步骤的运行状态信息(可能是重做请求),init()中已经删除！
        //初始化批次步骤状态信息
        initStepRunningState(batchRS);
        
        //更新批次的运行状态为开始
        batchRS.setState(RunningState.START);
        batchRS.setRunningStartTime(CommonUtils.getCrruentTime());
        tccDao.updateBatchRunningState(batchRS);
        //后续不更新启动时间
        batchRS.setRunningStartTime(null);
        
        Long taskId = batchRS.getTaskId();
        String cycleId = batchRS.getCycleId();
        Integer batchId = batchRS.getBatchId();
        
        if (null == stepKeyMap)
        {
            //更新批次运行状态为成功
            batchRS.setState(RunningState.SUCCESS);
            batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRS);
            return;
        }
        
        StepRunningStateEntity stepRS = new StepRunningStateEntity();
        stepRS.setTaskId(taskId);
        stepRS.setCycleId(cycleId);
        stepRS.setBatchId(batchId);
        for (TaskStepEntity stepSE : stepKeyMap.values())
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d_%d",
                batchRS.getTaskId(),
                batchRS.getCycleId(),
                batchRS.getBatchId(),
                stepSE.getStepId()));
            
            //退出点
            if (bStop)
            {
                return;
            }
            
            stepRS.setStepId(stepSE.getStepId());
            
            try
            {
                //步骤的执行退出状态
                int exitStatus = execTaskStep(batchRS, stepSE, stepRS);
                if (exitStatus == RunningState.ERROR)
                {
                    bBatchError = true;
                    break;
                }
                else if (exitStatus == RunningState.STOP)
                {
                    return;
                }
                else if (exitStatus == RunningState.TIMEOUT)
                {
                    //更新批次的运行状态为超时
                    batchRS.setState(RunningState.TIMEOUT);
                    batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
                    tccDao.updateBatchRunningState(batchRS);
                    return;
                }
            }
            catch (Throwable e)
            {
                LOGGER.error("exec step[batchRS={},stepSE={}] error!", new Object[] {batchRS, stepSE, e});
                bBatchError = true;
                break;
            }
        }
        //修改线程的名字
        Thread.currentThread().setName(String.format("CycleTaskMain_%d_%s_%d",
            batchRS.getTaskId(),
            batchRS.getCycleId(),
            batchRS.getBatchId()));
        
        //批次运行状态为出错
        if (bBatchError)
        {
            //更新批次运行状态为出错
            batchRS.setState(RunningState.ERROR);
            batchRS.setRunningStartTime(null);
            batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRS);
        }
        else
        {
            //更新批次运行状态为成功
            batchRS.setState(RunningState.SUCCESS);
            batchRS.setRunningEndTime(CommonUtils.getCrruentTime());
            tccDao.updateBatchRunningState(batchRS);
        }
        
        Thread.currentThread().setName(threadName);
    }
    
    /**
     * 将当前任务周期添加到所依赖的所有的周期任务的 “已启动依赖任务ID列表”或者“已完成依赖任务ID列表”。
     * 由于并发性操作很高，为了防止读取脏数据，运行“已启动依赖任务ID列表”或者“已完成依赖任务ID列表”中包含重复项，
     * 所以该方法运行重复执行。
     */
    private void addcurrCycleTask2DependTaskList(boolean bBegin)
        throws CException
    {
        LOGGER.debug("enter addcurrCycleTask2DependTaskList(),bBegin is [{}],cycleDepRLst is [{}]",
            bBegin,
            cycleDepRLst);
        if (null != cycleDepRLst)
        {
            //要添加的任务Id和周期id字符串：“Task_ID, Cycle_id;”
            String appendTaskCycleId = String.format("%d,%s;", taskRS.getTaskId(), taskRS.getCycleId());
            
            if (bBegin)
            {
                for (CycleDependRelation cycleDepR : cycleDepRLst)
                {
                    //退出点
                    if (bStop)
                    {
                        return;
                    }
                    
                    tccDao.updateAppendBeginDependTaskList(cycleDepR.getDependTaskId(),
                        cycleDepR.getDependCycleId(),
                        appendTaskCycleId);
                }
            }
            else
            {
                for (CycleDependRelation cycleDepR : cycleDepRLst)
                {
                    //退出点
                    if (bStop)
                    {
                        return;
                    }
                    
                    tccDao.updateAppendFinishDependTaskList(cycleDepR.getDependTaskId(),
                        cycleDepR.getDependCycleId(),
                        appendTaskCycleId);
                }
            }
        }
        LOGGER.debug("exit addcurrCycleTask2DependTaskList(), bBegin is [{}], cycleDepRLst is [{}]",
            bBegin,
            cycleDepRLst);
    }
    
    /**
     * 清除批次的所有步骤的超时处理器,线程同步
     */
    private void clearStepTimeOuts()
    {
        if (null != stepTimeOut)
        {
            //取消还没有开始运行的定时器
            stepTimeOut.cancel();
        }
    }
    
    /**
     * 初始化批次的步骤运行信息
     * @throws CException 
     */
    private void initStepRunningState(BatchRunningStateEntity batchRS)
        throws CException
    {
        LOGGER.debug("enter initStepRunningState(), param is {}", batchRS);
        //删除以前初始化未完成的状态记录(中途停机)
        tccDao.deleteStepRunningStates(batchRS.getTaskId(), batchRS.getCycleId(), batchRS.getBatchId());
        
        StepRunningStateEntity stepRS = new StepRunningStateEntity();
        stepRS.setTaskId(batchRS.getTaskId());
        stepRS.setCycleId(batchRS.getCycleId());
        stepRS.setBatchId(batchRS.getBatchId());
        
        if (null == stepKeyMap || null == stepKeyMap.values())
        {
            LOGGER.debug("exit initStepRunningState(), because task have no step. batchRS is [{}]", batchRS);
            return;
        }
        
        //初始化批次步骤运行记录
        for (TaskStepEntity taskStep : stepKeyMap.values())
        {
            //退出点
            if (bStop || bTimeout)
            {
                return;
            }
            
            stepRS.setStepId(taskStep.getStepId());
            stepRS.setState(RunningState.INIT);
            tccDao.addStepRunningState(stepRS);
        }
        LOGGER.debug("exit initStepRunningState(), param is {}", batchRS);
    }
    
    /**
     * 是否忽略错误依赖任务启动
     */
    private boolean ignoreErrorStart(boolean dependOk, List<CycleDependRelation> cycleDepRealationLst, Long taskId)
        throws CException
    {
        if (null == cycleDepRealationLst || !dependOk)
        {
            return false;
        }
        
        List<TaskRunningStateEntity> taskIdCycleIDStateLst = new ArrayList<TaskRunningStateEntity>();
        TaskRunningStateEntity taskRSE;
        for (CycleDependRelation cycleDepR : cycleDepRealationLst)
        {
            //如果忽略依赖任务执行出错的状态
            if (cycleDepR.isIgnoreError())
            {
                taskRSE = new TaskRunningStateEntity();
                taskRSE.setTaskId(cycleDepR.getDependTaskId());
                taskRSE.setCycleId(cycleDepR.getDependCycleId());
                taskRSE.setState(RunningState.ERROR);
                taskIdCycleIDStateLst.add(taskRSE);
            }
        }
        //存在且状态出错的周期数
        int errorNum = tccDao.getTaskRunningStateCount(taskIdCycleIDStateLst);
        
        //依赖任务中存在错误状态的周期
        return errorNum > 0;
    }
    
    /**
     * 停止正在执行的周期任务
     */
    public void stopCycleTask()
    {
        this.bStop = true;
        this.state = RunningState.STOP;
        
        stop();
        
        LOGGER.debug("stop CycleTask[taskId=[{}],cycleId=[{}]] sucessed.", taskRS.getTaskId(), taskRS.getCycleId());
    }
    
    private void stop()
    {
        try
        {
            //取消还没有开始运行的超时定时器
            if (null != taskRSTimeOutProc)
            {
                taskRSTimeOutProc.finished();
                taskRSTimeOutProc.cancel();
            }
            
            //取消还没有开始运行的最晚结束定时器
            if (null != taskRSLatestEndProc)
            {
                taskRSLatestEndProc.finished();
                taskRSLatestEndProc.cancel();
            }
            
            //不要继续等待批文件输入
            if (null != lsCommand)
            {
                lsCommand.stop();
            }
            
            //从休眠中唤醒
            if (null != waittingFilesThread && !waittingFilesThread.isInterrupted())
            {
                waittingFilesThread.interrupt();
            }
            
            //从休眠中唤醒
            if (null != mainThread && !mainThread.isInterrupted())
            {
                mainThread.interrupt();
            }
            
            //清理超时处理器
            clearStepTimeOuts();
            
            //执行hadoop的kill远程命令的主机
            if (null != hiveCommand)
            {
                hiveCommand.stop();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
        }
        finally
        {
            //释放连接
            if (null != sshConnect)
            {
                sshManager.releaseConnect(sshConnect, false);
            }
        }
    }
    
    /**
     * 根据优先级比较大小
     * @param other 周期任务对象
     * @return 优先级的差值
     */
    @Override
    public int compareTo(CycleTask other)
    {
        //优先级越小排越前
        int value = other.getPriority() - this.getPriority();
        if (0 == value)
        {
            
            value = this.getTaskWeight() - other.getTaskWeight();
        }
        
        return value;
    }
    
    /**
     * 
     * 等待输入的处理执行器
     * 
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
     */
    public class WaittingInputExcutor implements Runnable
    {
        
        @Override
        public void run()
        {
            try
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("WaittingInputExcutor_%d_%s",
                    taskRS.getTaskId(),
                    taskRS.getCycleId()));
                LOGGER.debug("cycleTask[taskId=[{}],cycleId=[{}]].waittingInput started.", getTaskId(), getCycleId());
                waittingInput();
                LOGGER.debug("cycleTask[taskId=[{}],cycleId=[{}]].waittingInput finished.", getTaskId(), getCycleId());
            }
            catch (Throwable e)
            {
                LOGGER.warn("WaittingInputExcutor.run failed, taskRS is [{}]!", taskRS, e);
                bStop = true;
                updateTaskRSError(taskRS);
            }
        }
    }
    
    /**
     * 
     * 单批次任务执行器
     * 
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
     * @see  [相关类/方法]
     */
    public class SingleBatchTaskExcutor implements Runnable
    {
        private BatchRunningStateEntity batchRS;
        
        /**
         * 构造函数
         * @param batchRS 批次运行状态
         */
        public SingleBatchTaskExcutor(BatchRunningStateEntity batchRS)
        {
            this.batchRS = batchRS;
        }
        
        @Override
        public void run()
        {
            try
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("SingleBatchTaskExcutor_%d_%s_%d",
                    batchRS.getTaskId(),
                    batchRS.getCycleId(),
                    batchRS.getBatchId()));
                
                LOGGER.debug("batchRS[{}] started to run", batchRS);
                execSingleBatchTask(batchRS);
                LOGGER.debug("batchRS[{}] finished to run", batchRS);
            }
            catch (Throwable e)
            {
                //记录日志
                LOGGER.error("SingleBatchTaskExcutor.run execute failed, batchRS is [{}]!", batchRS, e);
                updateBatchRSError(batchRS);
            }
        }
    }
    
    /**
     * 
     * 继续运行批任务的执行器
     * 
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
     * @see  [相关类/方法]
     */
    public class ContinueBatchTaskExcutor implements Runnable
    {
        private BatchRunningStateEntity batchRS;
        
        /**
         * 构造函数
         * @param batchRS 批次任务状态
         */
        public ContinueBatchTaskExcutor(BatchRunningStateEntity batchRS)
        {
            this.batchRS = batchRS;
        }
        
        @Override
        public void run()
        {
            try
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("ContinueBatchTaskExcutor_%d_%s_%d",
                    batchRS.getTaskId(),
                    batchRS.getCycleId(),
                    batchRS.getBatchId()));
                
                LOGGER.debug("continous batchRS[{}] started to run", batchRS);
                continueStartedBatchTask(batchRS);
                LOGGER.debug("continous batchRS[{}] finished to run", batchRS);
            }
            catch (Throwable e)
            {
                //记录日志
                LOGGER.error("continue Started BatchTask failed, batchRS is [{}]!", batchRS, e);
                updateBatchRSError(batchRS);
            }
        }
    }
    
    /**
     * 
     * 延迟告警处理器
     * 
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
     */
    public class DelayAlarmProcessor extends TimerTask
    {
        //超时的毫秒数
        private Long timeoutMillSeconds;
        
        //告警类型
        private int alarmType;
        
        //受到监控的线程是否执行完毕
        private boolean bFinished;
        
        /**
         * 构造函数
         * @param timeoutMillSeconds 超时的分钟数，只允许为大于1的正数，默认30
         * @param alarmType 告警类型
         */
        public DelayAlarmProcessor(Long timeoutMillSeconds, int alarmType)
        {
            //timeoutMinutes只允许为大于1的正数
            if (null == timeoutMillSeconds || timeoutMillSeconds < 0)
            {
                this.timeoutMillSeconds = 1L;
            }
            else
            {
                this.timeoutMillSeconds = timeoutMillSeconds;
            }
            
            this.alarmType = alarmType;
            
            this.bFinished = false;
            //timeoutMillSeconds毫秒后启动超时检查
            long delay = this.timeoutMillSeconds;
            timer.schedule(this, delay);
        }
        
        @Override
        public void run()
        {
            try
            {
                if (!bStop && !bFinished)
                {
                    LOGGER.debug("DelayAlarmProcessor[taskId={},cycleId={}] run. started to Alarm.",
                        taskRS.getTaskId(),
                        taskRS.getCycleId());
                    sendAlarm(this.alarmType);
                    //清除超时步骤处理器
                    this.cancel();
                }
            }
            catch (Throwable e)
            {
                LOGGER.debug("DelayAlarmProcessor[taskId={},cycleId={}] run failed!", new Object[] {taskRS.getTaskId(),
                    taskRS.getCycleId(), e});
            }
            
        }
        
        /**
         * 受监控线程是否处理完毕
         */
        public void finished()
        {
            this.bFinished = true;
        }
    }
    
    /**
     * 
     * 步骤超时处理器
     * 
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
     */
    public class StepTimeOutProcessor extends TimerTask
    {
        //超时的分钟数
        private Integer timeoutMinutes;
        
        //受到监控的线程是否执行完毕
        private boolean bFinished;
        
        //当前运行批次
        private StepRunningStateEntity stepRS;
        
        //当前运行步骤
        private BatchRunningStateEntity batchRS;
        
        /**
         * 构造函数
         * @param timeoutMinutes 超时的分钟数，只允许为大于1的正数，默认30
         * @param stepRS 当前运行批次
         */
        public StepTimeOutProcessor(Integer timeoutMinutes, StepRunningStateEntity stepRS)
        {
            //timeoutMinutes只允许为大于1的正数
            if (null == timeoutMinutes || timeoutMinutes < 0)
            {
                this.timeoutMinutes = TccConfig.DEFAULT_TIMEOUT_MINUTES;
            }
            else
            {
                this.timeoutMinutes = timeoutMinutes;
            }
            bTimeout = false;
            this.bFinished = false;
            this.stepRS = stepRS;
            //timeoutMinutes分钟后启动超时检查
            int timeoutMin = null == this.timeoutMinutes ? 0 : this.timeoutMinutes;
            
            long delay = 1;
            if (timeoutMin > 0)
            {
                delay = timeoutMin * TccConfig.MILLS_PER_MINUTE;
            }
            
            timer.schedule(this, delay);
        }
        
        @Override
        public void run()
        {
            bTimeout = true;
            state = RunningState.TIMEOUT;
            
            try
            {
                //修改线程的名字
                if (null != stepRS)
                {
                    Thread.currentThread().setName(String.format("StepTimeOutProcessor_%d_%s_%d_%d",
                        stepRS.getTaskId(),
                        stepRS.getCycleId(),
                        stepRS.getBatchId(),
                        stepRS.getStepId()));
                }
                
                LOGGER.debug("stepRS[{}] run timeout. started to stop.", stepRS);
                if (!bStop && !bFinished)
                {
                    if (null != stepRS)
                    {
                        stop();
                    }
                }
                
                LOGGER.debug("stepRS[{}] run timeout. finished to stop.", stepRS);
            }
            catch (Throwable e)
            {
                LOGGER.error("stepRS[{}] run timeout. stop failed!", stepRS, e);
                if (null != stepRS)
                {
                    updateStepRSError(stepRS);
                }
                
                if (null != batchRS)
                {
                    updateBatchRSError(batchRS);
                }
            }
            
        }
        
        /**
         * 受监控线程是否处理完毕
         */
        public void finished()
        {
            this.bFinished = true;
        }
        
        /**
         * 设置当前的批次运行状态
         * @param batchRS 对batchRS进行赋值
         */
        public void setBatchRS(BatchRunningStateEntity batchRS)
        {
            this.batchRS = batchRS;
        }
        
        /**
         * 设置当前的步骤运行状态
         * @param stepRS 对stepRS进行赋值
         */
        public void setStepRS(StepRunningStateEntity stepRS)
        {
            this.stepRS = stepRS;
        }
    }
    
    @Override
    public String toString()
    {
        return String.format("%d,%s,%d", this.getTaskId(), this.getCycleId(), this.getState());
    }
    
    /**
     * 获取哈希码
     * @return hashcode
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taskRS == null) ? 0 : taskRS.hashCode());
        return result;
    }
    
    /**
     * 判断对象是否相等
     * @param obj 待比较对象
     * @return 判断对象是否相等
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        CycleTask other = (CycleTask)obj;
        if (taskRS == null)
        {
            if (other.taskRS != null)
            {
                return false;
            }
        }
        else if (!taskRS.equals(other.taskRS))
        {
            return false;
        }
        return true;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    
    public String getStartCycleId()
    {
        return startCycleId;
    }
    
    public ISendAlarming getAlraming()
    {
        return alarming;
    }
    
    public void setAlraming(ISendAlarming alraming)
    {
        this.alarming = alraming;
    }
    
    public SSHManager getSshManager()
    {
        return sshManager;
    }
    
    public void setSshManager(SSHManager sshManager)
    {
        this.sshManager = sshManager;
    }
}
