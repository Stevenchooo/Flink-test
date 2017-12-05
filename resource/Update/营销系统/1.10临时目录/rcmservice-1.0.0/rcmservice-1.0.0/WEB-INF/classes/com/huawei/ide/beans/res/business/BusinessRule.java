/*
 * 文 件 名:  BusinessRule.java
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
 * 业务规则
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.business.BusinessRule")
@Scope(value = "prototype")
public class BusinessRule
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 外键：业务规则对象id
     */
    private int businessId;
    
    /**
     * BPM Model ID
     * 对应Activiti Model的ID
     */
    private String bpmModelId;
    
    /**
     * BPM存储对象
     */
    private byte[] bpmContent;
    
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
    
    public String getBpmModelId()
    {
        return bpmModelId;
    }
    
    public void setBpmModelId(String bpmModelId)
    {
        this.bpmModelId = bpmModelId;
    }
    
    public byte[] getBpmContent()
    {
        return bpmContent;
    }
    
    public void setBpmContent(byte[] bpmContent)
    {
        this.bpmContent = bpmContent;
    }
    
}
