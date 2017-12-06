package com.huawei.bi.task.bean.taskrunner;

import java.util.Map;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;

public class Hive2FileTask implements ITaskRunner {

	@Override
	public void run(Task task, Map<String, String> optionMap, String exeId,
			IExeLogger exeLogger) throws Exception {
		String sourceTable = optionMap.get("sourceTable");
		String targetDir = optionMap.get("targetDir");

		String sql = "INSERT OVERWRITE LOCAL DIRECTORY " + targetDir
				+ " SELECT * FROM " + sourceTable;
		CommandUtil.run(task,sql, exeId, exeLogger);
	}

}
