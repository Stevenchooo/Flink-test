package com.huawei.waf.core.config.method.response;

import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;

public class JsonResponseConfig extends AbstractResponseConfig {
    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        setNoCache(true);
        return true;
    }
}
