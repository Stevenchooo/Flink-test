/*
 * 文 件 名:  TccState.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  TCC状态
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-29
 */
package com.huawei.platform.tcc.state;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import com.huawei.platform.tcc.utils.TccUtil;

/**
 * TCC状态
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-29]
 */
public final class TccState
{
    private static final int DEFAULT_EXPIRED_SECONDS = 600;
    
    private static final TccState TCC_STATE = new TccState();
    
    /**
     * 禁止调度新增，记录了请求禁止调度数目已经过期时间
     */
    private final Map<Long, Date> forbidScheduleInfo = new HashMap<Long, Date>();
    
    /**
     * 当前ID
     */
    private final AtomicLong currentId = new AtomicLong(0);
    
    /**
     * 是否已经初始化
     */
    private volatile boolean initalized = false;
    
    private TccState()
    {
        
    }
    
    public boolean isInitalized()
    {
        return initalized;
    }
    
    public static TccState getInstance()
    {
        return TCC_STATE;
    }
    
    /**
     * 初始化完成
     */
    public void initalized()
    {
        this.initalized = true;
    }
    
    /**
     * 初始化完成
     */
    public void initalizing()
    {
        this.initalized = false;
    }
    
    /**
     * 初始化
     */
    public void init()
    {
        synchronized (forbidScheduleInfo)
        {
            initalized = false;
            forbidScheduleInfo.clear();
            currentId.set(0L);
        }
    }
    
    /**
     * 是否可以调度
     * @return 是否可以调度
     */
    public boolean canSchedule()
    {
        Date now = new Date();
        Entry<Long, Date> keyValue;
        synchronized (forbidScheduleInfo)
        {
            Iterator<Entry<Long, Date>> idEntryIter = forbidScheduleInfo.entrySet().iterator();
            //统计还未失效的
            while (idEntryIter.hasNext())
            {
                keyValue = idEntryIter.next();
                if (now.after(keyValue.getValue()))
                {
                    //已经过期
                    idEntryIter.remove();
                }
                else
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 临时停止调度
     * @return 标识
     */
    public Long tempStopSchedule()
    {
        Long id = currentId.addAndGet(1);
        //默认10分钟失效
        Date expiredTime = TccUtil.addSeconds(DEFAULT_EXPIRED_SECONDS);
        synchronized (forbidScheduleInfo)
        {
            forbidScheduleInfo.put(id, expiredTime);
        }
        
        return id;
    }
    
    /**
     * 开始继续调度
     * @param id 标识
     */
    public void continueSchedule(Long id)
    {
        if (null == id)
        {
            return;
        }
        
        synchronized (forbidScheduleInfo)
        {
            forbidScheduleInfo.remove(id);
        }
    }
}
