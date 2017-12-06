/*
 * 文 件 名:  Role.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.PrivilegeControl;

import java.util.ArrayList;
import java.util.List;

import com.huawei.platform.tcc.domain.ServiceTaskGroup;

/**
 * 角色
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 * @see  [相关类/方法]
 */
public class Role
{
    private String roleId;
    
    private List<PrivilegeItem> privilegeItems;
    
    /**
     * 获取操作员可见的业务和任务组集合
     * null表示全部业务和任务组可见，empty表示没有可见的业务和任务组
     * 
     * @return 获取操作员可见的业务和任务组集合
     */
    public List<ServiceTaskGroup> getVisibleServiceTaskGroup()
    {
        List<ServiceTaskGroup> vSTg = new ArrayList<ServiceTaskGroup>();
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return null;
        }
        
        if (null != privilegeItems)
        {
            ServiceTaskGroup serviceTaskGroup;
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                serviceTaskGroup = new ServiceTaskGroup();
                serviceTaskGroup.setServiceId(privilegeItem.getServiceId());
                serviceTaskGroup.setTaskGroup(privilegeItem.getTaskGroup());
                vSTg.add(serviceTaskGroup);
            }
        }
        return vSTg;
    }
    
    /**
     * 是否具有查询权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有查询权限
     */
    public boolean canQuery(Integer lServiceId, String lTaskGroup)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canQuery(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
            }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canAdd(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canRedo(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canBatchRedo(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
      //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canIntegratedRedo(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canDelete(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有新增权限(有任何一个任务组满足即可)
     * @param lServiceId 业务ID
     * @return 是否具有新增权限
     */
    public boolean canAdd(Integer lServiceId)
    {
      //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (lServiceId.equals(privilegeItem.getServiceId())
                    && privilegeItem.canAdd(lServiceId, privilegeItem.getTaskGroup()))
                {
                    //所有权限具有并的关系
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 是否具有删除权限(有任何一个任务组满足即可)
     * @param lServiceId 业务ID
     * @return 是否具有删除权限
     */
    public boolean canDelete(Integer lServiceId)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (lServiceId.equals(privilegeItem.getServiceId())
                    && privilegeItem.canDelete(lServiceId, privilegeItem.getTaskGroup()))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canModify(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canStart(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
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
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != privilegeItems)
        {
            for (PrivilegeItem privilegeItem : privilegeItems)
            {
                if (privilegeItem.canStop(lServiceId, lTaskGroup))
                {
                    //所有权限具有并的关系
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public String getRoleId()
    {
        return roleId;
    }
    
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }
    
    public List<PrivilegeItem> getPrivilegeItems()
    {
        return privilegeItems;
    }
    
    public void setPrivilegeItems(List<PrivilegeItem> privilegeItems)
    {
        this.privilegeItems = privilegeItems;
    }
    
    /**
     * 是否是系统管理员
     * @return 是否是系统管理员
     */
    public boolean isSystemAdmin()
    {
        if ("系统管理员".equals(roleId))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否是系统管理员
     * @param roleId 角色Id
     * @return 是否是系统管理员
     */
    public static boolean isSystemAdmin(String roleId)
    {
        if ("系统管理员".equals(roleId))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Role [roleId=");
        builder.append(roleId);
        builder.append(", privilegeItems=");
        builder.append(privilegeItems);
        builder.append("]");
        return builder.toString();
    }
}
