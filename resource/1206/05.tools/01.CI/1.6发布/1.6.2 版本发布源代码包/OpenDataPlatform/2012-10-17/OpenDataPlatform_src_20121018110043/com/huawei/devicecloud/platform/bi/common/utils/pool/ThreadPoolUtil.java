/*
 * 文 件 名:  ThreadPoolUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <线程池任务提交工具>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-23
 */
package com.huawei.devicecloud.platform.bi.common.utils.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;

/**
 * <线程池任务提交工具>
 * 
 * @author  l00166278
 * @version [Open Data Platform Service, 2011-8-23]
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
     * @throws CException 异常
     */
    public static void submmitTask(Runnable task)
        throws CException
    {
        try
        {
            TaskExecutor executor = (TaskExecutor)CommonUtils.getBeanByID("threadPoolService");
            executor.execute(task);
        }
        catch (Exception e)
        {
            LOGGER.error("Submmit new task failed,task type is {}", task.getClass(), e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
}
