package com.huawei.waf.thread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;

public class WAFPoolWorker extends Thread {
    private static final Logger LOG = LogUtil.getInstance();

    private final Semaphore available = new Semaphore(0, false);
    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

    public void run() {
        Runnable r;
        for (;;) {
            r = queue.poll();
            if(r == null) {
                try {
                    available.acquire();
                } catch (InterruptedException e) {
                }
                continue;
            }
            
            // If we don't catch RuntimeException,
            // the pool could leak threads
            try {
                r.run();
            } catch (Exception e) {
                LOG.error("Fail to execute {}", r.getClass().getSimpleName(), e);
            }
        }
    }

    public void execute(Runnable r) {
        queue.add(r);
        available.release();
    }
}
