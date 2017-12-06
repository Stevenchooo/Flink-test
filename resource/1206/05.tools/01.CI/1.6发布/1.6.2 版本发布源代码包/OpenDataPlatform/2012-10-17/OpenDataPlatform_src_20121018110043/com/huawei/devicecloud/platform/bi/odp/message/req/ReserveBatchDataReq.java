/*
 * 文 件 名:  ReserveBatchDataReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  预留批次数据请求体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

import java.util.List;

import com.huawei.devicecloud.platform.bi.odp.domain.DateRatioInfo;

/**
 * 预留批次数据请求体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class ReserveBatchDataReq extends AuthenReq
{
    //回调URL地址
    private String callbackURL;
    
    //过滤条件
    private String filterStmt;
    
    //分批信息
    private List<DateRatioInfo> batchInfo;
    
    //选择列枚举值列表
    private List<Integer> extractList;
    
    //预留天数
    private Integer days;
    
    //记录总数
    private Long recordNumber;
    
    //应用交易键
    private String appTransactionKey;
    
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
    
    public String getAppTransactionKey()
    {
        return appTransactionKey;
    }
    
    public void setAppTransactionKey(String appTransactionKey)
    {
        this.appTransactionKey = appTransactionKey;
    }
    
    public List<Integer> getExtractList()
    {
        return extractList;
    }
    
    public void setExtractList(List<Integer> extractList)
    {
        this.extractList = extractList;
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
    
    public List<DateRatioInfo> getBatchInfo()
    {
        return batchInfo;
    }
    
    public void setBatchInfo(List<DateRatioInfo> batchInfo)
    {
        this.batchInfo = batchInfo;
    }
    
    public Integer getDays()
    {
        return days;
    }
    
    public void setDays(Integer days)
    {
        this.days = days;
    }
    
    public Long getRecordNumber()
    {
        return recordNumber;
    }
    
    public void setRecordNumber(Long recordNumber)
    {
        this.recordNumber = recordNumber;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ReserveBatchDataReq [callbackURL=");
        builder.append(callbackURL);
        builder.append(", filterStmt=");
        builder.append(filterStmt);
        builder.append(", batchInfo=");
        builder.append(batchInfo);
        builder.append(", extractList=");
        builder.append(extractList);
        builder.append(", days=");
        builder.append(days);
        builder.append(", recordNumber=");
        builder.append(recordNumber);
        builder.append(", appTransactionKey=");
        builder.append(appTransactionKey);
        builder.append(", transactionId=");
        builder.append(transactionId);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
