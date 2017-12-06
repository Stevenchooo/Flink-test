/*
 * 文 件 名:  BatchRecord.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-14
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

import java.util.List;

/**
 * 批量记录
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-14]
 * @param <T> 泛型
 */
public class BatchRecord<T>
{
    //最大存放记录数
    private boolean exceedLimit = false;
    
    //记录列表
    private List<T> writeRecords = null;
    
    public boolean isExceedLimit()
    {
        return exceedLimit;
    }

    public void setExceedLimit(boolean exceedLimit)
    {
        this.exceedLimit = exceedLimit;
    }

    public List<T> getWriteRecords()
    {
        return writeRecords;
    }

    public void setWriteRecords(List<T> writeRecords)
    {
        this.writeRecords = writeRecords;
    }
}
