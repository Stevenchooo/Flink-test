package com.huawei.bi.task.bean.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogLogger implements IExeLogger
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogLogger.class);
    
    private String user;
    
    private String tableName;
    
    public LogLogger(String user, String tableName)
    {
        this.user = user;
        this.tableName = tableName;
    }
    
    @Override
    public long getResultRows()
    {
        return 0;
    }
    
    @Override
    public void writeDebug(String text)
        throws IOException
    {
        LOGGER.info(user + ":" + tableName + ": " + text);
    }
    
    @Override
    public void writeResult(String text)
        throws Exception
    {
        LOGGER.info(user + ":" + tableName + ": " + text);
    }
    
    @Override
    public List<String> readDebug()
        throws IOException
    {
        return null;
    }
    
    @Override
    public List<String> readResult()
        throws IOException
    {
        return null;
    }
    
    @Override
    public InputStream readStreamResult()
        throws IOException
    {
        return null;
    }
    
    @Override
    public void flush()
    {
        
    }
    
    @Override
    public void delete()
    {
    }
}
