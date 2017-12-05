/*
 * 文 件 名:  SplitDispatcher.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-1-14
 */
package com.huawei.manager.mkt.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huawei.manager.utils.Constant;
import com.huawei.waf.servlet.Dispatcher;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-1-14]
 * @see  [相关类/方法]
 */
public class SplitDispatcher extends Dispatcher
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 611116792124082002L;
    
    /**
     * 获取方法
     * @param req     请求消息
     * @param resp    返回消息
     * @return        方法
     */
    @Override
    protected String getMethod(HttpServletRequest req, HttpServletResponse resp)
    {
        final String method = super.getMethod(req, resp);
        if (null == method)
        {
            return null;
        }
        
        String[] methods = method.split(Constant.SEMICOLON);
        for (String m : methods)
        {
            //只取第一段
            return m.trim();
        }
        
        return method.trim();
    }
}
