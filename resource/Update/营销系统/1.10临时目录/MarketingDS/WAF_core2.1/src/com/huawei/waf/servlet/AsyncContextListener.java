package com.huawei.waf.servlet;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;

public class AsyncContextListener implements AsyncListener {
    private static final Logger LOG = LogUtil.getInstance();
    
    private MethodContext webContext = null;
    
    public AsyncContextListener(MethodContext webContext) {
        this.webContext = webContext;
    }
    
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
    }
    
    @Override
    public void onError(AsyncEvent event) throws IOException {
        webContext.invalidate();
        LOG.error("onError.{}", webContext.getMethodConfig().getName());
    }
    
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
    }
    
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        webContext.invalidate();
        LOG.error("onTimeout.{}", webContext.getMethodConfig().getName());
    }
    
    public MethodContext getWebContext() {
        return webContext;
    }
}
