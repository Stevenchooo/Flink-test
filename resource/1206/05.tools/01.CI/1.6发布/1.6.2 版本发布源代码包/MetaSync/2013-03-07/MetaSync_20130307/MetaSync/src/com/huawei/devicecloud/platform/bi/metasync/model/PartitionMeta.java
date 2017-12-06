/*
 * 文 件 名:  PartitionMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  分区元数据
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-1
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 分区元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-1]
 */
public class PartitionMeta
{
    private Long partId;
    
    private Integer createTime;
    
    private Integer lastAccessTime;
    
    private String partName;
    
    private Long sdId;
    
    private Long tblId;
    
    public Long getPartId()
    {
        return partId;
    }
    
    public void setPartId(Long partId)
    {
        this.partId = partId;
    }
    
    public Integer getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Integer createTime)
    {
        this.createTime = createTime;
    }
    
    public Integer getLastAccessTime()
    {
        return lastAccessTime;
    }
    
    public void setLastAccessTime(Integer lastAccessTime)
    {
        this.lastAccessTime = lastAccessTime;
    }
    
    public String getPartName()
    {
        return partName;
    }
    
    public void setPartName(String partName)
    {
        this.partName = partName;
    }
    
    public Long getSdId()
    {
        return sdId;
    }
    
    public void setSdId(Long sdId)
    {
        this.sdId = sdId;
    }
    
    public Long getTblId()
    {
        return tblId;
    }
    
    public void setTblId(Long tblId)
    {
        this.tblId = tblId;
    }
}
