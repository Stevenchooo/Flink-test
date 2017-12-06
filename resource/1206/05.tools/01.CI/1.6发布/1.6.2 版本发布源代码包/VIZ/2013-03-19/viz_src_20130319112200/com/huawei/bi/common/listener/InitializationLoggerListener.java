/*
 * 文 件 名:  InitializationLoggerListener.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-12
 */
package com.huawei.bi.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;



/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-12]
 * @see  [相关类/方法]
 */
public class InitializationLoggerListener implements ServletContextListener
{
    /**
     * logger
     */
    private static Logger log = LoggerFactory.getLogger(InitializationLoggerListener.class);
    public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";
    
    /**
     * @param arg0
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        
    }
    
    /**
     * @param arg0
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        String location = servletContextEvent.getServletContext()
                .getInitParameter("log4jConfigLocation");
        if (location != null)
        {
            initLog(location);
        }
    }
    /**
     * 初始化logback日志文件配置 <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    public void initLog(String location)
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory(); // 得到当前应用中logger上下文
        try
        {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(InitializationLoggerListener.class.getResourceAsStream("/" + location));
        }
        catch (JoranException je)
        {
            log.error("JoranException occur at:" + je.getMessage());
            je.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
        
    }
}
