/*
 * 文 件 名:  GroupsPrepareInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.info;

import java.util.List;

import com.huawei.devicecloud.platform.bi.odp.domain.DateRatioInfo;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class GroupsPrepareInfo
{
    //过滤条件
    private String filterStmt;
    
    //是否次数受限标识
    private Boolean limitFlag;
    
    //分批信息
    private List<DateRatioInfo> batchInfo;
    
    //文件对应的ID
    private String imeiFileId;
    
    //预留天数
    private Integer days;
    
    //记录总数
    private Long recordCount;

    public String getFilterStmt()
    {
        return filterStmt;
    }

    public void setFilterStmt(String filterStmt)
    {
        this.filterStmt = filterStmt;
    }

    public Boolean getLimitFlag()
    {
        return limitFlag;
    }

    public void setLimitFlag(Boolean limitFlag)
    {
        this.limitFlag = limitFlag;
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

    public Integer getDays()
    {
        return days;
    }

    public void setDays(Integer days)
    {
        this.days = days;
    }

    public Long getRecordCount()
    {
        return recordCount;
    }

    public void setRecordCount(Long recordCount)
    {
        this.recordCount = recordCount;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batchInfo == null) ? 0 : batchInfo.hashCode());
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((filterStmt == null) ? 0 : filterStmt.hashCode());
        result = prime * result + ((imeiFileId == null) ? 0 : imeiFileId.hashCode());
        result = prime * result + ((limitFlag == null) ? 0 : limitFlag.hashCode());
        result = prime * result + ((recordCount == null) ? 0 : recordCount.hashCode());
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
        GroupsPrepareInfo other = (GroupsPrepareInfo)obj;
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
        if (limitFlag == null)
        {
            if (other.limitFlag != null)
            {
                return false;
            }
        }
        else if (!limitFlag.equals(other.limitFlag))
        {
            return false;
        }
        if (recordCount == null)
        {
            if (other.recordCount != null)
            {
                return false;
            }
        }
        else if (!recordCount.equals(other.recordCount))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupsPrepareInfo [filterStmt=");
        builder.append(filterStmt);
        builder.append(", limitFlag=");
        builder.append(limitFlag);
        builder.append(", batchInfo=");
        builder.append(batchInfo);
        builder.append(", imeiFileId=");
        builder.append(imeiFileId);
        builder.append(", days=");
        builder.append(days);
        builder.append(", recordCount=");
        builder.append(recordCount);
        builder.append("]");
        return builder.toString();
    }
    
}
