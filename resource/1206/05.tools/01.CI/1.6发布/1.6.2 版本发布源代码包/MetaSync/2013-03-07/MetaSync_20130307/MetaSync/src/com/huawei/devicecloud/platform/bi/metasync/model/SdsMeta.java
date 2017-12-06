/*
 * 文 件 名:  SdsMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-1
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 存储描述元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-1]
 */
public class SdsMeta
{
    private Long sdId;
    
    private String inputFormat;
    
    private Integer isCompressed;
    
    private String location;
    
    private Integer numBuckets;
    
    private String outputFormat;
    
    private Long serdeId;
    
    public Long getSdId()
    {
        return sdId;
    }

    public void setSdId(Long sdId)
    {
        this.sdId = sdId;
    }

    public String getInputFormat()
    {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat)
    {
        this.inputFormat = inputFormat;
    }

    public Integer getIsCompressed()
    {
        return isCompressed;
    }

    public void setIsCompressed(Integer isCompressed)
    {
        this.isCompressed = isCompressed;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getNumBuckets()
    {
        return numBuckets;
    }

    public void setNumBuckets(Integer numBuckets)
    {
        this.numBuckets = numBuckets;
    }

    public String getOutputFormat()
    {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat)
    {
        this.outputFormat = outputFormat;
    }

    public Long getSerdeId()
    {
        return serdeId;
    }

    public void setSerdeId(Long serdeId)
    {
        this.serdeId = serdeId;
    }
}
