/*
 * 文 件 名:  GroupsPrepareReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  用户群准备接口请求体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

import java.util.List;

import com.huawei.devicecloud.platform.bi.odp.domain.DateRatioInfo;

/**
 * 用户群准备接口请求体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class GroupsPrepareReq extends GroupAuthenReq
{
    //回调url地址
    private String callback_url;
    
    //交易Id
    private String transaction_id;
    
    //列枚举值列表
    private List<Integer> columns;
    
    //过滤条件
    private String filter_stmt;
    
    //分批信息
    private List<DateRatioInfo> batch_info;
    
    //预留天数
    private Integer days;
    
    //记录总数
    private Long record_count;
    
    //应用交易id
    private String app_transaction_id;
    
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
    
    public List<Integer> getColumns()
    {
        return columns;
    }
    
    public void setColumns(List<Integer> columns)
    {
        this.columns = columns;
    }
    
    public String getFilter_stmt()
    {
        return filter_stmt;
    }
    
    public void setFilter_stmt(String filter_stmt)
    {
        this.filter_stmt = filter_stmt;
    }
    
    public List<DateRatioInfo> getBatch_info()
    {
        return batch_info;
    }
    
    public void setBatch_info(List<DateRatioInfo> batch_info)
    {
        this.batch_info = batch_info;
    }
    
    public Integer getDays()
    {
        return days;
    }
    
    public void setDays(Integer days)
    {
        this.days = days;
    }
    
    public Long getRecord_count()
    {
        return record_count;
    }
    
    public void setRecord_count(Long record_count)
    {
        this.record_count = record_count;
    }
    
    public String getApp_transaction_id()
    {
        return app_transaction_id;
    }
    
    public void setApp_transaction_id(String app_transaction_id)
    {
        this.app_transaction_id = app_transaction_id;
    }
    
    /**
     * 创建预留批数据请求对象
     * @return 预留批数据请求对象
     */
    public ReserveBatchDataReq createReserveBatchDataReq()
    {
        ReserveBatchDataReq req = new ReserveBatchDataReq();
        req.setAppId(this.getApp_id());
        req.setAppTransactionKey(app_transaction_id);
        req.setAuthenInfo(this.getAuthen_info());
        req.setBatchInfo(batch_info);
        req.setCallbackURL(callback_url);
        req.setDays(days);
        req.setExtractList(columns);
        req.setFilterStmt(filter_stmt);
        req.setRecordNumber(record_count);
        req.setTimestamp(this.getTimestamp());
        req.setTransactionId(transaction_id);
        return req;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupsPrepareReq [callback_url=");
        builder.append(callback_url);
        builder.append(", transaction_id=");
        builder.append(transaction_id);
        builder.append(", columns=");
        builder.append(columns);
        builder.append(", filter_stmt=");
        builder.append(filter_stmt);
        builder.append(", batch_info=");
        builder.append(batch_info);
        builder.append(", days=");
        builder.append(days);
        builder.append(", record_count=");
        builder.append(record_count);
        builder.append(", app_transaction_id=");
        builder.append(app_transaction_id);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
