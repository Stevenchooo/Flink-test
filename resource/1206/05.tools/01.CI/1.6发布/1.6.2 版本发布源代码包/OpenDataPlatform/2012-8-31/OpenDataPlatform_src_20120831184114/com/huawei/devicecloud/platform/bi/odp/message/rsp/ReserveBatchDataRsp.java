/*
 * 文 件 名:  QueryDataCountRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  reserveBatchDataReq方法返回体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.message.rsp;

import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * reserveBatchDataReq方法返回体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class ReserveBatchDataRsp implements IModifyResult
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
        builder.append("ReserveBatchDataRsp [result_code=");
        builder.append(result_code);
        builder.append("]");
        return builder.toString();
    }
}
