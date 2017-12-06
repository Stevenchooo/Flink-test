/*
 * 文 件 名:  ReservedInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  预留信息类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

import java.util.Date;

/**
 * 预留信息类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class ReservedInfoEntity
{
    //预留Id
    private String reserveId;
    
    //使用日期
    private Date useDate;
    
    //实现日期
    private Date expiredTime;
    
    //结果表名
    private String resultTableName;
    
    //用户标识类型
    private Integer userType;
         
    //保留天数
    private Integer days;
    
    //用“,”号分隔的列名集合
    private String columnNameList;
    
    //状态
    private Integer state;
    
    //文件的路径地址
    private String fileUrl;
    
    //创建预留信息的应用标识
    private String createAppId;
    
    public Date getExpiredTime()
    {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime)
    {
        this.expiredTime = expiredTime;
    }

    public Date getUseDate()
    {
        return useDate;
    }

    public void setUseDate(Date useDate)
    {
        this.useDate = useDate;
    }

    public String getResultTableName()
    {
        return resultTableName;
    }

    public void setResultTableName(String resultTableName)
    {
        this.resultTableName = resultTableName;
    }

    public Integer getUserType()
    {
        return userType;
    }

    public void setUserType(Integer userType)
    {
        this.userType = userType;
    }

    public Integer getDays()
    {
        return days;
    }
    
    public void setDays(Integer days)
    {
        this.days = days;
    }
    
    public String getReserveId()
    {
        return reserveId;
    }
    
    public void setReserveId(String reserveId)
    {
        this.reserveId = reserveId;
    }
    
    public String getColumnNameList()
    {
        return columnNameList;
    }
    
    public void setColumnNameList(String columnNameList)
    {
        this.columnNameList = columnNameList == null ? null : columnNameList.trim();
    }
    
    public Integer getState()
    {
        return state;
    }
    
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    public String getFileUrl()
    {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }
    
    public String getCreateAppId()
    {
        return createAppId;
    }
    
    public void setCreateAppId(String createAppId)
    {
        this.createAppId = createAppId == null ? null : createAppId.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ReservedInfoEntity [reserveId=");
        builder.append(reserveId);
        builder.append(", useDate=");
        builder.append(useDate);
        builder.append(", expiredTime=");
        builder.append(expiredTime);
        builder.append(", resultTableName=");
        builder.append(resultTableName);
        builder.append(", userType=");
        builder.append(userType);
        builder.append(", days=");
        builder.append(days);
        builder.append(", columnNameList=");
        builder.append(columnNameList);
        builder.append(", state=");
        builder.append(state);
        builder.append(", fileUrl=");
        builder.append(fileUrl);
        builder.append(", createAppId=");
        builder.append(createAppId);
        builder.append("]");
        return builder.toString();
    }
}