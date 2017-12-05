package com.huawei.ide.interceptors.res.rcm;

/**
 * 加密异常
 * 
 * @author c00219980
 *
 */

public class CryptException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     * 
     * @param message
     *            异常消息
     */
    public CryptException(String message)
    {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param cause
     *            异常原因
     */
    public CryptException(Throwable cause)
    {
        super(cause);
    }

    /**
     * 构造函数
     * 
     * @param message
     *            异常消息
     * @param cause
     *            异常原因
     */
    public CryptException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
