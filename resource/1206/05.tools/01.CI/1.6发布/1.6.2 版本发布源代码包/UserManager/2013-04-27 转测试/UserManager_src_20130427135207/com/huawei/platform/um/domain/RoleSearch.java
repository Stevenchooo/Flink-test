/*
 * 文 件 名:  ServiceSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.um.domain;


/**
 * 业务查询
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100,, 2012-2-27]
 */
public class RoleSearch extends Search
{
    private String roleName;
    
    private String osUsers;
    
    private String otherGroups;

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getOsUsers()
    {
        return osUsers;
    }

    public void setOsUsers(String osUsers)
    {
        this.osUsers = osUsers;
    }

    public String getOtherGroups()
    {
        return otherGroups;
    }

    public void setOtherGroups(String otherGroups)
    {
        this.otherGroups = otherGroups;
    }
}
