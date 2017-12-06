/*
 * 文 件 名:  Operator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.um.privilegeControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.um.constants.type.NameStoredInSession;
import com.huawei.platform.um.dao.UMDao;
import com.huawei.platform.um.domain.RolePrivilegeInfo;
import com.huawei.platform.um.domain.RoleSearch;
import com.huawei.platform.um.domain.Search;
import com.huawei.platform.um.domain.UserSearch;
import com.huawei.platform.um.domain.UsernameAndPasswordParam;
import com.huawei.platform.um.entity.OSUserGroupServiceEntity;
import com.huawei.platform.um.entity.OSUserInfoEntity;
import com.huawei.platform.um.entity.OperatorInfoEntity;
import com.huawei.platform.um.entity.RoleDefinationEntity;
import com.huawei.platform.um.entity.RolePrivilegeInfoEntity;
import com.huawei.platform.um.entity.ServiceDefinationEntity;
import com.huawei.platform.um.event.Event;
import com.huawei.platform.um.event.EventType;
import com.huawei.platform.um.event.Eventor;
import com.huawei.platform.um.listener.Listener;

/**
 * 操作员管理
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-6-21]
 */
public class OperatorMgnt implements Listener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorMgnt.class);
    
    /**
     * 缓存操作员
     */
    private static Map<String, Operator> userOperatorMaps = new HashMap<String, Operator>();
    
    private UMDao umDao;
    
    public UMDao getUmDao()
    {
        return umDao;
    }

    public void setUmDao(UMDao umDao)
    {
        this.umDao = umDao;
    }

    /**
     * 初始化方法
     */
    public void init()
    {
        synchronized (userOperatorMaps)
        {
            userOperatorMaps.clear();
        }
        
        Eventor.register(EventType.UPDATE_OPERATOR, this);
        Eventor.register(EventType.UPDATE_ROLE, this);
        Eventor.register(EventType.UPDATE_OS_USER, this);
        
        Eventor.register(EventType.DELETE_OPERATOR, this);
        Eventor.register(EventType.DELETE_ROLE, this);
        Eventor.register(EventType.DELETE_OS_USER, this);
    }
    
    /**
     * 操作员管理
     * @param opName 操作员
     * @return 获取操作员
     */
    public Operator getOperator(String opName)
    {
        Operator operator = null;
        if (null == opName)
        {
            return operator;
        }
        
        synchronized (userOperatorMaps)
        {
            operator = userOperatorMaps.get(opName);
        }
        
        //不为空则返回
        if (null != operator)
        {
            return operator;
        }
        
        //重新创建操作员信息
        try
        {
            operator = createOperator(opName);
        }
        catch (Exception e)
        {
            LOGGER.error("createOperator failed!", e);
            return operator;
        }
        
        if (null == operator)
        {
            return operator;
        }
        
        synchronized (userOperatorMaps)
        {
            userOperatorMaps.put(opName, operator);
        }
        
        return operator;
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
     * 获取登陆IP
     * @param session 会话
     * @return 登陆IP
     */
    public static String getLoginIp(HttpSession session)
    {
        if (null == session)
        {
            return null;
        }
        
        Object loginIp = session.getAttribute(NameStoredInSession.CLIENT_IP);
        if (null != loginIp)
        {
            return (String)loginIp;
        }
        
        return null;
    }
    
    /**
     * 是否是系统管理员
     * @param userName 用户名
     * @return 操作员
     */
    public static boolean isSysAdmin(String userName)
    {
        if (null == userName)
        {
            return false;
        }
        
        Operator operator = null;
        synchronized (userOperatorMaps)
        {
            operator = userOperatorMaps.get(userName);
        }
        
        return null != operator && operator.isSystemAdmin();
    }
    
    /**
     * 获取用户可见的业务Id、名字集合
     * @param operator 操作员
     * @return 用户可见的业务
     * @throws Exception 异常
     */
    public List<ServiceDefinationEntity> getVisibleServices(Operator operator)
        throws Exception
    {
        if (null != operator)
        {
            //系统管理员显示所有的业务集合
            if (operator.isSystemAdmin())
            {
                return umDao.getAllVisualServiceIdNames();
            }
            else
            {
                return umDao.getVisualServiceIdNames(operator.getVisibleGroups());
            }
        }
        else
        {
            return new ArrayList<ServiceDefinationEntity>(0);
        }
    }
    
    /**
     * 获取所有的业务Id、名字集合
     * @param operator 操作员
     * @return 用户可见的业务
     * @throws Exception 异常
     */
    public List<ServiceDefinationEntity> getAllServiceIdNames(Operator operator)
        throws Exception
    {
        if (null != operator)
        {
            return umDao.getAllServiceIdNameList();
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
     * @throws Exception 异常
     */
    public Operator createOperator(String operatorName)
        throws Exception
    {
        Operator operator = new Operator(operatorName, null);
        OperatorInfoEntity operatorInfo = getOperatorInfo(operatorName);
        //不存在操作员或者角色不存在
        if (null == operatorInfo || null == operatorInfo.getRoleId())
        {
            LOGGER.error("operatorInfo is null or roleId is null! operatorName is {}.", operatorName);
            return null;
        }
        
        Integer roleId = operatorInfo.getRoleId();
        RoleDefinationEntity roleDef = getRole(roleId);
        //角色不存在
        if (null == roleDef)
        {
            LOGGER.error("roleDef is null! operatorName is {}.", operatorName);
            return null;
        }
        
        List<String> osUsers = parse2List(roleDef.getOsUsers(), ",");
        List<String> groups = parse2List(roleDef.getOtherGroups(), ",");
        //获取所有的OS用户组
        List<OSUserInfoEntity> ownUserGroups = getGroupByOSUsers(osUsers);
        Map<String, String> userGroupMap = new HashMap<String, String>();
        for (OSUserInfoEntity ownUserGroup : ownUserGroups)
        {
            userGroupMap.put(ownUserGroup.getOsUser(), ownUserGroup.getUserGroup());
            groups.add(ownUserGroup.getUserGroup());
        }
        
        Role role = new Role(roleDef.getRoleId(), roleDef.getRoleName(), osUsers, groups, userGroupMap);
        operator.setRole(role);
        LOGGER.debug("createOperatorByName [operator={}]", operator);
        
        return operator;
    }
    
    /**
     * 将str分割成列表并剔除空白
     * @param str 字符串
     * @param splitChar 分割符
     * @return 列表
     */
    private List<String> parse2List(String str, String splitChar)
    {
        List<String> strList = new ArrayList<String>();
        if (StringUtils.isBlank(str))
        {
            return strList;
        }
        
        String[] strs = str.split(",");
        for (String s : strs)
        {
            if (!StringUtils.isBlank(s))
            {
                strList.add(s.trim());
            }
        }
        
        return strList;
    }
    
    /**
     * 通过账号密码查询用户
     * @param usernameAndPasswordParam 账号密码
     * @return 操作员信息
     * @throws Exception 异常
     */
    public OperatorInfoEntity getOperatorInfo(UsernameAndPasswordParam usernameAndPasswordParam)
        throws Exception
    {
        LOGGER.debug("Enter getOperatorInfo. usernameAndPasswordParam is {}", usernameAndPasswordParam);
        try
        {
            return umDao.getOperatorInfo(usernameAndPasswordParam);
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfo failed! usernameAndPasswordParam is {}", usernameAndPasswordParam, e);
            throw e;
        }
    }
    
    /**
     * 获取用户名集合所属的用户组集合
     * @param osUsers OS用户名集合
     * @return 用户组集合
     */
    public List<OSUserInfoEntity> getGroupByOSUsers(List<String> osUsers)
    {
        return umDao.getGroupByOSUsers(osUsers);
    }
    
    /**
     * 通过用户名查询用户
     * @param operatorName 用户名
     * @return 操作员信息
     */
    public OperatorInfoEntity getOperatorInfo(String operatorName)
    {
        LOGGER.debug("Enter getOperatorInfoByName. operatorName is {}", operatorName);
        try
        {
            if (StringUtils.isBlank(operatorName))
            {
                return null;
            }
            
            return umDao.getOperatorInfoByName(operatorName);
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfoByName failed! operatorName is {}", operatorName, e);
        }
        
        return null;
    }
    
    /**
     * 通过角色id查询角色
     * @param roleId 角色id
     * @return 角色信息
     */
    public RoleDefinationEntity getRole(Integer roleId)
    {
        LOGGER.debug("Enter getRole. roleId is {}", roleId);
        try
        {
            return umDao.getRole(roleId);
        }
        catch (Exception e)
        {
            LOGGER.error("getRole failed! roleId is {}", roleId, e);
        }
        
        return null;
    }
    
    /**
     * 通过用户名集合查询指定用户
     * @param userSearch 用户查询
     * @return 用户信息集合
     * @throws Exception 异常
     */
    public List<OperatorInfoEntity> getUsers(UserSearch userSearch)
        throws Exception
    {
        LOGGER.debug("Enter getUsers. userSearch is {}", userSearch);
        try
        {
            return umDao.getUsers(userSearch);
        }
        catch (Exception e)
        {
            LOGGER.error("getUsers failed! userSearch is {}", userSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过用户名集合查询指定用户总数
     * @param search 用户查询
     * @return 用户总数
     * @throws Exception 统一封装的异常
     */
    public Integer getUsersCount(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCount. search is {}", search);
        try
        {
            if (null != search)
            {
                return umDao.getUsersCount(search);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过绑定的OS用户查询指定用户
     * @param osUser 操作系统用户
     * @return 用户信息集合
     * @throws Exception 异常
     */
    public List<OperatorInfoEntity> getUsersByOsUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getUsersByOsUser. roleId is {}", osUser);
        try
        {
            return umDao.getUsersByOsUser(osUser);
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersByOsUser failed! roleId is {}", osUser, e);
            throw e;
        }
    }
    
    /**
     * 通过绑定的OS用户查询指定用户总数
     * @param osUser 角色Id
     * @return 用户总数
     * @throws Exception 异常
     */
    public Integer getUsersCountByOsUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCountByOsUser. roleId is {}", osUser);
        try
        {
            if (null != osUser)
            {
                return umDao.getUsersCountByOsUser(osUser);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersCountByOsUser failed! osUser is {}", osUser, e);
            throw e;
        }
    }
    
    /**
     * 新增用户
     * @param operatorInfo 用户
     * @throws Exception 异常
     */
    public void addUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter addUser. operatorInfo is {}", operatorInfo);
        try
        {
            umDao.addUser(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改用户
     * @param operatorInfo 用户
     * @throws Exception 异常
     */
    public void updateUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateUser. operatorInfo is {}", operatorInfo);
        try
        {
            umDao.updateUser(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
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
            umDao.deleteUser(operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取未绑定OS用户的用户集合
     * @return 未绑定OS用户的用户集合
     * @throws Exception 异常
     */
    public List<OperatorInfoEntity> getUsersNoOsUser()
        throws Exception
    {
        LOGGER.debug("Enter getUsersNoOsUser.");
        try
        {
            return umDao.getUsersNoOsUser();
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersNoOsUser failed!", e);
            throw e;
        }
    }
    
    /**
     * 通过角色ID集合查询指定角色
     * @param roleSearch 用户查询
     * @return 角色信息集合
     * @throws Exception 异常
     */
    public List<RoleDefinationEntity> getRolesByName(RoleSearch roleSearch)
        throws Exception
    {
        LOGGER.debug("Enter getRolesByName. roleSearch is {}", roleSearch);
        try
        {
            return umDao.getRolesByName(roleSearch);
        }
        catch (Exception e)
        {
            LOGGER.error("getRolesByName failed! roleSearch is {}", roleSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过角色Id集合查询指定角色总数
     * @param search 角色查询
     * @return 用户总数
     * @throws Exception 异常
     */
    public Integer getRolesCountByName(RoleSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getRolesCountByName. search is {}", search);
        try
        {
            if (null != search)
            {
                return umDao.getRolesCountByName(search);
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
     * 获取角色Id集合
     * @return 角色Id集合
     * @throws Exception 异常
     */
    public List<RoleDefinationEntity> getAllRoleIdList()
        throws Exception
    {
        LOGGER.debug("Enter getAllRoleIdList.");
        try
        {
            return umDao.getAllRoleIdList();
        }
        catch (Exception e)
        {
            LOGGER.error("getAllRoleIdList failed!", e);
            throw e;
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
            umDao.addRole(roleDef);
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
            umDao.updateRole(roleDef);
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
            umDao.deleteRole(roleDef);
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
            umDao.addRolePrivilege(rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
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
            umDao.updateRolePrivilege(rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
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
            umDao.deleteRolePrivilege(rolePrivilegeInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteRolePrivilege failed! rolePrivilegeInfo is {}", rolePrivilegeInfo, e);
            throw e;
        }
    }
    
    /**
     * 通过角色权限查询指定权限总数
     * @param rolePrivilegeInfo 角色权限查询
     * @return 角色总数
     * @throws Exception 异常
     */
    public Integer getRolePrivilegeCount(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter getRolePrivilegeCount. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            if (null != rolePrivilegeInfo)
            {
                return umDao.getRolePrivilegeCount(rolePrivilegeInfo);
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
            return umDao.getPrivileges(search);
        }
        catch (Exception e)
        {
            LOGGER.error("getPrivileges failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 获取已分配的任务组和对应的权限类型
     * @param search 角色id
     * @return 角色权限信息集合
     * @throws Exception 异常
     */
    public List<RolePrivilegeInfo> getHavePrivileges(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getHavePrivileges. search is {}", search);
        try
        {
            return umDao.getHavePrivileges(search);
        }
        catch (Exception e)
        {
            LOGGER.error("getHavePrivileges failed! search is {}", search, e);
            throw e;
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
            return umDao.getOSUsersByName(search);
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsersByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名查询相关的OS用户、用户组已经业务信息
     * @param osUsers OS用户列表
     * @return 用户信息集合
     * @throws Exception 异常
     */
    public List<OSUserGroupServiceEntity> getOSUserGroupServicesByName(List<String> osUsers)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersByName. osUsers is {}", osUsers);
        try
        {
            return umDao.getOSUserGroupServicesByName(osUsers);
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUserGroupServicesByName failed! osUsers is {}", osUsers, e);
            throw e;
        }
    }
    
    /**
     * 通过用户组查询指定的os用户列表
     * @param groups 用户组列表
     * @return 用户信息集合
     * @throws Exception 异常
     */
    public List<String> getVisibleOsUsers(List<String> groups)
        throws Exception
    {
        LOGGER.debug("Enter getVisibleOsUsers. groups is {}", groups);
        try
        {
            return umDao.getVisibleOsUsers(groups);
        }
        catch (Exception e)
        {
            LOGGER.error("getVisibleOsUsers failed! groups is {}", groups, e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名查询指定OS用户
     * @param osUser OS用户
     * @return 用户信息集合
     * @throws Exception 异常
     */
    public OSUserInfoEntity getOSUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getOSUser. osUser is {}", osUser);
        try
        {
            return umDao.getOSUser(osUser);
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUser failed! osUser is {}", osUser, e);
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
                return umDao.getOSUsersCountByName(search);
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
            umDao.addOSUser(mOSUserInfo);
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
            umDao.updateOSUser(mOSUserInfo);
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
            umDao.deleteOSUser(mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 移除角色Id为roleId的所有操作员
     * @param roleId 角色Id
     */
    private void remove(Integer roleId)
    {
        if (null == roleId)
        {
            return;
        }
        
        synchronized (userOperatorMaps)
        {
            Iterator<Entry<String, Operator>> iter = userOperatorMaps.entrySet().iterator();
            Entry<String, Operator> keyValue;
            Operator operator;
            while (iter.hasNext())
            {
                keyValue = iter.next();
                operator = keyValue.getValue();
                if (null != operator && null != operator.getRole())
                {
                    if (roleId.equals(operator.getRole().getRoleId()))
                    {
                        //移除角色Id相同的操作员
                        iter.remove();
                    }
                }
            }
        }
    }
    
    /**
     * 移除os用户为osUser的所有操作员
     * @param osUser os用户
     */
    private void remove(String osUser)
    {
        if (null == osUser)
        {
            return;
        }
        
        synchronized (userOperatorMaps)
        {
            Iterator<Entry<String, Operator>> iter = userOperatorMaps.entrySet().iterator();
            Entry<String, Operator> keyValue;
            Operator operator;
            while (iter.hasNext())
            {
                keyValue = iter.next();
                operator = keyValue.getValue();
                if (null != operator && null != operator.getRole())
                {
                    List<String> osUsers = operator.getRole().getOsUsers();
                    if (null != osUsers && osUsers.contains(osUser))
                    {
                        //移除包含osUser的操作员
                        iter.remove();
                    }
                }
            }
        }
    }
    
    @Override
    public void process(Event event)
    {
        if (EventType.UPDATE_OPERATOR == event.getType() || EventType.DELETE_OPERATOR == event.getType())
        {
            if (event.getData() instanceof String)
            {
                String opName = (String)event.getData();
                if (null != opName)
                {
                    synchronized (userOperatorMaps)
                    {
                        userOperatorMaps.remove(opName);
                    }
                }
            }
        }
        else if (EventType.UPDATE_ROLE == event.getType() || EventType.DELETE_ROLE == event.getType())
        {
            if (event.getData() instanceof Integer)
            {
                Integer roleId = (Integer)event.getData();
                remove(roleId);
            }
        }
        else if (EventType.UPDATE_OS_USER == event.getType() || EventType.DELETE_OS_USER == event.getType())
        {
            if (event.getData() instanceof String)
            {
                String osUser = (String)event.getData();
                remove(osUser);
            }
        }
        else if (EventType.DELETE_OPERATOR == event.getType())
        {
            if (event.getData() instanceof String)
            {
                String opName = (String)event.getData();
                if (null != opName)
                {
                    synchronized (userOperatorMaps)
                    {
                        userOperatorMaps.remove(opName);
                    }
                }
            }
        }
    }
}
