package com.huawei.manager.base.api;

import java.sql.SQLException;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.Utils;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 用于用户自行找回密码，密码会发送到邮箱
 * 
 * @author y00164418
 * 
 */
public class UserRemove extends AuthRDBProcessor
{
    /**
     * 系统处理后
     * 
     * @param context
     *            系统上下文
     * @param dbConn
     *            数据库连接
     * @return 是否成功
     * @throws SQLException
     *             sql异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection dbConn)
        throws SQLException
    {
        String account = Utils.parseString(context.getParameter("account"), "");
        removeRight(account);
        return RetCode.OK;
    }
}
