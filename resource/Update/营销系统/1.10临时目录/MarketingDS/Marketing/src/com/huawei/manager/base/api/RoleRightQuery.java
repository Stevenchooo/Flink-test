package com.huawei.manager.base.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.config.process.AuthRDBProcessConfig;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 按角色查询对不同数据的操作权限
 * 数据类型及操作权限在每个接口的process部分定义
 * @author l00152046
 * {
 *  resultCode:0,
 *  results:[
 *    {meta:"xxx", operations:[{name:"c", val:true},...]},
 *    ...
 *  ],
 *  total:x
 * }
 */
public class RoleRightQuery extends AuthRDBProcessor
{
    /**
     * 角色数量不会太多，一般10个以内，所以可以直接用map缓存，在RoleRightSave时会刷新
     */
    private static final Map<String, RoleRight> ROLE_RIGHTS = new HashMap<String, RoleRight>();
    
    /**
     * 处理中
     * @param context   上下文
     * @param conn      数据库链接
     * @return          是否成功
     */
    @Override
    public int process(MethodContext context, DBConnection conn)
    {
        Map<String, Object> reqParameters = context.getParameters();
        String role = JsonUtil.getAsStr(reqParameters, "role", "");
        
        RoleRight r = ROLE_RIGHTS.get(role);
        if (r == null || !r.isValid())
        {
            r = loadRoleRight(conn, role);
        }
        
        List<Map<String, Object>> rights = r.getRoleRight();
        context.setResult("results", rights);
        context.setResult("total", rights.size());
        
        return RetCode.OK;
    }
    
    /**
     * 加载角色的权限
     * @param conn    数据库链接
     * @param role    角色
     * @return        用户
     */
    public static RoleRight loadRoleRight(DBConnection conn, String role)
    {
        //每个接口都可以指定操作的数据及造作类型(crud)，
        Map<String, List<String>> cfgRights = AuthRDBProcessConfig.getRights();
        
        String dataType;
        Map<String, Object> one;
        Map<String, Object> opr;
        List<Object> oprs;
        Map<String, Object> result;
        List<Map<String, Object>> rights = new ArrayList<Map<String, Object>>();
        String oprRight;
        List<String> operations;
        
        for (Map.Entry<String, List<String>> e : cfgRights.entrySet())
        {
            dataType = e.getKey();
            operations = e.getValue();
            one = DBUtil.queryMap(conn, "{call sp_roleRightQuery(?,?,?)}", true, new Object[] {role, dataType});
            oprRight = one == null ? "" : JsonUtil.getAsStr(one, "oprRight");
            
            oprs = new ArrayList<Object>();
            for (String oprName : operations)
            {
                opr = new HashMap<String, Object>();
                opr.put("name", oprName);
                opr.put("val", oprRight.indexOf(Constant.CHARVLINE + oprName + Constant.CHARVLINE) >= 0);
                oprs.add(opr);
            }
            result = new HashMap<String, Object>();
            //记录对不同数据类型的操作权限有哪些
            result.put("dataType", dataType);
            result.put("operations", oprs);
            rights.add(result);
        }
        RoleRight rr = new RoleRight(rights);
        ROLE_RIGHTS.put(role, rr);
        
        return rr;
    }
    
    /**
     * 
     * <用户权限>
     * 
     * @author  w00190105
     * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
     * @see  [相关类/方法]
     */
    private static class RoleRight
    {
        /**
         * 有限时间
         */
        private static final long VALID_TIME = 300 * 1000;
        
        /**
         * 记录时间
         */
        private long recTime = 0L;
        
        /**
         * 权限列表
         */
        private List<Map<String, Object>> roleRightList;
        
        public RoleRight(List<Map<String, Object>> roleRightList)
        {
            this.roleRightList = roleRightList;
        }
        
        public boolean isValid()
        {
            if (System.currentTimeMillis() - this.recTime > VALID_TIME)
            { //无论中间是否访问过，60都过期
                return false;
            }
            return true;
        }
        
        public List<Map<String, Object>> getRoleRight()
        {
            return roleRightList;
        }
    }
}
