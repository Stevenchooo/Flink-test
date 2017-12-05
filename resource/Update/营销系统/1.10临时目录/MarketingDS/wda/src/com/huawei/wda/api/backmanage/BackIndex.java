package com.huawei.wda.api.backmanage;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.waf.core.run.MethodContext;
/**
 * BackIndex
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月14日]
 * @see  [相关类/方法]
 */
public class BackIndex extends AuthRDBProcessor
{

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        String account = context.getAccount();

        DBQueryResult res = DBUtil.query(conn.getConnection(),
                "select isAdmin from t_ms_user where account = ?", new Object[]
        {account});
        List<Map<String, Object>> userJson = res.getResult().get(0);

        context.setResult("__isHonor", userJson);
        
        return OK;
    }
}