/* 文 件 名:  ServiceDefinationEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  业务实体
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.um.entity;

import java.util.Date;

/**
 * 业务实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-06-18]
 */
public class ServiceDefinationEntity
{
    private Integer serviceId;
    
    private String serviceName;
    
    private String desc;
    
    private String contactPerson;
    
    private String alarmEmailList;
    
    private String alarmMobileList;
    
    private Date createTime;
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public void setServiceId(String serviceId)
    {
        this.serviceId = Integer.parseInt(serviceId);
    }
    
    public String getServiceName()
    {
        return serviceName;
    }
    
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc == null ? null : desc.trim();
    }
    
    public String getContactPerson()
    {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson == null ? null : contactPerson.trim();
    }
    
    public String getAlarmEmailList()
    {
        return alarmEmailList;
    }
    
    public void setAlarmEmailList(String alarmEmailList)
    {
        this.alarmEmailList = alarmEmailList == null ? null : alarmEmailList.trim();
    }
    
    public String getAlarmMobileList()
    {
        return alarmMobileList;
    }
    
    public void setAlarmMobileList(String alarmMobileList)
    {
        this.alarmMobileList = alarmMobileList == null ? null : alarmMobileList.trim();
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ServiceDefinationEntity [serviceId=");
        builder.append(serviceId);
        builder.append(", serviceName=");
        builder.append(serviceName);
        builder.append(", desc=");
        builder.append(desc);
        builder.append(", contactPerson=");
        builder.append(contactPerson);
        builder.append(", alarmEmailList=");
        builder.append(alarmEmailList);
        builder.append(", alarmMobileList=");
        builder.append(alarmMobileList);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append("]");
        return builder.toString();
    }
}