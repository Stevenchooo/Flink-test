/*
 * 文 件 名:  OsGroupSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  OS组检索
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-25
 */
package com.huawei.platform.um.domain;

/**
 * OS组检索
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-25]
 */
public class OsGroupSearch extends Search
{
    private String userGroup;
    
    private Integer serviceId;

    public String getUserGroup()
    {
        return userGroup;
    }

    public void setUserGroup(String userGroup)
    {
        this.userGroup = userGroup;
    }

    public Integer getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }
    
}
