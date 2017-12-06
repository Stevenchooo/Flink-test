/*
 * 文 件 名:  CheckParamUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  参数校验的通用工具
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.platform.tcc.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;

/**
 * 参数校验的通用工具
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class CheckParamUtils
{
    /**
     * 私有构造方法
     */
    private CheckParamUtils()
    {
        
    }
    
    /**
     * 统一封装参数校验异常
     * @param parameterName 参数名
     * @param parameterVal 参数值
     * @throws CException 异常
     */
    public static void buildParameterInvalid(String parameterName, Object parameterVal)
        throws CException
    {
        StringBuffer strb = new StringBuffer();
        //字段值不合法字符串
        strb.append("Parameter named ")
            .append(parameterName)
            .append(" with value [")
            .append(parameterVal)
            .append("] is invalid.");
        
        throw new CException(ResultCode.PARAMETER_INVALID, new Object[] {parameterVal, parameterName});
    }
    
    /**
     * 检查任务Id是否合法
     * @param taskId 任务Id
     * @return 是否合法
     */
    public static boolean checkTaskId(String taskId)
    {
        if (StringUtils.isBlank(taskId))
        {
            return true;
        }
        
        return checkValueWithRegex("([0-9]{1,21};?)+", taskId);
    }
    
    /**
     * 检查步骤Id是否合法
     * @param stepId 步骤Id
     * @return 是否合法
     */
    public static boolean checkStepId(String stepId)
    {
        if (StringUtils.isBlank(stepId))
        {
            return true;
        }
        
        return checkValueWithRegex("[0-9]{1,10}", stepId);
    }
    
    /**
     * 使用正则表示式校验的通用方法
     * 
     * @param regex 正则表示式
     * @param value 参数值
     * @return 是否符合要求
     */
    public static boolean checkValueWithRegex(String regex, String value)
    {
        try
        {
            //检查value是否符合正则表达式
            if (null != regex)
            {
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(value).matches();
            }
        }
        catch (Exception e)
        {
            return false;
        }
        
        return true;
    }
}
