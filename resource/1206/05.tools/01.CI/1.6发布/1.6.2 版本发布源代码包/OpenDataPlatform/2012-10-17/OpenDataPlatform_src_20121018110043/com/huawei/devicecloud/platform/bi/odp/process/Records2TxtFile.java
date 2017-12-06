/*
 * 文 件 名:  Records2TxtFile.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  记录集合写入到文件
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-15
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 记录集合写入到文件
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-15]
 * @param <T> 泛型类型
 */
public class Records2TxtFile<T> implements IRecords2File<T>
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Records2TxtFile.class);
    
    //文本文件后缀
    private static final String TXT_SUFFIX = ".txt";
    
    //行分割符
    private static final String ROW_SEPARATOR = "\r\n";
    
    //列分割符
    private static final char FIELD_SEPARATOR = '\001';
    
    //是否写将列表写到文件头
    private boolean writeHeader = false;
    
    //文件名
    private String fileName;
    
    //txt文件名
    private String txtFileName;
    
    //列名与字段名的映射
    private ColumnFieldMapping[] columnFields;
    
    /**
     * 构造函数
     * @param fileName 文件名
     * @param writeHeader 是否写文件头
     * @param columnFields 表列对象字段映射数组
     */
    public Records2TxtFile(String fileName, boolean writeHeader, ColumnFieldMapping[] columnFields)
    {
        this.fileName = fileName;
        //字段列表不为空
        this.columnFields = columnFields;
        
        //加上txt后缀构造txt文件名
        this.txtFileName = fileName + TXT_SUFFIX;
        
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
        
        PrintWriter writer = null;
        File file = new File(txtFileName);
        try
        {
            //文件名不存在则创建新文件
            if (!file.exists())
            {
                if (!file.createNewFile())
                {
                    //记录错误信息
                    LOGGER.error("create[{}] file failed!", txtFileName);
                    return null;
                }
            }
            
            FileOutputStream out = new FileOutputStream(file, true);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);
            
            //将表字段写入到文件中
            writeFields2File(writer);
            
            //写入记录
            Method[] getMethods = null;
            
            Object value = null;
            T record = null;
            //记录大小
            int recordSize = records.size();
            for (int j = 0; j < recordSize; j++)
            {
                record = records.get(j);
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
                            writer.print(value);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        //记录错误信息继续
                        LOGGER.error("writeRecords2File failed! record is {}.", record, e);
                    }
                    catch (IllegalAccessException e)
                    {
                        //记录错误信息继续
                        LOGGER.error("writeRecords2File failed! record is {}.", record, e);
                    }
                    catch (InvocationTargetException e)
                    {
                        //记录错误信息继续
                        LOGGER.error("writeRecords2File failed! record is {}.", record, e);
                    }
                    
                    //最后一列不适用列分割符
                    if (i != getMethods.length - 1)
                    {
                        //加上列分割符
                        writer.print(FIELD_SEPARATOR);
                    }
                    
                }
                //加上行分割符
                writer.append(ROW_SEPARATOR);
            }
        }
        catch (IOException e)
        {
            //记录错误信息并抛出异常终止
            LOGGER.error("writeRecords2File failed! fileName is {}", fileName, e);
            throw new CException(ResultCode.WRITE_FILE_ERROR, new Object[] {fileName, e});
        }
        finally
        {
            //关闭writer
            OdpCommonUtils.close(writer);
        }
        
        return txtFileName;
    }
    
    private void writeFields2File(PrintWriter writer)
    {
        //允许写字段或者writer可用
        if (null != writer && writeHeader)
        {
            StringBuilder fieldSB = new StringBuilder();
            for (int i = 0; i < columnFields.length; i++)
            {
                fieldSB.append(columnFields[i].getColumnName());
                
                //最后一列不添加列分割符
                if (i != columnFields.length - 1)
                {
                    fieldSB.append(FIELD_SEPARATOR);
                }
            }
            //增加行风格符
            fieldSB.append(ROW_SEPARATOR);
            
            writer.print(fieldSB);
            
            //列信息头最多写入一次
            writeHeader = false;
        }
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
