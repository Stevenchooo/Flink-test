/*
 * 文 件 名:  ErrorDescConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */
package com.huawei.ide.interceptors.res.rcm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 加载系统配置的异常码/异常描述信息
 * 并提供工具接口进行异常描述信息查询的服务实现
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class ErrorDescConfigService
{
    /**
     * 存储资源文件中配置的异常码及异常描述信息
     */
    private static Properties cfgDefinition = new Properties();
    
    /**
     * 存储资源文件中配置的个人UP异常码与企业UP异常码的映射信息
     */
    private static Properties mappercfgDefinition = new Properties();
    
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH = ErrorDescConfigService.class.getResource("/").getFile()
        + "../conf/common/resource/errorinfo/exception_definition.properties";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorDescConfigService.class);
    
    /** 
     * 初始化入口，加载资源文件
     * @throws FileNotFoundException 
     *         FileNotFoundException
     * @throws UnsupportedEncodingException 
     *         UnsupportedEncodingException
     * @see [类、类#方法、类#成员]
     */
    public void initErrorDefInfo()
        throws FileNotFoundException, UnsupportedEncodingException
    
    {
        File configFile = new File(CONFIG_FILE_PATH);
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader confPath = null;
        
        try
        {
            in = new FileInputStream(configFile);
            inReader = new InputStreamReader(in, "utf-8");
            confPath = new BufferedReader(inReader);
            cfgDefinition.load(confPath);
            
        }
        catch (IOException e)
        {
            LOGGER.debug("get system config file error! Exception is {}", e);
            LOGGER.error("get system config file error!");
        }
        finally
        {


       	   IOUtils.closeQuietly(confPath);
            IOUtils.closeQuietly(inReader);
            IOUtils.closeQuietly(in);
        }
    }
    
    /**
     * 根据异常码获取异常描述信息
     * @param exCode 异常码
     * @return 异常描述信息
     * @see [类、类#方法、类#成员]
     */
    public String getExceptionDesc(String exCode)
    {
        return (null == cfgDefinition) ? null : String.valueOf((null == cfgDefinition.get(exCode)) ? ""
            : cfgDefinition.get(exCode));
    }
    
    /**
     * 根据个人的异常码获取对应的企业的异常码
     * @param exCode 异常码
     * @return 异常描述信息
     * @see [类、类#方法、类#成员]
     */
    public int getMappingExceptionCode(int exCode)
    {
        return Integer.valueOf(String.valueOf((null == mappercfgDefinition) ? -1
            : (null == mappercfgDefinition.get(String.valueOf(exCode))) ? -1
                : mappercfgDefinition.get(String.valueOf(exCode))).trim());
    }
}
