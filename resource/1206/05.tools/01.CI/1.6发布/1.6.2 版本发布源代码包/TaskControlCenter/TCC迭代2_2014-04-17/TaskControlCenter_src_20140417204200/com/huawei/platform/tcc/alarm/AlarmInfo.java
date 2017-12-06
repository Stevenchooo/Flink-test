/*
 * 文 件 名:  AlarmInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-7-4
 */
package com.huawei.platform.tcc.alarm;

/**
 * 告警信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-7-4]
 */
public class AlarmInfo
{
    private int grade;
    
    private int alarmType;
    
    private String emailMsg;
    
    private String emailSubject;
    
    private String smsMsg;
    
    public int getGrade()
    {
        return grade;
    }
    
    public void setGrade(int grade)
    {
        this.grade = grade;
    }
    
    public int getAlarmType()
    {
        return alarmType;
    }
    
    public void setAlarmType(int alarmType)
    {
        this.alarmType = alarmType;
    }
    
    public String getEmailMsg()
    {
        return emailMsg;
    }
    
    public void setEmailMsg(String emailMsg)
    {
        this.emailMsg = emailMsg;
    }
    
    public String getSmsMsg()
    {
        return smsMsg;
    }
    
    public void setSmsMsg(String smsMsg)
    {
        this.smsMsg = smsMsg;
    }

    public String getEmailSubject()
    {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject)
    {
        this.emailSubject = emailSubject;
    }
}
