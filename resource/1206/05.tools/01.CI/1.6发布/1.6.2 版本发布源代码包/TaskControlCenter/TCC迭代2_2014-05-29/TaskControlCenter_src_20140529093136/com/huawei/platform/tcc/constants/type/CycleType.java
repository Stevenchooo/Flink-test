/*
 * 文 件 名:  CycleType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.tcc.constants.type;

import java.util.Calendar;

/**
 * 周期类型
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-21]
 * @see  [相关类/方法]
 */
public class CycleType
{
    /**
     * 分钟
     */
    public static final int MINUTE = 1;
    
    /**
     * 小时
     */
    public static final int HOUR = 60 * MINUTE;
    
    /**
     * 天
     */
    public static final int DAY = 24 * HOUR;
    
    /**
     * 月
     */
    public static final int MONTH = 30 * DAY;
    
    /**
     * 年
     */
    public static final int YEAR = 12 * MONTH;
    
    /**
     * 将周期类型转换为CalendarType
     * 
     * @param cycleType 周期类型
     * @return 转换为CalendarType
     */
    public static int toCalendarType(int cycleType)
    {
        switch (cycleType)
        {
            case CycleType.YEAR:
                return Calendar.YEAR;
            case CycleType.MONTH:
                return Calendar.MONTH;
            case CycleType.DAY:
                return Calendar.DAY_OF_MONTH;
            case CycleType.MINUTE:
                return Calendar.MINUTE;
            default:
                return Calendar.HOUR_OF_DAY;
        }
    }
    
    /**
     * 将周期类型的字符表示转换为整数表示
     * 
     * @param cycleType 周期类型
     * @return 周期类型的字符表示转换为整数表示
     * @see [类、类#方法、类#成员]
     */
    public static int toCycleType(String cycleType)
    {
        String cycleTypeLower = cycleType.toLowerCase();
        if ("y".endsWith(cycleTypeLower))
        {
            return CycleType.YEAR;
        }
        else if ("m".endsWith(cycleTypeLower))
        {
            return CycleType.MONTH;
        }
        else if ("d".endsWith(cycleTypeLower))
        {
            return CycleType.DAY;
        }
        else if ("i".endsWith(cycleTypeLower))
        {
            return CycleType.MINUTE;
        }
        else
        {
            return CycleType.HOUR;
        }
    }
}
