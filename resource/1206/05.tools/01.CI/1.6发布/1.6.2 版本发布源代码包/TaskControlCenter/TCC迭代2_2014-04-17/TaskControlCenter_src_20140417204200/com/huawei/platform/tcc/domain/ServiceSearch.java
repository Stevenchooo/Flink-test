/*
 * 文 件 名:  ServiceSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务查询
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100,, 2012-2-27]
 */
public class ServiceSearch
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
    private List<String> serviceNames = new ArrayList<String>();
    
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
    
    public List<String> getServiceNames()
    {
        return serviceNames;
    }
    
    public void setTaskNames(List<String> serviceName)
    {
        this.serviceNames = serviceName;
    }
}
