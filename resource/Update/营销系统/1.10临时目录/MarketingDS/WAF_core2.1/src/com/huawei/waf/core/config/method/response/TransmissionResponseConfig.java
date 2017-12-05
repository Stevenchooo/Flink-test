package com.huawei.waf.core.config.method.response;

import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;
import com.huawei.waf.protocol.Const;

public class TransmissionResponseConfig extends AbstractResponseConfig {
    private static final String CONFIG_CONTENTTYPE = "contentType";
    
    private String contentType = Const.XML_CONTENT_TYPE; //缓存时间，单位秒 

    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
    	this.contentType = JsonUtil.getAsStr(json, CONFIG_CONTENTTYPE, this.contentType);
        return true;
    }
    
    public String getContentType() {
        return contentType;
    }
}
