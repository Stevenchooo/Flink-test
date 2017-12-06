/*
 * 文 件 名:  ResourceFormatterServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-21
 */
package com.huawei.platform.tcc.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 
 * 资源信息加载及格式化引擎服务
 * 
 * @author  q00107831
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-8-21]
 * @see  [相关类/方法]
 */
public class ResourceFormatterServiceImpl
{
    /**
     * 日志记录
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFormatterServiceImpl.class);
    
    /**
     * 模板引擎存储公共目录
     */
    private static final String TEMPLATES_PATH = ResourceFormatterServiceImpl.class.getResource("/").getFile()
        + "../conf/";
    
    /**
     * 模板引擎配置
     */
    private static final Configuration CFG = new Configuration();
    
    /**
     * 当前引擎实例使用的模板文件存储目录
     */
    private String templatesPath;
    
    /** 
     * 服务加载入口方法
     * @see [类、类#方法、类#成员]
     */
    public void initResourceEngine()
    {
        try
        {
            //加载freemarker模板文件   
            CFG.setDirectoryForTemplateLoading(new File(TEMPLATES_PATH + templatesPath));
            
            //设置对象包装器   
            CFG.setObjectWrapper(new DefaultObjectWrapper());
            
            //设计异常处理器   
            CFG.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            CFG.setDefaultEncoding("UTF-8");
        }
        catch (IOException e)
        {
            LOGGER.debug("初始化资源引擎出现IO异常。", e);
            // e.printStackTrace();
        }
    }
    
    /**
     * 获取指定模板的格式化信息
     * @param temlateFileName 模板文件名
     * @param data 数据源
     * @return 使用数据源将模板格式化后的信息
     * @see [类、类#方法、类#成员]
     */
    public String getFormattedResource(String temlateFileName, Map<String, Object> data)
    {
        try
        {
            //获取指定模板文件   
            Template template = CFG.getTemplate(temlateFileName);
            template.setEncoding("UTF-8");
            
            //格式化后输出的目的地
            Writer strWriter = new StringWriter();
            
            //格式化资源信息
            template.process(data, strWriter);
            
            return strWriter.toString();
        }
        catch (IOException e)
        {
            return null;
        }
        catch (TemplateException e)
        {
            return null;
        }
    }
    
    public String getTemplatesPath()
    {
        return templatesPath;
    }
    
    public void setTemplatesPath(String templatesPath)
    {
        this.templatesPath = templatesPath;
    }
}
