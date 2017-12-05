package com.huawei.manager.base.config.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;

/**
 * 需要认证的数据库处理，在执行sql之前，需要通过用户角色来鉴权
 * 
 * @author l00152046
 *
 */
public class AuthRDBProcessConfig extends RDBProcessConfig
        implements IAuthConfig
{
    private static final Logger LOG = LogUtil.getInstance();

    private static final String CONFIG_PARA = "scopePara";

    private static final String CONFIG_HONOR = "scopeHonor";

    // meta->rights, example user->c,r,u,d
    private static final Map<String, List<String>> RIGHTS = new HashMap<String, List<String>>();

    private String scopePara = null;

    private String scopeHonor = null;

    /**
     * <权限初始化>
     * 
     * @see [类、类#方法、类#成员]
     */
    public static final void initRights()
    {
        DBConnection dbConn = DBUtil.getConnection(false);
        if (null == dbConn)
        {
            LOG.debug("dbConn error");
            return;
        }
        List<Map<String, Object>> list = DBUtil.query(dbConn,
                "select name,isRightCtrl from t_ms_meta", false, new Object[0]);

        LOG.info("=====Read metas====");
        if (list == null || list.isEmpty())
        {
            return;
        }

        String meta;
        List<String> metas = new ArrayList<String>();
        List<String> rr;

        for (Map<String, Object> one : list)
        {
            // if(!JsonUtil.getAsBool(one, "isRightCtrl")) {
            // continue;
            // }
            meta = JsonUtil.getAsStr(one, "name");

            // 根目录
            if ("root".equals(meta))
            {
                continue;
            }

            if (!RIGHTS.containsKey(meta))
            {
                rr = new ArrayList<String>();
                rr.add("r");
                RIGHTS.put(meta, rr);
            }

            metas.add(meta);
            LOG.info("metas:{}", metas.toString());
        }

    }

    /**
     * 解析配置信息
     * 
     * @param ver
     *            配置信息
     * @param json
     *            json对象
     * @param mc
     *            方法配置
     * @return 是否正确
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json,
            MethodConfig mc)
    {
        if (!super.parseExt(ver, json, mc))
        {
            return false;
        }
        scopePara = JsonUtil.getAsStr(json, CONFIG_PARA, scopePara);
        scopeHonor = JsonUtil.getAsStr(json, CONFIG_HONOR, scopeHonor);
        String dataType = mc.getDataType();
        String oprType = mc.getOperationType();
        if (!Utils.isStrEmpty(dataType) && !Utils.isStrEmpty(oprType))
        {
            addRight(dataType, oprType);
        }

        return true;
    }

    @Override
    public String getScopePara()
    {
        return scopePara;
    }

    @Override
    public String getScopeHonor()
    {
        return scopeHonor;
    }

    /**
     * 操作的数据类型及操作类型
     * 
     * @return RIGHTS
     */
    public static Map<String, List<String>> getRights()
    {
        return RIGHTS;
    }

    /**
     * 支持一个接口属于多个meta的情况，多个meta以竖线分隔
     * 
     * @param dataType
     *            数据类型，比如用户数据
     * @param operation
     *            操作类型，比如c/r/u/d
     */
    public static void addRight(String dataType, String operation)
    {
        String ss[] = dataType.split("\\|");

        for (String s : ss)
        {
            s = s.trim();
            List<String> oprs = RIGHTS.get(s);
            if (oprs == null)
            {
                oprs = new ArrayList<String>();
                RIGHTS.put(s, oprs);
            }

            if (oprs.contains(operation))
            {
                return; // 已存在，不必增加
            }
            oprs.add(operation);
            Collections.sort(oprs);
        }
    }
}
