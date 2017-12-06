/*
 * 文 件 名:  TempTableRecordProcessParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  临时表处理参数类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-14
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;

/**
 * 临时表处理参数类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-14]
 * @param <T> 泛型类型
 */
public class TempTableRecordProcessParam<T>
{
    //列字段类型映射关系数组
    private ColumnFieldTypeMapping[] columnFieldTypes;
    
    //记录类对象
    private Class<? extends Object> recordClass;
    
    //mapping文件中的resultMap的Id
    private String resultMapID;
    
    //记录队列
    private RecordQueue<T> recordQueue;
    
    //Dao对象
    private OdpDao odpDao;
    
    
    public ColumnFieldTypeMapping[] getColumnFieldTypes()
    {
        return columnFieldTypes;
    }

    public void setColumnFieldTypes(ColumnFieldTypeMapping[] columnFieldTypes)
    {
        this.columnFieldTypes = columnFieldTypes;
    }

    public RecordQueue<T> getRecordQueue()
    {
        return recordQueue;
    }
    
    public void setRecordQueue(RecordQueue<T> recordQueue)
    {
        this.recordQueue = recordQueue;
    }
    
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    public void setOdpDao(OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
    
    public Class<? extends Object> getRecordClass()
    {
        return recordClass;
    }
    
    public void setRecordClass(Class<? extends Object> recordClass)
    {
        this.recordClass = recordClass;
    }
    
    public String getResultMapID()
    {
        return resultMapID;
    }
    
    public void setResultMapID(String resultMapID)
    {
        this.resultMapID = resultMapID;
    }
}
