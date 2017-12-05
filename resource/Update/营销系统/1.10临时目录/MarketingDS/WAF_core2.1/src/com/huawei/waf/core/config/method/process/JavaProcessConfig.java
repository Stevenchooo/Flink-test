package com.huawei.waf.core.config.method.process;

import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig;

/**
 * @author l00152046
 * 基本处理配置
 */
public class JavaProcessConfig extends AbstractProcessConfig {
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        return true;
    }
}
