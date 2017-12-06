package com.huawei.bi.task.bean.taskrunner;

import java.util.Map;

import com.huawei.bi.hive.bean.JDBCClient;
import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;

public class JDBCScriptTask implements ITaskRunner {

	@Override
	public void run(Task task, Map<String, String> optionMap, String exeId,
			IExeLogger exeLogger) throws Exception {

		String code = optionMap.get("code");
		String driver = optionMap.get("driver");
		String url = optionMap.get("url");
		String user = optionMap.get("user");
		String password = optionMap.get("password");

		exeLogger.writeDebug("begin");
		JDBCClient jdbcClient = new JDBCClient(driver, url, user, password);
		String result = jdbcClient.excuteScriptSilently(code);
		exeLogger.writeDebug("finish successful");

		exeLogger.writeResult(result);
	}

}
