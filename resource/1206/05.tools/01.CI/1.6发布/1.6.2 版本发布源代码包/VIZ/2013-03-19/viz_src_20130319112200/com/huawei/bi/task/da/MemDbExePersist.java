package com.huawei.bi.task.da;

import java.util.List;

import com.huawei.bi.task.domain.Execution;

public class MemDbExePersist implements IExecutionPersist
{
    private IExecutionPersist memExePersist = new MemExePersist();
    
    private IExecutionPersist dbExePersist = new DbExePersist();
    
    @Override
    public void addExecution(Execution execution)
        throws Exception
    {
        memExePersist.addExecution(execution);
        dbExePersist.addExecution(execution);
    }
    
    @Override
    public void updateExecution(String exeId, String status, long records, Integer exportState)
        throws Exception
    {
        memExePersist.updateExecution(exeId, status, records, exportState);
        dbExePersist.updateExecution(exeId, status, records, exportState);
    }
    
    @Override
    public Execution getExecution(String exeId)
        throws Exception
    {
        return dbExePersist.getExecution(exeId);
    }
    
    @Override
    public void deleteExecution(String exeId)
        throws Exception
    {
        memExePersist.deleteExecution(exeId);
        dbExePersist.deleteExecution(exeId);
    }
    
    @Override
    public void updateExportState(String exeId, String user, int exportState)
        throws Exception
    {
        memExePersist.updateExportState(exeId, user, exportState);
        dbExePersist.updateExportState(exeId, user, exportState);
    }
    
    public static void deleteOutdateExecution()
    {
        MemExePersist.deleteOutdateExecution();
        DbExePersist.deleteOutdateExecution();
    }
}
