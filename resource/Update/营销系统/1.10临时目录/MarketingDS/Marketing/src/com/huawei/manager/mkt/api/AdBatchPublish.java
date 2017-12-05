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
 * @author  p84035806
 * @version [Internet Business Service Platform SP V100R100, 2015-7-08]
 * @see  [相关类/方法]
 */
public class AdBatchPublish extends AuthRDBProcessor
{
    /**
     * 日志
     */
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 数据库执行成功
     */
    private static final int SUCCESS = 0;
    
    /**
     * 初始化为0
     */
    private static final int ZERO = 0;
    
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
        
        LOG.debug("enter AdBatchPublish ,aid list is {}", aidList);
        String account = context.getAccount();
        
        //列表为空直接返回
        if (null == aidList || aidList.isEmpty())
        {
            context.setResult(Constant.RESTOTAL, ZERO);
            context.setResult(Constant.RESUCCESS, ZERO);
            return RetCode.OK;
        }
        
        int total = aidList.size();
        int success = publishAdInfoList(conn, aidList, account);
        
        context.setResult(Constant.RESTOTAL, total);
        context.setResult(Constant.RESUCCESS, success);
        
        return RetCode.OK;
    }
    
    /**
     * <发布广告位信息列表>
     * @param conn          数据库链接
     * @param aidList       广告位列表
     * @param account       用户账号
     * @return              成功广告位数
     * @see [类、类#方法、类#成员]
     */
    private int publishAdInfoList(DBConnection conn, List<Integer> aidList, String account)
    {
        int success = ZERO;
        
        for (Integer aid : aidList)
        {
            int out = ZERO;
            int res = DBUtil.execute(conn, "call sp_adInfoPublish(?,?,?) ", false, new Object[] {out, account, aid});
            if (SUCCESS == res)
            {
                success++;
            }
            
        }
        return success;
    }
    
}
