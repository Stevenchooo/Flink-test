/*
 * 文 件 名:  SSHConnect.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  SSH连接
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-29
 */
package com.huawei.platform.tcc.SSH;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.detector.SSHConnectDetector;
import com.huawei.platform.tcc.exception.ArgumentException;
import com.huawei.platform.tcc.exception.ConnException;
import com.huawei.platform.tcc.utils.crypt.CryptUtil;
import com.mindbright.nio.NetworkConnection;
import com.mindbright.ssh2.SSH2AuthModule;
import com.mindbright.ssh2.SSH2AuthPublicKey;
import com.mindbright.ssh2.SSH2Authenticator;
import com.mindbright.ssh2.SSH2Connection;
import com.mindbright.ssh2.SSH2Exception;
import com.mindbright.ssh2.SSH2KeyPairFile;
import com.mindbright.ssh2.SSH2Preferences;
import com.mindbright.ssh2.SSH2Signature;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.util.RandomSeed;
import com.mindbright.util.SecureRandomAndPad;

/**
 * SSH连接
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class SSHConnect
{
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(SSHConnect.class);
    
    //5秒的连接超时
    private static final int CONNECT_TIME_OUT = 5 * 1000;
    
    //30秒内无心跳就自动断开
    private static final int INTERVAL = 10;
    
    private final Object lock = new Object();
    
    /**
     * 连接信息集合，最最前面的能连接成功的连接
     */
    private ConnectInfo conInfo;
    
    /**
     * 对象标识
     */
    private Object id;
    
    /**
     * 连接实例
     */
    private SSH2Connection ssh2Conn;
    
    /**
     * 简单客户端
     */
    private SSH2SimpleClient simpleClient;
    
    /**
     * 是否已经关闭
     */
    private boolean closed = false;
    
    /**
     * 断开连接之前是否等待
     */
    private boolean wait = false;
    
    /**
     * 等待无效
     */
    private boolean waitInValid = false;
    
    /**
     * 等最长时间
     */
    private long waitMaxTime = 0;
    
    /**
     * 默认构造函数
     * @param conInfo 连接信息
     * @param id 对象标识
     * @throws ArgumentException 异常
     */
    public SSHConnect(ConnectInfo conInfo, Object id)
        throws ArgumentException
    {
        this.conInfo = new ConnectInfo(conInfo);
        this.id = id;
    }
    
    public SSH2Connection getSsh2Conn()
    {
        return ssh2Conn;
    }
    
    /**
     * 连接指定节点
     * @param loginInfo
     * @throws ConnException 连接异常
     */
    public void connect()
        throws ConnException
    {
        if (null == ssh2Conn)
        {
            try
            {
                //增加计数
                SSHConnectDetector.getInstance().start2Connect();
                SSH2Preferences prefs = new SSH2Preferences();
                prefs.setPreference(SSH2Preferences.LOG_LEVEL, TccConfig.getSshLogLevel());
                prefs.setPreference(SSH2Preferences.LOG_FILE, TccConfig.getSshLogFile());
                
                NetworkConnection socket = NetworkConnection.open(conInfo.getHost(), conInfo.getPort());
                SSH2Transport transport = new SSH2Transport(socket, prefs, createSecureRandom());
                SSH2KeyPairFile kpf = new SSH2KeyPairFile();
                //解密
                String pemKey = CryptUtil.decryptForAESStr(conInfo.getPemKey(), "RJz$asd9*zmkjsTH");
                //多加一个回车符作为结束符
                pemKey += "\n";
                InputStream inputStream = new ByteArrayInputStream(pemKey.getBytes());
                kpf.load(inputStream, null);
                SSH2Signature sign = SSH2Signature.getInstance(kpf.getAlgorithmName());
                sign.initSign(kpf.getKeyPair().getPrivate());
                sign.setPublicKey(kpf.getKeyPair().getPublic());
                SSH2AuthModule am = new SSH2AuthPublicKey(sign);
                SSH2Authenticator authen = new SSH2Authenticator(conInfo.getUser());
                authen.addModule(am);
                simpleClient = new SSH2SimpleClient(transport, authen, CONNECT_TIME_OUT);
                
                //开启心跳保持
                transport.enableKeepAlive(INTERVAL);
                ssh2Conn = simpleClient.getConnection();
            }
            catch (IOException e)
            {
                LOGGER.error("connect failed! id is {}.", id, e);
                throw new ConnException("connect failed! id is " + id, e);
            }
            catch (SSH2Exception e)
            {
                LOGGER.error("connect failed! id is {}.", id, e);
                throw new ConnException("connect failed! id is " + id, e);
            }
            finally
            {
                //减少计数
                SSHConnectDetector.getInstance().finish2Connect();
            }
        }
    }
    
    /**
     * 创建命令
     * @param cmdType 类型
     * @param command 命令
     * @return 创建命令 
     * @throws Exception 异常
     */
    public SSHCommand createCommand(int cmdType, String command)
        throws Exception
    {
        //连接不正常
        if (null == ssh2Conn)
        {
            LOGGER.error("connect must be first! id=[{}], cmd =[{}].", new Object[] {id, command});
            throw new Exception(String.format("connect must be first! id=[%s],cmd=[%s]", id.toString(), command));
        }
        
        if (CommandType.HIVE_CMD == cmdType)
        {
            return new RHiveCommand(id, this, command);
        }
        else
        {
            return new RLsCommand(id, this, command);
        }
    }
    
    /**
     * 获取连接信息
     * @return 连接信息
     */
    public ConnectInfo getConInfo()
    {
        return conInfo;
    }
    
    /**
     * 是否已经关闭
     * @return 是否已经关闭
     */
    public boolean isClosed()
    {
        return closed;
    }
    
    private void closeConn()
    {
        try
        {
            //关闭连接
            if (null != simpleClient)
            {
                SSH2Transport transport = simpleClient.getTransport();
                if (null != transport)
                {
                    transport.normalDisconnect("User disconnects");
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("closeConn failed!id=[{}]", id, e);
        }
    }
    
    /**
     * 等最长多次时间
     * @param time 时间
     * @return 是否设置成功
     */
    public boolean waitAMoment(long time)
    {
        synchronized (lock)
        {
            if (this.waitInValid)
            {
                return false;
            }
            
            if (time > 0)
            {
                this.wait = true;
                this.waitMaxTime = time;
                
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    /**
     * 关闭连接前先等一会儿
     */
    public void closeAMoment()
    {
        boolean waitA = false;
        synchronized (lock)
        {
            if (!this.wait)
            {
                this.waitInValid = true;
            }
            
            waitA = this.wait;
        }
        
        if (!waitInValid && waitA)
        {
            try
            {
                synchronized (this)
                {
                    this.wait(waitMaxTime);
                }
            }
            catch (InterruptedException e)
            {
                LOGGER.info("", e);
            }
        }
        
        close();
    }
    
    /**
     * 关闭连接
     */
    public void close()
    {
        synchronized (lock)
        {
            //关闭连接
            if (!closed)
            {
                closeConn();
            }
            closed = true;
        }
        
        //唤醒其它对象
        synchronized (this)
        {
            this.notifyAll();
        }
    }
    
    public Object getId()
    {
        return this.id;
    }
    
    /**
     * Create a random number generator. This implementation uses the
     * system random device if available to generate good random
     * numbers. Otherwise it falls back to some low-entropy garbage.
     */
    private static SecureRandomAndPad createSecureRandom()
    {
        final int numBytes = 20;
        
        byte[] seed;
        File devRandom = new File("/dev/urandom");
        if (devRandom.exists())
        {
            RandomSeed rs = new RandomSeed("/dev/urandom", "/dev/urandom");
            seed = rs.getBytesBlocking(numBytes);
        }
        else
        {
            seed = RandomSeed.getSystemStateHash();
        }
        return new SecureRandomAndPad(new SecureRandom(seed));
    }
}
