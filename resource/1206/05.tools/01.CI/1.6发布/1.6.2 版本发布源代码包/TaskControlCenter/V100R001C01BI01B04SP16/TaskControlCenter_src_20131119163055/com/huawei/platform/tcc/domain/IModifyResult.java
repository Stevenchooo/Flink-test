/*
 * 文 件 名:  IModifyResult.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  修改返回码接口
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-18
 */
package com.huawei.platform.tcc.domain;


/**
 * 修改返回码接口
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-18]
 */
public interface IModifyResult
{
    /**
     * 设置返回值
     * @param resultCode 返回码
     */
    void setResultCode(Integer resultCode);
}
