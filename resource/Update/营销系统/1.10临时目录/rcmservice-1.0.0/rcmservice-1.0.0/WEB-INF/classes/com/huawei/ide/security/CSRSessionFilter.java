package com.huawei.ide.security;

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
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.huawei.ide.portal.CommonUtils;

/**
 * 
 * 防CSRF攻击过滤器
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年7月18日]
 * @see  [相关类/方法]
 */
public class CSRSessionFilter implements Filter
{
    
    /**
     * Content-Security-Policy 响应头
     */
    private static final String RESPONSE_HEADER_CSP = "Content-Security-Policy";
    
    /**
     * X-Frame-Options 响应头
     */
    private static final String RESPONSE_HEADER_XFO = "X-Frame-Options";
    
    private static final String REFFER_PRE = "allow.referer.pre";
    
    private static final String ALLOW_FRAME_PRE = "allowFramePre";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CSRSessionFilter.class);
    
    private String[] arrExcludeFile = {};
    
    private String indexPage;
    
    @Override
    public void destroy()
    {
        
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse))
        {
            LOGGER.error("doFilter failed: HttpServlet cast error!");
            return;
        }
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-XSS-Protection", "1");
        response.setHeader(RESPONSE_HEADER_CSP, "img-src 'self' data:");
        String allowFramePre = CommonUtils.getSysConfig(ALLOW_FRAME_PRE);
        if (null == allowFramePre || "".equals(allowFramePre))
        {
            response.setHeader(RESPONSE_HEADER_XFO, "DENY");
        }
        else
        {
            response.setHeader(RESPONSE_HEADER_XFO, "ALLOW-FROM " + allowFramePre);
        }
        
        String requestUri = request.getRequestURL().toString();
        PathMatcher matcher = new AntPathMatcher();
        for (String excludePath : arrExcludeFile) // 例外url，外部调用接口直接过
        {
            if (matcher.match(excludePath, requestUri))
            {
                chain.doFilter(request, response);
                return;
            }
        }
        
        String context = request.getContextPath();
        String referer = request.getHeader("Referer");
        if (null != referer) // 初步用referer防一下
        {
            String allowReferers = CommonUtils.getSysConfig(REFFER_PRE);
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
                LOGGER.error(
                    "doFilter error, !preUrl.equals(preRef) requestUri = " + requestUri + " , referer = " + referer);
                response.sendRedirect(indexPage);
            }
        }
        
        chain.doFilter(request, response);
        
    }
    
    private String gainUrlPre(String url, String context)
    {
        int ind = url.indexOf(context);
        return url.substring(0, ind);
    }
    
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        String excludes = config.getInitParameter("ExcludeFile");
        if (null != excludes)
        {
            arrExcludeFile = excludes.split(",");
        }
        indexPage = config.getInitParameter("indexPage");
    }
    
}
