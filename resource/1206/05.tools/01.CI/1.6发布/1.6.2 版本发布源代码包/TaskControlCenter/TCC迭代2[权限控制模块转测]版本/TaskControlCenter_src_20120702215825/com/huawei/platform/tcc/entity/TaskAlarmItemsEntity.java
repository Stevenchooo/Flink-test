/* 文 件 名:  TaskAlarmItemsEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

/**
 * 任务告警项
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class TaskAlarmItemsEntity
{
    private Integer alarmType;
    
    private Integer taskId;
    
    public Integer getAlarmType()
    {
        return alarmType;
    }
    
    public void setAlarmType(Integer alarmType)
    {
        this.alarmType = alarmType;
    }
    
    public Integer getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
}