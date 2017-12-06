/*
 * 文 件 名:  StreamProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  错误流处理类
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-29
 */
package com.huawei.platform.tcc.SSH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 错误流处理类
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class LsErrorProcess extends StreamProcess
{
    //日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorProcess.class);
    
    private static final String NO_FILE_OUT = "ls: No match";
    
    /**
     * 默认构造函数
     * @param id id对象
     * @param stream 流对象
     */
    public LsErrorProcess(Object id, InputStream stream)
    {
        super(id, stream);
    }
    
    /**
     * 流处理
     */
    @Override
    public void processStream()
    {
        if (null == getStream())
        {
            setSucess(true);
            return;
        }
        //默认失败，不用调用setSucess(false);
        //reader
        BufferedReader bReader = null;
        String line = null;
        String firstLine = null;
        long i = 0;
        try
        {
            bReader = new BufferedReader(new InputStreamReader(getStream()));
            
            while (true)
            {
                line = bReader.readLine();
                
                if (0 == i)
                {
                    if (line == null)
                    {
                        //没有错误流输出，则为成功
                        setSucess(true);
                        break;
                    }
                    else
                    {
                        if (line.contains(NO_FILE_OUT))
                        {
                            firstLine = line;
                        }
                        else
                        {
                            getCmdLogger().error(line);
                        }
                    }
                }
                else
                {
                    //如果前一行line.contains(NO_FILE_OUT)而且仅有一行
                    if (1 == i && null != firstLine)
                    {
                        if (null == line)
                        {
                            setSucess(true);
                            setIgnoreExitCode(true);
                            break;
                        }
                        else
                        {
                            //补充显示前一行
                            getCmdLogger().error(firstLine);
                        }
                    }
                    
                    if (line == null)
                    {
                        break;
                    }
                    
                    //命令输出记录到指定的日志记录器中
                    getCmdLogger().error(line);
                }
                
                i++;
            }
        }
        catch (IOException e)
        {
            if (1 == i && null != firstLine)
            {
                setSucess(true);
                setIgnoreExitCode(true);
            }
            
            //默认失败，不用调用setSucess(false);
            LOGGER.error("read errStream error!", e);
        }
        finally
        {
            //关闭流
            close(bReader);
        }
    }
}
