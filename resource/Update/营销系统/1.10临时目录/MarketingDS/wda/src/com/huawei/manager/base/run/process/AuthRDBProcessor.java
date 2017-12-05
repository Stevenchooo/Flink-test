package com.huawei.manager.base.run.process;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.huawei.manager.base.config.process.AuthRDBProcessConfig;
import com.huawei.manager.base.config.process.IAuthConfig;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.EncryptUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.parameter.StringParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.protocol.RetCode;

/**
 * 处理类
 * 
 * @author w00296102
 */
public class AuthRDBProcessor extends AuthProcessor
{
    private static final Logger LOG = LogUtil.getInstance();

    private static final String LOG_SQL = "insert into t_ms_log(account,apiName,resultCode,info) values(?,?,?,?)";

    private static final String[] RISK_SQL =
    {"sp_pages_list", "sp_pages_summary", "sp_pages_trend_kpi",
            "sp_pages_trend_list", "sp_pages_trend_summary",
            "sp_visitor_area_distribution",
            "sp_visitor_area_distribution_list",
            "sp_visitor_area_distribution_map", "sp_visitor_trend_kpi",
            "sp_visitor_trend_list", "sp_visitor_trend_summary",
            "sp_website_referrer_kpi_line", "sp_website_referrer_kpi_pie",
            "sp_website_referrer_promotion_kpi_line",
            "sp_website_referrer_promotion_kpi_pie",
            "sp_website_referrer_promotion_list",
            "sp_website_referrer_promotion_summary",
            "sp_website_referrer_summary", "sp_website_referrer_tree_data",
            "sp_website_referrer_trend_kpi", "sp_website_referrer_trend_list",
            "sp_website_referrer_trend_summary",
            "sp_website_referrer_trend2_kpi",
            "sp_website_referrer_trend2_list",
            "sp_website_referrer_trend2_summary", "sp_website_summary",
            "sp_website_summary_kpi", "sp_website_summary_topAreaDistribution",
            "sp_website_summary_topPages", "sp_website_summary_topReferrer",
            "sp_website_trend_kpi", "sp_website_trend_list",
            "sp_website_trend_summary", "sp_pages_updown_summary",
            "sp_visitor_area_userinsight_age",
            "sp_visitor_area_userinsight_industry",
            "sp_visitor_area_userinsight_media",
            "sp_visitor_area_userinsight_sex"};

    /**
     * 运行方法
     * 
     * @param context
     *            方法的配置信息
     * @param dbConn
     *            数据库对象
     * 
     * @return 返回码
     */
    @Override
    public int process(MethodContext context, DBUtil.DBConnection dbConn)
    {
        MethodConfig mc = context.getMethodConfig();
        AbstractRequestConfig rc = mc.getRequestConfig();
        ParameterInfo[] params = rc.getParameters();
        AuthRDBProcessConfig config = (AuthRDBProcessConfig) mc
                .getProcessConfig();
        String sql = config.getSQL();
        for (String call : RISK_SQL)
        {
            if (sql.startsWith("{call " + call + "("))
            {
                // 风险sql
                for (ParameterInfo pi : params)
                {
                    if (pi instanceof StringParameterInfo)
                    {
                        String a = (String) context.getParameter(pi.getName());
                        if (pi.getIndex() == 0)
                        {
                            try
                            {
                                a = URLDecoder.decode(a, "UTF-8");
                            }
                            catch (UnsupportedEncodingException e)
                            {
                                LOG.error("AuthRDBProcessor UnsupportedEncodingException");
                            }
                        }
                        if (a.contains("'"))
                        {
                            LOG.error("Fail to auth, retCode={}",
                                    RetCode.DB_ERROR);
                            return RetCode.DB_ERROR;
                        }
                    }
                }
                break;
            }
        }

        int retCode = auth(context, dbConn);
        if (retCode != RetCode.OK)
        {
            LOG.error("Fail to auth, retCode={}", retCode);
            return retCode;
        }
        return super.process(context, dbConn);
    }

    /**
     * 鉴权
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int 返回码
     */
    public static final int auth(MethodContext context, DBConnection conn)
    {
        if (null == conn)
        {
            LOG.error("Fail to get conn");
            return RetCode.DB_ERROR;
        }
        int retCode = RetCode.OK;

        MethodConfig mc = context.getMethodConfig();
        IAuthConfig ac = (IAuthConfig) mc.getProcessConfig();
        // scopeId不为null，就只有admin123才能用
        if (null != ac.getScopePara())
        {
            // String userAccount = context.getAccount();
            String userAccount = (String) (context.getRequest().getSession()
                    .getAttribute("principal_name"));
            if (null == userAccount)
            {
                return RetCode.NO_RIGHT;
            }
            userAccount = EncryptUtil.configDecode(userAccount);
            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "select isAdmin from t_ms_user where account = ?",
                    new Object[]
                    {userAccount});
            List<Map<String, Object>> userJson = res.getResult().get(0);

            if (Integer.parseInt((String) userJson.get(0).get("isAdmin")) == 0)
            {
                return RetCode.NO_RIGHT;
            }
        }

        if (null != ac.getScopeHonor())
        {
            // String userAccount = context.getAccount();
            String userAccount = (String) (context.getRequest().getSession()
                    .getAttribute("principal_name"));
            if (null == userAccount)
            {
                return RetCode.NO_RIGHT;
            }
            userAccount = EncryptUtil.configDecode(userAccount);
            DBQueryResult res = DBUtil.query(conn.getConnection(),
                    "select isHonor from t_ms_user where account = ?",
                    new Object[]
                    {userAccount});
            List<Map<String, Object>> userJson = res.getResult().get(0);

            if (Integer.parseInt((String) userJson.get(0).get("isHonor")) == 0)
            {
                return RetCode.NO_RIGHT;
            }
        }
        return retCode;

    }

    /**
     * 鉴权
     * 
     * @param context
     *            context
     * @return int 返回码
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
     * 记录
     * 
     * @param context
     *            context
     * @param conn
     *            conn
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
                if (key.toLowerCase(Locale.US).indexOf("password") >= 0)
                {
                    filterResp.remove(key);
                }
            }
            info.append(",RESPONSE").append('[').append(filterResp.toString())
                    .append(']');
        }
        else
        {
            info.append(",NO RESPONSE");
        }

        String infoStr = info.toString();
        DBUtil.execute(conn, LOG_SQL, false,
                new Object[]
                {context.getAccount(), mc.getName(), context.getResultCode(),
                        infoStr.substring(0, Math.min(infoStr.length(), 255))});
    }

    private static void buildParams(ParameterInfo[] infos,
            Map<String, Object> params, StringBuilder sb)
    {
        if (params == null)
        {
            return;
        }

        String name, val;

        sb.append('[');
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
            sb.append(',');
        }
        sb.append(']');
    }

    /**
     * 结束后
     * 
     * @param context
     *            context
     * @return 返回码
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
     * 记录
     * 
     * @param context
     *            context
     * @return 返回码
     */
    public static final int writeLog(MethodContext context)
    {
        DBConnection dbConn = null;
        try
        {
            dbConn = DBUtil.getConnection(DBUtil.getDefaultSource(), false);
            if (null == dbConn)
            {
                LOG.error("Fail to get dbConn");
                return RetCode.INTERNAL_ERROR;
            }
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
