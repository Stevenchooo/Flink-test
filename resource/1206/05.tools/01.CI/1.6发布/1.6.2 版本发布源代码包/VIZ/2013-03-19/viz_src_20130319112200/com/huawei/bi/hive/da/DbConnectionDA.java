package com.huawei.bi.hive.da;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.huawei.bi.hive.domain.DbConnection;
import com.huawei.bi.util.DBUtil;

public class DbConnectionDA {

	public DbConnection getDbConnection(int connId) throws Exception {
		String sqlQuery = "select * from dbconnection where connId=?";
		QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
		return qr.query(sqlQuery, new BeanHandler<DbConnection>(
				DbConnection.class), connId);
	}

}
