package com.huawei.manager.base.api;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.SecureUtil;
import com.huawei.waf.core.config.sys.SecurityConfig;
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
public class AdminResetPwd extends AuthRDBProcessor
{
    /**
     * 密码
     */
    protected static final String PWD = "password";
    
    /**
     * 账号
     */
    protected static final String ACCOUNT = "account";
    
    /**
     * 处理前
     * @param context        系统上线文
     * @param conn           数据库链接
     * @param statement      执行命令
     * @return               是否成功
     * @throws SQLException  sql异常
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn, CallableStatement statement)
        throws SQLException
    {
        Map<String, Object> reqParameters = context.getParameters();
        String account = JsonUtil.getAsStr(reqParameters, ACCOUNT);
        String newPwd =
            SecureUtil.generatePassword(account,
                SecurityConfig.getMinPasswordLen(),
                SecurityConfig.getMinCharTypeNum(),
                SecurityConfig.getMinDiffCharNum());
        String pwd = SecureUtil.encodePassword(account, newPwd);
        context.setParameter(PWD, newPwd); //save it into request parameters, use it when response
        
        statement.setString(3, pwd);
        
        return RetCode.OK;
    }
    
    /**
     * 处理后
     * @param context    系统上下文
     * @param conn       数据库链接
     * @return           是否成功
     * @throws SQLException   sql异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        context.setResult(PWD, context.getParameter(PWD));
        return RetCode.OK;
    }
}
