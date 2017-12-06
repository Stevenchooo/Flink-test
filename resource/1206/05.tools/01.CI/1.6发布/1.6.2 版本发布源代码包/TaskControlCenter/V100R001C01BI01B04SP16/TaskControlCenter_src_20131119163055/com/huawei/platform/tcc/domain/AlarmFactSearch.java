/*
 * 文 件 名:  AlarmFactSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471    
 * 创建时间:  2012-07-02
 */
package com.huawei.platform.tcc.domain;

import java.util.Date;
import java.util.List;

/**
 * 审计记录查询条件
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100,, 2012-07-02]
 */
public class AlarmFactSearch
{
    /**
     * 告警级别
     */
    private Integer alarmGrade;
    
    /**
     * 告警状态
     */
    private Integer status;
    
    /**
     * 业务ID
     */
    private String serviceId;
    
    /**
     * 告警类型
     */
    private Integer alarmType;
    
    /**
     * 起始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 数据过滤的拥有查看权限的用户组
     */
    private List<String> visibleGroups;
    
    /**
     * 查询起始页
     */
    private Integer pageIndex;
    
    /**
     * 查询每页的大小
     */
    private Integer pageSize;
    
    public Integer getAlarmGrade()
    {
        return alarmGrade;
    }
    
    public void setAlarmGrade(Integer alarmGrade)
    {
        this.alarmGrade = alarmGrade;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public Integer getAlarmType()
    {
        return alarmType;
    }
    
    public void setAlarmType(Integer alarmType)
    {
        this.alarmType = alarmType;
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
    
    public List<String> getVisibleGroups()
    {
        return visibleGroups;
    }
    
    public void setVisibleGroups(List<String> visibleGroups)
    {
        this.visibleGroups = visibleGroups;
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
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("AlarmFactSearch [alarmGrade=");
        builder.append(alarmGrade);
        builder.append(", status=");
        builder.append(status);
        builder.append(", serviceId=");
        builder.append(serviceId);
        builder.append(", alarmType=");
        builder.append(alarmType);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", visibleGroups=");
        builder.append(visibleGroups);
        builder.append(", pageIndex=");
        builder.append(pageIndex);
        builder.append(", pageSize=");
        builder.append(pageSize);
        builder.append("]");
        return builder.toString();
    }
}
