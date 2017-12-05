package com.huawei.manager.base.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 按角色查询对不同数据的操作权限 数据类型及操作权限在每个接口的process部分定义
 * 
 * @author l00152046 request { role:"xxx", rights:[ {meta:"xxx",
 *         operations:[{name:"c", val:true},...]}, ... ] }
 */
public class RoleRightSave extends AuthRDBProcessor
{
    /**
     * 已做参数校验，此处不必判断、 api处理
     * 
     * @param context
     *            上下文
     * @param conn
     *            数据库连接
     * @return 是否成功
     * @see com.huawei.waf.adapter.handler.RDBProcessor#dbProcess(com.huawei.waf.core.bean.MethodContext,
     *      java.sql.Connection,
     *      com.huawei.waf.core.config.api.process.MethodConfig, java.util.Map,
     *      java.util.Map, com.huawei.waf.core.handler.APIProcessorAide)
     */
    @Override
    public int process(MethodContext context, DBConnection conn)
    {
        Map<String, Object> reqParameters = context.getParameters();
        String role = JsonUtil.getAsStr(reqParameters, "role", null);
        int num = 0;
        int retCode = DBUtil.execute(conn,
                "delete from t_ms_right where role=?", false, new Object[]
                {role});
        if (retCode != RetCode.OK)
        {
            return retCode;
        }
        List<Object> rights = JsonUtil.getAsList(reqParameters, "rights");
        RDBProcessConfig pc = (RDBProcessConfig) context.getMethodConfig()
                .getProcessConfig();
        String sql = pc.getSQL();

        Map<String, Object> one, opr;
        String dataType;
        StringBuilder right = new StringBuilder();
        right.append('|');

        List<Object> operations;
        if (null != rights)
        {
            num = rights.size();
        }
        else
        {
            return RetCode.NO_RIGHT;
        }
        int oprNum = 0;
        int rightNum;

        for (int i = 0; i < num; i++)
        {
            one = JsonUtil.getAsObject(rights, i);
            if (null == one)
            {
                continue;
            }
            dataType = JsonUtil.getAsStr(one, "dataType", null);

            operations = JsonUtil.getAsList(one, "operations");
            if (null == operations)
            {
                continue;
            }
            oprNum = operations.size();
            right.delete(0, right.length());
            right.append('|');
            rightNum = 0;
            for (int j = 0; j < oprNum; j++)
            {
                opr = JsonUtil.getAsObject(operations, j);
                if (null != opr && JsonUtil.getAsBool(opr, "val"))
                {
                    right.append(JsonUtil.getAsStr(opr, "name")).append('|');
                    rightNum++;
                }
            }

            if (rightNum <= 0)
            {
                continue;
            }

            retCode = DBUtil.execute(conn, sql, false, new Object[]
            {role, dataType, right.toString()});
            if (retCode != RetCode.OK)
            {
                context.setResult(retCode, "Fail to save " + dataType);
                return retCode;
            }
        }
        RoleRightQuery.loadRoleRight(conn, role);

        return RetCode.OK;
    }
}
