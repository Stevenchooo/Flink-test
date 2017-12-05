/*
 * 文 件 名:  MaterialDownLoad.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-7-16
 */
package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-7-16]
 * @see  [相关类/方法]
 */
public class MaterialDownLoad extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 接口处理后如何处理
     * @param context   系统上下文
     * @param conn      数据库连接
     * @return          是否成功
     * @throws SQLException   数据库异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        LOG.debug("enter MaterialDownLoad");
        
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        //存储过程返回的列表
        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = (List<Map<String, String>>)results.get("result");
        
        if (null == list || list.isEmpty())
        {
            return RetCode.OK;
        }
        
        //动态分配的文件路径
        String filePath = list.get(0).get("path");
        
        FileUtils.writeHttpFileResponse(context, filePath);
        
        //写入ad信息
        return RetCode.OK;
    }
}
