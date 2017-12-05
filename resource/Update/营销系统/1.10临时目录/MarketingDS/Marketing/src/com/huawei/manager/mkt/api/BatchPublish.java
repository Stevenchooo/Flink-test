/*
 * 文 件 名:  BatchPublish.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-27
 */
package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.util.MktUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-27]
 * @see  [相关类/方法]
 */
public class BatchPublish extends AuthRDBProcessor
{
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理后
     * @param context          系统上下文
     * @param conn             数据库连接
     * @return                 是否成功
     * @throws SQLException    sql异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        //导出信息列表
        List<Integer> aidList = MktUtils.getAdinfoIdList(context);
        
        LOG.debug("enter batch publish, aid list is {}", aidList);
        String account = context.getAccount();
        
        //列表为空直接返回
        if (null == aidList || aidList.isEmpty())
        {
            context.setResult(Constant.RESTOTAL, 0);
            context.setResult(Constant.RESUCCESS, 0);
            return RetCode.OK;
        }
        
        int total = aidList.size();
        int success = publisMonitorInfoList(conn, aidList, account);
        
        context.setResult(Constant.RESTOTAL, total);
        context.setResult(Constant.RESUCCESS, success);
        
        return RetCode.OK;
    }
    
    /**
     * <发布广告位列表>
     * @param conn          数据库链接
     * @param aidList       广告位id
     * @param account       操作员
     * @return              成功记录数
     * @see [类、类#方法、类#成员]
     */
    private int publisMonitorInfoList(DBConnection conn, List<Integer> aidList, String account)
    {
        int success = 0;
        
        for (Integer aid : aidList)
        {
            int out = 0;
            int res =
                DBUtil.execute(conn, "call sp_monitorInfoPublish(?,?,?) ", false, new Object[] {out, aid, account});
            if (0 == res)
            {
                success++;
            }
            
        }
        return success;
    }
    
}
