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
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-20]
 */
public class TaskEntity implements Cloneable
{
    private Long taskId;
    
    private Integer serviceId;
    
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
    
    private Boolean rtTaskFlag;
    
    private Integer endBatchFlag;
    
    private String inputFileList;
    
    private Integer inputFileMinCount;
    
    private Integer waitInputMinutes;
    
    private Date createTime;
    
    private Date lastUpdateTime;
    
    private String dependTaskIdList;
    
    private Integer redoDayLength;
    
    private Date redoStartTime;
    
    private Date redoEndTime;
    
    private Integer redoType;
    
    private Date startTime;
    
    private Integer weight;
    
    private String userGroup;
    
    private String creator;
    
    private String osUser;
    
    private String deployedNodeList;
    
    private String hiveTableName;
    
    /**
     * 默认构造函数
     */
    public TaskEntity()
    {
        
    }
    
    /**
     * 构造函数
     * @param taskId 周期Id
     */
    public TaskEntity(Long taskId)
    {
        this.taskId = taskId;
    }
    
    /**
     * 任务是否可以运行
     * @return 是否可以运行
     */
    public boolean canRun()
    {
        return null != this.getTaskEnableFlag() && this.getTaskEnableFlag() && null != this.getTaskState()
            && this.getTaskState() == 0;
    }
    
    @Override
    public TaskEntity clone()
    {
        try
        {
            super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            //do nothing
            e = null;
        }
        
        TaskEntity taskCpy = new TaskEntity();
        taskCpy.setCreateTime(createTime);
        taskCpy.setCreator(creator);
        taskCpy.setCycleDependFlag(cycleDependFlag);
        taskCpy.setCycleLength(cycleLength);
        taskCpy.setCycleOffset(cycleOffset);
        taskCpy.setCycleType(cycleType);
        taskCpy.setDependTaskIdList(dependTaskIdList);
        taskCpy.setDeployedNodeList(deployedNodeList);
        taskCpy.setEndBatchFlag(endBatchFlag);
        taskCpy.setInputFileList(inputFileList);
        taskCpy.setInputFileMinCount(inputFileMinCount);
        taskCpy.setLastUpdateTime(lastUpdateTime);
        taskCpy.setMultiBatchFlag(multiBatchFlag);
        taskCpy.setRtTaskFlag(rtTaskFlag);
        taskCpy.setOsUser(osUser);
        taskCpy.setPriority(priority);
        taskCpy.setRedoDayLength(redoDayLength);
        taskCpy.setRedoEndTime(redoEndTime);
        taskCpy.setRedoStartTime(redoStartTime);
        taskCpy.setRedoType(redoType);
        taskCpy.setServiceId(serviceId);
        taskCpy.setStartTime(startTime);
        taskCpy.setTaskDesc(taskDesc);
        taskCpy.setTaskEnableFlag(taskEnableFlag);
        taskCpy.setTaskId(taskId);
        taskCpy.setTaskName(taskName);
        taskCpy.setTaskState(taskState);
        taskCpy.setTaskType(taskType);
        taskCpy.setUserGroup(userGroup);
        taskCpy.setWaitInputMinutes(waitInputMinutes);
        taskCpy.setWeight(weight);
        taskCpy.setHiveTableName(hiveTableName);
        return taskCpy;
    }
    
    public Boolean getRtTaskFlag()
    {
        return rtTaskFlag;
    }
    
    public void setRtTaskFlag(Boolean rtTaskFlag)
    {
        this.rtTaskFlag = rtTaskFlag;
    }
    
    public String getDeployedNodeList()
    {
        return deployedNodeList;
    }
    
    public void setDeployedNodeList(String deployedNodeList)
    {
        this.deployedNodeList = deployedNodeList;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
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
    
    public String getUserGroup()
    {
        return userGroup;
    }
    
    public void setUserGroup(String userGroup)
    {
        this.userGroup = userGroup;
    }
    
    public String getCreator()
    {
        return creator;
    }
    
    public void setCreator(String creator)
    {
        this.creator = creator;
    }
    
    public String getOsUser()
    {
        return osUser;
    }
    
    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }
    
    public String getHiveTableName()
    {
        return hiveTableName;
    }
    
    public void setHiveTableName(String hiveTableName)
    {
        this.hiveTableName = hiveTableName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskEntity [taskId=");
        builder.append(taskId);
        builder.append(", serviceId=");
        builder.append(serviceId);
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
        builder.append(", rtTaskFlag=");
        builder.append(rtTaskFlag);
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
        builder.append(", userGroup=");
        builder.append(userGroup);
        builder.append(", creator=");
        builder.append(creator);
        builder.append(", osUser=");
        builder.append(osUser);
        builder.append(", hiveTableName=");
        builder.append(hiveTableName);
        builder.append("]");
        return builder.toString();
    }
}