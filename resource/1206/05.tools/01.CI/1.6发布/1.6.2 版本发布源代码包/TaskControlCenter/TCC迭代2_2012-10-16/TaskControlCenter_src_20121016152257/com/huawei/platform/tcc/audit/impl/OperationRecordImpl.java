/*
 * 文 件 名:  AuditMgnt.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-7-2
 */
package com.huawei.platform.tcc.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.audit.OperationRecord;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.OperationRecordSearch;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.TaskEntity;

/**
 * 审计管理
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-7-2]
 */
public class OperationRecordImpl implements OperationRecord
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRecordImpl.class);
    
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
     * 写入操作记录
     * @param operateAuditInfo 操作记录
     * @return 是否成功
     * @see [类、类#方法、类#成员]
     */
    public boolean writeOperLog(OperateAuditInfoEntity operateAuditInfo)
    {
        LOGGER.debug("Enter writeOperLog. operateAuditInfo is {}", operateAuditInfo);
        try
        {
            operateAuditInfo.setOperatorTime(new Date());
            String taskId = operateAuditInfo.getTaskId();
            String serviceId = operateAuditInfo.getServiceId();
            if (null != taskId && null == serviceId)
            {
                String[] taskIdStrArr = taskId.split(";");
                List<String> serviceIdList = new ArrayList<String>();
                for (String taskIdStr : taskIdStrArr)
                {
                    if (StringUtils.isEmpty(taskIdStr))
                    {
                        continue;
                    }
                    TaskEntity task = tccDao.getTask(Long.valueOf(taskIdStr));
                    if (null != task)
                    {
                        serviceIdList.add(String.valueOf(task.getServiceid()));
                    }
                }
                operateAuditInfo.setServiceIds(serviceIdList);
            }
            tccDao.addOperLog(operateAuditInfo);
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error("writeOperLog failed! operateAuditInfo is {}", operateAuditInfo, e);
        }
        return false;
    }
    
    /**
     * 由查询条件取出审计记录
     * @param search 查询条件
     * @return 审计记录集合
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public List<OperateAuditInfoEntity> readRecord(OperationRecordSearch search)
        throws Exception
    {
        LOGGER.debug("enter readRecord, search is {}", search);
        try
        {
            if (null == search)
            {
                return null;
            }
            
            return tccDao.getOperationRecords(search);
        }
        catch (Exception e)
        {
            LOGGER.error("readRecord failed, search is [{}].", search, e);
            throw e;
        }
    }
    
    /**
     * 通过查询条件查询指定审计记录总数
     * @param search 查询
     * @return 审计记录总数
     * @throws Exception 异常
     */
    public Integer getRecordCount(OperationRecordSearch search)
        throws Exception
    {
        LOGGER.debug("Enter getRecordCount. search is {}", search);
        try
        {
            if (null != search)
            {
                return tccDao.getRecordCount(search);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRecordCount failed! search is {}", search, e);
            throw e;
        }
    }
    
    /**
     * 删除三个月前的审计记录
     * @param date 三个月前的时间
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public void deleteOldOperationRecord(Date date)
        throws Exception
    {
        LOGGER.debug("Enter deleteOldOperationRecord. date is {}", date);
        try
        {
            tccDao.deleteOldOperationRecord(date);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOldOperationRecord failed! date is {}", date, e);
            throw e;
        }
    }
    
}
