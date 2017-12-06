/*
 * 文 件 名:  TaskExecuteException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-29
 */
package com.huawei.bi.task.bean.taskrunner;

/**
 * 任务执行异常
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-29]
 * @see  [相关类/方法]
 */
public class TaskExecuteException extends RuntimeException
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    /** <默认构造函数>
     */
    public TaskExecuteException()
    {
        super();
    }

    /** <默认构造函数>
     */
    public TaskExecuteException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /** <默认构造函数>
     */
    public TaskExecuteException(String message)
    {
        super(message);
    }

    /** <默认构造函数>
     */
    public TaskExecuteException(Throwable cause)
    {
        super(cause);
    }
    
    
}
