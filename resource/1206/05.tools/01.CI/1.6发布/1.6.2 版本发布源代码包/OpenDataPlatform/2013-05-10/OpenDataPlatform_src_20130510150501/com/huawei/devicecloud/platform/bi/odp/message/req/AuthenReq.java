/*
 * 文 件 名:  AuthenReq.java
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
public class AuthenReq
{
    //校验头
    private String authenInfo;
    
    //应用标识
    private String appId;
    
    //时间戳
    private String timestamp;
    
    public String getAuthenInfo()
    {
        return authenInfo;
    }
    
    public void setAuthenInfo(String authenInfo)
    {
        this.authenInfo = authenInfo;
    }
    
    public String getAppId()
    {
        return appId;
    }
    
    public void setAppId(String appId)
    {
        this.appId = appId;
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
        builder.append("AuthenReq [authenInfo=");
        builder.append(authenInfo);
        builder.append(", appId=");
        builder.append(appId);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append("]");
        return builder.toString();
    }
}
