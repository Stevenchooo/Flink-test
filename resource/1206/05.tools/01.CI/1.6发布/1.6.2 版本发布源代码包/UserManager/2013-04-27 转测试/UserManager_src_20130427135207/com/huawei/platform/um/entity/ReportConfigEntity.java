/*
 * 文 件 名:  ReportConfigEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  报表配置实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-4-24
 */
package com.huawei.platform.um.entity;

/**
 * 报表配置实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-4-24]
 */
public class ReportConfigEntity
{
    private Integer nodeId;
    
    private Integer browserPort;
    
    private String cptDir;
    
    private String addressPrefix;

    public Integer getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(Integer nodeId)
    {
        this.nodeId = nodeId;
    }

    public Integer getBrowserPort()
    {
        return browserPort;
    }

    public void setBrowserPort(Integer browserPort)
    {
        this.browserPort = browserPort;
    }

    public String getCptDir()
    {
        return cptDir;
    }

    public void setCptDir(String cptDir)
    {
        this.cptDir = cptDir;
    }

    public String getAddressPrefix()
    {
        return addressPrefix;
    }

    public void setAddressPrefix(String addressPrefix)
    {
        this.addressPrefix = addressPrefix;
    }
}
