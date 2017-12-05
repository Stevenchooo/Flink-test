/*
 * 文 件 名:  EmailAuthenticator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-5-28
 */
package com.huawei.manager.mkt.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2013-5-28]
 * @see  [相关类/方法]
 */
public class EmailAuthenticator extends Authenticator
{
    //用户名
    private String userName;
    
    //密码
    private String password;
    
    /**
     * <默认构造函数>
     */
    public EmailAuthenticator()
    {
    }
    
    /**
     * <默认构造函数>
     * @param username      用户名
     * @param password      密码
     */
    public EmailAuthenticator(String username, String password)
    {
        this.userName = username;
        this.password = password;
    }
    
    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(userName, password);
    }
}
