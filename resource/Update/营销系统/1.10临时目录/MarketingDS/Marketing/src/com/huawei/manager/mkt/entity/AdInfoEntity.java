/*
 * 文 件 名:  AdInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-2
 */
package com.huawei.manager.mkt.entity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-2]
 * @see  [相关类/方法]
 */
public class AdInfoEntity
{
    //活动ID
    private Integer id;
    
    //广告位ID
    private Integer aid;
    
    //着陆平台
    private String platform;
    
    //当前状态
    private Integer state;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getAid()
    {
        return aid;
    }

    public void setAid(Integer aid)
    {
        this.aid = aid;
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    /**
     * 转字符串
     * @return      字符串
     */
    @Override
    public String toString()
    {
        return "AdInfoEntity [id=" + id + ", aid=" + aid + ", platform=" + platform + ", state=" + state + "]";
    }
    
}
