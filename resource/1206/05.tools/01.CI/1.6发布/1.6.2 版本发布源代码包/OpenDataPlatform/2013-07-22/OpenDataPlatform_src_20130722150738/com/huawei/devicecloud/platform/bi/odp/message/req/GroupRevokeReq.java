/*
 * 文 件 名:  GroupRevokeReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  取消预留请求
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

/**
 * 取消预留请求
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class GroupRevokeReq extends GroupAuthenReq
{
    //组Id
    private String group_id;
    
    public String getGroup_id()
    {
        return group_id;
    }
    
    public void setGroup_id(String group_id)
    {
        this.group_id = group_id;
    }
    
    /**
     * 创建取消预留请求对象
     * @return 取消预留请求对象
     */
    public RevokeReserveReq createRevokeReserveReq()
    {
        RevokeReserveReq req = new RevokeReserveReq();
        req.setAppId(this.getApp_id());
        req.setAuthenInfo(this.getAuthen_info());
        req.setTimestamp(this.getTimestamp());
        req.setReserveId(group_id);
        return req;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupRevokeReq [group_id=");
        builder.append(group_id);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
