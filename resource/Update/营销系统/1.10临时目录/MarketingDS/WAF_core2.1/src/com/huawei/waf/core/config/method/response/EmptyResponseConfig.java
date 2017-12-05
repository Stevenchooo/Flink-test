package com.huawei.waf.core.config.method.response;

import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;

public class EmptyResponseConfig extends AbstractResponseConfig {
    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        return true;
    }

    /**
     * 是否需要框架处理返回信息
     * @return
     */
    @Override
    public boolean isNeedResponse() {
        return false;
    }
}
