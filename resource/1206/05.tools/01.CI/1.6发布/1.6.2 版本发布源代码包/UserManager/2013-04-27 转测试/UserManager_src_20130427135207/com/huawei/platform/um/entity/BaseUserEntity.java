/*
 * 文 件 名:  BaseUserEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-4-23
 */
package com.huawei.platform.um.entity;

/**
 * 用户信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-4-23]
 */
public class BaseUserEntity
{
    private Integer nodeOrClusterId;
    
    private Integer appType;
    
    private String user;

    public Integer getNodeOrClusterId()
    {
        return nodeOrClusterId;
    }

    public void setNodeOrClusterId(Integer nodeOrClusterId)
    {
        this.nodeOrClusterId = nodeOrClusterId;
    }

    public Integer getAppType()
    {
        return appType;
    }

    public void setAppType(Integer appType)
    {
        this.appType = appType;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
}
