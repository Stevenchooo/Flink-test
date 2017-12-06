/*
 * 文 件 名:  ReturnException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-8
 */
package com.huawei.devicecloud.platform.bi.common.domain;

import java.io.Serializable;

/**
 * 
 * 为方便Json工具转化异常信息定义的异常信息封装数据类
 * 
 * @author  l00166278
 * @version [华为终端云统一账号模块, 2011-8-8]
 * @see  [相关类/方法]
 */
public class ReturnExceptionInfo implements Serializable
{
    
    private static final long serialVersionUID = -4480538227767089546L;
    
    /**
     * 异常码
     */
    private int errorCode;
    
    /**
     * 异常描述信息
     */
    private String errorDesc;
    
    /**
     * 构造函数
     */
    public ReturnExceptionInfo()
    {
        
    }
    
    /**
     * 构造函数
     * @param errorCode 错误码
     * @param errorDesc 错误描述
     */
    public ReturnExceptionInfo(int errorCode, String errorDesc)
    {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }
    
    public int getErrorCode()
    {
        return errorCode;
    }
    
    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }
    
    public String getErrorDesc()
    {
        return errorDesc;
    }
    
    public void setErrorDesc(String errorDesc)
    {
        this.errorDesc = errorDesc;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ReturnException [errorCode=");
        builder.append(errorCode);
        builder.append(", errorDesc=");
        builder.append(errorDesc);
        builder.append("]");
        return builder.toString();
    }
    
}
