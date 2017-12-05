package com.huawei.manager.base.run.process;

import java.sql.CallableStatement;
import java.sql.SQLException;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.UploadRDBProcessor;
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
public class OMUploadProcessor extends UploadRDBProcessor
{
    /**
     * 处理前
     * @param context   上下文
     * @param conn      数据库链接
     * @param statement  数据库执行
     * @return           是否成功
     * @throws SQLException     sql异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn, CallableStatement statement)
        throws SQLException
    {
        int retCode = super.beforeProcess(context, conn, statement);
        if (retCode != RetCode.OK)
        {
            return retCode;
        }
        
        return AuthRDBProcessor.auth(context, conn);
    }
    
    /**
     * 处理完成
     * @param context  上下文
     * @return         接口返回值
     */
    @Override
    public int afterAll(MethodContext context)
    {
        AuthRDBProcessor.writeLog(context);
        return RetCode.OK;
    }
}
