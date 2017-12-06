/* 文 件 名:  OperateAuditInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 操作审计信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class OperateAuditInfoEntity
{
    private String operator;
    
    private Date operatorTime;
    
    private Integer serviceId;
    
    private Integer taskId;
    
    private String taskPeriod;
    
    private String loginIp;
    
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
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public Integer getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
    
    public String getTaskPeriod()
    {
        return taskPeriod;
    }
    
    public void setTaskPeriod(String taskPeriod)
    {
        this.taskPeriod = taskPeriod == null ? null : taskPeriod.trim();
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