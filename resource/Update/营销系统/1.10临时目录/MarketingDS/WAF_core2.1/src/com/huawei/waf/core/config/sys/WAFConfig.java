package com.huawei.waf.core.config.sys;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.huawei.util.EncryptUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.run.SessionMethodAide;
import com.huawei.waf.facade.run.IMethodAide;

public class WAFConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String CFG_AUTHCODE = "authCode";
    private static final String CFG_INITVEC = "initVec"; //通行证加解密初始向量
    private static final String CFG_COOKIEHEAD = "cookieHead";
    
    private static final String CFG_THREADPOOLSIZE = "threadPoolSize";
    private static final String CFG_RESERVED_THREADNUM = "reservedThreadNum";
    private static final String CFG_KEEPALIVETIME = "keepAliveTime";
    private static final String CFG_MAX_BLOCKTASKNUM = "maxBlockTaskNum";
    
    private static final String CFG_MAX_BUSYTIMEOUT_NUM = "maxBusyTimeoutNum"; //超时置忙的个数上限
    
    private static final String CFG_ROOTURI = "rootUri";
    private static final String CFG_AIDE = "aide";
    private static final String CFG_METHODTIMEOUT = "methodTimeout";
    private static final String CFG_SESSIONTIMEOUT = "sessionTimeout";
    private static final String CFG_CHECKIP = "checkIp";
    private static final String CFG_IPAUTH = "ipAuth";
    private static final String CFG_ACCESSLOG = "accessLog";
    private static final String CFG_UTIL_LOG_EXCEPTION = "utilLogException";
    
    //配置缓存的有效期，配置是缓存在concurrenthashmap中的
    private static final String CFG_LOCALCACHE_TIME = "localCacheTime";

    /**
     * azdg算法中用到的密码，通常用在通行证中
     */
    private static byte[] authCode = "0A1B2C3D4E5F".getBytes();
    private static byte[] initVec = "1A2B3C4D5E6F".getBytes();
    
    /**
     * 所有method uri中特殊的开头
     * 凡是以rootUri开头的请求都转到相应的method中处理
     */
    private static String rootUri = null;
    
    /**
     * 放在cookie名称的前面，避免cookie命名空间冲突
     */
    private static String cookieHead = "";
    
    /**
     * 异步servlet线程中不做处理，只是接受请求，然后启动异步servlet，
     * 接着由线程池完成后面的业务处理，
     * threadPoolSize用来指定这个线程池中线程的个数，
     * 建议为cpu内核数的2倍
     */
    private static int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
    private static int reservedThreadNum = 5;
    private static long keepAliveTime = 60 * 1000; //ms，任务等待的超时时间
    
    private static int maxBlockTaskNum; //最大等待任务数
    
    private static int busyBlockTaskNum = 0;
    
    private static boolean accessLog = false;
    private static boolean utilLogException = true; //工具类是否打印详细的异常信息
    
   
    /**
     * 默认的异步servlet超时时间，单位毫秒
     * 每个method中可以配置timeout，如果不配置则使用此值
     */
    private static long methodTimeout = 10 * 1000;
    
    /**
     * session超时时间，表示用户登录后，如果无任何操作，
     * 经过sessionTimeout秒后，用户被强制退出，单位秒
     */
    private static int sessionTimeout = 30 * 60;
    
    private static int maxBusyTimeoutNum = Integer.MAX_VALUE;
    
    /**
     * 用来处理不同系统认证、多语言处理的差异，
     * 系统默认使用通行证方式认证
     */
    private static IMethodAide aide;
    
    /**
     * 在调用时时，是否检查当前ip与登录时ip是否一致，
     * 移动互联网络中，ip地址可能会经常变化
     */
    private static boolean checkIp = false;
    
    private static int localCacheTime = 300 * 1000;
    
    private static IpAuth[] ipAuth = null;
    
    @SuppressWarnings("unchecked")
	public static final boolean parse(Map<String, Object> cfg) {
        if(cfg == null) {
            LOG.info("There are no waf config, use default configs");
            return true;
        }
        rootUri = JsonUtil.getAsStr(cfg, CFG_ROOTURI, rootUri);
        
        methodTimeout = JsonUtil.getAsLong(cfg, CFG_METHODTIMEOUT, methodTimeout);
        //默认为两倍cpu核数
        threadPoolSize = JsonUtil.getAsInt(cfg, CFG_THREADPOOLSIZE, threadPoolSize);
        if(threadPoolSize <= 0) {
            LOG.error("Invalid {}({}) , must be bigger than 0", CFG_THREADPOOLSIZE, threadPoolSize);
            return false;
        }
        
        reservedThreadNum = JsonUtil.getAsInt(cfg, CFG_RESERVED_THREADNUM, (int)Math.ceil(5.0 * threadPoolSize / 100));

        keepAliveTime = JsonUtil.getAsLong(cfg, CFG_KEEPALIVETIME, keepAliveTime);
        if(keepAliveTime < methodTimeout) {
            LOG.error("Invalid {}({}) , must be bigger than {}({})",
                    CFG_KEEPALIVETIME, keepAliveTime, CFG_METHODTIMEOUT, methodTimeout);
            return false;
        }
        
        maxBlockTaskNum = JsonUtil.getAsInt(cfg, CFG_MAX_BLOCKTASKNUM, threadPoolSize * 10);
        if(maxBlockTaskNum <= 0) {
            LOG.error("Invalid {}({}) , must be bigger than 0", CFG_MAX_BLOCKTASKNUM, maxBlockTaskNum);
            return false;
        }
        
        maxBusyTimeoutNum = JsonUtil.getAsInt(cfg, CFG_MAX_BUSYTIMEOUT_NUM, maxBusyTimeoutNum);
        
        //以秒为单位，最大可以70年
        sessionTimeout = JsonUtil.getAsInt(cfg, CFG_SESSIONTIMEOUT, sessionTimeout);
        checkIp = JsonUtil.getAsBool(cfg, CFG_CHECKIP, false);
        accessLog = JsonUtil.getAsBool(cfg, CFG_ACCESSLOG, accessLog);
        utilLogException = JsonUtil.getAsBool(cfg, CFG_UTIL_LOG_EXCEPTION, utilLogException);
        
        String s = JsonUtil.getAsStr(cfg, CFG_AUTHCODE, null);
        if(!Utils.isStrEmpty(s)) {
            s = EncryptUtil.configDecode(s);
            authCode = s.getBytes();
        }
        
        s = JsonUtil.getAsStr(cfg, CFG_INITVEC, null);
        if(!Utils.isStrEmpty(s)) {
            s = EncryptUtil.configDecode(s);
            initVec = s.getBytes();
        }

        String aideCls = JsonUtil.getAsStr(cfg, CFG_AIDE, null);
        if(!Utils.isStrEmpty(aideCls)) {
            try {
                aide = ((Class<? extends IMethodAide>)Class.forName(aideCls)).newInstance();
            } catch (Exception e) {
                LOG.error("Fail to load aide {}", aideCls);
                return false;
            }
        } else {
            aide = new SessionMethodAide();
        }
        cookieHead = JsonUtil.getAsStr(cfg, CFG_COOKIEHEAD, cookieHead);
        localCacheTime = JsonUtil.getAsInt(cfg, CFG_LOCALCACHE_TIME, localCacheTime);
        
        //解析ipAuth
        List<Object> ipAuthList = JsonUtil.getAsList(cfg, CFG_IPAUTH);
        int num;
        
        if(ipAuthList != null && (num = ipAuthList.size()) > 0) {
            IpAuth ia;
            ipAuth = new IpAuth[num];
            for(int i = 0; i < num; i++) {
                ia = IpAuth.parse(JsonUtil.getAsObject(ipAuthList, i));
                if(ia == null) {
                    LOG.error("Fail to parse {} ip auth config", i);
                    return false;
                }
                ipAuth[i] = ia;
            }
        }
        
        return true;
    } 

    public static final byte[] getAuthCode() {
        return authCode;
    }
    
    //AES-CBC加密时用到的初始向量
    public static final byte[] getInitVec() {
        return initVec;
    }
    
    public static final int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    public static final int getReservedThreadNum() {
        return reservedThreadNum;
    }
    
    public static final long getKeepAliveTime() {
        return keepAliveTime;
    }
    
    /**
     * 当线程池任务队列超过maxBlockTaskNum时，最老的任务会被淘汰到执行handleBusy
     * @return
     */
    public static final int getMaxBlockTaskNum() {
        return maxBlockTaskNum;
    }
    
    public static final int getBusyBlockTaskNum() {
        return busyBlockTaskNum;
    }
    
    /**
     * 最大超时数，用于判断当前系统是否超时，
     * 如果为0，则表示不做此类判断
     * @return
     */
    public static final int getMaxBusyTimeoutNum() {
        return maxBusyTimeoutNum;
    }
    
    public static final String getRootUri() {
        return rootUri;
    }
    
    /**
     * 放在cookie名称的前面，避免cookie命名空间冲突
     * @return
     */
    public static final String getCookieHead() {
        return cookieHead;
    }
    
    public static final IMethodAide getAide() {
        return aide;
    }
    
    public static final void setAide(IMethodAide aide) {
        WAFConfig.aide = aide;
    }
    
    public static final long getMethodTimeout() {
        return methodTimeout;
    }
    
    /**
     * 返回session的有效时长，以10秒为单位，最大可以700年
     * @return
     */
    public static final int getSessionTimeout() {
        return sessionTimeout;
    }
    
    public static final boolean isCheckIp() {
        return checkIp;
    }
    
	/**
	 * 本地内存的缓存时间
	 * @return
	 */
	public static final int getLocalCacheTime() {
		return localCacheTime;
	}
	
	public static final boolean isAccessLog() {
	    return accessLog;
	}
	
    public static final boolean isUtilLogException() {
        return utilLogException;
    }
    
	public static final boolean isRemoteAddrValid(String addr) {
	    if(ipAuth == null) {
	        return true;
	    }
	    
	    for(IpAuth ia : ipAuth) {
	        if(ia.in(addr)) {
	            return true;
	        }
	    }
	    LOG.info("Remote ip {} not a valid ip", addr);
	    return false;
	}
	
	public static abstract class IpAuth {
		public abstract boolean in(String ip);
		public abstract boolean parseEx(Map<String, Object> cfg);
		
		public static IpAuth parse(Map<String, Object> cfg) {
			String type = JsonUtil.getAsStr(cfg, "type", "list").trim().toLowerCase();
			IpAuth ia;
			
			if(type.equals("list")) {
			    ia = new IpListAuth();
			} else /*if(type.equals("seg"))*/ {
			    ia = new IpSegAuth();
			}
			
			if(!ia.parseEx(cfg)) {
				return null;
			}
			
			return ia;
		}
	}
	
	private static class IpSegAuth extends IpAuth {
		String start;
		String end;
		
		@Override
		public boolean in(String ip) {
			if(ip.compareTo(start) < 0) {
				return false;
			}
			
			if(ip.compareTo(end) > 0) {
				return false;
			}
			
			return true;
		}
		
		public boolean parseEx(Map<String, Object> cfg) {
			this.start = JsonUtil.getAsStr(cfg, "start", null);
			if(Utils.isStrEmpty(this.start)) {
				return false;
			}
			
			this.end = JsonUtil.getAsStr(cfg, "end", null);
			if(Utils.isStrEmpty(this.end)) {
				return false;
			}
			
			return true;
		}
	} 
	
	public static String description() {
	    return "{coreThreadPoolSize:" + threadPoolSize
               + ",reservedThreadNum:" + reservedThreadNum
	           + ",keepAliveTime:" + keepAliveTime + "ms"
	           + ",maxBlockTaskNum:" + maxBlockTaskNum
	           + ",methodTimeout:" + methodTimeout + "ms"
	           + ",sessionTimeout:" + sessionTimeout + "s"
	           + ",utilLogException:" + utilLogException
	           + ",accessLog:" + accessLog
	           + ",localCacheTime:" + localCacheTime + "}";
	}
	
	private static class IpListAuth extends IpAuth {
		Set<String> list = null;
		
		public boolean in(String ip) {
			return list.contains(ip);
		}
		
		
		public boolean parseEx(Map<String, Object> cfg) {
			this.list = JsonUtil.getAsStrSet(cfg, "list");
			return this.list != null && this.list.size() > 0;
		}
	}
}
