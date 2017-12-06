/*
 * 文 件 名:  Log2DBQueryParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;


/**
 * 日志查询参数
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class Log2DBQueryParam
{
    /**
     * 分页开始索引
     */
    private Long startIndex;
    
    /**
     * 行数
     */
    private Integer rows;
    
    /**
     * 页数
     */
    private Integer page;
    
    /**
     * 操作员
     */
    private String userName;
    
    /**
     * 日志级别
     */
    private String level;
    
    /**
     * 任务Id
     */
    private String taskId;
    
    /**
     * 周期Id
     */
    private String cycleId;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;

    public Long getStartIndex()
    {
        return startIndex;
    }

    public void setStartIndex(Long startIndex)
    {
        this.startIndex = startIndex;
    }

    public Integer getRows()
    {
        return rows;
    }

    public void setRows(Integer rows)
    {
        this.rows = rows;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

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

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Log2DBQueryParam [startIndex=");
        builder.append(startIndex);
        builder.append(", rows=");
        builder.append(rows);
        builder.append(", page=");
        builder.append(page);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", level=");
        builder.append(level);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", cycleId=");
        builder.append(cycleId);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append("]");
        return builder.toString();
    }
}
