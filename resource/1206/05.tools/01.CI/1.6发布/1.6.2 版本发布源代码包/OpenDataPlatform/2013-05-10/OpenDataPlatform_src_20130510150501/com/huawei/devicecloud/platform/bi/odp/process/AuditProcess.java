/*
 * 文 件 名:  AuditProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ReservedInfo;
import com.huawei.devicecloud.platform.bi.odp.entity.AuditInfoEntity;

/**
 * 审计处理类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class AuditProcess
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditProcess.class);
    
    /**
     * Dao层调用对象
     */
    private OdpDao odpDao;
    
    /**
     * 记录审计信息
     * @param appId 应用标准
     * @param operatorType 操作类型
     * @param appTransactionKey 应用事务键值
     * @param reservedInfos 预留信息数组
     */
    public void logAuditInfos(final String appId, final int operatorType, final String appTransactionKey,
        final ReservedInfo[] reservedInfos)
    {
        //记录审计信息
        AuditInfoEntity auditInfo = null;
        for (int i = 0; i < reservedInfos.length; i++)
        {
            //初始化审计信息对象
            auditInfo = new AuditInfoEntity();
            auditInfo.setReserveId(reservedInfos[i].getReserveId());
            auditInfo.setOperatorType(operatorType);
            auditInfo.setAppId(appId);
            auditInfo.setRecordNumber(reservedInfos[i].getRecordCount());
            auditInfo.setAppTransactionKey(appTransactionKey);
            try
            {
                odpDao.addAuditInfo(auditInfo);
            }
            catch (CException e)
            {
                LOGGER.error("addAuditInfo faild! auditInfo is {}", auditInfo, e);
            }
        }
    }
    
    /**
     * 记录审计信息
     * @param auditInfo 审计信息
     */
    public void logAuditInfo(final AuditInfoEntity auditInfo)
    {
        try
        {
            odpDao.addAuditInfo(auditInfo);
        }
        catch (CException e)
        {
            LOGGER.error("logAuditInfo faild! auditInfo is {}", auditInfo, e);
        }
    }
    
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    public void setOdpDao(final OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
}
