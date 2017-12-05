package com.huawei.manager.base.api;

import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
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
public class RoleRightSave extends AuthRDBProcessor
{
    /**
     * api处理
     * @param context       上下文
     * @param conn          数据库连接
     * @return              是否成功
     */
    @Override
    public int process(MethodContext context, DBConnection conn)
    {
        Map<String, Object> reqParameters = context.getParameters();
        String role = JsonUtil.getAsStr(reqParameters, "role", null);
        int retCode = DBUtil.execute(conn, "delete from t_ms_right where role=?", false, new Object[] {role});
        if (retCode != RetCode.OK)
        {
            return retCode;
        }
        
        List<Object> rights = JsonUtil.getAsList(reqParameters, "rights");
        RDBProcessConfig pc = (RDBProcessConfig)context.getMethodConfig().getProcessConfig();
        String sql = pc.getSQL();
        
        Map<String, Object> one, opr;
        String dataType;
        StringBuilder right = new StringBuilder(Constant.STRVLINE);
        
        List<Object> operations;
        if (null == rights || rights.isEmpty())
        {
            return RetCode.OK;
        }
        int num = rights.size();
        int oprNum = 0;
        int rightNum;
        
        for (int i = 0; i < num; i++)
        {
            one = JsonUtil.getAsObject(rights, i);
            dataType = JsonUtil.getAsStr(one, "dataType", null);
            
            operations = JsonUtil.getAsList(one, "operations");
            
            if (null == operations || operations.isEmpty())
            {
                continue;
            }
            
            oprNum = operations.size();
            right.delete(0, right.length());
            right.append(Constant.CHARVLINE);
            rightNum = 0;
            for (int j = 0; j < oprNum; j++)
            {
                opr = JsonUtil.getAsObject(operations, j);
                if (JsonUtil.getAsBool(opr, "val"))
                {
                    right.append(JsonUtil.getAsStr(opr, "name")).append(Constant.CHARVLINE);
                    rightNum++;
                }
            }
            
            if (rightNum <= 0)
            {
                continue;
            }
            
            retCode = DBUtil.execute(conn, sql, false, new Object[] {role, dataType, right.toString()});
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
