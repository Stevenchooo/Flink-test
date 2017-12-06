/*
 * 文 件 名:  DependTaskNode.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-3-6
 */
package com.huawei.platform.tcc.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 依赖树节点复合体
 * 
 * @author  w00190929
 * @version [华为终端云统一账号模块, 2012-3-6]
 * @see  [相关类/方法]
 */
public class DependTaskNode implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2155191828754715142L;
    
    private Long id;
    
    private String text;
    
    private String iconCls;
    
    private String cycleId;
        
    private List<DependTaskNode> children;
    
    private transient DependTaskNode parent;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<DependTaskNode> getChildren()
    {
        return children;
    }

    public void setChildren(List<DependTaskNode> children)
    {
        this.children = children;
    }

    public String getIconCls()
    {
        return iconCls;
    }

    public void setIconCls(String iconCls)
    {
        this.iconCls = iconCls;
    }

    public String getCycleId()
    {
        return cycleId;
    }

    public void setCycleId(String cycleId)
    {
        this.cycleId = cycleId;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("DependTaskNode [id=");
        builder.append(id);
        builder.append(", text=");
        builder.append(text);
        builder.append(", iconCls=");
        builder.append(iconCls);
        builder.append(", children=");
        builder.append(children);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cycleId == null) ? 0 : cycleId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        DependTaskNode other = (DependTaskNode)obj;
        if (cycleId == null)
        {
            if (other.cycleId != null)
            {
                return false;
            }
        }
        else if (!cycleId.equals(other.cycleId))
        {
            return false;
        }
        if (id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else if (!id.equals(other.id))
        {
            return false;
        }
        return true;
    }

    public DependTaskNode getParent()
    {
        return parent;
    }

    public void setParent(DependTaskNode parent)
    {
        this.parent = parent;
    }
   
}
