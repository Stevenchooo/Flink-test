/* 文 件 名:  ServiceTaskGroupInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  业务任务组信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

/**
 * 业务任务组信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-06-18]
 */
public class ServiceTaskGroupInfoEntity
{
    private Integer serviceId;
    
    private String taskGroup;
    
    private String desc;
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public String getTaskGroup()
    {
        return taskGroup;
    }
    
    public void setTaskGroup(String taskGroup)
    {
        this.taskGroup = taskGroup == null ? null : taskGroup.trim();
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc == null ? null : desc.trim();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ServiceTaskGroupInfoEntity [serviceId=");
        builder.append(serviceId);
        builder.append(", taskGroup=");
        builder.append(taskGroup);
        builder.append(", desc=");
        builder.append(desc);
        builder.append("]");
        return builder.toString();
    }
}