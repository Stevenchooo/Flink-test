/*
 * 文 件 名:  ConfigEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  配置实体类（单记录）
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

/**
 * 配置实体类（单记录）
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class ConfigEntity
{
    //push服务地址
    private String pushSvrAddr;
    
    //hiad服务地址
    private String hiadAddr;
    
    //流控阈值
    private Integer loadCtrlThreshold;
    
    //token失效时间
    private Integer tokenExpiredTime;
    
    //route每天执行时间
    private Integer routineExecuteTime;
    
    //最大预留天数
    private Integer maxReservedDays;
    
    //最大返回记录数
    private Long maxReturnRecords;
    
    public Integer getMaxReservedDays()
    {
        return maxReservedDays;
    }
    
    public void setMaxReservedDays(Integer maxReservedDays)
    {
        this.maxReservedDays = maxReservedDays;
    }
    
    public Long getMaxReturnRecords()
    {
        return maxReturnRecords;
    }
    
    public void setMaxReturnRecords(Long maxReturnRecords)
    {
        this.maxReturnRecords = maxReturnRecords;
    }
    
    public String getPushSvrAddr()
    {
        return pushSvrAddr;
    }
    
    public void setPushSvrAddr(String pushSvrAddr)
    {
        this.pushSvrAddr = pushSvrAddr;
    }
    
    public String getHiadAddr()
    {
        return hiadAddr;
    }
    
    public void setHiadAddr(String hiadAddr)
    {
        this.hiadAddr = hiadAddr;
    }
    
    public Integer getLoadCtrlThreshold()
    {
        return loadCtrlThreshold;
    }
    
    public void setLoadCtrlThreshold(Integer loadCtrlThreshold)
    {
        this.loadCtrlThreshold = loadCtrlThreshold;
    }
    
    public Integer getTokenExpiredTime()
    {
        return tokenExpiredTime;
    }
    
    public void setTokenExpiredTime(Integer tokenExpiredTime)
    {
        this.tokenExpiredTime = tokenExpiredTime;
    }
    
    public Integer getRoutineExecuteTime()
    {
        return routineExecuteTime;
    }
    
    public void setRoutineExecuteTime(Integer routineExecuteTime)
    {
        this.routineExecuteTime = routineExecuteTime;
    }
}