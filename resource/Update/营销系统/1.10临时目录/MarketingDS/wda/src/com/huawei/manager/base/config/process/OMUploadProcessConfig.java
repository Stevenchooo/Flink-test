package com.huawei.manager.base.config.process;

import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.UploadRDBProcessConfig;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see [相关类/方法]
 */
public class OMUploadProcessConfig extends UploadRDBProcessConfig
        implements IAuthConfig
{
    private static final String PARAMETER_SCOPE_PARA = "scopePara";

    private static final String PARAMETER_SCOPE_HONOR = "scopeHonor";

    private String scopePara = "scopeId";

    private String scopeHonor = "scopeId1";

    /**
     * 获取配置信息
     * 
     * @param ver
     *            配置字符串
     * @param json
     *            json对象
     * @param mc
     *            方法配置
     * @return 是否成
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json,
            MethodConfig mc)
    {
        if (!super.parseExt(ver, json, mc))
        {
            return false;
        }
        this.scopePara = JsonUtil.getAsStr(json, PARAMETER_SCOPE_PARA,
                "scopeId");

        this.scopeHonor = JsonUtil.getAsStr(json, PARAMETER_SCOPE_HONOR,
                "scopeId1");

        return true;
    }

    /**
     * 获取scopePara
     * 
     * @return scopePara
     */
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
