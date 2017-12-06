package com.huawei.bi.task.bean;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.huawei.bi.util.Config;

public class TaskTimerListner implements ServletContextListener
{
    private static final long OUTDATE_MILLSEC = Long.parseLong(Config.get("mem.outdate.seconds")) * 1000;
    
    /**
     * logger
     */
    private Timer timer;
    
    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
        if (timer != null)
        {
            timer.cancel();
            event.getServletContext().log("定时器已销毁");
        }
    }
    
    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        timer = new Timer(true);
        event.getServletContext().log("定时器已启动");
        timer.schedule(new ExecutionDeleter(event.getServletContext()), 0, OUTDATE_MILLSEC);
        event.getServletContext().log("已添加任务");
    }
    
}
