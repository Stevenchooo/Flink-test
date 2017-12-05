/*
 * 文 件 名:  RCMResultCodeConstants.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2011-10-14
 */
package com.huawei.ide.interceptors.res.rcm;

/**
 * 
 * RCMResultCodeConstants <功能详细描述>
 * 
 * @author cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see [相关类/方法]
 */
public class RCMResultCodeConstants
{
    // ===================内部错误 8700014XX begin================
    /** 系统错误 */
    public static final int SYSTEM_ERROR = 500;

    /**
     * 接口调用过程鉴权失败，会话失效且鉴权码错误
     */
    public static final int SERVICE_AUTH_FAILED = 401;

    /** 参数不合法 */
    public static final int PARAMETER_INVALID = 1;

    /** 消息格式不合法 */
    public static final int REQUEST_FORMAT_ERROR = 1;

    /** APPID不合法 */
    public static final int APPID_INVALID = 401;

    /** redis读取数据为null **/
    public static final int REDIS_MULL_ERROR = 003;

    /**
     * REDIS_CONNECT_ERROR
     */
    public static final int REDIS_CONNECT_ERROR = 004;

    /**
     * REDIS_SDK_ERROR
     */
    public static final int REDIS_SDK_ERROR = 005;

    /**
     * SYS_ERROR
     */
    public static final String SYS_ERROR = "101";

    /**
     * INVALID_APPSECRET(鉴权出了问题)
     */
    public static final String INVALID_APPSECRET = "31";

    /**
     * 不合法的时间戳
     */
    public static final String INVALID_TIMESTAMP = "5";

    /**
     * REDIST_ERROR
     */
    public static final String REDIST_ERROR = "6";

    /**
     * JSON_FORMAT_ERROR
     */
    public static final String JSON_FORMAT_ERROR = "15";

    /**
     * appKey不合法
     */
    public static final String INVALID_APPKEY_ERROR = "11";

    /**
     * 设备Id不合法
     */
    public static final String INVALID_DEVICE_ID_ERROR = "10";

    /**
     * reqId不合法
     */
    public static final String INVALID_REQ_ID_ERROR = "33";

    /**
     * rcmScenario不合法
     */
    public static final String INVALID_RCM_SCENARIO = "9";
}
