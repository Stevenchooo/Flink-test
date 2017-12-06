/*
 * 文 件 名:  TccDataSource.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  数据源
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.platform.tcc.datasource;

import org.apache.commons.dbcp.BasicDataSource;

import com.huawei.platform.tcc.utils.crypt.CryptUtil;

/**
 * 数据源
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-6]
 */
public class TccDataSource extends BasicDataSource
{
    private String keyword = "PkmJygVfrDxsDeeD";
    
    /**
     * 对数据源密码进行解密
     * @param password 加密的密码 
     */
    public void setPassword(String password)
    {
        super.setPassword(password);
        super.password = CryptUtil.decryptForAESStr(password.trim(), keyword);
    }
}
