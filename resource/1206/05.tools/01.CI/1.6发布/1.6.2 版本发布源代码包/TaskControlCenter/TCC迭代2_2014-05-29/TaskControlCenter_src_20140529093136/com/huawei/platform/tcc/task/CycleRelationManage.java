/*
 * 文 件 名:  CycleManage.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  周期依赖关系管理类
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-21
 */
package com.huawei.platform.tcc.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.TaskRSQueryParam;
import com.huawei.platform.tcc.event.Event;
import com.huawei.platform.tcc.listener.Listener;

/**
 * 周期依赖关系管理类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-21]
 */
public class CycleRelationManage  implements Listener
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CycleRelationManage.class);
    
    //任务周期集合,key为任务id和周期id的形如"201111,20121001-00"复合
    private static final Map<String, CycleRelation> CYCLE_RELATIONS = new HashMap<String, CycleRelation>();
    
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
        //获取所有状态为非sucess状态的任务周期（并获取任务的最小周期Id和最大周期Id，可能会很多，需要分批加载）
        //TaskRSQueryParam taskRSQP = new TaskRSQueryParam();
        //tccDao.getTaskRSList(taskRSQP);
        
        //遍历所有的周期，根据任务依赖构建周期依赖关系[对不存在的任务周期创建不存在状态，对昨天的已经完成的任务周期不在更新前驱后继]
    }

    @Override
    public void process(Event event)
    {
        
    }
    
    
}
