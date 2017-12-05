package com.huawei.waf.core.config.method.process;

import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;

public class UploadRDBProcessConfig extends RDBProcessConfig implements IUploadProcessConfig {
    private UploadProcessInfo uploadProcessInfo;
    
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        if(!super.parseExt(ver, json, mc)) {
            return false;
        }
        this.uploadProcessInfo = UploadProcessInfo.parse(ver, json, mc);
        
        return this.uploadProcessInfo != null;
    }

    @Override
    public UploadProcessInfo getUploadProcessInfo() {
        return uploadProcessInfo;
    }
}
