/*
 * 文 件 名:  PoiExcelFileWrite.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-7-9
 */
package com.huawei.bi.task.bean.operator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;

/**
 * 使用POI导出Excel文件
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-7-9]
 * @see  [相关类/方法]
 */
public class PoiExcelFileWrite implements IExeLogger
{
    private static Logger log = LoggerFactory.getLogger(PoiExcelFileWrite.class);
    private SXSSFWorkbook wb;
    private String file;
    private int rowNum;
    private List<String> runDebugInfo = new ArrayList<String>();
    private Sheet sheet;
    private OutputStream out;
    
    public PoiExcelFileWrite(String exeId,String path) throws Exception
    {
        this.file = path + File.separator + exeId + "";
        this.out = new FileOutputStream(file);
        this.wb = new SXSSFWorkbook(100);
        wb.setCompressTempFiles(true);
        this.rowNum = 0;
        this.sheet = wb.createSheet("Sheet1");
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
     * @throws Exception
     */
    @Override
    public void writeResult(String text) throws Exception
    {
        String[] values = text.split("\t");
        if (null != values && values.length > 0)
        {
            Row row = sheet.createRow(rowNum);
            for (int i = 0; i < values.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(values[i]);
            }
            rowNum ++;
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
        return null;
    }

    /**
     * 
     */
    @Override
    public void flush()
    {
        try
        {
            wb.write(out);
        }
        catch (IOException e)
        {
            log.error("Write workbook error.",e);
        }
        try
        {
            out.close();
        }
        catch (IOException e)
        {
            log.error("Close output stream erro.",e);
        }
    }

    /**
     * 
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
