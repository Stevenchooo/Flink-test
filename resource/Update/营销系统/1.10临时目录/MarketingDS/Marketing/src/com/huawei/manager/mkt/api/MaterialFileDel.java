/*
 * 文 件 名:  MaterialFileDel.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-9-6
 */
package com.huawei.manager.mkt.api;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-9-6]
 * @see  [相关类/方法]
 */
public class MaterialFileDel extends AuthRDBProcessor
{
    /**
     * 接口处理前如何处理
     * @param context   系统上下文
     * @param conn      数据库连接
     * @param statement sql执行语句
     * @return          是否成功
     * @throws SQLException   数据库异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn, CallableStatement statement)
        throws SQLException
    {
        Integer adInfoId = Integer.valueOf((String)context.getParameter("adInfoId"));
        
        List<Map<String, Object>> resutList =
            DBUtil.query(conn, "select path from t_mkt_material_info where aid = ?", false, new Object[] {adInfoId});
        
        //找不到直接返回
        if (null == resutList || resutList.isEmpty())
        {
            return RetCode.OK;
        }
        
        String path = (String)resutList.get(0).get("path");
        
        if (null != path)
        {
            File file = new File(path);
            
            if (file.exists())
            {
                FileUtils.deleteQuietly(file);
            }
        }
        
        //写入ad信息
        return RetCode.OK;
    }
}
