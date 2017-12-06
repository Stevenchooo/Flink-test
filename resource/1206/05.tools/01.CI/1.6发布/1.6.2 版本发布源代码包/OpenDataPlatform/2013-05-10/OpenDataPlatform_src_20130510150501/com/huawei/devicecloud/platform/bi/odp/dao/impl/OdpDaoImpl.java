/*
 * 文 件 名: OdpDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  数据�?��平台的Dao层接口实现类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.dao.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.GroupUserFilterParam;
import com.huawei.devicecloud.platform.bi.odp.entity.AuditInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ConfigEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ControlFlagEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.DBServerAddressEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.FileUploadTableEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ReservedInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.RouteEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.TokenEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.UserEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.privelege.Token;

/**
 * 数据开放平台的Dao层接口实现类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class OdpDaoImpl extends SqlSessionDaoSupport implements OdpDao
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpDaoImpl.class);
    
    private static final int SMALL_SIZE = 5;
    
    /**
     * 获取mysql的数据源
     * @return 操作hive的数据源对象
     */
    @Override
    public BasicDataSource getMysqlBDS()
    {
        return (BasicDataSource)this.getSqlSession().getConfiguration().getEnvironment().getDataSource();
    }
    
    /**
     * 获取用户信息
     * @param appId 应用ID�?
     * @return  用户信息
     * @throws CException 异常
     */
    @Override
    public UserEntity getUserInfo(final String appId)
        throws CException
    {
        LOGGER.debug("Enter getUserInfo. appId is {}", appId);
        try
        {
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao." + "user.getUserInfo", appId);
            
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                //不为空就强制转换
                return (UserEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUserInfo failed! appId is {}", appId, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取配置记录
     * @return 配置记录
     * @throws CException 异常
     */
    @Override
    public ConfigEntity getConfigEntity()
        throws CException
    {
        LOGGER.debug("Enter getConfigEntity.");
        try
        {
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao." + "config.getConfig");
            
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                //不为空就强制转换
                return (ConfigEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getConfigEntity failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取存储业务数据的数据库连接信息
     * @return 存储业务数据的数据库连接信息
     * @throws CException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DBServerAddressEntity> getDataDBServers()
        throws CException
    {
        LOGGER.debug("Enter getDataDBServers.");
        try
        {
            final Object objRtn =
                getSqlSession().selectList("com.huawei.devicecloud.platform.bi.odp.dao.dbServers.getDataDBServers");
            
            if (null == objRtn)
            {
                return new ArrayList<DBServerAddressEntity>(0);
            }
            else
            {
                return (List<DBServerAddressEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getDataDBServers failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 添加Token记录
     * @param token token
     * @param tokenExpiredTime 失效分钟�?
     * @return 是否�?
     * @throws CException 异常 
     */
    @Override
    public boolean addDialogToken(final Token token, final int tokenExpiredTime)
        throws CException
    {
        LOGGER.debug("Enter addDialogToken..., token is {}", token);
        try
        {
            //如果token为空，直接返回false
            if (null == token || null == token.getToken() || tokenExpiredTime < 0)
            {
                LOGGER.error("addDialogToken error! token is {},tokenExpiredTime is {}", token, tokenExpiredTime);
                return false;
            }
            
            final Map<String, Object> map = new HashMap<String, Object>(SMALL_SIZE);
            map.put("token", token.getToken());
            map.put("tokenExpiredTime", tokenExpiredTime);
            final int rows =
                getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.token.addDialogToken", map);
            
            if (rows > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("addDialogToken failed! token is {}!", token, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 查询未过期的Token记录
     * @param token token
     * @return Token实体
     * @throws CException 异常 
     */
    @Override
    public TokenEntity getVaildToken(final Token token)
        throws CException
    {
        LOGGER.debug("Enter getVaildToken. token is {}", token);
        try
        {
            //空token直接返回
            if (null == token || null == token.getToken())
            {
                return null;
            }
            
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao.token.getVaildToken",
                    token.getToken());
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                //不为空就强制转换
                return (TokenEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getVaildToken failed! token is {}", token, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除已经过期的Token记录
     * @throws CException 异常
     */
    @Override
    public void deleteInVaildToken()
        throws CException
    {
        LOGGER.debug("Enter deleteInVaildToken.");
        try
        {
            getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp.dao.token.deleteInvaildToken");
        }
        catch (Exception e)
        {
            LOGGER.error("deleteInVaildToken failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将loadCtrlValue加1
     * @param loadCtrlTd 阈值
     * @return 是否成功
     * @throws CException 异常
     */
    @Override
    public boolean incLoadControl(final int loadCtrlTd)
        throws CException
    {
        LOGGER.debug("Enter incLoadControl. loadCtrlThreshHold is {}", loadCtrlTd);
        try
        {
            final int rows =
                getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao.controlflag.incLoadControl",
                    loadCtrlTd);
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("incLoadControl failed! loadCtrlThreshHold is {}", loadCtrlTd, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将loadCtrlValue�?
     * @return 是否成功
     * @throws CException 异常
     */
    @Override
    public boolean decLoadControl()
        throws CException
    {
        LOGGER.debug("Enter dscLoadControl.");
        try
        {
            final int rows =
                getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao.controlflag.decLoadControl");
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("decLoadControl failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 初始化流控�?
     * @return 是否成功
     * @throws CException 异常
     */
    @Override
    public boolean initLoadCtrl()
        throws CException
    {
        LOGGER.debug("Enter initLoadCtrl.");
        try
        {
            final int rows =
                getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao.controlflag.initLoadCtrl");
            return rows > 0;
        }
        catch (Exception e)
        {
            LOGGER.error("initLoadCtrl failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取控制相关配置
     * @return 控制相关配置
     * @throws CException 异常 
     */
    @Override
    public ControlFlagEntity getControlFlag()
        throws CException
    {
        LOGGER.debug("Enter getControlFlag.");
        try
        {
            final Object objRtn =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao.controlflag.getControlFlag");
            if (null == objRtn)
            {
                return null;
            }
            else
            {
                //不为空就强制转换
                return (ControlFlagEntity)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getControlFlag failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取表路由信�?
     * @param tableName 表名
     * @param currentRTableId 当前路由表Id
     * @return  表路由信�?
     * @throws CException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RouteEntity> getRoutes(final String tableName, final int currentRTableId)
        throws CException
    {
        LOGGER.debug("Enter getRoutes. tableName is {}, currentRouteTableId is {}", tableName, currentRTableId);
        try
        {
            final Map<String, Object> map = new HashMap<String, Object>(SMALL_SIZE);
            map.put("tableName", tableName);
            map.put("currentRouteTableId", currentRTableId);
            final Object objRtn =
                getSqlSession().selectList("com.huawei.devicecloud.platform.bi.odp.dao.route.getRoutes", map);
            
            if (null == objRtn)
            {
                return new ArrayList<RouteEntity>(0);
            }
            else
            {
                return (List<RouteEntity>)objRtn;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getRoutes failed! tableName is {}, currentRouteTableId is {}", new Object[] {tableName,
                currentRTableId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 批量插入记录
     * @param tableName 表名
     * @param cfMappings 表列对象字段映射数组
     * @param records 记录
     * @exception CException 异常
     */
    @Override
    public void batchInsertRecords(final String tableName, final ColumnFieldMapping[] cfMappings,
        final List<UserProfileEntity> records)
        throws CException
    {
        LOGGER.debug("Enter batchInsertRecord... tableName is {}, cfMappings is {}", new Object[] {tableName,
            cfMappings});
        if (StringUtils.isEmpty(tableName) || null == records || records.isEmpty() || null == cfMappings
            || 0 == cfMappings.length)
        {
            //为空直接返回
            LOGGER.error("parameters is empty! tableName is {}, cfMappings is {}.",
                new Object[] {tableName, cfMappings});
            return;
        }
        
        final String columnNames = getCloumnNames(cfMappings);
        
        final Method[] getMethods = getGetMethods(cfMappings);
        
        final StringBuilder valuesSB = new StringBuilder();
        Object value = null;
        
        //拼接value
        for (UserProfileEntity userProfile : records)
        {
            valuesSB.append('(');
            for (int i = 0; i < cfMappings.length; i++)
            {
                try
                {
                    value = getMethods[i].invoke(userProfile);
                }
                catch (Exception e)
                {
                    LOGGER.error("{}({}) fail!", new Object[] {getMethods[i].getName(), userProfile, e});
                }
                
                //string类型加行引号
                if (value instanceof String || value instanceof Timestamp)
                {
                    valuesSB.append('\'');
                    valuesSB.append(value);
                    valuesSB.append('\'');
                }
                else
                {
                    valuesSB.append(value);
                }
                
                if (i != cfMappings.length - 1)
                {
                    valuesSB.append(',');
                }
            }
            valuesSB.append(')');
            valuesSB.append(',');
        }
        
        //删除最后的字符
        if (valuesSB.length() != 0)
        {
            valuesSB.deleteCharAt(valuesSB.length() - 1);
        }
        
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableName", tableName);
        map.put("columns", columnNames);
        map.put("values", valuesSB.toString());
        getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.batchInsertRecords", map);
    }
    
    //获取对象的字段�?获取方法数组
    private Method[] getGetMethods(final ColumnFieldMapping[] cfMappings)
        throws CException
    {
        PropertyDescriptor propertyDes = null;
        //初始化get方法数组
        Method[] getMethods = new Method[cfMappings.length];
        //获取字段的get方法
        for (int i = 0; i < cfMappings.length; i++)
        {
            try
            {
                propertyDes = new PropertyDescriptor(cfMappings[i].getFieldName(), UserProfileEntity.class);
                getMethods[i] = propertyDes.getReadMethod();
            }
            catch (IntrospectionException e)
            {
                LOGGER.error("batchInsertRecords failed!", e);
                throw new CException(ResultCode.WRITE_FILE_ERROR, e);
            }
        }
        return getMethods;
    }
    
    //获取以�?号分割的列名集合
    private String getCloumnNames(final ColumnFieldMapping[] cfMappings)
    {
        final StringBuilder columnsSB = new StringBuilder();
        for (int i = 0; i < cfMappings.length; i++)
        {
            columnsSB.append(cfMappings[i].getColumnName());
            if (i != cfMappings.length - 1)
            {
                columnsSB.append(',');
            }
        }
        return columnsSB.toString();
    }
    
    /**
     * 创建用户信息临时�?
     * @param tempTableName 临时表表名
     * @param columnFields 表列与对象字段映射数�?
     * @throws CException 异常
     */
    @Override
    public void createTempTable(final String tempTableName, final ColumnFieldMapping[] columnFields)
        throws CException
    {
        LOGGER.debug("Enter createTempTable. tableName is {}", tempTableName);
        try
        {
            if (null == columnFields || 0 == columnFields.length)
            {
                return;
            }
            
            final Map<String, Object> map = new HashMap<String, Object>(SMALL_SIZE);
            final List<String> columns = new ArrayList<String>(columnFields.length);
            for (int i = 0; i < columnFields.length; i++)
            {
                columns.add(columnFields[i].getColumnName());
            }
            
            map.put("tempTableName", tempTableName);
            map.put("columns", columns);
            getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.createTempTable", map);
        }
        catch (Exception e)
        {
            LOGGER.error("createTempTable failed! tableName is {}", new Object[] {tempTableName, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 更新预留信息
     * @param info 预留实体信息
     * @throws CException 异常
     * @return 是否成功
     */
    @Override
    public boolean updateReservedInfo(final ReservedInfoEntity info)
        throws CException
    {
        LOGGER.debug("Enter updateReservedInfo. info is {}", info);
        try
        {
            if (null == info)
            {
                return false;
            }
            
            final int rows =
                getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao.reserved.updateReservedInfo", info);
            return rows == 1;
        }
        catch (Exception e)
        {
            LOGGER.error("updateReservedInfo failed! info is {}", new Object[] {info, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 新增预留信息，获取预留Id
     * @param info 预留实体信息
     * @throws CException 异常
     * @return 预留Id
     */
    @Override
    public String addReservedInfo(final ReservedInfoEntity info)
        throws CException
    {
        LOGGER.debug("Enter addReservedInfo. info is {}", info);
        try
        {
            final int rows =
                getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.reserved.addReservedInfo", info);
            //插入成功
            if (1 == rows)
            {
                return info.getReserveId();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("addReservedInfo failed! info is {}", new Object[] {info, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        LOGGER.error("addReservedInfo failed! no row affected!");
        throw new CException(ResultCode.SYSTEM_ERROR, "addReservedInfo failed! no row affected!");
    }
    
    /**
     * 获取预留信息
     * @param reserveID 预留Id
     * @throws CException 异常
     * @return 预留Id
     */
    @Override
    public ReservedInfoEntity getReservedInfo(final String reserveID)
        throws CException
    {
        LOGGER.debug("Enter getReservedInfo. reserveID is {}", reserveID);
        try
        {
            final Object obj =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao.reserved.getReservedInfo",
                    reserveID);
            
            if (null == obj)
            {
                return null;
            }
            else
            {
                return (ReservedInfoEntity)obj;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getReservedInfo failed! reserveID is {}", new Object[] {reserveID, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 新增审计
     * @param info 审计实体
     * @throws CException 异常
     * @return 预留Id
     */
    @Override
    public boolean addAuditInfo(final AuditInfoEntity info)
        throws CException
    {
        LOGGER.debug("Enter addAuditInfo. info is {}", info);
        try
        {
            final int rows =
                getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.audit.addAuditInfo", info);
            //插入成功
            if (1 == rows)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("addAuditInfo failed! info is {}", new Object[] {info, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return false;
    }
    
    /**
     * 更新审计信息
     * @param info 审计实体
     * @throws CException 异常
     * @return 预留Id
     */
    @Override
    public boolean updateAuditInfo(final AuditInfoEntity info)
        throws CException
    {
        LOGGER.debug("Enter updateAuditInfo. info is {}", info);
        try
        {
            final int rows =
                getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp" + ".dao.audit.updateAuditInfo", info);
            //插入成功
            if (1 == rows)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("updateAuditInfo failed! info is {}", new Object[] {info, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        
        return false;
    }
    
    /**
     * 删除临时�?
     * @param tmpTableName 表明
     * @throws CException 异常
     */
    @Override
    public void dropTempTable(final String tmpTableName)
        throws CException
    {
        LOGGER.debug("Enter deleteTempTable. tmpTableName is {}", tmpTableName);
        try
        {
            final Map<String, String> map = new HashMap<String, String>(SMALL_SIZE);
            map.put("tmpTableName", tmpTableName);
            getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.deleteTempTable", map);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteTempTable failed! tmpTableName is {}", new Object[] {tmpTableName, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除预留信息
     * @param reserveID 预留�?
     * @return 是否成功
     * @throws CException 异常
     */
    @Override
    public boolean deleteReservedInfo(final String reserveID)
        throws CException
    {
        LOGGER.debug("Enter deleteTempTable. reserveID is {}", reserveID);
        try
        {
            final int rows =
                getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp.dao.reserved.deleteReservedInfo",
                    reserveID);
            if (1 == rows)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("deleteReservedInfo failed! reserveID is {}", new Object[] {reserveID, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        return false;
    }
    
    /**
     * 获取已经过去的预留信息
     * @return 获取已经过期的预留信息
     * @throws CException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservedInfoEntity> getAllExpiredReservedInfos()
        throws CException
    {
        LOGGER.debug("Enter getAllExpiredReservedInfos.");
        try
        {
            final Object rtnObj =
                getSqlSession().selectList("com.huawei.devicecloud.platform.bi.odp.dao"
                    + ".reserved.getAllExpiredReservedInfos");
            if (null == rtnObj)
            {
                return new ArrayList<ReservedInfoEntity>(0);
            }
            else
            {
                return (List<ReservedInfoEntity>)rtnObj;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getAllExpiredReservedInfos failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取表的�?��用户信息记录集合
     * @return 获取表的�?��用户信息记录集合
     * @param tableName 表名
     * @throws CException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserProfileEntity> getUPRecords(final String tableName)
        throws CException
    {
        LOGGER.debug("Enter getUPRecords.");
        try
        {
            final Map<String, String> map = new HashMap<String, String>(SMALL_SIZE);
            map.put("tableName", tableName);
            final Object rtnObj =
                getSqlSession().selectList("com.huawei.devicecloud.platform.bi.odp.dao" + ".userprofile.getUPRecords",
                    map);
            if (null == rtnObj)
            {
                return new ArrayList<UserProfileEntity>(0);
            }
            else
            {
                return (List<UserProfileEntity>)rtnObj;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getUPRecords failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将文件中的数据加载到指定表中
     * @param fileName 文件名
     * @param tableName 表名
     * @param columns 列名数组
     * @throws CException 异常
     */
    @Override
    public void loadFile2Table(final String fileName, final String tableName, final List<String> columns)
        throws CException
    {
        LOGGER.debug("Enter loadFile2Table.");
        try
        {
            final Map<String, Object> map = new HashMap<String, Object>(SMALL_SIZE);
            //在mysql中需要继续转义
            map.put("fileName", fileName.replace("\\", "\\\\"));
            map.put("tableName", tableName);
            map.put("columns", columns);
            map.put("fc", '\001');
            getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao" + ".userprofile.loadfile2table", map);
        }
        catch (Exception e)
        {
            LOGGER.error("loadFile2Table failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 将文件中的数据加载到指定表中
     * @param fileName 文件名
     * @param tableName 表名
     * @throws CException 异常
     */
    @Override
    public void loadFile2TRT(final String fileName, final String tableName)
        throws CException
    {
        LOGGER.debug("Enter loadFile2TmpResultTable.");
        try
        {
            final Map<String, Object> map = new HashMap<String, Object>(SMALL_SIZE);
            //在mysql中需要继续转义
            map.put("fileName", fileName.replace("\\", "\\\\"));
            map.put("tableName", tableName);
            map.put("fc", '\001');
            getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao" + ".userprofile.loadFile2TRT", map);
        }
        catch (Exception e)
        {
            LOGGER.error("loadFile2TmpResultTable failed!", e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 创建Tmp_Result_Table表（查询表）
     * @param tableName 表名
     * @throws CException 异常
     */
    @Override
    public void createTRT(final String tableName)
        throws CException
    {
        LOGGER.debug("Enter createTRT. tableName is {}", tableName);
        try
        {
            Map<String, String> table = new HashMap<String, String>(SMALL_SIZE);
            table.put("tableName", tableName);
            getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.createTRT", table);
        }
        catch (Exception e)
        {
            LOGGER.error("createTRT failed! tableName is {}", new Object[] {tableName, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 创建Common_Result_Table表（周表）
     * @param tableName 表名
     * @throws CException 异常
     */
    @Override
    public void createCRT(final String tableName)
        throws CException
    {
        LOGGER.debug("Enter createCRT. tableName is {}", tableName);
        try
        {
            Map<String, String> table = new HashMap<String, String>(SMALL_SIZE);
            table.put("tableName", tableName);
            getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.createCRT", table);
        }
        catch (Exception e)
        {
            LOGGER.error("createCRT failed! tableName is {}", new Object[] {tableName, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 准备分组数据（过滤每个分组的imei号）
     * @param param 参数
     * @throws CException 异常
     */
    @Override
    public void prepareGroup(final GroupUserFilterParam param)
        throws CException
    {
        LOGGER.debug("Enter prepareGroup. param is {}", param);
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for (String date : param.getNeighborDates())
            {
                sb.append('\'');
                sb.append(date);
                sb.append('\'');
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(')');
            param.setNeiDatas(sb.toString());
            getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.prepareGroup", param);
        }
        catch (Exception e)
        {
            LOGGER.error("prepareGroup failed! param is {}", new Object[] {param, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 准备分组数据（仅查询，过滤每个分组的imei号）
     * @param param 参数
     * @return 返回查询记录总数
     * @throws CException 异常
     */
    @Override
    public Long prepareGroupQuery(final GroupUserFilterParam param)
        throws CException
    {
        LOGGER.debug("Enter prepareGroupQuery. param is {}", param);
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for (String date : param.getNeighborDates())
            {
                sb.append('\'');
                sb.append(date);
                sb.append('\'');
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(')');
            param.setNeiDatas(sb.toString());
            Long destableCount = new Long(0L);
            param.setDestTableCount(destableCount);
            getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.prepareGroupQuery",
                param);
            return param.getDestTableCount();
        }
        catch (Exception e)
        {
            LOGGER.error("prepareGroupQuery failed! param is {}", new Object[] {param, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除trt表
     * @param trt tmp_result_flag表
     * @throws CException 异常
     */
    @Override
    public void dropTRT(final String trt)
        throws CException
    {
        LOGGER.debug("Enter dropTRT. trt is {}", trt);
        try
        {
            Map<String, String> table = new HashMap<String, String>(SMALL_SIZE);
            table.put("trt", trt);
            getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.dropTRT", table);
        }
        catch (Exception e)
        {
            LOGGER.error("dropTRT failed! trt is {}", new Object[] {trt, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取trt表中指定destTable的记录总数
     * @param trt tmp_result_flag表
     * @param destTable 目标表
     * @throws CException 异常
     * @return trt表中指定destTable的记录总数
     */
    @Override
    public Long getTRTCount(final String trt, final String destTable)
        throws CException
    {
        LOGGER.debug("Enter getTRTCount. trt is {}, destTable is {}", trt, destTable);
        try
        {
            Map<String, String> table = new HashMap<String, String>(SMALL_SIZE);
            table.put("trt", trt);
            table.put("destTable", destTable);
            Object objRtn =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.getTRTCount", table);
            if (null != objRtn)
            {
                return (Long)objRtn;
            }
            else
            {
                return 0L;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getTRTCount failed! trt is {}, destTable is {}", new Object[] {trt, destTable, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除crt表
     * @param crt common_result_flag表
     * @throws CException 异常
     */
    @Override
    public void dropCRT(final String crt)
        throws CException
    {
        LOGGER.debug("Enter dropCRT. crt is {}", crt);
        try
        {
            Map<String, String> table = new HashMap<String, String>(SMALL_SIZE);
            table.put("crt", crt);
            getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.dropCRT", table);
        }
        catch (Exception e)
        {
            LOGGER.error("dropCRT failed! crt is {}", new Object[] {crt, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除crt表中reserveId相关记录
     * @param crt common_result_flag表
     * @param reserveId 预留Id
     * @throws CException 异常
     */
    @Override
    public void deleteCRT(final String crt, final String reserveId)
        throws CException
    {
        LOGGER.debug("Enter deleteCRT. crt is {}, reserveId is {}", crt, reserveId);
        try
        {
            Map<String, Object> table = new HashMap<String, Object>(SMALL_SIZE);
            table.put("crt", crt);
            table.put("reserveId", Long.parseLong(reserveId));
            getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.deleteCRT", table);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteCRT failed! crt is {}, reserveId is {}", new Object[] {crt, reserveId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取crt表中的记录总数
     * @param crt crt表
     * @return crt表中的记录总数
     */
    @Override
    public Long getCRTCount(final String crt)
    {
        LOGGER.debug("Enter getCRTCount. crt is {}", crt);
        try
        {
            Map<String, String> table = new HashMap<String, String>(SMALL_SIZE);
            table.put("crt", crt);
            Object objRtn =
                getSqlSession().selectOne("com.huawei.devicecloud.platform.bi.odp.dao.userprofile.getCRTCount", table);
            if (null != objRtn)
            {
                return (Long)objRtn;
            }
            else
            {
                return 0L;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getCRTCount failed! crt is {}", new Object[] {crt, e});
        }
        
        return 0L;
    }

    @Override
    public void insertFileUploadTableInfo(FileUploadTableEntity entity)
        throws CException
    {
        LOGGER.debug("Enter insertFileUploadTableInfo. FileUploadTableEntity is {}", entity);
        try
        {
            getSqlSession().insert("com.huawei.devicecloud.platform.bi.odp.dao.fileupload.insertFileUploadTableInfo",
                entity);
        }
        catch (Exception e)
        {
            LOGGER.error("insertFileUploadTableInfo failed! entity is {}, exception is {}", new Object[] {entity, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }

    @Override
    public void updateFileUpLoadTableInfo(FileUploadTableEntity entity)
        throws CException
    {
        LOGGER.debug("Enter updateFileUpLoadTableInfo. FileUploadTableEntity is {}", entity);
        try
        {
            getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao.fileupload.updateFileUpLoadTableInfo",
                entity);
        }
        catch (Exception e)
        {
            LOGGER.error("updateFileUpLoadTableInfo failed! entity is {}, exception is {}", new Object[] {entity, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }

    @Override
    public FileUploadTableEntity getFileUploadTableInfo(String fileId)
        throws CException
    {
        LOGGER.debug("Enter getFileUploadTableInfo. fileId is {}", fileId);
        try
        {
            FileUploadTableEntity entity = (FileUploadTableEntity)getSqlSession()
                .selectOne("com.huawei.devicecloud.platform.bi.odp.dao.fileupload.getFileUploadTableInfo", fileId);
            return entity;
        }
        catch (Exception e)
        {
            LOGGER.error("getFileUploadTableInfo failed! fileId is {}, exception is {}", new Object[] {fileId, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getFileIdList(int days)
        throws CException
    {
        LOGGER.debug("Enter getFileIdList. save days is {}", days);
        try
        {
            List<String> list = getSqlSession().selectList("com.huawei.devicecloud.platform.bi.odp."
                + "dao.fileupload.getFileIdList",days);
            
            LOGGER.info("file id list is {}",new Object[]{list});
            return list;
        }
        catch (Exception e)
        {
            LOGGER.error("getFileIdList failed! save days is {}, exception is {}", new Object[] {days, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }

    @Override
    public void delFileInfo(List<String> fileIdList)
        throws CException
    {
        LOGGER.debug("Enter delFileInfo. fileIdList is {}", fileIdList);
        try
        {
            getSqlSession().delete("com.huawei.devicecloud.platform.bi.odp."
                + "dao.fileupload.delFileInfo",fileIdList);
        }
        catch (Exception e)
        {
            LOGGER.error("delFileInfo failed! fileIdList is {}, exception is {}", new Object[] {fileIdList, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }

    @Override
    public List<UserProfileEntity> getFinalRecords(String tableName) throws CException
    {
        LOGGER.debug("getFinalRecords begin,tableName is {}", tableName);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<UserProfileEntity> list = new ArrayList<UserProfileEntity>();
        try
        {
            conn = getMysqlBDS().getConnection();
            stmt = conn.createStatement();
            String hql = "select device_id from " + tableName;
            rs = stmt.executeQuery(hql);
            
            if (null != rs)
            {
                while (rs.next())
                {
                    UserProfileEntity entity = new UserProfileEntity();
                    String deviceID = rs.getString("device_id");
                    
                    entity.setDeviceId(deviceID);
                    list.add(entity);
                }
            }
            return list;
        }
        catch (SQLException e)
        {
            LOGGER.error("getFinalRecords fail,tableName is {}", tableName, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
    }
    
    
}
