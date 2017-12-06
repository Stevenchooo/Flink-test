/*
 * 文 件 名:  UserGroupMappingEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  用户组映射实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-17
 */
package com.huawei.platform.tcc.entity;

/**
 * 用户组映射实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-17]
 */
public class UserGroupMappingEntity
{
    /**
     * 目标TCC名字
     */
    private String dstTccName;
    
    /**
     * 源用户组
     */
    private String srcUserGroup;
    
    /**
     * 目标用户组
     */
    private String dstUserGroup;

    public String getDstTccName()
    {
        return dstTccName;
    }

    public void setDstTccName(String dstTccName)
    {
        this.dstTccName = dstTccName;
    }

    public String getSrcUserGroup()
    {
        return srcUserGroup;
    }

    public void setSrcUserGroup(String srcUserGroup)
    {
        this.srcUserGroup = srcUserGroup;
    }

    public String getDstUserGroup()
    {
        return dstUserGroup;
    }

    public void setDstUserGroup(String dstUserGroup)
    {
        this.dstUserGroup = dstUserGroup;
    }
}
