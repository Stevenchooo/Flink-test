/*
 * 文 件 名:  Alarming.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-7-4
 */
package com.huawei.platform.tcc.alarm;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.AlarmStatusType;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.entity.AlarmFactInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskAlarmChannelInfoEntity;
import com.huawei.platform.tcc.entity.TaskAlarmItemsEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.server.IEmailServer;
import com.huawei.platform.tcc.server.PushServer;

/**
 * 告警
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-7-4]
 */
public class Alarming implements ISendAlarming
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Alarming.class);
    
    private PushServer pushServer;
    
    private IEmailServer emailServer;
    
    private TccDao tccDao;
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    @Override
    public void sendAlarm(long taskId, String cycleId, int instanceID, AlarmInfo alarmInfo)
    {
        //参数为空，失败
        if (StringUtils.isEmpty(cycleId) || null == alarmInfo)
        {
            LOGGER.error("cycleId or alarmInfo can't be null!");
            return;
        }
        try
        {
            TaskEntity task = tccDao.getTask(taskId);
            if (null == task)
            {
                LOGGER.error("task[taskid={}] don't exist!", taskId);
                return;
            }
            
            //获取任务告警项
            TaskAlarmItemsEntity taskAlarmItems = tccDao.getTaskAlarmItems(taskId);
            //如果启用了相应告警
            if (null != taskAlarmItems && taskAlarmItems.isEnableAlarm(alarmInfo.getAlarmType()))
            {
                //查询告警等级渠道
                TaskAlarmChannelInfoEntity toAlarmchannel = getAlarmChannelInfo(taskId, alarmInfo.getGrade());
                
                StringBuilder mobileList = new StringBuilder();
                StringBuilder emailList = new StringBuilder();
                //如果配置了任务告警渠道
                if (null != toAlarmchannel)
                {
                    if (!StringUtils.isBlank(toAlarmchannel.getMobileList()))
                    {
                        mobileList.append(toAlarmchannel.getMobileList());
                    }
                    if (!StringUtils.isBlank(toAlarmchannel.getEmailList()))
                    {
                        emailList.append(toAlarmchannel.getEmailList());
                    }
                }
                
                //向业务负责人发送告警信息
                ServiceDefinationEntity service = tccDao.getService(task.getServiceid());
                if (null != service)
                {
                    if (!StringUtils.isBlank(service.getAlarmMobileList()))
                    {
                        if (0 != mobileList.length())
                        {
                            mobileList.append(";");
                        }
                        
                        mobileList.append(service.getAlarmMobileList());
                    }
                    if (!StringUtils.isBlank(service.getAlarmEmailList()))
                    {
                        if (0 != emailList.length())
                        {
                            emailList.append(";");
                        }
                       
                        emailList.append(service.getAlarmEmailList());
                    }
                }
                
                //发送告警
                if (!StringUtils.isEmpty(mobileList.toString()) || !StringUtils.isEmpty(emailList.toString()))
                {
                    sendAlarm(mobileList.toString(), emailList.toString(), alarmInfo);
                }
                
                //产生告警事实实体
                AlarmFactInfoEntity alarmFact = new AlarmFactInfoEntity();
                alarmFact.setAlarmTime(new Date());
                alarmFact.setTaskId(taskId);
                alarmFact.setCycleId(cycleId);
                alarmFact.setInstanceId(instanceID);
                alarmFact.setAlarmType(alarmInfo.getAlarmType());
                alarmFact.setAlarmGrade(alarmInfo.getGrade());
                alarmFact.setServiceId(task.getServiceid());
                alarmFact.setStatus(AlarmStatusType.NOHANDLE);
                alarmFact.setEmailList(emailList.toString());
                alarmFact.setMobileList(mobileList.toString());
                //保持告警事实
                saveAlarmInfo(alarmFact);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sendAlarm failed! taskId={}, cycleId={}, instanceID={}, alarmInfo={}", new Object[] {taskId,
                cycleId, instanceID, alarmInfo, e});
        }
    }
    
    private void sendAlarm(String toMoblies, String toEmails, AlarmInfo alarmInfo)
    {
        //发送短信告警
        if (null != pushServer && !StringUtils.isBlank(toMoblies))
        {
            String[] phoneNums = null;
            try
            {
                phoneNums = toMoblies.split(";");
                pushServer.sendSms(phoneNums, alarmInfo.getSmsMsg());
            }
            catch (Exception e)
            {
                LOGGER.error("pushSever.sendSms failed! phoneNums is {},msg is {}",
                    new Object[] {phoneNums, alarmInfo.getSmsMsg(), e});
            }
        }
        
        //发送邮件告警
        if (null != emailServer && !StringUtils.isBlank(toEmails))
        {
            String[] emails = null;
            try
            {
                emails = toEmails.split(";");
                emailServer.sendEmail(TccConfig.getEmailFrom(),
                    emails,
                    alarmInfo.getEmailSubject(),
                    alarmInfo.getEmailMsg());
            }
            catch (Exception e)
            {
                LOGGER.error("emailSever.sendEmail failed! from is {},toEmails is {},subject is {},emailMsg is {}",
                    new Object[] {TccConfig.getEmailFrom(), emails, alarmInfo.getEmailSubject(),
                        alarmInfo.getEmailMsg(), e});
            }
        }
    }
    
    /**
     * 获取渠道告警信息
     * @param taskId 任务Id
     * @param grade 等级
     * @return 渠道告警信息
     * @throws Exception 异常
     */
    public TaskAlarmChannelInfoEntity getAlarmChannelInfo(long taskId, Integer grade)
        throws Exception
    {
        //查询告警等级渠道
        TaskAlarmChannelInfoEntity taskAlarmChannel = new TaskAlarmChannelInfoEntity();
        taskAlarmChannel.setTaskId(taskId);
        taskAlarmChannel.setAlarmGrade(grade);
        return tccDao.getTaskAlarmChannelInfo(taskAlarmChannel);
    }
    
    /**
     * 保存告警信息到历史告警记录中
     * @param alarmFactInfo 告警事实记录
     */
    public void saveAlarmInfo(AlarmFactInfoEntity alarmFactInfo)
    {
        try
        {
            if (null != alarmFactInfo)
            {
                tccDao.addAlarmFactInfo(alarmFactInfo);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("saveAlarmInfo fail! alarmFactInfo is {}", alarmFactInfo, e);
        }
    }
    
    /**
     * 通过PushServer发送短信
     * @return 是否成功
     */
    public boolean sendSMS()
    {
        return false;
    }
    
    /**
     * 发送邮件
     * @param taskId 任务Id
     * @param taskName 任务名
     * @param cycleId 周期Id
     * @param state 状态
     */
    public void sendEmail(Long taskId, String taskName, String cycleId, Integer state)
    {
        
    }
    
    public PushServer getPushServer()
    {
        return pushServer;
    }
    
    public void setPushServer(PushServer pushServer)
    {
        this.pushServer = pushServer;
    }
    
    public IEmailServer getEmailServer()
    {
        return emailServer;
    }
    
    public void setEmailServer(IEmailServer emailServer)
    {
        this.emailServer = emailServer;
    }
}
