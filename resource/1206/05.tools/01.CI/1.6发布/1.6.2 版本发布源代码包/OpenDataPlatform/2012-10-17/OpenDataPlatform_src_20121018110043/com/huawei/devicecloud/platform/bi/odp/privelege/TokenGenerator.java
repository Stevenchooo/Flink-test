/*
 * 文 件 名:  TokenGenerator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  Token生成类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-6
 */
package com.huawei.devicecloud.platform.bi.odp.privelege;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Token生成类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-6]
 */
public class TokenGenerator
{
    /**
     * 序号
     */
    private static AtomicInteger serialNum = new AtomicInteger(0);
    
    /**
     * 根据roleId产生token
     * @param roleId 角色Id
     * @return 根据roleId产生token
     */
    public Token genToken(Integer roleId)
    {
        StringBuilder tokenSB = new StringBuilder();
        
        //获取接口的访问权限值
        InterfaceCallPrevilege icallPre = new InterfaceCallPrevilege();
        tokenSB.append(icallPre.getInterfaceCallPrevilege(roleId));
        
        //产生指定长度的随机值
        tokenSB.append(RandomStringUtils.random(Token.RANDOM_LENGTH, true, true));
        int num = serialNum.incrementAndGet();
        //添加序号信息
        tokenSB.append(String.format("%04x", num));
        
        return new Token(tokenSB.toString().toUpperCase(Locale.ENGLISH));
    }
}
