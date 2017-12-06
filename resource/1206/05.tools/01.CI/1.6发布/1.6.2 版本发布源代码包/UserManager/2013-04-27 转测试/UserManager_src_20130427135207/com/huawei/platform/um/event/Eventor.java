/*
 * 文 件 名:  eventor.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.um.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.um.listener.Listener;

/**
 * 事件发生器
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-12-10]
 */
public class Eventor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Eventor.class);
    
    private static Map<Integer, List<Listener>> listeners = new HashMap<Integer, List<Listener>>();
    
    /**
     * 广播事件
     * @param sender 发送者
     * @param eventType 事件类型 
     * @param data 数据
     */
    public static synchronized void fireEvent(Object sender, int eventType, Object data)
    {
        List<Listener> typeLiss = listeners.get(eventType);
        Event event = new Event(sender, eventType, data);
        Long startScheduleTime = 0L;
        Long endScheduleTime = 0L;
        Long scheduleDuration;
        if (null != typeLiss && !typeLiss.isEmpty())
        {
            for (Listener lis : typeLiss)
            {
                try
                {
                    //if (TccConfig.getIsLogScheduleDuration())
                    {
                        // 记录调度开始时间
                        startScheduleTime = new Date().getTime();
                    }
                    
                    LOGGER.debug("process started... listerner is {}, event is {}.", lis, event);
                    lis.process(event);
                    LOGGER.debug("process finished... listerner is {}, event is {}.", lis, event);
                    // 如果tcc配置里开启记录调度时长，则将调度时长写入日志中
                    //if (TccConfig.getIsLogScheduleDuration())
                    {
                        // 记录调度结束时间
                        endScheduleTime = new Date().getTime();
                        scheduleDuration = endScheduleTime - startScheduleTime;
                        LOGGER.info("listerner[{}] needs {} ms to process {}!", new Object[] {lis, scheduleDuration,
                            event});
                    }
                }
                catch (Exception e)
                {
                    //记录出错信息
                    LOGGER.error("listener is {}, event is {}.", new Object[] {lis, event, e});
                }
            }
        }
    }
    
    /**
     * listener注册到指定的事件类型
     * @param eventType 事件类型
     * @param listener 监听器
     */
    public static synchronized void register(int eventType, Listener listener)
    {
        if (null == listener)
        {
            //监听器不合法
            return;
        }
        
        List<Listener> typeLiss = listeners.get(eventType);
        if (null == typeLiss)
        {
            typeLiss = new ArrayList<Listener>();
            listeners.put(eventType, typeLiss);
        }
        
        typeLiss.add(listener);
    }
    
    /**
     * 初始化
     */
    public static synchronized void init()
    {
        listeners.clear();
    }
}
