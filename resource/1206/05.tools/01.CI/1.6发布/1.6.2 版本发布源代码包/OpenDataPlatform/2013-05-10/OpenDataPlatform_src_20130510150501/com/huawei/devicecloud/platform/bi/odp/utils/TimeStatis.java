/*
 * 文 件 名:  TimeStatis.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  执行时间统计类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-29
 */
package com.huawei.devicecloud.platform.bi.odp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行时间统计类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-29]
 */
public class TimeStatis
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeStatis.class);
    
    //开始时间
    private long start;
    
    //结束时间
    private long end;
    
    //时间差
    private long interval;
    
    //名字
    private String name;
    
    /**
     * 构造函数
     * @param name 名字
     */
    public TimeStatis(String name)
    {
        this.name = name;
    }
    
    /**
     * 开始计时
     */
    public void startTiming()
    {
        start = System.currentTimeMillis();
    }
    
    /**
     * 将运行时间记录到日志中
     */
    public void endTiming()
    {
        end = System.currentTimeMillis();
        //计算时间差
        interval = end - start;
        //记录信息
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(interval);
        sb.append("ms] ");
        sb.append(name);
        sb.append(" runs ");
        sb.append(interval);
        sb.append("ms {enter time:");
        sb.append(start);
        sb.append("; exit time:");
        sb.append(end);
        sb.append(";}");
        LOGGER.warn(sb.toString());
    }
}
