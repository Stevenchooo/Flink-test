/*
 * 文 件 名:  ReturnValue2Page.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回给页面的值
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-06-19]
 */
public class ReturnValue2Page
{
    /**
     * 成功
     */
    public static final String SUCCESS = "true";
    
    /**
     * 失败
     */
    public static final String FAILED = "false";
    
    private int returnValue2PageType;
    
    private boolean success;
    
    private transient StringBuilder sReturnValue;
    
    private List<String> values = new ArrayList<String>();
    
    /**
     * 扩展数据
     */
    private Object extValue;
    
    /**
     * 构造函数
     * @param success 返回成功还是失败标识
     * @param rv2PageType 返回码类型
     */
    public ReturnValue2Page(boolean success, int rv2PageType)
    {
        this.success = success;
        this.returnValue2PageType = rv2PageType;
    }
    
    public Object getExtValue()
    {
        return extValue;
    }

    public void setExtValue(Object extValue)
    {
        this.extValue = extValue;
    }
    
    public int getReturnValue2PageType()
    {
        return returnValue2PageType;
    }
    
    public void setReturnValue2PageType(int returnValue2PageType)
    {
        this.returnValue2PageType = returnValue2PageType;
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    /**
     * 修改返回标识，并清空返回值列表
     * @param success 返回标识
     */
    public void setSuccess(boolean success)
    {
        this.success = success;
        this.values.clear();
    }
    
    /**
     * 增加返回的值
     * @param value 值
     */
    public void addReturnValue(String value)
    {
        values.add(value);
    }
    
    @Override
    public String toString()
    {
        sReturnValue = new StringBuilder(); 
        sReturnValue.append(success ? SUCCESS : FAILED);
        sReturnValue.append(',');
        sReturnValue.append(returnValue2PageType);
        sReturnValue.append(',');
        for (String value : values)
        {
            sReturnValue.append(value);
            sReturnValue.append(',');
        }
        return sReturnValue.toString();
    }

    public List<String> getValues()
    {
        return values;
    }
}
