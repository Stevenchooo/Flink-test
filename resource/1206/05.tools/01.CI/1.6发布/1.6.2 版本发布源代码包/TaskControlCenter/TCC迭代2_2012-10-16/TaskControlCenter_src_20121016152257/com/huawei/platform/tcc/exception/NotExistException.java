/*
 * 文 件 名:  PrivilegeNotEnoughException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.exception;


/**
 * 对象不存在异常
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 */
public class NotExistException extends Exception
{
    /**
     * 序号
     */
    private static final long serialVersionUID = -74626541507586022L;

    /**
     * 构造函数
     * @param msg 错误信息
     */
    public NotExistException(String msg)
    {
        super(msg);
    }
}
