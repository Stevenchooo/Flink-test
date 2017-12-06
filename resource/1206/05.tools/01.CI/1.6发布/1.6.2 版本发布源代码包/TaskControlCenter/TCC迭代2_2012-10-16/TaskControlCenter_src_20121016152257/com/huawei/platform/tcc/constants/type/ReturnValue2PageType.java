/*
 * 文 件 名:  ReturnValue2PageType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.constants.type;


/**
 * 返回给页面的值类型
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-19]
 */
public class ReturnValue2PageType
{
    /**
     * 正常返回，无值列表
     */
    public static final int NORMAL = 0;
    
    /**
     * 主键冲突，无值列表
     */
    public static final int DUPLICATE_KEY = 1;
    
    /**
     * 权限不足,并返回权限不足列表
     */
    public static final int NO_ENOUGT_PRIVILEGE = 2;
    
    /**
     * 自定义值列表，用‘,’分割多个值
     */
    public static final int VALUE = 3;
    
    /**
     * 不存在
     */
    public static final int NOT_EXIST = 4;
    
    /**
     * 已经启动
     */
    public static final int HAVE_STARTED = 5;
    
    /**
     * 外键约束
     */
    public static final int FOREIGNKEY_CONSTRAINT = 6;

    /**
     * 删除当前用户
     */
    public static final int DELECT_ITSELF = 7;
    
}
