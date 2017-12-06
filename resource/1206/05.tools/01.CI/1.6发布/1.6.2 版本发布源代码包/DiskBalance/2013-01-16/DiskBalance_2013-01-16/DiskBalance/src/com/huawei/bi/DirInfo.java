/*
 * 文 件 名:  DiskInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  目录信息实体
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi;

/**
 * 目录信息实体类
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-14]
 */
public class DirInfo
{
    private String dirName;
    
    private long size;
    
    private DiskInfo disk;
    
    private int state;
    
    /**
     * 默认构造函数
     */
    public DirInfo()
    {
        super();
    }
    
    /**
     * 构造函数
     * @param dirName 目录名
     * @param size 目录占用空间大小
     * @param state 状态
     */
    public DirInfo(String dirName, int size, int state)
    {
        this.dirName = dirName;
        this.size = size;
        this.state = state;
    }
    
    public DiskInfo getDisk()
    {
        return disk;
    }
    
    public void setDisk(DiskInfo disk)
    {
        this.disk = disk;
    }
    
    public String getDirName()
    {
        return dirName;
    }
    
    public void setDirName(String dirName)
    {
        this.dirName = dirName;
    }
    
    public long getSize()
    {
        return size;
    }
    
    public void setSize(long size)
    {
        this.size = size;
    }
    
    public int getState()
    {
        return state;
    }
    
    /**
     * 不移动:0
     * 不移动:1
     * 告警:2
     * @param state 状态
     */
    public void setState(int state)
    {
        this.state = state;
    }
}
