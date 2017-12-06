/*
 * 文 件 名:  SysConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */
package com.huawei.platform.um.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 加载系统配置项
 * 并提供工具接口进行系统配置信息查询的服务实现
 * 
 * @author  l00190934
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2011-12-7]
 */
public class SysConfigService
{
    /**
     * 存储资源文件中配置信息
     */
    private static Properties cfgDefinition = new Properties();
    
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH = SysConfigService.class.getResource("/").getFile()
        + "../conf/common/resource/systemconfig/sysconfig.properties";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigService.class);
    
    /** 
     * 初始化入口，加载资源文件
     * @throws IOException 
     * @see [类、类#方法、类#成员]
     */
    public void initSysConfigDefInfo()
        throws IOException
    {
        BufferedReader confPath = new BufferedReader(new FileReader(new File(CONFIG_FILE_PATH)));
        
        try
        {
            cfgDefinition.load(confPath);
        }
        catch (Exception e)
        {
            LOGGER.error("Get configuration file path or email configuration file path failed.", e);
        }
        finally
        {
            confPath.close();
        }
    }
    
    /**
     * 存储原来的配置
     * @throws IOException 异常
     */
    public void save()
        throws IOException
    {
        BufferedWriter confWriter = new BufferedWriter(new FileWriter(new File(CONFIG_FILE_PATH)));
        try
        {
            cfgDefinition.store(confWriter, null);
        }
        catch (IOException e)
        {
            LOGGER.error("save Properties failed.", e);
            throw e;
        }
        finally
        {
            if (null != confWriter)
            {
                confWriter.close();
            }
        }
    }
    
    /**
     * 更新键值对
     * @param key 键
     * @param value 值
     */
    public void setProperty(String key, String value)
    {
        cfgDefinition.setProperty(key, value);
    }
    
    /**
     * 获取属性集
     * @return 获取属性集
     */
    public Properties getPropertys()
    {
        return cfgDefinition;
    }
    
    /**
     * 根据配置key值获取配置项信息
     * @param key 配置key值
     * @return 配置信息
     * @see [类、类#方法、类#成员]
     */
    public String getSysConfigKey(String key)
    {
        return (null == cfgDefinition) ? null : String.valueOf((null == cfgDefinition.get(key)) ? ""
            : cfgDefinition.get(key));
    }
    
}
