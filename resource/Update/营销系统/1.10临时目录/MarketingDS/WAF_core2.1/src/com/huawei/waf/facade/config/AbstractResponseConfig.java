package com.huawei.waf.facade.config;

import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.run.AbstractHandler;
import com.huawei.waf.facade.run.AbstractResponser;

public abstract class AbstractResponseConfig {
	private static final Logger LOG = LogUtil.getInstance();
	
	private static final int RESERVED_RESULTCODE = 1000;
	
	private static final String CONFIG_REASONS = "reasons";
    private static final String CONFIG_NOCACHE = "noCache"; //接口的返回都应该nocache
    private static final String CONFIG_CORS = "cors"; //支持的cors可跨域的域名
	
	private Map<Integer, String> reasons = new HashMap<Integer, String>();
	private AbstractResponser responser = null;
    private boolean noCache = false;
    
    private String cors = null;
    
    abstract protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc);
	
	public boolean parseConfig(String version, Map<String, Object> json, MethodConfig mc) {
        if(json == null) {
            return true; //使用默认的配置
        }
        AbstractResponser processor = (AbstractResponser)AbstractHandler.getHandler(json, mc.getResponser());
        if(processor == null) {
            LOG.error("Fail to read responser-processor of {}", mc.getName());
            return false;
        }
        
        mc.setResponser(processor);
        
		if(json.containsKey(CONFIG_REASONS)) {
			Map<String, Object> obj = JsonUtil.getAsObject(json, CONFIG_REASONS);
			int code;
			
			for(Map.Entry<String, Object> one : obj.entrySet()) {
				code = Utils.parseInt(one.getKey(), -1);
				if(code <= RESERVED_RESULTCODE) {
					LOG.error("Fail to parse reason-code:{}, must be bigger than {}", one.getKey(), RESERVED_RESULTCODE);
					return false;
				}
				reasons.put(code, one.getValue().toString());
			}
		}
		
        noCache = JsonUtil.getAsBool(json, CONFIG_NOCACHE, noCache);
        cors = JsonUtil.getAsStr(json, CONFIG_CORS, null);
        if(Utils.isStrEmpty(cors)) { //减少后面空字符串的判断
            cors = null;
        }
		
		return parseExt(version, json, mc);
	}

	public void setResponser(AbstractResponser responser) {
	    this.responser = responser; 
	}
	
    public AbstractResponser setResponser() {
        return responser; 
    }
	
	public Map<Integer, String> getReasons() {
		return reasons;
	}
	
    public String getReason(int resultCode) {
        return reasons.get(resultCode);
    }

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }
    
    /**
     * 如果返回非空，表示当前响应可以被其他域的请求
     * @return
     */
    public String getCors() {
        return cors;
    }
    
    
    /**
     * 是否需要框架处理返回信息
     * @return
     */
    public boolean isNeedResponse() {
        return true;
    }

    //override it when need it
    public void destroy() {
    }
}
