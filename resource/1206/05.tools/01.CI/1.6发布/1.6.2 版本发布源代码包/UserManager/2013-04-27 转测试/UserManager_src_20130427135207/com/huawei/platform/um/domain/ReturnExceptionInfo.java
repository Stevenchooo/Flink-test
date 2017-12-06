/*
 * 文 件 名:  ReturnException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-8
 */
package com.huawei.platform.um.domain;

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
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + errorCode;
        result = prime * result + ((errorDesc == null) ? 0 : errorDesc.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof ReturnExceptionInfo))
        {
            return false;
        }
        ReturnExceptionInfo other = (ReturnExceptionInfo)obj;
        if (errorCode != other.errorCode)
        {
            return false;
        }
        if (errorDesc == null)
        {
            if (other.errorDesc != null)
            {
                return false;
            }
        }
        else if (!errorDesc.equals(other.errorDesc))
        {
            return false;
        }
        return true;
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
