/*
 * 文 件 名:  OperatorType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  操作类型
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-14
 */
package com.huawei.devicecloud.platform.bi.odp.constants.type;

/**
 * 操作类型
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-14]
 */
public class OperatorType
{
    /**
     * 预留数据操作
     */
    public static final int RESERVE_DATA = 1;
    
    /**
     * 取消预留数据
     */
    public static final int REVOKE_RESERVE = 2;
    
    /**
     * 获取文件操作
     */
    public static final int GET_FILE = 3;
}
