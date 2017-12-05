/*
 * 文 件 名:  MailSender.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-5-27
 */
package com.huawei.manager.mkt.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

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
public class MailSender
{
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);
    
    /**
     * <开始发送邮件>
     * @param mailInfo    邮件信息
     * @param pageContent 邮件内容
     * @param subject     邮件主题
     * @param attachFile  附件
     * @throws MessagingException     异常信息
     * @throws UnsupportedEncodingException  异常信息
     * @see [类、类#方法、类#成员]
     */
    public void sendMail(MailInfo mailInfo, String pageContent, String subject, String attachFile)
        throws MessagingException, UnsupportedEncodingException
    {
        //入口日志
        LOG.debug("enter into sendMail");
        
        //参数校验
        if (null != mailInfo && null != pageContent && null != subject)
        {
            EmailAuthenticator authenticator = null;
            Properties pro = mailInfo.getProperties();
            
            //如果需要身份认证，则创建一个密码验证器     
            if (mailInfo.isValidate())
            {
                authenticator = new EmailAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
            }
            
            //获取会话
            Session session = getSession(pro, authenticator);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(mailInfo.getFromAddress());
            
            //主送人
            if (null != mailInfo.getToAddress())
            {
                message.setRecipients(Message.RecipientType.TO, mailInfo.getToAddress());
            }
            
            //抄送人
            if (null != mailInfo.getCcAddress())
            {
                message.setRecipients(Message.RecipientType.CC, mailInfo.getCcAddress());
            }
            
            message.setSubject(subject);
            message.setSentDate(new Date());
            
            MimeMultipart mm = new MimeMultipart();
            
            //加载页面HTML内容
            if (!loadPageContent(mm, pageContent))
            {
                return;
            }
            
            //附加文件
            if (null != attachFile)
            {
                File attachment = new File(attachFile);
                // 添加附件的内容
                if (attachment.exists())
                {
                    
                    BodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachment);
                    attachmentBodyPart.setDataHandler(new DataHandler(source));
                    
                    attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                    mm.addBodyPart(attachmentBodyPart);
                }
            }
            
            //把mm作为消息对象的内容
            message.setContent(mm);
            
            //发送邮件
            Transport.send(message);
            
            LOG.debug("sendMail success");
        }
        
    }
    
    /**
     * <加载页面内容>
     * @param mm       文件格式
     * @param content  文件内容
     * @return         加载内容是否成功
     * @throws  
     * @see [类、类#方法、类#成员]
     */
    private boolean loadPageContent(MimeMultipart mm, String content)
    {
        if (null != mm && null != content)
        {
            MimeBodyPart mdp = new MimeBodyPart();
            try
            {
                mdp.setContent(content, "text/html; charset=utf-8");
                mm.addBodyPart(mdp);
                LOG.debug("Complete load HTML file !!!");
                return true;
            }
            catch (MessagingException e)
            {
                LOG.error("load HTML file error! Exception is MessagingException");
            }
            
        }
        
        return false;
    }
    
    /**
     * 获得SESSION
     * @param props            配置文件夹
     * @param authenticator    判决器
     * @return                 会话
     */
    private Session getSession(Properties props, EmailAuthenticator authenticator)
    {
        return Session.getDefaultInstance(props, authenticator);
    }
    
}
