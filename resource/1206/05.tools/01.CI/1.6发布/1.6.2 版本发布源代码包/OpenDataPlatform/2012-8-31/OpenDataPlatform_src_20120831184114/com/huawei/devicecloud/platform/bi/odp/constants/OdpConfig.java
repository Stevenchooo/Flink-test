/*
 * 文 件 名:  OdpConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  ODP的配置文件类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;

/**
 * ODP的配置文件类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class OdpConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpConfig.class);
    
    private static OdpConfig config;
    
    /**
     * 集群Id
     */
    private Integer clusterId = loadClusterId();
    
    /**
     * 是否启用HTTP头校验
     */
    private boolean enableAuthen = loadEnableAuthen();
    
    /**
     * 记录文件目录
     */
    private String rfilesDir = loadRfilesDir();
    
    /**
     * 是否在启动时初始化流控值
     */
    private boolean initLoadCtrlValue = loadLoadCtrlValue();
    
    /**
     * 默认构造函数
     */
    private OdpConfig()
    {
        
    }
    
    public String getRfilesDir()
    {
        return rfilesDir;
    }
    
    public void setRfilesDir(String rfilesDir)
    {
        this.rfilesDir = rfilesDir;
    }
    
    /**
     * 加载集群Id配置项
     */
    private int loadClusterId()
    {
        try
        {
            return Integer.parseInt(CommonUtils.getSysConfig("cluster.id"));
        }
        catch (Exception e)
        {
            LOGGER.error("cluster.id(eg '1') is not correct!", e);
            LOGGER.warn("beacuse no cluster.id item do be found in config file" + ", cluster.id is set to {}", 1);
            return 1;
        }
    }
    
    /**
     * 是否启用鉴权(默认启用)
     */
    private boolean loadEnableAuthen()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("authen.switch"));
        }
        catch (Exception e)
        {
            LOGGER.error("authen.switch(eg '1') is not correct!", e);
            LOGGER.warn("beacuse no authen.switch item do be found in config file" + ", authen.switch is set to {}", 1);
            return true;
        }
    }
    
    /**
     * 是否启用鉴权(默认启用)
     */
    private boolean loadLoadCtrlValue()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("initloadctrl.switch"));
        }
        catch (Exception e)
        {
            LOGGER.error("initloadctrl.switch(eg '1') is not correct!", e);
            LOGGER.warn("beacuse no initloadctrl.switch item do be found in config file"
                + ", initloadctrl.switch is set to {}", 1);
            return true;
        }
    }
    
    /**
     * 加载
     */
    private String loadRfilesDir()
    {
        try
        {
            return CommonUtils.getSysConfig("rfiles.dir");
        }
        catch (Exception e)
        {
            LOGGER.error("rfiles.dir(eg '/home/user/rfiles/') is not correct!", e);
            LOGGER.warn("beacuse no rfiles.dir item do be found in config file" + ", rfiles.dir is set to \"\"");
            return "";
        }
    }
    
    public boolean isInitLoadCtrlValue()
    {
        return initLoadCtrlValue;
    }

    public void setInitLoadCtrlValue(boolean initLoadCtrlValue)
    {
        this.initLoadCtrlValue = initLoadCtrlValue;
    }
    
    /**
     * 创建单例对象
     * @return odp配置
     */
    public static OdpConfig createOdpConfig()
    {
        if (null == config)
        {
            config = new OdpConfig();
        }
        
        return config;
    }
    
    public Integer getClusterId()
    {
        return clusterId;
    }
    
    public void setClusterId(Integer clusterId)
    {
        this.clusterId = clusterId;
    }
    
    public boolean isEnableAuthen()
    {
        return enableAuthen;
    }
    
    public void setEnableAuthen(boolean enableAuthen)
    {
        this.enableAuthen = enableAuthen;
    }
}
