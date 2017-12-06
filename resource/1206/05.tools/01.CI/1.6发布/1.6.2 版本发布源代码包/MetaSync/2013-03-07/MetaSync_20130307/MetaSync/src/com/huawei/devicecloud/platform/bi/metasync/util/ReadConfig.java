/*
 * 文 件 名:  ReadConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  读取配置文件
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取配置文件
 * @author  z00190554
 * @version [Internet Business Service Platform SP V100R100, 2013-2-5]
 */
public final class ReadConfig
{
    private static String config = "conf/config";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadConfig.class);
    
    private static Properties prop = null;
    
    /**
     * 禁止初始化
     */
    private ReadConfig()
    {
        
    }
    
    /**
     * 根据key得到配置的value
     * @param key 键
     * @return 值
     */
    public static String get(String key)
    {
        if (prop == null)
        {
            initProperties();
        }
        
        if (prop == null)
        {
            return null;
        }
        else
        {
            return prop.getProperty(key);
        }
    }
    
    private static synchronized void initProperties()
    {
        FileReader fr = null;
        try
        {
            prop = new Properties();
            fr = new FileReader(config);
            prop.load(fr);
        }
        catch (Exception e)
        {
            LOGGER.error("init failed!", e);
        }
        finally
        {
            if (null != fr)
            {
                try
                {
                    fr.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("close failed!", e);
                }
            }
        }
    }
}
