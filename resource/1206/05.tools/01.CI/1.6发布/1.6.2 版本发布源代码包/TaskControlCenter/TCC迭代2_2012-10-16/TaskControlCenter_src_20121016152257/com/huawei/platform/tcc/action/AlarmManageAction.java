/*
 * 文 件 名:  AlarmManageAciont.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-07-04
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.constants.type.GradeType;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.domain.AlarmConfig;
import com.huawei.platform.tcc.domain.AlarmFactSearch;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.ServiceTaskGroup;
import com.huawei.platform.tcc.entity.AlarmFactInfoEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmChannelInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmItemsEntity;
import com.huawei.platform.tcc.entity.TaskAlarmThresholdEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.exception.NotExistException;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 审计
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-07-04]
 */
public class AlarmManageAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 1643967054284306885L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmManageAction.class);
    
    /**
     * 由查询条件获取告警列表
     * @return 查询成功标志位
     * @throws Exception 异常
     */
    public String reqAlarmFacts()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            request.setCharacterEncoding("UTF-8");
            Integer count = 0;
            String serviceId = request.getParameter("serviceId");
            String alarmGrade = request.getParameter("alarmGrade");
            String alarmState = request.getParameter("status");
            String alarmType = request.getParameter("alarmType");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //以名字模糊查询
            AlarmFactSearch search = new AlarmFactSearch();
            search.setStatus(StringUtils.isEmpty(alarmState) ? null : Integer.valueOf(alarmState));
            search.setAlarmGrade(StringUtils.isEmpty(alarmGrade) ? null : Integer.valueOf(alarmGrade));
            search.setServiceId(StringUtils.isEmpty(serviceId) ? null : serviceId);
            search.setAlarmType(StringUtils.isEmpty(alarmType) ? null : Integer.valueOf(alarmType));
            search.setStartTime(StringUtils.isEmpty(startTime) ? null : df.parse(startTime));
            search.setEndTime(StringUtils.isEmpty(endTime) ? null : df.parse(endTime));
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            //数据过滤
            Operator operator = getOperator();
            List<ServiceTaskGroup> visibleSTGs;
            
            if (null != operator)
            {
                visibleSTGs = operator.getVisibleServiceTaskGroup();
                List<String> vSTgs = new ArrayList<String>();
                if (null != visibleSTGs)
                {
                    for (ServiceTaskGroup sTG : visibleSTGs)
                    {
                        vSTgs.add(String.format("%d,%s", sTG.getServiceId(), sTG.getTaskGroup()));
                    }
                    search.setVisibleSTgs(vSTgs);
                }
                else
                {
                    search.setVisibleSTgs(null);
                }
            }
            else
            {
                search.setVisibleSTgs(new ArrayList<String>(0));
            }
            
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            //返回空数据
            if (null != search.getVisibleSTgs() && search.getVisibleSTgs().isEmpty())
            {
                jsonObject.put("total", 0);
                jsonObject.put("rows", new ArrayList<TaskEntity>(0));
            }
            else
            {
                List<AlarmFactInfoEntity> alarmFacts = getTccPortalService().getAlarmFacts(search);
                count = getTccPortalService().getAlarmFactCount(search);
                
                jsonObject.put("total", count);
                jsonObject.put("rows", alarmFacts);
            }
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        }
        catch (IOException ex)
        {
            LOGGER.error("reqAlarmFacts fail", ex);
            throw ex;
        }
        
        return SUCCESS;
    }
    
    /**
     * 保存告警处理信息
     * @return 操作成功标志符
     */
    public String saveAlarmHandle()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        AlarmFactInfoEntity alarmFact = new AlarmFactInfoEntity();
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            HttpSession session = request.getSession();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alarmFact.setTaskId(StringUtils.isEmpty(request.getParameter("taskId")) ? null
                : Long.valueOf(request.getParameter("taskId")));
            alarmFact.setCycleId(StringUtils.isEmpty(request.getParameter("cycleId")) ? null
                : request.getParameter("cycleId"));
            alarmFact.setAlarmTime(StringUtils.isEmpty(request.getParameter("alarmTime")) ? null
                : df.parse(request.getParameter("alarmTime")));
            alarmFact.setAlarmType(StringUtils.isEmpty(request.getParameter("alarmType")) ? null
                : Integer.valueOf(request.getParameter("alarmType")));
            alarmFact.setReason(StringUtils.isEmpty(request.getParameter("reason")) ? null
                : request.getParameter("reason"));
            alarmFact.setSolution(StringUtils.isEmpty(request.getParameter("solution")) ? null
                : request.getParameter("solution"));
            alarmFact.setStatus(StringUtils.isEmpty(request.getParameter("status")) ? null
                : Integer.valueOf(request.getParameter("status")));
            alarmFact.setOperatorName((String)session.getAttribute(NameStoredInSession.USER_NAME));
            alarmFact.setProcessTime(new Date());
            
            //越权判断
            TaskEntity taskE = getTccPortalService().getTaskInfo(alarmFact.getTaskId());
            if (null != taskE)
            {
                checkModify(taskE);
            }
            else
            {
                throw new NotExistException(String.format("%d does not exist!", alarmFact.getTaskId()));
            }
            
            //更新记录
            getTccPortalService().updateAlarmFact(alarmFact);
            //记录日志
            LOGGER.info("saveAlarmHandle[{}] successfully!", new Object[] {alarmFact});
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
            
            //记录审计信息
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.ALARM_HANDLE);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            operateAuditInfo.setServiceIdSingle(null == taskE.getServiceid() ? null
                : String.valueOf(taskE.getServiceid()));
            operateAuditInfo.setTaskIdSingle(null == taskE.getTaskId() ? null : String.valueOf(taskE.getTaskId()));
            operateAuditInfo.setOperParameters(alarmFact.toString());
            getOperationRecord().writeOperLog(operateAuditInfo);
        }
        catch (PrivilegeNotEnoughException e)
        {
            LOGGER.error("failed to saveAlarmHandle[{}].", new Object[] {alarmFact, e});
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (NotExistException e)
        {
            LOGGER.error("failed to saveAlarmHandle[{}].", new Object[] {alarmFact, e});
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("failed to saveAlarmHandle[{}].", new Object[] {alarmFact, e});
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
            LOGGER.error("save the taskInfo fail", e1);
        }
        
        return SUCCESS;
    }
    
    /**
     * 保存告警配置信息
     * @return 操作成功标志符
     */
    public String saveAlarmConfig()
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        AlarmConfig alarmConfig = new AlarmConfig();
        try
        {
            if (!StringUtils.isEmpty(request.getParameter("taskId")))
            {
                alarmConfig.setTaskId(Long.parseLong(request.getParameter("taskId")));
            }
            
            //是否开启告警
            if (!StringUtils.isEmpty(request.getParameter("isAlarmPermitted")))
            {
                alarmConfig.setIsAlarmPermitted(Boolean.parseBoolean(request.getParameter("isAlarmPermitted")));
            }
            
            if (!StringUtils.isEmpty(request.getParameter("alarmType")))
            {
                alarmConfig.setAlarmType(request.getParameter("alarmType"));
            }
            
            alarmConfig.setLatestStartTime(request.getParameter("latestStartTime"));
            alarmConfig.setLatestEndTime(request.getParameter("latestEndTime"));
            
            if (!StringUtils.isEmpty(request.getParameter("maxRunTime")))
            {
                alarmConfig.setMaxRunTime(Integer.parseInt(request.getParameter("maxRunTime")));
            }
            
            alarmConfig.setNormalEmailList(request.getParameter("normalEmailList"));
            alarmConfig.setNormalMobileList(request.getParameter("normalMobileList"));
            alarmConfig.setSevereEmailList(request.getParameter("severeEmailList"));
            alarmConfig.setSevereMobileList(request.getParameter("severeMobileList"));
            
            //任务Id不能为null
            if (null != alarmConfig.getTaskId())
            {
                //修改线程的名字
                Thread.currentThread().setName(String.format("saveAlarmConfig_%d", alarmConfig.getTaskId()));
                MDC.put(TccUtil.TASK_ID, alarmConfig.getTaskId());
                
                //越权判断
                TaskEntity taskE = getTccPortalService().getTaskInfo(alarmConfig.getTaskId());
                if (null != taskE)
                {
                    checkModify(taskE);
                }
                else
                {
                    throw new NotExistException(String.format("%d does not exist!", alarmConfig.getTaskId()));
                }
                
                //更新告警项
                if (null != alarmConfig.getIsAlarmPermitted() || null != alarmConfig.getAlarmType())
                {
                    TaskAlarmItemsEntity taskAlarmItems = new TaskAlarmItemsEntity();
                    taskAlarmItems.setTaskId(alarmConfig.getTaskId());
                    taskAlarmItems.setIsAlarmPermitted(alarmConfig.getIsAlarmPermitted());
                    taskAlarmItems.setAlarmType(alarmConfig.getAlarmType());
                    
                    if (null == getTccPortalService().getTaskAlarmItems(alarmConfig.getTaskId()))
                    {
                        getTccPortalService().addAlarmItems(taskAlarmItems);
                        LOGGER.info("add AlarmItems. taskAlarmItems is {}", taskAlarmItems);
                    }
                    else
                    {
                        getTccPortalService().updateAlarmItems(taskAlarmItems);
                        LOGGER.info("update AlarmItems. taskAlarmItems is {}", taskAlarmItems);
                    }
                }
                
                //更新告警阈值
                if (null != alarmConfig.getLatestStartTime() || null != alarmConfig.getLatestEndTime()
                    || null != alarmConfig.getMaxRunTime())
                {
                    TaskAlarmThresholdEntity alarmThreshold = new TaskAlarmThresholdEntity();
                    alarmThreshold.setTaskId(alarmConfig.getTaskId());
                    alarmThreshold.setLatestStartTime(alarmConfig.getLatestStartTime());
                    alarmThreshold.setLatestEndTime(alarmConfig.getLatestEndTime());
                    alarmThreshold.setMaxRunTime(alarmConfig.getMaxRunTime());
                    
                    if (null == getTccPortalService().getAlarmThreshold(alarmConfig.getTaskId()))
                    {
                        getTccPortalService().addAlarmThreshold(alarmThreshold);
                        LOGGER.info("add AlarmThreshold. alarmThreshold is {}", alarmThreshold);
                    }
                    else
                    {
                        getTccPortalService().updateAlarmThreshold(alarmThreshold);
                        LOGGER.info("update AlarmThreshold. alarmThreshold is {}", alarmThreshold);
                    }
                    
                }
                
                //更新一般等级告警渠道
                if (null != alarmConfig.getNormalEmailList() || null != alarmConfig.getNormalMobileList())
                {
                    TaskAlarmChannelInfoEntity alarmChannel = new TaskAlarmChannelInfoEntity();
                    alarmChannel.setTaskId(alarmConfig.getTaskId());
                    alarmChannel.setAlarmGrade(GradeType.NORMAL);
                    alarmChannel.setEmailList(alarmConfig.getNormalEmailList());
                    alarmChannel.setMobileList(alarmConfig.getNormalMobileList());
                    
                    if (null == getTccPortalService().getAlarmChannel(alarmConfig.getTaskId(), GradeType.NORMAL))
                    {
                        getTccPortalService().addAlarmChannel(alarmChannel);
                        LOGGER.info("add AlarmThreshold. alarmThreshold is {}", alarmChannel);
                    }
                    else
                    {
                        getTccPortalService().updateAlarmChannel(alarmChannel);
                        LOGGER.info("update AlarmChannel. alarmChannel is {}", alarmChannel);
                    }
                }
                
                //更新严重等级告警渠道
                if (null != alarmConfig.getSevereEmailList() || null != alarmConfig.getSevereMobileList())
                {
                    TaskAlarmChannelInfoEntity alarmChannel = new TaskAlarmChannelInfoEntity();
                    alarmChannel.setTaskId(alarmConfig.getTaskId());
                    alarmChannel.setAlarmGrade(GradeType.SEVERE);
                    alarmChannel.setEmailList(alarmConfig.getSevereEmailList());
                    alarmChannel.setMobileList(alarmConfig.getSevereMobileList());
                    
                    if (null == getTccPortalService().getAlarmChannel(alarmConfig.getTaskId(), GradeType.SEVERE))
                    {
                        getTccPortalService().addAlarmChannel(alarmChannel);
                        LOGGER.info("add AlarmThreshold. alarmThreshold is {}", alarmChannel);
                    }
                    else
                    {
                        getTccPortalService().updateAlarmChannel(alarmChannel);
                        LOGGER.info("update AlarmChannel. alarmChannel is {}", alarmChannel);
                    }
                }
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.ALARM_CONFIGURE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setServiceIdSingle(null == taskE.getServiceid() ? null
                    : String.valueOf(taskE.getServiceid()));
                operateAuditInfo.setTaskIdSingle(null == taskE.getTaskId() ? null : String.valueOf(taskE.getTaskId()));
                operateAuditInfo.setOperParameters(alarmConfig.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
        }
        catch (PrivilegeNotEnoughException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (NotExistException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("saveAlarmConfig failed! alarmConfig is {}", alarmConfig, e);
        }
        finally
        {
            MDC.remove(TccUtil.TASK_ID);
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
            LOGGER.error("save the taskInfo fail", e1);
        }
        
        return SUCCESS;
    }
    
    /**
     * 获取告警配置
     * 
     * @return 是否成功标准
     * @throws IOException 异常
     */
    public String reqAlarmConfig()
        throws IOException
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setCharacterEncoding("UTF-8");
        Long taskId = null;
        try
        {
            taskId = Long.parseLong(request.getParameter("taskId"));
            //越权判断
            TaskEntity taskE = getTccPortalService().getTaskInfo(taskId);
            if (null != taskE)
            {
                checkQuery(taskE);
            }
            else
            {
                throw new NotExistException(String.format("%d does not exist!", taskId));
            }
            
            AlarmConfig alarmConfig = new AlarmConfig();
            //获取告警项
            TaskAlarmItemsEntity taskAlarmItem = getTccPortalService().getTaskAlarmItems(taskId);
            if (null != taskAlarmItem)
            {
                alarmConfig.setAlarmType(taskAlarmItem.getAlarmType());
                alarmConfig.setIsAlarmPermitted(taskAlarmItem.getIsAlarmPermitted());
            }
            
            //获取告警阈值
            TaskAlarmThresholdEntity taskThreshold = getTccPortalService().getAlarmThreshold(taskId);
            if (null != taskThreshold)
            {
                alarmConfig.setLatestStartTime(taskThreshold.getLatestStartTime());
                alarmConfig.setLatestEndTime(taskThreshold.getLatestEndTime());
                alarmConfig.setMaxRunTime(taskThreshold.getMaxRunTime());
            }
            
            //获取告警阈值
            List<TaskAlarmChannelInfoEntity> alarmChannelList = getTccPortalService().getAlarmChannelList(taskId);
            for (TaskAlarmChannelInfoEntity alarm : alarmChannelList)
            {
                if (alarm.getAlarmGrade().equals(GradeType.NORMAL))
                {
                    alarmConfig.setNormalEmailList(alarm.getEmailList());
                    alarmConfig.setNormalMobileList(alarm.getMobileList());
                }
                else
                {
                    alarmConfig.setSevereEmailList(alarm.getEmailList());
                    alarmConfig.setSevereMobileList(alarm.getMobileList());
                }
            }
            
            rv.setSuccess(true);
            rv.setReturnValue2PageType(ReturnValue2PageType.VALUE);
            rv.addReturnValue(replace2Quotes(JSONObject.toJSONString(alarmConfig,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
        }
        catch (PrivilegeNotEnoughException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NO_ENOUGT_PRIVILEGE);
            rv.addReturnValue(e.toString());
        }
        catch (NotExistException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.NOT_EXIST);
            rv.addReturnValue(e.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("load alarmConfig failed!", e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        return SUCCESS;
    }
}
