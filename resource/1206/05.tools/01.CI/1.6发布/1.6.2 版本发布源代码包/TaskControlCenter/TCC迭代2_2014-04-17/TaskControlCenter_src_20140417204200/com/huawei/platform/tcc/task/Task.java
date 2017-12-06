/*
 * 文 件 名:  Task.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.tcc.task;

import java.util.List;

import com.huawei.platform.tcc.entity.TaskEntity;

/**
 * 任务
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-10]
 */
public class Task
{
    //任务Id
    private Long taskId;
    
    //任务实体
    private TaskEntity taskEntity;
    
    //任务依赖关系错误
    private boolean dependError;
    
    //前驱关系
    private List<TaskRelation> preTaskRs;
    
    //后继关系
    private List<TaskRelation> subTaskRs;
    
    public boolean isDependError()
    {
        return dependError;
    }
    
    public void setDependError(boolean dependError)
    {
        this.dependError = dependError;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public TaskEntity getTaskEntity()
    {
        return taskEntity;
    }
    
    public void setTaskEntity(TaskEntity taskEntity)
    {
        this.taskEntity = taskEntity;
    }
    
    public List<TaskRelation> getPreTaskRs()
    {
        return preTaskRs;
    }
    
    public void setPreTaskRs(List<TaskRelation> preTaskRs)
    {
        this.preTaskRs = preTaskRs;
    }
    
    public List<TaskRelation> getSubTaskRs()
    {
        return subTaskRs;
    }
    
    public void setSubTaskRs(List<TaskRelation> subTaskRs)
    {
        this.subTaskRs = subTaskRs;
    }
}
