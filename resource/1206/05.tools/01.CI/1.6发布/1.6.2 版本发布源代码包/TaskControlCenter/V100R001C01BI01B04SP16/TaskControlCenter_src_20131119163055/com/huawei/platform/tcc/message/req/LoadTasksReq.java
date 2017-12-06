/*
 * 文 件 名:  LoadTasksReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  加载任务请求
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-18
 */
package com.huawei.platform.tcc.message.req;

import java.util.List;

/**
 * 加载任务请求
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-18]
 */
public class LoadTasksReq
{
    /**
     * 是否提前准备
     */
    private boolean prepare;
    
    /**
     * 准备时返回的标识
     */
    private Long id;
    
    /**
     * 是否全部加载
     */
    private boolean loadAll;
    
    /**
     * 加载的任务Id集合，loadAll为false有效
     */
    private List<Long> taskIds;
    
    /**
     * 目标TCC用户名
     */
    private String userName;
    
    /**
     * 目标TCC密码
     */
    private String password;
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public boolean isPrepare()
    {
        return prepare;
    }

    public void setPrepare(boolean prepare)
    {
        this.prepare = prepare;
    }

    public boolean isLoadAll()
    {
        return loadAll;
    }

    public void setLoadAll(boolean loadAll)
    {
        this.loadAll = loadAll;
    }

    public List<Long> getTaskIds()
    {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds)
    {
        this.taskIds = taskIds;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("LoadTasksReq [prepare=");
        builder.append(prepare);
        builder.append(", id=");
        builder.append(id);
        builder.append(", loadAll=");
        builder.append(loadAll);
        builder.append(", taskIds=");
        builder.append(taskIds);
        builder.append(", userName=");
        builder.append(userName);
        builder.append("]");
        return builder.toString();
    }
}
