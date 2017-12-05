/*
 * 文 件 名:  MvcInterceptor.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年11月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * MVC拦截器
 * @author  z00219375
 * @version  [版本号, 2015年11月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public class MvcInterceptor implements HandlerInterceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MvcInterceptor.class);
    
    /**
     * afterCompletion
     * @param arg0   arg0
     * @param arg1   arg1
     * @param arg2   arg2
     * @param arg3   arg3
     * @throws Exception  Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
        throws Exception
    {
        LOGGER.info("MvcInterceptor afterCompletion");
    }
    
    /**
     * 可以通过ModelAndView参数来改变显示的视图，或修改发往视图的方法
     * @param arg0  arg0
     * @param arg1  arg1
     * @param arg2  arg2
     * @param arg3  arg3
     * @throws Exception  Exception
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
        throws Exception
    {
        LOGGER.info("MvcInterceptor postHandle");
    }
    
    /**
     * preHandle
     * @param arg0  arg0
     * @param arg1  arg1
     * @param arg2   表示的是被拦截的请求的目标对象
     * @return   boolean  表示是否需要将当前的请求拦截下来.返回false，请求将被终止;返回true，请求会被继续执行
     * @throws Exception
     *         Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2)
        throws Exception
    {
        //arg0.getRequestDispatcher("/../..").forward(arg0, arg1);
        LOGGER.info("MvcInterceptor preHandle");
        return true;
    }
    
}
