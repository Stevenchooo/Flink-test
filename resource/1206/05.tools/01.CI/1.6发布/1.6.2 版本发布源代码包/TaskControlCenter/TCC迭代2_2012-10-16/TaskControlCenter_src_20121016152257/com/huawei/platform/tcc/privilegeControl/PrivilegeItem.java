/*
 * 文 件 名:  PrivilegeItem.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.privilegeControl;

import com.huawei.platform.tcc.constants.type.PrivilegeType;

/**
 * 权限项
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 * @see  [相关类/方法]
 */
public class PrivilegeItem
{
    private Integer serviceId;
    
    private String serviceName;
    
    private String taskGroup;
    
    private int privilegeType;
    
    /**
     * 构造函数
     * @param serviceId 业务ID
     * @param serviceName 业务名
     * @param taskGroup 任务组
     * @param privilegeType 权限类型
     */
    public PrivilegeItem(Integer serviceId, String serviceName, String taskGroup, int privilegeType)
    {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.taskGroup = taskGroup;
        this.privilegeType = privilegeType;
    }
    
    /**
     * 是否具有查询权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有查询权限
     */
    public boolean canQuery(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有新增权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有新增权限
     */
    public boolean canAdd(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有重做权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有重做权限
     */
    public boolean canRedo(Integer lServiceId, String lTaskGroup)
    {
      //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.EXECUTE || privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有批量重做权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有重做权限
     */
    public boolean canBatchRedo(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.EXECUTE || privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有集成重做权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有集成重做权限
     */
    public boolean canIntegratedRedo(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.EXECUTE || privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有删除权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有删除权限
     */
    public boolean canDelete(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有修改权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有修改权限
     */
    public boolean canModify(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.EXECUTE || privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有启动权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有启动权限
     */
    public boolean canStart(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.EXECUTE || privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有停止权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有停止权限
     */
    public boolean canStop(Integer lServiceId, String lTaskGroup)
    {
        //严格要求参数不为空
        if (null == lServiceId || null == lTaskGroup)
        {
            return false;
        }
        
        if (lServiceId.equals(serviceId) && lTaskGroup.equals(taskGroup))
        {
            if (privilegeType == PrivilegeType.EXECUTE || privilegeType == PrivilegeType.ALL)
            {
                return true;
            }
        }
        
        return false;
    }
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public String getServiceName()
    {
        return serviceName;
    }
    
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
    
    public String getTaskGroup()
    {
        return taskGroup;
    }
    
    public void setTaskGroup(String taskGroup)
    {
        this.taskGroup = taskGroup;
    }
    
    public int getPrivilegeType()
    {
        return privilegeType;
    }
    
    public void setPrivilegeType(int privilegeType)
    {
        this.privilegeType = privilegeType;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("PrivilegeItem [serviceId=");
        builder.append(serviceId);
        builder.append(", serviceName=");
        builder.append(serviceName);
        builder.append(", taskGroup=");
        builder.append(taskGroup);
        builder.append(", privilegeType=");
        builder.append(privilegeType);
        builder.append("]");
        return builder.toString();
    }
}
