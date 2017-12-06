/*
 * 文 件 名:  DBServerAddressEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  数据库服务器信息实体类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

/**
 * 数据库服务器信息实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class DBServerAddressEntity
{
    //数据库Id
    private Integer dbServerId;
    
    //IP地址
    private String ipAddress;
    
    //端口
    private Integer port;
    
    //用户名
    private String userName;
    
    //数据库名
    private String dbname;
    
    //密码
    private String pwd;
    
    public Integer getDbServerId()
    {
        return dbServerId;
    }
    
    public void setDbServerId(Integer dbServerId)
    {
        this.dbServerId = dbServerId;
    }
    
    public String getIpAddress()
    {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }
    
    public Integer getPort()
    {
        return port;
    }
    
    public void setPort(Integer port)
    {
        this.port = port;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName == null ? null : userName.trim();
    }
    
    public String getDbname()
    {
        return dbname;
    }
    
    public void setDbname(String dbname)
    {
        this.dbname = dbname == null ? null : dbname.trim();
    }
    
    public String getPwd()
    {
        return pwd;
    }
    
    public void setPwd(String pwd)
    {
        this.pwd = pwd == null ? null : pwd.trim();
    }
}