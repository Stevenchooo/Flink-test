/*
 * 文 件 名:  DomainPackageRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年1月30日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 领域对象package关联表
 * @author  z00219375
 * @version  [版本号, 2016年1月30日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.domain.DomainPackageRelation")
@Scope(value = "prototype")
public class DomainPackageRelation
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
    private int domainId;
    
    /**
     * 领域对象package名称
     */
    private String packageName;
    
    /**
     * 领域对象名称
     */
    private String domainName;
    
    /**
     * 领域对象是否发布
     */
    private String domainPublished;
    
    /**
     * 获取id
     * @return 返回 id
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
     * 获取packageId
     * @return int 返回 packageId
     */
    public int getPackageId()
    {
        return packageId;
    }
    
    /**
     * 设置packageId
     * @param packageId 对packageId进行赋值
     */
    public void setPackageId(int packageId)
    {
        this.packageId = packageId;
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
     * 获取packageName
     * @return packageName 返回 packageName
     */
    public String getPackageName()
    {
        return packageName;
    }
    
    /**
     * 设置packageName
     * @param packageName 对packageName进行赋值
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    
    /**
     * 获取domainName
     * @return domainName 返回 domainName
     */
    public String getDomainName()
    {
        return domainName;
    }
    
    /**设置domainName
     * @param domainName 对domainName进行赋值
     */
    public void setDomainName(String domainName)
    {
        this.domainName = domainName;
    }
    
    /**
     * 获取domainPublished
     * @return domainPublished 返回 domainPublished
     */
    public String getDomainPublished()
    {
        return domainPublished;
    }
    
    /**
     * 设置domainPublished
     * @param domainPublished 对domainPublished进行赋值
     */
    public void setDomainPublished(String domainPublished)
    {
        this.domainPublished = domainPublished;
    }
    
}
