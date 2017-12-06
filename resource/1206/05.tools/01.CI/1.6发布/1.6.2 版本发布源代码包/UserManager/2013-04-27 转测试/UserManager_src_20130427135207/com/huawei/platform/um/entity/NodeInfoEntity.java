/* 文 件 名:  NodeInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.um.entity;

/**
 * 节点信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-06-18]
 */
public class NodeInfoEntity
{
    private Integer nodeId;
    
    private String name;
    
    private String host;
    
    private Integer port;
    
    private String rootPwd;
    
    private String desc;
    
    public Integer getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(Integer nodeId)
    {
        this.nodeId = nodeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getRootPwd()
    {
        return rootPwd;
    }

    public void setRootPwd(String rootPwd)
    {
        this.rootPwd = rootPwd;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeInfoEntity [nodeId=");
        builder.append(nodeId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", host=");
        builder.append(host);
        builder.append(", port=");
        builder.append(port);
        builder.append(", rootPwd=");
        builder.append(", desc=");
        builder.append(desc);
        builder.append("]");
        return builder.toString();
    }
}