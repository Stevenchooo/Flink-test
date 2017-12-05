/*
 * 文 件 名:  Scenario.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.scenario;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 应用场景对象
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.scenario.Scenario")
@Scope(value = "prototype")
public class Scenario
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 应用场景对象名称
     */
    private String name;
    
    /**
     * 是否已发布
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
