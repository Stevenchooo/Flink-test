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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;

/**
 * 处理返回的流
 * Copyright:   Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * Description: Initialize
 */
public class StreamLogger extends Thread
{
    private static Logger log = LoggerFactory.getLogger(StreamLogger.class);
    
    private InputStream is;
    
    private String type;
    
    private IExeLogger exeLogger;
    
    StreamLogger(InputStream is, String type, IExeLogger exeLogger)
    {
        this.is = is;
        this.type = type;
        this.exeLogger = exeLogger;
    }
    
    public void run()
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (type.equals("ERROR"))
                {
                    if (null != exeLogger)
                    {
                        exeLogger.writeDebug(line);
                    }
                    log.error(line);
                }
                else
                {
                    if (null != exeLogger)
                    {
                        exeLogger.writeDebug(line);
                    }
                    log.debug(line);
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
}
