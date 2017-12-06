/*
 * 文 件 名:  TaskSearchEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-2-27
 */
package com.huawei.platform.tcc.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务信息查询条件类
 * 
 * @author  w00190929
 * @version [华为终端云统一账号模块, 2012-2-27]
 */
public class TaskSearchEntity
{
    /**
     * 查询起始页
     */
    private Integer pageIndex;
    /**
     * 查询每页的大小
     */
    private Integer pageSize;
    /**
     * 任务ID
     */
    private List<Long> taskIds = new ArrayList<Long>();
    
    /**
     * 任务名
     */
    private List<String> taskNames = new ArrayList<String>();
    
    private List<String> visibleGroups;
    
    private Integer serviceId;
    
    private String osUser;
    
    /**
     * 任务状态
     */
    private Integer taskState;
    /**
     * 任务类型
     */
    private Integer taskType;
    /**
     * 周期类型
     */
    private String cycleType;
    
    /**
     * 优先级
     */
    private Integer priority;
    
    public Integer getPriority()
    {
        return priority;
    }
    
    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }
    
    public Integer getPageIndex()
    {
        return pageIndex;
    }
    public void setPageIndex(Integer pageIndex)
    {
        this.pageIndex = pageIndex;
    }
    public Integer getPageSize()
    {
        return pageSize;
    }
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public Integer getTaskState()
    {
        return taskState;
    }
    public void setTaskState(Integer taskState)
    {
        this.taskState = taskState;
    }
    public Integer getTaskType()
    {
        return taskType;
    }
    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }
    public String getCycleType()
    {
        return cycleType;
    }
    public void setCycleType(String cycleType)
    {
        this.cycleType = cycleType;
    }
    public List<Long> getTaskIds()
    {
        return taskIds;
    }
    public void setTaskIds(List<Long> taskIds)
    {
        this.taskIds = taskIds;
    }
    public List<String> getTaskNames()
    {
        return taskNames;
    }
    public void setTaskNames(List<String> taskNames)
    {
        this.taskNames = taskNames;
    }
    
    public List<String> getVisibleGroups()
    {
        return visibleGroups;
    }
    public void setVisibleGroups(List<String> visibleGroups)
    {
        this.visibleGroups = visibleGroups;
    }
    public Integer getServiceId()
    {
        return serviceId;
    }
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    public String getOsUser()
    {
        return osUser;
    }
    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }
}
