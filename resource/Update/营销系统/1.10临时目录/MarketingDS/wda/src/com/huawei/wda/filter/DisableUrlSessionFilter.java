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
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;

/**
 * 客户端在禁用Cookie的情况下，服务器默认会重定向url，将JSSESSIONID拼接在url的后面
 * 如：http:xx.yy.com;jssessionid=NNNNNNNNNNNNNNNNNN
 * 但是，这种方式存在安全隐患，可能会被发生会话劫持的危险，所以对于客户端禁用Cookie的情况，
 * 我们决定不支持url重定向（禁用Cookie的客户端无法使用本系统）
 * 
 * @author c00219980
 *
 */
public class DisableUrlSessionFilter implements Filter
{
    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    /**
     * session字段表示rongyao
     */
    private static final String HREFNAMR = "href_name";

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
            LOGGER.error("HttpServlet cast error!");
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURL().toString();

        HttpSession session = httpRequest.getSession(false);
        if (httpRequest.isRequestedSessionIdFromURL())
        {
            if (session != null)
            {
                session.invalidate();
            }

        }
        session = httpRequest.getSession(true);
        if (session.getAttribute(HREFNAMR) == null)
        {
            if (requestUri.indexOf("rongyao") >= 0)
            {
                session.setAttribute(HREFNAMR, "yes");
            }
            else
            {
                session.setAttribute(HREFNAMR, "no");
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
