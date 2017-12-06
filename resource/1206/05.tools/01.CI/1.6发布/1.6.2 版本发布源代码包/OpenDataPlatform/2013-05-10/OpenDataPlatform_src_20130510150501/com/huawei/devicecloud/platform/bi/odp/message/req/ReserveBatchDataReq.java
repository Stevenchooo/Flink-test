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
    
    //iemi文件id
    private String imeiFileId;
    
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

    public String getImeiFileId()
    {
        return imeiFileId;
    }

    public void setImeiFileId(String imeiFileId)
    {
        this.imeiFileId = imeiFileId;
    }

    public List<Integer> getExtractList()
    {
        return extractList;
    }

    public void setExtractList(List<Integer> extractList)
    {
        this.extractList = extractList;
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

    public String getAppTransactionKey()
    {
        return appTransactionKey;
    }

    public void setAppTransactionKey(String appTransactionKey)
    {
        this.appTransactionKey = appTransactionKey;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appTransactionKey == null) ? 0 : appTransactionKey.hashCode());
        result = prime * result + ((batchInfo == null) ? 0 : batchInfo.hashCode());
        result = prime * result + ((callbackURL == null) ? 0 : callbackURL.hashCode());
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((extractList == null) ? 0 : extractList.hashCode());
        result = prime * result + ((filterStmt == null) ? 0 : filterStmt.hashCode());
        result = prime * result + ((imeiFileId == null) ? 0 : imeiFileId.hashCode());
        result = prime * result + ((recordNumber == null) ? 0 : recordNumber.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
        ReserveBatchDataReq other = (ReserveBatchDataReq)obj;
        if (appTransactionKey == null)
        {
            if (other.appTransactionKey != null)
            {
                return false;
            }
        }
        else if (!appTransactionKey.equals(other.appTransactionKey))
        {
            return false;
        }
        if (batchInfo == null)
        {
            if (other.batchInfo != null)
            {
                return false;
            }
        }
        else if (!batchInfo.equals(other.batchInfo))
        {
            return false;
        }
        if (callbackURL == null)
        {
            if (other.callbackURL != null)
            {
                return false;
            }
        }
        else if (!callbackURL.equals(other.callbackURL))
        {
            return false;
        }
        if (days == null)
        {
            if (other.days != null)
            {
                return false;
            }
        }
        else if (!days.equals(other.days))
        {
            return false;
        }
        if (extractList == null)
        {
            if (other.extractList != null)
            {
                return false;
            }
        }
        else if (!extractList.equals(other.extractList))
        {
            return false;
        }
        if (filterStmt == null)
        {
            if (other.filterStmt != null)
            {
                return false;
            }
        }
        else if (!filterStmt.equals(other.filterStmt))
        {
            return false;
        }
        if (imeiFileId == null)
        {
            if (other.imeiFileId != null)
            {
                return false;
            }
        }
        else if (!imeiFileId.equals(other.imeiFileId))
        {
            return false;
        }
        if (recordNumber == null)
        {
            if (other.recordNumber != null)
            {
                return false;
            }
        }
        else if (!recordNumber.equals(other.recordNumber))
        {
            return false;
        }
        if (transactionId == null)
        {
            if (other.transactionId != null)
            {
                return false;
            }
        }
        else if (!transactionId.equals(other.transactionId))
        {
            return false;
        }
        return true;
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
        builder.append(", imeiFileId=");
        builder.append(imeiFileId);
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
        builder.append("]");
        return builder.toString();
    }
    
}
