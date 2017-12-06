/*
 * 文 件 名:  CharacterEncodingFilter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-2-20
 */
package com.huawei.bi.common.login;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 字符编码过滤
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-4-26]
 * @see  [相关类/方法]
 */
public class CharacterEncodingFilter implements Filter
{
    
    /**
     * 编码格式
     */
    private String encoding;
    
    /** 获取设定的编码格式
     * @return encoding
     * @see [类、类#方法、类#成员]
     */
    public String getEncoding()
    {
        return encoding;
    }
    
    @Override
    public void destroy()
    {
        
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) throws IOException, ServletException
    {
        if (encoding != null && encoding.length() > 0)
        {
            req.setCharacterEncoding(encoding);
            res.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(req, res);
        
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        String initEncoding = filterConfig.getInitParameter("encoding");
        if (initEncoding != null)
        {
            encoding = initEncoding;
        }
    }
    
}
