/*
 * 文 件 名:  OdpSysConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */
package com.huawei.devicecloud.platform.bi.common.exmapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 
 * 加载系统配置项
 * 并提供工具接口进行系统配置信息查询的服务实现
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class OdpSysConfigService
{
    /**
     * 存储资源文件中配置信息
     */
    private static Properties cfgDefinition = new Properties();
    
    /**
     * 列枚举值名字映射配置
     */
    private static Properties columnDefinition = new Properties();
    
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH = OdpSysConfigService.class.getResource("/").getFile()
        + "../conf/common/resource/systemconfig/sysconfig.properties";
    
    private static OdpSysConfigService sysconfigService;
    
    /**
     * 列枚举值名字映射路径
     */
    private static final String COLUMN_ENUM_NAME_PATH = OdpSysConfigService.class.getResource("/").getFile()
        + "../conf/common/resource/tablecolumn/columns_definition.properties";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpSysConfigService.class);
    
    private OdpSysConfigService()
    {
        
    }
    
    /**
     * 创建配置Service
     * @return 配置Service对象
     */
    public static OdpSysConfigService createConfigService()
    {
        if (null == sysconfigService)
        {
            OdpSysConfigService configService = new OdpSysConfigService();
            configService.initSysConfigDefInfo();
            sysconfigService = configService;
        }
        
        return sysconfigService;
    }
    
    /** 
     * 初始化入口，加载资源文件
     */
    public void initSysConfigDefInfo()
    {
        BufferedReader confPath = null;
        BufferedReader columnFilePath = null;
        try
        {
            confPath = new BufferedReader(new FileReader(new File(CONFIG_FILE_PATH)));
            columnFilePath = new BufferedReader(new FileReader(new File(COLUMN_ENUM_NAME_PATH)));
            
            cfgDefinition.load(confPath);
            columnDefinition.load(columnFilePath);
        }
        catch (Exception e)
        {
            //记录日志
            LOGGER.error("Get configuration file path or column configuration file path failed.", e);
        }
        finally
        {
            
            //关闭系统配置文件流
            OdpCommonUtils.close(confPath);
            
            //关闭列枚举值文件流
            OdpCommonUtils.close(columnFilePath);
        }
    }
    
    /**
     * 根据配置key值获取配置项信息
     * @param key 配置key值
     * @return 配置信息
     */
    public String getSysConfigKey(String key)
    {
        return (null == cfgDefinition) ? null : String.valueOf((null == cfgDefinition.get(key)) ? ""
            : cfgDefinition.get(key));
    }
    
    /**
     * 根据列枚举值获取列名
     * @param columnEnum 类枚举值
     * @return 字段名
     */
    public String getColumnName(int columnEnum)
    {
        if (null == columnDefinition)
        {
            return null;
        }
        Object obj = columnDefinition.get(Integer.toString(columnEnum));
        if (null != obj)
        {
            return obj.toString();
        }
        
        return null;
    }
}
