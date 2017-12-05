package com.huawei.ide.services.algorithm.entity;

/**
 * 
 * AppHeapEntity
 * <功能详细描述>
 * 
 * @author  z00280396
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
public class AppHeapEntity
{
    
    /*
     * appId
     */
    private String appId;
    
    /*
     * weight
     */
    private double weight;
    
    public String getAppId()
    {
        return appId;
    }
    
    public void setAppId(String appId)
    {
        this.appId = appId;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public void setWeight(double weight)
    {
        this.weight = weight;
    }
}
