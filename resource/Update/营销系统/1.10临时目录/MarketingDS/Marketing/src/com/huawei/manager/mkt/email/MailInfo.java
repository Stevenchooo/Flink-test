/*
 * 文 件 名:  MailInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-5-27
 */
package com.huawei.manager.mkt.email;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.manager.mkt.constant.EmailInfo;
import com.huawei.util.EncryptUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2013-5-27]
 * @see  [相关类/方法]
 */
public final class MailInfo
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailInfo.class);
    
    /**
     * 邮箱见分隔符
     */
    private static final String EMAIL_SEPRATOR = ";";
    
    /**
     * 邮件服务器地址
     */
    private String smtpHost;
    
    /**
     * 用户名称
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 服务器端口
     */
    private int port;
    
    /**
     * 发件人
     */
    private Address fromAddress;
    
    /**
     * 收件人数组
     */
    private Address[] toAddress;
    
    /**
     * 抄送人数组
     */
    private Address[] ccAddress;
    
    /**
     * 密送人列表
     */
    private Address[] bccAddress;
    
    /**
     * 是否需要身份验证
     */
    private boolean validate = false;
    
    /**
     * <默认构造函数>
     * @param prop          邮件配置属性
     * @param toAddress     主送邮件信息
     * @param ccAddress     抄送邮件信息
     * @param bccAddress    密送邮件信息
     * @throws AddressException    地址非法异常 
     */
    public MailInfo(Properties prop, String toAddress, String ccAddress, String bccAddress)
        throws AddressException
    {
        this.setSmtpHost(prop.getProperty(EmailInfo.SMTPHOST));
        this.setUsername(prop.getProperty(EmailInfo.USERNAME));
        this.setPassword(EncryptUtil.decode(prop.getProperty(EmailInfo.ENCODEPWD)));
        this.setPort(Integer.parseInt(prop.getProperty(EmailInfo.SMTPPROT)));
        this.setFromAddress(new InternetAddress(prop.getProperty(EmailInfo.FROMADDRESS)));
        this.setValidate(true);
        
        if (null != toAddress && toAddress.trim().length() > 0)
        {
            String[] toAddressStr = toAddress.split(EMAIL_SEPRATOR);
            
            setToAddress(new Address[toAddressStr.length]);
            for (int i = 0; i < toAddressStr.length; i++)
            {
                getToAddress()[i] = new InternetAddress(toAddressStr[i]);
            }
            
        }
        LOGGER.debug("toAddress is {} ", toAddress);
        
        if (null != ccAddress && ccAddress.trim().length() > 0)
        {
            String[] ccAddressStr = ccAddress.split(EMAIL_SEPRATOR);
            
            setCcAddress(new Address[ccAddressStr.length]);
            for (int i = 0; i < ccAddressStr.length; i++)
            {
                getCcAddress()[i] = new InternetAddress(ccAddressStr[i]);
            }
            
        }
        LOGGER.debug("ccAddress is {} ", ccAddress);
        
        
    }
    
    /**
     * <获得邮件会话属性>
     * @return  邮件配置属性
     * @see [类、类#方法、类#成员]
     */
    public Properties getProperties()
    {
        Properties p = new Properties();
        p.put(EmailInfo.MAILSMTPHOST, this.smtpHost);
        p.put(EmailInfo.MAILSMTPPROT, this.port);
        p.put(EmailInfo.MAILSMTPAUTH, validate ? "true" : "false");
        return p;
    }
    
    public Address getFromAddress()
    {
        return fromAddress;
    }
    
    public void setFromAddress(Address fromAddress)
    {
        this.fromAddress = fromAddress;
    }
    
    public Address[] getToAddress()
    {
        return toAddress;
    }
    
    public void setToAddress(Address[] toAddress)
    {
        this.toAddress = toAddress;
    }
    
    public Address[] getCcAddress()
    {
        return ccAddress;
    }
    
    public void setCcAddress(Address[] ccAddress)
    {
        this.ccAddress = ccAddress;
    }
    
    public Address[] getBccAddress()
    {
        return bccAddress;
    }
    
    public void setBccAddress(Address[] bccAddress)
    {
        this.bccAddress = bccAddress;
    }
    
    public String getSmtpHost()
    {
        return smtpHost;
    }
    
    public void setSmtpHost(String smtpHost)
    {
        this.smtpHost = smtpHost;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public boolean isValidate()
    {
        return validate;
    }
    
    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }
    
}
