/*
 * 文 件 名:  SessionListener.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  会话监听器
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-28
 */
package com.huawei.platform.um.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 会话监听器
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-28]
 */
public class SessionListener implements HttpSessionListener
{
    private SessionContext sessionCtx = SessionContext.getInstance();
    
    public void sessionCreated(HttpSessionEvent httpSessionEvent)
    {
        sessionCtx.AddSession(httpSessionEvent.getSession());
    }
    
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent)
    {
        HttpSession session = httpSessionEvent.getSession();
        sessionCtx.DelSession(session);
    }
}
