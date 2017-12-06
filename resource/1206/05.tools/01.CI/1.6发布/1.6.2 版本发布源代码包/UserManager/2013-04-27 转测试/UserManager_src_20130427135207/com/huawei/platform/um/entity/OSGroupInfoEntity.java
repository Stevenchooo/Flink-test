/* 文 件 名:  OSUserInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  OS用户组
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-13
 */
package com.huawei.platform.um.entity;

/**
 * OS用户组
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-12-13]
 */
public class OSGroupInfoEntity
{
    private String userGroup;
    
    private String desc;
    
    public String getUserGroup()
    {
        return userGroup;
    }

    public void setUserGroup(String userGroup)
    {
        this.userGroup = userGroup;
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