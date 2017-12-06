/*
 * 文 件 名:  OdpDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  数据开发平台的Dao层接口类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.dao;

import java.util.List;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.entity.AuditInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ConfigEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ControlFlagEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.DBServerAddressEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ReservedInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.RouteEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.TokenEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.UserEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.privelege.Token;

/**
 * dao接口
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public interface OdpDao
{
    /**
     * 获取用户信息
     * @param appId 应用ID号
     * @return  用户信息
     * @throws CException 异常
     */
    UserEntity getUserInfo(String appId)
        throws CException;
    
    /**
     * 获取配置记录
     * @return 配置记录
     * @throws CException 异常
     */
    ConfigEntity getConfigEntity()
        throws CException;
    
    /**
     * 获取存储业务数据的数据库连接信息
     * @return 存储业务数据的数据库连接信息
     * @throws CException 异常
     */
    List<DBServerAddressEntity> getDataDBServers()
        throws CException;
    
    /**
     * 添加Token记录
     * @param token token
     * @param tokenExpiredTime 失效分钟数
     * @return 是否成
     * @throws CException 异常 
     */
    boolean addDialogToken(Token token, int tokenExpiredTime)
        throws CException;
    
    /**
     * 查询未过期的Token记录
     * @param token token
     * @return Token实体
     * @throws CException 异常 
     */
    TokenEntity getVaildToken(Token token)
        throws CException;
    
    /**
     * 删除已经过期的Token记录
     * @throws CException 异常
     */
    void deleteInVaildToken()
        throws CException;
    
    /**
     * 将loadCtrlValue加1
     * @param loadCtrlThreshHold 阈值
     * @return 是否成功
     * @throws CException 异常
     */
    boolean incLoadControl(int loadCtrlThreshHold)
        throws CException;
    
    /**
     * 初始化流控值
     * @return 是否成功
     * @throws CException 异常
     */
    boolean initLoadCtrl()
        throws CException;
    
    /**
     * 将loadCtrlValue减1
     * @return 是否成功
     * @throws CException 异常
     */
    boolean decLoadControl()
        throws CException;
    
    /**
     * 获取控制相关配置
     * @return 控制相关配置
     * @throws CException 异常 
     */
    ControlFlagEntity getControlFlag()
        throws CException;
    
    /**
     * 获取表路由信息
     * @param tableName 表名
     * @param currentRouteTableId 当前路由表Id
     * @return  表路由信息
     * @throws CException 异常
     */
    List<RouteEntity> getRoutes(String tableName, int currentRouteTableId)
        throws CException;
    
    /**
     * 批量插入记录
     * @param tableName 表名
     * @param cfMappings 表列对象字段映射数组
     * @param records 记录
     * @exception CException 异常
     */
    void batchInsertRecords(String tableName, ColumnFieldMapping[] cfMappings, List<UserProfileEntity> records)
        throws CException;
    
    /**
     * 创建用户信息临时表
     * @param tempTableName 临时表表名
     * @param columnFields 表列与对象字段映射数组
     * @throws CException 异常
     */
    void createTempTable(String tempTableName, ColumnFieldMapping[] columnFields)
        throws CException;
    
    /**
     * 更新预留信息
     * @param info 预留实体信息
     * @throws CException 异常
     * @return 是否成功
     */
    boolean updateReservedInfo(ReservedInfoEntity info)
        throws CException;
    
    /**
     * 新增预留信息，获取预留Id
     * @param info 预留实体信息
     * @throws CException 异常
     * @return 预留Id
     */
    String addReservedInfo(ReservedInfoEntity info)
        throws CException;
    
    /**
     * 更新临时表失效时间
     * @param reserveID 预留ID
     * @param days 预留天数
     * @return 是否成功
     * @throws CException 异常
     */
    boolean updateReservedExpiredTime(String reserveID, int days)
        throws CException;
    
    /**
     * 获取预留信息
     * @param reserveID 预留号
     * @throws CException 异常
     * @return 预留Id
     */
    ReservedInfoEntity getReservedInfo(String reserveID)
        throws CException;
    
    /**
     * 新增审计
     * @param info 审计实体
     * @throws CException 异常
     * @return 预留Id
     */
    boolean addAuditInfo(AuditInfoEntity info)
        throws CException;
    
    /**
     * 更新审计信息
     * @param info 审计实体
     * @throws CException 异常
     * @return 预留Id
     */
    boolean updateAuditInfo(AuditInfoEntity info)
        throws CException;
    
    /**
     * 删除临时表
     * @param tmpTableName 表明
     * @throws CException 异常
     */
    void dropTempTable(String tmpTableName)
        throws CException;
    
    /**
     * 删除预留信息
     * @param reserveID 预留号
     * @return 是否成功
     * @throws CException 异常
     */
    boolean deleteReservedInfo(String reserveID)
        throws CException;
    
    /**
     * 获取所有过期的预留信息
     * @return 所有过期的预留信息
     * @throws CException 异常
     */
    List<ReservedInfoEntity> getAllExpiredReservedInfos()
        throws CException;
    
    /**
     * 获取表的所有用户信息记录集合
     * @return 获取表的所有用户信息记录集合
     * @param tableName 表名
     * @throws CException 异常
     */
    List<UserProfileEntity> getUPRecords(String tableName)
        throws CException;
    
    /**
     * 将文件中的数据加载到指定表中
     * @param fileName 文件名
     * @param tableName 表名
     * @param columns 列名数组
     * @throws CException 异常
     */
    void loadFile2Table(final String fileName, final String tableName, final List<String> columns)
        throws CException;
}
