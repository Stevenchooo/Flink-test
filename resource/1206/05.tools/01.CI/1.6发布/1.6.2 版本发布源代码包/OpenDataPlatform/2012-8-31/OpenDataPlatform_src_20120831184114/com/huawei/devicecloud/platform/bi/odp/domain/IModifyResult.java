/*
 * 文 件 名:  IModifyResult.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  修改返回码接口
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-6
 */
package com.huawei.devicecloud.platform.bi.odp.domain;


/**
 * 修改返回码接口
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-6]
 */
public interface IModifyResult
{
    /**
     * 设置返回值
     * @param result_code 返回码
     */
    void setResult_code(Integer result_code);
}
