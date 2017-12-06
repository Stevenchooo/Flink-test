/*
 * 文 件 名:  NodeMappingEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  节点映射实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-17
 */
package com.huawei.platform.tcc.entity;

/**
 * 节点映射实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-17]
 */
public class NodeMappingEntity
{
    /**
     * 目标TCC
     */
    private String dstTccName;
    
    /**
     * 源节点Id
     */
    private int srcNodeId;
    
    /**
     * 目标节点Id
     */
    private String dstNodeId;

    public String getDstTccName()
    {
        return dstTccName;
    }

    public void setDstTccName(String dstTccName)
    {
        this.dstTccName = dstTccName;
    }

    public int getSrcNodeId()
    {
        return srcNodeId;
    }

    public void setSrcNodeId(int srcNodeId)
    {
        this.srcNodeId = srcNodeId;
    }

    public String getDstNodeId()
    {
        return dstNodeId;
    }

    public void setDstNodeId(String dstNodeId)
    {
        this.dstNodeId = dstNodeId;
    }
}
