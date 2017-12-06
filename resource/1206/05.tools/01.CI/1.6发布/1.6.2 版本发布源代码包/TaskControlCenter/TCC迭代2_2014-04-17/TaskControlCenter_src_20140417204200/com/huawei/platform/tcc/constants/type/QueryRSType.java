/*
 * 文 件 名:  QueryRSType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.tcc.constants.type;


/**
 * 查询运行状态类别
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-21]
 * @see  [相关类/方法]
 */
public class QueryRSType
{
    /**
     * 周期运行状态
     */
    public static final int CYCLE = 1;
    
    /**
     * 批次运行状态
     */
    public static final int BATCH = 2;
    
    /**
     * 步骤运行状态
     */
    public static final int STEP = 3;
    
    /**
     * 依赖任务周期运行状态
     */
    public static final int DEPEND_CYCLE = 4;
}
