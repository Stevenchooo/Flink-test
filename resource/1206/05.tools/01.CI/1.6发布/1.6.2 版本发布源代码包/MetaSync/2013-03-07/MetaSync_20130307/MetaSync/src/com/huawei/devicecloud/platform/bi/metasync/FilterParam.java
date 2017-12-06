/*
 * 文 件 名:  FilterParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  过滤参数
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-1
 */
package com.huawei.devicecloud.platform.bi.metasync;

/**
 * 表过滤参数
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-1]
 */
public class FilterParam
{
    private String tblName;
    
    private Long tblId;
    
    private String comPartName;
    
    private String minPartValue;
    
    private String maxPartValue;
    
    public Long getTblId()
    {
        return tblId;
    }
    
    public void setTblId(Long tblId)
    {
        this.tblId = tblId;
    }
    
    public String getTblName()
    {
        return tblName;
    }
    
    public void setTblName(String tblName)
    {
        this.tblName = tblName;
    }
    
    public String getComPartName()
    {
        return comPartName;
    }
    
    public void setComPartName(String comPartName)
    {
        this.comPartName = comPartName;
    }
    
    public String getMinPartValue()
    {
        return minPartValue;
    }
    
    public void setMinPartValue(String minPartValue)
    {
        this.minPartValue = minPartValue;
    }
    
    public String getMaxPartValue()
    {
        return maxPartValue;
    }
    
    public void setMaxPartValue(String maxPartValue)
    {
        this.maxPartValue = maxPartValue;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("FilterParam [tblName=");
        builder.append(tblName);
        builder.append(", tblId=");
        builder.append(tblId);
        builder.append(", comPartName=");
        builder.append(comPartName);
        builder.append(", minPartValue=");
        builder.append(minPartValue);
        builder.append(", maxPartValue=");
        builder.append(maxPartValue);
        builder.append("]");
        return builder.toString();
    }
}
