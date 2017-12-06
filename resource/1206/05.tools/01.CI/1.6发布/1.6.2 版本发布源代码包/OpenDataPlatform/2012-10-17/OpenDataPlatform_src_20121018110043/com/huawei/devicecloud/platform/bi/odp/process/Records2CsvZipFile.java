/*
 * 文 件 名:  Records2CsvZipFile.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  记录集合写入到文件（CSV文件格式，ZIP压缩）
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-15
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 记录集合写入到文件（CSV文件格式，ZIP压缩）
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-15]
 * @param <T> 泛型类型
 */
public class Records2CsvZipFile<T> implements IRecords2File<T>
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Records2CsvZipFile.class);
    
    //csv文件后缀
    private static final String CSV_SUFFIX = ".csv";
    
    //文件名
    private String fileName;
    
    //csv文件名
    private String csvFileName;
    
    //列字段映射
    private ColumnFieldMapping[] columnFields;
    
    //是否将列名写在文件头中
    private boolean writeHeader = true;
    
    /**
     * 构造函数
     * @param fileName 文件名
     * @param writeHeader 是否写文件头
     * @param columnFields 表列对象字段映射数组
     */
    public Records2CsvZipFile(String fileName, boolean writeHeader, ColumnFieldMapping[] columnFields)
    {
        this.fileName = fileName;
        
        //加上csv后缀构造csv文件名
        this.csvFileName = fileName + CSV_SUFFIX;
        
        //字段列表不为空
        this.columnFields = columnFields;
        
        this.writeHeader = writeHeader;
    }
    
    /**
     * 将记录
     * @param records 记录列表
     * @throws CException 异常
     * @return 实际产生的文件名
     */
    @Override
    public String writeRecords2File(List<T> records)
        throws CException
    {
        //记录为空直接返回
        if (null == columnFields || columnFields.length <= 0)
        {
            return null;
        }
        
        CSVWriter csvWriter = null;
        
        try
        {
            //文件不存在则创建新文件
            File file = new File(csvFileName);
            if (!file.exists())
            {
                if (!file.createNewFile())
                {
                    //记录错误信息
                    LOGGER.error("create[{}] file failed!", csvFileName);
                    return null;
                }
            }
            
            FileOutputStream out = new FileOutputStream(file);
            csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out)));
            //将表字段写入到文件中
            writeFields2File(csvWriter);
            
            //写入记录
            Method[] getMethods = null;
            String[] line = new String[columnFields.length];
            Object value = null;
            
            for (T record : records)
            {
                //忽略空记录
                if (null == record)
                {
                    continue;
                }
                
                //获取字段的get方法数组
                if (null == getMethods)
                {
                    getMethods = OdpCommonUtils.getFieldsGetMethod(record, columnFields);
                }
                
                //获取字段值
                for (int i = 0; i < getMethods.length; i++)
                {
                    try
                    {
                        //获取属性值
                        value = getMethods[i].invoke(record);
                        
                        if (null != value)
                        {
                            //NULL值不写入
                            line[i] = String.valueOf(value);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        //记录错误信息
                        LOGGER.error("writeRecords2File failed! record is {}.", record, e);
                    }
                    catch (IllegalAccessException e)
                    {
                        //记录错误信息
                        LOGGER.error("writeRecords2File failed! record is {}.", record, e);
                    }
                    catch (InvocationTargetException e)
                    {
                        //记录错误信息
                        LOGGER.error("writeRecords2File failed! record is {}.", record, e);
                    }
                }
                //写csv文件
                csvWriter.writeNext(line);
            }
        }
        catch (IOException e)
        {
            //记录错误信息，并抛出异常
            LOGGER.error("writeRecords2File failed! fileName is {}", fileName, e);
            throw new CException(ResultCode.WRITE_FILE_ERROR, new Object[] {fileName, e});
        }
        finally
        {
            //关闭writer
            OdpCommonUtils.close(csvWriter);
        }
        
        //压缩文件产生zip的文件
        OdpCommonUtils.compress(csvFileName, fileName);
        
        //删除原文件
        OdpCommonUtils.deleteFile(csvFileName);
        
        return fileName;
    }
    
    //将列名写入到文件头中
    private void writeFields2File(CSVWriter csvWriter)
    {
        //允许写字段或者writer可用
        if (null != csvWriter && writeHeader)
        {
            String[] line = new String[columnFields.length];
            for (int i = 0; i < columnFields.length; i++)
            {
                line[i] = columnFields[i].getColumnName();
            }
            //将列数组作为一行写入
            csvWriter.writeNext(line);
            //列名头仅写入一次
            writeHeader = false;
        }
    }
}
