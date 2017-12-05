package com.huawei.wda.api.backmanage;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 请求列表处理
 */
public class GroupCreate extends AuthRDBProcessor
{

    private static final String SPLIT_STR = ",";

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
    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        DBQueryResult res = DBUtil.query(conn.getConnection(),
                "select account as Id, fullName as Name from t_ms_user",
                new Object[]
                {});
        List<Map<String, Object>> userLists = res.getResult().get(0);
        List idList = new ArrayList();
        List nameList = new ArrayList();
        for (Map<String, Object> ul : userLists)
        {
            idList.add(ul.get("Id"));
            nameList.add(ul.get("Name"));
        }
        // 获取的对比日期
        String userId = JsonUtil.getAsStr(reqParameters, "userId");
        String userName = JsonUtil.getAsStr(reqParameters, "userName");
        String[] ids = userId.split(SPLIT_STR);
        String[] names = userName.split(SPLIT_STR);
        for (String id : ids)
        {
            if (!idList.contains(id))
            {
                return RetCode.WRONG_PARAMETER;
            }
        }
        for (String name : names)
        {
            if (!nameList.contains(name))
            {
                return RetCode.WRONG_PARAMETER;
            }
        }
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