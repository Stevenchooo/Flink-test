package com.huawei.bi.task.da;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.TailedLogger;
import com.huawei.bi.task.domain.Execution;
import com.huawei.bi.util.Config;

public class MemExePersist implements IExecutionPersist
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MemExePersist.class);
    
    private static Map<String, Execution> EXECUTION_MAP = new HashMap<String, Execution>();
    
    private static final long OUTDATE_MILLSEC = Long.parseLong(Config.get("mem.outdate.seconds")) * 1000;
    
    /**
     * 获取正运行的执行任务列表
     * @return 正运行的执行任务列表
     */
    public List<Execution> getRunningExecutions(String user)
    {
        List<Execution> exeList = new ArrayList<Execution>();
        if (StringUtils.isBlank(user))
        {
            return exeList;
        }
        
        //遍历
        Execution exe;
        synchronized (EXECUTION_MAP)
        {
            for (Entry<String, Execution> entry : EXECUTION_MAP.entrySet())
            {
                exe = entry.getValue();
                if ("running".equals(exe.getStatus()) && user.equals(exe.getUser()))
                {
                    exeList.add(exe);
                }
            }
        }
        
        return exeList;
    }
    
    /**
     * 获取正运行的执行任务列表
     * @return 正运行的执行任务列表
     */
    public Integer getRunningExeNum(String user)
    {
        //不存在的用户默认返回最大数
        if (StringUtils.isBlank(user))
        {
            return Integer.MAX_VALUE;
        }
        
        //遍历
        Execution exe;
        Integer runningN = 0;
        synchronized (EXECUTION_MAP)
        {
            for (Entry<String, Execution> entry : EXECUTION_MAP.entrySet())
            {
                exe = entry.getValue();
                if ("running".equals(exe.getStatus()) && user.equals(exe.getUser()))
                {
                    runningN++;
                }
            }
        }
        
        return runningN;
    }
    
    @Override
    public void addExecution(Execution execution)
    {
        EXECUTION_MAP.put(execution.getExeId(), execution);
    }
    
    @Override
    public void updateExecution(String exeId, String status, long records, Integer exportState)
        throws Exception
    {
        Execution execution = null;
        synchronized (EXECUTION_MAP)
        {
            execution = EXECUTION_MAP.get(exeId);
        }
        
        if (null != execution)
        {
            execution.setStatus(status);
            execution.setEndAt(new Date());
            execution.setRecordNumber(records);
            execution.setExportState(exportState);
        }
    }
    
    @Override
    public Execution getExecution(String exeId)
    {
        return EXECUTION_MAP.get(exeId);
    }
    
    @Override
    public void deleteExecution(String exeId)
    {
        synchronized (EXECUTION_MAP)
        {
            EXECUTION_MAP.remove(exeId);
        }
    }
    
    public static void deleteOutdateExecution()
    {
        List<String> delKeyList = new ArrayList<String>();
        try
        {
            //遍历
            Execution exe;
            synchronized (EXECUTION_MAP)
            {
                for (Entry<String, Execution> entry : EXECUTION_MAP.entrySet())
                {
                    exe = entry.getValue();
                    if ("complete".equals(exe.getStatus()))
                    {
                        if (exe.getEndAt() == null)
                        {
                            // exceptional, delete
                            delKeyList.add(entry.getKey());
                            
                        }
                        else
                        {
                            // calculate the time between
                            long timeInMS = new Date().getTime() - exe.getEndAt().getTime();
                            // 5 min  
                            // TODO 时间需要支持可配置
                            if (timeInMS > OUTDATE_MILLSEC)
                            {
                                delKeyList.add(entry.getKey());
                            }
                        }
                    }
                }
                
                for (String key : delKeyList)
                {
                    EXECUTION_MAP.remove(key);
                }
                
                // TailedLogger.
                for (String key : delKeyList)
                {
                    TailedLogger.DEBUG_POOL.remove(key);
                    TailedLogger.RESULT_POOL.remove(key);
                }
            }
            
            LOGGER.debug("deleteOutdateExecution success! delKeyList is {}", delKeyList);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOutdateExecution failed! delKeyList is {}", delKeyList, e);
        }
    }
    
    @Override
    public void updateExportState(String exeId, String user, int applyExport)
        throws Exception
    {
        Execution execution = EXECUTION_MAP.get(exeId);
        if (user.equals(execution.getUser()))
        {
            execution.setExportState(applyExport);
        }
    }
    
}
