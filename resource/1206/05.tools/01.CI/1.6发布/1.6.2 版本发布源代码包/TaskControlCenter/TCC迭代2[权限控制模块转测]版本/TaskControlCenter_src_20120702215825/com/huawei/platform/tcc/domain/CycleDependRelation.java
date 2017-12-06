/*
 * 文 件 名:  CycleDependRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

/**
 * 周期依赖关系
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class CycleDependRelation
{
    private Long dependTaskId;
    
    private String dependCycleId;
    
    private boolean ignoreError;
    
    /**
     * 构造函数
     * @param dependTaskId 依赖任务ID
     * @param dependCycleId 依赖周期ID
     * @param bIgnoreError 是否忽略错误
     */
    public CycleDependRelation(Long dependTaskId, String dependCycleId, boolean bIgnoreError)
    {
        this.dependTaskId = dependTaskId;
        this.dependCycleId = dependCycleId;
        this.ignoreError = bIgnoreError;
    }
    
    /**
     * 默认构造函数
     */
    public CycleDependRelation()
    {
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
     * 获取依赖周期Id
     * @return 返回 依赖周期Id
     */
    public String getDependCycleId()
    {
        return dependCycleId;
    }
    
    /**
     * 设置依赖周期Id
     * @param dependCycleId 对dependCycleId进行赋值
     */
    public void setDependCycleId(String dependCycleId)
    {
        this.dependCycleId = dependCycleId;
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
    
    /**
     * 获取hashcode
     * @return 获取hashcode
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dependCycleId == null) ? 0 : dependCycleId.hashCode());
        result = prime * result + ((dependTaskId == null) ? 0 : dependTaskId.hashCode());
        result = prime * result + (ignoreError ? 1231 : 1237);
        return result;
    }
    
    /**
     * 对象是否相等
     * @param obj 待比较对象
     * @return 对象是否相等
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        CycleDependRelation other = (CycleDependRelation)obj;
        if (dependCycleId == null)
        {
            if (other.dependCycleId != null)
            {
                return false;
            }
        }
        else if (!dependCycleId.equals(other.dependCycleId))
        {
            return false;
        }
        if (dependTaskId == null)
        {
            if (other.dependTaskId != null)
            {
                return false;
            }
        }
        else if (!dependTaskId.equals(other.dependTaskId))
        {
            return false;
        }
        return true;
    }

    /**
     * 字符串表示
     * @return 字符串表示
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CycleDependRelation [dependTaskId=");
        builder.append(dependTaskId);
        builder.append(", dependCycleId=");
        builder.append(dependCycleId);
        builder.append(", ignoreError=");
        builder.append(ignoreError);
        builder.append("]");
        return builder.toString();
    }
}
