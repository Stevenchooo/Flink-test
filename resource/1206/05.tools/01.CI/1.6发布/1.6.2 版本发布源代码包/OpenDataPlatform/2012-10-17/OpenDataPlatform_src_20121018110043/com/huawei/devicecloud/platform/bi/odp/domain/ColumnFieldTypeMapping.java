/*
 * 文 件 名:  ColumnFieldTypeMapping.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  表列名、对象字段名与类型映射关系
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-16
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

/**
 * 表列名、对象字段名与类型映射关系
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-16]
 */
public class ColumnFieldTypeMapping extends ColumnFieldMapping
{
    //字段的java类型
    @SuppressWarnings("rawtypes")
    private Class javaType;
    
    @SuppressWarnings("rawtypes")
    public Class getJavaType()
    {
        return javaType;
    }
    
    @SuppressWarnings("rawtypes")
    public void setJavaType(Class javaType)
    {
        this.javaType = javaType;
    }
    
}
