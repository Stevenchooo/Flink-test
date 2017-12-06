/*
 * 文 件 名:  ReadConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  配置项读取
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取基本配置文件
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-14]
 */
public final class ReadConfig
{
    private static final String CONFIG = "." + File.separator + "conf"
            + File.separator + "config";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadConfig.class);
    
    /**
     * 存储配置信息
     */
    private static Properties prop = null;
    
    /**
     * 禁止初始化
     */
    private ReadConfig()
    {
    }
    
    /**
     * 根据key得到配置的value
     * @param key 所取值的健
     * @return String
     */
    public static String getProperties(String key)
    {
        
        //初始化
        if (prop == null)
        {
            initProperties();
        }
        
        //取值
        if (prop == null)
        {
            
            return null;
        }
        else
        {
            String p = prop.getProperty(key);
            if (null == p || "".equals(p))
            {
                LOGGER.error("the parm " + key + " not found...");
            }
            return p;
        }
    }
    
    /**
     * 初始化配置信息
     */
    private static synchronized void initProperties()
    {
        
        //读取配置文件sysconfig.properties
        if (prop == null)
        {
            InputStream in = null;
            
            FileInputStream fileInputSream = null;
            
            prop = new Properties();
            
            try
            {
                fileInputSream = new FileInputStream(CONFIG);
                
                in = new BufferedInputStream(fileInputSream);
                
                prop.load(in);
            }
            catch (FileNotFoundException e)
            {
                LOGGER.error("initProperties error!!!", e);
                
                prop = new Properties();
            }
            catch (IOException e)
            {
                LOGGER.error("initProperties error!!!", e);
                prop = new Properties();
            }
            finally
            {
                if (null != fileInputSream)
                {
                    try
                    {
                        fileInputSream.close();
                    }
                    catch (IOException e)
                    {
                        LOGGER.error("close stream failed!", e);
                    }
                }
                
                if (null != in)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e)
                    {
                        LOGGER.error("close stream failed!", e);
                    }
                }
            }
        }
        
    }
}
