/*
 * 文 件 名:  ExportState.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  导出状态
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-7
 */
package com.huawei.bi.task.constant;

/**
 * 导出状态
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-11-7]
 */
public class ExportState
{
    /**
     * 自由导出
     */
    public static final int UNLIMIT_EXPORT = 1;
    
    /**
     * 申请导出
     */
    public static final int APPLY_EXPORT = 2;
    
    /**
     * 禁止导出
     */
    public static final int FORBID_EXPORT = 3;
    
    /**
     * 审核通过
     */
    public static final int AUDIT_AGREE = 4;
    
}
