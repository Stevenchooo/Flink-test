package com.huawei.bi.task.da;

import com.huawei.bi.task.domain.Execution;

public class ExePersistBuilder
{
    public static IExecutionPersist buildExePersist(String exeType)
    {
        IExecutionPersist exePersist = null;
        
        if (Execution.EXE_TYPE_FRONTEND.equals(exeType))
        {
            exePersist = new MemExePersist();
        }
        else if (Execution.EXE_TYPE_BACKEND.equals(exeType))
        {
            exePersist = new DbExePersist();
        }
        else
        {
            exePersist = new MemDbExePersist();
        }
        
        return exePersist;
    }
}
