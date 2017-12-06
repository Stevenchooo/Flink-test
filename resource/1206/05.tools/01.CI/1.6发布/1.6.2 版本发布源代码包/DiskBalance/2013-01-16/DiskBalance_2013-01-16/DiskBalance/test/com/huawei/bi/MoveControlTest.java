/*
 * 文 件 名:  MoveControlTest.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  移动hadoop目录策略控制测试类
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi;

import static org.junit.Assert.assertEquals;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * 移动hadoop目录策略控制测试类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-14]
 */
public class MoveControlTest
{
    private static final Long K = 1024L;
    
    private static final Long M = K * K;
    
    private static final Long G = K * M;
    
    /**
     * 最大空闲空间磁盘低于阈值，不需要磁盘平衡[单磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testLessMaxThresIsNeedBalance1() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 15 * G;
        long lThresholdMin = Long.MAX_VALUE;
        long lDiffThreshold = -1L;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long diskIdleSize = lThresholdMax - 1;
        diskInfos.append(getRow(5 * G, 1, disk, diskIdleSize));
        
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(false, mc.isNeedBalance());
    }
    
    /**
     * 最大空闲空间磁盘低于阈值，不需要磁盘平衡[多磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testLessMaxThresIsNeedBalance2() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 15 * G;
        long lThresholdMin = Long.MAX_VALUE;
        long lDiffThreshold = -1L;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long maxDiskIdleSize = 5 * G;
        diskInfos.append(getRow(5 * G, 1, disk, maxDiskIdleSize));
        
        long minDiskIdleSize = 3 * G;
        disk = "/data2";
        diskInfos.append(getRow(5 * G, 2, disk, minDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(false, mc.isNeedBalance());
    }
    
    /**
     * 最小空闲空间磁盘高于阈值，不需要磁盘平衡[单磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testMoreMinThresIsNeedBalance1() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = Long.MIN_VALUE;
        long lThresholdMin = 5 * 1024 * 1024 * 1024L;
        long lDiffThreshold = -1L;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long diskIdleSize = lThresholdMin + 1;
        diskInfos.append(getRow(5 * 1024 * 1024 * 1024L, 1, disk, diskIdleSize));
        
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(false, mc.isNeedBalance());
    }
    
    /**
     * 最小空闲空间磁盘高于阈值，不需要磁盘平衡[多磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testMoreMinThresIsNeedBalance2() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = Long.MIN_VALUE;
        long lThresholdMin = 5 * G;
        long lDiffThreshold = -1L;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long maxDiskIdleSize = 7 * G;
        diskInfos.append(getRow(5 * G, 1, disk, maxDiskIdleSize));
        
        long minDiskIdleSize = 6 * G;
        disk = "/data2";
        diskInfos.append(getRow(5 * G, 2, disk, minDiskIdleSize));
        
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(false, mc.isNeedBalance());
    }
    
    /**
     * 最大空闲空间差值磁盘小于阈值，不需要磁盘平衡[单磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testLessDiffThresIsNeedBalance1() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = Long.MIN_VALUE;
        long lThresholdMin = Long.MAX_VALUE;
        long lDiffThreshold = 1L;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long diskIdleSize = lThresholdMin + 1;
        diskInfos.append(getRow(5 * 1024 * 1024 * 1024L, 1, disk, diskIdleSize));
        
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(false, mc.isNeedBalance());
    }
    
    /**
     * 最大空闲空间差值磁盘小于阈值，不需要磁盘平衡[多磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testLessDiffThresIsNeedBalance2() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = Long.MIN_VALUE;
        long lThresholdMin = Long.MAX_VALUE;
        long lDiffThreshold = 2 * G;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long minDiskIdleSize = 1 * G;
        diskInfos.append(getRow(5 * 1024 * 1024 * 1024L,
                1,
                disk,
                minDiskIdleSize));
        
        long maxDiskIdleSize = 2 * G;
        disk = "/data2";
        diskInfos.append(getRow(5 * 1024 * 1024 * 1024L,
                2,
                disk,
                maxDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(false, mc.isNeedBalance());
    }
    
    /**
     * 满足磁盘平衡条件[多磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testNeedBalance() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 5 * G;
        long lThresholdMin = 2 * G;
        long lDiffThreshold = 2 * G;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long minDiskIdleSize = 1 * G;
        diskInfos.append(getRow(5 * 1024 * 1024 * 1024L,
                1,
                disk,
                minDiskIdleSize));
        
        long maxDiskIdleSize = 6 * G;
        disk = "/data2";
        diskInfos.append(getRow(5 * 1024 * 1024 * 1024L,
                2,
                disk,
                maxDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(true, mc.isNeedBalance());
    }
    
    /**
     * 测试移动策略[单移除磁盘，单移入磁盘,移动全部目录]
     * @throws Exception 异常
     */
    @Test
    public void testSingleOutInGetMovePolicy1() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 5 * G;
        long lThresholdMin = 2 * G;
        long lDiffThreshold = 2 * G;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long minDiskIdleSize = 1 * G;
        diskInfos.append(getRow(64 * M, 1, disk, minDiskIdleSize));
        diskInfos.append(getRow(64 * M, 2, disk, minDiskIdleSize));
        diskInfos.append(getRow(64 * M, 3, disk, minDiskIdleSize));
        diskInfos.append(getRow(64 * M, 4, disk, minDiskIdleSize));
        long maxDiskIdleSize = 6 * G;
        disk = "/data2";
        diskInfos.append(getRow(5 * G, 2, disk, maxDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(true, mc.isNeedBalance());
        
        List<MovePolicy> movePolicies = mc.getMovePolicies();
        Collections.sort(movePolicies);
        List<MovePolicy> expects = new ArrayList<MovePolicy>();
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir4",
                "/data2/hadoop/data/current/subdir3"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir3",
                "/data2/hadoop/data/current/subdir4"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir2",
                "/data2/hadoop/data/current/subdir5"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir1",
                "/data2/hadoop/data/current/subdir6"));
        Collections.sort(expects);
        assertEquals(expects, movePolicies);
    }
    
    /**
     * 测试移动策略[单移除磁盘，单移入磁盘,移动部分目录]
     * @throws Exception 异常
     */
    @Test
    public void testSingleOutInGetMovePolicy2() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 3 * G;
        long lThresholdMin = 2 * G;
        long lDiffThreshold = 1 * G;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long minDiskIdleSize = 1 * G;
        diskInfos.append(getRow(64 * M, 1, disk, minDiskIdleSize));
        diskInfos.append(getRow(1984 * M, 21, disk, minDiskIdleSize));
        diskInfos.append(getRow(1088 * M, 32, disk, minDiskIdleSize));
        diskInfos.append(getRow(512 * M, 44, disk, minDiskIdleSize));
        diskInfos.append(getRow(256 * M, 35, disk, minDiskIdleSize));
        diskInfos.append(getRow(128 * M, 36, disk, minDiskIdleSize));
        diskInfos.append(getRow(65 * M, 56, disk, minDiskIdleSize));
        diskInfos.append(getRow(64 * M, 67, disk, minDiskIdleSize));
        diskInfos.append(getRow(1 * K, 79, disk, minDiskIdleSize));
        long maxDiskIdleSize = 4 * G;
        disk = "/data2";
        diskInfos.append(getRow(5 * G, 2, disk, maxDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(true, mc.isNeedBalance());
        
        List<MovePolicy> movePolicies = mc.getMovePolicies();
        Collections.sort(movePolicies);
        List<MovePolicy> expects = new ArrayList<MovePolicy>();
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir32",
                "/data2/hadoop/data/current/subdir3"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir35",
                "/data2/hadoop/data/current/subdir4"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir36",
                "/data2/hadoop/data/current/subdir5"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir67",
                "/data2/hadoop/data/current/subdir6"));
        Collections.sort(expects);
        assertEquals(expects, movePolicies);
    }
    
    /**
     * 测试移动策略[多移出磁盘，单移入磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testMultiOutSingleInGetMovePolicy() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 3 * G;
        long lThresholdMin = 2 * G;
        long lDiffThreshold = 1 * G;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data2";
        long minDiskIdleSize = 1 * G;
        diskInfos.append(getRow(1984 * M, 21, disk, minDiskIdleSize));
        diskInfos.append(getRow(1088 * M, 32, disk, minDiskIdleSize));
        diskInfos.append(getRow(512 * M, 44, disk, minDiskIdleSize));
        diskInfos.append(getRow(256 * M, 35, disk, minDiskIdleSize));
        diskInfos.append(getRow(128 * M, 36, disk, minDiskIdleSize));
        diskInfos.append(getRow(65 * M, 56, disk, minDiskIdleSize));
        diskInfos.append(getRow(64 * M, 67, disk, minDiskIdleSize));
        diskInfos.append(getRow(1 * K, 79, disk, minDiskIdleSize));
        
        long middleDiskIdleSize = 1 * G;
        disk = "/data3";
        diskInfos.append(getRow(64 * M, 1, disk, middleDiskIdleSize));
        diskInfos.append(getRow(1984 * M, 21, disk, middleDiskIdleSize));
        diskInfos.append(getRow(1088 * M, 32, disk, middleDiskIdleSize));
        diskInfos.append(getRow(768 * M, 44, disk, middleDiskIdleSize));
        diskInfos.append(getRow(256 * M, 35, disk, middleDiskIdleSize));
        diskInfos.append(getRow(128 * M, 36, disk, middleDiskIdleSize));
        diskInfos.append(getRow(65 * M, 56, disk, middleDiskIdleSize));
        diskInfos.append(getRow(64 * M, 67, disk, middleDiskIdleSize));
        diskInfos.append(getRow(1 * K, 79, disk, middleDiskIdleSize));
        
        long maxDiskIdleSize = 4 * G;
        disk = "/data1";
        diskInfos.append(getRow(5 * G, 2, disk, maxDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(true, mc.isNeedBalance());
        
        List<MovePolicy> movePolicies = mc.getMovePolicies();
        Collections.sort(movePolicies);
        List<MovePolicy> expects = new ArrayList<MovePolicy>();
        expects.add(new MovePolicy("/data3/hadoop/data/current/subdir44",
                "/data1/hadoop/data/current/subdir3"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir44",
                "/data1/hadoop/data/current/subdir4"));
        expects.add(new MovePolicy("/data3/hadoop/data/current/subdir35",
                "/data1/hadoop/data/current/subdir5"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir35",
                "/data1/hadoop/data/current/subdir6"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir36",
                "/data1/hadoop/data/current/subdir7"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir56",
                "/data1/hadoop/data/current/subdir8"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir79",
                "/data1/hadoop/data/current/subdir9"));
        Collections.sort(expects);
        assertEquals(expects, movePolicies);
    }
    
    /**
     * 测试移动策略[多移出磁盘，多移入磁盘]
     * @throws Exception 异常
     */
    @Test
    public void testMultiOutInGetMovePolicy() throws Exception
    {
        MoveControl mc = new MoveControl();
        long lThresholdMax = 3 * G;
        long lThresholdMin = 2 * G;
        long lDiffThreshold = 1 * G;
        mc.initialize(lThresholdMax, lThresholdMin, lDiffThreshold);
        StringBuilder diskInfos = new StringBuilder();
        String disk = "/data1";
        long minDiskIdleSize = 1 * G;
        diskInfos.append(getRow(1984 * M, 21, disk, minDiskIdleSize));
        diskInfos.append(getRow(1088 * M, 32, disk, minDiskIdleSize));
        diskInfos.append(getRow(512 * M, 44, disk, minDiskIdleSize));
        diskInfos.append(getRow(256 * M, 35, disk, minDiskIdleSize));
        diskInfos.append(getRow(128 * M, 36, disk, minDiskIdleSize));
        diskInfos.append(getRow(65 * M, 56, disk, minDiskIdleSize));
        diskInfos.append(getRow(62 * M, 67, disk, minDiskIdleSize));
        diskInfos.append(getRow(1 * K, 79, disk, minDiskIdleSize));
        
        long middleDiskIdleSize1 = 2 * G;
        disk = "/data2";
        diskInfos.append(getRow(65 * M, 1, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(1984 * M, 21, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(1088 * M, 32, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(768 * M, 44, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(256 * M, 35, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(128 * M, 36, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(60 * M, 67, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(1 * M, 78, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(1 * M, 79, disk, middleDiskIdleSize1));
        diskInfos.append(getRow(1 * M, 80, disk, middleDiskIdleSize1));
        
        long middleDiskIdleSize2 = 3 * G;
        disk = "/data3";
        diskInfos.append(getRow(64 * M, 1, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(1984 * M, 21, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(1088 * M, 32, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(768 * M, 44, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(256 * M, 35, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(128 * M, 36, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(64 * M, 67, disk, middleDiskIdleSize2));
        diskInfos.append(getRow(1 * K, 79, disk, middleDiskIdleSize2));
        
        long maxDiskIdleSize = 4 * G;
        disk = "/data4";
        diskInfos.append(getRow(5 * G, 2, disk, maxDiskIdleSize));
        @SuppressWarnings("deprecation")
        StringBufferInputStream sInput = new StringBufferInputStream(
                diskInfos.toString());
        mc.getDiskInfo(sInput);
        assertEquals(true, mc.isNeedBalance());
        
        List<MovePolicy> movePolicies = mc.getMovePolicies();
        Collections.sort(movePolicies);
        List<MovePolicy> expects = new ArrayList<MovePolicy>();
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir32",
                "/data4/hadoop/data/current/subdir3"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir35",
                "/data4/hadoop/data/current/subdir4"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir36",
                "/data4/hadoop/data/current/subdir5"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir67",
                "/data4/hadoop/data/current/subdir6"));
        expects.add(new MovePolicy("/data1/hadoop/data/current/subdir79",
                "/data3/hadoop/data/current/subdir86"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir35",
                "/data3/hadoop/data/current/subdir80"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir36",
                "/data3/hadoop/data/current/subdir81"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir1",
                "/data3/hadoop/data/current/subdir82"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir67",
                "/data3/hadoop/data/current/subdir83"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir80",
                "/data3/hadoop/data/current/subdir84"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir79",
                "/data3/hadoop/data/current/subdir85"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir78",
                "/data4/hadoop/data/current/subdir7"));
        expects.add(new MovePolicy("/data2/hadoop/data/current/subdir79",
                "/data4/hadoop/data/current/subdir8"));
        Collections.sort(expects);
        assertEquals(expects, movePolicies);
    }
    
    private String getRow(long subdirSize, int subdirId, String disk,
            long diskIdleSize)
    {
        return String.format("%d,%s/hadoop/data/current/subdir%d,%d\n",
                subdirSize,
                disk,
                subdirId,
                diskIdleSize);
    }
}
