package com.huawei.waf.facade;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.MethodContext;

public class MethodRunner {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static ThreadPoolExecutor NORMAL_THREAD_POOL = null;
    private static ArrayBlockingQueue<Runnable> queue = null;
    
    public static boolean init() {
        LOG.info(WAFConfig.description());
        queue = new ArrayBlockingQueue<Runnable>(WAFConfig.getMaxBlockTaskNum());
        NORMAL_THREAD_POOL = new ThreadPoolExecutor(
                WAFConfig.getThreadPoolSize(),
                WAFConfig.getThreadPoolSize(),
                WAFConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                queue,
                new WAFResponseBusyPolicy());
        
        return true;
    }
    
    public static int taskNum() {
		return queue != null ? queue.size() : 0;
    }
    
    public static boolean destroy() {
        //如果线程池不关闭，在关闭tomcat时会长时间无法关闭
        try {
            LOG.info("THREAD_POOL shutdown,current thread-pool-size:{}", NORMAL_THREAD_POOL.getPoolSize());
            
            NORMAL_THREAD_POOL.shutdown();
        } catch(Exception e) {
            LOG.error("Fail to close NORMAL_THREAD_POOL", e);
        }
        
        return true;
    } 
    
    public abstract static class MethodRunnable implements Runnable {
        protected MethodContext context;
        
        public MethodRunnable(MethodContext context) {
            this.context = context;
        }
        
        public MethodContext getContext() {
            return context;
        }
    }
    
    private static class WAFResponseBusyPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (e.isShutdown()) {
                return;
            }

            BlockingQueue<Runnable> queue = e.getQueue();
            MethodRunnable task = (MethodRunnable)queue.poll(); //清除最老的
            e.execute(r); //加入最新的
            
            /**
             * 当正常业务已经不能继续执行时，执行降级处理
             */
            MethodContext context = task.getContext();
            boolean contextValid = context.isValid();
            LOG.info("Too busy, method:{}, queue size:{}, context-valid:{}",
                    context.getMethodConfig().getName(),
                    queue.size(),
                    contextValid);
            
            if(contextValid) {
                try {
                    context.responseBusy();
                } catch (IOException ex) {
                    LOG.error("Fail to response busy", ex);
                }
            }
        }
    }
    
    public static final void exec(MethodRunnable r) {
        NORMAL_THREAD_POOL.execute(r);
    }
}
