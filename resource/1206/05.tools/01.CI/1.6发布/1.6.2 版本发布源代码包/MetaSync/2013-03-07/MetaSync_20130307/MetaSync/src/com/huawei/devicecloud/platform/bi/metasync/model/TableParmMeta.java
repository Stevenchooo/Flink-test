/*
 * 文 件 名:  TableParmMeta.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  表参数信息
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.model;

/**
 *表参数信息
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-4]
 */
public class TableParmMeta
{
    private Long tblId;
    
    private String paramKey;
    
    private String paramValue;
    
    public Long getTblId()
    {
        return tblId;
    }
    
    public void setTblId(Long tblId)
    {
        this.tblId = tblId;
    }
    
    public String getParamKey()
    {
        return paramKey;
    }
    
    public void setParamKey(String paramKey)
    {
        this.paramKey = paramKey;
    }
    
    public String getParamValue()
    {
        return paramValue;
    }
    
    public void setParamValue(String paramValue)
    {
        this.paramValue = paramValue;
    }
}
