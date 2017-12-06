/*
 * 文 件 名:  QueryDataCountRespReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  查询记录总数返回接口的请求体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-9
 */
package com.huawei.devicecloud.platform.bi.odp.remote.message.rsp;

/**
 * 批量数据预留接口返回接口的响应
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-9]
 */
public class GroupsPrepareResultRsp
{
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
        builder.append("GroupsPrepareResultRsp [result_code=");
        builder.append(result_code);
        builder.append("]");
        return builder.toString();
    }
}
