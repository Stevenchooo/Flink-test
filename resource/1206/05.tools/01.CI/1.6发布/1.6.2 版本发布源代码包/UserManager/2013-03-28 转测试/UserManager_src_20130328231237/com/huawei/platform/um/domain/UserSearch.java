/*
 * 文 件 名:  UserSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.um.domain;


/**
 * 用户查询
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100,, 2012-06-19]
 */
public class UserSearch extends Search
{
    /**
     * 角色id
     */
    private Integer roleId;
    
    private String roleName;
    
    
    
    public Integer getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
}
