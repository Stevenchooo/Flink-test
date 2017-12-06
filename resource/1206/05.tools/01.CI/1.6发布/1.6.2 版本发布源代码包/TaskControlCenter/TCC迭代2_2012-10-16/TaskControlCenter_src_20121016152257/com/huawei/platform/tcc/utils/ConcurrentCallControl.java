/*
 * 文 件 名:  ConcurrentCallControl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-1-11
 */
package com.huawei.platform.tcc.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.interfaces.Tcc2Shell;
import com.huawei.platform.tcc.utils.remoteshell.Constants;
import com.huawei.platform.tcc.utils.remoteshell.ProcessShell;

/**
 * 并发调用控制
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-1-11]
 * @see  [相关类/方法]
 */
public class ConcurrentCallControl extends ProcessShell
{
    /**
     * 命令类型 shell
     */
    private static final String SHELL_TYPE = "shell";
    
    /**
     * 命令类型 shell
     */
    private static final String OTHER_TYPE = "other";
    
    /**
     * 记录日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentCallControl.class);
    
    /**
     * 唯一的id
     */
    private static Long id = 0L;
    
    /**
     * 不同资源类型的阻塞队列map
     */
    private static ConcurrentHashMap<String, BlockingQueue<Long>> multiTypeResourceMap =
        new ConcurrentHashMap<String, BlockingQueue<Long>>();
    
    /**
     * 不同资源的线程Map
     */
    private static ConcurrentHashMap<String, ConcurrentHashMap<Long, Thread>> multiTypeThreadMap =
        new ConcurrentHashMap<String, ConcurrentHashMap<Long, Thread>>();
    
    private static ConcurrentHashMap<String, Object> resTypeLocks = new ConcurrentHashMap<String, Object>();
    
    /**
     * 申请的资源id
     */
    private Long applyResourseId = 0L;
    
    /**
     * 资源类型
     */
    private String resType = "default";
    
    /**
     * 已获取资源标识
     */
    private boolean bgotResourse;
    
    static
    {
        LOGGER.info("init ConcurrentCallControl resourceType start...");
        
        String resourctType;
        BlockingQueue<Long> remoteShellCallQueue;
        ConcurrentHashMap<Long, Thread> remoteShellThreadHashMap;
        
        Map<String, Map<String, String>> ipMaps = TccUtil.readRemoteShellSysInfo();
        for (String ipAddr : ipMaps.keySet())
        {
            //长执行时间命令资源
            resourctType = getResourceType(ipAddr, SHELL_TYPE);
            resTypeLocks.put(resourctType, resourctType);
            LOGGER.info("add an new resourctType[{}].", resourctType);
            
            remoteShellCallQueue = new ArrayBlockingQueue<Long>(TccConfig.getLongTimeCallNum());
            multiTypeResourceMap.put(resourctType, remoteShellCallQueue);
            
            remoteShellThreadHashMap = new ConcurrentHashMap<Long, Thread>();
            multiTypeThreadMap.put(resourctType, remoteShellThreadHashMap);
            
            //短执行时间命令资源
            resourctType = getResourceType(ipAddr, OTHER_TYPE);
            resTypeLocks.put(resourctType, resourctType);
            LOGGER.info("add an new resourctType[{}].", resourctType);
            
            remoteShellCallQueue = new ArrayBlockingQueue<Long>(TccConfig.getShortTimeCallNum());
            multiTypeResourceMap.put(resourctType, remoteShellCallQueue);
            
            remoteShellThreadHashMap = new ConcurrentHashMap<Long, Thread>();
            multiTypeThreadMap.put(resourctType, remoteShellThreadHashMap);
        }
        
        LOGGER.info("init ConcurrentCallControl resourceType finished.");
    }
    
    /**
     * 构造函数
     * @param tcc2Shell 提供给shell的数据更新调用接口
     * 
     */
    public ConcurrentCallControl(Tcc2Shell tcc2Shell)
    {
        super(tcc2Shell);
    }
    
    /**
     * 停止执行当前真正执行的命令
     */
    @Override
    public void stopExec()
    {
        //从阻塞队列中唤醒，并释放掉相关资源
        wakeupFromBlock();
        LOGGER.debug("stopExec, start to call ProcessShell.stopExec().");
        super.stopExec();
    }
    
    /**
     * 在指定机器上执行命令
     * @param ip ip地址
     * @param type 类型
     * @param command 命令
     * @param taskID 任务Id
     * @param cycleID 周期Id
     * @param batchID 批次Id
     * @param stepID 步骤Id
     * @return 结果集合
     */
    @Override
    public Map<String, Object> exec(String ip, String type, String command, int taskID, String cycleID, int batchID,
        int stepID)
    {
        if (bgotResourse)
        {
            LOGGER.debug("start to call ProcessShell.exec().");
            return super.exec(ip, type, command, taskID, cycleID, batchID, stepID);
        }
        else
        {
            LOGGER.error("please call applyResourse() first.");
            return null;
        }
    }
    
    /**
     * 在指定机器上执行命令
     * @param ip ip地址
     * @param userName 用户名
     * @param password 密码
     * @param type 类型
     * @param command 命令
     * @param taskID 任务Id
     * @param cycleID 周期Id
     * @param batchID 批次Id
     * @param stepID 步骤Id
     * @return 结果集合
     */
    @Override
    public Map<String, Object> exec(String ip, String userName, String password, String type, String command,
        int taskID, String cycleID, int batchID, int stepID)
    {
        if (bgotResourse)
        {
            LOGGER.debug("start to call ProcessShell.exec().");
            return super.exec(ip, userName, password, type, command, taskID, cycleID, batchID, stepID);
        }
        else
        {
            LOGGER.error("please call applyResourse() first.");
            return null;
        }
    }
    
    /**
     * 在指定机器上执行命令
     * @param ip ip地址
     * @param userName 用户名
     * @param password 密码
     * @param type 类型
     * @param command 命令
     * @param taskID 任务Id
     * @param cycleID 周期Id
     * @param batchID 批次Id
     * @param stepID 步骤Id
     * @return 结果集合
     */
    public Map<String, Object> blockExecSu(String ip, String userName, String password, String type, String command,
        int taskID, String cycleID, int batchID, int stepID)
    {
        String resourceType = getResourceType(ip, type);
        Map<String, Object> mapResult = null;
        try
        {
            //申请资源
            if (applyResourse(resourceType))
            {
                LOGGER.debug("enteger blockExec, start to call ProcessShell.exec().");
                mapResult = this.exec(ip, userName, password, type, command, taskID, cycleID, batchID, stepID);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("applyResourse error!", e);
            mapResult = new HashMap<String, Object>();
            //退出状态为出错
            mapResult.put(Constants.EXITSTATUES, 1);
        }
        finally
        {
            //释放资源
            releaseResource();
        }
        return mapResult;
    }
    
    /**
     * 在指定机器上执行命令
     * @param ip ip地址
     * @param type 类型
     * @param command 命令
     * @param taskID 任务Id
     * @param cycleID 周期Id
     * @param batchID 批次Id
     * @param stepID 步骤Id
     * @return 结果集合
     */
    public Map<String, Object> blockExec(String ip, String type, String command, int taskID, String cycleID,
        int batchID, int stepID)
    {
        String resourceType = getResourceType(ip, type);
        Map<String, Object> mapResult = null;
        try
        {
            //申请资源
            if (applyResourse(resourceType))
            {
                LOGGER.debug("enteger blockExec, start to call ProcessShell.exec().");
                mapResult = this.exec(ip, type, command, taskID, cycleID, batchID, stepID);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("applyResourse error!", e);
            mapResult = new HashMap<String, Object>();
            //退出状态为出错
            mapResult.put(Constants.EXITSTATUES, 1);
        }
        finally
        {
            //释放资源
            releaseResource();
        }
        return mapResult;
    }
    
    /**
     * 获取资源类型
     * @param ip ip地址
     * @param type 命令类型
     * @return 资源类型
     */
    public static String getResourceType(String ip, String type)
    {
        if (SHELL_TYPE.equalsIgnoreCase(type))
        {
            return String.format("%s:%s", ip, SHELL_TYPE);
        }
        else
        {
            return String.format("%s:%s", ip, OTHER_TYPE);
        }
    }
    
    /**
     * 申请资源
     * 如果资源不足，则阻塞等待直到有可用的资源
     * @param ip ip地址
     * @param type 命令类型
     * @return 是否成功申请到资源
     * @throws Exception 参数检查异常
     */
    public boolean applyResourse(String ip, String type)
        throws Exception
    {
        return applyResourse(getResourceType(ip, type));
    }
    
    /**
     * 申请资源
     * 如果资源不足，则阻塞等待直到有可用的资源
     * @param resourceType 资源类型
     * @return 是否成功申请到资源
     * @throws Exception 参数检查异常
     */
    public boolean applyResourse(String resourceType)
        throws Exception
    {
        LOGGER.debug("enter applyResourse, applyResourseId is [{}], resType is [{}].", applyResourseId, resourceType);
        if (StringUtils.isEmpty(resourceType) || null == applyResourseId)
        {
            LOGGER.error("resourceType[{}] and applyResourseId[{}] cann't be not null.", resourceType, applyResourseId);
            throw new ArgumentException(String.format("resourceType[%s] and applyResourseId[%d] cann't be not null.",
                resourceType,
                applyResourseId));
        }
        
        if (!resTypeLocks.containsKey(resourceType))
        {
            LOGGER.error("resourceType[{}] does not exist. please config ipAddr in systemconf.xml!", resourceType);
            throw new ArgumentException(String.format("resourceType[%s] does not exist.", resourceType));
        }
        
        //如果已经获得锁了，立即返回
        if (bgotResourse)
        {
            return true;
        }
        
        //获取资源id,保证applyResourseId全局唯一
        synchronized (ConcurrentCallControl.class)
        {
            id++;
            applyResourseId = id;
            resType = resourceType;
        }
        
        //获取指定资源类型的线程队列
        ConcurrentHashMap<Long, Thread> threadHashMap = multiTypeThreadMap.get(resType);
        
        //不可能发生，修改findbugs所致
        if (null == threadHashMap)
        {
            LOGGER.error(String.format("resourceType[%s] does not exist.", resType));
            throw new ArgumentException(String.format("resourceType[%s] does not exist.", resType));
        }
        
        //获取资源类型对应的锁对象
        Object lockObj = resTypeLocks.get(resType);
        //不可能发生，修改findbugs所致
        if (null == lockObj)
        {
            LOGGER.error(String.format("resourceType[%s] does not exist.", resType));
            throw new ArgumentException(String.format("resourceType[%s] does not exist.", resType));
        }
        
        //将当前的执行线程添加到不同资源的线程Map
        synchronized (lockObj)
        {
            if (!threadHashMap.contains(applyResourseId))
            {
                threadHashMap.put(applyResourseId, Thread.currentThread());
            }
        }
        
        //获取指定资源类型的资源队列
        BlockingQueue<Long> resourceQueue = multiTypeResourceMap.get(resType);
        
        //不可能发生，修改findbugs所致
        if (null == resourceQueue)
        {
            LOGGER.error(String.format("resourceType[%s] does not exist.", resType));
            throw new ArgumentException(String.format("resourceType[%s] does not exist.", resType));
        }
        
        if (!resourceQueue.contains(applyResourseId))
        {
            try
            {
                //资源不够时，会在此处阻塞
                resourceQueue.put(applyResourseId);
                bgotResourse = true;
            }
            catch (InterruptedException e)
            {
                LOGGER.warn("applyResourse(resourceType=[{}],applyResourseId=[{}]) get an InterruptedException!",
                    new Object[] {resType, applyResourseId},
                    e);
            }
        }
        
        LOGGER.debug("exit applyResourse, applyResourseId is [{}], resType is [{}].", applyResourseId, resType);
        return bgotResourse;
    }
    
    /**
     * 从阻塞等待中退出，
     */
    public void wakeupFromBlock()
    {
        LOGGER.debug("enter cancelExec, applyResourseId is [{}], resType is [{}].", applyResourseId, resType);
        
        //获取指定资源类型的线程队列
        ConcurrentHashMap<Long, Thread> threadHashMap = multiTypeThreadMap.get(resType);
        
        if (null != threadHashMap)
        {
            Thread thd = threadHashMap.get(applyResourseId);
            
            //如果没有调用过中断
            if (null != thd && !thd.isInterrupted())
            {
                thd.interrupt();
            }
            
            if (bgotResourse)
            {
                //释放资源
                releaseResource();
            }
        }
        LOGGER.debug("exit cancelExec, applyResourseId is [{}], resType is [{}].", applyResourseId, resType);
    }
    
    /**
     * 释放资源
     */
    public void releaseResource()
    {
        LOGGER.debug("enter releaseResource, applyResourseId is [{}], resType is [{}].", applyResourseId, resType);
        if (bgotResourse)
        {
            //获取指定资源类型的资源队列
            BlockingQueue<Long> resourceQueue = multiTypeResourceMap.get(resType);
            //不可能发生，修改findbugs所致
            if (null == resourceQueue)
            {
                LOGGER.error(String.format("resourceType[%s] does not exist.", resType));
                return;
            }
            
            //获取指定资源类型的线程队列
            ConcurrentHashMap<Long, Thread> threadHashMap = multiTypeThreadMap.get(resType);
            
            //不可能发生，修改findbugs所致
            if (null == threadHashMap)
            {
                LOGGER.error(String.format("resourceType[%s] does not exist.", resType));
                return;
            }
            
            //允许重复申请资源
            bgotResourse = false;
            resourceQueue.remove(applyResourseId);
            LOGGER.debug("remove applyResourseId[{}] of resType[{}] sucessed.", applyResourseId, resType);
            
            if (threadHashMap.containsKey(applyResourseId))
            {
                threadHashMap.remove(applyResourseId);
                
                LOGGER.debug("remove thread[applyResourseId=[{}]," + "resType=[{}]] sucessed.",
                    applyResourseId,
                    resType);
            }
            
        }
        LOGGER.debug("exit releaseResource, applyResourseId is [{}], resType is [{}].", applyResourseId, resType);
    }
}

/**
 * 
 * 参数异常类
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-2-8]
 * @see  [相关类/方法]
 */
class ArgumentException extends Exception
{
    
    /**
     * 序列化
     */
    private static final long serialVersionUID = -2249822525877134905L;
    
    public ArgumentException(String message)
    {
        super(message);
    }
}
