package com.huawei.manager.base.run.process;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.huawei.manager.base.config.process.IAuthConfig;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractRequestConfig;
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
public class AuthRDBProcessor extends AuthProcessor
{
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String LOG_SQL = "insert into t_ms_log(account,apiName,resultCode,info) values(?,?,?,?)";
    
    /**
     * 处理前
     * @param context   上下文
     * @param dbConn      数据库链接
     * @return           是否成功
     */
    @Override
    public int process(MethodContext context, DBUtil.DBConnection dbConn)
    {
        int retCode = auth(context, dbConn);
        if (retCode != RetCode.OK)
        {
            LOG.error("Fail to auth, retCode={}", retCode);
            return retCode;
        }
        return super.process(context, dbConn);
    }
    
    /**
     * 有没有权限
     * @param context   上下文
     * @param conn      数据库链接
     * @return           是否成功
     */
    public static final int auth(MethodContext context, DBConnection conn)
    {
        int retCode = RetCode.OK;
        
        MethodConfig mc = context.getMethodConfig();
        IAuthConfig ac = (IAuthConfig)mc.getProcessConfig();
        String dataTypes = mc.getDataType();
        if (mc.getAuthType() == MethodConfig.AUTH_USER && dataTypes != null)
        {
            int pid = JsonUtil.getAsInt(context.getParameters(), ac.getScopePara(), 0);
            if (pid == 0)
            {
                context.setResult(RetCode.WRONG_PARAMETER, "Invalid scope parameter used");
                LOG.error("Fail to get {} from parameters", ac.getScopePara());
                return RetCode.WRONG_PARAMETER;
            }
            
            String[] ss = dataTypes.split(Constant.STRSPLITLINE);
            String operation = Constant.STRVLINE + mc.getOperationType() + Constant.CHARVLINE;
            String name =
                "sp_checkRight?" + context.getAccount() + Constant.CHARCOMMA + pid + Constant.CHARCOMMA + operation;
                
            for (String meta : ss)
            {
                String k = Utils.md5_base64(name + Constant.CHARCOMMA + meta);
                if (Utils.parseBool(getRight(k), false))
                {
                    return RetCode.OK;
                }
                
                retCode = DBUtil.execute(conn,
                    "{call sp_checkRight(?,?,?,?,?)}",
                    true,
                    new Object[] {context.getAccount(), pid, meta, operation});
                    
                if (retCode == RetCode.OK)
                {
                    saveRight(k, true);
                    return retCode;
                }
                saveRight(k, false);
                
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("No right for ({}{}) under {}, retCode is {}", meta, operation, pid, retCode);
                }
            }
        }
        return retCode;
    }
    
    /**
     * 有没有权限
     * @param context   上下文
     * @return           是否成功
     */
    public static final int auth(MethodContext context)
    {
        DBConnection dbConn = null;
        try
        {
            dbConn = DBUtil.getConnection(DBUtil.getDefaultSource(), false);
            /**
             * 鉴权，通过范围与数据类型+操作类型判断权限
             */
            return auth(context, dbConn);
        }
        finally
        {
            DBUtil.freeConnection(dbConn, true);
        }
    }
    
    /**
     * 写日志
     * @param context   上下文
     * @param conn      数据库链接
     */
    public static final void writeLog(MethodContext context, Connection conn)
    {
        StringBuilder info = new StringBuilder();
        MethodConfig mc = context.getMethodConfig();
        AbstractRequestConfig rc = mc.getRequestConfig();
        
        if (rc != null)
        {
            info.append("REQUEST");
            buildParams(rc.getParameters(), context.getParameters(), info);
        }
        else
        {
            info.append("NO REQUEST");
        }
        
        Map<String, Object> resp = context.getResults();
        if (resp != null)
        {
            Map<String, Object> filterResp = new HashMap<String, Object>(resp);
            Set<String> keys = resp.keySet();
            
            for (String key : keys)
            {
                if (key.toLowerCase(Locale.CHINA).indexOf("password") >= 0)
                {
                    filterResp.remove(key);
                }
            }
            info.append(",RESPONSE").append(Constant.LEFTMID).append(filterResp.toString()).append(Constant.RIGHTMID);
        }
        else
        {
            info.append(",NO RESPONSE");
        }
        
        String infoStr = info.toString();
        DBUtil.execute(conn,
            LOG_SQL,
            false,
            new Object[] {context.getAccount(), mc.getName(), context.getResultCode(),
                infoStr.substring(0, Math.min(infoStr.length(), 255))});
    }
    
    /**
     * <构造参数>
     * @param infos   消息内容
     * @param params  map对象
     * @param sb      buffer
     * @see [类、类#方法、类#成员]
     */
    private static void buildParams(ParameterInfo[] infos, Map<String, Object> params, StringBuilder sb)
    {
        if (params == null)
        {
            return;
        }
        
        String name, val;
        
        sb.append(Constant.LEFTMID);
        for (ParameterInfo pi : infos)
        {
            if (!pi.isCanLog()) // 有部分字段不可以写入日志，比如密码
            {
                continue;
            }
            
            name = pi.getName();
            val = JsonUtil.getAsStr(params, name, null);
            if (val != null)
            {
                sb.append(name).append(':').append(val);
            }
            else
            {
                sb.append(name).append(":null");
            }
            sb.append(Constant.CHARCOMMA);
        }
        sb.append(Constant.RIGHTMID);
    }
    
    /**
     * 处理完成
     * @param context  上下文
     * @return         接口返回值
     */
    @Override
    public int afterAll(MethodContext context)
    {
        if (context.getMethodConfig().isCanLog())
        {
            writeLog(context);
        }
        
        return RetCode.OK;
    }
    
    /**
     * 写日志
     * @param context  上下文
     * @return         接口返回值
     */
    public static final int writeLog(MethodContext context)
    {
        DBConnection dbConn = null;
        try
        {
            dbConn = DBUtil.getConnection(DBUtil.getDefaultSource(), false);
            Connection conn = dbConn.getConnection();
            if (conn == null)
            {
                LOG.error("Fail to get db connection");
                return RetCode.INTERNAL_ERROR;
            }
            AuthRDBProcessor.writeLog(context, conn);
        }
        finally
        {
            DBUtil.freeConnection(dbConn, true);
        }
        return RetCode.OK;
    }
}
