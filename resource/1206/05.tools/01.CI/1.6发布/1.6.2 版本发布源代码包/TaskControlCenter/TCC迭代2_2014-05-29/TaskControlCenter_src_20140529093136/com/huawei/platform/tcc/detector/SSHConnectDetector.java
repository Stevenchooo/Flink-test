/*
 * 文 件 名:  SSHConnectDetector.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-7-5
 */
package com.huawei.platform.tcc.detector;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;

/**
 * Ssh连接异常检测
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-7-5]
 */
public class SSHConnectDetector
{
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SSHConnectDetector.class);
    
    private static final SSHConnectDetector SINGLE = new SSHConnectDetector();
    
    /**
     * 正进行ssh连接的数目
     */
    private final AtomicInteger sshConnecttingNum = new AtomicInteger(0);
    
    /**
     * 上次检测时的连接数
     */
    private int lastNum = Integer.MAX_VALUE;
    
    /**
     * 持续增长次数
     */
    private int continueIncTimes = 0;
    
    public static SSHConnectDetector getInstance()
    {
        return SINGLE;
    }
    
    /**
     * 开始连接
     */
    public void start2Connect()
    {
        sshConnecttingNum.incrementAndGet();
    }
    
    /**
     * 结束连接
     */
    public void finish2Connect()
    {
        sshConnecttingNum.decrementAndGet();
    }
    
    /**
     * 获取正在进行ssh连接的数目
     * @return 正在进行ssh连接的数目
     */
    public int getConnecttingNum()
    {
        return sshConnecttingNum.get();
    }
    
    /**
     * 是否发现问题
     * @return 是否发现问题
     */
    public boolean foundProblem()
    {
        int cur = getConnecttingNum();
        if (cur > 0 && cur >= lastNum)
        {
            continueIncTimes++;
        }
        else
        {
            continueIncTimes = 0;
        }
        
        LOGGER.info("number of connectting ssh server is {},lastNum is {}, continueIncTimes is {}!", new Object[] {cur,
            lastNum, continueIncTimes});
        
        lastNum = cur;
        
        return continueIncTimes > TccConfig.getScbAscendTimesThreshold();
    }
}
