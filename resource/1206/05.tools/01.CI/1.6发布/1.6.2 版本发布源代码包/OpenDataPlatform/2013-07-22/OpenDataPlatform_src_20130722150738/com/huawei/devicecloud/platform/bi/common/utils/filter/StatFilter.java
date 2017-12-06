/*
 * 文 件 名:  StatFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-11-7
 */
package com.huawei.devicecloud.platform.bi.common.utils.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <统计各个接口的调用时间>
 * 
 * @author  l00166278
 * @version [Open Data Platform Service, 2011-11-7]
 * @see  [相关类/方法]
 */
public class StatFilter implements Filter
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StatFilter.class);
    
    /**
     * 用于存放每个接口的每个线程的入口时间
     */
    private static ThreadLocal<Long> local = new ThreadLocal<Long>();
    
    /**
     * 处理请求
     * @param request 请求
     * @param response 响应
     * @param chain 过滤链
     * @throws IOException IO异常
     * @throws ServletException servele异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        long start = System.currentTimeMillis();
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
        {
            throw new ServletException("StatFilter just supports HTTP requests");
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        local.set(start);
        LOGGER.info("start:" + start);
        chain.doFilter(httpRequest, httpResponse);
    }
    
    /**
     * 统计接口执行的时间
     * @param interfacename 接口名
     */
    public static void timeStat(String interfacename)
    {
        long curtime = System.currentTimeMillis();
        long interval = curtime - local.get();
        //记录信息
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(interval);
        sb.append("ms] ");
        sb.append(interfacename);
        sb.append(" runs ");
        sb.append(interval);
        sb.append("ms {enter time:");
        sb.append(local.get());
        sb.append("; exit time:");
        sb.append(curtime);
        sb.append(";}");
        LOGGER.warn(sb.toString());
        local.remove();
    }
    
    @Override
    public void destroy()
    {
        
    }
    
    @Override
    public void init(FilterConfig arg0)
        throws ServletException
    {
    }
}
