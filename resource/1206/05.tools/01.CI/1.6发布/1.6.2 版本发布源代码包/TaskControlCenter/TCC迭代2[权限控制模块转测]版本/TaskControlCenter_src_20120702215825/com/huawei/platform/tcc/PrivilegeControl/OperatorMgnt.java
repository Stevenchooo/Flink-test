/*
 * 文 件 名:  Operator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.PrivilegeControl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.RolePrivilegeInfo;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.OSUserInfoEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.RoleDefinationEntity;
import com.huawei.platform.tcc.entity.RolePrivilegeInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.ServiceTaskGroupInfoEntity;

/**
 * 操作员管理
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 */
public class OperatorMgnt
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorMgnt.class);
    
    private TccDao tccDao;
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 获取已经登陆的操作员
     * @param session 存储操作员的会话
     * @return 操作员
     */
    public static String getOperatorName(HttpSession session)
    {
        if (null == session)
        {
            return null;
        }
        
        Object operatorName = session.getAttribute(NameStoredInSession.USER_NAME);
        if (null != operatorName)
        {
            return (String)operatorName;
        }
        
        return null;
    }
    
    /**
     * 是否是系统管理员
     * @param session 存储操作员的会话
     * @return 操作员
     */
    public static boolean isSysAdmin(HttpSession session)
    {
        if (null == session)
        {
            return false;
        }
        
        Object sysAdmin = session.getAttribute(NameStoredInSession.IS_SYSADMIN);
        if (null != sysAdmin)
        {
            return (Boolean)sysAdmin;
        }
        
        return false;
    }
    
    /**
     * 获取用户可见的业务任务组
     * @param operatorName 用户名
     * @param serviceId 业务Id
     * @return 用户可见的业务任务组
     * @throws Exception 数据库异常
     */
    public List<String> getVisibleTaskGroupNames(String operatorName, Integer serviceId)
        throws Exception
    {
        
        //获取角色Id
        OperatorInfoEntity operatorInfo = this.getOperatorInfo(operatorName);
        
        if (null != operatorInfo && null != operatorInfo.getRoleId())
        {
            //系统管理员显示所有的业务集合
            if (Role.isSystemAdmin(operatorInfo.getRoleId()))
            {
                List<ServiceTaskGroupInfoEntity> taskGroups = tccDao.getTaskGroups(serviceId);
                List<String> names = new ArrayList<String>();
                for (ServiceTaskGroupInfoEntity taskGroup : taskGroups)
                {
                    //不允许重复
                    if (!names.contains(taskGroup.getTaskGroup()))
                    {
                        names.add(taskGroup.getTaskGroup());
                    }
                }
                return names;
            }
            else
            {
                return tccDao.getVisualTaskGroupNames(operatorInfo.getRoleId(), serviceId);
            }
        }
        else
        {
            return new ArrayList<String>(0);
        }
    }
    
    /**
     * 获取用户可见的业务Id、名字集合
     * @param operatorName 用户名
     * @return 用户可见的业务
     * @throws Exception 异常
     */
    public List<ServiceDefinationEntity> getVisibleServices(String operatorName)
        throws Exception
    {
        //获取角色Id
        OperatorInfoEntity operatorInfo = this.getOperatorInfo(operatorName);
        
        if (null != operatorInfo && null != operatorInfo.getRoleId())
        {
            //系统管理员显示所有的业务集合
            if (Role.isSystemAdmin(operatorInfo.getRoleId()))
            {
                return tccDao.getAllServiceIdNameList();
            }
            else
            {
                return tccDao.getVisualServiceIdNames(operatorInfo.getRoleId());
            }
        }
        else
        {
            return new ArrayList<ServiceDefinationEntity>(0);
        }
    }
    
    /**
     * 由用户名获得权限信息，保存到一个operator对象
     * @param operatorName 用户名
     * @return operator对象
     * @throws CException 统一封装的异常
     */
    public Operator createOperator(String operatorName)
        throws CException
    {
        Operator operator = new Operator(operatorName, null);
        OperatorInfoEntity operatorInfo = getOperatorInfo(operatorName);
        if (null == operatorInfo)
        {
            return null;
        }
        
        operator.setOsUserName(operatorInfo.getOsUserName());
        String roleId = operatorInfo.getRoleId();
        Role role = new Role();
        if (Role.isSystemAdmin(roleId))
        {
            role.setRoleId(roleId);
            operator.setRole(role);
            LOGGER.debug("createOperatorByName [operator={}]", operator);
        }
        else
        {
            if (!StringUtils.isBlank(roleId))
            {
                List<RolePrivilegeInfoEntity> rolePrivilegeInfoList = getRolePrivilegeInfo(roleId);
                List<PrivilegeItem> privilegeItemList = new ArrayList<PrivilegeItem>();
                for (int i = 0, n = rolePrivilegeInfoList.size(); i < n; i++)
                {
                    RolePrivilegeInfoEntity rolePrivilegeInfo = rolePrivilegeInfoList.get(i);
                    if (null != rolePrivilegeInfo.getPrivilegeType())
                    {
                        PrivilegeItem privilegeItem = new PrivilegeItem(rolePrivilegeInfo.getServiceId(), null, // 业务名
                            rolePrivilegeInfo.getTaskGroup(), rolePrivilegeInfo.getPrivilegeType());
                        privilegeItemList.add(privilegeItem);
                    }
                }
                if (privilegeItemList.size() > 0)
                {
                    role.setPrivilegeItems(privilegeItemList);
                    operator.setRole(role);
                    LOGGER.debug("createOperatorByName [operator={}]", operator);
                }
            }
        }
        return operator;
    }
    
    /**
     * 通过账号密码查询用户
     * @param usernameAndPasswordParam 账号密码
     * @return 操作员信息
     * @throws CException 统一封装的异常
     */
    public OperatorInfoEntity getOperatorInfo(UsernameAndPasswordParam usernameAndPasswordParam)
        throws CException
    {
        LOGGER.debug("Enter getOperatorInfo. usernameAndPasswordParam is {}", usernameAndPasswordParam);
        try
        {
            return tccDao.getOperatorInfo(usernameAndPasswordParam);
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfo failed! usernameAndPasswordParam is {}", usernameAndPasswordParam, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过用户名查询用户
     * @param operatorName 用户名
     * @return 操作员信息
     * @throws CException 统一封装的异常
     */
    public OperatorInfoEntity getOperatorInfo(String operatorName)
        throws CException
    {
        LOGGER.debug("Enter getOperatorInfoByName. operatorName is {}", operatorName);
        try
        {
            if (StringUtils.isBlank(operatorName))
            {
                return null;
            }
            
            return tccDao.getOperatorInfoByName(operatorName);
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfoByName failed! operatorName is {}", operatorName, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过角色id查询权限
     * @param roleId 角色id
     * @return 权限信息集合
     * @throws CException 统一封装的异常
     */
    public List<RolePrivilegeInfoEntity> getRolePrivilegeInfo(String roleId)
        throws CException
    {
        LOGGER.debug("Enter getRolePrivilegeInfo. roleId is {}", roleId);
        try
        {
            return tccDao.getRolePrivilegeInfo(roleId);
        }
        catch (Exception e)
        {
            LOGGER.error("getRolePrivilegeInfo failed! roleId is {}", roleId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过用户名集合查询指定用户
     * @param userSearch 用户查询
     * @return 用户信息集合
     * @throws CException 统一封装的异常
     */
    public List<OperatorInfoEntity> getUsersByName(Search userSearch)
        throws CException
    {
        LOGGER.debug("Enter getUsersByName. userSearch is {}", userSearch);
        try
        {
            return tccDao.getUsersByName(userSearch);
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersByName failed! userSearch is {}", userSearch, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过用户名集合查询指定用户总数
     * @param search 用户查询
     * @return 用户总数
     * @throws Exception 统一封装的异常
     */
    public Integer getUsersCountByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCountByName. search is {}", search);
        try
        {
            if (null != search)
            {
                return tccDao.getUsersCountByName(search);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCountByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过角色id查询指定用户
     * @param roleId 角色id
     * @return 用户信息集合
     * @throws CException 统一封装的异常
     */
    public List<OperatorInfoEntity> getUsersByRoleId(String roleId)
        throws CException
    {
        LOGGER.debug("Enter getUsersByRoleId. roleId is {}", roleId);
        try
        {
            return tccDao.getUsersByRoleId(roleId);
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersByRoleId failed! roleId is {}", roleId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过角色Id查询指定用户总数
     * @param roleId 角色Id
     * @return 用户总数
     * @throws Exception 统一封装的异常
     */
    public Integer getUsersCountByRoleId(String roleId)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCountByRoleId. roleId is {}", roleId);
        try
        {
            if (null != roleId)
            {
                return tccDao.getUsersCountByRoleId(roleId);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCountByRoleId failed! roleId is {}", roleId, e);
            throw e;
        }
    }
    
    /**
     * 通过查询条件查询指定用户总数
     * @param operator 用户查询条件
     * @return 用户总数
     * @throws Exception 异常
     */
    public Integer getUsersCount(OperatorInfoEntity operator)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCount. operator is {}", operator);
        try
        {
            if (null != operator)
            {
                return tccDao.getUsersCount(operator);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCount failed! operator is {}", operator, e);
            throw e;
        }
    }
    
    /**
     * 新增用户
     * @param operatorInfo 用户
     * @throws CException 统一封装的异常
     */
    public void addUser(OperatorInfoEntity operatorInfo)
        throws CException
    {
        LOGGER.debug("Enter addUser. operatorInfo is {}", operatorInfo);
        try
        {
            tccDao.addUser(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addUser failed! operatorInfo is {}", operatorInfo, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 修改用户
     * @param operatorInfo 用户
     * @throws CException 统一封装的异常
     */
    public void updateUser(OperatorInfoEntity operatorInfo)
        throws CException
    {
        LOGGER.debug("Enter updateUser. operatorInfo is {}", operatorInfo);
        try
        {
            tccDao.updateUser(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateUser failed! operatorInfo is {}", operatorInfo, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除用户信息
     * @param operatorInfo 用户
     * @throws Exception 异常
     */
    public void deleteUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteUser. operatorInfo is {}", operatorInfo);
        try
        {
            tccDao.deleteUser(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 根据条件删除用户某些信息
     * @param operatorInfo 用户
     * @throws Exception 异常
     */
    public void deleteInOperatorInfo(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteInOperatorInfo. operatorInfo is {}", operatorInfo);
        try
        {
            tccDao.deleteInOperatorInfo(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteInOperatorInfo failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取未分配角色的用户集合
     * @return 未分配角色的用户集合
     * @throws Exception 异常
     */
    public List<OperatorInfoEntity> getUsersNoRoleId()
        throws Exception
    {
        LOGGER.debug("Enter getUsersNoRoleId.");
        try
        {
            return tccDao.getUsersNoRoleId();
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersNoRoleId failed!", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过角色ID集合查询指定角色
     * @param roleSearch 用户查询
     * @return 角色信息集合
     * @throws Exception 异常
     */
    public List<RoleDefinationEntity> getRolesByName(Search roleSearch)
        throws Exception
    {
        LOGGER.debug("Enter getRolesByName. roleSearch is {}", roleSearch);
        try
        {
            return tccDao.getRolesByName(roleSearch);
        }
        catch (Exception e)
        {
            LOGGER.error("getRolesByName failed! roleSearch is {}", roleSearch, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过角色Id集合查询指定角色总数
     * @param search 角色查询
     * @return 用户总数
     * @throws Exception 统一封装的异常
     */
    public Integer getRolesCountByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getRolesCountByName. search is {}", search);
        try
        {
            if (null != search)
            {
                return tccDao.getRolesCountByName(search);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRolesCountByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过角色id集合查询角色
     * @param roleId 角色id
     * @return 角色信息集合
     * @throws CException 统一封装的异常
     */
    public RoleDefinationEntity getRole(String roleId)
        throws CException
    {
        LOGGER.debug("Enter getRole. roleId is {}", roleId);
        try
        {
            return tccDao.getRole(roleId);
        }
        catch (Exception e)
        {
            LOGGER.error("getRole failed! roleId is {}", roleId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取角色Id集合
     * @return 角色Id集合
     * @throws CException 统一封装的异常
     */
    public List<String> getAllRoleIdList()
        throws CException
    {
        LOGGER.debug("Enter getAllRoleIdList.");
        try
        {
            return tccDao.getAllRoleIdList();
        }
        catch (Exception e)
        {
            LOGGER.error("getAllRoleIdList failed!", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 新增角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    public void addRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter addRole. roleDef is {}", roleDef);
        try
        {
            tccDao.addRole(roleDef);
        }
        catch (Exception e)
        {
            LOGGER.error("addRole failed! roleDef is {}", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 修改角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    public void updateRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter updateRole. roleDef is {}", roleDef);
        try
        {
            tccDao.updateRole(roleDef);
        }
        catch (Exception e)
        {
            LOGGER.error("updateRole failed! roleDef is {}", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 删除角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    public void deleteRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter deleteRole. roleDef is {}", roleDef);
        try
        {
            tccDao.deleteRole(roleDef);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteRole failed! roleDef is {}", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 新增角色权限
     * @param rolePrivilegeInfo 角色权限
     * @throws Exception 异常
     */
    public void addRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter addRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            tccDao.addRolePrivilege(rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 修改角色权限
     * @param rolePrivilegeInfo 角色权限
     * @throws Exception 异常
     */
    public void updateRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            tccDao.updateRolePrivilege(rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除角色权限
     * @param rolePrivilegeInfo 角色权限
     * @throws Exception 异常
     */
    public void deleteRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            tccDao.deleteRolePrivilege(rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 通过角色查询指定角色总数
     * @param rolePrivilegeInfo 角色查询
     * @return 角色总数
     * @throws Exception 统一封装的异常
     */
    public Integer getRolePrivilegeCount(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter getRolePrivilegeCount. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            if (null != rolePrivilegeInfo)
            {
                return tccDao.getRolePrivilegeCount(rolePrivilegeInfo);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRolePrivilegeCount failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取所有任务组和对应的权限类型
     * @param search 角色id
     * @return 角色权限信息集合
     * @throws Exception 异常
     */
    public List<RolePrivilegeInfo> getPrivileges(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getPrivileges. search is {}", search);
        try
        {
            return tccDao.getPrivileges(search);
        }
        catch (Exception e)
        {
            LOGGER.error("getPrivileges failed! search is {}", search, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过OS用户名集合查询指定OS用户
     * @param search OS用户查询
     * @return 用户信息集合
     * @throws Exception 异常
     */
    public List<OSUserInfoEntity> getOSUsersByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersByName. search is {}", search);
        try
        {
            return tccDao.getOSUsersByName(search);
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsersByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名集合查询指定OS用户总数
     * @param search OS用户查询
     * @return OS用户总数
     * @throws Exception 异常
     */
    public Integer getOSUsersCountByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersCountByName. search is {}", search);
        try
        {
            if (null != search)
            {
                return tccDao.getOSUsersCountByName(search);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsersCountByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 新增OS用户
     * @param mOSUserInfo OS用户
     * @throws Exception 异常
     */
    public void addOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter addOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            tccDao.addOSUser(mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改OS用户
     * @param mOSUserInfo OS用户
     * @throws Exception 异常
     */
    public void updateOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            tccDao.updateOSUser(mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除OS用户信息
     * @param mOSUserInfo OS用户
     * @throws Exception 异常
     */
    public void deleteOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            tccDao.deleteOSUser(mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
}
