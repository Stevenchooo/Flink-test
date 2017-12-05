package com.huawei.manager.base.config.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.SelfRDBProcessConfig;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class AuthPageProcessConfig extends SelfRDBProcessConfig implements IAuthConfig
{
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String CONFIG_PARA = "scopePara";
    
    private String scopePara = "id";
    
    /**
     * 解析配置信息
     * @param ver     配置信息
     * @param json    json对象
     * @param mc      配置方法
     * @return        解析是否成功
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc)
    {
        if (!super.parseExt(ver, json, mc))
        {
            LOG.error("Fail to parse super config in method {}", mc.getName());
            return false;
        }
        scopePara = JsonUtil.getAsStr(json, CONFIG_PARA, scopePara);
        return true;
    }
    
    /**
     * 获取scopePara
     * @return     scopePara
     */
    public String getScopePara()
    {
        return scopePara;
    }
}
