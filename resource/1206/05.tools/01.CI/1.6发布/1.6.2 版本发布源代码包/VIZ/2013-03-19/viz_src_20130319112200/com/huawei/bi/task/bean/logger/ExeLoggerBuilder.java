package com.huawei.bi.task.bean.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.domain.Execution;

/**
 * 构建不同的日志记录工具
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-30]
 */
public class ExeLoggerBuilder
{
    private static Logger log = LoggerFactory.getLogger(ExeLoggerBuilder.class);
    
    public static IExeLogger buildExeLogger(String exeType, String exeId)
        throws Exception
    {
        log.debug("Construct execute logger buulder,exeType=" + exeType + ",exeId=" + exeId);
        IExeLogger exeLogger = null;
        
        if (Execution.EXE_TYPE_FRONTEND.equals(exeType))
        {
            exeLogger = new TailedLogger(exeId);
            log.debug("Construct TailedLogger.class success.");
        }
        else if (Execution.EXE_TYPE_BACKEND.equals(exeType))
        {
            exeLogger = new FileLogger(exeId);
            log.debug("Construct FileLogger.class success.");
        }
        else
        {
            exeLogger = new TailedFileLogger(exeId);
            log.debug("Construct TailedFileLogger.class success.");
        }
        
        return exeLogger;
    }
}
