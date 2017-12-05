package com.huawei.manager.base.config.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.SelfRDBProcessConfig;

/**
 * 需要认证的页面处理，在返回页面之前，需要通过用户角色来鉴权
 * 
 * @author l00152046
 *
 */
public class AuthPageProcessConfig extends SelfRDBProcessConfig
        implements IAuthConfig
{
    private static final Logger LOG = LogUtil.getInstance();

    private static final String CONFIG_PARA = "scopePara";

    private static final String CONFIG_HONOR = "scopeHonor";

    private String scopePara = "id";

    private String scopeHonor = "id";

    /**
     * 解析配置信息
     * 
     * @param ver
     *            配置信息
     * @param json
     *            json对象
     * @param mc
     *            配置方法
     * @return 解析是否成功
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json,
            MethodConfig mc)
    {
        if (!super.parseExt(ver, json, mc))
        {
            LOG.error("Fail to parse super config in method {}", mc.getName());
            return false;
        }
        scopePara = JsonUtil.getAsStr(json, CONFIG_PARA, scopePara);
        scopeHonor = JsonUtil.getAsStr(json, CONFIG_HONOR, scopeHonor);
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
}
