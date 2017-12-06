/*
 * 文 件 名:  DbBean.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  数据库连接管理
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库连接管理
 * @author  z00190554
 * @version [Internet Business Service Platform SP V100R100, 2013-2-5]
 */
public class DbBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DbBean.class);
    
    private Connection conn = null;
    
    /**
     * 选取要连接的数据源
     * @param src 是否是源数据库
     * @return 连接
     */
    public Connection getConnection(boolean src)
    {
        ConnInfo connInfo = new ConnInfo();
        connInfo.getConnInfo();
        
        try
        {
            Class.forName(connInfo.getDriver());
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error("mysql driver load error.", e);
        }
        
        try
        {
            if (src)
            {
                conn =
                    (Connection)DriverManager.getConnection(connInfo.getSourceUrl(),
                        connInfo.getSourceUser(),
                        connInfo.getSourcePw());
            }
            else
            {
                conn =
                    (Connection)DriverManager.getConnection(connInfo.getDestUrl(),
                        connInfo.getDestUser(),
                        connInfo.getDestPw());
            }
        }
        catch (SQLException e)
        {
            LOGGER.error("mysql connection init error.", e);
        }
        
        return conn;
    }
    
    /**
     * 关闭所有连接
     * @param rs 结构集合
     * @param stat 语句
     * @param conn 连接
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
                LOGGER.error("Close resultSet error.", e);
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
                LOGGER.error("Close statement error.", e);
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
                LOGGER.error("Close connection error.", e);
            }
        }
        LOGGER.debug("Close connection successfully.");
    }
    
    /**
     * 关闭rs、stat
     * @param rs 结果集
     * @param stat 语句
     */
    public static void close(ResultSet rs, Statement stat)
    {
        
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                LOGGER.error("Close resultSet error.", e);
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
                LOGGER.error("Close statement error.", e);
            }
        }
    }
    
    /**
     * 关闭语句
     * @param stat 语句
     */
    public static void close(Statement stat)
    {
        if (stat != null)
        {
            try
            {
                stat.close();
            }
            catch (SQLException e)
            {
                LOGGER.error("close statement error.", e);
            }
        }
    }
    
    /**
     * 关闭连接
     * @param conn 连接
     */
    public static void close(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                LOGGER.error("Close statement error.", e);
            }
        }
    }
}
