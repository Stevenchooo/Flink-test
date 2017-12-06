/*
 * 文 件 名:  TaskStepEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-2
 */
package com.huawei.platform.tcc.entity;


/**
 * 
 * 任务步骤序列实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-26]
 * @see  [相关类/方法]
 */
public class TaskStepIdSerialEntity
{
    private Long taskId;
    
    private Integer stepSerialno;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Integer getStepSerialno()
    {
        return stepSerialno;
    }

    public void setStepSerialno(Integer stepSerialno)
    {
        this.stepSerialno = stepSerialno;
    }
}