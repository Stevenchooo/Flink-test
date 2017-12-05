package com.huawei.waf.core.config.sys;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.sys.TimerConfig;
import com.huawei.waf.facade.WAFTimerTask;

public class TimerConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String CONFIG_NAME = "name";
    
    private static final String CONFIG_HOST = "host";
    
    private static final String CONFIG_DELAY = "delay";
    
    private static final String CONFIG_RUNAT = "runAt"; //指定每天运行的时间点，delay是计算出来的
    
    private static final String CONFIG_PERIOD = "period";
    
    private static final String CONFIG_PROCESSOR = "processor";
    
    private String name = "";
    
    private List<String> hosts = null;
    
    private long delay = 0 * 1000;
    
    private long period = 1800 * 1000;
    
    private String runAt = null;
    
    private Map<String, Object> config;
    
    private WAFTimerTask processor;
    
    private TimerConfig() {} //外部不容许直接调用
    
    /**
     * 解析JsonObject，获得定时器配置的各个数据
     * @param config 定时器配置的json结构体
     * @return TimerConfig 定时器配置
     */
    public static TimerConfig parse(Map<String, Object> config) {
        if(!config.containsKey(CONFIG_NAME) || !config.containsKey(CONFIG_PERIOD) || !config.containsKey(CONFIG_PROCESSOR)) {
            LOG.error("Half-baked timer config");
            return null;
        }
        TimerConfig tc = new TimerConfig();
        
        tc.hosts = JsonUtil.getAsStrList(config, CONFIG_HOST);
        tc.name = JsonUtil.getAsStr(config, CONFIG_NAME);
        if(config.containsKey(CONFIG_DELAY)) {
            tc.delay = Utils.parseLong(JsonUtil.getAsStr(config, CONFIG_DELAY), 0);
        }
        tc.period = Utils.parseLong(JsonUtil.getAsStr(config, CONFIG_PERIOD), 1800 * 1000L);
        tc.runAt = JsonUtil.getAsStr(config, CONFIG_RUNAT, tc.runAt);
        
        String str = JsonUtil.getAsStr(config, CONFIG_PROCESSOR);
        try {
            tc.processor = (WAFTimerTask)Class.forName(str).newInstance();
        } catch (Exception e) {
            LOG.error("Fail to initialize {}", str, e);
            return null;
        }
        tc.config = config;
        
        return tc;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 只有指定的host才运行定时器，避免定时器多实例运行
     * @return
     */
    public List<String> getHosts() {
        return hosts;
    }
    
    public long getDelay() {
        return delay;
    }
    
    public long getPeriod() {
        return period;
    }
    
    public WAFTimerTask getProcessor() {
        return processor;
    }
    
    public Map<String, Object> getConfig() {
    	return config;
    }
}
