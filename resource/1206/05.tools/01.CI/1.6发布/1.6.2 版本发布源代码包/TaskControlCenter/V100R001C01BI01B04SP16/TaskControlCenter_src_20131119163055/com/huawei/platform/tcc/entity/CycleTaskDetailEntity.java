/* 文 件 名:  CycleTaskDetailEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 周期任务详细信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-20]
 */
public class CycleTaskDetailEntity
{
    private Long taskId;
    
    private String cycleId;
    
    private Integer state;
    
    private Date startTime;
    
    private Date currentTime;
    
    private String startCycleId;
    
    private Integer priority;
    
    private Integer weight;
    
    private String dependIdList;
    
    private Boolean cycleDependFlag;
    
    private String osUser;
    
    private String userGroup;

    public String getOsUser()
    {
        return osUser;
    }

    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }

    public String getUserGroup()
    {
        return userGroup;
    }

    public void setUserGroup(String userGroup)
    {
        this.userGroup = userGroup;
    }

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public String getCycleId()
    {
        return cycleId;
    }

    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime)
    {
        this.currentTime = currentTime;
    }

    public String getStartCycleId()
    {
        return startCycleId;
    }

    public void setStartCycleId(String startCycleId)
    {
        this.startCycleId = startCycleId;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    public Integer getWeight()
    {
        return weight;
    }

    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }

    public String getDependIdList()
    {
        return dependIdList;
    }

    public void setDependIdList(String dependIdList)
    {
        this.dependIdList = dependIdList;
    }

    public boolean getCycleDependFlag()
    {
        return cycleDependFlag;
    }

    public void setCycleDependFlag(Boolean cycleDependFlag)
    {
        this.cycleDependFlag = cycleDependFlag;
    }
    
    
}