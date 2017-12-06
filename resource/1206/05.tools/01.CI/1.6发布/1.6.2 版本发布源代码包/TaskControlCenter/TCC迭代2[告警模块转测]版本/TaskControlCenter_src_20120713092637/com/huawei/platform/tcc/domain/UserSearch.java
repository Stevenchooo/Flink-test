/*
 * 文 件 名:  UserSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;


/**
 * 用户查询
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100,, 2012-06-19]
 */
public class UserSearch extends Search
{
    /**
     * 角色id
     */
    private String roleId;
    
    /**
     * os用户
     */
    private String osUser;

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getOsUser()
    {
        return osUser;
    }

    public void setOsUser(String osUser)
    {
        this.osUser = osUser;
    }
}
