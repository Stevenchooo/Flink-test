/*
 * 文 件 名:  OperationRecordSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471    
 * 创建时间:  2012-07-02
 */
package com.huawei.platform.tcc.domain;

import java.util.Date;

/**
 * 审计记录查询条件
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100,, 2012-07-02]
 */
public class OperationRecordSearch
{
    /**
     * 操作员
     */
    private String operatorName;
    
    /**
     * 操作类型
     */
    private Integer operType;
    
    /**
     * 业务ID
     */
    private String serviceId;
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 起始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 查询起始页
     */
    private Integer pageIndex;
    
    /**
     * 查询每页的大小
     */
    private Integer pageSize;

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }

    public Integer getOperType()
    {
        return operType;
    }

    public void setOperType(Integer operType)
    {
        this.operType = operType;
    }

    public String getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
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

}
