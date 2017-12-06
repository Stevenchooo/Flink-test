/* 文 件 名:  StepRunningStateEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 步骤运行状态实体 
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-20]
 * @see  [相关类/方法]
 */
public class StepRunningStateEntity
{
    private Integer batchId;
    
    private String cycleId;
    
    private Integer stepId;
    
    private Long taskId;
    
    private Integer state;
    
    private Date runningStartTime;
    
    private Date runningEndTime;
    
    private String runningJobId;
    
    private Integer retryCount;
    
    private String jobInputList;
    
    private String jobOutputList;
    
    private String failReason;
    
    public Integer getBatchId()
    {
        return batchId;
    }
    
    public void setBatchId(Integer batchId)
    {
        this.batchId = batchId;
    }
    
    public String getCycleId()
    {
        return cycleId;
    }
    
    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId == null ? null : cycleId.trim();
    }
    
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
    
    public String getRunningJobId()
    {
        return runningJobId;
    }
    
    public void setRunningJobId(String runningJobId)
    {
        this.runningJobId = runningJobId == null ? null : runningJobId.trim();
    }
    
    public Integer getRetryCount()
    {
        return retryCount;
    }
    
    public void setRetryCount(Integer retryCount)
    {
        this.retryCount = retryCount;
    }
    
    public String getJobInputList()
    {
        return jobInputList;
    }
    
    public void setJobInputList(String jobInputList)
    {
        this.jobInputList = jobInputList;
    }
    
    public String getJobOutputList()
    {
        return jobOutputList;
    }
    
    public void setJobOutputList(String jobOutputList)
    {
        this.jobOutputList = jobOutputList;
    }
    
    public String getFailReason()
    {
        return failReason;
    }
    
    public void setFailReason(String failReason)
    {
        this.failReason = failReason;
    }

    /**
     * 字符串表示
     * @return 字符串
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("StepRunningStateEntity [batchId=");
        builder.append(batchId);
        builder.append(", cycleId=");
        builder.append(cycleId);
        builder.append(", stepId=");
        builder.append(stepId);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", state=");
        builder.append(state);
        builder.append(", runningStartTime=");
        builder.append(runningStartTime);
        builder.append(", runningEndTime=");
        builder.append(runningEndTime);
        builder.append(", runningJobId=");
        builder.append(runningJobId);
        builder.append(", retryCount=");
        builder.append(retryCount);
        builder.append(", jobInputList=");
        builder.append(jobInputList);
        builder.append(", jobOutputList=");
        builder.append(jobOutputList);
        builder.append(", failReason=");
        builder.append(failReason);
        builder.append("]");
        return builder.toString();
    }
}