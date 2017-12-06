/*
 * 文 件 名:  WGetFileReq.java
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
public class WGetFileReq extends AuthenReq
{
    //预留Id
    private String reserveId;
    
    public String getReserveId()
    {
        return reserveId;
    }
    
    public void setReserveId(String reserveId)
    {
        this.reserveId = reserveId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("WGetFileReq [reserveId=");
        builder.append(reserveId);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
