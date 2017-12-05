package com.huawei.waf.thread;

/**
 * @author l00152046
 * 保证相同id的runnable，都会在相同线程中执行，
 * 这块可以满足高并发时，业务逻辑对部分任务有先后依赖的情况
 */
public class WAFThreadPool {
    private final int nThreads;
    private final WAFPoolWorker[] workers;
    private int cur = 0; //未考虑这个值的并发处理，因为大致均匀即可
    
    public WAFThreadPool(int nThreads) {
        if(nThreads <= 0) {
            throw new IllegalArgumentException("Invalid nThreads, must be bigger than 0");
        }
        this.nThreads = nThreads;
        this.workers = new WAFPoolWorker[nThreads];
        for (int i = 0; i < nThreads; i++) {
            this.workers[i] = new WAFPoolWorker();
        }
    }
    
    public void start() {
        for (WAFPoolWorker worker : this.workers) {
            worker.start();
        }
    }
    
    public void execute(WAFRunnable r) {
        int id = r.getTaskId();
        if(id < 0) {
            id = Math.abs(cur++);
        }
        workers[id % nThreads].execute(r);
    }
    
    public static interface WAFRunnable extends Runnable {
        /**
         * 获得任务的标识，相同id的任务放入相同的thread中执行
         * 保证在高并发时，仍然是先到先执行
         * @return
         */
        public int getTaskId();
    }
//    
//    public static class TestThread implements WAFRunnable {
//        private int id;
//        private java.util.concurrent.CountDownLatch counter;
//        
//        public TestThread(int id, java.util.concurrent.CountDownLatch counter) {
//            this.id = id;
//            this.counter = counter;
//        }
//        
//        @Override
//        public void run() {
//            //System.out.println("task_" + id + "->thread_" + Thread.currentThread().getId());
//            counter.countDown();
//        }
//
//        @Override
//        public int getTaskId() {
//            return id;
//        }
//    }
//    
//    public static void main(String[] args) throws Exception {
//        int N = 30000000;
//        java.util.concurrent.CountDownLatch counter = new java.util.concurrent.CountDownLatch(N);
//        WAFThreadPool pool = new WAFThreadPool(8);
//        //java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(8);
//        long t0 = System.currentTimeMillis();
//        WAFRunnable task;
//        for(int i = 0; i < N; i++) {
//            task = new TestThread(i & 0x0f, counter);
//            pool.execute(task);
//        }
//        counter.await();
//        long t = System.currentTimeMillis() - t0;
//        System.out.println("Speed = " + ((1000L * N)/t));
//        System.exit(0);
//    }
}
