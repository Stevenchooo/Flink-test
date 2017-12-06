/*
 * 文 件 名:  ComparetorSize.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  目录排序
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi.util;

import java.io.Serializable;
import java.util.Comparator;

import com.huawei.bi.DirInfo;

/**
 * 目录排序
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-15]
 */
public class ComparetorSize implements Comparator<DirInfo>,Serializable
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = -244242764707139592L;

    @Override
    public int compare(DirInfo o1, DirInfo o2)
    {
        DirInfo dirInfo1 = (DirInfo) o1;
        DirInfo dirInfo2 = (DirInfo) o2;
        
        if (dirInfo1.getSize() == dirInfo2.getSize())
        {
            return dirInfo2.getDirName().compareTo(dirInfo1.getDirName());
        }
        else
        {
            return (int) (dirInfo2.getSize() - dirInfo1.getSize());
        }
    }
}
