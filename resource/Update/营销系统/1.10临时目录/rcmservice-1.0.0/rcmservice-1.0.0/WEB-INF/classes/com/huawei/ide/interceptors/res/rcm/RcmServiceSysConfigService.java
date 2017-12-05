/*
 * 文 件 名:  CorpSysConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */
package com.huawei.ide.interceptors.res.rcm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 加载系统配置项
 * 并提供工具接口进行系统配置信息查询的服务实现
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
public final class RcmServiceSysConfigService
{
    /**
     * 存储资源文件中配置信息
     */
    private static Properties cfgDefinition = new Properties();
    
    /**
     * 存储邮件配置资源文件中配置信息
     */
    @SuppressWarnings("unused")
    private static Properties emailcfgDefinition = new Properties();
    
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH = RcmServiceSysConfigService.class.getResource("/").getFile()
        + "../conf/common/systemconfig/rcm.service.sysconfig.properties";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RcmServiceSysConfigService.class);
    
    private static RcmServiceSysConfigService ingleInstanse = new RcmServiceSysConfigService();
    
    private RcmServiceSysConfigService()
    {
        initSysConfigDefInfo();
    }
    
    /**
     * 创建配置Service
     * @return 配置Service对象
     */
    public static RcmServiceSysConfigService getConfigService()
    {
        return ingleInstanse;
    }
    
    /** 
     * 初始化入口，加载资源文件
     * @see [类、类#方法、类#成员]
     */
    public static synchronized void initSysConfigDefInfo()
    
    {
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader reader = null;
        
        try
        {
            in = new FileInputStream(CONFIG_FILE_PATH);
            inReader = new InputStreamReader(in, "utf-8");
            reader = new BufferedReader(inReader);
            
            cfgDefinition.load(reader);
            
        }
        catch (IOException e)
        {
            LOGGER.debug("get config file error! Exception is {}", e);
            LOGGER.error("get config file error!");
        }
        finally
        {
        	 IOUtils.closeQuietly(reader);
             IOUtils.closeQuietly(inReader);
             IOUtils.closeQuietly(in);
           
        }
    }
    
    /**
     * 运行时刷新sys config info
     * @throws IOException
     *          IOException
     */
    public static synchronized void reloadSysConfigDefInfo()
        throws IOException
    {
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader reader = null;
        Properties cfgDefinitionNew = new Properties();
        
        try
        {
            in = new FileInputStream(CONFIG_FILE_PATH);
            inReader = new InputStreamReader(in, "utf-8");
            reader = new BufferedReader(inReader);
            
            cfgDefinitionNew.load(reader);
            cfgDefinition = cfgDefinitionNew;
        }
        catch (IOException e)
        {
            LOGGER.debug("Failed to reaload system configuration Exception is {}", e);
        }
        finally
        {
            if (null != in)
            {
                IOUtils.closeQuietly(in);
            }
            if (null != inReader)
            {
                IOUtils.closeQuietly(inReader);
            }
            if (null != reader)
            {
                IOUtils.closeQuietly(reader);
            }
            
        }
        
    }
    
    /**
     * 根据配置key值获取配置项信息
     * @param key 配置key值
     * @return  String 
     * @see [类、类#方法、类#成员]
     */
    public String getSysConfigKey(String key)
    {
        return (null == cfgDefinition) ? null : String.valueOf((null == cfgDefinition.get(key)) ? ""
            : cfgDefinition.get(key));
    }
    
}
