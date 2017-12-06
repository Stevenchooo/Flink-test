/*
 * 文 件 名:  InstanceInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  实例信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-30
 */
package com.huawei.platform.tcc.SSH;

/**
 * 实例信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-30]
 */
public class InstanceInfo
{
    //任务实例Id
    private Object taskInstanceId;
    
    //状态
    private int state;
    
    /**
     * 构造函数
     * @param instanceId 实例Id
     * @param state 状态
     */
    public InstanceInfo(Object instanceId, int state)
    {
        this.taskInstanceId = instanceId;
        this.state = state;
    }
    
    protected Object getTaskInstanceId()
    {
        return taskInstanceId;
    }
    
    protected void setTaskInstanceId(Object taskInstanceId)
    {
        this.taskInstanceId = taskInstanceId;
    }
    
    protected int getState()
    {
        return state;
    }
    
    protected void setState(int state)
    {
        this.state = state;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("InstanceInfo [taskInstanceId=");
        builder.append(taskInstanceId);
        builder.append(", state=");
        builder.append(state);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + state;
        result = prime * result + ((taskInstanceId == null) ? 0 : taskInstanceId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        InstanceInfo other = (InstanceInfo)obj;
        if (state != other.state)
        {
            return false;
        }
        if (taskInstanceId == null)
        {
            if (other.taskInstanceId != null)
            {
                return false;
            }
        }
        else if (!taskInstanceId.equals(other.taskInstanceId))
        {
            return false;
        }
        return true;
    }
}
