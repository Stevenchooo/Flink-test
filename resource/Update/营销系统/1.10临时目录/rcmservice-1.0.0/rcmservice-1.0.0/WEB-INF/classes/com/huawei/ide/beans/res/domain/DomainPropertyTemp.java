/*
 * 文 件 名:  Domain_Property.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cWX306007
 * 修改时间:  2016年3月31日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.domain;

/**
 * 
 * Domain_Property
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月31日]
 * @see  [相关类/方法]
 */
public class DomainPropertyTemp
{
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
     * 领域对象属性ID
     */
    private int domainPropertyId;
    
    /**
     * 领域对象属性名称
     */
    private String domainPropertyName;
    
    /**
     * 对象属性规则运算符
     * (当前支持的运算符：==、!=、>、>=、<、<=)
     */
    private String domainPropertyOperator;
    
    /**
     * 领域对象规则运算符比较值
     */
    private String domainPropertyOperatorValue;
    
    /**
     * 标识对象规则运算符
     * ||代表规则或运算；&&代表规则与运算
     */
    private String domainPropertyRuleOperator;
    
    /**
     * 获取domainPropertyCategory
     * @return domainPropertyCategory 返回 domainPropertyCategory
     */
    public String getDomainPropertyCategory()
    {
        return domainPropertyCategory;
    }
    
    /**
     * 设置domainPropertyCategory
     * @param domainPropertyCategory 对domainPropertyCategory进行赋值
     */
    public void setDomainPropertyCategory(String domainPropertyCategory)
    {
        this.domainPropertyCategory = domainPropertyCategory;
    }
    
    /**
     * 获取domainPropertyDefaultVal
     * @return domainPropertyDefaultVal 返回 domainPropertyDefaultVal
     */
    public String getDomainPropertyDefaultVal()
    {
        return domainPropertyDefaultVal;
    }
    
    /**
     * 设置domainPropertyDefaultVal
     * @param domainPropertyDefaultVal 对domainPropertyDefaultVal进行赋值
     */
    public void setDomainPropertyDefaultVal(String domainPropertyDefaultVal)
    {
        this.domainPropertyDefaultVal = domainPropertyDefaultVal;
    }
    
    /**
     * 返回 domainPropertyId
     * @return domainPropertyId 返回 domainPropertyId
     */
    public int getDomainPropertyId()
    {
        return domainPropertyId;
    }
    
    /**
     * 对domainPropertyId进行赋值
     * @param domainPropertyId 对domainPropertyId进行赋值
     */
    public void setDomainPropertyId(int domainPropertyId)
    {
        this.domainPropertyId = domainPropertyId;
    }
    
    /**
     * 返回 domainPropertyName
     * @return domainPropertyName 返回 domainPropertyName
     */
    public String getDomainPropertyName()
    {
        return domainPropertyName;
    }
    
    /**
     * 对domainPropertyName进行赋值
     * @param domainPropertyName 对domainPropertyName进行赋值
     */
    public void setDomainPropertyName(String domainPropertyName)
    {
        this.domainPropertyName = domainPropertyName;
    }
    
    /**
     * 返回domainPropertyOperator
     * @return domainPropertyOperator 返回 domainPropertyOperator
     */
    public String getDomainPropertyOperator()
    {
        return domainPropertyOperator;
    }
    
    /**
     * 设置domainPropertyOperator
     * @param domainPropertyOperator 对domainPropertyOperator进行赋值
     */
    public void setDomainPropertyOperator(String domainPropertyOperator)
    {
        this.domainPropertyOperator = domainPropertyOperator;
    }
    
    /**
     * 获取domainPropertyOperatorValue
     * @return domainPropertyOperatorValue 返回 domainPropertyOperatorValue
     */
    public String getDomainPropertyOperatorValue()
    {
        return domainPropertyOperatorValue;
    }
    
    /**
     *设置domainPropertyOperatorValue 
     * @param domainPropertyOperatorValue 对domainPropertyOperatorValue进行赋值
     */
    public void setDomainPropertyOperatorValue(String domainPropertyOperatorValue)
    {
        this.domainPropertyOperatorValue = domainPropertyOperatorValue;
    }
    
    /**
     * 获取domainPropertyRuleOperator
     * @return domainPropertyRuleOperator 返回 domainPropertyRuleOperator
     */
    public String getDomainPropertyRuleOperator()
    {
        return domainPropertyRuleOperator;
    }
    
    /**
     * 设置domainPropertyRuleOperator
     * @param domainPropertyRuleOperator 对domainPropertyRuleOperator进行赋值
     */
    public void setDomainPropertyRuleOperator(String domainPropertyRuleOperator)
    {
        this.domainPropertyRuleOperator = domainPropertyRuleOperator;
    }
    
}
