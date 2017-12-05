package com.huawei.ide.portal.cas;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas10TicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 
 * 统一portal Ticket过滤器
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月7日]
 * @see  [相关类/方法]
 */
public class CASTicketValidationFilter extends AbstractCasFilter
{
    
    private static final String EXCLUDEFILE = "ExcludeFile"; // excludeFile 列表
    
    private TicketValidator ticketValidator;
    
    private boolean redirectAfterValidation;
    
    private boolean exceptionOnValidationFailure;
    
    private boolean useSession;
    
    private String strExcludeFile;
    
    private String[] arrExcludeFile = null;
    
    /**
     * 构造方法
     */
    public CASTicketValidationFilter()
    {
        redirectAfterValidation = false;
        exceptionOnValidationFailure = true;
        useSession = true;
        setStrExcludeFile("");
    }
    
    /**
     * getTicketValidator
     * 
     * @param filterConfig
     *            filterConfig
     * @return TicketValidator
     */
    protected final TicketValidator getTicketValidator(FilterConfig filterConfig)
    {
        String casServerUrlPrefix = getPropertyFromInitParams(filterConfig, "casServerUrlPrefix", null);
        Cas10TicketValidator validator = new Cas10TicketValidator(casServerUrlPrefix);
        validator.setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
        validator.setHostnameVerifier(getHostnameVerifier(filterConfig));
        validator.setEncoding(getPropertyFromInitParams(filterConfig, "encoding", null));
        
        return validator;
    }
    
    /**
     * getHostnameVerifier
     * 
     * @param filterConfig
     *            filterConfig
     * @return HostnameVerifier
     */
    protected HostnameVerifier getHostnameVerifier(FilterConfig filterConfig)
    {
        String className = getPropertyFromInitParams(filterConfig, "hostnameVerifier", null);
        String config = getPropertyFromInitParams(filterConfig, "hostnameVerifierConfig", null);
        if (className != null)
        {
            if (config != null)
            {
                return (HostnameVerifier)ReflectUtils.newInstance(className, new Object[] {config});
            }
            return (HostnameVerifier)ReflectUtils.newInstance(className, new Object[0]);
        }
        
        return null;
    }
    
    /**
     * initInternal
     * 
     * @param filterConfig
     *            filterConfig
     * @throws ServletException
     *             ServletException
     */
    @Override
    protected void initInternal(FilterConfig filterConfig)
        throws ServletException
    {
        setExceptionOnValidationFailure(
            parseBoolean(getPropertyFromInitParams(filterConfig, "exceptionOnValidationFailure", "true")));
        setRedirectAfterValidation(
            parseBoolean(getPropertyFromInitParams(filterConfig, "redirectAfterValidation", "true")));
        setUseSession(parseBoolean(getPropertyFromInitParams(filterConfig, "useSession", "true")));
        setTicketValidator(getTicketValidator(filterConfig));
        
        setStrExcludeFile(getPropertyFromInitParams(filterConfig, EXCLUDEFILE, ""));
        super.initInternal(filterConfig);
    }
    
    /**
     * 在Filter实例化后会自动执行init方法，一般在此方法中做一些数据的初始化工作
     */
    @Override
    public void init()
    {
        super.init();
        CommonUtils.assertNotNull(this.ticketValidator, "ticketValidator cannot be null.");
        if (strExcludeFile != null && strExcludeFile.trim().length() > 0)
        {
            arrExcludeFile = strExcludeFile.split(",");
        }
    }
    
    /**
     * preFilter
     * 
     * @param servletRequest
     *            servletRequest
     * @param servletResponse
     *            servletResponse
     * @param filterChain
     *            filterChain
     * @return boolean
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    protected boolean preFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        return true;
    }
    
    /**
     * onSuccessfulValidation
     * 
     * @param request
     *            请求
     * @param response
     *            响应
     * @param assertion
     *            assertion
     */
    protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response, Assertion assertion)
    {
    }
    
    /**
     * onFailedValidation
     * 
     * @param request
     *            请求
     * @param response
     *            响应
     */
    protected void onFailedValidation(HttpServletRequest request, HttpServletResponse response)
    {
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
    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse))
        {
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        
        String requestUri = request.getRequestURL().toString();
        // LOGGER.debug("requestUri-->" + requestUri);
        PathMatcher matcher = new AntPathMatcher();
        if (arrExcludeFile != null)
        {
            for (String excludePath : arrExcludeFile)
            {
                boolean flag = matcher.match(excludePath, requestUri);
                if (!flag)
                {
                    flag = requestUri.indexOf(excludePath) != -1;
                }
                if (flag)
                {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        
        String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
        
        if (CommonUtils.isNotBlank(ticket))
        {
            try
            {
                Assertion assertion = this.ticketValidator.validate(ticket, constructServiceUrl(request, response));
                
                request.setAttribute("_const_cas_assertion_", assertion);
                
                if (this.useSession)
                {
                    request.getSession().setAttribute("_const_cas_assertion_", assertion);
                }
                onSuccessfulValidation(request, response, assertion);
                
                if (this.redirectAfterValidation)
                {
                    response.sendRedirect(constructServiceUrl(request, response));
                    return;
                }
            }
            catch (TicketValidationException e)
            {
                response.setStatus(403);
                onFailedValidation(request, response);
                
                if (this.exceptionOnValidationFailure)
                {
                    throw new ServletException(e);
                }
                
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    public final void setTicketValidator(TicketValidator ticketValidator)
    {
        this.ticketValidator = ticketValidator;
    }
    
    public final void setRedirectAfterValidation(boolean redirectAfterValidation)
    {
        this.redirectAfterValidation = redirectAfterValidation;
    }
    
    public final void setExceptionOnValidationFailure(boolean exceptionOnValidationFailure)
    {
        this.exceptionOnValidationFailure = exceptionOnValidationFailure;
    }
    
    public final void setUseSession(boolean useSession)
    {
        this.useSession = useSession;
    }
    
    public void setStrExcludeFile(String strExcludeFile)
    {
        this.strExcludeFile = strExcludeFile;
    }
}