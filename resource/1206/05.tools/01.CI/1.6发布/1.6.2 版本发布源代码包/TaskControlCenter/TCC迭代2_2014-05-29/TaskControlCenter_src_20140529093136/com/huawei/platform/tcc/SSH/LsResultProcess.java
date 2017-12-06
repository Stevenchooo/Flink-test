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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 标准输出流处理类
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class LsResultProcess extends StreamProcess
{
    //日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(LsResultProcess.class);
    
    private static final int SMALL_SIZE = 10;
    
    private List<String> fileNames = new ArrayList<String>(SMALL_SIZE);
    
    /**
     * 默认构造函数
     * @param id id对象
     * @param stream 流对象
     */
    public LsResultProcess(Object id, InputStream stream)
    {
        super(id, stream);
    }
    
    /**
     * 获取所有的文件名列表
     * @return 文件名列表
     */
    public List<String> getFileNames()
    {
        List<String> files = new ArrayList<String>(fileNames.size());
        files.addAll(fileNames);
        return files;
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
            
            fileNames.clear();
            
            while (true)
            {
                line = bReader.readLine();
                if (line == null)
                {
                    break;
                }
                
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
    
    //处理行【获取文件】
    private void handleLine(String line)
    {
        String[] fileStrs = line.split(" ");
        for (String file : fileStrs)
        {
            //文件名不能为空
            if (!StringUtils.isBlank(file))
            {
                fileNames.add(file);
            }
        }
    }
}
