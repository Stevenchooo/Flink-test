/*
 * 文 件 名:  ServiceTGsSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;


/**
 * 告警配置实体类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-27]
 */
public class AlarmConfig
{
    private String alarmType;
    
    private Long taskId;
    
    private Boolean isAlarmPermitted;
    
    private String normalEmailList;
    
    private String normalMobileList;
    
    private String severeEmailList;
    
    private String severeMobileList;
    
    private String latestStartTime;
    
    private String latestEndTime;
    
    private Integer maxRunTime;
    
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
    
    public Boolean getIsAlarmPermitted()
    {
        return isAlarmPermitted;
    }
    
    public void setIsAlarmPermitted(Boolean isAlarmPermitted)
    {
        this.isAlarmPermitted = isAlarmPermitted;
    }
    
    public String getNormalEmailList()
    {
        return normalEmailList;
    }
    
    public void setNormalEmailList(String normalEmailList)
    {
        this.normalEmailList = normalEmailList;
    }
    
    public String getNormalMobileList()
    {
        return normalMobileList;
    }
    
    public void setNormalMobileList(String normalMobileList)
    {
        this.normalMobileList = normalMobileList;
    }
    
    public String getSevereEmailList()
    {
        return severeEmailList;
    }
    
    public void setSevereEmailList(String severeEmailList)
    {
        this.severeEmailList = severeEmailList;
    }
    
    public String getSevereMobileList()
    {
        return severeMobileList;
    }
    
    public void setSevereMobileList(String severeMobileList)
    {
        this.severeMobileList = severeMobileList;
    }
    
    public String getLatestStartTime()
    {
        return latestStartTime;
    }
    
    public void setLatestStartTime(String latestStartTime)
    {
        this.latestStartTime = latestStartTime;
    }
    
    public String getLatestEndTime()
    {
        return latestEndTime;
    }
    
    public void setLatestEndTime(String latestEndTime)
    {
        this.latestEndTime = latestEndTime;
    }
    
    public Integer getMaxRunTime()
    {
        return maxRunTime;
    }
    
    public void setMaxRunTime(Integer maxRunTime)
    {
        this.maxRunTime = maxRunTime;
    }
    
    @Override
    public String toString()
    {
        return "AlarmConfig [alarmType=" + alarmType + ", taskId=" + taskId + ", isAlarmPermitted=" + isAlarmPermitted
            + ", normalEmailList=" + normalEmailList + ", normalMobileList=" + normalMobileList + ", severeEmailList="
            + severeEmailList + ", severeMobileList=" + severeMobileList + ", latestStartTime=" + latestStartTime
            + ", latestEndTime=" + latestEndTime + ", maxRunTime=" + maxRunTime + "]";
    }
}
