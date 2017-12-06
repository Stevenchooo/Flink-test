/*
 * 文 件 名:  ResultCode.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  TCC的错误码定义常量类
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-18
 */
package com.huawei.platform.um.constants;

/**
 * TCC的错误码定义常量类
 * 
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2013-2-18]
 */
public class ResultCode
{
    /**成功*/
    public static final int SUCCESS = 0;
    
    /**系统错误*/
    public static final int SYSTEM_ERROR = 1001;
    
    /**消息格式不合法*/
    public static final int REQUEST_FORMAT_ERROR = 1002;
    
    /**用户名密码认证失败 */
    public static final int AUTH_FAILED = 1005;
    
    /**权限不足 */
    public static final int NO_ENOUGH_PREVILEGE = 1006;
    
    /**参数不合法*/
    public static final int PARAMETER_INVALID = 1007;
    
    /**主机不存在 */
    public static final int HOST_NO_EXIST = 1008;
    
    /**连接超时 */
    public static final int CONNECT_TIMEOUT = 1009;
    
    /**权限拒绝 */
    public static final int PERMISSION_DENIED = 1010;
    
    /**
     * 用户不存在
     */
    public static final int USER_NOT_EXIST = 1011;
}
