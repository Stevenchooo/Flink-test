/*
 * 文 件 名:  Records2TxtZipFile.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  记录集合写入到文件
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-15
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.util.List;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;

/**
 * 记录集合写入到文件（HIVE文件格式）
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-15]
 * @param <T> 泛型类型
 */
public class Records2TxtZipFile<T> extends Records2TxtFile<T>
{
    /**
     * 构造函数
     * @param fileName 文件名
     * @param writeHeader 是否写文件头
     * @param columnFields 表列对象字段映射数组
     */
    public Records2TxtZipFile(String fileName, boolean writeHeader, ColumnFieldMapping[] columnFields)
    {
        super(fileName, writeHeader, columnFields);
    }
    
    /**
     * 将记录
     * @param records 记录列表
     * @throws CException 异常
     * @return 真实文件名
     */
    @Override
    public String writeRecords2File(List<T> records)
        throws CException
    {
        //初始化时间统计对象
        TimeStatis tsWrites = new TimeStatis(String.format("[NORMAL USER][writeRecords2File] "));
        tsWrites.startTiming();
        String srcFileName = super.writeRecords2File(records);
        tsWrites.endTiming();
        
        TimeStatis tsCompress = new TimeStatis(String.format("[NORMAL USER][compress] "));
        tsCompress.startTiming();
        //压缩文件产生zip的文件
        OdpCommonUtils.compress(srcFileName, getFileName());
        tsCompress.endTiming();
        
        //删除原文件
        OdpCommonUtils.deleteFile(srcFileName);
        
        return getFileName();
    }
}
