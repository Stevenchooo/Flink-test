package com.huawei.bi.task.domain;

public class User
{
    private String user;
    
    private String hivedbs;
    
    private String privilege;
    
    private Integer maxRunningSqls;
    
    private Integer connId;
    
    public Integer getConnId()
    {
        return connId;
    }

    public void setConnId(Integer connId)
    {
        this.connId = connId;
    }

    public String getUser()
    {
        return user;
    }
    
    public void setUser(String user)
    {
        this.user = user;
    }
    
    public String getHivedbs()
    {
        return hivedbs;
    }

    public void setHivedbs(String hivedbs)
    {
        this.hivedbs = hivedbs;
    }

    public String getPrivilege()
    {
        return privilege;
    }
    
    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }
    
    public Integer getMaxRunningSqls()
    {
        return maxRunningSqls;
    }
    
    public void setMaxRunningSqls(Integer maxRunningSqls)
    {
        this.maxRunningSqls = maxRunningSqls;
    }
}
