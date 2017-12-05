/*
 * 文 件 名:  BusinessPackageRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月17日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.business;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 业务规则对象package关联关系
 * @author  z00219375
 * @version  [版本号, 2016年2月17日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.business.BusinessPackageRelation")
@Scope(value = "prototype")
public class BusinessPackageRelation
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 外键:业务规则所属的package_id
     */
    private int packageId;
    
    /**
     * 外键：业务规则business_id
     */
    private int businessId;
    
    /**
     * 业务规则Package名称
     */
    private String packageName;
    
    /**
     * 业务规则名称
     */
    private String businessName;
    
    /**
     * 业务规则是否已发布
     */
    private String businessPublished;
    
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
    
    public int getBusinessId()
    {
        return businessId;
    }
    
    public void setBusinessId(int businessId)
    {
        this.businessId = businessId;
    }
    
    public String getPackageName()
    {
        return packageName;
    }
    
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    
    public String getBusinessName()
    {
        return businessName;
    }
    
    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }
    
    public String getBusinessPublished()
    {
        return businessPublished;
    }
    
    public void setBusinessPublished(String businessPublished)
    {
        this.businessPublished = businessPublished;
    }
    
}
