package com.huawei.waf.core.config.sys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.HttpUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;

@SuppressWarnings("unchecked")
public class SysConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String CONFIGCENTER = "configCenter";
    private static final String RDBCONFIG = "rdb";
    private static final String MEMCACHEDCONFIG = "memcached";
    private static final String FREEMARKERCONFIG = "freemarker";
    private static final String HS4JCONFIG = "hs4j";
    private static final String TIMERCONFIG = "timer";
    private static final String WAFCONFIG = "waf";
    private static final String SECURITYCONFIG = "security";
    private static final String GWCONFIG = "gw"; //网关相关的配置
    
    private static Map<String, Object> configs = null;
    private static List<TimerConfig> timerConfigs = new ArrayList<TimerConfig>();
    
    private SysConfig() {} //外部不容许直接调用
    
    public static final boolean readConfigs(File configFile) {
        configs = JsonUtil.jsonFileToMap(configFile);
        if(configs == null) {
            return false;
        }
        
        if(!SecurityConfig.init(JsonUtil.getAsObject(configs, SECURITYCONFIG))) {
            LOG.error("Fail to load {}", SECURITYCONFIG);
            return false;
        }
        
        String configCenter = JsonUtil.getAsStr(configs, CONFIGCENTER);
        if(!Utils.isStrEmpty(configCenter)) {
        	if(!readConfigCenter(configCenter, configFile)) {
                LOG.warn("Fail to read configs from config-center {}, use local configs instead", configCenter);
        	}
        }
        
        Object o = configs.get(FREEMARKERCONFIG);
        if(o == null || !(o instanceof Map<?, ?>)) {
            LOG.info("no {} config", FREEMARKERCONFIG);
            configs.put(FREEMARKERCONFIG, new HashMap<String, Object>());
        }
        
        o = configs.get(TIMERCONFIG);
        if(o != null && o instanceof List<?>) {
            TimerConfig tc;
            List<Map<String, Object>> tcfgs = (List<Map<String, Object>>)o;
            
            for(Map<String, Object> one : tcfgs) {
                if((tc = TimerConfig.parse(one)) == null) {
                    LOG.error("Wrong timer config, it was ignored:{}", one);
                } else {
                    timerConfigs.add(tc);
                }
            }
        }
        
        /**
         * WAF系统运行相关的配置 
         */
        if(!WAFConfig.parse(JsonUtil.getAsObject(configs, WAFCONFIG))) {
            LOG.error("Fail to load {}", WAFCONFIG);
            return false;
        }
        
        return true;
    }
    
    public static final  Map<String, Object> getRdbConfigs() {
        return JsonUtil.getAsObject(configs, RDBCONFIG);
    }
    
    public static final List<Map<String, Object>> getMemcachedConfigs() {
        return getArrConfig(MEMCACHEDCONFIG);
    }
    
    public static final Map<String, Object> getFreemarkerConfigs() {
        Map<String, Object> cfg = JsonUtil.getAsObject(configs, FREEMARKERCONFIG);
        return cfg != null ? cfg : new HashMap<String, Object>();
    }
    
    public static final List<TimerConfig> getTimerConfigs() {
        return timerConfigs;
    }
    
    public static final Map<String, Object> getHS4JConfigs() {
        return JsonUtil.getAsObject(configs, HS4JCONFIG);
    }
    
    public static final Map<String, Object> getGwConfigs() {
        return JsonUtil.getAsObject(configs, GWCONFIG);
    }
    
    private static final List<Map<String, Object>> getArrConfig(String name) {
        Object o = configs.get(name);
        if(o instanceof List<?>) {
            return (List<Map<String, Object>>)o;
        }
        return null;
    }
    
    public static final Map<String, Object> getConfig(String name) {
        Object o = configs.get(name);
        if(o instanceof Map<?,?>) {
            return (Map<String, Object>)o;
        }
        return null;
    }
    
    private static final boolean readConfigCenter(String configCenter, File configFile) {
    	HttpResponse cfgResp = HttpUtil.get(configCenter, 10 * 1000);
    	if(cfgResp == null) {
    		return false;
    	}
    	
		String cfgContent = HttpUtil.getString(cfgResp);
		if(Utils.isStrEmpty(cfgContent)) {
			return false;
		}
    	
		Map<String, Object> cfgs = JsonUtil.jsonToMap(cfgContent);
		if(cfgs == null || cfgs.size() <= 0) { 
			return false;
		}
		//如果远程有内容，则覆盖本地的配置
		configs.putAll(cfgs);
		if(!FileUtil.writeFile(configFile, JsonUtil.mapToBytes(configs))) {
			LOG.error("Fail to write system config file {}", configFile);
		}
		
    	return true;
    }
    
    public static String getConfigValue(String name) {
        Object v = JsonUtil.getFromMap(configs, name);
        if(v == null) {
            return "";
        } 
        return Utils.parseString(v, "");
    }
}
