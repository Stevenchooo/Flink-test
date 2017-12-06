/*
 * 文 件 名:  SyncTcc.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  同步TCC
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-18
 */
package com.huawei.platform.tcc.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.huawei.platform.tcc.alarm.AlarmThresholdManage;
import com.huawei.platform.tcc.constants.type.TaskState;
import com.huawei.platform.tcc.dao.DaoFactory;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.entity.NodeMappingEntity;
import com.huawei.platform.tcc.entity.OSUserMappingEntity;
import com.huawei.platform.tcc.entity.PathMappingEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskStepEntity;
import com.huawei.platform.tcc.entity.UserGroupMappingEntity;
import com.huawei.platform.tcc.task.TaskManage;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * 同步TCC
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-18]
 */
public class SyncTcc
{
    /**
     * 一批提交的数量
     */
    public static final int BATCH_NUM = 100;
    
    /**
     * 一批查询的运行状态的任务数量
     */
    public static final int TASK_BATCH_NUM = 5;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTcc.class);
    
    private TaskManage taskManage;
    
    private TccDao tccDao;
    
    private AlarmThresholdManage alarmThreManage;
    
    private DaoFactory daoFactory;
    
    public TaskManage getTaskManage()
    {
        return taskManage;
    }
    
    public void setTaskManage(TaskManage taskManage)
    {
        this.taskManage = taskManage;
    }
    
    public AlarmThresholdManage getAlarmThreManage()
    {
        return alarmThreManage;
    }
    
    public void setAlarmThreManage(AlarmThresholdManage alarmThreManage)
    {
        this.alarmThreManage = alarmThreManage;
    }
    
    public DaoFactory getDaoFactory()
    {
        return daoFactory;
    }
    
    public void setDaoFactory(DaoFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 替换名字重复的任务名
     * @param tasks 任务集合
     * @param dstDao 目标TCC Dao
     * @throws Exception 异常
     */
    protected void replaceDupNameTasks(TccDao dstDao, List<TaskEntity> tasks)
        throws Exception
    {
        String newName;
        //获取任务名冲突的任务Id集合
        List<Long> taskIds = dstDao.getDupNameTasks(tasks);
        for (TaskEntity taskE : tasks)
        {
            if (taskIds.contains(taskE.getTaskId()))
            {
                //对任务名改名
                newName = taskE.getTaskName() + "_" + Long.toString(new Date().getTime());
                taskE.setTaskName(newName);
            }
        }
    }
    
    /**
     * 停止任务
     * @param tasks 任务集合
     */
    protected void stop(List<TaskEntity> tasks)
    {
        //停止所有任务
        for (TaskEntity taskE : tasks)
        {
            //停用并停止任务
            taskE.setTaskEnableFlag(false);
            taskE.setTaskState(TaskState.STOP);
        }
    }
    
    /**
     * 替换任务步骤中的路径
     * @param pathMappings 路径映射
     * @param steps 步骤
     * @throws Exception 异常
     */
    protected void replaceCommandPath(List<PathMappingEntity> pathMappings, List<TaskStepEntity> steps)
        throws Exception
    {
        String dstPath;
        String regex;
        String replacement;
        //读取映射关系
        for (TaskStepEntity stepE : steps)
        {
            dstPath = stepE.getCommand();
            if (!StringUtils.isBlank(dstPath))
            {
                for (PathMappingEntity pathMapping : pathMappings)
                {
                    regex = pathMapping.getSrcPath();
                    replacement = pathMapping.getDstPath();
                    //可以使用正则表达式
                    dstPath = dstPath.replaceAll(regex, replacement);
                    //目标路径替换成功后，退出
                    if (!dstPath.equals(stepE.getCommand()))
                    {
                        stepE.setCommand(dstPath);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 循环修改任务
     * @param dstDao 目标TCC
     * @param tasks 任务集合
     * @throws Exception 异常
     */
    protected void replaceTasksSingle(TccDao dstDao, List<TaskEntity> tasks)
        throws Exception
    {
        for (TaskEntity taskE : tasks)
        {
            try
            {
                //循环处理每一个任务
                dstDao.replaceTask(taskE);
            }
            catch (DuplicateKeyException e)
            {
                //对任务名改名
                String newName = taskE.getTaskName() + "_" + Long.toString(new Date().getTime());
                taskE.setTaskName(newName);
                
                //重新提交
                dstDao.replaceTask(taskE);
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
    
    private Map<String, OSUserMappingEntity> getOsUserMappings(String tccName)
        throws Exception
    {
        //读取os用户映射
        List<OSUserMappingEntity> mappings = tccDao.getOSUserMappings(tccName);
        
        Map<String, OSUserMappingEntity> osUserMappings = new HashMap<String, OSUserMappingEntity>();
        //构造hash
        for (OSUserMappingEntity osUserMapping : mappings)
        {
            osUserMappings.put(osUserMapping.getSrcOsUser(), osUserMapping);
        }
        
        return osUserMappings;
    }
    
    /**
     * 替换OS用户
     * @param tccName tcc名
     * @param tasks 任务集合
     * @throws Exception 异常
     */
    protected void replaceOsUser(String tccName, List<TaskEntity> tasks)
        throws Exception
    {
        String dstOsUser;
        OSUserMappingEntity osUserME;
        Map<String, OSUserMappingEntity> osUserMappings = getOsUserMappings(tccName);
        
        //读取映射关系
        for (TaskEntity taskE : tasks)
        {
            if (StringUtils.isBlank(taskE.getOsUser()))
            {
                LOGGER.warn("cann't replace osUser , beacuse osUser of taskE[{}] is blank!", taskE);
                continue;
            }
            
            osUserME = osUserMappings.get(taskE.getOsUser());
            if (null == osUserME)
            {
                LOGGER.warn("cann't replace osUser, beacues of no osUser Mapping about tccName[{}] and osUser[{}]!",
                    TccUtil.truncatEncode(tccName),
                    TccUtil.truncatEncode(taskE.getOsUser()));
                continue;
            }
            
            dstOsUser = osUserME.getDstOsUser();
            
            //替换OS用户名
            taskE.setOsUser(dstOsUser);
        }
    }
    
    private Map<String, UserGroupMappingEntity> getUserGroupMappings(String tccName)
        throws Exception
    {
        //读取用户组映射
        List<UserGroupMappingEntity> mappings = tccDao.getUserGroupMappings(tccName);
        
        Map<String, UserGroupMappingEntity> userGroupMappings = new HashMap<String, UserGroupMappingEntity>();
        //构造hash
        for (UserGroupMappingEntity userGroupMapping : mappings)
        {
            userGroupMappings.put(userGroupMapping.getSrcUserGroup(), userGroupMapping);
        }
        
        return userGroupMappings;
    }
    
    /**
     * 替换OS用户组
     * @param tccName tcc名
     * @param tasks 任务集合
     * @throws Exception 异常
     */
    protected void replaceOsGroup(String tccName, List<TaskEntity> tasks)
        throws Exception
    {
        String dstUserGroup;
        UserGroupMappingEntity userGroupME;
        
        Map<String, UserGroupMappingEntity> userGroupMappings = getUserGroupMappings(tccName);
        
        //读取映射关系
        for (TaskEntity taskE : tasks)
        {
            if (StringUtils.isBlank(taskE.getUserGroup()))
            {
                LOGGER.warn("cann't replace userGroup , beacuse userGroup of taskE[{}] is blank!", taskE);
                continue;
            }
            
            userGroupME = userGroupMappings.get(taskE.getUserGroup());
            if (null == userGroupME)
            {
                LOGGER.warn("cann't replace userGroup, "
                    + "beacues of no userGroup Mapping about tccName[{}] and userGroup[{}]!",
                    TccUtil.truncatEncode(tccName),
                    TccUtil.truncatEncode(taskE.getUserGroup()));
                continue;
            }
            
            dstUserGroup = userGroupME.getDstUserGroup();
            
            //替换OS用户组
            taskE.setUserGroup(dstUserGroup);
        }
    }
    
    private Map<Integer, String> getNodeMappings(String tccName)
        throws Exception
    {
        //读取节点映射
        List<NodeMappingEntity> mappings = tccDao.getNodeMappings(tccName);
        
        Map<Integer, String> nodeMappings = new HashMap<Integer, String>();
        //构造hash
        for (NodeMappingEntity nodeE : mappings)
        {
            nodeMappings.put(nodeE.getSrcNodeId(), nodeE.getDstNodeId());
        }
        
        return nodeMappings;
    }
    
    /**
     * 替换节点Id
     * @param tccName tcc名
     * @param tasks 任务集合
     * @throws Exception 异常
     */
    protected void replaceNodeIds(String tccName, List<TaskEntity> tasks)
        throws Exception
    {
        Map<Integer, String> nodeMappings = getNodeMappings(tccName);
        //目标节点Id集合
        List<String> dstNodeIds = new ArrayList<String>();
        List<Integer> srcNodeIds;
        String dstNodeId;
        for (TaskEntity taskE : tasks)
        {
            dstNodeIds.clear();
            srcNodeIds = TccUtil.parseNodeIds(taskE.getDeployedNodeList());
            if (null != srcNodeIds && !srcNodeIds.isEmpty())
            {
                for (Integer srcNodeId : srcNodeIds)
                {
                    dstNodeId = nodeMappings.get(srcNodeId);
                    if (null == dstNodeId)
                    {
                        //保留，不进行替换
                        dstNodeIds.add(Integer.toString(srcNodeId));
                    }
                    else
                    {
                        if (StringUtils.isNotBlank(dstNodeId))
                        {
                            dstNodeIds.add(dstNodeId);
                        }
                        //空白字符串表示删除
                    }
                    
                }
                
                //修改部署节点列表
                taskE.setDeployedNodeList(TccUtil.toDeployedNodeIdStr(dstNodeIds));
            }
        }
    }
    
    /**
     * 替换文件路径
     * @param pathMappings 路径映射
     * @param tasks 任务集合
     * @throws Exception 异常
     */
    protected void replaceFilePath(List<PathMappingEntity> pathMappings, List<TaskEntity> tasks)
        throws Exception
    {
        String dstPath;
        String regex;
        String replacement;
        
        //读取映射关系
        for (TaskEntity taskE : tasks)
        {
            dstPath = taskE.getInputFileList();
            if (!StringUtils.isBlank(taskE.getInputFileList()))
            {
                for (PathMappingEntity pathMapping : pathMappings)
                {
                    regex = pathMapping.getSrcPath();
                    replacement = pathMapping.getDstPath();
                    //可以使用正则表达式
                    dstPath = taskE.getInputFileList().replaceAll(regex, replacement);
                    //目标路径替换成功后，退出
                    if (!dstPath.equals(taskE.getInputFileList()))
                    {
                        taskE.setInputFileList(dstPath);
                        break;
                    }
                }
            }
        }
    }
}
