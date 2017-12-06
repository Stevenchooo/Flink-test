/*
 * 文 件 名:  TableMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-1
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 表元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-1]
 */
public class TableMeta
{
    private Long tblId;
    
    private Integer createTime;
    
    private Long dbId;
    
    private Integer lastAccessTime;
    
    private String owner;
    
    private Integer retention;
    
    private String tblName;
    
    private String tblType;
    
    private String viewExpandedText;
    
    private String viewOriginalText;
    
    private Long sdId;
    
    /**
     * 默认构造函数
     */
    public TableMeta()
    {
    }
    
    /**
     * 默认构造函数
     * @param tblName 表名
     */
    public TableMeta(String tblName)
    {
        this.tblName = tblName;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tblName == null) ? 0 : tblName.hashCode());
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
        TableMeta other = (TableMeta)obj;
        if (tblName == null)
        {
            if (other.tblName != null)
            {
                return false;
            }
        }
        else if (!tblName.equals(other.tblName))
        {
            return false;
        }
        return true;
    }
    
    public Long getTblId()
    {
        return tblId;
    }
    
    public void setTblId(Long tblId)
    {
        this.tblId = tblId;
    }
    
    public Integer getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Integer createTime)
    {
        this.createTime = createTime;
    }
    
    public Long getDbId()
    {
        return dbId;
    }
    
    public void setDbId(Long dbId)
    {
        this.dbId = dbId;
    }
    
    public Integer getLastAccessTime()
    {
        return lastAccessTime;
    }
    
    public void setLastAccessTime(Integer lastAccessTime)
    {
        this.lastAccessTime = lastAccessTime;
    }
    
    public String getOwner()
    {
        return owner;
    }
    
    public void setOwner(String owner)
    {
        this.owner = owner;
    }
    
    public Integer getRetention()
    {
        return retention;
    }
    
    public void setRetention(Integer retention)
    {
        this.retention = retention;
    }
    
    public Long getSdId()
    {
        return sdId;
    }
    
    public void setSdId(Long sdId)
    {
        this.sdId = sdId;
    }
    
    public String getTblName()
    {
        return tblName;
    }
    
    public void setTblName(String tblName)
    {
        this.tblName = tblName;
    }
    
    public String getTblType()
    {
        return tblType;
    }
    
    public void setTblType(String tblType)
    {
        this.tblType = tblType;
    }
    
    public String getViewExpandedText()
    {
        return viewExpandedText;
    }
    
    public void setViewExpandedText(String viewExpandedText)
    {
        this.viewExpandedText = viewExpandedText;
    }
    
    public String getViewOriginalText()
    {
        return viewOriginalText;
    }
    
    public void setViewOriginalText(String viewOriginalText)
    {
        this.viewOriginalText = viewOriginalText;
    }
}
