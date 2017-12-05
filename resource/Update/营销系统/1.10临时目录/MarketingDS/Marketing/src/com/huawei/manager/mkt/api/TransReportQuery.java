package com.huawei.manager.mkt.api;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.dao.TransReportQueryDao;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-19]
 * @see  [相关类/方法]
 *  
 */
public class TransReportQuery extends AuthRDBProcessor
{
    /**
     * 根据查询参数， 查找结果，分配结果集
     * @param context 上下文
     * @param dbConn 数据库连接
     * @return 结果集
     */
    public int process(MethodContext context, DBConnection dbConn)
    {
        
        int from = 0;
        int to = 500;
        return TransReportQueryDao.setContext(context, dbConn, from, to);
        
    }
    
}
