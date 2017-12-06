/*
 * 文 件 名:  LoadTasksRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  加载任务响应
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-18
 */
package com.huawei.platform.um.message.rsp;

import com.huawei.platform.um.domain.IModifyResult;

/**
 * 加载任务响应
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-2-18]
 */
public class LoadTasksRsp implements IModifyResult
{
    /**
     * 返回码
     */
    private Integer resultCode;
    
    /**
     * 标识
     */
    private Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }
}
