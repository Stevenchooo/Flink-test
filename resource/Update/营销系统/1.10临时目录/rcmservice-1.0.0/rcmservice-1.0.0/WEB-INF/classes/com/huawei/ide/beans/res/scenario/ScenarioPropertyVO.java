/*
 * 文 件 名:  ScenarioPropertyVO.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.scenario;

import java.util.List;

import com.huawei.ide.beans.res.domain.CustomRule;
import com.huawei.ide.beans.res.domain.DomainPropertyTemp;

/**
 * ScenarioPropertyVO
 * @author  z00219375
 * @version  [版本号, 2016年2月14日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public class ScenarioPropertyVO
{
    /**
     * 外键：应用场景id
     */
    private int scenarioId;
    
    /**
     * 外键：领域对象id
     */
    private int domainId;
    
    /**
     * 标识对象规则运算符
     * (||代表规则或运算；&&代表规则与运算)
     */
    private String domainOperator;
    
    /**
     * 自定义规则内容
     */
    private List<CustomRule> customRules;
    
    /**
     * 应用场景对应的领域对象属性
     */
    private List<DomainPropertyTemp> domainProperties;
    
    public int getScenarioId()
    {
        return scenarioId;
    }
    
    public void setScenarioId(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }
    
    public int getDomainId()
    {
        return domainId;
    }
    
    public void setDomainId(int domainId)
    {
        this.domainId = domainId;
    }
    
    public String getDomainOperator()
    {
        return domainOperator;
    }
    
    public void setDomainOperator(String domainOperator)
    {
        this.domainOperator = domainOperator;
    }
    
    public List<CustomRule> getCustomRules()
    {
        return customRules;
    }
    
    public void setCustomRules(List<CustomRule> customRules)
    {
        this.customRules = customRules;
    }
    
    public List<DomainPropertyTemp> getDomainProperties()
    {
        return domainProperties;
    }
    
    public void setDomainProperties(List<DomainPropertyTemp> domainProperties)
    {
        this.domainProperties = domainProperties;
    }
    
}
