/*
 * 文 件 名:  Cycle.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 20013-2014,  All rights reserved
 * 描    述:  周期
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-22
 */
package com.huawei.platform.tcc.cycle;

import java.util.Date;
import java.util.List;

import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.entity.InstanceRelationEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;

/**
 * 周期
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-22]
 */
public class Cycle
{
    //任务运行状态
    private TaskRunningStateEntity taskRS;
    
    //前驱关系
    private List<CycleRelation> preCycleRs;
    
    //后继关系
    private List<CycleRelation> subCycleRs;
    
    //是否有更多的后继关系
    private boolean moreSubRs;
    
    //是否有更多的前驱关系
    private boolean morePreRs;
    
    //是否构建不存在的前驱
    private boolean conNExistPreCycle;
    
    //是否构建不存在的后继
    private boolean conNExistSubCycle;
    
    //是否非当天调度
    private boolean otherDaySchedule;
    
    //期待执行日期
    private Date expectExecuteDate;
    
    /**
     * 构造函数
     * @param taskRS 任务运行状态
     */
    public Cycle(TaskRunningStateEntity taskRS)
    {
        this.taskRS = taskRS;
    }
    
    /**
     * 构造函数
     * @param taskRS 任务运行状态
     * @param conNExistSubCycle 是否构建不存在的后继
     */
    public Cycle(TaskRunningStateEntity taskRS, boolean conNExistSubCycle)
    {
        this.taskRS = taskRS;
        this.conNExistSubCycle = conNExistSubCycle;
    }
    
    public Date getExpectExecuteDate()
    {
        return expectExecuteDate;
    }

    public void setExpectExecuteDate(Date expectExecuteDate)
    {
        this.expectExecuteDate = expectExecuteDate;
    }

    public boolean isOtherDaySchedule()
    {
        return otherDaySchedule;
    }
    
    public void setOtherDaySchedule(boolean otherDaySchedule)
    {
        this.otherDaySchedule = otherDaySchedule;
    }
    
    public boolean isMoreSubRs()
    {
        return moreSubRs;
    }
    
    public void setMoreSubRs(boolean moreSubRs)
    {
        this.moreSubRs = moreSubRs;
    }
    
    public boolean isMorePreRs()
    {
        return morePreRs;
    }
    
    public void setMorePreRs(boolean morePreRs)
    {
        this.morePreRs = morePreRs;
    }
    
    public boolean isConNExistPreCycle()
    {
        return conNExistPreCycle;
    }
    
    public void setConNExistPreCycle(boolean conNExistPreCycle)
    {
        this.conNExistPreCycle = conNExistPreCycle;
    }
    
    public boolean isConNExistSubCycle()
    {
        return conNExistSubCycle;
    }
    
    public void setConNExistSubCycle(boolean conNExistSubCycle)
    {
        this.conNExistSubCycle = conNExistSubCycle;
    }
    
    /**
     * 将周期转化为实例关系
     * @param currentDay 当天
     * @return 实例关系实体
     */
    public InstanceRelationEntity cov2InstanceRelation(Date currentDay)
    {
        InstanceRelationEntity instanceRel = new InstanceRelationEntity();
        instanceRel.setScheduleDate(currentDay);
        instanceRel.setTaskId(taskRS.getTaskId());
        instanceRel.setCycleId(taskRS.getCycleId());
        instanceRel.setPreDependentList(getPreDependentList());
        instanceRel.setSubDependentList(getSubDependentList());
        instanceRel.setExpectExecuteDate(expectExecuteDate);
        return instanceRel;
    }
    
    private String getSubDependentList()
    {
        StringBuilder sbSub = new StringBuilder();
        
        if (null != subCycleRs && !subCycleRs.isEmpty())
        {
            TaskRunningStateEntity taskRSE;
            for (CycleRelation cycleRelation : subCycleRs)
            {
                taskRSE = cycleRelation.getDstCycle().getTaskRS();
                sbSub.append(taskRSE.getTaskId());
                sbSub.append(',');
                sbSub.append(taskRSE.getCycleId());
                sbSub.append(',');
                sbSub.append(cycleRelation.isIgnoreError() ? 1 : 0);
                sbSub.append(';');
            }
            
            if (isMoreSubRs())
            {
                sbSub.append("...;");
            }
        }
        
        return sbSub.toString();
    }
    
    private String getPreDependentList()
    {
        StringBuilder sbPre = new StringBuilder();
        
        if (null != preCycleRs && !preCycleRs.isEmpty())
        {
            TaskRunningStateEntity taskRSE;
            for (CycleRelation cycleRelation : preCycleRs)
            {
                taskRSE = cycleRelation.getSrcCycle().getTaskRS();
                sbPre.append(taskRSE.getTaskId());
                sbPre.append(',');
                sbPre.append(taskRSE.getCycleId());
                sbPre.append(',');
                sbPre.append(cycleRelation.isIgnoreError() ? 1 : 0);
                sbPre.append(';');
            }
            if (isMorePreRs())
            {
                sbPre.append("...;");
            }
        }
        
        return sbPre.toString();
    }
    
    /**
     * 是否运行成功
     * @return 是否运行成功
     */
    public boolean isSuccessed()
    {
        if (null == taskRS || null != taskRS.getState())
        {
            return false;
        }
        
        return RunningState.SUCCESS == taskRS.getState();
    }
    
    /**
     * 是否可以参与调度
     * @return 是否可以参与调度
     */
    public boolean canSchedule()
    {
        if (null == taskRS || null == taskRS.getState())
        {
            return false;
        }
        
        int state = taskRS.getState();
        if (RunningState.INIT == state || RunningState.START == state || RunningState.WAITTINGRUN == state
            || RunningState.RUNNING == state || RunningState.WAIT_NEXT_NODE_EXE == state)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否不依赖其它周期
     * @return 是否不依赖其它周期
     */
    public boolean noDepends()
    {
        return null == subCycleRs || subCycleRs.isEmpty();
    }
    
    /**
     * 是否所有的依赖OK
     * @return 是否所有的依赖OK
     */
    public boolean isDependsOk()
    {
        if (null != subCycleRs && !subCycleRs.isEmpty())
        {
            for (CycleRelation cycleRelation : subCycleRs)
            {
                //只要有一个不满足、依赖关系就不满足
                if (!cycleRelation.isOK())
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 是否是忽略错误运行
     * @return 是否是忽略错误运行
     */
    public boolean ignoreErrorRun()
    {
        //依赖满足
        if (!isDependsOk())
        {
            return false;
        }
        
        if (null != subCycleRs && !subCycleRs.isEmpty())
        {
            for (CycleRelation cycleRelation : subCycleRs)
            {
                //只要有一个满足就认为是忽略错误运行
                if (cycleRelation.isRealIgnoreError())
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public TaskRunningStateEntity getTaskRS()
    {
        return taskRS;
    }
    
    public List<CycleRelation> getPreCycleRs()
    {
        return preCycleRs;
    }
    
    public void setPreCycleRs(List<CycleRelation> preCycleRs)
    {
        this.preCycleRs = preCycleRs;
    }
    
    public List<CycleRelation> getSubCycleRs()
    {
        return subCycleRs;
    }
    
    public void setSubCycleRs(List<CycleRelation> subCycleRs)
    {
        this.subCycleRs = subCycleRs;
    }
    
    public void setTaskRS(TaskRunningStateEntity taskRS)
    {
        this.taskRS = taskRS;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taskRS == null) ? 0 : taskRS.hashCode());
        return result;
    }
    
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
        Cycle other = (Cycle)obj;
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
}
