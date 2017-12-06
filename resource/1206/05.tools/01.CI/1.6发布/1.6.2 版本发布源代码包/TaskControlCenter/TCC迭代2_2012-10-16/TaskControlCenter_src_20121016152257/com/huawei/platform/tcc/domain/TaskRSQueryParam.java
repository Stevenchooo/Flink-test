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
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 * @see  [相关类/方法]
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
    
    private String taskGroup;
    
    private List<String> visibleSTgs = new ArrayList<String>();
    
    /**
     * 任务名
     */
    private List<String> taskNames  = new ArrayList<String>();;

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

    public String getStartCycleID()
    {
        return startCycleID;
    }

    public void setStartCycleID(String startCycleID)
    {
        this.startCycleID = startCycleID;
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

    public List<String> getVisibleSTgs()
    {
        return visibleSTgs;
    }

    public void setVisibleSTgs(List<String> visibleSTgs)
    {
        this.visibleSTgs = visibleSTgs;
    }

    public Integer getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }

    public String getTaskGroup()
    {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup)
    {
        this.taskGroup = taskGroup;
    }
}
