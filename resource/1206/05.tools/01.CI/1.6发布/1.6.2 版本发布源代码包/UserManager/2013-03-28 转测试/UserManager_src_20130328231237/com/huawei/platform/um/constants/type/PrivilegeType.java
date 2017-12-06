/*
 * 文 件 名:  PrivilegeType.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.um.constants.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限类型
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-06-19]
 */
public class PrivilegeType
{
    /**
     * 查询权限
     */
    public static final int QUERY = 2;
    
    /**
     * 操作权限
     */
    public static final int OPERATE = 1;
    
    private static Map<Integer, String> desc = new HashMap<Integer, String>();
    
    static
    {
        desc.put(QUERY, "查看");
        desc.put(OPERATE, "操作");
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
     * 获取权限类型
     * @param subPrivilegeType 子权限类型
     * @return 权限类型
     */
    public static int getPrivilegeType(int subPrivilegeType)
    {
        if (subPrivilegeType == SubPrivilegeType.QUERY)
        {
            return PrivilegeType.QUERY;
        }
        else
        {
            return PrivilegeType.OPERATE;
        }
    }
    
    /**
     * 字符串描述
     * @param subPrivilegeType 子权限类型
     * @return 字符串描述
     */
    public static String toSubPriString(int subPrivilegeType)
    {
        return desc.get(getPrivilegeType(subPrivilegeType));
    }
}
