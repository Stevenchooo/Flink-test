/*
 * 文 件 名:  ThreadPoolUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <线程池任务提交工具>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-23
 */
package com.huawei.platform.tcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.huawei.platform.common.CommonUtils;

/**
 * <线程池任务提交工具>
 * 
 * @author  l00166278
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-8-23]
 * @see  [相关类/方法]
 */
public final class ThreadPoolUtil
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolUtil.class);
    
    /**
     * 私有构造方法
     */
    private ThreadPoolUtil()
    {
        
    }
    
    /**
     * 提交新的处理任务
     * @param task 任务实例
     */
    public static void submmitTask(Runnable task)
    {
        try
        {
            TaskExecutor executor = (TaskExecutor)CommonUtils.getBeanByID("threadPoolService");
            executor.execute(task);
        }
        catch (Exception e)
        {
            LOGGER.error("Submmit new task failed,task type is {}",
                task.getClass(),e);
        }
    }
}
