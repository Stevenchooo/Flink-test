package com.huawei.ide.beans.res.user;

/**
 * 
 * 用户
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月14日]
 * @see  [相关类/方法]
 */
public class User
{
    /**
     * id
     */
    private int id;
    
    /**
     * 用户名
     */
    private String name;
    
    /**
     * 角色id
     */
    private int roleId;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getRoleId()
    {
        return roleId;
    }
    
    public void setRoleId(int roleId)
    {
        this.roleId = roleId;
    }
    
}
