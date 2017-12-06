/*
 * 文 件 名:  TccConfigAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-4-18
 */
package com.huawei.platform.tcc.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.JDBCExtAppender;
import com.huawei.platform.tcc.utils.TccSysConfigService;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * TCC配置
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-4-18]
 */
public class TccConfigAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 4864915535992793562L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccConfigAction.class);
    
    private transient TccSysConfigService tccSysConfigService;
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String saveTccConfig()
        throws Exception
    {
        //修改线程的名字
        Thread.currentThread().setName(String.format("saveTccConfig"));
        String returnValue = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Date benchDate = null;
        String executeCmdTemplate = TccConfig.DEFAULT_EXEC_CMD;
        String killJobCmdTemplate = TccConfig.DEFAULT_KILL_JOB_CMD;
        String lSCmdTemplate = TccConfig.DEFAULT_LS_CMD;
        String jobDetailUrl = TccConfig.DEFAULT_JOB_DETAIL_URL;
        String backupDir = TccConfig.DEFAULT_BACKUP_DIR;
        Integer reserveNLCCount = TccConfig.DEFAULT_RESERVE_NLC_COUNT;
        Integer maxSSHConnectionNum = TccConfig.DEFAULT_MAX_SSH_CONNECTION_NUM;
        Integer maxRTSSHConnectionNum = TccConfig.DEFAULT_MAX_RT_SSH_CONNECTION_NUM;
        Integer maxRunningCycleTaskNum = TccConfig.DEFAULT_MAX_RUNNING_CYCLETASK_NUM;
        Integer conRetryTimes = TccConfig.DEFAULT_CONNECT_RETRY_TIMES;
        Boolean isSendEmail = false;
        String portalUrl = TccConfig.DEFAULT_PORTAL_URL;
        String emailFrom = TccConfig.DEFAULT_EAMIL_FROM;
        boolean taskIdCenter = false;
        String url = "";
        String username = "";
        String password = "";
        String rsLogThreshold = "INFO";
        String rslog2dbThreshold = "INFO";
        String tccLogThreshold = "INFO";
        String tcclog2dbThreshold = "INFO";
        String consoleThreshold = "INFO";
        Boolean isFullName = false;
        Boolean isLogScheduleDuration = false;
        try
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            benchDate = df.parse(request.getParameter("benchDate"));
            maxRunningCycleTaskNum = Integer.parseInt(request.getParameter("maxRunningCycleTaskNum"));
            conRetryTimes = Integer.parseInt(request.getParameter("conRetryTimes"));
            maxSSHConnectionNum = Integer.parseInt(request.getParameter("maxSSHConnectionNum"));
            maxRTSSHConnectionNum = Integer.parseInt(request.getParameter("maxRTSSHConnectionNum"));
            reserveNLCCount = Integer.parseInt(request.getParameter("reserveNLCCount"));
            isFullName = Boolean.parseBoolean(request.getParameter("isFullName"));
            killJobCmdTemplate = truncatEncode(request.getParameter("killJobCmdTemplate"));
            executeCmdTemplate = truncatEncode(request.getParameter("executeCmdTemplate"));
            lSCmdTemplate = truncatEncode(request.getParameter("lSCmdTemplate"));
            jobDetailUrl = truncatEncode(request.getParameter("jobDetailUrl"));
            backupDir = truncatEncode(request.getParameter("backupDir"));
            isSendEmail = Boolean.parseBoolean(request.getParameter("isSendEmail"));
            portalUrl = truncatEncode(request.getParameter("portalUrl"));
            emailFrom = truncatEncode(request.getParameter("emailFrom"));
            isLogScheduleDuration = Boolean.parseBoolean(request.getParameter("isLogScheduleDuration"));
            taskIdCenter = Boolean.parseBoolean(request.getParameter("taskIdCenter"));
            //mysqldb配置
            url = request.getParameter("url");
            username = request.getParameter("username");
            password = request.getParameter("password");
            //log4j配置
            rsLogThreshold = request.getParameter("rsLogThreshold");
            rslog2dbThreshold = request.getParameter("rslog2dbThreshold");
            tccLogThreshold = request.getParameter("tccLogThreshold");
            tcclog2dbThreshold = request.getParameter("tcclog2dbThreshold");
            consoleThreshold = request.getParameter("consoleThreshold");
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("failed to save TccConfig.", e);
            returnValue = "false";
        }
        catch (ParseException e)
        {
            LOGGER.error("failed to save TccConfig.", e);
            returnValue = "false";
        }
        
        if (StringUtils.isNotBlank(password))
        {
            password = TccUtil.encrypt(password);
        }
        
        try
        {
            if ("true".endsWith(returnValue))
            {
                
                //更新内存数据配置
                TccConfig.setExecuteCmdTemplate(executeCmdTemplate);
                TccConfig.setKillJobCmdTemplate(killJobCmdTemplate);
                TccConfig.setMaxSSHConnectionNum(maxSSHConnectionNum);
                TccConfig.setMaxRTSSHConnectionNum(maxRTSSHConnectionNum);
                TccConfig.setReserverNLCCount(reserveNLCCount);
                TccConfig.setLSCmdTemplate(lSCmdTemplate);
                TccConfig.setMaxRunningCycleTaskNum(maxRunningCycleTaskNum);
                TccConfig.setConRetryTimes(conRetryTimes);
                TccConfig.setIsFullName(isFullName);
                TccConfig.setJobDetailUrl(jobDetailUrl);
                TccConfig.setBackupDir(backupDir);
                TccConfig.setIsSendEmail(isSendEmail);
                TccConfig.setPortalUrl(portalUrl);
                TccConfig.setEmailFrom(emailFrom);
                TccConfig.setIsLogScheduleDuration(isLogScheduleDuration);
                TccConfig.setTaskIdCenter(taskIdCenter);
                //更新配置文件
                //格式化日期
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                
                String benchDateStr = df.format(benchDate);
                tccSysConfigService.setProperty("tcc.benchDate", benchDateStr);
                tccSysConfigService.setProperty("tcc.maxRunngingNum",
                    Integer.toString(TccConfig.getMaxRunningCycleTaskNum()));
                tccSysConfigService.setProperty("tcc.conRetryTimes", Integer.toString(TccConfig.getConRetryTimes()));
                tccSysConfigService.setProperty("tcc.maxSSHConnectionNum",
                    Integer.toString(TccConfig.getMaxSSHConnectionNum()));
                tccSysConfigService.setProperty("tcc.maxRTSSHConnectionNum",
                    Integer.toString(TccConfig.getMaxRTSSHConnectionNum()));
                tccSysConfigService.setProperty("tcc.reserveNLCCount",
                    Integer.toString(TccConfig.getReserverNLCCount()));
                tccSysConfigService.setProperty("tcc.killJobCmcTemplate", TccConfig.getKillJobCmdTemplate());
                tccSysConfigService.setProperty("tcc.execCmdTemplate", TccConfig.getExecuteCmdTemplate());
                tccSysConfigService.setProperty("tcc.lsCmdTemplate", TccConfig.getLSCmdTemplate());
                tccSysConfigService.setProperty("tcc.isFullFileName", TccConfig.getIsFullName() ? "1" : "0");
                tccSysConfigService.setProperty("tcc.jobDetailUrl", TccConfig.getJobDetailUrl());
                tccSysConfigService.setProperty("tcc.backupDir", TccConfig.getBackupDir());
                
                tccSysConfigService.setProperty("tcc.isSendEmail", TccConfig.getIsSendEmail() ? "1" : "0");
                tccSysConfigService.setProperty("tcc.portalUrl", TccConfig.getPortalUrl());
                tccSysConfigService.setProperty("tcc.emailFrom", TccConfig.getEmailFrom());
                tccSysConfigService.setProperty("tcc.isLogScheduleDuration", TccConfig.getIsLogScheduleDuration() ? "1"
                    : "0");
                tccSysConfigService.setProperty("tcc.taskIdCenter", TccConfig.isTaskIdCenter() ? "1" : "0");
                
                //记录操作员修改的配置项
                logChanged(tccSysConfigService.getPropertys(), TccUtil.loadConfigItem(TccConfig.TCC_CONFIG_PATH));
                tccSysConfigService.save();
                //保存mysql配置
                Map<String, String> dbKeyValues = new HashMap<String, String>();
                dbKeyValues.put("jdbc.url", url);
                dbKeyValues.put("jdbc.username", username);
                if (StringUtils.isNotBlank(password))
                {
                    dbKeyValues.put("jdbc.password", password);
                }
                
                //记录操作员修改的配置项
                logChanged(dbKeyValues, TccUtil.loadConfigItem(TccConfig.TCC_DB_PROPERTIES_PATH));
                TccUtil.saveConfigItem(TccConfig.TCC_DB_PROPERTIES_PATH, dbKeyValues);
                //保存log4j配置
                Map<String, String> log4jKeyValues = new HashMap<String, String>();
                log4jKeyValues.put("log4j.appender.rsLog.Threshold", rsLogThreshold);
                log4jKeyValues.put("log4j.appender.rslog2db.Threshold", rslog2dbThreshold);
                log4jKeyValues.put("log4j.appender.rslog2db.URL", url);
                log4jKeyValues.put("log4j.appender.rslog2db.user", username);
                if (StringUtils.isNotBlank(password))
                {
                    log4jKeyValues.put("log4j.appender.rslog2db.password", password);
                }
                
                log4jKeyValues.put("log4j.appender.tccLog.Threshold", tccLogThreshold);
                log4jKeyValues.put("log4j.appender.tcclog2db.Threshold", tcclog2dbThreshold);
                log4jKeyValues.put("log4j.appender.tcclog2db.URL", url);
                log4jKeyValues.put("log4j.appender.tcclog2db.user", username);
                if (StringUtils.isNotBlank(password))
                {
                    log4jKeyValues.put("log4j.appender.tcclog2db.password", password);
                }
                
                log4jKeyValues.put("log4j.appender.Console.Threshold", consoleThreshold);
                //记录操作员修改的配置项
                logChanged(log4jKeyValues, TccUtil.loadConfigItem(TccConfig.TCC_LOG4J_PROPERTIES_PATH));
                TccUtil.saveConfigItem(TccConfig.TCC_LOG4J_PROPERTIES_PATH, log4jKeyValues);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TCCCONFIG_MODIFY);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("saveTccConfig failed!", e);
            returnValue = "false";
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    //记录修改日志
    private void logChanged(Map<String, String> dst, Properties src)
    {
        if (null == dst || null == src)
        {
            return;
        }
        
        String dstValue;
        String srcValue;
        String key;
        for (Entry<String, String> keyValue : dst.entrySet())
        {
            key = keyValue.getKey();
            dstValue = keyValue.getValue();
            if (src.containsKey(key))
            {
                srcValue = src.get(key).toString();
                //键值不等
                if (!dstValue.equals(srcValue))
                {
                    //密码项不记录具体密码
                    if (key.toLowerCase().contains("password"))
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ", new Object[] {TccUtil.truncatEncode(key),
                            "PASSWORD_A", TccUtil.truncatEncode(key), "PASSWORD_B"});
                    }
                    else
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ",
                            new Object[] {key, TccUtil.truncatEncode(src.get(key).toString()), key,
                                TccUtil.truncatEncode(dst.get(key))});
                    }
                }
            }
            else
            {
                //密码项不记录具体密码
                if (key.toLowerCase().contains("password"))
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, "PASSWORD_A"});
                }
                else
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, TccUtil.truncatEncode(dst.get(key))});
                }
            }
        }
    }
    
    //记录修改日志
    private void logChanged(Properties dst, Properties src)
    {
        if (null == dst || null == src)
        {
            return;
        }
        
        String dstValue;
        String srcValue;
        String key;
        for (Entry<Object, Object> keyValue : dst.entrySet())
        {
            key = keyValue.getKey().toString();
            dstValue = keyValue.getValue().toString();
            if (src.containsKey(key))
            {
                srcValue = src.get(key).toString();
                //键值不等
                if (!dstValue.equals(srcValue))
                {
                    //密码项不记录具体密码
                    if (key.toLowerCase().contains("password"))
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ", new Object[] {key, "PASSWORD_A", key,
                            "PASSWORD_B"});
                    }
                    else
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ",
                            new Object[] {key, TccUtil.truncatEncode(src.get(key)), key,
                                TccUtil.truncatEncode(dst.get(key))});
                    }
                }
            }
            else
            {
                //密码项不记录具体密码
                if (key.toLowerCase().contains("password"))
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, "PASSWORD_A"});
                }
                else
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, TccUtil.truncatEncode(dst.get(key))});
                }
            }
        }
    }
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public synchronized String rebootTcc()
        throws Exception
    {
        String returnValue = "true";
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("rebootTcc"));
            
            LOGGER.info("reboot TCC...");
            java.lang.Runtime runTime = Runtime.getRuntime();
            LOGGER.info("start to execute command[{}]...", TccConfig.getRebootCmd());
            
            Process process = runTime.exec(TccConfig.getRebootCmd());
            
            //如果执行的命令中包含"rebootTomcate.sh"，那么就不等待命令处理结束
            if (!TccConfig.getRebootCmd().contains(TccConfig.DEFAULT_REBOOT_SHELL_NAME))
            {
                try
                {
                    Thread thdError = new Thread(new ConsoleOutput(process.getErrorStream(), "ErrorStream: "));
                    Thread thdInput = new Thread(new ConsoleOutput(process.getInputStream(), "InputStream: "));
                    thdError.start();
                    thdInput.start();
                    process.waitFor();
                    LOGGER.info("command[{}] execute finished! exitValue is {}",
                        TccConfig.getRebootCmd(),
                        process.exitValue());
                }
                catch (Exception e)
                {
                    LOGGER.error("failed to execute command[{}]!", TccConfig.getRebootCmd(), e);
                }
                finally
                {
                    process.destroy();
                }
            }
            
            LOGGER.info("command[{}] execute finished!", TccConfig.getRebootCmd());
            JDBCExtAppender.fresh2Db();
            //记录审计信息
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.REBOOT_TCC);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            getOperationRecord().writeOperLog(operateAuditInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("failed to reboot TCC!", e);
            returnValue = "false";
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     */
    public String loadTccConfig()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //格式化日期
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        String benchDateStr = df.format(TccConfig.getBenchDate());
        request.setAttribute("benchDate", benchDateStr);
        request.setAttribute("maxRunningCycleTaskNum", TccConfig.getMaxRunningCycleTaskNum());
        request.setAttribute("maxSSHConnectionNum", TccConfig.getMaxSSHConnectionNum());
        request.setAttribute("maxRTSSHConnectionNum", TccConfig.getMaxRTSSHConnectionNum());
        request.setAttribute("conRetryTimes", TccConfig.getConRetryTimes());
        request.setAttribute("reserveNLCCount", TccConfig.getReserverNLCCount());
        request.setAttribute("killJobCmdTemplate", TccConfig.getKillJobCmdTemplate());
        request.setAttribute("executeCmdTemplate", TccConfig.getExecuteCmdTemplate());
        request.setAttribute("lSCmdTemplate", TccConfig.getLSCmdTemplate());
        request.setAttribute("isFullName", TccConfig.getIsFullName());
        request.setAttribute("jobDetailUrl", TccConfig.getJobDetailUrl());
        request.setAttribute("backupDir", TccConfig.getBackupDir());
        request.setAttribute("isSendEmail", TccConfig.getIsSendEmail());
        request.setAttribute("portalUrl", TccConfig.getPortalUrl());
        request.setAttribute("emailFrom", TccConfig.getEmailFrom());
        request.setAttribute("isLogScheduleDuration", TccConfig.getIsLogScheduleDuration());
        request.setAttribute("taskIdCenter", TccConfig.isTaskIdCenter());
        //mysqldb配置
        try
        {
            Properties pros = TccUtil.loadConfigItem(TccConfig.TCC_DB_PROPERTIES_PATH);
            request.setAttribute("url", pros.get("jdbc.url"));
            request.setAttribute("username", pros.get("jdbc.username"));
            request.setAttribute("password", "");
        }
        catch (Exception e)
        {
            LOGGER.error("load tccDb config failed!", e);
        }
        
        //log4j配置
        try
        {
            Properties pros = TccUtil.loadConfigItem(TccConfig.TCC_LOG4J_PROPERTIES_PATH);
            request.setAttribute("rsLogThreshold", pros.get("log4j.appender.rsLog.Threshold"));
            request.setAttribute("rslog2dbThreshold", pros.get("log4j.appender.rslog2db.Threshold"));
            request.setAttribute("tccLogThreshold", pros.get("log4j.appender.tccLog.Threshold"));
            request.setAttribute("tcclog2dbThreshold", pros.get("log4j.appender.tcclog2db.Threshold"));
            request.setAttribute("consoleThreshold", pros.get("log4j.appender.Console.Threshold"));
        }
        catch (Exception e)
        {
            LOGGER.error("load log4j Config failed!", e);
        }
        
        return SUCCESS;
    }
    
    public TccSysConfigService getTccSysConfigService()
    {
        return tccSysConfigService;
    }
    
    public void setTccSysConfigService(TccSysConfigService tccSysConfigService)
    {
        this.tccSysConfigService = tccSysConfigService;
    }
    
    /**
     * 将命令控制台输出日志中
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-4-28]
     */
    public static class ConsoleOutput implements Runnable
    {
        private InputStream inputStream;
        
        private String prefix;
        
        /**
         * 构造函数
         * @param inputStream 输入流
         * @param prefix 前缀
         */
        public ConsoleOutput(InputStream inputStream, String prefix)
        {
            this.inputStream = inputStream;
            this.prefix = prefix;
        }
        
        @Override
        public void run()
        {
            if (null != inputStream)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                try
                {
                    while (true)
                    {
                        line = br.readLine();
                        if (line == null)
                        {
                            break;
                        }
                        
                        LOGGER.info(prefix + line + "\n");
                    }
                }
                catch (IOException e)
                {
                    LOGGER.error("read outStream or errStream error!", e);
                }
                finally
                {
                    //关闭流
                    try
                    {
                        if (null != br)
                        {
                            br.close();
                        }
                    }
                    catch (IOException ioE)
                    {
                        LOGGER.error("close io error", ioE);
                    }
                }
            }
        }
    }
}
