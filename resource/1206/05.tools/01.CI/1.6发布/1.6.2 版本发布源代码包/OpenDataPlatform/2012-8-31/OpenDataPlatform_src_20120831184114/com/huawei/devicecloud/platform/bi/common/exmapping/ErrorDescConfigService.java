/*
 * 文 件 名:  ErrorDescConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */
package com.huawei.devicecloud.platform.bi.common.exmapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 加载系统配置的异常码/异常描述信息
 * 并提供工具接口进行异常描述信息查询的服务实现
 * 
 * @author  q00107831
 * @version [Open Data Platform Service, 2011-8-10]
 * @see  [相关类/方法]
 */
public class ErrorDescConfigService
{
    /**
     * 存储资源文件中配置的异常码及异常描述信息
     */
    private static Properties cfgDefinition = new Properties();
    
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH = ErrorDescConfigService.class.getResource("/").getFile()
        + "../conf/common/resource/errorinfo/exception_definition.properties";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorDescConfigService.class);
    /** 
     * 初始化入口，加载资源文件
     * @throws IOException 
     * @see [类、类#方法、类#成员]
     */
    public void initErrorDefInfo() throws IOException
    {
        BufferedReader confPath = new BufferedReader(new FileReader(new File(CONFIG_FILE_PATH)));
        try
        {
            cfgDefinition.load(confPath);
        }
        catch (Exception e)
        {
            //TODO记录日志
            LOGGER.error("Get configuration file path or mapper configuration file path failed.",e);
            //e.printStackTrace();
        }
        finally
        {
            confPath.close();
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
}
