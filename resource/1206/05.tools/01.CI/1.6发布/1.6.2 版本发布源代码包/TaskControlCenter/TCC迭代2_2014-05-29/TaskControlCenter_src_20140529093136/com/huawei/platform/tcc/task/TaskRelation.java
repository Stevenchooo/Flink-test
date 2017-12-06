/*
 * 文 件 名:  TaskRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务依赖关系
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.tcc.task;

/**
 * 任务依赖关系
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-10]
 */
public class TaskRelation
{
    //源任务
    private Task srcTask;
    
    //目标任务
    private Task dstTask;
    
    //是否周期依赖
    private boolean fullCycleDepend;
    
    //是否忽略错误
    private boolean ignoreError;
    
    /**
     * 构造函数
     * @param srcTask 源任务
     * @param dstTask 目标任务
     * @param fullCycleDepend 全周期依赖
     * @param ignoreError 是否忽略错误
     */
    public TaskRelation(Task srcTask, Task dstTask, boolean fullCycleDepend, boolean ignoreError)
    {
        this.srcTask = srcTask;
        this.dstTask = dstTask;
        this.fullCycleDepend = fullCycleDepend;
        this.ignoreError = ignoreError;
    }

    public Task getSrcTask()
    {
        return srcTask;
    }

    public Task getDstTask()
    {
        return dstTask;
    }

    public boolean isFullCycleDepend()
    {
        return fullCycleDepend;
    }

    public boolean isIgnoreError()
    {
        return ignoreError;
    }
}
