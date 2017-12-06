/*
 * 文 件 名:  TempTableRecordProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  临时表记录处理器
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-11
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.RecordQueue;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;

/**
 * 临时表记录处理器
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-11]
 * @param <T> 泛型类型
 */
public class TempTableRecordProcess<T> implements IConsumer
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TempTableRecordProcess.class);
    
    /**
     * 每次插入的记录数
     */
    private static final int RECORDS_PER_INSERT = 1000;
    
    //列字段名映射关系数组
    private ColumnFieldMapping[] columnFieldTypes;
    
    //临时表名数组
    private String[] tmpTableNames;
    
    //记录文件名数组
    private String[] rfileNames;
    
    //比例数组
    private int[] ratios;
    
    //存放记录的队列
    private RecordQueue<T> recordQueue;
    
    //Dao层调用对象
    private OdpDao odpDao;
    
    /**
     * 默认构造函数
     * @param columnFields 表列对象字段映射数组
     * @param tmpTableNames 临时表名
     * @param rfileNames 预留文件名
     * @param ratios 分批比例
     * @param recordQueue 记录队列
     */
    public TempTableRecordProcess(ColumnFieldMapping[] columnFields, String[] tmpTableNames, String[] rfileNames,
        int[] ratios, RecordQueue<T> recordQueue)
    {
        this.columnFieldTypes = columnFields;
        this.rfileNames = rfileNames;
        this.tmpTableNames = tmpTableNames;
        this.ratios = ratios.clone();
        this.recordQueue = recordQueue;
    }
    
    /**
     * 取出记录插入到相应的临时表中，参数不合法返回null
     * @param write 是否写入
     * @param transactionId 交易Id
     * @param timestamp 时间戳
     * @return 记录数
     * @throws CException 异常
     */
    public long[] writeRecords(boolean write, String transactionId, String timestamp)
        throws CException
    {
        //参数不合法
        if (null == recordQueue || null == ratios || null == tmpTableNames || ratios.length != tmpTableNames.length
            || ratios.length < 1)
        {
            LOGGER.error("parameters of TempTableRecordProcess is invaild!");
            return null;
        }
        
        long[] recordNums = new long[ratios.length];
        //将记录按照比例分配后剩余的非整数部分值数组
        float[] radioLefts = new float[ratios.length];
        
        //计算比例和
        int ratioSum = 0;
        for (int i = 0; i < ratios.length; i++)
        {
            ratioSum += ratios[i];
        }
        long recordCount = recordQueue.getRecordsCount();
        //计算每个临时表的记录数
        long restNum = recordCount;
        for (int i = 0; i < ratios.length; i++)
        {
            recordNums[i] = (long)((float)ratios[i] / ratioSum * recordCount);
            //剩余比例
            radioLefts[i] = (float)ratios[i] / ratioSum * recordCount - recordNums[i];
            restNum = restNum - recordNums[i];
        }
        //按照ratios剩余率从大到小均匀分配restNum数
        int[] sortedIndexs = OdpCommonUtils.sort(radioLefts);
        for (int i = 0; i < restNum; i++)
        {
            recordNums[sortedIndexs[i % sortedIndexs.length]] += 1;
        }
        
        //如果不写入，直接返回
        if (!write)
        {
            return recordNums;
        }
        
        //初始化时间统计对象
        TimeStatis tsWrite2File =
            new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] step 3.1 write records to files!",
                transactionId,
                timestamp));
        tsWrite2File.startTiming();
        
        //写文件获取真实文件名
        String[] realFileNames = write2File(rfileNames, recordNums, columnFieldTypes);
        
        tsWrite2File.endTiming();
        
        //写文件接收释放内存
        this.recordQueue = null;
        
        //初始化时间统计对象
        TimeStatis tsWrite2DB =
            new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] step 3.2 load files to database!",
                transactionId,
                timestamp));
        tsWrite2DB.startTiming();
        
        //写数据库临时表
        write2DB(realFileNames, tmpTableNames, columnFieldTypes);
        
        tsWrite2DB.endTiming();
        
        //初始化时间统计对象
        TimeStatis tsCompress =
            new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] step 3.3 compress files!",
                transactionId,
                timestamp));
        tsCompress.startTiming();
        
        //压缩并删除realFileNames
        compressDelete(realFileNames, rfileNames);
        
        tsCompress.endTiming();
        
        return recordNums;
    }
    
    /**
     * 压缩和删除文件
     * @param srcFileNames 原文件名数组(待压缩和删除文件)
     * @param dstFileNames 目标文件名数组(创建的压缩文件)
     * @throws CException 异常
     */
    private void compressDelete(String[] srcFileNames, String[] dstFileNames)
        throws CException
    {
        //参数检查错误直接返回
        if (null == srcFileNames || null == dstFileNames || srcFileNames.length != dstFileNames.length)
        {
            LOGGER.error("compressDelete failed! srcFileNames is {}, dstFileNames is {}", srcFileNames, dstFileNames);
            return;
        }
        
        for (int i = 0; i < srcFileNames.length; i++)
        {
            //压缩文件产生zip的文件
            OdpCommonUtils.compress(srcFileNames[i], dstFileNames[i]);
            
            //删除原文件
            OdpCommonUtils.deleteFile(srcFileNames[i]);
        }
        
    }
    
    
    /** 
     * 将文件导入到响应的数据表中（文件不应有列名头，并且列顺序需与表中的列顺序一致）
     * @param realFileNames 文件名数据
     * @param tableTables 导入数据的表名数组
     * @param cfMappings 表列对象字段映射数组
     */
    private void write2DB(String[] realFileNames, String[] tableTables, ColumnFieldMapping[] cfMappings)
    {
        //参数检查错误直接返回
        if (null == realFileNames || null == tableTables || realFileNames.length != tableTables.length)
        {
            LOGGER.error("write2DB failed! realFileNames is {}, tableTables is {}", realFileNames, tableTables);
            return;
        }
        
        final List<String> columns = new ArrayList<String>(cfMappings.length);
        for (int i = 0; i < cfMappings.length; i++)
        {
            columns.add(cfMappings[i].getColumnName());
        }
        
        for (int i = 0; i < realFileNames.length; i++)
        {
            loadFile2Table(realFileNames[i], tableTables[i], columns);
        }
    }
    
    /**
     * 向各个文件中写入相应记录数的记录
     * @param fileNames 文件名数组
     * @param recordNums 每个文件的记录数数组
     * @param cfMappings 表列对象字段映射数组
     * @throws CException 异常
     */
    private String[] write2File(final String[] fileNames, final long[] recordNums, ColumnFieldMapping[] cfMappings)
    {
        long startIndex = 0;
        IRecords2File<T> records2File = null;
        //真实文件名
        final String[] realFileNames = new String[fileNames.length];
        for (int i = 0; i < recordNums.length; i++)
        {
            //使用特殊行列分隔符分割的压缩文件
            records2File = new Records2TxtFile<T>(fileNames[i], false, cfMappings);
            //将记录写入到文件中
            try
            {
                realFileNames[i] = records2File.writeRecords2File(recordQueue.grabRecords(startIndex, recordNums[i]));
            }
            catch (CException e)
            {
                LOGGER.error("write2File failed! fileName is {},recordNums is {}", new Object[] {fileNames, recordNums,
                    e});
            }
            //起始位置累进
            startIndex += recordNums[i];
        }
        
        return realFileNames;
    }
    
    /**
     * 将文件中的数据加载到指定表中
     * @param fileName 文件名
     * @param tableName 表名
     * @param columns 列名列表
     */
    public void loadFile2Table(final String fileName, final String tableName, List<String> columns)
    {
        try
        {
            odpDao.loadFile2Table(fileName, tableName, columns);
        }
        catch (CException e)
        {
            //记录日志
            LOGGER.error("loadFile2Table failed! fileName is {}, tableName is {}",
                new Object[] {fileName, tableName, e});
        }
    }
    
    /** 
     * 将记录写入到临时表中
     * @param tmpTables 临时表表名数组
     * @param recordNums 每个临时表的记录数
     * @param cfMappings 表列对象字段映射数组
     */
    public void write2DB(String[] tmpTables, long[] recordNums, ColumnFieldMapping[] cfMappings)
    {
        //计算每个临时表的记录累加数
        long[] cumRecordNums = new long[recordNums.length];
        cumRecordNums[0] = recordNums[0];
        for (int i = 1; i < recordNums.length; i++)
        {
            cumRecordNums[i] = cumRecordNums[i - 1] + recordNums[i];
        }
        
        long startIndex = 0;
        //总记录数
        long endIndex = cumRecordNums[cumRecordNums.length - 1];
        //数组索引值
        int index = 0;
        long length = 0;
        String tableName = null;
        while (startIndex < endIndex)
        {
            tableName = tmpTables[index];
            if (startIndex + RECORDS_PER_INSERT > cumRecordNums[index])
            {
                length = cumRecordNums[index] - startIndex;
                //保证下一次表名切换
                index++;
            }
            else
            {
                length = RECORDS_PER_INSERT;
            }
            //将记录写入临时表
            batchInsertRecords(tableName, cfMappings, recordQueue.grabRecords(startIndex, length));
            
            startIndex += length;
        }
    }
    
    /**
     * 批量插入数据
     * @param records 用户记录
     * @param tableName 表明
     * @param cfMappings 表列对象字段映射数组
     */
    @SuppressWarnings("unchecked")
    public void batchInsertRecords(String tableName, ColumnFieldMapping[] cfMappings, List<T> records)
    {
        try
        {
            odpDao.batchInsertRecords(tableName, cfMappings, (List<UserProfileEntity>)records);
        }
        catch (CException e)
        {
            LOGGER.error("batchInsertRecords failed!", e);
        }
    }
    
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    public void setOdpDao(OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
}
