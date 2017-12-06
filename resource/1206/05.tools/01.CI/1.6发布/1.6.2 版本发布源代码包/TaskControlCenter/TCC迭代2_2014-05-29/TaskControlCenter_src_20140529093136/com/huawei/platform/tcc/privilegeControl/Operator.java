/*
 * 文 件 名:  Operator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.privilegeControl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.huawei.platform.tcc.constants.type.NameStoredInSession;

/**
 * 操作员
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-6-21]
 */
public class Operator
{
    private String operatorName;
    
    private Role role;
    
    private boolean isValid;
    
    /**
     * 构造函数
     * @param operatorName 操作员
     * @param role 角色
     */
    public Operator(String operatorName, Role role)
    {
        this.operatorName = operatorName;
        this.role = role;
        this.isValid = true;
    }
    
    public boolean isValid()
    {
        return isValid;
    }

    /**
     * 失效
     */
    public void invalidate()
    {
        this.isValid = false;
    }
    
    /**
     * 是否是系统管理员
     * @return 是否是系统管理员
     */
    public boolean isSystemAdmin()
    {
        if (null == role)
        {
            return false;
        }
        else
        {
            return role.isSystemAdmin();
        }
    }
    
    /**
     * 获取操作员可见的OS用户组
     * null表示可见，empty表示没有
     * 
     * @return 操作员可见的OS用户组,返回数据不会为null
     */
    public List<String> getVisibleGroups()
    {
        if (null != role)
        {
            return role.getVisibleGroups();
        }
        else
        {
            return new ArrayList<String>(0);
        }
    }
    
    /**
     * 获取操作员可操作的OS用户列表
     * null表示全部，empty表示没有
     * 
     * @return 操作员可操作的OS用户列表
     */
    public List<String> getOptOsUsers()
    {
        if (null != role)
        {
            return role.getOptOsUsers();
        }
        else
        {
            return new ArrayList<String>(0);
        }
    }
    
    /**
     * 获取已经登录过的操作员
     * @param session 存放操作员的会话
     * @return 操作员
     */
    public static Operator getLoggedInOperator(HttpSession session)
    {
        if (null == session)
        {
            return null;
        }
        
        Object operator = session.getAttribute(NameStoredInSession.OPERATOR);
        if (null != operator)
        {
            return (Operator)operator;
        }
        
        return null;
    }
    
    /**
     * 是否具有查询权限
     * @param userGroup os用户组
     * @return 是否具有查询权限
     */
    public boolean canQuery(String userGroup)
    {
        if (null != role)
        {
            return role.canQuery(userGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有新增权限
     * @param osUser os用户
     * @return 是否具有新增权限
     */
    public boolean canAdd(String osUser)
    {
        if (null != role)
        {
            return role.canAdd(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有重做权限
     * * @param osUser os用户
     * @return 是否具有重做权限
     */
    public boolean canRedo(String osUser)
    {
        if (null != role)
        {
            return role.canRedo(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有批量重做权限
     * @param osUser os用户
     * @return 是否具有重做权限
     */
    public boolean canBatchRedo(String osUser)
    {
        if (null != role)
        {
            return role.canBatchRedo(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有集成重做权限
     * @param osUser os用户
     * @return 是否具有集成重做权限
     */
    public boolean canIntegratedRedo(String osUser)
    {
        if (null != role)
        {
            return role.canIntegratedRedo(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有删除权限
     * @param osUser os用户
     * @return 是否具有删除权限
     */
    public boolean canDelete(String osUser)
    {
        if (null != role)
        {
            return role.canDelete(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有新增权限(有任何一个任务组满足即可)
     * @param lServiceId 业务ID
     * @return 是否具有新增权限
     */
    public boolean canAdd(Integer lServiceId)
    {
        if (null != role)
        {
            return role.canAdd(lServiceId);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有删除权限(有任何一个任务组满足即可)
     * @param lServiceId 业务ID
     * @return 是否具有删除权限
     */
    public boolean canDelete(Integer lServiceId)
    {
        if (null != role)
        {
            return role.canDelete(lServiceId);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有修改权限
     * @param osUser os用户
     * @return 是否具有修改权限
     */
    public boolean canModify(String osUser)
    {
        if (null != role)
        {
            return role.canModify(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有启动权限
     * @param osUser os用户
     * @return 是否具有启动权限
     */
    public boolean canStart(String osUser)
    {
        if (null != role)
        {
            return role.canStart(osUser);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有停止权限
     * @param osUser os用户
     * @return 是否具有停止权限
     */
    public boolean canStop(String osUser)
    {
        if (null != role)
        {
            return role.canStop(osUser);
        }
        else
        {
            return false;
        }
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
            return role.getGroup(osUser);
        }
        else
        {
            return null;
        }
    }
    
    public String getOperatorName()
    {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }
    
    public Role getRole()
    {
        return role;
    }
    
    public void setRole(Role role)
    {
        this.role = role;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Operator [operatorName=");
        builder.append(operatorName);
        builder.append(", role=");
        builder.append(role);
        builder.append("]");
        return builder.toString();
    }
}
