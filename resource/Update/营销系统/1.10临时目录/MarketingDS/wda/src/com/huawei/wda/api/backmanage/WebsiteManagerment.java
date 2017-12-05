package com.huawei.wda.api.backmanage;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 
 * 网站管理后台参数校验 <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月24日]
 * @see [相关类/方法]
 */
public class WebsiteManagerment extends AuthRDBProcessor
{
    /**
     * 日志类
     */
    private static final Logger LOG = LogUtil.getInstance();

    /**
     * 执行脚本之前的参数校验（网站维护的新增和修改）
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return int
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {
        Map<String, Object> reqParameters = context.getParameters();
        int groupId = JsonUtil.getAsInt(reqParameters, "group_id");

        DBQueryResult res = DBUtil.query(conn.getConnection(),
                "select id as groupId from t_wda_group", new Object[]
                {});
        List<Map<String, Object>> groupIdList = res.getResult().get(0);
        List<Integer> list = new ArrayList<Integer>();
        if (null != groupIdList && !groupIdList.isEmpty())
        {
            for (Map<String, Object> gl : groupIdList)
            {
                try
                {
                    int tableGroupId = Integer.parseInt((String) gl
                            .get("groupId"));
                    list.add(tableGroupId);
                }
                catch (NumberFormatException e)
                {
                    LOG.error("NumberFormatException");
                    return WRONG_PARAMETER;
                }
            }
        }
        if (groupId == 0)
        {
            return OK;
        }
        if (!list.contains(groupId))
        {
            return WRONG_PARAMETER;
        }

        return OK;
    }

    /**
     * 执行脚本之后（网站维护的新增和修改）
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int
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
