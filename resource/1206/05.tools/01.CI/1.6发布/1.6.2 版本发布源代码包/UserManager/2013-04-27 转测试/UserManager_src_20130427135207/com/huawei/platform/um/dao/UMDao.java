/*
 * 
 * 文 件 名:  UMDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  DAO接口
 * 创 建 人:  z00190165
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.um.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.huawei.platform.common.CException;
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
import com.huawei.platform.um.entity.ReportConfigEntity;
import com.huawei.platform.um.entity.RoleDefinationEntity;
import com.huawei.platform.um.entity.RolePrivilegeInfoEntity;
import com.huawei.platform.um.entity.ServiceDefinationEntity;
import com.huawei.platform.um.entity.UserInfoEntity;
import com.huawei.platform.um.entity.UserProfileEntity;

/**
 * 
 * UM的数据库操作接口
 * 
 * @author z00190165
 * @version [Device Cloud Base Platform Dept UserManager V100R100,
 *          2011-11-21]
 */
public interface UMDao extends Serializable
{
    /**
     * 获取已经记录日志的所有用户名列表
     * 
     * @return 已经记录日志的所有用户名列表
     * @throws Exception
     *             异常
     */
    abstract List<String> getAllUserName()
        throws Exception;
    
    /**
     * 获取业务Id业务名集合
     * 
     * @return 业务Id业务名集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws Exception;
    
    /**
     * 通过业务名集合查询指定业务
     * 
     * @param serviceSearch
     *            业务查询
     * @return 业务Id业务名集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 通过业务名集合查询指定业务总数
     * 
     * @param serviceSearch
     *            业务查询
     * @return 业务Id业务名集合
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getServicesCountByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 新增业务信息
     * 
     * @param serviceDef
     *            业务实体
     * @throws Exception
     *             异常
     */
    abstract void addService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 更新业务信息
     * 
     * @param serviceDef
     *            业务实体
     * @throws Exception
     *             异常
     */
    abstract void updateService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 删除业务信息
     * 
     * @param serviceId
     *            业务Id
     * @throws Exception
     *             异常
     */
    abstract void deleteService(Integer serviceId)
        throws Exception;
    
    /**
     * 获取业务实体
     * 
     * @param serviceId
     *            业务Id
     * @return 业务实体
     * @throws Exception
     *             异常
     */
    abstract ServiceDefinationEntity getService(Integer serviceId)
        throws Exception;
    
    /**
     * 节点信息列表
     * 
     * @return 节点信息列表
     * @throws Exception
     *             异常
     */
    abstract List<NodeInfoEntity> getNodeInfoList()
        throws Exception;
    
    /**
     * 节点信息列表
     * 
     * @param search
     *            查询条件
     * @return 节点信息列表
     * @throws Exception
     *             异常
     */
    abstract List<NodeInfoEntity> getNodes(Search search)
        throws Exception;
    
    /**
     * 节点总数
     * 
     * @param search
     *            查询条件
     * @return 节点信息列表
     * @throws Exception
     *             异常
     */
    abstract Integer getNodesCount(Search search)
        throws Exception;
    
    /**
     * 获取节点信息
     * 
     * @param nodeId
     *            节点Id
     * @return 节点信息
     * @throws Exception
     *             异常
     */
    abstract NodeInfoEntity getNodeInfo(Integer nodeId)
        throws CException;
    
    /**
     * 新增节点信息
     * 
     * @param nodeInfo
     *            节点信息
     * @return 是否新增成功
     * @throws Exception
     *             异常
     */
    abstract boolean addNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception;
    
    /**
     * 修改节点信息
     * 
     * @param nodeInfo
     *            节点信息
     * @return 是否更新成功
     * @throws Exception
     *             异常
     */
    abstract boolean updateNodeInfo(NodeInfoEntity nodeInfo)
        throws Exception;
    
    /**
     * 删除节点信息
     * 
     * @param nodeId
     *            节点Id
     * @return 是否删除成功
     */
    abstract boolean deleteNodeInfo(Integer nodeId);
    
    /**
     * 通过账号密码查询用户
     * 
     * @param usernameAndPasswordParam
     *            账号密码
     * @return 操作员信息
     * @throws Exception
     *             异常
     */
    abstract OperatorInfoEntity getOperatorInfo(UsernameAndPasswordParam usernameAndPasswordParam)
        throws Exception;
    
    /**
     * 通过用户名查询用户
     * 
     * @param operatorName
     *            用户名
     * @return 操作员信息
     * @throws Exception
     *             异常
     */
    abstract OperatorInfoEntity getOperatorInfoByName(String operatorName)
        throws Exception;
    
    /**
     * 通过用户名集合查询指定用户
     * 
     * @param userSearch
     *            用户查询
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OperatorInfoEntity> getUsers(UserSearch userSearch)
        throws Exception;
    
    /**
     * 通过用户名集合查询指定用户总数
     * 
     * @param search
     *            用户查询
     * @return 用户集合
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getUsersCount(Search search)
        throws Exception;
    
    /**
     * 通过绑定的OS用户查询指定用户
     * 
     * @param osUser
     *            操作系统用户
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OperatorInfoEntity> getUsersByOsUser(String osUser)
        throws Exception;
    
    /**
     * 通过绑定的OS用户查询指定用户总数
     * 
     * @param osUser
     *            操作系统用户
     * @return 用户总数
     * @throws Exception
     *             统一封装的异常
     */
    abstract Integer getUsersCountByOsUser(String osUser)
        throws Exception;
    
    /**
     * 新增用户
     * 
     * @param operatorInfo
     *            用户
     * @throws Exception
     *             异常
     */
    abstract void addUser(OperatorInfoEntity operatorInfo)
        throws Exception;
    
    /**
     * 修改用户
     * 
     * @param operatorInfo
     *            用户
     * @throws Exception
     *             异常
     */
    abstract void updateUser(OperatorInfoEntity operatorInfo)
        throws Exception;
    
    /**
     * 删除用户
     * 
     * @param operatorInfo
     *            用户
     * @throws Exception
     *             统一封装的异常
     */
    abstract void deleteUser(OperatorInfoEntity operatorInfo)
        throws Exception;
    
    /**
     * 通过角色ID集合查询指定角色
     * 
     * @param roleSearch
     *            角色查询
     * @return 角色信息集合
     * @throws Exception
     *             异常
     */
    abstract List<RoleDefinationEntity> getRolesByName(RoleSearch roleSearch)
        throws Exception;
    
    /**
     * 通过角色ID集合查询指定角色总数
     * 
     * @param search
     *            角色查询
     * @return 角色总数
     * @throws Exception
     *             异常
     */
    abstract Integer getRolesCountByName(RoleSearch search)
        throws Exception;
    
    /**
     * 通过角色id查询角色
     * 
     * @param roleId
     *            角色id
     * @return 角色信息
     * @throws Exception
     *             异常
     */
    abstract RoleDefinationEntity getRole(Integer roleId)
        throws Exception;
    
    /**
     * 获取可见的任务Id集合
     * 
     * @param groups
     *            用户组列表
     * @return 可见的任务Id集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getVisualServiceIdNames(List<String> groups)
        throws Exception;
    
    /**
     * 获取所有可见的业务Id集合
     * 
     * @return 可见的业务Id集合
     * @throws Exception
     *             异常
     */
    abstract List<ServiceDefinationEntity> getAllVisualServiceIdNames()
        throws Exception;
    
    /**
     * 获取角色Id集合
     * 
     * @return 角色Id集合
     * @throws Exception
     *             异常
     */
    abstract List<RoleDefinationEntity> getAllRoleIdList()
        throws Exception;
    
    /**
     * 获取所有任务组和对应的权限类型
     * 
     * @param search
     *            角色id
     * @return 角色权限信息集合
     * @throws Exception
     *             异常
     */
    abstract List<RolePrivilegeInfo> getPrivileges(Search search)
        throws Exception;
    
    /**
     * 获取已分配的任务组和对应的权限类型
     * 
     * @param search
     *            角色id
     * @return 角色权限信息集合
     * @throws Exception
     *             异常
     */
    abstract List<RolePrivilegeInfo> getHavePrivileges(Search search)
        throws Exception;
    
    /**
     * 新增角色
     * 
     * @param roleDef
     *            角色
     * @throws Exception
     *             异常
     */
    abstract void addRole(RoleDefinationEntity roleDef)
        throws Exception;
    
    /**
     * 修改角色
     * 
     * @param roleDef
     *            角色
     * @throws Exception
     *             异常
     */
    abstract void updateRole(RoleDefinationEntity roleDef)
        throws Exception;
    
    /**
     * 删除角色
     * 
     * @param roleDef
     *            角色
     * @throws Exception
     *             异常
     */
    abstract void deleteRole(RoleDefinationEntity roleDef)
        throws Exception;
    
    /**
     * 新增角色权限
     * 
     * @param rolePrivilegeInfo
     *            角色权限
     * @throws Exception
     *             异常
     */
    abstract void addRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 修改角色权限
     * 
     * @param rolePrivilegeInfo
     *            角色权限
     * @throws Exception
     *             异常
     */
    abstract void updateRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 删除角色权限
     * 
     * @param rolePrivilegeInfo
     *            角色权限
     * @throws Exception
     *             异常
     */
    abstract void deleteRolePrivilege(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 通过角色查询指定角色总数
     * 
     * @param rolePrivilegeInfo
     *            角色查询
     * @return 角色总数
     * @throws Exception
     *             异常
     */
    abstract Integer getRolePrivilegeCount(RolePrivilegeInfoEntity rolePrivilegeInfo)
        throws Exception;
    
    /**
     * 获取未绑定OS用户的用户集合
     * 
     * @return 取未未绑定OS用户的用户集合
     * @throws Exception
     *             异常
     */
    abstract List<OperatorInfoEntity> getUsersNoOsUser()
        throws Exception;
    
    /**
     * 通过OS用户名集合查询指定OS用户
     * 
     * @param search
     *            OS用户查询
     * @return OS用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OSUserInfoEntity> getOSUsersByName(Search search)
        throws Exception;
    
    /**
     * 获取所有的OS用户列表
     * 
     * @return OS用户列表
     * @throws Exception
     *             异常
     */
    abstract List<OSUserInfoEntity> getOSUsers()
        throws Exception;
    
    /**
     * 通过OS用户名集合查询指定OS用户总数
     * 
     * @param search
     *            OS用户查询
     * @return OS用户总数
     * @throws Exception
     *             异常
     */
    abstract Integer getOSUsersCountByName(Search search)
        throws Exception;
    
    /**
     * 新增OS用户
     * 
     * @param mOSUserInfoEntity
     *            OS用户
     * @throws Exception
     *             异常
     */
    abstract void addOSUser(OSUserInfoEntity mOSUserInfoEntity)
        throws Exception;
    
    /**
     * 修改OS用户
     * 
     * @param mOSUserInfoEntity
     *            OS用户
     * @throws Exception
     *             异常
     */
    abstract void updateOSUser(OSUserInfoEntity mOSUserInfoEntity)
        throws Exception;
    
    /**
     * 删除OS用户
     * 
     * @param mOSUserInfoEntity
     *            OS用户
     * @throws Exception
     *             异常
     */
    abstract void deleteOSUser(OSUserInfoEntity mOSUserInfoEntity)
        throws Exception;
    
    /**
     * 获取OS用户信息
     * 
     * @param osUserName
     *            OS用户名
     * @return OS用户信息
     */
    abstract OSUserInfoEntity getOSUser(String osUserName);
    
    /**
     * 获取用户名集合所属的用户组集合
     * 
     * @param osUsers
     *            OS用户名集合
     * @return 用户组集合
     */
    abstract List<OSUserInfoEntity> getGroupByOSUsers(List<String> osUsers);
    
    /**
     * 获取用户组信息
     * 
     * @param group
     *            OS用户组
     * @return 用户组集合
     */
    abstract OSGroupInfoEntity getOSGroup(String group);
    
    /**
     * 新增OS用户组
     * 
     * @param groupE
     *            OS用户组
     * @throws Exception
     *             异常
     */
    abstract void addOSGroup(OSGroupInfoEntity groupE)
        throws Exception;
    
    /**
     * 修改OS用户组
     * 
     * @param groupE
     *            OS用户组
     * @throws Exception
     *             异常
     */
    abstract void updateOSGroup(OSGroupInfoEntity groupE)
        throws Exception;
    
    /**
     * 删除OS用户组信息
     * 
     * @param groupE
     *            OS用户组
     * @throws Exception
     *             异常
     */
    abstract void deleteOSGroup(OSGroupInfoEntity groupE)
        throws Exception;
    
    /**
     * 获取OS用户组列表
     * 
     * @return OS用户组列表
     */
    abstract List<OSGroupInfoEntity> getOSGroups();
    
    /**
     * 增加操作记录
     * 
     * @param operateAuditInfo
     *            操作记录
     * @throws Exception
     *             异常
     */
    abstract void addOperLog(OperateAuditInfoEntity operateAuditInfo)
        throws Exception;
    
    /**
     * 由查询条件取出审计记录
     * 
     * @param search
     *            查询条件
     * @return 审计记录集合
     * @throws Exception
     *             异常
     */
    abstract List<OperateAuditInfoEntity> getOperationRecords(OperationRecordSearch search)
        throws Exception;
    
    /**
     * 通过查询条件查询指定审计记录总数
     * 
     * @param search
     *            查询
     * @return 审计记录总数
     * @throws Exception
     *             异常
     */
    abstract Integer getRecordCount(OperationRecordSearch search)
        throws Exception;
    
    /**
     * 删除三个月前的审计记录
     * 
     * @param date
     *            三个月前的时间
     * @throws Exception
     *             异常
     */
    abstract void deleteOldOperationRecord(Date date)
        throws Exception;
    
    /**
     * 通过OS用户名查询相关的OS用户、用户组已经业务信息
     * 
     * @param osUsers
     *            OS用户列表
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<OSUserGroupServiceEntity> getOSUserGroupServicesByName(List<String> osUsers)
        throws Exception;
    
    /**
     * 通过用户组查询指定的os用户列表
     * 
     * @param groups
     *            用户组列表
     * @return 用户信息集合
     * @throws Exception
     *             异常
     */
    abstract List<String> getVisibleOsUsers(List<String> groups)
        throws Exception;
    
    /**
     * 获取OS组总数
     * 
     * @param search
     *            检索条件
     * @return OS任务组总数
     */
    abstract Integer getOSGroupsCountBySearch(OsGroupSearch search);
    
    /**
     * 获取OS组列表
     * 
     * @param search
     *            检索条件
     * @return OS组列表
     */
    abstract List<OSGroupInfoEntity> getOSGroupsBySearch(OsGroupSearch search);
    
    /**
     * 获取所有目标数据库配置信息
     * @return 所有目标数据库配置信息
     * @throws CException 异常
     */
    abstract List<DBServerConfigEntity> getDBServers()
        throws CException;
    
    /**
     * 获取TCC服务数据库服务配置
     * @param tccName tcc名字
     * @return TCC服务数据库服务配置
     * @throws CException 异常
     */
    abstract DBServerConfigEntity getDBServerConfig(String tccName)
        throws CException;
    
    /**
     * 获取用户信息
     * @param userInfo 用户信息（节点、应用类型、用户名）
     * @return 用户信息
     * @throws CException 异常
     */
    abstract UserInfoEntity getUserInfo(UserInfoEntity userInfo)
        throws CException;
    
    /**
     * 获取用户配置
     * @param userName 用户名
     * @return 用户配置
     * @throws CException 异常
     */
    abstract UserProfileEntity getUserProfile(String userName)
        throws CException;
    
    /**
     * 新增或更新用户配置
     * @param profile 配置信息
     * @return 是否成功
     * @throws CException 异常
     */
    abstract boolean addOrUpdateUserProfile(UserProfileEntity profile)
        throws CException;
    
    /**
     * 新增或更新用户信息
     * @param userInfo 用户信息
     * @return 是否成功
     * @throws CException 异常
     */
    abstract boolean addOrUpdateUserInfo(UserInfoEntity userInfo)
        throws CException;
    
    /**
     * 获取报表配置
     * @param nodeId 节点Id
     * @return 用户配置
     * @throws CException 异常
     */
    abstract ReportConfigEntity getReportCfg(Integer nodeId)
        throws CException;
    
    /**
     * 删除用户信息
     * @param userName 用户名
     * @return 是否删除成功
     */
    abstract boolean deleteUserInfo(String userName);
    
    /**
     * 删除用户配置信息
     * @param userName 用户名
     * @return 是否删除成功
     */
    abstract boolean deleteUserProfile(String userName);
}
