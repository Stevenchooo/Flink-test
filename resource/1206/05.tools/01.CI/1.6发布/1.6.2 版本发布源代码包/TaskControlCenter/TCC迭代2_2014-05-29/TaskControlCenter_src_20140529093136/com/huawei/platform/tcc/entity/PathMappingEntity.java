/*
 * 文 件 名:  PathMappingEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  路径映射实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-17
 */
package com.huawei.platform.tcc.entity;

/**
 * 路径映射实体
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-17]
 */
public class PathMappingEntity
{
    /**
     * 目标TCC名
     */
    private String dstTccName;
    
    /**
     * 源路径
     */
    private String srcPath;
    
    /**
     * 目标路径
     */
    private String dstPath;
    
    public String getDstTccName()
    {
        return dstTccName;
    }
    
    public void setDstTccName(String dstTccName)
    {
        this.dstTccName = dstTccName;
    }
    
    public String getSrcPath()
    {
        return srcPath;
    }
    
    public void setSrcPath(String srcPath)
    {
        this.srcPath = srcPath;
    }
    
    public String getDstPath()
    {
        return dstPath;
    }
    
    public void setDstPath(String dstPath)
    {
        this.dstPath = dstPath;
    }
}
