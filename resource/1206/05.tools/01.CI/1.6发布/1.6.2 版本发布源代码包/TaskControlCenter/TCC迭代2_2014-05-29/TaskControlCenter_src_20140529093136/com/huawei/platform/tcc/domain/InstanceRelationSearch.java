/*
 * 文 件 名:  InstanceRelationSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 20013-2014,  All rights reserved
 * 描    述:  实例关系搜索
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-1
 */
package com.huawei.platform.tcc.domain;

import java.util.Date;
import java.util.List;

/**
 * 实例关系搜索
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-1]
 */
public class InstanceRelationSearch
{
    private List<Long> taskIds;
    
    private Integer state;
    
    private Date scheduleDate;
    
    private Date expectExecuteDate;

    public List<Long> getTaskIds()
    {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds)
    {
        this.taskIds = taskIds;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Date getScheduleDate()
    {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate)
    {
        this.scheduleDate = scheduleDate;
    }

    public Date getExpectExecuteDate()
    {
        return expectExecuteDate;
    }

    public void setExpectExecuteDate(Date expectExecuteDate)
    {
        this.expectExecuteDate = expectExecuteDate;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("InstanceRelationSearch [taskIds=");
        builder.append(taskIds);
        builder.append(", state=");
        builder.append(state);
        builder.append(", scheduleDate=");
        builder.append(scheduleDate);
        builder.append(", expectExecuteDate=");
        builder.append(expectExecuteDate);
        builder.append("]");
        return builder.toString();
    }
}
