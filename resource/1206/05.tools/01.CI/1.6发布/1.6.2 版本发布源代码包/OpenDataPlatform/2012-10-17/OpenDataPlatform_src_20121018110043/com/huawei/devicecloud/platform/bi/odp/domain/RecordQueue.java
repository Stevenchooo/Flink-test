/*
 * 文 件 名:  RecordQueue.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  记录队列集合
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-13
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录队列集合
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-13]
 * @param <T> 记录类型
 */
public class RecordQueue<T>
{
    //大集合初始化大小值
    private static final int BIG_SIZE = 10000;
    
    //记录总数
    private int recordCount;
    
    //记录限制数
    private Long recordLimit;
    
    //用户信息集合Map
    private List<T> records;
    
    /**
     * 构造函数
     * @param recordLimit 记录总数限制，为null表示无限制
     */
    public RecordQueue(Long recordLimit)
    {
        this.recordCount = 0;
        this.recordLimit = recordLimit;
        records = new ArrayList<T>(BIG_SIZE);
    }
    
    /**
     * 将record放到队列中
     * @param record 记录对象
     * @return 是否允许继续插入记录（记录总数达到限制数后不允许继续插入记录）
     */
    public synchronized boolean add(T record)
    {
        if (null != recordLimit && recordCount + 1 > recordLimit)
        {
            return true;
        }
        
        //记录总数+1
        recordCount++;
        
        //将record存放到集合中
        if (null != record)
        {
            records.add(record);
        }
        
        return false;
    }
    
    /**
     * 记录总数
     * @return 记录总数
     */
    public synchronized long getRecordsCount()
    {
        return records.size();
    }
    
    /**
     * 从记录集合中获取startIndex开始的num条记录
     * @param startIndex 起始位置
     * @param length 获取的记录数
     * @return 记录列表
     */
    public synchronized List<T> grabRecords(long startIndex, long length)
    {
        if (length < 0 || startIndex < 0 || startIndex >= records.size())
        {
            return new ArrayList<T>(0);
        }
        
        return records.subList((int)startIndex, (int)Math.min(startIndex + length, records.size()));
    }
    
    /**
     * 获取所有的记录列表
     * @return 记录列表
     */
    public synchronized List<T> grabRecords()
    {
        return records;
    }
}
