/*
 * 文 件 名:  AuthenticationFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-4-27
 */
package com.huawei.platform.tcc.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 认证过滤器
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-4-27]
 */
public class AuthenticationFilter implements Filter
{
    /**
     * 认证过滤器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    private String defaultEncoding = "utf-8";
    
    private String excludeResource;
    
    private String operatorExcludeResource;
    
    private List<String> excludeSuffixs = new ArrayList<String>();
    
    private List<String> operatorExcludeSuffixs = new ArrayList<String>();
    
    @Override
    public void destroy()
    {
        defaultEncoding = null;
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("destroy...");
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse responce, FilterChain chain)
        throws IOException, ServletException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter doFilter...");
        }
        
        if (!(request instanceof HttpServletRequest) || !(responce instanceof HttpServletResponse))
        {
            throw new ServletException("Just only support HTTP requests!");
        }
        
        request.setCharacterEncoding(defaultEncoding);
        
        HttpServletRequest httpReq = (HttpServletRequest)request;
        //会话不存在，则创建会话
        HttpSession session = httpReq.getSession(true);
        Object clientIp = session.getAttribute(TccUtil.CLIENT_IP);
        
        if (null == clientIp)
        {
            Map<String, String> mapIp = TccUtil.getRealIp(httpReq);
            session.setAttribute(NameStoredInSession.CLIENT_IP, mapIp.get(NameStoredInSession.CLIENT_IP));
            session.setAttribute(NameStoredInSession.IP_HEADER_NAME, mapIp.get(NameStoredInSession.IP_HEADER_NAME));
        }
        
        //未登陆
        Object userName = OperatorMgnt.getOperatorName(session);
        HttpServletResponse httpRsp = (HttpServletResponse)responce;
        String uri = httpReq.getRequestURI();
        if (null == userName)
        {
            try
            {
                if (!uri.endsWith("login.jsp") && !endWithAnySuffix(uri))
                {
                    httpRsp.sendRedirect("login.jsp");
                    return;
                }
                
            }
            catch (Exception e)
            {
                LOGGER.error("sendRedirect failed!", e);
            }
        }
        else
        {
            if (!OperatorMgnt.isSysAdmin(session))
            {
                //系统管理员权限
                if (!isOperatorVisible(uri))
                {
                    httpRsp.sendRedirect("index.jsp");
                    return;
                }
            }
            else
            {
                String toIndexSuffix = httpReq.getContextPath();
                //替换默认页面
                if (uri.endsWith("index.jsp") || uri.endsWith(toIndexSuffix) || uri.endsWith(toIndexSuffix + "/"))
                {
                    httpRsp.sendRedirect("SysAdminIndex.jsp");
                    return;
                }
            }
            
        }
        
        chain.doFilter(request, responce);
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("exit doFilter...");
        }
    }
    
    private boolean isOperatorVisible(String url)
    {
        if (StringUtils.isBlank(url))
        {
            return true;
        }
        
        for (String suffix : operatorExcludeSuffixs)
        {
            if (url.endsWith(suffix))
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 是否以任何后缀结尾
     */
    private boolean endWithAnySuffix(String url)
    {
        if (StringUtils.isBlank(url))
        {
            return false;
        }
        
        for (String suffix : excludeSuffixs)
        {
            if (url.endsWith(suffix))
            {
                return true;
            }
        }
        
        return false;
    }
    
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
        
        setExcludeResource(config.getInitParameter("excludeResource"));
        setOperatorExcludeResource(config.getInitParameter("operatorExcludeResource"));
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("init...");
        }
    }
    
    public String getExcludeResource()
    {
        return excludeResource;
    }
    
    /**
     * 用分号分隔的后缀列表
     * @param excludeResource 用分号分隔的后缀列表
     */
    public void setExcludeResource(String excludeResource)
    {
        this.excludeResource = excludeResource;
        
        if (!StringUtils.isBlank(this.excludeResource))
        {
            String[] suffixs = this.excludeResource.split(";");
            for (String suffix : suffixs)
            {
                if (!StringUtils.isBlank(suffix))
                {
                    this.excludeSuffixs.add(suffix.trim());
                }
            }
        }
    }
    
    public String getOperatorExcludeResource()
    {
        return operatorExcludeResource;
    }
    
    /**
     * 用分号分隔的后缀列表
     * @param operatorExcludeResource 操作员不可见的资源后缀列表
     */
    public void setOperatorExcludeResource(String operatorExcludeResource)
    {
        this.operatorExcludeResource = operatorExcludeResource;
        
        if (!StringUtils.isBlank(this.operatorExcludeResource))
        {
            String[] suffixs = this.operatorExcludeResource.split(";|\t|\r|\n");
            for (String suffix : suffixs)
            {
                if (!StringUtils.isBlank(suffix))
                {
                    this.operatorExcludeSuffixs.add(suffix.trim());
                }
            }
        }
    }
    
}
