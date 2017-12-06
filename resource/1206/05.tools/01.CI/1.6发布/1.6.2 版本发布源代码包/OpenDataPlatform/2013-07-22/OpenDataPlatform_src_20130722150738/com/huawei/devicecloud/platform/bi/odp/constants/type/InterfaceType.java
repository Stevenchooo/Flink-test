/*
 * 文 件 名:  InterfaceType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <接口类型
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-8
 */
package com.huawei.devicecloud.platform.bi.odp.constants.type;

/**
 * 接口类型
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-8]
 */
public class InterfaceType
{
    /**
     *  查询数据总数接口
     */
    public static final int QUERY_DATA_COUNT = 1;
    
    /**
     * 分批预留数据接口
     */
    public static final int RESERVE_BATCH_DATA = 2;
    
    /**
     * 取消预留数据接口
     */
    public static final int REVOKE_RESERVE = 3;
    
    /**
     * 获取文件流接口
     */
    public static final int WGET_FILE = 4;
    
    /**
     * 文件上传接口
     */
    public static final int FILE_UPLOAD = 2;
}
