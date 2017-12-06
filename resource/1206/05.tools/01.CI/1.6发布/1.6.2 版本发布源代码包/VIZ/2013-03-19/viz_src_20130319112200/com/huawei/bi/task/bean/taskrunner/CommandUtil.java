package com.huawei.bi.task.bean.taskrunner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;
import com.huawei.bi.util.Config;

public class CommandUtil
{
    private static Logger log = LoggerFactory.getLogger(CommandUtil.class);
    
    /**
     * hive命令路径
     */
    private static String hiveCommand = Config.get("app.hive.command");
    
    public static void run(Task task, String code, String exeId, IExeLogger exeLogger)
    {
        
        try
        {
            if (StringUtils.isBlank(hiveCommand))
            {
                hiveCommand = "hive";
            }
            //只支持远程登录
            String command = hiveCommand + " -e \"" + code + "\"";
            RemoteCommandExecuteUtil.run(task,exeId, command, exeLogger);
        }
        catch (Exception e)
        {
            log.error("Command execute exception.", e);
        }
    }
    
}
