/*
 * 文 件 名:  AuditInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  审计信息实体类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

import java.util.Date;

/**
 * 审计信息实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class AuditInfoEntity
{
    //应用标识
    private String appId;
    
    //操作时间
    private Date operatorTime;
    
    //操作类型
    private Integer operatorType;
    
    //预留Id
    private String reserveId;
    
    //记录数
    private Long recordNumber;
    
    //应用TransactionKey
    private String appTransactionKey;
    
    public String getAppId()
    {
        return appId;
    }
    
    public void setAppId(String appId)
    {
        this.appId = appId;
    }
    
    public Date getOperatorTime()
    {
        return operatorTime;
    }
    
    public void setOperatorTime(Date operatorTime)
    {
        this.operatorTime = operatorTime;
    }
    
    public Integer getOperatorType()
    {
        return operatorType;
    }
    
    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }
    
    public String getReserveId()
    {
        return reserveId;
    }
    
    public void setReserveId(String reserveId)
    {
        this.reserveId = reserveId;
    }
    
    public Long getRecordNumber()
    {
        return recordNumber;
    }
    
    public void setRecordNumber(Long recordNumber)
    {
        this.recordNumber = recordNumber;
    }
    
    public String getAppTransactionKey()
    {
        return appTransactionKey;
    }
    
    public void setAppTransactionKey(String appTransactionKey)
    {
        this.appTransactionKey = appTransactionKey;
    }
    
}