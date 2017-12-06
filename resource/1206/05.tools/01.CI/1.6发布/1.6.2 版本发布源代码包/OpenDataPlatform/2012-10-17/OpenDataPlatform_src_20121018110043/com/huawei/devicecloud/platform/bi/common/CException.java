/*
 * 文 件 名:  UniAccountException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2010,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-3-28
 */
package com.huawei.devicecloud.platform.bi.common;

import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;

/**
 *
 * 终端云项目统一账号模块异常类
 * 
 * @author  q00107831
 * @version [华为终端云统一账号模块, 2011-3-31]
 * @see  [相关类/方法]
 */
public class CException extends Exception
{
    private static final long serialVersionUID = 5432444997938992137L;
    
    /**
     * 异常错误码
     */
    private int errorCode;
    
    /**
     * 异常描述信息
     */
    private String errorDesc;
    
    /** <默认构造函数>
     * @param errorCode 
     * @param cause 
     * @param objects 
     */
    public CException(int errorCode, Throwable cause, Object... objects)
    {
        super(CommonUtils.getErrorDesc(errorCode, objects), cause);
        errorDesc = CommonUtils.getErrorDesc(errorCode, objects);
        
        this.errorCode = errorCode;
    }
    /**
     * <默认构造函数>
     * @param errorCode 
     * @param cause 
     * @param errorDesc 
     */
    public CException(int errorCode, String errorDesc, Throwable cause)
    {
        super(errorDesc, cause);
        this.errorDesc = errorDesc;
        
        this.errorCode = errorCode;
    }
    
    /** <默认构造函数>
     * @param errorCode 
     * @param objects 
     */
    public CException(int errorCode, Object... objects)
    {
        super(CommonUtils.getErrorDesc(errorCode, objects));
        this.errorCode = errorCode;
        errorDesc = CommonUtils.getErrorDesc(errorCode, objects);
    }
    /**
     * <默认构造函数>
     * @param errorCode 
     * @param errorDesc 
     */
    public CException(int errorCode, String errorDesc)
    {
        super(errorDesc);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }
    
    public int getErrorCode()
    {
        return errorCode;
    }
    
    public String getErrorDesc()
    {
        return errorDesc;
    }
    
}
