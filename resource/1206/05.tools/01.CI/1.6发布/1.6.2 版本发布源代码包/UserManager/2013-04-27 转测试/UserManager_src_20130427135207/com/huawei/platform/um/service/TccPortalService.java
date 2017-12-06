/*
 * 文 件 名:  TccService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.um.service;

import java.util.List;

import com.huawei.platform.common.CException;
import com.huawei.platform.um.domain.ServiceSearch;
import com.huawei.platform.um.entity.DBServerConfigEntity;
import com.huawei.platform.um.entity.ServiceDefinationEntity;

/**
 * TCC业务逻辑操作接口
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2011-11-10]
 */
public interface TccPortalService
{
    /**
     * 获取已经记录日志的所有用户名列表
     * @return 已经记录日志的所有用户名列表
     * @throws CException 统一封装的异常
     */
    public abstract List<String> getAllUserName()
        throws CException;
    
    /**
     * 获取业务Id业务名集合
     * @return 业务Id业务名集合
     * @throws CException 统一封装的异常
     */
    public abstract List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws CException;
    
    /**
     * 通过业务名集合查询指定业务
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    public abstract List<ServiceDefinationEntity> getServicesByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 通过业务名集合查询指定业务总数
     * @param serviceSearch 业务查询
     * @return 业务Id业务名集合
     * @throws Exception 统一封装的异常
     */
    public abstract Integer getServicesCountByName(ServiceSearch serviceSearch)
        throws Exception;
    
    /**
     * 新增业务信息
     * @param serviceDef 业务实体
     * @throws Exception 异常
     */
    public abstract void addService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 更新业务信息
     * @param serviceDef 业务实体
     * @throws Exception 异常
     */
    public abstract void updateService(ServiceDefinationEntity serviceDef)
        throws Exception;
    
    /**
     * 删除业务信息
     * @param serviceId 业务Id
     * @throws Exception 异常
     */
    public abstract void deleteService(Integer serviceId)
        throws Exception;
    
    /**
     * 获取业务实体
     * @param serviceId 业务Id
     * @return 业务实体
     * @throws Exception 异常
     */
    public abstract ServiceDefinationEntity getService(Integer serviceId)
        throws Exception;
    
    /**
     * 获取所有目标数据库配置信息
     * @return 所有目标数据库配置信息
     * @throws CException 异常
     */
    abstract List<DBServerConfigEntity> getDBServers() throws CException;
}