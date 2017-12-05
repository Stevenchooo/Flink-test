/*
 * 文 件 名:  BusinessProperty.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cWX306007
 * 修改时间:  2016年4月1日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.business;

/**
 * 
 * BusinessProperty
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月1日]
 * @see  [相关类/方法]
 */
public class BusinessProperty
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 外键： 业务规则id
     */
    private int businessId;
    
    /**
     * 外键：应用场景id
     */
    private int scenarioId;
    
    /**
     * 标识应用场景运算符
     * (||代表规则或运算；&&代表规则与运算)
     */
    private String operator;
    
    /**
     * 外键：领域对象id
     */
    private int domainId;
    
    /**
     * 标识对象规则运算符
     * (||代表规则或运算；&&代表规则与运算)
     */
    private String scenarioDomainOperator;
    
    /**
     * 自定义规则内容
     * 若非空，则表示自定义规则内容
     * 若为空，则不会使用自定义规则
     */
    private String customRule;
    
    /**
     * 领域对象属性ID
     */
    private int domainPropertyId;
    
    /**
     * 领域对象属性名称
     */
    private String domainPropertyName;
    
    /**
     * 领域对象属性值类型
     * (INT/DOUBLE/VARCHAR)
     */
    private String domainPropertyCategory;
    
    /**
     * 领域对象属性默认值
     */
    private String domainPropertyDefaultVal;
    
    /**
     * 对象属性规则运算符
     * (当前支持的运算符：==、!=、>、>=、<、<=)
     */
    private String domainPropertyOperator;
    
    /**
     * 领域对象规则运算符比较值
     */
    private String domainOperatorValue;
    
    /**
     * 标识对象规则运算符
     * ||代表规则或运算；&&代表规则与运算
     */
    private String ruleOperator;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getBusinessId()
    {
        return businessId;
    }
    
    public void setBusinessId(int businessId)
    {
        this.businessId = businessId;
    }
    
    public int getScenarioId()
    {
        return scenarioId;
    }
    
    public void setScenarioId(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    public int getDomainId()
    {
        return domainId;
    }
    
    public void setDomainId(int domainId)
    {
        this.domainId = domainId;
    }
    
    public String getScenarioDomainOperator()
    {
        return scenarioDomainOperator;
    }
    
    public void setScenarioDomainOperator(String scenarioDomainOperator)
    {
        this.scenarioDomainOperator = scenarioDomainOperator;
    }
    
    public String getCustomRule()
    {
        return customRule;
    }
    
    public void setCustomRule(String customRule)
    {
        this.customRule = customRule;
    }
    
    public int getDomainPropertyId()
    {
        return domainPropertyId;
    }
    
    public void setDomainPropertyId(int domainPropertyId)
    {
        this.domainPropertyId = domainPropertyId;
    }
    
    public String getDomainPropertyName()
    {
        return domainPropertyName;
    }
    
    public void setDomainPropertyName(String domainPropertyName)
    {
        this.domainPropertyName = domainPropertyName;
    }
    
    public String getDomainPropertyCategory()
    {
        return domainPropertyCategory;
    }
    
    public void setDomainPropertyCategory(String domainPropertyCategory)
    {
        this.domainPropertyCategory = domainPropertyCategory;
    }
    
    public String getDomainPropertyDefaultVal()
    {
        return domainPropertyDefaultVal;
    }
    
    public void setDomainPropertyDefaultVal(String domainPropertyDefaultVal)
    {
        this.domainPropertyDefaultVal = domainPropertyDefaultVal;
    }
    
    public String getDomainPropertyOperator()
    {
        return domainPropertyOperator;
    }
    
    public void setDomainPropertyOperator(String domainPropertyOperator)
    {
        this.domainPropertyOperator = domainPropertyOperator;
    }
    
    public String getDomainOperatorValue()
    {
        return domainOperatorValue;
    }
    
    public void setDomainOperatorValue(String domainOperatorValue)
    {
        this.domainOperatorValue = domainOperatorValue;
    }
    
    public String getRuleOperator()
    {
        return ruleOperator;
    }
    
    public void setRuleOperator(String ruleOperator)
    {
        this.ruleOperator = ruleOperator;
    }
    
}
