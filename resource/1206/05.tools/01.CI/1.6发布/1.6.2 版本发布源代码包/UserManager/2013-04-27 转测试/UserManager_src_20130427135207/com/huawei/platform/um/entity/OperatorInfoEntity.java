/* 文 件 名:  OperatorInfoEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.um.entity;

import java.util.Date;

/**
 * 操作员信息
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-06-18]
 */
public class OperatorInfoEntity
{
    private String operatorName;
    
    private String pwd;
    
    private String desc;
    
    private Integer roleId;  
    
    private Date createTime;
    
    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }
    
    public Integer getRoleId()
    {
        return roleId;
    }

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
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
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
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append("]");
        return builder.toString();
    }
}