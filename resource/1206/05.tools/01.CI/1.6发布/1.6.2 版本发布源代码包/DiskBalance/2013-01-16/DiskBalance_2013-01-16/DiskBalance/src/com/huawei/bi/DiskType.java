/*
 * 文 件 名:  DiskType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  磁盘类型
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-10
 */
package com.huawei.bi;

/**
 * 磁盘类型
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-10]
 */
public class DiskType
{
    /**
     * 移除目录磁盘类型
     */
    public static final int MOVE_IN = 0;
    
    /**
     * 移出目录磁盘类型
     */
    public static final int MOVE_OUT = 1;
    
    /**
     * 磁盘类型的字符串表示
     * @param diskType 磁盘类型
     * @return 磁盘类型的字符串表示
     */
    public static String toString(int diskType)
    {
        if (diskType == MOVE_IN)
        {
            return "MOVE_IN";
        }
        else if (diskType == MOVE_OUT)
        {
            return "MOVE_OUT";
        }
        else
        {
            return "";
        }
    }
}
