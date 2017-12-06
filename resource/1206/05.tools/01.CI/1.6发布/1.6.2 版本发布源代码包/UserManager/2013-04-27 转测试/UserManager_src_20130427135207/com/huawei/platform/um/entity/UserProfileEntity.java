/*
 * 文 件 名:  UserProfileEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  用户配置信息
 * 创 建 人:  z00190465
 * 创建时间:  2013-4-23
 */
package com.huawei.platform.um.entity;

/**
 * 用户配置信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-4-23]
 */
public class UserProfileEntity
{
    //用户名
    private String userName;
    
    //报表文件存放目录
    private String cptLocation;
    
    //默认的加载节点Id
    private String cptDefaultLoadNode;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getCptLocation()
    {
        return cptLocation;
    }

    public void setCptLocation(String cptLocation)
    {
        this.cptLocation = cptLocation;
    }

    public String getCptDefaultLoadNode()
    {
        return cptDefaultLoadNode;
    }

    public void setCptDefaultLoadNode(String cptDefaultLoadNode)
    {
        this.cptDefaultLoadNode = cptDefaultLoadNode;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("UserProfileEntity [userName=");
        builder.append(userName);
        builder.append(", cptLocation=");
        builder.append(cptLocation);
        builder.append(", cptDefaultLoadNode=");
        builder.append(cptDefaultLoadNode);
        builder.append("]");
        return builder.toString();
    }
}
