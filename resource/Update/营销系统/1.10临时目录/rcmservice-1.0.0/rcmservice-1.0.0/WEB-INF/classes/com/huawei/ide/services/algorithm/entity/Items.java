package com.huawei.ide.services.algorithm.entity;

import java.io.Serializable;

/**
 * 
 * Items
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年7月20日]
 * @see  [相关类/方法]
 */
public class Items implements Serializable
{
    private static final long serialVersionUID = 5591461352992337784L;
    
    private String itemId;
    
    private String templateId;
    
    private String pubTime;
    
    private String score;
    
    public String getItemId()
    {
        return itemId;
    }
    
    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }
    
    public String getTemplateId()
    {
        return templateId;
    }
    
    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }
    
    public String getPubTime()
    {
        return pubTime;
    }
    
    public void setPubTime(String pubTime)
    {
        this.pubTime = pubTime;
    }
    
    public String getScore()
    {
        return score;
    }
    
    public void setScore(String score)
    {
        this.score = score;
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
        result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
        result = prime * result + ((pubTime == null) ? 0 : pubTime.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
        return result;
    }
    
    /**
     * equals
     * @param obj
     *          obj
     * @return  boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Items other = (Items)obj;
        if (itemId == null)
        {
            if (other.itemId != null)
                return false;
        }
        else if (!itemId.equals(other.itemId))
            return false;
        if (pubTime == null)
        {
            if (other.pubTime != null)
                return false;
        }
        else if (!pubTime.equals(other.pubTime))
            return false;
        if (score == null)
        {
            if (other.score != null)
                return false;
        }
        else if (!score.equals(other.score))
            return false;
        if (templateId == null)
        {
            if (other.templateId != null)
                return false;
        }
        else if (!templateId.equals(other.templateId))
            return false;
        return true;
    }
    
    /**
     * toString
     * @return  String
     */
    @Override
    public String toString()
    {
        return "Items [itemId=" + itemId + ", templateId=" + templateId + ", pubTime=" + pubTime + ", score=" + score
            + "]";
    }
    
}
