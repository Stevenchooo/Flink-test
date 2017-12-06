/*
 * 文 件 名:  TaskIDSerialEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.entity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190929
 * @version [华为终端云统一账号模块, 2012-2-17]
 * @see  [相关类/方法]
 */
public class TaskIDSerialEntity
{
    private Integer serviceId;
    
    private Integer taskType;
    
    private Integer serialno;

    public Integer getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }

    public Integer getTaskType()
    {
        return taskType;
    }

    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }

    public Integer getSerialno()
    {
        return serialno;
    }

    public void setSerialno(Integer serialno)
    {
        this.serialno = serialno;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskIDSerialEntity [serviceid=");
        builder.append(serviceId);
        builder.append(", tasktype=");
        builder.append(taskType);
        builder.append(", serialno=");
        builder.append(serialno);
        builder.append("]");
        return builder.toString();
    }
    
}
