/* 文 件 名:  Log2DBEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;


/**
 * 日志记录实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-20]
 */
public class Log2DBEntity
{
    private Long logId;
    
    private String userName;
    
    private String clientIp;
    
    private String cycleId;
    
    private String taskId;
    
    private String createTime;
    
    private String threadName;
    
    private String className;
    
    private String methodName;
    
    private String logLevel;
    
    private String message;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getCycleId()
    {
        return cycleId;
    }

    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getThreadName()
    {
        return threadName;
    }

    public void setThreadName(String threadName)
    {
        this.threadName = threadName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public String getLogLevel()
    {
        return logLevel;
    }

    public void setLogLevel(String logLevel)
    {
        this.logLevel = logLevel;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Log2DBEntity [logId=");
        builder.append(logId);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", cycleId=");
        builder.append(cycleId);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", threadName=");
        builder.append(threadName);
        builder.append(", className=");
        builder.append(className);
        builder.append(", methodName=");
        builder.append(methodName);
        builder.append(", logLevel=");
        builder.append(logLevel);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }
}