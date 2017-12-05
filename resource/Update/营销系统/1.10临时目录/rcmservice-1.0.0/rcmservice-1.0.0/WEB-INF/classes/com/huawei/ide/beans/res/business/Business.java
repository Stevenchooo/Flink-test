/*
 * 文 件 名:  Business.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.business;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 业务规则对象
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.business.Business")
@Scope(value = "prototype")
public class Business
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 业务规则对象名称
     */
    private String name;
    
    /**
     * 是否已经发布
     */
    private String published;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPublished()
    {
        return published;
    }
    
    public void setPublished(String published)
    {
        this.published = published;
    }
    
}
