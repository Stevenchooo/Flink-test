/*
 * 文 件 名:  ConfigInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  配置信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

import java.util.List;

import com.huawei.devicecloud.platform.bi.odp.entity.ConfigEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.DBServerAddressEntity;

/**
 *  配置信息
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class ConfigInfo
{
    //集群Id
    private Integer clusterId;
    
    //阈值相关配置
    private ConfigEntity configEntity;
    
    //用户信息数据库连接信息
    private List<DBServerAddressEntity> dataDBServers;
    
    public ConfigEntity getConfigEntity()
    {
        return configEntity;
    }

    public void setConfigEntity(ConfigEntity configEntity)
    {
        this.configEntity = configEntity;
    }
    
    public Integer getClusterId()
    {
        return clusterId;
    }
    
    public void setClusterId(Integer clusterId)
    {
        this.clusterId = clusterId;
    }

    public List<DBServerAddressEntity> getDataDBServers()
    {
        return dataDBServers;
    }

    public void setDataDBServers(List<DBServerAddressEntity> dataDBServers)
    {
        this.dataDBServers = dataDBServers;
    }
}
