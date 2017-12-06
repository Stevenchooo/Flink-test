/*
 * 文 件 名:  ParallelCountor.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  并发查询记录数汇总
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-9
 */
package com.huawei.devicecloud.platform.bi.odp.parallel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;

/**
 * 并发查询记录数汇总
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-9]
 */
public class ParallelCountor
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelCountor.class);
    
    //查询记录总数的sql语句
    private String countSql;
    
    //执行countSql的数据库连接池
    private List<BasicDataSource> routeDataDSs;
    
    //最大记录数
    private Long maxRecordCount;
    
    /**
     * 认构造函数
     * @param countSql 统计sql语句
     * @param maxRecordCount 最大记录总数
     * @param routeDataDSs 表分布的数据源集合
     */
    public ParallelCountor(String countSql, Long maxRecordCount, List<BasicDataSource> routeDataDSs)
    {
        this.countSql = countSql;
        this.maxRecordCount = maxRecordCount;
        this.routeDataDSs = routeDataDSs;
    }
    
    /**
     * 并行查询汇总
     * @param transactionId 用于跟踪一次请求
     * @param timestamp 时间戳
     * @return 记录总数
     * @throws CException 异常
     */
    public long parallelCount(String transactionId, String timestamp)
        throws CException
    {
        //不能超过最大记录总数
        if (null != maxRecordCount && maxRecordCount <= 0)
        {
            return 0L;
        }
        
        //参数不合法
        if (null == routeDataDSs || StringUtils.isEmpty(countSql))
        {
            throw new CException(ResultCode.SYSTEM_ERROR, "parameter error!");
        }
        
        List<CountSqlExector> exectorThds = new ArrayList<CountSqlExector>(routeDataDSs.size());
        CountSqlExector exector = null;
        //启动多个线程处理
        for (BasicDataSource dataDs : routeDataDSs)
        {
            //创建sql执行器，并发执行查询语句
            if (null != dataDs)
            {
                exector =
                    new CountSqlExector(countSql, dataDs, new TimeStatis(
                        String.format("[user_query(tid=%s,ts=%s)] step 1.X select the count from database[%s]!",
                            transactionId,
                            timestamp,
                            dataDs.getUrl())));
                //ThreadPoolUtil.submmitTask(exector);
                exectorThds.add(exector);
                //启动线程
                exector.start();
            }
        }
        
        //记录查询的服务器地址
        if (routeDataDSs.isEmpty())
        {
            LOGGER.error("query record count from database server[{}]", routeDataDSs);
        }
        else
        {
            LOGGER.info("query record count from database server[{}]", routeDataDSs);
        }
        
        //初始化记录总数
        Long recordCount = 0L;
        try
        {
            //等待所有执行器执行完毕
            for (CountSqlExector exectorThd : exectorThds)
            {
                exectorThd.join();
            }
            
            //将所有节点的记录汇总
            for (CountSqlExector exectorThd : exectorThds)
            {
                recordCount += exectorThd.getRecordCount();
            }
            
            //超过最大值后只取最大值
            if (null != maxRecordCount && recordCount >= maxRecordCount)
            {
                recordCount = maxRecordCount;
            }
        }
        catch (InterruptedException e)
        {
            //有任何一个线程终止，均认为执行出错
            LOGGER.error("ParallelCountor interrupted!", e);
            //TODO 清理线程资源
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return recordCount;
    }
    
    /**
     * 字符串表示
     * @return 字符串表示
     */
    @Override
    public String toString()
    {
        final int maxLen = 10;
        StringBuilder builder = new StringBuilder();
        builder.append("ParallelCountor [countSql=");
        builder.append(countSql);
        builder.append(", routeDataDSs=");
        builder.append(routeDataDSs != null ? routeDataDSs.subList(0, Math.min(routeDataDSs.size(), maxLen)) : null);
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * 获取查询记录总数的sql
     * @return 查询记录总数的sql
     */
    public String getCountSql()
    {
        return countSql;
    }
    
    /**
     * 获取记录总数的sql的连接池列表
     * @return 执行记录总数的sql的连接池列表
     */
    public List<BasicDataSource> getRouteDataDSs()
    {
        return routeDataDSs;
    }
}