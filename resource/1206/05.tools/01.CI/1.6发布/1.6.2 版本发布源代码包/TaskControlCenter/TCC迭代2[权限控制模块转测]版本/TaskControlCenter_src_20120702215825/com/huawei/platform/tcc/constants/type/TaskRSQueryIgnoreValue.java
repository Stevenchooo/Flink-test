/*
 * 文 件 名:  TaskRSQueryIgnoreValue.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.constants.type;

import java.util.Date;

/**
 * 从页面传递过来的查询参数中，忽略该字段对应的取值
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class TaskRSQueryIgnoreValue
{
    /**
     * 分页开始索引
     */
    public static final Long START_INDEX_IGNORE = 0L;
    
    /**
     * 行数
     */
    public static final Integer ROWS_IGNORE = Integer.MAX_VALUE;
    
    /**
     * 开始时间
     */
    public static final Date START_TIME_IGNORE = null;
    
    /**
     * 结束时间
     */
    public static final Date END_TIME_IGNORE = null;
    
    /**
     * 状态
     */
    public static final Integer STATE_IGNORE = RunningState.ALL;
    
    /**
     * 任务Id
     */
    public static final Long TASK_ID_INGORE = 0L;
}
