/*
 * 文 件 名:  OperType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-7-2
 */
package com.huawei.platform.tcc.constants.type;


/**
 * 任务类型
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-7-2]
 * @see  [相关类/方法]
 */
public class OperType
{    
    /**
     * 选择全部类型
     */
    public static final int ALL = 0;
    
    /**
     * 新增任务
     */
    public static final int TASK_ADD = 111;
    
    /**
     * 修改任务
     */
    public static final int TASK_MODIFY = 112;
    
    /**
     * 删除任务
     */
    public static final int TASK_DELETE = 113;
    
    /**
     * 启动任务
     */
    public static final int TASK_START = 114;
    
    /**
     * 停止任务
     */
    public static final int TASK_STOP = 115;
    
//    /**
//     * 修改任务的业务以及任务组
//     */    
//    public static final int TASK_MODIFYSERVICETG = 116;
    
    /**
     * 任务批量重做
     */
    public static final int TASK_BATCH_REDO = 117;

    /**
     * 新增任务步骤
     */
    public static final int TASKSTEP_ADD = 121;
    
    /**
     * 修改任务步骤
     */
    public static final int TASKSTEP_MODIFY = 122;
    
    /**
     * 删除任务步骤
     */
    public static final int TASKSTEP_DELETE = 123;
    
    /**
     * 启动任务步骤
     */
    public static final int TASKSTEP_START = 124;
    
    /**
     * 停止任务步骤
     */
    public static final int TASKSTEP_STOP = 125;
    
    /**
     * 交换任务步骤
     */
    public static final int TASKSTEP_EXCHANGE = 126;
    
    /**
     * 任务周期重做
     */
    public static final int TASKCYCLE_REDO = 131;
    
    /**
     * 任务周期批量重做
     */
    public static final int TASKCYCLE_BATCH_REDO = 132;

    /**
     * 任务周期集成重做
     */
    public static final int TASKCYCLE_INTEGRATED_REDO = 133;
    
//    /**
//     * 修改依赖关系
//     */
//    public static final int DEPEND_MODIFY = 141;
    
    /**
     * 创建用户
     */
    public static final int USER_ADD = 211;
    
    /**
     * 删除用户
     */
    public static final int USER_DELETE = 212;
    
    /**
     * 修改用户
     */
    public static final int USER_MODIFY = 213;
    
    /**
     * 增加角色
     */
    public static final int ROLE_ADD = 221;
    
    /**
     * 删除角色
     */
    public static final int ROLE_DELETE = 222;
    
    /**
     * 修改角色
     */
    public static final int ROLE_MODIFY = 223;
    
    /**
     * 绑定角色
     */
    public static final int ROLE_BIND = 224;
    
    /**
     * 增加OS用户
     */
    public static final int OSUSER_ADD = 231;
    
    /**
     * 删除OS用户
     */
    public static final int OSUSER_DELETE = 232;
    
    /**
     * 修改OS用户
     */
    public static final int OSUSER_MODIFY = 233;
    
    /**
     * 增加业务
     */
    public static final int SERVICE_ADD = 241;
    
    /**
     * 删除业务
     */
    public static final int SERVICE_DELETE = 242;
    
    /**
     * 修改业务
     */
    public static final int SERVICE_MODIFY = 243;
    
    /**
     * 更新业务部署信息
     */
    public static final int SERVICEDEPLOY_UPDATE = 244;
    
    /**
     * 增加任务组
     */
    public static final int TASKGROUP_ADD = 251;
    
    /**
     * 删除任务组
     */
    public static final int TASKGROUP_DELETE = 252;
    
    /**
     * 修改任务组
     */
    public static final int TASKGROUP_MODIFY = 253;
    
    /**
     * 修改密码
     */
    public static final int MODIFYPWD = 311;
    
    /**
     * 登陆
     */
    public static final int LOGIN = 312;
    
    /**
     * 登出
     */
    public static final int LOGOUT = 313;
    
    /**
     * 修改tcc配置
     */
    public static final int TCCCONFIG_MODIFY = 314;
    
    /**
     * 重启tcc
     */
    public static final int REBOOT_TCC = 315;
}
