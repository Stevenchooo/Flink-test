/*
 * 文 件 名:  AdDealState.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-5
 */
package com.huawei.manager.mkt.constant;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-5]
 * @see  [相关类/方法]
 */
public enum UploadDealState
{
    /**
     * 新增成功
     */
    ADD_SUCCESS(1), 
    
    /**
     * 更新成功
     */
    UPDATE_SUCCESS(2),
    
    /**
     * 处理失败
     */
    DEAL_FAIL(3),
    
    /**
     * 处理忽略(荣耀)
     */
    IGNOREHONOR(4),
    /**
     * 处理忽略(vmall)
     */
    IGNOREVMALL(6),
    
    /**
     * 处理忽略(other)
     */
    IGNOREOTHER(7),
    
    /**
     * 处理
     */
    DEAL(5);
    
    /**
     * 状态
     */
    private int state;
    
    private UploadDealState(int state)
    {
        this.state = state;
    }
    
    
}
