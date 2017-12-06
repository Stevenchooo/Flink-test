/*
 * 文 件 名:  CountSqlExector.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  查询记录总数的sql执行器
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-9
 */
package com.huawei.devicecloud.platform.bi.odp.parallel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;

/**
 * 查询记录总数的sql执行器
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-9]
 */
public class CountSqlExector extends Thread
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CountSqlExector.class);
    
    //时间统计
    private TimeStatis ts;
    
    //数据源
    private BasicDataSource ds;
    
    //执行sql
    private String countSql;
    
    //是否执行成功
    private boolean successed = false;
    
    //sql执行时异常
    private Exception exception;
    
    //记录总数
    private Long count = 0L;
    
    /**
     * 构造函数
     * @param countSql 查询记录总数的sql语句
     * @param ds 数据源
     * @param ts 时间统计对象
     */
    public CountSqlExector(String countSql, BasicDataSource ds, TimeStatis ts)
    {
        this.countSql = countSql;
        this.ds = ds;
        this.ts = ts;
    }
    
    /**
     * 获取记录总数
     * @return 获取记录总数
     * @throws CException 执行失败异常
     */
    public Long getRecordCount()
        throws CException
    {
        //不成功抛出异常
        if (!this.successed)
        {
            throw new CException(ResultCode.SQL_EXEC_ERROR, new Object[] {this.countSql, exception.getMessage()});
        }
        
        return this.count;
    }
    
    public BasicDataSource getDs()
    {
        return ds;
    }
    
    public void setDs(BasicDataSource ds)
    {
        this.ds = ds;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CountSqlExector [ds=");
        builder.append(ds);
        builder.append(", countSql=");
        builder.append(countSql);
        builder.append(", successed=");
        builder.append(successed);
        builder.append(", exception=");
        builder.append(exception);
        builder.append(", count=");
        builder.append(count);
        builder.append("]");
        return builder.toString();
    }
    
    public String getCountSql()
    {
        return countSql;
    }
    
    public void setCountSql(String countSql)
    {
        this.countSql = countSql;
    }
    
    public boolean isSuccessed()
    {
        return successed;
    }
    
    public void setSuccessed(boolean successed)
    {
        this.successed = successed;
    }
    
    public Exception getException()
    {
        return exception;
    }
    
    public void setException(Exception exception)
    {
        this.exception = exception;
    }
    
    public Long getCount()
    {
        return count;
    }
    
    public void setCount(Long count)
    {
        this.count = count;
    }
    
    @Override
    public void run()
    {
        try
        {
            //开始计时
            if (null != ts)
            {
                ts.startTiming();
            }
            
            this.count = execCountSql(countSql);
        }
        catch (Exception e)
        {
            this.successed = false;
            LOGGER.error("run failed! countSql is {}", countSql, e);
            //保存异常
            exception = e;
        }
        
        //开始计时
        if (null != ts)
        {
            ts.endTiming();
        }
    }
    
    /**
     * 执行sql
     * @param sql 查询记录总数的sql语句
     * @return 记录总数
     */
    private Long execCountSql(final String sql)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //默认执行失败
        this.successed = false;
        
        try
        {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            //如果获取到接口
            if (rs.next())
            {
                //执行成功
                this.successed = true;
                return rs.getLong(1);
            }
        }
        catch (SQLException e)
        {
            //执行失败
            LOGGER.error("execCountSql failed! countSql is {}", sql, e);
            this.exception = e;
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
        return 0L;
    }
}
