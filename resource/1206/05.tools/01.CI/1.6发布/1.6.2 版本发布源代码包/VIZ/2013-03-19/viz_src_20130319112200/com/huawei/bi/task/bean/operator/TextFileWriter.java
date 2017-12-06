/*
 * 文 件 名:  TextFileWriter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-30
 */
package com.huawei.bi.task.bean.operator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-30]
 * @see  [相关类/方法]
 */
public class TextFileWriter implements IExeLogger
{

    private static Logger log = LoggerFactory.getLogger(TextFileWriter.class);
    private FileWriter resultFileWriter;
    private String file;
    private List<String> runDebugInfo = new ArrayList<String>();
    public TextFileWriter(String exeId,String path) throws IOException
    {

        this.file = path + File.separator + exeId;
        resultFileWriter = new FileWriter(file, true);
    }
    /**
     * @param text
     * @throws IOException
     */
    @Override
    public void writeDebug(String text) throws IOException
    {
        runDebugInfo.add(text);
        runDebugInfo.add("\n");
    }

    /**
     * @param text
     * @throws IOException
     */
    @Override
    public void writeResult(String text) throws IOException
    {
        resultFileWriter.write(text);
        resultFileWriter.write("\n");
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public List<String> readDebug() throws IOException
    {
        return runDebugInfo;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public List<String> readResult() throws IOException
    {
        return null;
    }

    /**
     * @throws IOException 
     * 
     */
    @Override
    public void flush()
    {
        if (resultFileWriter != null) {
            try {
                resultFileWriter.flush();
            } catch (IOException e) {
                log.error("File write flush exception." + e);
            }
            try
            {
                resultFileWriter.close();
            }
            catch (IOException e)
            {
                log.error("File write close exception." + e);
            }
        }
    }

    /**
     * 删除文件
     */
    @Override
    public void delete()
    {
        File textFile = new File(file);
        if(textFile.exists())
        {
            if(!textFile.delete())
            {
                log.warn(file + " delete failed.");
            }
        }
    }
    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream readStreamResult() throws IOException
    {
        return new FileInputStream(file);
    }
    @Override
    public long getResultRows()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
