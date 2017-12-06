/* 文 件 名:  AlarmFactInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 历史告警信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class AlarmFactInfoEntity
{
    private Date alarmTime;
    
    private String cycleId;
    
    private Integer taskId;
    
    private String serviceName;
    
    private Integer instanceId;
    
    private Integer alarmGrade;
    
    private Integer alarmType;
    
    private String emailList;
    
    private String mobileList;
    
    private Integer status;
    
    private String reason;
    
    private String solution;
    
    private String operatorName;
    
    private Date processTime;
    
    public Date getAlarmTime()
    {
        return alarmTime;
    }
    
    public void setAlarmTime(Date alarmTime)
    {
        this.alarmTime = alarmTime;
    }
    
    public String getCycleId()
    {
        return cycleId;
    }
    
    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId == null ? null : cycleId.trim();
    }
    
    public Integer getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
    
    public String getServiceName()
    {
        return serviceName;
    }
    
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }
    
    public Integer getInstanceId()
    {
        return instanceId;
    }
    
    public void setInstanceId(Integer instanceId)
    {
        this.instanceId = instanceId;
    }
    
    public Integer getAlarmGrade()
    {
        return alarmGrade;
    }
    
    public void setAlarmGrade(Integer alarmGrade)
    {
        this.alarmGrade = alarmGrade;
    }
    
    public Integer getAlarmType()
    {
        return alarmType;
    }
    
    public void setAlarmType(Integer alarmType)
    {
        this.alarmType = alarmType;
    }
    
    public String getEmailList()
    {
        return emailList;
    }
    
    public void setEmailList(String emailList)
    {
        this.emailList = emailList == null ? null : emailList.trim();
    }
    
    public String getMobileList()
    {
        return mobileList;
    }
    
    public void setMobileList(String mobileList)
    {
        this.mobileList = mobileList == null ? null : mobileList.trim();
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getReason()
    {
        return reason;
    }
    
    public void setReason(String reason)
    {
        this.reason = reason == null ? null : reason.trim();
    }
    
    public String getSolution()
    {
        return solution;
    }
    
    public void setSolution(String solution)
    {
        this.solution = solution == null ? null : solution.trim();
    }
    
    public String getOperatorName()
    {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }
    
    public Date getProcessTime()
    {
        return processTime;
    }
    
    public void setProcessTime(Date processTime)
    {
        this.processTime = processTime;
    }
}