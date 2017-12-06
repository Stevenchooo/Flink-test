package com.huawei.bi.common.domain;

import java.util.ArrayList;
import java.util.List;

public class TreeNode
{
    
    // TODO more specific
    public static final String TYPE_TABLE = "table";
    
    public static final String TYPE_DB = "db";
    
    public static final String TYPE_SERVER = "server";
    
    // tree node type for resource
    public static final String TYPE_RSC_FOLDER = "folder";
    
    public static final String TYPE_RSC_SCRIPT = "script";
    
    public static final String TYPE_RSC_DMJob = "DMJob";
    
    private String id;
    
    private String pid;
    
    private String name;
    
    private String pname;
    
    private boolean isParent;
    
    private boolean open = false;
    
    private String type;
    
    private List<TreeNode> children = new ArrayList<TreeNode>();
    
    public boolean getIsParent()
    {
        return isParent;
    }
    
    public void setIsParent(boolean isParent)
    {
        this.isParent = isParent;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public boolean isOpen()
    {
        return open;
    }
    
    public void setOpen(boolean open)
    {
        this.open = open;
    }
    
    public List<TreeNode> getChildren()
    {
        return children;
    }
    
    public void setChildren(List<TreeNode> children)
    {
        this.children = children;
    }
    
    public String getPname()
    {
        return pname;
    }
    
    public void setPname(String pname)
    {
        this.pname = pname;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getPid()
    {
        return pid;
    }
    
    public void setPid(String pid)
    {
        this.pid = pid;
    }
    
}
