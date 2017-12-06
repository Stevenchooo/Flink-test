/*
 * 文 件 名:  OSUserMappingEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  os用户映射实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-17
 */
package com.huawei.platform.tcc.entity;

/**
 * os用户映射实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-17]
 */
public class OSUserMappingEntity
{
    /**
     * 目标TCC名
     */
    private String dstTccName;
    
    /**
     * 目标OS用户名
     */
    private String dstOsUser;
    
    /**
     * 源OS用户名
     */
    private String srcOsUser;
    
    public String getDstTccName()
    {
        return dstTccName;
    }
    
    public void setDstTccName(String dstTccName)
    {
        this.dstTccName = dstTccName;
    }
    
    public String getDstOsUser()
    {
        return dstOsUser;
    }
    
    public void setDstOsUser(String dstOsUser)
    {
        this.dstOsUser = dstOsUser;
    }
    
    public String getSrcOsUser()
    {
        return srcOsUser;
    }
    
    public void setSrcOsUser(String srcOsUser)
    {
        this.srcOsUser = srcOsUser;
    }
}
