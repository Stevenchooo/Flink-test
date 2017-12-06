/*
 * 文 件 名:  TnstanceRelationEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 20013-2014,  All rights reserved
 * 描    述:  实例关系实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-31
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 实例关系实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-31]
 */
public class InstanceRelationEntity
{
    private Date scheduleDate;
    
    private Long taskId;
    
    private String cycleId;
    
    private String preDependentList;
    
    private String subDependentList;
    
    private Date expectExecuteDate;

    public Date getScheduleDate()
    {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate)
    {
        this.scheduleDate = scheduleDate;
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

    public String getPreDependentList()
    {
        return preDependentList;
    }

    public void setPreDependentList(String preDependentList)
    {
        this.preDependentList = preDependentList;
    }

    public String getSubDependentList()
    {
        return subDependentList;
    }

    public void setSubDependentList(String subDependentList)
    {
        this.subDependentList = subDependentList;
    }

    public Date getExpectExecuteDate()
    {
        return expectExecuteDate;
    }

    public void setExpectExecuteDate(Date expectExecuteDate)
    {
        this.expectExecuteDate = expectExecuteDate;
    }
}
