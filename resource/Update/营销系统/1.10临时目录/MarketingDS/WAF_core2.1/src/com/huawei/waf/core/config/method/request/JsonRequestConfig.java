package com.huawei.waf.core.config.method.request;

import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractRequestConfig;

public class JsonRequestConfig extends AbstractRequestConfig {
    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        return true;
    }
}
