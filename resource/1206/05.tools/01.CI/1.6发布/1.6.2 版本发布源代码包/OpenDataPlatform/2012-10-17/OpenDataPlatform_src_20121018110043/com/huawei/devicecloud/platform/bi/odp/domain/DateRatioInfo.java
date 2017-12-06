/*
 * 文 件 名:  DateRatioInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  日期比例复合类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.domain;


/**
 *  日期比例复合类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class DateRatioInfo
{
    //日期
    private String date;
    
    //非负比例值
    private Integer ratio;

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public Integer getRatio()
    {
        return ratio;
    }

    public void setRatio(Integer ratio)
    {
        this.ratio = ratio;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("DateRatioInfo [date=");
        builder.append(date);
        builder.append(", ratio=");
        builder.append(ratio);
        builder.append("]");
        return builder.toString();
    }
}
