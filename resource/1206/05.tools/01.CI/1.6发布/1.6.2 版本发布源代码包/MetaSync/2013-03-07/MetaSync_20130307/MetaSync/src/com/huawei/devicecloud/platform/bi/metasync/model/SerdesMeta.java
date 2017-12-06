/*
 * 文 件 名:  SerSDMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-1
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 * 序列化资源元数据
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-1]
 */
public class SerdesMeta
{
    private Long serdeId;
    
    private String name;
    
    private String slib;
    
    public Long getSerdeId()
    {
        return serdeId;
    }

    public void setSerdeId(Long serdeId)
    {
        this.serdeId = serdeId;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSlib()
    {
        return slib;
    }

    public void setSlib(String slib)
    {
        this.slib = slib;
    }
}
