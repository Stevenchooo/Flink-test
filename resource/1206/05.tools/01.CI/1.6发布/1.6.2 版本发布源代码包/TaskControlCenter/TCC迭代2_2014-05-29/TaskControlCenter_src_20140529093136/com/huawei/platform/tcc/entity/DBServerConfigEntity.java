/*
 * 文 件 名:  DBServerConfigEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  数据库服务器信息实体类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.platform.tcc.entity;

/**
 * 数据库服务器信息实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class DBServerConfigEntity
{
    //数据库Id
    private String tccName;
    
    //Tcc访问地址
    private String tccUrl;
    
    //IP地址
    private String ipAddress;
    
    //端口
    private Integer port;
    
    //用户名
    private String userName;
    
    //数据库名
    private String dbName;
    
    //密码
    private String pwd;
    
    public String getTccUrl()
    {
        return tccUrl;
    }

    public void setTccUrl(String tccUrl)
    {
        this.tccUrl = tccUrl;
    }

    public String getTccName()
    {
        return tccName;
    }

    public void setTccName(String tccName)
    {
        this.tccName = tccName;
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
    
    public String getDbName()
    {
        return dbName;
    }
    
    public void setDbName(String dbname)
    {
        this.dbName = dbname == null ? null : dbname.trim();
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