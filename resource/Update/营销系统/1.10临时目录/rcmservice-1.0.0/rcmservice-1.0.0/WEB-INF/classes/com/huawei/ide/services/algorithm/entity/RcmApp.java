package com.huawei.ide.services.algorithm.entity;

/**
 * 
 * RcmApp
 * <功能详细描述>
 * 
 * @author  z00280396
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class RcmApp
{
    
    private String appId;
    
    public String getAppId()
    {
        return appId;
    }
    
    public void setAppId(String appId)
    {
        this.appId = appId;
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
        result = prime * result + ((appId == null) ? 0 : appId.hashCode());
        return result;
    }
    
    /**
     * equals
     * @param obj
     *        obj
     * @return  boolean
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
        RcmApp other = (RcmApp)obj;
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
        return true;
    }
    
    /**
     * toString
     * @return  String
     */
    @Override
    public String toString()
    {
        return "RcmApp [appId=" + appId + "]";
    }
    
}
