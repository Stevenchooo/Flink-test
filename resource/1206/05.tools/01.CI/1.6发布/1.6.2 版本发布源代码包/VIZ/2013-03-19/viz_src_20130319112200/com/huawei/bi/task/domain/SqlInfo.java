/*
 * 文 件 名:  SqlInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-6
 */
package com.huawei.bi.task.domain;

import java.sql.Date;

/**
 * SqlInfo实体
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-11-6]
 */
public class SqlInfo
{
    private Integer sqlId;
    
    private String user;
    
    private String sqlStmt;
    
    private boolean exportPrivilege;
    
    private Date validStartDate;
    
    private Date validEndDate;
    
    private String exportReason;
    
    private Date createTime;
    
    public Integer getSqlId()
    {
        return sqlId;
    }
    
    public void setSqlId(Integer sqlId)
    {
        this.sqlId = sqlId;
    }
    
    public String getUser()
    {
        return user;
    }
    
    public Date getValidStartDate()
    {
        return validStartDate;
    }
    
    public void setValidStartDate(Date validStartDate)
    {
        this.validStartDate = validStartDate;
    }
    
    public Date getValidEndDate()
    {
        return validEndDate;
    }
    
    public void setValidEndDate(Date validEndDate)
    {
        this.validEndDate = validEndDate;
    }
    
    public void setUser(String user)
    {
        this.user = user;
    }
    
    public String getSqlStmt()
    {
        return sqlStmt;
    }
    
    public void setSqlStmt(String sqlStmt)
    {
        this.sqlStmt = sqlStmt;
    }
    
    public boolean isExportPrivilege()
    {
        return exportPrivilege;
    }
    
    public void setExportPrivilege(boolean exportPrivilege)
    {
        this.exportPrivilege = exportPrivilege;
    }
    
    public String getExportReason()
    {
        return exportReason;
    }
    
    public void setExportReason(String exportReason)
    {
        this.exportReason = exportReason;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
