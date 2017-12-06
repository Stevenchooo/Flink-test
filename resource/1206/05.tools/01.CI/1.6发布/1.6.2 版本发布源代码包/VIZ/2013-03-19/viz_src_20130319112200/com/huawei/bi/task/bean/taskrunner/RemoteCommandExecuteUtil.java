/*
 * 文 件 名:  RemoteCommandExecute.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-29
 */
package com.huawei.bi.task.bean.taskrunner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-29]
 */
public class RemoteCommandExecuteUtil
{
    private static Logger log = LoggerFactory.getLogger(RemoteCommandExecuteUtil.class);
    
    private static final Map<String, RemoteCommandExecute> EXE_REMOTES = new HashMap<String, RemoteCommandExecute>();
    
    public static void run(Task task, String exeId, String command, IExeLogger exeLogger)
    {
        RemoteCommandExecute rcE = new RemoteCommandExecute(exeLogger);
        try
        {
            EXE_REMOTES.put(exeId, rcE);
            rcE.start(task.getOwner(), command);
            EXE_REMOTES.remove(exeId);
        }
        catch (Exception e)
        {
            log.error("run execution[id={}] failed!", exeId, e);
        }
    }
    
    /**
     * 停止正在执行的命令
     * @param exeId 执行Id
     */
    public static void stop(String exeId)
    {
        RemoteCommandExecute rcE = EXE_REMOTES.get(exeId);
        if (null != rcE)
        {
            rcE.stop();
            EXE_REMOTES.remove(exeId);
        }
    }
}
