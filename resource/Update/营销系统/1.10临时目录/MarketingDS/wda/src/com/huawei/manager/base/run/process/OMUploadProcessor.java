package com.huawei.manager.base.run.process;

import java.sql.CallableStatement;
import java.sql.SQLException;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.UploadRDBProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * 处理类
 * 
 * @author w00296102
 */
public class OMUploadProcessor extends UploadRDBProcessor
{
    /**
     * 处理前
     * 
     * @param context
     *            方法的配置信息
     * @param conn
     *            数据库连接
     * @param statement
     *            状态
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {
        int retCode = super.beforeProcess(context, conn, statement);
        if (retCode != RetCode.OK)
        {
            return retCode;
        }

        return AuthRDBProcessor.auth(context, conn);
    }

    /**
     * 结束后
     * 
     * @param context
     *            方法的配置信息
     * @return 返回码
     */
    @Override
    public int afterAll(MethodContext context)
    {
        AuthRDBProcessor.writeLog(context);
        return RetCode.OK;
    }
}
