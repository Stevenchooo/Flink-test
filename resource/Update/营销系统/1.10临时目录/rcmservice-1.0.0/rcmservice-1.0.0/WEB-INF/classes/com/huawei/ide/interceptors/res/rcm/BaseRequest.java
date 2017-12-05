/*
 * 文 件 名:  BasicReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2008-2010,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-3-25
 */
package com.huawei.ide.interceptors.res.rcm;

/**
 * 
 * <请求基类>
 * 
 * @author  z00280396
 * @version [Internet Business Service Platform SP V100R100, 2014-2-20]
 * @see  [相关类/方法]
 */
public class BaseRequest
{
    /**
     * 业务id
     */
    private String appKey;
    
    /**
     * 时间戳，用于加密和缓存透传
     */
    private String ts;
    
    public String getAppKey()
    {
        return appKey;
    }
    
    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }
    
    public String getTs()
    {
        return ts;
    }
    
    public void setTs(String ts)
    {
        this.ts = ts;
    }
    
    /**
     * hashCode
     * @return  int
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appKey == null) ? 0 : appKey.hashCode());
        result = prime * result + ((ts == null) ? 0 : ts.hashCode());
        return result;
    }
    
    /**
     * equals
     * @param obj
     *        obj
     * @return   boolean
     */
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
        BaseRequest other = (BaseRequest)obj;
        if (appKey == null)
        {
            if (other.appKey != null)
            {
                return false;
            }
        }
        else if (!appKey.equals(other.appKey))
        {
            return false;
        }
        if (ts == null)
        {
            if (other.ts != null)
            {
                return false;
            }
        }
        else if (!ts.equals(other.ts))
        {
            return false;
        }
        return true;
    }
    
    /**
     * toString
     * @return   String
     */
    @Override
    public String toString()
    {
        return "BaseRequest [appKey=" + appKey + ", ts=" + ts + "]";
    }
    
}
