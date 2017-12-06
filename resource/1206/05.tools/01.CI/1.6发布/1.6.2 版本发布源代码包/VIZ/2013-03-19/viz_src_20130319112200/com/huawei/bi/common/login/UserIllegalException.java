/*
 * 文 件 名:  UserIllegalException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-12
 */
package com.huawei.bi.common.login;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-12]
 * @see  [相关类/方法]
 */
public class UserIllegalException extends RuntimeException
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    /** <默认构造函数>
     */
    public UserIllegalException()
    {
        super();
    }

    /** <默认构造函数>
     */
    public UserIllegalException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /** <默认构造函数>
     */
    public UserIllegalException(String message)
    {
        super(message);
    }

    /** <默认构造函数>
     */
    public UserIllegalException(Throwable cause)
    {
        super(cause);
    }
    
}
