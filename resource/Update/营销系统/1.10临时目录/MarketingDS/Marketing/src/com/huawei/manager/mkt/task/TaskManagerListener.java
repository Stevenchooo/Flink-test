/*
 * 文 件 名:  TaskManager.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-10
 */
package com.huawei.manager.mkt.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-10]
 * @see  [相关类/方法]
 */
public class TaskManagerListener implements ServletContextListener
{
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 每天的毫秒数
     */
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    
    /**
     * 定时器
     */
    private Timer timer;
    
    /**
     * 在Web应用启动时初始化任务
     * @param event    事件
     */
    public void contextInitialized(ServletContextEvent event)
    {
        
        try
        {
            LOG.debug("TaskManagerListener contextInitialized begin");
            
            //定义定时器
            timer = new Timer("delete system temp file", true);
            
            // 规定的每天时间00:00:30运行
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '00:00:00'");
            
            // 首次运行时间
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            
            startTime = new Date(startTime.getTime() + PERIOD_DAY);
            
            //启动定时删除文件任务
            timer.schedule(new DelTempFileTask(), startTime, PERIOD_DAY);
            
        }
        catch (ParseException e)
        {
            LOG.error("TaskManager error! contextInitialized error! exception is ParseException");
        }
        
    }
    
    /**
     * 在Web应用结束时停止任务
     * @param event     事件
     */
    public void contextDestroyed(ServletContextEvent event)
    {
        //timer.cancel(); // 定时器销毁
    }
}
