/*
 * 文 件 名:  TaskIdCycleIdPair.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

/**
 * 键值对
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class TaskIdCycleIdPair
{
    private String taskId;
    
    private String cycleId;

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getCycleId()
    {
        return cycleId;
    }

    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }
    
}
