/*
 * 文 件 名:  DependRealtion.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

/**
 * 依赖关系
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class DependRelation
{
    /**
     * taskId的索引
     */
    public static final int ID_INDEX = 0;
    
    /**
     * 是否全周期依赖的索引
     */
    public static final int FULLDEPEND_INDEX = 1;
    
    /**
     * 是否忽略错误的索引
     */
    public static final int IGNORE_INDEX = 2;
    
    private Long dependTaskId;
    
    private boolean fullCycleDepend;
    
    private boolean ignoreError;
    
    /**
     * 构造函数
     * @param dependTaskId 依赖任务ID
     * @param bFullCycleDepend 是否全周期依赖
     * @param bIgnoreError 是否忽略错误
     */
    public DependRelation(Long dependTaskId, boolean bFullCycleDepend, boolean bIgnoreError)
    {
        this.dependTaskId = dependTaskId;
        this.fullCycleDepend = bFullCycleDepend;
        this.ignoreError = bIgnoreError;
    }
    
    /**
     * 获取依赖任务ID
     * @return 返回 dependTaskId
     */
    public Long getDependTaskId()
    {
        return dependTaskId;
    }
    
    /**
     * 设置依赖任务ID
     * @param dependTaskId 对dependTaskId进行赋值
     */
    public void setDependTaskId(Long dependTaskId)
    {
        this.dependTaskId = dependTaskId;
    }
    
    /**
     * 是否全周期依赖
     * @return 返回 bFullCycleDepend
     */
    public boolean isFullCycleDepend()
    {
        return fullCycleDepend;
    }
    
    /**
     * 设置全周期依赖
     * @param bFullCycleDepend 对bFullCycleDepend进行赋值
     */
    public void setFullCycleDepend(boolean bFullCycleDepend)
    {
        this.fullCycleDepend = bFullCycleDepend;
    }
    
    /**
     * 是否忽略错误
     * @return 返回 bIgnoreError
     */
    public boolean isIgnoreError()
    {
        return ignoreError;
    }
    
    /**
     * 设置忽略错误
     * @param bIgnoreError 对bIgnoreError进行赋值
     */
    public void setIgnoreError(boolean bIgnoreError)
    {
        this.ignoreError = bIgnoreError;
    }
}
