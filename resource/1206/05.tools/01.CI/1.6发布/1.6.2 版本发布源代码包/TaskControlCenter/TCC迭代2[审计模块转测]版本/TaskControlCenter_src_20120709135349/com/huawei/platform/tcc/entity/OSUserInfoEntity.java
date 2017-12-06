/* 文 件 名:  OSUserInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-30
 */
package com.huawei.platform.tcc.entity;

/**
 * 操作员信息
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-06-30]
 */
public class OSUserInfoEntity
{
    private String userName;
    
    private String pwd;
    
    private String desc;
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName == null ? null : userName.trim();
    }
    
    public String getPwd()
    {
        return pwd;
    }
    
    public void setPwd(String pwd)
    {
        this.pwd = pwd == null ? null : pwd.trim();
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc == null ? null : desc.trim();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("OSUserInfoEntity [userName=");
        builder.append(userName);
        builder.append(", desc=");
        builder.append(desc);
        builder.append("]");
        return builder.toString();
    }
}