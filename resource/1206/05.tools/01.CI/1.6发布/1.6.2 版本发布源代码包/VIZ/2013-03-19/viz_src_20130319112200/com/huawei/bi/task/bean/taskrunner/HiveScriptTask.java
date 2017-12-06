package com.huawei.bi.task.bean.taskrunner;

import java.util.Map;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;

public class HiveScriptTask implements ITaskRunner
{
    @Override
    public void run(Task task, Map<String, String> optionMap, String exeId, IExeLogger exeLogger)
        throws Exception
    {
        CommandUtil.run(task, optionMap.get("preCode"), exeId, exeLogger);
    }
}
