package com.binlog;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3PODataSource {
	private static ComboPooledDataSource dataSource = null;
	private static final String driver = "com.mysql.jdbc.Driver";
 

	public static DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = new ComboPooledDataSource();
			try {
				dataSource.setDriverClass(driver);
			} catch (PropertyVetoException e) {
				//LogMe.println("DataSource Load Driver Exception!!" ); 
				e.printStackTrace();
			}
			dataSource.setJdbcUrl("jdbc:mysql://"+BinlogMain.slave_ip+":3306/"+BinlogMain.slave_dbname);
			dataSource.setUser(BinlogMain.slave_user );
			dataSource.setPassword( BinlogMain.slave_passwd);
			// 设置连接池最大连接容量
			dataSource.setMaxPoolSize(20);
			// 设置连接池最小连接容量
			dataSource.setMinPoolSize(2);
			// 设置连接池最大statements对象容量
			dataSource.setMaxStatements(100);
			
			//最大丢弃时间秒
			dataSource.setMaxIdleTime(3700);
			// 测连接有效的时间间隔 1小时 
			dataSource.setIdleConnectionTestPeriod(3600); 
			//每次连接验证连接是否可用  
			dataSource.setTestConnectionOnCheckout(true);  

		}
		return dataSource;
	}

	public static Connection getConnection() throws SQLException {
		return C3PODataSource.getDataSource().getConnection();
		 

	}
}