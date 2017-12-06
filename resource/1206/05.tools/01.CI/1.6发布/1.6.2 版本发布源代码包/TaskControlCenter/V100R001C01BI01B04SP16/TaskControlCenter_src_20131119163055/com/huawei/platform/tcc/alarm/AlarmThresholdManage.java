/*
 * 文 件 名:  AlarmThresholdManage.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  告警阈值管理类
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-11
 */
package com.huawei.platform.tcc.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.entity.TaskAlarmThresholdEntity;
import com.huawei.platform.tcc.event.Event;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.listener.Listener;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 告警阈值管理类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-11]
 */
public class AlarmThresholdManage implements Listener
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmThresholdManage.class);
    
    private static final Map<Long, TaskAlarmThresholdEntity> ALARM_THRESHOLDS =
        new HashMap<Long, TaskAlarmThresholdEntity>();
    
    private TccDao tccDao;
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 初始化
     */
    public void init()
    {
        try
        {
            //初始化任务
            synchronized (ALARM_THRESHOLDS)
            {
                ALARM_THRESHOLDS.clear();
            }
            
            //获取任务告警阈值列表
            List<TaskAlarmThresholdEntity> alarmThreLst = tccDao.getAlarmThresholdList();
            
            //任务告警阈值列表不为空
            if (null != alarmThreLst)
            {
                for (TaskAlarmThresholdEntity alarmThre : alarmThreLst)
                {
                    synchronized (ALARM_THRESHOLDS)
                    {
                        ALARM_THRESHOLDS.put(alarmThre.getTaskId(), alarmThre);
                    }
                }
            }
            
            Eventor.register(EventType.ADD_ALARM_THRESHOLD, this);
            Eventor.register(EventType.UPDATE_ALARM_THRESHOLD, this);
            Eventor.register(EventType.DELETE_ALARM_THRESHOLD, this);
        }
        catch (Exception e)
        {
            LOGGER.error("AlarmThresholdManage init failed!", e);
        }
    }
    
    /**
     * 获取告警阈值
     * @param taskId 任务Id
     * @return 告警阈值
     */
    public TaskAlarmThresholdEntity getAlarmThreshold(Long taskId)
    {
        if (null == taskId)
        {
            return null;
        }
        else
        {
            TaskAlarmThresholdEntity alarmThre;
            synchronized (ALARM_THRESHOLDS)
            {
                alarmThre = ALARM_THRESHOLDS.get(taskId);
            }
            
            return alarmThre;
        }
        
    }
    
    /**
     * 获取所有告警阈值
     * @return 告警阈值列表
     */
    public List<TaskAlarmThresholdEntity> getAlarmThresholds()
    {
        List<TaskAlarmThresholdEntity> alarmThres = new ArrayList<TaskAlarmThresholdEntity>();
        TaskAlarmThresholdEntity alarmThre;
        synchronized (ALARM_THRESHOLDS)
        {
            for (Entry<Long, TaskAlarmThresholdEntity> entry : ALARM_THRESHOLDS.entrySet())
            {
                alarmThre = entry.getValue();
                if (null != alarmThre)
                {
                    alarmThres.add(alarmThre);
                }
            }
        }
        
        return alarmThres;
    }
    
    /**
     * 获取所有告警阈值
     * @param taskIds 任务Id集合
     * @return 告警阈值列表
     */
    public List<TaskAlarmThresholdEntity> getAlarmThresholds(List<Long> taskIds)
    {
        List<TaskAlarmThresholdEntity> alarmThres = new ArrayList<TaskAlarmThresholdEntity>();
        if (null == taskIds || taskIds.isEmpty())
        {
            return alarmThres;
        }
        
        TaskAlarmThresholdEntity alarmThre;
        synchronized (ALARM_THRESHOLDS)
        {
            for (Long taskId : taskIds)
            {
                alarmThre = ALARM_THRESHOLDS.get(taskId);
                if (null != alarmThre)
                {
                    alarmThres.add(alarmThre);
                }
            }
        }
        
        return alarmThres;
    }
    
    /**
     * 加载告警项
     * @param taskIds 任务Id集合
     */
    public void loadAlarmThres(List<Long> taskIds)
    {
        try
        {
            List<TaskAlarmThresholdEntity> alarmThres = tccDao.getAlarmThresholds(taskIds);
            if (null == alarmThres || alarmThres.isEmpty())
            {
                return;
            }
            
            for (TaskAlarmThresholdEntity alarmThre : alarmThres)
            {
                synchronized (ALARM_THRESHOLDS)
                {
                    ALARM_THRESHOLDS.put(alarmThre.getTaskId(), alarmThre);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("loadAlarmThres failed!", e);
        }
    }
    
    /**
     * 新增或者更新任务告警阈值
     * @param taskId 任务Id
     * @throws Exception 异常
     */
    private void addOrUpdate(Long taskId)
        throws Exception
    {
        if (null == taskId)
        {
            return;
        }
        
        //获取任务告警阈值列表
        TaskAlarmThresholdEntity alarmThreNew = tccDao.getAlarmThreshold(taskId);
        if (null != alarmThreNew)
        {
            synchronized (ALARM_THRESHOLDS)
            {
                ALARM_THRESHOLDS.put(taskId, alarmThreNew);
            }
        }
    }
    
    /**
     * 新增或者更新任务告警阈值
     * @param taskId 任务Id
     */
    private void remove(Long taskId)
    {
        //不允许为空
        if (null == taskId)
        {
            return;
        }
        
        synchronized (ALARM_THRESHOLDS)
        {
            ALARM_THRESHOLDS.remove(taskId);
        }
    }
    
    @Override
    public synchronized void process(Event event)
    {
        try
        {
            if (null != event)
            {
                int eventType = event.getType();
                if (EventType.ADD_ALARM_THRESHOLD == eventType)
                {
                    addOrUpdate((Long)(event.getData()));
                }
                else if (EventType.UPDATE_ALARM_THRESHOLD == eventType)
                {
                    addOrUpdate((Long)(event.getData()));
                }
                else if (EventType.DELETE_ALARM_THRESHOLD == eventType)
                {
                    remove((Long)(event.getData()));
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("process failed! event is {}", TccUtil.truncatEncode(event), e);
        }
    }
    
}
