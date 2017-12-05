package com.huawei.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;

import com.google.code.yanf4j.core.impl.StandardSocketOption;
import com.huawei.tool.PerformanceTest;
import com.huawei.tool.PerformanceTest.TestTask;
import com.huawei.util.MemcachedUtil;

/**
 * 基于xmemcached库，实现简单的按key均衡的cache工具类
 * @author Administrator
 *
 */
public final class MemcachedUtil {
    private static final Logger LOG = LogUtil.getInstance();
    private static final String CONFIG_SERVERS = "servers";
    
    private static MemCachedGroup defaultGroup = null;
    private static final HashMap<String, MemCachedGroup> groups = new HashMap<String, MemCachedGroup>(); 
    
    private MemcachedUtil() {
    }
    
    @SuppressWarnings("unchecked")
    public static final boolean init(List<Map<String, Object>> cfgs) {
        try {
            for(Object oneCfg : cfgs) {
                Map<String, Object> cfg = (Map<String, Object>)oneCfg;
                if(!cfg.containsKey("name") || !cfg.containsKey(CONFIG_SERVERS)) {
                    LOG.error("No name or server config item for memcahced");
                    continue;
                }
                String name = cfg.get("name").toString(); //名称是用来均衡的，在MemCachedBalancer中需要设置好规则
                String servers = cfg.get(CONFIG_SERVERS).toString();
                servers = servers.replace(";", " ");
                servers = servers.replace(",", " ");

                int poolSize = 4;
                if(cfg.containsKey("poolSize")) {
                    poolSize = Integer.valueOf(JsonUtil.getAsStr(cfg, "poolSize"));
                }
                
                int mergeFactor = 10;
                if(cfg.containsKey("mergeFactor")) {
                    mergeFactor = Integer.valueOf(JsonUtil.getAsStr(cfg, "mergeFactor"));
                }
                
                MemCachedGroup node = new MemCachedGroup(servers, poolSize, mergeFactor);
                groups.put(name, new MemCachedGroup(servers, poolSize, mergeFactor));
                if(defaultGroup == null) {
                    defaultGroup = node; //将第一个node作为默认，防止未设均衡的情况下，取不到node
                }
            }
        } catch(Exception e) {
            LOG.error("Fail to init memcahced client", e);
            return false;
        }
        return true;
    }
    
    public static final MemCachedGroup getNode(String name) {
        return groups.get(name);
    }
    
    public static final MemCachedGroup getDefaultNode() {
    	return defaultGroup;
    }
    
    public interface MemCachedBalancer {
        public String getNode(String key);
    }

    public static class MemCachedGroup {
        
        private MemcachedClient mcc = null;
        
        /**
         * 创建cache组，每个组中使用一致性hash进行均衡
         * @param servers 服务器地址，ip:port，多个的情况使用空格号分隔
         * @param poolSize
         * @param mergeFactor
         */
        public MemCachedGroup(String servers, int poolSize, int mergeFactor) {
            try {
                MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(servers));
                builder.setSocketOption(StandardSocketOption.SO_RCVBUF, 32 * 1024); // 设置接收缓存区为32K，默认16K
                builder.setSocketOption(StandardSocketOption.SO_SNDBUF, 32 * 1024); // 设置发送缓冲区为32K，默认为8K
                builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false); // 启用nagle算法，提高吞吐量，默认关闭
                builder.getConfiguration().setSessionIdleTimeout(10000);
                
                builder.setConnectionPoolSize(poolSize);
                mcc = builder.build();
                mcc.setMergeFactor(mergeFactor); //默认是150，不能太大，否则在get时会有延迟
                mcc.setOptimizeMergeBuffer(true);
                mcc.setEnableHeartBeat(false);
            } catch (IOException e) {
                LOG.error("Fail to init memcached client:{}", servers, e);
            }
        }
        
        /** 
         * 添加缓存 
         */
        public boolean put(String key, Object value, int lifecycle) {
            try {
                return mcc.set(key, lifecycle, value);
            } catch (Exception e) {
                LOG.error("Fail to get {}", key, e);
                return false;
            }
        }
        
        /** 
         * 根据key获取信息 
         */
        public Object get(String key) {
            try {
                return mcc.get(key);
            } catch (Exception e) {
                LOG.error("Fail to get {}", key, e);
                return null;
            }
        }
        
        /** 
         * 根据key获取信息，并同时刷新key对应的超期时间 
         */
        public Object get(String key, int lifecycle) {
            try {
                return mcc.getAndTouch(key, lifecycle);
            } catch (Exception e) {
                LOG.error("Fail to get {}", key, e);
                return null;
            }
        }
        
        /** 
         * 根据key删除缓存 
         */
        public boolean remove(String key) {
            try {
                return mcc.delete(key);
            } catch (Exception e) {
                LOG.error("Fail to remove {}", key, e);
                return false;
            }
        }
        
        /**
         * 如果相减失败，则设置默认值为def
         */
        public long decr(String key, int by, long def, int lifecycle) {
            try {
                return mcc.decr(key, by, def, lifecycle);
            } catch (Exception e) {
                LOG.error("Fail to decr {}", key, e);
                return 0L;
            }
        }
        
        /**
         * 如果递减失败，则返回-1
         */
        public long decr(String key, int by) {
            try {
                return mcc.decr(key, by);
            } catch (Exception e) {
                LOG.error("Fail to decr {}", key, e);
                return 0L;
            }
        }
        
        /**
         * 递增失败则设置默认值为def
         */
        public long incr(String key, int by, long def, int lifecycle) {
            try {
                return mcc.incr(key, by, def, lifecycle);
            } catch (Exception e) {
                LOG.error("Fail to incr {}", key, e);
                return 0L;
            }
        }
        
        /**
         * 递增
         */
        public long incr(String key, int by) {
            try {
                return mcc.incr(key, by);
            } catch (Exception e) {
                LOG.error("Fail to incr {}", key, e);
                return 0L;
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException, IOException {
        String configFile = "E:\\Work\\Code\\WAF\\WebServer1\\logback.xml"; 
        File f = new File(configFile);
        if(!f.exists() || !LogUtil.init(f)) {
            System.out.println("Fail to load log config file:" + configFile);
        }
        
        MemCachedGroup mcg = new MemCachedGroup("192.168.22.111:11211", 10, 5);
        //MemCachedGroup mcg = new MemCachedGroup("192.168.22.233:11211", 50, 50);
         
//        for(int i = 0; i < 200000; i++) {
//        Object test = mcg.get("ddddddddd");
//        //System.out.println("test=" + (test != null ? test.toString() : "null"));
//        
//        
//        test = mcg.get("ZvqeMOxq4iKcAFQIplaiYA");
//        //System.out.println("test=" + (test != null ? test.toString() : "null"));
//        }
        TestTask t = new TestTask("mysql-memcached", 50, 100, mcg){
            @Override
            public void test(Object runtimeData) {
                MemCachedGroup mcg = (MemCachedGroup)runtimeData;
                for(int i = 0; i < 100; i++) {
                    //mcg.put(Utils.genUUID_64(), "test", 100000);
                    mcg.get(Utils.genUUID_64());
                    //mcg.get("ZvqeMOxq4iKcAFQIplaiYA");
                }
            }
        };
        
        PerformanceTest.test(t);
        System.exit(0);
    }
}
