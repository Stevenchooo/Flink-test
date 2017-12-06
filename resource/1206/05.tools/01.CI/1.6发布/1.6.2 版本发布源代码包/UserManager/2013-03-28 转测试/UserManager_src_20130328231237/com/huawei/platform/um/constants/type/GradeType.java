/*
 * 文 件 名:  AlarmType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.um.constants.type;

/**
 * 等级类型
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-21]
 */
public class GradeType
{
    /**
     * 一般
     */
    public static final int NORMAL = 1;
    
    /**
     * 严重
     */
    public static final int SEVERE = 2;
    
    /**
     * 获取告警类型等级
     * @param alarmType 告警类型
     * @return 告警类型等级
     */
    public static int getGrade(int alarmType)
    {
        switch (alarmType)
        {
            case AlarmType.REDO:
            case AlarmType.IGNORE_ERROR_START:
            case AlarmType.NO_ENOUGH_RESOURCE:
                return NORMAL;
            default:
                return SEVERE;
        }
    }
}