/*
 * 文 件 名:  IRecords2File.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  记录写入文件接口
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-17
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.util.List;

import com.huawei.devicecloud.platform.bi.common.CException;

/**
 * 记录写入文件接口
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-17]
 * @param <T> 泛型
 */
public interface IRecords2File<T>
{
    /**
     * 将记录
     * @param records 记录列表
     * @throws CException 异常
     * @return 实际产生的文件名
     */
    String writeRecords2File(List<T> records)
        throws CException;
}
