package com.huawei.bi.task.bean.taskrunner;

import java.util.Map;

import com.huawei.bi.hive.bean.HiveClientFactory;
import com.huawei.bi.hive.da.DbConnectionDA;
import com.huawei.bi.hive.domain.DbConnection;
import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;

public class DBScriptTask implements ITaskRunner {

	/**
	 * optionMap: connId code
	 */
	@Override
	public void run(Task task, Map<String, String> optionMap, String exeId,
			IExeLogger exeLogger) throws Exception {

//		int connId = Integer.parseInt(optionMap.get("connId"));
//		DbConnection dbConn = new DbConnectionDA().getDbConnection(connId);
//
//		if (HiveClientFactory.CONN_TYPE_JDBC.equals(dbConn.getConnType())) {
//			// append jdbc option
//			optionMap.put("driver", dbConn.getConnDriver());
//			optionMap.put("url", dbConn.getConnUrl());
//			optionMap.put("user", dbConn.getConnUser());
//			optionMap.put("password", dbConn.getConnPassword());
//			new JDBCScriptTask().run(task, optionMap, exeId, exeLogger);
//		} else if (HiveClientFactory.CONN_TYPE_THRIFT.equals(dbConn
//				.getConnType())) {
//			// code is enough
//			new HiveScriptTask().run(task, optionMap, exeId, exeLogger);
//		} else {
//			throw new Exception("type not supported");
//		}
	    new HiveScriptTask().run(task, optionMap, exeId, exeLogger); 
	}

}
