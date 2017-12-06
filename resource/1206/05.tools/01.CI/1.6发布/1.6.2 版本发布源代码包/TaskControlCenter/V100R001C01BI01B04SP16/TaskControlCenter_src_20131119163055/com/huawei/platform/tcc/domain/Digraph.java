/*
 * 文 件 名:  Digraph.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  有向图
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-20
 */
package com.huawei.platform.tcc.domain;

import java.util.List;

/**
 * 有向图
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-20]
 */
public class Digraph
{
    private List<DGLink> links;
    
    private List<DGNode> nodes;
    
    private List<List<DGNode>> hiNodes;
    
    public List<List<DGNode>> getHiNodes()
    {
        return hiNodes;
    }

    public void setHiNodes(List<List<DGNode>> hiNodes)
    {
        this.hiNodes = hiNodes;
    }

    public List<DGNode> getNodes()
    {
        return nodes;
    }
    
    public void setNodes(List<DGNode> nodes)
    {
        this.nodes = nodes;
    }
    
    public List<DGLink> getLinks()
    {
        return links;
    }
    
    public void setLinks(List<DGLink> links)
    {
        this.links = links;
    }
}
