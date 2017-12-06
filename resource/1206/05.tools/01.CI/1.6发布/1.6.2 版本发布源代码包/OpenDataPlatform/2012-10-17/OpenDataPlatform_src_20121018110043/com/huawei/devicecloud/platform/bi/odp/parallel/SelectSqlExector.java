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

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.process.IResultSetProcess;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;

/**
 * 查询记录的sql执行器
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-9]
 */
public class SelectSqlExector extends Thread
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectSqlExector.class);
    
    //时间统计
    private TimeStatis ts;
    
    //数据源
    private BasicDataSource ds;
    
    //执行sql
    private String selectSql;
    
    //是否执行成功
    private boolean successed = false;
    
    //sql执行时异常
    private Throwable exception;
    
    //记录总数
    private Long count = 0L;
    
    //结果集处理接口
    private IResultSetProcess process;
    
    /**
     * 构造函数
     * @param selectSql 查询记录的sql语句
     * @param process 结果集处理接口
     * @param ds 数据源
     * @param ts 时间统计对象
     */
    public SelectSqlExector(String selectSql, BasicDataSource ds, IResultSetProcess process, TimeStatis ts)
    {
        this.selectSql = selectSql;
        this.ds = ds;
        this.process = process;
        this.ts = ts;
    }
    
    /**
     * 检查是否sql是否执行成功
     * @throws CException 执行失败异常
     */
    public void checkSucess()
        throws CException
    {
        //不成功抛出异常
        if (!this.successed)
        {
            throw new CException(ResultCode.SQL_EXEC_ERROR, new Object[] {this.selectSql, exception.getMessage()});
        }
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
            
            this.count = execSelectSql(selectSql);
        }
        catch (Exception e)
        {
            this.successed = false;
            LOGGER.error("run failed! countSql is {}", selectSql, e);
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
     * @param sql 查询记录的sql语句
     * @return 记录总数
     */
    private Long execSelectSql(String sql)
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
            
            long recordCount = 0L;
            //如果获取到接口
            while (rs.next())
            {
                recordCount++;
                if (null != process)
                {
                    //如果处理结束
                    if (process.processResultSet(rs))
                    {
                        break;
                    }
                }
            }
            
            //执行成功
            this.successed = true;
            return recordCount;
        }
        catch (Throwable e)
        {
            //执行失败
            LOGGER.error("execSelectSql failed! countSql is {}", sql, e);
            this.exception = e;
        }
        finally
        {
            //关闭流
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
        return 0L;
    }
    
    /**
     * 获取数据源
     * @return 数据源
     */
    public BasicDataSource getDs()
    {
        return ds;
    }
    
    /**
     * 设置数据源
     * @param ds 数据源
     */
    public void setDs(BasicDataSource ds)
    {
        this.ds = ds;
    }
    
    /**
     * 设置执行成功标识
     * @param successed 成功标识
     */
    public void setSuccessed(boolean successed)
    {
        this.successed = successed;
    }
    
    /**
     * 获取sql执行异常
     * @return sql执行异常
     */
    public Throwable getException()
    {
        return exception;
    }
    
    /**
     * 是否执行成功
     * @return 是否执行成功
     */
    public boolean isSuccessed()
    {
        return successed;
    }
    
    /**
     * 设置异常
     * @param exception 异常
     */
    public void setException(Exception exception)
    {
        this.exception = exception;
    }
    
    /**
     * 设置count
     * @param count 记录总数
     */
    public void setCount(Long count)
    {
        this.count = count;
    }
    
    /**
     * 获取count
     * @return 记录总数
     */
    public Long getCount()
    {
        return count;
    }
}
