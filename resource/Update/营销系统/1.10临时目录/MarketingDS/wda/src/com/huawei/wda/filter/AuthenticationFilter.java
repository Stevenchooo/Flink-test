/*
 * 文 件 名:  AuthenticationFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-1-14
 */
package com.huawei.wda.filter;

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

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;

import com.huawei.util.CookieUtil;
import com.huawei.util.EncryptUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.facade.run.IMethodAide;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-1-14]
 * @see [相关类/方法]
 */
public class AuthenticationFilter implements Filter
{
    /**
     * 认证过滤器
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    private String defaultEncoding = "utf-8";

    /**
     * 销毁
     */
    public void destroy()
    {
        defaultEncoding = null;
    }

    /**
     * 过滤器处理
     * 
     * @param request
     *            消息请求
     * @param sresponse
     *            消息返回
     * @param chain
     *            过滤链
     * @throws IOException
     *             IO异常
     * @throws ServletException
     *             servlet异常
     */
    public void doFilter(ServletRequest request, ServletResponse sresponse,
            FilterChain chain) throws IOException, ServletException
    {
        if (!(request instanceof HttpServletRequest)
                || !(sresponse instanceof HttpServletResponse))
        {
            throw new ServletException("Just only support HTTP[S] requests!");
        }

        HttpServletResponse response = (HttpServletResponse) sresponse;
        request.setCharacterEncoding(defaultEncoding);
        HttpServletRequest httpReq = (HttpServletRequest) request;

        // 将principal放到session中，以便能在异步上下文中获取到
        HttpSession session = httpReq.getSession(true);
        if (null == session.getAttribute("principal_name"))
        {
            final AttributePrincipal principal = (AttributePrincipal) (httpReq
                    .getUserPrincipal());
            session.setAttribute("principal_name",
                    EncryptUtil.configEncode(principal.getName()));
            // 重新登录
            passportInvalidate(httpReq);
        }

        chain.doFilter(request, response);
    }

    /**
     * <凭证校验>
     * 
     * @param httpReq
     *            请求消息
     * @see [类、类#方法、类#成员]
     */
    private void passportInvalidate(HttpServletRequest httpReq)
    {
        javax.servlet.http.Cookie cookie = CookieUtil.getCookie(
                httpReq.getCookies(), WAFConfig.getCookieHead()
                        + IMethodAide.AUTH);
        if (null != cookie)
        {
            cookie.setValue("");
        }
    }

    /**
     * 初始化
     * 
     * @param config
     *            过滤器配置
     * @throws ServletException
     *             servlet异常
     */
    public void init(FilterConfig config) throws ServletException
    {
        String encoding = config.getInitParameter("encoding");
        if (null != encoding)
        {
            LOGGER.debug("encoding is set on configuration file!");
            defaultEncoding = encoding;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("init...");
        }
    }
}
