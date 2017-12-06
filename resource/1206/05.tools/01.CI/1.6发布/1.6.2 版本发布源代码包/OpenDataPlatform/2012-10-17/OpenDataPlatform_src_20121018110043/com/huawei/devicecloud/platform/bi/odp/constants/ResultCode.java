/*
 * 文 件 名:  ResultCode.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  Odp的错误码定义常量类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.constants;

/**
 * Odp的错误码定义常量类
 * 
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class ResultCode
{
    /**成功*/
    public static final int SUCCESS = 0;
    
    /**系统错误*/
    public static final int SYSTEM_ERROR = 1001;
    
    /**消息头认证失败 */
    public static final int AUTH_FAILED = 1005;
    
    /**参数不合法*/
    public static final int PARAMETER_INVALID = 1007;
    
    /**流控限制*/
    public static final int FLOW_CONTROL_LIMIT = 1008;
    
    /**消息格式不合法*/
    public static final int REQUEST_FORMAT_ERROR = 1002;
    
    /**权限不足 */
    public static final int NO_ENOUGH_PREVILEGE = 1006;
    
    /**SQL执行错误*/
    public static final int SQL_EXEC_ERROR = 1003;
    
    /**列不存在*/
    public static final int COLUMN_NOT_EXIST = 1004;
    
    /**预留数据操作没有结束*/
    public static final int RESERVE_NO_END = 1009;
    
    /**预留信息不存在*/
    public static final int RESERVE_NO_EXIST = 1010;
    
    /** 配置不存在 */
    public static final int CONFIG_NOT_EXIST = 1011;
    
    /** 控制状态配置不存在 */
    public static final int CONTROL_FLAG_NOT_EXIST = 1012;
    
    /**写文件错误*/
    public static final int WRITE_FILE_ERROR = 1013;
    
    /**用户标识错误*/
    public static final int USER_AD_FLAG_ERROR = 1014;
}
