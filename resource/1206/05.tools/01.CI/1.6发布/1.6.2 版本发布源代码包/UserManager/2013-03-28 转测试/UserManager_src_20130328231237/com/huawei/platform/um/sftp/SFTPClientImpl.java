/*
 * 文 件 名:  SFTPClientImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2012-3-31
 */
package com.huawei.platform.um.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.um.constants.ResultCode;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * sftp工具
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2012-3-31]
 */
public class SFTPClientImpl
{
    /**
     * 公共的SFTP
     */
    private ChannelSftp sftp;
    
    /**
     * 公用的execchannel
     */
    private ChannelExec channelExec;
    
    /**
     * SFTP服务器IP
     */
    private String host;
    
    /**
     * SFTP服务器端口号
     */
    private Integer port;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 目标路径
     */
    private String basePath;
    
    /**
     *公用的session 
     */
    private Session sshSession;
    
    /**
     * 日志常量
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPClientImpl.class);
    
    public String getHost()
    {
        return host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public Integer getPort()
    {
        return port;
    }
    
    public void setPort(Integer port)
    {
        this.port = port;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getBasePath()
    {
        return basePath;
    }
    
    public void setBasePath(String basePath)
    {
        this.basePath = basePath;
    }
    
    //初始化SFTP链接
    public void initRemoteSFTPService()
        throws CException
    {
        this.sftp = connect(this.host, this.port, this.username, this.password);
    }
    
    //关闭SFTP链接
    public void disconnectSFTPService()
    {
        if (null != this.sftp)
        {
            this.sftp.disconnect();
        }
    }
    
    /**
     * 连接sftp服务器
     * @param host 主机
     * @param port  端口
     * @param username 用户名
     * @param password 密码
     * @return sftp通道
     * @throws CException 异常
     */
    private ChannelSftp connect(String host, int port, String username, String password)
        throws CException
    {
        ChannelSftp sftp = null;
        try
        {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(username, host, port);
            LOGGER.debug("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            LOGGER.debug("Session connected.");
        }
        catch (JSchException e)
        {
            LOGGER.error("connect to sftp failed! Exception is {}", new Object[] {e});
            
            if (e.getCause() instanceof UnknownHostException)
            {
                throw new CException(ResultCode.HOST_NO_EXIST, new Object[] {host});
            }
            else if (e.getCause() instanceof ConnectException)
            {
                throw new CException(ResultCode.CONNECT_TIMEOUT, new Object[] {host, port});
            }
            else
            {
                String msg = e.getMessage();
                if ("Auth cancel".equals(msg) || "Auth fail".equals(msg))
                {
                    throw new CException(ResultCode.AUTH_FAILED, new Object[] {username});
                }
                else
                {
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("connect to sftp failed! Exception is {}", new Object[] {e});
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        
        return sftp;
    }
    
    /**
     * 检查通道是否OK
     * @throws CException 异常
     */
    public void checkChannelOK(String dstDir)
        throws CException
    {
        Channel channel = null;
        FileInputStream fis = null;
        try
        {
            initRemoteSFTPService();
            channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            
            //逐级创建
            cdCreateDir(dstDir);
        }
        catch (JSchException e)
        {
            LOGGER.error("connect to sftp failed! Exception is {}", new Object[] {e});
            
            if (e.getCause() instanceof UnknownHostException)
            {
                throw new CException(ResultCode.HOST_NO_EXIST, new Object[] {host});
            }
            else if (e.getCause() instanceof ConnectException)
            {
                throw new CException(ResultCode.CONNECT_TIMEOUT, new Object[] {host, port});
            }
            else
            {
                String msg = e.getMessage();
                if ("Auth cancel".equals(msg) || "Auth fail".equals(msg))
                {
                    throw new CException(ResultCode.AUTH_FAILED, new Object[] {username});
                }
                else
                {
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
        }
        catch (SftpException se)
        {
            String msg = se.getMessage();
            if ("Permission denied".equals(msg))
            {
                throw new CException(ResultCode.PERMISSION_DENIED, new Object[] {dstDir});
            }
            else
            {
                throw new CException(ResultCode.SYSTEM_ERROR);
            }
        }
        catch (CException e)
        {
            LOGGER.error("scp file failed! Exception is {}", new Object[] {e});
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("scp file failed! Exception is {}", new Object[] {e});
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        finally
        {
            if (null != fis)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("close fis failed!", e);
                }
            }
            
            if (null != channel)
            {
                channel.disconnect();
            }
            
            disconnectSFTPService();
        }
    }
    
    /**
     * 上传文件
     * @param dstDir 目标目录[$HOME相对目录]
     * @param uploadFile 要上传的文件
     * @param dstFileName 目标文件名
     * @return 是否成功
     * @throws CException 异常
     */
    public void scp(File uploadFile, String dstDir, String dstFileName)
        throws CException
    {
        Channel channel = null;
        FileInputStream fis = null;
        try
        {
            initRemoteSFTPService();
            channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            
            //逐级创建
            cdCreateDir(dstDir);
            
            fis = new FileInputStream(uploadFile);
            this.sftp.put(fis, dstFileName);
        }
        catch (JSchException e)
        {
            LOGGER.error("connect to sftp failed! Exception is {}", new Object[] {e});
            
            if (e.getCause() instanceof UnknownHostException)
            {
                throw new CException(ResultCode.HOST_NO_EXIST, new Object[] {host});
            }
            else if (e.getCause() instanceof ConnectException)
            {
                throw new CException(ResultCode.CONNECT_TIMEOUT, new Object[] {host, port});
            }
            else
            {
                String msg = e.getMessage();
                if ("Auth cancel".equals(msg))
                {
                    throw new CException(ResultCode.AUTH_FAILED, new Object[] {username});
                }
                else
                {
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
        }
        catch (SftpException se)
        {
            String msg = se.getMessage();
            if ("Permission denied".equals(msg))
            {
                throw new CException(ResultCode.PERMISSION_DENIED, new Object[] {dstDir});
            }
            else
            {
                throw new CException(ResultCode.SYSTEM_ERROR);
            }
        }
        catch (CException e)
        {
            LOGGER.error("scp file failed! Exception is {}", new Object[] {e});
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("scp file failed! Exception is {}", new Object[] {e});
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        finally
        {
            if (null != fis)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("close fis failed!", e);
                }
            }
            
            if (null != channel)
            {
                channel.disconnect();
            }
            
            disconnectSFTPService();
        }
    }
    
    private void cdCreateDir(String dir)
        throws SftpException
    {
        if (StringUtils.isNotBlank(dir))
        {
            //替换HOME目录
            dir = dir.replace("$HOME", this.sftp.getHome());
        }
        
        if (StringUtils.isNotBlank(dir) && dir.startsWith("/"))
        {
            this.sftp.cd("/");
        }
        else
        {
            this.sftp.cd(this.sftp.getHome());
        }
        
        String[] dirs = getHierDirNames(dir);
        for (String cdir : dirs)
        {
            //忽略空目录
            if (StringUtils.isBlank(cdir))
            {
                continue;
            }
            
            //逐层创建目录
            try
            {
                this.sftp.cd(cdir);
            }
            catch (Exception e)
            {
                LOGGER.debug("cd to dstDir[{}] failed! Now create {} ! Exception is {}", new Object[] {cdir, cdir, e});
                this.sftp.mkdir(cdir);
                this.sftp.cd(cdir);
            }
        }
        
    }
    
    private String[] getHierDirNames(String dir)
        throws SftpException
    {
        return dir.split("/");
    }
    
    public ChannelSftp getSftp()
    {
        return sftp;
    }
    
    public void setSftp(ChannelSftp sftp)
    {
        this.sftp = sftp;
    }
    
    public ChannelExec getChannelExec()
    {
        return channelExec;
    }
    
    public void setChannelExec(ChannelExec channelExec)
    {
        this.channelExec = channelExec;
    }
    
    public Session getSshSession()
    {
        return sshSession;
    }
    
    public void setSshSession(Session sshSession)
    {
        this.sshSession = sshSession;
    }
    
    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     */
    public File download(String directory, String downloadFile, String saveFile)
    {
        try
        {
            initRemoteSFTPService();
            LOGGER.debug("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            LOGGER.debug("Connected to " + host + ".");
            this.sftp.cd(this.basePath);
            if (null != directory)
            {
                this.sftp.cd(directory);
            }
            File file = new File(saveFile);
            FileOutputStream fos = new FileOutputStream(file);
            this.sftp.get(downloadFile, fos);
            channel.disconnect();
            fos.close();
            return file;
        }
        catch (Exception e)
        {
            LOGGER.error("download file failed! Exception is {}", new Object[] {e});
            return null;
        }
        finally
        {
            disconnectSFTPService();
        }
        
    }
    
    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile)
    {
        try
        {
            LOGGER.debug("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            LOGGER.debug("Connected to " + host + ".");
            this.sftp.cd(directory);
            this.sftp.rm(deleteFile);
            channel.disconnect();
        }
        catch (Exception e)
        {
            LOGGER.error("delete file failed! Exception is {}", new Object[] {e});
        }
    }
    
    /**
     * 远程执行linux命令
     * @param copyFromPath cp的源地址
     * @param copytoPath cp的目的地址
     * @return 是否操作成功
     */
    public boolean runLinuxRemote(String copyFromPath, String copytoPath)
    {
        try
        {
            LOGGER.debug("Opening Channel.");
            //首先需要创建目录
            String command = "mkdir -p " + basePath + "/" + copytoPath.substring(0, copytoPath.lastIndexOf("/"));
            Channel channel = sshSession.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.connect();
            channel.disconnect();
            //在实现cp，不能批量执行
            channel = sshSession.openChannel("exec");
            command = "cp " + basePath + "/" + copyFromPath + " " + basePath + "/" + copytoPath;
            ((ChannelExec)channel).setCommand(command);
            channel.connect();
            channel.disconnect();
            return true;
            //sshSession.disconnect();
        }
        catch (Exception e)
        {
            LOGGER.error("connect to sftp failed! Exception is {}", new Object[] {e});
            return false;
        }
    }
}
