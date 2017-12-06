/*
 * 文 件 名:  UserQueryReq.java
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
public class UserQueryReq extends GroupAuthenReq
{
    //回调URL地址
    private String callback_url;
    
    //交易Id
    private String transaction_id;
    
    //记录总数
    private Long record_count;
    
    //过滤条件
    private String filter_stmt;
    
    public String getCallback_url()
    {
        return callback_url;
    }
    
    public void setCallback_url(String callback_url)
    {
        this.callback_url = callback_url;
    }
    
    public String getTransaction_id()
    {
        return transaction_id;
    }
    
    public void setTransaction_id(String transaction_id)
    {
        this.transaction_id = transaction_id;
    }
    
    public Long getRecord_count()
    {
        return record_count;
    }
    
    public void setRecord_count(Long record_count)
    {
        this.record_count = record_count;
    }
    
    public String getFilter_stmt()
    {
        return filter_stmt;
    }
    
    public void setFilter_stmt(String filter_stmt)
    {
        this.filter_stmt = filter_stmt;
    }
    
    /**
     * 创建查询数据请求对象
     * @return 查询数据请求对象
     */
    public QueryDataCountReq createQueryDataCountReq()
    {
        QueryDataCountReq req = new QueryDataCountReq();
        req.setAppId(this.getApp_id());
        req.setAuthenInfo(this.getAuthen_info());
        req.setCallbackURL(this.callback_url);
        req.setFilterStmt(this.filter_stmt);
        req.setRecordCount(this.record_count);
        req.setTimestamp(this.getTimestamp());
        req.setTransactionId(this.transaction_id);
        
        return req;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("UserQueryReq [callback_url=");
        builder.append(callback_url);
        builder.append(", transaction_id=");
        builder.append(transaction_id);
        builder.append(", record_count=");
        builder.append(record_count);
        builder.append(", filter_stmt=");
        builder.append(filter_stmt);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
