/*
 * 文 件 名:  UsernameAndPasswordEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-6-19
 */
package com.huawei.platform.tcc.domain;


/**
 * 账号密码实体类
 * 
 * @author  l00194471
 * @version [华为终端云统一账号模块, 2012-6-19]
 */
public class UsernameAndPasswordParam
{
    /**
     * username
     */
    private String userName;
    
    /**
     * password
     */
    private String password;
    
    public String getUserName()
    {
        return this.userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("UsernameAndPasswordParam [userName=");
        builder.append(userName);
        builder.append(", password=");
        builder.append("]");
        return builder.toString();
    }
}