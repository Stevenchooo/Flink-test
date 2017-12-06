package com.huawei.bi.task.bean.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.huawei.bi.util.Config;
import com.huawei.bi.util.Util;

public class FileLogger implements IExeLogger
{
    private long resultRows = 0L;
    
    private String exeId;
    
    private FileWriter debugFileWriter;
    
    private FileWriter resultFileWriter;
    
    public FileLogger(String exeId)
        throws Exception
    {
        super();
        this.exeId = exeId;
        
        debugFileWriter = new FileWriter(getDebugFilePath(), true);
        resultFileWriter = new FileWriter(getResultFilePath(), true);
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
        throws IOException
    {
        debugFileWriter.append(text);
        debugFileWriter.append("\n");
    }
    
    @Override
    public void writeResult(String text)
        throws IOException
    {
        resultRows++;
        resultFileWriter.append(text);
        resultFileWriter.append("\n");
    }
    
    @Override
    public List<String> readDebug()
        throws IOException
    {
        return Util.fileToList(getDebugFilePath());
    }
    
    @Override
    public List<String> readResult()
        throws IOException
    {
        return Util.fileToList(getResultFilePath());
    }
    
    public List<String> readResult(long limit)
        throws IOException
    {
        return Util.fileToList(getResultFilePath(),limit);
    }
    
    public String getDebugFilePath()
    {
        String debugDir = Config.get("task.exe.debug.dir");
        return debugDir + "/" + exeId;
    }
    
    public String getResultFilePath()
    {
        String resultDir = Config.get("task.exe.result.dir");
        return resultDir + File.separatorChar + exeId;
    }
    
    @Override
    protected void finalize()
        throws Throwable
    {
        super.finalize();
        
        this.flush();
    }
    
    @Override
    public void flush()
    {
        if (debugFileWriter != null)
        {
            try
            {
                debugFileWriter.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        if (resultFileWriter != null)
        {
            try
            {
                resultFileWriter.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
    }
    
    @Override
    public void delete()
    {
        new File(getDebugFilePath()).delete();
        new File(getResultFilePath()).delete();
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
