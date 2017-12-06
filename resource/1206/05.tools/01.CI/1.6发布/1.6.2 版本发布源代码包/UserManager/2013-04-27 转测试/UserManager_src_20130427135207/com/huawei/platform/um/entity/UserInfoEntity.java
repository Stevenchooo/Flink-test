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
    private String userName;
    
    private String password;
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
}
