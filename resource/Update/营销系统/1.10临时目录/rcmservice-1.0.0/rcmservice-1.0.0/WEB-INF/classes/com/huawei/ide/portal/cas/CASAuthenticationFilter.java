package com.huawei.ide.portal.cas;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 
 * 新增一个ExcludeFile属性，可以通过配置该属性，让某些资源不适用单点鉴权，而采用另外的鉴权策略。
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月7日]
 * @see  [相关类/方法]
 */
public class CASAuthenticationFilter extends AbstractCasFilter
{
    private static final String EXCLUDEFILE = "ExcludeFile"; // excludeFile 列表
    
    private String casServerLoginUrl;
    
    private boolean renew;
    
    private boolean gateway;
    
    private GatewayResolver gatewayStorage;
    
    private String strExcludeFile;
    
    private String[] arrExcludeFile = null;
    
    /**
     * 构造函数
     */
    public CASAuthenticationFilter()
    {
        renew = false;
        gateway = false;
        gatewayStorage = new DefaultGatewayResolverImpl();
        setStrExcludeFile("");
    }
    
    /**
     * initInternal
     * 
     * @param filterConfig
     *            filterConfig
     * @throws ServletException
     *             ServletException
     */
    protected void initInternal(FilterConfig filterConfig)
        throws ServletException
    {
        if (!isIgnoreInitConfiguration())
        {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            setStrExcludeFile(getPropertyFromInitParams(filterConfig, EXCLUDEFILE, ""));
            String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);
            
            if (gatewayStorageClass != null)
            {
                try
                {
                    this.gatewayStorage = (GatewayResolver)Class.forName(gatewayStorageClass).newInstance();
                }
                catch (Exception e)
                {
                    throw new ServletException(e);
                }
            }
        }
    }
    
    /**
     * init
     */
    public void init()
    {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
        if (strExcludeFile != null && strExcludeFile.trim().length() > 0)
        {
            arrExcludeFile = strExcludeFile.split(",");
        }
    }
    
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
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse))
        {
            return;
        }
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession(false);
        Assertion assertion = session != null ? (Assertion)session.getAttribute("_const_cas_assertion_") : null;
        
        if (assertion != null)
        {
            filterChain.doFilter(request, response);
            return;
        }
        
        String requestStr = request.getRequestURL().toString();
        // LOGGER.debug("requestStr-->" + requestStr);
        PathMatcher matcher = new AntPathMatcher();
        if (arrExcludeFile != null)
        {
            for (String excludePath : arrExcludeFile)
            {
                boolean flag = matcher.match(excludePath, requestStr);
                if (!flag)
                {
                    flag = requestStr.indexOf(excludePath) != -1;
                }
                if (flag)
                {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        
        String serviceUrl = constructServiceUrl(request, response);
        String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
        boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);
        
        if ((CommonUtils.isNotBlank(ticket)) || wasGatewayed)
        {
            filterChain.doFilter(request, response);
            return;
        }
        
        String modifiedServiceUrl;
        if (this.gateway)
        {
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        }
        else
        {
            modifiedServiceUrl = serviceUrl;
        }
        
        String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl,
            getServiceParameterName(),
            modifiedServiceUrl,
            this.renew,
            this.gateway);
        
        response.sendRedirect(urlToRedirectTo);
    }
    
    public final void setRenew(boolean renew)
    {
        this.renew = renew;
    }
    
    public final void setGateway(boolean gateway)
    {
        this.gateway = gateway;
    }
    
    public final void setCasServerLoginUrl(String casServerLoginUrl)
    {
        this.casServerLoginUrl = casServerLoginUrl;
    }
    
    public final void setGatewayStorage(GatewayResolver gatewayStorage)
    {
        this.gatewayStorage = gatewayStorage;
    }
    
    public void setStrExcludeFile(String strExcludeFile)
    {
        this.strExcludeFile = strExcludeFile;
    }
}