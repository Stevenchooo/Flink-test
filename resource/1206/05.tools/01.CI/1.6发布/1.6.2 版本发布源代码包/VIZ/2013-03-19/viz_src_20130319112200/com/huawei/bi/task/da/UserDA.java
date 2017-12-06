package com.huawei.bi.task.da;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.domain.User;
import com.huawei.bi.util.DBUtil;

public class UserDA
{
    private static final Logger LOG = LoggerFactory.getLogger(UserDA.class);
    
    /**
     * 获取用户信息
     * @param user 用户
     * @return 用户信息
     * @throws Exception 异常
     */
    public User getUser(String user)
        throws Exception
    {
        
        String sqlSelect = "select user,hiveDBs,privilege,maxRunningSqls,connId from user where user=?";
        
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        return qr.query(sqlSelect, new BeanHandler<User>(User.class), user);
    }
    
    /**
     * 认证用户名和密码是否匹配
     * @param user 用户
     * @param passwd 密码
     * @return 用户名和密码是否匹配
     * @throws Exception 异常
     */
    public boolean authen(String user, String passwd)
        throws Exception
    {
        
        String sqlSelect = "select count(*) where user=? and pwd = ?";
        
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        return (Integer)qr.query(sqlSelect, new ScalarHandler(), user, passwd) > 0;
    }
    
    /**
     * 获取用户同步过的未删除的表
     * @param user 用户
     * @return 表信息
     * @throws Exception 异常
     */
    public String getTab(String user)
        throws Exception
    {
        String sqlSelect = "select tabName from syncrecord where userName='" + user + "'";
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        String tabList = null;
        try
        {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next())
            {
                tabList = rs.getString("tabName");
                //               tabList.add(name);
            }
        }
        catch (Exception e)
        {
            LOG.error("error occured read " + user + "'s tables infomation:" + e);
            throw e;
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return tabList;
    }
    
    public boolean updateTabList(String user, String tablist)
        throws Exception, SQLException
    {
        boolean flag = true;
        if (StringUtils.isBlank(user) || StringUtils.isBlank(tablist))
        {
            return false;
        }
        Connection conn = DBUtil.getConnection();
        java.sql.PreparedStatement updateTabs = null;
        try
        {
            String updateString =
                "replace into syncrecord(userName,tabName) values ('" + user + "','" + tablist + "');";
            
            updateTabs = conn.prepareStatement(updateString);
            updateTabs.execute();
            
        }
        catch (Exception e)
        {
            LOG.error("error occured update table");
            throw e;
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        
        return flag;
    }
}
