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
    private Integer serviceid;
    
    private Integer tasktype;
    
    private Integer serialno;

    public Integer getServiceid()
    {
        return serviceid;
    }

    public void setServiceid(Integer serviceid)
    {
        this.serviceid = serviceid;
    }

    public Integer getTasktype()
    {
        return tasktype;
    }

    public void setTasktype(Integer tasktype)
    {
        this.tasktype = tasktype;
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
        builder.append(serviceid);
        builder.append(", tasktype=");
        builder.append(tasktype);
        builder.append(", serialno=");
        builder.append(serialno);
        builder.append("]");
        return builder.toString();
    }
    
}
