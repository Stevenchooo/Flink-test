/*
 * 文 件 名:  DGNode.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  有向图节点
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-20
 */
package com.huawei.platform.tcc.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 有向图节点
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-20]
 */
public class DGNode
{
    /**
     * 未访问过
     */
    public static final int BLANK = 0;
    
    /**
     * 正访问中
     */
    public static final int GRAY = 1;
    
    /**
     * 访问结束
     */
    public static final int BLACK = 2;
    
    private String name;
    
    private String type;
    
    private int state;
    
    private float ratio;
    
    private float weight;
    
    private boolean moreFLs;
    
    private boolean moreTLs;
    
    private transient Long taskId;
    
    private transient String cycleId;
    
    private transient int depth;
    
    private transient List<DGLink> fromLinks;
    
    private transient List<DGLink> toLinks;
    
    private transient int color = BLANK;
    
    /**
     * 构造函数
     * @param name 节点名
     */
    public DGNode(String name)
    {
        this.name = name;
        this.fromLinks = new ArrayList<DGLink>();
        this.toLinks = new ArrayList<DGLink>();
        //this.ratio = new Random().nextFloat();
        //int ra = new Random().nextInt(100);
        //if (ra > 95)
        //{
        //    this.ratio = 1;
        //}
        //else if (ra < 5)
        //{
        //   this.ratio = 0;
        //}
        //this.weight = new Random().nextFloat();
        //this.state = new Random().nextInt(5);
        this.ratio = 0;
        this.weight = 0;
        this.state = 0;
        
        this.type = "normal";
    }
    
    public boolean isMoreFLs()
    {
        return moreFLs;
    }
    
    public void setMoreFLs(boolean moreFLs)
    {
        this.moreFLs = moreFLs;
    }
    
    public boolean isMoreTLs()
    {
        return moreTLs;
    }
    
    public void setMoreTLs(boolean moreTLs)
    {
        this.moreTLs = moreTLs;
    }
    
    public Long getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }
    
    public String getCycleId()
    {
        return cycleId;
    }
    
    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public int getState()
    {
        return state;
    }
    
    public void setState(int state)
    {
        this.state = state;
    }
    
    public float getRatio()
    {
        return ratio;
    }
    
    public void setRatio(float ratio)
    {
        this.ratio = ratio;
    }
    
    public float getWeight()
    {
        return weight;
    }
    
    public void setWeight(float weight)
    {
        this.weight = weight;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public void setColor(int color)
    {
        this.color = color;
    }
    
    public List<DGLink> getFromLinks()
    {
        return fromLinks;
    }
    
    public void setFromLinks(List<DGLink> fromLinks)
    {
        this.fromLinks = fromLinks;
    }
    
    public List<DGLink> getToLinks()
    {
        return toLinks;
    }
    
    public void setToLinks(List<DGLink> toLinks)
    {
        this.toLinks = toLinks;
    }
    
    public int getDepth()
    {
        return depth;
    }
    
    public void setDepth(int depth)
    {
        this.depth = depth;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
}
