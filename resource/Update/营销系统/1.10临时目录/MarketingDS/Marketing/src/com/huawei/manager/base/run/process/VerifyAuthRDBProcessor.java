package com.huawei.manager.base.run.process;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.api.VerifyCode;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-9-9]
 * @see  [相关类/方法]
 */
public class VerifyAuthRDBProcessor extends AuthRDBProcessor
{
    private static final String PARA_VERIFYCODE = "verifyCode";
    
    /**
     * process前处理
     * @param context    上下文
     * @param conn       数据库链接
     * @param statement  执行命令
     * @return           是否成功
     * @throws SQLException     数据库异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn, CallableStatement statement)
        throws SQLException
    {
        Map<String, Object> params = context.getParameters();
        String input = JsonUtil.getAsStr(params, PARA_VERIFYCODE, "");
        String check = VerifyCode.getVerifyCodeCookie(context);
        if (!input.equalsIgnoreCase(check))
        {
            return RetCode.WRONG_VERIFYCODE;
        }
        VerifyCode.rmvVerifyCodeCookie(context); //验证后，删除老的验证码
        
        return super.beforeProcess(context, conn, statement);
    }
}
