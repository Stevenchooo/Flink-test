/* 文 件 名:  RoleDefinationEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

/**
 * 角色权限实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class RolePrivilegeInfoEntity
{
    private String roleId;
    
    private Integer serviceId;
    
    private String taskGroup;
    
    private Integer privilegeType;
    
    public Integer getPrivilegeType()
    {
        return privilegeType;
    }
    
    public void setPrivilegeType(Integer privilegeType)
    {
        this.privilegeType = privilegeType;
    }
    
    public String getRoleId()
    {
        return roleId;
    }
    
    public void setRoleId(String roleId)
    {
        this.roleId = roleId == null ? null : roleId.trim();
    }
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public String getTaskGroup()
    {
        return taskGroup;
    }
    
    public void setTaskGroup(String taskGroup)
    {
        this.taskGroup = taskGroup == null ? null : taskGroup.trim();
    }
}