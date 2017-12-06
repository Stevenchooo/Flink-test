/* 文 件 名:  LongTimeShellEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 执行时间脚本
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-20]
 */
public class LongTimeShellEntity
{
    private Integer stepId;
    
    private String cycleId;
    
    private Long taskId;
    
    private Date runningStartTime;
    
    private Date runningEndTime;
    
    private String taskName;
    
    private String command;

    public String getCycleId()
    {
        return cycleId;
    }

    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
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

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public Integer getStepId()
    {
        return stepId;
    }

    public void setStepId(Integer stepId)
    {
        this.stepId = stepId;
    }
    
}