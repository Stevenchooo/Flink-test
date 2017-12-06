/*
 * 文 件 名:  KeyValuePair.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.um.domain;

/**
 * 键值对
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class KeyValuePair
{
    private String key;
    
    private String value;
    
    /**
     * 构造函数
     * @param key 键
     * @param value 值
     */
    public KeyValuePair(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
