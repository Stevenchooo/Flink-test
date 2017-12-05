package com.huawei.util;

public class TimeWinCounter {
    private final int[] counter;
    private int MASK;
    private int TIMEUNIT = 1000; //ms
    
    private int total = 0;
    private int oldUnit = 0;
    
    /**
     * @param interval 时间窗长度
     * @param timeUnit 时间窗每格时间单位，毫秒，比如时间窗每格1秒，则此参数为1000
     */
    public TimeWinCounter(int interval, int timeUnit) {
        if(interval <= 0 || Integer.bitCount(interval) != 1) {
            throw new IllegalArgumentException("Invalid interval parameter, must be 2^n");
        }
        
        if(timeUnit <= 0) {
            throw new IllegalArgumentException("Invalid timeUnit parameter, must be bigger than 0");
        }
        
        this.MASK = interval - 1;
        this.TIMEUNIT = timeUnit;
        
        this.counter = new int[interval];
        this.oldUnit = ((int)((1.0 * System.currentTimeMillis()) / timeUnit));
        
        for(int i = 0; i < interval; i++) {
            this.counter[i] = 0;
        }
    }
    
    public TimeWinCounter(int interval) {
        this(interval, 1000);
    }
    
    public synchronized int inc(int num) {
        int unit = ((int)((1.0 * System.currentTimeMillis()) / TIMEUNIT));
        if(oldUnit != unit) {
            for(int i = oldUnit + 1; i <= unit; i++) {
                total -= counter[i & MASK];
                counter[i & MASK] = 0;
            }
            oldUnit = unit;
        }
        total += num;
        counter[unit & MASK] += num;
        
        return total;
    }
    
    public int inc() {
        return inc(1);
    }
    
    public int dec(int num) {
        return inc(-num);
    }
    
    public int dec() {
        return inc(-1);
    }
    
    /**
     * 返回当前总数，并发时，这是一个不怎么准确的数字
     * @return
     */
    public int getTotal() {
        return total;
    }
//    
//    public static void main(String[] args) throws InterruptedException {
//        int interval = 4;
//        TimeWinCounter twc = new TimeWinCounter(interval, 1000);
//        for(int i = 0; i < 10; i++) {
//            twc.inc();
//        }
//        System.out.println("1.total:" + twc.getTotal());
//        Thread.sleep(1000);
//        for(int i = 0; i < 1000; i++) {
//            twc.inc();
//        }
//        System.out.println("2.total:" + twc.getTotal());
//        Thread.sleep(1000);
//        for(int i = 0; i < 100; i++) {
//            twc.inc();
//        }
//        System.out.println("3.total:" + twc.getTotal());
//        Thread.sleep((interval+1) * 1000);
//        for(int i = 0; i < 900; i++) {
//            twc.inc();
//        }
//        System.out.println("4.total:" + twc.getTotal());
//        Thread.sleep(1000);
//        for(int i = 0; i < 800; i++) {
//            twc.inc();
//        }
//        System.out.println("5.total:" + twc.getTotal());
//        Thread.sleep(1000);
//        for(int i = 0; i < 300; i++) {
//            twc.inc();
//        }
//        System.out.println("6.total:" + twc.getTotal());
//        System.exit(0);
//    }
}
