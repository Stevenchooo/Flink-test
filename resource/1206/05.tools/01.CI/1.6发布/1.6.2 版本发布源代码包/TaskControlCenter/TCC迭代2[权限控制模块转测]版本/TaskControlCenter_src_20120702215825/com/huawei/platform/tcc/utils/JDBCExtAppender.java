/*
 * 文 件 名:  ConcurrentCallControl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-1-11
 */
package com.huawei.platform.tcc.utils;

import java.util.Enumeration;
import java.util.Iterator;

import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CryptUtilforcfg;

/**
 * JDBCAppender扩展类
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-1-11]
 * @see  [相关类/方法]
 */
public class JDBCExtAppender extends JDBCAppender
{
    private static final String KEYWORD = "PkmJygVfrDxsDeeD";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCExtAppender.class);
    
    /**
     * 立即将remoteshell.cmd和com.huawei.platform日志刷新到db中
     */
    public static void fresh2Db()
    {
        org.apache.log4j.Logger rs2DbLog = LogManager.getLogger("remoteshell.cmd");
        org.apache.log4j.Logger tcc2DbLog = LogManager.getLogger("com.huawei.platform");
        if (null == rs2DbLog)
        {
            LOGGER.error("item[log4j.logger.remoteshell.cmd=] must be configured in log4j.properties.");
            return;
        }
        
        if (null == tcc2DbLog)
        {
            LOGGER.error("item[log4j.logger.com.huawei.platform=] must be configured in log4j.properties.");
            return;
        }
        
        @SuppressWarnings("unchecked")
        Enumeration<Appender> rs2DbAppEnum = rs2DbLog.getAllAppenders();
        Appender rs2DbApp;
        if (null != rs2DbAppEnum)
        {
            while (rs2DbAppEnum.hasMoreElements())
            {
                rs2DbApp = rs2DbAppEnum.nextElement();
                if (rs2DbApp instanceof JDBCExtAppender)
                {
                    ((JDBCExtAppender)rs2DbApp).flushBuffer();
                }
            }
        }
        
        @SuppressWarnings("unchecked")
        Enumeration<Appender> tcc2DbAppEnum = tcc2DbLog.getAllAppenders();
        Appender tcc2DbApp;
        if (null != tcc2DbAppEnum)
        {
            while (tcc2DbAppEnum.hasMoreElements())
            {
                tcc2DbApp = tcc2DbAppEnum.nextElement();
                if (tcc2DbApp instanceof JDBCExtAppender)
                {
                    ((JDBCExtAppender)tcc2DbApp).flushBuffer();
                }
            }
        }
    }
    
    @Override
    protected void execute(String sql)
    {
        //执行出错记录日志
        try
        {
            super.execute(sql);
        }
        catch (Exception e)
        {
            LOGGER.error("To forbid the log2db, because of sql executed failed! the sql is {}!", sql, e);
        }
    }
    
    /**
     * 线程同步
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void flushBuffer()
    {
        try
        {
            removes.ensureCapacity(buffer.size());
            StringBuilder msgBuilder;
            String msg;
            String[] errors;
            for (Iterator<LoggingEvent> i = buffer.iterator(); i.hasNext();)
            {
                LoggingEvent logEvent = i.next();
                msg = logEvent.getRenderedMessage();
                //将message中的引号替换成两个双引号
                msg = (null == msg) ? "" : msg.replace("\"", "\"\"");
                msgBuilder = new StringBuilder();
                msgBuilder.append(msg);
                //上层没有打印，这显示
                if (super.layout.ignoresThrowable())
                {
                    errors = logEvent.getThrowableStrRep();
                    //记录错误信息
                    if (null != errors)
                    {
                        for (int j = 0; j < errors.length; j++)
                        {
                            msgBuilder.append("\n");
                            msgBuilder.append((null == errors[j]) ? "" : errors[j].replace("\"", "\"\""));
                        }
                    }
                }
                logEvent.setProperty("msg", msgBuilder.toString());
                String sql = getLogStatement(logEvent);
                execute(sql);
                removes.add(logEvent);
            }
            
            buffer.removeAll(removes);
            removes.clear();
        }
        catch (Exception e)
        {
            LOGGER.error("flushBuffer execute failed!", e);
        }
    }
    
    /**
     * 对数据源密码进行解密
     * @param password 加密的密码 
     */
    public void setPassword(String password)
    {
        super.setPassword(CryptUtilforcfg.decryptForAESStr(password.trim(), KEYWORD));
    }
}
