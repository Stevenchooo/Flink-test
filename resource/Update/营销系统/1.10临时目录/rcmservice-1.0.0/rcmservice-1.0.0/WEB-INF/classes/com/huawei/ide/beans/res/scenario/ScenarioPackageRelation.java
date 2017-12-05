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
 * 应用场景package关联对象
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月25日]
 * @see  [相关类/方法]
 */
@Component(value = "com.huawei.ide.beans.res.scenario.ScenarioPackageRelation")
@Scope(value = "prototype")
public class ScenarioPackageRelation
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 外键：领域对象package id
     */
    private int packageId;
    
    /**
     * 外键：领域对象id
     */
    private int scenarioId;
    
    /**
     * 领域对象package名称
     */
    private String packageName;
    
    /**
     * 领域对象名称
     */
    private String scenarioName;
    
    /**
     * 领域对象是否发布
     */
    private String scenarioPublished;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getPackageId()
    {
        return packageId;
    }
    
    public void setPackageId(int packageId)
    {
        this.packageId = packageId;
    }
    
    public int getScenarioId()
    {
        return scenarioId;
    }
    
    public void setScenarioId(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }
    
    public String getPackageName()
    {
        return packageName;
    }
    
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    
    public String getScenarioName()
    {
        return scenarioName;
    }
    
    public void setScenarioName(String scenarioName)
    {
        this.scenarioName = scenarioName;
    }
    
    public String getScenarioPublished()
    {
        return scenarioPublished;
    }
    
    public void setScenarioPublished(String scenarioPublished)
    {
        this.scenarioPublished = scenarioPublished;
    }
    
}