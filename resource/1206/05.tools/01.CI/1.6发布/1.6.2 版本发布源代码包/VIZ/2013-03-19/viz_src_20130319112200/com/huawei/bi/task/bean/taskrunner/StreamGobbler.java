/**
 * @(#)StreamGobbler.java 1.0 Oct 12, 2011
 * @Copyright:  Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * @Description: 
 * 
 * Modification History:
 * Date:        Oct 12, 2011
 * Author:      ZhangFeng 605801
 * Version:     HIBI V1.1
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package com.huawei.bi.task.bean.taskrunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.constant.Constants;
import com.huawei.bi.util.Constant;

/**
 * 处理返回的流
 * Copyright:   Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * Description: Initialize
 */
public class StreamGobbler extends Thread
{
    private static Logger log = LoggerFactory.getLogger(StreamGobbler.class);
    
    private InputStream is;
    
    private String type;
    
    private IExeLogger exeLogger;
    
    /**
     * 是否执行成功
     */
    private boolean sucessed = true;
    
    //jobids
    private String[] jobId;
    
    StreamGobbler(InputStream is, String type, String[] jobId, IExeLogger exeLogger)
    {
        this.is = is;
        this.type = type;
        this.jobId = jobId;
        this.exeLogger = exeLogger;
    }
    
    public void run()
    {
        BufferedReader br = null;
        //当前正运行的jobid
        String cjobid;
        try
        {
            br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (type.equals("ERROR"))
                {
                    //提取jobid,更新到数据库
                    if (null != jobId && line.contains(Constants.START_JOB))
                    {
                        cjobid = extractJobId(line);
                        if (!StringUtils.isBlank(cjobid))
                        {
                            jobId[0] = cjobid;
                        }
                    }
                    
                    //程序抛出的异常信息
                    if (line.contains(Constants.EXCEPTION_IN))
                    {
                        this.sucessed = false;
                        log.error(line);
                    }
                    
                    if (null != exeLogger)
                    {
                        exeLogger.writeDebug(line);
                    }
                }
                else
                {
                    if (null != exeLogger)
                    {
                        exeLogger.writeResult(line);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("Read input stream error.", e);
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    log.warn("IO exception while read input stream.", e);
                }
                br = null;
            }
        }
    }
    
    /**
     * 提取jobId
     * @param line line
     */
    public String extractJobId(String line)
    {
        String jobline =
            line.substring(line.indexOf(Constants.START_JOB), line.indexOf(Constants.START_JOB) + Constants.FIFTY);
        
        //截取jobid
        String jobId =
            jobline.substring(jobline.indexOf(Constants.EQUAL_SIGN) + 1, jobline.indexOf(Constants.COMMA)).trim();
        return jobId;
    }
    
    public boolean isSucessed()
    {
        return sucessed;
    }
}
