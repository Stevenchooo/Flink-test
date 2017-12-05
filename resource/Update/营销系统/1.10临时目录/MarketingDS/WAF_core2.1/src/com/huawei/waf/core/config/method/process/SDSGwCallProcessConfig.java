package com.huawei.waf.core.config.method.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;

/**
 * @author l00152046
 * sds结构化存储的处理配置
 */
public class SDSGwCallProcessConfig extends GwCallProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();

    private static final String CONFIG_TABLE_NAME = "tableName";
	
	private String tableName;
	
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
    	if(!super.parseExt(ver, json, mc)) {
    		return false;
    	}
    	
    	this.tableName = JsonUtil.getAsStr(json, CONFIG_TABLE_NAME, null);
    	if(Utils.isStrEmpty(this.tableName)) {
    		LOG.error("There no {} config", CONFIG_TABLE_NAME);
    		return false;
    	}
    	
        return true;
    }
    
    /**
     * sds的表名称
     * @return
     */
    public String getTableName() {
    	return tableName;
    }
}
