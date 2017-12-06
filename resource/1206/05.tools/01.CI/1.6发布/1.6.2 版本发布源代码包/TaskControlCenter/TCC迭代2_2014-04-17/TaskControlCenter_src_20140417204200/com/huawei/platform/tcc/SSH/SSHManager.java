/*
 * 文 件 名:  SSHManager.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  SSH管理器
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-14
 */
package com.huawei.platform.tcc.SSH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.exception.ArgumentException;

/**
 * SSH管理器
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-11-29]
 */
public class SSHManager
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SSHManager.class);
    
    private Map<String, List<SSHConnect>> busyConnects = new HashMap<String, List<SSHConnect>>();
    
    /**
     * 是否是实时任务通道
     */
    private boolean rtSSH;
    
    /**
     * 默认构造函数
     * @param rtSSH 是否实时任务
     */
    public SSHManager(boolean rtSSH)
    {
        this.rtSSH = rtSSH;
    }
    
    /**
     * 默认构造函数
     */
    public SSHManager()
    {
    }
    
    public boolean isRtSSH()
    {
        return rtSSH;
    }
    
    public void setRtSSH(boolean rtSSH)
    {
        this.rtSSH = rtSSH;
    }
    
    /**
     * 获取空闲的连接
     * @param conInfo 连接信息
     * @param id 对象标识
     * @return 空闲的连接
     */
    public SSHConnect getIdleConnect(ConnectInfo conInfo, Object id)
    {
        //参数不合法
        if (null == id || null == conInfo || null == conInfo.getHost() || null == conInfo.getPemKey()
            || conInfo.getPort() <= 0)
        {
            LOGGER.error("parameter error! conInfo is {},id is {}", conInfo, id);
            return null;
        }
        
        //同一个主机
        String identify = conInfo.getIdentify();
        List<SSHConnect> connects;
        SSHConnect sshConnect = null;
        synchronized (busyConnects)
        {
            connects = busyConnects.get(identify);
            
            //不存在
            if (null == connects)
            {
                connects = new ArrayList<SSHConnect>();
                busyConnects.put(identify, connects);
            }
            
            //if (connects.size() + 1 <= maxConnNum)
            int maxConnNum = rtSSH ? TccConfig.getMaxRTSSHConnectionNum() : TccConfig.getMaxSSHConnectionNum();
            if (connects.size() + 1 <= maxConnNum)
            {
                //允许分配
                try
                {
                    sshConnect = new SSHConnect(conInfo, id);
                }
                catch (ArgumentException e)
                {
                    LOGGER.error("conInfo is {}!", conInfo, e);
                    return null;
                }
                connects.add(sshConnect);
                LOGGER.info("allocate the connect[host={},port={},user={}] for id[{}]. remaining {} connects[rt={}]!",
                    new Object[] {conInfo.getHost(), conInfo.getPort(), conInfo.getUser(), id,
                        TccConfig.getMaxSSHConnectionNum() - connects.size(), rtSSH});
            }
        }
        
        return sshConnect;
    }
    
    /**
     * 释放连接
     * @param connect 连接
     * @param wait 是否等一小会
     */
    public void releaseConnect(SSHConnect connect, boolean wait)
    {
        if (null != connect && null != connect.getConInfo())
        {
            ConnectInfo connInfo = connect.getConInfo();
            //获取标识
            String identify = connInfo.getIdentify();
            List<SSHConnect> connects;
            synchronized (busyConnects)
            {
                connects = busyConnects.get(identify);
                if (null != connects)
                {
                    connects.remove(connect);
                }
            }
            
            //关闭连接
            if (wait)
            {
                connect.closeAMoment();
            }
            else
            {
                connect.close();
            }
            
            if (null != connects)
            {
                LOGGER.info("release the connect[host={},port={},user={}] for id[{}].remaining {} connects[rt={}]!",
                    new Object[] {connInfo.getHost(), connInfo.getPort(), connInfo.getUser(), connect.getId(),
                        TccConfig.getMaxSSHConnectionNum() - connects.size(), rtSSH});
            }
        }
    }
}
