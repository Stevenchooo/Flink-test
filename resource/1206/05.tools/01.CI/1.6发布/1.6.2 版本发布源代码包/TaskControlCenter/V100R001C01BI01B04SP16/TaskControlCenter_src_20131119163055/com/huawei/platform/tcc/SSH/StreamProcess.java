/*
 * 文 件 名:  StreamProcess.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  流处理类
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-29
 */
package com.huawei.platform.tcc.SSH;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流处理类
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public abstract class StreamProcess implements Runnable
{
    //日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamProcess.class);
    
    //命令执行日志记录器
    private static final Logger CMD_EXECUTE_LOGGER = LoggerFactory.getLogger("cmd_execute_info");
    
    //id
    private Object id;
    
    //流对象
    private InputStream stream;
    
    //是否执行成功
    private boolean sucess = false;
    
    //是否请求忽略错误码
    private boolean ignoreExitCode = false;
    
    /**
     * 默认构造函数
     * @param id id对象
     * @param stream 流对象
     */
    public StreamProcess(Object id, InputStream stream)
    {
        this.id = id;
        this.stream = stream;
    }
    
    protected InputStream getStream()
    {
        return stream;
    }
    
    protected Object getId()
    {
        return id;
    }
    
    public boolean isSucess()
    {
        return sucess;
    }
    
    protected void setSucess(boolean sucess)
    {
        this.sucess = sucess;
    }
    
    /**
     * 是否请求忽略错误码
     * @return 是否请求忽略错误码
     */
    public boolean ignoreExitCode()
    {
        return ignoreExitCode;
    }
    
    protected void setIgnoreExitCode(boolean ignoreExitCode)
    {
        this.ignoreExitCode = ignoreExitCode;
    }
    
    /**
     * 流处理
     */
    public abstract void processStream();
    
    /**
     * 获取命令日志记录器
     * @return 命令日志记录器
     */
    public Logger getCmdLogger()
    {
        return CMD_EXECUTE_LOGGER;
    }
    
    @Override
    public void run()
    {
        try
        {
            processStream();
        }
        catch (Exception e)
        {
            setSucess(false);
            //打印异常
            LOGGER.error("processStream failed! id is {}, stream is {}", new Object[] {id, stream, e});
        }
    }
    
    /**
     * 关闭处理对象
     */
    public void close()
    {
        close(stream);
    }
    
    /**
     * 关闭指定流
     * @param closeObj 可关闭对象
     */
    public void close(Closeable closeObj)
    {
        //关闭流
        if (null != closeObj)
        {
            try
            {
                closeObj.close();
            }
            catch (IOException e)
            {
                LOGGER.error("close closeObj[{}] failed!", closeObj, e);
            }
        }
    }
}
