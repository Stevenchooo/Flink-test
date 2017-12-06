/*
 * 文 件 名:  DirState.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  目录状态
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-10
 */
package com.huawei.bi;

/**
 * 目录状态
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-10]
 */
public class DirState
{
    /**
     * 初始化默认状态
     */
    public static final int INIT = 0;
    
    /**
     * 可以移出，需要接收方
     */
    public static final int WOULD_MOVE_OUT = 1;
    
    /**
     * 不能移出，未找到接收方
     */
    public static final int CANNOT_MOVE_OUT = 2;
    
    /**
     * 确定可以移出
     */
    public static final int FOUND_MOVE_IN_DISK = 3;
}
