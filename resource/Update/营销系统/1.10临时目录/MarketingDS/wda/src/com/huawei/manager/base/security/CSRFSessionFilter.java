package com.huawei.manager.base.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.huawei.manager.base.config.ConfigConstant;
import com.huawei.util.LogUtil;
import com.huawei.wda.util.StringUtils;

/**
 * 防止CSRF等安全攻击的过滤器
 * 
 * @author w00296102
 */
public class CSRFSessionFilter implements Filter
{
    /**
     * Content-Security-Policy 响应头
     */
    private static final String RESPONSE_HEADER_CSP = "Content-Security-Policy";

    /**
     * X-Frame-Options 响应头
     */
    private static final String RESPONSE_HEADER_XFO = "X-Frame-Options";

    private static final String ALLOW_FRAME_PRE = "allowFramePre";

    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    /**
     * doFilter
     * 
     * @param servletRequest
     *            servletRequest
     * @param servletResponse
     *            servletResponse
     * @param chain
     *            filterChain
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest)
                || !(servletResponse instanceof HttpServletResponse))
        {
            LOGGER.error("doFilter failed: HttpServlet cast error!");
            return;
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-XSS-Protection", "1");
        response.setHeader(RESPONSE_HEADER_CSP, "img-src 'self' data:");
        String allowIframe = StringUtils.getConfigInfo(ALLOW_FRAME_PRE);
        if (null == allowIframe || "".equals(allowIframe))
        {
            response.setHeader(RESPONSE_HEADER_XFO, "DENY");
        }
        else
        {
            response.setHeader(RESPONSE_HEADER_XFO, "ALLOW-FROM " + allowIframe);
        }
        // response.setHeader(RESPONSE_HEADER_XFO, "");
        String requestUri = request.getRequestURL().toString();
        String context = request.getContextPath();
        String referer = request.getHeader("Referer");
        if (null != referer) // 初步用referer防一下
        {
            String allowReferers = StringUtils
                    .getConfigInfo(ConfigConstant.ALLOW_REFERERS);
            for (String allowReferer : allowReferers.split(",")) // 准备referer例外
            {
                if (referer.startsWith(allowReferer))
                {
                    chain.doFilter(request, response);
                    return;
                }
            }
            String preUrl = gainUrlPre(requestUri, context);
            String preRef = gainUrlPre(referer, context);
            if (!preUrl.equals(preRef))
            {
                LOGGER.error("doFilter error, !preUrl.equals(preRef) requestUri");
                response.sendRedirect("/index.jsp");
            }
        }
        chain.doFilter(request, response);
    }

    private String gainUrlPre(String url, String context)
    {
        int ind = url.indexOf(context);
        return url.substring(0, ind);
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

}
