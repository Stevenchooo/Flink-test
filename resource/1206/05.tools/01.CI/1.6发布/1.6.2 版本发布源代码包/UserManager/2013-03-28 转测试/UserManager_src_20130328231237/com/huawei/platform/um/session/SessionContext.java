/*
 * 文 件 名:  SessionContex.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  会话上下文
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-28
 */
package com.huawei.platform.um.session;

/**
 * 会话上下文
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-28]
 */

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class SessionContext
{
    private static SessionContext instance = new SessionContext();;
    
    private Map<String, HttpSession> sessionMap;
    
    private SessionContext()
    {
        sessionMap = new HashMap<String, HttpSession>();
    }
    
    public static SessionContext getInstance()
    {
        return instance;
    }
    
    public synchronized void AddSession(HttpSession session)
    {
        if (session != null)
        {
            sessionMap.put(session.getId(), session);
        }
    }
    
    public synchronized void DelSession(HttpSession session)
    {
        if (session != null)
        {
            sessionMap.remove(session.getId());
        }
    }
    
    public synchronized HttpSession getSession(String sessionId)
    {
        if (sessionId == null)
        {
            return null;
        }
        
        return (HttpSession)sessionMap.get(sessionId);
    }
    
}
