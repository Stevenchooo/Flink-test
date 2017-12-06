/*
 * 文 件 名:  MovePolicy.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  移动策略
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi;

/**
 * 移动策略
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-14]
 */
public class MovePolicy implements Comparable<MovePolicy>
{
    private String srcDir;
    
    private String destDir;
    
    /**
     * 默认构造函数
     */
    public MovePolicy()
    {
        
    }
    
    /**
     * 构造函数
     * @param srcDir 源目录
     * @param destDir 目标目录
     */
    public MovePolicy(String srcDir, String destDir)
    {
        this.srcDir = srcDir;
        this.destDir = destDir;
    }
    
    public String getDestDir()
    {
        return destDir;
    }
    
    public void setDestDir(String destDir)
    {
        this.destDir = destDir;
    }
    
    public String getSrcDir()
    {
        return srcDir;
    }
    
    public void setSrcDir(String srcDir)
    {
        this.srcDir = srcDir;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((destDir == null) ? 0 : destDir.hashCode());
        result = prime * result + ((srcDir == null) ? 0 : srcDir.hashCode());
        return result;
    }
    
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
        
        MovePolicy other = (MovePolicy) obj;
        if (destDir == null)
        {
            if (other.destDir != null)
            {
                return false;
            }
        }
        else if (!destDir.equals(other.destDir))
        {
            return false;
        }
        
        if (srcDir == null)
        {
            if (other.srcDir != null)
            {
                return false;
            }
        }
        else if (!srcDir.equals(other.srcDir))
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MovePolicy [");
        builder.append(srcDir);
        builder.append(" --> ");
        builder.append(destDir);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public int compareTo(MovePolicy other)
    {
        //order by srcDir,destDir
        if (this.srcDir.equals(other.srcDir))
        {
            return this.destDir.compareTo(other.destDir);
        }
        
        return this.srcDir.compareTo(other.srcDir);
    }
}
