package com.huawei.ide.services.algorithm.entity;


import java.io.Serializable;
/**
 * 
 * cards <功能详细描述>
 * 
 * @author cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月27日]
 * @see [相关类/方法]
 */
public class Cards implements  Serializable
{
	 private static final long serialVersionUID = 5591461352992337784L;
	
    /*
     * 本地应用的包名
     */
    private String packageName;
    
    /*
     * 卡片唯一标识
     */
    private String cardId;
    
    /*
     * 本地应用展示时间
     */
    private String pubTime;
    
    public String getPackageName()
    {
        return packageName;
    }
    
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    
    public String getCardId()
    {
        return cardId;
    }
    
    public void setCardId(String cardId)
    {
        this.cardId = cardId;
    }
    
    public String getPubTime()
    {
        return pubTime;
    }
    
    public void setPubTime(String pubTime)
    {
        this.pubTime = pubTime;
    }
    
    /**
     * hashCode
     * 
     * @return int
     */
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cardId == null) ? 0 : cardId.hashCode());
        result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
        result = prime * result + ((pubTime == null) ? 0 : pubTime.hashCode());
        return result;
    }
    
    /**
     * equals
     * 
     * @param obj
     *            obj
     * @return boolean
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
        Cards other = (Cards)obj;
        if (cardId == null)
        {
            if (other.cardId != null)
            {
                return false;
            }
        }
        else if (!cardId.equals(other.cardId))
        {
            return false;
        }
        if (packageName == null)
        {
            if (other.packageName != null)
            {
                return false;
            }
        }
        else if (!packageName.equals(other.packageName))
        {
            return false;
        }
        if (pubTime == null)
        {
            if (other.pubTime != null)
            {
                return false;
            }
        }
        else if (!pubTime.equals(other.pubTime))
        {
            return false;
        }
        return true;
    }
    
    /**
     * toString
     * 
     * @return String
     */
    @Override
    public String toString()
    {
        return "cards [packageName=" + packageName + ", cardId=" + cardId + ", pubTime=" + pubTime + "]";
    }
    
}
