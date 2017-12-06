package com.huawei.bi.task.bean.taskrunner;

public class TaskRunnerFactory {
    public static ITaskRunner buildTaskRunner(String taskType) throws Exception {

		String className = "com.huawei.bi.task.bean.taskrunner." + taskType;
		Object obj = Class.forName(className).newInstance();

		if (obj instanceof ITaskRunner) {
			return (ITaskRunner) obj;
		} else {
			throw new Exception("not supported for task type " + taskType);
		}
	}
}
