package com.huawei.bi.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.termcloud.uniaccount.crypt.CryptUtil;

public class DBUtil
{
    private static Logger log = LoggerFactory.getLogger(DBUtil.class);
    
    private static DataSource dataSource;
    static
    {
        Properties p = new Properties();
        p.setProperty("driverClassName", Config.get("jdbc.driver"));
        p.setProperty("url", Config.get("jdbc.url"));
        p.setProperty("username", Config.get("jdbc.user"));
        p.setProperty("password",
                CryptUtil.decryptForAESStr(Config.get("jdbc.pwd"),
                        Constant.KET_WORD));
        
        try
        {
            dataSource = BasicDataSourceFactory.createDataSource(p);
        }
        catch (Exception e)
        {
            log.error("Create dataSource error.", e);
        }
    }
    
    public static Connection getConnection() throws ClassNotFoundException,
            SQLException
    {
        return dataSource.getConnection();
    }
    
    /**
     * close safely
     * 
     * @param rs
     * @param stat
     * @param conn
     */
    public static void close(ResultSet rs, Statement stat, Connection conn)
    {
        
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                log.error("Close resultSet error.", e);
            }
        }
        
        if (stat != null)
        {
            try
            {
                stat.close();
            }
            catch (SQLException e)
            {
                log.error("Close statement error.", e);
            }
        }
        
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                log.error("Close connection error.", e);
            }
        }
        
    }
    
    public static java.sql.Date toSqlDate(java.util.Date date)
    {
        return new java.sql.Date(date.getTime());
    }
    
    public static DataSource getDataSource() throws Exception
    {
        return dataSource;
    }
}
