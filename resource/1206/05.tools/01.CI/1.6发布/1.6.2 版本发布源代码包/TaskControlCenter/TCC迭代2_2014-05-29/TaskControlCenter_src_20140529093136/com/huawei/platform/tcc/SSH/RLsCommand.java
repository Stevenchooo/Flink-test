/*
 * 文 件 名:  RLsCommand.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  远程LS命令执行器
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-29
 */
package com.huawei.platform.tcc.SSH;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.exception.ExecuteException;
import com.mindbright.ssh2.SSH2Connection;
import com.mindbright.ssh2.SSH2SessionChannel;

/**
 * 远程LS命令执行器
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class RLsCommand implements SSHCommand
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(RLsCommand.class);
    
    //最大超时时间为1天
    private static final int MAX_TIME_OUT = 24 * 3600 * 1000;
    
    /**
     * 命令执行状态
     */
    private int state = RunningState.ERROR;
    
    /**
     * ssh连接
     */
    private SSHConnect sshConn;
    
    /**
     * 连接实例
     */
    private SSH2Connection ssh2Conn;
    
    /**
     * 命令
     */
    private String command;
    
    /**
     * 会话实例
     */
    private SSH2SessionChannel ssh2Session;
    
    /**
     * 标准输出流处理器
     */
    private StreamProcess resultProcess;
    
    private Object id;
    
    private boolean finished = false;
    
    private boolean started = false;
    
    private boolean stop = false;
    
    /**
     * 默认构造函数
     * @param id Id
     * @param sshConn 连接信息
     * @param command 执行命令
     */
    public RLsCommand(Object id, SSHConnect sshConn, String command)
    {
        this.id = id;
        this.sshConn = sshConn;
        this.ssh2Conn = sshConn.getSsh2Conn();
        this.command = command;
    }
    
    /**
     * 执行命令
     * @throws ExecuteException 异常
     * @return 文件名集合
     */
    public List<String> execute()
        throws ExecuteException
    {
        return execute(null);
    }
    
    /**
     * 执行命令
     * @param idNew 新的Id标识
     * @throws ExecuteException 异常
     * @return 文件名集合
     */
    public List<String> execute(Object idNew)
        throws ExecuteException
    {
        List<String> files = null;
        
        //不允许重复执行
        if (started)
        {
            return files;
        }
        else
        {
            started = true;
            if (null != idNew)
            {
                id = idNew;
            }
        }
        
        try
        {
            files = execCmd();
            grabState();
        }
        catch (Exception e)
        {
            //记录错误
            LOGGER.error("SSHConnect run failed!", e);
        }
        finally
        {
            //执行完毕
            finished = true;
            //如果保持连接
            close();
        }
        
        if (this.state != RunningState.SUCCESS)
        {
            throw new ExecuteException(this.state);
        }
        
        return files;
    }
    
    /**
     * 停止正运行的命令
     */
    @Override
    public void stop()
    {
        LOGGER.info("stoping");
        //关闭会话即可停止掉终端以及启动的进程
        if (!finished)
        {
            //状态改为停止状态
            this.state = RunningState.STOP;
            this.stop = true;
            this.close();
        }
        else
        {
            LOGGER.info("finished! don't need to stop!");
        }
        LOGGER.info("stoped!");
    }
    
    /**
     * 运行命令
     */
    private List<String> execCmd()
        throws IOException
    {
        if (null == ssh2Conn)
        {
            LOGGER.error("ssh2Conn state is not correct! id is {}, cmd is {}", new Object[] {id, command});
            return null;
        }
        
        List<String> fileNames = null;
        
        //打开会话
        ssh2Session = ssh2Conn.newSession();
        
        if (stop)
        {
            //stop方法发生在会话创建之前，需要重新调用stop方法
            this.stop();
            return fileNames;
        }
        
        //禁用错误输出流，因为即使使用单独线程读取，ssh2依赖处理很慢影响整体性能
        //ssh2Session.enableStdErr();
        ssh2Session.doSingleCommand(command);
        //ssh2Session.doExit(0);
        //初始化标准输出流处理器
        resultProcess = new LsResultProcess(id, ssh2Session.getStdOut());
        //用线程池提交任务
        Thread thdRP = new Thread(resultProcess);
        thdRP.start();
        try
        {
            thdRP.join();
        }
        catch (Exception e)
        {
            LOGGER.info("", e);
        }
        
        fileNames = ((LsResultProcess)resultProcess).getFileNames();
        //执行完毕
        finished = true;
        
        return fileNames;
    }
    
    //获取运行状态
    private void grabState()
    {
        if (null == ssh2Session)
        {
            LOGGER.error("ssh2Session is null!");
            return;
        }
        
        if (state == RunningState.STOP)
        {
            return;
        }
        
        //两个流执行正确
        waitForExitStatus(MAX_TIME_OUT);
        Integer exitStatus = waitForExitStatus(MAX_TIME_OUT);
        boolean rpSucess = resultProcess.isSucess();
        //根据是否忽略处错误处理
        if ((null != exitStatus && exitStatus.equals(0)) && rpSucess)
        {
            //默认失败，仅此记录状态为成功
            this.state = RunningState.SUCCESS;
        }
        else
        {
            //执行失败
            LOGGER.error("execute failed! id[{}],connectInfo[{}], command[{}],"
                + " ssh2Session[exitStatus={}], resultProcess[{}]",
                new Object[] {id, sshConn.getConInfo(), command, exitStatus, rpSucess});
        }
    }
    
    private void closeNonConn()
    {
        try
        {
            //关闭标准输出流处理
            if (null != resultProcess)
            {
                resultProcess.close();
            }
            
            //关闭会话
            if (null != ssh2Session)
            {
                ssh2Session.close();
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("closeNonConn failed!id=[{}]", id, e);
        }
    }
    
    private void close()
    {
        //关闭会话等
        closeNonConn();
    }
    
    //等待退出码
    private int waitForExitStatus(int timeout)
    {
        if (ssh2Session == null)
        {
            return -1;
        }
        int status = ssh2Session.waitForExit(timeout);
        ssh2Session.waitUntilClosed(timeout);
        return status;
    }
}
