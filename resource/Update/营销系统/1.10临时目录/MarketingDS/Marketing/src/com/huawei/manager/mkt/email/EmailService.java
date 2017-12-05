/*
 * 文 件 名:  EmailService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-5-27
 */
package com.huawei.manager.mkt.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2013-5-27]
 * @see  [相关类/方法]
 */
public final class EmailService
{
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    
    /**
     * <默认构造函数>
     */
    private EmailService()
    {
    }
    
    /**
     * <发送营销活动邮件>
     * @param mainsendemails     主送邮件
     * @param copysendemails     抄送邮件
     * @param title              标题
     * @param content            内容
     * @param attachFile         附件
     * @return                   是否发送成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean sendMktEmail(String mainsendemails, String copysendemails,
        String title, String content, String attachFile)
    {
        //邮件与标题不能为空
        if ((null != mainsendemails || null != copysendemails) && null != title)
        {
            //获取邮件的基本配置信息
            EmailConfig mailConfig = new EmailConfig();
            Properties pro = mailConfig.getProperties();
            MailInfo mailInfo = null;
            try
            {
                mailInfo = new MailInfo(pro, mainsendemails, copysendemails, null);
            }
            catch (AddressException e)
            {
                LOG.error("sendMktEmail AddressException,exception is AddressException");
                return false;
            }
            
            //发送邮件
            MailSender mailSender = new MailSender();
            
            try
            {
                mailSender.sendMail(mailInfo, content, title, attachFile);
                return true;
            }
            catch (UnsupportedEncodingException e)
            {
                LOG.error("sendMktEmail error! Exception is UnsupportedEncodingException");
            }
            catch (MessagingException e)
            {
                LOG.error("sendMktEmail error! Exception is MessagingException");
            }
            
        }
        
        return false;
    }
    
}
