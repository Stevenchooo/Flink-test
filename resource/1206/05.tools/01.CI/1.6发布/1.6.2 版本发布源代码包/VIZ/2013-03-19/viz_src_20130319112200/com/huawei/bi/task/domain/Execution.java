package com.huawei.bi.task.domain;

import java.util.Date;

public class Execution
{
    private String exeId;
    
    private String taskId;
    
    private Date startAt;
    
    private Date endAt;
    
    private String status;
    
    private String options;
    
    private String code;
    
    private Integer sqlId;
    
    private String user;
    
    private Long recordNumber;
    
    private Integer exportState;
    
    // persist or once?
    private String executionType;
    
    public static String EXE_TYPE_BACKEND = "backendExe";
    
    public static String EXE_TYPE_FRONTEND = "frontendExe";
    
    public static String EXE_TYPE_MERGE = "mergeExe";
    
    public Long getRecordNumber()
    {
        return recordNumber;
    }
    
    public void setRecordNumber(Long recordNumber)
    {
        this.recordNumber = recordNumber;
    }
    
    public Integer getExportState()
    {
        return exportState;
    }
    
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setExportState(Integer exportState)
    {
        this.exportState = exportState;
    }
    
    public Integer getSqlId()
    {
        return sqlId;
    }
    
    public void setSqlId(Integer sqlId)
    {
        this.sqlId = sqlId;
    }
    
    public String getUser()
    {
        return user;
    }
    
    public void setUser(String user)
    {
        this.user = user;
    }
    
    public String getExeId()
    {
        
        return exeId;
    }
    
    public void setExeId(String exeId)
    {
        this.exeId = exeId;
    }
    
    public String getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }
    
    public Date getStartAt()
    {
        return startAt;
    }
    
    public void setStartAt(Date startAt)
    {
        this.startAt = startAt;
    }
    
    public Date getEndAt()
    {
        return endAt;
    }
    
    public void setEndAt(Date endAt)
    {
        this.endAt = endAt;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getExecutionType()
    {
        return executionType;
    }
    
    public void setExecutionType(String executionType)
    {
        this.executionType = executionType;
    }
    
    public String getOptions()
    {
        return options;
    }
    
    public void setOptions(String options)
    {
        this.options = options;
    }
}
