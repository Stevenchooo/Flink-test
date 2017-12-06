/*
 * 文 件 名:  AlarmType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.um.constants.type;

/**
 * 告警类型
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2011-12-21]
 */
public class AlarmType
{
    /**
     * 1)   任务失败（严重）
     */
    public static final int ERROR = 1;
    
    /**
     * 2)   任务发生重做 (一般)
     */
    public static final int REDO = 2;
    
    /**
     * 3)   任务执行时间超时（严重）
     */
    public static final int EXEC_TIME_TIMEOUT = 3;
    
    /**
     * 4)   任务到达最迟启动时间时仍未启动（严重）
     */
    public static final int LATEST_START_TIME_NOT_START = 4;
    
    /**
     * 5)    任务到达最迟结束时间时仍未结束（严重）
     */
    public static final int LATEST_END_TIME_NOT_END = 5;
    
    /**
     * 6)   忽略依赖任务（弱依赖）的错误后启动任务(一般)
     */
    public static final int IGNORE_ERROR_START = 6;
    
    /**
     * 7)    Hadoop资源不足(一般)
     */
    public static final int NO_ENOUGH_RESOURCE = 7;
    
    /**
     * 8)   文件未到达就执行ods任务（严重）
     */
    public static final int NOBATCH = 8;
    
    /**
     * 9)   步骤执行超时反馈成任务周期超时（严重）
     */
    public static final int TIMEOUT = 9;
    
    /**
     * 获取严重告警级别的告警类型字符串
     * @return 严重告警级别的告警类型字符串
     */
    public static String getSevereAlarmType()
    {
        StringBuilder sbAlarmType = new StringBuilder("0");
        for (int i = ERROR; i <= TIMEOUT; i++)
        {
            if (GradeType.SEVERE == GradeType.getGrade(i))
            {
                sbAlarmType.append("1");
            }
            else
            {
                sbAlarmType.append("0");
            }
        }
        return sbAlarmType.toString();
    }
}