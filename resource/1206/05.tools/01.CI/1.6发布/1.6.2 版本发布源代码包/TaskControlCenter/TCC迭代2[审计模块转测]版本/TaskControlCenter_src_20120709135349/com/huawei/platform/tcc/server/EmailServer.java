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

import com.huawei.platform.tcc.task.CycleTask;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 邮件服务
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-7-4]
 */
public class EmailServer implements IEmailServer
{
    //记录日志
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServer.class);
    
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
        if (null != emailsTo && emailsTo.length > 0)
        {
            try
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
                TccUtil.sendEmail(from, emailSb.toString(), subject, content);
            }
            catch (Exception e)
            {
                LOGGER.error("sendEmail fail! from is {},emailsTo is {},subject is {},content is {}",
                    new Object[] {from, emailsTo, subject, content, e});
            }
        }
    }
}
