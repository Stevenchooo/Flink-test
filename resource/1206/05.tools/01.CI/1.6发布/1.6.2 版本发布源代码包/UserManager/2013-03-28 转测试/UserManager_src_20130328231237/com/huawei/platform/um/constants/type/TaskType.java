/*
 * 文 件 名:  TaskType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.um.constants.type;


/**
 * 任务类型
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-21]
 * @see  [相关类/方法]
 */
public class TaskType
{
    /**
     * 任务类型编号的默认长度
     */
    public static final int LENGTH = 2;
    
    /**
     * 选择全部类型
     */
    public static final int ALL = 0;
    
    /**
     * 数据文件导入ODS
     */
    public static final int FILE2ODS = 1;
    
    /**
     * ODS转换到DW
     */
    public static final int ODS2DW = 2;
    
    /**
     * ODS转换到DIM
     */
    public static final int ODS2DIM = 3;
    
    /**
     * DW内部转换
     */
    public static final int DW2DW = 4;
    
    /**
     * DW转换到DM
     */
    public static final int DW2DM = 5;
    
    /**
     * DW统计输出报表
     */
    
    public static final int DW2RPT = 6;
    
    /**
     * 报表导出文件
     */
    public static final int RPT2FILE = 7;
    
    /**
     * DW导出文件
     */
    public static final int DW2FILE = 8;
    
    /**
     * 混合型
     */
    public static final int MIX = 9;
}
