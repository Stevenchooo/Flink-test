/* 文 件 名:  ServiceDeployInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;


/**
 * 业务部署信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class ServiceDeployInfoEntity
{
    private Integer nodeType;
    
    private Integer serviceId;
    
    private String nodeIpAddr;
    
    private String dataDirectory;
    
    public Integer getNodeType()
    {
        return nodeType;
    }
    
    public void setNodeType(Integer nodeType)
    {
        this.nodeType = nodeType;
    }
    
    public Integer getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
    public String getNodeIpAddr()
    {
        return nodeIpAddr;
    }
    
    public void setNodeIpAddr(String nodeIpAddr)
    {
        this.nodeIpAddr = nodeIpAddr == null ? null : nodeIpAddr.trim();
    }
    
    public String getDataDirectory()
    {
        return dataDirectory;
    }
    
    public void setDataDirectory(String dataDirectory)
    {
        this.dataDirectory = dataDirectory == null ? null : dataDirectory.trim();
    }
}