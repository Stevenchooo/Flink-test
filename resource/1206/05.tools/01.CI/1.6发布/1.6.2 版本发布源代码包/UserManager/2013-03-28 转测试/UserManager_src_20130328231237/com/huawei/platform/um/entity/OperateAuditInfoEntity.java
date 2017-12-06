/* 文 件 名:  OperateAuditInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-07-03
 */
package com.huawei.platform.um.entity;

import java.util.Date;
import java.util.List;

/**
 * 操作员信息
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-07-03]
 */
public class OperateAuditInfoEntity
{
    private Long id;
    
    private String operator;
    
    private Date operatorTime;
    
    private Integer opType;
    
    private String serviceId;
    
    private String taskId;
    
    private String operParameters;
    
    private String loginIp;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator == null ? null : operator.trim();
    }
    
    public Date getOperatorTime()
    {
        return operatorTime;
    }
    
    public void setOperatorTime(Date operatorTime)
    {
        this.operatorTime = operatorTime;
    }
    
    public Integer getOpType()
    {
        return opType;
    }
    
    public void setOpType(Integer opType)
    {
        this.opType = opType;
    }
    
    public String getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }

    /**
     * 由serviceid用';'拼接
     * @param serviceid serviceid
     * @see [类、类#方法、类#成员]
     */
    public void setServiceIdSingle(String serviceid)
    {
        this.serviceId = ";" + serviceid + ";";
    }
    
    /**
     * 由serviceId集合用';'拼接
     * @param serviceIds serviceId集合
     * @see [类、类#方法、类#成员]
     */
    public void setServiceIds(List<String> serviceIds)
    {
        StringBuilder builder = new StringBuilder();
        int num = serviceIds.size();
        if (0 == num)
        {
            return;
        }
        builder.append(";");
        for (int i = 0; i < num; i++)
        {
            builder.append(serviceIds.get(i)).append(";");
        }
        this.serviceId = builder.toString();
    }
    
    public String getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }
    
    /**
     * 由taskid用';'拼接
     * @param taskid taskid
     * @see [类、类#方法、类#成员]
     */
    public void setTaskIdSingle(String taskid)
    {
        this.taskId = ";" + taskid + ";";
    }
    
    /**
     * 由taskId集合用';'拼接
     * @param taskIds taskId集合
     * @see [类、类#方法、类#成员]
     */
    public void setTaskIds(List<Long> taskIds)
    {
        StringBuilder builder = new StringBuilder();
        int num = taskIds.size();
        if (0 == num)
        {
            return;
        }
        builder.append(";");
        for (int i = 0; i < num; i++)
        {
            builder.append(String.valueOf(taskIds.get(i))).append(";");
        }
        this.taskId = builder.toString();
    }
    
    public String getOperParameters()
    {
        return operParameters;
    }
    
    public void setOperParameters(String operParameters)
    {
        this.operParameters = operParameters == null ? null
                : operParameters.trim();
    }
    
    public String getLoginIp()
    {
        return loginIp;
    }
    
    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }
}