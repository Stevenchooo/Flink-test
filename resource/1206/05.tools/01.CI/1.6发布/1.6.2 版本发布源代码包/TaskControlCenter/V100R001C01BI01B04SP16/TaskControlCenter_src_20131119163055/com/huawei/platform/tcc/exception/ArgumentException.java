/*
 * 文 件 名:  ArgumentException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  参数异常
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-14
 */
package com.huawei.platform.tcc.exception;

/**
 * 
 * 参数异常类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-8]
 */
public class ArgumentException extends Exception
{
    
    /**
     * 序列化
     */
    private static final long serialVersionUID = -2249822525877134905L;
    
    /**
     * 构造函数
     * @param message 提示消息
     */
    public ArgumentException(String message)
    {
        super(message);
    }
}