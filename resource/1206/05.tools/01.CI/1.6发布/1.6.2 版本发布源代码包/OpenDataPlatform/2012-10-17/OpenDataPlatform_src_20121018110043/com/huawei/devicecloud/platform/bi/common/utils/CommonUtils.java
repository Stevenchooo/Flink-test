/*
 * 文 件 名:  CommonUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-9
 */
package com.huawei.devicecloud.platform.bi.common.utils;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.common.exmapping.ErrorDescConfigService;
import com.huawei.devicecloud.platform.bi.common.exmapping.OdpSysConfigService;
import com.huawei.devicecloud.platform.bi.common.exmapping.OdpSysLangService;
import com.huawei.devicecloud.platform.bi.common.validate.config.init.ParamCheckerConfigService;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;

/**
 * 
 * 通用工具类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class CommonUtils
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    
    //应用上下文
    private static ApplicationContext appContext;
    
    //私有构造函数
    private CommonUtils()
    {
    }
    
    /**
     * 获取指定Bean
     * @param beanID Bean定义的唯一标识,仅适用于web项目中
     * @return 指定名称Bean实例
     */
    public static Object getBeanByID(String beanID)
    {
        if (StringUtils.isBlank(beanID))
        {
            return null;
        }
        
        //获取上下文
        if (null == appContext)
        {
            appContext = ContextLoader.getCurrentWebApplicationContext();
        }
        
        if (null != appContext)
        {
            return appContext.getBean(beanID);
        }
        
        return null;
    }
    
    /**
     * 获取当前时间
     * 
     * @return 时间
     */
    public static Timestamp getCrruentTime()
    {
        
        return new Timestamp(new Date().getTime());
    }
    
    /**
     * 根据异常码获取异常描述信息
     * @param errorCode 异常码
     * @param fmtObjects 待格式化的异常描述信息
     * @return 异常描述信息
     * @see [类、类#方法、类#成员]
     */
    public static String getErrorDesc(int errorCode, Object... fmtObjects)
    {
        //资源文件获取配置的异常信息
        String cfgErrorDesc = getErrorDescFromResource(errorCode);
        
        //如果未配置指定异常码对应的异常描述信息
        if (StringUtils.isBlank(cfgErrorDesc))
        {
            cfgErrorDesc = "Unkown exception or error occured.";
        }
        
        //没有需要合入并格式化的参数信息则直接返回资源文件定义的异常信息描述
        if ((null == fmtObjects) || (fmtObjects.length < 1))
        {
            return cfgErrorDesc;
        }
        
        return MessageFormat.format(cfgErrorDesc, fmtObjects);
    }
    
    /** 
     * 从错误码资源文件获取指定错误码对应的错误描述信息
     * @param errorCode 异常错误码
     * @return 配置的错误描述信息
     * @see [类、类#方法、类#成员]
     */
    private static String getErrorDescFromResource(int errorCode)
    {
        ErrorDescConfigService exDefServ = (ErrorDescConfigService)getBeanByID("exDefConfigService");
        
        return (null == exDefServ) ? null : exDefServ.getExceptionDesc(String.valueOf(errorCode));
    }
    
    /** 
     * 从系统配置文件中获取配置项信息
     * @param key 配置项key值
     * @return 配置信息
     */
    public static String getSysConfigValueByKey(String key)
    {
        OdpSysConfigService sysConfigService = (OdpSysConfigService)getBeanByID("odpSysConfigService");
        
        return (null == sysConfigService) ? null : sysConfigService.getSysConfigKey(key);
    }
    
    /**
     * 根据列枚举值获取列名
     * @param columnEnum 类枚举值
     * @return 字段名
     */
    public static String getColumnName(int columnEnum)
    {
        OdpSysConfigService sysConfigService = (OdpSysConfigService)getBeanByID("odpSysConfigService");
        
        return (null == sysConfigService) ? null : sysConfigService.getColumnName(columnEnum);
    }
    
    /**
     * 
     * 从参数列表中获取指定类型的参数实例
     * @param <T> 泛型类型
     * @param targetClazz 待获取参数的类型
     * @param objects 参数数组
     * @return 符合类型要求的参数实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSpecifiedArgument(Class<T> targetClazz, Object... objects)
    {
        if ((null == objects) || (objects.length < 1))
        {
            return null;
        }
        
        //循环比对第一个匹配指定类型的参数
        for (Object curObj : objects)
        {
            if (null == curObj)
            {
                return null;
            }
            if (curObj.getClass().equals(targetClazz))
            {
                return (T)curObj;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * 从参数列表中获取指定类型的参数实例，支持多个参数中有重复类型
     * 可指定参数索引进行获取
     * @param <T> 泛型类型
     * @param targetClazz 待获取参数的类型
     * @param index 指定参数的索引值
     * @param objects 参数数组
     * @return 符合类型要求的参数实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSpecifiedArgument(Class<T> targetClazz, int index, Object... objects)
    {
        if ((null == objects) || (objects.length < 1))
        {
            return null;
        }
        
        if ((index < 0) || (index > (objects.length - 1)))
        {
            throw new IllegalArgumentException("Invalid index for arguments,value is " + index);
        }
        
        //获取index位置的参数
        Object targetObj = objects[index];
        
        if (targetObj.getClass().equals(targetClazz))
        {
            return (T)targetObj;
        }
        
        return null;
    }
    
    public static ParamCheckerConfigService getFieldValidService()
    {
        return (ParamCheckerConfigService)getBeanByID("fieldValidateCfgService");
    }
    
    /**
     * 将String类型的时间按照指定格式转成Date类型
     * 
     * @param time 时间 如：2011-08-17T15:46:10+0800
     * @param timeFormat 时间格式
     * @return 转化后的时间
     * @throws CException 异常
     */
    public static Date convertDateTimeFormat(String time, String timeFormat)
        throws CException
    {
        String tFormat = timeFormat;
        if (StringUtils.isBlank(time))
        {
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        
        //将2011-08-17T15:46:10+08:00 （这种不好解析）手动转成2011-08-17T15:46:10+0800
        if (StringUtils.isBlank(tFormat))
        {
            tFormat = "yyyy-MM-dd'T'HH:mm:ssZZ";
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date date = null;
        try
        {
            sdf.applyPattern(tFormat);
            date = sdf.parse(time);
        }
        catch (Exception e)
        {
            LOGGER.error("convert time {} according to patter {} error", new Object[] {time, tFormat}, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return date;
    }
    
    /**
     * 将Date类型的时间按照指定格式转成String类型
     * 
     * @param time 时间 
     * @param timeFormat 时间格式
     * @return 转化后的时间  如：2011-08-17T15:46:10+0800
     * @throws CException 异常
     */
    public static String convertDateTimeFormat(Date time, String timeFormat)
        throws CException
    {
        String tFormat = timeFormat;
        if (null == time)
        {
            return null;
        }
        if (StringUtils.isBlank(tFormat))
        {
            tFormat = "yyyy-MM-dd'T'HH:mm:ssZZ";
        }
        
        String date = null;
        try
        {
            //日期格式化
            date = DateFormatUtils.format(time, tFormat);
        }
        catch (Exception e)
        {
            LOGGER.error("convert time {} according to patter {} error", new Object[] {time, tFormat}, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return date;
    }
    
    /**
     * <获取语言名>
     * @param langCode 语言码
     * @return 用户类型
     */
    public static String getLangName(String langCode)
    {
        return ((OdpSysLangService)getBeanByID("odpSysLangService")).getLocaleByLangCode(langCode);
    }
    
    /**
     * 判断一个集合是否为空
     * @param collect 输入的集合
     * @return 返回是否为空的bool
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEmptyObject(Collection<?> collect)
    {
        return !((null == collect) || collect.isEmpty());
    }
    
    /**
     * 获取配置项值
     * @param key 键
     * @return 配置项值
     */
    public static String getSysConfig(String key)
    {
        OdpSysConfigService sysConfigService = OdpSysConfigService.createConfigService();
        return (sysConfigService != null) ? sysConfigService.getSysConfigKey(key) : null;
    }
    
    public static ApplicationContext getAppContext()
    {
        return appContext;
    }

    public static void setAppContext(ApplicationContext appContext)
    {
        CommonUtils.appContext = appContext;
    }
}
