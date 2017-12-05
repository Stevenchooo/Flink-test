package com.huawei.waf.core.config.method.process;

import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig;

public class UploadProcessConfig extends AbstractProcessConfig implements IUploadProcessConfig {
    private UploadProcessInfo uploadProcessInfo;
    
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        this.uploadProcessInfo = UploadProcessInfo.parse(ver, json, mc);
        
        return this.uploadProcessInfo != null;
    }

    @Override
    public UploadProcessInfo getUploadProcessInfo() {
        return uploadProcessInfo;
    }
}
