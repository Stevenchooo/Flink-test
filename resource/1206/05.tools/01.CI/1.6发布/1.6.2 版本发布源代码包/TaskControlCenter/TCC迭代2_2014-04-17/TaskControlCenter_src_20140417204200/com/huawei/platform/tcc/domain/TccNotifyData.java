/*
 * 文 件 名:  NotifyData.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-7-3
 */
package com.huawei.platform.tcc.domain;

import java.util.Date;

/**
 * 通知数据
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-7-3]
 */
public class TccNotifyData extends NotifyData
{
    /**
     * 任务名
     */
    private String taskName;
    
    /**
     * hive表名
     */
    private String hiveTableName;
    
    /**
     * 周期Id
     */
    private String cycleId;
    
    /**
     * 状态
     */
    private int state;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    public String getTaskName()
    {
        return taskName;
    }
    
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }
    
    public String getHiveTableName()
    {
        return hiveTableName;
    }
    
    public void setHiveTableName(String hiveTableName)
    {
        this.hiveTableName = hiveTableName;
    }
    
    public String getCycleId()
    {
        return cycleId;
    }
    
    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }
    
    public int getState()
    {
        return state;
    }
    
    public void setState(int state)
    {
        this.state = state;
    }
    
    public Date getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
}
