/*
 * 文 件 名:  Dispatcher.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  调度
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.exception.ExecuteException;

/**
 * 调度
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-11]
 */
public class Dispatcher
{
    private static final int HUNDRED = 100;
    
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);
    
    /**
     * 默认最小剩余空间阈值
     */
    private static final long DEFAULT_THRESHOLDMIN = 200 * 1024 * 1024 * 1024L;
    
    /**
     * 默认最大剩余空间阈值
     */
    private static final long DEFAULT_THRESHOLDMAX = 300 * 1024 * 1024 * 1024L;
    
    /**
     * 默认最大空间差值阈值
     */
    private static final long DEFAULT_THRESHOLDDIFF = 100 * 1024 * 1024 * 1024L;
    
    //最大磁盘空间阈值(单位字节)
    private long thresholdMax = DEFAULT_THRESHOLDMAX;
    
    //最小磁盘空间阈值(单位字节)
    private long thresholdMin = DEFAULT_THRESHOLDMIN;
    
    //磁盘差异空间阈值(单位字节)
    private long thresholdDiff = DEFAULT_THRESHOLDDIFF;
    
    private void parseArgs(String[] args) throws Exception
    {
        if (null != args && args.length > 0)
        {
            String arg;
            for (int i = 0; i < args.length; i++)
            {
                arg = args[i];
                if (arg.equalsIgnoreCase("--min_threshold"))
                {
                    thresholdMin = Long.parseLong(args[i + 1].trim());
                }
                else if (arg.equalsIgnoreCase("--max_threshold"))
                {
                    thresholdMax = Long.parseLong(args[i + 1].trim());
                }
                else if (arg.equalsIgnoreCase("--diff_threshold"))
                {
                    thresholdDiff = Long.parseLong(args[i + 1].trim());
                }
            }
        }
    }
    
    /**
     * 平衡磁盘
     * @throws Exception 异常
     */
    public void balanceDisk() throws Exception
    {
        // 初始化
        MoveControl moveControl = new MoveControl();
        moveControl.initialize(thresholdMax, thresholdMin, thresholdDiff);
        
        moveControl.getDiskInfo();
        //平衡开始时的磁盘空闲值均方差
        double sBlanceVar = moveControl.printDisks();
        if (moveControl.isNeedBalance())
        {
            moveControl.quitHadoop();
            moveControl.getMovePolicies();
            moveControl.executeMove();
            moveControl.enterHadoop();
            //平衡结束时的磁盘空闲值均方差
            double eBlanceVar = moveControl.printDisks();
            
            String msg = String.format("balance successfully! the measure of disk balance raise %2.2f%s",
                    (sBlanceVar - eBlanceVar) / sBlanceVar * HUNDRED,
                    '%');
            if (eBlanceVar < sBlanceVar)
            {
                LOGGER.info(msg);
            }
            else
            {
                //平衡度不应该降低
                LOGGER.error(msg);
                throw new ExecuteException(msg);
            }
        }
    }
    
    /**
     * 主函数
     * @param args 参数
     */
    public static void main(String[] args)
    {
        LOGGER.info("start...");
        Dispatcher dispatcher = new Dispatcher();
        try
        {
            dispatcher.parseArgs(args);
            dispatcher.balanceDisk();
        }
        catch (Exception e)
        {
            LOGGER.error("execute failed!", e);
            LOGGER.info("step 8. exit failture!");
            //定义错误码
            System.exit(1);
        }
        
        LOGGER.info("step 8. exit successfully!");
        System.exit(0);
    }
}
