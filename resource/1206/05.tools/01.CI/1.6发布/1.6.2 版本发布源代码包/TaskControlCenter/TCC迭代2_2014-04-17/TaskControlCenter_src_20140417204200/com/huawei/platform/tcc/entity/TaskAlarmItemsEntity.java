/* 文 件 名:  TaskAlarmItemsEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

import org.apache.commons.lang.StringUtils;

/**
 * 任务告警项
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-06-18]
 */
public class TaskAlarmItemsEntity
{
    private String alarmType;
    
    private Long taskId;
    
    private Boolean isAlarmPermitted;
    
    public String getAlarmType()
    {
        return alarmType;
    }
    
    public void setAlarmType(String alarmType)
    {
        this.alarmType = alarmType;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskAlarmItemsEntity [alarmType=");
        builder.append(alarmType);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", isAlarmPermitted=");
        builder.append(isAlarmPermitted);
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * 检查是否启用了相应告警类型的告警
     * @param alarmTypePos 告警类型位置（告警类型）
     * @return 检查是否启用了相应告警类型的告警
     */
    public boolean isEnableAlarm(int alarmTypePos)
    {
        //如果关闭告警，直接返回false
        if (!this.isAlarmPermitted)
        {
            return false;
        }
        
        if (StringUtils.isEmpty(this.alarmType))
        {
            return false;
        }
        
        if (alarmType.length() <= alarmTypePos || alarmTypePos < 0)
        {
            return false;
        }
        
        return '1' == alarmType.charAt(alarmTypePos);
    }
    
    public Boolean getIsAlarmPermitted()
    {
        return isAlarmPermitted;
    }
    
    public void setIsAlarmPermitted(Boolean isAlarmPermitted)
    {
        this.isAlarmPermitted = isAlarmPermitted;
    }
}