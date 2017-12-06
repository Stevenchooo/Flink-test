/*
 * 文 件 名:  Sync2Test.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  同步到测试
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-6
 */
package com.huawei.platform.tcc.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.DBServerConfigEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.PathMappingEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskAlarmChannelInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmItemsEntity;
import com.huawei.platform.tcc.entity.TaskAlarmThresholdEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.message.req.LoadTasksReq;
import com.huawei.platform.tcc.message.rsp.LoadTasksRsp;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.huawei.platform.tcc.utils.crypt.CryptUtil;

/**
 * 同步到测试
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-6]
 */
public class Sync2Test extends SyncTcc
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Sync2Test.class);
    
    /**
     * 同步TCC任务配置和运行状态
     * @param tccName tcc名字
     * @param userName 用户名
     * @param password 密码
     * @throws Exception 异常
     */
    public void syncTcc(String tccName, String userName, String password)
        throws Exception
    {
        // 记录调度开始时间
        Long startTime = new Date().getTime();
        
        //参数不合法，认证失败
        if (StringUtils.isBlank(tccName) || StringUtils.isBlank(userName) || StringUtils.isBlank(password))
        {
            LOGGER.error("any argument can't be blank! tccName is {}, userName is {}. password id ***",
                TccUtil.truncatEncode(tccName),
                TccUtil.truncatEncode(userName));
            
            throw new CException(ResultCode.PARAMETER_INVALID);
        }
        
        TccDao dstDao;
        Long id;
        try
        {
            DBServerConfigEntity dbServerConfig = getTccDao().getDBServerConfig(tccName);
            if (null == dbServerConfig)
            {
                LOGGER.error("dbServerConfig[{}] don't exist!", TccUtil.truncatEncode(tccName));
                throw new CException(ResultCode.SYSTEM_ERROR);
            }
            
            dstDao = getDaoFactory().createDao(dbServerConfig);
            
            //认证权限
            authenticate(dstDao, userName, password);
            
            //读取路径映射
            List<PathMappingEntity> pathMappings = getTccDao().getPathMappings(tccName);
            
            List<Long> taskIds = new ArrayList<Long>();
            //读取所有的任务
            List<TaskEntity> tasks = getTaskManage().getAllTasksClone();
            if (null != tasks && !tasks.isEmpty())
            {
                //获取任务Id集合
                for (TaskEntity taskE : tasks)
                {
                    taskIds.add(taskE.getTaskId());
                }
            }
            
            //获取目标存在的TCC任务
            List<Long> existTaskIds = dstDao.getExistTaskIds(taskIds);
            
            //获取存在的任务Id
            id = prepareLoadTasks(dbServerConfig.getTccUrl(), userName, password, existTaskIds);
            
            //同步任务
            syncTasks(tccName, dstDao, pathMappings, tasks);
            
            //同步任务告警阈值
            syncAlarmThreshold(dstDao, taskIds);
            
            //同步任务告警项
            syncAlarmItems(dstDao, taskIds);
            
            //同步任务告警阈值
            syncAlarmChannels(dstDao, taskIds);
            
            //同步步骤
            syncTaskSteps(pathMappings, dstDao, taskIds);
            
            //同步周期
            syncTaskRunningStates(tccName, dstDao, taskIds);
            
            //同步所有的业务定义
            syncServices(dstDao);
            
            //通知测试TCC重新加载TCC任务
            notifyLoadTasks(dbServerConfig.getTccUrl(), userName, password, id);
            
            LOGGER.info("syncTcc sucessed!");
        }
        catch (Exception e)
        {
            LOGGER.error("syncTcc failed!", e);
            throw e;
        }
        finally
        {
            // 记录结束时间
            Long endTime = new Date().getTime();
            Long duration = endTime - startTime;
            LOGGER.info("syncTcc executes {} ms", duration);
        }
    }
    
    private void syncAlarmThreshold(TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        if (null == taskIds || taskIds.isEmpty())
        {
            return;
        }
        
        //删除告警阈值
        dstDao.deleteAlarmThresholds(taskIds);
        
        //读取所有的告警阈值
        List<TaskAlarmThresholdEntity> alarmThresholds = getAlarmThreManage().getAlarmThresholds();
        
        //写到目标数据库
        dstDao.replaceAlarmThresholds(alarmThresholds);
    }
    
    private void syncAlarmItems(TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        if (null == taskIds || taskIds.isEmpty())
        {
            return;
        }
        //删除告警项
        dstDao.deleteAlarmItems(taskIds);
        
        //读取所有的告警项
        List<TaskAlarmItemsEntity> alarmItems = getTccDao().getAlarmItems();
        
        //写到目标数据库
        dstDao.replaceAlarmItems(alarmItems);
    }
    
    private void syncAlarmChannels(TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        if (null == taskIds || taskIds.isEmpty())
        {
            return;
        }
        
        //删除告警渠道
        dstDao.deleteAlarmChannels(taskIds);
        
        //读取所有的告警渠道
        List<TaskAlarmChannelInfoEntity> alarmChannels = getTccDao().getAlarmChannels();
        
        //写到目标数据库
        dstDao.replaceAlarmChannels(alarmChannels);
    }
    
    private void syncServices(TccDao dstDao)
        throws Exception
    {
        //读取所有的业务定义信息
        List<ServiceDefinationEntity> services = getTccDao().getServices();
        
        //写到目标数据库
        dstDao.replaceServices(services);
    }
    
    private void notifyLoadTasks(String tccWebAppsAddr, String userName, String password, Long id)
        throws CException
    {
        //构造请求体
        LoadTasksReq req = new LoadTasksReq();
        req.setLoadAll(true);
        req.setUserName(userName);
        req.setPassword(password);
        req.setId(id);
        
        String url = String.format("%s/loadTasks", tccWebAppsAddr);
        LoadTasksRsp rsp = TccUtil.callLoadTasks(url, req);
        
        if (ResultCode.SUCCESS != rsp.getResultCode())
        {
            //执行不成功，则直接抛出异常
            throw new CException(rsp.getResultCode());
        }
    }
    
    private Long prepareLoadTasks(String tccWebAppsAddr, String userName, String password, List<Long> taskIds)
        throws CException
    {
        //构造请求体
        LoadTasksReq req = new LoadTasksReq();
        req.setPrepare(true);
        req.setLoadAll(true);
        req.setUserName(userName);
        req.setPassword(password);
        req.setTaskIds(taskIds);
        
        String url = String.format("%s/prepareLoadTasks", tccWebAppsAddr);
        LoadTasksRsp rsp = TccUtil.callLoadTasks(url, req);
        
        if (ResultCode.SUCCESS != rsp.getResultCode())
        {
            //执行不成功，则直接抛出异常
            throw new CException(rsp.getResultCode());
        }
        
        return rsp.getId();
    }
    
    private void syncTaskRunningStates(String tccName, TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        if (null == taskIds || taskIds.isEmpty())
        {
            return;
        }
        
        //删除不存在的任务运行状态
        dstDao.deleteTaskRSs(taskIds);
        
        List<TaskRunningStateEntity> taskRSs;
        //分批写入目标数据库
        int i = 0;
        int j;
        while (i < taskIds.size())
        {
            j = i + TASK_BATCH_NUM;
            if (j > taskIds.size())
            {
                j = taskIds.size();
            }
            
            //读取任务周期
            taskRSs = getTccDao().getAllTaskRSs(taskIds.subList(i, j));
            
            //将start状态修改为Init状体
            changeStart2Init(taskRSs);
            //写入目标
            dstDao.replaceTaskRSs(taskRSs);
            
            LOGGER.info("i[{}],j[{}]", i, j);
            
            i = j;
        }
    }
    
    private void changeStart2Init(List<TaskRunningStateEntity> taskRSs)
    {
        if (null == taskRSs || taskRSs.isEmpty())
        {
            return;
        }
        
        for (TaskRunningStateEntity taskRSE : taskRSs)
        {
            //将start状态修改为Init状体
            if (null != taskRSE.getState() && taskRSE.getState().equals(RunningState.START))
            {
                taskRSE.setState(RunningState.INIT);
            }
        }
    }
    
    private void syncTasks(String tccName, TccDao dstDao, List<PathMappingEntity> pathMappings, List<TaskEntity> tasks)
        throws Exception
    {
        //停止所有任务
        stop(tasks);
        //替换任务中的OS用户
        replaceOsUser(tccName, tasks);
        //替换任务中的OS用户组
        replaceOsGroup(tccName, tasks);
        //替换任务中的节点ID
        replaceNodeIds(tccName, tasks);
        //替换任务中的路径
        replaceFilePath(pathMappings, tasks);
        //将任务名重名的任务进行改名
        replaceDupNameTasks(dstDao, tasks);
        
        //分批写入目标数据库
        int i = 0;
        int j;
        while (i < tasks.size())
        {
            j = i + BATCH_NUM;
            if (j > tasks.size())
            {
                j = tasks.size();
            }
            
            dstDao.replaceTasks(tasks.subList(i, j));
            
            i = j;
        }
    }
    
    private void syncTaskSteps(List<PathMappingEntity> pathMappings, TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        if (null == taskIds || taskIds.isEmpty())
        {
            return;
        }
        
        //删除任务步骤
        dstDao.deleteSteps(taskIds);
        
        //读取所有的任务
        List<TaskStepEntity> steps = getTccDao().getAllTaskSteps();
        if (null == steps || steps.isEmpty())
        {
            return;
        }
        
        //停止不需要启动的任务步骤
        stopSomeSteps(steps);
        
        //替换任务步骤中的路径
        replaceCommandPath(pathMappings, steps);
        
        //分批写入目标数据库
        int i = 0;
        int j;
        while (i < steps.size())
        {
            j = i + BATCH_NUM;
            if (j > steps.size())
            {
                j = steps.size();
            }
            
            dstDao.replaceSteps(steps.subList(i, j));
            
            i = j;
        }
    }
    
    private void stopSomeSteps(List<TaskStepEntity> steps)
        throws Exception
    {
        if (null == steps || steps.isEmpty())
        {
            return;
        }
        
        //获取需要停止步骤的任务Id集合
        List<Long> sstepsTaskIds = getTccDao().getStepsStoppedTaskIds();
        if (null == sstepsTaskIds || sstepsTaskIds.isEmpty())
        {
            return;
        }
        
        //构造hash集，加快查询速度
        Set<Long> sstepsIds = new HashSet<Long>();
        for (Long taskId : sstepsTaskIds)
        {
            sstepsIds.add(taskId);
        }
        
        for (TaskStepEntity step : steps)
        {
            //停用步骤
            if (sstepsIds.contains(step.getTaskId()))
            {
                step.setStepEnableFlag(false);
            }
        }
    }
    
    /**
     * 是否认证通过
     * @param tccName tcc名字
     * @param userName 用户名
     * @param password 密码
     * @throws Exception 异常
     */
    private void authenticate(TccDao dstDao, String userName, String password)
        throws Exception
    {
        //参数不合法，认证失败
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password))
        {
            LOGGER.error("both userName[{}] and password can't be blank!", TccUtil.truncatEncode(userName));
            throw new CException(ResultCode.PARAMETER_INVALID);
        }
        
        UsernameAndPasswordParam param = new UsernameAndPasswordParam();
        param.setUserName(userName);
        param.setPassword(CryptUtil.getMD5HexString(password));
        OperatorInfoEntity operatorInfo = dstDao.getOperatorInfo(param);
        if (null == operatorInfo)
        {
            LOGGER.error("userName[{}] and password are not correct!", TccUtil.truncatEncode(userName));
            throw new CException(ResultCode.AUTH_FAILED, userName);
        }
        
        //密码验证通过
        OperatorMgnt operatorMgnt = new OperatorMgnt();
        operatorMgnt.setTccDao(dstDao);
        
        //必需是系统管理员账号
        Operator operator = operatorMgnt.createOperator(userName);
        if (null == operator || !operator.isSystemAdmin())
        {
            LOGGER.error("user[{}] must be system administrator!", TccUtil.truncatEncode(userName));
            throw new CException(ResultCode.NO_ENOUGH_PREVILEGE, userName);
        }
    }
}
