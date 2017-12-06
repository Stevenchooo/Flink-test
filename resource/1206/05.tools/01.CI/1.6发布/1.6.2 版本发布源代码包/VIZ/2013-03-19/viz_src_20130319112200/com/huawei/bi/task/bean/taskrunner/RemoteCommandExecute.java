/*
 * 文 件 名:  RemoteCommandExecute.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-29
 */
package com.huawei.bi.task.bean.taskrunner;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.parse.HiveParser.execStatement_return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import com.huawei.bi.common.login.UserCheck;
import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.domain.Task;
import com.huawei.bi.util.Config;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-29]
 */
public class RemoteCommandExecute
{
    private static Logger log = LoggerFactory.getLogger(RemoteCommandExecute.class);
    
    private static final String IP = Config.get("app.login.server.ip");
    
    private static final String KILL_COMMOND = Config.get("app.hive.killjob");
    
    private IExeLogger exeLogger;
    
    /**
     * 连接实例
     */
    private Connection conn = null;
    
    /**
     * 会话实例
     */
    private Session session = null;
    
    /**
     * 当前正运行的jobid
     */
    private String[] jobId = new String[1];
    
    public RemoteCommandExecute(IExeLogger exeLogger)
    {
        this.exeLogger = exeLogger;
    }
    
    /**
     * 停止正在执行的命令
     * @throws IOException 
     */
    public void stop()
    {
        log.debug("enter stop...");
        //连接未关闭
        if (null != conn)
        {
            Session session = null;
            String cjobid = null;
            String command = null;
            try
            {
                if (jobId.length > 0)
                {
                    cjobid = jobId[0];
                }
                
                //jobid有效
                if (!StringUtils.isBlank(cjobid))
                {
                    session = conn.openSession();
                    command = String.format(KILL_COMMOND, cjobid);
                    session.execCommand(command);
                    
                    //启动线程同步输出hadoop日志
                    StreamLogger errorS = new StreamLogger(session.getStderr(), "ERROR", exeLogger);
                    
                    //kick  off  stderr  
                    errorS.start();
                    
                    StreamLogger outS = new StreamLogger(session.getStdout(), "STDOUT", exeLogger);
                    
                    //kick  off  stdout  
                    outS.start();
                    try
                    {
                        //主线程等待子线程
                        errorS.join();
                        outS.join();
                    }
                    catch (InterruptedException e)
                    {
                        log.warn("Thread exception.", e);
                    }
                    int exitCode = -1;
                    if (session != null)
                    {
                        try
                        {
                            exitCode = session.getExitStatus();
                            log.debug("Command returned with exit status: " + exitCode);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                    if (exitCode != 0)
                    {
                        log.debug("Execute command[{}] exit code [{}]", command, exitCode);
                    }
                }
            }
            catch (IOException e)
            {
                //出错原因可能是连接已经断开，等等
                log.debug("stop failed! jobid is {}", new Object[] {cjobid, e});
            }
            finally
            {
                //关闭会话
                if (null != session)
                {
                    session.close();
                }
                //连接有start方法正常关闭
            }
        }
        log.debug("exit stop...");
    }
    
    public void start(String user, String command)
    {
        try
        {
            conn = new Connection(IP);
            //连接 
            conn.connect();
            //认证
            if (conn.authenticateWithPassword(user, UserCheck.getUserPwd(user)))
            {
                //打开一个会话 
                session = conn.openSession();
                
                //执行命令
                session.execCommand(command);
                //启动线程同步输出hadoop日志
                StreamGobbler errorGobbler = new StreamGobbler(session.getStderr(), "ERROR", jobId, exeLogger);
                
                //kick  off  stderr  
                errorGobbler.start();
                
                StreamGobbler outGobbler = new StreamGobbler(session.getStdout(), "STDOUT", null, exeLogger);
                
                //kick  off  stdout  
                outGobbler.start();
                try
                {
                    //主线程等待子线程
                    outGobbler.join();
                    errorGobbler.join();
                }
                catch (InterruptedException e)
                {
                    log.warn("Thread exception.", e);
                }
                int exitCode = -1;
                if (session != null)
                {
                    try
                    {
                        exitCode = session.getExitStatus();
                        log.debug("Command returned with exit status: " + exitCode);
                    }
                    catch (Exception e)
                    {
                    }
                }
                if (exitCode != 0 || !errorGobbler.isSucessed() || !outGobbler.isSucessed())
                {
                    throw new TaskExecuteException("Execute command exit code " + exitCode);
                }
            }
            else
            {
                throw new TaskExecuteException("login failed!");
            }
        }
        catch (Exception e)
        {
            try
            {
                exeLogger.writeDebug("error occurs");
                exeLogger.writeDebug(e.getMessage());
            }
            catch (IOException ex)
            {
                log.warn("IO exception when execute hive command." + ex.getMessage());
            }
            log.error("Remote command execute exception.", e);
            throw new TaskExecuteException("Remote command execute exception", e);
        }
        finally
        {
            if (session != null)
            {
                session.close();
                session = null;
            }
            if (conn != null)
            {
                conn.close();
                conn = null;
            }
        }
    }
}
