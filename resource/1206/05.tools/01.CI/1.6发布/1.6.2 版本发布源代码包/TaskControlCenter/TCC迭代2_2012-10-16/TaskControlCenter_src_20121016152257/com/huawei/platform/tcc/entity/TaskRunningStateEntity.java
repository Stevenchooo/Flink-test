/* 文 件 名:  TaskRunningStateEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 任务运行状态
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-20]
 * @see  
 */
public class TaskRunningStateEntity
{
    private String cycleId;
    
    private Long taskId;
    
    private Integer state;
    
    private Date runningStartTime;
    
    private Date runningEndTime;
    
    private String beginDependTaskList;
    
    private String finishDependTaskList;
    
    private Integer returnTimes;
    
    private boolean hasAlarmLatestStart;
    
    public String getCycleId()
    {
        return cycleId;
    }
    
    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId == null ? null : cycleId.trim();
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public Integer getState()
    {
        return state;
    }
    
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    public Date getRunningStartTime()
    {
        return runningStartTime;
    }
    
    public void setRunningStartTime(Date runningStartTime)
    {
        this.runningStartTime = runningStartTime;
    }
    
    public Date getRunningEndTime()
    {
        return runningEndTime;
    }
    
    public void setRunningEndTime(Date runningEndTime)
    {
        this.runningEndTime = runningEndTime;
    }
    
    public String getBeginDependTaskList()
    {
        return beginDependTaskList;
    }
    
    public void setBeginDependTaskList(String beginDependTaskList)
    {
        this.beginDependTaskList = beginDependTaskList == null ? null : beginDependTaskList.trim();
    }
    
    public String getFinishDependTaskList()
    {
        return finishDependTaskList;
    }
    
    public void setFinishDependTaskList(String finishDependTaskList)
    {
        this.finishDependTaskList = finishDependTaskList == null ? null : finishDependTaskList.trim();
    }

    /**
     * 字符串表示
     * @return 字符串
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskRunningStateEntity [cycleId=");
        builder.append(cycleId);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", state=");
        builder.append(state);
        builder.append(", runningStartTime=");
        builder.append(runningStartTime);
        builder.append(", runningEndTime=");
        builder.append(runningEndTime);
        builder.append(", beginDependTaskList=");
        builder.append(beginDependTaskList);
        builder.append(", finishDependTaskList=");
        builder.append(finishDependTaskList);
        builder.append("]");
        return builder.toString();
    }

    /**
     * 获取对象的hashcode码
     * @return 对象的hashcode码
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cycleId == null) ? 0 : cycleId.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        return result;
    }

    /**
     * 比较对象是否相等
     * @param obj 比较对象
     * @return 是否相等
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
        TaskRunningStateEntity other = (TaskRunningStateEntity)obj;
        if (cycleId == null)
        {
            if (other.cycleId != null)
            {
                return false;
            }
        }
        else if (!cycleId.equals(other.cycleId))
        {
            return false;
        }
        if (taskId == null)
        {
            if (other.taskId != null)
            {
                return false;
            }
        }
        else if (!taskId.equals(other.taskId))
        {
            return false;
        }
        return true;
    }

    public Integer getReturnTimes()
    {
        return returnTimes;
    }

    public void setReturnTimes(Integer returnTimes)
    {
        this.returnTimes = returnTimes;
    }

    public boolean isHasAlarmLatestStart()
    {
        return hasAlarmLatestStart;
    }

    public void setHasAlarmLatestStart(boolean hasAlarmLatestStart)
    {
        this.hasAlarmLatestStart = hasAlarmLatestStart;
    }
}