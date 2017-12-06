/* 文 件 名:  TaskAlarmThresholdEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;


/**
 * 任务告警阈值信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class TaskAlarmThresholdEntity
{
    private Integer taskId;
    
    private String latestStartTime;
    
    private String latestEndTime;
    
    private Integer maxRunTime;
    
    public Integer getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
    
    public Integer getMaxRunTime()
    {
        return maxRunTime;
    }
    
    public void setMaxRunTime(Integer maxRunTime)
    {
        this.maxRunTime = maxRunTime;
    }

    public String getLatestStartTime()
    {
        return latestStartTime;
    }

    public void setLatestStartTime(String latestStartTime)
    {
        this.latestStartTime = latestStartTime;
    }

    public String getLatestEndTime()
    {
        return latestEndTime;
    }

    public void setLatestEndTime(String latestEndTime)
    {
        this.latestEndTime = latestEndTime;
    }
}