/*
 * 文 件 名:  TaskStepAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.QueryRSType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.Log2DBQueryParam;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.TaskOwnerInfo;
import com.huawei.platform.tcc.entity.BatchRunningStateEntity;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.entity.StepRunningStateEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务周期详情
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-17]
 */
public class TaskRunningDetailAction extends BaseAction
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = 5227205423165903689L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunningDetailAction.class);
    
    private transient List<Long> taskIdList;
    
    private transient TaskRunningStateEntity taskRs;
    
    private transient List<String> cycleIdList;
    
    /**
     * 获取周期的相关日志
     * @return  周期的相关日志
     * @throws Exception 统一封装的异常
     */
    public String getLogMsgs()
        throws Exception
    {
        //默认返回空
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.VALUE);
        rv.addReturnValue("");
        
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Log2DBQueryParam logQuery = new Log2DBQueryParam();
        
        boolean returnEmpty = false;
        boolean tccLog = false;
        Long taskId = null;
        try
        {
            logQuery.setTaskId(request.getParameter("taskId"));
            taskId = Long.parseLong(logQuery.getTaskId());
            logQuery.setCycleId(request.getParameter("cycleId"));
            
            tccLog = Boolean.parseBoolean(request.getParameter("tccLog"));
            logQuery.setPage(Integer.parseInt(request.getParameter("pageNumber")));
            logQuery.setRows(Integer.parseInt(request.getParameter("pageSize")));
        }
        catch (Exception e)
        {
            LOGGER.error("getLogMsg4Detail error! param is [tccLog={},taskId={},cycleId={},pageNumber={},pageSize={}].",
                new Object[] {TccUtil.truncatEncode(request.getParameter("tccLog")),
                    TccUtil.truncatEncode(request.getParameter("taskId")),
                    TccUtil.truncatEncode(request.getParameter("cycleId")),
                    TccUtil.truncatEncode(request.getParameter("pageNumber")),
                    TccUtil.truncatEncode(request.getParameter("pageSize")), e});
            returnEmpty = true;
        }
        
        try
        {
            if (!returnEmpty)
            {
                returnEmpty = !isVisiblePri(getOperator(), taskId);
            }
            
            if (!returnEmpty && !StringUtils.isEmpty(logQuery.getCycleId()))
            {
                try
                {
                    List<Log2DBEntity> logList;
                    logQuery.setStartIndex((long)(logQuery.getPage() - 1) * logQuery.getRows());
                    if (tccLog)
                    {
                        //获取任务周期相关日志
                        logList = getTccPortalService().getTccLogList(logQuery);
                    }
                    else
                    {
                        //获取控制台输出日志
                        logList = getTccPortalService().getRsLogList(logQuery);
                    }
                    
                    if (null != logList && !logList.isEmpty())
                    {
                        rv.setSuccess(true);
                        rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                        rv.addReturnValue(TccUtil.format(logList));
                    }
                }
                catch (Exception e)
                {
                    LOGGER.info("grabCycleOffsetLeftTime error! taskId is {},cycleId is {}.",
                        new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")),
                            TccUtil.truncatEncode(logQuery.getCycleId())},
                        e);
                }
            }
            
        }
        catch (Exception e)
        {
            LOGGER.info("grabCycleOffsetLeftTime error! taskId is {},cycleId is {}.",
                new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")),
                    TccUtil.truncatEncode(logQuery.getCycleId())},
                e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("getLogMsgs fail!", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 根据条件查询日志总记录数
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String getLogsCount()
        throws Exception
    {
        //默认返回0
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.VALUE);
        rv.addReturnValue("0");
        
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Log2DBQueryParam logQuery = new Log2DBQueryParam();
        
        boolean returnEmpty = false;
        boolean tccLog = false;
        Long taskId = null;
        try
        {
            logQuery.setTaskId(request.getParameter("taskId"));
            taskId = Long.parseLong(logQuery.getTaskId());
            logQuery.setCycleId(request.getParameter("cycleId"));
            tccLog = Boolean.parseBoolean(request.getParameter("tccLog"));
        }
        catch (Exception e)
        {
            LOGGER.error("getLogsCount error! param is [tccLog={},taskId={},cycleId={}].",
                new Object[] {TccUtil.truncatEncode(request.getParameter("tccLog")),
                    TccUtil.truncatEncode(request.getParameter("taskId")),
                    TccUtil.truncatEncode(request.getParameter("cycleId")), e});
            returnEmpty = true;
        }
        
        try
        {
            if (!returnEmpty)
            {
                returnEmpty = !isVisiblePri(getOperator(), taskId);
            }
            
            if (!returnEmpty && !StringUtils.isEmpty(logQuery.getTaskId())
                && !StringUtils.isEmpty(logQuery.getCycleId()))
            {
                
                try
                {
                    Integer count;
                    if (tccLog)
                    {
                        count = getTccPortalService().getTccLogCount(logQuery);
                    }
                    else
                    {
                        count = getTccPortalService().getRsLogCount(logQuery);
                    }
                    
                    rv.setSuccess(true);
                    rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
                    rv.addReturnValue(Integer.toString(count));
                }
                catch (Exception e)
                {
                    LOGGER.info("grabCycleOffsetLeftTime error! taskId is {},cycleId is {}.",
                        new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")),
                            TccUtil.truncatEncode(logQuery.getCycleId())},
                        e);
                }
            }
            
        }
        catch (Exception e)
        {
            LOGGER.info("getLogsCount error! taskId is {},cycleId is {}.",
                new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")),
                    TccUtil.truncatEncode(logQuery.getCycleId())},
                e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(rv.toString().getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("getLogsCount fail!", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 获取周期偏移剩余时间
     * @return  获取周期偏移剩余时间
     * @throws Exception 统一封装的异常
     */
    public String grabCycleOffsetLeftTime()
        throws Exception
    {
        String result = "false";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Long taskId = null;
        String cycleId = null;
        boolean returnFalse = false;
        try
        {
            cycleId = request.getParameter("cycleId");
            taskId = Long.parseLong(request.getParameter("taskId"));
        }
        catch (NumberFormatException e)
        {
            LOGGER.info("grabCycleOffsetLeftTime error! taskId is {},cycleId is {}.",
                new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")), TccUtil.truncatEncode(cycleId)},
                e);
            returnFalse = true;
        }
        
        try
        {
            if (!returnFalse && !StringUtils.isEmpty(cycleId))
            {
                try
                {
                    TaskEntity taskE = getTccPortalService().getTaskInfo(taskId);
                    Date cycleTime = TccUtil.covCycleID2Date(cycleId);
                    if (null != taskE)
                    {
                        result = "true|" + getCycleOffsetLeftTime(taskE.getCycleOffset(), cycleTime);
                    }
                }
                catch (Exception e)
                {
                    LOGGER.info("grabCycleOffsetLeftTime error! taskId is {},cycleId is {}.",
                        new Object[] {TccUtil.truncatEncode(request.getParameter("taskId")),
                            TccUtil.truncatEncode(cycleId)},
                        e);
                }
            }
            
        }
        catch (Exception e)
        {
            LOGGER.info("grabCycleOffsetLeftTime error! taskId is {},cycleId is {}.",
                new Object[] {request.getParameter("taskId"), cycleId},
                e);
        }
        
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取周期偏移剩余时间
     * @param cycleOffSet 周期偏移
     * @param cycleIdTime 周期Id日期
     */
    private String getCycleOffsetLeftTime(String cycleOffSet, Date cycleIdTime)
    {
        //cycleOffSet 形如：xMxD xhxm
        
        String[] cycleOffSetArr = new String[1];
        cycleOffSetArr[0] = cycleOffSet;
        //获取偏移月数
        int mouths = TccUtil.extractNum(cycleOffSetArr, "M");
        //获取偏移天数
        int days = TccUtil.extractNum(cycleOffSetArr, "D");
        //获取偏移小时数
        int hours = TccUtil.extractNum(cycleOffSetArr, "h");
        //获取偏移分钟数
        int minutes = TccUtil.extractNum(cycleOffSetArr, "m");
        
        Calendar cDate = Calendar.getInstance();
        //将周期加上周期偏移
        cDate.setTime(cycleIdTime);
        cDate.add(Calendar.MONTH, mouths);
        cDate.add(Calendar.DAY_OF_MONTH, days);
        cDate.add(Calendar.HOUR_OF_DAY, hours);
        cDate.add(Calendar.MINUTE, minutes);
        
        Date currentDate = CommonUtils.getCrruentTime();
        long diffMillSec = cDate.getTime().getTime() - currentDate.getTime();
        if (diffMillSec <= 0)
        {
            return "0:0:0";
        }
        else
        {
            long diffHours = diffMillSec / TccConfig.MILLS_PER_HOUR;
            diffMillSec = diffMillSec % TccConfig.MILLS_PER_HOUR;
            long diffMinutes = diffMillSec / TccConfig.MILLS_PER_MINUTE;
            diffMillSec = diffMillSec % TccConfig.MILLS_PER_MINUTE;
            long diffSeconds = diffMillSec / TccConfig.MILLS_PER_SECOND;
            return String.format("%d:%d:%d", diffHours, diffMinutes, diffSeconds);
        }
    }
    
    /**
     * 任务批次运行状态列表数据json格式
     * 
     * @return 任务批次运行状态列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String queryRSList2Json()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Long taskId = null;
        String cycleId = null;
        int queryRSType = 1;
        Integer pageNumber = 1;
        Integer pageSize = 1;
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            cycleId = request.getParameter("cycleId");
            queryRSType = Integer.parseInt(request.getParameter("queryRSType"));
            if (queryRSType != QueryRSType.CYCLE)
            {
                pageNumber = Integer.parseInt(request.getParameter("page"));
                pageSize = Integer.parseInt(request.getParameter("rows"));
            }
            
            //检查cycleId是否合法
            if (!TccUtil.isCorrect(cycleId))
            {
                returnEmpty = true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("taskId or queryRSType must be an Integer!", e);
            returnEmpty = true;
        }
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        
        Operator operator = getOperator();
        if (!returnEmpty)
        {
            returnEmpty = !isVisiblePri(operator, taskId);
        }
        
        String returnStr = "";
        try
        {
            switch (queryRSType)
            {
                case QueryRSType.CYCLE:
                {
                    List<TaskRunningStateEntity> taskRsRtnLst = new ArrayList<TaskRunningStateEntity>(1);
                    if (!returnEmpty)
                    {
                        TaskRunningStateEntity taskRsRtn = getTccService().getRunningStateEntity(taskId, cycleId);
                        taskRsRtnLst.add(taskRsRtn);
                    }
                    jsonObject.put("total", taskRsRtnLst.size());
                    jsonObject.put("rows", taskRsRtnLst);
                    break;
                }
                case QueryRSType.BATCH:
                {
                    if (!returnEmpty)
                    {
                        Long startIndex = (long)(pageNumber - 1) * pageSize;
                        Integer rows = pageSize;
                        
                        List<BatchRunningStateEntity> batchRsRtn =
                            getTccPortalService().getBatchRSList(taskId, cycleId, startIndex, rows);
                        jsonObject.put("total", getTccPortalService().getBatchRSCount(taskId, cycleId));
                        jsonObject.put("rows", batchRsRtn);
                    }
                    else
                    {
                        jsonObject.put("total", 0);
                        jsonObject.put("rows", new ArrayList<BatchRunningStateEntity>(0));
                    }
                    
                    break;
                }
                case QueryRSType.STEP:
                {
                    if (!returnEmpty)
                    {
                        Long startIndex = (long)(pageNumber - 1) * pageSize;
                        Integer rows = pageSize;
                        
                        List<StepRunningStateEntity> stepRsRtn =
                            getTccPortalService().getStepRSList(taskId, cycleId, startIndex, rows);
                        jsonObject.put("total", getTccPortalService().getStepRSCount(taskId, cycleId));
                        jsonObject.put("rows", stepRsRtn);
                    }
                    else
                    {
                        jsonObject.put("total", 0);
                        jsonObject.put("rows", new ArrayList<StepRunningStateEntity>(0));
                    }
                    
                    break;
                }
                case QueryRSType.DEPEND_CYCLE:
                {
                    List<TaskRunningStateEntity> filterRSs;
                    if (!returnEmpty)
                    {
                        List<TaskRunningStateEntity> taskRsListRtn =
                            getTccService().getDependingRunningState(taskId, cycleId);
                        
                        //检查是否具有权限
                        List<Long> taskIds = new ArrayList<Long>();
                        for (TaskRunningStateEntity taskRS : taskRsListRtn)
                        {
                            if (!taskIds.contains(taskRS.getTaskId()))
                            {
                                taskIds.add(taskRS.getTaskId());
                            }
                        }
                        
                        //获取原任务的任务归属信息
                        List<TaskOwnerInfo> srcTaskOwners = getTccPortalService().getTaskOwnersByIds(taskIds);
                        String userGroup;
                        //没有权限的任务
                        List<TaskOwnerInfo> noPriTasks = new ArrayList<TaskOwnerInfo>();
                        for (TaskOwnerInfo srcTaskOwner : srcTaskOwners)
                        {
                            userGroup = srcTaskOwner.getUserGroup();
                            //需要具有重做权限
                            if (!operator.canQuery(userGroup))
                            {
                                noPriTasks.add(srcTaskOwner);
                            }
                        }
                        rv.setExtValue(noPriTasks);
                        
                        Map<Long, TaskOwnerInfo> noPriTaskMaps = new HashMap<Long, TaskOwnerInfo>();
                        for (TaskOwnerInfo noPriTask : noPriTasks)
                        {
                            noPriTaskMaps.put(noPriTask.getTaskId(), noPriTask);
                        }
                        
                        filterRSs = new ArrayList<TaskRunningStateEntity>();
                        for (TaskRunningStateEntity taskRS : taskRsListRtn)
                        {
                            //过滤掉无权限的周期
                            if (noPriTaskMaps.containsKey(taskRS.getTaskId()))
                            {
                                continue;
                            }
                            
                            filterRSs.add(taskRS);
                        }
                    }
                    else
                    {
                        filterRSs = new ArrayList<TaskRunningStateEntity>(0);
                    }
                    
                    jsonObject.put("total", filterRSs.size());
                    int startIndex = (pageNumber - 1) * pageSize;
                    startIndex = startIndex >= filterRSs.size() ? filterRSs.size() : startIndex;
                    int endIndex = pageNumber * pageSize;
                    endIndex = endIndex >= filterRSs.size() ? filterRSs.size() : endIndex;
                    
                    jsonObject.put("rows", filterRSs.subList(startIndex, endIndex));
                    break;
                }
                
                default:
                    break;
            }
            
            returnStr =
                JSONObject.toJSONString(jsonObject,
                    SerializerFeature.UseISO8601DateFormat,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteMapNullValue);
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue(returnStr);
            
        }
        catch (Exception e)
        {
            LOGGER.error("get queryRSList2Json fail", e);
        }
        
        if (QueryRSType.DEPEND_CYCLE == queryRSType)
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        else
        {
            setInputStream(new ByteArrayInputStream(returnStr.getBytes("UTF-8")));
        }
        
        return SUCCESS;
    }
    
    /**
     * jobids
     * 
     * @return jobids
     * @throws Exception 数据库操作异常
     */
    public String jobIds2Json()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Long taskId = null;
        String cycleId = null;
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            cycleId = request.getParameter("cycleId");
            
            //检查cycleId是否合法
            if (!TccUtil.isCorrect(cycleId))
            {
                returnEmpty = true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("taskId or queryRSType must be an Integer!", e);
            returnEmpty = true;
        }
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        
        Operator operator = null;
        if (!returnEmpty)
        {
            operator = getOperator();
            returnEmpty = !isVisiblePri(operator, taskId);
        }
        
        String returnStr = "";
        try
        {
            List<StepRunningStateEntity> stepRsRtn;
            if (!returnEmpty)
            {
                stepRsRtn = getTccPortalService().getStepRSList(taskId, cycleId);
            }
            else
            {
                stepRsRtn = new ArrayList<StepRunningStateEntity>(0);
            }
            
            List<StepRunningStateEntity> stepsHaveJob = new ArrayList<StepRunningStateEntity>();
            for (StepRunningStateEntity step : stepRsRtn)
            {
                if (!StringUtils.isBlank(step.getRunningJobId()))
                {
                    stepsHaveJob.add(step);
                }
            }
            
            jsonObject.put("total", stepsHaveJob.size());
            jsonObject.put("rows", stepsHaveJob);
            
            returnStr =
                JSONObject.toJSONString(jsonObject,
                    SerializerFeature.UseISO8601DateFormat,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteMapNullValue);
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue(returnStr);
            
        }
        catch (Exception e)
        {
            LOGGER.error("get queryRSList2Json fail", e);
        }
        
        setInputStream(new ByteArrayInputStream(returnStr.getBytes("UTF-8")));
        
        return SUCCESS;
    }
    
    /**
     * 是否可见
     * @param taskId 任务Id
     * @return 是否可见权限
     * @throws Exception 异常
     */
    private boolean isVisiblePri(Operator operator, final Long taskId)
        throws Exception
    {
        Operator mOperator = operator;
        //权限处理
        if (null == mOperator)
        {
            mOperator = getOperator();
        }
        
        Boolean visible = false;
        if (null != mOperator)
        {
            //获取原任务的业务和任务组
            TaskEntity srcTask = getTccPortalService().getTaskInfo(taskId);
            if (null != srcTask)
            {
                String srcGroupName = srcTask.getUserGroup();
                //具有查询权限
                if (mOperator.canQuery(srcGroupName))
                {
                    visible = true;
                }
            }
        }
        return visible;
    }
    
    /**
     * job的详细信息展示页面地址
     * @return  job的详细信息展示页面地址
     * @throws Exception 异常
     */
    public String grabJobDetailUrl()
        throws Exception
    {
        String result = "true|" + TccConfig.getJobDetailUrl();
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 周期Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqCycleIdJsonObject()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Long taskId = null;
        boolean returnEmpty = false;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
        }
        catch (Exception e)
        {
            LOGGER.error("taskId must be an Integer!", e);
            returnEmpty = true;
        }
        
        cycleIdList = getTccService().getAllCycleIdList(taskId);
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        
        if (!returnEmpty)
        {
            KeyValuePair keyValuePair;
            for (String cycleId : cycleIdList)
            {
                keyValuePair = new KeyValuePair(cycleId, null);
                keyValuePairList.add(keyValuePair);
            }
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    public List<Long> getTaskIdList()
    {
        return taskIdList;
    }
    
    public void setTaskIdList(List<Long> taskIdList)
    {
        this.taskIdList = taskIdList;
    }
    
    public TaskRunningStateEntity getTaskRs()
    {
        return taskRs;
    }
    
    public void setTaskRs(TaskRunningStateEntity taskRs)
    {
        this.taskRs = taskRs;
    }
    
    public List<String> getCycleIdList()
    {
        return cycleIdList;
    }
    
    public void setCycleIdList(List<String> cycleIdList)
    {
        this.cycleIdList = cycleIdList;
    }
}
