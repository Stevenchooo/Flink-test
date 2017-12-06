/*
 * 文 件 名:  Validator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-9
 */
package com.huawei.devicecloud.platform.bi.common.validate;

import com.huawei.devicecloud.platform.bi.common.CException;

/**
 * 
 * 统一校验器接口，所有校验器由校验框架统一调用
 * 
 * @author  q00107831
 * @version [Open Data Platform Service, 2011-8-9]
 * @see  [相关类/方法]
 */
public interface Validator
{
    /**
     * 执行参数校验的核心接口方法
     * @param argObjArray 待校验的请求参数数组
     * @throws CException 
     */
    void validate(Object... argObjArray) throws CException;
}
