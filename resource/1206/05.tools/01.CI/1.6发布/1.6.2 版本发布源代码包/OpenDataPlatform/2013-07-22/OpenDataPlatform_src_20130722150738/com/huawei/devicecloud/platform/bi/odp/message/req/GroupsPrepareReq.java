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
    
    
    private Integer user_ad_flag;
    
    //分批信息
    private List<DateRatioInfo> batch_info;
    
    //文件对应的ID
    private String imei_file_id;
    
    //预留天数
    private Integer days;
    
    //记录总数
    private Long record_count;
    
    //应用交易id
    private String app_transaction_id;
    
    
    /**
     * 创建预留批数据请求对象
     * @return 预留批数据请求对象
     */
    public ReserveBatchDataReq createReserveBatchDataReq()
    {
        final ReserveBatchDataReq req = new ReserveBatchDataReq();
        req.setAppId(this.getApp_id());
        req.setAppTransactionKey(app_transaction_id);
        req.setAuthenInfo(this.getAuthen_info());
        req.setBatchInfo(batch_info);
        req.setImeiFileId(imei_file_id);
        req.setCallbackURL(callback_url);
        req.setDays(days);
        req.setExtractList(columns);
        req.setFilterStmt(filter_stmt);
        req.setRecordNumber(record_count);
        req.setTimestamp(this.getTimestamp());
        req.setTransactionId(transaction_id);
        req.setUserAdFlag(user_ad_flag);
        return req;
    }


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


    public Integer getUser_ad_flag()
    {
        return user_ad_flag;
    }


    public void setUser_ad_flag(Integer user_ad_flag)
    {
        this.user_ad_flag = user_ad_flag;
    }


    public List<DateRatioInfo> getBatch_info()
    {
        return batch_info;
    }


    public void setBatch_info(List<DateRatioInfo> batch_info)
    {
        this.batch_info = batch_info;
    }


    public String getImei_file_id()
    {
        return imei_file_id;
    }


    public void setImei_file_id(String imei_file_id)
    {
        this.imei_file_id = imei_file_id;
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


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((app_transaction_id == null) ? 0 : app_transaction_id.hashCode());
        result = prime * result + ((batch_info == null) ? 0 : batch_info.hashCode());
        result = prime * result + ((callback_url == null) ? 0 : callback_url.hashCode());
        result = prime * result + ((columns == null) ? 0 : columns.hashCode());
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((filter_stmt == null) ? 0 : filter_stmt.hashCode());
        result = prime * result + ((imei_file_id == null) ? 0 : imei_file_id.hashCode());
        result = prime * result + ((record_count == null) ? 0 : record_count.hashCode());
        result = prime * result + ((transaction_id == null) ? 0 : transaction_id.hashCode());
        result = prime * result + ((user_ad_flag == null) ? 0 : user_ad_flag.hashCode());
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
        GroupsPrepareReq other = (GroupsPrepareReq)obj;
        if (app_transaction_id == null)
        {
            if (other.app_transaction_id != null)
            {
                return false;
            }
        }
        else if (!app_transaction_id.equals(other.app_transaction_id))
        {
            return false;
        }
        if (batch_info == null)
        {
            if (other.batch_info != null)
            {
                return false;
            }
        }
        else if (!batch_info.equals(other.batch_info))
        {
            return false;
        }
        if (callback_url == null)
        {
            if (other.callback_url != null)
            {
                return false;
            }
        }
        else if (!callback_url.equals(other.callback_url))
        {
            return false;
        }
        if (columns == null)
        {
            if (other.columns != null)
            {
                return false;
            }
        }
        else if (!columns.equals(other.columns))
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
        if (filter_stmt == null)
        {
            if (other.filter_stmt != null)
            {
                return false;
            }
        }
        else if (!filter_stmt.equals(other.filter_stmt))
        {
            return false;
        }
        if (imei_file_id == null)
        {
            if (other.imei_file_id != null)
            {
                return false;
            }
        }
        else if (!imei_file_id.equals(other.imei_file_id))
        {
            return false;
        }
        if (record_count == null)
        {
            if (other.record_count != null)
            {
                return false;
            }
        }
        else if (!record_count.equals(other.record_count))
        {
            return false;
        }
        if (transaction_id == null)
        {
            if (other.transaction_id != null)
            {
                return false;
            }
        }
        else if (!transaction_id.equals(other.transaction_id))
        {
            return false;
        }
        if (user_ad_flag == null)
        {
            if (other.user_ad_flag != null)
            {
                return false;
            }
        }
        else if (!user_ad_flag.equals(other.user_ad_flag))
        {
            return false;
        }
        return true;
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
        builder.append(", user_ad_flag=");
        builder.append(user_ad_flag);
        builder.append(", batch_info=");
        builder.append(batch_info);
        builder.append(", imei_file_id=");
        builder.append(imei_file_id);
        builder.append(", days=");
        builder.append(days);
        builder.append(", record_count=");
        builder.append(record_count);
        builder.append(", app_transaction_id=");
        builder.append(app_transaction_id);
        builder.append("]");
        return builder.toString();
    }
    
}
