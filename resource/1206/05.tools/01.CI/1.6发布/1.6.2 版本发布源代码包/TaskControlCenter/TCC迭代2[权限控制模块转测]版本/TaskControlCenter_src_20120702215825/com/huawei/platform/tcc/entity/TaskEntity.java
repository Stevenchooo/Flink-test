/* 文 件 名:  TaskEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 任务实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-20]
 * @see  [相关类/方法]
 */
public class TaskEntity
{
    private Long taskId;
    
    private Integer serviceid;
    
    private String taskName;
    
    private String taskDesc;
    
    private Integer taskType;
    
    private Boolean taskEnableFlag;
    
    private Integer taskState;
    
    private Integer priority;
    
    private String cycleType;
    
    private Integer cycleLength;
    
    private String cycleOffset;
    
    private Boolean cycleDependFlag;
    
    private Boolean multiBatchFlag;
    
    private Integer endBatchFlag;
    
    private String inputFileList;
    
    private Integer inputFileMinCount;
    
    private Integer waitInputMinutes;
    
    private Date createTime;
    
    private Date lastUpdateTime;
    
    private String filesInHost;
    
    private String dependTaskIdList;
    
    private Integer redoDayLength;
    
    private Date redoStartTime;
    
    private Date redoEndTime;
    
    private Integer redoType;
    
    private Date startTime;
    
    private Integer weight;
    
    private Boolean successSendEmailFlag;
    
    private String failureEmailsTo;
    
    private String serviceTaskGroup;
    
    private Boolean isAlarmPermitted;
    
    private String startOperator;
    
    private String osUserName;
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public Integer getServiceid()
    {
        return serviceid;
    }
    
    public void setServiceid(Integer serviceid)
    {
        this.serviceid = serviceid;
    }
    
    public String getTaskName()
    {
        return taskName;
    }
    
    public void setTaskName(String taskName)
    {
        this.taskName = taskName == null ? null : taskName.trim();
    }
    
    public String getTaskDesc()
    {
        return taskDesc;
    }
    
    public void setTaskDesc(String taskDesc)
    {
        this.taskDesc = taskDesc == null ? null : taskDesc.trim();
    }
    
    public Integer getTaskType()
    {
        return taskType;
    }
    
    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }
    
    public Boolean getTaskEnableFlag()
    {
        return taskEnableFlag;
    }
    
    public void setTaskEnableFlag(Boolean taskEnableFlag)
    {
        this.taskEnableFlag = taskEnableFlag;
    }
    
    public Integer getTaskState()
    {
        return taskState;
    }
    
    public void setTaskState(Integer taskState)
    {
        this.taskState = taskState;
    }
    
    public Integer getPriority()
    {
        return priority;
    }
    
    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }
    
    public String getCycleType()
    {
        return cycleType;
    }
    
    public void setCycleType(String cycleType)
    {
        this.cycleType = cycleType;
    }
    
    public Integer getCycleLength()
    {
        return cycleLength;
    }
    
    public void setCycleLength(Integer cycleLength)
    {
        this.cycleLength = cycleLength;
    }
    
    public String getCycleOffset()
    {
        return cycleOffset;
    }
    
    public void setCycleOffset(String cycleOffset)
    {
        this.cycleOffset = cycleOffset == null ? null : cycleOffset.trim();
    }
    
    public Boolean getCycleDependFlag()
    {
        return cycleDependFlag;
    }
    
    public void setCycleDependFlag(Boolean cycleDependFlag)
    {
        this.cycleDependFlag = cycleDependFlag;
    }
    
    public Boolean getMultiBatchFlag()
    {
        return multiBatchFlag;
    }
    
    public void setMultiBatchFlag(Boolean multiBatchFlag)
    {
        this.multiBatchFlag = multiBatchFlag;
    }
    
    public Integer getEndBatchFlag()
    {
        return endBatchFlag;
    }
    
    public void setEndBatchFlag(Integer endBatchFlag)
    {
        this.endBatchFlag = endBatchFlag;
    }
    
    public String getInputFileList()
    {
        return inputFileList;
    }
    
    public void setInputFileList(String inputFileList)
    {
        this.inputFileList = inputFileList == null ? null : inputFileList.trim();
    }
    
    public Integer getInputFileMinCount()
    {
        return inputFileMinCount;
    }
    
    public void setInputFileMinCount(Integer inputFileMinCount)
    {
        this.inputFileMinCount = inputFileMinCount;
    }
    
    public Integer getWaitInputMinutes()
    {
        return waitInputMinutes;
    }
    
    public void setWaitInputMinutes(Integer waitInputMinutes)
    {
        this.waitInputMinutes = waitInputMinutes;
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
    
    public String getFilesInHost()
    {
        return filesInHost;
    }
    
    public void setFilesInHost(String filesInHost)
    {
        this.filesInHost = filesInHost == null ? null : filesInHost.trim();
    }
    
    public String getDependTaskIdList()
    {
        return dependTaskIdList;
    }
    
    public void setDependTaskIdList(String dependTaskIdList)
    {
        this.dependTaskIdList = dependTaskIdList == null ? null : dependTaskIdList.trim();
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Integer getWeight()
    {
        return weight;
    }

    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }

    public Integer getRedoDayLength()
    {
        return redoDayLength;
    }

    public void setRedoDayLength(Integer redoDayLength)
    {
        this.redoDayLength = redoDayLength;
    }

    public Date getRedoStartTime()
    {
        return redoStartTime;
    }

    public void setRedoStartTime(Date redoStartTime)
    {
        this.redoStartTime = redoStartTime;
    }

    public Date getRedoEndTime()
    {
        return redoEndTime;
    }

    public void setRedoEndTime(Date redoEndTime)
    {
        this.redoEndTime = redoEndTime;
    }

    public Boolean getSuccessSendEmailFlag()
    {
        return successSendEmailFlag;
    }

    public void setSuccessSendEmailFlag(Boolean successSendEmailFlag)
    {
        this.successSendEmailFlag = successSendEmailFlag;
    }

    public String getFailureEmailsTo()
    {
        return failureEmailsTo;
    }

    public void setFailureEmailsTo(String failureEmailsTo)
    {
        this.failureEmailsTo = failureEmailsTo;
    }

    public Integer getRedoType()
    {
        return redoType;
    }

    public void setRedoType(Integer redoType)
    {
        this.redoType = redoType;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
        TaskEntity other = (TaskEntity)obj;
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

    public String getServiceTaskGroup()
    {
        return serviceTaskGroup;
    }

    public void setServiceTaskGroup(String serviceTaskGroup)
    {
        this.serviceTaskGroup = serviceTaskGroup;
    }

    public Boolean getIsAlarmPermitted()
    {
        return isAlarmPermitted;
    }

    public void setIsAlarmPermitted(Boolean isAlarmPermitted)
    {
        this.isAlarmPermitted = isAlarmPermitted;
    }

    public String getStartOperator()
    {
        return startOperator;
    }

    public void setStartOperator(String startOperator)
    {
        this.startOperator = startOperator;
    }

    public String getOsUserName()
    {
        return osUserName;
    }

    public void setOsUserName(String osUserName)
    {
        this.osUserName = osUserName;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskEntity [taskId=");
        builder.append(taskId);
        builder.append(", serviceid=");
        builder.append(serviceid);
        builder.append(", taskName=");
        builder.append(taskName);
        builder.append(", taskDesc=");
        builder.append(taskDesc);
        builder.append(", taskType=");
        builder.append(taskType);
        builder.append(", taskEnableFlag=");
        builder.append(taskEnableFlag);
        builder.append(", taskState=");
        builder.append(taskState);
        builder.append(", priority=");
        builder.append(priority);
        builder.append(", cycleType=");
        builder.append(cycleType);
        builder.append(", cycleLength=");
        builder.append(cycleLength);
        builder.append(", cycleOffset=");
        builder.append(cycleOffset);
        builder.append(", cycleDependFlag=");
        builder.append(cycleDependFlag);
        builder.append(", multiBatchFlag=");
        builder.append(multiBatchFlag);
        builder.append(", endBatchFlag=");
        builder.append(endBatchFlag);
        builder.append(", inputFileList=");
        builder.append(inputFileList);
        builder.append(", inputFileMinCount=");
        builder.append(inputFileMinCount);
        builder.append(", waitInputMinutes=");
        builder.append(waitInputMinutes);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", lastUpdateTime=");
        builder.append(lastUpdateTime);
        builder.append(", filesInHost=");
        builder.append(filesInHost);
        builder.append(", dependTaskIdList=");
        builder.append(dependTaskIdList);
        builder.append(", redoDayLength=");
        builder.append(redoDayLength);
        builder.append(", redoStartTime=");
        builder.append(redoStartTime);
        builder.append(", redoEndTime=");
        builder.append(redoEndTime);
        builder.append(", redoType=");
        builder.append(redoType);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", weight=");
        builder.append(weight);
        builder.append(", successSendEmailFlag=");
        builder.append(successSendEmailFlag);
        builder.append(", failureEmailsTo=");
        builder.append(failureEmailsTo);
        builder.append(", serviceTaskGroup=");
        builder.append(serviceTaskGroup);
        builder.append(", isAlarmPermitted=");
        builder.append(isAlarmPermitted);
        builder.append(", startOperator=");
        builder.append(startOperator);
        builder.append(", osUserName=");
        builder.append(osUserName);
        builder.append("]");
        return builder.toString();
    }
}