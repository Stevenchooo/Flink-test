package com.huawei.bi.task.bean.taskrunner;

import java.util.Map;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;

public interface ITaskRunner {
	void run(Task task, Map<String, String> optionMap, String exeId,
			IExeLogger exeLogger) throws Exception;
}
