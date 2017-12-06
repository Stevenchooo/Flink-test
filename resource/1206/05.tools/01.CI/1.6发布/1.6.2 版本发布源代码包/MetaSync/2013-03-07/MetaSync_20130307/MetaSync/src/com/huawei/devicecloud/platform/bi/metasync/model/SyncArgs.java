/*
 * 文 件 名:  SyncArgs.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  同步参数
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

import java.util.HashMap;
import java.util.Map;

import com.huawei.devicecloud.platform.bi.metasync.FilterParam;

/**
 * 同步参数
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-4]
 */
public class SyncArgs
{
    private String osUser;
    
    private String srcDbName;
    
    private String dstDbName;
    
    private boolean forceDelete;
    
    private Map<String,FilterParam> filters = new HashMap<String, FilterParam>();

    public boolean isForceDelete()
    {
        return forceDelete;
    }

    public void setForceDelete(boolean forceDelete)
    {
        this.forceDelete = forceDelete;
    }

    public String getOsUser()
    {
        return osUser;
    }

    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }

    public String getSrcDbName()
    {
        return srcDbName;
    }

    public void setSrcDbName(String srcDbName)
    {
        this.srcDbName = srcDbName;
    }

    public String getDstDbName()
    {
        return dstDbName;
    }

    public void setDstDbName(String dstDbName)
    {
        this.dstDbName = dstDbName;
    }

    public Map<String, FilterParam> getFilters()
    {
        return filters;
    }

    public void setFilters(Map<String, FilterParam> filters)
    {
        this.filters = filters;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SyncArgs [osUser=");
        builder.append(osUser);
        builder.append(", srcDbName=");
        builder.append(srcDbName);
        builder.append(", dstDbName=");
        builder.append(dstDbName);
        builder.append(", forceDelete=");
        builder.append(forceDelete);
        builder.append(", filters=");
        builder.append(filters);
        builder.append("]");
        return builder.toString();
    }
}
