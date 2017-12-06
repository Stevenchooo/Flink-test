/*
 * 文 件 名:  ArgumentException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  参数异常
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-6
 */
package com.huawei.devicecloud.platform.bi.metasync.exception;

/**
 * 参数异常
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-6]
 */
public class ArgumentException extends Exception
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = -6615377216069301547L;

    /**
     * 构造函数
     * @param message 消息
     */
    public ArgumentException(final String message)
    {
        super(message);
    }
}
