/*
 * 文 件 名:  LoginServlet.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-12
 */
package com.huawei.bi.common.login;


import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.util.Util;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-12]
 * @see  [相关类/方法]
 */
public class LoginServlet extends HttpServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(LoginServlet.class);
    /**
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String user = request.getParameter("loginName");
        log.debug("User [" +user + "] begin to login." + Util.httpRequestInfo(request, response));
        String pwd = request.getParameter("password");
        HttpSession session = request.getSession(true);
        String message = "";
        if (StringUtils.isBlank(user) || StringUtils.isBlank(pwd)
                || !UserCheck.check(user, pwd))
        {
            message = URLEncoder.encode("User name or password error.","UTF-8");
//            response.sendRedirect("/viz/login.jsp?denyMessage=" + message);
            request.setAttribute("denyMessage", message);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            log.debug("User [" +user + "] login failed.");
            return;
        }
        else
        {
            session.setAttribute("appUser", user);
            //request.getRequestDispatcher("/ui/index.jsp").forward(request,
            //        response);
			response.sendRedirect("/viz/ui/hive/index.jsp");
            log.debug("User [" +user + "] login success.");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        session.setAttribute("appUser", null);
        
        resp.sendRedirect("/viz/login.jsp");
    }
}
