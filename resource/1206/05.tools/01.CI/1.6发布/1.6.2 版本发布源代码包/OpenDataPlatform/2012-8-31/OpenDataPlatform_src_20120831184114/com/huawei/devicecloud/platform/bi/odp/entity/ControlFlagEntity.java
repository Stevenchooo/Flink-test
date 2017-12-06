/*
 * 文 件 名:  ConfigEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  控制标识实体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

/**
 * 控制标识实体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class ControlFlagEntity
{
    //当前可用表Id
    private Integer currentTableID;
    
    //当前可用路由表Id
    private Integer currentRouteTable;
    
    //流量值
    private Integer loadCtrlValue;
    
    //当前服务器Id
    private Integer currentServerID;
    
    public Integer getCurrentTableID()
    {
        return currentTableID;
    }
    
    public void setCurrentTableID(Integer currentTableID)
    {
        this.currentTableID = currentTableID;
    }
    
    public Integer getCurrentRouteTable()
    {
        return currentRouteTable;
    }
    
    public void setCurrentRouteTable(Integer currentRouteTable)
    {
        this.currentRouteTable = currentRouteTable;
    }
    
    public Integer getLoadCtrlValue()
    {
        return loadCtrlValue;
    }
    
    public void setLoadCtrlValue(Integer loadCtrlValue)
    {
        this.loadCtrlValue = loadCtrlValue;
    }
    
    public Integer getCurrentServerID()
    {
        return currentServerID;
    }
    
    public void setCurrentServerID(Integer currentServerID)
    {
        this.currentServerID = currentServerID;
    }
}