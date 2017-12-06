/*
 * 文 件 名:  ConfigMgnt.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  配置管理
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.management;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.OdpConfig;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ConfigInfo;
import com.huawei.devicecloud.platform.bi.odp.entity.ConfigEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.DBServerAddressEntity;

/**
 * 配置管理
 * 
 * 从db或者配置文件中获取配置
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class ConfigMgnt
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigMgnt.class);
    
    //配置信息
    private ConfigInfo configInfo;
    
    /**
     * Dao层调用对象
     */
    private OdpDao odpDao;
    
    /**
     * 构造函数
     */
    public ConfigMgnt()
    {
        super();
    }
    
    /**
     * 构造函数
     * @param odpDao 数据库操作层
     */
    public ConfigMgnt(final OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
    
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    @Autowired
    public void setOdpDao(final OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
    
    /**
     * 读取配置
     * @throws CException 异常
     */
    public void readConfig()
        throws CException
    {
        try
        {
            //加载系统配置
            final OdpConfig odpConfig = OdpConfig.createOdpConfig();
            
            //获取数据表中的配置
            final ConfigEntity configEntity = this.odpDao.getConfigEntity();
            //获取数据库连接信息列表
            final List<DBServerAddressEntity> dataDbServers = this.odpDao.getDataDBServers();
            
            //构造配置
            configInfo = new ConfigInfo();
            configInfo.setClusterId(odpConfig.getClusterId());
            configInfo.setDataDBServers(dataDbServers);
            //不存在配置记录
            if (null == configEntity)
            {
                LOGGER.error("configEntity can't be null!");
                throw new CException(ResultCode.CONFIG_NOT_EXIST);
            }
            else
            {
                configInfo.setConfigEntity(configEntity);
            }
        }
        catch (CException e)
        {
            //记录异常
            LOGGER.error("readConfig fail!", e);
            throw e;
        }
        
    }
    
    //获取配置信息
    public ConfigInfo getConfigInfo()
    {
        return configInfo;
    }
    
    //设置配置信息
    public void setConfigInfo(final ConfigInfo configInfo)
    {
        this.configInfo = configInfo;
    }
}
