/*
 * 文 件 名:  QueryDataCountReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  查询记录总数
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

/**
 * 查询记录总数
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class QueryDataCountReq extends AuthenReq
{
    //回调URL地址
    private String callbackURL;
    
    //过滤条件
    private String filterStmt;
    
    //记录总数
    private Long recordCount;
    
    //交易Id
    private String transactionId;
    
    public String getTransactionId()
    {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }
    
    public Long getRecordCount()
    {
        return recordCount;
    }
    
    public void setRecordCount(Long recordCount)
    {
        this.recordCount = recordCount;
    }
    
    public String getCallbackURL()
    {
        return callbackURL;
    }
    
    public void setCallbackURL(String callbackURL)
    {
        this.callbackURL = callbackURL;
    }
    
    public String getFilterStmt()
    {
        return filterStmt;
    }
    
    public void setFilterStmt(String filterStmt)
    {
        this.filterStmt = filterStmt;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("QueryDataCountReq [callbackURL=");
        builder.append(callbackURL);
        builder.append(", filterStmt=");
        builder.append(filterStmt);
        builder.append(", recordCount=");
        builder.append(recordCount);
        builder.append(", transactionId=");
        builder.append(transactionId);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
