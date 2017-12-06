/*
 * 文 件 名:  ColumnsMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  列元数据
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 列元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-4]
 */
public class ColumnsMeta
{
    private Long sdId;
    
    private String comment;
    
    private String columnName;
    
    private String typeName;
    
    private int integerIdx;
    
    public Long getSdId()
    {
        return sdId;
    }
    
    public void setSdId(Long sdId)
    {
        this.sdId = sdId;
    }
    
    public String getComment()
    {
        return comment;
    }
    
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
    public int getIntegerIdx()
    {
        return integerIdx;
    }
    
    public void setIntegerIdx(int integerIdx)
    {
        this.integerIdx = integerIdx;
    }
}
