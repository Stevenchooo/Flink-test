package com.huawei.manager.base.api;

import java.sql.SQLException;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.Utils;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */

public class UserRemove extends AuthRDBProcessor
{
    
    /**
     * 系统处理后
     * @param context       系统上下文
     * @param dbConn        数据库连接
     * @return              是否成功
     * @throws SQLException  sql异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection dbConn)
        throws SQLException
    {
        String account = Utils.parseString(context.getParameter(Constant.ACCOUNT), "");
        removeRight(account);
        return RetCode.OK;
    }
}
