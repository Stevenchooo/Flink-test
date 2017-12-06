/*
 * 文 件 名:  TccConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  UM的配置类
 * 创 建 人:  z00190465
 * 创建时间:  2013-03-27
 */
package com.huawei.platform.um.constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.um.utils.UMUtil;

/**
 * UM的配置类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2013-03-27]
 */
public class UMConfig
{
    private static Map<String, Object> CONFIGS = new HashMap<String, Object>();
    
    /**
     * 一天的小时数
     */
    public static final int HOURS_OF_DAY = 24;
    
    /**
     * TCC数据库连接配置文件路径
     */
    public static final String TCC_DB_PROPERTIES_PATH = "../classes/jdbc.properties";
    
    /**
     * TCC配置文件路径
     */
    public static final String TCC_CONFIG_PATH = "../conf/common/resource/systemconfig/sysconfig.properties";
    
    /**
     * TCC log4j配置文件路径
     */
    public static final String TCC_LOG4J_PROPERTIES_PATH = "../classes/log4j.properties";
    
    /**
     * 每秒的毫秒数
     */
    public static final long MILLS_PER_SECOND = 1000;
    
    /**
     * 每分钟的毫秒数
     */
    public static final long MILLS_PER_MINUTES = 60 * MILLS_PER_SECOND;
    
    /**
     * 每小时的毫秒数
     */
    public static final long MILLS_PER_HOUR = 60 * MILLS_PER_MINUTES;
    
    /**
     * 每天的毫秒数
     */
    public static final long MILLS_PER_DAY = 24 * MILLS_PER_HOUR;
    
    /**
     * 每天的秒数
     */
    public static final long SECOND_PER_DAY = 24 * 3600;
    
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UMConfig.class);
    
    static
    {
        CONFIGS.put("um.uploadBase", UMConfig.class.getResource("/").getFile() + File.separatorChar + "../upload/");
        CONFIGS.put("um.rebootCmd", "/bin/sh -c rebootTomcate.sh");
        CONFIGS.put("um.portalUrl", "http://localhost/userManager");
        CONFIGS.put("um.deployNodeId", null);
        
        loadAll();
    }
    
    private static void loadAll()
    {
        loadUploadBase();
        loadPortalUrl();
        loadRebootCmd();
        loadUmDeployNodeId();
    }
    
    private static void loadUmDeployNodeId()
    {
        try
        {
            CONFIGS.put("um.deployNodeId", Integer.parseInt(UMUtil.getSysConfig("um.deployNodeId")));
        }
        catch (Exception e)
        {
            LOGGER.error("um.deployNodeId(eg '1') is not correct!", e);
            LOGGER.info("beacuse no um.deployNodeId item do be found in config file" + ", um.deployNodeId is set to {}",
                CONFIGS.get("um.deployNodeId"));
        }
    }
    
    private static void loadUploadBase()
    {
        try
        {
            CONFIGS.put("um.uploadBase", UMUtil.getSysConfig("um.uploadBase"));
        }
        catch (Exception e)
        {
            LOGGER.error("um.uploadBase(eg '/home/test/') is not correct!", e);
            LOGGER.info("beacuse no um.uploadBase item do be found in config file" + ", um.uploadBase is set to {}",
                CONFIGS.get("um.uploadBase"));
        }
    }
    
    private static void loadPortalUrl()
    {
        try
        {
            CONFIGS.put("um.portalUrl", UMUtil.getSysConfig("um.portalUrl"));
        }
        catch (Exception e)
        {
            LOGGER.error("um.portalUrl(eg 'http://localhost/userManager') is not correct!", e);
            LOGGER.info("beacuse no um.portalUrl item do be found in config file" + ", um.portalUrl is set to {}",
                CONFIGS.get("um.portalUrl"));
        }
    }
    
    private static void loadRebootCmd()
    {
        try
        {
            CONFIGS.put("um.rebootCmd", UMUtil.getSysConfig("um.rebootCmd"));
        }
        catch (Exception e)
        {
            LOGGER.error("um.rebootCmd(eg '/bin/sh -c rebootTomcate.sh" + "') is not correct!", e);
            LOGGER.info("beacuse no um.rebootCmd item do be found in config file" + ", um.rebootCmd is set to {}",
                CONFIGS.get("um.rebootCmd"));
        }
    }
    
    public static String getUploadBase()
    {
        return (String)CONFIGS.get("um.uploadBase");
    }
    
    public static void setUploadBase(String uploadBase)
    {
        CONFIGS.put("um.uploadBase", uploadBase);
    }
    
    public static String getPortalUrl()
    {
        return (String)CONFIGS.get("um.portalUrl");
    }
    
    public static void setPortalUrl(String portalUrl)
    {
        CONFIGS.put("um.portalUrl", portalUrl);
    }
    
    public static String getRebootCmd()
    {
        return (String)CONFIGS.get("um.rebootCmd");
    }
    
    public static void setRebootCmd(String rebootCmd)
    {
        CONFIGS.put("um.rebootCmd", rebootCmd);
    }
    
    public static Integer getUmDeployNodeId()
    {
        return (Integer)CONFIGS.get("um.deployNodeId");
    }
    
    public static void setUmDeployNodeId(Integer umDeployNodeId)
    {
        CONFIGS.put("um.deployNodeId", umDeployNodeId);
    }
}
