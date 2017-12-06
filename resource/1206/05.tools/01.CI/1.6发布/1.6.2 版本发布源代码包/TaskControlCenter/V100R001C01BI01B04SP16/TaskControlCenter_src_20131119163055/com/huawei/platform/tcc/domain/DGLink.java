/*
 * 文 件 名:  DGLink.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  有向线段
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-20
 */
package com.huawei.platform.tcc.domain;


/**
 * 有向图线段
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-20]
 */
public class DGLink
{
    private String source;
    
    private String target;
    
    private String type;
    
    private transient DGNode sourceNode;
    
    private transient DGNode targetNode;
    
    public DGNode getSourceNode()
    {
        return sourceNode;
    }
    
    public void setSourceNode(DGNode sourceNode)
    {
        this.sourceNode = sourceNode;
    }
    
    public DGNode getTargetNode()
    {
        return targetNode;
    }
    
    public void setTargetNode(DGNode targetNode)
    {
        this.targetNode = targetNode;
    }
    
    public String getSource()
    {
        return source;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    public String getTarget()
    {
        return target;
    }
    
    public void setTarget(String target)
    {
        this.target = target;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(boolean weak)
    {
        this.type = weak ? "weak" : "normal";
    }
}
