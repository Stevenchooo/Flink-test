/*
 * 文 件 名:  ISendAlarming.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-7-4
 */
package com.huawei.platform.tcc.alarm;

/**
 * 发送告警接口
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-7-4]
 */
public interface ISendAlarming
{
    /**
     * 发送告警信息
     * @param taskId 任务Id
     * @param cycleId 周期Id
     * @param instanceID 实例Id
     * @param alarmInfo 告警信息
     */
    public void sendAlarm(long taskId, String cycleId, int instanceID, AlarmInfo alarmInfo);
}
