package com.huawei.manager.base.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author y84030100
 *
 */
public class AdminModels extends AuthRDBProcessor
{
    /**
     * 异步
     */
    private static final String ASYNC = "async";

    /**
     * ID
     */
    private static final String ID = "id";

    /**
     * PID
     */
    private static final String PID = "pid";

    /**
     * 是否是父节点
     */
    private static final String ISPARENT = "isParent";

    /**
     * MODELS名称
     */
    private static final String MODELS = "models";

    /**
     * META名称
     */
    private static final String META = "meta";

    /**
     * 系统执行
     * 
     * @param context
     *            系统上下文
     * @return 是否成功
     */
    @Override
    public int process(MethodContext context)
    {
        CacheResultData rc = (CacheResultData) getRight(context.getAccount());
        if (rc != null)
        {
            context.addResults(rc.results);
            return rc.retCode;
        }
        return super.process(context);
    }

    /**
     * 系统执行
     * 
     * @param context
     *            系统上下文
     * @param dbConn
     *            数据库链接
     * @return 是否成功
     */
    @Override
    public int process(MethodContext context, DBConnection dbConn)
    {
        String account = context.getAccount();
        Map<String, Object> params = context.getParameters();
        boolean isAsync = JsonUtil.getAsBool(params, ASYNC);

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        find(dbConn, account, 0, result, isAsync);
        context.setResult(MODELS, result);
        saveRight(context.getAccount(),
                new CacheResultData(RetCode.OK, context.getResults()));
        return RetCode.OK;
    }

    /**
     * <查询>
     * 
     * @param conn
     *            数据库链接
     * @param account
     *            用户账号
     * @param pid
     *            父类id
     * @param result
     *            返回值
     * @param isAsync
     *            是否异步
     * @param pidList
     *            父id列表
     * @see [类、类#方法、类#成员]
     */
    private void find(DBConnection conn, String account, int pid,
            List<Map<String, Object>> result, boolean isAsync)
    {
        List<Map<String, Object>> list = DBUtil.query(conn,
                "select id,name,meta from t_ms_model where pid=? order by id",
                false, new Object[]
                {pid});

        if (list == null || list.size() <= 0)
        {
            return;
        }

        for (Map<String, Object> one : list)
        {
            int id = JsonUtil.getAsInt(one, ID);
            List<Map<String, Object>> rr = DBUtil
                    .query(conn,
                            "select pid from t_ms_model where pid=? and name=? and meta='admin'",
                            false, new Object[]
                            {id, account});
            if (rr == null || rr.size() <= 0)
            { // 找不到就继续往下找
                find(conn, account, id, result, isAsync);
            }
            else
            { /* 如果有管理权限，则不必找是否为下层的管理员 */
                one.put(PID, pid);
                result.add(one);
                if (!isAsync)
                { // 不是异步的情况，加载下面的内容
                    boolean isParent = findSubs(conn, account, id, result) > 0;
                    one.put(ISPARENT, isParent);
                }
            }
        }
    }

    /**
     * <寻找子信息>
     * 
     * @param conn
     *            数据库链接
     * @param account
     *            用户账号
     * @param pid
     *            父ID
     * @param result
     *            查询结果
     * @param pidList
     *            父ID列表
     * @param depType
     * @return 是否成功
     * @see [类、类#方法、类#成员]
     */
    private int findSubs(DBConnection conn, String account, int pid,
            List<Map<String, Object>> result)
    {
        int num;

        List<Map<String, Object>> list = DBUtil
                .query(conn,
                        "select pid,id,name,meta from t_ms_model where pid=? and visible=1 order by id",
                        false, new Object[]
                        {pid});

        if (list == null || (num = list.size()) <= 0)
        {
            return 0;
        }

        int id, retCode;
        String meta;

        for (Map<String, Object> one : list)
        {
            id = JsonUtil.getAsInt(one, ID);
            meta = JsonUtil.getAsStr(one, META);

            retCode = DBUtil.execute(conn.getConnection(),
                    "{call sp_checkRight(?,?,?,?,?)}", true, new Object[]
                    {account, id, meta, "r"});
            if (retCode != RetCode.OK)
            {
                continue;
            }

            result.add(one);
            boolean isParent = findSubs(conn, account, id, result) > 0;
            one.put(ISPARENT, isParent);
        }
        return num;
    }
}
