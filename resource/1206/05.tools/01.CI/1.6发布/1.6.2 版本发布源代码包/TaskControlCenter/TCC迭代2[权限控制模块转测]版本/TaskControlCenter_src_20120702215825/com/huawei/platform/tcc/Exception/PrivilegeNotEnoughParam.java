/*
 * 文 件 名:  PrivilegeNotEnoughParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.Exception;

import com.huawei.platform.tcc.constants.type.PrivilegeType;

/**
 * 权限不足异常
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 */
public class PrivilegeNotEnoughParam
{
    private Integer subPrivilegeType;
    
    private String serviceName;
    
    private String taskGroup;
    
    /**
     * 构造函数
     * @param serviceName 业务名
     * @param taskGroup 任务组
     * @param subPrivilegeType 权限类型
     */
    public PrivilegeNotEnoughParam(String serviceName, String taskGroup, Integer subPrivilegeType)
    {
        this.serviceName = serviceName;
        this.taskGroup = taskGroup;
        this.subPrivilegeType = subPrivilegeType;
    }
    
    @Override
    public String toString()
    {
        return String.format("没有[%s_%s]的[%s]权限",
            this.serviceName,
            this.taskGroup,
            PrivilegeType.toSubPriString(subPrivilegeType));
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result + ((subPrivilegeType == null) ? 0 : subPrivilegeType.hashCode());
        result = prime * result + ((taskGroup == null) ? 0 : taskGroup.hashCode());
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
        if (this.toString().equals(other.toString()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
