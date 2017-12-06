package com.huawei.bi.task.da;

import com.huawei.bi.task.domain.Execution;

public interface IExecutionPersist
{
    void addExecution(Execution execution)
        throws Exception;
    
    void updateExecution(String exeId, String status, long records, Integer exportState)
        throws Exception;
    
    Execution getExecution(String exeId)
        throws Exception;
    
    void deleteExecution(String exeId)
        throws Exception;
    
    void updateExportState(String exeId, String user, int applyExport)
        throws Exception;
}
