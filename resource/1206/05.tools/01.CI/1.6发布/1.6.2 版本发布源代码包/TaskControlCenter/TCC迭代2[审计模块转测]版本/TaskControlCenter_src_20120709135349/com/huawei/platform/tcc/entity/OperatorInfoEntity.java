/* 文 件 名:  OperatorInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.entity;

import java.util.Date;

/**
 * 操作员信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-06-18]
 */
public class OperatorInfoEntity
{
    private String operatorName;
    
    private String pwd;
    
    private String desc;
    
    private String roleId;  
    
    private String osUserName;
    
    private Date createTime;
    
    public String getOperatorName()
    {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName == null ? null : operatorName.trim();
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
    
    public String getRoleId()
    {
        return roleId;
    }
    
    public void setRoleId(String roleId)
    {
        this.roleId = roleId == null ? null : roleId.trim();
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getOsUserName()
    {
        return osUserName;
    }

    public void setOsUserName(String osUserName)
    {
        this.osUserName = osUserName;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("OperatorInfoEntity [operatorName=");
        builder.append(operatorName);
        builder.append(", desc=");
        builder.append(desc);
        builder.append(", roleId=");
        builder.append(roleId);
        builder.append(", osUserName=");
        builder.append(osUserName);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append("]");
        return builder.toString();
    }
}