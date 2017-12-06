/*
 * 文 件 名:  GroupsPrepareResultReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  批数据预留返回接口的请求体
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-9
 */
package com.huawei.devicecloud.platform.bi.odp.remote.message.req;

import java.util.List;

import com.huawei.devicecloud.platform.bi.odp.domain.GroupInfo;
import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * 批数据预留返回接口的请求体
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-9]
 */
public class GroupsPrepareResultReq implements IModifyResult
{
    private List<GroupInfo> group_info;
    
    private String transaction_id;
    
    /**
     * 返回值
     */
    private Integer result_code;
    
    public List<GroupInfo> getGroup_info()
    {
        return group_info;
    }

    public void setGroup_info(List<GroupInfo> group_info)
    {
        this.group_info = group_info;
    }

    public String getTransaction_id()
    {
        return transaction_id;
    }
    
    public Integer getResult_code()
    {
        return result_code;
    }
    
    public void setTransaction_id(String transaction_id)
    {
        this.transaction_id = transaction_id;
    }
    
    public void setResult_code(Integer result_code)
    {
        this.result_code = result_code;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupsPrepareResultReq [group_info=");
        builder.append(group_info);
        builder.append(", transaction_id=");
        builder.append(transaction_id);
        builder.append(", result_code=");
        builder.append(result_code);
        builder.append("]");
        return builder.toString();
    }
}
