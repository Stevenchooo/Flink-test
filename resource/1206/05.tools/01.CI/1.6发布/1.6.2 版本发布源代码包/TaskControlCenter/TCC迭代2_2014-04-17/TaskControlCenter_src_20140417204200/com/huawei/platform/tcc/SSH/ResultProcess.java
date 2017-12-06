/*
 * 文 件 名:  StreamProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  标准输出流处理类
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

import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;

/**
 * 标准输出流处理类
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class ResultProcess extends StreamProcess
{
    //日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultProcess.class);
    
    //开始job
    private static final String START_JOB = "Starting Job = ";
    
    //包含异常描述字符串
    private static final String EXCEPTION_IN = "Exception in thread";
    
    //jobid前缀
    private static final String JOB_ID_PREFIX = "job_";
    
    //逗号
    private static final String COMMA = ",";
    
    //jobids
    private StringBuilder jobIds = new StringBuilder();
    
    //最新的JobId
    private String latestJobId;
    
    /**
     * 默认构造函数
     * @param id id对象
     * @param stream 流对象
     */
    public ResultProcess(Object id, InputStream stream)
    {
        super(id, stream);
    }
    
    /**
     * 获取最新的jobId
     * @return 最新的jobId
     */
    public String getLatestJobId()
    {
        //获取字符串拷贝
        return latestJobId;
    }
    
    /**
     * 流处理
     */
    public void processStream()
    {
        setSucess(true);
        //reader
        BufferedReader bReader = null;
        String line = null;
        try
        {
            bReader = new BufferedReader(new InputStreamReader(getStream()));
            while (true)
            {
                line = bReader.readLine();
                if (line == null)
                {
                    break;
                }
                
                //命令输出记录到指定的日志记录器中
                getCmdLogger().info(line);
                
                //处理行
                handleLine(line);
            }
        }
        catch (IOException e)
        {
            //失败
            setSucess(false);
            LOGGER.error("read errStream error!", e);
        }
        finally
        {
            //关闭流
            close(bReader);
        }
    }
    
    //处理行【提取jobid以及异常】
    private void handleLine(String line)
    {
        String jobId = parseJobId(line);
        if (null != jobId)
        {
            //最新jobid
            latestJobId = jobId;
            //添加到jobids集合中
            jobIds.append(jobId);
            jobIds.append(COMMA);
            
            Eventor.fireEvent(this, EventType.UPDATE_JOB_IDS, new Object[] {getId(), jobIds.toString()});
        }
        
        //程序抛出的异常信息
        if (line.contains(EXCEPTION_IN))
        {
            //发生异常
            setSucess(false);
        }
    }
    
    private String parseJobId(String line)
    {
        int startJobIndex = line.indexOf(START_JOB);
        //提取jobid,更新到数据库
        if (-1 != startJobIndex)
        {
            int sJobIdIndex = startJobIndex + START_JOB.length();
            int eJobIdIndex = line.indexOf(",");
            //截取jobid
            String jobId = line.substring(sJobIdIndex, eJobIdIndex);
            
            //检查jobid是否正确
            if (null != jobId && jobId.startsWith(JOB_ID_PREFIX))
            {
                //更新jobids
                return jobId;
            }
            else
            {
                LOGGER.error("parserJobId error! line is [{}], jobId is [{}].", line, jobId);
            }
        }
        
        return null;
    }
}
