package com.binlog;

import java.io.InputStream;
import java.sql.*;
import java.util.*;
 
 

public class DBUtil {

	 
	  

	public static TableInfo descTable(String tableName)
	{
		Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    TableInfo ti = new TableInfo(tableName );
		try {
			conn = C3PODataSource.getConnection();
			// 创建Statement对象
			 

		    stmt = conn.prepareStatement("desc " + tableName);

			rs= stmt.executeQuery();
		    while (rs.next()) {
		    	ti.addColumn(rs.getString("Field"),rs.getString("Type"));
				 
			}
			//stmt.setString(1, id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally 
	    {
	        closeAble(stmt);
	        closeAble(conn);
	    }
		
		return ti;
	}
	public static void udpateTable(String sql) {

	    Connection conn = null;
	    PreparedStatement stmt = null;
		try {
			conn = C3PODataSource.getConnection();
			// 创建Statement对象
			 

		    stmt = conn.prepareStatement(sql);
			//stmt.setString(1, id);

			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally 
	    {
	        closeAble(stmt);
	        closeAble(conn);
	    }

	}
	
	private static void closeAble(AutoCloseable autoCloseable) 
	{
	    if (autoCloseable != null) 
	    {
	        try
            {
                autoCloseable.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
	    }
	}

}
