/*
 * 文 件 名:  Listenter.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.tcc.listener;

import com.huawei.platform.tcc.event.Event;

/**
 * 监听器
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-10]
 */
public interface Listener
{
    /**
     * 处理事件
     * @param event 处理事件
     */
    void process(Event event);
}
