/*
 * 文 件 名:  FieldCheckInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-22
 */
package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-22]
 * @see  [相关类/方法]
 */
public class FieldCheckInfo
{
    //标志
    private boolean flag;
    
    //描述
    private String desc;

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    /**
     * 转字符串
     * @return      字符串
     */
    @Override
    public String toString()
    {
        return "FieldCheckInfo [flag=" + flag + ", desc=" + desc + "]";
    }

    
    
}
