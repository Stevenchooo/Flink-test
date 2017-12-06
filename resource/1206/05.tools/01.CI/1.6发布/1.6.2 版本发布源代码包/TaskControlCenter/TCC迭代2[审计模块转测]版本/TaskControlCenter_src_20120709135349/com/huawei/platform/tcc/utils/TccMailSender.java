/*
 * 文 件 名:  TccMailSender.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-4-13
 */
package com.huawei.platform.tcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.huawei.platform.common.CryptUtilforcfg;

/**
 * 邮件发送器
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-9-13]
 */
public class TccMailSender extends JavaMailSenderImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TccMailSender.class);
    
    private String keyword = "PkmJygVfrDxsDeeD";
    
    private String password;
    
    public String getPassword()
    {
        return password;
    }
    
    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password)
    {
        try
        {
            this.password = CryptUtilforcfg.decryptForAESStr(password.trim(), keyword);
        }
        catch (Exception e)
        {
            this.password = password;
            LOGGER.error("decryptForAESStr failed! use the source password!");
        }
    }
}
