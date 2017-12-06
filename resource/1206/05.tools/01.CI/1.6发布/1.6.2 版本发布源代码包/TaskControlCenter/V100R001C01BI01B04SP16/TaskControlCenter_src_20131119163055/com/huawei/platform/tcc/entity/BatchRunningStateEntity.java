/* 文 件 名:  BatchRunningStateEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 批运行状态实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-20]
 * @see  [相关类/方法]
 */
public class BatchRunningStateEntity
{
    private Integer batchId;
    
    private String cycleId;
    
    private Long taskId;
    
    private Integer state;
    
    private Date runningStartTime;
    
    private Date runningEndTime;
    
    private String jobInput;
    
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
    
    public String getJobInput()
    {
        return jobInput;
    }
    
    public void setJobInput(String jobInput)
    {
        this.jobInput = jobInput == null ? null : jobInput.trim();
    }
    
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
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    /**
     * 转换成字符串
     * @return 字符串
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("BatchRunningStateEntity [batchId=");
        builder.append(batchId);
        builder.append(", cycleId=");
        builder.append(cycleId);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", state=");
        builder.append(state);
        builder.append(", runningStartTime=");
        builder.append(runningStartTime);
        builder.append(", runningEndTime=");
        builder.append(runningEndTime);
        builder.append(", jobInput=");
        builder.append(jobInput);
        builder.append("]");
        return builder.toString();
    }
}