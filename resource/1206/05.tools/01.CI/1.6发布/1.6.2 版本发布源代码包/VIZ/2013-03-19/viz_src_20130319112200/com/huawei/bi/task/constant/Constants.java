/**
 * @(#)Constants.java 1.0 Nov 26, 2011
 * @Copyright:  Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * @Description: 
 * 
 * Modification History:
 * Date:        Nov 26, 2011
 * Author:      ZhangFeng 605801
 * Version:     HIBI V1.1
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package com.huawei.bi.task.constant;

/**
 * 常量类
 * Copyright:   Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * Date:        Dec 14, 2011
 * Author:      ZhangFeng 605801
 * Version:     HIBI V1.1
 * Description: Initialize
 */
public class Constants
{
    public static final String PKILL_CMD = "./killtree.sh %d";
    
    /**
     * /bin/sh/
     */
    public static final String BIN_SH = "/bin/sh";
    
    /**
     * -c
     */
    public static final String HIVE_C = "-c";
    
    /**
     * -c
     */
    public static final String SH_C = "-c";
    
    /**
     * 用户不存在
     */
    public static final String USER_NOT_EXIST = "su: user %s does not exist";
    
    /**
     * 密码错误
     */
    public static final String PASSWORD_ERROR = "su: incorrect password";
    
    /**
     * 请求删除PID的前缀
     */
    public static final String REQUEST_KILL_PID = "REQUEST_KILL_PID:";
    
    /**
     * expect交互式命令
     */
    public static final String SUDO = "sudo";
    
    /**
     * expect交互式命令参数
     */
    public static final String SUDO_U = "-u";
    
    /**
     * 配置文件所在的目录
     */
    public static final String XMLPATH = Constants.class.getResource("/").getFile();
    
    /**
     * systemconf文件存放的路径
     */
    public static final String SYS_CONFIGPATH = XMLPATH + "../conf/remoteshell/systemconf.xml";
    
    /**
     * Brainpower模块对应的日志
     */
    public static final String BRANIPOWER_LOG = "Brainpower";
    
    /**
     * 错误级别日志
     */
    public static final String ERROR_LOG = "error";
    
    /**
     * info级别日志
     */
    public static final String INFO_LOG = "info";
    
    /**
     * 1024
     */
    public static final int READFILE_SIZE = 1024;
    
    /**
     * linux服务器ip
     */
    public static final String LINUX_IP = "ip";
    
    /**
     * linux服务器用户
     */
    public static final String LINUX_USER = "user";
    
    /**
     * linux服务器密码
     */
    public static final String LINUX_PASSWORD = "password";
    
    /**
     * 编码格式
     */
    public static final String CHARSET = "utf-8";
    
    /**
     * 导出的记录数
     */
    public static final String EXPORTED_RECORDS = "exported";
    
    /**
     * 返回状态码
     */
    public static final String EXITSTATUES = "exitstatus";
    
    /**
     * 返回内容
     */
    public static final String EXITCONTENT = "exitcontent";
    
    /**
     * 连接超时的时间
     */
    public static final int TIME_OUT = 1000 * 5 * 60;
    
    /**
     * 50
     */
    public static final int FIFTY = 50;
    
    /**
     * 3
     */
    public static final int THREE = 3;
    
    /**
     * 命令类型 shell
     */
    public static final String SHELL_TYPE = "shell";
    
    /**
     * 操作类型
     */
    public static final String DATA_TYPE = "exec-hive.sh";
    
    /**
     * =
     */
    public static final String EQUAL_SIGN = "=";
    
    /**
     * \n
     */
    public static final String LINE_FEED = "\n";
    
    /**
     * ,
     */
    public static final String COMMA = ",";
    
    /**
     * "Starting Job"
     */
    public static final String START_JOB = "Starting Job";
    
    /**
     * "Exception in thread"
     */
    public static final String EXCEPTION_IN = "Exception in thread";
    
    /**
     * input source
     */
    public static final String INPUT_SRC = "input source";
    
    /**
     * output source
     */
    public static final String OUTPUT_SRC = "output source";
    
    /**
     * :::
     */
    public static final String COLON_SPLIT = ":::";
    
    /**
     * 命令类型 ls
     */
    public static final String LS_TYPE = "ls";
    
    /**
     * 命令类型kill
     */
    public static final String KILL_TYPE = "kill";
    
    /**
     * sqoop命令
     */
    public static final String SQOOP_TYPE = "sqoop";
    
}
