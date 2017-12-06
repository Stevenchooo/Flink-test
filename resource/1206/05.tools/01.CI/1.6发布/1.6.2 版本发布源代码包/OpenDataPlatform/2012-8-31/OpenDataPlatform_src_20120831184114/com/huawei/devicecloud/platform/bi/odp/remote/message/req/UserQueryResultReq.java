/*
 * 文 件 名:  UserQueryResultReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  查询记录总数返回接口的请求体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-9
 */
package com.huawei.devicecloud.platform.bi.odp.remote.message.req;

import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * 查询记录总数返回接口的请求体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-9]
 */
public class UserQueryResultReq implements IModifyResult
{
    private Long record_count;
    
    private String transaction_id;
    
    /**
     * 返回值
     */
    private Integer result_code;

    public Long getRecord_count()
    {
        return record_count;
    }

    public void setRecord_count(Long record_count)
    {
        this.record_count = record_count;
    }

    public String getTransaction_id()
    {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id)
    {
        this.transaction_id = transaction_id;
    }

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
        builder.append("UserQueryResultReq [record_count=");
        builder.append(record_count);
        builder.append(", transaction_id=");
        builder.append(transaction_id);
        builder.append(", result_code=");
        builder.append(result_code);
        builder.append("]");
        return builder.toString();
    }
}
