package com.huawei.waf.facade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.servlet.ServletContext;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.MemcachedUtil;
import com.huawei.util.DBUtil;
import com.huawei.util.GwClient;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.Languages;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.Methods;
import com.huawei.waf.core.config.sys.SysConfig;
import com.huawei.waf.core.config.sys.TimerConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.response.HtmlResponser;
import com.huawei.waf.facade.security.AbstractSecurityAide;
import com.huawei.waf.protocol.RetCode;

abstract public class AbstractInitializer {
    private static final List<Timer> lTimer = new ArrayList<Timer>();
    private Logger LOG = null;
    private static String webRoot = "/";
    private static CacheManager cacheManager = null;  
    private static AbstractSecurityAide securityAide = new AbstractSecurityAide(){};
    
    private ServletContext context = null;
    
    /**
     * 在log、system.cfg读取之后，其他配置初始化之前调用
     * @param configPath
     * @return
     */
    abstract protected int beforeInit(String configPath);
    
    /**
     * 在所有初始成功之后，包括定时器启动完毕
     * @param configPath
     * @return
     */
    abstract protected int afterInit(String configPath);
    
    /**
     * 可以重装此函数，用于销毁beforeInit/afterInit时初始化的资源
     */
    abstract protected void destroy();
    
    /**
     * 获取日志对象，一定要在init中调用的抽象函数中，或者之后才可以调用
     * @return
     */
    protected Logger getLOG() {
        return LOG;
    }
    
    public int init(String configPath) {
        //初始化日志相关的配置，如果日志初始化失败则退出
        String configFile = configPath + "logback.xml"; 
        File f = new File(configFile);
        if(!f.exists() || !LogUtil.init(f)) {
            System.out.println("Fail to load log config file:" + configFile);
            return 1;
        }
        
        LOG = LogUtil.getInstance();
        //读取系统配置
        try {
            RetCode.print(LOG); //打印默认返回码定义，便于阅读
            
            LOG.info("\n===Load system.cfg");
            if(!SysConfig.readConfigs(new File(configPath + "system.cfg"))) {
                LOG.error("Fail to parse system config:{}system.cfg", configPath);
                return 2;
            }
            
            //需确保在SysConfig读取之后，因为WAFConfig是在SysConfig中读取的
            webRoot = WAFConfig.getRootUri(); 
            if(webRoot == null) {
                webRoot = context.getContextPath();
            }
            LOG.info("===Run webserver in {}", webRoot);

            int retCode = beforeInit(configPath);
            if(retCode != 0) {
                LOG.error("Fail to call beforeInit, retCode {}", retCode);
                return retCode;
            }
            
            /*
             * DataBase
             * 每秒处理速度在几百到几千不等，
             * 数据库连接池初始化，非必须，如果不需要，可以不配置
             * 如果将业务逻辑（及数据）与呈现隔离了，则无需数据库
             */
            LOG.info("\n===Load rdb config");
            Map<String, Object> cfgs = SysConfig.getRdbConfigs();
            if(cfgs != null && cfgs.size() > 0) {
                if(!DBUtil.init(cfgs)) {
                    LOG.error("--------------Platform fail to init RDB------------");
                    return 4;
                }
            } else {
                LOG.info("No rdb config, so ignore it");
            }
            
            /*
             * memcahced
             * 跨进程或机器的内存cache，每秒访问可以接近10万级
             * memcached初始化，非必须，如果不需要，可以不配置
             * 如果是一个memcached的集群，可以配置多个，注意名称要有规律
             * 这样在balancer中可以容易根据key进行负载均衡
             */
            List<Map<String, Object>> cfgList = SysConfig.getMemcachedConfigs();
            if(cfgList != null && cfgList.size() > 0) {
                if(!MemcachedUtil.init(cfgList)) {
                    LOG.error("--------------Platform fail to init Memached------------");
                    return 5;
                }
                //extension:如果需要按key进行负载均衡，请设置均衡器
                //MemCachedUtil.setBalancer(balancer);
            } else {
                LOG.info("No memcached config, so ignore it");
            }
            
            configFile = configPath + "ehcache.xml"; 
            if(!(new File(configFile)).exists()) {
                LOG.info("There are no ehcache config, so ignore it");
            } else {
                try {
                    cacheManager = new CacheManager(configFile);  
                } catch(Exception e) {
                    LOG.error("Fail to load ehcache config", e);
                    return 5;
                }
            }
        
            Map<String, Object> gwConfig = SysConfig.getGwConfigs();
            if(gwConfig != null) {
            	if(!GwClient.init(configPath, gwConfig)) {
	                LOG.error("Not a valid gw config, {}", gwConfig);
	                return 6;
            	}
            } else {
                LOG.info("There are no gw config, so ignore it");
            }
            
            LOG.info("===Load freemarker config===");
            Map<String, Object> freeMarkerConfigs = SysConfig.getFreemarkerConfigs();
            if(freeMarkerConfigs != null) {
	            if(!HtmlResponser.initFreemarker(context, SysConfig.getFreemarkerConfigs())) {
	            	LOG.error("--------------Platform fail to init freemarker------------");
	                return 7;
	            }
            } else {
                LOG.info("There are no freemarker config, so ignore it");
            }
            
            MethodConfig.init();
            if(!Methods.init(new File(configPath + "web-cfg"))) {
                LOG.error("Fail to parse methods");
                return 8;
            }
            
            if(!MethodRunner.init()) {
                LOG.error("Fail to init method runner");
                return 8;
            }
            
            //版权申明
            String s = "7Jibh1WZiojIvEGcp9yYvBXeyl2ZoRnIsAiIhVHdoRVewVmI6Iibv5WZiwCIiQXewVmIgoDIiYUSYVERiwCIiIXZzB3buNXZiAiOgsHIiM2buRXZuRnI6IyVBZEIy4CMsM0bwlncpdGa0BES1F2dllGIj9GLsRHZuAiMwEjMtIDMxUjI91H";
            String ver = "100000";
            MethodConfig mc = MethodConfig.parse(ver, JsonUtil.jsonToMap(Utils.base642bin(s)));
            Methods.addMethod(ver, mc);
            
            if(!Languages.init(new File(configPath + "../languages"))) {
                LOG.error("Fail to parse languages");
                return 9;
            }
        
            retCode = afterInit(configPath);
            if(retCode != 0) {
                LOG.error("Fail to call afterInit, retCode {}", retCode);
               return retCode;
            }
            
            //启动定时器，非必须，如果不需要，可以不配置
            LOG.info("=== Start times ===");
            startTimers(LOG);
        } catch(Exception e) {
            LOG.error("Fail to load config", e);
            return 100;
        }
        LOG.info("=== Success load system config, run it in {} ===", webRoot);
        return 0;
    }
    
    private void startTimers(Logger LOG) {
        List<TimerConfig> timerCfgs = SysConfig.getTimerConfigs();
        if(timerCfgs.size() <= 0) {
            LOG.info("No timer config, so ignore it");
            return;
        }
        
        Set<String> localIps = Utils.getLocalAddrs();
        List<String> ipList;
        
        WAFTimerTask timerHandler;
        
        for (TimerConfig tc : timerCfgs) {
            if((ipList = tc.getHosts()) != null) {
                if(!Utils.isIntersected(ipList, localIps)) {
                    LOG.info("Host {} not in local address list, don't start timer {}", ipList, tc.getName());
                    continue;
                }
            }
            LOG.info("Start timer {}", tc.getName());

            /**
             * schedule模式，上一次未完成，下一次不启动
             */
            timerHandler = tc.getProcessor();
            if(!timerHandler.init(tc)) {
                LOG.error("Fail to call {}.init", tc.getName());
            	continue;
            }
            Timer timer = new Timer();
            timer.schedule(timerHandler, tc.getDelay(), tc.getPeriod());
            
            lTimer.add(timer);
        }
    }
    
    private void stopTimers() {
        getLOG().info("Stop all timers, total {}", lTimer.size());
        for (Timer t : lTimer) {
            t.cancel();
        }
        lTimer.clear();
    }

    public void destroyed() {
        MethodRunner.destroy();
        stopTimers();
        Methods.destroy();
        destroy();
        if(cacheManager != null) {
            cacheManager.shutdown();
        }
        DBUtil.destroy();
        getLOG().info("=== System destroyed ===");
    }

    public static String getWebRoot() {
        return webRoot;
    }
    
    public static final CacheManager getCacheManager() {
        return cacheManager;
    }
    
    public void setContext(ServletContext context) {
        this.context = context;
    }
    
    public ServletContext getContext() {
        return context;
    }
    
    public static AbstractSecurityAide getSecurityAide() {
    	return securityAide;
    }
    
    protected static void setSecurityAide(AbstractSecurityAide aide) {
    	securityAide = aide;
    }
}
