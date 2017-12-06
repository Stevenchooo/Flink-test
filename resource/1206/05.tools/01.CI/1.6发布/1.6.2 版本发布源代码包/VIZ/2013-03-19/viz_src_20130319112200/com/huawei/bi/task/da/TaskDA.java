package com.huawei.bi.task.da;

import org.apache.commons.dbutils.QueryRunner;

import com.huawei.bi.task.domain.Task;
import com.huawei.bi.util.DBUtil;

public class TaskDA {

	public void addTask(Task task) throws Exception {
		// TODO Auto-generated method stub

		String sqlInsert = "INSERT INTO task (taskId, taskName, taskType, options, owner, lastModTime) VALUES (?,?,?,?,?,?)";

		QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
		qr.update(sqlInsert, task.getTaskId(), task.getTaskName(),
				task.getTaskType(), task.getOptions(), task.getOwner(),
				task.getLastModTime());
	}

}
