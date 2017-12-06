/*
 * 文 件 名:  ExeclFileWrite.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-30
 */
package com.huawei.bi.task.bean.operator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;

/**
 * 写出Excel格式的文件
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-30]
 * @see  [相关类/方法]
 */
@Deprecated
public class ExeclFileWrite implements IExeLogger
{
    private static Logger log = LoggerFactory.getLogger(ExeclFileWrite.class);
    private WritableWorkbook wwb ;
    private WritableSheet ws;
    private String file;
    private List<String> runDebugInfo = new ArrayList<String>();
    /**当前Excel的行数*/
    private int row;
    private OutputStream out;
    
    public ExeclFileWrite(String exeId,String path) throws Exception
    {
        this.file = path + File.separator + exeId + "";
        this.row = 0;
        this.out = new FileOutputStream(file);
        wwb = Workbook.createWorkbook(out);
        ws = wwb.createSheet("Sheet1", 0);
    }
    /**写出日志
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
     * @throws WriteException 
     * @throws RowsExceededException 
     */
    @Override
    public void writeResult(String text) throws Exception
    {
        String[] values = text.split("\t");
        if(null != values && values.length > 0)
        {
            if (row < 65536)
            {
                for (int i = 0; i < values.length; i++)
                {
                    Label labelC = new Label(i, row, values[i]);
                    ws.addCell(labelC);
                }
            }
            if(row == 65536)
            {
                log.warn("Excel 超过最大支持行数。");
            }
            row ++;
        }
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
     * @return
     * @throws IOException
     */
    @Override
    public InputStream readStreamResult() throws IOException
    {
       return new FileInputStream(file);
    }

    /**
     * 
     */
    @Override
    public void flush()
    {
        try
        {
            wwb.write();
        }
        catch (IOException ioe)
        {
            log.error(
                    "WritableWorkbook write exception.",
                    ioe);
        }
        
        if (wwb != null) {
            try {
                wwb.close();
            } catch (Exception e) {
                log.error(
                        "WritableWorkbook Close exception.",
                        e);
            }
            wwb = null;
        }
        if(out != null)
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                log.warn("Close output stream exception.",e);
            }
        }
    }

    /**
     * 使用完成后删除文件
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
    @Override
    public long getResultRows()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
