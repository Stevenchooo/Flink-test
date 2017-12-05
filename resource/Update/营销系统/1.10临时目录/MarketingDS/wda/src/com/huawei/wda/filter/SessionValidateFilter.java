/*
 * 文 件 名:  SessionValidateFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  w00357590
 * 修改时间:  2015年10月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.wda.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.wda.util.StringUtils;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00357590
 * @version [版本号, 2015年10月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SessionValidateFilter implements Filter
{

    private static final Logger LOGGER = LogUtil.getInstance();

    private String[] excludePath =
    {"/page/login"};

    private ServletContext context;

    /**
     * 认证过滤器
     * 
     * @param config
     *            config
     * @throws ServletException
     *             ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException
    {
        context = config.getServletContext();
    }

    /**
     * 认证过滤器
     * 
     */
    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    /**
     * 认证过滤器
     * 
     * @param servletRequest
     *            servletRequest
     * @param servletResponse
     *            servletResponse
     * @param chain
     *            chain
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain chain)
                throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest)
                || !(servletResponse instanceof HttpServletResponse))
        {
            throw new ServletException("Just only support HTTP[S] requests!");
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURL().toString();
        String contextPath = context.getContextPath();

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            if (session.getAttribute("href_name") == null)
            {
                if (requestUri.indexOf("rongyao") >= 0)
                {
                    session.setAttribute("href_name", "yes");
                }
                else
                {
                    session.setAttribute("href_name", "no");
                }
            }

            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        for (String path : excludePath)
        {
            String p = contextPath + path;
            if (requestUri.endsWith(p))
            {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // ajax异步请求session超时的处理
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with")
                        .equalsIgnoreCase("XMLHttpRequest"))
        {
            // response.sendError(HttpServletResponse.SC_FORBIDDEN, "登录超时或无效！");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            LOGGER.warn("[for ajax] session is expire!");
        }
        else
        {
            response.sendRedirect(
                    StringUtils.getConfigInfo("webRoot") + "/page/login");
            LOGGER.warn("[for servlet] session is expire!");
        }
    }
}
