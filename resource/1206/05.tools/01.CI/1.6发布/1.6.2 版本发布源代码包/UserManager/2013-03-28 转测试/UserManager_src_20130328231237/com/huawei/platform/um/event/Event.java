/*
 * 文 件 名:  TaskEvent.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  事件对象
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-10
 */
package com.huawei.platform.um.event;

/**
 * 事件
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-10]
 */
public class Event
{
    private Object sender;
    
    private int type;
    
    private Object data;
    
    /**
     * 构造函数
     * @param sender 发送者
     * @param type 事件类型
     * @param data 数据
     */
    public Event(Object sender, int type, Object data)
    {
        this.sender = sender;
        this.type = type;
        this.data = data;
    }
    
    public Object getSender()
    {
        return sender;
    }
    
    public int getType()
    {
        return type;
    }
    
    public Object getData()
    {
        return data;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Event[");
        builder.append(EventType.toString(type));
        builder.append(",");
        builder.append(data);
        builder.append(",");
        builder.append(sender);
        builder.append("]");
        return builder.toString();
    }
}
