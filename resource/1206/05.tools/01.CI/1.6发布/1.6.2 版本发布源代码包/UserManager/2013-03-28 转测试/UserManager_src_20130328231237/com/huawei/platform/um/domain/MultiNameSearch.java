/*
 * 文 件 名:  MultiNameSearch.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  多名字检索
 * 创 建 人:  l00194471    
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.um.domain;

import java.util.List;


/**
 * 多名字检索
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-22]
 */
public class MultiNameSearch
{
    /**
     * 名字集合
     */
    private List<String> names;

    public List<String> getNames()
    {
        return names;
    }

    public void setNames(List<String> names)
    {
        this.names = names;
    }
}
