package com.huawei.bi.task.da;

import com.huawei.bi.task.domain.SqlInfo;

public interface ISqlInfoPersist
{
    /**
     * 新增sql信息
     * @param user 用户
     * @param sqlStmt sql语句
     * @return 自增长Id
     * @throws Exception 异常
     */
    Integer addSqlInfo(String user, String sqlStmt)
        throws Exception;
    
    /**
     * 更新申请原因
     * @param sqlId id
     * @param exportReason 大数据导出原因
     * @throws Exception 异常
     */
    void updateExportReason(Integer sqlId,String user, String exportReason)
        throws Exception;
    
    /**
     * 通过user和stmt获取sqlId
     * @param user 用户
     * @param stmt sql语句
     * @return sqlId
     * @throws Exception 异常
     */
    Integer getSqlId(String user, String stmt)
        throws Exception;
    
    /**
     * 通过sqlId获取SqlInfo
     * @param sqlId id
     * @return sqlInfo
     * @throws Exception 异常
     */
    SqlInfo getSqlInfo(Integer sqlId)
        throws Exception;
    
    /**
     * 删除sql信息
     * @param sqlId id
     * @throws Exception 异常
     */
    void deleteSqlInfo(Integer sqlId)
        throws Exception;
}
