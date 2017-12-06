/*
 * 文 件 名:  ServiceTaskGroup.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;

/**
 * 业务任务组
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-19]
 */
public class ServiceTaskGroup
{
    private Integer serviceId;
    
    private String taskGroup;
    
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
        this.taskGroup = taskGroup;
    }
    
    /**
     * 给mapper使用，产生形如"5,’任务组A‘"的字符串
     * @return 给mapper使用，产生形如"5,’任务组A‘"的字符串
     */
    @Override
    public String toString()
    {
        return String.format("%d,'%s'", serviceId, taskGroup);
    }
}
