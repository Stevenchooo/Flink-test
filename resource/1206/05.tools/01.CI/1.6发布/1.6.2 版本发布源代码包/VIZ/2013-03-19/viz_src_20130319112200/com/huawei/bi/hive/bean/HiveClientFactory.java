package com.huawei.bi.hive.bean;

import com.huawei.bi.hive.da.DbConnectionDA;
import com.huawei.bi.hive.domain.DbConnection;

public class HiveClientFactory {

	public static final String CONN_TYPE_THRIFT = "hive";
	public static final String CONN_TYPE_JDBC = "jdbc";

	public static IHiveClient getHiveClient(int connId) throws Exception {

		DbConnection dbConn = new DbConnectionDA().getDbConnection(connId);

		if (CONN_TYPE_THRIFT.equals(dbConn.getConnType())) {
			return new HiveThriftClient(dbConn.getConnUrl());
		} else if (CONN_TYPE_JDBC.equals(dbConn.getConnType())) {
			return new JDBCClient(dbConn.getConnDriver(), dbConn.getConnUrl(),
					dbConn.getConnUser(), dbConn.getConnPassword());
		}

		// error
		throw new Exception("not implemented yet");
	}
}
