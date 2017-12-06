/*
 * 文 件 名:  MessageQueue.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  消息队列
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-30
 */
package com.huawei.platform.tcc.SSH;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息队列
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-30]
 */
public final class MessageQueue
{
    private static MessageQueue unqQueue = new MessageQueue();
    
    private List<InstanceInfo> msgQueue;
    
    /**
     * 单例
     */
    private MessageQueue()
    {
        msgQueue = new ArrayList<InstanceInfo>();
    }
    
    /**
     * 写入消息
     * @param info 消息
     */
    public void writeMsg(InstanceInfo info)
    {
        if (null == info)
        {
            return;
        }
        
        //每次都加锁写入，有点影响效率
        synchronized (MessageQueue.class)
        {
            msgQueue.add(info);
        }
    }
    
    /**
     * 读出所有消息
     * @return 实例状态集合
     */
    public List<InstanceInfo> readMsgs()
    {
        List<InstanceInfo> instanceInfos;
        synchronized (MessageQueue.class)
        {
            instanceInfos = msgQueue;
            msgQueue = new ArrayList<InstanceInfo>();
        }
        
        return instanceInfos;
    }
    
    /**
     * 获取单例消息队列对象
     * @return 单例消息队列对象
     */
    public static MessageQueue getInstance()
    {
        return unqQueue;
    }
}
