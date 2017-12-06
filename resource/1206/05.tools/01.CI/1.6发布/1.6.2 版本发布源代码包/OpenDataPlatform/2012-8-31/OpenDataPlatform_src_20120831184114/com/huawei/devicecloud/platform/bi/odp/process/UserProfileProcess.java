/*
 * 文 件 名:  UserProfileProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  用户信息处理
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.constants.type.ReservedStateType;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.dao.impl.OdpDaoImpl;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldTypeMapping;
import com.huawei.devicecloud.platform.bi.odp.entity.ControlFlagEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.DBServerAddressEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ReservedInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.RouteEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;

/**
 * 用户信息处理
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class UserProfileProcess
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileProcess.class);
    
    /**
     * Dao层调用对象
     */
    private OdpDao odpDao;
    
    /**
     * 获取当前的路由表Id
     * @return 当前的路由表Id
     * @throws CException 异常
     */
    public int getCurrentRouteTable()
        throws CException
    {
        return getControlFlag().getCurrentRouteTable();
    }
    
    /**
     * 获取当前有效的服务器Id（真正执行routine的服务器节点）
     * @return 有效的服务器Id
     * @throws CException 异常
     */
    public int getCurrentVaildServer()
        throws CException
    {
        return getControlFlag().getCurrentServerID();
    }
    
    /**
     * 获取当前的用户信息表ID
     * @return 当前的用户信息表ID
     * @throws CException 异常
     */
    public int getCurrentUserProfile()
        throws CException
    {
        return getControlFlag().getCurrentTableID();
    }
    
    /**
     * 获取控制相关信息，包含当前有效服务器Id，当前路由表Id，当前用户信息表Id等
     * @return 控制相关信息
     * @throws CException 异常
     */
    public ControlFlagEntity getControlFlag()
        throws CException
    {
        //获取控制相关配置信息
        ControlFlagEntity controlFlag = odpDao.getControlFlag();
        
        if (null == controlFlag)
        {
            LOGGER.error("don't exist ControlFlag config!");
            throw new CException(ResultCode.CONTROL_FLAG_NOT_EXIST, "don't exist control flag config!");
        }
        
        return controlFlag;
    }
    
    /** 
     * 获取表路由信息
     * @param currRouteTableID 当前路由表ID
     * @param tableName 表名
     * @return 表路由信息
     * @throws CException 异常
     */
    public List<RouteEntity> getRoutes(Integer currRouteTableID, String tableName)
        throws CException
    {
        //获取路由信息
        List<RouteEntity> tableRoutes = odpDao.getRoutes(tableName, currRouteTableID);
        if (null == tableRoutes || tableRoutes.isEmpty())
        {
            LOGGER.error("routes for table[{}] is empty!", tableName);
            throw new CException(ResultCode.SYSTEM_ERROR, String.format("routes for table[%s] is empty!", tableName));
        }
        return tableRoutes;
    }
    
    /**
     * 回收资源
     * @param appId 应用标识
     * @param reserveId 预留ID
     * @throws CException 异常
     */
    public void reclaimResource(String appId, String reserveId)
        throws CException
    {
        //获取预留信息
        ReservedInfoEntity rservedInfo = getReservedInfo(reserveId);
        if (null != rservedInfo)
        {
            if (rservedInfo.getState().equals(ReservedStateType.FINISH))
            {
                //只能回收本应用的数据资源
                if (appId.equals(rservedInfo.getCreateAppId()))
                {
                    //删除数据临时表
                    dropTempTable(rservedInfo.getTmpTableName());
                    //删除数据文件
                    OdpCommonUtils.deleteFile(rservedInfo.getFileUrl());
                    //删除预留信息
                    deleteReservedInfo(reserveId);
                    LOGGER.info("reclaim resource[{}] successfully! appId is {}", rservedInfo, appId);
                }
                else
                {
                    LOGGER.error("reclaimResource failed! only allow delete own's reserveInfo."
                        + "reserveId is {}, appId is {},createAppId is {}",
                        new Object[] {reserveId, appId, rservedInfo.getCreateAppId()});
                    throw new CException(ResultCode.NO_ENOUGH_PREVILEGE);
                }
            }
            else
            {
                LOGGER.error("reclaimResource failed! reserve data has not been fininshed.reserveId is {}", reserveId);
                throw new CException(ResultCode.RESERVE_NO_END);
            }
        }
    }
    
    /**
     * 回收资源
     * @param rservedInfo 预留信息
     * @throws CException 异常
     */
    public void reclaimResource(ReservedInfoEntity rservedInfo)
        throws CException
    {
        //为空直接退出
        if (null != rservedInfo)
        {
            //删除数据临时表
            dropTempTable(rservedInfo.getTmpTableName());
            //删除数据文件
            OdpCommonUtils.deleteFile(rservedInfo.getFileUrl());
            //删除预留信息
            deleteReservedInfo(rservedInfo.getReserveId());
        }
    }
    
    /**
     * 获取文件对象
     * @param reserveId 预留ID
     * @param resultMapID 结果集映射ID
     * @throws CException 异常
     * @return 获取文件对象
     */
    public File getFile(String reserveId, String resultMapID)
        throws CException
    {
        //获取预留信息
        ReservedInfoEntity rservedInfo = getReservedInfo(reserveId);
        if (null == rservedInfo)
        {
            //预留数据信息不存在
            LOGGER.error("reserve[{}] not exist!", reserveId);
            throw new CException(ResultCode.RESERVE_NO_EXIST, reserveId);
        }
        
        if (!rservedInfo.getState().equals(ReservedStateType.FINISH))
        {
            //预留数据操作没有结束，不能获取数据
            LOGGER.error("getFile failed! reserve data has not been fininshed. reserveId is {}", reserveId);
            throw new CException(ResultCode.RESERVE_NO_END);
        }
        
        String fileName = rservedInfo.getFileUrl();
        if (StringUtils.isBlank(fileName))
        {
            //文件不应该不存在
            LOGGER.error("reserve[{}] has not file!", reserveId);
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        
        try
        {
            //获取文件
            File file = new File(fileName);
            if (file.exists())
            {
                return file;
            }
            else
            {
                //创建读取文件
                List<UserProfileEntity> records = getUPRecords(rservedInfo.getTmpTableName());
                //获取列名数组
                String[] columnNames = rservedInfo.getColumnNameList().split(",");
                //获取表列与字段的映射数组
                ColumnFieldTypeMapping[] cfMappings = getChoosedCFTMappings(resultMapID, columnNames);
                //初始化记录写文件对象
                IRecords2File<UserProfileEntity> records2File =
                    new Records2TxtZipFile<UserProfileEntity>(rservedInfo.getFileUrl(), false, cfMappings);
                //将记录写入到压缩文件中
                records2File.writeRecords2File(records);
                
                //重新读取文件
                File fileC = new File(fileName);
                if (fileC.exists())
                {
                    return fileC;
                }
                else
                {
                    //文件不应该不存在
                    LOGGER.error("write file again,but reserve[{}] has not file!", reserveId);
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
        }
        catch (SecurityException e)
        {
            LOGGER.error("getFile failed! fileName is {}", fileName, e);
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 根据columnNames获取表列与对象字段映射数组
     * @param resultMapId 结果集映射ID
     * @param columnNames 列名数组
     * @return 表列与对象字段映射数组
     */
    public ColumnFieldTypeMapping[] getChoosedCFTMappings(String resultMapId, String[] columnNames)
    {
        //不合法直接返回
        if (null == columnNames)
        {
            return null;
        }
        
        //表列与对象字段映射数组
        ColumnFieldTypeMapping[] cfTMappings = new ColumnFieldTypeMapping[columnNames.length];
        if (null != odpDao)
        {
            //获取结果集Map
            ResultMap resultMap = ((OdpDaoImpl)odpDao).getSqlSession().getConfiguration().getResultMap(resultMapId);
            List<ResultMapping> resultMappings = resultMap.getResultMappings();
            
            if (null == resultMappings)
            {
                LOGGER.error("resultMappings can't be null, TempTableRecordProcess can't work normally!");
                return null;
            }
            
            //从resultMap中获取列名与字段的映射关系
            ColumnFieldTypeMapping cfTMapping = null;
            for (int i = 0; i < columnNames.length; i++)
            {
                for (ResultMapping resultMapping : resultMappings)
                {
                    //过滤指定的列
                    if (columnNames[i].equals(resultMapping.getColumn()))
                    {
                        cfTMapping = new ColumnFieldTypeMapping();
                        cfTMapping.setColumnName(columnNames[i]);
                        cfTMapping.setFieldName(resultMapping.getProperty());
                        cfTMapping.setJavaType(resultMapping.getJavaType());
                        cfTMappings[i] = cfTMapping;
                        break;
                    }
                }
            }
            
        }
        
        return cfTMappings;
    }
    
    /**
     * 获取所有的表列与对象字段映射数组
     * @param resultMapId 结果集映射ID
     * @return 表列与对象字段映射数组
     */
    public ColumnFieldTypeMapping[] getAllCFTMappings(String resultMapId)
    {
        ColumnFieldTypeMapping[] cfTMappings = null;
        
        if (null != odpDao)
        {
            //获取结果集Map
            ResultMap resultMap = ((OdpDaoImpl)odpDao).getSqlSession().getConfiguration().getResultMap(resultMapId);
            List<ResultMapping> resultMappings = resultMap.getResultMappings();
            
            if (null == resultMappings)
            {
                LOGGER.error("resultMappings can't be null, TempTableRecordProcess can't work normally!");
                return null;
            }
            
            cfTMappings = new ColumnFieldTypeMapping[resultMappings.size()];
            //从resultMap中获取列名与字段的映射关系
            ColumnFieldTypeMapping cfTMapping = null;
            for (int i = 0; i < cfTMappings.length; i++)
            {
                cfTMapping = new ColumnFieldTypeMapping();
                cfTMapping.setColumnName(resultMappings.get(i).getColumn());
                cfTMapping.setFieldName(resultMappings.get(i).getProperty());
                cfTMapping.setJavaType(resultMappings.get(i).getJavaType());
                cfTMappings[i] = cfTMapping;
            }
            
        }
        
        return cfTMappings;
    }
    
    /**
     * 获取表的所有用户信息记录集合
     * @return 获取表的所有用户信息记录集合
     * @param tableName 表名
     * @throws CException 异常
     */
    public List<UserProfileEntity> getUPRecords(String tableName)
        throws CException
    {
        return odpDao.getUPRecords(tableName);
    }
    
    /**
     * 获取预留信息
     * @param reserveID 预留号
     * @throws CException 异常
     * @return 预留Id
     */
    public ReservedInfoEntity getReservedInfo(String reserveID)
        throws CException
    {
        return odpDao.getReservedInfo(reserveID);
    }
    
    /**
     * 获取所有过期的预留信息
     * @return 所有过期的预留信息
     * @throws CException 异常
     */
    public List<ReservedInfoEntity> getAllExpiredReservedInfos()
        throws CException
    {
        return odpDao.getAllExpiredReservedInfos();
    }
    
    /**
     * 更新预留信息
     * @param info 预留信息
     * @throws CException 异常
     */
    public void updateReservedInfo(ReservedInfoEntity info)
        throws CException
    {
        odpDao.updateReservedInfo(info);
    }
    
    /**
     * 创建用户信息临时表
     * @param tempTableName 临时表表名
     * @param columnFields 表列与对象字段映射数组
     * @throws CException 异常
     */
    public void createTempTable(String tempTableName, ColumnFieldMapping[] columnFields)
        throws CException
    {
        odpDao.createTempTable(tempTableName, columnFields);
    }
    
    /**
     * 删除预留信息
     * @param reserveID 预留号
     * @return 是否成功
     * @throws CException 异常
     */
    public boolean deleteReservedInfo(String reserveID)
        throws CException
    {
        return odpDao.deleteReservedInfo(reserveID);
    }
    
    /**
     * 删除临时表
     * @param tmpTableName 表明
     * @throws CException 异常
     */
    public void dropTempTable(String tmpTableName)
        throws CException
    {
        if (StringUtils.isEmpty(tmpTableName))
        {
            return;
        }
        
        odpDao.dropTempTable(tmpTableName);
    }
    
    /**
     * 获取数据服务器连接信息列表
     * @return 数据服务器连接信息列表
     * @throws CException 异常
     */
    public List<DBServerAddressEntity> getDBServerAddressInfo()
        throws CException
    {
        return this.odpDao.getDataDBServers();
    }
    
    /**
     * 申请数据查询资源，失败抛出异常；
     * 
     * @param threshold 阈值
     * @throws CException 流控超限异常
     * 
     */
    public void applyDataQuery(int threshold)
        throws CException
    {
        boolean applySuccessed = odpDao.incLoadControl(threshold);
        //已经到达流控上线
        if (!applySuccessed)
        {
            LOGGER.info("flow control limit!");
            throw new CException(ResultCode.FLOW_CONTROL_LIMIT, "flow control limit!");
        }
    }
    
    /**
     * 初始化流控值
     * @throws CException 异常
     */
    public void initLoadCtrl()
        throws CException
    {
        if (odpDao.initLoadCtrl())
        {
            //初始化流控值成功
            LOGGER.info("init the loadCtrlValue(0) sucessfully!");
        }
        else
        {
            //初始化流控值失败
            LOGGER.error("init the loadCtrlValue(0) failed!");
        }
        
    }
    
    /**
     * 释放数据查询资源
     */
    public void releaseDataQuery()
    {
        try
        {
            //将流量值减1
            odpDao.decLoadControl();
        }
        catch (Exception e)
        {
            LOGGER.error("releaseDataQuery failed!", e);
        }
    }
    
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    public void setOdpDao(OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
}
