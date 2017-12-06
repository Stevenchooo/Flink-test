/*
 * 文 件 名:  ConnectInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  SSH连接信息
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-29
 */
package com.huawei.platform.tcc.SSH;

import com.huawei.platform.tcc.exception.ArgumentException;

/**
 * SSH连接信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class ConnectInfo
{
    //主机
    private String host;
    
    //端口
    private int port;
    
    //用户
    private String user;
    
    //pem密钥
    private String pemKey;
    
    /**
     * 构造函数
     * @param connInfo 连接信息
     * @throws ArgumentException 异常
     */
    public ConnectInfo(ConnectInfo connInfo)
        throws ArgumentException
    {
        if (null == connInfo || null == connInfo.getHost() || null == connInfo.getUser() || null == connInfo.pemKey)
        {
            throw new ArgumentException("connInfo and it's field can't be null!");
        }
        
        this.host = connInfo.getHost();
        this.port = connInfo.getPort();
        this.user = connInfo.getUser();
        this.pemKey = connInfo.getPemKey();
    }
    
    /**
     * 构造函数
     */
    public ConnectInfo()
    {
    }
    
    /**
     * 连接Id
     * @return 连接Id
     */
    public String getIdentify()
    {
        return String.format("%s,%d", host, port);
    }
    
    public String getHost()
    {
        return host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public String getUser()
    {
        return user;
    }
    
    public void setUser(String user)
    {
        this.user = user;
    }
    
    public String getPemKey()
    {
        return pemKey;
    }
    
    public void setPemKey(String pemKey)
    {
        this.pemKey = pemKey;
    }
    
    /**
     * 字符串表示，禁止显示pemKey值
     * @return 字符串表示
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ConnectInfo [host=");
        builder.append(host);
        builder.append(", port=");
        builder.append(port);
        builder.append(", user=");
        builder.append(user);
        builder.append("]");
        return builder.toString();
    }
}
