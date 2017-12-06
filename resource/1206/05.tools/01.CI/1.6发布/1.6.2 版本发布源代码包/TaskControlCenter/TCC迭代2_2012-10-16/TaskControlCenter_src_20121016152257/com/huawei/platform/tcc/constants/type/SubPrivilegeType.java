/*
 * 文 件 名:  SubPrivilegeType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.constants.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 子权限类型
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-19]
 */
public class SubPrivilegeType
{
    /**
     * 查询权限
     */
    public static final int QUERY = 1;
    
    /**
     * 修改
     */
    public static final int MODIFY = 2;
    
    /**
     * 修改
     */
    public static final int START = 3;
    
    /**
     * 修改
     */
    public static final int STOP = 4;
    
    /**
     * 删除
     */
    public static final int DELETE = 5;
    
    /**
     * 新增
     */
    public static final int ADD = 6;
    
    /**
     * 重做
     */
    public static final int REDO = 7;
    
    /**
     * 批量重做
     */
    public static final int BATCH_REDO = 8;
    
    /**
     * 集成重做
     */
    public static final int INTEGRATED_REDO = 9;
    
    private static Map<Integer, String> desc = new HashMap<Integer, String>();
    
    static
    {
        desc.put(QUERY, "查询");
        desc.put(MODIFY, "修改");
        desc.put(START, "启动");
        desc.put(STOP, "停止");
        desc.put(DELETE, "删除");
        desc.put(ADD, "新增");
        desc.put(REDO, "重做");
        desc.put(BATCH_REDO, "批量重做");
        desc.put(INTEGRATED_REDO, "集成重做");
    }
    
    /**
     * 字符串描述
     * @param subPrivilegeType 子权限类型
     * @return 字符串描述
     */
    public static String toString(Integer subPrivilegeType)
    {
        return desc.get(subPrivilegeType);
    }
}
