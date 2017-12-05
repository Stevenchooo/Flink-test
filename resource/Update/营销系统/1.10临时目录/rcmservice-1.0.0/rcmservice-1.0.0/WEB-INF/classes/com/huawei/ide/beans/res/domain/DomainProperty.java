/*
 * 文 件 名:  DomainProperty.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月23日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 领域对象属性
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.domain.DomainProperty")
@Scope(value = "prototype")
public class DomainProperty
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 外键：领域对象id
     */
    private int domainId;
    
    /**
     * 领域对象属性名称
     */
    private String name;
    
    /**
     * 领域对象属性值类型
     */
    private String category;
    
    /**
     * 领域对象属性默认值
     * (INT/DOUBLE/VARCHAR/..)
     */
    private String defaultVal;
    
    /**
     * 获取id
     * @return id  返回 id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * 设置id
     * @param id 对id进行赋值
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * 获取domainId
     * @return domainId 返回 domainId
     */
    public int getDomainId()
    {
        return domainId;
    }
    
    /**
     * 设置domainId
     * @param domainId 对domainId进行赋值
     */
    public void setDomainId(int domainId)
    {
        this.domainId = domainId;
    }
    
    /**
     * 获取name
     * @return name 返回 name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置name
     * @param name 对name进行赋值
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * 获取category
     * @return category 返回 category
     */
    public String getCategory()
    {
        return category;
    }
    
    /**
     * 设置category
     * @param category 对category进行赋值
     */
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    /**
     * 获取defaultVal
     * @return defaultVal 返回 defaultVal
     */
    public String getDefaultVal()
    {
        return defaultVal;
    }
    
    /**
     * 设置defaultVal
     * @param defaultVal 对defaultVal进行赋值
     */
    public void setDefaultVal(String defaultVal)
    {
        this.defaultVal = defaultVal;
    }
    
}
