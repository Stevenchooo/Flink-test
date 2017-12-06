package com.huawei.bi.task.bean;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.huawei.bi.task.da.MemDbExePersist;

public class ExecutionDeleter extends TimerTask
{
    private ServletContext servletContext;
    
    private static boolean isRunning = false;
    
    public ExecutionDeleter(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }
    
    @Override
    public void run()
    {
        if (!isRunning)
        {
            isRunning = true;
            //删除过期的执行信息
            MemDbExePersist.deleteOutdateExecution();
            isRunning = false;
        }
    }
    
}
