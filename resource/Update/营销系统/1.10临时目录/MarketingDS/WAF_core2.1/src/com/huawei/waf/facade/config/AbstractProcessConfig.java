package com.huawei.waf.facade.config;

import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.CacheStoreHelper;
import net.sf.ehcache.Element;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.run.AbstractHandler;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.core.WAFException;

public abstract class AbstractProcessConfig {
	private static final Logger LOG = LogUtil.getInstance();

	private static final String CONFIG_EHCACHE = "ehcache"; //ehcache的名称
    
    private EHCacheConfig ehCacheConfig;
    
	/**
	 * Override it to parse more information if needed
	 * @param ver
	 * @param json
	 * @return
	 */
	abstract protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc);
	
	public boolean parseConfig(String version, Map<String, Object> json, MethodConfig mc) {
        if(json == null) {
        	if(isCanBeNull()) {
                return true; //使用默认的配置
        	}
        	LOG.error("process config can't be null in {}", mc.getName());
        	return false;
        }
        AbstractProcessor processor = (AbstractProcessor)AbstractHandler.getHandler(json, mc.getProcessor());
        
        if(processor == null) {
            LOG.error("Fail to parse processor-config of {}", mc.getName());
            return false;
        }
        
        mc.setProcessor(processor);
		
    	if(json.containsKey(CONFIG_EHCACHE)) {
	    	CacheManager manager = AbstractInitializer.getCacheManager();
	    	if(manager == null) {
	    		LOG.error("EHCache not started");
	    		return false;
	    	}
	    	
	    	try {
                this.ehCacheConfig = new EHCacheConfig(json.get(CONFIG_EHCACHE), manager);
            } catch (WAFException e) {
                LOG.error("Fail to parse {} config", CONFIG_EHCACHE, e);
                return false;
            }
    	}
    	
		if(!parseExt(version, json, mc)) {
			LOG.error("Fail to parse process config");
			return false;
		}
        return true;
	}
	
	//override it when need it
	public void destroy() {
	}
	
	/**
	 * process配置是否可以使用默认值
	 * @return
	 */
	protected boolean isCanBeNull() {
		return true;
	}
    
    /**
     * 如果需要缓存，则配置ehcache
     * @return
     */
    public EHCacheConfig getEHCacheConfig() {
        return ehCacheConfig;
    }
    
    public static class EHCacheConfig {
        private static final String CONFIG_NAME = "name"; //ehcache的名称
        private static final String CONFIG_USEOLDWHENFAILED = "useOldWhenFailed";
        private static final String CONFIG_CACHEOLD_TIME = "cacheOldTime";
        
        public final CacheStoreHelper cacheHelper; //cacheHelper用于获取超期的element
        public final Cache cache;
        public final boolean useOldWhenFailed; //当cache超期，处理又失败的情况，使用前面的缓存
        public final int cacheOldTime; //如果大于0，表示即使处理失败，仍然cache，默认为0
        
        @SuppressWarnings("unchecked")
        protected EHCacheConfig(Object config, CacheManager manager) throws WAFException {
            String cacheName;
            
            if(config instanceof Map<?,?>) {
                Map<String, Object> cfg = (Map<String, Object>)config;
                this.cacheOldTime = JsonUtil.getAsInt(cfg, CONFIG_CACHEOLD_TIME, 0);
                if(this.cacheOldTime <= 0) {
                    this.useOldWhenFailed = JsonUtil.getAsBool(cfg, CONFIG_USEOLDWHENFAILED, true);
                } else {
                    this.useOldWhenFailed = true; //设置了超时保存时间，则失败时一定会使用老数据
                }
                cacheName = JsonUtil.getAsStr(cfg, CONFIG_NAME, "");
            } else if(config instanceof String){
                cacheName = (String)config;
                this.cacheOldTime = 0;
                this.useOldWhenFailed = true;
            } else {
                throw new WAFException("Invalid ehcahce content, should be string or object");
            }
            
            this.cache = manager.getCache(cacheName);
            if(this.cache == null) {
                throw new WAFException("EHCache not started, or " + cacheName + " not exists in ehcache.xml");
            }
            this.cacheHelper = new CacheStoreHelper(this.cache);
        }
        
        /**
         * 获取，如果cache已超时，则删除之，并返回null
         * @param key
         * @return
         */
        public Element get(String key) {
            return cache.get(key);
        }
        
        /**
         * 获取，但是不删除超期的cache
         * @param key
         * @return
         */
        public Element pick(String key) {
            return cacheHelper.getStore().get(key);
        }
        
        public boolean isExpired(Element e) {
            return cache.isExpired(e);
        }
    }
}
