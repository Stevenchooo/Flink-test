/*
 * 文 件 名:  DependTaskTree.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-3-5
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.domain.CycleDependRelation;
import com.huawei.platform.tcc.domain.DependTaskNode;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 *  依赖树
 * 
 * @author  w00190929
 * @version [华为终端云统一账号模块, 2012-3-5]
 */
public class DependTaskTreeAction extends BaseAction
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4864455181377442666L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DependTaskTreeAction.class);
    
    /**
     * 获取正向依赖树json形式数据
     * @return 操作成功的标志
     * @throws Exception 异常
     */
    public String getDependTaskTree()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setCharacterEncoding("UTF-8");
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = null;
        String cycleId = null;
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            cycleId = request.getParameter("cycleId");
            
            if (!TccUtil.isCorrect(cycleId))
            {
                returnEmpty = true;
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("get dependTaskTree fail", e);
            returnEmpty = true;
        }
        
        try
        {
            if (returnEmpty)
            {
                //返回空
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                rv.addReturnValue("[]");
                rv.addReturnValue("0");
                rv.addReturnValue("");
                rv.addReturnValue("");
                
                setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                    SerializerFeature.UseISO8601DateFormat,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
                return SUCCESS;
            }
            
            DependTaskNode rootNode = new DependTaskNode();
            DependTaskNode curNode = null;
            TaskRunningStateEntity entity = null;
            TaskRunningStateEntity tRSEntity;
            //待处理任务状态队列
            Deque<DependTaskNode> processingQueue = new ArrayDeque<DependTaskNode>();
            //已经处理过依赖关系的任务周期,防止重复读取依赖关系
            List<TaskRunningStateEntity> taskRSProcessed = new ArrayList<TaskRunningStateEntity>();
            List<TaskRunningStateEntity> taskRunningStateList = new ArrayList<TaskRunningStateEntity>();
            List<TaskEntity> tasks = new ArrayList<TaskEntity>();
            Map<Long, TaskEntity> taskMap = new HashMap<Long, TaskEntity>();
            TaskEntity task = null;
            String endTime = "";
            if (null != taskId && (StringUtils.isNotBlank(cycleId)))
            {
                //封装根节点
                rootNode.setId(taskId);
                rootNode.setCycleId(cycleId);
                //获取任务周期状态
                entity = getTccService().getRunningStateEntity(taskId, cycleId);
                taskRunningStateList.add(entity);
                //设置根节点的运行结束时间到页面显示
                endTime = convertDateTimeFormat(entity.getRunningEndTime(), "yyyy-MM-dd HH:mm:ss");
                rootNode.setIconCls(showIconCls(entity.getState()));
                rootNode.setText(String.format("%d,%s,%d,%s,%s",
                    taskId,
                    cycleId,
                    entity.getState(),
                    convertDateTimeFormat(entity.getRunningStartTime(), null),
                    convertDateTimeFormat(entity.getRunningEndTime(), null)));
                
                List<TaskRunningStateEntity> dependTaskList;
                //初始化依赖任务树的根
                processingQueue.push(rootNode);
                //处理依赖周期任务树
                while (!processingQueue.isEmpty())
                {
                    curNode = processingQueue.remove();
                    
                    //封装已处理依赖关系的任务周期
                    tRSEntity = new TaskRunningStateEntity();
                    tRSEntity.setTaskId(curNode.getId());
                    tRSEntity.setCycleId(curNode.getCycleId());
                    taskRSProcessed.add(tRSEntity);
                    //获取任务信息
                    if (!taskMap.containsKey(curNode.getId()))
                    {
                        task = getTccPortalService().getTaskInfo(curNode.getId());
                        taskMap.put(curNode.getId(), task);
                        tasks.add(task);
                    }
                    else
                    {
                        task = taskMap.get(curNode.getId());
                    }
                    
                    //获取正向依赖的任务周期
                    dependTaskList = getTccService().getDependingRunningState(task, curNode.getCycleId());
                    
                    if (null != dependTaskList && dependTaskList.size() > 0)
                    {
                        //封装子节点children
                        List<DependTaskNode> childrenList = new ArrayList<DependTaskNode>();
                        for (TaskRunningStateEntity taskRSEntity : dependTaskList)
                        {
                            DependTaskNode newNode = new DependTaskNode();
                            newNode.setId(taskRSEntity.getTaskId());
                            newNode.setCycleId(taskRSEntity.getCycleId());
                            //保证processedQueue中的任务运行状态对象(主键)的唯一性和已经处理过的任务周期 
                            if (!processingQueue.contains(newNode) && !taskRSProcessed.contains(taskRSEntity))
                            {
                                //获取任务信息
                                if (!taskMap.containsKey(newNode.getId()))
                                {
                                    task = getTccPortalService().getTaskInfo(newNode.getId());
                                    taskMap.put(newNode.getId(), task);
                                    tasks.add(task);
                                }
                                else
                                {
                                    task = taskMap.get(newNode.getId());
                                }
                                entity = getTccService().getRunningStateEntity(newNode.getId(), newNode.getCycleId());
                                taskRunningStateList.add(entity);
                                newNode.setText(String.format("%d,%s,%d,%s,%s",
                                    taskRSEntity.getTaskId(),
                                    taskRSEntity.getCycleId(),
                                    entity.getState(),
                                    convertDateTimeFormat(entity.getRunningStartTime(), null),
                                    convertDateTimeFormat(entity.getRunningEndTime(), null)));
                                
                                
                                newNode.setIconCls(showIconCls(entity.getState()));
                                childrenList.add(newNode);
                                if (!curNode.getId().equals(newNode.getId()))
                                {
                                    processingQueue.add(newNode);
                                }
                            }
                        }
                        curNode.setChildren(childrenList);
                    }
                }
            }
            
            checkQueryPrivelge(null, tasks);
            
            //获取最早的开始运行时间
            Date minStartTime = getMinStartTime(taskRunningStateList);
            Integer treeNodeCount = taskRunningStateList.size();
            String startTime = convertDateTimeFormat(minStartTime, "yyyy-MM-dd HH:mm:ss");
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue("[" + JSONObject.toJSONString(rootNode) + "]");
            rv.addReturnValue(Integer.toString(treeNodeCount));
            rv.addReturnValue(startTime);
            rv.addReturnValue(endTime);
        }
        catch (PrivilegeNotEnoughException e)
        {
            LOGGER.error("get dependTaskTree fail", e);
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("get dependTaskTree fail", e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取反向依赖树
     * @return 反向依赖树
     * @throws Exception 异常
     */
    public String getDependReverseTree()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = null;
        String cycleId = null;
        Integer maxCount = 0;
        
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            cycleId = request.getParameter("cycleId");
            maxCount = Integer.parseInt(request.getParameter("maxcount"));
            
            if (!TccUtil.isCorrect(cycleId))
            {
                returnEmpty = true;
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("getDependReverseTree fail", e);
            returnEmpty = true;
        }
        
        try
        {
            
            if (returnEmpty)
            {
                //返回空
                rv.setSuccess(true);
                rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                rv.addReturnValue("[]");
                rv.addReturnValue("0");
                rv.addReturnValue("");
                rv.addReturnValue("");
                
                setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                    SerializerFeature.UseISO8601DateFormat,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
                return SUCCESS;
            }
            
            DependTaskNode rootNode = new DependTaskNode();
            DependTaskNode curNode = null;
            TaskRunningStateEntity tRSEntity;
            //待处理任务状态队列
            Deque<DependTaskNode> processingQueue = new ArrayDeque<DependTaskNode>();
            //已经处理过依赖关系的任务周期,防止重复读取依赖关系
            List<TaskRunningStateEntity> taskRSProcessed = new ArrayList<TaskRunningStateEntity>();
            //依赖任务节点
            DependTaskNode newNode;
            Map<Long, TaskEntity> taskMaps = new HashMap<Long, TaskEntity>();
            Map<Long, List<TaskEntity>> taskDepMaps = new HashMap<Long, List<TaskEntity>>();
            TaskEntity currTask;
            List<TaskEntity> tasks;
            List<TaskEntity> allTasks = new ArrayList<TaskEntity>();
            List<CycleDependRelation> cycleDeppingRs;
            List<Long> taskIds;
            TaskRunningStateEntity taskRsTemp = new TaskRunningStateEntity();
            boolean noAdd = false;
            if (null != taskId && (StringUtils.isNotBlank(cycleId)))
            {
                //封装根节点
                rootNode.setId(taskId);
                rootNode.setCycleId(cycleId);
                //初始化依赖任务树的根
                processingQueue.push(rootNode);
                List<DependTaskNode> childrens;
                //处理依赖周期任务树
                while (!processingQueue.isEmpty())
                {
                    curNode = processingQueue.remove();
                    
                    //封装已处理依赖关系的任务周期
                    tRSEntity = new TaskRunningStateEntity();
                    tRSEntity.setTaskId(curNode.getId());
                    tRSEntity.setCycleId(curNode.getCycleId());
                    //获取反向依赖的任务周期
                    //List<TaskRunningStateEntity> dependTaskList = getTccService().getDependedRSList(tRSEntity);
                    //List<CycleDependRelation> getAllCycleDeppingRs
                    if (!taskMaps.containsKey(tRSEntity.getTaskId()))
                    {
                        currTask = getTccPortalService().getTaskInfo(tRSEntity.getTaskId());
                        if (null == currTask)
                        {
                            LOGGER.warn("task[taskId={}] does't exist! ignore it!", tRSEntity.getTaskId());
                            continue;
                        }
                        allTasks.add(currTask);
                        taskMaps.put(currTask.getTaskId(), currTask);
                    }
                    else
                    {
                        currTask = taskMaps.get(tRSEntity.getTaskId());
                    }
                    
                    //缓存任务依赖关系
                    if (!taskDepMaps.containsKey(currTask.getTaskId()))
                    {
                        taskIds = new ArrayList<Long>();
                        taskIds.add(currTask.getTaskId());
                        //查询直接反向依赖任务
                        tasks = getTccPortalService().getDeppingTasks(taskIds);
                        taskDepMaps.put(currTask.getTaskId(), tasks);
                    }
                    else
                    {
                        tasks = taskDepMaps.get(currTask.getTaskId());
                    }
                    
                    cycleDeppingRs = getTccService().getAllCycleDeppingRs(currTask, tasks, tRSEntity.getCycleId());
                    
                    //查询任务周期的状态
                    tRSEntity = getTccService().getRunningStateEntity(tRSEntity.getTaskId(), tRSEntity.getCycleId());
                    
                    //不存在该周期
                    if (null == tRSEntity || tRSEntity.getState() == RunningState.NOTINIT)
                    {
                        //根节点显示不存在状态
                        if (curNode == rootNode)
                        {
                            curNode.setIconCls(showIconCls(tRSEntity.getState()));
                            curNode.setText(String.format("%d,%s,%d,%s,%s",
                                curNode.getId(),
                                curNode.getCycleId(),
                                tRSEntity.getState(),
                                convertDateTimeFormat(tRSEntity.getRunningStartTime(), null),
                                convertDateTimeFormat(tRSEntity.getRunningEndTime(), null)));
                        }
                        else
                        {
                            childrens = curNode.getParent().getChildren();
                            if (null != childrens)
                            {
                                childrens.remove(curNode);
                            }
                        }
                        continue;
                    }
                    
                    curNode.setIconCls(showIconCls(tRSEntity.getState()));
                    curNode.setText(String.format("%d,%s,%d,%s,%s",
                        curNode.getId(),
                        curNode.getCycleId(),
                        tRSEntity.getState(),
                        convertDateTimeFormat(tRSEntity.getRunningStartTime(), null),
                        convertDateTimeFormat(tRSEntity.getRunningEndTime(), null)));
                    
                    taskRSProcessed.add(tRSEntity);
                    
                    //封装子节点children
                    List<DependTaskNode> childrenList = new ArrayList<DependTaskNode>();
                    
                    if (noAdd)
                    {
                        if (childrenList.size() != 0)
                        {
                            curNode.setText(curNode.getText() + ",1");
                        }
                        
                    }
                    else
                    {
                        for (CycleDependRelation cycleDependR : cycleDeppingRs)
                        {
                            //保证processedQueue中的任务运行状态对象(主键)的唯一性和已经处理过的任务周期 
                            newNode = new DependTaskNode();
                            newNode.setId(cycleDependR.getDependTaskId());
                            newNode.setCycleId(cycleDependR.getDependCycleId());
                            
                            taskRsTemp.setTaskId(newNode.getId());
                            taskRsTemp.setCycleId(newNode.getCycleId());
                            
                            if (!processingQueue.contains(newNode) && !childrenList.contains(newNode)
                                && !taskRSProcessed.contains(taskRsTemp))
                            {
                                if (taskRSProcessed.size() + processingQueue.size() + childrenList.size() >= maxCount)
                                {
                                    noAdd = true;
                                    break;
                                    //不添加到队列中了
                                }
                                else
                                {
                                    newNode.setParent(curNode);
                                    childrenList.add(newNode);
                                }
                            }
                        }
                        
                        if (noAdd)
                        {
                            curNode.setText(curNode.getText() + ",1");
                        }
                        else
                        {
                            processingQueue.addAll(childrenList);
                            curNode.setChildren(childrenList);
                        }
                    }
                }
            }
            
            checkQueryPrivelge(null, allTasks);
            
            Integer reverserTreeNodeCount = taskRSProcessed.size();
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue("[" + JSONObject.toJSONString(rootNode) + "]");
            rv.addReturnValue(Integer.toString(reverserTreeNodeCount));
            rv.addReturnValue("");
            rv.addReturnValue("");
        }
        catch (PrivilegeNotEnoughException e)
        {
            LOGGER.error("getDependReverseTree fail", e);
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("get DependReverseTree fail", e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取最早开始运行时间
     * @param taskRunningStateList 传入的任务周期运行状态列表
     * @return  最早开始运行的时间
     * @see [类、类#方法、类#成员]
     */
    private Date getMinStartTime(List<TaskRunningStateEntity> taskRunningStateList)
    {
        Date minStartTime = null;
        if (null != taskRunningStateList && taskRunningStateList.size() > 0)
        {
            
            //先找一个不为空的运行开始时间为最小值
            for (TaskRunningStateEntity entity : taskRunningStateList)
            {
                if (null != entity.getRunningStartTime())
                {
                    //不为空，且在最小值前
                    if (null == minStartTime)
                    {
                        minStartTime = entity.getRunningStartTime();
                    }
                    else if (minStartTime.after(entity.getRunningStartTime()))
                    {
                        minStartTime = entity.getRunningStartTime();
                    }
                }
            }
        }
        return minStartTime;
    }
    
    /**
     * <将Date类型的时间按照指定格式转成String类型>
     * 
     * @param time 时间 
     * @param ownTimeFormat 时间格式
     * @return 转化后的时间  如：2011-08-17T15:46:10+08:00
     * @throws CException 异常
     * @see [类、类#方法、类#成员]
     */
    private static String convertDateTimeFormat(Date time, String timeFormat)
        throws CException
    {
        String ownTimeFormat = timeFormat;
        if (null == time)
        {
            return "";
        }
        if (StringUtils.isBlank(ownTimeFormat))
        {
            ownTimeFormat = "yyyy-MM-dd HH:mm:ss";
        }
        
        String date = null;
        try
        {
            date = DateFormatUtils.format(time, ownTimeFormat);
        }
        catch (Exception e)
        {
            LOGGER.error("convert time {} according to patter {} error", new Object[] {time, ownTimeFormat, e});
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        return date;
    }
    
    /**
     * 根据状态生成显示到页面的图片
     * @param state 状态
     * @return 图片标志
     * @see [类、类#方法、类#成员]
     */
    private String showIconCls(Integer state)
    {
        String statePng = null;
        
        switch (state)
        {
            case RunningState.INIT:
                statePng = "icon-state-init";
                break;
            case RunningState.START:
                statePng = "icon-state-start";
                break;
            case RunningState.SUCCESS:
                statePng = "icon-state-success";
                break;
            case RunningState.ERROR:
                statePng = "icon-state-error";
                break;
            case RunningState.TIMEOUT:
                statePng = "icon-state-timeout";
                break;
            case RunningState.VSUCCESS:
                statePng = "icon-state-vsucess";
                break;
            case RunningState.NOTINIT:
                statePng = "icon-state-noinit";
                break;
            case RunningState.WAITTINGRUN:
                statePng = "icon-state-waitrun";
                break;
            case RunningState.RUNNING:
                statePng = "icon-state-running";
                break;
            case RunningState.REQDELETE:
                statePng = "icon-state-reqdelete";
                break;
            case RunningState.STOP:
                statePng = "icon-state-stop";
                break;
            case RunningState.NOBATCH:
                statePng = "icon-nobatch";
                break;
            default:
                statePng = "icon-state-unknown";
                break;
        }
        return statePng;
    }
}
