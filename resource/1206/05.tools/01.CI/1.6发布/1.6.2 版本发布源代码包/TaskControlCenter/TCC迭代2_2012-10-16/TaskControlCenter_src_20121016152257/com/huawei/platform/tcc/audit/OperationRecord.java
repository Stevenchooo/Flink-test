/*
 * 文 件 名:  OperationRecord.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-7-2
 */
package com.huawei.platform.tcc.audit;

import java.util.Date;
import java.util.List;

import com.huawei.platform.tcc.domain.OperationRecordSearch;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;

/**
 * 操作记录接口
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-7-2]
 * @see  [相关类/方法]
 */
public interface OperationRecord
{
    /**
     * 写入操作记录
     * @param operateAuditInfo 操作记录
     * @return 是否成功
     * @see [类、类#方法、类#成员]
     */
    public abstract boolean writeOperLog(OperateAuditInfoEntity operateAuditInfo);
    
    /**
     * 由查询条件取出审计记录
     * @param search 查询条件
     * @return 审计记录集合
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public abstract List<OperateAuditInfoEntity> readRecord(
            OperationRecordSearch search) throws Exception;
    
    /**
     * 通过查询条件查询指定审计记录总数
     * @param search 查询
     * @return 审计记录总数
     * @throws Exception 异常
     */
    public abstract Integer getRecordCount(OperationRecordSearch search)
        throws Exception;
    
    /**
     * 删除三个月前的审计记录
     * @param date 三个月前的时间
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public abstract void deleteOldOperationRecord(Date date) throws Exception;
    
}