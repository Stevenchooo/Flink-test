/* 文 件 名:  OSUserInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-30
 */
package com.huawei.platform.um.entity;
/**
 * 操作员信息
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-06-30]
 */
public class OSUserInfoEntity
{
    private String osUser;
    
    private String pemKey;
    
    private String userGroup;
    
    private String desc;
    
    public String getOsUser()
    {
        return osUser;
    }
    
    public void setOsUser(String osUser)
    {
        this.osUser = osUser == null ? null : osUser.trim();
    }

    public String getPemKey()
    {
        return pemKey;
    }

    public void setPemKey(String pemKey)
    {
        this.pemKey = pemKey;
    }

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