/* 文 件 名:  TaskRunningStateTimeFormat.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-20
 */
package com.huawei.platform.tcc.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.huawei.platform.tcc.constants.TccConfig;

/**
 * 任务运行状态（包含格式化好的日期字符串）
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-20]
 * @see  [相关类/方法]
 */
public class TaskRunningStateTimeFormat extends TaskRunningStateEntity
{
    private static final String FORMATSTRING = "yyyy-MM-dd HH:mm:ss";
    
    private String runningStartTimeFormat;
    
    private String runningEndTimeFormat;
    
    private String execTimeLength;
    
    /**
     * 构造函数
     * @param taskRs 任务运行状态
     */
    public TaskRunningStateTimeFormat(TaskRunningStateEntity taskRs)
    {
        if (null == taskRs.getBeginDependTaskList())
        {
            this.setBeginDependTaskList("");
        }
        else
        {
            this.setBeginDependTaskList(taskRs.getBeginDependTaskList());
        }
        
        this.setCycleId(taskRs.getCycleId());
        
        if (null == taskRs.getFinishDependTaskList())
        {
            this.setFinishDependTaskList("");
        }
        else
        {
            this.setFinishDependTaskList(taskRs.getFinishDependTaskList());
        }
        
        this.setRunningEndTime(taskRs.getRunningEndTime());
        this.setRunningStartTime(taskRs.getRunningStartTime());
        this.setState(taskRs.getState());
        this.setTaskId(taskRs.getTaskId());
        
        DateFormat df = new SimpleDateFormat(FORMATSTRING);
        df.setLenient(false);
        if (null != this.getRunningStartTime())
        {
            this.runningStartTimeFormat = df.format(this.getRunningStartTime());
        }
        else
        {
            this.runningStartTimeFormat = "";
        }
        
        if (null != this.getRunningEndTime())
        {
            runningEndTimeFormat = df.format(this.getRunningEndTime());
        }
        else
        {
            this.runningEndTimeFormat = "";
        }
        
        if (null != this.getRunningStartTime()
                && null != this.getRunningEndTime())
        {
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(this.getRunningStartTime());
            
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(this.getRunningEndTime());
            
            long diff = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
            long hours = diff / TccConfig.MILLS_PER_HOUR;
            diff = diff % TccConfig.MILLS_PER_HOUR;
            long minutes = diff / TccConfig.MILLS_PER_MINUTE;
            diff = diff % TccConfig.MILLS_PER_MINUTE;
            long seconds = diff / TccConfig.MILLS_PER_SECOND;
            execTimeLength = String.format("%d:%d:%d", hours, minutes, seconds);
        }
        else
        {
            execTimeLength = "";
        }
    }
    
    /**
     * 获取开始时间格式化的字符串
     * @return 获取开始时间格式化的字符串
     */
    public String getRunningStartTimeFormat()
    {
        
        return runningStartTimeFormat;
    }
    
    /**
     * 设置开始时间格式化的字符串
     * @param runningStartTimeFormat 格式化的字符串
     */
    public void setRunningStartTimeFormat(String runningStartTimeFormat)
    {
        this.runningStartTimeFormat = runningStartTimeFormat;
        DateFormat df = new SimpleDateFormat(FORMATSTRING);
        df.setLenient(false);
        Date time = null;
        try
        {
            time = df.parse(this.runningStartTimeFormat);
        }
        catch (ParseException e)
        {
            this.setRunningStartTime(null);
        }
        
        this.setRunningStartTime(time);
    }
    
    /**
     * 获取结束时间格式化的字符串
     * @return 获取结束时间格式化的字符串
     */
    public String getRunningEndTimeFormat()
    {
        return runningEndTimeFormat;
    }
    
    /**
     * 设置结束时间格式化的字符串
     * @param runningEndTimeFormat 格式化的字符串
     */
    public void setRunningEndTimeFormat(String runningEndTimeFormat)
    {
        this.runningEndTimeFormat = runningEndTimeFormat;
        DateFormat df = new SimpleDateFormat(FORMATSTRING);
        df.setLenient(false);
        Date time = null;
        try
        {
            time = df.parse(this.runningEndTimeFormat);
        }
        catch (ParseException e)
        {
            this.setRunningEndTime(null);
        }
        
        this.setRunningEndTime(time);
    }
    
    public String getExecTimeLength()
    {
        return execTimeLength;
    }
    
    public void setExecTimeLength(String execTimeLength)
    {
        this.execTimeLength = execTimeLength;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((execTimeLength == null) ? 0 : execTimeLength.hashCode());
        result = prime
                * result
                + ((runningEndTimeFormat == null) ? 0
                        : runningEndTimeFormat.hashCode());
        result = prime
                * result
                + ((runningStartTimeFormat == null) ? 0
                        : runningStartTimeFormat.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        TaskRunningStateTimeFormat other = (TaskRunningStateTimeFormat) obj;
        if (execTimeLength == null)
        {
            if (other.execTimeLength != null)
            {
                return false;
            }
        }
        else if (!execTimeLength.equals(other.execTimeLength))
        {
            return false;
        }
        if (runningEndTimeFormat == null)
        {
            if (other.runningEndTimeFormat != null)
            {
                return false;
            }
        }
        else if (!runningEndTimeFormat.equals(other.runningEndTimeFormat))
        {
            return false;
        }
        if (runningStartTimeFormat == null)
        {
            if (other.runningStartTimeFormat != null)
            {
                return false;
            }
        }
        else if (!runningStartTimeFormat.equals(other.runningStartTimeFormat))
        {
            return false;
        }
        return true;
    }
    
}