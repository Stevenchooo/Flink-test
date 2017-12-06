/*
 * 文 件 名:  UserProfileEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  用户信息实体类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;


/**
 * 用户信息实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class UserProfileEntity
{
    //IMEI号
    private String deviceId;

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
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
        UserProfileEntity other = (UserProfileEntity)obj;
        if (deviceId == null)
        {
            if (other.deviceId != null)
            {
                return false;
            }
        }
        else if (!deviceId.equals(other.deviceId))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("UserProfileEntity [deviceId=");
        builder.append(deviceId);
        builder.append("]");
        return builder.toString();
    }
    
    
    
    
}