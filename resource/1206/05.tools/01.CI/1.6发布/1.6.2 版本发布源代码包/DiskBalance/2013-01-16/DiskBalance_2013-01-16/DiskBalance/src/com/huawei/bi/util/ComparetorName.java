/*
 * 文 件 名:  ComparetorName.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  目录排序[根据名字降序]
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi.util;

import java.io.Serializable;
import java.util.Comparator;

import com.huawei.bi.DirInfo;

/**
 * 目录排序[根据名字降序]
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-15]
 */
public class ComparetorName implements Comparator<DirInfo>,Serializable
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = -327945937877879817L;

    @Override
    public int compare(DirInfo o1, DirInfo o2)
    {
        DirInfo dirInfo1 = (DirInfo) o1;
        DirInfo dirInfo2 = (DirInfo) o2;
        //降序
        return -(dirInfo1.getDirName().compareTo(dirInfo2.getDirName()));
    }
    
}
