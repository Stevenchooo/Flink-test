/*
 * 文 件 名:  StateException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  执行异常
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi.exception;

/**
 * 执行异常
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-13]
 */
public class ExecuteException extends Exception
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -368185935236015509L;
    
    /**
     * 构造函数
     * @param msg 消息
     */
    public ExecuteException(String msg)
    {
        super(msg);
    }
}
