/*
 * 文 件 名:  Role.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.um.privilegeControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-6-21]
 */
public class Role
{
    /**
     *  系统管理员
     */
    public static final String SYS_ADMIN = "系统管理员";
    
    /**
     * 角色Id
     */
    private Integer roleId;
    
    /**
     * 角色名
     */
    private String roleName;
    
    /**
     * 可见的用户组集合
     */
    private List<String> visualGroups;
    
    private Map<String, String> userGroupMap;
    
    /**
     * 归属的用户集合
     */
    private List<String> osUsers;
    
    /**
     * 默认构造函数
     * @param roleId 角色Id
     * @param roleName 角色名
     * @param osUsers 绑定的OS用户集合
     * @param visualGroups 可见的用户组集合
     * @param userGroupMap 用户与用户组的映射
     */
    public Role(Integer roleId, String roleName, List<String> osUsers, List<String> visualGroups,
        Map<String, String> userGroupMap)
    {
        this.roleId = roleId;
        this.roleName = roleName;
        this.osUsers = osUsers;
        this.visualGroups = new ArrayList<String>();
        if (null != visualGroups)
        {
            for (String group : visualGroups)
            {
                if (!this.visualGroups.contains(group))
                {
                    this.visualGroups.add(group);
                }
            }
        }
        this.userGroupMap = userGroupMap;
    }
    
    /**
     * 获取os用户关联的用户组
     * @param osUser os用户
     * @return  关联的用户组
     */
    public String getGroup(String osUser)
    {
        if (null != osUser)
        {
            return this.userGroupMap.get(osUser);
        }
        else
        {
            return null;
        }
    }
    
    public String getRoleName()
    {
        return roleName;
    }
    
    public Integer getRoleId()
    {
        return roleId;
    }
    
    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }
    
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    
    public List<String> getVisualGroups()
    {
        return visualGroups;
    }
    
    public void setVisualGroups(List<String> visualGroups)
    {
        this.visualGroups = visualGroups;
    }
    
    public List<String> getOsUsers()
    {
        return osUsers;
    }
    
    public void setOsUsers(List<String> osUsers)
    {
        this.osUsers = osUsers;
    }
    
    /**
     * 获取操作员可见的OS用户组
     * null表示全部可见，empty表示没有
     * 
     * @return 操作员可见的OS用户组
     */
    public List<String> getVisibleGroups()
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return null;
        }
        
        return visualGroups;
    }
    
    /**
     * 获取操作员可操作的OS用户列表
     * null表示全部，empty表示没有
     * 
     * @return 操作员可操作的OS用户列表
     */
    public List<String> getOptOsUsers()
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return null;
        }
        
        return osUsers;
    }
    
    /**
     * 是否具有查询权限
     * @param userGroup os用户组
     * @return 是否具有查询权限
     */
    public boolean canQuery(String userGroup)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != visualGroups && visualGroups.contains(userGroup))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有新增权限
     * @param osUser os用户
     * @return 是否具有新增权限
     */
    public boolean canAdd(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有重做权限
     * * @param osUser os用户
     * @return 是否具有重做权限
     */
    public boolean canRedo(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有批量重做权限
     * @param osUser os用户
     * @return 是否具有重做权限
     */
    public boolean canBatchRedo(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有集成重做权限
     * @param osUser os用户
     * @return 是否具有集成重做权限
     */
    public boolean canIntegratedRedo(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有删除权限
     * @param osUser os用户
     * @return 是否具有删除权限
     */
    public boolean canDelete(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
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
        
        return false;
    }
    
    /**
     * 是否具有修改权限
     * @param osUser os用户
     * @return 是否具有修改权限
     */
    public boolean canModify(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有启动权限
     * @param osUser os用户
     * @return 是否具有启动权限
     */
    public boolean canStart(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否具有停止权限
     * @param osUser os用户
     * @return 是否具有停止权限
     */
    public boolean canStop(String osUser)
    {
        //系统管理具有所有权限
        if (isSystemAdmin())
        {
            return true;
        }
        
        if (null != osUsers && osUsers.contains(osUser))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 是否是系统管理员
     * @return 是否是系统管理员
     */
    public boolean isSystemAdmin()
    {
        if (SYS_ADMIN.equals(roleName))
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
     * @param roleName 角色名
     * @return 是否是系统管理员
     */
    public static boolean isSystemAdmin(String roleName)
    {
        if (SYS_ADMIN.equals(roleName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
