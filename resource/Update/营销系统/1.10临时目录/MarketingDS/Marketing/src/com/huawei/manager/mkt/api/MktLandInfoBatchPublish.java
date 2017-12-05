/*
 * 文 件 名:  MktLandInfoBatchPublish.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  p84035806
 * 创建时间:  2015-7-21
 */
package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.constant.AdState;
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
 * @author  p84035806
 * @version [Internet Business Service Platform SP V100R100, 2015-7-08]
 * @see  [相关类/方法]
 */
public class MktLandInfoBatchPublish extends AuthRDBProcessor
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
        
        LOG.debug("enter MktLandInfoBatchPublish, aid list is {}", aidList);
        String account = context.getAccount();
        
        //列表为空直接返回
        if (null == aidList || aidList.isEmpty())
        {
            context.setResult(Constant.RESTOTAL, 0);
            context.setResult(Constant.RESUCCESS, 0);
            return RetCode.OK;
        }
        
        int total = aidList.size();
        int success = modifyAdInfoState(conn, aidList, account);
        
        context.setResult(Constant.RESTOTAL, total);
        context.setResult(Constant.RESUCCESS, success);
        
        return RetCode.OK;
    }
    
    /**
     * <修改广告位状态>
     * @param conn          数据库链接
     * @param aidList       广告位id列表
     * @param account       用户账号
     * @return              修改成功记录数
     * @see [类、类#方法、类#成员]
     */
    private int modifyAdInfoState(DBConnection conn, List<Integer> aidList, String account)
    {
        int success = 0;
        
        for (Integer aid : aidList)
        {
            int out = 0;
            int res =
                DBUtil.execute(conn, "call sp_adInfoModifyState(?,?,?,?) ", false, new Object[] {out, account, aid,
                    AdState.FOR_MONITOR});
            if (0 == res)
            {
                success++;
            }
            
        }
        return success;
    }
    
}
