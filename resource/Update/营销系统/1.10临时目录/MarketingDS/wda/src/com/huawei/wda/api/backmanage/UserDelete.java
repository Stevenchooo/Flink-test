package com.huawei.wda.api.backmanage;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 请求列表处理
 */
public class UserDelete extends AuthRDBProcessor
{

    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情 参数校验userid和username
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();
        String id = JsonUtil.getAsStr(reqParameters, "id");
        DBQueryResult res = DBUtil
                .query(conn.getConnection(),
                        "select account as userID, fullName as userName from t_ms_user where id = ?",
                        new Object[]
                        {id});
        List<Map<String, Object>> userJson = res.getResult().get(0);

        DBUtil.execute(conn.getConnection(),
                "delete from t_wda_group_user where userID =?", false,
                new Object[]
                {userJson.get(0).get("userID")});

        return OK;
    }

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
        return OK;
    }
}