/*
 * 文 件 名:  ReserveCycleLogParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

/**
 * 保留最新日志的周期数
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 */
public class ReserveCycleLogParam
{
    //任务Id
    private String taskId;
    
    //保留最新日志的周期数
    private Integer reserveCount;

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public Integer getReserveCount()
    {
        return reserveCount;
    }

    public void setReserveCount(Integer reserveCount)
    {
        this.reserveCount = reserveCount;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ReserveCycleLogParam [taskId=");
        builder.append(taskId);
        builder.append(", reserveCount=");
        builder.append(reserveCount);
        builder.append("]");
        return builder.toString();
    }
}
