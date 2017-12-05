package com.huawei.ide.portal.cas;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 用户鉴权
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年7月9日]
 * @see  [相关类/方法]
 */
public class UserAuthenticationFilter implements Filter
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationFilter.class);
    
    /**
     * 有权限的用户列表
     */
    String[] userList;
    
    /**
     * 无权限页面路径
     */
    String failedPage;
    
    public void setFailedPage(String failedPage)
    {
        this.failedPage = failedPage;
    }
    
    public void setUserList(String[] userList)
    {
        this.userList = userList;
    }
    
    /**
     * 初始化
     * @param config
     *        config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        String userStr = config.getInitParameter("userList");
        failedPage = config.getInitParameter("failedPage");
        userList = userStr.split(",");
        
    }
    
    /**
     * doFilter
     * @param servletRequest
     *          servletRequest
     * @param servletResponse
     *        servletResponse
     * @param filterChain
     *         filterChain
     * @throws IOException
     *          IOException
     * @throws ServletException
     *           ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        Assertion assertion = (Assertion)request.getSession().getAttribute("_const_cas_assertion_");
        String name = assertion.getPrincipal().getName();
        for (String tempName : userList)
        {
            if (tempName.equals(name))
            {
                filterChain.doFilter(request, response);
                return;
            }
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(failedPage);
        try
        {
            dispatcher.forward(request, response);
            return;
        }
        catch (Exception e)
        {
            LOGGER.error("page forward failed!");
            return;
        }
        
    }
    
    /**
     * destroy
     */
    @Override
    public void destroy()
    {
        
    }
    
}
