/*
 * 文 件 名:  Sync2Produce.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  同步到生产
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-6
 */
package com.huawei.platform.tcc.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.DBServerConfigEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.PathMappingEntity;
import com.huawei.platform.tcc.entity.TaskAlarmChannelInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmItemsEntity;
import com.huawei.platform.tcc.entity.TaskAlarmThresholdEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.message.req.LoadTasksReq;
import com.huawei.platform.tcc.message.rsp.LoadTasksRsp;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.huawei.platform.tcc.utils.crypt.CryptUtil;

/**
 * 同步到生产
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-6]
 */
public class Sync2Produce extends SyncTcc
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Sync2Produce.class);
    
    /**
     * 同步TCC任务配置和运行状态
     * @param tccName tcc名字
     * @param userName 用户名
     * @param password 密码
     * @param taskIds 任务Id集合
     * @return 未同步的任务集合
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public List<Long> syncTcc(String tccName, String userName, String password, List<Long> taskIds)
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
            
            //同步有权限的任务，并获取不能同步的任务Id集合
            Object[] canorNotSyncTaskIds = syncTasks(tccName, userName, taskIds, dstDao, pathMappings);
            List<Long> canSyncTaskIds = (List<Long>)canorNotSyncTaskIds[0];
            List<Long> noSyncTaskIds = (List<Long>)canorNotSyncTaskIds[1];
            
            //同步任务告警阈值
            syncAlarmThreshold(dstDao, canSyncTaskIds);
            
            //同步任务告警项
            syncAlarmItems(dstDao, canSyncTaskIds);
            
            //同步任务告警阈值
            syncAlarmChannels(dstDao, canSyncTaskIds);
            
            //同步步骤
            syncTaskSteps(pathMappings, dstDao, canSyncTaskIds);
            
            //通知测试TCC重新加载TCC任务
            notifyLoadTasks(dbServerConfig.getTccUrl(), userName, password, canSyncTaskIds);
            
            LOGGER.info("syncTcc sucessed!");
            
            return noSyncTaskIds;
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
    
    private void syncTaskSteps(List<PathMappingEntity> pathMappings, TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        //读取所有的任务
        List<TaskStepEntity> steps = getTccDao().getTaskSteps(taskIds);
        if (null == steps || steps.isEmpty())
        {
            return;
        }
        
        //替换任务步骤中的路径
        replaceCommandPath(pathMappings, steps);
        
        //分批写入目标数据库
        int i = 0;
        int j;
        while (i < steps.size())
        {
            j = i + SyncTcc.BATCH_NUM;
            if (j > steps.size())
            {
                j = steps.size();
            }
            
            dstDao.replaceSteps(steps.subList(i, j));
            
            i = j;
        }
    }
    
    private void syncAlarmThreshold(TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        //读取所有的告警阈值
        List<TaskAlarmThresholdEntity> alarmThresholds = getAlarmThreManage().getAlarmThresholds(taskIds);
        //写到目标数据库
        dstDao.replaceAlarmThresholds(alarmThresholds);
    }
    
    private void syncAlarmItems(TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        //读取所有的告警项
        List<TaskAlarmItemsEntity> alarmItems = getTccDao().getAlarmItems(taskIds);
        //写到目标数据库
        dstDao.replaceAlarmItems(alarmItems);
    }
    
    private void syncAlarmChannels(TccDao dstDao, List<Long> taskIds)
        throws Exception
    {
        //读取所有的告警渠道
        List<TaskAlarmChannelInfoEntity> alarmChannels = getTccDao().getAlarmChannels(taskIds);
        //写到目标数据库
        dstDao.replaceAlarmChannels(alarmChannels);
    }
    
    private void notifyLoadTasks(String tccWebAppsAddr, String userName, String password, List<Long> taskIds)
        throws CException
    {
        //构造请求体
        LoadTasksReq req = new LoadTasksReq();
        req.setPrepare(false);
        req.setLoadAll(false);
        req.setUserName(userName);
        req.setPassword(password);
        req.setTaskIds(taskIds);
        String url = String.format("%s/loadTasks", tccWebAppsAddr);
        LoadTasksRsp rsp = TccUtil.callLoadTasks(url, req);
        
        if (ResultCode.SUCCESS != rsp.getResultCode())
        {
            //执行不成功，则直接抛出异常
            throw new CException(rsp.getResultCode());
        }
    }
    
    private Object[] syncTasks(String tccName, String userName, List<Long> taskIds, TccDao dstDao,
        List<PathMappingEntity> pathMappings)
        throws Exception
    {
        List<Long> canSyncTaskIds = new ArrayList<Long>();
        List<Long> noSyncTaskIds = new ArrayList<Long>();
        
        //存在的任务Id不允许部署
        List<Long> existTaskIds = dstDao.getExistTaskIds(taskIds);
        
        //读取所有的任务
        List<TaskEntity> tasks = getTaskManage().getTasksClone(taskIds);
        if (null == tasks || tasks.isEmpty())
        {
            return new Object[] {canSyncTaskIds, noSyncTaskIds};
        }
        
        //密码验证通过
        OperatorMgnt operatorMgnt = new OperatorMgnt();
        operatorMgnt.setTccDao(dstDao);
        
        //必需是管理员账号
        Operator operator = operatorMgnt.createOperator(userName);
        if (null == operator)
        {
            LOGGER.error("operator[userName={}] don't exist!", TccUtil.truncatEncode(userName));
            return new Object[] {canSyncTaskIds, noSyncTaskIds};
        }
        
        //停止所有任务
        stop(tasks);
        //替换任务中的OS用户
        replaceOsUser(tccName, tasks);
        
        List<TaskEntity> canSyncTasks = new ArrayList<TaskEntity>();
        for (TaskEntity taskE : tasks)
        {
            //必需目标任务不存在且有目标TCC的写权限
            if (!existTaskIds.contains(taskE.getTaskId()) && null != taskE.getOsUser()
                && operator.canAdd(taskE.getOsUser()))
            {
                canSyncTaskIds.add(taskE.getTaskId());
                canSyncTasks.add(taskE);
            }
            else
            {
                noSyncTaskIds.add(taskE.getTaskId());
            }
        }
        
        //替换任务中的OS用户组
        replaceOsGroup(tccName, canSyncTasks);
        //替换任务中的节点ID
        replaceNodeIds(tccName, canSyncTasks);
        //替换任务中的路径
        replaceFilePath(pathMappings, canSyncTasks);
        //将任务名重名的任务进行改名
        replaceDupNameTasks(dstDao, canSyncTasks);
        
        //分批写入目标数据库
        int i = 0;
        int j;
        while (i < canSyncTasks.size())
        {
            j = i + BATCH_NUM;
            if (j > canSyncTasks.size())
            {
                j = canSyncTasks.size();
            }
            
            dstDao.replaceTasks(canSyncTasks.subList(i, j));
            
            i = j;
        }
        
        //获取任务Id集合
        return new Object[] {canSyncTaskIds, noSyncTaskIds};
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
    }
}
