/*
 * 文 件 名:  NormalUsersProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  普通用户处理器
 * 创 建 人:  z00190465
 * 创建时间:  2012-10-9
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;
import com.huawei.devicecloud.platform.bi.odp.constants.OdpConfig;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldTypeMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.DateRatioInfo;
import com.huawei.devicecloud.platform.bi.odp.domain.GroupUserFilterParam;
import com.huawei.devicecloud.platform.bi.odp.domain.RecordQueue;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;

/**
 * 普通用户处理器
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-11]
 * @param <T> 泛型类型
 */
public class NormalUsersProcess<T> implements IConsumer
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TempTableRecordProcess.class);
    
    //列字段名映射关系数组
    private ColumnFieldMapping[] columnFieldTypes;
    
    //临时表名数组
    private String[] tmpTableNames;
    
    private String[] rfileNames;
    
    //批次信息数组
    private List<DateRatioInfo> batchs;
    
    //存放记录的队列
    private RecordQueue<T> recordQueue;
    
    //Dao层调用对象
    private OdpDao odpDao;
    
    /**
     * 默认构造函数
     * @param columnFields 表列对象字段映射数组
     * @param resTableNames 结果表名
     * @param rfileNames 预留文件名
     * @param batchs 分批信息
     * @param recordQueue 记录队列
     */
    public NormalUsersProcess(ColumnFieldMapping[] columnFields, String[] resTableNames, String[] rfileNames,
        List<DateRatioInfo> batchs, RecordQueue<T> recordQueue)
    {
        this.columnFieldTypes = columnFields;
        this.rfileNames = rfileNames;
        this.tmpTableNames = resTableNames;
        this.batchs = batchs;
        this.recordQueue = recordQueue;
    }
    
    /**
     * 取出记录插入到相应的临时表中，参数不合法返回null
     * @param write 是否写入
     * @param transactionId 交易Id
     * @param groupIds 分组id集合
     * @param limit 总记录数限制
     * @param timestamp 时间戳
     * @return 记录数
     * @throws CException 异常
     */
    public long[] writeRecords(boolean write, long limit, String[] groupIds, String transactionId, String timestamp)
        throws CException
    {
        //需要保证每次请求的文件名和表名不重复
        String trt = String.format("trt_%s", groupIds[0]);
        //trt的文件名
        String trtFile = OdpConfig.createOdpConfig().getRfilesDir() + trt;
        
        //初始化时间统计对象
        TimeStatis tsWrite2File =
            new TimeStatis(
                String.format("[NORMAL USER][groups_prepare(tid=%s,ts=%s)] step 3.1 write records to file[%s...]!",
                    transactionId,
                    timestamp,
                    trtFile));
        tsWrite2File.startTiming();
        
        //创建TRT表
        odpDao.createTRT(trt);
        
        //写入到文件中
        String realTrtFile = write2File(trtFile, columnFieldTypes);
        long[] limitNums = computeLimitNums(limit);
        long[] recordNums = new long[batchs.size()];
        this.recordQueue = null;
        
        tsWrite2File.endTiming();
        
        //初始化时间统计对象
        TimeStatis tsWrite2DB =
            new TimeStatis(
                String.format("[NORMAL USER][groups_prepare(tid=%s,ts=%s)] step 3.2 load file[%s] to table[%s]!",
                    transactionId,
                    timestamp,
                    realTrtFile,
                    trt));
        tsWrite2DB.startTiming();
        
        //将数据写入
        write2DB(realTrtFile, trt);
        
        tsWrite2DB.endTiming();
        
        //初始化时间统计对象
        TimeStatis tsFiltrate =
            new TimeStatis(String.format("[NORMAL USER][groups_prepare(tid=%s,ts=%s)] step 3.3 filtrate imeis!",
                transactionId,
                timestamp));
        tsFiltrate.startTiming();
        
       
       
        //日期比例信息集合
        DateRatioInfo dateRatio;
        //crt表
        String crt;
        for (int i = 0; i < batchs.size(); i++)
        {
            dateRatio = batchs.get(i);
            //获取日期
            Date date = CommonUtils.convertDateTimeFormat(dateRatio.getDate(), null);
            //获取周Id
            String mondayStr = OdpCommonUtils.getMondayDateStr(date);
            //创建crt表
            crt = String.format("crt_%s", mondayStr);
            odpDao.createCRT(crt);
            
            GroupUserFilterParam param = new GroupUserFilterParam();
            param.setCrt(crt);
            param.setDestTable(tmpTableNames[i]);
            param.setGroupId(groupIds[i]);
            param.setLimitNum(limitNums[i]);
            //获取相邻三天日期（周一和周日仅取两天）
            List<String> neighborStrs = OdpCommonUtils.getNeighborDateStr(date);
            param.setNeighborDates(neighborStrs);
            param.setTrt(trt);
            param.setUseDate(OdpCommonUtils.covDayStr(date));
            
            if (write)
            {
                odpDao.prepareGroup(param);
            }
            else
            {
                //仅查询
                recordNums[i] = odpDao.prepareGroupQuery(param);
            }
        }
        tsFiltrate.endTiming();
        
        
       
        if (!write)
        {
            //初始化时间统计对象
            TimeStatis tsQueryGroups =
                new TimeStatis(String.format("[NORMAL USER][groups_prepare(tid=%s,ts=%s)] "
                    + "step 3.4 get records count of tables!", transactionId, timestamp));
            tsQueryGroups.startTiming();
            
            //获取每批数据的记录总数
            for (int i = 0; i < batchs.size(); i++)
            {
                //recordNums[i] = odpDao.getTRTCount(trt, tmpTableNames[i]);
            }
            
            tsQueryGroups.endTiming();
        }
        else
        {
            //初始化时间统计对象
            TimeStatis tsWriteGroups =
                new TimeStatis(String.format("[NORMAL USER][groups_prepare(tid=%s,ts=%s)] "
                    + "step 3.4 write records to compress files from tables!", transactionId, timestamp));
            tsWriteGroups.startTiming();
            
            //获取每个批次的记录并写入到文件
            for (int i = 0; i < batchs.size(); i++)
            {
                recordNums[i] = table2File(tmpTableNames[i], rfileNames[i]);
            }
            
            tsWriteGroups.endTiming();
        }
        
        //清楚临时资源
        //删除trt表
        odpDao.dropTRT(trt);
        //删除realTrtFile文件
        OdpCommonUtils.deleteFile(realTrtFile);
        
        return recordNums;
    }
    
    //将表中的记录写入压缩文件并返回记录的总数
    private long table2File(final String tableName, final String fileName)
        throws CException
    {
        //初始化时间统计对象
        TimeStatis tsWriteGroups =
            new TimeStatis(String.format("[NORMAL USER][table2File] " + "table2File", tableName, fileName));
        tsWriteGroups.startTiming();
        //创建读取文件
//        List<UserProfileEntity> records = odpDao.getUPRecords(tableName);
        List<UserProfileEntity> records = odpDao.getFinalRecords(tableName);
        
        //初始化时间统计对象
        tsWriteGroups.endTiming();
        
        //获取列名数组
        String[] columnNames = {"device_id"};
        String resultMapId = OdpConfig.createOdpConfig().getResultMapID();
        //获取表列与字段的映射数组
        ColumnFieldTypeMapping[] cfMappings =
            OdpCommonUtils.getChoosedCFTMappings(this.odpDao, resultMapId, columnNames);
        //初始化记录写文件对象
        IRecords2File<UserProfileEntity> records2File =
            new Records2TxtZipFile<UserProfileEntity>(fileName, false, cfMappings);
        //将记录写入到压缩文件中
        records2File.writeRecords2File(records);
        
        return records.size();
    }
    
    //计算限制数
    private long[] computeLimitNums(long limit)
    {
        long[] recordNums = new long[batchs.size()];
        //将记录按照比例分配后剩余的非整数部分值数组
        float[] radioLefts = new float[batchs.size()];
        
        //计算比例和
        int ratioSum = 0;
        for (int i = 0; i < batchs.size(); i++)
        {
            ratioSum += batchs.get(i).getRatio();
        }
        long recordCount = OdpCommonUtils.getMinValue(recordQueue.getRecordsCount(), limit);
        //计算每个临时表的记录数
        long restNum = recordCount;
        for (int i = 0; i < batchs.size(); i++)
        {
            recordNums[i] = (long)((float)batchs.get(i).getRatio() / ratioSum * recordCount);
            //剩余比例
            radioLefts[i] = (float)batchs.get(i).getRatio() / ratioSum * recordCount - recordNums[i];
            restNum = restNum - recordNums[i];
        }
        //按照ratios剩余率从大到小均匀分配restNum数
        int[] sortedIndexs = OdpCommonUtils.sort(radioLefts);
        for (int i = 0; i < restNum; i++)
        {
            recordNums[sortedIndexs[i % sortedIndexs.length]] += 1;
        }
        return recordNums;
    }
    
    /** 
     * 将文件导入到响应的数据表中（文件不应有列名头，并且列顺序需与表中的列顺序一致）
     * @param realFileNames 文件名数据
     * @param tableTables 导入数据的表名数组
     * @param cfMappings 表列对象字段映射数组
     */
    private void write2DB(String fileName, String tableName)
        throws CException
    {
        //参数检查错误直接返回
        if (null == fileName || null == tableName)
        {
            LOGGER.error("write2DB failed! fileName is {}, tableName is {}", fileName, tableName);
            return;
        }
        
        //创建Tmp_Result_Table表
        odpDao.createTRT(tableName);
        
        loadFile2TRT(fileName, tableName);
    }
    
    /**
     * 向各个文件中写入相应记录数的记录
     * @param fileName 文件名数组
     * @param cfMappings 表列对象字段映射数组
     * @throws CException 异常
     * @return 真正产生的文件名
     */
    private String write2File(final String fileName, final ColumnFieldMapping[] cfMappings)
    {
        IRecords2File<T> records2File = new Records2TxtFile<T>(fileName, false, cfMappings);
        //将记录写入到文件中
        try
        {
            return records2File.writeRecords2File(recordQueue.grabRecords());
        }
        catch (CException e)
        {
            LOGGER.error("write2File failed! fileName is {}", new Object[] {fileName, e});
        }
        
        return null;
    }
    
    /**
     * 将文件中的数据加载到指定表中
     * @param fileName 文件名
     * @param tableName 表名
     */
    public void loadFile2TRT(final String fileName, final String tableName)
    {
        try
        {
            odpDao.loadFile2TRT(fileName, tableName);
        }
        catch (CException e)
        {
            //记录日志
            LOGGER.error("loadFile2Table failed! fileName is {}, tableName is {}",
                new Object[] {fileName, tableName, e});
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
