/*
 * 文 件 名:  ReservedInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  预留信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-11
 */
package com.huawei.devicecloud.platform.bi.odp.domain;


/**
 * 预留信息
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-11]
 */
public class ReservedInfo
{
    //预留Id
    private String reserveId;
    
    //日期
    private String date;
    
    //记录数
    private Long recordCount;
    
    /**
     * 获取预留Id
     * @return 预留Id
     */
    public String getReserveId()
    {
        return reserveId;
    }

    /**
     * 设置预留Id
     * @param reserveId 预留Id
     */
    public void setReserveId(String reserveId)
    {
        this.reserveId = reserveId;
    }
    
    /**
     * 获取日期
     * @return 日期
     */
    public String getDate()
    {
        return date;
    }
    
    /**
     * 设置日期
     * @param date 日期
     */
    public void setDate(String date)
    {
        this.date = date;
    }
    
    /**
     * 获取记录总数
     * @return 记录总数
     */
    public Long getRecordCount()
    {
        return recordCount;
    }
    
    /**
     * 设置记录总数
     * @param recordCount 记录总数
     */
    public void setRecordCount(Long recordCount)
    {
        this.recordCount = recordCount;
    }
}
