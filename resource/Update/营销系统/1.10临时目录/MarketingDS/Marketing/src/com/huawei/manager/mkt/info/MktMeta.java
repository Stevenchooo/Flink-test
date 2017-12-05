/*
 * 文 件 名:  MktMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-10-26
 */
package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-10-26]
 * @see  [相关类/方法]
 */
public class MktMeta
{
    private Integer id;
    
    private Integer type;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    /**
     * 转字符串
     * @return      字符串
     */
    @Override
    public String toString()
    {
        return "MktMeta [id=" + id + ", type=" + type + "]";
    }
    
    
    
}
