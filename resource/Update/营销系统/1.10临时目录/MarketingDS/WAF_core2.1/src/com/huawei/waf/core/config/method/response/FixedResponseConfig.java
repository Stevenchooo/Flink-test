package com.huawei.waf.core.config.method.response;

import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;
import com.huawei.waf.protocol.Const;

public class FixedResponseConfig extends AbstractResponseConfig {
    private static final String CONFIG_CONTENT = "content";
    
    private byte[] content;
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        setNoCache(false);
        content = JsonUtil.getAsStr(json, CONFIG_CONTENT, "").getBytes(Const.DEFAULT_CHARSET);
        return true;
    }
    
    public byte[] getContent() {
        return content;
    }
}
