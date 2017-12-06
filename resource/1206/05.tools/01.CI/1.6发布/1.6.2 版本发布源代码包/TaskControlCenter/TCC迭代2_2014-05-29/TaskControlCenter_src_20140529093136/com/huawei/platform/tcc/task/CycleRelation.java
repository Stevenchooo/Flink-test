/*
 * 文 件 名:  CycleRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务周期关系【依赖】
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-21
 */
package com.huawei.platform.tcc.task;

import java.util.List;

import com.huawei.platform.tcc.entity.TaskRunningStateEntity;

/**
 * 周期关系【依赖】
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-21]
 */
public class CycleRelation
{
    /**
     * 任务运行状态
     */
    private TaskRunningStateEntity taskRS;
    
    /**
     * 前驱列表【直接正向依赖】
     */
    private List<TaskRunningStateEntity> preDependents;
    
    /**
     * 后继列表【直接反向依赖】
     */
    private List<TaskRunningStateEntity> subDependents;

    public TaskRunningStateEntity getTaskRS()
    {
        return taskRS;
    }

    public void setTaskRS(TaskRunningStateEntity taskRS)
    {
        this.taskRS = taskRS;
    }

    public List<TaskRunningStateEntity> getPreDependents()
    {
        return preDependents;
    }

    public void setPreDependents(List<TaskRunningStateEntity> preDependents)
    {
        this.preDependents = preDependents;
    }

    public List<TaskRunningStateEntity> getSubDependents()
    {
        return subDependents;
    }

    public void setSubDependents(List<TaskRunningStateEntity> subDependents)
    {
        this.subDependents = subDependents;
    }
}
