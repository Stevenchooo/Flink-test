/*
 * 文 件 名:  TaskOwnerInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务归属(权限相关)信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-24
 */
package com.huawei.platform.tcc.domain;

/**
 * 任务归属信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-24]
 */
public class TaskOwnerInfo
{
    private Long taskId;
    
    private String taskName;
    
    private String creator;
    
    private String osUser;
    
    private String userGroup;
    
    private Integer serviceId;
    
    private String serviceName;
    
    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public String getTaskName()
    {
        return taskName;
    }
    
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }
    
    public String getOsUser()
    {
        return osUser;
    }
    
    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }
    
    public String getUserGroup()
    {
        return userGroup;
    }
    
    public void setUserGroup(String userGroup)
    {
        this.userGroup = userGroup;
    }
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public String getServiceName()
    {
        return serviceName;
    }
    
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
}
