/*
 * 文 件 名:  DependCyclesExistNum.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-12
 */
package com.huawei.platform.tcc.domain;

/**
 * 依赖周期存在数目
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-3-12]
 */
public class DependCyclesExistNum
{
    private Long taskId;
    
    private String cycleId;
    
    private Integer existNum;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
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

    public Integer getExistNum()
    {
        return existNum;
    }

    public void setExistNum(Integer existNum)
    {
        this.existNum = existNum;
    }
}
