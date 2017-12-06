/*
 * 文 件 名:  CommonUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  数据开发平台公共工具类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * 数据开发平台公共工具类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class OdpCommonUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpCommonUtils.class);
    
    private static final int BUFFER_SIZE = 1024;
    
    /**
     * 私有的构造方法
     */
    private OdpCommonUtils()
    {
        
    }
    
    /**
     * 通过record对象获取所有的属性的get方法
     * @param record 记录对象
     * @param columnFields 列名字段名映射关系数组
     * @return 所有的属性的get方法
     * @throws CException 异常
     */
    public static Method[] getFieldsGetMethod(Object record, ColumnFieldMapping[] columnFields)
        throws CException
    {
        if (null == record || null == columnFields || 0 == columnFields.length)
        {
            return null;
        }
        
        Method[] getMethods = new Method[columnFields.length];
        PropertyDescriptor pd = null;
        //获取字段值
        for (int i = 0; i < columnFields.length; i++)
        {
            try
            {
                //构造属性描述符
                pd = new PropertyDescriptor(columnFields[i].getFieldName(), record.getClass());
                //获取get方法
                getMethods[i] = pd.getReadMethod();
            }
            catch (IntrospectionException e)
            {
                LOGGER.error("writeRecords2File failed!", e);
                throw new CException(ResultCode.WRITE_FILE_ERROR, e);
            }
        }
        return getMethods;
    }
    
    /**
     * 压缩产生的目标文件名
     * @param srcFileName 源文件名
     * @param dstFileName 目标文件名
     * @throws CException 异常
     */
    public static void compress(String srcFileName, String dstFileName)
        throws CException
    {
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        try
        {
            //输出文件
            File outfile = new File(dstFileName);
            //创建zip文件
            CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(outfile), new CRC32());
            zos = new ZipOutputStream(cos);
            
            //获取输入文件名
            File inputfile = new File(srcFileName);
            ZipEntry entry = new ZipEntry(outfile.getName());
            //创建压缩包中的文件名
            zos.putNextEntry(entry);
            bis = new BufferedInputStream(new FileInputStream(inputfile));
            int count = 0;
            byte[] data = new byte[BUFFER_SIZE];
            //将文件流输出到压缩文件流中
            while (true)
            {
                count = bis.read(data, 0, BUFFER_SIZE);
                if (-1 == count)
                {
                    break;
                }
                
                zos.write(data, 0, count);
            }
        }
        catch (FileNotFoundException e)
        {
            //文件不存在异常
            LOGGER.error("compress failed! srcFileName is {},dstFileName is {}", new Object[] {srcFileName,
                dstFileName, e});
            throw new CException(ResultCode.WRITE_FILE_ERROR, new Object[] {srcFileName, dstFileName, e});
        }
        catch (IOException e)
        {
            //IO读写异常
            LOGGER.error("compress failed! srcFileName is {},dstFileName is {}", new Object[] {srcFileName,
                dstFileName, e});
            throw new CException(ResultCode.WRITE_FILE_ERROR, new Object[] {srcFileName, dstFileName, e});
        }
        finally
        {
            //关闭输入流
            OdpCommonUtils.close(bis);
            
            //关闭输出流
            OdpCommonUtils.close(zos);
        }
    }
    
    /**
     * 获取不存在的列名数组
     * @param columnEnums 列枚举列表
     * @return 不存在列数组
     * @throws CException 异常
     */
    public static List<Integer> getNotExistColumns(List<Integer> columnEnums)
        throws CException
    {
        //列表为空直接返回
        if (null == columnEnums || columnEnums.isEmpty())
        {
            return null;
        }
        
        //不存在列集合
        List<Integer> notExistColumns = new ArrayList<Integer>(columnEnums.size());
        String columnName = null;
        int columnSize = columnEnums.size();
        for (int i = 0; i < columnSize; i++)
        {
            //获取列名
            columnName = CommonUtils.getColumnName(columnEnums.get(i));
            if (null == columnName)
            {
                notExistColumns.add(columnEnums.get(i));
            }
        }
        
        return notExistColumns;
    }
    
    /**
     * 获取列名数组
     * @param columnEnums 列枚举列表
     * @return 列名数组
     * @throws CException 异常
     */
    public static String[] getColumnNams(List<Integer> columnEnums)
        throws CException
    {
        if (null == columnEnums || columnEnums.isEmpty())
        {
            return null;
        }
        
        String[] columnNames = new String[columnEnums.size()];
        String columnName = null;
        int columnSize = columnEnums.size();
        //将枚举值代表的列转换成列名数组，如果有不存在的列则返回异常
        for (int i = 0; i < columnSize; i++)
        {
            //获取列名
            columnName = CommonUtils.getColumnName(columnEnums.get(i));
            if (null != columnName)
            {
                columnNames[i] = columnName;
            }
            else
            {
                LOGGER.error("column[{}] doesn't exist!", columnEnums.get(i));
                throw new CException(ResultCode.COLUMN_NOT_EXIST, columnEnums.get(i));
            }
        }
        
        return columnNames;
    }
    
    /**
     * 关闭流
     * @param stream 流
     */
    public static void close(Closeable stream)
    {
        //流关闭方法
        if (null != stream)
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                LOGGER.error("close stream failed!", e);
            }
        }
    }
    
    /**
     * 解析请求json消息体为相应的对象
     * @param body 请求json消息体
     * @param reqClass 请求json消息体对应的类
     * @param <T> 泛型类型
     * @return 请求对象 
     */
    public static <T extends Object> T parseObject(String body, Class<T> reqClass)
    {
        //在json对象外部包装上req字段名，方便读取和转换对象
        JSONObject json = JSONObject.parseObject(String.format("{\"req\":%s}", body));
        T req = json.getObject("req", reqClass);
        return req;
    }
    
    /**
     * 将Obj对象转换成“字段名：字段值”对象的Map对象
     * @param obj 待转换对象
     * @return Map对象
     */
    public static Map<String, Object> covObj2Map(final Object obj)
    {
        //字段名，字段值Map
        final Map<String, Object> keyValueMap = new HashMap<String, Object>();
        if (null != obj)
        {
            //获取声明的字段数组
            final Field[] fields = obj.getClass().getDeclaredFields();
            //使用权限块处理
            AccessController.doPrivileged(new PrivilegedAction<Map<String, Object>>()
            {
                @Override
                public Map<String, Object> run()
                {
                    Object value = null;
                    for (int j = 0; j < fields.length; j++)
                    {
                        fields[j].setAccessible(true);
                        try
                        {
                            //获取字段值
                            value = fields[j].get(obj);
                            keyValueMap.put(fields[j].getName(), value);
                        }
                        catch (IllegalArgumentException e)
                        {
                            //参数不合法
                            LOGGER.error("getValue failed!", e);
                        }
                        catch (IllegalAccessException e)
                        {
                            //访问不合法
                            LOGGER.error("getValue failed!", e);
                        }
                    }
                    return keyValueMap;
                }
            });
        }
        else
        {
            //参数不能为空
            LOGGER.error("obj can't be null!");
        }
        
        return keyValueMap;
    }
    
    /**
     * 根据异常设置返回码
     * @param result 可以修改返回码的接口
     * @param e 异常
     */
    public static void setResultByException(IModifyResult result, Exception e)
    {
        //如果参数为null，直接返回
        if (null == result || null == e)
        {
            LOGGER.error("setResultByException error! result is {}, e is {}", result, e);
            return;
        }
        
        //从异常中提取错误码
        if (e instanceof CException)
        {
            result.setResult_code(((CException)e).getErrorCode());
        }
        else if (e instanceof JSONException)
        {
            //JSON异常返回消息格式不合法错误码
            result.setResult_code(ResultCode.REQUEST_FORMAT_ERROR);
        }
        else
        {
            //其它返回系统错误码
            result.setResult_code(ResultCode.SYSTEM_ERROR);
        }
    }
    
    /**
     * 将异常e转换成封装的异常
     * @param e 异常
     * @return 封装的异常
     */
    public static CException covertE2CE(Exception e)
    {
        //如果参数为null，直接返回
        if (null == e)
        {
            LOGGER.error("covertE2CE error! e is null");
            return new CException(ResultCode.SYSTEM_ERROR);
        }
        
        //将非CException类型异常转换成该异常
        if (e instanceof CException)
        {
            return (CException)e;
        }
        else if (e instanceof JSONException)
        {
            //JSON异常返转换成CException异常
            return new CException(ResultCode.REQUEST_FORMAT_ERROR);
        }
        else
        {
            //其它转化成系统异常
            return new CException(ResultCode.SYSTEM_ERROR);
        }
    }
    
    /**
     * 获取最小值
     * @param valueA 值A
     * @param valueB 值B
     * @return 返回valueA和valueB中的最小值
     */
    public static Long getMinValue(Long valueA, Long valueB)
    {
        //valueA和valueB有一个为null，则取另一个非null值
        //否则取两个中的最小值
        Long value = null;
        
        if (null != valueA)
        {
            if (null != valueB)
            {
                value = Math.min(valueA, valueB);
            }
            else
            {
                value = valueA;
            }
        }
        else
        {
            value = valueB;
        }
        
        return value;
    }
    
    /**
     * 获取最小值
     * @param valueA 值A
     * @param valueB 值B
     * @return 返回valueA和valueB中的最小值
     */
    public static Integer getMinValue(Integer valueA, Integer valueB)
    {
        //valueA和valueB有一个为null，则取另一个非null值
        //否则取两个中的最小值
        Integer value = null;
        
        if (null != valueA)
        {
            if (null != valueB)
            {
                value = Math.min(valueA, valueB);
            }
            else
            {
                value = valueA;
            }
        }
        else
        {
            value = valueB;
        }
        
        return value;
    }
    
    /**
     * 删除数据文件
     * @param fileName 文件
     */
    public static void deleteFile(String fileName)
    {
        //文件名为null直接返回
        if (null == fileName)
        {
            return;
        }
        
        try
        {
            //文件存在则删除，删除失败记录日志退出
            File file = new File(fileName);
            //文件存在
            if (file.exists())
            {
                if (!file.delete())
                {
                    //创建失败记录错误信息
                    LOGGER.error("delete file[{}] failed!", fileName);
                }
            }
        }
        catch (SecurityException e)
        {
            //记录错误信息
            LOGGER.error("delete file[{}] failed!", fileName, e);
        }
    }
}
