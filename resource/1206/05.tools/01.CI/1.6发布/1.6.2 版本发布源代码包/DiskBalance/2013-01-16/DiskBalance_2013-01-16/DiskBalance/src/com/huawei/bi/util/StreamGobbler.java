/**
 * @(#)Constants.java 1.0 Oct 12, 2011
 * @Copyright:  Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * @Description: 
 * 
 * Modification History:
 * Date:        Oct 12, 2011
 * Author:      ZHANGFENG 11672
 * Version:     IDC V100R001C06
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package com.huawei.bi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程类 
 * Copyright:   Copyright 2000 - 2011 Huawei Tech. Co. Ltd. All Rights Reserved.
 * Date:        Nov 30, 2011
 * Author:      ChenHaiting 11672
 * Version:     IDC V100R001C06
 * Description: Initialize
 */
public class StreamGobbler extends Thread
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamGobbler.class);
    
    /**
     * 输入流
     */
    private InputStream is = null;
    
    /**
     * 输出流
     */
    private OutputStream os = null;
    
    /**
     * 构造器
     * @param is 输入流
     * @param type 类型
     */
    public StreamGobbler(InputStream is, String type)
    {
        this(is, type, null);
    }
    
    /**
     * 构造器
     * @param is 输入流
     * @param type 类型
     * @param redirect 输出
     */
    public StreamGobbler(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.os = redirect;
    }
    
    /**
     * 启动
     */
    public void run()
    {
        try
        {
            PrintWriter pw = null;
            
            if (null != os)
            {
                pw = new PrintWriter(os);
            }
            
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            
            while ((line = br.readLine()) != null)
            {
                if (null != pw)
                {
                    pw.println(line);
                }
                
                //往控制台打印
                LOGGER.info("exe info====" + line);
            }
            
            if (null != pw)
            {
                pw.flush();
            }
        }
        catch (IOException ioe)
        {
            LOGGER.error("execute failed!" + ioe);
        }
    }
}
