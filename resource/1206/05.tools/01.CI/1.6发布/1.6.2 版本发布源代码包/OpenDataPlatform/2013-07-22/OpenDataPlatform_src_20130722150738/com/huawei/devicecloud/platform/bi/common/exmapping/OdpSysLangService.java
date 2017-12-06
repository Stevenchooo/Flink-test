/*
 * 文 件 名:  OdpSysLangService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-23
 */
package com.huawei.devicecloud.platform.bi.common.exmapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载系统语言配置项
 * 并提供工具接口进行系统配置信息查询的服务实现
 * 
 * @author  l00166278
 * @version [Open Data Platform Service, 2011-8-23]
 * @see  [相关类/方法]
 */
public class OdpSysLangService
{
    private static final int SMALL_SIZE = 5;
    
    /**
     * 存储资源文件中配置信息
     */
    private static Properties cfgDefinition = new Properties();
    /**
     * 系统支持的语言类型，如<LANG1,en_US>
     */
    private static final Map<String,Object> SYSLANGMAP = new TreeMap<String,Object>();
    /**
     * 系统语言列表
     */
    private static final List<Object> LANGNAMELIST = new ArrayList<Object>(SMALL_SIZE);
     
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH = OdpSysLangService.class.getResource("/").getFile()
        + "../conf/common/resource/systemconfig/common.i18n.config.properties";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpSysLangService.class);
    
    /**
     * 系统默认语言
     */
    private String defaultLang;
    /**
     * 系统支持的语言数
     */
    private int langSize;
   
    /** 
     * 初始化入口，加载资源文件
     * @throws IOException 
     * @see [类、类#方法、类#成员]
     */
    public void initSysConfigDefInfo() throws IOException
    {
        BufferedReader confPath = new BufferedReader(new FileReader(new File(CONFIG_FILE_PATH)));
        try
        {
            cfgDefinition.load(confPath);
        }
        catch (Exception e)
        {
            LOGGER.error("Get configuration file path failed.",e);
            //e.printStackTrace();
        }
        finally 
        {
            confPath.close();
        }
        String langsize = (String)((null == cfgDefinition) ? null 
            : (null == cfgDefinition.get("support.language.size")) ? null 
                : cfgDefinition.get("support.language.size"));
        
        if (null != langsize)
        {
            try
            {
                langSize = Integer.valueOf(langsize);
            }
            catch (Exception e)
            {
                langSize = 0;
            }
        }
        else
        {
            langSize = 0;
        }
        
        
        defaultLang = (String)((null == cfgDefinition) ? null 
            : (null == cfgDefinition.get("DEFAULT")) ? null 
                : cfgDefinition.get("DEFAULT"));
        
        for (int i = 1; i < langSize + 1; i++)
        {
            SYSLANGMAP.put("LANG" + i, cfgDefinition.get("LANG" + i));
            LANGNAMELIST.add(cfgDefinition.get("LANG" + i));
        }
    }
    /** 
     * 获取系统默认语言Locale信息
     * @return 系统默认语言信息
     * @see [类、类#方法、类#成员]
     */
    public String getDefaultLangName()
    {
        return (String)SYSLANGMAP.get(defaultLang);
    }
    /**
     * 根据用户语言代码获取本地化信息
     * @param langCode 语言代码
     * @return 语言地区信息
     * @see [类、类#方法、类#成员]
     */
    public String getLocaleByLangCode(final String langCode)
    {
        for (Map.Entry<String, Object> entry : SYSLANGMAP.entrySet())
        {
            if (((String)entry.getValue()).startsWith(langCode + '_'))
            {
                return (String)entry.getValue();
            }
        }
        
        return getDefaultLangName();
    }

    public static List<Object> getLangnamelist()
    {
        return LANGNAMELIST;
    }
}
