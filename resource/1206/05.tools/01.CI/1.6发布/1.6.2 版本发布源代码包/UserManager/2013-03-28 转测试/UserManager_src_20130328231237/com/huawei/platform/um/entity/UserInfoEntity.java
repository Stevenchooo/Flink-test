/*
 * 文 件 名:  UserInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  用户信息实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-27
 */
package com.huawei.platform.um.entity;

/**
 * 用户信息实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-27]
 */
public class UserInfoEntity
{
    private Integer nodeOrClusterId;
    
    private Integer appType;
    
    private String user;
    
    private Integer uid;
    
    private String password;
    
    private String ownerGroupName;
    
    private String homeDir;
    
    private String groupList;
    
    private Integer dbId;
    
    private String desc;

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

    public Integer getUid()
    {
        return uid;
    }

    public void setUid(Integer uid)
    {
        this.uid = uid;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getOwnerGroupName()
    {
        return ownerGroupName;
    }

    public void setOwnerGroupName(String ownerGroupName)
    {
        this.ownerGroupName = ownerGroupName;
    }

    public String getHomeDir()
    {
        return homeDir;
    }

    public void setHomeDir(String homeDir)
    {
        this.homeDir = homeDir;
    }

    public String getGroupList()
    {
        return groupList;
    }

    public void setGroupList(String groupList)
    {
        this.groupList = groupList;
    }

    public Integer getDbId()
    {
        return dbId;
    }

    public void setDbId(Integer dbId)
    {
        this.dbId = dbId;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
