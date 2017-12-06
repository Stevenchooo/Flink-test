/* 文 件 名:  NodeInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;


/**
 * 节点信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class NodeInfoEntity
{
    private String nodeIpAddr;
    
    private Integer nodeType;
    
    private String cpu;
    
    private Long memory;
    
    private Long storage;
    
    private String desc;
    
    public String getNodeIpAddr()
    {
        return nodeIpAddr;
    }
    
    public void setNodeIpAddr(String nodeIpAddr)
    {
        this.nodeIpAddr = nodeIpAddr == null ? null : nodeIpAddr.trim();
    }
    
    public Integer getNodeType()
    {
        return nodeType;
    }
    
    public void setNodeType(Integer nodeType)
    {
        this.nodeType = nodeType;
    }
    
    public String getCpu()
    {
        return cpu;
    }
    
    public void setCpu(String cpu)
    {
        this.cpu = cpu == null ? null : cpu.trim();
    }
    
    public Long getMemory()
    {
        return memory;
    }
    
    public void setMemory(Long memory)
    {
        this.memory = memory;
    }
    
    public Long getStorage()
    {
        return storage;
    }
    
    public void setStorage(Long storage)
    {
        this.storage = storage;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc == null ? null : desc.trim();
    }
}