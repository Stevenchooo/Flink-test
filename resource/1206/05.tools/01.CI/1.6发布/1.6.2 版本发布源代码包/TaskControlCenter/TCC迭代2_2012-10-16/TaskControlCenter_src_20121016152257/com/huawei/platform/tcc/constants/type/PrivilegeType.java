/*
 * 文 件 名:  PrivilegeType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.constants.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限类型
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-19]
 */
public class PrivilegeType
{
    /**
     * 查询权限
     */
    public static final int QUERY = 2;
    
    /**
     * 执行权限
     */
    public static final int EXECUTE = 1;
    
    /**
     * 完全权限
     */
    public static final int ALL = 0;
    
    /**
     * 无权限
     */
    public static final int NO = -1;
    
    private static Map<Integer, String> desc = new HashMap<Integer, String>();
    
    static
    {
        desc.put(NO, "无权限");
        desc.put(QUERY, "查询");
        desc.put(EXECUTE, "执行");
        desc.put(ALL, "完全");
    }
    
    /**
     * 字符串描述
     * @param privilegeType 权限类型
     * @return 字符串描述
     */
    public static String toString(Integer privilegeType)
    {
        return desc.get(privilegeType);
    }
    
    /**
     * 字符串描述
     * @param subPrivilegeType 子权限类型
     * @return 字符串描述
     */
    public static String toSubPriString(int subPrivilegeType)
    {
        if (subPrivilegeType == SubPrivilegeType.QUERY)
        {
            return desc.get(PrivilegeType.QUERY);
        }
        else if (subPrivilegeType == SubPrivilegeType.DELETE || subPrivilegeType == SubPrivilegeType.ADD)
        {
            return desc.get(PrivilegeType.ALL);
        }
        else
        {
            return desc.get(PrivilegeType.EXECUTE);
        }
    }
}
