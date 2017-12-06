/*
 * 文 件 名:  AsyncEmailSender.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-7-4
 */
package com.huawei.platform.tcc.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 异步邮件发送
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-7-4]
 */
public class AsyncEmailSender implements Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailSender.class);
    
    private String from;
    
    private String emailsTo;
    
    private String subject;
    
    private String content;
    
    /**
     * 给邮箱列表发送邮件
     * @param emailsTo 目的地址列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param from 邮件发件人（显示名）
     */
    public AsyncEmailSender(String from, final String[] emailsTo, final String subject, final String content)
    {
        if (null != emailsTo && emailsTo.length > 0)
        {
            StringBuilder emailSb = new StringBuilder();
            List<String> distinctEmails = new ArrayList<String>();
            for (String email : emailsTo)
            {
                if (!StringUtils.isBlank(email) && !distinctEmails.contains(email))
                {
                    distinctEmails.add(email);
                    emailSb.append(email);
                    emailSb.append(';');
                }
            }
            this.from = from;
            this.emailsTo = emailSb.toString();
            this.subject = subject;
            this.content = content;
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            if (!StringUtils.isBlank(from) && !StringUtils.isBlank(emailsTo))
            {
                TccUtil.sendEmail(from, emailsTo, subject, content);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("AsyncEmailSender fail! from is {},emailsTo is {},subject is {},content is {}", new Object[] {
                from, emailsTo, subject, content, e});
        }
        
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("AsyncEmailSender [from=");
        builder.append(from);
        builder.append(", emailsTo=");
        builder.append(emailsTo);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", content=");
        builder.append(content);
        builder.append("]");
        return builder.toString();
    }
}
