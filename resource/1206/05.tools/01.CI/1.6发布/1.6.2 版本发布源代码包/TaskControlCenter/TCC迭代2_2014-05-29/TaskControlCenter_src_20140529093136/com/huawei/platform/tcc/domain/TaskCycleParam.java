/*
 * 文 件 名:  TaskCycleParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

import java.util.Date;

/**
 * 最小最大周期
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class TaskCycleParam
{
    private Long taskId;
    
    private String minCycleId;
    
    private String maxCycleId;
    
    private Date startDate;
    
    private Date endDate;
    
    private Integer state;
    
    private Date modifyTime;

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    /**
     * 获取最小周期Id
     * @return 最小周期Id
     */
    public String getMinCycleId()
    {
        return minCycleId;
    }

    /**
     * 设置最小周期Id
     * @param minCycleId 最小周期Id
     */
    public void setMinCycleId(String minCycleId)
    {
        this.minCycleId = minCycleId;
    }

    /**
     * 获取最大周期Id
     * @return 最大周期Id
     */
    public String getMaxCycleId()
    {
        return maxCycleId;
    }

    /**
     * 设置最大周期Id
     * @param maxCycleId 最大周期Id
     */
    public void setMaxCycleId(String maxCycleId)
    {
        this.maxCycleId = maxCycleId;
    }

    /**
     * 获取任务Id
     * @return 任务Id
     */
    public Long getTaskId()
    {
        return taskId;
    }

    /**
     * 设置任务Id
     * @param taskId 任务Id
     */
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    /**
     * 获取任务周期状态
     * @return 任务周期状态
     */
    public Integer getState()
    {
        return state;
    }

    /**
     * 设置任务周期状态
     * @param state 状态
     */
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Date getModifyTime()
    {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime)
    {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskCycleParam [taskId=");
        builder.append(taskId);
        builder.append(", minCycleId=");
        builder.append(minCycleId);
        builder.append(", maxCycleId=");
        builder.append(maxCycleId);
        builder.append(", state=");
        builder.append(state);
        builder.append(", modifyTime=");
        builder.append(modifyTime);
        builder.append("]");
        return builder.toString();
    }
}
