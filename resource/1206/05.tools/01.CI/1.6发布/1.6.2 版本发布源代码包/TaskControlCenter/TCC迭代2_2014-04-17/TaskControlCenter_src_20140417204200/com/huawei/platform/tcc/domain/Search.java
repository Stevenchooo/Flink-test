/*
 * 文 件 名:  Search.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;


/**
 * 业务查询
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100,, 2012-06-19]
 */
public class Search
{
    /**
     * 查询起始页
     */
    private Integer pageIndex;
    
    /**
     * 查询每页的大小
     */
    private Integer pageSize;
    
    /**
     * 业务名
     */
    private Object names;
    
    public Integer getPageIndex()
    {
        return pageIndex;
    }
    
    public void setPageIndex(Integer pageIndex)
    {
        this.pageIndex = pageIndex;
    }
    
    public Integer getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }
    
    public Object getNames()
    {
        return names;
    }
    
    public void setNames(Object names)
    {
        this.names = names;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Search [pageIndex=");
        builder.append(pageIndex);
        builder.append(", pageSize=");
        builder.append(pageSize);
        builder.append(", names=");
        builder.append(names);
        builder.append("]");
        return builder.toString();
    }
}
