/*
 * 文 件 名:  ReservedInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  预留信息类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

import java.util.Date;

/**
 * 预留信息类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class ReservedNewInfoEntity
{
    //预留Id
    private String groupId;
    
    //使用日期
    private Date useDate;
    
    //实现日期
    private Date expiredDate;
    
    //保留天数
    private Integer days;
    
    //状态
    private Integer state;
    
    //文件的路径地址
    private String fileUrl;
    
    //创建预留信息的应用标识
    private String appId;
    
    //数目
    private Long cnt;

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public Date getUseDate()
    {
        return useDate;
    }

    public void setUseDate(Date useDate)
    {
        this.useDate = useDate;
    }

    public Date getExpiredDate()
    {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate)
    {
        this.expiredDate = expiredDate;
    }

    public Integer getDays()
    {
        return days;
    }

    public void setDays(Integer days)
    {
        this.days = days;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public Long getCnt()
    {
        return cnt;
    }

    public void setCnt(Long cnt)
    {
        this.cnt = cnt;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appId == null) ? 0 : appId.hashCode());
        result = prime * result + ((cnt == null) ? 0 : cnt.hashCode());
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((expiredDate == null) ? 0 : expiredDate.hashCode());
        result = prime * result + ((fileUrl == null) ? 0 : fileUrl.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((useDate == null) ? 0 : useDate.hashCode());
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
        ReservedNewInfoEntity other = (ReservedNewInfoEntity)obj;
        if (appId == null)
        {
            if (other.appId != null)
            {
                return false;
            }
        }
        else if (!appId.equals(other.appId))
        {
            return false;
        }
        if (cnt == null)
        {
            if (other.cnt != null)
            {
                return false;
            }
        }
        else if (!cnt.equals(other.cnt))
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
        if (expiredDate == null)
        {
            if (other.expiredDate != null)
            {
                return false;
            }
        }
        else if (!expiredDate.equals(other.expiredDate))
        {
            return false;
        }
        if (fileUrl == null)
        {
            if (other.fileUrl != null)
            {
                return false;
            }
        }
        else if (!fileUrl.equals(other.fileUrl))
        {
            return false;
        }
        if (groupId == null)
        {
            if (other.groupId != null)
            {
                return false;
            }
        }
        else if (!groupId.equals(other.groupId))
        {
            return false;
        }
        if (state == null)
        {
            if (other.state != null)
            {
                return false;
            }
        }
        else if (!state.equals(other.state))
        {
            return false;
        }
        if (useDate == null)
        {
            if (other.useDate != null)
            {
                return false;
            }
        }
        else if (!useDate.equals(other.useDate))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ReservedNewInfoEntity [groupId=");
        builder.append(groupId);
        builder.append(", useDate=");
        builder.append(useDate);
        builder.append(", expiredDate=");
        builder.append(expiredDate);
        builder.append(", days=");
        builder.append(days);
        builder.append(", state=");
        builder.append(state);
        builder.append(", fileUrl=");
        builder.append(fileUrl);
        builder.append(", appId=");
        builder.append(appId);
        builder.append(", cnt=");
        builder.append(cnt);
        builder.append("]");
        return builder.toString();
    }

}