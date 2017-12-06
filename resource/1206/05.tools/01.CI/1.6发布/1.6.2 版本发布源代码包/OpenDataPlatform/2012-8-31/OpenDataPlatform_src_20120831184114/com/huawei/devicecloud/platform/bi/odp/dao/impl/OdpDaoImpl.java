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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
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
     * 更新临时表失效时�?
     * @param reserveID 预留ID
     * @param days 预留天数
     * @return 是否成功
     * @throws CException 异常
     */
    @Override
    public boolean updateReservedExpiredTime(final String reserveID, final int days)
        throws CException
    {
        LOGGER.debug("Enter updateReservedExpiredTime. reservedID is {},days is {}", reserveID, days);
        try
        {
            final Map<String, Object> map = new HashMap<String, Object>(SMALL_SIZE);
            map.put("reserveID", reserveID);
            map.put("days", days);
            final int rows =
                getSqlSession().update("com.huawei.devicecloud.platform.bi.odp.dao.reserved.updateReservedExpiredTime",
                    map);
            //插入成功
            return 1 == rows;
        }
        catch (Exception e)
        {
            LOGGER.error("updateReservedExpiredTime failed! reservedID is {},days is {}", new Object[] {reserveID,
                days, e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取预留信息
     * @param reserveID 预留�?
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
     * 获取�?��过期的预留信�?
     * @return �?��过期的预留信�?
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
}
