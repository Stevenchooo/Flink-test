/*
 * 文 件 名:  GroupInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  组信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-11
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

/**
 * 组信息
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-11]
 */
public class GroupInfo
{
    //组Id
    private String group_id;
    
    //日期
    private String date;
    
    //记录总数
    private Long record_count;
    
    public String getGroup_id()
    {
        return group_id;
    }
    
    public void setGroup_id(final String group_id)
    {
        this.group_id = group_id;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public void setDate(final String date)
    {
        this.date = date;
    }
    
    public Long getRecord_count()
    {
        return record_count;
    }
    
    public void setRecord_count(final Long record_count)
    {
        this.record_count = record_count;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("GroupInfo [group_id=");
        builder.append(group_id);
        builder.append(", date=");
        builder.append(date);
        builder.append(", record_count=");
        builder.append(record_count);
        builder.append("]");
        return builder.toString();
    }
}
