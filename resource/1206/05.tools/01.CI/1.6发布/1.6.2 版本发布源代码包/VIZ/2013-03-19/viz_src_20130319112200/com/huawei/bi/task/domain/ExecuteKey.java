/*
 * 文 件 名:  ExecuteKey.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  执行Id
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-15
 */
package com.huawei.bi.task.domain;

/**
 * 执行Id
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-11-15]
 */
public class ExecuteKey
{
    private String exeId;

    public String getExeId()
    {
        return exeId;
    }

    public void setExeId(String exeId)
    {
        this.exeId = exeId;
    }

    @Override
    public String toString()
    {
        return this.exeId;
    }
}
