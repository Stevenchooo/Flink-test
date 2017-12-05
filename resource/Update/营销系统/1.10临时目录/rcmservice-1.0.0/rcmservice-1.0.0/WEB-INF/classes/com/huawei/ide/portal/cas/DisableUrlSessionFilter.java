package com.huawei.ide.portal.cas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统一portal 禁止url重定向
 * 
 * 
 */
public class DisableUrlSessionFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DisableUrlSessionFilter.class);
    
    /**
     * doFilter
     * 
     * @param request
     *            servletRequest
     * @param response
     *            servletResponse
     * @param chain
     *            filterChain
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        if (!(request instanceof HttpServletRequest)
                || !(response instanceof HttpServletResponse))
        {
            LOGGER.error("doFilter failed: HttpServlet cast error!");
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.isRequestedSessionIdFromURL())
        {
            HttpSession session = httpRequest.getSession();
            if (session != null)
            {
                session.invalidate();
            }
        }
        // 不对url进行重定向，以免将jssessionid拼接至url后面
        HttpServletResponseWrapper wrappedResponse = new NoRedirectResponseWrapper(
                httpResponse);
        chain.doFilter(request, wrappedResponse);
    }
    
    /**
     * 初始化函数
     * 
     * @param config
     *            FilterConfig
     * @throws ServletException
     *             ServletException
     */
    public void init(FilterConfig config) throws ServletException
    {
    }
    
    /**
     * destroy
     */
    public void destroy()
    {
    }
    
    /**
     * HttpServletResponse包装类，此类屏蔽了http重定向
     */
    public static class NoRedirectResponseWrapper
            extends HttpServletResponseWrapper
    {
        /**
         * 构造函数
         * 
         * @param response
         *            HttpServletResponse
         */
        public NoRedirectResponseWrapper(HttpServletResponse response)
        {
            super(response);
        }
        
        /**
         * encodeRedirectUrl
         * 
         * @param url
         *            String
         * @return String
         */
        @Override
        public String encodeRedirectUrl(String url)
        {
            return url;
        }
        
        /**
         * encodeRedirectURL
         * 
         * @param url
         *            String
         * @return String
         */
        public String encodeRedirectURL(String url)
        {
            return url;
        }
        
        /**
         * encodeUrl
         * 
         * @param url
         *            String
         * @return String
         */
        public String encodeUrl(String url)
        {
            return url;
        }
        
        /**
         * encodeURL
         * 
         * @param url
         *            String
         * @return String
         */
        public String encodeURL(String url)
        {
            return url;
        }
    };
}
