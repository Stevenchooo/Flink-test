/*
 * 文 件 名:  AuthReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  authReq方法请求参数类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

/**
 * authReq方法请求体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class AuthReq
{
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 密码
     */
    private String pwd;

    /**
     * 获取用户名
     * @return 用户名
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * 设置用户名
     * @param userName 用户名
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * 获取密码
     * @return 密码
     */
    public String getPwd()
    {
        return pwd;
    }

    /**
     * 设置密码
     * @param pwd 密码
     */
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    /**
     * 字符串表示
     * @return 字符串表示
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("AuthReq [userName=");
        builder.append(userName);
        builder.append(", pwd=");
        builder.append(pwd);
        builder.append("]");
        return builder.toString();
    }
}
