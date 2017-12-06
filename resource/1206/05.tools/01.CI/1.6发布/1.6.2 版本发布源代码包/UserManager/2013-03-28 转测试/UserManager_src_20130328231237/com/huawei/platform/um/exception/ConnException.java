/*
 * 文 件 名:  AuthException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  连接异常
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-18
 */
package com.huawei.platform.um.exception;

/**
 * 连接异常
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-18]
 */
public class ConnException extends Exception
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8101482729817211501L;
    
    /**
     * 默认构造函数
     * @param msg 信息
     */
    public ConnException(String msg)
    {
        super(msg);
    }
    
    /**
     * 默认构造函数
     * @param msg 信息
     * @param e 异常
     */
    public ConnException(String msg, Throwable e)
    {
        super(msg, e);
    }
}
