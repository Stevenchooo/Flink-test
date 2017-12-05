package com.huawei.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.sys.WAFConfig;

/**
 * @author l00152046
 * 本地缓存，使用ConcurrentHashMap保存数据，
 * 使用之前，一定要确定数据量不是太大，几万级别
 */
public class LocalCacheUtil {
	protected static final Logger LOG = LogUtil.getInstance();
	
	/**
	 * 配置数据量不会太大，直接使用LinkedHashMap缓存，
	 * 一定比ehcache高效，操作也更方便，
	 * LinkedHashMap已考虑过多线程并发的问题
	 */
	protected static Map<String, LocalCacheEle> localCache = new ConcurrentHashMap<String, LocalCacheEle>();
	
	public static final Object get(String key) {
	    LocalCacheEle ele = localCache.get(key);
	    if(ele == null) {
	        return null;
	    }
	    
	    if(ele.isExpired()) {
	        localCache.remove(key);
	        return null;
	    }
	    return ele.getData();
	}
	
    /**
     * 即使超时，也不删除
     * @param key
     * @return
     */
    public static final Object pick(String key) {
        LocalCacheEle ele = localCache.get(key);
        if(ele == null) {
            return null;
        }
        
        return ele.getData();
    }
    
    public static final Object getAndTouch(String key, int validTime) {
        LocalCacheEle ele = localCache.get(key);
        if(ele == null) {
            return null;
        }
        
        if(ele.isExpired()) {
            localCache.remove(key);
            return null;
        }
        ele.touch(validTime);
        
        return ele.getData();
    }
    
    public static final void put(String key, Object data, int validTime) {
        localCache.put(key, new LocalCacheEle(data, validTime));
    }
	
    public static final void put(String key, Object data) {
        localCache.put(key, new LocalCacheEle(data, WAFConfig.getLocalCacheTime()));
    }
    
    protected static class LocalCacheEle {
		private long validTime = 0;
		private Object data;
		
		public LocalCacheEle(Object data, int validTime) {
		    this.data = data;
		    this.validTime = System.currentTimeMillis() + validTime;
		}
		
		public Object getData() {
		    return data;
		}
		
        public void setData(Object data) {
            this.data = data;
        }
        
        public void touch(int validTime) {
            this.validTime = System.currentTimeMillis() + validTime;
        }
        
        public boolean isExpired() {
            return this.validTime < System.currentTimeMillis();
        }
	}
}
