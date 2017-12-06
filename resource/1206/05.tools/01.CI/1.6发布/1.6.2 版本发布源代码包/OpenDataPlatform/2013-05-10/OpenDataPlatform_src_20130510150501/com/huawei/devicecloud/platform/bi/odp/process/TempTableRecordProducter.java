/*
 * 文 件 名:  TempTableRecordProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-11
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.dao.impl.OdpDaoImpl;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldTypeMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.RecordQueue;
import com.huawei.devicecloud.platform.bi.odp.domain.TempTableRecordProcessParam;

/**
 * 临时表记录处理器
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-11]
 * @param <T> 泛型类型
 */
public class TempTableRecordProducter<T> implements IResultSetProcess, IProducter
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TempTableRecordProducter.class);
    
    //记录队列
    private RecordQueue<T> recordQueue;
    
    //记录类型对象
    private Class<? extends Object> recordClass;
    
    //结果集映射ID
    private String resultMapID;
    
    //Dao层调用对象
    private OdpDao odpDao;
    
    //set方法数组
    private Method[] setMethods;
    
    //列字段类型映射数组
    private ColumnFieldTypeMapping[] columnFieldTypes;
    
    //结果映射列表
    private List<ResultMapping> resultMappings;
    
    //记录的构造函数对象
    @SuppressWarnings("rawtypes")
    private Constructor recordConstructor;
    
    /**
     * 默认构造函数
     * @param param 参数
     */
    public TempTableRecordProducter(TempTableRecordProcessParam<T> param)
    {
        this.columnFieldTypes = param.getColumnFieldTypes();
        
        this.resultMapID = param.getResultMapID();
        
        this.recordClass = param.getRecordClass();
        
        this.recordQueue = param.getRecordQueue();
        
        this.odpDao = param.getOdpDao();
        
        //初始化构造函数、set字段方法数组
        initialize();
    }
    
    //初始化resultMap，记录构造函数，字段方法
    private void initialize()
    {
        OdpDaoImpl odpDaoImpl = (OdpDaoImpl)odpDao;
        if (null == odpDaoImpl)
        {
            LOGGER.error("odpDaoImpl can't be null, TempTableRecordProcess can't work normally!");
            return;
        }
        
        //获取mapping文件中定义的resultMap
        ResultMap resultMap = odpDaoImpl.getSqlSession().getConfiguration().getResultMap(resultMapID);
        resultMappings = resultMap.getResultMappings();
        
        if (null == resultMappings)
        {
            LOGGER.error("resultMappings can't be null, TempTableRecordProcess can't work normally!");
            return;
        }
        
        try
        {
            setMethods = new Method[columnFieldTypes.length];
            
            //初始化构造函数
            this.recordConstructor = recordClass.getConstructor();
            
            StringBuilder setMethodNameSB = null;
            for (int i = 0; i < columnFieldTypes.length; i++)
            {
                //构造set方法名
                setMethodNameSB = new StringBuilder();
                setMethodNameSB.append("set");
                setMethodNameSB.append(columnFieldTypes[i].getFieldName().substring(0, 1).toUpperCase(Locale.ENGLISH));
                setMethodNameSB.append(columnFieldTypes[i].getFieldName().substring(1));
                //构造属性方法键值对
                setMethods[i] = recordClass.getMethod(setMethodNameSB.toString(), columnFieldTypes[i].getJavaType());
            }
        }
        catch (SecurityException e)
        {
            //记录错误信息
            LOGGER.error("initialize failed! TempTableRecordProcess can't work normally!");
        }
        catch (NoSuchMethodException e)
        {
            //记录错误信息
            LOGGER.error("initialize failed! TempTableRecordProcess can't work normally!");
        }
        catch (IllegalArgumentException e)
        {
            //记录错误信息
            LOGGER.error("initialize failed! TempTableRecordProcess can't work normally!");
        }
    }
    
    /**
     * 处理结果集
     * @param rs 结果集
     * @return 是否结束后续的结果集处理
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean processResultSet(ResultSet rs)
    {
        try
        {
            //结果集未关闭
            if (null != rs && !rs.isClosed())
            {
                T recordObj = null;
                try
                {
                    //构造记录对象
                    recordObj = (T)recordConstructor.newInstance();
                    
                    for (int i = 0; i < setMethods.length; i++)
                    {
                        setMethods[i].invoke(recordObj, rs.getObject(columnFieldTypes[i].getColumnName()));
                    }
                }
                catch (IllegalArgumentException e)
                {
                    //参数不合法异常
                    LOGGER.error("Method.invoke failed! recordObj is {}", recordObj, e);
                }
                catch (IllegalAccessException e)
                {
                    //非法访问异常
                    LOGGER.error("Method.invoke failed! recordObj is {}", recordObj, e);
                }
                catch (InvocationTargetException e)
                {
                    //调用异常
                    LOGGER.error("Method.invoke failed! recordObj is {}", recordObj, e);
                }
                catch (InstantiationException e)
                {
                    //实例化异常
                    LOGGER.error("Method.invoke failed! rs is {}", rs, e);
                }
                
                //如果超过记录数限制，直接退出
                if (recordQueue.add(recordObj))
                {
                    return true;
                }
            }
        }
        catch (SQLException e)
        {
            LOGGER.error("processResultSet failed! rs is {}", rs, e);
        }
        
        //默认是继续处理后面的结果集
        return false;
    }
}
