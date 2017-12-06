/*
 * 文 件 名:  Token.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  Token类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.privelege;

/**
 * Token类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class Token
{
    /**
     * Token长度
     */
    public static final int TOKEN_LENGTH = 32;
    
    /**
     * 随机串长度
     */
    public static final int RANDOM_LENGTH = 16;
    
    //token字符串
    private String tokenStr;
    
    /**
     * 构造函数
     * @param token token字符串
     */
    public Token(String token)
    {
        this.tokenStr = token;
    }
    
    public String getToken()
    {
        return tokenStr;
    }
}
