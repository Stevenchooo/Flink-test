/*
 * 文 件 名:  AuthRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  authReq方法返回体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.message.rsp;

import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * AuthRsp方法返回体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class AuthRsp implements IModifyResult
{
    /**
     * Token
     */
    private String token;
    
    /**
     * 返回值
     */
    private Integer result_code;
    

    public Integer getResult_code()
    {
        return result_code;
    }

    public void setResult_code(final Integer result_code)
    {
        this.result_code = result_code;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(final String token)
    {
        this.token = token;
    }

    /**
     * 字符串表示
     * @return 字符串表示
     */
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("AuthRsp [token=");
        builder.append(token);
        builder.append(", result_code=");
        builder.append(result_code);
        builder.append("]");
        return builder.toString();
    }
}
