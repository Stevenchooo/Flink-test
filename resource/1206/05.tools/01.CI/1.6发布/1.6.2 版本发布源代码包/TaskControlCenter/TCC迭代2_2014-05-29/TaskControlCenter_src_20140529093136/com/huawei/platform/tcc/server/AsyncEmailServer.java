/*
 * 文 件 名:  EmailServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-7-4
 */
package com.huawei.platform.tcc.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件服务
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-7-4]
 */
public class AsyncEmailServer implements IEmailServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailServer.class);
    
    private static List<AsyncEmailSender> emails = new ArrayList<AsyncEmailSender>();
    
    /**
     * 给邮箱列表发送邮件
     * @param emailsTo 目的地址列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param from 邮件发件人（显示名）
     */
    @Override
    public void sendEmail(String from, final String[] emailsTo, final String subject, final String content)
    {
        try
        {
            if (!StringUtils.isBlank(from) && null != emailsTo && emailsTo.length > 0)
            {
                synchronized (emails)
                {
                    emails.add(new AsyncEmailSender(from, emailsTo, subject, content));
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sendEmail fail! from is {},emailsTo is {},subject is {},content is {}", new Object[] {from,
                emailsTo, subject, content, e});
        }
    }
    
    /**
     * 发送队列中的邮件,由定时器统一处理
     */
    public void sendEmails()
    {
        //取出要发送的邮件
        List<AsyncEmailSender> currEmails;
        synchronized (emails)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("emails is {}", emails);
            }
            
            currEmails = new ArrayList<AsyncEmailSender>(emails);
            emails.clear();
        }
        
        //依次发送
        if (null != currEmails && !currEmails.isEmpty())
        {
            for (AsyncEmailSender sender : currEmails)
            {
                if (null != sender)
                {
                    sender.run();
                }
            }
        }
    }
}
