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
 * 需要加验证码的处理
 * 
 * @author flyinmind
 */
public class VerifyAuthRDBProcessor extends AuthRDBProcessor
{
    private static final String PARA_VERIFYCODE = "verifyCode";

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
        Map<String, Object> params = context.getParameters();
        String input = JsonUtil.getAsStr(params, PARA_VERIFYCODE, "");
        String check = VerifyCode.getVerifyCodeCookie(context);
        if (!input.equalsIgnoreCase(check))
        {
            return RetCode.WRONG_VERIFYCODE;
        }
        VerifyCode.rmvVerifyCodeCookie(context); // 验证后，删除老的验证码

        return super.beforeProcess(context, conn, statement);
    }
}
