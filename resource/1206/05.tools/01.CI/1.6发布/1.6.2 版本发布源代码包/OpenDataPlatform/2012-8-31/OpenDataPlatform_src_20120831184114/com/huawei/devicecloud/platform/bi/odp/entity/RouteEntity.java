/*
 * 文 件 名:  RouteEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  表路由实体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

/**
 * 表路由实体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class RouteEntity
{
    //数据库Id
    private Integer dbServerId;
    
    //表名
    private String tableName;
    
    //hash值
    private Long hashValue;
    
    public Integer getDbServerId()
    {
        return dbServerId;
    }
    
    public void setDbServerId(Integer dbServerId)
    {
        this.dbServerId = dbServerId;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName == null ? null : tableName.trim();
    }
    
    public Long getHashValue()
    {
        return hashValue;
    }
    
    public void setHashValue(Long hashValue)
    {
        this.hashValue = hashValue;
    }
}