/*
 * 文 件 名:  MaterialFileUpload.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-7-14
 */
package com.huawei.manager.mkt.api;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;

import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.ResultCode;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.UploadRDBProcessor;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-7-14]
 * @see  [相关类/方法]
 */
public class MaterialFileUpload extends UploadRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理文件上传
     * @param context    系统上下文
     * @param paraName   请求参数
     * @param fileName   文件名称
     * @param size       文件大小
     * @param stream     文件流
     * @return           处理是否成功
     */
    public int processFile(MethodContext context, String paraName, String fileName, long size, InputStream stream)
    {
        DBConnection dbConn = null;
        try
        {
            
            //获取广告位ID
            Integer adInfoId = Integer.valueOf((String)context.getParameter("adInfoId"));
            
            //获取用户名
            String account = context.getAccount();
            
            //获取系统的文件大小
            Integer maxFileSize = Integer.valueOf(StringUtils.getConfigInfo("maxFileSize"));
            
            //如果文件过大直接退出
            if (size < 0 || size > maxFileSize)
            {
                return ResultCode.FILE_TOO_LARGE;
            }
            
            //检查文件名
            if (StringUtils.getLength(fileName) > 100)
            {
                return ResultCode.FILE_NAME_TOO_LONG;
            }
            
            //获取配置目录
            String materialSavePath = StringUtils.getConfigInfo("materialSavePath");
            String path = materialSavePath + File.separator + adInfoId;
            FileUtils.createPath(path);
            
            //写文件
            String desFile = materialSavePath + File.separator + adInfoId + File.separator + fileName;
            FileUtils.writeFile(stream, desFile);
            
            //数据库连接
            MethodConfig mc = context.getMethodConfig();
            RDBProcessConfig config = (RDBProcessConfig)mc.getProcessConfig();
            dbConn = DBUtil.getConnection(config.getDataSource(context), config.useTransaction());
            
            Connection conn = (Connection)dbConn.getConnection();
            
            //插入数据表
            String sql = "call sp_adInfoSaveMaterialFile(?,?,?,?,?)";
            
            int res = DBUtil.execute(conn, sql, true, new Object[] {account, adInfoId, desFile, fileName});
            
            if (res != 0)
            {
                return ResultCode.MATERIAL_ERROR;
            }
            
        }
        finally
        {
            
            DBUtil.freeConnection(dbConn, true);
        }
        
        LOG.debug("MaterialFileUpload success");
        return ResultCode.OK;
    }
}
