/*
 * 文 件 名:  SequenceInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  序号相关信息
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-28
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 序号相关信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-2-28]
 */
public class SequenceInfo
{
    /**
     * 表Id
     */
    private Long tId;
    
    /**
     * 分区Id
     */
    private Long pId;
    
    /**
     * 存储Id
     */
    private Long sdId;
    
    /**
     * 序列号Id
     */
    private Long serId;
    
    /**
     * 获取tId值并将其加1
     * @return tId
     */
    public Long incAndGetTId()
    {
        return this.tId++;
    }
    
    /**
     * 获取pId值并将其加1
     * @return pId
     */
    public Long incAndGetPId()
    {
        return this.pId++;
    }
    
    /**
     * 获取serId值并将其加1
     * @return serId
     */
    public Long incAndGetSerId()
    {
        return this.serId++;
    }
    
    /**
     * 获取sdid值并将其加1
     * @return sdId
     */
    public Long incAndGetSdId()
    {
        return this.sdId++;
    }
    
    public Long getTId()
    {
        return tId;
    }
    
    public void setTId(Long tId)
    {
        this.tId = tId;
    }
    
    public Long getPId()
    {
        return pId;
    }
    
    public void setPId(Long pId)
    {
        this.pId = pId;
    }
    
    public Long getSdId()
    {
        return sdId;
    }
    
    public void setSdId(Long sdId)
    {
        this.sdId = sdId;
    }
    
    public Long getSerId()
    {
        return serId;
    }
    
    public void setSerId(Long serId)
    {
        this.serId = serId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SequenceInfo [tId=");
        builder.append(tId);
        builder.append(", pId=");
        builder.append(pId);
        builder.append(", sdId=");
        builder.append(sdId);
        builder.append(", serId=");
        builder.append(serId);
        builder.append("]");
        return builder.toString();
    }
}
