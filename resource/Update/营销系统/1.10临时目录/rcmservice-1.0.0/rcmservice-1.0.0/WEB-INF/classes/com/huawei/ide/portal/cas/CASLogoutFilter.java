package com.huawei.ide.portal.cas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 统一portal退出过滤器。 处理推荐系统的退出操作，将退出请求转发到统一portal的退出接口
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月7日]
 * @see  [相关类/方法]
 */
public class CASLogoutFilter implements Filter
{
    private static final String CAS_LOG_OUT_URL_KEY = "casLogoutUrl";
    
    private String casLogoutUrl;
    
    /**
     * doFilter
     * 
     * @param servletRequest
     *            servletRequest
     * @param servletResponse
     *            servletResponse
     * @param filterChain
     *            filterChain
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        if (!(servletResponse instanceof HttpServletResponse))
        {
            return;
        }
        ((HttpServletResponse)servletResponse).sendRedirect(casLogoutUrl);
    }
    
    /**
     * init
     * 
     * @param config
     *            FilterConfig
     * @throws ServletException
     *             ServletException
     */
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        casLogoutUrl = config.getInitParameter(CAS_LOG_OUT_URL_KEY);
    }
    
    /**
     * destroy
     */
    @Override
    public void destroy()
    {
        
    }
}
