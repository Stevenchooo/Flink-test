/*
 * 文 件 名:  MinMaxCycle.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 20013-2014,  All rights reserved
 * 描    述:  任务最大最小周期Id
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-22
 */
package com.huawei.platform.tcc.domain;

/**
 * 任务最大最小周期Id
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-22]
 */
public class TaskMinMaxCycle
{
    private Long taskId;
    
    private String minCycleId;
    
    private String maxCycleId;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public String getMinCycleId()
    {
        return minCycleId;
    }

    public void setMinCycleId(String minCycleId)
    {
        this.minCycleId = minCycleId;
    }

    public String getMaxCycleId()
    {
        return maxCycleId;
    }

    public void setMaxCycleId(String maxCycleId)
    {
        this.maxCycleId = maxCycleId;
    }
}
