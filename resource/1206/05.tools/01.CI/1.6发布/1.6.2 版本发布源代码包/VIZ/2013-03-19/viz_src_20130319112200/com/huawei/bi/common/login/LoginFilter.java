/*
 * 文 件 名:  LoginFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-12
 */
package com.huawei.bi.common.login;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-12]
 * @see  [相关类/方法]
 */
public class LoginFilter implements Filter
{

    /**
     * 
     */
    @Override
    public void destroy()
    {
        
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse rsp,
            FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        
        HttpServletResponse response = (HttpServletResponse) rsp;
        HttpSession session = request.getSession();
        String goPage = request.getRequestURI();
        String user = (String)session.getAttribute("appUser");
        session.setAttribute("gopage", goPage);
        if(StringUtils.isBlank(user))
        {
            request.getRequestDispatcher("/ui/loginServlet").forward(request, response);
        }
        else
        {
            chain.doFilter(req, rsp);
        }
    }

    /**
     * @param arg0
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException
    {
        
    }
    

}
