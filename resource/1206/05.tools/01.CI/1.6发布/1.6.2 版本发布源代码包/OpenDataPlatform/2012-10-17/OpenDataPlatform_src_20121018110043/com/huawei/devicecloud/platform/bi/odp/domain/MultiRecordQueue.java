/*
 * 文 件 名:  MultiRecordQueue.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-13
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录队列集合
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-13]
 * @param <T> 记录类型
 */
public class MultiRecordQueue<T>
{
    //小集合初始化大小
    private static final int SMALL_SIZE = 20;
    
    //大集合初始化大小
    private static final int BIG_SIZE = 1000;
    
    /**
     * 写入数据库表阈值
     */
    private static final Integer WRITE_THRESHOLD = 2;
    
    //锁对象
    private Map<Integer, Object> locks;
    
    //分批写入临时表的记录总数
    private int[] recordNums;
    
    //记录总数
    private int recordCount;
    
    //记录限制数
    private int recordLimit;
    
    //用户信息集合Map
    private Map<Integer, List<T>> recordsMap;
    
    //队列数
    private Integer queueNum;
    
    /**
     * 构造函数
     * @param recordLimit 记录总数限制
     * @param queueNum 队列数
     */
    public MultiRecordQueue(int queueNum, int recordLimit)
    {
        this.recordCount = 0;
        this.recordLimit = recordLimit;
        this.queueNum = queueNum;
        recordNums = new int[queueNum];
        recordsMap = new HashMap<Integer, List<T>>(SMALL_SIZE);
        locks = new HashMap<Integer, Object>(SMALL_SIZE);
        for (Integer i = 0; i < queueNum; i++)
        {
            //计算比例和
            //为每个批次分配一个用户信息集合
            recordsMap.put(i, new ArrayList<T>(BIG_SIZE));
            //锁对象
            locks.put(i, new Object());
        }
    }
    
    /**
     * 将record放到queueId队列中，如果队列总数超过写入阈值就取出整个队列
     * @param queueId 队列Id
     * @param record 记录对象
     * @return 可写入数据库的记录队列
     */
    public BatchRecord<T> put(Integer queueId, T record)
    {
        BatchRecord<T> rtn = new BatchRecord<T>();
        List<T> writeRecords = null;
        if (null == queueId || queueId < 0 || queueId >= queueNum)
        {
            return rtn;
        }
        
        synchronized (locks.get(queueId))
        {
            if (recordCount + 1 > recordLimit)
            {
                rtn.setExceedLimit(true);
                return rtn;
            }
            
            //记录总数+1
            recordCount++;
            
            //将userprofile存放到集合中
            if (null != record)
            {
                recordsMap.get(queueId).add(record);
            }
            
            recordNums[queueId]++;
            //判断记录是否超过WRITE_THRESHOLD
            if (recordsMap.get(queueId).size() >= WRITE_THRESHOLD)
            {
                //取WRITE_THRESHOLD记录
                writeRecords = recordsMap.remove(queueId);
                //存放新的数组对象
                recordsMap.put(queueId, new ArrayList<T>(BIG_SIZE));
            }
            
            rtn.setWriteRecords(writeRecords);
        }
        
        return rtn;
    }
    
    /**
     * 获取队列中剩余的的记录集合
     * @param queueId 队列Id
     * @return 获取队列中剩余的的记录集合
     */
    public List<T> getLastRecords(int queueId)
    {
        List<T> writeRecords = null;
        
        if (queueId >= queueNum || queueId < 0)
        {
            return writeRecords;
        }
        
        synchronized (locks.get(queueId))
        {
            //取WRITE_THRESHOLD记录
            writeRecords = recordsMap.remove(queueId);
            //存放新的数组对象
            recordsMap.put(queueId, new ArrayList<T>(BIG_SIZE));
        }
        
        return writeRecords;
    }
    
    /**
     * 获取队列已经存放过的记录总数
     * @param queueId 队列Id
     * @return 已经存放过的记录总数
     */
    public Integer getRecordNums(int queueId)
    {
        if (queueId >= queueNum || queueId < 0)
        {
            return 0;
        }
        
        synchronized (locks.get(queueId))
        {
            return recordNums[queueId];
        }
    }
}
