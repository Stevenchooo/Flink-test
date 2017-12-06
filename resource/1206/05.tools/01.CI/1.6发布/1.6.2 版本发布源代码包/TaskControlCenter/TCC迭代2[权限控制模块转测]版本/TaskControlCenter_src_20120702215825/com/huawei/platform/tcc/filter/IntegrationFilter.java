/*
 * 文 件 名:  IntegrationFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2012-5-14
 */
package com.huawei.platform.tcc.filter;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.utils.MemCacheClientService;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2012-5-14]
 * @see  [相关类/方法]
 */
public class IntegrationFilter implements Filter
{

    /**
     * 认证过滤器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationFilter.class);
    
    private String defaultEncoding = "utf-8";
    

    @Override
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter doFilter...");
        }
        
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
        {
            throw new ServletException("Just only support HTTP requests!");
        }
        
        request.setCharacterEncoding(defaultEncoding);
        
        HttpServletRequest httpReq = (HttpServletRequest)request;
        
        String userName = httpReq.getParameter("userName");
        
        String token = httpReq.getParameter("token");
        
        //会话不存在，则创建会话
        HttpSession session = httpReq.getSession(true);
        
        Boolean flag = false;
        if (null != userName && null != token)
        {
            try
            {
                MemCacheClientService memcaClientcheService =
                    (MemCacheClientService)CommonUtils.getBeanByID("memcaClientcheService");
                String value = memcaClientcheService.getAuthentication(token);
                if (userName.equals(value))
                {
                    session.setAttribute("userName", value);
                    memcaClientcheService.deleteAuthenticationInfo(token);
                    LOGGER.debug("check token successfully!");
                    flag = true;
                }
            }
            catch (Exception e)
            {
                LOGGER.error("use memcache get token error! Exception is {}", new Object[] {e});
                throw new IOException(e);
            }
           
        }
        else
        {
            Object user = session.getAttribute("userName");
            if (null != user)
            {
                LOGGER.debug("cannot check token but session exist userName, so enter!");
                flag = true;
            }
        }
        if (flag)
        {
            chain.doFilter(request, response);
        }
        else
        {
            String url = TccConfig.getLoginUrl();
            HttpServletResponse httpRsp = (HttpServletResponse)response;
            httpRsp.sendRedirect(url);
            return;
        }
    }

    @Override
    public void destroy()
    {
        defaultEncoding = null;
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("destroy...");
        }
    }
    
}
