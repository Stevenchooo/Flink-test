/*
 * 文 件 名:  ConnInfoManager.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  连接信息管理类
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-29
 */
package com.huawei.platform.tcc.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.SSH.ConnectInfo;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.entity.NodeInfoEntity;
import com.huawei.platform.tcc.entity.OSUserInfoEntity;
import com.huawei.platform.tcc.event.Event;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.exception.ArgumentException;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 连接信息管理类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-29]
 */
public class ConnInfoManager implements Listener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnInfoManager.class);
    
    private static Map<Integer, NodeInfoEntity> nodeMaps = new HashMap<Integer, NodeInfoEntity>();
    
    private static Map<String, OSUserInfoEntity> osUserMaps = new HashMap<String, OSUserInfoEntity>();
    
    private TccDao tccDao;
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 初始化
     */
    public void init()
    {
        try
        {
            //清理数据
            synchronized (nodeMaps)
            {
                nodeMaps.clear();
            }
            
            synchronized (osUserMaps)
            {
                nodeMaps.clear();
            }
            
            List<NodeInfoEntity> nodes = tccDao.getNodeInfoList();
            List<OSUserInfoEntity> osUsers = tccDao.getOSUsers();
            //缓存节点
            for (NodeInfoEntity node : nodes)
            {
                if (null != node && null != node.getNodeId())
                {
                    synchronized (nodeMaps)
                    {
                        nodeMaps.put(node.getNodeId(), node);
                    }
                }
                else
                {
                    LOGGER.error("node[{}] is not correct!", node);
                }
            }
            
            //缓存os用户
            for (OSUserInfoEntity osUser : osUsers)
            {
                if (null != osUser && null != osUser.getOsUser())
                {
                    synchronized (osUserMaps)
                    {
                        osUserMaps.put(osUser.getOsUser(), osUser);
                    }
                }
                else
                {
                    LOGGER.error("osUser[{}] is not correct!", osUser);
                }
            }
            
            //注册监听的事件
            Eventor.register(EventType.ADD_OS_USER, this);
            Eventor.register(EventType.UPDATE_OS_USER, this);
            Eventor.register(EventType.DELETE_OS_USER, this);
            Eventor.register(EventType.ADD_NODE, this);
            Eventor.register(EventType.UPDATE_NODE, this);
            Eventor.register(EventType.DELETE_NODE, this);
        }
        catch (Exception e)
        {
            LOGGER.error("init failed!", e);
        }
    }
    
    /**
     * 获取节点名
     * @param nodeId 节点Id
     * @return 节点名
     */
    public String getNodeName(Integer nodeId)
    {
        if (null == nodeId)
        {
            return null;
        }
        
        NodeInfoEntity node = null;
        synchronized (nodeMaps)
        {
            node = nodeMaps.get(nodeId);
        }
        
        if (null != node)
        {
            return node.getName();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取连接信息
     * 
     * @param nodeId 节点Id
     * @param osUserName osUser名字
     * @return 连接信息
     * @throws ArgumentException 异常
     */
    public ConnectInfo getConnectInfo(Integer nodeId, String osUserName)
        throws ArgumentException
    {
        if (null == nodeId || null == osUserName)
        {
            throw new ArgumentException(String.format("node[%s] or osUser[%s] can't be null!", new Object[] {nodeId,
                osUserName}));
        }
        
        NodeInfoEntity node = null;
        synchronized (nodeMaps)
        {
            node = nodeMaps.get(nodeId);
        }
        
        OSUserInfoEntity osUser = null;
        synchronized (osUserMaps)
        {
            osUser = osUserMaps.get(osUserName);
        }
        
        if (null == node || null == osUser)
        {
            LOGGER.error("node[{}] or osUser[{}] can't be null!", new Object[] {node, osUser});
            throw new ArgumentException(String.format("node[%s] or osUser[%s] can't be null!", new Object[] {node,
                osUser}));
        }
        
        //构造连接信息
        ConnectInfo connInfo = new ConnectInfo();
        connInfo.setHost(node.getHost());
        connInfo.setPort(node.getPort());
        connInfo.setUser(osUser.getOsUser());
        connInfo.setPemKey(osUser.getPemKey());
        
        return connInfo;
    }
    
    @Override
    public void process(Event event)
    {
        //新增或者更新使用同样的处理逻辑
        if (EventType.ADD_NODE == event.getType() || EventType.UPDATE_NODE == event.getType())
        {
            
            try
            {
                if (event.getData() instanceof Integer)
                {
                    Integer nodeId = (Integer)event.getData();
                    NodeInfoEntity node;
                    node = tccDao.getNodeInfo(nodeId);
                    if (null != node && null != node.getNodeId())
                    {
                        synchronized (nodeMaps)
                        {
                            nodeMaps.put(node.getNodeId(), node);
                        }
                    }
                    else
                    {
                        LOGGER.error("node[{}] is not correct!", node);
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.error("data is {}", TccUtil.truncatEncode(event.getData().toString()), e);
            }
            
        }
        else if (EventType.DELETE_NODE == event.getType())
        {
            if (event.getData() instanceof Integer)
            {
                Integer nodeId = (Integer)event.getData();
                synchronized (nodeMaps)
                {
                    nodeMaps.remove(nodeId);
                }
            }
        }
        else if (EventType.ADD_OS_USER == event.getType() || EventType.UPDATE_OS_USER == event.getType())
        {
            if (event.getData() instanceof String)
            {
                String osUserName = (String)event.getData();
                //重新加载os用户
                OSUserInfoEntity osUser = tccDao.getOSUser(osUserName);
                if (null != osUser && null != osUser.getOsUser())
                {
                    synchronized (osUserMaps)
                    {
                        osUserMaps.put(osUser.getOsUser(), osUser);
                    }
                }
                else
                {
                    LOGGER.error("osUser[{}] is not correct!", osUser);
                }
            }
        }
        else if (EventType.DELETE_OS_USER == event.getType())
        {
            if (event.getData() instanceof String)
            {
                String osUserName = (String)event.getData();
                synchronized (osUserMaps)
                {
                    osUserMaps.remove(osUserName);
                }
            }
        }
    }
}
