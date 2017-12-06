/*
 * 文 件 名:  GroupAuthenReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  校验请求参数
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

/**
 * 校验请求参数
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class GroupAuthenReq
{
    //检验信息
    private String authen_info;
    
    //应用标识
    private String app_id;
    
    //时间戳
    private String timestamp;
    
    public String getAuthen_info()
    {
        return authen_info;
    }
    
    public void setAuthen_info(String authen_info)
    {
        this.authen_info = authen_info;
    }
    
    public String getApp_id()
    {
        return app_id;
    }
    
    public void setApp_id(String app_id)
    {
        this.app_id = app_id;
    }
    
    public String getTimestamp()
    {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupAuthenReq [authen_info=");
        builder.append(authen_info);
        builder.append(", app_id=");
        builder.append(app_id);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append("]");
        return builder.toString();
    }
}
