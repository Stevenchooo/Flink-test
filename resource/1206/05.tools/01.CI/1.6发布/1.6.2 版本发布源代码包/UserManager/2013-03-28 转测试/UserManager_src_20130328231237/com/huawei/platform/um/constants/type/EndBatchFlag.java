/*
 * 文 件 名:  EndBatchFlag.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.um.constants.type;

/**
 * 批任务结束标识
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-21]
 * @see  [相关类/方法]
 */
public class EndBatchFlag
{
    /**
     * 普通方式(任务执行逻辑结束)
     */
    public static final int NORMAL = 0;
    /**
     * 指定的输入文件处理处理完成
     */
    public static final int FILES = 1;
    /**
     * 等待时间内输入的文件处理完成
     */
    public static final int FILESINWAITTING = 2;
    /**
     * 超过等待时间，且最少处理N个文件
     */
    public static final int NINWAITTING = 3;
    
    /**
     * 超过等待时间或者最少处理N个文件
     */
    public static final int NORWAITTING = 4;
}
