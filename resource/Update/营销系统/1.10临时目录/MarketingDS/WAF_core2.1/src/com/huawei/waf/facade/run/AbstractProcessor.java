package com.huawei.waf.facade.run;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import org.slf4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractProcessConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig.EHCacheConfig;

import static com.huawei.waf.protocol.RetCode.*;

abstract public class AbstractProcessor extends AbstractHandler {
	private static final Logger LOG = LogUtil.getInstance();
	
    private static final ConcurrentHashMap<String, Future<CacheResultData>> syncTasks = new ConcurrentHashMap<String, Future<CacheResultData>>();
	/**
	 * 实现接口的主要功能，因为实现类在运行前只有一个实例，
	 * 所以必须保证process是能够重入的，即，process不依赖全局的状态
	 * @param context 接口调用的上下文，包括了所有请求参数等
	 * @return RetCode.OK表示成功，其他为失败
	 */
	abstract public int process(MethodContext context);
    
    /**
     * 当所有处理都完成后执行，无论前面执行的是否成功都会执行
     * @param context 会话
     * @return
     */
    abstract public int afterAll(MethodContext context);
    
	/**
	 * 在设置了ehcache的情况，获取cache的key，在不同情况下可以返回空，
	 * 如果返回空，则不会使用cache，与普通的处理相同，在部分情况需要用cache，部分不用的接口中可以重载此函数
	 * @param context
	 * @return
	 */
	protected String getCacheKey(MethodContext context) {
		MethodConfig mc = context.getMethodConfig();
        String methodName = mc.getName();
        ParameterInfo[] params = mc.getRequestConfig().getParameters();
        
        StringBuilder sb = new StringBuilder(methodName);
        String k, v;
        Map<String, Object> reqParams = context.getParameters();
        
        for(ParameterInfo pi : params) {
            if(pi.getIndex() == -1) {
                continue;
            }
            k = pi.getName();
            v = JsonUtil.getAsStr(reqParams, k, "");
            sb.append('&').append(k).append('=').append(v);
        }
        
        return Utils.md5_base64(sb.toString());
	}
	
	/**
	 * 所有接口处理的总入口，在dispatcher中被调用
	 * @param context
	 * @return
	 */
	public int handle(MethodContext context) {
        MethodConfig mc = context.getMethodConfig();
        AbstractProcessConfig config = (AbstractProcessConfig) mc.getProcessConfig();
        
        process: { //使用标签保证单个出口，同时减少if嵌套层次
            EHCacheConfig ecc = config.getEHCacheConfig();
            if(ecc == null) {
                break process;
            }
            
            CacheResultData oldData = null;
            /**
             * 空的情况，不使用缓存，适用于大部分情况需要cache，少部分情况不需要缓存的情况,
             * 这种情况下可以重载getCacheKey函数
             */
            String cacheKey = getCacheKey(context);
        	if(Utils.isStrEmpty(cacheKey)) {
        	    break process;
        	}
        	
    	    Element cv;
    	    
    	    if(ecc.useOldWhenFailed || context.isBusy()) {
    	        if((cv = ecc.cacheHelper.getStore().get(cacheKey)) != null) {
        	        if(ecc.cache.isExpired(cv)) {
        	            //先取出前面的缓存数据
        	            oldData = (CacheResultData)cv.getObjectValue();
        	            cv = null;
        	        } else {
        	            cv = ecc.cache.get(cacheKey); //用于更新统计信息，get了两次，性能稍有下降
        	        }
    	        }
    	    } else {
    	        cv = ecc.cache.get(cacheKey);
    	    }
    	    
	        if(cv != null) {
	        	CacheResultData cd = (CacheResultData)cv.getObjectValue();
	            context.addResults(cd.results);
	            return cd.retCode;
	    	}

	        if(context.isBusy() && oldData != null) {
	            LOG.info("Too busy, {}, use old cache-data", mc.getName());
	            return useOldCache(context, cacheKey, oldData, ecc);
	        }
	        
            /**
             * 当缓存没有命中时，只有第一个运行到此的线程执行process，
             * 其他线程都等待，直到第一个线程超时或执行结束
             */
            return process(context, cacheKey, oldData, ecc);
        }
        
        return process(context);
	}
	
	/**
     * 当cache没有命中时，执行process，执行业务逻辑。
     * 
     * 以下实现未使用synchronized，如果synchronized，
     * 则无论cacheKey是什么，所有没有命中缓存的都会在此堵塞。
     * 使用ConcurrentHashMap保存任务，保证只有第一个线程能够抢到执行权，
     * 其他相同cachekey的线程在f.get处等待，而不同cachekey的线程，不受影响。
     * 这样在实际业务中，可以大幅提升系统的整体表现
	 * @param context
	 * @param cache
	 * @param cacheKey
	 * @param oldData 原来的cache，如果曾经缓存过
	 * @return
	 */
	private int process(final MethodContext context, String cacheKey, CacheResultData oldData, EHCacheConfig ecc) {
        Future<CacheResultData> f = syncTasks.get(cacheKey);
        if (f == null){
            FutureTask<CacheResultData> ft = new FutureTask<CacheResultData>(new Callable<CacheResultData>() {
                @Override
                public CacheResultData call() throws Exception {
                    int retCode = process(context);
                    return new CacheResultData(retCode, context.getResults());
                }
            });
            
            //不会对相同key的值进行覆盖，避免了相同key的任务被重复计算
            f = syncTasks.putIfAbsent(cacheKey, ft); 
            if (f == null) {
                ft.run(); //执行计算
                f = ft;
            }
        }
        
        try {
            String methodName = context.getMethodConfig().getName();
            Cache cache = ecc.cache;
            
            /**
             * 使用future，让后进的线程在此等候，
             * 直到第一个抢到执行权的线程执行完毕。
             */
            CacheResultData cacheData = f.get();
            if(cacheData.retCode == OK) {
                context.addResults(cacheData.results);
                cache.put(new Element(cacheKey, cacheData));
                LOG.info("Cache not hits, {}, process ok", methodName);
                return OK;
            }
            
            //刷新数据的操作失败了，如果缓存中有老数据，则使用老数据
            if(oldData != null) {
                LOG.info("Cache not hits, {}, use old cache-data", methodName);
                return useOldCache(context, cacheKey, oldData, ecc);
            }
            
            //处理失败，返回失败结果
            context.addResults(cacheData.results);
            LOG.info("Cache not hits, {}, process failed, retCode={}", methodName, cacheData.retCode);
            return cacheData.retCode;
        } catch (Exception e){
            LOG.error("Fail to get result from future task", e);
            return context.setResult(INTERNAL_ERROR, "Fail to process when cache not hits");
        } finally {
            syncTasks.remove(cacheKey);
        }
	}
	
	private int useOldCache(MethodContext context, String cacheKey, CacheResultData oldData, EHCacheConfig ecc) {
	    Cache cache = ecc.cache;
        if(ecc.cacheOldTime > 0) {
            cache.put(new Element(cacheKey, oldData, ecc.cacheOldTime, ecc.cacheOldTime));
        }
        
        context.addResults(oldData.results);
        return oldData.retCode;
	}
	
	/**
	 * 添加新任务，便于自定义processor处理互斥
	 * @param key
	 * @param ft
	 * @return
	 */
	protected static Future<CacheResultData> addTask(String key, FutureTask<CacheResultData> ft) {
        Future<CacheResultData> f = syncTasks.get(key);
        if (f == null){
            //不会对相同key的值进行覆盖，避免了相同key的任务被重复计算
            f = syncTasks.putIfAbsent(key, ft); 
            if (f == null) {
                ft.run(); //执行计算
                f = ft;
            }
        }
        
        return f;
	}
	
    /**
     * 任务执行完毕后，必须删除，否则业务逻辑会出现混乱
     * @param key
     */
    protected static void removeTask(String key) {
        syncTasks.remove(key);
    }
	
	/**
	 * 在登录时已确定了通行证的检查类型，但是可以通过重载此函数改变checkType
	 * @return
	 */
	public int getCheckType() {
		return IMethodAide.CHECK_TYPE_NULL;
	}
	
	public static class CacheResultData {
	    public final int retCode;
	    public final Map<String, Object> results;
		
		public CacheResultData(int retCode, Map<String, Object> results) {
			this.retCode = retCode;
			this.results = results;
		}
	}
}
