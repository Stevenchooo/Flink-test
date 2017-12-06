/*
 * 文 件 名:  StateException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  执行异常
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-14
 */
package com.huawei.platform.um.exception;

/**
 * 执行异常
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-14]
 */
public class ExecuteException extends Exception
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -368185935236015509L;
    
    private int state;
    
    /**
     * 构造函数
     * @param state 状态
     * @param msg 消息
     */
    public ExecuteException(int state, String msg)
    {
        super(msg);
        this.state = state;
    }
    
    /**
     *构造函数
     * @param state 状态
     */
    public ExecuteException(int state)
    {
        super();
        this.state = state;
    }
    
    
    
    public int getState()
    {
        return state;
    }
}
