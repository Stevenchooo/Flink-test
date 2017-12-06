/*
 * 文 件 名:  ComparetorName.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  磁盘排序
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi.util;

import java.io.Serializable;
import java.util.Comparator;

import com.huawei.bi.DiskInfo;

/**
 * 磁盘排序
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-15]
 */
public class ComparetorAdjust implements Comparator<DiskInfo>,Serializable
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1932537755485896186L;

    @Override
    public int compare(DiskInfo o1, DiskInfo o2)
    {
        DiskInfo diskInfo1 = (DiskInfo) o1;
        DiskInfo diskInfo2 = (DiskInfo) o2;
        //对每个磁盘的离差降序排序
        if (diskInfo1.getAdjustSize() == diskInfo2.getAdjustSize())
        {
            return diskInfo2.getRootDir().compareTo(diskInfo1.getRootDir());
        }
        else
        {
            //降序排列
            return (int)(diskInfo2.getAdjustSize() - diskInfo1.getAdjustSize());
        }
    }
}
