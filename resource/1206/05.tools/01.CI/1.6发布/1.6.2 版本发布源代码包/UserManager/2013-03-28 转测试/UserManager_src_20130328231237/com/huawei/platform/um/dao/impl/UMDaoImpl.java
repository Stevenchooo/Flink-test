/*
 * 
 * 文 件 名:  UMDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190165
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.um.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.dao.UMDao;
import com.huawei.platform.um.domain.MultiNameSearch;
import com.huawei.platform.um.domain.OperationRecordSearch;
import com.huawei.platform.um.domain.OsGroupSearch;
import com.huawei.platform.um.domain.RolePrivilegeInfo;
import com.huawei.platform.um.domain.RoleSearch;
import com.huawei.platform.um.domain.Search;
import com.huawei.platform.um.domain.ServiceSearch;
import com.huawei.platform.um.domain.UserSearch;
import com.huawei.platform.um.domain.UsernameAndPasswordParam;
import com.huawei.platform.um.entity.DBServerConfigEntity;
import com.huawei.platform.um.entity.NodeInfoEntity;
import com.huawei.platform.um.entity.OSGroupInfoEntity;
import com.huawei.platform.um.entity.OSUserGroupServiceEntity;
import com.huawei.platform.um.entity.OSUserInfoEntity;
import com.huawei.platform.um.entity.OperateAuditInfoEntity;
import com.huawei.platform.um.entity.OperatorInfoEntity;
import com.huawei.platform.um.entity.RoleDefinationEntity;
import com.huawei.platform.um.entity.RolePrivilegeInfoEntity;
import com.huawei.platform.um.entity.ServiceDefinationEntity;
import com.huawei.platform.um.entity.UserInfoEntity;

/**
 * UM的数据库操作类实现
 * 
 * @author z00190165
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-11-10]
 */
public class UMDaoImpl extends SqlSessionDaoSupport implements UMDao
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 2560796253460468758L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UMDaoImpl.class);
    
    /**
     * 获取已经记录日志的所有用户名列表
     * 
     * @return 已经记录日志的所有用户名列表
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAllUserName()
        throws CException
    {
        LOGGER.debug("enter getAllUserName.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao" + ".log2DB.getAllUserName");
            
            if (null != objRtn)
            {
                return (List<String>)objRtn;
            }
            
            return new ArrayList<String>(0);
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed.", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取业务Id业务名集合
     * 
     * @return 业务Id业务名集合
     * @throws CException
     *             统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws CException
    {
        LOGGER.debug("Enter getAllServiceIdNameList.");
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.serviceDef" + ".getAllServiceIdNameList");
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllServiceIdNameList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过业务名集合查询指定业务
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception
    {
        LOGGER.debug("Enter getServicesByNames. serviceSearch is {}", serviceSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.serviceDef.getServicesByName", serviceSearch);
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getServicesByNames failed! serviceSearch is {}", serviceSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过业务名集合查询指定业务总数
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    @Override
    public Integer getServicesCountByName(ServiceSearch serviceSearch)
        throws Exception
    {
        LOGGER.debug("Enter getServicesCountByName. serviceSearch is {}", serviceSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.serviceDef.getServicesCountByName", serviceSearch);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getServicesCountByName failed! serviceSearch is {}", serviceSearch, e);
            throw e;
        }
    }
    
    /**
     * 新增业务信息
     * @param serviceDef 业务实体
     * @throws Exception 异常
     */
    @Override
    public void addService(ServiceDefinationEntity serviceDef)
        throws Exception
    {
        LOGGER.debug("Enter addService. serviceDef is {}", serviceDef);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.serviceDef.addService", serviceDef);
        }
        catch (Exception e)
        {
            LOGGER.error("addService failed! serviceDef is {}", serviceDef, e);
            throw e;
        }
    }
    
    /**
     * 更新业务信息
     * @param serviceDef 业务实体
     * @throws Exception 统一封装的异常
     */
    @Override
    public void updateService(ServiceDefinationEntity serviceDef)
        throws Exception
    {
        LOGGER.debug("Enter updateService. serviceDef is {}", serviceDef);
        try
        {
            getSqlSession().update("com.huawei.platform.um.dao.serviceDef.updateService", serviceDef);
        }
        catch (Exception e)
        {
            LOGGER.error("updateService failed! serviceDef is {}", serviceDef, e);
            throw e;
        }
    }
    
    /**
     * 删除业务信息
     * @param serviceId 业务Id
     * @throws Exception 异常
     */
    @Override
    public void deleteService(Integer serviceId)
        throws Exception
    {
        LOGGER.debug("Enter deleteService. serviceId is {}", serviceId);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.serviceDef.deleteService", serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteService failed! serviceId is {}", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 获取业务实体
     * @param serviceId 业务Id
     * @return 业务实体
     * @throws Exception 异常
     */
    @Override
    public ServiceDefinationEntity getService(Integer serviceId)
        throws Exception
    {
        LOGGER.debug("enter getService, serviceId is {}", serviceId);
        try
        {
            return (ServiceDefinationEntity)getSqlSession().selectOne("com.huawei.platform.um.dao."
                + "serviceDef.getService",
                serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("get Service failed, serviceId is [{}].", serviceId, e);
            throw e;
        }
    }
    
    /**
     * 节点信息列表
     * @param search 查询条件
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfoEntity> getNodes(Search search)
        throws Exception
    {
        LOGGER.debug("enter getNodes, search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.nodeInfo.getNodes", search);
            
            if (null != objRtn)
            {
                return (List<NodeInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<NodeInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodes failed!", e);
            throw e;
        }
    }
    
    /**
     * 节点总数
     * @param search 查询条件
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @Override
    public Integer getNodesCount(Search search)
        throws Exception
    {
        LOGGER.debug("enter getNodesCount, search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.um.dao.nodeInfo.getNodesCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodesCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 节点信息列表
     * @return 节点信息列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfoEntity> getNodeInfoList()
        throws Exception
    {
        LOGGER.debug("enter getNodeInfoList");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.nodeInfo.getNodeInfoList");
            
            if (null != objRtn)
            {
                return (List<NodeInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<NodeInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeInfoList failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取节点信息
     * @param nodeId 节点Id
     * @return 节点信息
     * @throws Exception 异常
     */
    @Override
    public NodeInfoEntity getNodeInfo(Integer nodeId)
        throws Exception
    {
        LOGGER.debug("enter getNodeInfo, nodeId is {}", nodeId);
        try
        {
            if (null == nodeId)
            {
                return null;
            }
            
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.um.dao.nodeInfo.getNodeInfo", nodeId);
            
            if (null != objRtn)
            {
                return (NodeInfoEntity)objRtn;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeInfo failed, nodeId is [{}].", nodeId, e);
            throw e;
        }
    }
    
    /**
     * 新增节点信息
     * @param nodeInfo 节点信息
     * @return 是否新增成功
    * @throws Exception 异常
     */
    @Override
    public boolean addNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception
    {
        LOGGER.debug("Enter addNodeInfo. nodeInfo is {}", nodeInfo);
        try
        {
            int rows = getSqlSession().insert("com.huawei.platform.um.dao.nodeInfo.addNodeInfo", nodeInfo);
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("addNodeInfo failed! nodeInfo is {}", nodeInfo, e);
            throw e;
        }
    }
    
    /**
     * 修改节点信息
     * @param nodeInfo 节点信息
     * @return 是否更新成功
     * @throws Exception 异常
     */
    public boolean updateNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateNodeInfo. nodeInfo is {}", nodeInfo);
        try
        {
            int rows = getSqlSession().update("com.huawei.platform.um.dao.nodeInfo.updateNodeInfo", nodeInfo);
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("updateNodeInfo failed! nodeInfo is {}", nodeInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除节点信息
     * @param nodeId 节点Id
     * @return 是否删除成功
     */
    public boolean deleteNodeInfo(Integer nodeId)
    
    {
        LOGGER.debug("Enter deleteNodeInfo. nodeId is {}", nodeId);
        try
        {
            int rows = getSqlSession().delete("com.huawei.platform.um.dao.nodeInfo.deleteNodeInfo", nodeId);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteNodeInfo failed! nodeId is {}", nodeId, e);
        }
        return false;
    }
    
    /**
     * 通过账号密码查询用户权限
     * 
     * @param usernameAndPasswordParam 账号密码
     * @return 操作员信息
     * @throws Exception 异常
     */
    @Override
    public OperatorInfoEntity getOperatorInfo(UsernameAndPasswordParam usernameAndPasswordParam)
        throws Exception
    
    {
        LOGGER.debug("Enter getOperatorInfo. usernameAndPasswordParam is {}", usernameAndPasswordParam);
        try
        {
            Object objOperatorInfo =
                getSqlSession().selectOne("com.huawei.platform.um.dao" + ".operatorInfo.getOperatorInfo",
                    usernameAndPasswordParam);
            if (null != objOperatorInfo)
            {
                return (OperatorInfoEntity)objOperatorInfo;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfo failed! usernameAndPasswordParam is {}", usernameAndPasswordParam, e);
            throw e;
        }
    }
    
    /**
     * 通过用户名查询用户权限
     * @param operatorName 用户名
     * @return 操作员信息
     * @throws Exception 异常
     */
    @Override
    public OperatorInfoEntity getOperatorInfoByName(String operatorName)
        throws Exception
    
    {
        LOGGER.debug("Enter getOperatorInfoByName. operatorName is {}", operatorName);
        try
        {
            Object objOperatorInfo =
                getSqlSession().selectOne("com.huawei.platform.um.dao" + ".operatorInfo.getOperatorInfoByName",
                    operatorName);
            if (null != objOperatorInfo)
            {
                return (OperatorInfoEntity)objOperatorInfo;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOperatorInfoByName failed! operatorName is {}", operatorName, e);
            throw e;
        }
    }
    
    /**
     * 通过用户名集合查询指定用户
     * @param userSearch 用户查询
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperatorInfoEntity> getUsers(UserSearch userSearch)
        throws Exception
    {
        LOGGER.debug("Enter getUsers. userSearch is {}", userSearch);
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.operatorInfo.getUsers", userSearch);
            
            if (null != objRtn)
            {
                return (List<OperatorInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<OperatorInfoEntity>(0);
            }
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
     * @return 用户集合
     * @throws Exception 异常
     */
    @Override
    public Integer getUsersCount(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCount. search is {}", search);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.um.dao.operatorInfo.getUsersCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
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
    @SuppressWarnings("unchecked")
    @Override
    public List<OperatorInfoEntity> getUsersByOsUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getUsersByOsUser. osUser is {}", osUser);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.operatorInfo.getUsersByOsUser", osUser);
            
            if (null != objRtn)
            {
                return (List<OperatorInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<OperatorInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersByOsUser failed! osUser is {}", osUser, e);
            throw e;
        }
    }
    
    /**
     * 通过绑定的OS用户查询指定用户总数
     * @param osUser 操作系统用户
     * @return 用户总数
     * @throws Exception 统一封装的异常
     */
    @Override
    public Integer getUsersCountByOsUser(String osUser)
        throws Exception
    {
        LOGGER.debug("Enter getUsersCountByOsUser. osUser is {}", osUser);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.operatorInfo.getUsersCountByOsUser", osUser);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
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
    @Override
    public void addUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter addUser. operatorInfo is {}", operatorInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.operatorInfo.addUser", operatorInfo);
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
    @Override
    public void updateUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateUser. operatorInfo is {}", operatorInfo);
        try
        {
            getSqlSession().update("com.huawei.platform.um.dao.operatorInfo.updateUser", operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("updateUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 删除用户
     * @param operatorInfo 用户
     * @throws Exception 统一封装的异常
     */
    @Override
    public void deleteUser(OperatorInfoEntity operatorInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteUser. operatorInfo is {}", operatorInfo);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.operatorInfo.deleteUser", operatorInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteUser failed! operatorInfo is {}", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 通过角色ID集合查询指定角色
     * @param roleSearch 角色查询
     * @return 角色信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RoleDefinationEntity> getRolesByName(RoleSearch roleSearch)
        throws Exception
    {
        LOGGER.debug("Enter getRolesByName. roleSearch is {}", roleSearch);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.roleDef" + ".getRolesByName", roleSearch);
            
            if (null != objRtn)
            {
                return (List<RoleDefinationEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<RoleDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRolesByName failed! roleSearch is {}", roleSearch, e);
            throw e;
        }
    }
    
    /**
     * 通过角色ID集合查询指定角色总数
     * @param search 角色查询
     * @return 角色总数
     * @throws Exception 异常
     */
    @Override
    public Integer getRolesCountByName(RoleSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getRolesCountByName. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.roleDef" + ".getRolesCountByName", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
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
     * 通过角色id查询角色
     * @param roleId 角色id
     * @return 角色信息
     * @throws Exception 异常
     */
    @Override
    public RoleDefinationEntity getRole(Integer roleId)
        throws Exception
    {
        LOGGER.debug("Enter getRole. roleId is {}", roleId);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.um.dao.roleDef.getRole", roleId);
            
            if (null != objRtn)
            {
                return (RoleDefinationEntity)objRtn;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRole failed! roleId is {}", roleId, e);
            throw e;
        }
    }
    
    /**
     * 获取角色Id集合
     * 
     * @return 角色Id集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RoleDefinationEntity> getAllRoleIdList()
        throws Exception
    {
        LOGGER.debug("Enter getAllRoleIdList.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.roleDef.getAllRoleIdList");
            
            if (null != objRtn)
            {
                return (List<RoleDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<RoleDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllRoleIdList failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取可见的业务Id集合
     * @param groups 用户组列表
     * @return 可见的任务Id集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getVisualServiceIdNames(List<String> groups)
        throws Exception
    {
        LOGGER.debug("Enter getVisualServiceIdNames. groups is {}", groups);
        try
        {
            //直接返回空集合
            if (null != groups && groups.isEmpty())
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.serviceDef.getVisualServiceIdNames", groups);
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getVisualServiceIdNames failed! groups is {}", groups, e);
            throw e;
        }
    }
    
    /**
     * 获取所有可见的业务Id集合
     * @throws Exception 异常
     * @return 可见的业务Id集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceDefinationEntity> getAllVisualServiceIdNames()
        throws Exception
    {
        LOGGER.debug("Enter getAllVisualServiceIdNames. groups is {}");
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.serviceDef.getAllVisualServiceIdNames");
            
            if (null != objRtn)
            {
                return (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                return new ArrayList<ServiceDefinationEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllVisualServiceIdNames failed!", e);
            throw e;
        }
    }
    
    /**
     * 获取所有任务组和对应的权限类型
     * @param search 角色id
     * @return 角色权限信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RolePrivilegeInfo> getPrivileges(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getPrivileges. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.rolePrivilegeInfo.getPrivileges", search);
            
            if (null != objRtn)
            {
                return (List<RolePrivilegeInfo>)objRtn;
            }
            else
            {
                return new ArrayList<RolePrivilegeInfo>(0);
            }
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
    @SuppressWarnings("unchecked")
    @Override
    public List<RolePrivilegeInfo> getHavePrivileges(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getHavePrivileges. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.rolePrivilegeInfo.getHavePrivileges", search);
            
            if (null != objRtn)
            {
                return (List<RolePrivilegeInfo>)objRtn;
            }
            else
            {
                return new ArrayList<RolePrivilegeInfo>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getHavePrivileges failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 新增角色
     * @param roleDef 角色
     * @throws Exception 异常
     */
    @Override
    public void addRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter addRole. roleDef is {}", roleDef);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.roleDef.addRole", roleDef);
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
    @Override
    public void updateRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter updateRole. roleDef is {}", roleDef);
        try
        {
            getSqlSession().update("com.huawei.platform.um.dao.roleDef.updateRole", roleDef);
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
    @Override
    public void deleteRole(RoleDefinationEntity roleDef)
        throws Exception
    {
        LOGGER.debug("Enter deleteRole. roleDef is {}", roleDef);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.roleDef.deleteRole", roleDef);
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
    @Override
    public void addRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter addRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.rolePrivilegeInfo.addRolePrivilege", rolePrivilegeInfo);
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
    @Override
    public void updateRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            getSqlSession().update("com.huawei.platform.um.dao.rolePrivilegeInfo.updateRolePrivilege",
                rolePrivilegeInfo);
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
    @Override
    public void deleteRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteRolePrivilege. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.rolePrivilegeInfo.deleteRolePrivilege",
                rolePrivilegeInfo);
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
    @Override
    public Integer getRolePrivilegeCount(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception
    {
        LOGGER.debug("Enter getRolePrivilegeCount. rolePrivilegeInfo is {}", rolePrivilegeInfo);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.rolePrivilegeInfo" + ".getRolePrivilegeCount",
                    rolePrivilegeInfo);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
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
     * 获取未绑定OS用户的用户集合
     * @return 取未未绑定OS用户的用户集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperatorInfoEntity> getUsersNoOsUser()
        throws Exception
    {
        LOGGER.debug("Enter getUsersNoOsUser.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.operatorInfo.getUsersNoOsUser");
            
            if (null != objRtn)
            {
                return (List<OperatorInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<OperatorInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUsersNoOsUser failed!", e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名集合查询指定OS用户
     * @param search OS用户查询
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserInfoEntity> getOSUsersByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersByName. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.OSUserInfo.getOSUsersByName", search);
            
            if (null != objRtn)
            {
                return (List<OSUserInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<OSUserInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsersByName failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 获取所有的OS用户列表
     * @return OS用户列表
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserInfoEntity> getOSUsers()
        throws Exception
    {
        LOGGER.debug("Enter getOSUsers");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.OSUserInfo.getOSUsers");
            
            if (null != objRtn)
            {
                return (List<OSUserInfoEntity>)objRtn;
            }
            else
            {
                return new ArrayList<OSUserInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUsers failed!", e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名集合查询指定OS用户总数
     * @param search OS用户查询
     * @return OS用户总数
     * @throws Exception 异常
     */
    @Override
    public Integer getOSUsersCountByName(Search search)
        throws Exception
    {
        LOGGER.debug("Enter getOSUsersCountByName. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.OSUserInfo.getOSUsersCountByName", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
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
    @Override
    public void addOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter addOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.OSUserInfo.addOSUser", mOSUserInfo);
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
    @Override
    public void updateOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter updateOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            getSqlSession().update("com.huawei.platform.um.dao.OSUserInfo.updateOSUser", mOSUserInfo);
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
    @Override
    public void deleteOSUser(OSUserInfoEntity mOSUserInfo)
        throws Exception
    {
        LOGGER.debug("Enter deleteOSUser. mOSUserInfo is {}", mOSUserInfo);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.OSUserInfo.deleteOSUser", mOSUserInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOSUser failed! mOSUserInfo is {}", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 获取OS用户信息
     * @param osUserName OS用户名
     * @return OS用户信息
     */
    @Override
    public OSUserInfoEntity getOSUser(String osUserName)
    {
        LOGGER.debug("Enter getOSUser. osUserName is {}", osUserName);
        try
        {
            if (StringUtils.isBlank(osUserName))
            {
                return null;
            }
            
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.um.dao.OSUserInfo.getOSUser", osUserName);
            
            if (null != objRtn)
            {
                return (OSUserInfoEntity)objRtn;
                
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUser failed! osUserName is {}", osUserName, e);
        }
        
        return null;
    }
    
    /**
     * 获取用户名集合所属的用户组集合
     * @param osUsers OS用户名集合
     * @return 用户组集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSUserInfoEntity> getGroupByOSUsers(List<String> osUsers)
    {
        LOGGER.debug("Enter getGroupByOSUsers. osUsers is {}", osUsers);
        try
        {
            if (null == osUsers || osUsers.isEmpty())
            {
                return new ArrayList<OSUserInfoEntity>(0);
            }
            
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.OSUserInfo.getGroupByOSUsers", osUsers);
            
            if (null != objRtn)
            {
                return (List<OSUserInfoEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getGroupByOSUsers failed! osUsers is {}", osUsers, e);
        }
        
        return new ArrayList<OSUserInfoEntity>(0);
    }
    
    /**
     * 获取用户组信息
     * @param group OS用户组
     * @return 用户组集合
     */
    @Override
    public OSGroupInfoEntity getOSGroup(String group)
    {
        LOGGER.debug("Enter getOSGroup. group is {}", group);
        try
        {
            Object objRtn = getSqlSession().selectOne("com.huawei.platform.um.dao.OSGroupInfo.getOSGroup", group);
            
            if (null != objRtn)
            {
                return (OSGroupInfoEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSGroup failed! group is {}", group, e);
        }
        
        return null;
    }
    
    /**
     * 新增OS用户组
     * @param groupE OS用户组
     * @throws Exception 异常
     */
    @Override
    public void addOSGroup(OSGroupInfoEntity groupE)
        throws Exception
    {
        LOGGER.debug("Enter addOSGroup. groupE is {}", groupE);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.OSGroupInfo.addOSGroup", groupE);
        }
        catch (Exception e)
        {
            LOGGER.error("addOSGroup failed! groupE is {}", groupE, e);
            throw e;
        }
    }
    
    /**
     * 修改OS用户组
     * @param groupE OS用户组
     * @throws Exception 异常
     */
    @Override
    public void updateOSGroup(OSGroupInfoEntity groupE)
        throws Exception
    {
        LOGGER.debug("Enter updateOSGroup. groupE is {}", groupE);
        try
        {
            getSqlSession().update("com.huawei.platform.um.dao.OSGroupInfo.updateOSGroup", groupE);
        }
        catch (Exception e)
        {
            LOGGER.error("updateOSGroup failed! groupE is {}", groupE, e);
            throw e;
        }
    }
    
    /**
     * 删除OS用户组信息
     * @param groupE OS用户组
     * @throws Exception 异常
     */
    @Override
    public void deleteOSGroup(OSGroupInfoEntity groupE)
        throws Exception
    {
        LOGGER.debug("Enter deleteOSGroup. mOSUserInfo is {}", groupE);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.OSGroupInfo.deleteOSGroup", groupE);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOSGroup failed! mOSUserInfo is {}", groupE, e);
            throw e;
        }
    }
    
    /**
     * 获取OS用户组列表
     * @return OS用户组列表
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OSGroupInfoEntity> getOSGroups()
    {
        LOGGER.debug("Enter getOSGroups.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.OSGroupInfo.getOSGroups");
            
            if (null != objRtn)
            {
                return (List<OSGroupInfoEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOSGroups failed!", e);
        }
        
        return null;
    }
    
    /**
     * 增加操作记录
     * @param operateAuditInfo 操作记录
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void addOperLog(OperateAuditInfoEntity operateAuditInfo)
        throws Exception
    {
        LOGGER.debug("Enter addOperLog. operateAuditInfo is {}", operateAuditInfo);
        try
        {
            getSqlSession().insert("com.huawei.platform.um.dao.operationRecord.addOperLog", operateAuditInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("addOperLog failed! operateAuditInfo is {}", operateAuditInfo, e);
            throw e;
        }
    }
    
    /**
     * 由查询条件取出审计记录
     * @param search 查询条件
     * @return 审计记录集合
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<OperateAuditInfoEntity> getOperationRecords(OperationRecordSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getOperationRecords. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectList("com.huawei.platform.um.dao.operationRecord" + ".getOperationRecords",
                    search);
            
            if (null != objRtn)
            {
                return (List<OperateAuditInfoEntity>)objRtn;
                
            }
            else
            {
                return new ArrayList<OperateAuditInfoEntity>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOperationRecords failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 通过查询条件查询指定审计记录总数
     * @param search 查询
     * @return 审计记录总数
     * @throws Exception 异常
     */
    @Override
    public Integer getRecordCount(OperationRecordSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getRecordCount. search is {}", search);
        try
        {
            Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.operationRecord.getRecordCount", search);
            
            if (null != objRtn)
            {
                return (Integer)objRtn;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRecordCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 删除三个月前的审计记录
     * @param date 三个月前的时间
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteOldOperationRecord(Date date)
        throws Exception
    {
        LOGGER.debug("Enter deleteOldOperationRecord. date is {}", date);
        try
        {
            getSqlSession().delete("com.huawei.platform.um.dao.operationRecord.deleteOldOperationRecord", date);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOldOperationRecord failed! date is {}", date, e);
            throw e;
        }
    }
    
    /**
     * 通过OS用户名查询相关的OS用户、用户组已经业务信息
     * @param osUsers OS用户列表
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<OSUserGroupServiceEntity> getOSUserGroupServicesByName(List<String> osUsers)
        throws Exception
    {
        try
        {
            //空集合直接返回
            if (null != osUsers && osUsers.isEmpty())
            {
                return new ArrayList<OSUserGroupServiceEntity>(0);
            }
            
            MultiNameSearch search = new MultiNameSearch();
            search.setNames(osUsers);
            Object obj = getSqlSession().selectList("com.huawei.platform.um.dao.OSUserInfo.getOSUGEByName", search);
            
            if (null != obj)
            {
                return (List<OSUserGroupServiceEntity>)obj;
            }
            
            return new ArrayList<OSUserGroupServiceEntity>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getOSUserGroupServicesByName failed. osUsers is {}.", osUsers, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 通过用户组查询指定的os用户列表
     * @param groups 用户组列表
     * @return 用户信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<String> getVisibleOsUsers(List<String> groups)
        throws Exception
    {
        try
        {
            //空集合直接返回
            if (null != groups && groups.isEmpty())
            {
                return new ArrayList<String>(0);
            }
            
            MultiNameSearch search = new MultiNameSearch();
            search.setNames(groups);
            Object obj = getSqlSession().selectList("com.huawei.platform.um.dao.OSUserInfo.getVisibleOsUsers", search);
            
            if (null != obj)
            {
                return (List<String>)obj;
            }
            
            return new ArrayList<String>(0);
        }
        catch (Exception e)
        {
            LOGGER.error("getVisibleOsUsers failed. groups is {}.", groups, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取OS组总数
     * @param search 检索条件
     * @return OS任务组总数
     */
    public Integer getOSGroupsCountBySearch(OsGroupSearch search)
    {
        try
        {
            Object obj =
                getSqlSession().selectOne("com.huawei.platform.um.dao.OSGroupInfo.getOSGroupsCountBySearch", search);
            
            if (null != obj)
            {
                return (Integer)obj;
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getOSGroupsBySearch failed. search is {}.", search, e);
        }
        
        return 0;
    }
    
    /**
     * 获取OS组列表
     * @param search 检索条件
     * @return OS组列表
     */
    @SuppressWarnings("unchecked")
    public List<OSGroupInfoEntity> getOSGroupsBySearch(OsGroupSearch search)
    {
        try
        {
            Object obj =
                getSqlSession().selectList("com.huawei.platform.um.dao.OSGroupInfo.getOSGroupsBySearch", search);
            
            if (null != obj)
            {
                return (List<OSGroupInfoEntity>)obj;
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("search failed. search is {}.", search, e);
        }
        
        return new ArrayList<OSGroupInfoEntity>(0);
    }
    
    /**
     * 获取所有目标数据库配置信息
     * @return 所有目标数据库配置信息
     * @throws CException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DBServerConfigEntity> getDBServers()
        throws CException
    {
        LOGGER.debug("Enter getDataDBServers.");
        try
        {
            final Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.dbServers.getDBServers");
            
            if (null == objRtn)
            {
                return new ArrayList<DBServerConfigEntity>(0);
            }
            else
            {
                return (List<DBServerConfigEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getDataDBServers failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取TCC服务数据库服务配置
     * @param tccName tcc名字
     * @return TCC服务数据库服务配置
     * @throws CException 异常
     */
    public DBServerConfigEntity getDBServerConfig(String tccName)
        throws CException
    {
        LOGGER.debug("Enter getDBServerConfig. tccName is {}.", tccName);
        try
        {
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.dbServers.getDBServer", tccName);
            
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                return (DBServerConfigEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getDBServerConfig failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取所有的业务信息
     * @return 业务信息集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<ServiceDefinationEntity> getServices()
        throws Exception
    {
        List<ServiceDefinationEntity> serviceDefs;
        LOGGER.debug("Enter getServices.");
        try
        {
            Object objRtn = getSqlSession().selectList("com.huawei.platform.um.dao.serviceDef.getServices");
            
            if (null != objRtn)
            {
                serviceDefs = (List<ServiceDefinationEntity>)objRtn;
            }
            else
            {
                serviceDefs = new ArrayList<ServiceDefinationEntity>(0);
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getServices failed!", e);
            throw e;
        }
        
        return serviceDefs;
    }
    
    /**
     * 获取用户信息
     * @param userInfo 用户信息（节点、应用类型、用户名）
     * @return 用户信息
     * @throws CException 异常
     */
    @Override
    public UserInfoEntity getUserInfo(UserInfoEntity userInfo)
        throws CException
    {
        try
        {
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.platform.um.dao.userinfo.getUserInfo", userInfo);
            
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                return (UserInfoEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getDBServerConfig failed! userInfo is {}.", userInfo, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    /************************* portal [end] ***********************************************/
    
}
