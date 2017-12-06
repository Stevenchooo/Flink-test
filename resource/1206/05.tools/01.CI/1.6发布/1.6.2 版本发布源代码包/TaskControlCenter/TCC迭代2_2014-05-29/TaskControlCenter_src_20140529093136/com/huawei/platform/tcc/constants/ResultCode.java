/*
 * 文 件 名:  ResultCode.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  TCC的错误码定义常量类
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-18
 */
package com.huawei.platform.tcc.constants;

/**
 * TCC的错误码定义常量类
 * 
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-18]
 */
public class ResultCode
{
    /**成功*/
    public static final int SUCCESS = 0;
    
    /**系统错误*/
    public static final int SYSTEM_ERROR = 1001;
    
    /**消息格式不合法*/
    public static final int REQUEST_FORMAT_ERROR = 1002;
    
    /**输入无效,包含非法字符*/
    public static final int INPUT_INVALID = 1003;
    
    /**用户名密码认证失败 */
    public static final int AUTH_FAILED = 1005;
    
    /**参数不合法*/
    public static final int PARAMETER_INVALID = 1007;
    
    /**权限不足 */
    public static final int NO_ENOUGH_PREVILEGE = 1006;
    
    /**
     * 转换周期Id为日期错误
     */
    public static final int PARSE_CYCLEID_DATE_ERROR = 870002010;
    
    /**
     * 执行远程命令错误
     */
    public static final int EXEC_REMOTE_COMMAND_ERROR = 870002012;
    
    /**
     * 指定任务的周期不存在
     */
    public static final int TASK_CYCLE_NOT_EXIST_ERROR = 870002013;
    
    /**
     * 依赖关系错误
     */
    public static final int TASK_DEPEND_REALATION_ERROR = 870002015;
    
    /**
     * 生成任务ID编号错误
     */
    public static final int GENERATE_TASKID_ERROR = 870002016;
}
