/*
 * 文 件 名:  ServiceTGsSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务任务组列表查询
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100,, 2012-2-27]
 */
public class ServiceTGsSearch
{
    private List<String> visibleSTgs = new ArrayList<String>();

    public List<String> getVisibleSTgs()
    {
        return visibleSTgs;
    }

    public void setVisibleSTgs(List<String> visibleSTgs)
    {
        this.visibleSTgs = visibleSTgs;
    }
}
