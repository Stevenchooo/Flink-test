/* 文 件 名:  TaskAlarmChannelInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

/**
 * 任务告警渠道信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class TaskAlarmChannelInfoEntity
{
    private Integer alarmGrade;
    
    private Long taskId;
    
    private String emailList;
    
    private String mobileList;
    
    public Integer getAlarmGrade()
    {
        return alarmGrade;
    }
    
    public void setAlarmGrade(Integer alarmGrade)
    {
        this.alarmGrade = alarmGrade;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
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
}