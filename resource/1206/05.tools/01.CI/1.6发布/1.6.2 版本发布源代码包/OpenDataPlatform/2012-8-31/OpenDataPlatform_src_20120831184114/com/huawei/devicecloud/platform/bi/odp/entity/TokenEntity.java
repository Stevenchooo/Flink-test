/*
 * 文 件 名:  TokenEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

import java.util.Date;

/**
 * Token实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class TokenEntity
{
    //token
    private String token;
    
    //失效时间
    private Date expiredTime;
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token == null ? null : token.trim();
    }
    
    public Date getExpiredTime()
    {
        return expiredTime;
    }
    
    public void setExpiredTime(Date expiredTime)
    {
        this.expiredTime = expiredTime;
    }
}
