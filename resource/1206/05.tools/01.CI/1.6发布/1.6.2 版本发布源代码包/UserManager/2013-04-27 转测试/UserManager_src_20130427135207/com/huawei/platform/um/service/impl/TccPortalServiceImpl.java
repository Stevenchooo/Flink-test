/*
 * 文 件 名:  TccServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-11-10
 */
package com.huawei.platform.um.service.impl;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.dao.UMDao;
import com.huawei.platform.um.domain.ServiceSearch;
import com.huawei.platform.um.entity.DBServerConfigEntity;
import com.huawei.platform.um.entity.ServiceDefinationEntity;
import com.huawei.platform.um.service.TccPortalService;
import com.huawei.platform.um.utils.JDBCExtAppender;

/**
 * TCC业务逻辑操作实现
 * 
 * @author z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2011-11-10]
 */
public class TccPortalServiceImpl implements TccPortalService, Serializable
{
    /**
     * 序号
     */
    private static final long serialVersionUID = -1088385948803845580L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccPortalServiceImpl.class);
    
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
     * 获取已经记录日志的所有用户名列表
     * @return 已经记录日志的所有用户名列表
     * @throws CException 统一封装的异常
     */
    @Override
    public List<String> getAllUserName()
        throws CException
    {
        LOGGER.debug("enter getAllUserName.");
        try
        {
            //查询前先刷新日志
            JDBCExtAppender.fresh2Db();
            return umDao.getAllUserName();
        }
        catch (Exception e)
        {
            LOGGER.debug("get tccLogList failed.", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取业务Id业务名集合
     * @return 业务Id业务名集合
     * @throws CException 统一封装的异常
     */
    @Override
    public List<ServiceDefinationEntity> getAllServiceIdNameList()
        throws CException
    {
        LOGGER.debug("Enter getAllServiceIdNameList.");
        try
        {
            return umDao.getAllServiceIdNameList();
        }
        catch (Exception e)
        {
            LOGGER.error("getAllServiceIdNameList failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
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
            if (null != serviceSearch)
            {
                return umDao.getServicesCountByName(serviceSearch);
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
            return umDao.getServicesByName(serviceSearch);
        }
        catch (Exception e)
        {
            LOGGER.error("getServicesByNames failed! serviceSearch is {}", serviceSearch, e);
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
            umDao.addService(serviceDef);
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
     * @throws Exception 异常
     */
    @Override
    public void updateService(ServiceDefinationEntity serviceDef)
        throws Exception
    {
        LOGGER.debug("Enter updateService. serviceDef is {}", serviceDef);
        try
        {
            umDao.updateService(serviceDef);
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
            umDao.deleteService(serviceId);
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
            if (null == serviceId)
            {
                return null;
            }
            
            return umDao.getService(serviceId);
        }
        catch (Exception e)
        {
            LOGGER.error("get Service failed, serviceId is [{}].", serviceId, e);
            throw e;
        }
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
            return umDao.getDBServers();
        }
        catch (Exception e)
        {
            LOGGER.error("getDataDBServers failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
}
