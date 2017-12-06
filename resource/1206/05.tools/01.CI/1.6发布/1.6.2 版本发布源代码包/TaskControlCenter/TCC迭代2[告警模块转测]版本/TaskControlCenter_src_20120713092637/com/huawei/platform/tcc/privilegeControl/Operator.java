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
import com.huawei.platform.tcc.domain.ServiceTaskGroup;

/**
 * 操作员
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 */
public class Operator
{
    private String operatorName;
    
    /**
     * 管理的BI系统用户名
     */
    private String osUserName;
    
    private Role role;
    
    /**
     * 构造函数
     * @param operatorName 操作员
     * @param role 角色
     */
    public Operator(String operatorName, Role role)
    {
        this.operatorName = operatorName;
        this.role = role;
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
     * 获取操作员可见的业务和任务组集合
     * null表示全部业务和任务组可见，empty表示没有可见的业务和任务组
     * 
     * @return 获取操作员可见的业务和任务组集合
     */
    public List<ServiceTaskGroup> getVisibleServiceTaskGroup()
    {
        if (null != role)
        {
            return role.getVisibleServiceTaskGroup();
        }
        else
        {
            return new ArrayList<ServiceTaskGroup>(0);
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
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有查询权限
     */
    public boolean canQuery(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canQuery(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有新增权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有新增权限
     */
    public boolean canAdd(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canAdd(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有重做权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有重做权限
     */
    public boolean canRedo(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canRedo(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有批量重做权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有重做权限
     */
    public boolean canBatchRedo(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canBatchRedo(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有集成重做权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有集成重做权限
     */
    public boolean canIntegratedRedo(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canIntegratedRedo(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有删除权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有删除权限
     */
    public boolean canDelete(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canDelete(lServiceId, lTaskGroup);
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
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有修改权限
     */
    public boolean canModify(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canModify(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有启动权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有启动权限
     */
    public boolean canStart(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canStart(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 是否具有停止权限
     * @param lServiceId 业务ID
     * @param lTaskGroup 任务组
     * @return 是否具有停止权限
     */
    public boolean canStop(Integer lServiceId, String lTaskGroup)
    {
        if (null != role)
        {
            return role.canStop(lServiceId, lTaskGroup);
        }
        else
        {
            return false;
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

    public String getOsUserName()
    {
        return osUserName;
    }

    public void setOsUserName(String osUserName)
    {
        this.osUserName = osUserName;
    }
}
