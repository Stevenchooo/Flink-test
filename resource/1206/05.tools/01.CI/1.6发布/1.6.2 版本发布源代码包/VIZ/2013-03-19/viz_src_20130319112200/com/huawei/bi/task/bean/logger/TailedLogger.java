package com.huawei.bi.task.bean.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.huawei.bi.util.Config;

public class TailedLogger implements IExeLogger
{
    
    public static Map<String, List<String>> DEBUG_POOL = new ConcurrentHashMap<String, List<String>>();
    
    public static Map<String, List<String>> RESULT_POOL = new ConcurrentHashMap<String, List<String>>();
    
    private long resultRows;
    
    private String exeId;
    
    public TailedLogger(String exeId)
    {
        super();
        this.exeId = exeId;
    }
    
    public String getExeId()
    {
        return exeId;
    }
    
    public void setExeId(String exeId)
    {
        this.exeId = exeId;
    }
    
    @Override
    public void writeDebug(String text)
    {
        synchronized (exeId)
        {
            if (DEBUG_POOL.get(exeId) == null)
            {
                DEBUG_POOL.put(exeId, new ArrayList<String>());
            }
            DEBUG_POOL.get(exeId).add(text);
        }
    }
    
    @Override
    public void writeResult(String text)
    {
        if (RESULT_POOL.get(exeId) == null)
        {
            RESULT_POOL.put(exeId, new ArrayList<String>());
        }
        List<String> list = RESULT_POOL.get(exeId);
        int size = list.size();
        //最多显示500条
        if (size <= Integer.parseInt(Config.get("max.result.page.num")))
        {
            resultRows++;
            list.add(text);
        }
    }
    
    @Override
    public List<String> readDebug()
    {
        List<String> list = new ArrayList<String>();
        
        synchronized (exeId)
        {
            if (DEBUG_POOL.get(exeId) != null)
            {
                list.addAll(DEBUG_POOL.get(exeId));
                DEBUG_POOL.get(exeId).clear();
            }
        }
        
        return list;
    }
    
    @Override
    public List<String> readResult()
    {
        List<String> list = new ArrayList<String>();
        
        if (RESULT_POOL.get(exeId) != null)
        {
            list.addAll(RESULT_POOL.get(exeId));
            RESULT_POOL.get(exeId).clear();
        }
        
        return list;
    }
    
    @Override
    public void flush()
    {
        // no effect
    }
    
    @Override
    public void delete()
    {
        // no effect
    }
    
    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream readStreamResult()
        throws IOException
    {
        return null;
    }
    
    @Override
    public long getResultRows()
    {
        return resultRows;
    }
    
}
