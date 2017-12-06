/*
 * 文 件 名:  EventType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  事件类型
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.um.event;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件类型
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-10]
 */
public class EventType
{
    /**
     * 启动任务
     */
    public static final int START_TASK = 1;
    
    /**
     * 停止任务
     */
    public static final int STOP_TASK = 2;
    
    /**
     * 任务停止完成
     */
    public static final int STOPPED_TASK = 3;
    
    /**
     * 更新任务配置
     */
    public static final int UPDATE_TASK = 4;
    
    /**
     * 新增任务
     */
    public static final int ADD_TASK = 5;
    
    /**
     * 删除任务
     */
    public static final int DELETE_TASK = 6;
    
    /**
     * 新增告警阈值
     */
    public static final int ADD_ALARM_THRESHOLD = 7;
    
    /**
     * 更新告警阈值
     */
    public static final int UPDATE_ALARM_THRESHOLD = 8;
    
    /**
     * 删除告警阈值
     */
    public static final int DELETE_ALARM_THRESHOLD = 9;
    
    /**
     * 更新JOB ID
     */
    public static final int UPDATE_JOB_IDS = 10;
    
    /**
     * 新增OS用户
     */
    public static final int ADD_OS_USER = 11;
    
    /**
     * 更新OS用户
     */
    public static final int UPDATE_OS_USER = 12;
    
    /**
     * 删除OS用户
     */
    public static final int DELETE_OS_USER = 13;
    
    /**
     * 更新角色
     */
    public static final int UPDATE_ROLE = 14;
    
    /**
     * 删除角色
     */
    public static final int DELETE_ROLE = 15;
    
    /**
     * 更新操作员
     */
    public static final int UPDATE_OPERATOR = 16;
    
    /**
     * 删除操作员
     */
    public static final int DELETE_OPERATOR = 17;
    
    /**
     * 新增节点
     */
    public static final int ADD_NODE = 18;
    
    /**
     * 更新节点
     */
    public static final int UPDATE_NODE = 19;
    
    /**
     * 删除节点
     */
    public static final int DELETE_NODE = 20;
    
    //---------------周期相关------------//
    /**
     * 周期状态修改
     */
    public static final int CHANGE_TASKRS_STATE = 21;
    
    /**
     * 新增周期状态
     */
    public static final int ADD_TASKRS = 22;
    
    /**
     * 删除周期状态
     */
    public static final int DELETE_TASKRS = 23;
    
    /**
     * 任务开始时间改变
     */
    public static final int TASK_START_TIME_CHANGED = 24;
    
    /**
     * 任务依赖关系改变
     */
    public static final int TASK_DEPEND_RELATION_CHANGED = 25;
    
    /**
     * 任务周期类型或者长度改变
     */
    public static final int TASK_CYCLE_TYPE_LENGTH_CHANGED = 26;
    
    /**
     * 任务添加完成
     */
    public static final int TASK_ADD_FINISHED = 27;
    
    /**
     * 任务删除完成
     */
    public static final int TASK_DELETE_FINISHED = 28;
    
    /**
     * 任务启动完成
     */
    public static final int TASK_START_FINISHED = 29;
    
    /**
     * 任务停止完成
     */
    public static final int TASK_STOP_FINISHED = 30;
    
    /**
     * 任务周期集合状态改变
     */
    public static final int CHANGE_MULTI_TASKRS_STATE = 31;
    
    /**
     * 批量重做时重新初始化任务周期状态
     */
    public static final int RANGE_REINIT_TASKRS_STATE = 32;
    
    /**
     * 添加多个任务周期
     */
    public static final int ADD_MULTI_TASKRS = 33;
    
    /**
     * 集成重做时统一修改周期状态
     */
    public static final int INTEGRATION_CHANGE_TASKRS_STATE = 34;
    
    private static Map<Integer, String> names = new HashMap<Integer, String>();
    
    static
    {
        names.put(EventType.ADD_ALARM_THRESHOLD, "ADD_ALARM_THRESHOLD");
        names.put(EventType.ADD_NODE, "ADD_NODE");
        names.put(EventType.ADD_OS_USER, "ADD_OS_USER");
        names.put(EventType.ADD_TASK, "ADD_TASK");
        names.put(EventType.DELETE_ALARM_THRESHOLD, "DELETE_ALARM_THRESHOLD");
        names.put(EventType.DELETE_NODE, "DELETE_NODE");
        names.put(EventType.DELETE_OPERATOR, "DELETE_OPERATOR");
        names.put(EventType.DELETE_OS_USER, "DELETE_OS_USER");
        names.put(EventType.DELETE_ROLE, "DELETE_ROLE");
        names.put(EventType.DELETE_TASK, "DELETE_TASK");
        names.put(EventType.START_TASK, "START_TASK");
        names.put(EventType.STOP_TASK, "STOP_TASK");
        names.put(EventType.STOPPED_TASK, "STOPPED_TASK");
        names.put(EventType.UPDATE_TASK, "UPDATE_TASK");
        names.put(EventType.UPDATE_ALARM_THRESHOLD, "UPDATE_ALARM_THRESHOLD");
        names.put(EventType.UPDATE_JOB_IDS, "UPDATE_JOB_IDS");
        names.put(EventType.UPDATE_NODE, "UPDATE_NODE");
        names.put(EventType.UPDATE_OPERATOR, "UPDATE_OPERATOR");
        names.put(EventType.UPDATE_OS_USER, "UPDATE_OS_USER");
        names.put(EventType.UPDATE_ROLE, "UPDATE_ROLE");
        
        names.put(EventType.CHANGE_TASKRS_STATE, "CHANGE_TASKRS_STATE");
        names.put(EventType.ADD_TASKRS, "ADD_TASKRS");
        names.put(EventType.DELETE_TASKRS, "DELETE_TASKRS");
        names.put(EventType.TASK_START_TIME_CHANGED, "TASK_START_TIME_CHANGED");
        names.put(EventType.TASK_DEPEND_RELATION_CHANGED, "TASK_DEPEND_RELATION_CHANGED");
        names.put(EventType.TASK_CYCLE_TYPE_LENGTH_CHANGED, "TASK_CYCLE_TYPE_LENGTH_CHANGED");
        names.put(EventType.TASK_ADD_FINISHED, "TASK_ADD_FINISHED");
        names.put(EventType.TASK_DELETE_FINISHED, "TASK_DELETE_FINISHED");
        names.put(EventType.TASK_START_FINISHED, "TASK_START_FINISHED");
        names.put(EventType.TASK_STOP_FINISHED, "TASK_STOP_FINISHED");
        names.put(EventType.CHANGE_MULTI_TASKRS_STATE, "CHANGE_MULTI_TASKRS_STATE");
        names.put(EventType.RANGE_REINIT_TASKRS_STATE, "RANGE_REINIT_TASKRS_STATE");
        names.put(EventType.ADD_MULTI_TASKRS, "ADD_MULTI_TASKRS");
        names.put(EventType.INTEGRATION_CHANGE_TASKRS_STATE, "INTEGRATION_CHANGE_TASKRS_STATE");
    }
    
    /**
     * 获取类型字符串
     * @param eventType 时间类型
     * @return 类型字符串
     */
    public static String toString(int eventType)
    {
        return names.get(eventType);
    }
}
