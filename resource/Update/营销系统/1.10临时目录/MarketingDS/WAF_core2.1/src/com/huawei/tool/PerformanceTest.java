package com.huawei.tool;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import com.huawei.util.Utils;
import com.huawei.waf.protocol.Const;

public class PerformanceTest {
    public static final void test(TestTask task, OutputStream out) throws InterruptedException, IOException {
        //counter不能在task中创建，否则程序不能停止
        CountDownLatch counter = new CountDownLatch(task.threadNum * task.runtimes);
        task.setCounter(counter);
        ExecutorService exectors = Executors.newFixedThreadPool(task.threadNum);
        
        long start = System.currentTimeMillis();
        for(int i = 0; i < task.threadNum; i++) {
            exectors.execute(task);
        }
        counter.await();
        long end = System.currentTimeMillis();
        exectors.shutdown();
        
        long interval = end > start ? (end - start) : 1;
        
        out.write(("RUN " + task.name + ",test result:"
                + "\nthreadNum=" + task.threadNum
                + "\nruntimes=" + task.runtimes
                + "\ninterval=" + interval + "ms"
                + "\nspeed=" + Math.round(1000d * task.threadNum * task.runtimes / interval)
                + "\n").getBytes(Const.DEFAULT_CHARSET));
    }
    
    public static final void test(TestTask task) throws InterruptedException, IOException {
        test(task, System.out);
    }
    
    public static abstract class TestTask implements Runnable {
        public final int runtimes;
        public final int threadNum;
        private CountDownLatch counter = null;
        public final Object runtimeData;
        public final String name;
        
        public abstract void test(Object runtimeData);
        public void run() {
            for(int i = 0; i < runtimes; i++) {
                test(this.runtimeData);
                counter.countDown();
            }
        }
        
        public TestTask(String name, int threadNum, int runtimes, Object runtimeData) {
            this.name = name;
            this.threadNum = threadNum;
            this.runtimes = runtimes;
            this.runtimeData = runtimeData;
        }
        
        public TestTask(String name, int runtimes, Object runtimeData) {
            //默认2倍CPU核数，计算型的性能测试，为最佳并发数，充分利用多核的并发能力
            this(name, Runtime.getRuntime().availableProcessors() * 2, runtimes, runtimeData);
        }
        
        private void setCounter(CountDownLatch counter) {
            this.counter = counter;
        }
    }
    
    public static void main(String[] args) throws Exception {
        final int N = 20000;
        final int threadNum = 4;
//        final Map<String, Object> paras = new java.util.HashMap<String, Object>();
//        paras.put("e", 1);
//        paras.put("b", "2");
//        paras.put("c", "3");
//        paras.put("d", 6);
//        paras.put("a", 6);
//        final HashSet<Integer> a = new HashSet<Integer>(); 
//        Random r = new Random();
//        
//        for(int i = 0; i < 20000; i++) {
//            a.add(r.nextInt());
//        }
//        
//        TestTask t1 = new TestTask("signdata", threadNum, N, null){
//            @Override
//            public void test(Object runtimeData) {
//                if(a.contains(N)) {
//                    //
//                }
//            }
//        };
//        
//        test(t1);
//
        
//        TestTask t1 = new TestTask("sparse", threadNum, N, null){
//            @Override
//            public void test(Object runtimeData) {
//                com.huawei.util.SparseArray<String> sparse = new com.huawei.util.SparseArray<String>();
//                for(int i = 0; i < N; i++) {
//                    sparse.put(i, Integer.toHexString(i));
//                }
//                
//                for(int i = 0; i < N; i++) {
//                    sparse.get(i);
//                }
//            }
//        };
//        
//        TestTask t2 = new TestTask("map", threadNum, N, null){
//            @Override
//            public void test(Object runtimeData) {
//                java.util.Map<Integer, String> map = new java.util.concurrent.ConcurrentHashMap<Integer, String>();
//                
//                for(int i = 0; i < N; i++) {
//                    map.put(i, Integer.toHexString(i));
//                }
//                
//                for(int i = 0; i < N; i++) {
//                    map.get(i);
//                }
//            }
//        };
        
        final Map<byte[], String> testByte = new HashMap<byte[], String>();
        final Map<String, String> testStr = new HashMap<String, String>();
        final List<byte[]> keys = new ArrayList<byte[]>();
        final List<String> skeys = new ArrayList<String>();
        for(int i = 0; i < 2000; i++) {
            byte[] key = com.huawei.util.Utils.genUUID();
            String v = com.huawei.util.Utils.bin2base64(key);
            keys.add(key);
            skeys.add(v);
            
            testByte.put(key, v);
            testStr.put(v, v);
        }
        
        TestTask t1 = new TestTask("bytemap", threadNum, N, null){
            @Override
            public void test(Object runtimeData) {
                String v;
                for(byte[] k : keys) {
                    v = testByte.get(k);
                    if(v.equals("01234567890123456789123")) {
                        System.out.println("wrong");
                    }
                }
            }
        };
        
        TestTask t2 = new TestTask("strmap", threadNum, N, null){
            @Override
            public void test(Object runtimeData) {
                String v;
                for(String k : skeys) {
                    v = testStr.get(k);
                    if(!v.equals(k)) {
                        System.out.println("wrong");
                    }
                }
            }
        };
        
        test(t1);
        test(t2);
        System.exit(0);
    }
}
