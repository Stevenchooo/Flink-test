/*
 * 文 件 名:  TaskRSQueryParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务周期查询条件
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-22]
 */
public class TaskRSQueryParam
{
    /**
     * 分页开始索引
     */
    private Long startIndex;
    
    /**
     * 行数
     */
    private Integer rows;
    
    /**
     * 页数
     */
    private Integer page;
    
    /**
     * 开始时间
     */
    private String startCycleID;
    
    /**
     * 结束时间
     */
    private String endCycleID;
    
    /**
     * 状态
     */
    private Integer state;
    
    /**
     * 任务Id
     */
    private Long taskId;
    
    private Integer serviceId;
    
    private String osUser;
    
    private List<String> visibleGroups;
    
    /**
     * 任务名
     */
    private List<String> taskNames = new ArrayList<String>();;
    
    public Long getStartIndex()
    {
        return startIndex;
    }
    
    public void setStartIndex(Long startIndex)
    {
        this.startIndex = startIndex;
    }
    
    public Integer getRows()
    {
        return rows;
    }
    
    public void setRows(Integer rows)
    {
        this.rows = rows;
    }
    
    public Integer getState()
    {
        return state;
    }
    
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public String getStartCycleId()
    {
        return startCycleID;
    }
    
    public void setStartCycleId(String startCycleId)
    {
        this.startCycleID = startCycleId;
    }
    
    public String getEndCycleID()
    {
        return endCycleID;
    }
    
    public void setEndCycleID(String endCycleID)
    {
        this.endCycleID = endCycleID;
    }
    
    public Integer getPage()
    {
        return page;
    }
    
    public void setPage(Integer page)
    {
        this.page = page;
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
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskRSQueryParam [startIndex=");
        builder.append(startIndex);
        builder.append(", rows=");
        builder.append(rows);
        builder.append(", page=");
        builder.append(page);
        builder.append(", startCycleID=");
        builder.append(startCycleID);
        builder.append(", endCycleID=");
        builder.append(endCycleID);
        builder.append(", state=");
        builder.append(state);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", serviceId=");
        builder.append(serviceId);
        builder.append(", osUser=");
        builder.append(osUser);
        builder.append(", visibleGroups=");
        builder.append(visibleGroups);
        builder.append(", taskNames=");
        builder.append(taskNames);
        builder.append("]");
        return builder.toString();
    }
}
