/*
 * 文 件 名:  GroupFetchReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  获取文件流请求
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

/**
 * 获取文件流请求
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class GroupFetchReq extends GroupAuthenReq
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
     * 创建获取文件请求对象
     * @return 获取文件请求对象
     */
    public WGetFileReq createWGetFileReq()
    {
        //初始化获取文件请求对象
        WGetFileReq req = new WGetFileReq();
        req.setAppId(this.getApp_id());
        req.setAuthenInfo(this.getAuthen_info());
        req.setReserveId(group_id);
        req.setTimestamp(this.getTimestamp());
        return req;
    }
    
    /**
     * 对象字符串表示
     * @return 对象字符串表示
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupFetchReq [group_id=");
        builder.append(group_id);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
