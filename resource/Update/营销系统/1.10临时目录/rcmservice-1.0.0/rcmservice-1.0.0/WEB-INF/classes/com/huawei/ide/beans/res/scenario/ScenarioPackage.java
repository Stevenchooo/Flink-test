/*
 * 文 件 名:  ScenarioPackage.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cWX306007
 * 修改时间:  2016年3月25日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.scenario;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * 应用场景package对象
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月25日]
 * @see  [相关类/方法]
 */
@Component(value = "com.huawei.ide.beans.res.scenario.ScenarioPackage")
@Scope(value = "prototype")
public class ScenarioPackage
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 应用场景名称
     */
    private String name;
    
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
    
}
