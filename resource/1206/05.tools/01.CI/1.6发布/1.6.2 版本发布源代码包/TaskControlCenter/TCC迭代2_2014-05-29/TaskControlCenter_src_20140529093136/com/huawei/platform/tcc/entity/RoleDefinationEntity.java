/* 文 件 名:  RoleDefinationEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 角色定义
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-06-18]
 */
public class RoleDefinationEntity
{
    private Integer roleId;
    
    private String roleName;
    
    private String osUsers;
    
    private String otherGroups;
    
    private Date createTime;
    
    private String desc;
    
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
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("RoleDefinationEntity [roleId=");
        builder.append(roleId);
        builder.append(", roleName=");
        builder.append(roleName);
        builder.append(", osUsers=");
        builder.append(osUsers);
        builder.append(", otherGroups=");
        builder.append(otherGroups);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", desc=");
        builder.append(desc);
        builder.append("]");
        return builder.toString();
    }
}