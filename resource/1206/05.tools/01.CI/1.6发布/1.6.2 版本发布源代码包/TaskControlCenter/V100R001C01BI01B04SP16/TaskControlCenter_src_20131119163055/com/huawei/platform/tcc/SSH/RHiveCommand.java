/*
 * 文 件 名:  RHiveCommand.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  SSH连接
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-29
 */
package com.huawei.platform.tcc.SSH;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.exception.ExecuteException;
import com.mindbright.ssh2.SSH2Connection;
import com.mindbright.ssh2.SSH2SessionChannel;

/**
 * 远程hive命令执行
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class RHiveCommand implements SSHCommand
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(RHiveCommand.class);
    
    //最大超时时间为1天
    private static final int MAX_TIME_OUT = 24 * 3600 * 1000;
    
    //等待killjob的时间
    private static final int WAIT_KILL_JOB = 60 * 1000;
    
    //行
    private static final int ROW = 40;
    
    //列
    private static final int COL = 80;
    
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
     * 输入流
     */
    private PrintWriter printer;
    
    /**
     * 命令
     */
    private String command;
    
    /**
     * 会话实例
     */
    private SSH2SessionChannel ssh2Session;
    
    /**
     * 错误流处理器
     */
    private StreamProcess errorProcess;
    
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
     * @param idDefault 默认Id
     * @param sshConn 连接信息
     * @param command 执行命令
     */
    public RHiveCommand(Object idDefault, SSHConnect sshConn, String command)
    {
        this.id = idDefault;
        this.sshConn = sshConn;
        this.ssh2Conn = sshConn.getSsh2Conn();
        this.command = command;
    }
    
    /**
     * 使用默认Id执行命令
     * @throws ExecuteException 异常
     */
    public void execute()
        throws ExecuteException
    {
        execute(null);
    }
    
    public boolean isFinished()
    {
        return finished;
    }
    
    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }
    
    /**
     * 执行命令
     * @param idNew 命令执行标识
     * @throws ExecuteException 异常
     */
    public void execute(Object idNew)
        throws ExecuteException
    {
        //不允许重复执行
        if (started)
        {
            return;
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
            execCmd();
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
            close();
        }
        
        if (this.state != RunningState.SUCCESS)
        {
            throw new ExecuteException(this.state);
        }
    }
    
    /**
     * 停止正运行的命令
     */
    @Override
    public void stop()
    {
        LOGGER.info("stoping");
        //关闭会话即可停止掉终端以及启动的进程
        if (!stop)
        {
            //状态改为停止状态
            this.state = RunningState.STOP;
            this.stop = true;
            
            this.close();
            
            //获取jobid
            if (null != resultProcess)
            {
                String jobId = ((ResultProcess)resultProcess).getLatestJobId();
                if (null != jobId)
                {
                    killJob(jobId);
                }
            }
        }
        else
        {
            LOGGER.info("stoped! don't need to stop!");
        }
        LOGGER.info("stoped!");
    }
    
    /**
     * 运行命令
     */
    private void execCmd()
        throws IOException
    {
        if (null == ssh2Conn)
        {
            LOGGER.error("ssh2Conn state is not correct! id is {}, cmd is {}", new Object[] {id, command});
            return;
        }
        
        //打开会话
        ssh2Session = ssh2Conn.newSession();
        
        if (stop)
        {
            //stop方法发生在会话创建之前，需要重新调用stop方法
            this.stop();
            return;
        }
        
        //错误流经过修改后可以关闭
        ssh2Session.enableStdErr();
        //请求终端以及开启shell
        ssh2Session.requestPTY("dumb", ROW, COL, null);
        ssh2Session.doShell();
        
        //初始化标准输出流处理器
        resultProcess = new ResultProcess(id, ssh2Session.getStdOut());
        //初始化错误流处理器
        errorProcess = new ErrorProcess(id, ssh2Session.getStdErr());
        //用线程池提交两个任务
        Thread thdRP = new Thread(resultProcess);
        thdRP.start();
        Thread thdEP = new Thread(errorProcess);
        thdEP.start();
        //ThreadPoolUtil.submmitTask(resultProcess);
        //ThreadPoolUtil.submmitTask(errorProcess);
        
        //提交命令
        printer = new PrintWriter(ssh2Session.getStdIn());
        
        printer.println(command);
        printer.flush();
        printer.println("exit $?");
        printer.flush();
        
        //等待执行完毕
        try
        {
            thdRP.join();
            thdEP.join();
        }
        catch (Exception e)
        {
            LOGGER.info("", e);
        }
        
        //执行完毕
        finished = true;
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
        Integer exitStatus = waitForExitStatus(MAX_TIME_OUT);
        boolean rpSucess = resultProcess.isSucess();
        boolean epSucess = errorProcess.isSucess();
        if (null != exitStatus && exitStatus.equals(0) && rpSucess && epSucess)
        {
            //默认失败，仅此记录状态为成功
            this.state = RunningState.SUCCESS;
            
            //记录日志
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("execute failed! id[{}],connectInfo[{}],command[{}],"
                    + "ssh2Session[exitStatus={}], resultProcess[{}], errorProcess[{}]",
                    new Object[] {id, sshConn.getConInfo(), command, exitStatus, rpSucess, epSucess});
            }
        }
        else
        {
            //执行失败
            LOGGER.error("execute failed! id[{}],connectInfo[{}],command[{}],"
                + "ssh2Session[exitStatus={}], resultProcess[{}], errorProcess[{}]",
                new Object[] {id, sshConn.getConInfo(), command, exitStatus, rpSucess, epSucess});
        }
    }
    
    private void closeNonConn()
    {
        try
        {
            //关闭输入流
            if (null != printer)
            {
                printer.close();
            }
            
            //关闭错误流处理
            if (null != errorProcess)
            {
                errorProcess.close();
            }
            
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
    
    private void killJob(String jobId)
    {
        String killJobCmd = String.format(TccConfig.getKillJobCmdTemplate(), jobId);
        //复用原来的连接，执行command命令
        boolean successed = this.sshConn.waitAMoment(WAIT_KILL_JOB);
        if (!successed)
        {
            LOGGER.warn("sshConn closed! cancel to kill job[{}]", jobId);
            return;
        }
        
        RHiveCommand sshKillConn = new RHiveCommand(id, this.sshConn, killJobCmd);
        try
        {
            sshKillConn.execute();
        }
        catch (ExecuteException e)
        {
            LOGGER.error("error state is {}!", e.getState());
        }
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
