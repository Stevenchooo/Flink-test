package com.huawei.waf.core.config.method.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig;

/**
 * @author l00152046
 * 基本处理配置
 */
public class GwCallProcessConfig extends AbstractProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();

    private static final String CONFIG_API_NAME = "apiName";
	private static final String CONFIG_RETCODE_NAME = "retCodeName";
	
	private String apiName;
	
	private String retCodeName = "retCode"; //返回码名称，默认为retCode
	
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
    	this.apiName = JsonUtil.getAsStr(json, CONFIG_API_NAME, null);
    	if(Utils.isStrEmpty(this.apiName)) {
    		LOG.error("There no {} config", CONFIG_API_NAME);
    		return false;
    	}
    	this.retCodeName = JsonUtil.getAsStr(json, CONFIG_RETCODE_NAME, this.retCodeName);
    	
        return true;
    }
    
    /**
     * api名称，在网关上注册的接口名称
     * @return
     */
    public String getApiName() {
    	return apiName;
    }
    
    /**
     * 返回码名称，默认为retCode
     * @return
     */
    public String getRetCodeName() {
    	return retCodeName;
    }

    @Override
	protected boolean isCanBeNull() {
		return false;
	}
}
