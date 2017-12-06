/*
 * 文 件 名:  CheckParamUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  参数校验的通用工具
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.utils;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.constants.type.UserAdFlagType;
import com.huawei.devicecloud.platform.bi.odp.constants.type.UserProfileTable;
import com.huawei.devicecloud.platform.bi.odp.domain.DateRatioInfo;
import com.huawei.devicecloud.platform.bi.odp.message.req.FileUploadReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.QueryDataCountReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.ReserveBatchDataReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.RevokeReserveReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.WGetFileReq;
import com.huawei.devicecloud.platform.bi.odp.privelege.Token;

/**
 * 参数校验的通用工具
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class CheckParamUtils
{
    /**
     * 私有构造方法
     */
    private CheckParamUtils()
    {
        
    }
    
    /**
     * 统一封装参数校验异常
     * @param parameterName 参数名
     * @param parameterVal 参数值
     * @throws CException 异常
     * @see [类、类#方法、类#成员]
     */
    public static void buildParameterInvalid(String parameterName, Object parameterVal)
        throws CException
    {
        StringBuffer strb = new StringBuffer();
        //字段值不合法字符串
        strb.append("Parameter named ")
            .append(parameterName)
            .append(" with value [")
            .append(parameterVal)
            .append("] is invalid.");
        
        throw new CException(ResultCode.PARAMETER_INVALID, new Object[] {parameterVal, parameterName});
    }
    
    /**
     * 检查用户名是否合法
     * @param userName 用户名
     * @throws CException 异常
     */
    public static void checkUserName(String userName)
        throws CException
    {
        String fieldName = "userName";
        if (StringUtils.isBlank(userName))
        {
            buildParameterInvalid(fieldName, null);
        }
        
        if (!checkValueWithRegex(CommonUtils.getFieldValidService().getFieldRegex(fieldName), userName))
        {
            buildParameterInvalid(fieldName, userName);
        }
    }
    
    /**
     * 检查密码是否合法
     * @param pwd 密码
     * @throws CException 异常
     */
    public static void checkPwd(String pwd)
        throws CException
    {
        String fieldName = "pwd";
        if (StringUtils.isBlank(pwd))
        {
            buildParameterInvalid(fieldName, null);
        }
        
        if (!checkValueWithRegex(CommonUtils.getFieldValidService().getFieldRegex(fieldName), pwd))
        {
            buildParameterInvalid(fieldName, pwd);
        }
    }
    
    /**
     * obj非NULL检查
     * @param obj 对象
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkNotNull(Object obj, String filedName)
        throws CException
    {
        //非null检查
        if (null == obj)
        {
            buildParameterInvalid(filedName, null);
        }
    }
    
    /**
     * obj非空检查
     * @param obj 对象
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkFilterStmt(String obj, String filedName)
        throws CException
    {
        //非空白检查
        checkNotBlank(obj, filedName);
        try
        {
            OdpCommonUtils.extractUserAdFlag(obj);
        }
        catch (CException e)
        {
            //不合法
            buildParameterInvalid(filedName, obj);
        }
    }
    
    /**
     * obj非空检查
     * @param obj 对象
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkNotBlank(String obj, String filedName)
        throws CException
    {
        //非空白检查
        if (StringUtils.isBlank(obj))
        {
            buildParameterInvalid(filedName, obj);
        }
    }
    
    /**
     * 文件上传filetype字段检查
     * @param obj 对象
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkFileType(String obj, String filedName)
        throws CException
    {
        if (StringUtils.isBlank(obj) || !obj.equals("001"))
        {
            buildParameterInvalid(filedName, obj);
        }
    }
    
    /**
     * obj非空检查
     * @param groupId 预留号
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkGroupId(String groupId, String filedName)
        throws CException
    {
        //非空白检查
        if (StringUtils.isBlank(groupId))
        {
            buildParameterInvalid(filedName, groupId);
        }
    }
    
    /**
     * 检查列枚举集合是否合法
     * @param columns 列枚举值
     * @param filedName 字段名
     * @param userAdFlag 用户标识
     * @throws CException 异常
     */
    public static void checkColumns(List<Integer> columns, int userAdFlag, String filedName)
        throws CException
    {
        if (null == columns || columns.isEmpty())
        {
            buildParameterInvalid(filedName, columns);
        }
        
        //获取不存在的列集合
        List<Integer> notExistsColumns = OdpCommonUtils.getNotExistColumns(columns);
        if (null != notExistsColumns && !notExistsColumns.isEmpty())
        {
            buildParameterInvalid(filedName, notExistsColumns);
        }
        
        if (userAdFlag == UserAdFlagType.NORMAL)
        {
            //普通用户必须仅包含DEVICE_ID列
            if (1 != columns.size() || columns.get(0) != UserProfileTable.DEVICE_ID)
            {
                buildParameterInvalid(filedName, columns);
            }
        }
        
    }
    
    /**
     * 检查batchInfo是否合法
     * @param batchInfo 批次信息
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkBatchInfo(List<DateRatioInfo> batchInfo, String filedName)
        throws CException
    {
        if (null == batchInfo || batchInfo.isEmpty())
        {
            buildParameterInvalid(filedName, batchInfo);
        }
        
        int ratioCount = 0;
        //dr的每个字段都需要合法
        for (DateRatioInfo dr : batchInfo)
        {
            if (null == dr || null == dr.getRatio() || dr.getRatio() < 0)
            {
                buildParameterInvalid(filedName, batchInfo);
                break;
            }
            
            try
            {
                //日期格式是否合法
                CommonUtils.convertDateTimeFormat(dr.getDate(), null);
            }
            catch (CException e)
            {
                buildParameterInvalid(filedName, batchInfo);
            }
            
            ratioCount += dr.getRatio();
        }
        
        //比例和不能为0
        if (ratioCount <= 0)
        {
            buildParameterInvalid(filedName, batchInfo);
        }
    }
    
    /**
     * 检查记录总数（可以为null，但是不能小于0）
     * @param recordCount 记录数
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkRecordCount(Long recordCount, String filedName)
        throws CException
    {
        //可null，非负数
        if (null != recordCount && recordCount < 0)
        {
            buildParameterInvalid(filedName, recordCount);
        }
    }
    
    /**
     * 非负数检查（非null非负数）
     * @param num 记录数
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkNonNegative(Integer num, String filedName)
        throws CException
    {
        //非null非负数
        if (null == num || num < 0)
        {
            buildParameterInvalid(filedName, num);
        }
    }
    
    /**
     * 非负数检查（非null非负数）
     * @param num 记录数
     * @param filedName 字段名
     * @throws CException 异常
     */
    public static void checkNonNegative(Long num, String filedName)
        throws CException
    {
        //非null非负数
        if (null == num || num < 0)
        {
            buildParameterInvalid(filedName, num);
        }
    }
    
    /**
     * 检查token是否合法
     * @param token 对话Id
     * @throws CException 异常
     */
    public static void checkToken(Token token)
        throws CException
    {
        String fieldName = "token";
        if (null == token || StringUtils.isBlank(token.getToken()))
        {
            buildParameterInvalid(fieldName, null);
        }
    }
    
    /**
     * 使用正则表示式校验的通用方法
     * 
     * @param regex 正则表示式
     * @param value 参数值
     * @return 是否符合要求
     */
    public static boolean checkValueWithRegex(String regex, String value)
    {
        try
        {
            //检查value是否符合正则表达式
            if (null != regex)
            {
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(value).matches();
            }
        }
        catch (Exception e)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查预留数据消息体是否合法
     * @param req 参数
     * @throws CException  异常
     */
    public static void checkReserveBatchDataReq(ReserveBatchDataReq req)
        throws CException
    {
        if (null == req)
        {
            throw new CException(ResultCode.REQUEST_FORMAT_ERROR);
        }
        
        //对每一个字段进行校验
        checkNotBlank(req.getAppId(), "app_id");
        checkNotBlank(req.getTimestamp(), "timestamp");
        checkNotBlank(req.getCallbackURL(), "callback_url");
        checkNotBlank(req.getTransactionId(), "transaction_id");
        //检查filter_stmt是否为空以及包含user_ad_flag的字段是否合法
        checkFilterStmt(req.getFilterStmt(), "filter_stmt");
        //提取用户标识，要求必需位于checkFilterStmt之后
        int userAdFlag = OdpCommonUtils.extractUserAdFlag(req.getFilterStmt());
        checkColumns(req.getExtractList(), userAdFlag, "columns");
        checkBatchInfo(req.getBatchInfo(), "batch_info");
        checkNonNegative(req.getDays(), "days");
        checkRecordCount(req.getRecordNumber(), "record_count");
    }
    
    /**
     * 检查取消预留数据消息体是否合法
     * @param req 参数
     * @throws CException  异常
     */
    public static void checkRevokeReserveReq(RevokeReserveReq req)
        throws CException
    {
        if (null == req)
        {
            throw new CException(ResultCode.REQUEST_FORMAT_ERROR);
        }
        
        //对请求的每一个字段进行校验
        checkNotBlank(req.getAppId(), "app_id");
        checkNotBlank(req.getTimestamp(), "timestamp");
        checkGroupId(req.getReserveId(), "group_id");
    }
    
    /**
     * 检查查询数据总数消息体是否合法
     * @param req 参数
     * @throws CException  异常
     */
    public static void checkQueryDataCountReq(QueryDataCountReq req)
        throws CException
    {
        if (null == req)
        {
            throw new CException(ResultCode.REQUEST_FORMAT_ERROR);
        }
        
        //对请求的每一个字段进行校验
        checkNotBlank(req.getAppId(), "app_id");
        checkNotBlank(req.getTimestamp(), "timestamp");
        checkNotBlank(req.getCallbackURL(), "callback_url");
        checkNotBlank(req.getTransactionId(), "transaction_id");
        checkRecordCount(req.getRecordCount(), "record_count");
        checkNotBlank(req.getFilterStmt(), "filter_stmt");
    }
    
    /**
     * 检查获取文件流消息
     * @param req 参数
     * @throws CException  异常
     */
    public static void checkWgetFileReq(WGetFileReq req)
        throws CException
    {
        if (null == req)
        {
            throw new CException(ResultCode.REQUEST_FORMAT_ERROR);
        }
        
        //对请求的每一个字段进行校验
        checkNotBlank(req.getAppId(), "app_id");
        checkNotBlank(req.getTimestamp(), "timestamp");
        checkGroupId(req.getReserveId(), "group_id");
    }
    
    
    /**
     * 检查文件上传消息
     * @param req 参数
     * @throws CException  异常
     */
    public static void checkFileUploadReq(FileUploadReq req)
        throws CException
    {
        if (null == req)
        {
            throw new CException(ResultCode.REQUEST_FORMAT_ERROR);
        }
        
        //对请求的每一个字段进行校验
        checkNotBlank(req.getAppId(), "app_id");
        checkNotBlank(req.getTimestamp(), "timestamp");
        checkFileType(req.getFileType(), "file_type");
    }
    
}
