/*
 * 文 件 名:  PrivilegeNotEnoughParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.um.exception;

import com.huawei.platform.um.constants.type.PrivilegeType;

/**
 * 权限不足异常
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-6-21]
 */
public class PrivilegeNotEnoughParam
{
    /**
     * OS用户类型
     */
    public static final String OS_USER_TYPE = "OS用户";
    
    /**
     * OS组类型
     */
    public static final String OS_GROUP_TYPE = "OS组";
    
    /**
     * 主权限类型
     */
    private int privilegeType;
    
    private Integer subPrivilegeType;
    
    private String name;
    
    /**
     * 构造函数
     * @param name 名字
     * @param subPrivilegeType 权限类型
     */
    public PrivilegeNotEnoughParam(String name, Integer subPrivilegeType)
    {
        this.name = name;
        this.subPrivilegeType = subPrivilegeType;
        this.privilegeType = PrivilegeType.getPrivilegeType(subPrivilegeType);
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + privilegeType;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        PrivilegeNotEnoughParam other = (PrivilegeNotEnoughParam)obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (privilegeType != other.privilegeType)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return String.format("请申请[%s:%s]的[%s]权限",
            getOsType(),
            this.name,
            PrivilegeType.toSubPriString(subPrivilegeType));
    }
    
    private String getOsType()
    {
        if (privilegeType == PrivilegeType.QUERY)
        {
            return OS_GROUP_TYPE;
        }
        else
        {
            return OS_USER_TYPE;
        }
    }
}
