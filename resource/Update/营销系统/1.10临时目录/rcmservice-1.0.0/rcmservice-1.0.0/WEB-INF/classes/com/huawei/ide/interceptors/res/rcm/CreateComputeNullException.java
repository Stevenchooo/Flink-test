package com.huawei.ide.interceptors.res.rcm;

/**
 * 
 * CreateComputeNullException
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class CreateComputeNullException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message
     *            异常消息
     */
    public CreateComputeNullException(String message)
    {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param cause
     *            异常原因
     */
    public CreateComputeNullException(Throwable cause)
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
    public CreateComputeNullException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

