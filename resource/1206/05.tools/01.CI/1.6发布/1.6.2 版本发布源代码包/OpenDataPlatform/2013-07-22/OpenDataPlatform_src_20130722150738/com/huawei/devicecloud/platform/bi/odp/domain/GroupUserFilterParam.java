/*
 * 文 件 名:  GroupUserFilterParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  过滤用户组数据的参数
 * 创 建 人:  z00190465
 * 创建时间:  2012-10-9
 */
package com.huawei.devicecloud.platform.bi.odp.domain;

import java.util.List;

/**
 * 过滤用户组数据的参数
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-10-9]
 */
public class GroupUserFilterParam
{
    //目标表（最终设备信息表）
    private String destTable;
    
    //临时结果表
    private String trt;
    
    //公用结果表（周表）
    private String crt;
    
    //组Id
    private String groupId;
    
    //使用日期
    private String useDate;
    
    //一周内的左右邻居日期集合
    private List<String> neighborDates;
    
    //一周内的左右邻居日期集合“('2012-10-08','2012-10-09','2012-10-10'）”
    private String neiDatas;
    
    //最多选取的记录数
    private long limitNum;
    
    private Long destTableCount;
    
    public String getNeiDatas()
    {
        return neiDatas;
    }

    public void setNeiDatas(String neiDatas)
    {
        this.neiDatas = neiDatas;
    }

    public String getDestTable()
    {
        return destTable;
    }
    
    public void setDestTable(String destTable)
    {
        this.destTable = destTable;
    }
    
    public String getTrt()
    {
        return trt;
    }
    
    public void setTrt(String trt)
    {
        this.trt = trt;
    }
    
    public String getCrt()
    {
        return crt;
    }
    
    public void setCrt(String crt)
    {
        this.crt = crt;
    }
    
    public String getGroupId()
    {
        return groupId;
    }
    
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }
    
    public String getUseDate()
    {
        return useDate;
    }
    
    public void setUseDate(String useDate)
    {
        this.useDate = useDate;
    }
    
    public List<String> getNeighborDates()
    {
        return neighborDates;
    }
    
    public void setNeighborDates(List<String> neighborDates)
    {
        this.neighborDates = neighborDates;
    }
    
    public long getLimitNum()
    {
        return limitNum;
    }
    
    public void setLimitNum(long limitNum)
    {
        this.limitNum = limitNum;
    }

    public Long getDestTableCount()
    {
        return destTableCount;
    }

    public void setDestTableCount(Long destTableCount)
    {
        this.destTableCount = destTableCount;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupUserFilterParam [destTable=");
        builder.append(destTable);
        builder.append(", trt=");
        builder.append(trt);
        builder.append(", crt=");
        builder.append(crt);
        builder.append(", groupId=");
        builder.append(groupId);
        builder.append(", useDate=");
        builder.append(useDate);
        builder.append(", neighborDates=");
        builder.append(neighborDates);
        builder.append(", neiDatas=");
        builder.append(neiDatas);
        builder.append(", limitNum=");
        builder.append(limitNum);
        builder.append(", destTableCount=");
        builder.append(destTableCount);
        builder.append("]");
        return builder.toString();
    }
    
    
    
}
