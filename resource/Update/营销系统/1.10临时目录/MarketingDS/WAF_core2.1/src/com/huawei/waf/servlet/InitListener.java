package com.huawei.waf.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.facade.AbstractInitializer;

public class InitListener implements ServletContextListener {
    protected static final long serialVersionUID = -2440216393145762479L;
    private static final Logger LOG = LogUtil.getInstance();
    
    protected AbstractInitializer initializer = null;
    private static String configRootPath = "";
    
    public InitListener() {
        initializer = new ServletInit();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String sp = System.getProperty("file.separator");
        ServletContext context = event.getServletContext();
        configRootPath = context.getRealPath("/") + "WEB-INF" + sp + "conf" + sp;
        
        initializer.setContext(context);
        int retCode = initializer.init(configRootPath);
        if(retCode != 0) {
            LOG.error("Fail to call ServletInit, retCode {}", retCode);
            System.exit(retCode);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        initializer.destroyed();
    }
    
    protected static class ServletInit extends AbstractInitializer {
        @Override
        protected int beforeInit(String configPath) {
            return 0;
        }

        @Override
        protected int afterInit(String configPath) {
            return 0;
        }

        @Override
        protected void destroy() {
        }
    }
    
    public static final String getConfigRootPath() {
        return configRootPath;
    }
}
