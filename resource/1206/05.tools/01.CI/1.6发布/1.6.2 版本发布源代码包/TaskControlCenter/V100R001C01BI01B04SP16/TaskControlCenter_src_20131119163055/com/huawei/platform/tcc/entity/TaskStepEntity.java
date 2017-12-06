/*
 * 文 件 名:  TaskStepEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-2
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 
 * 任务步骤实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
 * @see  [相关类/方法]
 */
public class TaskStepEntity
{
    private Integer stepId;
    
    private Long taskId;
    
    private String stepName;
    
    private String stepDesc;
    
    private Integer stepTaskType;
    
    private Boolean stepEnableFlag;
    
    private Integer allowRetryCount;
    
    private Integer timeoutMinutes;
    
    private Date createTime;
    
    private Date lastUpdateTime;
    
    private String command;
    
    public Integer getStepId()
    {
        return stepId;
    }
    
    public void setStepId(Integer stepId)
    {
        this.stepId = stepId;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public String getStepName()
    {
        return stepName;
    }
    
    public void setStepName(String stepName)
    {
        this.stepName = stepName == null ? null : stepName.trim();
    }
    
    public String getStepDesc()
    {
        return stepDesc;
    }
    
    public void setStepDesc(String stepDesc)
    {
        this.stepDesc = stepDesc == null ? null : stepDesc.trim();
    }
    
    public Integer getStepTaskType()
    {
        return stepTaskType;
    }
    
    public void setStepTaskType(Integer stepTaskType)
    {
        this.stepTaskType = stepTaskType;
    }
    
    public Boolean getStepEnableFlag()
    {
        return stepEnableFlag;
    }
    
    public void setStepEnableFlag(Boolean stepEnableFlag)
    {
        this.stepEnableFlag = stepEnableFlag;
    }
    
    public Integer getAllowRetryCount()
    {
        return allowRetryCount;
    }
    
    public void setAllowRetryCount(Integer allowRetryCount)
    {
        this.allowRetryCount = allowRetryCount;
    }
    
    public Integer getTimeoutMinutes()
    {
        return timeoutMinutes;
    }
    
    public void setTimeoutMinutes(Integer timeoutMinutes)
    {
        this.timeoutMinutes = timeoutMinutes;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }
    
    public void setLastUpdateTime(Date lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public String getCommand()
    {
        return command;
    }
    
    public void setCommand(String command)
    {
        this.command = command == null ? null : command.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskStepEntity [stepId=");
        builder.append(stepId);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", stepName=");
        builder.append(stepName);
        builder.append(", stepDesc=");
        builder.append(stepDesc);
        builder.append(", stepTaskType=");
        builder.append(stepTaskType);
        builder.append(", stepEnableFlag=");
        builder.append(stepEnableFlag);
        builder.append(", allowRetryCount=");
        builder.append(allowRetryCount);
        builder.append(", timeoutMinutes=");
        builder.append(timeoutMinutes);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", lastUpdateTime=");
        builder.append(lastUpdateTime);
        builder.append(", command=");
        builder.append(command);
        builder.append("]");
        return builder.toString();
    }
}