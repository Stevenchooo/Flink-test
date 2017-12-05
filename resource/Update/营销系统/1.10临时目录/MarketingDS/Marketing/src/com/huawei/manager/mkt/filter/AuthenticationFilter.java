/*
 * 文 件 名:  AuthenticationFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-1-14
 */
package com.huawei.manager.mkt.filter;

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
import org.slf4j.LoggerFactory;

import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.CookieUtil;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.facade.run.IMethodAide;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-1-14]
 * @see  [相关类/方法]
 */
public class AuthenticationFilter implements Filter
{
    /**
     * 认证过滤器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    private String defaultEncoding = "utf-8";
    
    /**
     * 销毁
     */
    public void destroy()
    {
        defaultEncoding = null;
    }
    
//    private String hideTitle = "";
//    
//    public String getHideTitle()
//    {
//        return hideTitle;
//    }
//
//    public void setHideTitle(String hideTitle)
//    {
//        this.hideTitle = hideTitle;
//    }

    /**
     * 过滤器处理
     * @param request       消息请求
     * @param responce      消息返回
     * @param chain         过滤链
     * @throws IOException      IO异常
     * @throws ServletException servlet异常
     */
    public void doFilter(ServletRequest request, ServletResponse responce, FilterChain chain)
        throws IOException, ServletException
    {
        if (!(request instanceof HttpServletRequest) || !(responce instanceof HttpServletResponse))
        {
            throw new ServletException("Just only support HTTP[S] requests!");
        }
        
        request.setCharacterEncoding(defaultEncoding);
        HttpServletRequest httpReq = (HttpServletRequest)request;
        
        String hideTitle = httpReq.getParameter("hideTitle");
        boolean hideTitleFlag = false;
        if(hideTitle != null && hideTitle.equals(StringUtils.getConfigInfo("hideTitle"))){
            httpReq.getSession().setAttribute("generalSituat", "generalSituat");
            hideTitleFlag = true;
        }
        
        //将principal放到session中，以便能在异步上下文中获取到
        if (principalChange(httpReq))
        {
            final AttributePrincipal principal = (AttributePrincipal)(httpReq.getUserPrincipal());
            HttpSession session = httpReq.getSession(true);
            session.setAttribute("principal", principal);
            //重新登录
            passportInvalidate(httpReq);
        }
        
        HttpServletResponse httpRsp = (HttpServletResponse)responce;
        if(hideTitleFlag){
            httpRsp.addHeader("x-frame-options", "ALLOW-FROM " + StringUtils.getConfigInfo("honorUrl"));
        }else{
            httpRsp.addHeader("x-frame-options", "SAMEORIGIN");
        }
        httpRsp.addHeader("Content-Security-Policy", "script-src 'unsafe-inline' 'self' 'unsafe-eval'");
        httpRsp.addHeader("X-Content-Type-Options", "nosniff");
        httpRsp.addHeader("X-XSS-Protection", "1");
        
        /*httpRsp.setHeader("X-Content-Type-Options", "nosniff");
        httpRsp.setHeader("X-XSS-Protection", "1");
        httpRsp.setHeader("Content-Security-Policy", "img-src 'self'");
        httpRsp.setHeader("X-Frame-Options", "DENY");*/
        
        chain.doFilter(request, responce);
    }
    
    /**
     * <凭证校验>
     * @param httpReq      请求消息
     * @see [类、类#方法、类#成员]
     */
    private void passportInvalidate(HttpServletRequest httpReq)
    {
        javax.servlet.http.Cookie cookie =
            CookieUtil.getCookie(httpReq.getCookies(), WAFConfig.getCookieHead() + IMethodAide.AUTH);
        if (null != cookie)
        {
            cookie.setValue("");
        }
    }
    
    /**
     * <凭证改变>
     * @param httpReq   请求消息
     * @return          是否成功
     * @see [类、类#方法、类#成员]
     */
    private boolean principalChange(final HttpServletRequest httpReq)
    {
        final AttributePrincipal principal = (AttributePrincipal)(httpReq.getUserPrincipal());
        //会话不存在，则创建会话
        HttpSession session = httpReq.getSession(true);
        final String curPrincipalName = (null == principal) ? null : principal.getName();
        if (null == curPrincipalName)
        {
            return false;
        }
        
        final String prePrincipalName = (String)session.getAttribute("principal_name");
        session.setAttribute("pre_principal_name", prePrincipalName);
        session.setAttribute("principal_name", curPrincipalName);
        return !equals(curPrincipalName, prePrincipalName);
    }
    
    /**
     * <判断字符串是否相等>
     * @param left   左
     * @param right  右
     * @return       是否相等
     * @see [类、类#方法、类#成员]
     */
    private boolean equals(String left, String right)
    {
        if (null == left && null != right || null != left && null == right)
        {
            return false;
        }
        
        if (null == left && null == right)
        {
            return true;
        }
        
        if (null != left)
        {
            return left.equals(right);
        }
        
        return false;
    }
    
    /**
     * 初始化
     * @param config        过滤器配置
     * @throws ServletException   servlet异常
     */
    public void init(FilterConfig config)
        throws ServletException
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
