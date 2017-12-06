/*
 * 文 件 名:  ColumnFieldMapping.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  表列名与对象字段名映射关系
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-16
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

/**
 * 表列名与对象字段名映射关系
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-16]
 */
public class ColumnFieldMapping
{
    //列名
    private String columnName;
    
    //字段名
    private String fieldName;
    
    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }
}
