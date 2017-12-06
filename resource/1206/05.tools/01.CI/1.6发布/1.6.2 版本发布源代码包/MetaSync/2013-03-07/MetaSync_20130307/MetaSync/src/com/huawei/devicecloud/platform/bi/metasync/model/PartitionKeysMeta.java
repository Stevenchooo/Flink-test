/*
 * 文 件 名:  PartitionKeysMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  分区键元数据
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 分区键元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-4]
 */
public class PartitionKeysMeta
{
    private String pkeyComment;
    
    private String pkeyName;
    
    private String pkeyType;
    
    private Integer integerIdx;
    
    private Long tblId;

    public String getPkeyComment()
    {
        return pkeyComment;
    }

    public void setPkeyComment(String pkeyComment)
    {
        this.pkeyComment = pkeyComment;
    }

    public String getPkeyName()
    {
        return pkeyName;
    }

    public void setPkeyName(String pkeyName)
    {
        this.pkeyName = pkeyName;
    }

    public String getPkeyType()
    {
        return pkeyType;
    }

    public void setPkeyType(String pkeyType)
    {
        this.pkeyType = pkeyType;
    }

    public Integer getIntegerIdx()
    {
        return integerIdx;
    }

    public void setIntegerIdx(Integer integerIdx)
    {
        this.integerIdx = integerIdx;
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
