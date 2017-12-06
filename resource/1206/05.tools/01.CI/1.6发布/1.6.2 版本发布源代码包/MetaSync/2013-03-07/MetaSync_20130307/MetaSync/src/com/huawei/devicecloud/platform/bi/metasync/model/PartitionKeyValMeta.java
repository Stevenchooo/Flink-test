/*
 * 文 件 名:  PartitionValuesMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  分区值元数据
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-1
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 分区值元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-1]
 */
public class PartitionKeyValMeta
{
    private Long partId;
    
    private String partKeyVal;
    
    private Integer integerIdx;

    public Long getPartId()
    {
        return partId;
    }

    public void setPartId(Long partId)
    {
        this.partId = partId;
    }

    public String getPartKeyVal()
    {
        return partKeyVal;
    }

    public void setPartKeyVal(String partKeyVal)
    {
        this.partKeyVal = partKeyVal;
    }

    public Integer getIntegerIdx()
    {
        return integerIdx;
    }

    public void setIntegerIdx(Integer integerIdx)
    {
        this.integerIdx = integerIdx;
    }
}
