package com.huawei.waf.facade.run;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;

/**
 * 需要初始化与销毁的处理类
 * @author l00152046
 *
 */
public abstract class AbstractHandler {
    private static final String CONFIG_PROCESSOR = "processor";
    
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 初始化
     * @return
     */
    abstract public boolean init();
    
    /**
     * 销毁
     */
    abstract public void destroy();
    
    public static AbstractHandler getHandler(Map<String, Object> json, AbstractHandler defaultHandler) {
        String clsName = JsonUtil.getAsStr(json, CONFIG_PROCESSOR, null);
        if(Utils.isStrEmpty(clsName)) {
            return defaultHandler;
        }
        
        try {
            @SuppressWarnings("unchecked")
            Class<? extends AbstractHandler> cls = (Class<? extends AbstractHandler>)Class.forName(clsName);
            if(!defaultHandler.getClass().isAssignableFrom(cls)) {
                LOG.error("{} is not sub-class of {}", cls.getName(), defaultHandler.getClass().getName());
                return null;
            }
            
            AbstractHandler handler = (AbstractHandler)cls.newInstance();
            if(!handler.init()) {
                LOG.error("Fail to call {}.init()", clsName);
                return null;
            }
            return handler;
        } catch(Exception e) {
            LOG.error("Fail to load processor {}", clsName, e);
            return null;
        }
    }
}
