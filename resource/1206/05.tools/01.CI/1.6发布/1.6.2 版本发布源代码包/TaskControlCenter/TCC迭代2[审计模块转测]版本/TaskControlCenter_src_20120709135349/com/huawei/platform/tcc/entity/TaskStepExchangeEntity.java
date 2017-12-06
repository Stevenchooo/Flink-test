/*
 * 文 件 名:  TaskStepExchangeEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-2
 */
package com.huawei.platform.tcc.entity;

/**
 * 
 * 任务步骤交换实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-26]
 * @see  [相关类/方法]
 */
public class TaskStepExchangeEntity
{
    private Long taskId;
    
    private Integer stepIdOne;
       
    private Integer stepIdTwo;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Integer getStepIdOne()
    {
        return stepIdOne;
    }

    public void setStepIdOne(Integer stepIdOne)
    {
        this.stepIdOne = stepIdOne;
    }

    public Integer getStepIdTwo()
    {
        return stepIdTwo;
    }

    public void setStepIdTwo(Integer stepIdTwo)
    {
        this.stepIdTwo = stepIdTwo;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskStepExchangeEntity [taskId=");
        builder.append(taskId);
        builder.append(", stepIdOne=");
        builder.append(stepIdOne);
        builder.append(", stepIdTwo=");
        builder.append(stepIdTwo);
        builder.append("]");
        return builder.toString();
    } 
}