package com.huawei.bi.task.bean.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.huawei.bi.util.Config;

public class TailedFileLogger implements IExeLogger
{
    private IExeLogger tailedLoggers;
    
    private FileLogger fileLoggers;
    
    public TailedFileLogger(String exeId)
        throws Exception
    {
        tailedLoggers = new TailedLogger(exeId);
        fileLoggers = new FileLogger(exeId);
    }
    
    @Override
    public void writeDebug(String text)
        throws IOException
    {
        tailedLoggers.writeDebug(text);
        fileLoggers.writeDebug(text);
    }
    
    @Override
    public void writeResult(String text)
        throws Exception
    {
        tailedLoggers.writeResult(text);
        fileLoggers.writeResult(text);
    }
    
    @Override
    public List<String> readDebug()
        throws IOException
    {
        //仅从tailed日志中读取
        return tailedLoggers.readDebug();
    }
    
    public List<String> readResult(boolean fromFile)
        throws IOException
    {
        if(fromFile)
        {
            return fileLoggers.readResult(Long.parseLong(Config.get("max.result.page.num")));
        }
        else
        {
            return tailedLoggers.readResult();
        }
    }
    
    public List<String> readDebug(boolean fromFile)
        throws IOException
    {
        if(fromFile)
        {
            return fileLoggers.readDebug();
        }
        else
        {
            return tailedLoggers.readDebug();
        }
    }
    
    @Override
    public List<String> readResult()
        throws IOException
    {
        return tailedLoggers.readResult();
    }
    
    @Override
    public InputStream readStreamResult()
        throws IOException
    {
        return fileLoggers.readStreamResult();
    }
    
    @Override
    public void flush()
    {
        tailedLoggers.flush();
        fileLoggers.flush();
    }
    
    @Override
    public void delete()
    {
        tailedLoggers.delete();
        fileLoggers.delete();
    }
    
    //返回最大行数
    public long getResultRows()
    {
        return Math.max(tailedLoggers.getResultRows(), fileLoggers.getResultRows());
    }
}
