/*
 * 文 件 名:  CheckParamUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-10
 */
package com.huawei.ide.interceptors.res.rcm;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * CheckParamUtils
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
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
     * <统一封装参数校验异常>
     * <功能详细描述>
     * @param parameterName 参数名
     * @param parameterVal 参数值
     * @throws CException 异常
     * @see [类、类#方法、类#成员]
     */
    public static void buildParameterInvalid(String parameterName, Object parameterVal)
        throws CException
    {
        StringBuffer strb = new StringBuffer();
        
        strb.append("Parameter named ")
            .append(parameterName)
            .append(" with value [")
            .append(parameterVal)
            .append("] is invalid.");
        
        throw new CException(RCMResultCodeConstants.PARAMETER_INVALID, new Object[] {parameterVal, parameterName});
    }
    
    /**
     * <使用正则表示式校验的通用方法>
     * 
     * @param regex 正则表示式
     * @param value 参数值
     * @return 是否符合要求
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkValueWithRegex(String regex, String value)
    {
        try
        {
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(value).matches();
        }
        catch (PatternSyntaxException e)
        {
            return false;
        }
    }
    
    /**
     * <检查appId字段>
     * 
     * @param appId   值校验,该值必须固定
     * @throws CException 异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkAppKey(String appId)
        throws CException
    {
        if (null == appId)
        {
            buildParameterInvalid("appId", null);
        }
        if (!("hiboard".equals(appId)))
        {
            buildParameterInvalid("appId", appId);
        }
        
    }
    
    /**
     * <检查timestamp字段>
     * @param timestamp 参数值
     * @throws CException 异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkTimestamp(String timestamp)
        throws CException
    {
        if (null == timestamp)
        {
            buildParameterInvalid("timestamp", null);
        }
        else if (!checkStringParameter(timestamp, 10, 0))
        {
            buildParameterInvalid("timestamp", timestamp);
        }
    }
    
    /**
     * <检查String类型参数通用方法>
     *
     * @param str 参数值
     * @param strlen 要求的最大长度
     * @param filedName 待测对象名
     * @param nullable 该字段是否可以为空
     * @throws CException 不可为空为空，或者是不为空但是不符合合法性要求，抛出异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkStringParameter(String str, int strlen, String filedName, boolean nullable)
        throws CException
    {
        if (!nullable && StringUtils.isBlank(str))
        {
            buildParameterInvalid(filedName, str);
        }
        
        if (null != str && str.length() > strlen)
        {
            buildParameterInvalid(filedName, str);
        }
    }
    
    /**
     * <检查String类型参数通用方法>
     *
     * @param str 参数值
     * @param maxlen 要求的最大长度
     * @param minlen 要求的最小长度
     * @param filedName 待测对象名
     * @param nullable 该字段是否可以为空 
     * @throws CException 不可为空为空，或者是不为空但是不符合合法性要求，抛出异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkStringParameter(String str, int maxlen, int minlen, String filedName, boolean nullable)
        throws CException
    {
        if (!nullable && StringUtils.isBlank(str))
        {
            buildParameterInvalid(filedName, str);
        }
        
        if (null != str && !(str.length() >= minlen && str.length() <= maxlen))
        {
            buildParameterInvalid(filedName, str);
        }
    }
    
    /**
     * <检查String类型参数通用方法>
     *
     * @param str 参数值
     * @param strlen 要求的最大长度
     * @return 是否符合要求
     * @see [类、类#方法、类#成员]
     */
    public static Boolean checkStringParameter(String str, int strlen)
    {
        return StringUtils.isNotBlank(str) && str.trim().length() <= strlen;
    }
    
    /**
     * 检查String类型参数通用方法
     *
     * @param str 参数值
     * @param maxlen 要求的最大长度
     * @param minlen 要求的最小长度
     * @return 是否符合要求
     * @see [类、类#方法、类#成员]
     */
    public static Boolean checkStringParameter(String str, int maxlen, int minlen)
    {
        return !StringUtils.isBlank(str) && str.trim().length() <= maxlen && str.trim().length() >= minlen;
    }
    
    /**
     * <检查phoneType>
     * @param phoneType 设备ID类型
     * @param nullable   是否可以为空
     * @throws CException 检查deviceType异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkPhoneType(String phoneType, boolean nullable)
        throws CException
    {
        if (!nullable && null == phoneType)
        {
            buildParameterInvalid("phoneType", null);
        }
        if (null != phoneType && !checkStringParameter(phoneType, 40, 1))
        {
            buildParameterInvalid("phoneType", null);
        }
        
    }
    
    /**
     * <检查deviceID>
     * @param deviceID  设备ID
     * @param nullable  是否可以为空
     * @throws CException  检查deviceID异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkDeviceID(String deviceID, boolean nullable)
        throws CException
    {
        if (null == deviceID)
        {
            buildParameterInvalid("deviceID", null);
        }
        else if (!checkStringParameter(deviceID, 16, 14))
        {
            buildParameterInvalid("deviceID", null);
        }
        
    }
    
    /**
     * <检查请求推荐数>
     * @param reqRcmCount 请求推荐数1
     * @param nullable     是否可以为空
     * @throws CException  检查请求推荐数1异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkReqRcmCount(Integer reqRcmCount, boolean nullable)
        throws CException
    {
        if (!nullable && null == reqRcmCount)
        {
            buildParameterInvalid("reqRcmCount", null);
        }
        else if (reqRcmCount > 0)
        {
            try
            {
                
                NumberUtils.createInteger(reqRcmCount.toString());
                
            }
            catch (NumberFormatException e)
            {
                buildParameterInvalid("reqRcmCount", reqRcmCount.toString());
            }
        }
        else if (reqRcmCount <= 0)
        {
            
            buildParameterInvalid("reqRcmCount", reqRcmCount.toString());
            
        }
        
    }
    
    /**
     * <检查rcmScenario>
     * @param reqFolder  推荐场景
     * @param nullable     是否可以为空
     * @throws CException  检查rcmScenario异常
     * @see [类、类#方法、类#成员]
     */
    public static void checkReqFolder(Integer reqFolder, boolean nullable)
        throws CException
    {
        if (!nullable && null == reqFolder)
        {
            buildParameterInvalid("reqFolder", null);
        }
        ParamCheckerConfigService parConfig = CommonUtils.getFieldValidService();
        if (parConfig != null)
        {
            if (null != reqFolder
                && !checkValueWithRegex(parConfig.getFieldRegex("reqFolder"), String.valueOf(reqFolder)))
            {
                buildParameterInvalid("reqFolder", String.valueOf(reqFolder));
            }
        }
    }
    
}
