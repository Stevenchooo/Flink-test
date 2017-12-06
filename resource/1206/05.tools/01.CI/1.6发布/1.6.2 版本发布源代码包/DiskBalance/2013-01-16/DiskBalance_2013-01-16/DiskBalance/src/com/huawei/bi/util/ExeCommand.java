/*
 * 文 件 名:  ExeCommand.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  linux命令执行
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * linux命令执行
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-14]
 */
public class ExeCommand
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExeCommand.class);
    
    /**
     * java执行命令行程序框架
     * @param commands 命令数组
     * @throws Exception 异常
     */
    public static void exeCommand(String[] commands) throws Exception
    {
        Process process = null;
        Runtime rt = Runtime.getRuntime();
        try
        {
            //执行command
            process = rt.exec(commands);
            
            //启动线程同步输出日志
            StreamGobbler errorGobbler = new StreamGobbler(
                    process.getErrorStream(), "ERROR");
            //kick  off  stderr  
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(
                    process.getInputStream(), "STDOUT");
            //kick  off  stdout  
            outGobbler.start();
            process.waitFor();
            //返回状态
            int exitValue = process.exitValue();
            if (0 != exitValue)
            {
                LOGGER.error("execute commands[{}] failed. exitValue is {}.",
                        commands,
                        exitValue);
                throw new Exception(
                        String.format("execute command[%s] failed. exitValue is %s.",
                                toString(commands),
                                exitValue));
            }
        }
        catch (IOException e)
        {
            LOGGER.error("execute error!!!" + e);
            throw e;
        }
        catch (InterruptedException e)
        {
            LOGGER.error("execute error!!!" + e);
            throw e;
        }
    }
    
    private static String toString(Object[] objs)
    {
        if (null == objs || objs.length == 0)
        {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs)
        {
            sb.append(obj);
            sb.append(';');
        }
        
        return sb.toString();
    }
}
