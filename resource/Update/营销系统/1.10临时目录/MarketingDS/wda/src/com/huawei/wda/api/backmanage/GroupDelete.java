package com.huawei.wda.api.backmanage;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 查询数据RefTrendList <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class GroupDelete extends AuthRDBProcessor
{
    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {
        Map<String, Object> reqParameters = context.getParameters();
        String groupId = JsonUtil.getAsStr(reqParameters, "id");
        DBUtil.execute(conn.getConnection(),
                "update t_wda_site_basic_info set group_id = 0 where group_id =?",
                false, new Object[]
        {groupId});
        DBUtil.execute(conn.getConnection(),
                "delete from t_wda_group_user where groupId =?", false,
                new Object[]
        {groupId});

        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        return OK;
    }
}