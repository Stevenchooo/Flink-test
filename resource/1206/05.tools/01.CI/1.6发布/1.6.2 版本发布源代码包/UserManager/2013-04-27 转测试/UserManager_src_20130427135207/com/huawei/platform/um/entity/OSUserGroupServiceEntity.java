/* 文 件 名:  OSUserInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  OS用户用户组业务关联信息
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-30
 */
package com.huawei.platform.um.entity;
/**
 * OS用户用户组业务关联信息
 * 已经删除业务信息
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-06-30]
 */
public class OSUserGroupServiceEntity
{
    private String osUser;
    
    private String userGroup;
    
    public String getOsUser()
    {
        return osUser;
    }

    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }

    public String getUserGroup()
    {
        return userGroup;
    }

    public void setUserGroup(String userGroup)
    {
        this.userGroup = userGroup;
    }
}