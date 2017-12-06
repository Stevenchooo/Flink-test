/*
 * 文 件 名:  UserEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  用户实体类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

/**
 * 用户实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class UserEntity
{
    //应用标识
    private String appId;
    
    //访问授权码
    private String accessCode;
    
    //角色Id
    private Integer roleId;
    
    public String getAppId()
    {
        return appId;
    }
    
    public void setAppId(String appId)
    {
        this.appId = appId == null ? null : appId.trim();
    }
    
    public String getAccessCode()
    {
        return accessCode;
    }
    
    public void setAccessCode(String accessCode)
    {
        this.accessCode = accessCode == null ? null : accessCode.trim();
    }
    
    public Integer getRoleId()
    {
        return roleId;
    }
    
    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }
}