/*
 * 文 件 名:  RevokeReserveRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  取消预留数据返回消息
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.message.rsp;

import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * 取消预留数据返回消息
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class RevokeReserveRsp implements IModifyResult
{
    /**
     * 返回值
     */
    private Integer result_code;
    

    public Integer getResult_code()
    {
        return result_code;
    }

    public void setResult_code(Integer result_code)
    {
        this.result_code = result_code;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("RevokeReserveRsp [result_code=");
        builder.append(result_code);
        builder.append("]");
        return builder.toString();
    }
}
